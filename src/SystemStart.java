public class SystemStart
{
	private static final String MICROSOFT_WINDOWS_REGISTRY_KEY = "Software\\Microsoft\\Windows\\CurrentVersion\\Run"; //$NON-NLS-1$
	private static final String MICROSOFT_WINDOWS_REGISTRY_VALUE_NAME = WorldTimeSystemTray.APPLICATION_NAME;
	private static final String MICROSOFT_WINDOWS_REGISTRY_VALUE_DATA = System.getProperties().get( "user.dir" ) + "\\" + WorldTimeSystemTray.APPLICATION_EXECUTABLE; //$NON-NLS-1$ //$NON-NLS-2$


    public static boolean runOnSystemStart()
    {
    	boolean runOnSystemStart = false;
    	if( OperatingSystem.isWindows() ) 
    		runOnSystemStart = runOnSystemStartMicrosoftWindows();

    	return runOnSystemStart;
    }


    private static boolean runOnSystemStartMicrosoftWindows()
    {
    	boolean runOnSystemStart;
    	try
    	{
			String registryValue = WindowsRegistry.readString( WindowsRegistry.HKEY_CURRENT_USER, MICROSOFT_WINDOWS_REGISTRY_KEY, MICROSOFT_WINDOWS_REGISTRY_VALUE_NAME );
			runOnSystemStart = registryValue != null && registryValue.contentEquals( MICROSOFT_WINDOWS_REGISTRY_VALUE_DATA ); 
    	}
    	catch( Exception exception ) { runOnSystemStart = false; }
    	return runOnSystemStart;
    }

    
    public static boolean setRunOnSystemStart( boolean runOnSystemStart )
    {
    	boolean setRunOnSystemStart = true;
    	if( OperatingSystem.isWindows() )
    		setRunOnSystemStart = setRunOnSystemStartMicrosoftWindows( runOnSystemStart );

    	return setRunOnSystemStart;
    }

    
    private static boolean setRunOnSystemStartMicrosoftWindows( boolean runOnSystemStart )
    {
    	boolean setRunOnSystemStart;
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

	    	setRunOnSystemStart = true;
    	}
    	catch( Exception exception )
    	{
    		exception.printStackTrace(); // Not the best way to handle it...but at least running from the command line will give the stack trace.
    		setRunOnSystemStart = false;
    	}

    	return setRunOnSystemStart;
    }
}