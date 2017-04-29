import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.GregorianCalendar;

import javax.swing.ImageIcon;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
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
	private static String APPLICATION_ICON_IMAGE = "stardatesystemtray16x16.gif";  //$NON-NLS-1$
	private static String TRAY_ICON_IMAGE = OperatingSystem.isWindows() ? "stardatesystemtray16x16.gif" : "stardatesystemtray20x20.gif";  //$NON-NLS-1$ //$NON-NLS-2$

	private static final String MESSAGE_NO_CHRONOLOGIES = Messages.getString( "TrayIcon.1" );  //$NON-NLS-1$
	private static final String MESSAGE_BUDDHIST = Messages.getString( "TrayIcon.2" );  //$NON-NLS-1$
	private static final String MESSAGE_COPTIC = Messages.getString( "TrayIcon.3" );  //$NON-NLS-1$
	private static final String MESSAGE_ETHIOPIC = Messages.getString( "TrayIcon.4" );  //$NON-NLS-1$
	private static final String MESSAGE_GREGORIAN = Messages.getString( "TrayIcon.5" );  //$NON-NLS-1$
	private static final String MESSAGE_GREGORIAN_JULIAN = Messages.getString( "TrayIcon.6" );  //$NON-NLS-1$
	private static final String MESSAGE_ISLAMIC = Messages.getString( "TrayIcon.7" );  //$NON-NLS-1$
	private static final String MESSAGE_ISO8601 = Messages.getString( "TrayIcon.8" );  //$NON-NLS-1$
	private static final String MESSAGE_JULIAN = Messages.getString( "TrayIcon.9" );  //$NON-NLS-1$
	private static final String MESSAGE_STARDATE = Messages.getString( "TrayIcon.10" );  //$NON-NLS-1$

	private static final String MESSAGE_TOOL_TIP = Messages.getString( "TrayIcon.11" );  //$NON-NLS-1$

	private static final PopupMenu ms_popupMenu = new PopupMenu();


	private TrayIcon()
	{
		super( TrayIcon.getTrayIconImage(), null, ms_popupMenu );

		setImageAutoSize( true );
		addMouseListener( this );
		addMouseMotionListener( this );
		setImageAutoSize( false );
	}


	public static final Image getTrayIconImage() { return new ImageIcon( ClassLoader.getSystemResource( TRAY_ICON_IMAGE ) ).getImage(); }


	public static final Image getApplicationIconImage() { return new ImageIcon( ClassLoader.getSystemResource( APPLICATION_ICON_IMAGE ) ).getImage(); }


	public static TrayIcon createTrayIcon() { return new TrayIcon(); }


	public void displayStartupBalloon() 
	{
		String messageString = getMessageString( false );
		if( messageString.length() == 0 )
			displayMessage( StardateSystemTray.APPLICATION_NAME, MESSAGE_NO_CHRONOLOGIES, TrayIcon.MessageType.NONE ); 
		else
			displayMessage( StardateSystemTray.APPLICATION_NAME, messageString, TrayIcon.MessageType.NONE ); 
	}


    public static String getMessageString( boolean html )
    {
    	StringBuilder message = new StringBuilder();
    	DateTime currentDateTime = new DateTime( DateTimeZone.UTC );
    	DateTime conversionDateTime = null;

		String newLine = html ? "<br>" : System.getProperty( "line.separator" ); //$NON-NLS-1$ //$NON-NLS-2$

    	if( Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_CHRONOLOGY_BUDDHIST, true ) )
    	{
        	conversionDateTime = currentDateTime.withChronology( BuddhistChronology.getInstance() );
        	message.append( message.length() > 0 ? newLine : "" );  //$NON-NLS-1$
    		message.append( MESSAGE_BUDDHIST ).append( conversionDateTime.toString( getDateTimeFormatter() ) );
    	}

    	if( Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_CHRONOLOGY_COPTIC, true ) )
    	{
        	conversionDateTime = currentDateTime.withChronology( CopticChronology.getInstance() );
        	message.append( message.length() > 0 ? newLine : "" );   //$NON-NLS-1$
    		message.append( MESSAGE_COPTIC ).append( conversionDateTime.toString( getDateTimeFormatter() ) );
    	}

    	if( Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_CHRONOLOGY_ETHIOPIC, true ) )
    	{
        	conversionDateTime = currentDateTime.withChronology( EthiopicChronology.getInstance() );
        	message.append( message.length() > 0 ? newLine : "" );  //$NON-NLS-1$
    		message.append( MESSAGE_ETHIOPIC ).append( conversionDateTime.toString( getDateTimeFormatter() ) );
    	}

    	if( Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_CHRONOLOGY_GREGORIAN, true ) )
    	{
        	conversionDateTime = currentDateTime.withChronology( GregorianChronology.getInstance() );
        	message.append( message.length() > 0 ? newLine : "" );   //$NON-NLS-1$
    		message.append( MESSAGE_GREGORIAN ).append( conversionDateTime.toString( getDateTimeFormatter() ) );
    	}

    	if( Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_CHRONOLOGY_GREGORIAN_JULIAN, true ) )
    	{
        	conversionDateTime = currentDateTime.withChronology( GJChronology.getInstance() );
        	message.append( message.length() > 0 ? newLine : "" );   //$NON-NLS-1$
    		message.append( MESSAGE_GREGORIAN_JULIAN ).append( conversionDateTime.toString( getDateTimeFormatter() ) );
    	}

    	if( Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_CHRONOLOGY_ISLAMIC, true ) )
    	{
        	conversionDateTime = currentDateTime.withChronology( IslamicChronology.getInstance() );
        	message.append( message.length() > 0 ? newLine : "" );   //$NON-NLS-1$
    		message.append( MESSAGE_ISLAMIC ).append( conversionDateTime.toString( getDateTimeFormatter() ) );
    	}

    	if( Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_CHRONOLOGY_ISO8601, true ) )
    	{
        	conversionDateTime = currentDateTime.withChronology( ISOChronology.getInstance() );
        	message.append( message.length() > 0 ? newLine : "" );   //$NON-NLS-1$
    		message.append( MESSAGE_ISO8601 ).append( conversionDateTime.toString( getDateTimeFormatter() ) );
    	}

    	if( Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_CHRONOLOGY_JULIAN, true ) )
    	{
        	conversionDateTime = currentDateTime.withChronology( JulianChronology.getInstance() );
        	message.append( message.length() > 0 ? newLine : "" );   //$NON-NLS-1$
    		message.append( MESSAGE_JULIAN ).append( conversionDateTime.toString( getDateTimeFormatter() ) );
    	}

    	if( Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_CHRONOLOGY_STARDATE, true ) )
    	{
        	message.append( message.length() > 0 ? newLine : "" );  //$NON-NLS-1$ 
    		message.append( MESSAGE_STARDATE ).append( toStardateString( currentDateTime.toGregorianCalendar() ) );
    	}

		if( html )
		{
			message.insert( 0, "<html>" ); //$NON-NLS-1$
			message.append( "</html>");  //$NON-NLS-1$
		}

    	return message.toString();
    }


	@Override public void mouseDragged( MouseEvent mouseEvent ) { /** Do nothing. */ }


	@Override public void mouseMoved( MouseEvent mouseEvent )
	{
		setToolTip( MESSAGE_TOOL_TIP + toStardateString( new DateTime( DateTimeZone.UTC ).toGregorianCalendar() ) );
	}


	@Override public void mousePressed( MouseEvent mouseEvent )
	{
		if( mouseEvent.getButton() == MouseEvent.BUTTON1 )
		{
        	TrayIcon trayIcon = (TrayIcon)mouseEvent.getSource();
			String messageString = getMessageString( false );
			if( messageString.length() == 0 )
				trayIcon.displayMessage( StardateSystemTray.APPLICATION_NAME, MESSAGE_NO_CHRONOLOGIES, TrayIcon.MessageType.NONE );
			else
				trayIcon.displayMessage( StardateSystemTray.APPLICATION_NAME, messageString, TrayIcon.MessageType.NONE );
		}
    	if( mouseEvent.getButton() == MouseEvent.BUTTON3 && OperatingSystem.isWindows() )
    	{
    		// To block the right click action we check here if the right mouse button is clicked.
    		// If a dialog is already showing, then we don't want to show the popup.
    		// This only works for Microsoft Windows...we have another "hack" in PopupMenu::show().
    		if( PopupMenu.isPopupDisabled() )
    			setPopupMenu( null );
    		else
    			setPopupMenu( ms_popupMenu );
    	}
	}


	@Override public void mouseClicked( MouseEvent mouseEvent ) { /** Do nothing. */}


	@Override public void mouseEntered( MouseEvent mouseEvent ) { /** Do nothing. */}


	@Override public void mouseExited( MouseEvent mouseEvent ) { /** Do nothing. */}


	@Override public void mouseReleased( MouseEvent mouseEvent ) { /** Do nothing. */}


	private static DateTimeFormatter getDateTimeFormatter()
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


	private static String toStardateString( GregorianCalendar gregorianCalendar )
	{
    	String stardateString;
    	if( Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_SHOW_STARDATE_CLASSIC, true ) )
    	{
    		int[] issueIntegerFractionPeriod = Stardate.getStardateClassic( gregorianCalendar );
    		stardateString =
				Stardate.toStardateClassicString
				(
					issueIntegerFractionPeriod[ 0 ],
					issueIntegerFractionPeriod[ 1 ],
					issueIntegerFractionPeriod[ 2 ],
					Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_SHOW_STARDATE_ISSUE, true ),
					Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_PAD_STARDATE, true )
				);
    	}
    	else
    	{
    		int[] issueIntegerFraction = Stardate.getStardate2009Revised( gregorianCalendar );
    		stardateString = Stardate.toStardateRevised2009String( issueIntegerFraction[ 0 ], issueIntegerFraction[ 1 ] );
    	}

    	return stardateString;
	}
}