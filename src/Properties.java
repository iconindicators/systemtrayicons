import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.StringTokenizer;
import java.util.Vector;


public class Properties
{
    private static java.util.Properties ms_properties = null;
	private static final String PROPERTY_FILE = System.getProperty( "user.dir" ) + File.separator + "properties" ;
    private static Properties ms_instance = new Properties();

    /** Options for combo portions of the layout */
    public static final String PROPERTY_LAYOUT_LEFT_OPTION = "LayoutLeftOption"; 
    public static final String PROPERTY_LAYOUT_CENTRE_OPTION = "LayoutCentreOption"; 
    public static final String PROPERTY_LAYOUT_RIGHT_OPTION = "LayoutRightOption"; 

    /** Values for combo portions of the layout */
    public static final String PROPERTY_LAYOUT_OPTION_NONE = "";
	public static final String PROPERTY_LAYOUT_OPTION_DIFFERENT_DAY_INDICATOR = Messages.getString( "Properties.0" );
	public static final String PROPERTY_LAYOUT_OPTION_TIME = Messages.getString( "Properties.1" );
	public static final String PROPERTY_LAYOUT_OPTION_TIME_ZONE = Messages.getString( "Properties.2" );

    /** Values for text portions of the layout */
    public static final String PROPERTY_LAYOUT_LEFT_TEXT = "LayoutLeftText"; 
    public static final String PROPERTY_LAYOUT_LEFT_TEXT_DEFAULT = ""; 
    public static final String PROPERTY_LAYOUT_LEFT_CENTRE_TEXT = "LayoutLeftCentreText"; 
    public static final String PROPERTY_LAYOUT_LEFT_CENTRE_TEXT_DEFAULT = ": "; 
    public static final String PROPERTY_LAYOUT_RIGHT_CENTRE_TEXT = "LayoutRightCentreText"; 
    public static final String PROPERTY_LAYOUT_RIGHT_CENTRE_TEXT_DEFAULT = ""; 
    public static final String PROPERTY_LAYOUT_RIGHT_TEXT = "LayoutRightText"; 
    public static final String PROPERTY_LAYOUT_RIGHT_TEXT_DEFAULT = ""; 

    /** Options for combining Time Zones. */
    public static final String PROPERTY_COMBINE_TIME_ZONES_OPTION = "CombineTimeZone";
    public static final String PROPERTY_COMBINE_TIME_ZONES_SAME_DATE_TIME_AND_TIME_ZONE = "SameDateTimeAndTimeZone";
    public static final String PROPERTY_COMBINE_TIME_ZONES_SAME_DATE_TIME = "SameDateTime";
    public static final String PROPERTY_COMBINE_TIME_ZONES_NEVER = "Never";

    /** Separator for Time Zones - only used when combining Time Zones */
    public static final String PROPERTY_COMBINE_TIME_ZONES_SEPARATOR = "Separator";
    public static final String PROPERTY_COMBINE_TIME_ZONES_SEPARATOR_DEFAULT = " | ";

    /** Indicators for the previous/next day. */
    public static final String PROPERTY_DIFFERENT_DAY_INDICATOR_PREVIOUS_DAY = "DifferentDayIndicatorPreviousDay";
    public static final String PROPERTY_DIFFERENT_DAY_INDICATOR_NEXT_DAY = "DifferentDayIndicatorNextDay";

	/** User time zones and their display names. */
    public static final String PROPERTY_TIME_ZONES_SELECTED = "TimeZonesSelected";
    public static final String PROPERTY_TIME_ZONES_SELECTED_DISPLAY_NAMES = "TimeZonesSelectedDisplayNames";

	/** Sort date/times either by date/time or user time zone. */
    public static final String PROPERTY_SORT_DATE_TIME = "SortDateTime";
    public static final String PROPERTY_SORT_DATE_TIME_BY_DATE_TIME = "SortDateTimeByDateTime";
    public static final String PROPERTY_SORT_DATE_TIME_BY_TIME_ZONE = "SortDateTimeByTimeZone";

