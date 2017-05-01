import java.awt.CheckboxMenuItem;
import java.awt.Component;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.MessageFormat;

import javax.swing.JOptionPane;


public class PopupMenu extends java.awt.PopupMenu implements ActionListener, ItemListener
{
	private static final long serialVersionUID = 1L;

	static final String POPUP_ABOUT = Messages.getString( "PopupMenu.0" );  //$NON-NLS-1$
	static final String POPUP_EXIT = Messages.getString( "PopupMenu.1" );  //$NON-NLS-1$
	static final String POPUP_CHRONOLOGY = Messages.getString( "PopupMenu.2" );  //$NON-NLS-1$
	static final String POPUP_DATE_FORMAT = Messages.getString( "PopupMenu.3" );  //$NON-NLS-1$
	static final String POPUP_RUN_ON_SYSTEM_START = Messages.getString( "PopupMenu.4" );  //$NON-NLS-1$
	static final String POPUP_SHOW_CALENDARS = Messages.getString( "PopupMenu.27" );  //$NON-NLS-1$
	static final String POPUP_SHOW_STARDATE_ISSUE = Messages.getString( "PopupMenu.5" );  //$NON-NLS-1$
	static final String POPUP_PAD_STARDATE = Messages.getString( "PopupMenu.26" );  //$NON-NLS-1$
	static final String POPUP_SHOW_STARDATE_CLASSIC = Messages.getString( "PopupMenu.24" );  //$NON-NLS-1$

	static final String SUBPOPUP_SHOW_MESSAGE_BUDDHIST = Messages.getString( "PopupMenu.6" );  //$NON-NLS-1$
	static final String SUBPOPUP_SHOW_MESSAGE_COPTIC = Messages.getString( "PopupMenu.7" );  //$NON-NLS-1$
	static final String SUBPOPUP_SHOW_MESSAGE_ETHIOPIC = Messages.getString( "PopupMenu.8" );  //$NON-NLS-1$
	static final String SUBPOPUP_SHOW_MESSAGE_GREGORIAN = Messages.getString( "PopupMenu.9" );  //$NON-NLS-1$
	static final String SUBPOPUP_SHOW_MESSAGE_GREGORIAN_JULIAN = Messages.getString( "PopupMenu.10" );  //$NON-NLS-1$
	static final String SUBPOPUP_SHOW_MESSAGE_ISLAMIC = Messages.getString( "PopupMenu.11" );  //$NON-NLS-1$
	static final String SUBPOPUP_SHOW_MESSAGE_ISO8601 = Messages.getString( "PopupMenu.12" );  //$NON-NLS-1$
	static final String SUBPOPUP_SHOW_MESSAGE_JULIAN = Messages.getString( "PopupMenu.13" );  //$NON-NLS-1$
	static final String SUBPOPUP_SHOW_MESSAGE_STARDATE = Messages.getString( "PopupMenu.14" );  //$NON-NLS-1$

	static final String SUBPOPUP_DATE_TIME_FORMAT_LONG = Messages.getString( "PopupMenu.15" );  //$NON-NLS-1$
	static final String SUBPOPUP_DATE_TIME_FORMAT_MEDIUM = Messages.getString( "PopupMenu.16" );  //$NON-NLS-1$
	static final String SUBPOPUP_DATE_TIME_FORMAT_SHORT = Messages.getString( "PopupMenu.17" );  //$NON-NLS-1$

	private static final String CREDIT_ALGORITHM_LINE1 = Messages.getString( "PopupMenu.21" );  //$NON-NLS-1$
	private static final String CREDIT_ALGORITHM_LINE2 = Messages.getString( "PopupMenu.22" );  //$NON-NLS-1$
	private static final String CREDIT_CHRONOLOGY = Messages.getString( "PopupMenu.23" );  //$NON-NLS-1$
	private static final String CREDIT_NSIS = Messages.getString( "PopupMenu.25" );  //$NON-NLS-1$

