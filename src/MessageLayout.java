import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EtchedBorder;


public class MessageLayout extends JDialog
{
	private static final long serialVersionUID = 1L;
	private static final int BORDER_INDENT = 10;
	private static final int NUMBER_OF_COLUMNS = 5;

	protected static String m_leftText, m_leftOption, m_leftCentreText, m_centreOption, m_rightCentreText, m_rightOption, m_rightText;
	protected static boolean m_columnsLeftTextAndLeftOptionAreSeparateOption, m_columnsLeftOptionAndLeftCentreTextAreSeparateOption, m_columnsLeftCentreTextAndCentreOptionAreSeparateOption, m_columnsCentreOptionAndRightCentreTextAreSeparateOption, m_columnsRightCentreTextAndRightOptionAreSeparateOption, m_columnsRightOptionAndRightTextAreSeparateOption;

	private MessageLayout() { super( (JDialog)null ); }


    public static MessageLayout create()
    {
    	final MessageLayout messageLayout = new MessageLayout();

    	m_leftText = Properties.getInstance().getProperty( Properties.PROPERTY_LAYOUT_LEFT_TEXT, Properties.PROPERTY_LAYOUT_LEFT_TEXT_DEFAULT, false );
    	m_leftOption = Properties.getInstance().getProperty( Properties.PROPERTY_LAYOUT_LEFT_OPTION, Properties.PROPERTY_LAYOUT_OPTION_TIME_ZONE, false );
    	m_leftCentreText = Properties.getInstance().getProperty( Properties.PROPERTY_LAYOUT_LEFT_CENTRE_TEXT, Properties.PROPERTY_LAYOUT_LEFT_CENTRE_TEXT_DEFAULT, false );
    	m_centreOption = Properties.getInstance().getProperty( Properties.PROPERTY_LAYOUT_CENTRE_OPTION, Properties.PROPERTY_LAYOUT_OPTION_DIFFERENT_DAY_INDICATOR, false );
    	m_rightCentreText = Properties.getInstance().getProperty( Properties.PROPERTY_LAYOUT_RIGHT_CENTRE_TEXT, Properties.PROPERTY_LAYOUT_RIGHT_CENTRE_TEXT_DEFAULT, false );
    	m_rightOption = Properties.getInstance().getProperty( Properties.PROPERTY_LAYOUT_RIGHT_OPTION, Properties.PROPERTY_LAYOUT_OPTION_TIME, false );
    	m_rightText = Properties.getInstance().getProperty( Properties.PROPERTY_LAYOUT_RIGHT_TEXT, Properties.PROPERTY_LAYOUT_RIGHT_TEXT_DEFAULT, false );

    	m_columnsLeftTextAndLeftOptionAreSeparateOption = Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_COLUMNS_LEFT_TEXT_AND_LEFT_OPTION_ARE_SEPARATE, false );
    	m_columnsLeftOptionAndLeftCentreTextAreSeparateOption = Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_COLUMNS_LEFT_OPTION_AND_LEFT_CENTRE_TEXT_ARE_SEPARATE, false );
    	m_columnsLeftCentreTextAndCentreOptionAreSeparateOption = Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_COLUMNS_LEFT_CENTRE_TEXT_AND_CENTRE_OPTION_ARE_SEPARATE, false );
		m_columnsCentreOptionAndRightCentreTextAreSeparateOption = Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_COLUMNS_CENTRE_OPTION_AND_RIGHT_CENTRE_TEXT_ARE_SEPARATE, false );
		m_columnsRightCentreTextAndRightOptionAreSeparateOption = Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_COLUMNS_RIGHT_CENTRE_TEXT_AND_RIGHT_OPTION_ARE_SEPARATE, false );
		m_columnsRightOptionAndRightTextAreSeparateOption = Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_COLUMNS_RIGHT_OPTION_AND_RIGHT_TEXT_ARE_SEPARATE, false );

    	Vector<String> comboValues = new Vector<String>();
    	comboValues.add( Properties.PROPERTY_LAYOUT_OPTION_DIFFERENT_DAY_INDICATOR );
    	comboValues.add( Properties.PROPERTY_LAYOUT_OPTION_TIME );
    	comboValues.add( Properties.PROPERTY_LAYOUT_OPTION_TIME_ZONE );
    	comboValues.add( Properties.PROPERTY_LAYOUT_OPTION_NONE );

    	JLabel labelComponents = new JLabel( "Layout Components: " );
    	
    	final JTextField leftTextfield = new JTextField( Properties.getInstance().getProperty( Properties.PROPERTY_LAYOUT_LEFT_TEXT, Properties.PROPERTY_LAYOUT_LEFT_TEXT_DEFAULT, false ) );
    	leftTextfield.setColumns( NUMBER_OF_COLUMNS );

    	final JComboBox leftCombo = new JComboBox( comboValues );
    	leftCombo.setSelectedItem(  Properties.getInstance().getProperty( Properties.PROPERTY_LAYOUT_LEFT_OPTION, Properties.PROPERTY_LAYOUT_OPTION_TIME_ZONE, false ) ) ;

    	final JTextField leftCentreTextfield = new JTextField( Properties.getInstance().getProperty( Properties.PROPERTY_LAYOUT_LEFT_CENTRE_TEXT, Properties.PROPERTY_LAYOUT_LEFT_CENTRE_TEXT_DEFAULT, false ) );
    	leftCentreTextfield.setColumns( NUMBER_OF_COLUMNS );

    	final JComboBox centreCombo = new JComboBox( comboValues );
    	centreCombo.setSelectedItem(  Properties.getInstance().getProperty( Properties.PROPERTY_LAYOUT_CENTRE_OPTION, Properties.PROPERTY_LAYOUT_OPTION_DIFFERENT_DAY_INDICATOR, false ) ) ;

    	final JTextField rightCentreTextfield = new JTextField( Properties.getInstance().getProperty( Properties.PROPERTY_LAYOUT_RIGHT_CENTRE_TEXT, Properties.PROPERTY_LAYOUT_RIGHT_CENTRE_TEXT_DEFAULT, false ) );
    	rightCentreTextfield.setColumns( NUMBER_OF_COLUMNS );

    	final JComboBox rightCombo = new JComboBox( comboValues );
    	rightCombo.setSelectedItem(  Properties.getInstance().getProperty( Properties.PROPERTY_LAYOUT_RIGHT_OPTION, Properties.PROPERTY_LAYOUT_OPTION_TIME, false ) ) ;

    	final JTextField rightTextfield = new JTextField( Properties.getInstance().getProperty( Properties.PROPERTY_LAYOUT_RIGHT_TEXT, Properties.PROPERTY_LAYOUT_RIGHT_TEXT_DEFAULT, false ) );
    	rightTextfield.setColumns( NUMBER_OF_COLUMNS );

    	JLabel labelColumnBreaks = new JLabel( "Column Breaks: " );

    	String columsCheckboxToolTip =
    		"<html>When checked, the adjoining components will be in a separate column.<br>When unchecked, the adjoining components will be in a single column.</html>";

    	final JCheckBox columnsLeftTextAndLeftOptionAreSeparateCheckbox = new JCheckBox( "", null, m_columnsLeftTextAndLeftOptionAreSeparateOption );
    	columnsLeftTextAndLeftOptionAreSeparateCheckbox.setToolTipText( columsCheckboxToolTip );

    	final JCheckBox columnsLeftOptionAndLeftCentreTextAreSeparateCheckbox = new JCheckBox( "", null, m_columnsLeftOptionAndLeftCentreTextAreSeparateOption );
    	columnsLeftOptionAndLeftCentreTextAreSeparateCheckbox.setToolTipText( columsCheckboxToolTip );

    	final JCheckBox columnsLeftCentreTextAndCentreOptionAreSeparateCheckbox = new JCheckBox( "", null, m_columnsLeftCentreTextAndCentreOptionAreSeparateOption );
    	columnsLeftCentreTextAndCentreOptionAreSeparateCheckbox.setToolTipText( columsCheckboxToolTip );

    	final JCheckBox columnsCentreOptionAndRightCentreTextAreSeparateCheckbox = new JCheckBox( "", null, m_columnsCentreOptionAndRightCentreTextAreSeparateOption );
    	columnsCentreOptionAndRightCentreTextAreSeparateCheckbox.setToolTipText( columsCheckboxToolTip );

    	final JCheckBox columnsRightCentreTextAndRightOptionAreSeparateCheckbox = new JCheckBox( "", null, m_columnsRightCentreTextAndRightOptionAreSeparateOption );
    	columnsRightCentreTextAndRightOptionAreSeparateCheckbox.setToolTipText( columsCheckboxToolTip );

    	final JCheckBox columnsRightOptionAndRightTextAreSeparateCheckbox = new JCheckBox( "", null, m_columnsRightOptionAndRightTextAreSeparateOption );
    	columnsRightOptionAndRightTextAreSeparateCheckbox.setToolTipText( columsCheckboxToolTip );

    	JButton sampleButton = new JButton( Messages.getString( "MessageLayout.0" ) ); //$NON-NLS-1$

    	final JLabel sampleLabel = new JLabel( Message.getMessageString( new GregorianCalendar(), true ) );
    	sampleLabel.setHorizontalAlignment( SwingConstants.CENTER );
    	sampleLabel.setBorder
        ( 
        	BorderFactory.createCompoundBorder
        	(
            	BorderFactory.createCompoundBorder
            	( 
            		BorderFactory.createEmptyBorder( BORDER_INDENT, BORDER_INDENT, BORDER_INDENT, BORDER_INDENT ),
            		BorderFactory.createEtchedBorder( EtchedBorder.LOWERED )
            	),
        		BorderFactory.createEmptyBorder( BORDER_INDENT, BORDER_INDENT, BORDER_INDENT, BORDER_INDENT )
    		)
        );

    	sampleButton.addActionListener
    	(
			new ActionListener()
			{
				public void actionPerformed( ActionEvent actionEvent )
				{
		    		Properties.getInstance().setProperty( Properties.PROPERTY_LAYOUT_LEFT_TEXT, leftTextfield.getText() );
		    		Properties.getInstance().setProperty( Properties.PROPERTY_LAYOUT_LEFT_OPTION, leftCombo.getSelectedItem().toString() );
		    		Properties.getInstance().setProperty( Properties.PROPERTY_LAYOUT_LEFT_CENTRE_TEXT, leftCentreTextfield.getText() );
		    		Properties.getInstance().setProperty( Properties.PROPERTY_LAYOUT_CENTRE_OPTION, centreCombo.getSelectedItem().toString() );
		    		Properties.getInstance().setProperty( Properties.PROPERTY_LAYOUT_RIGHT_CENTRE_TEXT, rightCentreTextfield.getText() );
		    		Properties.getInstance().setProperty( Properties.PROPERTY_LAYOUT_RIGHT_OPTION, rightCombo.getSelectedItem().toString() );
		    		Properties.getInstance().setProperty( Properties.PROPERTY_LAYOUT_RIGHT_TEXT, rightTextfield.getText() );

		    		Properties.getInstance().setProperty( Properties.PROPERTY_COLUMNS_LEFT_TEXT_AND_LEFT_OPTION_ARE_SEPARATE, Boolean.toString( columnsLeftTextAndLeftOptionAreSeparateCheckbox.isSelected() ) );
		    		Properties.getInstance().setProperty( Properties.PROPERTY_COLUMNS_LEFT_OPTION_AND_LEFT_CENTRE_TEXT_ARE_SEPARATE, Boolean.toString( columnsLeftOptionAndLeftCentreTextAreSeparateCheckbox.isSelected() ) );
		    		Properties.getInstance().setProperty( Properties.PROPERTY_COLUMNS_LEFT_CENTRE_TEXT_AND_CENTRE_OPTION_ARE_SEPARATE, Boolean.toString( columnsLeftCentreTextAndCentreOptionAreSeparateCheckbox.isSelected() ) );
		    		Properties.getInstance().setProperty( Properties.PROPERTY_COLUMNS_CENTRE_OPTION_AND_RIGHT_CENTRE_TEXT_ARE_SEPARATE, Boolean.toString( columnsCentreOptionAndRightCentreTextAreSeparateCheckbox.isSelected() ) );
		    		Properties.getInstance().setProperty( Properties.PROPERTY_COLUMNS_RIGHT_CENTRE_TEXT_AND_RIGHT_OPTION_ARE_SEPARATE, Boolean.toString( columnsRightCentreTextAndRightOptionAreSeparateCheckbox.isSelected() ) );
		    		Properties.getInstance().setProperty( Properties.PROPERTY_COLUMNS_RIGHT_OPTION_AND_RIGHT_TEXT_ARE_SEPARATE, Boolean.toString( columnsRightOptionAndRightTextAreSeparateCheckbox.isSelected() ) );

		    		Properties.getInstance().store();

		    		sampleLabel.setText( Message.getMessageString( new GregorianCalendar(), true ) );
					messageLayout.pack();
				}
			}
		);

    	JButton ok = new JButton( Messages.getString( "MessageLayout.1" ) ); //$NON-NLS-1$
    	ok.addActionListener
    	(
			new ActionListener()
			{
				public void actionPerformed( ActionEvent actionEvent )
				{
		    		Properties.getInstance().setProperty( Properties.PROPERTY_LAYOUT_LEFT_TEXT, leftTextfield.getText() );
		    		Properties.getInstance().setProperty( Properties.PROPERTY_LAYOUT_LEFT_OPTION, leftCombo.getSelectedItem().toString() );
		    		Properties.getInstance().setProperty( Properties.PROPERTY_LAYOUT_LEFT_CENTRE_TEXT, leftCentreTextfield.getText() );
		    		Properties.getInstance().setProperty( Properties.PROPERTY_LAYOUT_CENTRE_OPTION, centreCombo.getSelectedItem().toString() );
		    		Properties.getInstance().setProperty( Properties.PROPERTY_LAYOUT_RIGHT_CENTRE_TEXT, rightCentreTextfield.getText() );
		    		Properties.getInstance().setProperty( Properties.PROPERTY_LAYOUT_RIGHT_OPTION, rightCombo.getSelectedItem().toString() );
		    		Properties.getInstance().setProperty( Properties.PROPERTY_LAYOUT_RIGHT_TEXT, rightTextfield.getText() );

		    		Properties.getInstance().setProperty( Properties.PROPERTY_COLUMNS_LEFT_TEXT_AND_LEFT_OPTION_ARE_SEPARATE, Boolean.toString( columnsLeftTextAndLeftOptionAreSeparateCheckbox.isSelected() ) );
		    		Properties.getInstance().setProperty( Properties.PROPERTY_COLUMNS_LEFT_OPTION_AND_LEFT_CENTRE_TEXT_ARE_SEPARATE, Boolean.toString( columnsLeftOptionAndLeftCentreTextAreSeparateCheckbox.isSelected() ) );
		    		Properties.getInstance().setProperty( Properties.PROPERTY_COLUMNS_LEFT_CENTRE_TEXT_AND_CENTRE_OPTION_ARE_SEPARATE, Boolean.toString( columnsLeftCentreTextAndCentreOptionAreSeparateCheckbox.isSelected() ) );
		    		Properties.getInstance().setProperty( Properties.PROPERTY_COLUMNS_CENTRE_OPTION_AND_RIGHT_CENTRE_TEXT_ARE_SEPARATE, Boolean.toString( columnsCentreOptionAndRightCentreTextAreSeparateCheckbox.isSelected() ) );
		    		Properties.getInstance().setProperty( Properties.PROPERTY_COLUMNS_RIGHT_CENTRE_TEXT_AND_RIGHT_OPTION_ARE_SEPARATE, Boolean.toString( columnsRightCentreTextAndRightOptionAreSeparateCheckbox.isSelected() ) );
		    		Properties.getInstance().setProperty( Properties.PROPERTY_COLUMNS_RIGHT_OPTION_AND_RIGHT_TEXT_ARE_SEPARATE, Boolean.toString( columnsRightOptionAndRightTextAreSeparateCheckbox.isSelected() ) );

		    		Properties.getInstance().store();

					messageLayout.dispose(); 
				} 
			} 
		);

    	JButton cancel = new JButton( Messages.getString( "MessageLayout.2" ) ); //$NON-NLS-1$
    	cancel.addActionListener
    	(
			new ActionListener()
			{
				public void actionPerformed( ActionEvent actionEvent )
				{
					Properties.getInstance().setProperty( Properties.PROPERTY_LAYOUT_LEFT_TEXT, m_leftText );
		    		Properties.getInstance().setProperty( Properties.PROPERTY_LAYOUT_LEFT_OPTION, m_leftOption );
		    		Properties.getInstance().setProperty( Properties.PROPERTY_LAYOUT_LEFT_CENTRE_TEXT, m_leftCentreText );
		    		Properties.getInstance().setProperty( Properties.PROPERTY_LAYOUT_CENTRE_OPTION, m_centreOption );
		    		Properties.getInstance().setProperty( Properties.PROPERTY_LAYOUT_RIGHT_CENTRE_TEXT, m_rightCentreText );
		    		Properties.getInstance().setProperty( Properties.PROPERTY_LAYOUT_RIGHT_OPTION, m_rightOption );
		    		Properties.getInstance().setProperty( Properties.PROPERTY_LAYOUT_RIGHT_TEXT, m_rightText );

		    		Properties.getInstance().setProperty( Properties.PROPERTY_COLUMNS_LEFT_TEXT_AND_LEFT_OPTION_ARE_SEPARATE, Boolean.toString( m_columnsLeftTextAndLeftOptionAreSeparateOption ) );
		    		Properties.getInstance().setProperty( Properties.PROPERTY_COLUMNS_LEFT_OPTION_AND_LEFT_CENTRE_TEXT_ARE_SEPARATE, Boolean.toString( m_columnsLeftOptionAndLeftCentreTextAreSeparateOption ) );
		    		Properties.getInstance().setProperty( Properties.PROPERTY_COLUMNS_LEFT_CENTRE_TEXT_AND_CENTRE_OPTION_ARE_SEPARATE, Boolean.toString( m_columnsLeftCentreTextAndCentreOptionAreSeparateOption ) );
		    		Properties.getInstance().setProperty( Properties.PROPERTY_COLUMNS_CENTRE_OPTION_AND_RIGHT_CENTRE_TEXT_ARE_SEPARATE, Boolean.toString( m_columnsCentreOptionAndRightCentreTextAreSeparateOption ) );
		    		Properties.getInstance().setProperty( Properties.PROPERTY_COLUMNS_RIGHT_CENTRE_TEXT_AND_RIGHT_OPTION_ARE_SEPARATE, Boolean.toString( m_columnsRightCentreTextAndRightOptionAreSeparateOption ) );
		    		Properties.getInstance().setProperty( Properties.PROPERTY_COLUMNS_RIGHT_OPTION_AND_RIGHT_TEXT_ARE_SEPARATE, Boolean.toString( m_columnsRightOptionAndRightTextAreSeparateOption ) );

		    		Properties.getInstance().store();

					messageLayout.dispose(); 
				} 
			} 
		);


    	GroupLayout layout = new GroupLayout( messageLayout.getContentPane() );
        messageLayout.getContentPane().setLayout( layout );
        layout.setAutoCreateGaps( true );
        layout.setAutoCreateContainerGaps( true );

        layout.linkSize( SwingConstants.HORIZONTAL, ok, cancel );
        layout.linkSize( SwingConstants.HORIZONTAL, leftCombo, centreCombo, rightCombo );
        layout.linkSize( SwingConstants.VERTICAL, leftTextfield, leftCombo, leftCentreTextfield, centreCombo, rightCentreTextfield, rightCombo, rightTextfield );

        layout.setHorizontalGroup
        (
    		layout.createParallelGroup( Alignment.CENTER )
    			.addGroup
    			(
	    			layout.createSequentialGroup()
		    			.addGroup
		    			(
			    			layout.createParallelGroup()
			    				.addComponent( labelComponents )
			    				.addComponent( labelColumnBreaks )
						)
						.addComponent( leftTextfield )
						.addComponent( columnsLeftTextAndLeftOptionAreSeparateCheckbox )
						.addComponent( leftCombo )
						.addComponent( columnsLeftOptionAndLeftCentreTextAreSeparateCheckbox )
						.addComponent( leftCentreTextfield )
						.addComponent( columnsLeftCentreTextAndCentreOptionAreSeparateCheckbox )
						.addComponent( centreCombo )
						.addComponent( columnsCentreOptionAndRightCentreTextAreSeparateCheckbox )
						.addComponent( rightCentreTextfield )
						.addComponent( columnsRightCentreTextAndRightOptionAreSeparateCheckbox )
						.addComponent( rightCombo )
						.addComponent( columnsRightOptionAndRightTextAreSeparateCheckbox )
						.addComponent( rightTextfield )
				)
    			.addGroup
    			(
	    			layout.createSequentialGroup()
						.addComponent( sampleButton )
						.addPreferredGap( ComponentPlacement.UNRELATED )
						.addComponent( sampleLabel )
				)
    			.addGroup
    			(
	    			layout.createSequentialGroup()
	    				.addComponent( ok )
	    				.addPreferredGap( ComponentPlacement.UNRELATED )
	    				.addComponent( cancel )
				)
		);

        layout.setVerticalGroup
        (
    		layout.createSequentialGroup()
    			.addGroup
    			(
    				layout.createParallelGroup( Alignment.BASELINE )
    					.addComponent( labelComponents )
    					.addComponent( leftTextfield )
    					.addComponent( leftCombo )
    					.addComponent( leftCentreTextfield )
    					.addComponent( centreCombo )
    					.addComponent( rightCentreTextfield )
    					.addComponent( rightCombo )
    					.addComponent( rightTextfield )
    			)
    			.addGroup
    			(
    				layout.createParallelGroup( Alignment.BASELINE )
						.addComponent( labelColumnBreaks )
						.addComponent( columnsLeftTextAndLeftOptionAreSeparateCheckbox )
						.addComponent( columnsLeftOptionAndLeftCentreTextAreSeparateCheckbox )
						.addComponent( columnsLeftCentreTextAndCentreOptionAreSeparateCheckbox )
						.addComponent( columnsCentreOptionAndRightCentreTextAreSeparateCheckbox )
						.addComponent( columnsRightCentreTextAndRightOptionAreSeparateCheckbox )
						.addComponent( columnsRightOptionAndRightTextAreSeparateCheckbox )
    			)
				.addPreferredGap( ComponentPlacement.UNRELATED )
    			.addGroup
    			(
    				layout.createParallelGroup( Alignment.CENTER )
    					.addComponent( sampleButton, Alignment.CENTER )
    					.addComponent( sampleLabel )
    			)
				.addPreferredGap( ComponentPlacement.UNRELATED )
    			.addGroup
    			(
    				layout.createParallelGroup()
    					.addComponent( ok )
    					.addComponent( cancel )
    			)
		);

		messageLayout.setTitle( Messages.getString( "MessageLayout.3" ) );  //$NON-NLS-1$
        messageLayout.setIconImage( TrayIcon.getApplicationIconImage() );
        messageLayout.pack();
        messageLayout.setLocationRelativeTo( null );
        messageLayout.setModalityType( ModalityType.APPLICATION_MODAL );
        messageLayout.addComponentListener( new ComponentListener( messageLayout, false, true ) );
        messageLayout.setVisible( true );

        return messageLayout;
    }
}
