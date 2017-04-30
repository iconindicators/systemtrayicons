import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;


/**
 * Converts between Star Trek&#8482; stardates and Gregorian date/times.<br>
 * There are two types of stardates: 'classic' and '2009 revised'.<br><br><br>
 *
 * <b><u>'classic' stardates</u></b><br>
 * The 'classic' stardate is based on STARDATES IN STAR TREK FAQ V1.6 by Andrew Main.<br>
 * <pre> 
 *      <------x------x------x------x------x------x------x------>
 *             |      |      |      |     |       |      |
 *             |      |      |      |     |       |      |
 *          6000 BC   |   1582 AD   | 2270-01-26  |  2323-01-01
 *                    |             |             |
 *                  46 BC       2162-01-04    2283-10-05
 * </pre>
 * From 6000 BC up to 46 BC, the Egyptian-developed calendar was used.<br>
 * Initially based on counting lunar cycles, it was eventually changed to a solar calendar.<br><br>
 * 
 * Between 46 BC to 1582 AD, the Julian calendar was used.<br>
 * This was the first calendar to introduce the "leap year".<br><br>
 * 
 * The Gregorian calendar commenced in 1582 and is in use to this day.<br>
 * It is based on a modified version of the Julian calendar.<br><br>
 * 
 * In 2162, stardates were developed by Starfleet.<br>
 * Stardate [0]0000.0 commenced on midnight 4/1/2162.<br>
 * The stardate rate from this date to 26/1/2270 was 5 units per day.<br><br>
 * 
 * Between 26/1/2270 and 5/10/2283 ([19]7340.0 and [19]7840.0, respectively)<br>
 * the rate changes to 0.1 units per day.<br><br>
 * 
 * Between 5/10/2283 to 1/1/2323 ([19]7840.0 and [20]5006.0, respectively),<br>
 * the rate changes to 0.5 units per day.<br><br>
 * 
 * From 1/1/2323 ([20]5006.0) the rate changed to 1000 units per mean solar year (365.2425 days).<br>
 * Also, stardate [20]5006.0 becomes [21]00000.0.<br><br><br>
 * 
 * <b><u>'2009 revised' stardates</u></b><br>
 * The '2009 revised' stardate is based on
 * <a href="http://en.wikipedia.org/wiki/Stardate">http://en.wikipedia.org/wiki/Stardate</a>.
 */
public class Stardate
{
    /**
     * Get the API version.
     *
     * @return The API version.
     */
	public static String getVersion() { return "Version 4.0 (2017-04-28)"; } //$NON-NLS-1$


    /** Rates (in 'classic' stardate units per day) for each 'classic' stardate era. */
    private static double[] ms_stardateClassicRates = new double[] { 5.0, 5.0, 0.1, 0.5, 1000.0 / 365.2425 };


    /**
     * The Gregorian dates which reflect the start date for each rate in the 'classic' stardate era.
     * 
     * For example, an index of 3 (Gregorian date of 5/10/2283,
     * noting that months are zero-based and January = 0),
     * corresponds to the rate of 0.5 stardate units per day.
     */
    private static GregorianCalendar[] ms_stardateClassicGregorianStartDates = 
        new GregorianCalendar[]
    	{
            new GregorianCalendar( 2162, 0, 4 ),
            new GregorianCalendar( 2162, 0, 4 ),
            new GregorianCalendar( 2270, 0, 26 ),
            new GregorianCalendar( 2283, 9, 5 ),
            new GregorianCalendar( 2323, 0, 1 )
        };


    static
    {
    	// Based on this bug http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4255109 ensure all timezones are UTC.
    	for( GregorianCalendar gregorianCalendar : ms_stardateClassicGregorianStartDates )
    		gregorianCalendar.setTimeZone( TimeZone.getTimeZone( "UTC" ) ); //$NON-NLS-1$
    }


    /**
     * Build a new GregorianCalendar initialised to the same attributes as that passed in but also set to UTC.
     *
     * Have found a difference in the final stardate calculation between
     * setting a date/time from using 'new GregorianCalendar()' and
     * setting each of year/month/day/hour/minute/second explicitly.
     * 
     * Compared with the Python implementation, setting explicitly yields an exact match.
     *
     * @param gregorianCalendar
     * 
     * @return The GregorianCalendar.
     */
    private static GregorianCalendar getGregorianCalendar( GregorianCalendar gregorianCalendar )
    {
		GregorianCalendar _gregorianCalendar =
			new GregorianCalendar
			(
				gregorianCalendar.get( Calendar.YEAR ), 
				gregorianCalendar.get( Calendar.MONTH ), 
				gregorianCalendar.get( Calendar.DAY_OF_MONTH ), 
				gregorianCalendar.get( Calendar.HOUR_OF_DAY ), 
				gregorianCalendar.get( Calendar.MINUTE), 
				gregorianCalendar.get( Calendar.SECOND )
			);

		// Need to remove any timezone as this also gives an incorrect value.
		_gregorianCalendar.setTimeZone( TimeZone.getTimeZone( "UTC" ) ); //$NON-NLS-1$

		return _gregorianCalendar;
    }


