import java.awt.CheckboxMenuItem;
import java.awt.MenuItem;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;


public class PopupMenu extends java.awt.PopupMenu implements ActionListener, ItemListener
{
	static final String POPUP_ABOUT = "About";
	static final String POPUP_EXIT = "Exit";
	static final String POPUP_CHRONOLOGY = "Chronologies";
	static final String POPUP_DATE_FORMAT = "Date Format";
	static final String POPUP_RUN_ON_SYSTEM_START = "Run on system start";
	static final String POPUP_SHOW_STARDATE_ISSUE = "Show Stardate ISSUE";

	static final String SUBPOPUP_SHOW_MESSAGE_BUDDHIST = "Buddhist";
	static final String SUBPOPUP_SHOW_MESSAGE_COPTIC = "Coptic";
	static final String SUBPOPUP_SHOW_MESSAGE_ETHIOPIC = "Ethiopic";
	static final String SUBPOPUP_SHOW_MESSAGE_GREGORIAN = "Gregorian";
	static final String SUBPOPUP_SHOW_MESSAGE_GREGORIAN_JULIAN = "Gregorian-Julian";
	static final String SUBPOPUP_SHOW_MESSAGE_ISLAMIC = "Islamic";
	static final String SUBPOPUP_SHOW_MESSAGE_ISO8601 = "ISO8601";
	static final String SUBPOPUP_SHOW_MESSAGE_JULIAN = "Julian";
	static final String SUBPOPUP_SHOW_MESSAGE_STARDATE = "Stardate";

	static final String SUBPOPUP_DATE_TIME_FORMAT_LONG = "Long";
	static final String SUBPOPUP_DATE_TIME_FORMAT_MEDIUM = "Medium";
	static final String SUBPOPUP_DATE_TIME_FORMAT_SHORT = "Short";

	public static final String APPLICATION_NAME = "Stardate System Tray";
	public static final String APPLICATION_VERSION = "Version 1.1 (2007-06-22)";

	private static final String CREDIT_ALGORITHM_LINE1 = "Stardate algorithm based on";
	private static final String CREDIT_ALGORITHM_LINE2 = "<i>STARDATES IN STAR TREK FAQ</i>";
	private static final String CREDIT_CHRONOLOGY = "Chronology conversion via <i>Joda Time</i>";
	private static final String CREDIT_REGISTRY = "Registry access via <i>JRegistryKey</i>";
	private static final String CREDIT_NSIS = "Executables created with <i>NSIS</i>";

    private CheckboxMenuItem m_checkboxMenuItemDateFormatLong = null;
    private CheckboxMenuItem m_checkboxMenuItemDateFormatMedium = null;
    private CheckboxMenuItem m_checkboxMenuItemDateFormatShort = null;

    private static boolean m_popupDisabled = false;

    
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

