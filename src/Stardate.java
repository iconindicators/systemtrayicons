import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;


/**
 * Converts between Star Trek<SUP>&reg;</SUP> stardates and Gregorian date/times.<br>
 * <br>
 * Based on <i>STARDATES IN STAR TREK FAQ V1.6</i><br><br>
 * 
 * Stardates are related to Julian/Gregorian dates as depicted on the following time line:
 * <pre>     
 *      <------x------x------x------x------x------x------x------>
 *             |      |      |      |     |       |      |
 *             |      |      |      |     |       |      |
 *          6000 BC   |   1582 AD   | 2270-01-26  |  2323-01-01
 *                    |             |             |
 *                  46 BC       2162-01-04    2283-10-05
 * </pre>     
 * From 6000 BC up to 46 BC, the Egyptian-developed calendar was used.
 * Initially based on counting lunar cycles, it was eventually changed to a solar calendar.<br>
 * <br>
 * Between 46 BC to 1582 AD, the Julian calendar was used.  
 * This was the first calendar to introduce the "leap year".<br>
 * <br>
 * The Gregorian calendar commenced in 1582 and is in use to this day.
 * It is based on a modified version of the Julian calendar.<br>
 * <br>
 * In 2162, stardates were developed by Starfleet.  Stardate [0]0000.0 commenced on midnight 4/1/2162.  
 * The stardate rate from this date to 26/1/2270 was 5 units per day.<br>
 * <br>
 * Between 26/1/2270 and 5/10/2283 ([19]7340.0 and [19]7840.0, respectively) the stardate rate changes to 0.1 units per day.<br>
 * <br>
 * Between 5/10/2283 to 1/1/2323 ([19]7840.0 and [20]5006.0, respectively) the rate changes once again, this time to 0.5 units per day.<br>
 * <br>
 * From 1/1/2323 ([20]5006.0) the rate changed to 1000 units per mean solar year (365.2425 days) and stardate [20]5006.0 becomes [21]00000.0.
 */
public class Stardate
{
	/** API version. */
	private static final String API_VERSION = "Version 1.3 (2012-06-11)"; //$NON-NLS-1$


    /** Rates (in stardate units per day) for each stardate era. */
    private static final double[] ms_stardateRates = { 5.0, 5.0, 0.1, 0.5, 1000.0 / 365.2425 };


