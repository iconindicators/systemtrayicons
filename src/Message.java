import java.text.Collator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Vector;


public class Message
{
	public static String getMessageString( GregorianCalendar gregorianCalendar, boolean html )
    {
		StringBuilder message = new StringBuilder();

		Vector<UserTimeZoneItem> userTimeZoneItems = UserTimeZones.getUserTimeZoneItems( gregorianCalendar );
    	if( userTimeZoneItems.isEmpty() )
    		return Messages.getString( "Message.0" ); //$NON-NLS-1$

		combineTimeZones( userTimeZoneItems );

		if( html )
			message.append( "<html><table>");  //$NON-NLS-1$

		DateFormat dateFormat = getDateTimeFormatter();
		String previousDayIndicator = Properties.getInstance().getProperty( Properties.PROPERTY_DIFFERENT_DAY_INDICATOR_PREVIOUS_DAY, DifferentDayIndicator.PREVIOUS_DAY_INDICATOR, false );
		String nextDayIndicator = Properties.getInstance().getProperty( Properties.PROPERTY_DIFFERENT_DAY_INDICATOR_NEXT_DAY, DifferentDayIndicator.NEXT_DAY_INDICATOR, false );

		for( int i = 0; i < userTimeZoneItems.size(); i++ )
    	{
			String currentTimeZone = userTimeZoneItems.get( i ).getTimeZoneDisplayable();
			dateFormat.setCalendar( userTimeZoneItems.get( i ).getGregorianCalendar() );
    		String formattedGregorianCalendar =  dateFormat.format( userTimeZoneItems.get( i ).getGregorianCalendar().getTime() );
    		int compare = UserTimeZones.compareYearMonthDay( userTimeZoneItems.get( i ).getGregorianCalendar(), gregorianCalendar );
    		if( compare > 0 )
    			message.append( buildSingleLineOfOutput( html, currentTimeZone, nextDayIndicator, formattedGregorianCalendar ) );
    		else if( compare < 0 )
    			message.append( buildSingleLineOfOutput( html, currentTimeZone, previousDayIndicator, formattedGregorianCalendar ) );
    		else
    			message.append( buildSingleLineOfOutput( html, currentTimeZone, "", formattedGregorianCalendar ) ); //$NON-NLS-1$

    		if( ! html && i < ( userTimeZoneItems.size() - 1 ) )
    			message.append( System.getProperty( "line.separator" ) ); //$NON-NLS-1$
    	}

		if( html )
			message.append( "</table></html>");  //$NON-NLS-1$

		return message.toString();
    }


