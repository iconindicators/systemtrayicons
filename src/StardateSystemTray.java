import java.awt.SystemTray;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;


public class StardateSystemTray
{
	public static final String APPLICATION_AUTHOR = "Bernard Giannetti"; //$NON-NLS-1$
	public static final String APPLICATION_EXECUTABLE = "StardateSystemTray.exe";  //$NON-NLS-1$
	public static final String APPLICATION_NAME = "Stardate System Tray";  //$NON-NLS-1$
	public static final String APPLICATION_URL = "http://stardatesystray.sourceforge.net"; //$NON-NLS-1$
	public static final String APPLICATION_VERSION_NUMBER = "3.0 (2017-04-29)"; //$NON-NLS-1$


	public StardateSystemTray()
	{
    	try { UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() ); }
        catch( Exception exception ) { /** Do nothing. */ }

    	if( systemTraySupported() && initialiseProperties() )
        {
    		boolean thunderbirdsAreGo = true;
    		if( OperatingSystem.isWindows() )
	        {
	        	try { WindowsRegistry.initialise(); }
	        	catch( Exception exception )
	        	{
	        		thunderbirdsAreGo = false;
	        		MessageDialog.showMessageDialog( Messages.getString( "StardateSystemTray.9" ), Messages.getString( "StardateSystemTray.10" ), JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION );  //$NON-NLS-1$//$NON-NLS-2$
	        	}
	        }

    		if( thunderbirdsAreGo )
	    		SwingUtilities.invokeLater
		        (
		        	new Runnable() 
		        	{
		        		@Override
						public void run() 
		        		{
		        			TrayIcon trayIcon = TrayIcon.createTrayIcon();
			            	try 
			            	{ 
			            		SystemTray.getSystemTray().add( trayIcon ); 
			            		trayIcon.displayStartupBalloon();
			            	}
			        		catch( Exception exception ) 
			        		{
			        			MessageDialog.showMessageDialog
			        			(
			        				Messages.getString( "StardateSystemTray.2" ), //$NON-NLS-1$ 
			        				exception.getMessage(), 
			        				JOptionPane.ERROR_MESSAGE, 
			        				JOptionPane.DEFAULT_OPTION
			        			);
			        			
			        			System.exit( 1 );
			        		}
		        		}
		        	}
		        );
		}
	}

	
	private static boolean systemTraySupported()
	{
        if( ! SystemTray.isSupported() )
        {
        	MessageDialog.showMessageDialog
        	(
        		Messages.getString( "StardateSystemTray.1" ), //$NON-NLS-1$
        		Messages.getString( "StardateSystemTray.0" ), //$NON-NLS-1$
        		JOptionPane.ERROR_MESSAGE,
				JOptionPane.DEFAULT_OPTION
        	);
        }
		
        return SystemTray.isSupported();
	}


	private static boolean initialiseProperties()
	{
		boolean initialised = true;
		Properties.getInstance();
		if( ! Properties.canCreatePropertyDirectory() )
        {
			initialised = false;
	    	MessageDialog.showMessageDialog
	    	(
	    		Messages.getString( "StardateSystemTray.3" ), //$NON-NLS-1$
	    		Messages.getString( "StardateSystemTray.4" ) + Properties.PROPERTY_DIRECTORY, //$NON-NLS-1$
				JOptionPane.ERROR_MESSAGE,
				JOptionPane.DEFAULT_OPTION
	    	);
        }
		else if( ! Properties.canReadPropertyFile() )
        {
			initialised = false;
	    	MessageDialog.showMessageDialog
	    	(
	    		Messages.getString( "StardateSystemTray.5" ), //$NON-NLS-1$
	    		Messages.getString( "StardateSystemTray.6" ) + Properties.PROPERTY_FILE, //$NON-NLS-1$
				JOptionPane.ERROR_MESSAGE,
				JOptionPane.DEFAULT_OPTION
	    	);
        }
		else if( ! Properties.canWritePropertyFile() )
        {
			initialised = false;
	    	MessageDialog.showMessageDialog
	    	(
	    		Messages.getString( "StardateSystemTray.7" ), //$NON-NLS-1$
	    		Messages.getString( "StardateSystemTray.8" ) + Properties.PROPERTY_FILE, //$NON-NLS-1$
				JOptionPane.ERROR_MESSAGE,
				JOptionPane.DEFAULT_OPTION
	    	);
        }

		return initialised;
	}


    public static void main( String[] args ) { new StardateSystemTray(); }
}