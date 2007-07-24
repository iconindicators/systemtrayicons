import java.awt.SystemTray;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class StardateSystemTray
{
    public StardateSystemTray()
	{
        try { UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() ); }
        catch( IllegalAccessException illegalAccessException ) {  }
        catch( InstantiationException instantiationException ) {  }
        catch( ClassNotFoundException classNotFoundException ) {  }
        catch( UnsupportedLookAndFeelException unsupportedLookAndFeelException ) {  }

        if( ! SystemTray.isSupported() )
        {
			JOptionPane.showMessageDialog
			(
				null, 
				Messages.getString( "StardateSystemTray.0" ),  
				Messages.getString( "StardateSystemTray.1" ),  
				JOptionPane.ERROR_MESSAGE
			);

			return;
        }

    	TrayIcon trayIcon = TrayIcon.createTrayIcon( new PopupMenu() );
    	SystemTray systemTray = SystemTray.getSystemTray();
		try { systemTray.add( trayIcon ); }
		catch( Throwable throwable ) 
		{
			JOptionPane.showMessageDialog( null, throwable.getMessage(), Messages.getString( "StardateSystemTray.2" ), JOptionPane.ERROR_MESSAGE );
			return;
		}
		trayIcon.displayStartupBalloon();
	}


    public static void main( String[] args ) { new StardateSystemTray(); }
}
