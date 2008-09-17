import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.GregorianCalendar;

import javax.swing.ImageIcon;


public class TrayIcon extends java.awt.TrayIcon implements MouseListener, MouseMotionListener
{
	private static final String TRAY_ICON_IMAGE = "trayicon.gif";  //$NON-NLS-1$
	private static final String MESSAGE_TOOL_TIP = Messages.getString( "TrayIcon.1" );  //$NON-NLS-1$

	private PopupMenu m_popupMenu = null;
	

	private TrayIcon( PopupMenu popupMenu )
	{
		super( getTrayIconImage(), null, popupMenu );

		setImageAutoSize( true );
		addMouseListener( this );
		addMouseMotionListener( this );
		m_popupMenu = popupMenu;
	}


	public static TrayIcon createTrayIcon( PopupMenu popupMenu ) { return new TrayIcon( popupMenu ); }


	public void displayStartupBalloon() { displayMessage( PopupMenu.APPLICATION_NAME, getMessageString(), TrayIcon.MessageType.NONE ); }


	public static final Image getTrayIconImage() { return new ImageIcon( ClassLoader.getSystemResource( TRAY_ICON_IMAGE ) ).getImage(); }


	private String getMessageString() { return Message.getMessageString( new GregorianCalendar(), false ); }


    public void mouseDragged( MouseEvent mouseEvent ) { /** Do nothing. */ }


	public void mouseMoved( MouseEvent mouseEvent ) 
	{ 
		if( Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_SHOW_TIMES_IN_TOOL_TIP, true ) )
			setToolTip( getMessageString() );
		else
			setToolTip( MESSAGE_TOOL_TIP );
	}


    public void mousePressed( MouseEvent mouseEvent )
	{   	
    	if( m_popupMenu.popupIsDisabled() )
    	{
    		if( mouseEvent.getButton() == MouseEvent.BUTTON1 || mouseEvent.getButton() == MouseEvent.BUTTON2 || mouseEvent.getButton() == MouseEvent.BUTTON3 )
    		{
    			setPopupMenu( null );
    			m_popupMenu.getCurrentDialog().toFront();
    		}
    	}
    	else if( mouseEvent.getButton() == MouseEvent.BUTTON1 )
		{
    		( (TrayIcon)mouseEvent.getSource() ).displayMessage( PopupMenu.APPLICATION_NAME, getMessageString(), TrayIcon.MessageType.NONE );
		}
		else if( mouseEvent.getButton() == MouseEvent.BUTTON3 )
		{
			setPopupMenu( m_popupMenu );
		}
	}


	public void mouseClicked( MouseEvent mouseEvent ) { /** Do nothing. */ }


	public void mouseEntered( MouseEvent mouseEvent ) { /** Do nothing. */ }


	public void mouseExited( MouseEvent mouseEvent ) { /** Do nothing. */ }


	public void mouseReleased( MouseEvent mouseEvent ) { /** Do nothing. */ }
}
