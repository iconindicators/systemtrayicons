package worldtimesystemtrayicon.gui;


import java.awt.Image;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;

import basesystemtrayicon.Messages;
import basesystemtrayicon.gui.Utils;
import worldtimesystemtrayicon.Properties;


public class DialogMessageLayout
extends JDialog
implements ActionListener, KeyListener
{
	private static final long serialVersionUID = 1L;

	private static final String LAYOUT_DIFFERENT_DAY_INDICATOR =
        Messages.getString( "DialogMessageLayout.4" );

	private static final String LAYOUT_DATE_TIME =
        Messages.getString( "DialogMessageLayout.5" );

	private static final String LAYOUT_TIME_ZONE =
        Messages.getString( "DialogMessageLayout.6" );

    private static final String LAYOUT_NONE =
        Messages.getString( "DialogMessageLayout.7" );

	private JTextField
	    m_layoutTextLeft,
	    m_layoutTextLeftCentre,
	    m_layoutTextRightCentre,
	    m_layoutTextRight;

	private JComboBox<String>
	    m_layoutComboleft,
	    m_layoutComboCentre,
	    m_layoutComboRight;

	private String
	    m_backupTextLeft,
	    m_backupComboLeft,
	    m_backupTextLeftCentre,
	    m_backupComboCentre,
	    m_backupTextRightCentre,
	    m_backupComboRight,
	    m_backupTextRight;

    private JLabel m_sampleLabel;

    private JButton
        m_ok,
        m_cancel;
    
    private PopupMenu m_popupMenu;


	public DialogMessageLayout(
        Image applicationIconImage,
        PopupMenu popupMenu )
    {
        super( (JDialog)null );

        m_popupMenu = popupMenu;

        Utils.initialiseDialog(
            this,
            Messages.getString( "DialogMessageLayout.3" ),
            applicationIconImage );

        backupProperties();

    	final String[] layoutOptions =
	        new String[] {
                LAYOUT_DIFFERENT_DAY_INDICATOR,
                LAYOUT_NONE,
                LAYOUT_DATE_TIME,
                LAYOUT_TIME_ZONE };

    	m_layoutTextLeft =
	        new JTextField(
                Properties.getProperty(
                    Properties.KEY_LAYOUT_TEXT_LEFT,
                    Properties.VALUE_LAYOUT_TEXT_LEFT_DEFAULT ) );

    	m_layoutTextLeft.setToolTipText(
	        Messages.getString( "DialogMessageLayout.12" ) );

    	m_layoutTextLeft.setColumns( Utils.NUMBER_OF_COLUMNS );
    	m_layoutTextLeft.addKeyListener( this );

    	m_layoutComboleft = new JComboBox<>( layoutOptions );
    	m_layoutComboleft.setSelectedItem(
	        convertLayoutPropertyValueToLocale(
                Properties.KEY_LAYOUT_COMBO_LEFT,
                Properties.VALUE_LAYOUT_COMBO_TIME_ZONE ) );

    	m_layoutComboleft.addActionListener( this );

    	m_layoutTextLeftCentre =
	        new JTextField(
                Properties.getProperty(
                    Properties.KEY_LAYOUT_TEXT_LEFT_CENTRE,
                    Properties.VALUE_LAYOUT_TEXT_LEFT_CENTRE_DEFAULT ) );

        m_layoutTextLeftCentre.setToolTipText(
            Messages.getString( "DialogMessageLayout.13" ) );

    	m_layoutTextLeftCentre.setColumns( Utils.NUMBER_OF_COLUMNS );
    	m_layoutTextLeftCentre.addKeyListener( this );

    	m_layoutComboCentre = new JComboBox<>( layoutOptions );
    	m_layoutComboCentre.setSelectedItem(
            convertLayoutPropertyValueToLocale(
                Properties.KEY_LAYOUT_COMBO_CENTRE,
                Properties.VALUE_LAYOUT_COMBO_DIFFERENT_DAY_INDICATOR ) );

        m_layoutComboCentre.addActionListener( this );

    	m_layoutTextRightCentre =
	        new JTextField(
                Properties.getProperty(
                    Properties.KEY_LAYOUT_TEXT_RIGHT_CENTRE,
                    Properties.VALUE_LAYOUT_TEXT_RIGHT_CENTRE_DEFAULT ) );

        m_layoutTextRightCentre.setToolTipText(
            Messages.getString( "DialogMessageLayout.14" ) );

    	m_layoutTextRightCentre.setColumns( Utils.NUMBER_OF_COLUMNS );
    	m_layoutTextRightCentre.addKeyListener( this );

    	m_layoutComboRight = new JComboBox<>( layoutOptions );
    	m_layoutComboRight.setSelectedItem(
            convertLayoutPropertyValueToLocale(
                Properties.KEY_LAYOUT_COMBO_RIGHT,
                Properties.VALUE_LAYOUT_COMBO_DATE_TIME ) );

    	m_layoutComboRight.addActionListener( this );

    	m_layoutTextRight =
	        new JTextField(
                Properties.getProperty(
                    Properties.KEY_LAYOUT_TEXT_RIGHT,
                    Properties.VALUE_LAYOUT_TEXT_RIGHT_DEFAULT ) );

        m_layoutTextRight.setToolTipText(
            Messages.getString( "DialogMessageLayout.15" ) );

    	m_layoutTextRight.setColumns( Utils.NUMBER_OF_COLUMNS );
    	m_layoutTextRight.addKeyListener( this );

    	m_sampleLabel = new JLabel();
    	m_sampleLabel.setBorder( Utils.createBorder() );

    	m_ok = new JButton( Messages.getString( "DialogMessageLayout.1" ) );
    	m_ok.addActionListener( this );

    	m_cancel = new JButton( Messages.getString( "DialogMessageLayout.2" ) );
    	m_cancel.addActionListener( this );

        GroupLayout layout = new GroupLayout( getContentPane() );
        getContentPane().setLayout( layout );
        layout.setAutoCreateGaps( true );
        layout.setAutoCreateContainerGaps( true );
        layout.linkSize( SwingConstants.HORIZONTAL, m_ok, m_cancel );

        layout.setHorizontalGroup(
            layout.createParallelGroup()
                .addGroup(
                    layout.createSequentialGroup()
                        .addComponent( m_layoutTextLeft )
                        .addComponent( m_layoutComboleft )
                        .addComponent( m_layoutTextLeftCentre )
                        .addComponent( m_layoutComboCentre )
                        .addComponent( m_layoutTextRightCentre )
                        .addComponent( m_layoutComboRight )
                        .addComponent( m_layoutTextRight ) )
                .addComponent(
                    m_sampleLabel,
                    Alignment.CENTER,
                    GroupLayout.PREFERRED_SIZE,
                    GroupLayout.DEFAULT_SIZE,
                    GroupLayout.PREFERRED_SIZE )
                .addGroup(
                    Alignment.CENTER,
                    layout.createSequentialGroup()
                        .addComponent( m_ok )
                        .addPreferredGap( ComponentPlacement.UNRELATED )
                        .addComponent( m_cancel ) ) );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGroup(
                    layout.createParallelGroup( Alignment.BASELINE )
                        .addComponent( m_layoutTextLeft )
                        .addComponent( m_layoutComboleft )
                        .addComponent( m_layoutTextLeftCentre )
                        .addComponent( m_layoutComboCentre )
                        .addComponent( m_layoutTextRightCentre )
                        .addComponent( m_layoutComboRight )
                        .addComponent( m_layoutTextRight ) )
                .addPreferredGap( ComponentPlacement.UNRELATED )
                .addComponent( m_sampleLabel )
                .addPreferredGap( ComponentPlacement.UNRELATED )
                .addGroup(
                    layout.createParallelGroup()
                        .addComponent( m_ok )
                        .addComponent( m_cancel ) ) );

        updateSampleText();

        Utils.postDialog( this );
    }


	@Override
	public void actionPerformed( ActionEvent actionEvent )
    {
	    boolean isLayoutCombobox =
            actionEvent.getSource() == m_layoutComboleft
            ||
            actionEvent.getSource() == m_layoutComboCentre
            ||
            actionEvent.getSource() == m_layoutComboRight;

	    if( actionEvent.getSource() == m_ok )
        {
            setProperties();
            Properties.store();
            dispose();
        }
        else if( actionEvent.getSource() == m_cancel )
        {
            revertProperties();
            dispose(); 
        }
        else if( isLayoutCombobox )
            updateSampleText();
    }


    @Override
    public void keyReleased( KeyEvent keyEvent )
    {
        updateSampleText();
    }


	@Override
	public void keyTyped( KeyEvent keyEvent )
    {
        /** Do nothing. */
    }


    @Override
    public void keyPressed( KeyEvent keyEvent )
    {
        /** Do nothing. */
    }


    private void updateSampleText()
    {
        setProperties();

        m_sampleLabel.setText(
            PopupMenu.toHTML(
                m_popupMenu.getMessage() ) );

        pack();
    }


    /**
     * Set the original (unmodified) layout properties.
     */
	protected void revertProperties()
    {
        Properties.setProperty(
            Properties.KEY_LAYOUT_TEXT_LEFT,
            m_backupTextLeft );

        Properties.setProperty(
            Properties.KEY_LAYOUT_COMBO_LEFT,
            m_backupComboLeft );

        Properties.setProperty(
            Properties.KEY_LAYOUT_TEXT_LEFT_CENTRE,
            m_backupTextLeftCentre );

        Properties.setProperty(
            Properties.KEY_LAYOUT_COMBO_CENTRE,
            m_backupComboCentre );

        Properties.setProperty(
            Properties.KEY_LAYOUT_TEXT_RIGHT_CENTRE,
            m_backupTextRightCentre );

        Properties.setProperty(
            Properties.KEY_LAYOUT_COMBO_RIGHT,
            m_backupComboRight );

        Properties.setProperty(
            Properties.KEY_LAYOUT_TEXT_RIGHT,
            m_backupTextRight );
    }


	/**
	 * When the message is displayed, the properties are read.
	 * 
	 * So as the user adjusts layout options, those new (temporary) values
	 * must be saved to the properties for the message to reflect those changes.
	 * 
	 * If the user cancels however, the original properties must be restored.
	 */
    protected void backupProperties()
    {
        m_backupTextLeft =
            Properties.getProperty(
                Properties.KEY_LAYOUT_TEXT_LEFT,
                Properties.VALUE_LAYOUT_TEXT_LEFT_DEFAULT );

        m_backupComboLeft =
            Properties.getProperty(
                Properties.KEY_LAYOUT_COMBO_LEFT,
                Properties.VALUE_LAYOUT_COMBO_TIME_ZONE );

        m_backupTextLeftCentre =
            Properties.getProperty(
                Properties.KEY_LAYOUT_TEXT_LEFT_CENTRE,
                Properties.VALUE_LAYOUT_TEXT_LEFT_CENTRE_DEFAULT );

        m_backupComboCentre =
            Properties.getProperty(
                    Properties.KEY_LAYOUT_COMBO_CENTRE,
                    Properties.VALUE_LAYOUT_COMBO_DIFFERENT_DAY_INDICATOR );

        m_backupTextRightCentre =
            Properties.getProperty(
                Properties.KEY_LAYOUT_TEXT_RIGHT_CENTRE,
                Properties.VALUE_LAYOUT_TEXT_RIGHT_CENTRE_DEFAULT );

        m_backupComboRight =
            Properties.getProperty(
                    Properties.KEY_LAYOUT_COMBO_RIGHT,
                    Properties.VALUE_LAYOUT_COMBO_DATE_TIME );

        m_backupTextRight =
            Properties.getProperty(
                Properties.KEY_LAYOUT_TEXT_RIGHT,
                Properties.VALUE_LAYOUT_TEXT_RIGHT_DEFAULT );
    }


    protected void setProperties()
    {
        Properties.setProperty(
            Properties.KEY_LAYOUT_TEXT_LEFT,
            m_layoutTextLeft.getText() );

        setConvertedLayoutLocaleToProperty(
            Properties.KEY_LAYOUT_COMBO_LEFT,
            m_layoutComboleft.getSelectedItem().toString() );

        Properties.setProperty(
            Properties.KEY_LAYOUT_TEXT_LEFT_CENTRE,
            m_layoutTextLeftCentre.getText() );

        setConvertedLayoutLocaleToProperty(
            Properties.KEY_LAYOUT_COMBO_CENTRE,
            m_layoutComboCentre.getSelectedItem().toString() );

        Properties.setProperty(
            Properties.KEY_LAYOUT_TEXT_RIGHT_CENTRE,
            m_layoutTextRightCentre.getText() );

        setConvertedLayoutLocaleToProperty(
            Properties.KEY_LAYOUT_COMBO_RIGHT,
            m_layoutComboRight.getSelectedItem().toString() );

        Properties.setProperty(
            Properties.KEY_LAYOUT_TEXT_RIGHT,
            m_layoutTextRight.getText() );
    }


    private static String
    convertLayoutPropertyValueToLocale(
        String propertyName,
        String defaultValue )
    {
        String value = Properties.getProperty( propertyName, defaultValue );
        String valueConverted;

        if( Properties.VALUE_LAYOUT_COMBO_DATE_TIME.equals( value ) )
            valueConverted = LAYOUT_DATE_TIME;

        else if( Properties.VALUE_LAYOUT_COMBO_DIFFERENT_DAY_INDICATOR.equals( value ) )
            valueConverted = LAYOUT_DIFFERENT_DAY_INDICATOR;

        else if( Properties.VALUE_LAYOUT_COMBO_NONE.equals( value ) )
            valueConverted = LAYOUT_NONE;

        else if( Properties.VALUE_LAYOUT_COMBO_TIME_ZONE.equals( value ) )
            valueConverted = LAYOUT_TIME_ZONE;

        else
        {
            System.err.println( "Unknown layout value: " + value );
            valueConverted = LAYOUT_DATE_TIME; // Arbitrary default.
        }

        return valueConverted;
    }


    private static void
    setConvertedLayoutLocaleToProperty(
        String propertyName,
        String value )
    {
        if( value.equals( LAYOUT_DATE_TIME ) )
            Properties.setProperty(
                propertyName,
                Properties.VALUE_LAYOUT_COMBO_DATE_TIME );

        else if( value.equals( LAYOUT_DIFFERENT_DAY_INDICATOR ) )
            Properties.setProperty(
                propertyName,
                Properties.VALUE_LAYOUT_COMBO_DIFFERENT_DAY_INDICATOR );

        else if( value.equals( LAYOUT_NONE ) )
            Properties.setProperty(
                propertyName,
                Properties.VALUE_LAYOUT_COMBO_NONE );

        else if( value.equals( LAYOUT_TIME_ZONE  ) )
            Properties.setProperty(
                propertyName,
                Properties.VALUE_LAYOUT_COMBO_TIME_ZONE );

        else
            throw new IllegalStateException(); // Should never happen!
    }
}