    /**
	 * Converts the Gregorian date/time in UTC to a 'classic' stardate.
	 * 
     * @param gregorianCalendar The specified Gregorian date/time in UTC.
     * 
     * @return A 'classic' stardate as an array of integers comprising:
     * 			stardate issue
     * 			stardate integer
     * 			stardate fraction
     * 			fractionalPeriod (time in seconds when the fractional part changes).
     */
    public static int[] getStardateClassic( GregorianCalendar gregorianCalendar )
    {
    	if( gregorianCalendar == null ) throw new IllegalArgumentException( "Gregorian calendar cannot be null." ); //$NON-NLS-1$

		GregorianCalendar _gregorianCalendar = getGregorianCalendar( gregorianCalendar );

        int stardateIssues[] = { -1, 0, 19, 19, 21 };
        int stardateIntegers[] = { 0, 0, 7340, 7840, 0 };
        int stardateRange[] = { 10000, 10000, 10000, 10000, 100000 };
        int index = -1;

        // Determine into which era the given Gregorian date falls...
        int year = _gregorianCalendar.get( Calendar.YEAR );
        int month = _gregorianCalendar.get( Calendar.MONTH ) + 1; // Month is zero-based.
        int day = _gregorianCalendar.get( Calendar.DATE );
        int stardateIssue, stardateInteger, stardateFraction;
        double rate;
        if( ( year < 2162 ) || ( year == 2162 && month == 1 && day < 4 ) ) 
        {
            // Pre-stardate era (pre 4/1/2162)...do the conversion here because a negative time is generated and throws out all other cases.
        	index = 0;
        	long numberOfMillis = ms_stardateClassicGregorianStartDates[ index ].getTime().getTime() - _gregorianCalendar.getTime().getTime();
            double numberOfDays = numberOfMillis / 1000.0 / 60.0 / 60.0 / 24.0;
            rate = ms_stardateClassicRates[ index ];
            double units = numberOfDays * rate;

            stardateIssue = stardateIssues[ index ] - (int)( units / stardateRange[ index ] );

            double remainder = stardateRange[ index ] - ( units % stardateRange[ index ] );
            stardateInteger = (int)remainder;
            stardateFraction = (int)( remainder * 10.0 ) - ( (int)remainder * 10 );
        }
        else
        {
	        // Remainder of time periods can be treated equally...
	        if( ( year == 2162 && month == 1 && day >= 4 ) || ( year == 2162 && month > 1 ) || ( year > 2162 && year < 2270 ) || ( year == 2270 && month == 1 && day < 26 ) ) 
	        	index = 1; // First period of stardates (4/1/2162 - 26/1/2270).
	        else if( ( year == 2270 && month == 1 && day >= 26 ) || ( year == 2270 & month > 1 ) || ( year > 2270 && year < 2283 ) || ( year == 2283 && month < 10 ) || ( year == 2283 && month == 10 && day < 5 ) )
	        	index = 2; // Second period of stardates (26/1/2270 - 5/10/2283)
	        else if( ( year == 2283 && month == 10 && day >= 5 ) || ( year == 2283 && month > 10 ) || ( year > 2283 && year < 2323 ) )
	        	index = 3; // Third period of stardates (5/10/2283 - 1/1/2323)
	        else if( year >= 2323 ) // Fourth period of stardates (1/1/2323 - )
	        	index = 4;
	        else
	        	throw new IllegalStateException( "Invalid date: " + _gregorianCalendar ); //$NON-NLS-1$
	
	        // Now convert...
	        long numberOfMillis = _gregorianCalendar.getTime().getTime() - ms_stardateClassicGregorianStartDates[ index ].getTime().getTime();
	        double numberOfDays = numberOfMillis / 1000.0 / 60.0 / 60.0 / 24.0;
	        rate = ms_stardateClassicRates[ index ];
	        double units = numberOfDays * rate;
	        stardateIssue = (int)( units / stardateRange[ index ] ) + stardateIssues[ index ];
	        double remainder = units % stardateRange[ index ];
	        stardateInteger = (int)remainder + stardateIntegers[ index ];
	        stardateFraction = (int)( remainder * 10.0 ) - ( (int)remainder * 10 );
        }

        return new int[] { stardateIssue, stardateInteger, stardateFraction, (int)( ( 24.0 * 60.0 * 60.0 ) / ( rate * 10.0 ) ) };
    }


