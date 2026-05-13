package stardatesystemtrayicon.gui;


import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;

import basesystemtrayicon.Messages;
import basesystemtrayicon.SystemStart;
import basesystemtrayicon.gui.DialogSettingsBase;
import basesystemtrayicon.gui.Utils;
import stardatesystemtrayicon.Properties;


public class DialogSettings extends DialogSettingsBase
{
    private static final long serialVersionUID = 1L;

    private JLabel m_dateFormatUserDefinedLabel;
    private JComboBox<String> m_dateFormatCombobox;
    private JTextField m_dateFormatUserDefinedTextfield;

    private JCheckBox
        m_classicCheckbox,
        m_showIssueCheckbox,
        m_padCheckbox;


    public DialogSettings( Image applicationIconImage )
    {
        super( applicationIconImage );
    }


    /** Required to satisfy the "this-escape" warning introduced in JDK 21. */
    @Override
    public void initialise()
    {
    	super.initialise();

        JLabel dateFormatLabel =
            new JLabel( Messages.getString( "DialogSettings.6" ) );

        m_dateFormatCombobox =
            new JComboBox<>(
                Messages.getDateTimeFormatsInLocale( true, false, true ) );

        m_dateFormatCombobox.addActionListener( this );

        m_dateFormatUserDefinedLabel =
            new JLabel( Messages.getString( "DialogSettings.28" ) );

        m_dateFormatUserDefinedTextfield =
            new JTextField(
                Properties.getProperty(
                    Properties.VALUE_USER_DEFINED,
                    Properties.VALUE_USER_DEFINED_DATE_DEFAULT ) );

        m_dateFormatUserDefinedTextfield.setColumns( Utils.NUMBER_OF_COLUMNS );

        m_dateFormatUserDefinedTextfield.setToolTipText(
            Messages.getString( "DialogSettings.29" ) );

        boolean showStardateClassic =
            Properties.getPropertyBoolean(
                Properties.KEY_CLASSIC,
                Properties.VALUE_CLASSIC_DEFAULT );

        m_classicCheckbox =
            new JCheckBox( Messages.getString( "DialogSettings.0" ) );

        m_classicCheckbox.setToolTipText(
            Messages.getString( "DialogSettings.1" ) );

        m_classicCheckbox.setSelected( showStardateClassic );

        boolean showStardateIssue =
            Properties.getPropertyBoolean(
                Properties.KEY_ISSUE,
                Properties.VALUE_ISSUE_DEFAULT );

        m_showIssueCheckbox =
            new JCheckBox( Messages.getString( "DialogSettings.2" ) );

        m_showIssueCheckbox.setToolTipText(
            Messages.getString( "DialogSettings.3" ) );

        m_showIssueCheckbox.setSelected( showStardateIssue );
        m_showIssueCheckbox.setEnabled( showStardateClassic );

        boolean padStardate =
            Properties.getPropertyBoolean(
                Properties.KEY_PAD,
                Properties.VALUE_PAD_DEFAULT );

        m_padCheckbox =
            new JCheckBox( Messages.getString( "DialogSettings.4" ) );

        m_padCheckbox.setToolTipText(
            Messages.getString( "DialogSettings.5" ) );

        m_padCheckbox.setSelected( padStardate );
        m_padCheckbox.setEnabled( showStardateClassic );

        m_classicCheckbox.addItemListener( this );

        m_runOnStartup.setSelected( SystemStart.getRunOnSystemStart() );
        m_runOnStartup.addItemListener( this );

        GroupLayout layout = new GroupLayout( getContentPane() );
        getContentPane().setLayout( layout );
        layout.setAutoCreateGaps( true );
        layout.setAutoCreateContainerGaps( true );

        layout.linkSize( SwingConstants.HORIZONTAL, m_ok, m_cancel );

        layout.setHorizontalGroup(
            layout.createParallelGroup()
                .addGroup(
                    layout.createSequentialGroup()
                        .addComponent( dateFormatLabel )
                        .addComponent( m_dateFormatCombobox ) )
                .addGroup(
                    layout.createSequentialGroup()
                        .addPreferredGap(
                            dateFormatLabel,
                            m_dateFormatUserDefinedLabel,
                            ComponentPlacement.INDENT )
                        .addComponent( m_dateFormatUserDefinedLabel )
                        .addComponent( m_dateFormatUserDefinedTextfield ) )
                .addComponent( m_classicCheckbox )
                .addGroup(
                    layout.createSequentialGroup()
                        .addPreferredGap(
                            m_classicCheckbox,
                            m_showIssueCheckbox,
                            ComponentPlacement.INDENT )
                        .addComponent( m_showIssueCheckbox ) )
                .addGroup(
                    layout.createSequentialGroup()
                        .addPreferredGap(
                            m_classicCheckbox,
                            m_padCheckbox,
                            ComponentPlacement.INDENT )
                        .addComponent( m_padCheckbox ) )
                .addComponent( m_runOnStartup )
                .addGroup(
                    Alignment.CENTER,
                    layout.createSequentialGroup()
                        .addComponent( m_ok )
                        .addPreferredGap( ComponentPlacement.UNRELATED )
                        .addComponent( m_cancel ) )
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGroup(
                    layout.createParallelGroup( Alignment.BASELINE )
                        .addComponent( dateFormatLabel )
                        .addComponent( m_dateFormatCombobox ) )
                .addGroup(
                    layout.createParallelGroup( Alignment.BASELINE )
                        .addComponent( m_dateFormatUserDefinedLabel )
                        .addComponent( m_dateFormatUserDefinedTextfield ) )
                .addPreferredGap( ComponentPlacement.UNRELATED )
                .addComponent( m_classicCheckbox )
                .addComponent( m_showIssueCheckbox )
                .addComponent( m_padCheckbox )
                .addPreferredGap( ComponentPlacement.UNRELATED )
                .addComponent( m_runOnStartup )
                .addPreferredGap( ComponentPlacement.UNRELATED )
                .addGroup(
                    layout.createParallelGroup()
                        .addComponent( m_ok )
                        .addComponent( m_cancel ) ) );

        String dateFormat = 
            Properties.getProperty(
                Properties.KEY_DATE_FORMAT,
                Properties.VALUE_DATE_FULL );

        m_dateFormatCombobox.setSelectedItem(
            Messages.convertDateTimePropertyValueToLocale(
                dateFormat ) );

        Utils.postDialog( this );
    }


