package worldtimesystemtrayicon.gui;


import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.text.Collator;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Vector;

import basesystemtrayicon.Messages;
import basesystemtrayicon.Utils;
import basesystemtrayicon.gui.DialogMessage;
import basesystemtrayicon.gui.PopupMenuBase;
import worldtimesystemtrayicon.Properties;
import worldtimesystemtrayicon.TimeZoneItem;
import worldtimesystemtrayicon.TimeZoneUtils;
import worldtimesystemtrayicon.TimeZoneUtils.SortTimeZoneItems;


public class PopupMenu extends PopupMenuBase
{
	private static final long serialVersionUID = 1L;

    private static final String POPUP_ADD_EDIT_REMOVE_TIME_ZONES =
        Messages.getString( "PopupMenu.7" );

	private static final String POPUP_SET_LAYOUT =
        Messages.getString( "PopupMenu.53" );

    private static final String POPUP_COPY_TO_CLIPBOARD =
        Messages.getString( "PopupMenu.59" );

	private static final String POPUP_TIME_TRAVEL =
        Messages.getString( "PopupMenu.9" );


    public PopupMenu(
        String[] applicationAuthors,
        String applicationIconImage,
        String applicationName,
        String applicationURL,
        String applicationVersion )
    {
        super(
            applicationAuthors,
            new String[] { },
            applicationIconImage,
            applicationName,
            applicationURL,
            applicationVersion,
            DialogSettings.class );

        Properties.upgrade( applicationVersion );

        insertSeparator( 0 );

        MenuItem menuItem = null;

        menuItem = new MenuItem( POPUP_ADD_EDIT_REMOVE_TIME_ZONES );
        menuItem.addActionListener( this );
        insert( menuItem, 0 );

        menuItem = new MenuItem( POPUP_SET_LAYOUT );
        menuItem.addActionListener( this );
        insert( menuItem, 0 );

        insertSeparator( 0 );

        menuItem = new MenuItem( POPUP_TIME_TRAVEL );
        menuItem.addActionListener( this );
        insert( menuItem, 0 );

        menuItem = new MenuItem( POPUP_COPY_TO_CLIPBOARD );
        menuItem.addActionListener( this );
        insert( menuItem, 0 );
    }


    @Override
	public void actionPerformed( ActionEvent actionEvent )
	{
        String menuItem = ( (MenuItem)actionEvent.getSource() ).getLabel();
        if( POPUP_ADD_EDIT_REMOVE_TIME_ZONES.equals( menuItem ) )
    	{
            setEnabled( false );
    		new DialogAddEditRemoveTimeZones( getApplicationIconImage() );
            setEnabled( true );
    	}
        else if( POPUP_SET_LAYOUT.equals( menuItem ) )
        {
            if( userHasAddedTimeZones() )
            {
                setEnabled( false );
                new DialogMessageLayout( getApplicationIconImage(), this );
                setEnabled( true );
            }
        }
        else if( POPUP_TIME_TRAVEL.equals( menuItem ) )
        {
            if( userHasAddedTimeZones() )
            {
                setEnabled( false );
                new DialogTimeTravel( getApplicationIconImage());
                setEnabled( true );
            }
        }
        else if( POPUP_COPY_TO_CLIPBOARD.equals( menuItem ) )
        {
            if( userHasAddedTimeZones() )
                copyToClipboard( getMessage() );
        }
        else
            super.actionPerformed( actionEvent );            

        // For debug.
        System.out.println( getMessage() );
        System.out.println();
	}


    @Override
    public String getMessage()
    {
        String message = getMessage( ZonedDateTime.now() );
        if( message.length() == 0 )
            message = Messages.getString( "PopupMenu.40" );

        return message;
    }


    public boolean userHasAddedTimeZones()
    {
        boolean noTimeZonesSelected =
            Properties.getPropertyList(
                Properties.KEY_TIME_ZONES ).isEmpty();

        if( noTimeZonesSelected )
            DialogMessage.showInformation(
                Messages.getString( "PopupMenu.41" ),
                Messages.getString( "PopupMenu.40" ),
                getApplicationIconImage() );

        return ! noTimeZonesSelected;
    }
    

