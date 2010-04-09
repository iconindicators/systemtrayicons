//TODO Maybe align the sample button with the labels and the sample text with the combos.

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EtchedBorder;


public class MessageLayout extends JDialog implements ItemListener
{
	private static final long serialVersionUID = 1L;

	private static final int BORDER_INDENT = 10;
	private static final int NUMBER_OF_COLUMNS = 5;

	private static final String LAYOUT_OPTION_DIFFERENT_DAY_INDICATOR = "Different Day Indicator";
	private static final String LAYOUT_OPTION_TIME = "Time";
	private static final String LAYOUT_OPTION_TIME_ZONE = "Time Zone";
	private static final String LAYOUT_OPTION_NONE = "";

	private static final String ALIGNMENT_LEFT = "Left";
	private static final String ALIGNMENT_CENTRE = "Centre";
	private static final String ALIGNMENT_RIGHT = "Right";

	protected JLabel m_layoutLabel, m_columnBreaksLabel, m_columnAlignmentsLabel, m_sampleLabel;
	protected JTextField m_leftTextLayout, m_leftCentreTextLayout, m_rightCentreTextLayout, m_rightTextLayout;
	protected JComboBox m_leftOptionLayout, m_centreOptionLayout, m_rightOptionLayout;
	protected JCheckBox m_leftTextLeftOptionBreak, m_leftOptionLeftCentreTextBreak, m_leftCentreTextCentreOptionBreak, m_centreOptionRightCentreTextBreak, m_rightCentreTextRightOptionBreak, m_rightOptionRightTextBreak;
	protected JComboBox m_leftTextAlignment, m_leftOptionAlignment, m_leftCentreTextAlignment, m_centreOptionAlignment, m_rightCentreTextAlignment, m_rightOptionAlignment, m_rightTextAlignment;
	protected JButton m_sample, m_ok, m_cancel;

	protected String m_leftText, m_leftOption, m_leftCentreText, m_centreOption, m_rightCentreText, m_rightOption, m_rightText;
	protected boolean m_leftTextAndLeftOptionAreSeparateOption, m_leftOptionAndLeftCentreTextAreSeparateOption, m_leftCentreTextAndCentreOptionAreSeparateOption, m_centreOptionAndRightCentreTextAreSeparateOption, m_rightCentreTextAndRightOptionAreSeparateOption, m_rightOptionAndRightTextAreSeparateOption;
	protected String m_leftTextAlignmentProperty, m_leftOptionAlignmentProperty, m_leftCentreTextAlignmentProperty, m_centreOptionAlignmentProperty, m_rightCentreTextAlignmentProperty, m_rightOptionAlignmentProperty, m_rightTextAlignmentProperty;
	
	protected Vector<JComponent> m_layoutComponents;
	protected Vector<JCheckBox> m_columnCheckboxes;
	protected Vector<JComboBox> m_columnAlignmentCombos;

	private MessageLayout() { super( (JDialog)null ); }


