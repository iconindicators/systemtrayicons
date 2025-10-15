package basesystemtrayicon;


import java.util.ArrayList;
import java.util.MissingResourceException;
import java.util.ResourceBundle;


public class Messages 
{
    private static final String BUNDLE_DIRECTORY = "i18n/";

    private static ResourceBundle ms_resourceBundleBase = null;
    private static ResourceBundle ms_resourceBundleImplementation = null;

    /** Date/time formats in locale. */
    public static String LOCALE_DATE_TIME_FULL;
    public static String LOCALE_DATE_TIME_LONG;
    public static String LOCALE_DATE_TIME_MEDIUM;
    public static String LOCALE_DATE_TIME_SHORT;
    public static String LOCALE_DATE_FULL;
    public static String LOCALE_DATE_LONG;
    public static String LOCALE_DATE_MEDIUM;
    public static String LOCALE_DATE_SHORT;
    public static String LOCALE_TIME_FULL;
    public static String LOCALE_TIME_LONG;
    public static String LOCALE_TIME_MEDIUM;
    public static String LOCALE_TIME_SHORT;
    public static String LOCALE_USER_DEFINED;


    protected Messages()
    {
        /** Do nothing. */
    }


    /**
     * The base class and implementing/concrete class have their own i18n,
     * so load each separately.
     *
     * @throws MissingResourceException if either the base or concrete bundles
     * cannot be found.
     */
    public static void
    initialise( String packageNameOfConcreteClass )
    throws MissingResourceException
    {
        ms_resourceBundleBase = 
            ResourceBundle.getBundle(
                BUNDLE_DIRECTORY + Messages.class.getPackage().getName() );

        ms_resourceBundleImplementation =
            ResourceBundle.getBundle(
                BUNDLE_DIRECTORY + packageNameOfConcreteClass );

        LOCALE_DATE_TIME_FULL = Messages.getString( "Messages.2" );
        LOCALE_DATE_TIME_LONG = Messages.getString( "Messages.3" );
        LOCALE_DATE_TIME_MEDIUM = Messages.getString( "Messages.4" );
        LOCALE_DATE_TIME_SHORT = Messages.getString( "Messages.5" );
        LOCALE_DATE_FULL = Messages.getString( "Messages.6" );
        LOCALE_DATE_LONG = Messages.getString( "Messages.7" );
        LOCALE_DATE_MEDIUM = Messages.getString( "Messages.8" );
        LOCALE_DATE_SHORT = Messages.getString( "Messages.9" );
        LOCALE_TIME_FULL = Messages.getString( "Messages.10" );
        LOCALE_TIME_LONG = Messages.getString( "Messages.11" );
        LOCALE_TIME_MEDIUM = Messages.getString( "Messages.12" );
        LOCALE_TIME_SHORT = Messages.getString( "Messages.13" );
        LOCALE_USER_DEFINED = Messages.getString( "Messages.14" );
    }


    public static String getString( String key ) 
    {
        if( key == null || key.length() == 0 )
            throw new IllegalArgumentException( "Key cannot be null or empty." );

        String string = null;
        if( ms_resourceBundleBase.containsKey( key ) )
            string = ms_resourceBundleBase.getString( key );

        else if( ms_resourceBundleImplementation.containsKey( key ) )
            string = ms_resourceBundleImplementation.getString( key );

        else
        {
            string = '!' + key + '!';
            System.err.println( "Unable to locate key = '" + key + "'" );
        }

        return string;
    }


    public static String[]
    getDateTimeFormatsInLocale(
        boolean dates,
        boolean times,
        boolean userDefined )
    {
        ArrayList<String> dateTimeFormats = new ArrayList<>();
        if( dates && times )
        {
            dateTimeFormats.add( LOCALE_DATE_TIME_FULL );
            dateTimeFormats.add( LOCALE_DATE_TIME_LONG );
            dateTimeFormats.add( LOCALE_DATE_TIME_MEDIUM );
            dateTimeFormats.add( LOCALE_DATE_TIME_SHORT );
        }

        if( dates )
        {
            dateTimeFormats.add( LOCALE_DATE_FULL );
            dateTimeFormats.add( LOCALE_DATE_LONG );
            dateTimeFormats.add( LOCALE_DATE_MEDIUM );
            dateTimeFormats.add( LOCALE_DATE_SHORT );            
        }
        
        if( times )
        {
            dateTimeFormats.add( LOCALE_TIME_FULL );
            dateTimeFormats.add( LOCALE_TIME_LONG );
            dateTimeFormats.add( LOCALE_TIME_MEDIUM );
            dateTimeFormats.add( LOCALE_TIME_SHORT );            
        }

        if( userDefined )
            dateTimeFormats.add( LOCALE_USER_DEFINED );

        return dateTimeFormats.toArray( new String[ 0 ] );
    }


