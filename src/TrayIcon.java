import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.GregorianCalendar;

import javax.swing.ImageIcon;

import org.joda.time.DateTime;
import org.joda.time.chrono.BuddhistChronology;
import org.joda.time.chrono.CopticChronology;
import org.joda.time.chrono.EthiopicChronology;
import org.joda.time.chrono.GJChronology;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.chrono.IslamicChronology;
import org.joda.time.chrono.JulianChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


public class TrayIcon extends java.awt.TrayIcon implements MouseListener, MouseMotionListener
{
	private static final String TRAY_ICON_IMAGE = "trayicon.gif"; 

	private static final String MESSAGE_NO_CHRONOLOGIES = Messages.getString( "TrayIcon.1" ); 
	private static final String MESSAGE_BUDDHIST = Messages.getString( "TrayIcon.2" ); 
	private static final String MESSAGE_COPTIC = Messages.getString( "TrayIcon.3" ); 
	private static final String MESSAGE_ETHIOPIC = Messages.getString( "TrayIcon.4" ); 
	private static final String MESSAGE_GREGORIAN = Messages.getString( "TrayIcon.5" ); 
	private static final String MESSAGE_GREGORIAN_JULIAN = Messages.getString( "TrayIcon.6" ); 
	private static final String MESSAGE_ISLAMIC = Messages.getString( "TrayIcon.7" ); 
	private static final String MESSAGE_ISO8601 = Messages.getString( "TrayIcon.8" ); 
	private static final String MESSAGE_JULIAN = Messages.getString( "TrayIcon.9" ); 
	private static final String MESSAGE_STARDATE = Messages.getString( "TrayIcon.10" ); 

	private static final String MESSAGE_TOOL_TIP = Messages.getString( "TrayIcon.11" ); 

	private PopupMenu m_popupMenu = null;

	
	private TrayIcon( PopupMenu popupMenu )
	{
		super( getTrayIconImage(), null, popupMenu );

		setImageAutoSize( true );
		addMouseListener( this );
		addMouseMotionListener( this );
		m_popupMenu = popupMenu;
	}


	public static final Image getTrayIconImage() { return new ImageIcon( ClassLoader.getSystemResource( TRAY_ICON_IMAGE ) ).getImage(); }

	
	public static TrayIcon createTrayIcon( PopupMenu popupMenu ) { return new TrayIcon( popupMenu ); }

	
	public void displayStartupBalloon() { displayMessage( PopupMenu.APPLICATION_NAME, getMessageString(), TrayIcon.MessageType.NONE ); }


    private String getMessageString()
    {
    	StringBuilder message = new StringBuilder();
    	DateTime currentDateTime = new DateTime();
    	DateTime conversionDateTime = null;

    	if( Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_CHRONOLOGY_BUDDHIST, true ) )
    	{
        	conversionDateTime = currentDateTime.withChronology( BuddhistChronology.getInstance() );
        	message.append( message.length() > 0 ? "\n" : ""); 
    		message.append( MESSAGE_BUDDHIST ).append( conversionDateTime.toString( getDateTimeFormatter() ) );
    	}