	    checkboxMenuItem = new CheckboxMenuItem( SUBPOPUP_SHOW_MESSAGE_BUDDHIST, Boolean.valueOf( Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_CHRONOLOGY_BUDDHIST, true ) ) );
	    checkboxMenuItem.addItemListener( this );
	    subpopup.add( checkboxMenuItem );

	    checkboxMenuItem = new CheckboxMenuItem( SUBPOPUP_SHOW_MESSAGE_COPTIC, Boolean.valueOf( Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_CHRONOLOGY_COPTIC, true ) ) );
	    checkboxMenuItem.addItemListener( this );
	    subpopup.add( checkboxMenuItem );

	    checkboxMenuItem = new CheckboxMenuItem( SUBPOPUP_SHOW_MESSAGE_ETHIOPIC, Boolean.valueOf( Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_CHRONOLOGY_ETHIOPIC, true ) ) );
	    checkboxMenuItem.addItemListener( this );
	    subpopup.add( checkboxMenuItem );

	    checkboxMenuItem = new CheckboxMenuItem( SUBPOPUP_SHOW_MESSAGE_GREGORIAN, Boolean.valueOf( Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_CHRONOLOGY_GREGORIAN, true ) ) );
	    checkboxMenuItem.addItemListener( this );
	    subpopup.add( checkboxMenuItem );

	    checkboxMenuItem = new CheckboxMenuItem( SUBPOPUP_SHOW_MESSAGE_GREGORIAN_JULIAN, Boolean.valueOf( Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_CHRONOLOGY_GREGORIAN_JULIAN, true ) ) );
	    checkboxMenuItem.addItemListener( this );
	    subpopup.add( checkboxMenuItem );

	    checkboxMenuItem = new CheckboxMenuItem( SUBPOPUP_SHOW_MESSAGE_ISLAMIC, Boolean.valueOf( Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_CHRONOLOGY_ISLAMIC, true ) ) );
	    checkboxMenuItem.addItemListener( this );
	    subpopup.add( checkboxMenuItem );

	    checkboxMenuItem = new CheckboxMenuItem( SUBPOPUP_SHOW_MESSAGE_ISO8601, Boolean.valueOf( Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_CHRONOLOGY_ISO8601, true ) ) );
	    checkboxMenuItem.addItemListener( this );
	    subpopup.add( checkboxMenuItem );

	    checkboxMenuItem = new CheckboxMenuItem( SUBPOPUP_SHOW_MESSAGE_JULIAN, Boolean.valueOf( Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_CHRONOLOGY_JULIAN, true ) ) );
	    checkboxMenuItem.addItemListener( this );
	    subpopup.add( checkboxMenuItem );

	    checkboxMenuItem = new CheckboxMenuItem( SUBPOPUP_SHOW_MESSAGE_STARDATE, Boolean.valueOf( Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_CHRONOLOGY_STARDATE, true ) ) );
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

	    add( subpopup );

	    checkboxMenuItem = new CheckboxMenuItem( POPUP_RUN_ON_SYSTEM_START, SystemStart.runOnSystemStart() );
	    checkboxMenuItem.addItemListener( this );
	    add( checkboxMenuItem );

	    boolean showStardateIssue = Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_SHOW_STARDATE_ISSUE, Properties.PROPERTY_SHOW_STARDATE_ISSUE_DEFAULT_VALUE );
	    checkboxMenuItem = new CheckboxMenuItem( POPUP_SHOW_STARDATE_ISSUE, showStardateIssue );
	    checkboxMenuItem.addItemListener( this );
	    add( checkboxMenuItem );

	    menuItem = new MenuItem( POPUP_EXIT );
	    menuItem.addActionListener( this );
	    add( menuItem );
	}

    
    public boolean popupIsDisabled() { return m_popupDisabled; }
    
    
    public void actionPerformed( ActionEvent actionEvent )
	{
    	String menuItem = ( (MenuItem)actionEvent.getSource() ).getLabel();

    	if( POPUP_ABOUT.equals( menuItem ) )
    	{
    		m_popupDisabled = true;
    		final JLabel label = 
    			new JLabel
    			( 
        				"<html><center>" +
        				"<b>" + APPLICATION_NAME + "</b><br>" + APPLICATION_VERSION + "<br><br>" + 
        				CREDIT_ALGORITHM_LINE1 + "<br>" + CREDIT_ALGORITHM_LINE2 + "<br><br>" + 
        				CREDIT_CHRONOLOGY + "<br><br>" + 
        				CREDIT_REGISTRY + "<br><br>" + 
        				CREDIT_NSIS + "<br><br>" +
        				"</center></html>",
        				SwingConstants.CENTER
    			);

    		final JDialog dialog = new JOptionPane( new Object[] { label } ).createDialog( POPUP_ABOUT );
	        int originX = ( Toolkit.getDefaultToolkit().getScreenSize().width - dialog.getWidth() ) / 2;
	        int originY = ( Toolkit.getDefaultToolkit().getScreenSize().height - dialog.getHeight() ) / 2;
	        dialog.setLocation( originX, originY );
	        dialog.setVisible( true );
	        
	        m_popupDisabled = false;

			return;
    	}

    	if( POPUP_EXIT.equals( menuItem ) )
    	{
    		Properties.getInstance().store();
    		System.exit( 0 );
    	}
    }


	public void itemStateChanged( ItemEvent itemEvent )
    {
    	CheckboxMenuItem checkboxMenuItem = (CheckboxMenuItem)itemEvent.getSource();
    	String label = checkboxMenuItem.getLabel();

    	// Toggle the show/hide Stardate ISSUE.
    	if( POPUP_SHOW_STARDATE_ISSUE.equals( label ) )
    	{
    		Properties.getInstance().setProperty( Properties.PROPERTY_SHOW_STARDATE_ISSUE, Boolean.toString( checkboxMenuItem.getState() ) );
    	}

    	// Toggle the run on system start option.
    	else if( POPUP_RUN_ON_SYSTEM_START.equals( label ) )
    	{
    		SystemStart.setRunOnSystemStart( checkboxMenuItem.getState() );
    	}

    	// Handle each chronology.
    	else if( SUBPOPUP_SHOW_MESSAGE_BUDDHIST.equals( label ) )
    	{
    		Properties.getInstance().setProperty( Properties.PROPERTY_CHRONOLOGY_BUDDHIST, Boolean.toString( checkboxMenuItem.getState() ) );
    	}
    	else if( SUBPOPUP_SHOW_MESSAGE_COPTIC.equals( label ) )
    	{
    		Properties.getInstance().setProperty( Properties.PROPERTY_CHRONOLOGY_COPTIC, Boolean.toString( checkboxMenuItem.getState() ) );
    	}
    	else if( SUBPOPUP_SHOW_MESSAGE_ETHIOPIC.equals( label ) )
    	{
    		Properties.getInstance().setProperty( Properties.PROPERTY_CHRONOLOGY_ETHIOPIC, Boolean.toString( checkboxMenuItem.getState() ) );
    	}
    	else if( SUBPOPUP_SHOW_MESSAGE_GREGORIAN.equals( label ) )
    	{
    		Properties.getInstance().setProperty( Properties.PROPERTY_CHRONOLOGY_GREGORIAN, Boolean.toString( checkboxMenuItem.getState() ) );
    	}
    	else if( SUBPOPUP_SHOW_MESSAGE_GREGORIAN_JULIAN.equals( label ) )
    	{
    		Properties.getInstance().setProperty( Properties.PROPERTY_CHRONOLOGY_GREGORIAN_JULIAN, Boolean.toString( checkboxMenuItem.getState() ) );
    	}
    	else if( SUBPOPUP_SHOW_MESSAGE_ISLAMIC.equals( label ) )
    	{
    		Properties.getInstance().setProperty( Properties.PROPERTY_CHRONOLOGY_ISLAMIC, Boolean.toString( checkboxMenuItem.getState() ) );
    	}
    	else if( SUBPOPUP_SHOW_MESSAGE_ISO8601.equals( label ) )
    	{
    		Properties.getInstance().setProperty( Properties.PROPERTY_CHRONOLOGY_ISO8601, Boolean.toString( checkboxMenuItem.getState() ) );
    	}
    	else if( SUBPOPUP_SHOW_MESSAGE_JULIAN.equals( label ) )
    	{
    		Properties.getInstance().setProperty( Properties.PROPERTY_CHRONOLOGY_JULIAN, Boolean.toString( checkboxMenuItem.getState() ) );
    	}
    	else if( SUBPOPUP_SHOW_MESSAGE_STARDATE.equals( label ) )
    	{
    		Properties.getInstance().setProperty( Properties.PROPERTY_CHRONOLOGY_STARDATE, Boolean.toString( checkboxMenuItem.getState() ) );
    	}

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
