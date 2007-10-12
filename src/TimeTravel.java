import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.Collator;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.TimeZone;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EtchedBorder;


public class TimeTravel extends JDialog implements ActionListener, ItemListener, WindowListener
{
    private JComboBox m_timeZoneComboBox = null;
    private JSpinner m_hourSpinner = null;
    private JSpinner m_minuteSpinner = null;
    private JLabel m_output = null;
    private JButton m_calculate = null;
    private Vector<String> m_timeZones = null;
    private Vector<String> m_timeZonesDisplayable = null;
    private Vector<String> m_timeZonesCache = null;
    private Vector<String> m_timeZonesDisplayableCache = null;
    private JCheckBox m_includeLocalCheckbox = null;
    private JCheckBox m_showAllTimeZonesCheckbox = null;


    public static TimeTravel create()
    {
    	TimeTravel timeTravel = new TimeTravel();

    	timeTravel.initialiseTimeZoneMapping();

    	JLabel selectTimeZone = new JLabel( Messages.getString( "TimeTravel.0" ) ); 
    	JLabel setTime = new JLabel( Messages.getString( "TimeTravel.1" ) ); 
    	JLabel hours = new JLabel( Messages.getString( "TimeTravel.2" ) ); 
    	JLabel minutes = new JLabel( Messages.getString( "TimeTravel.3" ) ); 

        timeTravel.m_output = new JLabel();
        timeTravel.m_output.setHorizontalAlignment( SwingConstants.CENTER );
        timeTravel.m_output.setBorder
        ( 
        	BorderFactory.createCompoundBorder
        	( 
            		BorderFactory.createEmptyBorder( 10, 10, 10, 10 ),
            		BorderFactory.createEtchedBorder( EtchedBorder.LOWERED )
        	)
        );

        // The data we pass to the combo box has to be a copy.
        // When we (later) remove the items from the combo, the original data gets clobbered.
        timeTravel.m_timeZoneComboBox = new JComboBox( (Vector<?>)timeTravel.m_timeZonesDisplayable.clone() ); 
    	timeTravel.m_timeZoneComboBox.addActionListener( timeTravel );

    	timeTravel.m_showAllTimeZonesCheckbox = new JCheckBox( Messages.getString( "TimeTravel.4" ) ); 
    	timeTravel.m_showAllTimeZonesCheckbox.addItemListener( timeTravel );

        timeTravel.m_hourSpinner = new JSpinner( new SpinnerNumberModel( new GregorianCalendar().get( GregorianCalendar.HOUR_OF_DAY ), 0, 23, 1 ) );

        timeTravel.m_minuteSpinner = new JSpinner( new SpinnerNumberModel( new GregorianCalendar().get( GregorianCalendar.MINUTE ), 0, 59, 1 ) );

    	timeTravel.m_includeLocalCheckbox = new JCheckBox( Messages.getString( "TimeTravel.5" ) );
    	timeTravel.m_includeLocalCheckbox.addItemListener( timeTravel );

        timeTravel.m_calculate = new JButton( Messages.getString( "TimeTravel.6" ) ); 
        timeTravel.m_calculate.addActionListener( timeTravel );

        GroupLayout layout = new GroupLayout( timeTravel.getContentPane() );
        timeTravel.getContentPane().setLayout( layout );
        layout.setAutoCreateGaps( true );
        layout.setAutoCreateContainerGaps( true );

        layout.setHorizontalGroup
        (
    		layout.createParallelGroup( Alignment.CENTER )
    			.addGroup
    			(
		    		layout.createSequentialGroup()
		    			.addGroup
		    			(
		    				layout.createParallelGroup()
		    					.addComponent( selectTimeZone )
		    					.addComponent( setTime )
		    			)
		    			.addGroup
		    			(
		    				layout.createParallelGroup()
		    					.addComponent( timeTravel.m_timeZoneComboBox )
		    					.addComponent( timeTravel.m_showAllTimeZonesCheckbox )
				    			.addGroup
				    			(
				    				layout.createSequentialGroup()
						    			.addGroup
						    			(
						    				layout.createParallelGroup()
						    					.addComponent( timeTravel.m_hourSpinner )
						    					.addComponent( hours )
						    			)
						    			.addGroup
						    			(
						    				layout.createParallelGroup()
												.addComponent( timeTravel.m_minuteSpinner )
												.addComponent( minutes )
						    			)
				    			)
		    			)
	    		)
	    		.addComponent( timeTravel.m_includeLocalCheckbox, Alignment.LEADING )
				.addComponent( timeTravel.m_output )
				.addComponent( timeTravel.m_calculate )
		);

        layout.setVerticalGroup
        (
    		layout.createSequentialGroup()
    			.addGroup
    			(
    				layout.createParallelGroup()
    					.addComponent( selectTimeZone )
    					.addComponent( timeTravel.m_timeZoneComboBox )
    			)
    			.addComponent( timeTravel.m_showAllTimeZonesCheckbox )
				.addPreferredGap( ComponentPlacement.UNRELATED )
    			.addGroup
    			(
    				layout.createParallelGroup()
    					.addComponent( setTime )
    					.addComponent( timeTravel.m_hourSpinner )
    					.addComponent( timeTravel.m_minuteSpinner )
    			)
    			.addGroup
    			(
    				layout.createParallelGroup()
    					.addComponent( hours )
    					.addComponent( minutes )
    			)
				.addPreferredGap( ComponentPlacement.UNRELATED )
				.addComponent( timeTravel.m_includeLocalCheckbox )
				.addComponent( timeTravel.m_output )
				.addComponent( timeTravel.m_calculate )
		);

		GregorianCalendar gregorianCalendar = new GregorianCalendar( TimeZone.getTimeZone( timeTravel.m_timeZones.firstElement() ) );
		gregorianCalendar.set( GregorianCalendar.HOUR_OF_DAY, ( (SpinnerNumberModel)timeTravel.m_hourSpinner.getModel() ).getNumber().intValue() );
		gregorianCalendar.set( GregorianCalendar.MINUTE, ( (SpinnerNumberModel)timeTravel.m_minuteSpinner.getModel() ).getNumber().intValue() );

		timeTravel.m_output.setText( Message.getMessageString( gregorianCalendar, true ) );

		timeTravel.setTitle( Messages.getString( "TimeTravel.7" ) ); 
        timeTravel.setIconImage( TrayIcon.getTrayIconImage() );
        timeTravel.setModalityType( ModalityType.APPLICATION_MODAL );
        timeTravel.pack();
		int originX = ( Toolkit.getDefaultToolkit().getScreenSize().width - timeTravel.getWidth() ) / 2;
        int originY = ( Toolkit.getDefaultToolkit().getScreenSize().height - timeTravel.getHeight() ) / 2;
        timeTravel.setLocation( originX, originY );
        timeTravel.setResizable( false );
        timeTravel.addWindowListener( timeTravel );
        timeTravel.setVisible( true );

        return timeTravel;
    }