    	if( Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_CHRONOLOGY_COPTIC, true ) )
    	{
        	conversionDateTime = currentDateTime.withChronology( CopticChronology.getInstance() );
        	message.append( message.length() > 0 ? "\n" : ""); 
    		message.append( MESSAGE_COPTIC ).append( conversionDateTime.toString( getDateTimeFormatter() ) );
    	}


    	if( Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_CHRONOLOGY_ETHIOPIC, true ) )
    	{
        	conversionDateTime = currentDateTime.withChronology( EthiopicChronology.getInstance() );
        	message.append( message.length() > 0 ? "\n" : ""); 
    		message.append( MESSAGE_ETHIOPIC ).append( conversionDateTime.toString( getDateTimeFormatter() ) );
    	}

    	if( Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_CHRONOLOGY_GREGORIAN, true ) )
    	{
        	conversionDateTime = currentDateTime.withChronology( GregorianChronology.getInstance() );
        	message.append( message.length() > 0 ? "\n" : ""); 
    		message.append( MESSAGE_GREGORIAN ).append( conversionDateTime.toString( getDateTimeFormatter() ) );
    	}

    	if( Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_CHRONOLOGY_GREGORIAN_JULIAN, true ) )
    	{
        	conversionDateTime = currentDateTime.withChronology( GJChronology.getInstance() );
        	message.append( message.length() > 0 ? "\n" : ""); 
    		message.append( MESSAGE_GREGORIAN_JULIAN ).append( conversionDateTime.toString( getDateTimeFormatter() ) );
    	}

    	if( Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_CHRONOLOGY_ISLAMIC, true ) )
    	{
        	conversionDateTime = currentDateTime.withChronology( IslamicChronology.getInstance() );
        	message.append( message.length() > 0 ? "\n" : ""); 
    		message.append( MESSAGE_ISLAMIC ).append( conversionDateTime.toString( getDateTimeFormatter() ) );
    	}

    	if( Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_CHRONOLOGY_ISO8601, true ) )
    	{
        	conversionDateTime = currentDateTime.withChronology( ISOChronology.getInstance() );
        	message.append( message.length() > 0 ? "\n" : ""); 
    		message.append( MESSAGE_ISO8601 ).append( conversionDateTime.toString( getDateTimeFormatter() ) );
    	}

    	if( Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_CHRONOLOGY_JULIAN, true ) )
    	{
        	conversionDateTime = currentDateTime.withChronology( JulianChronology.getInstance() );
        	message.append( message.length() > 0 ? "\n" : ""); 
    		message.append( MESSAGE_JULIAN ).append( conversionDateTime.toString( getDateTimeFormatter() ) );
    	}

    	if( Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_CHRONOLOGY_STARDATE, true ) )
    	{
    		Stardate stardate = new Stardate();
    		stardate.setGregorian( currentDateTime.toGregorianCalendar() );
        	message.append( message.length() > 0 ? "\n" : ""); 
    		message.append( MESSAGE_STARDATE ).append( stardate.toStardateString() );
    	}

    	if( message.length() == 0 )
    		message = new StringBuilder( MESSAGE_NO_CHRONOLOGIES );

    	return message.toString();
    }
    
    
	public void mouseDragged( MouseEvent mouseEvent ) { }


	public void mouseMoved( MouseEvent mouseEvent )
	{
		Stardate stardate = new Stardate();
		stardate.setGregorian(  new GregorianCalendar() );

		boolean showStardateIssue = 
			Properties.getInstance().getPropertyBoolean
			( 
				Properties.PROPERTY_SHOW_STARDATE_ISSUE, 
				Properties.PROPERTY_SHOW_STARDATE_ISSUE_DEFAULT_VALUE 
			);
		
		if( showStardateIssue )
			setToolTip( MESSAGE_TOOL_TIP + stardate.toStardateString() );
		else
			setToolTip( MESSAGE_TOOL_TIP + stardate.getStardateInteger() + "." + stardate.getStardateFraction() ); 
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


	private DateTimeFormatter getDateTimeFormatter()
    {
		DateTimeFormatter dateTimeFormatter = DateTimeFormat.longDate(); // Default.
		String dateFormat = Properties.getInstance().getProperty( Properties.PROPERTY_DATE_FORMAT, Properties.PROPERTY_DATE_FORMAT_LONG );
	   
	    if( dateFormat.equalsIgnoreCase( Properties.PROPERTY_DATE_FORMAT_LONG ) )
	    	dateTimeFormatter =  DateTimeFormat.longDate();
	    else if( dateFormat.equalsIgnoreCase( Properties.PROPERTY_DATE_FORMAT_MEDIUM ) )
	    	dateTimeFormatter =  DateTimeFormat.mediumDate();
	    else if( dateFormat.equalsIgnoreCase( Properties.PROPERTY_DATE_FORMAT_SHORT ) )
	    	dateTimeFormatter =  DateTimeFormat.shortDate();
	    
	    return dateTimeFormatter;
    }
}
