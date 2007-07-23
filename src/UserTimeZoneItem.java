import org.joda.time.DateTime;

	
	
public class UserTimeZoneItem
{
	private DateTime m_dateTime = null;
	private String m_timeZone = null;
	private String m_timeZoneDisplayable = null;
	
	
	public UserTimeZoneItem( String timeZone, String timeZoneDisplayable )
	{
		if( timeZone == null )
			throw new IllegalArgumentException( "Time Zone cannot be null." );

		if( ! UserTimeZones.isValidTimeZone( timeZone ) )
			throw new IllegalArgumentException( "Invalid Time Zone." );
		
		if( timeZoneDisplayable == null )
			throw new IllegalArgumentException( "Time Zone display name cannot be null." );
			
		m_timeZone = timeZone;
		m_timeZoneDisplayable = timeZoneDisplayable;
	}


	public DateTime getDateTime() { return m_dateTime; } 


	public String getTimeZone() { return m_timeZone; } 


	public String getTimeZoneDisplayable() { return m_timeZoneDisplayable; } 


	public void setDateTime( DateTime dateTime ) 
	{ 
		if( dateTime == null )
			throw new IllegalArgumentException( "DateTime cannot be null." );

		m_dateTime = dateTime; 
	} 


	public void setTimeZone( String timeZone ) 
	{ 
		if( timeZone == null )
			throw new IllegalArgumentException( "Time Zone cannot be null." );

		if( ! UserTimeZones.isValidTimeZone( timeZone ) )
			throw new IllegalArgumentException( "Invalid Time Zone." );
		
		m_timeZone = timeZone; 
	} 


	public void setTimeZoneDisplayable( String timeZoneDisplayable ) 
	{ 
		if( timeZoneDisplayable == null )
			throw new IllegalArgumentException( "Time Zone display name cannot be null." );
			
		m_timeZoneDisplayable = timeZoneDisplayable; 
	} 
}
