package worldtimesystemtrayicon;


import java.text.Collator;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.Vector;


public class TimeZoneUtils
{
    private static String[] ms_systemTimeZones;

    public enum DATE_TIME_SORT_OPTIONS
    {
        SORT_BY_DATE_TIME,
        SORT_BY_TIME_ZONE
    }

    
    static
    {
        final String TIME_ZONE_ETC = "Etc/";
        final String TIME_ZONE_SYSTEMV = "SystemV/";
        final String TIME_ZONE_FORWARD_SLASH = "/";

        // Drop time zones without a "/" as they seem to be repeats or bogus.
        // Drop time zones which begin with "SystemV/".
        // Keep time zones:
        //      Which contain a "/".
        //      Beginning with "Etc/" - but strip this off.
        TreeSet<String> systemTimeZones = new TreeSet<>();
        for( String timezone : TimeZone.getAvailableIDs() )
        {
            if( timezone.startsWith( TIME_ZONE_SYSTEMV ) )
                continue;

            if( timezone.startsWith( TIME_ZONE_ETC ) )
            {
                systemTimeZones.add(
                    timezone.substring( TIME_ZONE_ETC.length() ) );

                continue;
            }

            if( timezone.contains( TIME_ZONE_FORWARD_SLASH ) )
                systemTimeZones.add( timezone );
        }

        ms_systemTimeZones = systemTimeZones.toArray( new String[ 0 ] );
    }


    public static String[] getSystemTimeZones()
    {
        return ms_systemTimeZones;
    }


    public static Vector<TimeZoneItem> getUserTimeZones()
    {
        Vector<String> timeZones =
            Properties.getPropertyList(
                Properties.KEY_TIME_ZONES );

        Vector<String> displayNames =
            Properties.getPropertyList(
                Properties.KEY_TIME_ZONES_DISPLAY_NAMES );
        
        Vector<TimeZoneItem> userTimeZones =
            new Vector<>( timeZones.size() );

        List<String> systemTimeZones = Arrays.asList( getSystemTimeZones() );
        for( int i = 0; i < timeZones.size(); i++ )
            if( systemTimeZones.contains( timeZones.get( i ) ) )
                userTimeZones.add(
                    new TimeZoneItem(
                        timeZones.get( i ),
                        displayNames.get( i ) ) );

            else
                // Should never occur, unless in the unlikely situation the time
                // zone has changed from one Java version to the next.
                System.err.println(
                    "Dropping time zone: " + timeZones.get( i ) );

        return userTimeZones;
    }

    
    public static
    Vector<TimeZoneItem> 
    combineTimeZones(
        Vector<TimeZoneItem> timeZoneItems,
        String separator )
    {
        long timeInMillis = new GregorianCalendar().getTimeInMillis();

        Hashtable<Integer, String> offsetToTimeZone = new Hashtable<>();

        Hashtable<Integer, Vector<String>> offsetToTimeZoneDisplayable =
            new Hashtable<>();

        for( TimeZoneItem timeZoneItem : timeZoneItems )
        {
            TimeZone timezone =
                TimeZone.getTimeZone(                   
                    timeZoneItem.getTimeZone() );

            Integer offset =
                Integer.valueOf(
                    timezone.getOffset( timeInMillis ) );

            if( ! offsetToTimeZone.containsKey( offset ) )
            {
                offsetToTimeZone.put(
                    offset,
                    timeZoneItem.getTimeZone() );

                offsetToTimeZoneDisplayable.put( offset, new Vector<String>() );
            }

            offsetToTimeZoneDisplayable.get( offset ).add(
                timeZoneItem.getTimeZoneDisplayable() );
        }

        Vector<TimeZoneItem> combinedTimeZoneItems = new Vector<>();

        for( Map.Entry<Integer, String> entry : offsetToTimeZone.entrySet() )
        {
            Integer key_offset = entry.getKey();
            String value_timeZone = entry.getValue();

            Vector<String> timeZonesDisplayableForOffset =
                offsetToTimeZoneDisplayable.get( key_offset );

            Collections.sort( timeZonesDisplayableForOffset );

            StringBuilder timeZonesDisplayableCombined = new StringBuilder();
            for( String timeZoneDisplayable : timeZonesDisplayableForOffset )
                timeZonesDisplayableCombined
                    .append( timeZoneDisplayable )
                    .append( separator );

            timeZonesDisplayableCombined.setLength(
                timeZonesDisplayableCombined.length() - separator.length() );

            combinedTimeZoneItems.add(
                new TimeZoneItem(
                    value_timeZone,
                    timeZonesDisplayableCombined.toString() ) );
        }

        return combinedTimeZoneItems;
    }


    public static class SortTimeZoneItems implements Comparator<TimeZoneItem> 
    {
        private static long m_timeInMillis =
            new GregorianCalendar().getTimeInMillis();

        private boolean m_sortByDateTime;


        public SortTimeZoneItems( boolean sortByDateTime )
        {
            m_sortByDateTime = sortByDateTime;
        }


        @Override
        public int
        compare(
            TimeZoneItem timeZoneItemA,
            TimeZoneItem timeZoneItemB )
        {
            int comparison;
            if( m_sortByDateTime )
                comparison =
                    compareByDateTimeThenByTimeZone(
                        timeZoneItemA,
                        timeZoneItemB );

            else
                // Sufficient to only compare time zone display names as no two
                // time zones may have the same display name.
                comparison =
                    Collator.getInstance().compare(
                        timeZoneItemA.getTimeZoneDisplayable(),
                        timeZoneItemB.getTimeZoneDisplayable() );

            return comparison;
        }


        private static int
        compareByDateTimeThenByTimeZone(
            TimeZoneItem timeZoneItemA,
            TimeZoneItem timeZoneItemB )
        {
            TimeZone timezoneA =
                TimeZone.getTimeZone(                   
                    timeZoneItemA.getTimeZone() );

            Integer offsetA =
                Integer.valueOf(
                    timezoneA.getOffset( m_timeInMillis ) );

            TimeZone timezoneB =
                TimeZone.getTimeZone(                   
                    timeZoneItemB.getTimeZone() );

            Integer offsetB =
                Integer.valueOf(
                    timezoneB.getOffset( m_timeInMillis ) );

            int comparison = offsetA.intValue() - offsetB.intValue();
            if( comparison == 0 )
                comparison =
                    Collator.getInstance().compare(
                        timeZoneItemA.getTimeZoneDisplayable(),
                        timeZoneItemB.getTimeZoneDisplayable() );

            return comparison;
        }
    }
}
