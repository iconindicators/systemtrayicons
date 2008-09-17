import java.util.Calendar;
import java.util.GregorianCalendar;


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
 * From 1/1/2323 ([20]5006.0) the rate changed to 1000 units per mean solar year (365.2425 days).  
 * Also, stardate [20]5006.0 becomes [21]00000.0.
 */
public class Stardate
{
	/** API version. */
	private static final String API_VERSION = "Version 1.1 (2008-06-11)"; //$NON-NLS-1$  //TODO Change this!!!  Should be API version


	/** Internal representation for the Gregorian Calendar date/time. */
    private GregorianCalendar m_gregorianCalendar = null;


    /** Internal representation for the stardate. */
    private int m_issue = 0, m_integer = 0, m_fraction = 0;


    /**
     * Flag indicating that if any part of the current stardate has changed, 
     * the entire Gregorian Calendar needs to be recalculated.
     */
    private boolean m_recalculateGregorian = false;


    /**
     * Flag indicating that if any part of the current Gregorian Calendar has changed, 
     * the entire star date needs to be recalculated.
     */
    private boolean m_recalculateStardate = false;


    /**
     * Rates (in stardate units per day) for each stardate era.
     */
    private static final double[] m_stardateRates = { 5.0, 5.0, 0.1, 0.5, 1000.0 / 365.2425 };


    /**
     * The Gregorian dates which reflect the start date for each rate.
     * For example, if the array index 3, this corresponds to the rate of 0.5 stardate units per day.
     * The corresponding Gregorian date is 5/10/2283.
     * Note that the month is zero-based (January = 0).
     */
    private static final
        GregorianCalendar[] m_gregorianDates =
        {
            new GregorianCalendar( 2162, 0, 4 ),
            new GregorianCalendar( 2162, 0, 4 ),
            new GregorianCalendar( 2270, 0, 26 ),
            new GregorianCalendar( 2283, 9, 5 ),
            new GregorianCalendar( 2323, 0, 1 )
        };


    /** Stardate constructor. */
    public Stardate() { /** Do nothing. */ }


    /**
     * Gets the API version.
     *
     * @return The API version.
     */
    public static String getVersion() { return API_VERSION; }


    /**
	 * Sets a Gregorian Calendar object for conversion to a stardate.
	 *
	 * @param gregorianCalendar The specified Gregorian Calendar object.
	 */
	public void setGregorian( GregorianCalendar gregorianCalendar )
	{
		m_gregorianCalendar = gregorianCalendar;
		m_recalculateStardate = true;
	}


    /**
     * Sets an issue/integer/fraction for conversion to a Gregorian date/time.
     *
     * @param issue The issue number for the stardate.  Can be positive or negative.
     * @param integer The (always positive) integer part of a stardate.
     * @param fraction The (always positive) fractional part of a stardate.
     */
    public void setStardate( int issue, int integer, int fraction ) throws StardateException
    {
        // Check if the integer and fractional parts are of valid length.
        if( issue >= 21 )
        {
            if( integer > 99999 )
            	throw new StardateException( StardateException.Type.INTEGER_PART_MUST_BE_BETWEEN_ZERO_AND_99999_INCLUSIVE );

            if( fraction < 0 )
               	throw new StardateException( StardateException.Type.FRACTIONAL_PART_MUST_BE_GREATER_THAN_OR_EQUAL_TO_ZERO );
        }
        else if( issue == 20 )
        {
        	if( integer >= 5006 )
        		throw new StardateException( StardateException.Type.INTEGER_PART_MUST_BE_LESS_THAN_5006 );

        	if( fraction < 0 )
        		throw new StardateException( StardateException.Type.FRACTIONAL_PART_MUST_BE_GREATER_THAN_OR_EQUAL_TO_ZERO );
        }
        else
        {
        	if( integer > 9999 )
        		throw new StardateException( StardateException.Type.INTEGER_PART_MUST_BE_BETWEEN_ZERO_AND_9999_INCLUSIVE );

        	if( fraction < 0 )
        		throw new StardateException( StardateException.Type.FRACTIONAL_PART_MUST_BE_GREATER_THAN_OR_EQUAL_TO_ZERO );
        }

    	m_issue = issue;
        m_integer = integer;
        m_fraction = fraction;
        m_recalculateGregorian = true;
    }


    /**
     * Returns the current stardate value converted to a Gregorian Calendar.
     *
     * @return The current stardate converted to a Gregorian Calendar.
     */
    public GregorianCalendar getGregorian()
    {
        if( m_recalculateGregorian )
        {
            stardateToGregorian();
            m_recalculateGregorian = false;
        }

        return m_gregorianCalendar;
    }


