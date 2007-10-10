import ca.beq.util.win32.registry.RegistryKey;
import ca.beq.util.win32.registry.RegistryValue;
import ca.beq.util.win32.registry.RootKey;
import ca.beq.util.win32.registry.ValueType;


public class SystemStart
{
	// Various types of operating system as per System property "os.name" (see http://tolstoy.com/samizdat/sysprops.html).
	private static final String OS_NAME = "os.name";
	private static final String OS_NAME_MICROSOFT_WINDOWS = "Windows";

	private static final String MICROSOFT_WINDOWS_REGISTRY_KEY = "Software\\Microsoft\\Windows\\CurrentVersion\\Run";
	private static final String MICROSOFT_WINDOWS_REGISTRY_VALUE_NAME = "WorldTimeSystemTray";
	private static final String MICROSOFT_WINDOWS_REGISTRY_VALUE_DATA = System.getProperties().get( "user.dir" ) + "\\WorldTimeSystemTray.exe";


    public static boolean isMicrosoftWindows()
    {
    	return System.getProperty( OS_NAME ).toLowerCase().contains( OS_NAME_MICROSOFT_WINDOWS.toLowerCase() );
    }


    public static boolean runOnSystemStart()
    {
    	if( isMicrosoftWindows() )
    		return runOnSystemStartMicrosoftWindows();

    	return false;
    }


    private static boolean runOnSystemStartMicrosoftWindows()
    {
    	RegistryKey registryKey = new RegistryKey( RootKey.HKEY_CURRENT_USER, MICROSOFT_WINDOWS_REGISTRY_KEY );

    	return
    		registryKey.exists() &&
    		registryKey.hasValue( MICROSOFT_WINDOWS_REGISTRY_VALUE_NAME ) &&
    		registryKey.getValue( MICROSOFT_WINDOWS_REGISTRY_VALUE_NAME ).getStringValue().equals( MICROSOFT_WINDOWS_REGISTRY_VALUE_DATA );
    }


    public static void setRunOnSystemStart( boolean b )
    {
    	if( isMicrosoftWindows() )
    		setRunOnSystemStartMicrosoftWindows( b );
    }


    private static void setRunOnSystemStartMicrosoftWindows( boolean b )
    {
    	RegistryKey registryKey = new RegistryKey( RootKey.HKEY_CURRENT_USER, MICROSOFT_WINDOWS_REGISTRY_KEY );

    	if( b )
    	{
    		// Create the key if need be and set the value.
			if( ! registryKey.exists() )
	    		registryKey.create();

        	RegistryValue registryValue = new RegistryValue( MICROSOFT_WINDOWS_REGISTRY_VALUE_NAME, ValueType.REG_SZ, MICROSOFT_WINDOWS_REGISTRY_VALUE_DATA );
        	registryKey.setValue( registryValue );
    	}
    	else
    	{
    		// Delete the value, if it exists.
    		if( ! registryKey.exists() )
        		return;

        	if( registryKey.hasValue( MICROSOFT_WINDOWS_REGISTRY_VALUE_NAME ) )
        		registryKey.deleteValue( MICROSOFT_WINDOWS_REGISTRY_VALUE_NAME );
    	}
    }
}
