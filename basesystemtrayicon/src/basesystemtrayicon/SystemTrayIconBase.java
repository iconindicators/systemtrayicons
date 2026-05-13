package basesystemtrayicon;


import java.awt.AWTException;
import java.awt.Frame;
import java.awt.SystemTray;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.jar.JarFile;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import basesystemtrayicon.gui.DialogMessage;
import basesystemtrayicon.gui.PopupMenuBase;
import basesystemtrayicon.gui.TrayIconBase;


/**
 * Abstract base class for a system tray icon, fully supported ONLY on
 * Microsoft Windows.
 *
 * To run from the command line and bypass the system tray icon (for the
 * purpose of testing or to run on Linux), specifying the command line
 * parameter
 *
 *      -DrunWithoutSystemTrayIcon=true
 *
 * will create an empty frame in lieu of the system tray icon and on a mouse
 * right-click, the popup menu will be launched.
 *
 * If the above command line parameter is absent, the system tray icon will run
 * as normal (only on Microsoft Windows).
 *
 * To print debug information, add
 *
 *      -Ddebug=true
 *
 * to the command line.
 *
 * Refer to the header comment of each system tray icon's implementing concrete
 * class for the incantation.
 */
public abstract class SystemTrayIconBase
{
    private static final String MANIFEST_KEY_PROJECT_AUTHOR = "Project-Author";
    private static final String MANIFEST_KEY_PROJECT_URL = "Project-URL";


    public SystemTrayIconBase(
    	final Class<? extends PopupMenuBase> popupMenuClass,
    	final Class<? extends TrayIconBase> trayIconClass)
    {
    	if( Boolean.parseBoolean( System.getProperty( "debug" ) ) )
            debug();

        String applicationNameHumanReadable =
            getClass().getPackage().getImplementationTitle();

        String executableName =
            applicationNameHumanReadable.replace( " ", "" ) + ".exe";

        String packageNameOfConcreteClass = getClass().getPackage().getName();

        boolean runWithoutSystemTrayIcon =
            Boolean.parseBoolean(
                System.getProperty( "runWithoutSystemTrayIcon" ) );

        Properties manifest = getManifest();

        boolean initialised =
            initialise(
                packageNameOfConcreteClass,
                runWithoutSystemTrayIcon,
                manifest,
                applicationNameHumanReadable,
                executableName );

        if( initialised )
        {
            String applicationIconImage =
                "img/" + packageNameOfConcreteClass + ".png";

            String[] applicationAuthors =
                manifest.getProperty( MANIFEST_KEY_PROJECT_AUTHOR ).split( "," );

            String applicationURL =
                manifest.getProperty( MANIFEST_KEY_PROJECT_URL );

            String applicationVersion =
                getClass().getPackage().getImplementationVersion();

            if( runWithoutSystemTrayIcon )
                runWithoutSystemTrayIcon(
                    popupMenuClass,
                    applicationAuthors,
                    applicationIconImage,
                    applicationNameHumanReadable,
                    applicationURL,
                    applicationVersion );

            else
                run(
                    popupMenuClass,
                    trayIconClass,
                    applicationAuthors,
                    applicationIconImage,
                    applicationNameHumanReadable,
                    applicationURL,
                    applicationVersion );
        }
    }


    protected static void run(
        final Class<? extends PopupMenuBase> popupMenuClass,
        final Class<? extends TrayIconBase> trayIconClass,
        final String[] applicationAuthors,
        final String applicationIconImage,
        final String applicationNameHumanReadable,
        final String applicationURL,
        final String applicationVersion )
    {
        SwingUtilities.invokeLater(
            new Runnable() 
            {
                @Override
                public void run() 
                {
                    try
                    {
                        final PopupMenuBase popupMenu =
                    		instantiatePopupMenuAndInitialise(
                				popupMenuClass,
                                applicationAuthors,
                                applicationIconImage,
                                applicationNameHumanReadable,
                                applicationURL,
                                applicationVersion );

                        Constructor<? extends TrayIconBase> constructor_trayIcon =
                            trayIconClass.getDeclaredConstructor(
                                new Class<?>[] {
                                	popupMenu.getClass() } );

                        TrayIconBase trayIcon =
                    		constructor_trayIcon.newInstance(
                				popupMenu );

                        trayIcon.initialise();

                        SystemTray.getSystemTray().add( trayIcon );
                        trayIcon.displayMessage();
                    }
                    catch(
                        AWTException | 
                        IllegalAccessException |
                        InstantiationException |
                        InvocationTargetException |
                        NoSuchMethodException exception )
                    {
                        DialogMessage.showError(
                            Messages.getString( "SystemTrayIconBase.0" ), 
                            Messages.getString( "SystemTrayIconBase.6" ),
                            exception,
                            null );
                    }
                }
            }
        );
    }