	private static void combineTimeZones( Vector<UserTimeZoneItem> userTimeZoneItems )
    {
		// We used to have another combine option: combine if time zone and date/time were the same.
		// However it turns out that Java does not give us the time zone (such as EDT or CST or whatever)
		// unless we ask for it in a time format which is not safe to compare in terms of i18n.
		// So we now just have combine or not combine.
	    if( Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_COMBINE_TIME_ZONES_OPTION, false ) )
	    {
	    	// We need to combine any time zones that have the same date/time.
	    	String separator = Properties.getInstance().getProperty( Properties.PROPERTY_COMBINE_TIME_ZONES_SEPARATOR, Properties.PROPERTY_COMBINE_TIME_ZONES_SEPARATOR_DEFAULT, false );
			for( int i = 0; i < userTimeZoneItems.size(); i++ )
	    	{
				for( int j = i + 1; j < userTimeZoneItems.size(); j++ )
				{
		    		if( UserTimeZones.compareYearMonthDayHourMinuteSecond( userTimeZoneItems.get( i ).getGregorianCalendar(), userTimeZoneItems.get( j ).getGregorianCalendar() ) == 0 )
					{
		    			userTimeZoneItems.get( i ).setTimeZoneDisplayable( userTimeZoneItems.get( i ).getTimeZoneDisplayable() + separator + userTimeZoneItems.get( j ).getTimeZoneDisplayable() );
						userTimeZoneItems.removeElementAt( j );
					}
				}
	    	}
	    }
    }


    private static DateFormat getDateTimeFormatter()
    {
		String dateTimeFormat = Properties.getInstance().getProperty( Properties.PROPERTY_SHOW_OPTION, Properties.PROPERTY_SHOW_TIME_MEDIUM, false );

		if( dateTimeFormat.equalsIgnoreCase( Properties.PROPERTY_SHOW_DATE_AND_TIME_FULL ) )
			return DateFormat.getDateTimeInstance( DateFormat.FULL, DateFormat.FULL );

		if( dateTimeFormat.equalsIgnoreCase( Properties.PROPERTY_SHOW_DATE_AND_TIME_LONG ) )
			return DateFormat.getDateTimeInstance( DateFormat.LONG, DateFormat.LONG );

		if( dateTimeFormat.equalsIgnoreCase( Properties.PROPERTY_SHOW_DATE_AND_TIME_MEDIUM ) )
			return DateFormat.getDateTimeInstance( DateFormat.MEDIUM, DateFormat.MEDIUM );

		if( dateTimeFormat.equalsIgnoreCase( Properties.PROPERTY_SHOW_DATE_AND_TIME_SHORT ) )
			return DateFormat.getDateTimeInstance( DateFormat.SHORT, DateFormat.SHORT );

		if( dateTimeFormat.equalsIgnoreCase( Properties.PROPERTY_SHOW_DATE_FULL ) )
			return DateFormat.getDateInstance( DateFormat.FULL );

		if( dateTimeFormat.equalsIgnoreCase( Properties.PROPERTY_SHOW_DATE_LONG ) )
			return DateFormat.getDateInstance( DateFormat.LONG );

		if( dateTimeFormat.equalsIgnoreCase( Properties.PROPERTY_SHOW_DATE_MEDIUM ) )
			return DateFormat.getDateInstance( DateFormat.MEDIUM );

		if( dateTimeFormat.equalsIgnoreCase( Properties.PROPERTY_SHOW_DATE_SHORT ) )
			return DateFormat.getDateInstance( DateFormat.SHORT );

		if( dateTimeFormat.equalsIgnoreCase( Properties.PROPERTY_SHOW_TIME_FULL ) )
			return DateFormat.getTimeInstance( DateFormat.FULL );

		if( dateTimeFormat.equalsIgnoreCase( Properties.PROPERTY_SHOW_TIME_LONG ) )
			return DateFormat.getTimeInstance( DateFormat.LONG );

		if( dateTimeFormat.equalsIgnoreCase( Properties.PROPERTY_SHOW_TIME_MEDIUM ) )
			return DateFormat.getTimeInstance( DateFormat.MEDIUM );

		if( dateTimeFormat.equalsIgnoreCase( Properties.PROPERTY_SHOW_TIME_SHORT ) )
			return DateFormat.getTimeInstance( DateFormat.SHORT );

		if( dateTimeFormat.equalsIgnoreCase( Properties.PROPERTY_SHOW_USER_DEFINED ) )
		{
    		try { return new SimpleDateFormat( Properties.getInstance().getProperty( Properties.PROPERTY_SHOW_USER_DEFINED_PATTERN, "", false ) ); } //$NON-NLS-1$
    		catch( IllegalArgumentException illegalArgumentException ) 
    		{ return DateFormat.getDateTimeInstance(); }
		}

		// Return some default.
		return DateFormat.getDateTimeInstance();
    }


    private static String buildSingleLineOfOutput( boolean html, String timeZone, String differentDayIndicator, String dateTime )
    {
    	if( html )
    		return buildSingleLineOfOutputInHTML( timeZone, differentDayIndicator, dateTime );
    	
    	return buildSingleLineOfOutputInPlaintext( timeZone, differentDayIndicator, dateTime );
    }


    private static String buildSingleLineOfOutputInPlaintext( String timeZone, String differentDayIndicator, String dateTime )
    {
    	StringBuilder stringBuilder = new StringBuilder();

    	stringBuilder.append( Properties.getInstance().getProperty( Properties.PROPERTY_LAYOUT_LEFT_TEXT, Properties.PROPERTY_LAYOUT_LEFT_TEXT_DEFAULT, false ) );
    	stringBuilder.append( getValueFromLayoutOption( Properties.PROPERTY_LAYOUT_LEFT_OPTION, Properties.PROPERTY_LAYOUT_OPTION_TIME_ZONE, timeZone, differentDayIndicator, dateTime ) );
    	stringBuilder.append( Properties.getInstance().getProperty( Properties.PROPERTY_LAYOUT_LEFT_CENTRE_TEXT, Properties.PROPERTY_LAYOUT_LEFT_CENTRE_TEXT_DEFAULT, false ) );
    	stringBuilder.append( getValueFromLayoutOption( Properties.PROPERTY_LAYOUT_CENTRE_OPTION, Properties.PROPERTY_LAYOUT_OPTION_DIFFERENT_DAY_INDICATOR, timeZone, differentDayIndicator, dateTime ) );
    	stringBuilder.append( Properties.getInstance().getProperty( Properties.PROPERTY_LAYOUT_RIGHT_CENTRE_TEXT, Properties.PROPERTY_LAYOUT_RIGHT_CENTRE_TEXT_DEFAULT, false ) );
    	stringBuilder.append( getValueFromLayoutOption( Properties.PROPERTY_LAYOUT_RIGHT_OPTION, Properties.PROPERTY_LAYOUT_OPTION_TIME, timeZone, differentDayIndicator, dateTime ) );
    	stringBuilder.append( Properties.getInstance().getProperty( Properties.PROPERTY_LAYOUT_RIGHT_TEXT, Properties.PROPERTY_LAYOUT_RIGHT_TEXT_DEFAULT, false ) );

    	return stringBuilder.toString();
    }


    private static String buildSingleLineOfOutputInHTML( String timeZone, String differentDayIndicator, String dateTime )
    {
    	StringBuilder stringBuilder = new StringBuilder();

    	stringBuilder.append( "<tr>" ); //$NON-NLS-1$

    	stringBuilder.append( "<td align=\"" + getColumnAlignmentFromProperty( Properties.PROPERTY_COLUMN_LEFT_TEXT_ALIGNMENT ) + "\">" ); //$NON-NLS-1$ //$NON-NLS-2$
    	stringBuilder.append( Properties.getInstance().getProperty( Properties.PROPERTY_LAYOUT_LEFT_TEXT, Properties.PROPERTY_LAYOUT_LEFT_TEXT_DEFAULT, false ) );
    	if( Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_COLUMNS_LEFT_TEXT_AND_LEFT_OPTION_ARE_SEPARATE, false ) )
    		stringBuilder.append( "</td><td align=\"" + getColumnAlignmentFromProperty( Properties.PROPERTY_COLUMN_LEFT_OPTION_ALIGNMENT ) + "\">" ); //$NON-NLS-1$ //$NON-NLS-2$

    	stringBuilder.append( getValueFromLayoutOption( Properties.PROPERTY_LAYOUT_LEFT_OPTION, Properties.PROPERTY_LAYOUT_OPTION_TIME_ZONE, timeZone, differentDayIndicator, dateTime ) );
    	if( Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_COLUMNS_LEFT_OPTION_AND_LEFT_CENTRE_TEXT_ARE_SEPARATE, false ) )
    		stringBuilder.append( "</td><td align=\"" + getColumnAlignmentFromProperty( Properties.PROPERTY_COLUMN_LEFT_CENTRE_TEXT_ALIGNMENT ) + "\">" ); //$NON-NLS-1$ //$NON-NLS-2$

    	stringBuilder.append( Properties.getInstance().getProperty( Properties.PROPERTY_LAYOUT_LEFT_CENTRE_TEXT, Properties.PROPERTY_LAYOUT_LEFT_CENTRE_TEXT_DEFAULT, false ) );
    	if( Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_COLUMNS_LEFT_CENTRE_TEXT_AND_CENTRE_OPTION_ARE_SEPARATE, false ) )
    		stringBuilder.append( "</td><td align=\"" + getColumnAlignmentFromProperty( Properties.PROPERTY_COLUMN_CENTRE_OPTION_ALIGNMENT ) + "\">" ); //$NON-NLS-1$ //$NON-NLS-2$

    	stringBuilder.append( getValueFromLayoutOption( Properties.PROPERTY_LAYOUT_CENTRE_OPTION, Properties.PROPERTY_LAYOUT_OPTION_DIFFERENT_DAY_INDICATOR, timeZone, differentDayIndicator, dateTime ) );
    	if( Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_COLUMNS_CENTRE_OPTION_AND_RIGHT_CENTRE_TEXT_ARE_SEPARATE, false ) )
    		stringBuilder.append( "</td><td align=\"" + getColumnAlignmentFromProperty( Properties.PROPERTY_COLUMN_RIGHT_CENTRE_TEXT_ALIGNMENT ) + "\">" ); //$NON-NLS-1$ //$NON-NLS-2$

    	stringBuilder.append( Properties.getInstance().getProperty( Properties.PROPERTY_LAYOUT_RIGHT_CENTRE_TEXT, Properties.PROPERTY_LAYOUT_RIGHT_CENTRE_TEXT_DEFAULT, false ) );
    	if( Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_COLUMNS_RIGHT_CENTRE_TEXT_AND_RIGHT_OPTION_ARE_SEPARATE, false ) )
    		stringBuilder.append( "</td><td align=\"" + getColumnAlignmentFromProperty( Properties.PROPERTY_COLUMN_RIGHT_OPTION_ALIGNMENT ) + "\">" ); //$NON-NLS-1$ //$NON-NLS-2$

    	stringBuilder.append( getValueFromLayoutOption( Properties.PROPERTY_LAYOUT_RIGHT_OPTION, Properties.PROPERTY_LAYOUT_OPTION_TIME, timeZone, differentDayIndicator, dateTime ) );
    	if( Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_COLUMNS_RIGHT_OPTION_AND_RIGHT_TEXT_ARE_SEPARATE, false ) )
    		stringBuilder.append( "</td><td align=\"" + getColumnAlignmentFromProperty( Properties.PROPERTY_COLUMN_RIGHT_TEXT_ALIGNMENT ) + "\">" ); //$NON-NLS-1$ //$NON-NLS-2$

    	stringBuilder.append( Properties.getInstance().getProperty( Properties.PROPERTY_LAYOUT_RIGHT_TEXT, Properties.PROPERTY_LAYOUT_RIGHT_TEXT_DEFAULT, false ) );
    	stringBuilder.append( "</td>" ); //$NON-NLS-1$

    	stringBuilder.append( "</tr>" ); //$NON-NLS-1$

    	return stringBuilder.toString();
    }


	private static String getValueFromLayoutOption( String property, String defaultValue, String timeZone, String differentDayIndicator, String dateTime )
	{
		String value = Properties.getInstance().getProperty( property, defaultValue, false );

		if( Collator.getInstance().equals( Properties.PROPERTY_LAYOUT_OPTION_DIFFERENT_DAY_INDICATOR, value ) )
			return differentDayIndicator;
		else if( Collator.getInstance().equals( Properties.PROPERTY_LAYOUT_OPTION_NONE, value ) )
			return ""; //$NON-NLS-1$
		else if( Collator.getInstance().equals( Properties.PROPERTY_LAYOUT_OPTION_TIME, value ) )
			return dateTime;
		else
			return timeZone;
	}


	private static String getColumnAlignmentFromProperty( String property )
	{
		String value = Properties.getInstance().getProperty( property, "", true ); //$NON-NLS-1$
		if( Properties.PROPERTY_COLUMN_ALIGNMENT_LEFT.equals( value ) || Properties.PROPERTY_COLUMN_ALIGNMENT_CENTRE.equals( value ) || Properties.PROPERTY_COLUMN_ALIGNMENT_RIGHT.equals( value ) )
			return value;

		return  Properties.PROPERTY_COLUMN_ALIGNMENT_LEFT;
	}
}