    public static MessageLayout create()
    {
    	final MessageLayout messageLayout = new MessageLayout();

    	// Read in property values and keep a copy of them here in case the user cancels the operation.
    	messageLayout.m_leftText = Properties.getInstance().getProperty( Properties.PROPERTY_LAYOUT_LEFT_TEXT, Properties.PROPERTY_LAYOUT_LEFT_TEXT_DEFAULT, false );
    	messageLayout.m_leftOption = getLayoutProperty( Properties.PROPERTY_LAYOUT_LEFT_OPTION, Properties.PROPERTY_LAYOUT_OPTION_TIME_ZONE );
    	messageLayout.m_leftCentreText = Properties.getInstance().getProperty( Properties.PROPERTY_LAYOUT_LEFT_CENTRE_TEXT, Properties.PROPERTY_LAYOUT_LEFT_CENTRE_TEXT_DEFAULT, false );
    	messageLayout.m_centreOption = getLayoutProperty( Properties.PROPERTY_LAYOUT_CENTRE_OPTION, Properties.PROPERTY_LAYOUT_OPTION_DIFFERENT_DAY_INDICATOR );
    	messageLayout.m_rightCentreText = Properties.getInstance().getProperty( Properties.PROPERTY_LAYOUT_RIGHT_CENTRE_TEXT, Properties.PROPERTY_LAYOUT_RIGHT_CENTRE_TEXT_DEFAULT, false );
    	messageLayout.m_rightOption = getLayoutProperty( Properties.PROPERTY_LAYOUT_RIGHT_OPTION, Properties.PROPERTY_LAYOUT_OPTION_TIME );
    	messageLayout.m_rightText = Properties.getInstance().getProperty( Properties.PROPERTY_LAYOUT_RIGHT_TEXT, Properties.PROPERTY_LAYOUT_RIGHT_TEXT_DEFAULT, false );

    	messageLayout.m_leftTextAndLeftOptionAreSeparateOption = Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_COLUMNS_LEFT_TEXT_AND_LEFT_OPTION_ARE_SEPARATE, true );
    	messageLayout.m_leftOptionAndLeftCentreTextAreSeparateOption = Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_COLUMNS_LEFT_OPTION_AND_LEFT_CENTRE_TEXT_ARE_SEPARATE, true );
    	messageLayout.m_leftCentreTextAndCentreOptionAreSeparateOption = Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_COLUMNS_LEFT_CENTRE_TEXT_AND_CENTRE_OPTION_ARE_SEPARATE, true );
    	messageLayout.m_centreOptionAndRightCentreTextAreSeparateOption = Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_COLUMNS_CENTRE_OPTION_AND_RIGHT_CENTRE_TEXT_ARE_SEPARATE, true );
    	messageLayout.m_rightCentreTextAndRightOptionAreSeparateOption = Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_COLUMNS_RIGHT_CENTRE_TEXT_AND_RIGHT_OPTION_ARE_SEPARATE, true );
    	messageLayout.m_rightOptionAndRightTextAreSeparateOption = Properties.getInstance().getPropertyBoolean( Properties.PROPERTY_COLUMNS_RIGHT_OPTION_AND_RIGHT_TEXT_ARE_SEPARATE, true );

    	messageLayout.m_leftTextAlignmentProperty = getAlignmentProperty( Properties.PROPERTY_COLUMN_LEFT_TEXT_ALIGNMENT );
    	messageLayout.m_leftOptionAlignmentProperty = getAlignmentProperty( Properties.PROPERTY_COLUMN_LEFT_OPTION_ALIGNMENT );
    	messageLayout.m_leftCentreTextAlignmentProperty = getAlignmentProperty( Properties.PROPERTY_COLUMN_LEFT_CENTRE_TEXT_ALIGNMENT );
    	messageLayout.m_centreOptionAlignmentProperty = getAlignmentProperty( Properties.PROPERTY_COLUMN_CENTRE_OPTION_ALIGNMENT );
    	messageLayout.m_rightCentreTextAlignmentProperty = getAlignmentProperty( Properties.PROPERTY_COLUMN_RIGHT_CENTRE_TEXT_ALIGNMENT );
    	messageLayout.m_rightOptionAlignmentProperty = getAlignmentProperty( Properties.PROPERTY_COLUMN_RIGHT_OPTION_ALIGNMENT );
    	messageLayout.m_rightTextAlignmentProperty = getAlignmentProperty( Properties.PROPERTY_COLUMN_RIGHT_TEXT_ALIGNMENT );

    	final String[] layoutOptions = new String[] { LAYOUT_OPTION_DIFFERENT_DAY_INDICATOR, LAYOUT_OPTION_TIME, LAYOUT_OPTION_TIME_ZONE, LAYOUT_OPTION_NONE };

    	messageLayout.m_layoutLabel = new JLabel( "Layout Components: " );

    	messageLayout.m_leftTextLayout = new JTextField( Properties.getInstance().getProperty( Properties.PROPERTY_LAYOUT_LEFT_TEXT, Properties.PROPERTY_LAYOUT_LEFT_TEXT_DEFAULT, false ) );
    	messageLayout.m_leftTextLayout.setColumns( NUMBER_OF_COLUMNS );

    	messageLayout.m_leftOptionLayout = new JComboBox( layoutOptions );
    	messageLayout.m_leftOptionLayout.setSelectedItem( messageLayout.m_leftOption ) ;

    	messageLayout.m_leftCentreTextLayout = new JTextField( Properties.getInstance().getProperty( Properties.PROPERTY_LAYOUT_LEFT_CENTRE_TEXT, Properties.PROPERTY_LAYOUT_LEFT_CENTRE_TEXT_DEFAULT, false ) );
    	messageLayout.m_leftCentreTextLayout.setColumns( NUMBER_OF_COLUMNS );

    	messageLayout.m_centreOptionLayout = new JComboBox( layoutOptions );
    	messageLayout.m_centreOptionLayout.setSelectedItem( messageLayout.m_centreOption ) ;

    	messageLayout.m_rightCentreTextLayout = new JTextField( Properties.getInstance().getProperty( Properties.PROPERTY_LAYOUT_RIGHT_CENTRE_TEXT, Properties.PROPERTY_LAYOUT_RIGHT_CENTRE_TEXT_DEFAULT, false ) );
    	messageLayout.m_rightCentreTextLayout.setColumns( NUMBER_OF_COLUMNS );

    	messageLayout.m_rightOptionLayout = new JComboBox( layoutOptions );
    	messageLayout.m_rightOptionLayout.setSelectedItem( messageLayout.m_rightOption ) ;

    	messageLayout.m_rightTextLayout = new JTextField( Properties.getInstance().getProperty( Properties.PROPERTY_LAYOUT_RIGHT_TEXT, Properties.PROPERTY_LAYOUT_RIGHT_TEXT_DEFAULT, false ) );
    	messageLayout.m_rightTextLayout.setColumns( NUMBER_OF_COLUMNS );

    	messageLayout.m_layoutComponents = new Vector<JComponent>();
    	messageLayout.m_layoutComponents.add( messageLayout.m_leftTextLayout );
    	messageLayout.m_layoutComponents.add( messageLayout.m_leftOptionLayout );
    	messageLayout.m_layoutComponents.add( messageLayout.m_leftCentreTextLayout );
    	messageLayout.m_layoutComponents.add( messageLayout.m_centreOptionLayout );
    	messageLayout.m_layoutComponents.add( messageLayout.m_rightCentreTextLayout );
    	messageLayout.m_layoutComponents.add( messageLayout.m_rightOptionLayout );
    	messageLayout.m_layoutComponents.add( messageLayout.m_rightTextLayout );

    	messageLayout.m_columnBreaksLabel = new JLabel( "Column Breaks: " );

    	String separateColumsCheckboxToolTip = "<html>When checked, the adjoining layout components will be in separate columns.<br>When unchecked, the adjoining layout components will be in a single column.</html>";

    	messageLayout.m_leftTextLeftOptionBreak = new JCheckBox( "", messageLayout.m_leftTextAndLeftOptionAreSeparateOption ); //$NON-NLS-1$
    	messageLayout.m_leftTextLeftOptionBreak.addItemListener( messageLayout );
    	messageLayout.m_leftTextLeftOptionBreak.setToolTipText( separateColumsCheckboxToolTip );

    	messageLayout.m_leftOptionLeftCentreTextBreak = new JCheckBox( "", messageLayout.m_leftOptionAndLeftCentreTextAreSeparateOption ); //$NON-NLS-1$
    	messageLayout.m_leftOptionLeftCentreTextBreak.addItemListener( messageLayout );
    	messageLayout.m_leftOptionLeftCentreTextBreak.setToolTipText( separateColumsCheckboxToolTip );

		messageLayout.m_leftCentreTextCentreOptionBreak = new JCheckBox( "", messageLayout.m_leftCentreTextAndCentreOptionAreSeparateOption ); //$NON-NLS-1$
    	messageLayout.m_leftCentreTextCentreOptionBreak.addItemListener( messageLayout );
    	messageLayout.m_leftCentreTextCentreOptionBreak.setToolTipText( separateColumsCheckboxToolTip );

		messageLayout.m_centreOptionRightCentreTextBreak = new JCheckBox( "", messageLayout.m_centreOptionAndRightCentreTextAreSeparateOption ); //$NON-NLS-1$
    	messageLayout.m_centreOptionRightCentreTextBreak.addItemListener( messageLayout );
    	messageLayout.m_centreOptionRightCentreTextBreak.setToolTipText( separateColumsCheckboxToolTip );

    	messageLayout.m_rightCentreTextRightOptionBreak = new JCheckBox( "", messageLayout.m_rightCentreTextAndRightOptionAreSeparateOption ); //$NON-NLS-1$
    	messageLayout.m_rightCentreTextRightOptionBreak.addItemListener( messageLayout );
    	messageLayout.m_rightCentreTextRightOptionBreak.setToolTipText( separateColumsCheckboxToolTip );

    	messageLayout.m_rightOptionRightTextBreak = new JCheckBox( "", messageLayout.m_rightOptionAndRightTextAreSeparateOption ); //$NON-NLS-1$
    	messageLayout.m_rightOptionRightTextBreak.addItemListener( messageLayout );
    	messageLayout.m_rightOptionRightTextBreak.setToolTipText( separateColumsCheckboxToolTip );

    	messageLayout.m_columnCheckboxes = new Vector<JCheckBox>();
    	messageLayout.m_columnCheckboxes.add( messageLayout.m_leftTextLeftOptionBreak );
    	messageLayout.m_columnCheckboxes.add( messageLayout.m_leftOptionLeftCentreTextBreak );
    	messageLayout.m_columnCheckboxes.add( messageLayout.m_leftCentreTextCentreOptionBreak );
    	messageLayout.m_columnCheckboxes.add( messageLayout.m_centreOptionRightCentreTextBreak );
    	messageLayout.m_columnCheckboxes.add( messageLayout.m_rightCentreTextRightOptionBreak );
    	messageLayout.m_columnCheckboxes.add( messageLayout.m_rightOptionRightTextBreak );

    	final String[] alignmentOptions = new String[] { ALIGNMENT_LEFT, ALIGNMENT_CENTRE, ALIGNMENT_RIGHT };

    	messageLayout.m_columnAlignmentsLabel = new JLabel( "Column Alignments: " );

    	String columAlignmentComboToolTip = "<html>Specifies the alignment of text within the column (or merged column).</html>";

    	messageLayout.m_leftTextAlignment = new JComboBox( alignmentOptions );
    	messageLayout.m_leftTextAlignment.setSelectedItem( messageLayout.m_leftTextAlignmentProperty );
    	messageLayout.m_leftTextAlignment.setToolTipText( columAlignmentComboToolTip );

    	messageLayout.m_leftOptionAlignment = new JComboBox( alignmentOptions );
    	messageLayout.m_leftOptionAlignment.setToolTipText( columAlignmentComboToolTip );

    	messageLayout.m_leftCentreTextAlignment = new JComboBox( alignmentOptions );
    	messageLayout.m_leftCentreTextAlignment.setToolTipText( columAlignmentComboToolTip );

    	messageLayout.m_centreOptionAlignment = new JComboBox( alignmentOptions );
    	messageLayout.m_centreOptionAlignment.setToolTipText( columAlignmentComboToolTip );

    	messageLayout.m_rightCentreTextAlignment = new JComboBox( alignmentOptions );
    	messageLayout.m_rightCentreTextAlignment.setToolTipText( columAlignmentComboToolTip );

    	messageLayout.m_rightOptionAlignment = new JComboBox( alignmentOptions );
    	messageLayout.m_rightOptionAlignment.setToolTipText( columAlignmentComboToolTip );

    	messageLayout.m_rightTextAlignment = new JComboBox( alignmentOptions );
    	messageLayout.m_rightTextAlignment.setToolTipText( columAlignmentComboToolTip );

    	messageLayout.m_columnAlignmentCombos = new Vector<JComboBox>();
    	messageLayout.m_columnAlignmentCombos.add( messageLayout.m_leftTextAlignment );
    	messageLayout.m_columnAlignmentCombos.add( messageLayout.m_leftOptionAlignment );
    	messageLayout.m_columnAlignmentCombos.add( messageLayout.m_leftCentreTextAlignment );
    	messageLayout.m_columnAlignmentCombos.add( messageLayout.m_centreOptionAlignment );
    	messageLayout.m_columnAlignmentCombos.add( messageLayout.m_rightCentreTextAlignment );
    	messageLayout.m_columnAlignmentCombos.add( messageLayout.m_rightOptionAlignment );
    	messageLayout.m_columnAlignmentCombos.add( messageLayout.m_rightTextAlignment );

    	messageLayout.m_sample = new JButton( Messages.getString( "MessageLayout.0" ) ); //$NON-NLS-1$

    	messageLayout.m_sampleLabel = new JLabel( Message.getMessageString( new GregorianCalendar(), true ) );
    	messageLayout.m_sampleLabel.setHorizontalAlignment( SwingConstants.CENTER );
    	messageLayout.m_sampleLabel.setBorder
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

    	messageLayout.m_sample.addActionListener
    	(
			new ActionListener()
			{
				public void actionPerformed( ActionEvent actionEvent )
				{
					messageLayout.storeProperties();
		    		messageLayout.m_sampleLabel.setText( Message.getMessageString( new GregorianCalendar(), true ) );
				}
			}
		);

    	messageLayout.m_ok = new JButton( Messages.getString( "MessageLayout.1" ) ); //$NON-NLS-1$
    	messageLayout.m_ok.addActionListener
    	(
			new ActionListener()
			{
				public void actionPerformed( ActionEvent actionEvent )
				{
					messageLayout.storeProperties();
					messageLayout.dispose(); 
				} 
			} 
		);

    	messageLayout.m_cancel = new JButton( Messages.getString( "MessageLayout.2" ) ); //$NON-NLS-1$
    	messageLayout.m_cancel.addActionListener
    	(
			new ActionListener()
			{
				public void actionPerformed( ActionEvent actionEvent )
				{
					Properties.getInstance().setProperty( Properties.PROPERTY_LAYOUT_LEFT_TEXT, messageLayout.m_leftText );
					setLayoutProperty( Properties.PROPERTY_LAYOUT_LEFT_OPTION, messageLayout.m_leftOption );
		    		Properties.getInstance().setProperty( Properties.PROPERTY_LAYOUT_LEFT_CENTRE_TEXT, messageLayout.m_leftCentreText );
		    		setLayoutProperty( Properties.PROPERTY_LAYOUT_CENTRE_OPTION, messageLayout.m_centreOption );
		    		Properties.getInstance().setProperty( Properties.PROPERTY_LAYOUT_RIGHT_CENTRE_TEXT, messageLayout.m_rightCentreText );
		    		setLayoutProperty( Properties.PROPERTY_LAYOUT_RIGHT_OPTION, messageLayout.m_rightOption );
		    		Properties.getInstance().setProperty( Properties.PROPERTY_LAYOUT_RIGHT_TEXT, messageLayout.m_rightText );

		    		Properties.getInstance().setProperty( Properties.PROPERTY_COLUMNS_LEFT_TEXT_AND_LEFT_OPTION_ARE_SEPARATE, Boolean.toString( messageLayout.m_leftTextAndLeftOptionAreSeparateOption ) );
		    		Properties.getInstance().setProperty( Properties.PROPERTY_COLUMNS_LEFT_OPTION_AND_LEFT_CENTRE_TEXT_ARE_SEPARATE, Boolean.toString( messageLayout.m_leftOptionAndLeftCentreTextAreSeparateOption ) );
		    		Properties.getInstance().setProperty( Properties.PROPERTY_COLUMNS_LEFT_CENTRE_TEXT_AND_CENTRE_OPTION_ARE_SEPARATE, Boolean.toString( messageLayout.m_leftCentreTextAndCentreOptionAreSeparateOption ) );
		    		Properties.getInstance().setProperty( Properties.PROPERTY_COLUMNS_CENTRE_OPTION_AND_RIGHT_CENTRE_TEXT_ARE_SEPARATE, Boolean.toString( messageLayout.m_centreOptionAndRightCentreTextAreSeparateOption ) );
		    		Properties.getInstance().setProperty( Properties.PROPERTY_COLUMNS_RIGHT_CENTRE_TEXT_AND_RIGHT_OPTION_ARE_SEPARATE, Boolean.toString( messageLayout.m_rightCentreTextAndRightOptionAreSeparateOption ) );
		    		Properties.getInstance().setProperty( Properties.PROPERTY_COLUMNS_RIGHT_OPTION_AND_RIGHT_TEXT_ARE_SEPARATE, Boolean.toString( messageLayout.m_rightOptionAndRightTextAreSeparateOption ) );

		    		setAlignmentProperty( Properties.PROPERTY_COLUMN_LEFT_TEXT_ALIGNMENT, messageLayout.m_leftTextAlignmentProperty );
		    		setAlignmentProperty( Properties.PROPERTY_COLUMN_LEFT_OPTION_ALIGNMENT, messageLayout.m_leftOptionAlignmentProperty );
		    		setAlignmentProperty( Properties.PROPERTY_COLUMN_LEFT_CENTRE_TEXT_ALIGNMENT, messageLayout.m_leftCentreTextAlignmentProperty );
		    		setAlignmentProperty( Properties.PROPERTY_COLUMN_CENTRE_OPTION_ALIGNMENT, messageLayout.m_centreOptionAlignmentProperty );
		    		setAlignmentProperty( Properties.PROPERTY_COLUMN_RIGHT_CENTRE_TEXT_ALIGNMENT, messageLayout.m_rightCentreTextAlignmentProperty );
		    		setAlignmentProperty( Properties.PROPERTY_COLUMN_RIGHT_OPTION_ALIGNMENT, messageLayout.m_rightOptionAlignmentProperty );
		    		setAlignmentProperty( Properties.PROPERTY_COLUMN_RIGHT_TEXT_ALIGNMENT, messageLayout.m_rightTextAlignmentProperty );

		    		Properties.getInstance().store();

					messageLayout.dispose(); 
				} 
			} 
		);

    	messageLayout.setTitle( Messages.getString( "MessageLayout.3" ) );  //$NON-NLS-1$
        messageLayout.setIconImage( TrayIcon.getApplicationIconImage() );
        messageLayout.itemStateChanged( null );
        messageLayout.pack();
        messageLayout.setLocationRelativeTo( null );
        messageLayout.setModalityType( ModalityType.APPLICATION_MODAL );
        messageLayout.addComponentListener( new ComponentListener( messageLayout, false, true ) );
        messageLayout.setVisible( true );

        return messageLayout;
    }


    private static String getLayoutProperty( String propertyName, String defaultValue )
    {
    	String value = Properties.getInstance().getProperty( propertyName, defaultValue, true );

    	if( Properties.PROPERTY_LAYOUT_OPTION_DIFFERENT_DAY_INDICATOR.equals( value ) )
    		return LAYOUT_OPTION_DIFFERENT_DAY_INDICATOR;

    	if( Properties.PROPERTY_LAYOUT_OPTION_TIME.equals( value ) )
    		return LAYOUT_OPTION_TIME;

    	if( Properties.PROPERTY_LAYOUT_OPTION_TIME_ZONE.equals( value ) )
    		return LAYOUT_OPTION_TIME_ZONE;

    	if( Properties.PROPERTY_LAYOUT_OPTION_NONE.equals( value ) )
    		return LAYOUT_OPTION_NONE;

    	return defaultValue;
    }


    protected static void setLayoutProperty( String propertyName, String value )
    {
    	if( value.equals( LAYOUT_OPTION_DIFFERENT_DAY_INDICATOR ) )
    		Properties.getInstance().setProperty( propertyName, Properties.PROPERTY_LAYOUT_OPTION_DIFFERENT_DAY_INDICATOR );

    	if( value.equals( LAYOUT_OPTION_NONE ) )
    		Properties.getInstance().setProperty( propertyName, Properties.PROPERTY_LAYOUT_OPTION_NONE );

    	if( value.equals( LAYOUT_OPTION_TIME ) )
    		Properties.getInstance().setProperty( propertyName, Properties.PROPERTY_LAYOUT_OPTION_TIME );

    	if( value.equals( LAYOUT_OPTION_TIME_ZONE ) )
    		Properties.getInstance().setProperty( propertyName, Properties.PROPERTY_LAYOUT_OPTION_TIME_ZONE );
    }


    private static String getAlignmentProperty( String propertyName )
    {
    	String value = Properties.getInstance().getProperty( propertyName, Properties.PROPERTY_COLUMN_ALIGNMENT_LEFT, false );
    	if( Properties.PROPERTY_COLUMN_ALIGNMENT_CENTRE.equals( value ) )
    		return ALIGNMENT_CENTRE;

    	if( Properties.PROPERTY_COLUMN_ALIGNMENT_LEFT.equals( value )  )
    		return ALIGNMENT_LEFT;

    	if( Properties.PROPERTY_COLUMN_ALIGNMENT_RIGHT.equals( value ) )
    		return ALIGNMENT_RIGHT;

    	return ALIGNMENT_LEFT;
    }


    protected static void setAlignmentProperty( String propertyName, String value )
    {
    	if( value.equals( ALIGNMENT_CENTRE ) )
    		Properties.getInstance().setProperty( propertyName, Properties.PROPERTY_COLUMN_ALIGNMENT_CENTRE );

    	if( value.equals( ALIGNMENT_LEFT ) )
    		Properties.getInstance().setProperty( propertyName, Properties.PROPERTY_COLUMN_ALIGNMENT_LEFT );

    	if( value.equals( ALIGNMENT_RIGHT ) )
    		Properties.getInstance().setProperty( propertyName, Properties.PROPERTY_COLUMN_ALIGNMENT_RIGHT );
    }


    protected void storeProperties()
    {
		Properties.getInstance().setProperty( Properties.PROPERTY_LAYOUT_LEFT_TEXT, m_leftTextLayout.getText() );
		setLayoutProperty( Properties.PROPERTY_LAYOUT_LEFT_OPTION, m_leftOptionLayout.getSelectedItem().toString() );
		Properties.getInstance().setProperty( Properties.PROPERTY_LAYOUT_LEFT_CENTRE_TEXT, m_leftCentreTextLayout.getText() );
		setLayoutProperty( Properties.PROPERTY_LAYOUT_CENTRE_OPTION, m_centreOptionLayout.getSelectedItem().toString() );
		Properties.getInstance().setProperty( Properties.PROPERTY_LAYOUT_RIGHT_CENTRE_TEXT, m_rightCentreTextLayout.getText() );
		setLayoutProperty( Properties.PROPERTY_LAYOUT_RIGHT_OPTION, m_rightOptionLayout.getSelectedItem().toString() );
		Properties.getInstance().setProperty( Properties.PROPERTY_LAYOUT_RIGHT_TEXT, m_rightTextLayout.getText() );

		Properties.getInstance().setProperty( Properties.PROPERTY_COLUMNS_LEFT_TEXT_AND_LEFT_OPTION_ARE_SEPARATE, Boolean.toString( m_leftTextLeftOptionBreak.isSelected() ) );
		Properties.getInstance().setProperty( Properties.PROPERTY_COLUMNS_LEFT_OPTION_AND_LEFT_CENTRE_TEXT_ARE_SEPARATE, Boolean.toString( m_leftOptionLeftCentreTextBreak.isSelected() ) );
		Properties.getInstance().setProperty( Properties.PROPERTY_COLUMNS_LEFT_CENTRE_TEXT_AND_CENTRE_OPTION_ARE_SEPARATE, Boolean.toString( m_leftCentreTextCentreOptionBreak.isSelected() ) );
		Properties.getInstance().setProperty( Properties.PROPERTY_COLUMNS_CENTRE_OPTION_AND_RIGHT_CENTRE_TEXT_ARE_SEPARATE, Boolean.toString( m_centreOptionRightCentreTextBreak.isSelected() ) );
		Properties.getInstance().setProperty( Properties.PROPERTY_COLUMNS_RIGHT_CENTRE_TEXT_AND_RIGHT_OPTION_ARE_SEPARATE, Boolean.toString( m_rightCentreTextRightOptionBreak.isSelected() ) );
		Properties.getInstance().setProperty( Properties.PROPERTY_COLUMNS_RIGHT_OPTION_AND_RIGHT_TEXT_ARE_SEPARATE, Boolean.toString( m_rightOptionRightTextBreak.isSelected() ) );

		setAlignmentProperty( Properties.PROPERTY_COLUMN_LEFT_TEXT_ALIGNMENT, m_leftTextAlignment.getSelectedItem().toString() );
		setAlignmentProperty( Properties.PROPERTY_COLUMN_LEFT_OPTION_ALIGNMENT, m_leftOptionAlignment.getSelectedItem().toString() );
		setAlignmentProperty( Properties.PROPERTY_COLUMN_LEFT_CENTRE_TEXT_ALIGNMENT, m_leftCentreTextAlignment.getSelectedItem().toString() );
		setAlignmentProperty( Properties.PROPERTY_COLUMN_CENTRE_OPTION_ALIGNMENT, m_centreOptionAlignment.getSelectedItem().toString() );
		setAlignmentProperty( Properties.PROPERTY_COLUMN_RIGHT_CENTRE_TEXT_ALIGNMENT, m_rightCentreTextAlignment.getSelectedItem().toString() );
		setAlignmentProperty( Properties.PROPERTY_COLUMN_RIGHT_OPTION_ALIGNMENT, m_rightOptionAlignment.getSelectedItem().toString() );
		setAlignmentProperty( Properties.PROPERTY_COLUMN_RIGHT_TEXT_ALIGNMENT, m_rightTextAlignment.getSelectedItem().toString() );

		Properties.getInstance().store();
    }


    public void itemStateChanged( ItemEvent itemEvent )
    {
    	getContentPane().removeAll();
    	GroupLayout layout = new GroupLayout( getContentPane() );
        getContentPane().setLayout( layout );
        layout.setAutoCreateGaps( true );
        layout.setAutoCreateContainerGaps( true );

        layout.linkSize( SwingConstants.HORIZONTAL, m_ok, m_cancel, m_sample );

        SequentialGroup sequentialGroup = null;
    	ParallelGroup parallelGroup = null;

    	// Horizontal group.
    	sequentialGroup =
    		layout.createSequentialGroup()
				.addGroup
				(
	    			layout.createParallelGroup()
	    				.addComponent( m_layoutLabel )
	    				.addComponent( m_columnBreaksLabel )
	    				.addComponent( m_columnAlignmentsLabel )
				);

    	int columnIndex = -1;
    	for( int i = 0; i < m_columnCheckboxes.size(); i++ )
    	{
    		if( m_columnCheckboxes.get( i ).isSelected() )
    		{
    			if( columnIndex == -1 )
    			{
    				// Current column is not merged with any preceding columns.
    				sequentialGroup
    					.addGroup
	    				(
	    	    			layout.createParallelGroup( Alignment.CENTER )
	    						.addComponent( m_layoutComponents.get( i ) )
	    						.addComponent( m_columnAlignmentCombos.get( i ) )
	    				)
	    				.addPreferredGap( ComponentPlacement.UNRELATED )
	    				.addComponent( m_columnCheckboxes.get( i ) )
    					.addPreferredGap( ComponentPlacement.UNRELATED );
    			}
    			else
    			{
    				// There are preceding columns to be merged with the current column.
    				SequentialGroup newSequentialGroup = layout.createSequentialGroup();
    				for( int j = columnIndex; j < i; j++ )
    					newSequentialGroup.addComponent( m_layoutComponents.get( j ) ).addComponent( m_columnCheckboxes.get( j ) );

    				// Need to add the next component (after the current checkbox).
    				newSequentialGroup.addComponent( m_layoutComponents.get( i ) );

					sequentialGroup
    					.addGroup
    					(
    						layout.createParallelGroup()
    							.addGroup( newSequentialGroup )
    							.addComponent( m_columnAlignmentCombos.get( columnIndex ) )
    					)
	    				.addPreferredGap( ComponentPlacement.UNRELATED )
	    				.addComponent( m_columnCheckboxes.get( i ) )
						.addPreferredGap( ComponentPlacement.UNRELATED );

    				columnIndex = -1;
    			}
    		}
    		else
    		{
    			// The current column is being merged with the column to the right.
    			// However it's possible the column to the left is also being merged.
    			// So we need to remember which column is the "primary" column (the left most of this merged column).
    			if( columnIndex == -1 )
    				columnIndex = i;
    		}
    	}

    	// Handle the last textfield/combobox or when no checkboxes are checked.
    	if( columnIndex == -1 )
    	{
			sequentialGroup
				.addGroup
				(
	    			layout.createParallelGroup()
						.addComponent( m_layoutComponents.lastElement() )
						.addComponent( m_columnAlignmentCombos.lastElement() )
				);
    	}
    	else
    	{
			SequentialGroup newSequentialGroup = layout.createSequentialGroup();
			for( int i = columnIndex; i < m_columnCheckboxes.size(); i++ )
				newSequentialGroup.addComponent( m_layoutComponents.get( i ) ).addComponent( m_columnCheckboxes.get( i ) );

			// Need to add the next component (after the current checkbox).
			newSequentialGroup.addComponent( m_layoutComponents.lastElement() );

			sequentialGroup
				.addGroup
				(
					layout.createParallelGroup()
						.addGroup( newSequentialGroup )
						.addComponent( m_columnAlignmentCombos.get( columnIndex ) )
				);
    	}

    	parallelGroup = layout.createParallelGroup( Alignment.CENTER );
    	parallelGroup
    		.addGroup( sequentialGroup )
			.addGroup
			(
    			layout.createSequentialGroup()
					.addComponent( m_sample )
					.addPreferredGap( ComponentPlacement.UNRELATED )
					.addComponent( m_sampleLabel )
			)
			.addGroup
			(
    			layout.createSequentialGroup()
    				.addComponent( m_ok )
    				.addPreferredGap( ComponentPlacement.UNRELATED )
    				.addComponent( m_cancel )
			);

    	layout.setHorizontalGroup( parallelGroup );

    	// Vertical group.
    	sequentialGroup = layout.createSequentialGroup();
    	sequentialGroup
    		.addGroup
    		(
				layout.createParallelGroup( Alignment.BASELINE )
    				.addComponent( m_layoutLabel )
    				.addComponent( m_leftTextLayout )
    				.addComponent( m_leftOptionLayout )
    				.addComponent( m_leftCentreTextLayout )
    				.addComponent( m_centreOptionLayout )
    				.addComponent( m_rightCentreTextLayout )
    				.addComponent( m_rightOptionLayout )
    				.addComponent( m_rightTextLayout )
    				
    		)
    		.addGroup
    		(
				layout.createParallelGroup( Alignment.BASELINE )
					.addComponent( m_columnBreaksLabel )
					.addComponent( m_leftTextLeftOptionBreak )
					.addComponent( m_leftOptionLeftCentreTextBreak )
					.addComponent( m_leftCentreTextCentreOptionBreak )
					.addComponent( m_centreOptionRightCentreTextBreak )
					.addComponent( m_rightCentreTextRightOptionBreak )
					.addComponent( m_rightOptionRightTextBreak )
    		);

    	// Dynamically build the alignment comboboxes based on the selections of each checkbox.
    	parallelGroup = layout.createParallelGroup( Alignment.BASELINE );
    	parallelGroup.addComponent( m_columnAlignmentsLabel );
    	JComboBox currentCombobox = null;
    	for( int i = 0; i < m_columnCheckboxes.size(); i++ )
    	{
    		if( m_columnCheckboxes.get( i ).isSelected() )
    		{
    			if( currentCombobox == null )
    			{
    				parallelGroup.addComponent( m_columnAlignmentCombos.get( i ) );
    			}
    			else
    			{
        			parallelGroup.addComponent( currentCombobox );
        			currentCombobox = null;
    			}
    		}
    		else
    		{
    			// The current column is being merged into the column to the right.
    			// However it's possible the column to the left is also being merged into the current column (etc).
    			// Need to remember which column is the "primary" column (or the left most of this merged column).
    			if( currentCombobox == null )
    				currentCombobox = m_columnAlignmentCombos.get( i );
    		}
    	}

    	// Handle the last alignment combobox.
    	if( currentCombobox == null )
    		parallelGroup.addComponent( m_columnAlignmentCombos.lastElement() );
    	else
    		parallelGroup.addComponent( currentCombobox );

    	// Finish off the remainder of the dialog.
    	sequentialGroup
			.addGroup( parallelGroup )
			.addPreferredGap( ComponentPlacement.UNRELATED )
			.addGroup
			(
				layout.createParallelGroup( Alignment.CENTER )
					.addComponent( m_sample )
					.addComponent( m_sampleLabel )
			)
			.addPreferredGap( ComponentPlacement.UNRELATED )
			.addGroup
			(
				layout.createParallelGroup()
					.addComponent( m_ok )
					.addComponent( m_cancel )
			);

		layout.setVerticalGroup( sequentialGroup );        
    }    
}
