import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.StringTokenizer;
import java.util.Vector;


public class Properties
{
	public static final String PROPERTY_DIRECTORY = System.getProperty( "user.home" ) + File.separator + ".worldtimesystemtray"; //$NON-NLS-1$ //$NON-NLS-2$
	public static final String PROPERTY_FILE = PROPERTY_DIRECTORY + File.separator + "properties" ; //$NON-NLS-1$

	private static java.util.Properties ms_properties = new java.util.Properties();
    private static Properties ms_instance = new Properties();
    private static boolean ms_canCreatePropertyDirectory = true;
    private static boolean ms_canReadPropertiesFile = true;
    private static boolean ms_canWritePropertiesFile = true;

    /** Options for combo portions of the layout */
    public static final String PROPERTY_LAYOUT_LEFT_OPTION = "LayoutLeftOption";  //$NON-NLS-1$
    public static final String PROPERTY_LAYOUT_CENTRE_OPTION = "LayoutCentreOption";  //$NON-NLS-1$
    public static final String PROPERTY_LAYOUT_RIGHT_OPTION = "LayoutRightOption";  //$NON-NLS-1$

    /** Values for combo portions of the layout */
    public static final String PROPERTY_LAYOUT_OPTION_NONE = ""; //$NON-NLS-1$
	public static final String PROPERTY_LAYOUT_OPTION_DIFFERENT_DAY_INDICATOR = Messages.getString( "Properties.0" ); //$NON-NLS-1$
	public static final String PROPERTY_LAYOUT_OPTION_TIME = Messages.getString( "Properties.1" ); //$NON-NLS-1$
	public static final String PROPERTY_LAYOUT_OPTION_TIME_ZONE = Messages.getString( "Properties.2" ); //$NON-NLS-1$

    /** Values for text portions of the layout */
    public static final String PROPERTY_LAYOUT_LEFT_TEXT = "LayoutLeftText";  //$NON-NLS-1$
    public static final String PROPERTY_LAYOUT_LEFT_TEXT_DEFAULT = "";  //$NON-NLS-1$
    public static final String PROPERTY_LAYOUT_LEFT_CENTRE_TEXT = "LayoutLeftCentreText";  //$NON-NLS-1$
    public static final String PROPERTY_LAYOUT_LEFT_CENTRE_TEXT_DEFAULT = ": ";  //$NON-NLS-1$
    public static final String PROPERTY_LAYOUT_RIGHT_CENTRE_TEXT = "LayoutRightCentreText";  //$NON-NLS-1$
    public static final String PROPERTY_LAYOUT_RIGHT_CENTRE_TEXT_DEFAULT = "";  //$NON-NLS-1$
    public static final String PROPERTY_LAYOUT_RIGHT_TEXT = "LayoutRightText";  //$NON-NLS-1$
    public static final String PROPERTY_LAYOUT_RIGHT_TEXT_DEFAULT = "";  //$NON-NLS-1$

    /** Option for combining Time Zones. */
    public static final String PROPERTY_COMBINE_TIME_ZONES_OPTION = "CombineTimeZone"; //$NON-NLS-1$    

    /** Separator for Time Zones - only used when combining Time Zones */
    public static final String PROPERTY_COMBINE_TIME_ZONES_SEPARATOR = "Separator"; //$NON-NLS-1$
    public static final String PROPERTY_COMBINE_TIME_ZONES_SEPARATOR_DEFAULT = " | "; //$NON-NLS-1$

    /** Indicators for the previous/next day. */
    public static final String PROPERTY_DIFFERENT_DAY_INDICATOR_PREVIOUS_DAY = "DifferentDayIndicatorPreviousDay"; //$NON-NLS-1$
    public static final String PROPERTY_DIFFERENT_DAY_INDICATOR_NEXT_DAY = "DifferentDayIndicatorNextDay"; //$NON-NLS-1$

	/** User time zones and their display names. */
    public static final String PROPERTY_TIME_ZONES_SELECTED = "TimeZonesSelected"; //$NON-NLS-1$
    public static final String PROPERTY_TIME_ZONES_SELECTED_DISPLAY_NAMES = "TimeZonesSelectedDisplayNames"; //$NON-NLS-1$

	/** Sort date/times either by date/time or user time zone. */
    public static final String PROPERTY_SORT_DATE_TIME = "SortDateTime"; //$NON-NLS-1$
    public static final String PROPERTY_SORT_DATE_TIME_BY_DATE_TIME = "SortDateTimeByDateTime"; //$NON-NLS-1$
    public static final String PROPERTY_SORT_DATE_TIME_BY_TIME_ZONE = "SortDateTimeByTimeZone"; //$NON-NLS-1$

