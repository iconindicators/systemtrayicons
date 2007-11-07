import java.util.GregorianCalendar;


public class UserTimeZoneItem
{
	private GregorianCalendar m_gregorianCalendar = null;
	private String m_timeZone = null;
	private String m_timeZoneDisplayable = null;


	public UserTimeZoneItem( String timeZone, String timeZoneDisplayable )
	{
		if( timeZone == null ) throw new IllegalArgumentException( "Time Zone cannot be null." );

		if( ! UserTimeZones.isValidTimeZone( timeZone ) ) throw new IllegalArgumentException( "Invalid Time Zone." );
		
		if( timeZoneDisplayable == null ) throw new IllegalArgumentException( "Time Zone display name cannot be null." );
			
		m_timeZone = timeZone;
		m_timeZoneDisplayable = timeZoneDisplayable;
	}


	public GregorianCalendar getGregorianCalendar() { return m_gregorianCalendar; } 


	public String getTimeZone() { return m_timeZone; } 


	public String getTimeZoneDisplayable() { return m_timeZoneDisplayable; } 


	public void setGregorianCalendar( GregorianCalendar gregorianCalendar ) 
	{ 
		if( gregorianCalendar == null ) throw new IllegalArgumentException( "Gregorian Calendar cannot be null." );

		m_gregorianCalendar = gregorianCalendar; 
	} 


	public void setTimeZone( String timeZone ) 
	{ 
		if( timeZone == null ) throw new IllegalArgumentException( "Time Zone cannot be null." );

		if( ! UserTimeZones.isValidTimeZone( timeZone ) ) throw new IllegalArgumentException( "Invalid Time Zone." );
		
		m_timeZone = timeZone; 
	} 


	public void setTimeZoneDisplayable( String timeZoneDisplayable ) 
	{ 
		if( timeZoneDisplayable == null ) throw new IllegalArgumentException( "Time Zone display name cannot be null." );
			
		m_timeZoneDisplayable = timeZoneDisplayable; 
	} 
}
