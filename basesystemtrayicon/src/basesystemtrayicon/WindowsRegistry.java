package basesystemtrayicon;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;


/**
 * Use reflection to access the Microsoft Windows registry:
 *      https://stackoverflow.com/q/62289/2156453
 */
public class WindowsRegistry
{
    public static final int HKEY_CURRENT_USER = 0x80000001;
    public static final int HKEY_LOCAL_MACHINE = 0x80000002;
    public static final int REG_SUCCESS = 0;
    public static final int REG_NOTFOUND = 2;
    public static final int REG_ACCESSDENIED = 5;

    private static final int KEY_ALL_ACCESS = 0xf003f;
    private static final int KEY_READ = 0x20019;

    private static Preferences userRoot = Preferences.userRoot();
    private static Preferences systemRoot = Preferences.systemRoot();

    private static Class<? extends Preferences>
        userClass = userRoot.getClass();

    private static Method regOpenKey = null;
    private static Method regCloseKey = null;
    private static Method regQueryValueEx = null;
    private static Method regEnumValue = null;
    private static Method regQueryInfoKey = null;
    private static Method regEnumKeyEx = null;
    private static Method regCreateKeyEx = null;
    private static Method regSetValueEx = null;
    private static Method regDeleteKey = null;
    private static Method regDeleteValue = null;


    protected WindowsRegistry()
    {
        /** Do nothing. */
    }


    public static boolean initialise()
    {
        boolean initialised = false;

        // From Java 11+, the first parameter of int should be long.
        // Refer to
        //        https://stackoverflow.com/a/6163701/2156453
        // and see the comment from September 20, 2019.
        //
        // Safe enough to use "1." to check if Java 9 or below.
        String javaVersion = System.getProperty( "java.specification.version" );
        Class<?> clazz;
        if( javaVersion.startsWith( "1." ) )
            clazz = int.class;

        else
            clazz = long.class;

        try
        {
            regOpenKey =
                getMethodForRegistryKey(
                    "WindowsRegOpenKey",
                    clazz,
                    byte[].class,
                    int.class );

            regCloseKey =
                getMethodForRegistryKey(
                    "WindowsRegCloseKey",
                    clazz );

            regQueryValueEx =
                getMethodForRegistryKey(
                    "WindowsRegQueryValueEx",
                    clazz,
                    byte[].class );

            regEnumValue =
                getMethodForRegistryKey(
                    "WindowsRegEnumValue",
                    clazz,
                    int.class,
                    int.class );

            regQueryInfoKey =
                getMethodForRegistryKey(
                    "WindowsRegQueryInfoKey1",
                    clazz );

            regEnumKeyEx =
                getMethodForRegistryKey(
                    "WindowsRegEnumKeyEx",
                    clazz,
                    int.class,
                    int.class );

            regCreateKeyEx =
                getMethodForRegistryKey(
                    "WindowsRegCreateKeyEx",
                    clazz,
                    byte[].class );

            regSetValueEx =
                getMethodForRegistryKey(
                    "WindowsRegSetValueEx",
                    clazz,
                    byte[].class,
                    byte[].class );

            regDeleteValue =
                getMethodForRegistryKey(
                    "WindowsRegDeleteValue",
                    clazz,
                    byte[].class );

            regDeleteKey =
                getMethodForRegistryKey(
                    "WindowsRegDeleteKey",
                    clazz,
                    byte[].class );

            initialised = true;
        }
        catch( NoSuchMethodException | SecurityException exception )
        {
            if( exception.getMessage() != null )
                System.err.println( exception.getMessage() );

            exception.printStackTrace();
        }

        return initialised;
    }


    private static Method
    getMethodForRegistryKey(
        String registryKey,
        Class<?> ... classes )
    throws
        NoSuchMethodException,
        SecurityException
    {
        Method registeryKeyMethod =
            userClass.getDeclaredMethod(
                registryKey,
                classes );

        registeryKeyMethod.setAccessible( true );
        return registeryKeyMethod;
    }


   /**
    * Read a value from key and value name.
    *
    * @param hkey HKEY_CURRENT_USER/HKEY_LOCAL_MACHINE
    * @param key
    * @param valueName
    *
    * @return the value
    *
    * @throws IllegalArgumentException
    * @throws IllegalAccessException
    * @throws InvocationTargetException
    */
    public static String
    readString( int hkey, String key, String valueName )
    throws
        IllegalAccessException,
        IllegalArgumentException,
        InvocationTargetException
    {
        return
            readString(
                getPreferences( hkey ),
                hkey,
                key,
                valueName );
    }


   /**
    * Read value(s) and value name(s) form given key.
    *
    * @param hkey HKEY_CURRENT_USER/HKEY_LOCAL_MACHINE
    * @param key
    *
    * @return the value name(s) plus the value(s)
    *
    * @throws IllegalArgumentException
    * @throws IllegalAccessException
    * @throws InvocationTargetException
    */
    public static Map<String, String>
    readStringValues( int hkey, String key )
    throws
        IllegalAccessException,
        IllegalArgumentException,
        InvocationTargetException
    {
        return
            readStringValues(
                getPreferences( hkey ),
                hkey,
                key );
    }