    @Override
    public void itemStateChanged( ItemEvent itemEvent )
    {
        if( itemEvent.getSource() == m_classicCheckbox )
        {
            m_showIssueCheckbox.setEnabled(
                itemEvent.getStateChange() == ItemEvent.SELECTED );

            m_padCheckbox.setEnabled(
                itemEvent.getStateChange() == ItemEvent.SELECTED );
        }
    }


    @Override
    public void actionPerformed( ActionEvent actionEvent )
    {
        if( actionEvent.getSource() == m_ok )
        {
            storeProperties();
            super.actionPerformed( actionEvent );
        }
        else if( actionEvent.getSource() == m_dateFormatCombobox )
        {
            boolean isUserDefined =
                m_dateFormatCombobox.getSelectedItem().equals(
                    Messages.LOCALE_USER_DEFINED );

            m_dateFormatUserDefinedLabel.setEnabled( isUserDefined );
            m_dateFormatUserDefinedTextfield.setEnabled( isUserDefined );
        }
        else
            super.actionPerformed( actionEvent );
    }


    private void storeProperties()
    {
        Properties.setProperty(
            Properties.KEY_DATE_FORMAT,
            Messages.convertDateTimeLocaleToPropertyValue(
                m_dateFormatCombobox.getSelectedItem().toString() ) );

        Properties.setProperty(
            Properties.KEY_USER_DEFINED,
            m_dateFormatUserDefinedTextfield.getText() );

        Properties.setProperty(
            Properties.KEY_CLASSIC,
            Boolean.toString( m_classicCheckbox.isSelected() ) );

        Properties.setProperty(
            Properties.KEY_ISSUE,
            Boolean.toString( m_showIssueCheckbox.isSelected() ) );

        Properties.setProperty(
            Properties.KEY_PAD,
            Boolean.toString( m_padCheckbox.isSelected() ) );
    }
}