	/** Options for showing the date/time. */
	public static final String PROPERTY_SHOW_OPTION = "ShowOption";
	public static final String PROPERTY_SHOW_DATE_AND_TIME_FULL = "ShowDateTimeFull";
	public static final String PROPERTY_SHOW_DATE_AND_TIME_LONG = "ShowDateTimeLong";
	public static final String PROPERTY_SHOW_DATE_AND_TIME_MEDIUM = "ShowDateTimeMedium";
	public static final String PROPERTY_SHOW_DATE_AND_TIME_SHORT = "ShowDateTimeShort";

	public static final String PROPERTY_SHOW_DATE_FULL = "ShowDateFull";
	public static final String PROPERTY_SHOW_DATE_LONG = "ShowDateLong";
	public static final String PROPERTY_SHOW_DATE_MEDIUM = "ShowDateMedium";
	public static final String PROPERTY_SHOW_DATE_SHORT = "ShowDateShort";

	public static final String PROPERTY_SHOW_TIME_FULL = "ShowTimeFull";
	public static final String PROPERTY_SHOW_TIME_LONG = "ShowTimeLong";
	public static final String PROPERTY_SHOW_TIME_MEDIUM = "ShowTimeMedium";
	public static final String PROPERTY_SHOW_TIME_SHORT = "ShowTimeShort";

	public static final String PROPERTY_SHOW_USER_DEFINED = "ShowUserDefined";
	public static final String PROPERTY_SHOW_USER_DEFINED_PATTERN = "ShowUserDefinedPattern";

	/** Show the times in the tool tip. */
	public static final String PROPERTY_SHOW_TIMES_IN_TOOL_TIP = "ShowTimesInToolTip";


	private Properties()
    {
        try
		{
            ms_properties = new java.util.Properties();
            ms_properties.load( new FileInputStream( PROPERTY_FILE ) );
        }
        catch( Throwable throwable ) { ms_properties = new java.util.Properties(); }
    }


    public static Properties getInstance() { return ms_instance; }


    public String getProperty( String key, String defaultValue )
    {
        if( key == null )
        	throw new IllegalArgumentException( "Key cannot be null." );

        Object value = ms_properties.getProperty( key );
        return value == null ? defaultValue : value.toString();
    }


    public boolean getPropertyBoolean( String key, boolean defaultValue )
    {
        if( key == null )
        	throw new IllegalArgumentException( "Key cannot be null." );

        String val = ms_properties.getProperty( key );
        if( val == null || val.length() == 0 )
            return defaultValue;

        try { return Boolean.valueOf( val ).booleanValue(); }
        catch( Throwable throwable ) { return defaultValue; }
    }


    public int getPropertyInteger( String key, int defaultValue )
    {
        if( key == null )
        	throw new IllegalArgumentException( "Key cannot be null." );

        String val = ms_properties.getProperty( key );
        if( val == null || val.length() == 0 )
            return defaultValue;

        try { return Integer.valueOf( val ).intValue(); }
        catch( Throwable throwable ) { return defaultValue; }
    }


    public Vector<String> getPropertyList( String key )
    {
        if( key == null )
        	throw new IllegalArgumentException( "Key cannot be null." );

        String value = ms_properties.getProperty( key );
        if( value == null || value.length() == 0 )
        	return new Vector<String>();

        Vector<String> v = new Vector<String>();
        StringTokenizer tokenizer = new StringTokenizer( value, "," );
        while( tokenizer.hasMoreElements() )
            v.add( tokenizer.nextToken().replace( "\\,", "," ) );

        return v;
    }


    public void setPropertyList( String key, Vector<String> values )
    {
        if( key == null )
        	throw new IllegalArgumentException( "Key cannot be null." );

        if( values == null )
        	throw new IllegalArgumentException( "Values cannot be null." );

        StringBuilder value = new StringBuilder();
        for( int i = 0; i < values.size(); i++ )
            value.append( values.elementAt( i ).replace( ",", "\\," ) + "," );

        // Trim the last "," character and store.
        if( ! values.isEmpty() )
        ms_properties.setProperty( key, value.substring( 0, value.length() - 1 ) );
    }
    
    
    public void setProperty( String key, String value )
    {
        if( key == null )
        	throw new IllegalArgumentException( "Key cannot be null." );

        if( value == null )
        	throw new IllegalArgumentException( "Value cannot be null." );

        ms_properties.setProperty( key, value );
    }


    public void store()
	{
        try { ms_properties.store( new FileOutputStream( PROPERTY_FILE ), null ); }
        catch( Throwable throwable ) { }
	}
}