	/** Options for showing the date/time. */
	public static final String PROPERTY_SHOW_OPTION = "ShowOption"; //$NON-NLS-1$
	public static final String PROPERTY_SHOW_DATE_AND_TIME_FULL = "ShowDateTimeFull"; //$NON-NLS-1$
	public static final String PROPERTY_SHOW_DATE_AND_TIME_LONG = "ShowDateTimeLong"; //$NON-NLS-1$
	public static final String PROPERTY_SHOW_DATE_AND_TIME_MEDIUM = "ShowDateTimeMedium"; //$NON-NLS-1$
	public static final String PROPERTY_SHOW_DATE_AND_TIME_SHORT = "ShowDateTimeShort"; //$NON-NLS-1$

	public static final String PROPERTY_SHOW_DATE_FULL = "ShowDateFull"; //$NON-NLS-1$
	public static final String PROPERTY_SHOW_DATE_LONG = "ShowDateLong"; //$NON-NLS-1$
	public static final String PROPERTY_SHOW_DATE_MEDIUM = "ShowDateMedium"; //$NON-NLS-1$
	public static final String PROPERTY_SHOW_DATE_SHORT = "ShowDateShort"; //$NON-NLS-1$

	public static final String PROPERTY_SHOW_TIME_FULL = "ShowTimeFull"; //$NON-NLS-1$
	public static final String PROPERTY_SHOW_TIME_LONG = "ShowTimeLong"; //$NON-NLS-1$
	public static final String PROPERTY_SHOW_TIME_MEDIUM = "ShowTimeMedium"; //$NON-NLS-1$
	public static final String PROPERTY_SHOW_TIME_SHORT = "ShowTimeShort"; //$NON-NLS-1$

	public static final String PROPERTY_SHOW_USER_DEFINED = "ShowUserDefined"; //$NON-NLS-1$
	public static final String PROPERTY_SHOW_USER_DEFINED_PATTERN = "ShowUserDefinedPattern"; //$NON-NLS-1$

	/** Show the times in the tool tip. */
	public static final String PROPERTY_SHOW_TIMES_IN_TOOL_TIP = "ShowTimesInToolTip"; //$NON-NLS-1$

	/** Determine if we combine columns with each other. */
	public static final String PROPERTY_COLUMNS_LEFT_TEXT_AND_LEFT_OPTION_ARE_SEPARATE = "ColumnsLeftTextAndLeftOptionAreSeparate"; //$NON-NLS-1$
	public static final String PROPERTY_COLUMNS_LEFT_OPTION_AND_CENTRE_TEXT_ARE_SEPARATE = "ColumnsLeftOptionAndLeftCentreTextAreSeparate"; //$NON-NLS-1$
	public static final String PROPERTY_COLUMNS_LEFT_CENTRE_TEXT_AND_CENTRE_OPTION_ARE_SEPARATE = "ColumnsLeftCentreTextAndCentreOptionAreSeparate"; //$NON-NLS-1$
	public static final String PROPERTY_COLUMNS_CENTRE_OPTION_AND_RIGHT_CENTRE_TEXT_ARE_SEPARATE = "ColumnsCentreOptionAndRightCentreTextAreSeparate"; //$NON-NLS-1$
	public static final String PROPERTY_COLUMNS_RIGHT_CENTRE_TEXT_AND_RIGHT_OPTION_ARE_SEPARATE = "ColumnsRightCentreTextAndRightOptionAreSeparate"; //$NON-NLS-1$
	public static final String PROPERTY_COLUMNS_RIGHT_OPTION_AND_RIGHT_TEXT_ARE_SEPARATE = "ColumnsRightOptionAndRightTextAreSeparate"; //$NON-NLS-1$

	/** Column alignments. */
	public static final String PROPERTY_COLUMN_LEFT_TEXT_ALIGNMENT = "ColumnLeftTextAlignment"; //$NON-NLS-1$
	public static final String PROPERTY_COLUMN_LEFT_OPTION_ALIGNMENT = "ColumnLeftOptionAlignment"; //$NON-NLS-1$
	public static final String PROPERTY_COLUMN_LEFT_CENTRE_TEXT_ALIGNMENT = "ColumnLeftCentreTextAlignment"; //$NON-NLS-1$
	public static final String PROPERTY_COLUMN_CENTRE_OPTION_ALIGNMENT = "ColumnCentreOptionAlignment"; //$NON-NLS-1$
	public static final String PROPERTY_COLUMN_RIGHT_CENTRE_TEXT_ALIGNMENT = "ColumnRightCentreTextAlignment"; //$NON-NLS-1$
	public static final String PROPERTY_COLUMN_RIGHT_OPTION_ALIGNMENT = "ColumnRightOptionAlignment"; //$NON-NLS-1$
	public static final String PROPERTY_COLUMN_RIGHT_TEXT_ALIGNMENT = "ColumnRightTextAlignment"; //$NON-NLS-1$

	/** Column alignment values. */
	public static final String PROPERTY_COLUMN_ALIGNMENT_LEFT = "left"; //$NON-NLS-1$
	public static final String PROPERTY_COLUMN_ALIGNMENT_CENTRE = "center"; //$NON-NLS-1$
	public static final String PROPERTY_COLUMN_ALIGNMENT_RIGHT = "right"; //$NON-NLS-1$


