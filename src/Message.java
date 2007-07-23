import java.util.Vector;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


public class Message 
{
    public static String getMessageString( String timeZone, int hourOfDay, int minuteOfHour, boolean html )
    {
    	StringBuilder message = new StringBuilder();

		Vector<UserTimeZoneItem> userTimeZoneItems = UserTimeZones.getUserTimeZoneItems( timeZone, hourOfDay, minuteOfHour );
    	if( userTimeZoneItems.isEmpty() )
    		return Messages.getString( "Message.0" );

		boolean showDifferentDayIndicator = Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_SHOW_DIFFERENT_DAY_INDICATOR, true );
		DateTimeFormatter dateTimeFormatter = getDateTimeFormatter();

		String newLine = "\n";
		if( html )
		{
			newLine = "<br>"; 
			message.append( "<html>"); 
		}

		if( showDifferentDayIndicator )
		{
			for( int i = 0; i < userTimeZoneItems.size(); i++ )
	    	{
	    		String currentTimeZone = userTimeZoneItems.get( i ).getTimeZoneDisplayable();
	    		String formattedDateTime = dateTimeFormatter.print( userTimeZoneItems.get( i ).getDateTime() );
	    		int compare = UserTimeZones.compareDateTimeYMD(userTimeZoneItems.get( i ).getDateTime(), UserTimeZones.getReferenceDateTime() );
	    		if( compare > 0 )
		    		message.append( currentTimeZone + ": +" + formattedDateTime ); 
				else if( compare < 0 )
		    		message.append( currentTimeZone + ": -" + formattedDateTime ); 
				else
		    		message.append( currentTimeZone + ": " + formattedDateTime ); 

	    		if( i < ( userTimeZoneItems.size() - 1 ) )
	    			message.append( newLine );
	    	}
		}
		else
		{
	    	for( int i = 0; i < userTimeZoneItems.size(); i++ )
	    	{
	    		String currentTimeZone = userTimeZoneItems.get( i ).getTimeZoneDisplayable();
	    		message.append( currentTimeZone + ": " + dateTimeFormatter.print( userTimeZoneItems.get( i ).getDateTime() ) ); 
	
	    		if( i < ( userTimeZoneItems.size() - 1 ) )
	    			message.append( newLine );
	    	}
		}

		if( html )
			message.append( "</html>"); 

		return message.toString();
    }
    
    
	private static DateTimeFormatter getDateTimeFormatter()
    {
		String dateTimeFormat = Properties.getInstance().getProperty( Properties.PROPERTY_SHOW_OPTION, Properties.PROPERTY_SHOW_TIME_MEDIUM );

		if( dateTimeFormat.equalsIgnoreCase( Properties.PROPERTY_SHOW_DATE_AND_TIME_FULL ) )
			return DateTimeFormat.fullDateTime();
			
		if( dateTimeFormat.equalsIgnoreCase( Properties.PROPERTY_SHOW_DATE_AND_TIME_LONG ) )
			return DateTimeFormat.longDateTime();
			
		if( dateTimeFormat.equalsIgnoreCase( Properties.PROPERTY_SHOW_DATE_AND_TIME_MEDIUM ) )
			return DateTimeFormat.mediumDateTime();

		if( dateTimeFormat.equalsIgnoreCase( Properties.PROPERTY_SHOW_DATE_AND_TIME_SHORT ) )
			return DateTimeFormat.shortDateTime();

		if( dateTimeFormat.equalsIgnoreCase( Properties.PROPERTY_SHOW_DATE_FULL ) )
			return DateTimeFormat.fullDate();

		if( dateTimeFormat.equalsIgnoreCase( Properties.PROPERTY_SHOW_DATE_LONG ) )
			return DateTimeFormat.longDate();

		if( dateTimeFormat.equalsIgnoreCase( Properties.PROPERTY_SHOW_DATE_MEDIUM ) )
			return DateTimeFormat.mediumDate();

		if( dateTimeFormat.equalsIgnoreCase( Properties.PROPERTY_SHOW_DATE_SHORT ) )
			return DateTimeFormat.shortDate();

		if( dateTimeFormat.equalsIgnoreCase( Properties.PROPERTY_SHOW_TIME_FULL ) )
			return DateTimeFormat.fullTime();

		if( dateTimeFormat.equalsIgnoreCase( Properties.PROPERTY_SHOW_TIME_LONG ) )
			return DateTimeFormat.longTime();

		if( dateTimeFormat.equalsIgnoreCase( Properties.PROPERTY_SHOW_TIME_MEDIUM ) )
			return DateTimeFormat.mediumTime();

		if( dateTimeFormat.equalsIgnoreCase( Properties.PROPERTY_SHOW_TIME_SHORT ) )
			return DateTimeFormat.shortTime();

		if( dateTimeFormat.equalsIgnoreCase( Properties.PROPERTY_SHOW_USER_DEFINED ) )
		{
    		try 
    		{
        		String pattern = Properties.getInstance().getProperty( Properties.PROPERTY_SHOW_USER_DEFINED_PATTERN, "" ); 
            	return DateTimeFormat.forPattern( pattern );
    		}
    		catch( IllegalArgumentException illegalArgumentException ) 
    		{
    			return DateTimeFormat.mediumTime();
    		}
		}

		// Return some default.
		return DateTimeFormat.mediumTime();
    }
}
