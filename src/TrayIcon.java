import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.GregorianCalendar;

import javax.swing.ImageIcon;


public class TrayIcon extends java.awt.TrayIcon implements MouseListener, MouseMotionListener
{
	private static String APPLICATION_ICON_IMAGE = "worldtimesystemtray16x16.gif";  //$NON-NLS-1$
	private static String TRAY_ICON_IMAGE = SystemStart.isMicrosoftWindows() ? "worldtimesystemtray16x16.gif" : "worldtimesystemtray20x20.gif";  //$NON-NLS-1$ //$NON-NLS-2$
	private static final String MESSAGE_TOOL_TIP = Messages.getString( "TrayIcon.1" );  //$NON-NLS-1$

	private static final PopupMenu ms_popupMenu = new PopupMenu();


	private TrayIcon()
	{
		super( TrayIcon.getTrayIconImage(), null, ms_popupMenu );
		
		setImageAutoSize( true );
		addMouseListener( this );
		addMouseMotionListener( this );
		setImageAutoSize( false );
	}


	public static TrayIcon createTrayIcon() { return new TrayIcon(); }


	public void displayStartupBalloon() { displayMessage( PopupMenu.APPLICATION_NAME, getMessageString(), TrayIcon.MessageType.NONE ); }


	public static final Image getTrayIconImage() { return new ImageIcon( ClassLoader.getSystemResource( TRAY_ICON_IMAGE ) ).getImage(); }


	public static final Image getApplicationIconImage() { return new ImageIcon( ClassLoader.getSystemResource( APPLICATION_ICON_IMAGE ) ).getImage(); }


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
    	if( mouseEvent.getButton() == MouseEvent.BUTTON1 )
    	{
    		( (TrayIcon)mouseEvent.getSource() ).displayMessage( PopupMenu.APPLICATION_NAME, getMessageString(), TrayIcon.MessageType.NONE );
    	}
    	else if( mouseEvent.getButton() == MouseEvent.BUTTON3 && SystemStart.isMicrosoftWindows() )
    	{
    		// To block the right click action we check here if the right mouse button is clicked.
    		// If a dialog is already showing, then we don't want to show the popup.
    		// This only works for Microsoft Windows...we have another "hack" in PopupMenu::show().
    		if( ms_popupMenu.isPopupDisabled() )
    			setPopupMenu( null );
    		else
    			setPopupMenu( ms_popupMenu );
    	}
	}


	public void mouseClicked( MouseEvent mouseEvent ) { /** Do nothing. */ }


	public void mouseEntered( MouseEvent mouseEvent ) { /** Do nothing. */ }


	public void mouseExited( MouseEvent mouseEvent ) { /** Do nothing. */ }


	public void mouseReleased( MouseEvent mouseEvent ) { /** Do nothing. */ }
}