	private Properties()
    {
        try
		{
        	File propertyDirectory = new File( PROPERTY_DIRECTORY );
        	if( ! propertyDirectory.exists() )
        	{
        		if( ! propertyDirectory.mkdir() )
        		{
        			ms_canCreatePropertyDirectory = false;
        			return;
        		}
        	}

        	File propertyFile = new File( PROPERTY_FILE );
        	if( ! propertyFile.canRead() )
        	{
        		ms_canReadPropertiesFile = false;
        		return;
        	}

        	if( ! propertyFile.canWrite() )
        	{
        		ms_canWritePropertiesFile = false;
        		return;
        	}

            ms_properties.load( new FileInputStream( PROPERTY_FILE ) );
        }
        catch( Throwable throwable ) { ms_properties = new java.util.Properties(); }
    }


    public static Properties getInstance() { return ms_instance; }


    public boolean canCreatePropertyDirectory() { return ms_canCreatePropertyDirectory; }


    public boolean canReadPropertyFile() { return ms_canReadPropertiesFile; }


    public boolean canWritePropertyFile() { return ms_canWritePropertiesFile; }


    public String getProperty( String key, String defaultValue, boolean allowEmptyValue )
    {
        if( key == null ) throw new IllegalArgumentException( "Key cannot be null." ); //$NON-NLS-1$

        Object value = ms_properties.getProperty( key );
        if( value == null )
        	return defaultValue;
        else if( allowEmptyValue )
        	return value.toString();
        else 
        	return value.toString().length() == 0 ? defaultValue : value.toString();
    }


    public boolean getPropertyBoolean( String key, boolean defaultValue )
    {
        if( key == null ) throw new IllegalArgumentException( "Key cannot be null." ); //$NON-NLS-1$

        String val = ms_properties.getProperty( key );
        if( val == null || val.length() == 0 )
            return defaultValue;

        try { return Boolean.valueOf( val ).booleanValue(); }
        catch( Throwable throwable ) { return defaultValue; }
    }


    public int getPropertyInteger( String key, int defaultValue )
    {
        if( key == null ) throw new IllegalArgumentException( "Key cannot be null." ); //$NON-NLS-1$

        String val = ms_properties.getProperty( key );
        if( val == null || val.length() == 0 )
            return defaultValue;

        try { return Integer.valueOf( val ).intValue(); }
        catch( Throwable throwable ) { return defaultValue; }
    }


    public Vector<String> getPropertyList( String key )
    {
        if( key == null ) throw new IllegalArgumentException( "Key cannot be null." ); //$NON-NLS-1$

        String value = ms_properties.getProperty( key );
        if( value == null || value.length() == 0 )
        	return new Vector<String>();

        // Read in each value, separated by a comma.
        // Some tokens are part of a larger token, so we need to read subsequent token(s) before the entire token is complete.
        Vector<String> v = new Vector<String>();
        StringTokenizer tokenizer = new StringTokenizer( value, "," ); //$NON-NLS-1$
        String oldToken = ""; //$NON-NLS-1$
        while( tokenizer.hasMoreElements() )
        {
        	String token = tokenizer.nextToken();
        	if( token.contains( "\\" ) ) //$NON-NLS-1$
        	{
        		// This token is part of a larger token containing "," so keep building it.
        		oldToken = oldToken + token.replace( "\\", "," ); //$NON-NLS-1$  //$NON-NLS-2$
        		continue;
        	}

        	// This token does not contain any "," but may be part of a preceding token...
        	oldToken = oldToken + token;
        	v.add( oldToken );
        	oldToken = ""; //$NON-NLS-1$
        }

        return v;
    }


    public void setPropertyList( String key, Vector<String> values )
    {
        if( key == null ) throw new IllegalArgumentException( "Key cannot be null." ); //$NON-NLS-1$

        if( values == null ) throw new IllegalArgumentException( "Values cannot be null." ); //$NON-NLS-1$

        StringBuilder value = new StringBuilder();
        for( int i = 0; i < values.size(); i++ )
            value.append( values.elementAt( i ).replace( ",", "\\," ) + "," );   //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$

        // Trim the last "," character if it exists.
        if( ! values.isEmpty() )
        	ms_properties.setProperty( key, value.substring( 0, value.length() - 1 ) );
        else
        	ms_properties.setProperty( key, value.toString() );
    }


    public void setProperty( String key, String value )
    {
        if( key == null ) throw new IllegalArgumentException( "Key cannot be null." ); //$NON-NLS-1$

        if( value == null ) throw new IllegalArgumentException( "Value cannot be null." ); //$NON-NLS-1$

        ms_properties.setProperty( key, value );
    }


    public void store()
	{
        try { ms_properties.store( new FileOutputStream( PROPERTY_FILE ), null ); }
        catch( Throwable throwable ) { /** Do nothing. */ }
	}
}
