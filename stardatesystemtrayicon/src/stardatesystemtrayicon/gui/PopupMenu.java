package stardatesystemtrayicon.gui;


import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.chrono.ChronoZonedDateTime;
import java.time.chrono.Chronology;
import java.time.chrono.HijrahChronology;
import java.time.chrono.IsoChronology;
import java.time.chrono.JapaneseChronology;
import java.time.chrono.MinguoChronology;
import java.time.chrono.ThaiBuddhistChronology;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.chrono.CopticChronology;
import org.joda.time.chrono.EthiopicChronology;
import org.joda.time.chrono.JulianChronology;
import org.joda.time.format.DateTimeFormat;

import basesystemtrayicon.Messages;
import basesystemtrayicon.Utils;
import basesystemtrayicon.gui.PopupMenuBase;
import stardate.Stardate;
import stardatesystemtrayicon.Properties;


public class PopupMenu extends PopupMenuBase
{
    private static final long serialVersionUID = 1L;

    private static final String POPUP_COPY_TO_CLIPBOARD =
        Messages.getString( "PopupMenu.30" );

	private static final String CREDIT_ALGORITHM =
        Messages.getString( "PopupMenu.20" );

	private static final String CREDIT_CHRONOLOGY =
        Messages.getString( "PopupMenu.23" );


    public PopupMenu(
        String[] applicationAuthors,
        String applicationIconImage,
        String applicationName,
        String applicationURL,
        String applicationVersion )
	{
    	super(
            applicationAuthors,
            new String[] {
                CREDIT_ALGORITHM,
                CREDIT_CHRONOLOGY },
            applicationIconImage,
	        applicationName,
            applicationURL,
            applicationVersion,
            DialogSettings.class );

        Properties.upgrade( applicationVersion );

        insertSeparator( 0 );

        MenuItem menuItem = new MenuItem( POPUP_COPY_TO_CLIPBOARD );
        menuItem.addActionListener( this );
        insert( menuItem, 0 );

        // For debug.
        System.out.println( getStardate() );
	}


    @Override
    public void actionPerformed( ActionEvent actionEvent )
	{
    	String menuItem = ( (MenuItem)actionEvent.getSource() ).getLabel();
        if( POPUP_COPY_TO_CLIPBOARD.equals( menuItem ) )
            copyToClipboard(
                getStardate()
                +
                System.getProperty( "line.separator" )
                +
                getMessage() );

        else
            super.actionPerformed( actionEvent );            

        // For debug.
        System.out.println( getStardate() );
        System.out.println( getMessage() );
        System.out.println();
	}


    @Override
    public String getMessage()
    {
        ArrayList<String> dates = new ArrayList<String>();
        Instant utcNow = Instant.now();
        calculateChronologies( utcNow, dates );
        calculateChronologiesJoda( utcNow, dates );

        StringBuilder stringBuilder = new StringBuilder();
        Collections.sort( dates );
        for( String chronologyDateTime : dates )
        {
            stringBuilder.append( chronologyDateTime );
            stringBuilder.append( System.getProperty( "line.separator" ) );
        }

        Utils.removeTrailingLineSeparator( stringBuilder );
        return stringBuilder.toString();
    }


    protected static void
    calculateChronologies(
        Instant utcNow,
        ArrayList<String> dates )
    {
        List<Chronology> chronologies = new ArrayList<>();
        chronologies.add( HijrahChronology.INSTANCE );
        chronologies.add( IsoChronology.INSTANCE );
        chronologies.add( JapaneseChronology.INSTANCE );
        chronologies.add( MinguoChronology.INSTANCE );
        chronologies.add( ThaiBuddhistChronology.INSTANCE );

        String[] labels =
            new String[] {
                Messages.getString( "PopupMenu.4" ), // Hijrah
                Messages.getString( "PopupMenu.3" ), // ISO
                Messages.getString( "PopupMenu.7" ), // Japanese
                Messages.getString( "PopupMenu.8" ), // Minguo
                Messages.getString( "PopupMenu.0" ) }; // Thai Buddhist

        DateTimeFormatter timeFormatter =
            getDateTimeFormatter(
                Properties.KEY_DATE_FORMAT,
                Properties.VALUE_DATE_FORMAT_DEFAULT,
                Properties.VALUE_USER_DEFINED_DATE_DEFAULT );

        for( int i = 0; i < chronologies.size(); i++ )
        {
            ChronoZonedDateTime<?> zonedDateNow =
                chronologies.get( i ).zonedDateTime( utcNow, ZoneId.of( "UTC" ) );

            DateTimeFormatter timeFormatterWithChronology =
                timeFormatter.withChronology( chronologies.get( i ) );

            Object[] arguments = 
                new Object[] { 
                    zonedDateNow.format( timeFormatterWithChronology ) };

            dates.add( new MessageFormat( labels[ i ] ).format( arguments ) );
        }
    }


