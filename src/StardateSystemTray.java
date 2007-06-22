import java.awt.SystemTray;

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
	    	return;

    	SystemTray systemTray = SystemTray.getSystemTray();
	 
    	TrayIcon trayIcon = TrayIcon.createTrayIcon( new PopupMenu() );
		try { systemTray.add( trayIcon ); }
		catch( Throwable throwable ) { }

		trayIcon.displayStartupBalloon();
	}


    public static void main( String[] args )
	{
        new StardateSystemTray();
	}
}
