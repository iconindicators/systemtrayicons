import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;


public class Properties
{
    private static java.util.Properties ms_properties = null;
	private static final String PROPERTY_FILE = System.getProperty( "user.dir" ) + File.separator + "properties" ;
    private static Properties ms_instance = new Properties();

	public static final String PROPERTY_SHOW_STARDATE_ISSUE = "ShowStardateIssue";
	public static final Boolean PROPERTY_SHOW_STARDATE_ISSUE_DEFAULT_VALUE = true;

	public static final String PROPERTY_CHRONOLOGY_BUDDHIST = "ChronologyBuddhist";
	public static final String PROPERTY_CHRONOLOGY_COPTIC = "ChronologyCoptic";
	public static final String PROPERTY_CHRONOLOGY_ETHIOPIC = "ChronologyEthiopic";
	public static final String PROPERTY_CHRONOLOGY_GREGORIAN = "ChronologyGregorian";
	public static final String PROPERTY_CHRONOLOGY_GREGORIAN_JULIAN = "ChronologyGregorianJulian";
	public static final String PROPERTY_CHRONOLOGY_ISLAMIC = "ChronologyIslamic";
	public static final String PROPERTY_CHRONOLOGY_ISO8601 = "ChronologyISO8601";
	public static final String PROPERTY_CHRONOLOGY_JULIAN = "ChronologyJulian";
	public static final String PROPERTY_CHRONOLOGY_STARDATE = "ChronologyStardate";

	public static final String PROPERTY_DATE_FORMAT = "DateTimeFormat";
	public static final String PROPERTY_DATE_FORMAT_LONG = "Long";
	public static final String PROPERTY_DATE_FORMAT_MEDIUM = "Medium";
	public static final String PROPERTY_DATE_FORMAT_SHORT = "Short";

	
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
        if( ms_properties == null || key == null )
        	return defaultValue;

        Object value = ms_properties.getProperty( key );
        return value == null ? defaultValue : value.toString();
    }


    public boolean getPropertyBoolean( String key, boolean defaultValue )
    {
        if( ms_properties == null || key == null )
            return defaultValue;

        String val = ms_properties.getProperty( key );
        if( val == null || val.length() == 0 )
            return defaultValue;

        try { return Boolean.valueOf( val ).booleanValue(); }
        catch( Throwable throwable ) { return defaultValue; }
    }


    public void setProperty( String key, String value )
    {
        if( ms_properties != null && key != null )
        	ms_properties.setProperty( key, value );
    }


    public void store()
	{
        try { ms_properties.store( new FileOutputStream( PROPERTY_FILE ), null ); }
        catch( Throwable throwable ) { }
	}
}