    protected static void debug()
    {
        String[] properties =
            new String[]
            {
                "java.home",
                "java.version",
                "os.arch",
                "os.name",
                "os.version",
                "user.dir",
                "user.home",
                "user.name"
            };

        System.out.println( "System properties..." );
        for( String property : properties )
            System.out.println(
                "\t" + property + ": " + System.getProperty( property ) );

        System.out.println();
    }


    protected static boolean
    initialise(
        String packageNameOfConcreteClass,
        boolean runWithoutSystemTrayIcon,
        Properties manifest,
        String applicationNameHumanReadable,
        String executableName )
    {
        return
            initialiseMessages( packageNameOfConcreteClass )
            &&
            initialiseLookAndFeel()
            &&
            manifestIsGood( manifest )
            &&
            systemTraySupported( runWithoutSystemTrayIcon )
            &&
            initialiseProperties( packageNameOfConcreteClass )
            &&
            initialiseWindowsRegistry( runWithoutSystemTrayIcon )
            &&
            SystemStart.initialise(
                applicationNameHumanReadable,
                executableName );
    }


    protected static boolean
    initialiseMessages(
        String packageNameOfConcreteClass )
    {
        boolean initialised = false;
        try
        {
            Messages.initialise( packageNameOfConcreteClass );
            initialised = true;
        } 
        catch( MissingResourceException missingResourceException )
        {
            DialogMessage.showError(
                "Fatal Error",
                "Unable to initialise messages.",
                missingResourceException,
                null );
        }

        return initialised;
    }


    protected static boolean initialiseLookAndFeel()
    {
        boolean initialised = false;
        try
        {
            UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName() );

            initialised = true;
        } 
        catch(
            ClassNotFoundException |
            IllegalAccessException |
            InstantiationException |
            UnsupportedLookAndFeelException exception )
        {
            DialogMessage.showError(
                Messages.getString( "SystemTrayIconBase.0" ), 
                Messages.getString( "SystemTrayIconBase.1" ),
                exception,
                null );
        }

