package basesystemtrayicon.gui;


import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.text.MessageFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import javax.swing.JEditorPane;

import basesystemtrayicon.Messages;
import basesystemtrayicon.PropertiesBase;


public abstract class PopupMenuBase
extends PopupMenu
implements ActionListener, ClipboardOwner
{
    private static final long serialVersionUID = 1L;

    private static final String POPUP_EXIT =
        Messages.getString( "PopupMenuBase.0" );

    private static final String POPUP_ABOUT =
        Messages.getString( "PopupMenuBase.1" );

    private static final String POPUP_SETTINGS =
        Messages.getString( "PopupMenuBase.2" );

    private String m_applicationName;

    private transient Image m_applicationIconImage;

    private Class<? extends DialogSettingsBase> m_dialogSettingsBase;

    private JEditorPane m_aboutLabel;

    private boolean m_enabled = true;


    public PopupMenuBase(
        String[] applicationAuthors,
        String[] applicationCredits,
        String applicationIconImage,
        String applicationName,
        String applicationURL,
        String applicationVersion,
        Class<? extends DialogSettingsBase> dialogSettings )
    {
        super();

        m_applicationName = applicationName;
        m_applicationIconImage = Utils.getImage( applicationIconImage );
        m_dialogSettingsBase = dialogSettings;

        m_aboutLabel =
            createAboutLabel(
                applicationAuthors,
                applicationCredits,
                m_applicationIconImage,
                applicationIconImage,
                applicationName,
                applicationURL,
                applicationVersion );

        MenuItem menuItem = null;

        menuItem = new MenuItem( POPUP_SETTINGS );
        menuItem.addActionListener( this );
        add( menuItem );

        menuItem = new MenuItem( POPUP_ABOUT );
        menuItem.addActionListener( this );
        add( menuItem );

        menuItem = new MenuItem( POPUP_EXIT );
        menuItem.addActionListener( this );
        add( menuItem );
    }


    public abstract String getMessage();


    @Override
    public boolean isEnabled()
    {
        return m_enabled;
    }


    @Override
    public synchronized void setEnabled( boolean enabled )
    {
        m_enabled = enabled;
    }


    public final String getApplicationName()
    {
        return m_applicationName;
    }


    public final Image getApplicationIconImage()
    {
        return m_applicationIconImage;
    }


    protected void copyToClipboard( String message )
    {
        try
        {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
                new StringSelection( message ), this );
        }
        catch( IllegalStateException illegalStateException )
        {
            DialogMessage.showError(
                Messages.getString( "PopupMenuBase.10" ),
                Messages.getString( "PopupMenuBase.8" ),
                illegalStateException,
                getApplicationIconImage() );
        }
    }


    @Override
    public void actionPerformed( ActionEvent actionEvent )
    {
        String menuItem = ( (MenuItem)actionEvent.getSource() ).getLabel();
        if( POPUP_SETTINGS.equals( menuItem ) )
        {
            setEnabled( false );

            try
            {
                Constructor<? extends DialogSettingsBase> constructor =
                    m_dialogSettingsBase.getDeclaredConstructor( Image.class );

                constructor.newInstance( getApplicationIconImage() );
            }
            catch
            (
                InstantiationException |
                IllegalAccessException |
                IllegalArgumentException |
                InvocationTargetException |
                NoSuchMethodException |
                SecurityException exception
            )
            {
                DialogMessage.showError(
                    Messages.getString( "PopupMenuBase.10" ),
                    Messages.getString( "PopupMenuBase.9" ), 
                    exception,
                    getApplicationIconImage() );
            }

            setEnabled( true );
        }
        else if( POPUP_ABOUT.equals( menuItem ) )
        {
            setEnabled( false );

            DialogMessage.showPlain(
                POPUP_ABOUT,
                m_aboutLabel,
                getApplicationIconImage() );

            setEnabled( true );
        }
        else if( POPUP_EXIT.equals( menuItem ) )
            System.exit( 0 );
    }


    @Override
    public void lostOwnership( Clipboard arg0, Transferable arg1 )
    {
        /** Do nothing. */
    }


    private static JEditorPane
    createAboutLabel(
        String[] applicationAuthors,
        String[] applicationCredits,
        Image applicationIconImageAsImage,
        String applicationIconImageAsString,
        String applicationName,
        String applicationURL,
        String applicationVersion )
    {
        URL imageURL =
            ClassLoader.getSystemResource( applicationIconImageAsString );

        String version =
            new MessageFormat(
                Messages.getString( "PopupMenuBase.6" ) ).format(
                    new Object[] { applicationVersion } );

        String projectURL =
            "<a href='" + applicationURL + "'>" + applicationURL + "</a>";

        String urlPadding = "&nbsp;&nbsp;&nbsp;&nbsp;";

        String author;
        if( applicationAuthors.length == 1 )
            author = Messages.getString( "PopupMenuBase.4" );

        else
            author = Messages.getString( "PopupMenuBase.5" );

        StringBuilder authors = new StringBuilder();
        for( String author_ : applicationAuthors )
            authors.append( author_ + "<br>" );

        String credit;
        if( applicationCredits.length == 0 )
            credit = Messages.getString( "PopupMenuBase.11" );

        else
            credit = Messages.getString( "PopupMenuBase.7" );

        StringBuilder credits = new StringBuilder();
        for( String credit_ : applicationCredits )
            credits.append(
                credit_.replace( "\n", "<br>" ) + "<br><br>" );

        String CREDIT_NSIS = Messages.getString( "PopupMenuBase.3" );

        credits.append( CREDIT_NSIS );

        String message =
            "<html>" +
                "<center>" +
                    "<img src=\"" + imageURL + "\"/>" +
                    "<h2>" + applicationName + "</h2>" +
                    version +
                    "<br>" +
                    urlPadding + projectURL + urlPadding +
                    "<br><br>" +
                    "<h3>" + author + "</h3>" +
                    authors.toString() +
                    "<br>" +
                    "<h3>" + credit + "</h3>" +
                    credits.toString() +
                    "<br><br>" +
                "</center>" +
            "</html>";

        return Utils.createURLLabel( message, applicationIconImageAsImage );
    }


    /**
     * Read the value from the given property key and return the corresponding
     * date/time formatter.
     *
     * If the property does not exist or there is no value, a date/time
     * formatter for a long date will be used.
     *
     * @param key Property key.
     * @param defaultValue Default value for property if the key is not found
     * or has no value.
     *
     * @return A date/time formatter for the property key's value.
     */
    protected static DateTimeFormatter
    getDateTimeFormatter(
        String key,
        String defaultValue,
        String userDefinedDefault )
    {
        DateTimeFormatter dateTimeFormatter;

        String dateTimeFormat = PropertiesBase.getProperty( key, defaultValue );
        switch( dateTimeFormat )
        {
            case PropertiesBase.VALUE_DATE_TIME_FULL:
                dateTimeFormatter =
                    DateTimeFormatter.ofLocalizedDateTime(
                        FormatStyle.FULL,
                        FormatStyle.FULL );

                break;

            case PropertiesBase.VALUE_DATE_TIME_LONG:
                dateTimeFormatter =
                    DateTimeFormatter.ofLocalizedDateTime(
                        FormatStyle.LONG,
                        FormatStyle.LONG );

                break;

            case PropertiesBase.VALUE_DATE_TIME_MEDIUM:
                dateTimeFormatter =
                    DateTimeFormatter.ofLocalizedDateTime(
                        FormatStyle.MEDIUM,
                        FormatStyle.MEDIUM );

                break;

            case PropertiesBase.VALUE_DATE_TIME_SHORT:
                dateTimeFormatter =
                    DateTimeFormatter.ofLocalizedDateTime(
                        FormatStyle.SHORT,
                        FormatStyle.SHORT );

                break;

            case PropertiesBase.VALUE_DATE_FULL:
                dateTimeFormatter =
                    DateTimeFormatter.ofLocalizedDate(
                        FormatStyle.FULL );

                break;

            case PropertiesBase.VALUE_DATE_LONG:
                dateTimeFormatter =
                    DateTimeFormatter.ofLocalizedDate(
                        FormatStyle.LONG );

                break;

            case PropertiesBase.VALUE_DATE_MEDIUM:
                dateTimeFormatter =
                    DateTimeFormatter.ofLocalizedDate(
                        FormatStyle.MEDIUM );

                break;

            case PropertiesBase.VALUE_DATE_SHORT:
                dateTimeFormatter =
                    DateTimeFormatter.ofLocalizedDate(
                        FormatStyle.SHORT );

                break;

            case PropertiesBase.VALUE_TIME_FULL:
                dateTimeFormatter =
                    DateTimeFormatter.ofLocalizedTime(
                        FormatStyle.FULL );

                break;

            case PropertiesBase.VALUE_TIME_LONG:
                dateTimeFormatter =
                    DateTimeFormatter.ofLocalizedTime(
                        FormatStyle.LONG );

                break;

            case PropertiesBase.VALUE_TIME_MEDIUM:
                dateTimeFormatter =
                    DateTimeFormatter.ofLocalizedTime(
                        FormatStyle.MEDIUM );

                break;

            case PropertiesBase.VALUE_TIME_SHORT:
                dateTimeFormatter =
                    DateTimeFormatter.ofLocalizedTime(
                        FormatStyle.SHORT );

                break;

            case PropertiesBase.VALUE_USER_DEFINED:
                try
                {
                    dateTimeFormatter =
                        DateTimeFormatter.ofPattern(
                            PropertiesBase.getProperty(
                                PropertiesBase.KEY_USER_DEFINED,
                                userDefinedDefault ) );
                }
                catch( IllegalArgumentException exception ) 
                {
                    dateTimeFormatter =
                        DateTimeFormatter.ofLocalizedDate( FormatStyle.LONG ); 

                    System.err.println( "Bad date/time format string: " + dateTimeFormat );
                }

                break;

            default:
                // This should never occur, unless the properties file is
                // manipulated for testing/debugging; handle gracefully rather than
                // throw an exception.
                dateTimeFormatter =
                    DateTimeFormatter.ofLocalizedDate( FormatStyle.LONG ); 

                System.err.println( "Unknown date/time format: " + dateTimeFormat );
        }

        return dateTimeFormatter;
    }
}
