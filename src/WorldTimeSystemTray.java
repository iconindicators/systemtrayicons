import java.awt.SystemTray;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class WorldTimeSystemTray
{
    public WorldTimeSystemTray()
	{
    	try { UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() ); }
        catch( IllegalAccessException illegalAccessException ) { }
        catch( InstantiationException instantiationException ) { }
        catch( ClassNotFoundException classNotFoundException ) { }
        catch( UnsupportedLookAndFeelException unsupportedLookAndFeelException ) { }

        if( ! SystemTray.isSupported() )
        {
			JOptionPane.showMessageDialog
			(
				null, 
				Messages.getString( "WorldTimeSystemTray.0" ), 
				Messages.getString( "WorldTimeSystemTray.1" ), 
				JOptionPane.ERROR_MESSAGE
			);

			return;
        }

    	TrayIcon trayIcon = TrayIcon.createTrayIcon( new PopupMenu() );
    	SystemTray systemTray = SystemTray.getSystemTray();
    	try { systemTray.add( trayIcon ); }
		catch( Throwable throwable ) 
		{
			JOptionPane.showMessageDialog( null, throwable.getMessage(), Messages.getString( "WorldTimeSystemTray.2" ), JOptionPane.ERROR_MESSAGE );
			return;
		}
		trayIcon.displayStartupBalloon();
	}


    public static void main( String[] args ) { new WorldTimeSystemTray(); }
}
