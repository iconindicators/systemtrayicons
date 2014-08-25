import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;


/**
 * Converts between Star Trek&#8482; stardates and Gregorian date/times.<br>
 * There are two types of stardates: 'classic' and '2009 revised'.<br><br>
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
 * Also, stardate [20]5006.0 becomes [21]00000.0.<br><br>
 * 
 * <b><u>'2009 revised' stardates</u></b><br>
 * The '2009 revised' stardate is based on
 * <a href="http://en.wikipedia.org/wiki/Stardate">http://en.wikipedia.org/wiki/Stardate</a>.
 */
public class Stardate
{
	/** API version. */
	private static final String API_VERSION = "Version 3.2 (2014-08-25)"; //$NON-NLS-1$


    /** Rates (in stardate units per day) for each 'classic' stardate era. */
    private static final double[] ms_stardateRates = { 5.0, 5.0, 0.1, 0.5, 1000.0 / 365.2425 };


    /**
     * The Gregorian dates which reflect the start date for each rate in the 'classic' stardate era.
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


    /** Internal representation for the 'classic' stardate. */
    private int m_stardateIssue = 0, m_stardateInteger = 0, m_stardateFraction = 0;


    /** The index specifying the specific 'classic' stardate rate. */
    private int m_index = -1;
    

    /** true = 'classic' conversion; false = '2009 revised' conversion. */
    private boolean m_classic = true;
    

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
    public double getStardateFractionalPeriod()
    {
    	if( m_index == -1 ) throw new IllegalStateException( "Please set a valid gregorian date or stardate." ); //$NON-NLS-1$

    	return ( 1.0 / ( ms_stardateRates[ m_index ] / 24.0 / 60.0 / 60.0 ) / 10.0 );
    }


    /**
     * Sets the conversion method, either 'classic' or '2009 revised'.
     * 
     * @param classic If true, 'classic' conversion is used; otherwise '2009 revised' conversion. 
     */
    public void setClassic( boolean classic ) { m_classic = classic; }


    /**
     * Gets the conversion method, either 'classic' or '2009 revised'.
     * 
     * @return Returns true if 'classic' conversion is used; false if '2009 revised' conversion.
     */
    public boolean getClassic() { return m_classic; }


    /**
	 * Sets a Gregorian date/time in UTC and converts to a ('classic' or '2009 revised') stardate.
	 * Note the 'classic' status must be set PRIOR to setting the Gregorian date/time.
	 * 
	 * @param dateTime The specified Gregorian date/time in UTC.
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

		if( m_classic ) gregorianToStardateClassic();
		else gregorianToStardate2009Revised();
	}


    /**
     * Sets a 'classic' stardate for conversion to a Gregorian date/time.
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
    public void setStardateClassic( int issue, int integer, int fraction )
    {
       	if( issue <= 19 && ( integer < 0 || integer > 9999 ) ) throw new IllegalArgumentException( "INTEGER PART MUST BE BETWEEN ZERO AND 9999 INCLUSIVE" ); //$NON-NLS-1$

        if( issue == 20 && ( integer < 0 || integer >= 5006 ) ) throw new IllegalArgumentException( "INTEGER PART MUST BE GREATER THAN OR EQUAL TO ZERO AND LESS THAN 5006" ); //$NON-NLS-1$

        if( issue >= 21 && ( integer < 0 || integer > 99999 ) ) throw new IllegalArgumentException( "INTEGER PART MUST BE BETWEEN ZERO AND 99999 INCLUSIVE" ); //$NON-NLS-1$

        if( fraction < 0 ) throw new IllegalArgumentException( "FRACTIONAL PART MUST BE GREATER THAN OR EQUAL TO ZERO" ); //$NON-NLS-1$

        m_stardateIssue = issue;
        m_stardateInteger = integer;
        m_stardateFraction = fraction;
        stardateToGregorianClassic();
    }


    /**
     * Sets a '2009 revised' stardate for conversion to a Gregorian date/time.
     * 
     * @param integer The integer part of a stardate (corresponds to a Gregorian year).
     * @param fraction The fractional part of a stardate (0 <= fraction <= 365, or 366 if integer corresponds to a leap year). 
     */
    public void setStardate2009Revised( int integer, int fraction )
    {
        if( fraction < 0 ) throw new IllegalArgumentException( "Fraction cannot be negative." );

        boolean isLeapYear = ( integer % 4 == 0 && integer % 100 != 0 ) || integer % 400 == 0;
        if( isLeapYear )
        {
            if( fraction > 366 ) throw new IllegalArgumentException( "Integer cannot exceed 366." );
        }
        else
        {
            if( fraction > 365 ) throw new IllegalArgumentException( "Integer cannot exceed 365." );
        }

        m_stardateInteger = integer;
        m_stardateFraction = fraction;
        stardateToGregorian2009Revised();
    }


