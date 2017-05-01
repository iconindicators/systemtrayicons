import java.awt.SystemTray;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;


public class WorldTimeSystemTray implements ClipboardOwner
{
	public static final String APPLICATION_AUTHOR = "Bernard Giannetti"; //$NON-NLS-1$
	public static final String APPLICATION_EXECUTABLE = "WorldTimeSystemTray.exe";  //$NON-NLS-1$
	public static final String APPLICATION_NAME = "World Time System Tray";  //$NON-NLS-1$
	public static final String APPLICATION_URL = "http://wrldtimesystray.sourceforge.net"; //$NON-NLS-1$
	public static final String APPLICATION_VERSION_NUMBER = "2.0 (2017-04-29)"; //$NON-NLS-1$


	public WorldTimeSystemTray()
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
	        		MessageDialog.showMessageDialog( Messages.getString( "WorldTimeSystemTray.9" ), Messages.getString( "WorldTimeSystemTray.10" ), JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION );  //$NON-NLS-1$//$NON-NLS-2$
	        	}
	        }

	        if( thunderbirdsAreGo )
		        SwingUtilities.invokeLater
		        (
		        	new Runnable() 
		        	{
		        		@Override public void run() 
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
			        				Messages.getString( "WorldTimeSystemTray.2" ), //$NON-NLS-1$ 
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
        	MessageDialog.showMessageDialog
        	( 
        		Messages.getString( "WorldTimeSystemTray.1" ), //$NON-NLS-1$
        		Messages.getString( "WorldTimeSystemTray.0" ), //$NON-NLS-1$
				JOptionPane.ERROR_MESSAGE,
				JOptionPane.DEFAULT_OPTION
        	);

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
	    		Messages.getString( "WorldTimeSystemTray.3" ), //$NON-NLS-1$
	    		Messages.getString( "WorldTimeSystemTray.4" ) + Properties.PROPERTY_DIRECTORY, //$NON-NLS-1$
				JOptionPane.ERROR_MESSAGE,
				JOptionPane.DEFAULT_OPTION
	    	);
        }
		else if( ! Properties.canReadPropertyFile() )
        {
			initialised = false;
	    	MessageDialog.showMessageDialog
	    	( 
	    		Messages.getString( "WorldTimeSystemTray.5" ), //$NON-NLS-1$
	    		Messages.getString( "WorldTimeSystemTray.6" ) + Properties.PROPERTY_FILE, //$NON-NLS-1$
				JOptionPane.ERROR_MESSAGE,
				JOptionPane.DEFAULT_OPTION
	    	);
        }
		else if( ! Properties.canWritePropertyFile() )
        {
			initialised = false;
	    	MessageDialog.showMessageDialog
	    	( 
	    		Messages.getString( "WorldTimeSystemTray.7" ), //$NON-NLS-1$
	    		Messages.getString( "WorldTimeSystemTray.8" ) + Properties.PROPERTY_FILE, //$NON-NLS-1$
				JOptionPane.ERROR_MESSAGE,
				JOptionPane.DEFAULT_OPTION
	    	);
        }

		return initialised;
	}


	@Override public void lostOwnership( Clipboard clipboard, Transferable transferable ) { /** Do nothing. */ }


	public static void main( String[] args ) { new WorldTimeSystemTray(); }
}