   /**
    * Read the value name(s) from a given key
    *
    * @param hkey HKEY_CURRENT_USER/HKEY_LOCAL_MACHINE
    * @param key
    *
    * @return the value name(s)
    *
    * @throws IllegalArgumentException
    * @throws IllegalAccessException
    * @throws InvocationTargetException
    */
    public static List<String>
    readStringSubKeys( int hkey, String key )
    throws
        IllegalAccessException,
        IllegalArgumentException,
        InvocationTargetException
    {
        return
            readStringSubKeys(
                getPreferences( hkey ),
                hkey,
                key );
    }


   /**
    * Create a key.
    *
    * @param hkey HKEY_CURRENT_USER/HKEY_LOCAL_MACHINE
    * @param key
    *
    * @throws IllegalArgumentException
    * @throws IllegalAccessException
    * @throws InvocationTargetException
    */
    public static void createKey( int hkey, String key )
    throws
        IllegalAccessException,
        IllegalArgumentException,
        InvocationTargetException
    {
        Preferences root = getPreferences( hkey );
        int[] ret = createKey( root, hkey, key );
        regCloseKey.invoke(
            root,
            new Object[] { Integer.valueOf( ret[ 0 ] ) } );

        if( ret[ 1 ] != REG_SUCCESS )
            throw new IllegalArgumentException(
                "rc = " + ret[ 1 ] + "  key = " + key );
    }


   /**
    * Write a value in a given key/value name.
    *
    * @param hkey
    * @param key
    * @param valueName
    * @param value
    *
    * @throws IllegalArgumentException
    * @throws IllegalAccessException
    * @throws InvocationTargetException
    */
    public static void
    writeStringValue(int hkey, String key, String valueName, String value )
    throws
        IllegalAccessException,
        IllegalArgumentException,
        InvocationTargetException
    {
        writeStringValue(
            getPreferences( hkey ),
            hkey,
            key,
            valueName,
            value );
    }


   /**
    * Delete a given key.
    *
    * @param hkey
    * @param key
    *
    * @throws IllegalArgumentException
    * @throws IllegalAccessException
    * @throws InvocationTargetException
    */
    public static void
    deleteKey( int hkey, String key )
    throws
        IllegalAccessException,
        IllegalArgumentException,
        InvocationTargetException
    {
        Preferences root = getPreferences( hkey );
        int rc = deleteKey( root, hkey, key );
        if( rc != REG_SUCCESS )
            throw new IllegalArgumentException(
                "rc = " + rc + "  key = " + key );
    }


   /**
    * Delete a value from a given key/value name.
    *
    * @param hkey
    * @param key
    * @param value
    *
    * @throws IllegalArgumentException
    * @throws IllegalAccessException
    * @throws InvocationTargetException
   */
    public static void deleteValue( int hkey, String key, String value )
    throws
        IllegalAccessException,
        IllegalArgumentException,
        InvocationTargetException
    {
        Preferences root = getPreferences( hkey );
        int rc = deleteValue( root, hkey, key, value );
        if( rc != REG_SUCCESS )
            throw new IllegalArgumentException(
                "rc = " + rc + "  key = " + key + "  value = " + value );
    }


    private static Preferences
    getPreferences( int hkey )
    throws IllegalArgumentException
    {
        Preferences root = null;
        if( hkey == HKEY_LOCAL_MACHINE )
            root = systemRoot;

        else if( hkey == HKEY_CURRENT_USER )
            root = userRoot;

        else
            throw new IllegalArgumentException( "hkey = " + hkey );

        return root;
    }


    private static int
    deleteValue( Preferences root, int hkey, String key, String value )
    throws
        IllegalAccessException,
        IllegalArgumentException,
        InvocationTargetException
    {
        int[] handles =
            ( int[]) regOpenKey.invoke(
                root,
                new Object[] {
                    Integer.valueOf( hkey ),
                    toCstr( key ),
                    Integer.valueOf( KEY_ALL_ACCESS ) } );

        if( handles[ 1 ] != REG_SUCCESS )
            return handles[ 1 ];

        int rc =
            ( (Integer)regDeleteValue.invoke(
                root,
                new Object[] {
                    Integer.valueOf( handles[ 0 ] ),
                    toCstr( value ) } ) ).intValue();

        regCloseKey.invoke(
            root,
            new Object[] {
                Integer.valueOf( handles[ 0 ] ) } );

        return rc;
    }


    private static int deleteKey( Preferences root, int hkey, String key )
    throws
        IllegalAccessException,
        IllegalArgumentException,
        InvocationTargetException
    {
        return
            ( (Integer)regDeleteKey.invoke(
                root,
                new Object[] {
                    Integer.valueOf( hkey ),
                    toCstr( key ) } ) ).intValue();
    }