    private CheckboxMenuItem m_checkboxMenuItemDateFormatLong = null;
    private CheckboxMenuItem m_checkboxMenuItemDateFormatMedium = null;
    private CheckboxMenuItem m_checkboxMenuItemDateFormatShort = null;

    private static boolean ms_popupDisabled = false;

    
    public PopupMenu()
	{
    	super();
    	
	    MenuItem menuItem = null;

	    menuItem = new MenuItem( POPUP_ABOUT );
	    menuItem.addActionListener( this );
	    add( menuItem );

	    java.awt.PopupMenu subpopup = new java.awt.PopupMenu();
	    subpopup.setLabel( POPUP_CHRONOLOGY );

	    CheckboxMenuItem checkboxMenuItem = null;

	    checkboxMenuItem = new CheckboxMenuItem( SUBPOPUP_SHOW_MESSAGE_BUDDHIST, Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_CHRONOLOGY_BUDDHIST, true ) );
	    checkboxMenuItem.addItemListener( this );
	    subpopup.add( checkboxMenuItem );

	    checkboxMenuItem = new CheckboxMenuItem( SUBPOPUP_SHOW_MESSAGE_COPTIC, Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_CHRONOLOGY_COPTIC, true ) );
	    checkboxMenuItem.addItemListener( this );
	    subpopup.add( checkboxMenuItem );

	    checkboxMenuItem = new CheckboxMenuItem( SUBPOPUP_SHOW_MESSAGE_ETHIOPIC, Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_CHRONOLOGY_ETHIOPIC, true ) );
	    checkboxMenuItem.addItemListener( this );
	    subpopup.add( checkboxMenuItem );

	    checkboxMenuItem = new CheckboxMenuItem( SUBPOPUP_SHOW_MESSAGE_GREGORIAN, Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_CHRONOLOGY_GREGORIAN, true ) );
	    checkboxMenuItem.addItemListener( this );
	    subpopup.add( checkboxMenuItem );

	    checkboxMenuItem = new CheckboxMenuItem( SUBPOPUP_SHOW_MESSAGE_GREGORIAN_JULIAN, Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_CHRONOLOGY_GREGORIAN_JULIAN, true ) );
	    checkboxMenuItem.addItemListener( this );
	    subpopup.add( checkboxMenuItem );

	    checkboxMenuItem = new CheckboxMenuItem( SUBPOPUP_SHOW_MESSAGE_ISLAMIC, Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_CHRONOLOGY_ISLAMIC, true ) );
	    checkboxMenuItem.addItemListener( this );
	    subpopup.add( checkboxMenuItem );

	    checkboxMenuItem = new CheckboxMenuItem( SUBPOPUP_SHOW_MESSAGE_ISO8601, Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_CHRONOLOGY_ISO8601, true ) );
	    checkboxMenuItem.addItemListener( this );
	    subpopup.add( checkboxMenuItem );

	    checkboxMenuItem = new CheckboxMenuItem( SUBPOPUP_SHOW_MESSAGE_JULIAN, Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_CHRONOLOGY_JULIAN, true ) );
	    checkboxMenuItem.addItemListener( this );
	    subpopup.add( checkboxMenuItem );

	    checkboxMenuItem = new CheckboxMenuItem( SUBPOPUP_SHOW_MESSAGE_STARDATE, Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_CHRONOLOGY_STARDATE, true ) );
	    checkboxMenuItem.addItemListener( this );
	    subpopup.add( checkboxMenuItem );

	    add( subpopup );

	    subpopup = new java.awt.PopupMenu();
	    subpopup.setLabel( POPUP_DATE_FORMAT );

	    String dateTimeFormat = Properties.getInstance().getProperty( Properties.PROPERTY_DATE_FORMAT, Properties.PROPERTY_DATE_FORMAT_LONG );

	    m_checkboxMenuItemDateFormatLong = new CheckboxMenuItem( SUBPOPUP_DATE_TIME_FORMAT_LONG, dateTimeFormat.equalsIgnoreCase( Properties.PROPERTY_DATE_FORMAT_LONG ) );
	    m_checkboxMenuItemDateFormatLong.addItemListener( this );
	    subpopup.add( m_checkboxMenuItemDateFormatLong );

	    m_checkboxMenuItemDateFormatMedium = new CheckboxMenuItem( SUBPOPUP_DATE_TIME_FORMAT_MEDIUM, dateTimeFormat.equalsIgnoreCase( Properties.PROPERTY_DATE_FORMAT_MEDIUM) );
	    m_checkboxMenuItemDateFormatMedium.addItemListener( this );
	    subpopup.add( m_checkboxMenuItemDateFormatMedium );

	    m_checkboxMenuItemDateFormatShort = new CheckboxMenuItem( SUBPOPUP_DATE_TIME_FORMAT_SHORT, dateTimeFormat.equalsIgnoreCase( Properties.PROPERTY_DATE_FORMAT_SHORT ) );
	    m_checkboxMenuItemDateFormatShort.addItemListener( this );
	    subpopup.add( m_checkboxMenuItemDateFormatShort );

	    boolean validFormat = 
	    	dateTimeFormat.equalsIgnoreCase( Properties.PROPERTY_DATE_FORMAT_LONG ) ||
	    	dateTimeFormat.equalsIgnoreCase( Properties.PROPERTY_DATE_FORMAT_MEDIUM ) ||
	    	dateTimeFormat.equalsIgnoreCase( Properties.PROPERTY_DATE_FORMAT_SHORT );
	    
	    if( ! validFormat )
		    m_checkboxMenuItemDateFormatLong.setState( true );
	    
	    add( subpopup );

	    if( OperatingSystem.isWindows() )
	    {
		    checkboxMenuItem = new CheckboxMenuItem( POPUP_RUN_ON_SYSTEM_START, SystemStart.runOnSystemStart() );
		    checkboxMenuItem.addItemListener( this );
		    add( checkboxMenuItem );
	    }

	    boolean padStardate = Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_PAD_STARDATE, true );
	    checkboxMenuItem = new CheckboxMenuItem( POPUP_PAD_STARDATE, padStardate );
	    checkboxMenuItem.addItemListener( this );
	    add( checkboxMenuItem );

	    boolean showStardateIssue = Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_SHOW_STARDATE_ISSUE, true );
	    checkboxMenuItem = new CheckboxMenuItem( POPUP_SHOW_STARDATE_ISSUE, showStardateIssue );
	    checkboxMenuItem.addItemListener( this );
	    add( checkboxMenuItem );

	    boolean showStardateClassic= Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_SHOW_STARDATE_CLASSIC, true );
	    checkboxMenuItem = new CheckboxMenuItem( POPUP_SHOW_STARDATE_CLASSIC, showStardateClassic );
	    checkboxMenuItem.addItemListener( this );
	    add( checkboxMenuItem );

	    menuItem = new MenuItem( POPUP_SHOW_CALENDARS );
	    menuItem.addActionListener( this );
	    add( menuItem );

	    menuItem = new MenuItem( POPUP_EXIT );
	    menuItem.addActionListener( this );
	    add( menuItem );
	}


    public static boolean isPopupDisabled() { return ms_popupDisabled; }


    @Override public void show( Component origin, int x, int y ) 
    {
		// To block the right click action we check here if the right mouse button is clicked.
		// If a dialog is already showing, then we don't want to show the popup.
		// This method is not called when executing on Microsoft Windows...it only works here for Linux...or non-Windows.
    	if( ! isPopupDisabled() )
    		super.show(origin, x, y);
	}


    @Override public void actionPerformed( ActionEvent actionEvent )
	{
    	boolean systemExit = false;

    	String menuItem = ( (MenuItem)actionEvent.getSource() ).getLabel();
    	if( POPUP_ABOUT.equals( menuItem ) )
    	{
    		String message =
				"<html><center><br>" +  //$NON-NLS-1$
        		"<h2>" + StardateSystemTray.APPLICATION_NAME + "</h2><br><br>" + //$NON-NLS-1$ //$NON-NLS-2$
        		new MessageFormat( Messages.getString("PopupMenu.19") ).format( new Object[] { StardateSystemTray.APPLICATION_VERSION_NUMBER } ) +"<br>" + //$NON-NLS-1$ //$NON-NLS-2$
        		"<a href='" + StardateSystemTray.APPLICATION_URL + "'>" + StardateSystemTray.APPLICATION_URL + "</a><br><br><br>" + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        		"<u>" + Messages.getString("PopupMenu.30") + "</u><br><br>" + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        		StardateSystemTray.APPLICATION_AUTHOR + "<br><br>" + //$NON-NLS-1$
				CREDIT_ALGORITHM_LINE1 + "<br>\"" + CREDIT_ALGORITHM_LINE2 + "\"<br><br>" +    //$NON-NLS-1$ //$NON-NLS-2$
				CREDIT_CHRONOLOGY + "<br><br>" +   //$NON-NLS-1$
				( OperatingSystem.isWindows() ? CREDIT_NSIS + "<br><br>" : "" ) + //$NON-NLS-1$ //$NON-NLS-2$
				"</center></html>";  //$NON-NLS-1$

    		MessageDialog.showMessageDialog( POPUP_ABOUT, MessageDialog.createURLLabel( message ), JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION );
	        ms_popupDisabled = false;
    	}
    	else if( POPUP_SHOW_CALENDARS.equals( menuItem ) )
    	{
        	if( TrayIcon.getMessageString( false ).length() == 0 )
        		MessageDialog.showMessageDialog( Messages.getString( "PopupMenu.28" ), Messages.getString( "PopupMenu.29" ), JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION ); //$NON-NLS-1$ //$NON-NLS-2$
        	else
        	{
	    		ms_popupDisabled = true;
	        	ShowCalendars.create();
	    		ms_popupDisabled = false;
        	}
    	}
    	else if( POPUP_EXIT.equals( menuItem ) )
    	{
    		Properties.getInstance().store();
    		systemExit = true;
    	}

    	if( systemExit )
    		System.exit( 0 );
	}


	@Override public void itemStateChanged( ItemEvent itemEvent )
    {
    	CheckboxMenuItem checkboxMenuItem = (CheckboxMenuItem)itemEvent.getSource();
    	String label = checkboxMenuItem.getLabel();

    	// Toggle the show/hide Stardate ISSUE.
    	if( POPUP_SHOW_STARDATE_ISSUE.equals( label ) )
    		Properties.getInstance().setProperty( Properties.PROPERTY_SHOW_STARDATE_ISSUE, Boolean.toString( checkboxMenuItem.getState() ) );

    	// Toggle the pad Stardate.
    	else if( POPUP_PAD_STARDATE.equals( label ) )
    		Properties.getInstance().setProperty( Properties.PROPERTY_PAD_STARDATE, Boolean.toString( checkboxMenuItem.getState() ) );

    	// Toggle the show 'classic' Stardate.
    	else if( POPUP_SHOW_STARDATE_CLASSIC.equals( label ) )
    		Properties.getInstance().setProperty( Properties.PROPERTY_SHOW_STARDATE_CLASSIC, Boolean.toString( checkboxMenuItem.getState() ) );

    	// Toggle the run on system start option.
    	else if( POPUP_RUN_ON_SYSTEM_START.equals( label ) )
    		if( ! SystemStart.setRunOnSystemStart( checkboxMenuItem.getState() ) )
    			MessageDialog.showMessageDialog( Messages.getString( "PopupMenu.31" ), Messages.getString( "PopupMenu.32" ), JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION );  //$NON-NLS-1$//$NON-NLS-2$

    	// Handle each chronology.
    	else if( SUBPOPUP_SHOW_MESSAGE_BUDDHIST.equals( label ) )
    		Properties.getInstance().setProperty( Properties.PROPERTY_CHRONOLOGY_BUDDHIST, Boolean.toString( checkboxMenuItem.getState() ) );
    	else if( SUBPOPUP_SHOW_MESSAGE_COPTIC.equals( label ) )
    		Properties.getInstance().setProperty( Properties.PROPERTY_CHRONOLOGY_COPTIC, Boolean.toString( checkboxMenuItem.getState() ) );
    	else if( SUBPOPUP_SHOW_MESSAGE_ETHIOPIC.equals( label ) )
    		Properties.getInstance().setProperty( Properties.PROPERTY_CHRONOLOGY_ETHIOPIC, Boolean.toString( checkboxMenuItem.getState() ) );
    	else if( SUBPOPUP_SHOW_MESSAGE_GREGORIAN.equals( label ) )
    		Properties.getInstance().setProperty( Properties.PROPERTY_CHRONOLOGY_GREGORIAN, Boolean.toString( checkboxMenuItem.getState() ) );
    	else if( SUBPOPUP_SHOW_MESSAGE_GREGORIAN_JULIAN.equals( label ) )
    		Properties.getInstance().setProperty( Properties.PROPERTY_CHRONOLOGY_GREGORIAN_JULIAN, Boolean.toString( checkboxMenuItem.getState() ) );
    	else if( SUBPOPUP_SHOW_MESSAGE_ISLAMIC.equals( label ) )
    		Properties.getInstance().setProperty( Properties.PROPERTY_CHRONOLOGY_ISLAMIC, Boolean.toString( checkboxMenuItem.getState() ) );
    	else if( SUBPOPUP_SHOW_MESSAGE_ISO8601.equals( label ) )
    		Properties.getInstance().setProperty( Properties.PROPERTY_CHRONOLOGY_ISO8601, Boolean.toString( checkboxMenuItem.getState() ) );
    	else if( SUBPOPUP_SHOW_MESSAGE_JULIAN.equals( label ) )
    		Properties.getInstance().setProperty( Properties.PROPERTY_CHRONOLOGY_JULIAN, Boolean.toString( checkboxMenuItem.getState() ) );
    	else if( SUBPOPUP_SHOW_MESSAGE_STARDATE.equals( label ) )
    		Properties.getInstance().setProperty( Properties.PROPERTY_CHRONOLOGY_STARDATE, Boolean.toString( checkboxMenuItem.getState() ) );

    	// Handle each date format.
    	else if( SUBPOPUP_DATE_TIME_FORMAT_LONG.equals( label ) )
    	{
    		m_checkboxMenuItemDateFormatLong.setState( true );
    		m_checkboxMenuItemDateFormatMedium.setState( false );
    		m_checkboxMenuItemDateFormatShort.setState( false );

    		Properties.getInstance().setProperty( Properties.PROPERTY_DATE_FORMAT, Properties.PROPERTY_DATE_FORMAT_LONG );
    	}
    	else if( SUBPOPUP_DATE_TIME_FORMAT_MEDIUM.equals( label ) )
    	{
    		m_checkboxMenuItemDateFormatLong.setState( false );
    		m_checkboxMenuItemDateFormatMedium.setState( true );
    		m_checkboxMenuItemDateFormatShort.setState( false );

    		Properties.getInstance().setProperty( Properties.PROPERTY_DATE_FORMAT, Properties.PROPERTY_DATE_FORMAT_MEDIUM );
    	}
    	else if( SUBPOPUP_DATE_TIME_FORMAT_SHORT.equals( label ) )
    	{
    		m_checkboxMenuItemDateFormatLong.setState( false );
    		m_checkboxMenuItemDateFormatMedium.setState( false );
    		m_checkboxMenuItemDateFormatShort.setState( true );

    		Properties.getInstance().setProperty( Properties.PROPERTY_DATE_FORMAT, Properties.PROPERTY_DATE_FORMAT_SHORT );
    	}

    	Properties.getInstance().store();
    }
}