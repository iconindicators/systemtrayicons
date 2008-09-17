import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;


public class Properties
{
    private static java.util.Properties ms_properties = null;
	private static final String PROPERTY_FILE = System.getProperty( "user.dir" ) + File.separator + "properties" ;  //$NON-NLS-1$//$NON-NLS-2$
    private static Properties ms_instance = new Properties();

	public static final String PROPERTY_SHOW_STARDATE_ISSUE = "ShowStardateIssue"; //$NON-NLS-1$
	public static final String PROPERTY_PAD_STARDATE = "PadStardate"; //$NON-NLS-1$

	public static final String PROPERTY_CHRONOLOGY_BUDDHIST = "ChronologyBuddhist"; //$NON-NLS-1$
	public static final String PROPERTY_CHRONOLOGY_COPTIC = "ChronologyCoptic"; //$NON-NLS-1$
	public static final String PROPERTY_CHRONOLOGY_ETHIOPIC = "ChronologyEthiopic"; //$NON-NLS-1$
	public static final String PROPERTY_CHRONOLOGY_GREGORIAN = "ChronologyGregorian"; //$NON-NLS-1$
	public static final String PROPERTY_CHRONOLOGY_GREGORIAN_JULIAN = "ChronologyGregorianJulian"; //$NON-NLS-1$
	public static final String PROPERTY_CHRONOLOGY_ISLAMIC = "ChronologyIslamic"; //$NON-NLS-1$
	public static final String PROPERTY_CHRONOLOGY_ISO8601 = "ChronologyISO8601"; //$NON-NLS-1$
	public static final String PROPERTY_CHRONOLOGY_JULIAN = "ChronologyJulian"; //$NON-NLS-1$
	public static final String PROPERTY_CHRONOLOGY_STARDATE = "ChronologyStardate"; //$NON-NLS-1$

	public static final String PROPERTY_DATE_FORMAT = "DateTimeFormat"; //$NON-NLS-1$
	public static final String PROPERTY_DATE_FORMAT_LONG = "Long"; //$NON-NLS-1$
	public static final String PROPERTY_DATE_FORMAT_MEDIUM = "Medium"; //$NON-NLS-1$
	public static final String PROPERTY_DATE_FORMAT_SHORT = "Short"; //$NON-NLS-1$

	
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
        if( key == null ) throw new IllegalArgumentException( "Key cannot be null." ); //$NON-NLS-1$

        Object value = ms_properties.getProperty( key );
        return value == null ? defaultValue : value.toString();
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