	public void itemStateChanged( ItemEvent itemEvent )
	{
    	if( itemEvent.getSource() == m_showAllTimeZonesCheckbox )
    	{
        	if( itemEvent.getStateChange() == ItemEvent.SELECTED )
        	{
        		m_timeZoneComboBox.removeAllItems();
        		Iterator<String> iterator = UserTimeZones.getAvailableTimeZones().iterator();
        		while( iterator.hasNext() )
        			m_timeZoneComboBox.addItem( iterator.next() );

        		pack();
        	}
        	else if( itemEvent.getStateChange() == ItemEvent.DESELECTED )
        	{
        		m_timeZoneComboBox.removeAllItems();

                // The data we add to the combo box has to be a copy.  
                // When we remove the items from the combo, the original data gets clobbered.
        		for( int i = 0; i < m_timeZonesDisplayable.size(); i++ )
        			m_timeZoneComboBox.addItem( new String( m_timeZonesDisplayable.get( i ) ) ); 

        		pack();
        	}
    	}
	}


    public void actionPerformed( ActionEvent actionEvent )
	{
    	if( actionEvent.getSource() == m_calculate )
    	{
    		resetProperties();

			String timeZone;
			if( m_showAllTimeZonesCheckbox.isSelected() )
			{
				timeZone = m_timeZoneComboBox.getSelectedItem().toString();
				appendTimeZoneToProperties( timeZone );
			}
			else
			{
				timeZone = m_timeZones.get( m_timeZoneComboBox.getSelectedIndex() );
			}
			
			if( m_includeLocalCheckbox.isSelected() )
				appendTimeZoneToProperties( TimeZone.getDefault().getID() );

			GregorianCalendar gregorianCalendar = new GregorianCalendar( TimeZone.getTimeZone( timeZone ) );
			gregorianCalendar.set( GregorianCalendar.HOUR_OF_DAY, ( (SpinnerNumberModel)m_hourSpinner.getModel() ).getNumber().intValue() );
			gregorianCalendar.set( GregorianCalendar.MINUTE, ( (SpinnerNumberModel)m_minuteSpinner.getModel() ).getNumber().intValue() );

	        m_output.setText( Message.getMessageString( gregorianCalendar, true ) );

	        pack();
	        
    		return;
    	}
	}


