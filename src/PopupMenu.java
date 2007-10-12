import java.awt.CheckboxMenuItem;
import java.awt.MenuItem;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;


public class PopupMenu extends java.awt.PopupMenu implements ActionListener, ItemListener
{
	public static final String APPLICATION_NAME = Messages.getString( "PopupMenu.0" );

	private static final String APPLICATION_VERSION = Messages.getString( "PopupMenu.1" );
	private static final String APPLICATION_VERSION_NUMBER = "1.2 (2007-10-12)";

	private static final String CREDIT_REGISTRY = Messages.getString( "PopupMenu.4" );
	private static final String CREDIT_NSIS = Messages.getString( "PopupMenu.5" );

	private static final String POPUP_ABOUT = Messages.getString( "PopupMenu.6" );
	private static final String POPUP_ADD_REMOVE_TIME_ZONES = Messages.getString( "PopupMenu.7" );
	private static final String POPUP_RENAME_TIME_ZONES = Messages.getString( "PopupMenu.8" );
	private static final String POPUP_SHOW_DATE_TIME = Messages.getString( "PopupMenu.59" );
	private static final String POPUP_TIME_TRAVEL = Messages.getString( "PopupMenu.9" );
	private static final String POPUP_FORMAT = Messages.getString( "PopupMenu.10" );
	private static final String POPUP_COMBINE_TIME_ZONES = Messages.getString( "PopupMenu.51" );
	private static final String POPUP_SET_TIME_ZONE_SEPARATOR = Messages.getString( "PopupMenu.52" );
	private static final String POPUP_SET_LAYOUT = Messages.getString( "PopupMenu.53" );
	private static final String POPUP_SORT_DATE_TIME = Messages.getString( "PopupMenu.11" );
	private static final String POPUP_SHOW_TIMES_IN_TOOL_TIP = Messages.getString( "PopupMenu.12" );
	private static final String POPUP_SET_DIFFERENT_DAY_INDICATOR = Messages.getString( "PopupMenu.54" );
	private static final String POPUP_RUN_ON_SYSTEM_START = Messages.getString( "PopupMenu.14" );
	private static final String POPUP_EXIT = Messages.getString( "PopupMenu.15" );

	private static final String SUBPOPUP_FORMAT_DATE_AND_TIME_FULL = Messages.getString( "PopupMenu.16" );
	private static final String SUBPOPUP_FORMAT_DATE_AND_TIME_LONG = Messages.getString( "PopupMenu.17" );
	private static final String SUBPOPUP_FORMAT_DATE_AND_TIME_MEDIUM = Messages.getString( "PopupMenu.18" );
	private static final String SUBPOPUP_FORMAT_DATE_AND_TIME_SHORT = Messages.getString( "PopupMenu.19" );

	private static final String SUBPOPUP_FORMAT_DATE_FULL = Messages.getString( "PopupMenu.20" );
	private static final String SUBPOPUP_FORMAT_DATE_LONG = Messages.getString( "PopupMenu.21" );
	private static final String SUBPOPUP_FORMAT_DATE_MEDIUM = Messages.getString( "PopupMenu.22" );
	private static final String SUBPOPUP_FORMAT_DATE_SHORT = Messages.getString( "PopupMenu.23" );

	private static final String SUBPOPUP_FORMAT_TIME_FULL = Messages.getString( "PopupMenu.24" );
	private static final String SUBPOPUP_FORMAT_TIME_LONG = Messages.getString( "PopupMenu.25" );
	private static final String SUBPOPUP_FORMAT_TIME_MEDIUM = Messages.getString( "PopupMenu.26" );
	private static final String SUBPOPUP_FORMAT_TIME_SHORT = Messages.getString( "PopupMenu.27" );

	private static final String SUBPOPUP_FORMAT_USER_DEFINED = Messages.getString( "PopupMenu.28" );

