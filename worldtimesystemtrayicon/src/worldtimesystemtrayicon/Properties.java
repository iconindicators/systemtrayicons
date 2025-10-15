package worldtimesystemtrayicon;


import java.util.Vector;

import basesystemtrayicon.PropertiesBase;


public class Properties extends PropertiesBase
{
    /** Combining time zones. */
    public static final String KEY_COMBINE = "Combine";
    public static final boolean VALUE_COMBINE_DEFAULT = false;
    public static final String KEY_SEPARATOR = "Separator";
    public static final String VALUE_SEPARATOR_DEFAULT = "  |  "; 

    /** Previous/next day indicators. */
    public static final String KEY_PREVIOUS_DAY = "PreviousDay";
    public static final String VALUE_PREVIOUS_DAY_DEFAULT = "-";
    public static final String KEY_NEXT_DAY = "NextDay";
    public static final String VALUE_NEXT_DAY_DEFAULT = "+";

    /** Sort by date/time (conversely sort by time zone). */
    public static final String KEY_SORT_DATE_TIME = "SortDateTime";
    public static final boolean VALUE_SORT_DATE_TIME_DEFAULT = true;

    /** Time zones and respective display names. */
    public static final String KEY_TIME_ZONES = "TimeZones";
    public static final String
        KEY_TIME_ZONES_DISPLAY_NAMES =
            "TimeZonesDisplayNames";

    /** Options for combo portions of the layout */
    public static final String KEY_LAYOUT_COMBO_LEFT = "LayoutComboLeft";
    public static final String KEY_LAYOUT_COMBO_CENTRE = "LayoutComboCentre";
    public static final String KEY_LAYOUT_COMBO_RIGHT = "LayoutComboRight";

    /** Values for combo portions of the layout */
	public static final String VALUE_LAYOUT_COMBO_DATE_TIME = "DateTime";
    public static final String VALUE_LAYOUT_COMBO_NONE = "None";
	public static final String VALUE_LAYOUT_COMBO_TIME_ZONE = "TimeZone";
    public static final String
        VALUE_LAYOUT_COMBO_DIFFERENT_DAY_INDICATOR =
            "DifferentDayIndicator";

    /** Keys/values for text portions of the layout */
    public static final String KEY_LAYOUT_TEXT_LEFT = "LayoutTextLeft";  
    public static final String
        KEY_LAYOUT_TEXT_LEFT_CENTRE =
            "LayoutTextLeftCentre";  

    public static final String
        KEY_LAYOUT_TEXT_RIGHT_CENTRE =
            "LayoutTextRightCentre";  

    public static final String KEY_LAYOUT_TEXT_RIGHT = "LayoutTextRight";  

    public static final String VALUE_LAYOUT_TEXT_LEFT_DEFAULT = "";
    public static final String VALUE_LAYOUT_TEXT_LEFT_CENTRE_DEFAULT = ": ";
    public static final String VALUE_LAYOUT_TEXT_RIGHT_CENTRE_DEFAULT = "";
    public static final String VALUE_LAYOUT_TEXT_RIGHT_DEFAULT = "";


