import java.awt.SystemTray;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class StardateSystemTray
{
    public StardateSystemTray()
	{
        try { UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() ); }
        catch( IllegalAccessException illegalAccessException ) { /** Do nothing. */  }
        catch( InstantiationException instantiationException ) { /** Do nothing. */ }
        catch( ClassNotFoundException classNotFoundException ) { /** Do nothing. */ }
        catch( UnsupportedLookAndFeelException unsupportedLookAndFeelException ) { /** Do nothing. */ }

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

    	TrayIcon trayIcon = TrayIcon.createTrayIcon( new PopupMenu() );
    	SystemTray systemTray = SystemTray.getSystemTray();

    	try { systemTray.add( trayIcon ); }
		catch( Throwable throwable ) 
		{
			MessageDialog.showMessageDialog
			(
					Messages.getString( "StardateSystemTray.2" ), //$NON-NLS-1$ 
				throwable.getMessage(), 
				JOptionPane.ERROR_MESSAGE, 
				JOptionPane.DEFAULT_OPTION
			); 

			return;
		}

		trayIcon.displayStartupBalloon();
	}


    public static void main( String[] args ) { new StardateSystemTray(); }
}
