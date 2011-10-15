import java.awt.CheckboxMenuItem;
import java.awt.Component;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;


public class PopupMenu extends java.awt.PopupMenu implements ActionListener, ItemListener
{
	private static final long serialVersionUID = 1L;

	private static final String CREDIT_NSIS = Messages.getString( "PopupMenu.5" ); //$NON-NLS-1$

	private static final String POPUP_ABOUT = Messages.getString( "PopupMenu.6" ); //$NON-NLS-1$
	private static final String POPUP_ADD_REMOVE_TIME_ZONES = Messages.getString( "PopupMenu.7" ); //$NON-NLS-1$
	private static final String POPUP_RENAME_TIME_ZONES = Messages.getString( "PopupMenu.8" ); //$NON-NLS-1$
	private static final String POPUP_SHOW_DATE_TIME = Messages.getString( "PopupMenu.59" ); //$NON-NLS-1$
	private static final String POPUP_TIME_TRAVEL = Messages.getString( "PopupMenu.9" ); //$NON-NLS-1$
	private static final String POPUP_FORMAT = Messages.getString( "PopupMenu.10" ); //$NON-NLS-1$
	private static final String POPUP_COMBINE_TIME_ZONES = Messages.getString( "PopupMenu.51" ); //$NON-NLS-1$
	private static final String POPUP_SET_TIME_ZONE_SEPARATOR = Messages.getString( "PopupMenu.52" ); //$NON-NLS-1$
	private static final String POPUP_SET_LAYOUT = Messages.getString( "PopupMenu.53" ); //$NON-NLS-1$
	private static final String POPUP_SORT_DATE_TIME = Messages.getString( "PopupMenu.11" ); //$NON-NLS-1$
	private static final String POPUP_SHOW_TIMES_IN_TOOL_TIP = Messages.getString( "PopupMenu.12" ); //$NON-NLS-1$
	private static final String POPUP_SET_DIFFERENT_DAY_INDICATOR = Messages.getString( "PopupMenu.54" ); //$NON-NLS-1$
	private static final String POPUP_RUN_ON_SYSTEM_START = Messages.getString( "PopupMenu.14" ); //$NON-NLS-1$
	private static final String POPUP_EXIT = Messages.getString( "PopupMenu.15" ); //$NON-NLS-1$

	private static final String SUBPOPUP_FORMAT_DATE_AND_TIME_FULL = Messages.getString( "PopupMenu.16" ); //$NON-NLS-1$
	private static final String SUBPOPUP_FORMAT_DATE_AND_TIME_LONG = Messages.getString( "PopupMenu.17" ); //$NON-NLS-1$
	private static final String SUBPOPUP_FORMAT_DATE_AND_TIME_MEDIUM = Messages.getString( "PopupMenu.18" ); //$NON-NLS-1$
	private static final String SUBPOPUP_FORMAT_DATE_AND_TIME_SHORT = Messages.getString( "PopupMenu.19" ); //$NON-NLS-1$

	private static final String SUBPOPUP_FORMAT_DATE_FULL = Messages.getString( "PopupMenu.20" ); //$NON-NLS-1$
	private static final String SUBPOPUP_FORMAT_DATE_LONG = Messages.getString( "PopupMenu.21" ); //$NON-NLS-1$
	private static final String SUBPOPUP_FORMAT_DATE_MEDIUM = Messages.getString( "PopupMenu.22" ); //$NON-NLS-1$
	private static final String SUBPOPUP_FORMAT_DATE_SHORT = Messages.getString( "PopupMenu.23" ); //$NON-NLS-1$

	private static final String SUBPOPUP_FORMAT_TIME_FULL = Messages.getString( "PopupMenu.24" ); //$NON-NLS-1$
	private static final String SUBPOPUP_FORMAT_TIME_LONG = Messages.getString( "PopupMenu.25" ); //$NON-NLS-1$
	private static final String SUBPOPUP_FORMAT_TIME_MEDIUM = Messages.getString( "PopupMenu.26" ); //$NON-NLS-1$
	private static final String SUBPOPUP_FORMAT_TIME_SHORT = Messages.getString( "PopupMenu.27" ); //$NON-NLS-1$

