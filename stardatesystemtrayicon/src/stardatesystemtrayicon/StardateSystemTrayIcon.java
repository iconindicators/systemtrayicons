package stardatesystemtrayicon;


import basesystemtrayicon.SystemTrayIconBase;
import stardatesystemtrayicon.gui.TrayIcon;


/**
 * Entry point for Stardate System Tray Icon.
 *
 * To run from within the source tree:
 *
 *    Microsoft Windows:
 *        java -cp release\stardatesystemtrayicon-6.0.0.jar;stardatesystemtrayicon\lib\joda-time-2.14.0.jar stardatesystemtrayicon.StardateSystemTrayIcon
 *
 *    Linux:
 *        java -DrunWithoutSystemTrayIcon=true -cp release/stardatesystemtrayicon-6.0.0.jar:stardatesystemtrayicon/lib/joda-time-2.14.0.jar stardatesystemtrayicon.StardateSystemTrayIcon
 */
public class StardateSystemTrayIcon extends SystemTrayIconBase
{
    private StardateSystemTrayIcon()
    {
        super( TrayIcon.class );
    }


    public static void main( String[] args )
    {
        new StardateSystemTrayIcon();
    }
}