    /**
     * Returns the current Gregorian date/time in UTC.
     *
     * @return The current Gregorian date/time in UTC.
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
     * @param showIssue If true, the issue part of the 'classic' stardate will be included (ignored for '2009 revised').
     * @param padded If true, leading zeros will be inserted into the integer part of the 'classic' stardate (ignored for '2009 revised').
     *
     * @return The current stardate as formatted string.
     */
    public String toStardateString( boolean showIssue, boolean padded )
    {
        StringBuilder stringBuilder = new StringBuilder();

        if( m_classic )
        {
	        if( showIssue ) stringBuilder.append( "[" ).append( Integer.toString( m_stardateIssue ) ).append( "] " ); //$NON-NLS-1$ //$NON-NLS-2$

	        if( padded )
	        {
	        	int padding;
	        	if( m_stardateIssue < 21 )
	            	padding = "1000".length() - Integer.toString( m_stardateInteger ).length();
	        	else
	        		padding = "10000".length() - Integer.toString( m_stardateInteger ).length();

	        	String integer = Integer.toString( m_stardateInteger );
	        	for( int i = 0; i < padding; i++ )
	        		integer = "0" + integer;

        		stringBuilder.append( integer );
	        }
	        else { stringBuilder.append( Integer.toString( m_stardateInteger ) ); }

	        stringBuilder.append( "." ).append( Integer.toString( m_stardateFraction ) ); //$NON-NLS-1$
        }
        else
        {
            stringBuilder.append( Integer.toString( m_stardateInteger ) ).append( "." ).append( Integer.toString( m_stardateFraction ) );
        }

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
	public String toString()
    {
    	String s;

    	if( m_classic )
    	{
    		s = toGregorianString();
        	if( m_index != -1 )
        		s = toStardateString( true, true ) + " | " + s; //$NON-NLS-1$
    	}
    	else
    	{
    		s = toStardateString( true, true ) + " | " + toGregorianString(); //$NON-NLS-1$    		
    	}

    	return s;
    }


    /** Converts the current stardate to the equivalent Gregorian date/time. */
    private void stardateToGregorianClassic()
    {
        // The number of units to which the current stardate reduces, relative to the start of its era.
        double units = 0.0;

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
            units = m_stardateIssue * 1000.0 + m_stardateInteger + m_stardateFraction / fractionDivisor;
        }
        else if( m_stardateIssue == 19 && m_stardateInteger < 7340 ) // First period of stardates (4/1/2162 - 26/1/2270).
        {
        	m_index = 1;
            units = m_stardateIssue * 19.0 * 1000.0 + m_stardateInteger + m_stardateFraction / fractionDivisor;
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
        double rate = ms_stardateRates[ m_index ];
        double days = units / rate;
        double hours = ( days - (int)days ) * 24.0;
        double minutes = ( hours - (int)hours ) * 60.0;
        double seconds = ( minutes - (int)minutes ) * 60.0;

        // Get the start date for this era.
        m_gregorian = new GregorianCalendar( ms_gregorianDates[ m_index ].get( Calendar.YEAR ), ms_gregorianDates[ m_index ].get( Calendar.MONTH ), ms_gregorianDates[ m_index ].get( Calendar.DAY_OF_MONTH ) );

        // Add the days, hours, minutes and seconds to the base date to get the current Gregorian date.
        m_gregorian.add( Calendar.DATE, (int)days );
        m_gregorian.add( Calendar.HOUR_OF_DAY, (int)hours );
        m_gregorian.add( Calendar.MINUTE, (int)minutes );
        m_gregorian.add( Calendar.SECOND, (int)seconds );
    }


    /** Converts the current Gregorian date/time to the equivalent stardate. */
    private void gregorianToStardateClassic()
    {
        int stardateIssues[] = { -1, 0, 19, 19, 21 };
        int stardateIntegers[] = { 0, 0, 7340, 7840, 0 };
        int stardateRange[] = { 10000, 10000, 10000, 10000, 100000 };
        m_index = -1;

        // Determine which era the given Gregorian date falls...
        int year = m_gregorian.get( Calendar.YEAR );
        int month = m_gregorian.get( Calendar.MONTH ) + 1; // Month is zero-based.
        int day = m_gregorian.get( Calendar.DATE );
        if( ( year < 2162 ) || ( year == 2162 && month == 1 && day < 4 ) ) 
        {
            // Pre-stardate era (pre 4/1/2162)...do the conversion here because a negative time is generated and throws out all other cases.
        	m_index = 0;
        	long numberOfMillis = ms_gregorianDates[ m_index ].getTime().getTime() - m_gregorian.getTime().getTime();
            double numberOfDays = numberOfMillis / 1000.0 / 60.0 / 60.0 / 24.0;
            double rate = ms_stardateRates[ m_index ];
            double units = numberOfDays * rate;

            m_stardateIssue = stardateIssues[ m_index ] - (int)( units / stardateRange[ m_index ] );

            double remainder = stardateRange[ m_index ] - ( units % stardateRange[ m_index ] );
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


    /** Converts the current '2009 revised' stardate to the equivalent Gregorian date/time. */
    private void stardateToGregorian2009Revised()
    {
        m_gregorian = new GregorianCalendar();
        m_gregorian.set( Calendar.YEAR, m_stardateInteger );
        m_gregorian.set( Calendar.DAY_OF_YEAR, m_stardateFraction );
    }


    /** Converts the current Gregorian date/time to the equivalent '2009 revised' stardate. */
    private void gregorianToStardate2009Revised()
    {
    	m_stardateIssue = 0; // Set to zero as it has no meaning here.
    	m_stardateInteger = m_gregorian.get( Calendar.YEAR );
    	m_stardateFraction = m_gregorian.get( Calendar.DAY_OF_YEAR );
    }
}