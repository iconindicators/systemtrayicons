package basesystemtrayicon;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;


public abstract class PropertiesBase
{
	protected static final java.util.Properties
        ms_properties = new java.util.Properties();

    protected static File ms_propertyDirectory = null;
    protected static File ms_propertyFile = null;


    public enum Status
    {
        OK,
        UNABLE_TO_CREATE_PROPERY_DIRECTORY,
        UNABLE_TO_READ_PROPERTY_FILE,
    }


    /** Property for version. */
    public static final String KEY_VERSION = "Version";

    /** Properties for showing date/time. */
    public static final String KEY_DATE_FORMAT = "DateFormat";
    public static final String KEY_DATE_TIME_FORMAT = "DateTimeFormat";
    public static final String KEY_USER_DEFINED = "UserDefined";

    public static final String VALUE_DATE_TIME_FULL = "DateTimeFull";
    public static final String VALUE_DATE_TIME_LONG = "DateTimeLong";
    public static final String VALUE_DATE_TIME_MEDIUM = "DateTimeMedium";
    public static final String VALUE_DATE_TIME_SHORT = "DateTimeShort";

    public static final String VALUE_DATE_FULL = "DateFull";
    public static final String VALUE_DATE_LONG = "DateLong";
    public static final String VALUE_DATE_MEDIUM = "DateMedium";
    public static final String VALUE_DATE_SHORT = "DateShort";

    public static final String VALUE_TIME_FULL = "TimeFull";
    public static final String VALUE_TIME_LONG = "TimeLong";
    public static final String VALUE_TIME_MEDIUM = "TimeMedium";
    public static final String VALUE_TIME_SHORT = "TimeShort";

    public static final String VALUE_USER_DEFINED = "UserDefined";

    public static final String VALUE_DATE_FORMAT_DEFAULT = VALUE_DATE_LONG;
    public static final String VALUE_DATE_TIME_FORMAT_DEFAULT = VALUE_TIME_SHORT;
    public static final String VALUE_USER_DEFINED_DATE_DEFAULT = "d MMMM y"; // Date long.
    public static final String VALUE_USER_DEFINED_TIME_DEFAULT = "h:mm a"; // Time short.


    protected PropertiesBase()
    {
        /** Do nothing. */
    }


    public static Status initialise( String packageNameOfConcreteClass )
    {
        Status status = Status.OK;
    	ms_propertyDirectory =
            new File(
                System.getProperty( "user.home" )
                +
                File.separator
                +
                "."
                +
                packageNameOfConcreteClass );

    	if( ! ms_propertyDirectory.exists() )
            if( ! ms_propertyDirectory.mkdir() )
                status = Status.UNABLE_TO_CREATE_PROPERY_DIRECTORY;

        if( status == Status.OK )
        {
            ms_propertyFile = new File( ms_propertyDirectory, "properties" );
            if( ms_propertyFile.exists() )
                try(
                    FileInputStream fileInputStream =
                        new FileInputStream( ms_propertyFile ); )
                {
                    ms_properties.load( fileInputStream );
                }
                catch( IOException ioException )
                {
                    status = Status.UNABLE_TO_READ_PROPERTY_FILE;

                    if( ioException.getMessage() != null )
                        System.err.println( ioException.getMessage() );

                    ioException.printStackTrace();
                }
        }

        return status;
    }


    public static File getPropertyDirectory()
    {
        return ms_propertyDirectory;
    }


    public static File getPropertyFile()
    {
        return ms_propertyFile;
    }


    public static boolean containsKey( String key )
    {
        return ms_properties.containsKey( key );
    }


    public static String getProperty( String key )
    {
        if( ms_propertyFile == null )
            throw new IllegalStateException( "Properties must be initialised." );

        if( key == null )
            throw new IllegalArgumentException( "Key cannot be null." );

        return ms_properties.getProperty( key );
    }


