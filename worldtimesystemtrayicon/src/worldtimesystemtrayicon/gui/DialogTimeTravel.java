package worldtimesystemtrayicon.gui;


import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.TimeZone;
import java.util.Vector;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import basesystemtrayicon.Messages;
import basesystemtrayicon.gui.Utils;
import worldtimesystemtrayicon.Properties;
import worldtimesystemtrayicon.TimeZoneItem;
import worldtimesystemtrayicon.TimeZoneUtils;
import worldtimesystemtrayicon.TimeZoneUtils.SortTimeZoneItems;


public class DialogTimeTravel
extends JDialog
implements ActionListener, ChangeListener
{
	private static final long serialVersionUID = 1L;

    private JSpinner
        m_hourSpinner,
        m_minuteSpinner;

    private JLabel m_output;

    private JComboBox<String> m_timeZoneComboBox;

    private Vector<TimeZoneItem> m_userTimeZones; 


    public DialogTimeTravel( Image applicationIconImage )
    {
        super( (JDialog)null );

        Utils.initialiseDialog(
            this,
            Messages.getString( "DialogTimeTravel.4" ),
            applicationIconImage );

        JLabel selectTimeZone =
            new JLabel( Messages.getString( "DialogTimeTravel.0" ) );

        m_userTimeZones = TimeZoneUtils.getUserTimeZones();

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

            m_userTimeZones =
                TimeZoneUtils.combineTimeZones(
                    m_userTimeZones,
                    separator );
        }

        boolean sortByDateTime =
            Properties.getPropertyBoolean(
                Properties.KEY_SORT_DATE_TIME,
                Properties.VALUE_SORT_DATE_TIME_DEFAULT );

        Collections.sort(
            m_userTimeZones,
            new SortTimeZoneItems( sortByDateTime ) );

    	Vector<String> userTimeZonesDisplayable = new Vector<>();
    	for( TimeZoneItem timeZoneItem : m_userTimeZones )
    	    userTimeZonesDisplayable.add(
	            timeZoneItem.getTimeZoneDisplayable() );

    	m_timeZoneComboBox = new JComboBox<>( userTimeZonesDisplayable );
    	m_timeZoneComboBox.addActionListener( this );

    	JLabel setTime = new JLabel( Messages.getString( "DialogTimeTravel.1" ) );

    	ZonedDateTime now = ZonedDateTime.now();

        m_hourSpinner =
            new JSpinner(
                new SpinnerNumberModel( now.getHour(), 0, 23, 1 ) );

        m_hourSpinner.setEditor(
            new JSpinner.NumberEditor( m_hourSpinner, "00" ) );

        m_hourSpinner.addChangeListener( this );

        m_minuteSpinner =
            new JSpinner(
                new SpinnerNumberModel( now.getMinute(), 0, 59, 1 ) );

        m_minuteSpinner.setEditor(
            new JSpinner.NumberEditor( m_minuteSpinner, "00" ) );

        m_minuteSpinner.addChangeListener( this );

    	JLabel hours = new JLabel( Messages.getString( "DialogTimeTravel.2" ) );
    	JLabel minutes = new JLabel( Messages.getString( "DialogTimeTravel.3" ) );

        m_output = new JLabel();
        m_output.setHorizontalAlignment( SwingConstants.CENTER );
        m_output.setBorder( Utils.createBorder() );

        GroupLayout layout = new GroupLayout( getContentPane() );
        getContentPane().setLayout( layout );
        layout.setAutoCreateGaps( true );
        layout.setAutoCreateContainerGaps( true );

        layout.setHorizontalGroup(
    		layout.createParallelGroup( Alignment.CENTER )
    			.addGroup(
		    		layout.createSequentialGroup()
		    			.addGroup(
		    				layout.createParallelGroup()
		    					.addComponent( selectTimeZone )
		    					.addComponent( setTime ) )
		    			.addGroup(
		    				layout.createParallelGroup()
		    					.addComponent( m_timeZoneComboBox )
				    			.addGroup(
				    				layout.createSequentialGroup()
						    			.addGroup(
						    				layout.createParallelGroup()
						    					.addComponent( m_hourSpinner )
						    					.addComponent( hours, Alignment.CENTER ) )
						    			.addGroup(
						    				layout.createParallelGroup()
												.addComponent( m_minuteSpinner )
												.addComponent( minutes, Alignment.CENTER ) ) ) ) )
                .addComponent(
                        m_output,
                        Alignment.CENTER,
                        GroupLayout.PREFERRED_SIZE,
                        GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE ) );

        layout.setVerticalGroup(
    		layout.createSequentialGroup()
    			.addGroup(
    				layout.createParallelGroup( Alignment.BASELINE )
    					.addComponent( selectTimeZone )
    					.addComponent( m_timeZoneComboBox ) )
				.addPreferredGap( ComponentPlacement.UNRELATED )
    			.addGroup(
    				layout.createParallelGroup( Alignment.BASELINE )
    					.addComponent( setTime )
    					.addComponent( m_hourSpinner )
    					.addComponent( m_minuteSpinner ) )
    			.addGroup(
    				layout.createParallelGroup()
    					.addComponent( hours )
    					.addComponent( minutes ) )
				.addPreferredGap( ComponentPlacement.UNRELATED )
				.addComponent( m_output ) );

        selectTimeZone();
        updateLabel();

        Utils.postDialog( this );
    }


    @Override
    public void actionPerformed( ActionEvent actionEvent )
	{
        updateLabel();
	}


    @Override
    public void stateChanged( ChangeEvent changeEvent )
    {
        updateLabel();
    }


    private void updateLabel()
    {
        String selectedTimeZoneDisplayable =
            m_timeZoneComboBox.getSelectedItem().toString();

        for( TimeZoneItem timeZoneItem : m_userTimeZones )
        {
            if( timeZoneItem.getTimeZoneDisplayable().equals(
                selectedTimeZoneDisplayable ) )
            {
                LocalTime localTime =
                    LocalTime.of(
                        ( (SpinnerNumberModel)m_hourSpinner.getModel() ).getNumber().intValue(),
                        ( (SpinnerNumberModel)m_minuteSpinner.getModel() ).getNumber().intValue() );

                LocalDateTime localDateTime =
                    LocalDateTime.of( LocalDate.now(), localTime );

                ZonedDateTime zonedDateTime =
                    ZonedDateTime.of(
                        localDateTime,
                        ZoneId.of( timeZoneItem.getTimeZone() ) );

                m_output.setText(
                    PopupMenu.toHTML(
                        PopupMenu.getMessage( zonedDateTime ) ) );

                pack();
                break;
            }
        }
    }


    private void selectTimeZone()
    {
        String currentTimeZone = TimeZone.getDefault().getID();
        for( TimeZoneItem timeZoneItem : m_userTimeZones )
        {
            if( timeZoneItem.getTimeZone().equals( currentTimeZone ) )
            {
                m_timeZoneComboBox.setSelectedItem(
                    timeZoneItem.getTimeZoneDisplayable() );

                break;
            }
        }
    }
}