    /**
     * Properties have been renamed and/or removed, so need to upgrade.
     */
    public static void upgrade( String applicationVersion )
    {
        // Combine time zones.
        upgradeKey( "CombineTimeZone", KEY_COMBINE );
        upgradeKey( "Separator", KEY_SEPARATOR );

        // Previous/next day indicators.
        upgradeKey( "DifferentDayIndicatorPreviousDay", KEY_PREVIOUS_DAY );
        upgradeKey( "DifferentDayIndicatorNextDay", KEY_NEXT_DAY );

        // Sort date/time.
        //
        // The new key and old key have the same name, 
        // so don't delete the old key.
        if( containsKey( "SortDateTime" ) )
        {
            String oldSortDateTimeValue =
                getProperty(
                    "SortDateTime",
                    "SortDateTimeByDateTime" );

            if( oldSortDateTimeValue.equals( "SortDateTimeByDateTime" ) )
                setProperty(
                    KEY_SORT_DATE_TIME,
                    Boolean.toString( true ) );

            else if( oldSortDateTimeValue.equals( "SortDateTimeByTimeZone" ) )
                setProperty(
                    KEY_SORT_DATE_TIME,
                    Boolean.toString( false ) );
        }

        // Time zones and corresponding display names.
        boolean timeZonesExist =
            containsKey( "TimeZonesSelected" )
            &&
            containsKey( "TimeZonesSelectedDisplayNames" );

        if( timeZonesExist )
        {
            Vector<String> oldTimeZones =
                getPropertyList(
                    "TimeZonesSelected" );

            Vector<String> oldTimeZonesDisplayNames =
                getPropertyList(
                    "TimeZonesSelectedDisplayNames" );

            setPropertyList( KEY_TIME_ZONES, oldTimeZones );
            setPropertyList(
                KEY_TIME_ZONES_DISPLAY_NAMES,
                oldTimeZonesDisplayNames );

            removeProperty( "TimeZonesSelected" );
            removeProperty( "TimeZonesSelectedDisplayNames" );
        }

        // Message layout options.
        upgradeLayoutComboOption( "LayoutLeftOption", KEY_LAYOUT_COMBO_LEFT );
        upgradeLayoutComboOption( "LayoutCentreOption", KEY_LAYOUT_COMBO_CENTRE );
        upgradeLayoutComboOption( "LayoutRightOption", KEY_LAYOUT_COMBO_RIGHT );
        upgradeLayoutTextOption( "LayoutLeftText", KEY_LAYOUT_TEXT_LEFT );
        upgradeLayoutTextOption( "LayoutLeftCentreText", KEY_LAYOUT_TEXT_LEFT_CENTRE );
        upgradeLayoutTextOption( "LayoutRightCentreText", KEY_LAYOUT_TEXT_RIGHT_CENTRE );
        upgradeLayoutTextOption( "LayoutRightText", KEY_LAYOUT_TEXT_RIGHT );

        // Date/time format.
        if( containsKey( "ShowOption" ) )
        {
            String dateTimeFormatOld =
                getProperty(
                    "ShowOption",
                    "ShowTimeMedium" );

            String dateTimeFormatNew = VALUE_TIME_MEDIUM;
            if( dateTimeFormatOld.equals( "ShowDateTimeFull" ) )
                dateTimeFormatNew = VALUE_DATE_TIME_FULL;

            else if( dateTimeFormatOld.equals( "ShowDateTimeLong" ) )
                dateTimeFormatNew = VALUE_DATE_TIME_LONG;

            else if( dateTimeFormatOld.equals( "ShowDateTimeMedium" ) )
                dateTimeFormatNew = VALUE_DATE_TIME_MEDIUM;

            else if( dateTimeFormatOld.equals( "ShowDateTimeShort" ) )
                dateTimeFormatNew = VALUE_DATE_TIME_SHORT;

            else if( dateTimeFormatOld.equals( "ShowDateFull" ) )
                dateTimeFormatNew = VALUE_DATE_FULL;

            else if( dateTimeFormatOld.equals( "ShowDateLong" ) )
                dateTimeFormatNew = VALUE_DATE_LONG;

            else if( dateTimeFormatOld.equals( "ShowDateMedium" ) )
                dateTimeFormatNew = VALUE_DATE_MEDIUM;

            else if( dateTimeFormatOld.equals( "ShowDateShort" ) )
                dateTimeFormatNew = VALUE_DATE_SHORT;

            else if( dateTimeFormatOld.equals( "ShowTimeFull" ) )
                dateTimeFormatNew = VALUE_TIME_FULL;

            else if( dateTimeFormatOld.equals( "ShowTimeLong" ) )
                dateTimeFormatNew = VALUE_TIME_LONG;

            else if( dateTimeFormatOld.equals( "ShowTimeMedium" ) )
                dateTimeFormatNew = VALUE_TIME_MEDIUM;

            else if( dateTimeFormatOld.equals( "ShowTimeShort" ) )
                dateTimeFormatNew = VALUE_TIME_SHORT;

            else if( dateTimeFormatOld.equals( "ShowUserDefined" ) )
            {
                dateTimeFormatNew = VALUE_USER_DEFINED;

                String userDefinedPatternOld =
                    getProperty(
                        "ShowUserDefinedPattern",
                        VALUE_USER_DEFINED_DATE_DEFAULT );

                setProperty( KEY_USER_DEFINED, userDefinedPatternOld );
                removeProperty( "ShowUserDefinedPattern" );
            }

            setProperty( KEY_DATE_TIME_FORMAT, dateTimeFormatNew );
            removeProperty( "ShowOption" );
        }

        // Remove redundant properties.
        removeProperty( "ShowTimesInToolTip" );

        removeProperty( "ColumnsLeftTextAndLeftOptionAreSeparate" );
        removeProperty( "ColumnsLeftOptionAndLeftCentreTextAreSeparate" );
        removeProperty( "ColumnsLeftCentreTextAndCentreOptionAreSeparate" );
        removeProperty( "ColumnsCentreOptionAndRightCentreTextAreSeparate" );
        removeProperty( "ColumnsRightCentreTextAndRightOptionAreSeparate" );
        removeProperty( "ColumnsRightOptionAndRightTextAreSeparate" );

        removeProperty( "ColumnLeftTextAlignment" );
        removeProperty( "ColumnLeftOptionAlignment" );
        removeProperty( "ColumnLeftCentreTextAlignment" );
        removeProperty( "ColumnCentreOptionAlignment" );
        removeProperty( "ColumnRightCentreTextAlignment" );
        removeProperty( "ColumnRightOptionAlignment" );
        removeProperty( "ColumnRightTextAlignment" );

        setProperty( KEY_VERSION, applicationVersion );

        store();
    }


    private static void upgradeLayoutComboOption( String keyOld, String keyNew )
    {
        if( containsKey( keyOld ) )
        {
            String layoutOption = getProperty( keyOld );
            String value = VALUE_LAYOUT_COMBO_NONE;
            if( layoutOption.equals( "None" ) )
                value = VALUE_LAYOUT_COMBO_NONE;

            else if( layoutOption.equals( "DifferentDayIndicator" ) )
                value = VALUE_LAYOUT_COMBO_DIFFERENT_DAY_INDICATOR;

            else if( layoutOption.equals( "Time" ) )
                value = VALUE_LAYOUT_COMBO_DATE_TIME;

            else if( layoutOption.equals( "TimeZone" ) )
                value = VALUE_LAYOUT_COMBO_TIME_ZONE;

            setProperty( keyNew, value );
            removeProperty( keyOld );
        }
    }


    private static void upgradeLayoutTextOption( String keyOld, String keyNew )
    {
        if( containsKey( keyOld ) )
        {
            setProperty( keyNew, getProperty( keyOld ) );
            removeProperty( keyOld );
        }
    }
}
