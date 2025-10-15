package worldtimesystemtrayicon;


import basesystemtrayicon.SystemTrayIconBase;
import worldtimesystemtrayicon.gui.TrayIcon;


/**
 * Entry point for World Time System Tray Icon.
 *
 * To run from within the source tree:
 *
 *    Windows:
 *        java -jar release\worldtimesystemtrayicon-3.0.0.jar
 *
 *    Linux:
 *        java -DrunWithoutSystemTrayIcon=true -jar release/worldtimesystemtrayicon-3.0.0.jar
 */
public class WorldTimeSystemTrayIcon extends SystemTrayIconBase
{
	private WorldTimeSystemTrayIcon()
	{
        super( TrayIcon.class );
	}


    public static void main( String[] args )
    {
        new WorldTimeSystemTrayIcon();
    }
}
