package stardatesystemtrayicon.gui;


import basesystemtrayicon.gui.TrayIconBase;


public class TrayIcon extends TrayIconBase
{
    public TrayIcon(
        String[] applicationAuthors,
        String applicationIconImage,
        String applicationName,
        String applicationURL,
        String applicationVersion )
    {
        super(
            new PopupMenu(
                applicationAuthors,
                applicationIconImage,
                applicationName,
                applicationURL,
                applicationVersion ) );
    }


    @Override
    public String getToolTip()
    {
        return PopupMenu.getStardate();
    }
}
