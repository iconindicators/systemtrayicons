import java.awt.SystemTray;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;


public class StardateSystemTray
{
    public StardateSystemTray()
	{
    	UIManager.put( "swing.boldMetal", Boolean.FALSE ); //$NON-NLS-1$
    	try { UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() ); }
        catch( Throwable throwable ) { /** Do nothing. */ }

        if( ! SystemTray.isSupported() )
        {
        	MessageDialog.showMessageDialog
        	(
        		Messages.getString( "StardateSystemTray.1" ), //$NON-NLS-1$
        		Messages.getString( "StardateSystemTray.0" ), //$NON-NLS-1$
        		JOptionPane.ERROR_MESSAGE,
				JOptionPane.DEFAULT_OPTION
        	);

        	return;
        }

        if( ! Properties.getInstance().canCreatePropertyDirectory() )
        {
	    	MessageDialog.showMessageDialog
	    	( 
	    		Messages.getString( "StardateSystemTray.3" ), //$NON-NLS-1$
	    		Messages.getString( "StardateSystemTray.4" ) + Properties.PROPERTY_DIRECTORY, //$NON-NLS-1$
				JOptionPane.ERROR_MESSAGE,
				JOptionPane.DEFAULT_OPTION
	    	);
	
			return;
        }
		
        if( ! Properties.getInstance().canReadPropertyFile() )
        {
	    	MessageDialog.showMessageDialog
	    	( 
	    		Messages.getString( "StardateSystemTray.5" ), //$NON-NLS-1$
	    		Messages.getString( "StardateSystemTray.6" ) + Properties.PROPERTY_FILE, //$NON-NLS-1$
				JOptionPane.ERROR_MESSAGE,
				JOptionPane.DEFAULT_OPTION
	    	);
	
			return;
        }
		
        if( ! Properties.getInstance().canWritePropertyFile() )
        {
	    	MessageDialog.showMessageDialog
	    	( 
	    		Messages.getString( "StardateSystemTray.7" ), //$NON-NLS-1$
	    		Messages.getString( "StardateSystemTray.8" ) + Properties.PROPERTY_FILE, //$NON-NLS-1$
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
	        				Messages.getString( "StardateSystemTray.2" ), //$NON-NLS-1$ 
	        				throwable.getMessage(), 
	        				JOptionPane.ERROR_MESSAGE, 
	        				JOptionPane.DEFAULT_OPTION
	        			); 
	        		}
        		}
        	}
        );
	}


    public static void main( String[] args ) { new StardateSystemTray(); }
}
