import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.LayoutStyle;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class StardateConverter extends JFrame implements ActionListener, ChangeListener, FocusListener
{
	private static final long serialVersionUID = 1L;

	private JRadioButton m_radioClassic, m_radio2009Revised;
	private JSpinner m_spinnerIssue, m_spinnerInteger, m_spinnerFraction, m_spinnerYYYYMMDD, m_spinnerHHMMSS;
    private boolean m_stardateToGregorian = true;


    public StardateConverter()
    {
        super( "Stardate/Gregorian Converter" ); //$NON-NLS-1$

        setDefaultCloseOperation( EXIT_ON_CLOSE );
        setContentPane( buildMainPanel() );
        pack();
        setLocationRelativeTo( null );
        setVisible( true );
    }


    @Override
	public void actionPerformed( ActionEvent actionEvent )
    {
    	if( actionEvent.getSource() == m_radioClassic || actionEvent.getSource() == m_radio2009Revised )
    	{
    		m_spinnerIssue.setEnabled( m_radioClassic.isSelected() );
    	}
    	else
    	{
	    	// Default to the convert button.
	    	if( m_stardateToGregorian )
	        {
	            Stardate starDate = new Stardate();
	            try 
	            {
	                starDate.setStardateClassic
	                (
	                    ( (Integer)m_spinnerIssue.getValue() ).intValue(),
	                    ( (Integer)m_spinnerInteger.getValue() ).intValue(),
	                    ( (Integer)m_spinnerFraction.getValue() ).intValue()
	                );
	
	                m_spinnerYYYYMMDD.setValue( starDate.getGregorian().getTime() );
	                m_spinnerHHMMSS.setValue( starDate.getGregorian().getTime() );
	            }
	            catch( IllegalArgumentException illegalArgumentException ) { JOptionPane.showMessageDialog( this, illegalArgumentException.getMessage() ); }
	        }
	        else
	        {
	            GregorianCalendar yyyyMMdd = new GregorianCalendar();
	            yyyyMMdd.setTime( ( (SpinnerDateModel)m_spinnerYYYYMMDD.getModel() ).getDate() );
	
	            GregorianCalendar hhmmss = new GregorianCalendar();
	            hhmmss.setTime( ( (SpinnerDateModel)m_spinnerHHMMSS.getModel() ).getDate() );
	
	            GregorianCalendar gc =
	            	new GregorianCalendar
	            	(
	            		yyyyMMdd.get( Calendar.YEAR ),
	                    yyyyMMdd.get( Calendar.MONTH ),
	                    yyyyMMdd.get( Calendar.DAY_OF_MONTH ),
	                    hhmmss.get( Calendar.HOUR_OF_DAY ),
	                    hhmmss.get( Calendar.MINUTE ),
	                    hhmmss.get( Calendar.SECOND )
	            	);
	
	        	GregorianCalendar utc = new GregorianCalendar( TimeZone.getTimeZone( "UTC" ) );
	            utc.setTimeInMillis( gc.getTimeInMillis() );
	
	            Stardate starDate = new Stardate();
	            starDate.setClassic( m_radioClassic.isSelected() );
	            starDate.setGregorian( utc );

	            m_spinnerIssue.setValue( Integer.valueOf( starDate.getStardateIssue() ) );
	            m_spinnerInteger.setValue( Integer.valueOf( starDate.getStardateInteger() ) );
	            m_spinnerFraction.setValue( Integer.valueOf( starDate.getStardateFraction() ) );            		            	
	        }
    	}
    }


    @Override
	public void stateChanged( ChangeEvent changeEvent )
    {
    	if( changeEvent.getSource() == m_spinnerHHMMSS || changeEvent.getSource() == m_spinnerYYYYMMDD )
    		m_stardateToGregorian = false;
    	else
    		m_stardateToGregorian = true;
    }


    @Override
	public void focusGained( FocusEvent focusEvent )
    {
    	if( focusEvent.getSource() == ( (JSpinner.DefaultEditor)m_spinnerHHMMSS.getEditor() ).getTextField() || focusEvent.getSource() == ( (JSpinner.DefaultEditor)m_spinnerYYYYMMDD.getEditor() ).getTextField() )
    		m_stardateToGregorian = false;
    	else
    		m_stardateToGregorian = true;
    }


    @Override
	public void focusLost( FocusEvent focusEvent ) { /** Do nothing. */ }


    private JPanel buildMainPanel()
    {
        JLabel gregorianToStardateLabel = new JLabel( "Convert from a Gregorian date to a Stardate:" ); //$NON-NLS-1$

        JLabel yearMonthDayLabel = new JLabel( "YYYY-MM-DD" ); //$NON-NLS-1$

        m_spinnerYYYYMMDD = new JSpinner( new SpinnerDateModel() );
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor( m_spinnerYYYYMMDD, "yyyy-MM-dd" ); //$NON-NLS-1$
        dateEditor.getTextField().addFocusListener( this );
        m_spinnerYYYYMMDD.setEditor( dateEditor );
        m_spinnerYYYYMMDD.setToolTipText( "Set the date in the format of YYYY-MM-DD" ); //$NON-NLS-1$
        m_spinnerYYYYMMDD.addChangeListener( this );

        JLabel hourMinuteSecondLabel = new JLabel( "HH:MM:SS" ); //$NON-NLS-1$

        m_spinnerHHMMSS = new JSpinner( new SpinnerDateModel() );
        dateEditor = new JSpinner.DateEditor( m_spinnerHHMMSS, "HH:mm:ss" ); //$NON-NLS-1$
        dateEditor.getTextField().addFocusListener( this );
        m_spinnerHHMMSS.setEditor( dateEditor );
        m_spinnerHHMMSS.setToolTipText( "Set the time in the format of HH:MM:DD (in 24 hour time)" ); //$NON-NLS-1$
        m_spinnerHHMMSS.addChangeListener( this );

        JLabel stardateToGregorianLabel = new JLabel( "Convert from a Stardate to a Gregorian date:" ); //$NON-NLS-1$

		m_radioClassic = new JRadioButton( "'classic' stardate", true ); //$NON-NLS-1$
		m_radioClassic.addActionListener( this );

		m_radio2009Revised = new JRadioButton( "'2009 revised' stardate", false ); //$NON-NLS-1$
		m_radio2009Revised.addActionListener( this );

		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add( m_radioClassic );
		buttonGroup.add( m_radio2009Revised );

		JLabel issueLabel = new JLabel( "Issue" ); //$NON-NLS-1$

        m_spinnerIssue = new JSpinner( new SpinnerNumberModel() );
        m_spinnerIssue.setToolTipText( "Only applies to 'classic'." ); //$NON-NLS-1$
        m_spinnerIssue.addChangeListener( this );
        ( (JSpinner.DefaultEditor)m_spinnerIssue.getEditor() ).getTextField().addFocusListener( this );

        JLabel integerLabel = new JLabel( "Integer" ); //$NON-NLS-1$

        m_spinnerInteger = new JSpinner( new SpinnerNumberModel() );
        m_spinnerInteger.setToolTipText
        (
        	"<html>" +
    		"For <b>'classic'</b>:<br>" +
			"issue <= 19: 0 <= integer <= 9999<br>" +
			"issue == 20: 0 <= integer < 5006<br>" +
			"issue >= 21: 0 <= integer <= 99999<br><br>" + 
    		"For <b>'2009 revised'</b>:<br>" +
			"integer > 0" +
    		"</html>"
        );
        m_spinnerInteger.addChangeListener( this );
        ( (JSpinner.DefaultEditor)m_spinnerInteger.getEditor() ).getTextField().addFocusListener( this );

        JLabel fractionLabel = new JLabel( "Fraction" ); //$NON-NLS-1$

        m_spinnerFraction = new JSpinner( new SpinnerNumberModel() );
        m_spinnerFraction.setToolTipText
        (
        	"<html>" +
    		"For <b>'classic'</b>:<br>" +
			"issue <= 19, 0 <= integer <= 9999: fraction >= 0<br>" +
			"issue == 20, 0 <= integer < 5006: fraction >= 0<br>" +
			"issue >= 21, 0 <= integer <= 99999: fraction > 0<br><br>" + 
    		"For <b>'2009 revised'</b>:<br>" +
			"0 <= fraction <= 365, or 366 if integer corresponds to a leap year" +
    		"</html>"
        );
        m_spinnerFraction.addChangeListener( this );
        ( (JSpinner.DefaultEditor)m_spinnerFraction.getEditor() ).getTextField().addFocusListener( this );

        JButton convert = new JButton( "Convert" ); //$NON-NLS-1$
        convert.addActionListener( this );

        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout( panel );
        panel.setLayout( layout );
        layout.setAutoCreateGaps( true );
        layout.setAutoCreateContainerGaps( true );

        layout.linkSize( SwingConstants.HORIZONTAL, yearMonthDayLabel, hourMinuteSecondLabel, issueLabel, integerLabel, fractionLabel );
        layout.linkSize( SwingConstants.HORIZONTAL, m_spinnerFraction, m_spinnerHHMMSS, m_spinnerInteger, m_spinnerIssue, m_spinnerYYYYMMDD );
        layout.linkSize( SwingConstants.VERTICAL, m_spinnerFraction, m_spinnerHHMMSS, m_spinnerInteger, m_spinnerIssue, m_spinnerYYYYMMDD );

        layout.setHorizontalGroup
        (
			layout.createParallelGroup()
				.addComponent( gregorianToStardateLabel )
				.addGroup
				(
		    		layout.createSequentialGroup()
						.addGroup
						(
				    		layout.createParallelGroup()
				    			.addComponent( yearMonthDayLabel )
				    			.addComponent( hourMinuteSecondLabel )
						)
						.addGroup
						(
				    		layout.createParallelGroup()
				    			.addComponent( m_spinnerYYYYMMDD )
				    			.addComponent( m_spinnerHHMMSS )
						)
		    			.addComponent( yearMonthDayLabel )
				)
				.addComponent( stardateToGregorianLabel )
    			.addComponent( m_radioClassic )
    			.addComponent( m_radio2009Revised )
				.addGroup
				(
		    		layout.createSequentialGroup()
						.addGroup
						(
				    		layout.createParallelGroup()
				    			.addComponent( issueLabel )
				    			.addComponent( integerLabel )
				    			.addComponent( fractionLabel )
						)
						.addGroup
						(
				    		layout.createParallelGroup()
				    			.addComponent( m_spinnerIssue )
				    			.addComponent( m_spinnerInteger )
				    			.addComponent( m_spinnerFraction )
						)
		    			.addComponent( yearMonthDayLabel )
				)
				.addComponent( convert, Alignment.CENTER )
		);

        layout.setVerticalGroup
        (
    		layout.createSequentialGroup()
				.addComponent( gregorianToStardateLabel )
				.addGroup
				(
		    		layout.createParallelGroup()
		    			.addComponent( yearMonthDayLabel )
		    			.addComponent( m_spinnerYYYYMMDD )
				)
				.addGroup
				(
		    		layout.createParallelGroup()
		    			.addComponent( hourMinuteSecondLabel )
		    			.addComponent( m_spinnerHHMMSS )
				)
				.addPreferredGap( LayoutStyle.ComponentPlacement.UNRELATED )
				.addComponent( stardateToGregorianLabel )
    			.addComponent( m_radioClassic )
    			.addComponent( m_radio2009Revised )
				.addGroup
				(
		    		layout.createParallelGroup()
		    			.addComponent( issueLabel )
		    			.addComponent( m_spinnerIssue )
				)
				.addGroup
				(
		    		layout.createParallelGroup()
		    			.addComponent( integerLabel )
		    			.addComponent( m_spinnerInteger )
				)
				.addGroup
				(
		    		layout.createParallelGroup()
		    			.addComponent( fractionLabel )
		    			.addComponent( m_spinnerFraction )
				)
				.addPreferredGap( LayoutStyle.ComponentPlacement.UNRELATED )
				.addComponent( convert )
		);

        return panel;
    }


    public static void main( String[] args ) { new StardateConverter(); }
}