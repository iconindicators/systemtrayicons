package basesystemtrayicon.gui;


import java.awt.TrayIcon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


public abstract class TrayIconBase
extends TrayIcon
implements MouseListener, MouseMotionListener
{
    private PopupMenuBase m_popupMenu;


    protected TrayIconBase( PopupMenuBase popupMenuBase )
    {
        super(
            popupMenuBase.getApplicationIconImage(),
            null,
            popupMenuBase );

        m_popupMenu = popupMenuBase;
    }


    public void initialise()
    {
        setImageAutoSize( true );
        addMouseListener( this );
        addMouseMotionListener( this );
        setImageAutoSize( true );
    }


    /**
     * Return the text for display in the icon's tool tip (hover text).
     * 
     * @return The tool tip.
     */
    @Override
    public abstract String getToolTip();

    
    /**
     * Return the (plain) text for display in the icon's balloon message.
     * 
     * @return The balloon message.
     */
    public void displayMessage()
    {
        PopupMenuBase popupMenuBase = ( PopupMenuBase )getPopupMenu();
        super.displayMessage(
            popupMenuBase.getApplicationName(),
            popupMenuBase.getMessage(),
            TrayIcon.MessageType.NONE );
    }


    @Override
    public void mouseMoved( MouseEvent mouseEvent )
    {
        // The tooltip can change depending on a change to user settings,
        // therefore must generate on demand.
        super.setToolTip( getToolTip() );
    }


    @Override
    public void mousePressed( MouseEvent mouseEvent )
    {
        if( mouseEvent.getButton() == MouseEvent.BUTTON1 )
            ( (TrayIconBase)mouseEvent.getSource() ).displayMessage();

        if( mouseEvent.getButton() == MouseEvent.BUTTON3 )
        {
            // If a dialog is already showing, disable the popup menu
            // as allowing another dialog to be displayed,
            // or the same dialog to display is trouble.
            if( m_popupMenu.isEnabled() )
                super.setPopupMenu( m_popupMenu );

            else
                super.setPopupMenu( null );
        }
    }


    @Override
    public void mouseClicked( MouseEvent mouseEvent )
    {
        /** Do nothing. */
    }


    @Override
    public void mouseDragged( MouseEvent mouseEvent )
    {
        /** Do nothing. */
    }


    @Override
    public void mouseEntered( MouseEvent mouseEvent )
    {
        /** Do nothing. */
    }


    @Override
    public void mouseExited( MouseEvent mouseEvent )
    {
        /** Do nothing. */
    }


    @Override
    public void mouseReleased( MouseEvent mouseEvent )
    {
        /** Do nothing. */
    }
}
