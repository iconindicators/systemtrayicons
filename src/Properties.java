import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;


public class Properties
{
	public static final String PROPERTY_DIRECTORY = System.getProperty( "user.home" ) + File.separator + "." + StardateSystemTray.APPLICATION_NAME.toLowerCase().replace( " ", "" ) ; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	public static final String PROPERTY_FILE = PROPERTY_DIRECTORY + File.separator + "properties" ; //$NON-NLS-1$

	private java.util.Properties m_properties = new java.util.Properties();
    private static Properties ms_instance = new Properties();
    private static boolean ms_canCreatePropertyDirectory = true;
    private static boolean ms_canReadPropertiesFile = true;
    private static boolean ms_canWritePropertiesFile = true;

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

            m_properties.load( new FileInputStream( PROPERTY_FILE ) );
        }
        catch( Exception exception ) { m_properties = new java.util.Properties(); }
    }


    public static Properties getInstance() { return ms_instance; }


    public static boolean canCreatePropertyDirectory() { return ms_canCreatePropertyDirectory; }


    public static boolean canReadPropertyFile() { return ms_canReadPropertiesFile; }


    public static boolean canWritePropertyFile() { return ms_canWritePropertiesFile; }


    public String getProperty( String key, String defaultValue )
    {
        if( key == null ) throw new IllegalArgumentException( "Key cannot be null." ); //$NON-NLS-1$

        Object value = m_properties.getProperty( key );
        return value == null ? defaultValue : value.toString();
    }


    public boolean getPropertyBoolean( String key, boolean defaultValue )
    {
        if( key == null ) throw new IllegalArgumentException( "Key cannot be null." ); //$NON-NLS-1$

        String val = m_properties.getProperty( key );
        if( val == null || val.length() == 0 )
            return defaultValue;

        try { return Boolean.valueOf( val ).booleanValue(); }
        catch( Exception exception ) { return defaultValue; }
    }


    public void setProperty( String key, String value )
    {
        if( key == null ) throw new IllegalArgumentException( "Key cannot be null." ); //$NON-NLS-1$

        if( value == null ) throw new IllegalArgumentException( "Value cannot be null." ); //$NON-NLS-1$

        m_properties.setProperty( key, value );
    }


    public void store()
	{
        try { m_properties.store( new FileOutputStream( PROPERTY_FILE ), null ); }
        catch( Exception exception ) { /** Do nothing. */ }
	}
}