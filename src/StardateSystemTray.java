import java.awt.SystemTray;


public class StardateSystemTray
{
    public StardateSystemTray()
	{
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
