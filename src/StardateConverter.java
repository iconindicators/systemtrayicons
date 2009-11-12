import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.LayoutStyle;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class StardateConverter extends JFrame implements ActionListener, ChangeListener, FocusListener
{
	private static String APPLICATION_ICON_IMAGE = "stardatesystemtray16x16.gif";  //$NON-NLS-1$

    private JSpinner m_spinnerIssue, m_spinnerInteger, m_spinnerFraction, m_spinnerYYYYMMDD, m_spinnerHHMMSS;
    private boolean m_stardateToGregorian = true;


    public StardateConverter()
    {
        super( "Stardate/Gregorian Converter" ); //$NON-NLS-1$

        setDefaultCloseOperation( EXIT_ON_CLOSE );
        setIconImage( new ImageIcon( ClassLoader.getSystemResource( APPLICATION_ICON_IMAGE ) ).getImage() );
        setContentPane( buildMainPanel() );
        pack();

        int originX = ( Toolkit.getDefaultToolkit().getScreenSize().width - getWidth() ) / 2;
        int originY = ( Toolkit.getDefaultToolkit().getScreenSize().height - getHeight() ) / 2;
        setLocation( originX, originY );
    }


    public void actionPerformed( ActionEvent actionEvent )
    {
        if( m_stardateToGregorian )
        {
            Stardate starDate = new Stardate();
            try 
            {
                starDate.setStardate
                (
                    ( (Integer)m_spinnerIssue.getValue() ).intValue(),
                    ( (Integer)m_spinnerInteger.getValue() ).intValue(),
                    ( (Integer)m_spinnerFraction.getValue() ).intValue()
                );

                m_spinnerYYYYMMDD.setValue( starDate.getGregorian().getTime() );
                m_spinnerHHMMSS.setValue( starDate.getGregorian().getTime() );
            }
            catch( StardateException stardateException ) 
            {
            	String message = null;
            	if( stardateException.getType() == StardateException.Type.FRACTIONAL_PART_MUST_BE_GREATER_THAN_OR_EQUAL_TO_ZERO )
            		message = "Fractional part must be greater than or equal to zero."; //$NON-NLS-1$
            	else if( stardateException.getType() == StardateException.Type.INTEGER_PART_MUST_BE_BETWEEN_ZERO_AND_99999_INCLUSIVE )
            		message = "Integer part must be between zero and 9999 (inclusive)."; //$NON-NLS-1$
            	else if( stardateException.getType() == StardateException.Type.INTEGER_PART_MUST_BE_BETWEEN_ZERO_AND_9999_INCLUSIVE )
            		message = "Integer part must be between zero and 99999 (inclusive)."; //$NON-NLS-1$
            	else if( stardateException.getType() == StardateException.Type.INTEGER_PART_MUST_BE_LESS_THAN_5006 )
            		message = "Integer part must be less than 5006."; //$NON-NLS-1$

            	JOptionPane.showMessageDialog( this, message );
        	}
        }
        else
        {
            Stardate starDate = new Stardate();
            GregorianCalendar yyyyMMdd = new GregorianCalendar();
            yyyyMMdd.setTime( ( (SpinnerDateModel)m_spinnerYYYYMMDD.getModel() ).getDate() );

            GregorianCalendar hhmmss = new GregorianCalendar();
            hhmmss.setTime( ( (SpinnerDateModel)m_spinnerHHMMSS.getModel() ).getDate() );

            starDate.setGregorian
            (
            	new GregorianCalendar
            	(
            		yyyyMMdd.get( Calendar.YEAR ),
                    yyyyMMdd.get( Calendar.MONTH ),
                    yyyyMMdd.get( Calendar.DAY_OF_MONTH ),
                    hhmmss.get( Calendar.HOUR_OF_DAY ),
                    hhmmss.get( Calendar.MINUTE ),
                    hhmmss.get( Calendar.SECOND )
            	)
            );

            m_spinnerIssue.setValue( Integer.valueOf(starDate.getStardateIssue() ) );
            m_spinnerInteger.setValue( Integer.valueOf(starDate.getStardateInteger() ) );
            m_spinnerFraction.setValue( Integer.valueOf(starDate.getStardateFraction() ) );
        }
    }


    public void stateChanged( ChangeEvent changeEvent )
    {
    	if( changeEvent.getSource() == m_spinnerHHMMSS || changeEvent.getSource() == m_spinnerYYYYMMDD )
    		m_stardateToGregorian = false;
    	else
    		m_stardateToGregorian = true;
    }


    public void focusGained( FocusEvent focusEvent )
    {
    	if( focusEvent.getSource() == ( (JSpinner.DefaultEditor)m_spinnerHHMMSS.getEditor() ).getTextField() || focusEvent.getSource() == ( (JSpinner.DefaultEditor)m_spinnerYYYYMMDD.getEditor() ).getTextField() )
    		m_stardateToGregorian = false;
    	else
    		m_stardateToGregorian = true;
    }


    public void focusLost( FocusEvent focusEvent ) { /** Do nothing. */ }


    private JPanel buildMainPanel()
    {
        JLabel gregorianToStardateLabel = new JLabel( "Convert from a Gregorian date to a Stardate:" ); 

        JLabel yearMonthDayLabel = new JLabel( "YYYY-MM-DD :" );

        m_spinnerYYYYMMDD = new JSpinner( new SpinnerDateModel() );
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor( m_spinnerYYYYMMDD, "yyyy-MM-dd" ); //$NON-NLS-1$
        dateEditor.getTextField().addFocusListener( this );
        m_spinnerYYYYMMDD.setEditor( dateEditor );
        m_spinnerYYYYMMDD.setToolTipText( "Set the date in the format of YYYY-MM-DD" ); //$NON-NLS-1$
        m_spinnerYYYYMMDD.addChangeListener( this );

        JLabel hourMinuteSecondLabel = new JLabel( "HH:MM:SS :" );

        m_spinnerHHMMSS = new JSpinner( new SpinnerDateModel() );
        dateEditor = new JSpinner.DateEditor( m_spinnerHHMMSS, "HH:mm:ss" ); //$NON-NLS-1$
        dateEditor.getTextField().addFocusListener( this );
        m_spinnerHHMMSS.setEditor( dateEditor );
        m_spinnerHHMMSS.setToolTipText( "Set the time in the format of HH:MM:DD (in 24 hour time)" ); //$NON-NLS-1$
        m_spinnerHHMMSS.addChangeListener( this );

        JLabel stardateToGregorianLabel = new JLabel( "Convert from a Stardate to a Gregorian date:" );

        JLabel issueLabel = new JLabel( "Issue:" );

        m_spinnerIssue = new JSpinner( new SpinnerNumberModel() );
        m_spinnerIssue.setToolTipText( "Set the issue for the stardate (can be zero or negative)" ); //$NON-NLS-1$
        m_spinnerIssue.addChangeListener( this );
        ( (JSpinner.DefaultEditor)m_spinnerIssue.getEditor() ).getTextField().addFocusListener( this );

        JLabel integerLabel = new JLabel( "Integer:" );

        m_spinnerInteger = new JSpinner( new SpinnerNumberModel() );
        m_spinnerInteger.setToolTipText( "Set the integer part of the stardate (must be greater than zero)" ); //$NON-NLS-1$
        m_spinnerInteger.addChangeListener( this );
        ( (JSpinner.DefaultEditor)m_spinnerInteger.getEditor() ).getTextField().addFocusListener( this );

        JLabel fractionLabel = new JLabel( "Fraction:" );

        m_spinnerFraction = new JSpinner( new SpinnerNumberModel() );
        m_spinnerFraction.setToolTipText( "Set the fractional part of the stardate (must not be negative)" ); //$NON-NLS-1$
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


    public static void main( String[] args ) { new StardateConverter().setVisible( true ); }
}