        return initialised;
    }


    protected static boolean
    systemTraySupported(
        boolean runWithoutTrayIcon )
    {
        boolean systemTraySupported;

        if( runWithoutTrayIcon )
            systemTraySupported = true;

        else
        {
            systemTraySupported = SystemTray.isSupported();
            if( ! systemTraySupported )
                DialogMessage.showError(
                    Messages.getString( "SystemTrayIconBase.0" ), 
                    Messages.getString( "SystemTrayIconBase.2" ),
                    null,
                    null );
        }

        return systemTraySupported;
    }


    protected static boolean
    initialiseProperties(
        String packageNameOfConcreteClass )
    {
        PropertiesBase.Status status =
            PropertiesBase.initialise( packageNameOfConcreteClass );

        if( status == PropertiesBase.Status.UNABLE_TO_CREATE_PROPERY_DIRECTORY )
        {
            DialogMessage.showError(
                Messages.getString( "SystemTrayIconBase.0" ), 
                Messages.getString( "SystemTrayIconBase.3" )
                +
                PropertiesBase.getPropertyDirectory().getAbsolutePath(), 
                null,
                null );
        }
        else if( status == PropertiesBase.Status.UNABLE_TO_READ_PROPERTY_FILE )
        {
            DialogMessage.showError(
                Messages.getString( "SystemTrayIconBase.0" ), 
                Messages.getString( "SystemTrayIconBase.4" )
                +
                PropertiesBase.getPropertyFile().getAbsolutePath(),
                null,
                null );
        }

        return status == PropertiesBase.Status.OK;
    }


    protected static boolean
    initialiseWindowsRegistry(
        boolean runWithoutTrayIcon )
    {
        boolean initialised;

        if( runWithoutTrayIcon )
            initialised = true;

        else
        {
            initialised = WindowsRegistry.initialise();
            if( ! initialised )
                DialogMessage.showError(
                    Messages.getString( "SystemTrayIconBase.0" ), 
                    Messages.getString( "SystemTrayIconBase.5" ),
                    null,
                    null );
            
        }
        
        return initialised;
    }


    protected static Properties getManifest()
    {
        Properties properties = new Properties();
        try(
            InputStream inputStream =
                ClassLoader.getSystemResourceAsStream( JarFile.MANIFEST_NAME ) )
        {
            if( inputStream != null )
                properties.load( inputStream );

            else
            {
                properties = new Properties();
                DialogMessage.showError(
                    "Fatal Error",
                    "Manifest is empty.",
                    null,
                    null );
            }
        }
        catch( Exception exception )
        {
            properties = new Properties();
            DialogMessage.showError(
                "Fatal Error",
                "Unable to read manifest.",
                exception,
                null );
        }

        return properties;
    }


    protected static boolean
    manifestIsGood(
        Properties manifest )
    {
        return(
            manifest.containsKey( MANIFEST_KEY_PROJECT_AUTHOR )
            &&
            manifest.getProperty( MANIFEST_KEY_PROJECT_AUTHOR ) != null
            &&
            manifest.getProperty( MANIFEST_KEY_PROJECT_AUTHOR ).length() != 0
            &&
            manifest.containsKey( MANIFEST_KEY_PROJECT_URL )
            &&
            manifest.getProperty( MANIFEST_KEY_PROJECT_URL ) != null
            &&
            manifest.getProperty( MANIFEST_KEY_PROJECT_URL ).length() != 0 );
    }


    protected static void runWithoutSystemTrayIcon(
        final Class<? extends PopupMenuBase> popupMenuClass,
        final String[] applicationAuthors,
        final String applicationIconImage,
        final String applicationNameHumanReadable,
        final String applicationURL,
        final String applicationVersion )
    {
        SwingUtilities.invokeLater(
            new Runnable() 
            {
                @Override
                public void run() 
                {
                    try
                    {
                        final PopupMenuBase popupMenu =
                    		instantiatePopupMenuAndInitialise(
                				popupMenuClass,
                                applicationAuthors,
                                applicationIconImage,
                                applicationNameHumanReadable,
                                applicationURL,
                                applicationVersion );

                        final Frame frame =
                    		new Frame(
                				popupMenu.getClass().getName() );

                        frame.addMouseListener
                        (
                            new MouseAdapter()
                            {
                                @Override
                                public void mousePressed( MouseEvent mouseEvent )
                                {
                                    if( mouseEvent.isPopupTrigger() )
                                        showPopupMenu( mouseEvent );
                                }


                                @Override
                                public void mouseReleased( MouseEvent mouseEvent )
                                {
                                    if( mouseEvent.isPopupTrigger() )
                                        showPopupMenu( mouseEvent );
                                }


                                protected void showPopupMenu( MouseEvent mouseEvent )
                                {
                                    popupMenu.show(
                                        frame,
                                        mouseEvent.getX(),
                                        mouseEvent.getY() );
                                }                                
                            }
                        );

                        frame.addWindowListener(
                            new WindowAdapter()
                            {
                                @Override
                                public void windowClosing( WindowEvent windowEvent )
                                {
                                    frame.dispose();
                                }
                            }
                        );

                        frame.addKeyListener(
                            new KeyAdapter()
                            {
                                @Override
                                public void keyPressed( KeyEvent keyEvent )
                                {
                                    if( keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE )
                                        frame.dispose();
                                }
                            }
                        );

                        System.out.println( popupMenu.getMessage() );
                        System.out.println();

                        frame.add( popupMenu );
                        frame.setSize( 400, 300 );
                        frame.setLocationRelativeTo( null );
                        frame.setVisible( true );
                    }
                    catch(
                        IllegalAccessException |
                        InstantiationException |
                        InvocationTargetException |
                        NoSuchMethodException exception )
                    {
                        DialogMessage.showError(
                            "Fatal Error",
                            "See stack trace after closing this dialog!",
                            exception,
                            null );
                    } 
                }
            }
        );
    }


    protected static PopupMenuBase
    instantiatePopupMenuAndInitialise(
		final Class<? extends PopupMenuBase> popupMenuClass,
	    final String[] applicationAuthors,
	    final String applicationIconImage,
	    final String applicationNameHumanReadable,
	    final String applicationURL,
	    final String applicationVersion )
	throws
		IllegalAccessException,
		InvocationTargetException,
		InstantiationException,
		NoSuchMethodException
    {
        Constructor<? extends PopupMenuBase> constructor_popupMenu =
    		popupMenuClass.getDeclaredConstructor(
                new Class<?>[] {
                    String[].class,
                    String.class,
                    String.class,
                    String.class,
                    String.class } );

        PopupMenuBase popupMenu =
    		constructor_popupMenu.newInstance(
            applicationAuthors,
            applicationIconImage,
            applicationNameHumanReadable,
            applicationURL,
            applicationVersion );

        popupMenu.initialise();
        return popupMenu;
    }
}
