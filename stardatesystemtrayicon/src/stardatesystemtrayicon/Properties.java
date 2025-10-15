package stardatesystemtrayicon;


import basesystemtrayicon.PropertiesBase;


public class Properties extends PropertiesBase
{
    /** Properties for showing stardate. */
    public static final String KEY_CLASSIC = "Classic";
    public static final String KEY_ISSUE = "Issue";
    public static final String KEY_PAD = "Pad";

    public static final boolean VALUE_CLASSIC_DEFAULT = true;
    public static final boolean VALUE_ISSUE_DEFAULT = false;
    public static final boolean VALUE_PAD_DEFAULT = false;


    /**
     * Properties have been renamed and/or removed, so need to upgrade.
     */
    public static void upgrade( String applicationVersion )
    {
        // Stardate
        upgradeKey( "ShowStardateClassic", KEY_CLASSIC );
        upgradeKey( "ShowStardateIssue", KEY_ISSUE );
        upgradeKey( "PadStardate", KEY_PAD );

        // Remove redundant chronologies properties.
        removeProperty( "ChronologyBuddhist" );
        removeProperty( "ChronologyCoptic" );
        removeProperty( "ChronologyEthiopic" );
        removeProperty( "ChronologyGregorian" );
        removeProperty( "ChronologyGregorianJulian" );
        removeProperty( "ChronologyIslamic" );
        removeProperty( "ChronologyISO8601" );
        removeProperty( "ChronologyJulian" );
        removeProperty( "ChronologyStardate" );

        // Date format; previously supported date and time, but only the date
        // makes sense.
        if( containsKey( "DateTimeFormat" ) )
        {
            String dateTimeFormatOld = getProperty( "DateTimeFormat" );
            String dateFormatNew = VALUE_DATE_FULL;
            if( dateTimeFormatOld.equals( "Long" ) )
                dateFormatNew = VALUE_DATE_LONG;

            else if( dateTimeFormatOld.equals( "Medium" ) )
                dateFormatNew = VALUE_DATE_MEDIUM;

            else if( dateTimeFormatOld.equals( "Short" ) )
                dateFormatNew = VALUE_DATE_SHORT;

            setProperty( KEY_DATE_FORMAT, dateFormatNew );
            removeProperty( "DateTimeFormat" );
        }

        setProperty( KEY_VERSION, applicationVersion );

        store();
    }
}
