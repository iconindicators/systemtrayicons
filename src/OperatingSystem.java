public class OperatingSystem
{
	// See http://lopica.sourceforge.net/os.html for os.name values.
	public static String getOS() { return System.getProperty( "os.name" ); } //$NON-NLS-1$


	public static boolean isLinux() { return getOS().toLowerCase().indexOf( "lin" ) >= 0; } //$NON-NLS-1$


	public static boolean isMacOSX() { return getOS().toLowerCase().indexOf( "mac os x" ) >= 0; } //$NON-NLS-1$


	public static boolean isWindows() { return getOS().toLowerCase().indexOf( "win" ) >= 0; } //$NON-NLS-1$
}
