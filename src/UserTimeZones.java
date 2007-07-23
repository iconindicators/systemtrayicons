import java.text.Collator;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentSkipListSet;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;


public class UserTimeZones
{
	private static ConcurrentSkipListSet<String> m_availableTimeZones = null;
	private static DateTime m_referenceDateTime = null;

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
			Set<String> availableIDs = DateTimeZone.getAvailableIDs();
			m_availableTimeZones = new ConcurrentSkipListSet<String>();

			// Only want time zones which contain a "/".  Time zones without a "/" seem to be repeats or bogus.
			// Also, drop time zones which contain GMT or SystemV.
			Iterator<String> iterator = availableIDs.iterator();
			while( iterator.hasNext() )
			{
				String timezone = iterator.next();
				if( timezone.startsWith( TIME_ZONE_ETC ) )
					continue;

				if( timezone.startsWith( TIME_ZONE_SYSTEMV ) )
					continue;

				if( timezone.contains( TIME_ZONE_FORWARD_SLASH ) )
				{
					m_availableTimeZones.add( timezone );
					continue;
				}
			}
		}

		return m_availableTimeZones;
	}


	public synchronized static Vector<UserTimeZoneItem> getUserTimeZoneItems( String timeZone, int hourOfDay, int minuteOfHour )
	{
		// Read in the time zones from the properties file.  
		// The default sort order is by Time Zone.
		Vector<String> timeZones = Properties.getInstance().getPropertyList( Properties.PROPERTY_TIME_ZONES_SELECTED );
		Vector<String> timeZonesDisplayNames = Properties.getInstance().getPropertyList( Properties.PROPERTY_TIME_ZONES_SELECTED_DISPLAY_NAMES );

		if( timeZones.size() != timeZonesDisplayNames.size() )
			timeZonesDisplayNames = timeZones; // Simple attempt at error recovery.

		Vector<UserTimeZoneItem> userTimeZoneItems = new Vector<UserTimeZoneItem>( timeZones.size() );
		for( int i = 0; i < timeZones.size(); i++ )
			userTimeZoneItems.add( new UserTimeZoneItem( timeZones.get( i ), timeZonesDisplayNames.get( i ) ) );

		// Compute the DateTime for each user Time Zone.
		if( timeZone == null || ! isValidTimeZone( timeZone ) )
    	{
        	m_referenceDateTime = new DateTime();
        	for( int i = 0; i < userTimeZoneItems.size(); i++ )
        	{
        		DateTimeZone dateTimeZone = DateTimeZone.forID( userTimeZoneItems.get( i ).getTimeZone() );
        		DateTime dateTime = m_referenceDateTime.withZone( dateTimeZone );
        		userTimeZoneItems.get( i ).setDateTime( dateTime );
        	}
    	}
    	else
    	{
	    	DateTime timeTravelDateTime =
	    		new DateTime
	    		(
					new DateTime().getYear(),
					new DateTime().getMonthOfYear(), 
					new DateTime().getDayOfMonth(),
					hourOfDay,
					minuteOfHour,
	    			0,
	    			0
	    		).withZoneRetainFields( DateTimeZone.forID( timeZone ) );

	    	for( int i = 0; i < userTimeZoneItems.size(); i++ )
			{
				DateTimeZone dateTimeZone = DateTimeZone.forID( userTimeZoneItems.get( i ).getTimeZone() );    	
				DateTime dateTime = timeTravelDateTime.withZone( dateTimeZone );    	
				userTimeZoneItems.get( i ).setDateTime( dateTime.withZone( dateTimeZone ) );
			}

	    	m_referenceDateTime = timeTravelDateTime.withZone( DateTimeZone.forID( DateTimeZone.getDefault().getID() ) );
    	}

		// Sort either by datetime or display timezone.
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
				comparison = compareDateTime( userTimeZoneItems.get( i ).getDateTime(), sorted.get( j ).getDateTime() );
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
					comparison = compareDateTime( userTimeZoneItems.get( i ).getDateTime(), sorted.get( j ).getDateTime() );
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


	public static int compareDateTimeYMD( DateTime dateTimeA, DateTime dateTimeB )
	{
		// Really we should compare the DateTimes directly with DateTime.compare(),
		// but this results in all of the DateTimes having the same value.
		if( dateTimeA.getYear() > dateTimeB.getYear() )
			return 1;

		if( dateTimeA.getYear() < dateTimeB.getYear() )
			return -1;

		if( dateTimeA.getMonthOfYear() > dateTimeB.getMonthOfYear() )
			return 1;

		if( dateTimeA.getMonthOfYear() < dateTimeB.getMonthOfYear() )
			return -1;

		if( dateTimeA.getDayOfMonth() > dateTimeB.getDayOfMonth() )
			return 1;

		if( dateTimeA.getDayOfMonth() < dateTimeB.getDayOfMonth() )
			return -1;

		return 0;
	}

    
    public synchronized static DateTime getReferenceDateTime() { return m_referenceDateTime; }
    

    private static int compareDateTime( DateTime dateTimeA, DateTime dateTimeB )
	{
		// Really we should compare the DateTimes directly with DateTime.compare(),
		// but this results in all of the DateTimes having the same value.
		if( dateTimeA.getYear() > dateTimeB.getYear() )
			return 1;

		if( dateTimeA.getYear() < dateTimeB.getYear() )
			return -1;
		
		if( dateTimeA.getMonthOfYear() > dateTimeB.getMonthOfYear() )
			return 1;
		
		if( dateTimeA.getMonthOfYear() < dateTimeB.getMonthOfYear() )
			return -1;

		if( dateTimeA.getDayOfMonth() > dateTimeB.getDayOfMonth() )
			return 1;
		
		if( dateTimeA.getDayOfMonth() < dateTimeB.getDayOfMonth() )
			return -1;

		if( dateTimeA.getHourOfDay() > dateTimeB.getHourOfDay() )
			return 1;
		
		if( dateTimeA.getHourOfDay() < dateTimeB.getHourOfDay() )
			return -1;

		if( dateTimeA.getMinuteOfHour() > dateTimeB.getMinuteOfHour() )
			return 1;
		
		if( dateTimeA.getMinuteOfHour() < dateTimeB.getMinuteOfHour() )
			return -1;

		if( dateTimeA.getSecondOfMinute() > dateTimeB.getSecondOfMinute() )
			return 1;
		
		if( dateTimeA.getSecondOfMinute() < dateTimeB.getSecondOfMinute() )
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