	private static final String SUBPOPUP_FORMAT_USER_DEFINED = Messages.getString( "PopupMenu.28" ); //$NON-NLS-1$

	private static final String SUBPOPUP_SORT_DATE_TIME_BY_DATE_TIME = Messages.getString( "PopupMenu.29" ); //$NON-NLS-1$
	private static final String SUBPOPUP_SORT_DATE_TIME_BY_TIME_ZONE = Messages.getString( "PopupMenu.30" ); //$NON-NLS-1$

	private CheckboxMenuItem m_checkboxMenuItemFormatDateAndTimeFull = null;
    private CheckboxMenuItem m_checkboxMenuItemFormatDateAndTimeLong = null;
    private CheckboxMenuItem m_checkboxMenuItemFormatDateAndTimeMedium = null;
    private CheckboxMenuItem m_checkboxMenuItemFormatDateAndTimeShort = null;

    private CheckboxMenuItem m_checkboxMenuItemFormatDateFull = null;
    private CheckboxMenuItem m_checkboxMenuItemFormatDateLong = null;
    private CheckboxMenuItem m_checkboxMenuItemFormatDateMedium = null;
    private CheckboxMenuItem m_checkboxMenuItemFormatDateShort = null;

    private CheckboxMenuItem m_checkboxMenuItemFormatTimeFull = null;
    private CheckboxMenuItem m_checkboxMenuItemFormatTimeLong = null;
    private CheckboxMenuItem m_checkboxMenuItemFormatTimeMedium = null;
    private CheckboxMenuItem m_checkboxMenuItemFormatTimeShort = null;

    private CheckboxMenuItem m_checkboxMenuItemFormatUserDefined = null;

    private CheckboxMenuItem m_checkboxMenuItemSortDateTimeByDateTime = null;
    private CheckboxMenuItem m_checkboxMenuItemSortDateTimeByTimeZone = null;

    private static boolean ms_popupDisabled = false;


