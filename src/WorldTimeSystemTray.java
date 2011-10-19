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
	public static final String APPLICATION_VERSION_NUMBER = "1.7 (2011-10-19)"; //$NON-NLS-1$


	public WorldTimeSystemTray()
	{
    	// On Ubuntu, running the application on startup failed as the system tray was not ready, so sleep (on all platforms, can't hurt).    	
    	try { Thread.sleep( 2500 ); }
		catch( InterruptedException interruptedException ) { /** Do nothing. */ }

    	try { UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() ); }
        catch( Exception exception ) { /** Do nothing. */ }

        if( ! SystemTray.isSupported() )
        {
        	MessageDialog.showMessageDialog
        	( 
        		Messages.getString( "WorldTimeSystemTray.1" ), //$NON-NLS-1$
        		Messages.getString( "WorldTimeSystemTray.0" ), //$NON-NLS-1$
				JOptionPane.ERROR_MESSAGE,
				JOptionPane.DEFAULT_OPTION
        	);

			return;
        }

        if( ! Properties.getInstance().canCreatePropertyDirectory() )
        {
	    	MessageDialog.showMessageDialog
	    	( 
	    		Messages.getString( "WorldTimeSystemTray.3" ), //$NON-NLS-1$
	    		Messages.getString( "WorldTimeSystemTray.4" ) + Properties.PROPERTY_DIRECTORY, //$NON-NLS-1$
				JOptionPane.ERROR_MESSAGE,
				JOptionPane.DEFAULT_OPTION
	    	);
	
			return;
        }

        if( ! Properties.getInstance().canReadPropertyFile() )
        {
	    	MessageDialog.showMessageDialog
	    	( 
	    		Messages.getString( "WorldTimeSystemTray.5" ), //$NON-NLS-1$
	    		Messages.getString( "WorldTimeSystemTray.6" ) + Properties.PROPERTY_FILE, //$NON-NLS-1$
				JOptionPane.ERROR_MESSAGE,
				JOptionPane.DEFAULT_OPTION
	    	);
	
			return;
        }

        if( ! Properties.getInstance().canWritePropertyFile() )
        {
	    	MessageDialog.showMessageDialog
	    	( 
	    		Messages.getString( "WorldTimeSystemTray.7" ), //$NON-NLS-1$
	    		Messages.getString( "WorldTimeSystemTray.8" ) + Properties.PROPERTY_FILE, //$NON-NLS-1$
				JOptionPane.ERROR_MESSAGE,
				JOptionPane.DEFAULT_OPTION
	    	);
	
			return;
        }

        if( OperatingSystem.isWindows() )
        {
        	try { WindowsRegistry.initialise(); }
        	catch( Exception exception )
        	{
        		MessageDialog.showMessageDialog( Messages.getString( "WorldTimeSystemTray.9" ), Messages.getString( "WorldTimeSystemTray.10" ), JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION );  //$NON-NLS-1$//$NON-NLS-2$
				return;
        	}
        }

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


	@Override public void lostOwnership( Clipboard clipboard, Transferable transferable ) { /** Do nothing. */ }


	public static void main( String[] args ) { new WorldTimeSystemTray(); }
}
