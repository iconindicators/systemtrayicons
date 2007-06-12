import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;


public class StardateConverter extends JFrame implements ActionListener
{
    private JRadioButton m_radioGregorianToStardate, m_radioStardateToGregorian;
    private JSpinner m_spinnerIssue, m_spinnerInteger, m_spinnerFraction;
    private JSpinner m_spinnerYYYYMMDD, m_spinnerHHMMSS;
    private JButton m_convert;


    public StardateConverter()
    {
        super( "Stardate/Gregorian Converter" );

        ImageIcon icon = new ImageIcon( "stardate.gif" );
        if( icon != null )
            setIconImage( icon.getImage() );

        setContentPane( buildMainPanel() );
        pack();

        // Centre the frame about the screen.
        int originX = ( Toolkit.getDefaultToolkit().getScreenSize().width - getWidth() ) / 2;
        int originY = ( Toolkit.getDefaultToolkit().getScreenSize().height - getHeight() ) / 2;
        setLocation( originX, originY );

        setDefaultCloseOperation( EXIT_ON_CLOSE );
        m_radioGregorianToStardate.doClick();
        m_convert.doClick();
    }


    public void dispose()
    {
    	m_convert.removeActionListener( this );
    	m_radioGregorianToStardate.removeActionListener( this );
    	m_radioStardateToGregorian.removeActionListener( this );

    	super.dispose();
    }


    public void actionPerformed( ActionEvent actionEvent )
    {
        Object source = actionEvent.getSource();

        if( source == m_convert )
        {
            if( m_radioGregorianToStardate.isSelected() )
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
                		yyyyMMdd.get( GregorianCalendar.YEAR ),
	                    yyyyMMdd.get( GregorianCalendar.MONTH ),
	                    yyyyMMdd.get( GregorianCalendar.DAY_OF_MONTH ),
	                    hhmmss.get( GregorianCalendar.HOUR_OF_DAY ),
	                    hhmmss.get( GregorianCalendar.MINUTE ),
	                    hhmmss.get( GregorianCalendar.SECOND )
                	)
                );

