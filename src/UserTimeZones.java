import java.text.Collator;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.Vector;
import java.util.concurrent.ConcurrentSkipListSet;


public class UserTimeZones
{
	private static ConcurrentSkipListSet<String> m_availableTimeZones = null;

	private static final String TIME_ZONE_ETC = "Etc/";
	private static final String TIME_ZONE_SYSTEMV = "SystemV/";
	private static final String TIME_ZONE_FORWARD_SLASH = "/";


	public enum DATE_TIME_SORT_OPTIONS
	{
		SORT_BY_DATE_TIME,
		SORT_BY_TIME_ZONE
	}


	public synchronized static ConcurrentSkipListSet<String> getAvailableTimeZones()
	{
		if( m_availableTimeZones == null )
		{
			String[] availableIDs = TimeZone.getAvailableIDs();
			m_availableTimeZones = new ConcurrentSkipListSet<String>();

			// Want Time Zones which contain a "/".  Time Zones without a "/" seem to be repeats or bogus.
			// Keep Time Zones beginning with "Etc/" - but strip this off.
			// Drop Time Zones which begin with "SystemV/".
			for( int i = 0; i < availableIDs.length; i++ ) 
			{
				String timezone = availableIDs[ i ];
				if( timezone.startsWith( TIME_ZONE_SYSTEMV ) )
					continue;

				if( timezone.startsWith( TIME_ZONE_ETC ) )
				{
					m_availableTimeZones.add( timezone.substring( TIME_ZONE_ETC.length() ) );
					continue;
				}

				if( timezone.contains( TIME_ZONE_FORWARD_SLASH ) )
				{
					m_availableTimeZones.add( timezone );
				}
			}
		}

		return m_availableTimeZones;
	}


    public synchronized static Vector<UserTimeZoneItem> getUserTimeZoneItems( GregorianCalendar gregorianCalendar )
	{
    	if( gregorianCalendar == null ) 
    		throw new IllegalArgumentException( "Gregorian Calendar cannot be null." );

		// Read in the time zones from the properties file.  
		// The default sort order is by Time Zone.
		Vector<String> timeZones = Properties.getInstance().getPropertyList( Properties.PROPERTY_TIME_ZONES_SELECTED );
		Vector<String> timeZonesDisplayNames = Properties.getInstance().getPropertyList( Properties.PROPERTY_TIME_ZONES_SELECTED_DISPLAY_NAMES );

		if( timeZones.size() != timeZonesDisplayNames.size() )
			timeZonesDisplayNames = timeZones; // Simple attempt at error recovery.

		Vector<UserTimeZoneItem> userTimeZoneItems = new Vector<UserTimeZoneItem>( timeZones.size() );
		for( int i = 0; i < timeZones.size(); i++ )
		{
			if( isValidTimeZone( timeZones.get( i ) ) )
			{
				userTimeZoneItems.add( new UserTimeZoneItem( timeZones.get( i ), timeZonesDisplayNames.get( i ) ) );
			}
			else
			{
				// Invalid Time Zone...so drop it.
				timeZones.removeElementAt( i );
				timeZonesDisplayNames.removeElementAt( i );
			}
		}

		// We may have dropped invalid Time Zones...so save out what we currently have.
		Properties.getInstance().setPropertyList( Properties.PROPERTY_TIME_ZONES_SELECTED, timeZones );
		Properties.getInstance().setPropertyList( Properties.PROPERTY_TIME_ZONES_SELECTED_DISPLAY_NAMES, timeZonesDisplayNames );
		Properties.getInstance().store();

		for( int i = 0; i < timeZones.size(); i++ )
    	{
			GregorianCalendar gregorianCalendarForTimeZone = new GregorianCalendar( TimeZone.getTimeZone( timeZones.get( i ) ) );
			gregorianCalendarForTimeZone.setTimeInMillis( gregorianCalendar.getTimeInMillis() );
			userTimeZoneItems.get( i ).setGregorianCalendar( gregorianCalendarForTimeZone );
    	}

		// Sort either by date/time or display time zone.
		UserTimeZones.DATE_TIME_SORT_OPTIONS dateTimeSortOption = UserTimeZones.DATE_TIME_SORT_OPTIONS.SORT_BY_DATE_TIME;
		String sortOption = Properties.getInstance().getProperty( Properties.PROPERTY_SORT_DATE_TIME, Properties.PROPERTY_SORT_DATE_TIME_BY_DATE_TIME );
		if( sortOption.equals( Properties.PROPERTY_SORT_DATE_TIME_BY_TIME_ZONE ) )
			dateTimeSortOption = UserTimeZones.DATE_TIME_SORT_OPTIONS.SORT_BY_TIME_ZONE;

		if( dateTimeSortOption == UserTimeZones.DATE_TIME_SORT_OPTIONS.SORT_BY_DATE_TIME )
			return sortByDateTime( userTimeZoneItems );
		else if( dateTimeSortOption == UserTimeZones.DATE_TIME_SORT_OPTIONS.SORT_BY_TIME_ZONE )
			return sortByTimeZoneDisplayable( userTimeZoneItems );

		throw new IllegalStateException( "Invalid sort option: " + dateTimeSortOption );
	}


