public class SystemStart
{
	private static final String MICROSOFT_WINDOWS_REGISTRY_KEY = "Software\\Microsoft\\Windows\\CurrentVersion\\Run"; //$NON-NLS-1$
	private static final String MICROSOFT_WINDOWS_REGISTRY_VALUE_NAME = StardateSystemTray.APPLICATION_NAME;
	private static final String MICROSOFT_WINDOWS_REGISTRY_VALUE_DATA = System.getProperties().get( "user.dir" ) + "\\" + StardateSystemTray.APPLICATION_EXECUTABLE; //$NON-NLS-1$ //$NON-NLS-2$


    public static boolean runOnSystemStart()
    {
    	if( OperatingSystem.isWindows() ) return runOnSystemStartMicrosoftWindows();

    	return false;
    }


    private static boolean runOnSystemStartMicrosoftWindows()
    {
    	try
    	{
			String registryValue = WindowsRegistry.readString( WindowsRegistry.HKEY_CURRENT_USER, MICROSOFT_WINDOWS_REGISTRY_KEY, MICROSOFT_WINDOWS_REGISTRY_VALUE_NAME );
			return registryValue != null && registryValue.contentEquals( MICROSOFT_WINDOWS_REGISTRY_VALUE_DATA ); 
    	}
    	catch( Exception exception ) { return false; }
    }

    
    public static boolean setRunOnSystemStart( boolean runOnSystemStart )
    {
    	if( OperatingSystem.isWindows() ) return setRunOnSystemStartMicrosoftWindows( runOnSystemStart );

    	return true;
    }

    
    private static boolean setRunOnSystemStartMicrosoftWindows( boolean runOnSystemStart )
    {
    	try
    	{
	    	if( runOnSystemStart )
	    	{
	    		// Create the key if need be and set the value.
	    		String registryValue = WindowsRegistry.readString( WindowsRegistry.HKEY_CURRENT_USER, MICROSOFT_WINDOWS_REGISTRY_KEY, MICROSOFT_WINDOWS_REGISTRY_VALUE_NAME );
	    		if( registryValue == null )
	    			WindowsRegistry.createKey( WindowsRegistry.HKEY_CURRENT_USER, MICROSOFT_WINDOWS_REGISTRY_KEY );

	    		WindowsRegistry.writeStringValue( WindowsRegistry.HKEY_CURRENT_USER, MICROSOFT_WINDOWS_REGISTRY_KEY, MICROSOFT_WINDOWS_REGISTRY_VALUE_NAME, MICROSOFT_WINDOWS_REGISTRY_VALUE_DATA );
        	}
	    	else
	    	{
	    		WindowsRegistry.deleteValue( WindowsRegistry.HKEY_CURRENT_USER, MICROSOFT_WINDOWS_REGISTRY_KEY, MICROSOFT_WINDOWS_REGISTRY_VALUE_NAME ); // Delete the value.
	    	}

	    	return true;
    	}
    	catch( Exception exception ) { exception.printStackTrace(); return false; } // Not the best way to handle it...but at least running from the command line will give the stack trace.
    }
}