    public static String
    convertDateTimePropertyValueToLocale(
        String dateTimePropertyValue )
    {
        if( dateTimePropertyValue == null )
            throw new IllegalArgumentException( "Date/time property value cannot be null." );

        String locale;
        switch( dateTimePropertyValue )
        {
            case PropertiesBase.VALUE_DATE_TIME_FULL:
                locale = LOCALE_DATE_TIME_FULL;
                break;

            case PropertiesBase.VALUE_DATE_TIME_LONG:
                locale = LOCALE_DATE_TIME_LONG;
                break;

            case PropertiesBase.VALUE_DATE_TIME_MEDIUM:
                locale = LOCALE_DATE_TIME_MEDIUM;
                break;

            case PropertiesBase.VALUE_DATE_TIME_SHORT:
                locale = LOCALE_DATE_TIME_SHORT;
                break;

            case PropertiesBase.VALUE_DATE_FULL:
                locale = LOCALE_DATE_FULL;
                break;

            case PropertiesBase.VALUE_DATE_LONG:
                locale = LOCALE_DATE_LONG;
                break;

            case PropertiesBase.VALUE_DATE_MEDIUM:
                locale = LOCALE_DATE_MEDIUM;
                break;

            case PropertiesBase.VALUE_DATE_SHORT:
                locale = LOCALE_DATE_SHORT;
                break;

            case PropertiesBase.VALUE_TIME_FULL:
                locale = LOCALE_TIME_FULL;
                break;

            case PropertiesBase.VALUE_TIME_LONG:
                locale = LOCALE_TIME_LONG;
                break;

            case PropertiesBase.VALUE_TIME_MEDIUM:
                locale = LOCALE_TIME_MEDIUM;
                break;

            case PropertiesBase.VALUE_TIME_SHORT:
                locale = LOCALE_TIME_SHORT;
                break;

            case PropertiesBase.VALUE_USER_DEFINED:
                locale = LOCALE_USER_DEFINED;
                break;

            default:
                String message =
                    "Unknown date/time property value: "
                    +
                    dateTimePropertyValue;

                throw new IllegalArgumentException( message );
        }

        return locale;
    }


    public static String
    convertDateTimeLocaleToPropertyValue( String dateTimeLocale )
    {
        if( dateTimeLocale == null )
            throw new IllegalArgumentException( "Date/time locale cannot be null." );

        String propertyValue;
        if( LOCALE_DATE_TIME_FULL.equals( dateTimeLocale ) )
            propertyValue = PropertiesBase.VALUE_DATE_TIME_FULL;

        else if( LOCALE_DATE_TIME_LONG.equals( dateTimeLocale ) )
            propertyValue = PropertiesBase.VALUE_DATE_TIME_LONG;

        else if( LOCALE_DATE_TIME_MEDIUM.equals( dateTimeLocale ) )
            propertyValue = PropertiesBase.VALUE_DATE_TIME_MEDIUM;

        else if( LOCALE_DATE_TIME_SHORT.equals( dateTimeLocale ) )
            propertyValue = PropertiesBase.VALUE_DATE_TIME_SHORT;

        else if( LOCALE_DATE_FULL.equals( dateTimeLocale ) )
            propertyValue = PropertiesBase.VALUE_DATE_FULL;

        else if( LOCALE_DATE_LONG.equals( dateTimeLocale ) )
            propertyValue = PropertiesBase.VALUE_DATE_LONG;

        else if( LOCALE_DATE_MEDIUM.equals( dateTimeLocale ) )
            propertyValue = PropertiesBase.VALUE_DATE_MEDIUM;

        else if( LOCALE_DATE_SHORT.equals( dateTimeLocale ) )
            propertyValue = PropertiesBase.VALUE_DATE_SHORT;

        else if( LOCALE_TIME_FULL.equals( dateTimeLocale ) )
            propertyValue = PropertiesBase.VALUE_TIME_FULL;

        else if( LOCALE_TIME_LONG.equals( dateTimeLocale ) )
            propertyValue = PropertiesBase.VALUE_TIME_LONG;

        else if( LOCALE_TIME_MEDIUM.equals( dateTimeLocale ) )
            propertyValue = PropertiesBase.VALUE_TIME_MEDIUM;

        else if( LOCALE_TIME_SHORT.equals( dateTimeLocale ) )
            propertyValue = PropertiesBase.VALUE_TIME_SHORT;

        else if( LOCALE_USER_DEFINED.equals( dateTimeLocale ) )
            propertyValue = PropertiesBase.VALUE_USER_DEFINED;

        else
            throw new IllegalArgumentException( "Unknown date/time locale: " + dateTimeLocale );

        return propertyValue;
    }
}
