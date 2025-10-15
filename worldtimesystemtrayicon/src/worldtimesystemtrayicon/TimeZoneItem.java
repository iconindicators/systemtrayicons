package worldtimesystemtrayicon;


public class TimeZoneItem
{
	private String m_timeZone = null;
	private String m_timeZoneDisplayable = null;


	public TimeZoneItem( String timeZone, String timeZoneDisplayable )
	{
		if( timeZone == null )
		    throw new IllegalArgumentException( "Time zone cannot be null." );

        if( timeZoneDisplayable == null )
            throw new IllegalArgumentException( "Time zone display name cannot be null." );

		m_timeZone = timeZone;
		m_timeZoneDisplayable = timeZoneDisplayable;
	}


	public String getTimeZone()
	{
	    return m_timeZone;
    }


	public String getTimeZoneDisplayable()
	{
	    return m_timeZoneDisplayable;
    } 
}