	private static final String SUBPOPUP_SORT_DATE_TIME_BY_DATE_TIME = Messages.getString( "PopupMenu.29" );
	private static final String SUBPOPUP_SORT_DATE_TIME_BY_TIME_ZONE = Messages.getString( "PopupMenu.30" );

	private static final String SUBPOPUP_COMBINE_TIME_ZONES_SAME_DATE_TIME_AND_TIME_ZONE = Messages.getString( "PopupMenu.55" );
	private static final String SUBPOPUP_COMBINE_TIME_ZONES_SAME_DATE_TIME = Messages.getString( "PopupMenu.56" );
	private static final String SUBPOPUP_COMBINE_TIME_ZONES_NEVER = Messages.getString( "PopupMenu.57" );

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

    private CheckboxMenuItem m_checkboxMenuItemCombineSameDateTimeAndTimeZones = null;
    private CheckboxMenuItem m_checkboxMenuItemCombineSameDateTimes = null;
    private CheckboxMenuItem m_checkboxMenuItemCombineNever = null;

    private CheckboxMenuItem m_checkboxMenuItemSortDateTimeByDateTime = null;
    private CheckboxMenuItem m_checkboxMenuItemSortDateTimeByTimeZone = null;

    private static boolean m_popupDisabled = false;


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

	    menuItem = new MenuItem( POPUP_TIME_TRAVEL );
	    menuItem.addActionListener( this );
	    add( menuItem );

	    menuItem = new MenuItem( POPUP_SHOW_DATE_TIME );
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
		String sortOption = Properties.getInstance().getProperty( Properties.PROPERTY_SORT_DATE_TIME, Properties.PROPERTY_SORT_DATE_TIME_BY_DATE_TIME );
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

	    menuItem = new MenuItem( POPUP_SET_TIME_ZONE_SEPARATOR );
	    menuItem.addActionListener( this );
	    add( menuItem );

	    subpopup = new java.awt.PopupMenu();
	    subpopup.setLabel( POPUP_COMBINE_TIME_ZONES );

	    String combineOption = Properties.getInstance().getProperty( Properties.PROPERTY_COMBINE_TIME_ZONES_OPTION, Properties.PROPERTY_COMBINE_TIME_ZONES_NEVER );

		m_checkboxMenuItemCombineSameDateTimeAndTimeZones =
	    	new CheckboxMenuItem
	    	(
	    		SUBPOPUP_COMBINE_TIME_ZONES_SAME_DATE_TIME_AND_TIME_ZONE,
	    		Collator.getInstance().equals( combineOption, Properties.PROPERTY_COMBINE_TIME_ZONES_SAME_DATE_TIME_AND_TIME_ZONE )
	    	);
		m_checkboxMenuItemCombineSameDateTimeAndTimeZones.addItemListener( this );
	    subpopup.add( m_checkboxMenuItemCombineSameDateTimeAndTimeZones );

		m_checkboxMenuItemCombineSameDateTimes =
	    	new CheckboxMenuItem
	    	(
	    		SUBPOPUP_COMBINE_TIME_ZONES_SAME_DATE_TIME,
	    		Collator.getInstance().equals( combineOption, Properties.PROPERTY_COMBINE_TIME_ZONES_SAME_DATE_TIME )
	    	);
		m_checkboxMenuItemCombineSameDateTimes.addItemListener( this );
		subpopup.add( m_checkboxMenuItemCombineSameDateTimes );

		m_checkboxMenuItemCombineNever =
	    	new CheckboxMenuItem
	    	(
	    		SUBPOPUP_COMBINE_TIME_ZONES_NEVER,
	    		Collator.getInstance().equals( combineOption, Properties.PROPERTY_COMBINE_TIME_ZONES_NEVER )
	    	);
		m_checkboxMenuItemCombineNever.addItemListener( this );
		subpopup.add( m_checkboxMenuItemCombineNever );

	    add( subpopup );

