//TODO In Ubuntu, it's possible to right click, bring up a dialog and while it's coming up, right click again!

//TODO Change APPLICATION_VERSION_NUMBER

import java.awt.SystemTray;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class WorldTimeSystemTray implements ClipboardOwner
{
    public WorldTimeSystemTray()
	{
    	try { UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() ); }
        catch( IllegalAccessException illegalAccessException ) { /** Do nothing. */ }
        catch( InstantiationException instantiationException ) { /** Do nothing. */ }
        catch( ClassNotFoundException classNotFoundException ) { /** Do nothing. */ }
        catch( UnsupportedLookAndFeelException unsupportedLookAndFeelException ) { /** Do nothing. */ }

        if( ! SystemTray.isSupported() )
        {
        	MessageDialog.showMessageDialog
        	( 
        		Messages.getString( "WorldTimeSystemTray.1" ),  //$NON-NLS-1$
        		Messages.getString( "WorldTimeSystemTray.0" ),  //$NON-NLS-1$
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
				Messages.getString( "WorldTimeSystemTray.2" ), //$NON-NLS-1$ 
				throwable.getMessage(), 
				JOptionPane.ERROR_MESSAGE, 
				JOptionPane.DEFAULT_OPTION
			); 

			return;
		}

		trayIcon.displayStartupBalloon();
	}


	public void lostOwnership( Clipboard clipboard, Transferable transferable ) { /** Do nothing. */ }


    public static void main( String[] args ) { new WorldTimeSystemTray(); }
}
