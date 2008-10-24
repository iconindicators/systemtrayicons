//TODO Install and Launcher directories need to be removed/deleted from SVN.

//TODO Put in date into changelog.Debian (date -R) and ReleaseNotes.txt

//TODO Add new stuff to SVN.

//TODO Change APPLICATION_VERSION_NUMBER ... the DATE!!! In popupmenu

import java.awt.SystemTray;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;


public class WorldTimeSystemTray implements ClipboardOwner
{
    public WorldTimeSystemTray()
	{
    	UIManager.put( "swing.boldMetal", Boolean.FALSE ); //$NON-NLS-1$
    	try { UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() ); }
        catch( Throwable throwable ) { /** Do nothing. */ }

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

        SwingUtilities.invokeLater
        (
        	new Runnable() 
        	{
        		public void run() 
        		{
        			TrayIcon trayIcon = TrayIcon.createTrayIcon();
	            	try 
	            	{ 
	            		SystemTray.getSystemTray().add( trayIcon ); 
	            		trayIcon.displayStartupBalloon();
	            	}
	        		catch( Throwable throwable ) 
	        		{
	        			MessageDialog.showMessageDialog
	        			(
	        				Messages.getString( "WorldTimeSystemTray.2" ), //$NON-NLS-1$ 
	        				throwable.getMessage(), 
	        				JOptionPane.ERROR_MESSAGE, 
	        				JOptionPane.DEFAULT_OPTION
	        			); 
	        		}
        		}
        	}
        );
	}


	public void lostOwnership( Clipboard clipboard, Transferable transferable ) { /** Do nothing. */ }


    public static void main( String[] args ) { new WorldTimeSystemTray(); }
}