    /**
     * Returns the current stardate value converted to a Gregorian Calendar in string format.
     *
     * @return The current stardate converted to a Gregorian Calendar in string format.
     */
    public String toGregorianString()
    {
        if( m_recalculateGregorian )
        {
            stardateToGregorian();
            m_recalculateGregorian = false;
        }

        return m_gregorianCalendar.toString();
    }


    /**
     * Returns the current value of the stardate in string format.
     *
     * @return The current stardate as a properly formatted string.
     */
    public String toStardateString()
    {
        if( m_recalculateStardate )
        {
            gregorianToStardate();
            m_recalculateStardate = false;
        }

        return 
        	"["	+  //$NON-NLS-1$
        	( Integer.valueOf( m_issue ) ) + 
        	"] " +  //$NON-NLS-1$
        	( Integer.valueOf( m_integer ) ) +
        	"." + //$NON-NLS-1$
        	( Integer.valueOf( m_fraction ) );
    }


    /**
     * Returns the current value of the stardate in string format, padded if needed with leading zeros.
     *
     * @return The current stardate as a properly formatted string, padded if needed with leading zeros.
     */
    public String toStardateStringPadded()
    {
        if( m_recalculateStardate )
        {
            gregorianToStardate();
            m_recalculateStardate = false;
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append( "[" ).append( Integer.valueOf( m_issue ) ).append( "] " ); //$NON-NLS-1$ //$NON-NLS-2$
        if( m_issue >= 21 )
        {
        	// Need to pad up to 4 digits.
            if( m_integer < 10 )
            	stringBuilder.append( "0000" ).append( Integer.valueOf( m_integer ) ); //$NON-NLS-1$
            else if( m_integer < 100 )
            	stringBuilder.append( "000" ).append( Integer.valueOf( m_integer ) ); //$NON-NLS-1$
            else if( m_integer < 1000 )
            	stringBuilder.append( "00" ).append( Integer.valueOf( m_integer ) ); //$NON-NLS-1$
            else if( m_integer < 10000 )
            	stringBuilder.append( "0" ).append( Integer.valueOf( m_integer ) ); //$NON-NLS-1$
            else
            	stringBuilder.append( Integer.valueOf( m_integer ) );
        }
        else
        {
        	// Need to pad up to 3 digits.
            if( m_integer < 10 )
            	stringBuilder.append( "000" ).append( Integer.valueOf( m_integer ) ); //$NON-NLS-1$
            else if( m_integer < 100 )
            	stringBuilder.append( "00" ).append( Integer.valueOf( m_integer ) ); //$NON-NLS-1$
            else if( m_integer < 1000 )
            	stringBuilder.append( "0" ).append( Integer.valueOf( m_integer ) ); //$NON-NLS-1$
            else
            	stringBuilder.append( Integer.valueOf( m_integer ) );
        }

        stringBuilder.append( "." ).append( Integer.valueOf( m_fraction ) ); //$NON-NLS-1$

        return stringBuilder.toString();
    }


    /**
     * Returns the current stardate <code>issue</code> value.
     *
     * @return The current stardate <code>issue</code> value.
     */
    public int getStardateIssue()
    {
        if( m_recalculateStardate )
        {
            gregorianToStardate();
            m_recalculateStardate = false;
        }

        return m_issue;
    }


    /**
     * Returns the current stardate <code>integer</code> value.
     *
     * @return The current stardate <code>integer</code> value.
     */
    public int getStardateInteger()
    {
        if( m_recalculateStardate )
        {
            gregorianToStardate();
            m_recalculateStardate = false;
        }

        return m_integer;
    }


    /**
     * Returns the current stardate <code>fraction</code> value.
     *
     * @return The current stardate <code>fraction</code> value.
     */
    public int getStardateFraction()
    {
        if( m_recalculateStardate )
        {
            gregorianToStardate();
            m_recalculateStardate = false;
        }

        return m_fraction;
    }


    /**
     * Returns a string representation of this object.
     *
     * @return a string representation of this object.
     */
    @Override
	public String toString() { return toStardateString() + ", " + getGregorian().toString(); } //$NON-NLS-1$


    /**
     * Converts the current stardate to the equivalent Gregorian calendar.
     */
    private void stardateToGregorian()
    {
        // Reset the internal representation of Gregorian dates.
        m_gregorianCalendar = null;

        // The index into the vector specifying the stardate rate to use.
        int index = -1;

        // The stardate rate based on the current stardate era.
        double rate = 0.0;

        // The number of units the current stardate reduces to, relative to the start of the its era.
        double units = 0.0;

        // The number of days, hours, minutes and seconds the current stardate exceeds the corresponding start date.
        double days = 0.0, hours = 0.0, minutes = 0.0, seconds = 0.0;

        double fractionDivisor = 0.0;
        int fractionLength = 0;

        // Work out what stardate era we are in...
        if( m_issue < 0 )
        {
            // Pre-stardate (pre 4/1/2162).
            index = 0;
            fractionLength = Integer.valueOf( m_fraction ).toString().length();
            fractionDivisor = Math.pow( 10.0, fractionLength );
            units = m_issue * 10000.0 + m_integer + m_fraction / fractionDivisor;
        }
        else if( m_issue >= 0 && m_issue < 19 )
        {
            // First period of stardates (4/1/2162 - 26/1/2270).
            index = 1;
            fractionLength = Integer.valueOf( m_fraction ).toString().length();
            fractionDivisor = Math.pow( 10.0, fractionLength );
            units = m_issue * 1000.0  + m_integer  + m_fraction / fractionDivisor;
        }
        else if( m_issue == 19 && m_integer < 7340 )
        {
            // First period of stardates (4/1/2162 - 26/1/2270).
            index = 1;
            fractionLength = Integer.valueOf( m_fraction ).toString().length();
            fractionDivisor = Math.pow( 10.0, fractionLength );
            units = m_issue * 19.0 * 1000.0 +  m_integer  +  m_fraction / fractionDivisor;
        }
        else if( m_issue == 19 && m_integer >= 7340 && m_integer < 7840 )
        {
            // Second period of stardates (26/1/2270 - 5/10/2283)
            index = 2;
            fractionLength = Integer.valueOf( m_fraction ).toString().length();
            fractionDivisor = Math.pow( 10.0, fractionLength );
            units = m_integer + m_fraction / fractionDivisor - 7340;
        }
        else if( m_issue == 19 && m_integer >= 7840 )
        {
            // Third period of stardates (5/10/2283 - 1/1/2323)
            index = 3;
            fractionLength = Integer.valueOf( m_fraction ).toString().length();
            fractionDivisor = Math.pow( 10.0, fractionLength );
            units = m_integer + m_fraction / fractionDivisor - 7840;
        }
        else if( m_issue == 20 && m_integer < 5006 )
        {
            // Third period of stardates (5/10/2283 - 1/1/2323)
            index = 3;
            fractionLength = Integer.valueOf( m_fraction ).toString().length();
            fractionDivisor = Math.pow( 10.0, fractionLength );
            units = 1000.0 + m_integer + m_fraction / fractionDivisor;
        }
        else if( m_issue >= 21 )
        {
            // Fourth period of stardates (1/1/2323 - )
            index = 4;
            fractionLength = Integer.valueOf( m_fraction ).toString().length();
            fractionDivisor = Math.pow( 10.0, fractionLength );
            units = ( m_issue - 21 ) * 10000.0 + m_integer + m_fraction / fractionDivisor;
        }
        else
        {
        	throw new IllegalStateException( "Invalid stardate: " + toStardateString() ); //$NON-NLS-1$
        }

        // Convert the current amount of units to the equivalent Gregorian date.
        rate = m_stardateRates[ index ];
        units = Math.round( 10.0 * units ) / 10.0;
        days = units / rate;
        hours = ( days - (int)days ) * 24.0;
        minutes = ( hours - (int)hours ) * 60.0;
        seconds = ( minutes - (int)minutes ) * 60.0;

        // Get the start date for this era.
        m_gregorianCalendar = (GregorianCalendar)m_gregorianDates[ index ].clone();

        // Add the days, hours, minutes and seconds to the base date to get the current Gregorian date.
        m_gregorianCalendar.add( Calendar.DATE, (int)days );
        m_gregorianCalendar.add( Calendar.HOUR_OF_DAY, (int)hours );
        m_gregorianCalendar.add( Calendar.MINUTE, (int)minutes );
        m_gregorianCalendar.add( Calendar.SECOND, (int)seconds );

        m_recalculateGregorian = false;
    }


    /**
     * Converts the current Gregorian calendar to the equivalent stardate.
     */
    private void gregorianToStardate()
    {
        int stardateIssues[] = { -1, 0, 19, 19, 21 };
        int stardateIntegers[] = { 0, 0, 7340, 7840, 0 };
        int stardateRange[] = { 10000, 10000, 10000, 10000, 100000 };

        int index = -1;
        long milliseconds = 0;

        // Determine which era the given Gregorian date falls...
        int year = m_gregorianCalendar.get( Calendar.YEAR );
        int month = m_gregorianCalendar.get( Calendar.MONTH );
        int day = m_gregorianCalendar.get( Calendar.DATE );

        if( ( year < 2162 ) || ( year == 2162 && month == 1 && day < 4 ) )
        {
            // Pre-stardate (pre 4/1/2162).
            // Need to do the conversion here because a negative time is generated and throws out all other cases.
            milliseconds = m_gregorianCalendar.getTime().getTime();
            long baseMilliseconds = m_gregorianDates[ 0 ].getTime().getTime();
            double numberOfDays = ( baseMilliseconds - milliseconds ) / 1000.0 / 60.0 / 60.0 / 24.0;
            numberOfDays = Math.round( 10.0 * numberOfDays ) / 10.0;
            double rate = m_stardateRates[ 0 ];
            double units = numberOfDays * rate;
            double remainder = units % stardateRange[ 0 ];

            if( (int)remainder == 0 )
                m_issue = -1 * (int)units / stardateRange[ 0 ];
            else
                m_issue = -1 * (int)units / stardateRange[ 0 ] + stardateIssues[ 0 ];

            remainder = ( -1 * m_issue * stardateRange[ 0 ] ) - units;
            m_integer = (int)remainder;
            m_fraction = (int)( remainder * 10.0 ) - ( (int)remainder * 10 );
            m_recalculateStardate = false;
            return;
        }
        else if( year == 2162 && month == 1 && day >= 4 )
        {
            // First period of stardates (4/1/2162 - 26/1/2270).
            index = 1;
            milliseconds = m_gregorianCalendar.getTime().getTime();
        }
        else if( year == 2162 && month != 1 )
        {
            // First period of stardates (4/1/2162 - 26/1/2270).
            index = 1;
            milliseconds = m_gregorianCalendar.getTime().getTime();
        }
        else if( year > 2162 && year < 2270 )
        {
            // First period of stardates (4/1/2162 - 26/1/2270).
            index = 1;
            milliseconds = m_gregorianCalendar.getTime().getTime();
        }
        else if( year == 2270 && month == 1 && day < 26 )
        {
            // First period of stardates (4/1/2162 - 26/1/2270).
            index = 1;
            milliseconds = m_gregorianCalendar.getTime().getTime();
        }
        else if( year == 2270 && month == 1 && day >= 26 )
        {
            // Second period of stardates (26/1/2270 - 5/10/2283)
            index = 2;
            milliseconds = m_gregorianCalendar.getTime().getTime();
        }
        else if( year == 2270 & month != 1 )
        {
            // Second period of stardates (26/1/2270 - 5/10/2283)
            index = 2;
            milliseconds = m_gregorianCalendar.getTime().getTime();
        }
        else if( year > 2270 && year < 2283 )
        {
            // Second period of stardates (26/1/2270 - 5/10/2283)
            index = 2;
            milliseconds = m_gregorianCalendar.getTime().getTime();
        }
        else if( year == 2283 && month == 10 && day < 5 )
        {
            // Second period of stardates (26/1/2270 - 5/10/2283)
            index = 2;
            milliseconds = m_gregorianCalendar.getTime().getTime();
        }
        else if( year == 2283 && month != 10 )
        {
            // Second period of stardates (26/1/2270 - 5/10/2283)
            index = 2;
            milliseconds = m_gregorianCalendar.getTime().getTime();
        }
        else if( year == 2283 && month == 10 && day >= 5 )
        {
            // Third period of stardates (5/10/2283 - 1/1/2323)
            index = 3;
            milliseconds = m_gregorianCalendar.getTime().getTime();
        }
        else if( year == 2283 && month > 10 )
        {
            // Third period of stardates (5/10/2283 - 1/1/2323)
            index = 3;
            milliseconds = m_gregorianCalendar.getTime().getTime();
        }
        else if( year > 2283 && year < 2323 )
        {
            // Third period of stardates (5/10/2283 - 1/1/2323)
            index = 3;
            milliseconds = m_gregorianCalendar.getTime().getTime();
        }
        else if( year >= 2323 )
        {
            // Fourth period of stardates (1/1/2323 - )
            index = 4;
            milliseconds = m_gregorianCalendar.getTime().getTime();
        }
        else
        {
        	throw new IllegalStateException( "Invalid date: " + m_gregorianCalendar ); //$NON-NLS-1$
        }

        // Now convert...
        long baseMilliseconds = m_gregorianDates[ index ].getTime().getTime();
        double numberOfDays = ( milliseconds - baseMilliseconds ) / 1000.0 / 60.0 / 60.0 / 24.0;
        numberOfDays = Math.round( 10.0 * numberOfDays ) / 10.0;
        double rate = m_stardateRates[ index ];
        double units = numberOfDays * rate;
        m_issue = (int)units / stardateRange[ index ] + stardateIssues[ index ];
        double remainder = units % stardateRange[ index ];
        m_integer = (int)remainder + stardateIntegers[ index ];
        remainder = ( remainder - (int)remainder ) * 10.0;
        remainder = Math.round( remainder );
        m_fraction = (int)( remainder );
        m_recalculateStardate = false;
    }
}