    /**
     * The Gregorian dates which reflect the start date for each rate.
     * For example, an index of 3 (Gregorian date of 5/10/2283) corresponds to the rate of 0.5 stardate units per day.
     * The month is zero-based (January = 0).
     */
    private static final
        GregorianCalendar[] ms_gregorianDates =
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
    	for( GregorianCalendar gregorianCalendar : ms_gregorianDates )
    		gregorianCalendar.setTimeZone( TimeZone.getTimeZone( "UTC" ) ); //$NON-NLS-1$
    }


    /** Internal representation for the Gregorian date/time. */
    private GregorianCalendar m_gregorian = new GregorianCalendar( TimeZone.getTimeZone( "UTC" ) ); //$NON-NLS-1$


    /** Internal representation for the stardate. */
    private int m_stardateIssue = 0, m_stardateInteger = 0, m_stardateFraction = 0;


    /** The index specifying the specific stardate rate. */
    private int m_index = 0;  
    

    /** Stardate constructor. */
    public Stardate() { /** Do nothing. */ }


    /**
     * Gets the API version.
     *
     * @return The API version.
     */
    public static String getVersion() { return API_VERSION; }


    /**
     * Gets the period (in seconds) between updates/changes to the current stardate.
     * 
     * @return The period (in seconds) between updates/changes to the current stardate.
     */
    public double getStardateFractionalPeriod() { return ( 1.0 / ( ms_stardateRates[ m_index ] / 24.0 / 60.0 / 60.0 ) / 10.0 ); }


    /**
	 * Sets a Gregorian date/time and converts to a stardate.
	 *
	 * @param dateTime The specified Gregorian date/time.
	 */
	public void setGregorian( GregorianCalendar dateTime )
	{
		// Have found a difference in the final stardate calculation between setting a date/time from using 'new GregorianCalendar()'
		// and setting each of year/month/day/hour/minute/second explicitly.
		// Compared with the Python implementation, setting explicitly yields an exact match.
		m_gregorian =
			new GregorianCalendar
			(
				dateTime.get( Calendar.YEAR ), 
				dateTime.get( Calendar.MONTH ), 
				dateTime.get( Calendar.DAY_OF_MONTH ), 
				dateTime.get( Calendar.HOUR_OF_DAY ), 
				dateTime.get( Calendar.MINUTE), 
				dateTime.get( Calendar.SECOND )
			);

		// Need to remove any timezone as this also gives an incorrect value.
		m_gregorian.setTimeZone( TimeZone.getTimeZone( "UTC" ) ); //$NON-NLS-1$

		gregorianToStardate();
	}


    /**
     * Sets an issue/integer/fraction for conversion to a Gregorian date/time.
     *
     * Rules:
     *   issue <= 19: 0 <= integer <= 9999, fraction >= 0. 
     *   issue == 20: 0 <= integer < 5006, fraction >= 0. 
     *   issue >= 21: 0 <= integer <= 99999, fraction > 0. 
     *
     * @param issue The issue number for the stardate (can be negative).
     * @param integer The integer part of a stardate.
     * @param fraction The fractional part of a stardate.
     */
    public void setStardate( int issue, int integer, int fraction )
    {
       	if( issue <= 19 && ( integer < 0 || integer > 9999 ) ) throw new IllegalArgumentException( "INTEGER PART MUST BE BETWEEN ZERO AND 9999 INCLUSIVE" ); //$NON-NLS-1$

        if( issue == 20 && ( integer < 0 || integer >= 5006 ) ) throw new IllegalArgumentException( "INTEGER PART MUST BE GREATER THAN OR EQUAL TO ZERO AND LESS THAN 5006" ); //$NON-NLS-1$

        if( issue >= 21 && ( integer < 0 || integer > 99999 ) ) throw new IllegalArgumentException( "INTEGER PART MUST BE BETWEEN ZERO AND 99999 INCLUSIVE" ); //$NON-NLS-1$

        if( fraction < 0 ) throw new IllegalArgumentException( "FRACTIONAL PART MUST BE GREATER THAN OR EQUAL TO ZERO" ); //$NON-NLS-1$

        m_stardateIssue = issue;
        m_stardateInteger = integer;
        m_stardateFraction = fraction;
        stardateToGregorian();
    }


    /**
     * Returns the current Gregorian date/time.
     *
     * @return The current Gregorian date/time.
     */
    public GregorianCalendar getGregorian() { return m_gregorian; }


    /**
     * Returns the current stardate <code>issue</code> value.
     *
     * @return The current stardate <code>issue</code> value.
     */
    public int getStardateIssue() { return m_stardateIssue; }


    /**
     * Returns the current stardate <code>integer</code> value.
     *
     * @return The current stardate <code>integer</code> value.
     */
    public int getStardateInteger() { return m_stardateInteger; }


    /**
     * Returns the current stardate <code>fraction</code> value.
     *
     * @return The current stardate <code>fraction</code> value.
     */
    public int getStardateFraction() { return m_stardateFraction; }


    /**
     * Returns the current value of the stardate in string format.
     *
     * @param padded If true, leading zeros will be inserted into the integer part of the stardate. 
     * @param showIssue If true, the issue part of the stardate will be included. 
     *
     * @return The current stardate as formatted string.
     */
    public String toStardateString( boolean padded, boolean showIssue )
    {
        StringBuilder stringBuilder = new StringBuilder();

        if( showIssue )
        	stringBuilder.append( "[" ).append( Integer.valueOf( m_stardateIssue ) ).append( "] " ); //$NON-NLS-1$ //$NON-NLS-2$

        if( padded )
        {
            if( m_stardateIssue >= 21 )
            {
            	// Need to pad up to 4 digits.
                if( m_stardateInteger < 10 ) stringBuilder.append( "0000" ); //$NON-NLS-1$
                else if( m_stardateInteger < 100 ) stringBuilder.append( "000" ); //$NON-NLS-1$
                else if( m_stardateInteger < 1000 ) stringBuilder.append( "00" ); //$NON-NLS-1$
                else if( m_stardateInteger < 10000 ) stringBuilder.append( "0" ); //$NON-NLS-1$
            }
            else
            {
            	// Need to pad up to 3 digits.
                if( m_stardateInteger < 10 ) stringBuilder.append( "000" ); //$NON-NLS-1$
                else if( m_stardateInteger < 100 ) stringBuilder.append( "00" ); //$NON-NLS-1$
                else if( m_stardateInteger < 1000 ) stringBuilder.append( "0" ); //$NON-NLS-1$
            }
        }

        stringBuilder.append( Integer.valueOf( m_stardateInteger ) ).append( "." ).append( Integer.valueOf( m_stardateFraction ) ); //$NON-NLS-1$
    	return stringBuilder.toString();
    }


    /**
     * Returns the current value of the Gregorian date/time in string format "yyyy-MM-dd HH:mm:ss".
     *
     * @return The current Gregorian date/time as formatted string.
     */
    public String toGregorianString() { return new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ).format( m_gregorian.getTime() ); } //$NON-NLS-1$


    /**
     * Returns a string representation of this object.
     *
     * @return a string representation of this object.
     */
    @Override
	public String toString() { return toStardateString( true, true ) + " | " + toGregorianString(); } //$NON-NLS-1$


    /** Converts the current stardate to the equivalent Gregorian date/time. */
    private void stardateToGregorian()
    {
        // The stardate rate based on the current stardate era.
        double rate = 0.0;

        // The number of units to which the current stardate reduces, relative to the start of its era.
        double units = 0.0;

        // The number of days, hours, minutes and seconds the current stardate exceeds the corresponding start date.
        double days = 0.0, hours = 0.0, minutes = 0.0, seconds = 0.0;

        // Work out the stardate era...
        int fractionLength = Integer.valueOf( m_stardateFraction ).toString().length();
        double fractionDivisor = Math.pow( 10.0, fractionLength );
        if( m_stardateIssue < 0 ) // Pre-stardate (pre 4/1/2162).
        {
            m_index = 0;
            units = m_stardateIssue * 10000.0 + m_stardateInteger + m_stardateFraction / fractionDivisor;
        }
        else if( m_stardateIssue >= 0 && m_stardateIssue < 19 ) // First period of stardates (4/1/2162 - 26/1/2270).
        {
        	m_index = 1;
            units = m_stardateIssue * 1000.0  + m_stardateInteger  + m_stardateFraction / fractionDivisor;
        }
        else if( m_stardateIssue == 19 && m_stardateInteger < 7340 ) // First period of stardates (4/1/2162 - 26/1/2270).
        {
        	m_index = 1;
            units = m_stardateIssue * 19.0 * 1000.0 +  m_stardateInteger  +  m_stardateFraction / fractionDivisor;
        }
        else if( m_stardateIssue == 19 && m_stardateInteger >= 7340 && m_stardateInteger < 7840 ) // Second period of stardates (26/1/2270 - 5/10/2283)
        {
        	m_index = 2;
            units = m_stardateInteger + m_stardateFraction / fractionDivisor - 7340;
        }
        else if( m_stardateIssue == 19 && m_stardateInteger >= 7840 ) // Third period of stardates (5/10/2283 - 1/1/2323)
        {
        	m_index = 3;
            units = m_stardateInteger + m_stardateFraction / fractionDivisor - 7840;
        }
        else if( m_stardateIssue == 20 && m_stardateInteger < 5006 ) // Third period of stardates (5/10/2283 - 1/1/2323)
        {
        	m_index = 3;
            units = 1000.0 + m_stardateInteger + m_stardateFraction / fractionDivisor;
        }
        else if( m_stardateIssue >= 21 ) // Fourth period of stardates (1/1/2323 - )
        {
        	m_index = 4;
            units = ( m_stardateIssue - 21 ) * 10000.0 + m_stardateInteger + m_stardateFraction / fractionDivisor;
        }
        else
        {
        	throw new IllegalStateException( "Invalid stardate: " + toStardateString( true, true ) ); //$NON-NLS-1$
        }

        // Convert the current amount of units to the equivalent Gregorian date.
        rate = ms_stardateRates[ m_index ];
        units = Math.round( 10.0 * units ) / 10.0;
        days = units / rate;
        hours = ( days - (int)days ) * 24.0;
        minutes = ( hours - (int)hours ) * 60.0;
        seconds = ( minutes - (int)minutes ) * 60.0;

        // Get the start date for this era.
        m_gregorian = (GregorianCalendar)ms_gregorianDates[ m_index ].clone();

        // Add the days, hours, minutes and seconds to the base date to get the current Gregorian date.
        m_gregorian.add( Calendar.DATE, (int)days );
        m_gregorian.add( Calendar.HOUR_OF_DAY, (int)hours );
        m_gregorian.add( Calendar.MINUTE, (int)minutes );
        m_gregorian.add( Calendar.SECOND, (int)seconds );
    }


    /** Converts the current Gregorian date/time to the equivalent stardate. */
    private void gregorianToStardate()
    {
        int stardateIssues[] = { -1, 0, 19, 19, 21 };
        int stardateIntegers[] = { 0, 0, 7340, 7840, 0 };
        int stardateRange[] = { 10000, 10000, 10000, 10000, 100000 };

        // Determine which era the given Gregorian date falls...
        int year = m_gregorian.get( Calendar.YEAR );
        int month = m_gregorian.get( Calendar.MONTH ) + 1; // Month is zero-based.
        int day = m_gregorian.get( Calendar.DATE );
        if( ( year < 2162 ) || ( year == 2162 && month == 1 && day < 4 ) ) 
        {
            // Pre-stardate era (pre 4/1/2162)...do the conversion here because a negative time is generated and throws out all other cases.
            long numberOfMillis = ms_gregorianDates[ 0 ].getTime().getTime() - m_gregorian.getTime().getTime();
            double numberOfDays = numberOfMillis / 1000.0 / 60.0 / 60.0 / 24.0;
            double rate = ms_stardateRates[ 0 ];
            double units = numberOfDays * rate;
            double remainder = units % stardateRange[ 0 ];

            if( (int)remainder == 0 )
                m_stardateIssue = -1 * (int)( units / stardateRange[ 0 ] );
            else
                m_stardateIssue = ( -1 * (int)( units / stardateRange[ 0 ] ) ) + stardateIssues[ 0 ];

            remainder = ( -1 * m_stardateIssue * stardateRange[ 0 ] ) - units;
            m_stardateInteger = (int)remainder;
            m_stardateFraction = (int)( remainder * 10.0 ) - ( (int)remainder * 10 );
            return;
        }

        // Remainder of time periods can be treated equally...
        if( ( year == 2162 && month == 1 && day >= 4 ) || ( year == 2162 && month > 1 ) || ( year > 2162 && year < 2270 ) || ( year == 2270 && month == 1 && day < 26 ) ) 
        	m_index = 1; // First period of stardates (4/1/2162 - 26/1/2270).
        else if( ( year == 2270 && month == 1 && day >= 26 ) || ( year == 2270 & month > 1 ) || ( year > 2270 && year < 2283 ) || ( year == 2283 && month < 10 ) || ( year == 2283 && month == 10 && day < 5 ) )
        	m_index = 2; // Second period of stardates (26/1/2270 - 5/10/2283)
        else if( ( year == 2283 && month == 10 && day >= 5 ) || ( year == 2283 && month > 10 ) || ( year > 2283 && year < 2323 ) )
        	m_index = 3; // Third period of stardates (5/10/2283 - 1/1/2323)
        else if( year >= 2323 ) // Fourth period of stardates (1/1/2323 - )
        	m_index = 4;
        else
        	throw new IllegalStateException( "Invalid date: " + m_gregorian ); //$NON-NLS-1$

        // Now convert...
        long numberOfMillis = m_gregorian.getTime().getTime() - ms_gregorianDates[ m_index ].getTime().getTime();
        double numberOfDays = numberOfMillis / 1000.0 / 60.0 / 60.0 / 24.0;
        double rate = ms_stardateRates[ m_index ];
        double units = numberOfDays * rate;
        m_stardateIssue = (int)( units / stardateRange[ m_index ] ) + stardateIssues[ m_index ];
        double remainder = units % stardateRange[ m_index ];
        m_stardateInteger = (int)remainder + stardateIntegers[ m_index ];
        m_stardateFraction = (int)( remainder * 10.0 ) - ( (int)remainder * 10 );
    }
}