    private void initialiseTimeZoneMapping()
    {
    	Vector<String> timeZones = Properties.getInstance().getPropertyList( Properties.PROPERTY_TIME_ZONES_SELECTED );
    	Vector<String> timeZonesDisplayable = Properties.getInstance().getPropertyList( Properties.PROPERTY_TIME_ZONES_SELECTED_DISPLAY_NAMES );

        m_timeZones = new Vector<String>( timeZones.size() );
        m_timeZonesDisplayable = new Vector<String>( timeZones.size() );

        for( int i = 0; i < timeZones.size(); i++ )
		{
			String timeZone = timeZones.get( i );
			String timeZoneDisplayable = timeZonesDisplayable.get( i );
			int j = 0;
			int comparison = 0;
			for( ; j < m_timeZonesDisplayable.size(); j++ )
			{
				comparison = Collator.getInstance().compare( timeZoneDisplayable, m_timeZonesDisplayable.get( j ) );
				if( comparison == 0 )
				{
					// Secondary sort by time zone.
					comparison = Collator.getInstance().compare( timeZone, m_timeZones.get( j ) );
					if( comparison == 0 )
						throw new IllegalStateException( "Cannot have the same Time Zone repeat: " + timeZone ); 

					if( comparison > 0 )
						continue;

					break;
				}

				if( comparison > 0 )
					continue;

				break;
			}

			m_timeZones.insertElementAt( timeZone, j );
			m_timeZonesDisplayable.insertElementAt( timeZoneDisplayable, j );
		}   

        // Need to cache the original values as we may twiddle the properties to allow the user to include the local time
        // and/or an additional suprise time zone!
    	m_timeZonesCache = Properties.getInstance().getPropertyList( Properties.PROPERTY_TIME_ZONES_SELECTED );
    	m_timeZonesDisplayableCache = Properties.getInstance().getPropertyList( Properties.PROPERTY_TIME_ZONES_SELECTED_DISPLAY_NAMES );
    }


    private void appendTimeZoneToProperties( String timeZone )
    {
    	Vector<String> timeZones = Properties.getInstance().getPropertyList( Properties.PROPERTY_TIME_ZONES_SELECTED );
    	Vector<String> timeZonesDisplayable = Properties.getInstance().getPropertyList( Properties.PROPERTY_TIME_ZONES_SELECTED_DISPLAY_NAMES );

        int i = 0;
        for( ; i < timeZones.size(); i++ )
		{
			int comparison = Collator.getInstance().compare( timeZone, timeZones.get( i ) );
			if( comparison == 0 )
				return; // Don't want duplicates.

			if( comparison > 0 )
				continue;

			break;
		}

		timeZones.insertElementAt( timeZone, i );
		timeZonesDisplayable.insertElementAt( timeZone, i );

		Properties.getInstance().setPropertyList( Properties.PROPERTY_TIME_ZONES_SELECTED, timeZones );
		Properties.getInstance().setPropertyList( Properties.PROPERTY_TIME_ZONES_SELECTED_DISPLAY_NAMES, timeZonesDisplayable );
		Properties.getInstance().store();
    }

    
    private void resetProperties()
    {
		Properties.getInstance().setPropertyList( Properties.PROPERTY_TIME_ZONES_SELECTED, m_timeZonesCache );
		Properties.getInstance().setPropertyList( Properties.PROPERTY_TIME_ZONES_SELECTED_DISPLAY_NAMES, m_timeZonesDisplayableCache );
		Properties.getInstance().store();
    }
    

    public void windowClosing( WindowEvent windowEvent ) { resetProperties(); }

    
    public void windowClosed( WindowEvent windowEvent ) { }

    
    public void windowOpened( WindowEvent windowEvent ) { }

    
    public void windowIconified( WindowEvent windowEvent ) { }

    
    public void windowDeiconified( WindowEvent windowEvent ) { }
    
    
    public void windowActivated( WindowEvent windowEvent ) { }

    
    public void windowDeactivated( WindowEvent windowEvent ) { }

    
    public void windowGainedFocus( WindowEvent windowEvent ) { }

    
    public void windowLostFocus( WindowEvent windowEvent ) { }

    
    public void windowStateChanged( WindowEvent windowEvent ) { }
}