    public PopupMenu()
	{
    	super();

	    MenuItem menuItem = null;

	    menuItem = new MenuItem( POPUP_ABOUT );
	    menuItem.addActionListener( this );
	    add( menuItem );

	    menuItem = new MenuItem( POPUP_ADD_REMOVE_TIME_ZONES );
	    menuItem.addActionListener( this );
	    add( menuItem );

	    menuItem = new MenuItem( POPUP_RENAME_TIME_ZONES );
	    menuItem.addActionListener( this );
	    add( menuItem );

	    menuItem = new MenuItem( POPUP_SET_LAYOUT );
	    menuItem.addActionListener( this );
	    add( menuItem );

	    menuItem = new MenuItem( POPUP_SET_DIFFERENT_DAY_INDICATOR );
	    menuItem.addActionListener( this );
	    add( menuItem );

	    menuItem = new MenuItem( POPUP_SET_TIME_ZONE_SEPARATOR );
	    menuItem.addActionListener( this );
	    add( menuItem );

	    java.awt.PopupMenu subpopup = new java.awt.PopupMenu();
	    subpopup.setLabel( POPUP_FORMAT );
	    CheckboxMenuItem checkboxMenuItem = null;

	    add( subpopup, m_checkboxMenuItemFormatDateAndTimeFull = new CheckboxMenuItem(), SUBPOPUP_FORMAT_DATE_AND_TIME_FULL, Properties.PROPERTY_SHOW_DATE_AND_TIME_FULL );
	    add( subpopup, m_checkboxMenuItemFormatDateAndTimeLong = new CheckboxMenuItem(), SUBPOPUP_FORMAT_DATE_AND_TIME_LONG, Properties.PROPERTY_SHOW_DATE_AND_TIME_LONG );
	    add( subpopup, m_checkboxMenuItemFormatDateAndTimeMedium = new CheckboxMenuItem(), SUBPOPUP_FORMAT_DATE_AND_TIME_MEDIUM, Properties.PROPERTY_SHOW_DATE_AND_TIME_MEDIUM );
	    add( subpopup, m_checkboxMenuItemFormatDateAndTimeShort = new CheckboxMenuItem(), SUBPOPUP_FORMAT_DATE_AND_TIME_SHORT, Properties.PROPERTY_SHOW_DATE_AND_TIME_SHORT );

	    add( subpopup, m_checkboxMenuItemFormatDateFull = new CheckboxMenuItem(), SUBPOPUP_FORMAT_DATE_FULL, Properties.PROPERTY_SHOW_DATE_FULL );
	    add( subpopup, m_checkboxMenuItemFormatDateLong = new CheckboxMenuItem(), SUBPOPUP_FORMAT_DATE_LONG, Properties.PROPERTY_SHOW_DATE_LONG );
	    add( subpopup, m_checkboxMenuItemFormatDateMedium = new CheckboxMenuItem(), SUBPOPUP_FORMAT_DATE_MEDIUM, Properties.PROPERTY_SHOW_DATE_MEDIUM );
	    add( subpopup, m_checkboxMenuItemFormatDateShort = new CheckboxMenuItem(), SUBPOPUP_FORMAT_DATE_SHORT, Properties.PROPERTY_SHOW_DATE_SHORT );

	    add( subpopup, m_checkboxMenuItemFormatTimeFull = new CheckboxMenuItem(), SUBPOPUP_FORMAT_TIME_FULL, Properties.PROPERTY_SHOW_TIME_FULL );
	    add( subpopup, m_checkboxMenuItemFormatTimeLong = new CheckboxMenuItem(), SUBPOPUP_FORMAT_TIME_LONG, Properties.PROPERTY_SHOW_TIME_LONG );
	    add( subpopup, m_checkboxMenuItemFormatTimeMedium = new CheckboxMenuItem(), SUBPOPUP_FORMAT_TIME_MEDIUM, Properties.PROPERTY_SHOW_TIME_MEDIUM );
	    add( subpopup, m_checkboxMenuItemFormatTimeShort = new CheckboxMenuItem(), SUBPOPUP_FORMAT_TIME_SHORT, Properties.PROPERTY_SHOW_TIME_SHORT );

	    add( subpopup, m_checkboxMenuItemFormatUserDefined = new CheckboxMenuItem(), SUBPOPUP_FORMAT_USER_DEFINED, Properties.PROPERTY_SHOW_USER_DEFINED );

	    add( subpopup );

	    subpopup = new java.awt.PopupMenu();
	    subpopup.setLabel( POPUP_SORT_DATE_TIME );

		UserTimeZones.DATE_TIME_SORT_OPTIONS dateTimeSortOption = UserTimeZones.DATE_TIME_SORT_OPTIONS.SORT_BY_DATE_TIME;
		String sortOption = Properties.getInstance().getProperty( Properties.PROPERTY_SORT_DATE_TIME, Properties.PROPERTY_SORT_DATE_TIME_BY_DATE_TIME, false );
		if( sortOption.equals( Properties.PROPERTY_SORT_DATE_TIME_BY_TIME_ZONE ) )
			dateTimeSortOption = UserTimeZones.DATE_TIME_SORT_OPTIONS.SORT_BY_TIME_ZONE;

		m_checkboxMenuItemSortDateTimeByDateTime =
	    	new CheckboxMenuItem
	    	(
	    		SUBPOPUP_SORT_DATE_TIME_BY_DATE_TIME,
	    		dateTimeSortOption == UserTimeZones.DATE_TIME_SORT_OPTIONS.SORT_BY_DATE_TIME
	    	);
		m_checkboxMenuItemSortDateTimeByDateTime.addItemListener( this );
	    subpopup.add( m_checkboxMenuItemSortDateTimeByDateTime );

		m_checkboxMenuItemSortDateTimeByTimeZone =
	    	new CheckboxMenuItem
	    	(
	    		SUBPOPUP_SORT_DATE_TIME_BY_TIME_ZONE,
	    		dateTimeSortOption == UserTimeZones.DATE_TIME_SORT_OPTIONS.SORT_BY_TIME_ZONE
	    	);
		m_checkboxMenuItemSortDateTimeByTimeZone.addItemListener( this );
		subpopup.add( m_checkboxMenuItemSortDateTimeByTimeZone );

	    add( subpopup );

	    checkboxMenuItem =
	    	new CheckboxMenuItem
	    	(
	    		POPUP_COMBINE_TIME_ZONES,
	    		Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_COMBINE_TIME_ZONES_OPTION, false ) 
	    	);
	    checkboxMenuItem.addItemListener( this );
	    add( checkboxMenuItem );