                m_spinnerIssue.setValue( Integer.valueOf(starDate.getStardateIssue() ) );
                m_spinnerInteger.setValue( Integer.valueOf(starDate.getStardateInteger() ) );
                m_spinnerFraction.setValue( Integer.valueOf(starDate.getStardateFraction() ) );
            }
            else // m_radioStardateToGregorian.isSelected() )
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
                		message = "Fractional part must be greater than or equal to zero.";
                	else if( stardateException.getType() == StardateException.Type.INTEGER_PART_MUST_BE_BETWEEN_ZERO_AND_99999_INCLUSIVE )
                		message = "Integer part must be between zero and 9999 (inclusive).";
                	else if( stardateException.getType() == StardateException.Type.INTEGER_PART_MUST_BE_BETWEEN_ZERO_AND_9999_INCLUSIVE )
                		message = "Integer part must be between zero and 99999 (inclusive).";
                	else if( stardateException.getType() == StardateException.Type.INTEGER_PART_MUST_BE_LESS_THAN_5006 )
                		message = "Integer part must be less than 5006.";

                	JOptionPane.showMessageDialog( this, message );
            	}
            }
        }
        else if( source == m_radioGregorianToStardate || source == m_radioStardateToGregorian )
        {
            m_spinnerIssue.setEnabled( ! m_radioGregorianToStardate.isSelected() );
            m_spinnerInteger.setEnabled( ! m_radioGregorianToStardate.isSelected() );
            m_spinnerFraction.setEnabled( ! m_radioGregorianToStardate.isSelected() );

            m_spinnerYYYYMMDD.setEnabled( m_radioGregorianToStardate.isSelected() );
            m_spinnerHHMMSS.setEnabled( m_radioGregorianToStardate.isSelected() );

            if( m_radioGregorianToStardate.isSelected() )
            	m_convert.setToolTipText( "Convert from a Gregorian date to a Stardate" );
            else
            	m_convert.setToolTipText( "Convert from a Stardate to a Gregorian date" );
        }
    }


    private JPanel buildMainPanel()
    {
        JPanel panel = new JPanel();
        panel.setBorder( BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) );
        panel.setLayout( new GridBagLayout() );

        GridBagConstraints constraints;

        m_radioGregorianToStardate = new JRadioButton( "Convert from a Gregorian date to a Stardate" );
        m_radioGregorianToStardate.addActionListener( this );
        constraints = new GridBagConstraints( 0, 0, 2, 1, 0.0D, 0.0D, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( m_radioGregorianToStardate, constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 0.0D, 0.0D, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 0, 20, 0, 0  ), 0, 0 );
        panel.add( new JLabel( "YYYY-MM-DD :" ), constraints );

        m_spinnerYYYYMMDD = new JSpinner( new SpinnerDateModel() );
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor( m_spinnerYYYYMMDD, "yyyy-MM-dd" );
        m_spinnerYYYYMMDD.setEditor( dateEditor );
        m_spinnerYYYYMMDD.setToolTipText( "Set the date in the format of YYYY-MM-DD" );
        constraints = new GridBagConstraints( 1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets( 0, 10, 0, 0  ), 0, 0 );
        panel.add( m_spinnerYYYYMMDD, constraints );

        constraints = new GridBagConstraints( 0, 2, 1, 1, 0.0D, 0.0D, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 0, 20, 0, 0  ), 0, 0 );
        panel.add( new JLabel( "HH:MM:SS :" ), constraints );

        m_spinnerHHMMSS = new JSpinner( new SpinnerDateModel() );
        dateEditor = new JSpinner.DateEditor( m_spinnerHHMMSS, "HH:mm:ss" );
        m_spinnerHHMMSS.setEditor( dateEditor );
        m_spinnerHHMMSS.setToolTipText( "Set the time in the format of HH:MM:DD (in 24 hour time)" );
        constraints = new GridBagConstraints( 1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets( 0, 10, 0, 0  ), 0, 0 );
        panel.add( m_spinnerHHMMSS, constraints );

        m_radioStardateToGregorian = new JRadioButton( "Convert from a Stardate to a Gregorian date" );
        m_radioStardateToGregorian.addActionListener( this );
        constraints = new GridBagConstraints( 0, 3, 2, 1, 0.0D, 0.0D, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 20, 0, 0, 0 ), 0, 0 );
        panel.add( m_radioStardateToGregorian, constraints );

        constraints = new GridBagConstraints( 0, 4, 1, 1, 0.0D, 0.0D, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 0, 20, 0, 0  ), 0, 0 );
        panel.add( new JLabel( "Issue:" ), constraints );

        m_spinnerIssue = new JSpinner( new SpinnerNumberModel() );
        m_spinnerIssue.setToolTipText( "Set the issue for the stardate (can be zero or negative)" );
        constraints = new GridBagConstraints( 1, 4, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets( 0, 10, 0, 0  ), 0, 0 );
        panel.add( m_spinnerIssue, constraints );

        constraints = new GridBagConstraints( 0, 5, 1, 1, 0.0D, 0.0D, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 0, 20, 0, 0  ), 0, 0 );
        panel.add( new JLabel( "Integer:" ), constraints );

        m_spinnerInteger = new JSpinner( new SpinnerNumberModel() );
        m_spinnerInteger.setToolTipText( "Set the integer part of the stardate (must be greater than zero)" );
        constraints = new GridBagConstraints( 1, 5, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets( 0, 10, 0, 0  ), 0, 0 );
        panel.add( m_spinnerInteger, constraints );

        constraints = new GridBagConstraints( 0, 6, 1, 1, 0.0D, 0.0D, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 0, 20, 0, 0  ), 0, 0 );
        panel.add( new JLabel( "Fraction:" ), constraints );

        m_spinnerFraction = new JSpinner( new SpinnerNumberModel() );
        m_spinnerFraction.setToolTipText( "Set the fractional part of the stardate (must not be negative)" );
        constraints = new GridBagConstraints( 1, 6, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets( 0, 10, 0, 0  ), 0, 0 );
        panel.add( m_spinnerFraction, constraints );

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add( m_radioGregorianToStardate );
        buttonGroup.add( m_radioStardateToGregorian );

        m_convert = new JButton( "Convert" );
        m_convert.addActionListener( this );
        constraints = new GridBagConstraints( 0, 7, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets( 20, 0, 0, 0  ), 0, 0 );
        panel.add( m_convert, constraints );

        return panel;
    }


    public static void main( String[] args )
    {
        new StardateConverter().setVisible( true );
    }
}