    /**
	 * Converts the Gregorian date/time in UTC to a '2009 revised' stardate.
	 * 
     * @param gregorianCalendar The specified Gregorian date/time in UTC.
     * 
     * @return A '2009 revised' stardate as an array of integers comprising:
     * 			stardate integer
     * 			stardate fraction
     * 			fractionalPeriod (time in seconds when the fractional part changes).
     */
    public static int[] getStardate2009Revised( GregorianCalendar gregorianCalendar )
    {
    	if( gregorianCalendar == null ) throw new IllegalArgumentException( "Gregorian calendar cannot be null." ); //$NON-NLS-1$

		GregorianCalendar _gregorianCalendar = getGregorianCalendar( gregorianCalendar );
    	int stardateInteger = _gregorianCalendar.get( Calendar.YEAR );
    	int stardateFraction = _gregorianCalendar.get( Calendar.DAY_OF_YEAR );
    	int fractionalPeriod = 24 * 60 * 60;
        return new int[] { stardateInteger, stardateFraction, fractionalPeriod };
    }


    /**
     * Converts a 'classic' stardate to a Gregorian date/time.
     *
     * Rules:
     *   issue <= 19: 0 <= integer <= 9999, fraction >= 0. 
     *   issue == 20: 0 <= integer < 5006, fraction >= 0. 
     *   issue >= 21: 0 <= integer <= 99999, fraction > 0. 
     *
     * @param stardateIssue The issue number for the stardate (can be negative).
     * @param stardateInteger The integer part of a stardate.
     * @param stardateFraction The fractional part of a stardate.
     * 
     * @return A GregorianCalendar.
     */
    public static GregorianCalendar getGregorianFromStardateClassic( int stardateIssue, int stardateInteger, int stardateFraction )
    {
       	if( stardateIssue <= 19 && ( stardateInteger < 0 || stardateInteger > 9999 ) ) throw new IllegalArgumentException( "Integer part must be between zero and 9999 inclusive" ); //$NON-NLS-1$

        if( stardateIssue == 20 && ( stardateInteger < 0 || stardateInteger >= 5006 ) ) throw new IllegalArgumentException( "Integer part must be greater than or equal to zero and less than 5006" ); //$NON-NLS-1$

        if( stardateIssue >= 21 && ( stardateInteger < 0 || stardateInteger > 99999 ) ) throw new IllegalArgumentException( "Integer part must be between zero and 99999 inclusive" ); //$NON-NLS-1$

        if( stardateFraction < 0 ) throw new IllegalArgumentException( "Fractional part must be greater than or equal to zero." ); //$NON-NLS-1$

        // The number of units to which the current stardate reduces, relative to the start of its era.
        double units = 0.0;

        // Work out the stardate era...
        int index;
        int fractionLength = Integer.valueOf( stardateFraction ).toString().length();
        double fractionDivisor = Math.pow( 10.0, fractionLength );
        if( stardateIssue < 0 ) // Pre-stardate (pre 4/1/2162).
        {
        	index = 0;
            units = stardateIssue * 10000.0 + stardateInteger + stardateFraction / fractionDivisor;
        }
        else if( stardateIssue >= 0 && stardateIssue < 19 ) // First period of stardates (4/1/2162 - 26/1/2270).
        {
        	index = 1;
            units = stardateIssue * 1000.0 + stardateInteger + stardateFraction / fractionDivisor;
        }
        else if( stardateIssue == 19 && stardateInteger < 7340 ) // First period of stardates (4/1/2162 - 26/1/2270).
        {
        	index = 1;
            units = stardateIssue * 19.0 * 1000.0 + stardateInteger + stardateFraction / fractionDivisor;
        }
        else if( stardateIssue == 19 && stardateInteger >= 7340 && stardateInteger < 7840 ) // Second period of stardates (26/1/2270 - 5/10/2283)
        {
        	index = 2;
            units = stardateInteger + stardateFraction / fractionDivisor - 7340;
        }
        else if( stardateIssue == 19 && stardateInteger >= 7840 ) // Third period of stardates (5/10/2283 - 1/1/2323)
        {
        	index = 3;
            units = stardateInteger + stardateFraction / fractionDivisor - 7840;
        }
        else if( stardateIssue == 20 && stardateInteger < 5006 ) // Third period of stardates (5/10/2283 - 1/1/2323)
        {
        	index = 3;
            units = 1000.0 + stardateInteger + stardateFraction / fractionDivisor;
        }
        else if( stardateIssue >= 21 ) // Fourth period of stardates (1/1/2323 - )
        {
        	index = 4;
            units = ( stardateIssue - 21 ) * 10000.0 + stardateInteger + stardateFraction / fractionDivisor;
        }
        else { throw new IllegalStateException( "Invalid stardate: " + toStardateClassicString( stardateIssue, stardateInteger, stardateFraction, true, false ) ); } //$NON-NLS-1$

        // Convert the current amount of units to the equivalent Gregorian date.
        double rate = ms_stardateClassicRates[ index ];
        double days = units / rate;
        double hours = ( days - (int)days ) * 24.0;
        double minutes = ( hours - (int)hours ) * 60.0;
        double seconds = ( minutes - (int)minutes ) * 60.0;

        // Get the start date for this era.
        GregorianCalendar gregorianCalendar =
    		new GregorianCalendar
    		(
				ms_stardateClassicGregorianStartDates[ index ].get( Calendar.YEAR ),
				ms_stardateClassicGregorianStartDates[ index ].get( Calendar.MONTH ),
				ms_stardateClassicGregorianStartDates[ index ].get( Calendar.DAY_OF_MONTH )
			);

        // Add the days, hours, minutes and seconds to the base date to get the current Gregorian date.
        gregorianCalendar.add( Calendar.DATE, (int)days );
        gregorianCalendar.add( Calendar.HOUR_OF_DAY, (int)hours );
        gregorianCalendar.add( Calendar.MINUTE, (int)minutes );
        gregorianCalendar.add( Calendar.SECOND, (int)seconds );

        return gregorianCalendar;
    }


