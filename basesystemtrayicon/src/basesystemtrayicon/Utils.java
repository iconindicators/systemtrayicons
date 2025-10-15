package basesystemtrayicon;


public class Utils
{
    public static boolean isWindows()
    {
        String osName = System.getProperty( "os.name" );
        return osName != null && osName.toLowerCase().contains( "windows" );
    }


    public static void removeTrailingLineSeparator( StringBuilder stringBuilder )
    {
        stringBuilder.setLength(
            stringBuilder.length()
            -
            System.getProperty( "line.separator" ).length() );        
    }
}