	    checkboxMenuItem =
	    	new CheckboxMenuItem
	    	(
	    		POPUP_SHOW_TIMES_IN_TOOL_TIP,
	    		Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_SHOW_TIMES_IN_TOOL_TIP, true )
	    	);
	    checkboxMenuItem.addItemListener( this );
	    add( checkboxMenuItem );

	    if( SystemStart.isMicrosoftWindows() )
	    {
		    checkboxMenuItem = new CheckboxMenuItem( POPUP_RUN_ON_SYSTEM_START, SystemStart.runOnSystemStart() );
		    checkboxMenuItem.addItemListener( this );
		    add( checkboxMenuItem );
	    }

	    menuItem = new MenuItem( POPUP_EXIT );
	    menuItem.addActionListener( this );
	    add( menuItem );
	}


    private void add( java.awt.PopupMenu popup, CheckboxMenuItem checkboxMenuItem, String checkboxName, String propertyName )
    {
	    String dateTimeFormat = Properties.getInstance().getProperty( Properties.PROPERTY_SHOW_OPTION, Properties.PROPERTY_SHOW_TIME_MEDIUM );
    	checkboxMenuItem.setLabel( checkboxName );
    	checkboxMenuItem.setState( dateTimeFormat.equalsIgnoreCase( propertyName ) );
    	checkboxMenuItem.addItemListener( this );
    	popup.add( checkboxMenuItem );
    }


    public boolean popupIsDisabled() { return m_popupDisabled; }


    public void actionPerformed( ActionEvent actionEvent )
	{
		Object source = actionEvent.getSource();

    	if( source instanceof MenuItem && POPUP_ABOUT.equals( ( (MenuItem)source ).getLabel() ) )
    	{
    		m_popupDisabled = true;
    		final JLabel label =
    			new JLabel
    			(
    				"<html><center>" +
    				"<b>" + APPLICATION_NAME + "</b><br>" + APPLICATION_VERSION + " " + APPLICATION_VERSION_NUMBER + "<br><br>" +
    				CREDIT_REGISTRY + "<br><br>" +
    				CREDIT_NSIS + "<br><br>" +
    				"</center></html>",
    				SwingConstants.CENTER
    			);

    		final JDialog dialog = new JOptionPane( new Object[] { label } ).createDialog( POPUP_ABOUT );
    		dialog.setIconImage( TrayIcon.getTrayIconImage() );
	        int originX = ( Toolkit.getDefaultToolkit().getScreenSize().width - dialog.getWidth() ) / 2;
	        int originY = ( Toolkit.getDefaultToolkit().getScreenSize().height - dialog.getHeight() ) / 2;
	        dialog.setLocation( originX, originY );
	        dialog.setVisible( true );

	        m_popupDisabled = false;

			return;
    	}


    	if( source instanceof MenuItem && POPUP_ADD_REMOVE_TIME_ZONES.equals( ( (MenuItem)source ).getLabel() ) )
    	{
    		m_popupDisabled = true;
    		AddRemoveTimeZones.create();
    		m_popupDisabled = false;
    		return;
    	}


    	if( source instanceof MenuItem && POPUP_RENAME_TIME_ZONES.equals( ( (MenuItem)source ).getLabel() ) )
    	{
        	if( Properties.getInstance().getPropertyList( Properties.PROPERTY_TIME_ZONES_SELECTED ).isEmpty() )
    		{
        		m_popupDisabled = true;

        		JOptionPane.showMessageDialog
        		(
        			null,
        			Messages.getString( "PopupMenu.40" ),
        			Messages.getString( "PopupMenu.41" ),
        			JOptionPane.OK_OPTION
        		);

        		m_popupDisabled = false;
        		return;
    		}

        	m_popupDisabled = true;
    		RenameTimeZones.create();
    		m_popupDisabled = false;
    		return;
    	}


    	if( source instanceof MenuItem && POPUP_SHOW_DATE_TIME.equals( ( (MenuItem)source ).getLabel() ) )
    	{
        	Vector<String> userTimeZones = Properties.getInstance().getPropertyList( Properties.PROPERTY_TIME_ZONES_SELECTED );
        	if( userTimeZones.isEmpty() )
    		{
        		m_popupDisabled = true;

        		JOptionPane.showMessageDialog
        		(
        			null,
        			Messages.getString( "PopupMenu.42" ),
        			Messages.getString( "PopupMenu.43" ),
        			JOptionPane.OK_OPTION
        		);

        		m_popupDisabled = false;
        		return;
    		}

    		m_popupDisabled = true;
    		ShowDateTime.create();
    		m_popupDisabled = false;
    		return;
    	}


    	if( source instanceof MenuItem && POPUP_TIME_TRAVEL.equals( ( (MenuItem)source ).getLabel() ) )
    	{
        	Vector<String> userTimeZones = Properties.getInstance().getPropertyList( Properties.PROPERTY_TIME_ZONES_SELECTED );
        	if( userTimeZones.isEmpty() )
    		{
        		m_popupDisabled = true;

        		JOptionPane.showMessageDialog
        		(
        			null,
        			Messages.getString( "PopupMenu.42" ),
        			Messages.getString( "PopupMenu.43" ),
        			JOptionPane.OK_OPTION
        		);

        		m_popupDisabled = false;
        		return;
    		}

    		m_popupDisabled = true;
    		TimeTravel.create();
    		m_popupDisabled = false;
    		return;
    	}


    	if( source instanceof MenuItem && POPUP_SET_DIFFERENT_DAY_INDICATOR.equals( ( (MenuItem)source ).getLabel() ) )
    	{
    		m_popupDisabled = true;
    		DifferentDayIndicator.create();
    		m_popupDisabled = false;
    		return;
    	}


    	if( source instanceof MenuItem && POPUP_SET_TIME_ZONE_SEPARATOR.equals( ( (MenuItem)source ).getLabel() ) )
    	{
    		m_popupDisabled = true;
    		String inputValue = Properties.getInstance().getProperty( Properties.PROPERTY_COMBINE_TIME_ZONES_SEPARATOR, Properties.PROPERTY_COMBINE_TIME_ZONES_SEPARATOR_DEFAULT );
    		String message = Messages.getString( "PopupMenu.58" );
    		inputValue = JOptionPane.showInputDialog( message, inputValue );
    		if( inputValue != null )
        		Properties.getInstance().setProperty( Properties.PROPERTY_COMBINE_TIME_ZONES_SEPARATOR, inputValue );

    		m_popupDisabled = false;
    		return;
    	}


    	if( source instanceof MenuItem && POPUP_SET_LAYOUT.equals( ( (MenuItem)source ).getLabel() ) )
    	{
        	if( Properties.getInstance().getPropertyList( Properties.PROPERTY_TIME_ZONES_SELECTED ).isEmpty() )
    		{
        		m_popupDisabled = true;

        		JOptionPane.showMessageDialog
        		(
        			null,
        			Messages.getString( "PopupMenu.42" ),
        			Messages.getString( "PopupMenu.43" ),
        			JOptionPane.OK_OPTION
        		);

        		m_popupDisabled = false;
        		return;
    		}

        	m_popupDisabled = true;
    		MessageLayout.create();
    		m_popupDisabled = false;
    		return;
    	}


    	if( source instanceof MenuItem && POPUP_EXIT.equals( ( (MenuItem)source ).getLabel() ) )
    	{
    		Properties.getInstance().store();
    		System.exit( 0 );
    	}
    }


	public void itemStateChanged( ItemEvent itemEvent )
    {
    	CheckboxMenuItem checkboxMenuItem = (CheckboxMenuItem)itemEvent.getSource();
    	String label = checkboxMenuItem.getLabel();


    	// Toggle the run on system start option.
    	if( POPUP_RUN_ON_SYSTEM_START.equals( label ) )
    	{
    		SystemStart.setRunOnSystemStart( checkboxMenuItem.getState() );
    	}


    	// Handle the show times int tool tip option.
    	else if( POPUP_SHOW_TIMES_IN_TOOL_TIP.equals( label ) )
    	{
    		Properties.getInstance().setProperty( Properties.PROPERTY_SHOW_TIMES_IN_TOOL_TIP, Boolean.toString( checkboxMenuItem.getState() ) );
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


    	// Handle the combine Time Zones option.
    	else if( SUBPOPUP_COMBINE_TIME_ZONES_NEVER.equals( label ) )
    	{
    		m_checkboxMenuItemCombineNever.setState( true );
    		m_checkboxMenuItemCombineSameDateTimeAndTimeZones.setState( false );
    		m_checkboxMenuItemCombineSameDateTimes.setState( false );
    		Properties.getInstance().setProperty( Properties.PROPERTY_COMBINE_TIME_ZONES_OPTION, Properties.PROPERTY_COMBINE_TIME_ZONES_NEVER );
    	}
    	else if( SUBPOPUP_COMBINE_TIME_ZONES_SAME_DATE_TIME_AND_TIME_ZONE.equals( label ) )
    	{
    		m_checkboxMenuItemCombineNever.setState( false );
    		m_checkboxMenuItemCombineSameDateTimeAndTimeZones.setState( true );
    		m_checkboxMenuItemCombineSameDateTimes.setState( false );
    		Properties.getInstance().setProperty( Properties.PROPERTY_COMBINE_TIME_ZONES_OPTION, Properties.PROPERTY_COMBINE_TIME_ZONES_SAME_DATE_TIME_AND_TIME_ZONE );
    	}
    	else if( SUBPOPUP_COMBINE_TIME_ZONES_SAME_DATE_TIME.equals( label ) )
    	{
    		m_checkboxMenuItemCombineNever.setState( false );
    		m_checkboxMenuItemCombineSameDateTimeAndTimeZones.setState( false );
    		m_checkboxMenuItemCombineSameDateTimes.setState( true );
    		Properties.getInstance().setProperty( Properties.PROPERTY_COMBINE_TIME_ZONES_OPTION, Properties.PROPERTY_COMBINE_TIME_ZONES_SAME_DATE_TIME );
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
    		m_popupDisabled = true;
    		clearAllFormatCheckboxes();
    		m_checkboxMenuItemFormatUserDefined.setState( true );
    		Properties.getInstance().setProperty( Properties.PROPERTY_SHOW_OPTION, Properties.PROPERTY_SHOW_USER_DEFINED );

    		String inputValue = Properties.getInstance().getProperty( Properties.PROPERTY_SHOW_USER_DEFINED_PATTERN, "" );
    		while( true )
    		{
    			String message = Messages.getString( "PopupMenu.45" ) + Messages.getString( "PopupMenu.46" );
        		inputValue = JOptionPane.showInputDialog( message, inputValue );
        		if( inputValue == null )
        			break;  // User cancelled.

        		if( inputValue.length() == 0 )
        		{
        			JOptionPane.showMessageDialog
        			(
        				null,
        				Messages.getString( "PopupMenu.47" ),
        				Messages.getString( "PopupMenu.48" ),
        				JOptionPane.ERROR_MESSAGE
        			);

        			continue;
        		}

        		try
        		{
        			new SimpleDateFormat( inputValue );
            		Properties.getInstance().setProperty( Properties.PROPERTY_SHOW_USER_DEFINED_PATTERN, inputValue );
            		break;
        		}
        		catch( IllegalArgumentException illegalArgumentException )
        		{
        			JOptionPane.showMessageDialog
        			(
        				null,
        				Messages.getString( "PopupMenu.49" ),
        				Messages.getString( "PopupMenu.50" ),
        				JOptionPane.ERROR_MESSAGE
        			);
        			continue;
        		}
    		}

    		m_popupDisabled = false;
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
}