    private static String
    readString( Preferences root, int hkey, String key, String value )
    throws
        IllegalAccessException,
        IllegalArgumentException,
        InvocationTargetException
    {
        int[] handles =
            ( int[] )regOpenKey.invoke(
                root,
                new Object[] {
                    Integer.valueOf( hkey ),
                    toCstr( key ), Integer.valueOf( KEY_READ ) } );

        if( handles[ 1 ] != REG_SUCCESS )
            return null;

        byte[] valb =

            ( byte[] )regQueryValueEx.invoke(
                root,
                new Object[] {
                    Integer.valueOf( handles[ 0 ] ),
                    toCstr( value ) } );

        regCloseKey.invoke(
            root,
            new Object[] {
                Integer.valueOf( handles[ 0 ] ) } );

        return( valb != null ? new String( valb ).trim() : null );
    }


    private static Map<String, String>
    readStringValues( Preferences root, int hkey, String key )
    throws
        IllegalAccessException,
        IllegalArgumentException,
        InvocationTargetException
    {
        HashMap<String, String> results = new HashMap<>();
        int[] handles =
            ( int[] )regOpenKey.invoke(
                root,
                new Object[] {
                    Integer.valueOf( hkey ),
                    toCstr( key ),
                    Integer.valueOf( KEY_READ ) } );

        if( handles[ 1 ] != REG_SUCCESS )
            return null;

        int[] info =
            ( int[] )regQueryInfoKey.invoke(
                root,
                new Object[] {
                    Integer.valueOf( handles[ 0 ] ) } );

        int count = info[ 2 ];
        int maxlen = info[ 3 ];
        for( int index = 0; index < count; index++ )
        {
            byte[] name =
                ( byte[] )regEnumValue.invoke(
                    root,
                    new Object[] {
                        Integer.valueOf( handles[ 0 ] ),
                        Integer.valueOf( index ),
                        Integer.valueOf( maxlen + 1 ) } );

            String value = readString( hkey, key, new String( name ) );
            results.put( new String( name ).trim(), value );
        }

        regCloseKey.invoke(
            root,
            new Object[] {
                Integer.valueOf( handles[ 0 ] ) } );

        return results;
    }


    private static List<String>
    readStringSubKeys( Preferences root, int hkey, String key )
    throws
        IllegalAccessException,
        IllegalArgumentException,
        InvocationTargetException
    {
        List<String> results = new ArrayList<>();
        int[] handles =
            ( int[] )regOpenKey.invoke(
                root,
                new Object[] {
                    Integer.valueOf( hkey ),
                    toCstr( key ),
                    Integer.valueOf( KEY_READ ) } );

        if( handles[ 1 ] != REG_SUCCESS )
            return null;

        int[] info =
            ( int[] )regQueryInfoKey.invoke(
                root,
                new Object[] {
                    Integer.valueOf( handles[ 0 ] ) } );

        int count = info[ 0 ];
        int maxlen = info[ 3 ];
        for( int index = 0; index < count; index++ )
        {
            byte[] name =
                ( byte[] )regEnumKeyEx.invoke(
                    root,
                    new Object[] {
                        Integer.valueOf( handles[ 0 ] ),
                        Integer.valueOf( index ),
                        Integer.valueOf( maxlen + 1 ) } );

            results.add( new String( name ).trim() );
        }

        regCloseKey.invoke(
            root,
            new Object[] {
                Integer.valueOf( handles[ 0 ] ) } );

        return results;
    }


    private static int []
    createKey( Preferences root, int hkey, String key )
    throws
        IllegalAccessException,
        IllegalArgumentException,
        InvocationTargetException
    {
        return
            ( int[] )regCreateKeyEx.invoke(
                root,
                new Object[] {
                    Integer.valueOf( hkey ),
                    toCstr( key ) } );
    }


    private static void
    writeStringValue(
        Preferences root,
        int hkey,
        String key,
        String valueName,
        String value
    )
    throws
        IllegalAccessException,
        IllegalArgumentException,
        InvocationTargetException
    {
        int[] handles =
            ( int[] )regOpenKey.invoke(
                root,
                new Object[] {
                    Integer.valueOf( hkey ),
                    toCstr( key ),
                    Integer.valueOf( KEY_ALL_ACCESS ) } );

        regSetValueEx.invoke(
            root,
            new Object[] {
                Integer.valueOf( handles[ 0 ] ),
                toCstr( valueName ),
                toCstr( value ) } );

        regCloseKey.invoke(
            root,
            new Object[] {
                Integer.valueOf( handles[ 0 ] ) } );
    }


    private static byte[] toCstr( String str )
    {
        byte[] result = new byte[ str.length() + 1 ];
        for( int i = 0; i < str.length(); i++ )
            result[ i ] = (byte)str.charAt( i );

        result[ str.length() ] = 0;
        return result;
    }
}
