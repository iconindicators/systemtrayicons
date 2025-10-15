package basesystemtrayicon;


import java.lang.reflect.InvocationTargetException;


/*
 * When running on Windows
 *      Vista 64 bit
 *      Windows 7
 *      Windows 8
 *      Windows 10
 *      (presumably Windows 11)
 * getting the following error in the console:
 *
 *  java.util.prefs.WindowsPreferences <init>
 *  WARNING: Could not open/create prefs root node Software\JavaSoft\Prefs 
 *    at root 0x80000002. Windows RegCreateKeyEx(...) returned error code 5.
 *
 * This is a known bug in Java:
 *   https://stackoverflow.com/a/42050131/2156453
 *   https://stackoverflow.com/q/16428098/2156453
 */
public class SystemStart
{
    private static final String
        MICROSOFT_WINDOWS_REGISTRY_KEY =
            "Software\\Microsoft\\Windows\\CurrentVersion\\Run";

    private static String MICROSOFT_WINDOWS_REGISTRY_VALUE_NAME = null;
    private static String MICROSOFT_WINDOWS_REGISTRY_VALUE_DATA = null;

    
    protected SystemStart()
    {
        /** Do nothing. */
    }


    public static boolean
    initialise(
        String applicationNameHumanReadable,
        String executableName )
    {
        MICROSOFT_WINDOWS_REGISTRY_VALUE_NAME = applicationNameHumanReadable;
        MICROSOFT_WINDOWS_REGISTRY_VALUE_DATA =
            "\""
            +
            System.getProperties().get( "user.dir" )
            +
            "\\"
            +
            executableName
            +
            "\"";

        return true;
    }


    public static boolean getRunOnSystemStart()
    {
        boolean runOnSystemStart = false;
        if( Utils.isWindows() )
            runOnSystemStart = getRunOnSystemStartWindows();

        return runOnSystemStart;
    }


    protected static boolean getRunOnSystemStartWindows()
    {
        boolean runOnSystemStart;
        try
        {
            String registryValue =
                WindowsRegistry.readString(
                    WindowsRegistry.HKEY_CURRENT_USER,
                    MICROSOFT_WINDOWS_REGISTRY_KEY,
                    MICROSOFT_WINDOWS_REGISTRY_VALUE_NAME );

            runOnSystemStart =
                registryValue != null
                &&
                registryValue.contentEquals( MICROSOFT_WINDOWS_REGISTRY_VALUE_DATA ); 
        }
        catch( 
            IllegalAccessException |
            IllegalArgumentException |
            InvocationTargetException exception )
        {
            System.err.println(
                "Unable to get \"run on system start\" in Windows registry." );

            exception.printStackTrace();
            runOnSystemStart = false;
        }

        return runOnSystemStart;
    }


    public static boolean setRunOnSystemStart( boolean runOnSystemStart )
    {
        boolean success = true;
        if( Utils.isWindows() )
            success = setRunOnSystemStartWindows( runOnSystemStart );

        return success;
    }


    protected static boolean
    setRunOnSystemStartWindows(
        boolean runOnSystemStart )
    {
        boolean success;
        try
        {
            String registryValue =
                WindowsRegistry.readString(
                    WindowsRegistry.HKEY_CURRENT_USER,
                    MICROSOFT_WINDOWS_REGISTRY_KEY,
                    MICROSOFT_WINDOWS_REGISTRY_VALUE_NAME );

            if( runOnSystemStart )
            {
                if( registryValue == null )
                    WindowsRegistry.createKey(
                        WindowsRegistry.HKEY_CURRENT_USER,
                        MICROSOFT_WINDOWS_REGISTRY_KEY );

                WindowsRegistry.writeStringValue(
                    WindowsRegistry.HKEY_CURRENT_USER,
                    MICROSOFT_WINDOWS_REGISTRY_KEY,
                    MICROSOFT_WINDOWS_REGISTRY_VALUE_NAME,
                    MICROSOFT_WINDOWS_REGISTRY_VALUE_DATA );
            }
            else
            {
                if( registryValue != null )
                    WindowsRegistry.deleteValue(
                        WindowsRegistry.HKEY_CURRENT_USER,
                        MICROSOFT_WINDOWS_REGISTRY_KEY,
                        MICROSOFT_WINDOWS_REGISTRY_VALUE_NAME );
            }

            success = true;
        }
        catch( 
            IllegalAccessException |
            IllegalArgumentException |
            InvocationTargetException exception )
        {
            System.err.println(
                "Unable to set \"run on system start\" in Windows registry." );

            exception.printStackTrace();
            success = false;
        }

        return success;
    }
}
