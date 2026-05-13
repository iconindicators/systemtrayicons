package stardatesystemtrayicon.gui;


import basesystemtrayicon.gui.TrayIconBase;


public class TrayIcon extends TrayIconBase
{
    public TrayIcon( PopupMenu popupMenu )
    {
        super( popupMenu );
    }


    @Override
    public String getToolTip()
    {
        return PopupMenu.getStardate();
    }
}