    public static String getProperty( String key, String defaultValue )
    {
        if( ms_propertyFile == null )
            throw new IllegalStateException( "Properties must be initialised." );

        if( key == null )
            throw new IllegalArgumentException( "Key cannot be null." );

        Object value = ms_properties.getProperty( key );
        return value == null ? defaultValue : value.toString();
    }


    public static boolean getPropertyBoolean( String key, boolean defaultValue )
    {
        if( ms_propertyFile == null )
        	throw new IllegalStateException( "Properties must be initialised." );

        if( key == null )
        	throw new IllegalArgumentException( "Key cannot be null." );

        return
            Boolean.parseBoolean(
                ms_properties.getProperty( key, String.valueOf( defaultValue ) ) );
    }


    public static void setProperty( String key, String value )
    {
        if( ms_propertyFile == null )
        	throw new IllegalStateException( "Properties must be initialised." );

        if( key == null )
        	throw new IllegalArgumentException( "Key cannot be null." );

        if( value == null )
        	throw new IllegalArgumentException( "Value cannot be null." );

        ms_properties.setProperty( key, value );
    }


    public static Vector<String> getPropertyList( String key )
    {
        if( ms_propertyFile == null )
            throw new IllegalStateException( "Properties must be initialised." );

        if( key == null )
            throw new IllegalArgumentException( "Key cannot be null." );

        Vector<String> values = new Vector<>();
        String value = ms_properties.getProperty( key );
        if( value != null && value.length() > 0 )
        {
            // Read in each value, separated by a comma.
            // A token may contain a comma, requiring finesse!
            StringTokenizer tokenizer = new StringTokenizer( value, "," );
            StringBuilder previousTokens = new StringBuilder();
            while( tokenizer.hasMoreElements() )
            {
                String token = tokenizer.nextToken();
                if( token.contains( "\\" ) )
                    // This token is part of a larger token containing a comma
                    // so keep building...
                    previousTokens.append( token.replace( "\\", "," ) );

                else
                {
                    // This token does not contain a comma, but may be part of
                    // a preceding token...
                    previousTokens.append( token );
                    values.add( previousTokens.toString() );
                    previousTokens.setLength( 0 );
                }
            }
        }

        return values;
    }


    public static void setPropertyList( String key, Vector<String> values )
    {
        if( ms_propertyFile == null )
            throw new IllegalStateException( "Properties must be initialised." );

        if( key == null )
            throw new IllegalArgumentException( "Key cannot be null." );

        if( values == null )
            throw new IllegalArgumentException( "Values cannot be null." );

        if( values.isEmpty() )
            ms_properties.setProperty( key, "" );

        else
        {
            StringBuilder value = new StringBuilder();
            for( String string : values )
                value.append( string.replace( ",", "\\," ) + "," ); 

            // Trim the last "," character.
            ms_properties.setProperty(
                key,
                value.substring( 0, value.length() - 1 ) );
        }
    }


    public static void removeProperty( String key )
    {
        if( ms_propertyFile == null )
        	throw new IllegalStateException( "Properties must be initialised." );

        if( key == null )
        	throw new IllegalArgumentException( "Key cannot be null." );

        ms_properties.remove( key );
    }


    public static boolean store()
	{
        if( ms_propertyFile == null )
        	throw new IllegalStateException( "Properties must be initialised." );

        boolean stored;
        try(
            FileOutputStream fileOutputStream =
                new FileOutputStream( ms_propertyFile ); )
        {
            ms_properties.store( fileOutputStream, null );
            stored = true;
    	}
        catch( IOException ioException )
        {
            stored = false;
            if( ioException.getMessage() != null )
                System.err.println( ioException.getMessage() );

            ioException.printStackTrace();
        }

        return stored;
	}


    /**
     * If the given old key exists, read the value, write to the new key and
     * delete the old key.
     *
     * @param keyOld
     * @param keyNew
     */
    protected static void upgradeKey( String keyOld, String keyNew )
    {
        if( containsKey( keyOld ) )
        {
            setProperty( keyNew, getProperty( keyOld ) );
            if( keyOld != keyNew )
                removeProperty( keyOld );
        }
    }
}