	    checkboxMenuItem =
	    	new CheckboxMenuItem
	    	(
	    		POPUP_SHOW_TIMES_IN_TOOL_TIP,
	    		Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_SHOW_TIMES_IN_TOOL_TIP, true )
	    	);
	    checkboxMenuItem.addItemListener( this );
	    add( checkboxMenuItem );

	    if( OperatingSystem.isWindows() )
	    {
		    checkboxMenuItem = new CheckboxMenuItem( POPUP_RUN_ON_SYSTEM_START, SystemStart.runOnSystemStart() );
		    checkboxMenuItem.addItemListener( this );
		    add( checkboxMenuItem );
	    }

	    menuItem = new MenuItem( POPUP_TIME_TRAVEL );
	    menuItem.addActionListener( this );
	    add( menuItem );

	    menuItem = new MenuItem( POPUP_SHOW_DATE_TIME );
	    menuItem.addActionListener( this );
	    add( menuItem );

	    menuItem = new MenuItem( POPUP_EXIT );
	    menuItem.addActionListener( this );
	    add( menuItem );
	}


    public boolean isPopupDisabled() { return ms_popupDisabled; }


    @Override
	public void show( Component origin, int x, int y ) 
    {
		// To block the right click action we check here if the right mouse button is clicked.
		// If a dialog is already showing, then we don't want to show the popup.
		// This method is not called when executing on Microsoft Windows...it only works here for Linux...or non-Windows.
    	if( ! isPopupDisabled() )
    		super.show(origin, x, y);
	}


	private void add( java.awt.PopupMenu popup, CheckboxMenuItem checkboxMenuItem, String checkboxName, String propertyName )
    {
	    String dateTimeFormat = Properties.getInstance().getProperty( Properties.PROPERTY_SHOW_OPTION, Properties.PROPERTY_SHOW_TIME_MEDIUM, false );
    	checkboxMenuItem.setLabel( checkboxName );
    	checkboxMenuItem.setState( dateTimeFormat.equalsIgnoreCase( propertyName ) );
    	checkboxMenuItem.addItemListener( this );
    	popup.add( checkboxMenuItem );
    }


	@Override 
	public void actionPerformed( ActionEvent actionEvent )
	{
		Object source = actionEvent.getSource();

    	if( source instanceof MenuItem && POPUP_ABOUT.equals( ( (MenuItem)source ).getLabel() ) )
    	{
    		String message =
				"<html><center><br>" +  //$NON-NLS-1$
        		"<h2>" + WorldTimeSystemTray.APPLICATION_NAME + "</h2><br><br>" + //$NON-NLS-1$ //$NON-NLS-2$
        		new MessageFormat( Messages.getString("PopupMenu.62") ).format( new Object[] { WorldTimeSystemTray.APPLICATION_VERSION_NUMBER } ) +"<br>" + //$NON-NLS-1$ //$NON-NLS-2$
        		"<a href='" + WorldTimeSystemTray.APPLICATION_URL + "'>" + WorldTimeSystemTray.APPLICATION_URL + "</a><br><br><br>" + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        		"<u>" + Messages.getString("PopupMenu.63") + "</u><br><br>" + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        		WorldTimeSystemTray.APPLICATION_AUTHOR + "<br><br>" + //$NON-NLS-1$
				( OperatingSystem.isWindows() ? CREDIT_NSIS + "<br><br>" : "" ) + //$NON-NLS-1$ //$NON-NLS-2$
				"</center></html>";  //$NON-NLS-1$

    		MessageDialog.showMessageDialog( POPUP_ABOUT, MessageDialog.createURLLabel( message ), JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION );
			return;
    	}


    	if( source instanceof MenuItem && POPUP_ADD_REMOVE_TIME_ZONES.equals( ( (MenuItem)source ).getLabel() ) )
    	{
    		ms_popupDisabled = true;
    		AddRemoveTimeZones.create();
    		ms_popupDisabled = false;
    		return;
    	}


    	if( source instanceof MenuItem && POPUP_RENAME_TIME_ZONES.equals( ( (MenuItem)source ).getLabel() ) )
    	{
        	if( Properties.getInstance().getPropertyList( Properties.PROPERTY_TIME_ZONES_SELECTED ).isEmpty() )
    		{
        		showMessageDialog( Messages.getString( "PopupMenu.41" ), Messages.getString( "PopupMenu.40" ), JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION );  //$NON-NLS-1$//$NON-NLS-2$
        		return;
    		}

        	ms_popupDisabled = true;
    		RenameTimeZones.create();
    		ms_popupDisabled = false;
    		return;
    	}


    	if( source instanceof MenuItem && POPUP_SHOW_DATE_TIME.equals( ( (MenuItem)source ).getLabel() ) )
    	{
        	Vector<String> userTimeZones = Properties.getInstance().getPropertyList( Properties.PROPERTY_TIME_ZONES_SELECTED );
        	if( userTimeZones.isEmpty() )
    		{
        		showMessageDialog( Messages.getString( "PopupMenu.41" ), Messages.getString( "PopupMenu.40" ), JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION ); //$NON-NLS-1$ //$NON-NLS-2$
        		return;
    		}

    		ms_popupDisabled = true;
        	ShowDateTime.create();
    		ms_popupDisabled = false;
    		return;
    	}


    	if( source instanceof MenuItem && POPUP_TIME_TRAVEL.equals( ( (MenuItem)source ).getLabel() ) )
    	{
        	Vector<String> userTimeZones = Properties.getInstance().getPropertyList( Properties.PROPERTY_TIME_ZONES_SELECTED );
        	if( userTimeZones.isEmpty() )
    		{
        		showMessageDialog( Messages.getString( "PopupMenu.41" ), Messages.getString( "PopupMenu.40" ), JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION );  //$NON-NLS-1$//$NON-NLS-2$
        		return;
    		}

    		ms_popupDisabled = true;
    		TimeTravel.create();
    		ms_popupDisabled = false;
    		return;
    	}


    	if( source instanceof MenuItem && POPUP_SET_DIFFERENT_DAY_INDICATOR.equals( ( (MenuItem)source ).getLabel() ) )
    	{
    		ms_popupDisabled = true;
    		DifferentDayIndicator.create();
    		ms_popupDisabled = false;
    		return;
    	}


    	if( source instanceof MenuItem && POPUP_SET_TIME_ZONE_SEPARATOR.equals( ( (MenuItem)source ).getLabel() ) )
    	{
    		ms_popupDisabled = true;
    		String inputValue = Properties.getInstance().getProperty( Properties.PROPERTY_COMBINE_TIME_ZONES_SEPARATOR, Properties.PROPERTY_COMBINE_TIME_ZONES_SEPARATOR_DEFAULT, false );
    		String outputValue = showInputDialog( Messages.getString( "PopupMenu.60" ), Messages.getString( "PopupMenu.58" ), inputValue );  //$NON-NLS-1$//$NON-NLS-2$
    		if( outputValue != null )
    			// We don't check for an empty value as this is legal...the user may well want an empty value!
    			Properties.getInstance().setProperty( Properties.PROPERTY_COMBINE_TIME_ZONES_SEPARATOR, inputValue );

    		ms_popupDisabled = false;
    		return;
    	}


    	if( source instanceof MenuItem && POPUP_SET_LAYOUT.equals( ( (MenuItem)source ).getLabel() ) )
    	{
        	if( Properties.getInstance().getPropertyList( Properties.PROPERTY_TIME_ZONES_SELECTED ).isEmpty() )
    		{
        		showMessageDialog( Messages.getString( "PopupMenu.41" ), Messages.getString( "PopupMenu.40" ), JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION );  //$NON-NLS-1$//$NON-NLS-2$
        		return;
    		}

    		ms_popupDisabled = true;
    		MessageLayout.create();
    		ms_popupDisabled = false;
    		return;
    	}


    	if( source instanceof MenuItem && POPUP_EXIT.equals( ( (MenuItem)source ).getLabel() ) )
    	{
    		Properties.getInstance().store();
    		System.exit( 0 );
    	}
    }


	@Override 
	public void itemStateChanged( ItemEvent itemEvent )
    {
    	CheckboxMenuItem checkboxMenuItem = (CheckboxMenuItem)itemEvent.getSource();
    	String label = checkboxMenuItem.getLabel();


    	// Toggle the run on system start option.
    	if( POPUP_RUN_ON_SYSTEM_START.equals( label ) )
    	{
    		SystemStart.setRunOnSystemStart( checkboxMenuItem.getState() );
    	}


    	// Handle the show times in the tool tip option.
    	else if( POPUP_SHOW_TIMES_IN_TOOL_TIP.equals( label ) )
    	{
    		Properties.getInstance().setProperty( Properties.PROPERTY_SHOW_TIMES_IN_TOOL_TIP, Boolean.toString( checkboxMenuItem.getState() ) );
    	}


    	// Handle the combine time zones option.
    	else if( POPUP_COMBINE_TIME_ZONES.equals( label ) )
    	{
    		Properties.getInstance().setProperty( Properties.PROPERTY_COMBINE_TIME_ZONES_OPTION, Boolean.toString( checkboxMenuItem.getState() ) );
    	}


    	// Handle the date/time sort option.
    	else if( SUBPOPUP_SORT_DATE_TIME_BY_DATE_TIME .equals( label ) )
    	{
    		m_checkboxMenuItemSortDateTimeByDateTime.setState( true );
    		m_checkboxMenuItemSortDateTimeByTimeZone.setState( false );
    		Properties.getInstance().setProperty( Properties.PROPERTY_SORT_DATE_TIME, Properties.PROPERTY_SORT_DATE_TIME_BY_DATE_TIME );
    	}
    	else if( SUBPOPUP_SORT_DATE_TIME_BY_TIME_ZONE.equals( label ) )
    	{
    		m_checkboxMenuItemSortDateTimeByDateTime.setState( false );
    		m_checkboxMenuItemSortDateTimeByTimeZone.setState( true );
    		Properties.getInstance().setProperty( Properties.PROPERTY_SORT_DATE_TIME, Properties.PROPERTY_SORT_DATE_TIME_BY_TIME_ZONE );
    	}


    	// Handle each date/time format.
    	else if( SUBPOPUP_FORMAT_DATE_AND_TIME_FULL.equals( label ) )
    	{
    		clearAllFormatCheckboxes();
    		m_checkboxMenuItemFormatDateAndTimeFull.setState( true );
    		Properties.getInstance().setProperty( Properties.PROPERTY_SHOW_OPTION, Properties.PROPERTY_SHOW_DATE_AND_TIME_FULL );
    	}
    	else if( SUBPOPUP_FORMAT_DATE_AND_TIME_LONG.equals( label ) )
    	{
    		clearAllFormatCheckboxes();
    		m_checkboxMenuItemFormatDateAndTimeLong.setState( true );
    		Properties.getInstance().setProperty( Properties.PROPERTY_SHOW_OPTION, Properties.PROPERTY_SHOW_DATE_AND_TIME_LONG );
    	}
    	else if( SUBPOPUP_FORMAT_DATE_AND_TIME_MEDIUM.equals( label ) )
    	{
    		clearAllFormatCheckboxes();
    		m_checkboxMenuItemFormatDateAndTimeMedium.setState( true );
    		Properties.getInstance().setProperty( Properties.PROPERTY_SHOW_OPTION, Properties.PROPERTY_SHOW_DATE_AND_TIME_MEDIUM );
    	}
    	else if( SUBPOPUP_FORMAT_DATE_AND_TIME_SHORT.equals( label ) )
    	{
    		clearAllFormatCheckboxes();
    		m_checkboxMenuItemFormatDateAndTimeShort.setState( true );
    		Properties.getInstance().setProperty( Properties.PROPERTY_SHOW_OPTION, Properties.PROPERTY_SHOW_DATE_AND_TIME_SHORT );
    	}
    	else if( SUBPOPUP_FORMAT_DATE_FULL.equals( label ) )
    	{
    		clearAllFormatCheckboxes();
    		m_checkboxMenuItemFormatDateFull.setState( true );
    		Properties.getInstance().setProperty( Properties.PROPERTY_SHOW_OPTION, Properties.PROPERTY_SHOW_DATE_FULL );
    	}
    	else if( SUBPOPUP_FORMAT_DATE_LONG.equals( label ) )
    	{
    		clearAllFormatCheckboxes();
    		m_checkboxMenuItemFormatDateLong.setState( true );
    		Properties.getInstance().setProperty( Properties.PROPERTY_SHOW_OPTION, Properties.PROPERTY_SHOW_DATE_LONG );
    	}
    	else if( SUBPOPUP_FORMAT_DATE_MEDIUM.equals( label ) )
    	{
    		clearAllFormatCheckboxes();
    		m_checkboxMenuItemFormatDateMedium.setState( true );
    		Properties.getInstance().setProperty( Properties.PROPERTY_SHOW_OPTION, Properties.PROPERTY_SHOW_DATE_MEDIUM );
    	}
    	else if( SUBPOPUP_FORMAT_DATE_SHORT.equals( label ) )
    	{
    		clearAllFormatCheckboxes();
    		m_checkboxMenuItemFormatDateShort.setState( true );
    		Properties.getInstance().setProperty( Properties.PROPERTY_SHOW_OPTION, Properties.PROPERTY_SHOW_DATE_SHORT );
    	}
    	else if( SUBPOPUP_FORMAT_TIME_FULL.equals( label ) )
    	{
    		clearAllFormatCheckboxes();
    		m_checkboxMenuItemFormatTimeFull.setState( true );
    		Properties.getInstance().setProperty( Properties.PROPERTY_SHOW_OPTION, Properties.PROPERTY_SHOW_TIME_FULL );
    	}
    	else if( SUBPOPUP_FORMAT_TIME_LONG.equals( label ) )
    	{
    		clearAllFormatCheckboxes();
    		m_checkboxMenuItemFormatTimeLong.setState( true );
    		Properties.getInstance().setProperty( Properties.PROPERTY_SHOW_OPTION, Properties.PROPERTY_SHOW_TIME_LONG );
    	}
    	else if( SUBPOPUP_FORMAT_TIME_MEDIUM.equals( label ) )
    	{
    		clearAllFormatCheckboxes();
    		m_checkboxMenuItemFormatTimeMedium.setState( true );
    		Properties.getInstance().setProperty( Properties.PROPERTY_SHOW_OPTION, Properties.PROPERTY_SHOW_TIME_MEDIUM );
    	}
    	else if( SUBPOPUP_FORMAT_TIME_SHORT.equals( label ) )
    	{
    		clearAllFormatCheckboxes();
    		m_checkboxMenuItemFormatTimeShort.setState( true );
    		Properties.getInstance().setProperty( Properties.PROPERTY_SHOW_OPTION, Properties.PROPERTY_SHOW_TIME_SHORT );
    	}
    	else if( SUBPOPUP_FORMAT_USER_DEFINED.equals( label ) )
    	{
    		ms_popupDisabled = true;

    		clearAllFormatCheckboxes();
    		m_checkboxMenuItemFormatUserDefined.setState( true );
    		Properties.getInstance().setProperty( Properties.PROPERTY_SHOW_OPTION, Properties.PROPERTY_SHOW_USER_DEFINED );
    		String inputValue = Properties.getInstance().getProperty( Properties.PROPERTY_SHOW_USER_DEFINED_PATTERN, "", false ); //$NON-NLS-1$

    		while( true )
    		{
        		String outputValue = showInputDialog( Messages.getString( "PopupMenu.45" ), new JLabel( Messages.getString( "PopupMenu.46" ) ), inputValue );  //$NON-NLS-1$//$NON-NLS-2$
        		if( outputValue == null )
        			break;

        		try
        		{
        			new SimpleDateFormat( outputValue );
            		Properties.getInstance().setProperty( Properties.PROPERTY_SHOW_USER_DEFINED_PATTERN, outputValue );
            		break;
        		}
        		catch( IllegalArgumentException illegalArgumentException )
        		{
            		showMessageDialog( Messages.getString( "PopupMenu.50" ), Messages.getString( "PopupMenu.49" ), JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION ); //$NON-NLS-1$ //$NON-NLS-2$
            		inputValue = outputValue;
            		continue;
        		}
    		}

    		ms_popupDisabled = false;
    	}

    	Properties.getInstance().store();
    }


	private void clearAllFormatCheckboxes()
	{
	    m_checkboxMenuItemFormatDateAndTimeFull.setState( false );
	    m_checkboxMenuItemFormatDateAndTimeLong.setState( false );
	    m_checkboxMenuItemFormatDateAndTimeMedium.setState( false );
	    m_checkboxMenuItemFormatDateAndTimeShort.setState( false );

	    m_checkboxMenuItemFormatDateFull.setState( false );
	    m_checkboxMenuItemFormatDateLong.setState( false );
	    m_checkboxMenuItemFormatDateMedium.setState( false );
	    m_checkboxMenuItemFormatDateShort.setState( false );

	    m_checkboxMenuItemFormatTimeFull.setState( false );
	    m_checkboxMenuItemFormatTimeLong.setState( false );
	    m_checkboxMenuItemFormatTimeMedium.setState( false );
	    m_checkboxMenuItemFormatTimeShort.setState( false );

	    m_checkboxMenuItemFormatUserDefined.setState( false );
	}


	private void showMessageDialog( String title, Object message, int messageType, int optionType )
	{
		ms_popupDisabled = true;
		JDialog messageDialog = new JOptionPane( message, messageType, optionType ).createDialog( title ); 
		messageDialog.setIconImage( TrayIcon.getApplicationIconImage() );
		messageDialog.setLocationRelativeTo( null );
		messageDialog.setVisible( true );

		ms_popupDisabled = false;
	}


	private String showInputDialog( String title, Object message, String initialValue )
	{	
		JOptionPane optionPane = new JOptionPane( message, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, null, initialValue );
		optionPane.setWantsInput( true );
		optionPane.setInitialSelectionValue( initialValue );
        optionPane.selectInitialValue();
        optionPane.updateUI();

        JDialog messageDialog = optionPane.createDialog( title );
        messageDialog.setIconImage( TrayIcon.getApplicationIconImage() );
        messageDialog.setVisible( true );

        Object value = optionPane.getInputValue();

		if( value == JOptionPane.UNINITIALIZED_VALUE )
            return null;

        return (String)value;
	}
}