    protected static void
    calculateChronologiesJoda(
        Instant utcNow,
        ArrayList<String> dates )
    {
        // Use Joda-Time for chronologies unsupported by Java.
        org.joda.time.Chronology[] chronologies =
            new org.joda.time.Chronology[] {
                CopticChronology.getInstance(),
                EthiopicChronology.getInstance(),
                JulianChronology.getInstance() };

        String[] labels =
            new String[] {
                Messages.getString( "PopupMenu.1" ), // Coptic
                Messages.getString( "PopupMenu.2" ), // Ehtiopic
                Messages.getString( "PopupMenu.5" ) }; // Julian

        org.joda.time.format.DateTimeFormatter dateFormatter =
            getDateFormatterJoda();

        for( int i = 0; i < chronologies.length; i++ )
        {
            DateTime dateTime =
                new DateTime( utcNow.toEpochMilli() ).withChronology(
                    chronologies[ i ] );

            Object[] arguments =
                new Object[] { dateTime.toString( dateFormatter ) };

            dates.add( new MessageFormat( labels[ i ] ).format( arguments ) );
        }
    }


    public static String getStardate()
    {
        String stardateString;

        Instant utcNow = Instant.now();

        boolean classic =
            Properties.getPropertyBoolean(
                Properties.KEY_CLASSIC,
                Properties.VALUE_CLASSIC_DEFAULT );

        if( classic )
        {
            int[] issueIntegerFraction =
                Stardate.getStardateClassic( utcNow );

            boolean showIssue =
                Properties.getPropertyBoolean(
                    Properties.KEY_ISSUE,
                    Properties.VALUE_ISSUE_DEFAULT );

            boolean pad =
                Properties.getPropertyBoolean(
                    Properties.KEY_PAD,
                    Properties.VALUE_PAD_DEFAULT );

            stardateString =
                Stardate.toStardateClassicString(
                    issueIntegerFraction[ 0 ],
                    issueIntegerFraction[ 1 ],
                    issueIntegerFraction[ 2 ],
                    showIssue,
                    pad );
        }
        else
        {
            int[] integerFraction = Stardate.getStardate2009Revised( utcNow );

            stardateString =
                Stardate.toStardateRevised2009String(
                    integerFraction[ 0 ],
                    integerFraction[ 1 ] );
        }

        Object[] arguments = new Object[] { stardateString };
        final String MESSAGE_STARDATE = Messages.getString( "PopupMenu.6" );
        return new MessageFormat( MESSAGE_STARDATE ).format( arguments );
    }


    private static org.joda.time.format.DateTimeFormatter getDateFormatterJoda()
    {
        org.joda.time.format.DateTimeFormatter dateFormatter;

        String dateFormat =
            Properties.getProperty(
                Properties.KEY_DATE_FORMAT,
                Properties.VALUE_DATE_LONG );

        switch( dateFormat )
        {
            case Properties.VALUE_DATE_FULL:
                dateFormatter = org.joda.time.format.DateTimeFormat.fullDate();
                break;

            case Properties.VALUE_DATE_LONG:
                dateFormatter = org.joda.time.format.DateTimeFormat.longDate();
                break;

            case Properties.VALUE_DATE_MEDIUM:
                dateFormatter = org.joda.time.format.DateTimeFormat.mediumDate();
                break;

            case Properties.VALUE_DATE_SHORT:
                dateFormatter = org.joda.time.format.DateTimeFormat.shortDate();
                break;

            case Properties.VALUE_USER_DEFINED:
                try
                {
                    dateFormatter =
                        DateTimeFormat.forPattern(
                            Properties.getProperty(
                                Properties.KEY_USER_DEFINED,
                                Properties.VALUE_USER_DEFINED_DATE_DEFAULT ) );
                }
                catch( IllegalArgumentException exception ) 
                {
                    dateFormatter = DateTimeFormat.longDate();
                    System.err.println( "Bad date format string: " + dateFormat );
                }

                break;

            default:
                // This should never occur, unless the properties file is
                // manipulated for testing/debugging; handle gracefully rather than
                // throw an exception.
                dateFormatter = DateTimeFormat.longDate();
                System.err.println( "Unknown date format: " + dateFormat );
                
        }

        return dateFormatter;
    }
}