	private synchronized static Vector<UserTimeZoneItem> sortByDateTime( Vector<UserTimeZoneItem> userTimeZoneItems )
	{
		Vector<UserTimeZoneItem> sorted = new Vector<UserTimeZoneItem>( userTimeZoneItems.size() );
		for( int i = 0; i < userTimeZoneItems.size(); i++ )
		{
			int j = 0;
			int comparison = -1;
			for( ; j < sorted.size(); j++ )
			{
				comparison = compareYearMonthDayHourMinuteSecond( userTimeZoneItems.get( i ).getGregorianCalendar(), sorted.get( j ).getGregorianCalendar() );
				if( comparison == 0 )
				{
					comparison = Collator.getInstance().compare( userTimeZoneItems.get( i ).getTimeZoneDisplayable(), sorted.get( j ).getTimeZoneDisplayable() );
					if( comparison == 0 )
					{
						comparison = Collator.getInstance().compare( userTimeZoneItems.get( i ).getTimeZone(), sorted.get( j ).getTimeZone() );
						if( comparison == 0 )
							throw new IllegalStateException( "Cannot have the same Time Zone twice." );

						if( comparison > 0 )
							continue;

						break;
					}

					if( comparison > 0 )
						continue;

					break;
				}

				if( comparison > 0 )
					continue;

				break;
			}

			sorted.insertElementAt( userTimeZoneItems.get( i ), j );
		}
		
		return sorted;
	}


	private synchronized static Vector<UserTimeZoneItem> sortByTimeZoneDisplayable( Vector<UserTimeZoneItem> userTimeZoneItems )
	{
		Vector<UserTimeZoneItem> sorted = new Vector<UserTimeZoneItem>( userTimeZoneItems.size() );
		for( int i = 0; i < userTimeZoneItems.size(); i++ )
		{
			int j = 0;
			int comparison = -1;
			for( ; j < sorted.size(); j++ )
			{
				comparison = Collator.getInstance().compare( userTimeZoneItems.get( i ).getTimeZoneDisplayable(), sorted.get( j ).getTimeZoneDisplayable() );
				if( comparison == 0 )
				{
					comparison = compareYearMonthDayHourMinuteSecond( userTimeZoneItems.get( i ).getGregorianCalendar(), sorted.get( j ).getGregorianCalendar() );
					if( comparison == 0 )
					{
						comparison = Collator.getInstance().compare( userTimeZoneItems.get( i ).getTimeZone(), sorted.get( j ).getTimeZone() );
						if( comparison == 0 )
							throw new IllegalStateException( "Cannot have the same Time Zone twice." );

						if( comparison > 0 )
							continue;

						break;
					}

					if( comparison > 0 )
						continue;

					break;
				}

				if( comparison > 0 )
					continue;

				break;
			}

			sorted.insertElementAt( userTimeZoneItems.get( i ), j );
		}
		
		return sorted;
	}