    /**
     * Converts a '2009 revised' stardate to a Gregorian date/time.
     *
     * @param stardateInteger The integer part of a stardate (corresponds to a Gregorian year).
     * @param stardateFraction The fractional part of a stardate (0 <= fraction <= 365, or 366 if integer corresponds to a leap year).
     * 
     * @return A GregorianCalendar.
     */    
    public static GregorianCalendar getGregorianFromStardate2009Revised( int stardateInteger, int stardateFraction )
    {
        if( stardateFraction < 0 ) throw new IllegalArgumentException( "Fraction cannot be negative." ); //$NON-NLS-1$

        boolean isLeapYear = ( stardateInteger % 4 == 0 && stardateInteger % 100 != 0 ) || stardateInteger % 400 == 0;
        if( isLeapYear )
        {
            if( stardateFraction > 366 ) throw new IllegalArgumentException( "Integer cannot exceed 366." ); //$NON-NLS-1$
        }
        else
        {
            if( stardateFraction > 365 ) throw new IllegalArgumentException( "Integer cannot exceed 365." ); //$NON-NLS-1$
        }

        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.set( Calendar.YEAR, stardateInteger );
        gregorianCalendar.set( Calendar.DAY_OF_YEAR, stardateFraction );
        return gregorianCalendar;
    }


    /**
     * Returns a stardate in string format.
     * 
     * @param stardateIssue The stardate issue.
     * @param stardateInteger The stardate integer.
     * @param stardateFraction The stardate fraction.
     * @param showIssue If true, the issue will be included in the string.
     * @param padded If true, the integer part of the string will be zero padded (if required).
     * 
     * @return A stardate in string format.
     */
    public static String toStardateClassicString( int stardateIssue, int stardateInteger, int stardateFraction, boolean showIssue, boolean padded )
    {
        StringBuilder stringBuilder = new StringBuilder();

        if( showIssue ) stringBuilder.append( "[" ).append( Integer.toString( stardateIssue ) ).append( "] " ); //$NON-NLS-1$ //$NON-NLS-2$

        if( padded )
        {
        	int padding;
        	if( stardateIssue < 21 )
            	padding = "1000".length() - Integer.toString( stardateInteger ).length(); //$NON-NLS-1$
        	else
        		padding = "10000".length() - Integer.toString( stardateInteger ).length(); //$NON-NLS-1$

        	String integer = Integer.toString( stardateInteger );
        	for( int i = 0; i < padding; i++ )
        		integer = "0" + integer; //$NON-NLS-1$

    		stringBuilder.append( integer );
        }
        else { stringBuilder.append( Integer.toString( stardateInteger ) ); }

        stringBuilder.append( "." ).append( Integer.toString( stardateFraction ) ); //$NON-NLS-1$
    	return stringBuilder.toString();
    }


    /**
     * Returns a stardate in string format.
     * 
     * @param stardateInteger The stardate integer.
     * @param stardateFraction The stardate fraction.
     * 
     * @return A stardate in string format.
	 */
    public static String toStardateRevised2009String( int stardateInteger, int stardateFraction )
    {
    	return Integer.toString( stardateInteger ) + "." + Integer.toString( stardateFraction ); //$NON-NLS-1$
    }
}