    /**
     * Creates a text message for the specified date/time, either 'now'
     * or that specified in 'time travel', for all the user time zones.
     *
     * @param zonedDateTime
     *
     * @return Text message showing the date/time for all user time zones.
     */
    protected static String getMessage( ZonedDateTime zonedDateTime )
    {
        Vector<TimeZoneItem> userTimeZones = TimeZoneUtils.getUserTimeZones();

        boolean combineTimeZones =
            Properties.getPropertyBoolean(
                Properties.KEY_COMBINE,
                Properties.VALUE_COMBINE_DEFAULT );

        if( combineTimeZones )
        {
            String separator =
                Properties.getProperty(
                    Properties.KEY_SEPARATOR,
                    Properties.VALUE_SEPARATOR_DEFAULT );

            userTimeZones =
                TimeZoneUtils.combineTimeZones(
                    userTimeZones,
                    separator );
        }

        boolean sortByDateTime =
            Properties.getPropertyBoolean(
                Properties.KEY_SORT_DATE_TIME,
                Properties.VALUE_SORT_DATE_TIME_DEFAULT );

        Collections.sort(
            userTimeZones,
            new SortTimeZoneItems( sortByDateTime ) );

        String previousDayIndicator =
            Properties.getProperty(
                Properties.KEY_PREVIOUS_DAY,
                Properties.VALUE_PREVIOUS_DAY_DEFAULT );

        String nextDayIndicator =
            Properties.getProperty(
                Properties.KEY_NEXT_DAY,
                Properties.VALUE_NEXT_DAY_DEFAULT );

        DateTimeFormatter dateTimeFormatter =
            getDateTimeFormatter(
                Properties.KEY_DATE_TIME_FORMAT,
                Properties.VALUE_DATE_TIME_FORMAT_DEFAULT,
                Properties.VALUE_USER_DEFINED_TIME_DEFAULT );

        StringBuilder message = new StringBuilder();
        for( TimeZoneItem timeZoneItem : userTimeZones )
        {
            ZonedDateTime zonedDateTimeForTimeZone =
                zonedDateTime.withZoneSameInstant(
                    ZoneId.of( timeZoneItem.getTimeZone() ) ); 

            int comparison =
                zonedDateTime.getYear()
                -
                zonedDateTimeForTimeZone.getYear();

            if( comparison == 0 )
            {
                comparison =
                    zonedDateTime.getMonthValue()
                    -
                    zonedDateTimeForTimeZone.getMonthValue();
                
                if( comparison == 0 )
                    comparison =
                        zonedDateTime.getDayOfMonth()
                        -
                        zonedDateTimeForTimeZone.getDayOfMonth();
            }

            String differentDayIndicator = "";
            if( comparison > 0 )
                differentDayIndicator = previousDayIndicator;

            else if( comparison < 0 )
                differentDayIndicator = nextDayIndicator;

            String line =
                buildLine(
                    timeZoneItem.getTimeZoneDisplayable(),
                    differentDayIndicator,
                    zonedDateTimeForTimeZone.format( dateTimeFormatter ) );

            message.append( line );
            message.append( System.getProperty( "line.separator" ) );        
        }

        // Remove last line separator; guard against no time zones.
        if( message.length() > 0 )
            Utils.removeTrailingLineSeparator( message );

        return message.toString();
    }


    protected static String toHTML( String plainText )
    {
        return
            "<html><body>"
            +
            plainText
                .replaceAll( System.getProperty( "line.separator" ), "<br>" )
                .replaceAll( " ", "&nbsp;" )
            +
            "</body></html>";
    }


    private static String
    buildLine( String timeZone, String differentDayIndicator, String dateTime )
    {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(
            Properties.getProperty(
                Properties.KEY_LAYOUT_TEXT_LEFT,
                Properties.VALUE_LAYOUT_TEXT_LEFT_DEFAULT ) );

        stringBuilder.append(
            getLayoutValue(
                Properties.KEY_LAYOUT_COMBO_LEFT,
                Properties.VALUE_LAYOUT_COMBO_TIME_ZONE,
                timeZone,
                differentDayIndicator,
                dateTime ) );

        stringBuilder.append(
            Properties.getProperty(
                Properties.KEY_LAYOUT_TEXT_LEFT_CENTRE,
                Properties.VALUE_LAYOUT_TEXT_LEFT_CENTRE_DEFAULT ) );

        stringBuilder.append(
            getLayoutValue(
                Properties.KEY_LAYOUT_COMBO_CENTRE,
                Properties.VALUE_LAYOUT_COMBO_DIFFERENT_DAY_INDICATOR,
                timeZone,
                differentDayIndicator,
                dateTime ) );

        stringBuilder.append(
            Properties.getProperty(
                Properties.KEY_LAYOUT_TEXT_RIGHT_CENTRE,
                Properties.VALUE_LAYOUT_TEXT_RIGHT_CENTRE_DEFAULT ) );

        stringBuilder.append(
            getLayoutValue(
                Properties.KEY_LAYOUT_COMBO_RIGHT,
                Properties.VALUE_LAYOUT_COMBO_DATE_TIME,
                timeZone,
                differentDayIndicator,
                dateTime ) );

        stringBuilder.append(
            Properties.getProperty(
                Properties.KEY_LAYOUT_TEXT_RIGHT,
                Properties.VALUE_LAYOUT_TEXT_RIGHT_DEFAULT ) );

        return stringBuilder.toString();
    }


    private static String
    getLayoutValue(
        String property,
        String defaultValue,
        String timeZone,
        String differentDayIndicator,
        String dateTime )
    {
        String value = Properties.getProperty( property, defaultValue );

        boolean isDifferentDayIndicatorLayout =
            Collator.getInstance().equals(
                Properties.VALUE_LAYOUT_COMBO_DIFFERENT_DAY_INDICATOR,
                value );

        boolean isNoLayout =
            Collator.getInstance().equals(
                Properties.VALUE_LAYOUT_COMBO_NONE,
                value );

        boolean isDateTimeLayout =
            Collator.getInstance().equals(
                Properties.VALUE_LAYOUT_COMBO_DATE_TIME,
                value );

        String layoutValue;
        if( isDifferentDayIndicatorLayout )
            layoutValue = differentDayIndicator;

        else if( isDateTimeLayout )
            layoutValue = dateTime;

        else if( isNoLayout )
            layoutValue = "";

        else
            layoutValue = timeZone;

        return layoutValue;
    }
}