	public static int compareYearMonthDay( GregorianCalendar gregorianCalendarA, GregorianCalendar gregorianCalendarB )
	{
		if( gregorianCalendarA.get( GregorianCalendar.YEAR ) > gregorianCalendarB.get( GregorianCalendar.YEAR ) )
			return 1;

		if( gregorianCalendarA.get( GregorianCalendar.YEAR ) < gregorianCalendarB.get( GregorianCalendar.YEAR ) )
			return -1;

		if( gregorianCalendarA.get( GregorianCalendar.MONTH ) > gregorianCalendarB.get( GregorianCalendar.MONTH ) )
			return 1;

		if( gregorianCalendarA.get( GregorianCalendar.MONTH ) < gregorianCalendarB.get( GregorianCalendar.MONTH ) )
			return -1;

		if( gregorianCalendarA.get( GregorianCalendar.DAY_OF_MONTH ) > gregorianCalendarB.get( GregorianCalendar.DAY_OF_MONTH ) )
			return 1;

		if( gregorianCalendarA.get( GregorianCalendar.DAY_OF_MONTH ) < gregorianCalendarB.get( GregorianCalendar.DAY_OF_MONTH ) )
			return -1;

		return 0;
	}

    
    public static int compareYearMonthDayHourMinuteSecond( GregorianCalendar gregorianCalendarA, GregorianCalendar gregorianCalendarB )
	{
		if( gregorianCalendarA.get( GregorianCalendar.YEAR ) > gregorianCalendarB.get( GregorianCalendar.YEAR ) )
			return 1;

		if( gregorianCalendarA.get( GregorianCalendar.YEAR ) < gregorianCalendarB.get( GregorianCalendar.YEAR ) )
			return -1;

		if( gregorianCalendarA.get( GregorianCalendar.MONTH ) > gregorianCalendarB.get( GregorianCalendar.MONTH ) )
			return 1;

		if( gregorianCalendarA.get( GregorianCalendar.MONTH ) < gregorianCalendarB.get( GregorianCalendar.MONTH ) )
			return -1;

		if( gregorianCalendarA.get( GregorianCalendar.DAY_OF_MONTH ) > gregorianCalendarB.get( GregorianCalendar.DAY_OF_MONTH ) )
			return 1;

		if( gregorianCalendarA.get( GregorianCalendar.DAY_OF_MONTH ) < gregorianCalendarB.get( GregorianCalendar.DAY_OF_MONTH ) )
			return -1;

		if( gregorianCalendarA.get( GregorianCalendar.HOUR_OF_DAY ) > gregorianCalendarB.get( GregorianCalendar.HOUR_OF_DAY ) )
			return 1;

		if( gregorianCalendarA.get( GregorianCalendar.HOUR_OF_DAY ) < gregorianCalendarB.get( GregorianCalendar.HOUR_OF_DAY ) )
			return -1;

		if( gregorianCalendarA.get( GregorianCalendar.MINUTE ) > gregorianCalendarB.get( GregorianCalendar.MINUTE ) )
			return 1;

		if( gregorianCalendarA.get( GregorianCalendar.MINUTE ) < gregorianCalendarB.get( GregorianCalendar.MINUTE ) )
			return -1;

		if( gregorianCalendarA.get( GregorianCalendar.SECOND ) > gregorianCalendarB.get( GregorianCalendar.SECOND ) )
			return 1;

		if( gregorianCalendarA.get( GregorianCalendar.SECOND ) < gregorianCalendarB.get( GregorianCalendar.SECOND ) )
			return -1;

		return 0;
	}


    public synchronized static boolean isValidTimeZone( String timeZone )
    {
    	if( timeZone == null )
			throw new IllegalArgumentException( "Time Zone cannot be null." );
    	
    	return getAvailableTimeZones().contains( timeZone );
    }
}
