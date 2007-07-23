import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;


public class TrayIcon extends java.awt.TrayIcon implements MouseListener, MouseMotionListener
{
	private static final String TRAY_ICON_IMAGE = "trayicon.gif"; 
	private static final String MESSAGE_TOOL_TIP = Messages.getString( "TrayIcon.1" ); 

	private PopupMenu m_popupMenu = null;
	

	private TrayIcon( PopupMenu popupMenu )
	{
		super( new ImageIcon( TRAY_ICON_IMAGE ).getImage(), null, popupMenu );
		setImageAutoSize( true );
		addMouseListener( this );
		addMouseMotionListener( this );
		m_popupMenu = popupMenu;
	}


	public static TrayIcon createTrayIcon( PopupMenu popupMenu ) { return new TrayIcon( popupMenu ); }


	public void displayStartupBalloon() { displayMessage( PopupMenu.APPLICATION_NAME, getMessageString(), TrayIcon.MessageType.NONE ); }


	public static final String getTrayIconImage() { return TRAY_ICON_IMAGE; }


	private String getMessageString() { return Message.getMessageString( null, 0, 0, false ); }

    
    public void mouseDragged( MouseEvent mouseEvent ) { }


	public void mouseMoved( MouseEvent mouseEvent ) 
	{ 
		if( Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_SHOW_TIMES_IN_TOOL_TIP, true ) )
			setToolTip( getMessageString() );
		else
			setToolTip( MESSAGE_TOOL_TIP );
	}


    public void mousePressed( MouseEvent mouseEvent )
	{
		if( mouseEvent.getButton() == MouseEvent.BUTTON1 )
		{
        	TrayIcon trayIcon = (TrayIcon)mouseEvent.getSource();
			trayIcon.displayMessage( PopupMenu.APPLICATION_NAME, getMessageString(), TrayIcon.MessageType.NONE );
		}
		else if( mouseEvent.getButton() == MouseEvent.BUTTON3 )
		{
			// Disable the pop up menu when we put up another dialog.
			if( m_popupMenu.popupIsDisabled() )
				setPopupMenu( null );
			else
				setPopupMenu( m_popupMenu );
		}
	}


	public void mouseClicked( MouseEvent mouseEvent ) { }


	public void mouseEntered( MouseEvent mouseEvent ) { }


	public void mouseExited( MouseEvent mouseEvent ) { }


	public void mouseReleased( MouseEvent mouseEvent ) { }
}
