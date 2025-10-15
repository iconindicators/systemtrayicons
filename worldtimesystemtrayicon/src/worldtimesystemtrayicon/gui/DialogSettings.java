package worldtimesystemtrayicon.gui;


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
import worldtimesystemtrayicon.Properties;


public class DialogSettings extends DialogSettingsBase
{
    private static final long serialVersionUID = 1L;

    private JLabel m_dateTimeFormatUserDefinedLabel;
    private JComboBox<String> m_dateTimeFormatCombobox;
    private JTextField m_dateTimeFormatUserDefinedTextfield;

    private JLabel m_combineTimeZonesSeparatorLabel;
    private JCheckBox m_combineTimeZonesCheckbox;
    private JTextField m_combineTimeZonesSeparatorTextfield;

    private JTextField
        m_differentDayIndicatorPreviousDayTextfield,
        m_differentDayIndicatorNextDayTextfield;

    private JCheckBox m_sortByDateTimeCheckbox;


    public DialogSettings( Image applicationIconImage )
    {
        super( applicationIconImage );

        JLabel dateTimeFormatLabel =
            new JLabel( Messages.getString( "DialogSettings.6" ) );

        m_dateTimeFormatCombobox =
            new JComboBox<>(
                Messages.getDateTimeFormatsInLocale( true, true, true ) );

        m_dateTimeFormatCombobox.addActionListener( this );

        m_dateTimeFormatUserDefinedLabel =
            new JLabel( Messages.getString( "DialogSettings.28" ) );

        m_dateTimeFormatUserDefinedTextfield =
            new JTextField(
                Properties.getProperty(
                    Properties.KEY_USER_DEFINED,
                    Properties.VALUE_USER_DEFINED_TIME_DEFAULT ) );

        m_dateTimeFormatUserDefinedTextfield.setColumns(
            Utils.NUMBER_OF_COLUMNS );

        m_dateTimeFormatUserDefinedTextfield.setToolTipText(
            Messages.getString( "DialogSettings.2" ) );

        m_combineTimeZonesCheckbox =
            new JCheckBox( Messages.getString( "DialogSettings.7" ) );

        m_combineTimeZonesCheckbox.setToolTipText(
            Messages.getString( "DialogSettings.8" ) );

        m_combineTimeZonesSeparatorLabel =
            new JLabel( Messages.getString( "DialogSettings.9" ) );

        m_combineTimeZonesSeparatorTextfield =
            new JTextField(
                Properties.getProperty(
                    Properties.KEY_SEPARATOR,
                    Properties.VALUE_SEPARATOR_DEFAULT ) );

        m_combineTimeZonesSeparatorTextfield.setColumns(
            Utils.NUMBER_OF_COLUMNS );

        m_combineTimeZonesCheckbox.addItemListener( this );

        JLabel differentDayIndicatorPreviousDayLabel =
            new JLabel( Messages.getString( "DialogSettings.10" ) );

        m_differentDayIndicatorPreviousDayTextfield =
            new JTextField(
                Properties.getProperty(
                    Properties.KEY_PREVIOUS_DAY,
                    Properties.VALUE_PREVIOUS_DAY_DEFAULT ) );

        m_differentDayIndicatorPreviousDayTextfield.setColumns(
            Utils.NUMBER_OF_COLUMNS );

        m_differentDayIndicatorPreviousDayTextfield.setToolTipText(
            Messages.getString( "DialogSettings.14" ) );

        JLabel differentDayIndicatorNextDayLabel =
            new JLabel( Messages.getString( "DialogSettings.11" ) );

        m_differentDayIndicatorNextDayTextfield =
            new JTextField(
                Properties.getProperty(
                    Properties.KEY_NEXT_DAY,
                    Properties.VALUE_NEXT_DAY_DEFAULT ) );

        m_differentDayIndicatorNextDayTextfield.setColumns(
            Utils.NUMBER_OF_COLUMNS );

        m_differentDayIndicatorNextDayTextfield.setToolTipText(
                Messages.getString( "DialogSettings.15" ) );

        m_sortByDateTimeCheckbox =
            new JCheckBox( Messages.getString( "DialogSettings.12" ) );

        m_sortByDateTimeCheckbox.setToolTipText(
            Messages.getString( "DialogSettings.13" ) );

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
                        .addComponent( dateTimeFormatLabel )
                        .addComponent( m_dateTimeFormatCombobox ) )
                .addGroup(
                    layout.createSequentialGroup()
                        .addPreferredGap(
                            dateTimeFormatLabel,
                            m_dateTimeFormatUserDefinedLabel,
                            ComponentPlacement.INDENT )
                        .addComponent( m_dateTimeFormatUserDefinedLabel )
                        .addComponent( m_dateTimeFormatUserDefinedTextfield ) )
                .addComponent( m_combineTimeZonesCheckbox )
                .addGroup(
                    layout.createSequentialGroup()
                        .addPreferredGap(
                            m_combineTimeZonesCheckbox,
                            m_combineTimeZonesSeparatorLabel,
                            ComponentPlacement.INDENT )
                        .addComponent( m_combineTimeZonesSeparatorLabel )
                        .addComponent( m_combineTimeZonesSeparatorTextfield ) )
                .addGroup(
                      layout.createSequentialGroup()
                          .addComponent( differentDayIndicatorPreviousDayLabel )
                          .addComponent( m_differentDayIndicatorPreviousDayTextfield ) )
                .addGroup(
                      layout.createSequentialGroup()
                          .addComponent( differentDayIndicatorNextDayLabel )
                          .addComponent( m_differentDayIndicatorNextDayTextfield ) )
                .addComponent( m_sortByDateTimeCheckbox )
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
                        .addComponent( dateTimeFormatLabel )
                        .addComponent( m_dateTimeFormatCombobox ) )
                .addGroup(
                    layout.createParallelGroup( Alignment.BASELINE )
                        .addComponent( m_dateTimeFormatUserDefinedLabel )
                        .addComponent( m_dateTimeFormatUserDefinedTextfield ) )
                .addPreferredGap( ComponentPlacement.UNRELATED )
                .addComponent( m_combineTimeZonesCheckbox )
                .addGroup(
                    layout.createParallelGroup( Alignment.BASELINE )
                        .addComponent( m_combineTimeZonesSeparatorLabel )
                        .addComponent( m_combineTimeZonesSeparatorTextfield ) )
                .addPreferredGap( ComponentPlacement.UNRELATED )
                .addGroup(
                    layout.createParallelGroup( Alignment.BASELINE )
                        .addComponent( differentDayIndicatorPreviousDayLabel )
                        .addComponent( m_differentDayIndicatorPreviousDayTextfield ) )
                .addGroup(
                    layout.createParallelGroup( Alignment.BASELINE )
                        .addComponent( differentDayIndicatorNextDayLabel )
                        .addComponent( m_differentDayIndicatorNextDayTextfield ) )
                .addPreferredGap( ComponentPlacement.UNRELATED )
                .addComponent( m_sortByDateTimeCheckbox )
                .addPreferredGap( ComponentPlacement.UNRELATED )
                .addComponent( m_runOnStartup )
                .addPreferredGap( ComponentPlacement.UNRELATED )
                .addGroup(
                    layout.createParallelGroup()
                        .addComponent( m_ok )
                        .addComponent( m_cancel ) ) );

        String dateTimeFormat = 
            Properties.getProperty(
                Properties.KEY_DATE_TIME_FORMAT,
                Properties.VALUE_TIME_SHORT );

            m_dateTimeFormatCombobox.setSelectedItem(
                Messages.convertDateTimePropertyValueToLocale( dateTimeFormat ) );

        boolean combineTimeZones =
            Properties.getPropertyBoolean(
                Properties.KEY_COMBINE,
                Properties.VALUE_COMBINE_DEFAULT );

        m_combineTimeZonesCheckbox.setSelected( combineTimeZones );

        boolean sortByDateTime =
            Properties.getPropertyBoolean(
                Properties.KEY_SORT_DATE_TIME,
                Properties.VALUE_SORT_DATE_TIME_DEFAULT );

        m_sortByDateTimeCheckbox.setSelected( sortByDateTime );

        Utils.postDialog( this );
    }


    @Override
    public void itemStateChanged( ItemEvent itemEvent )
    {
        if( itemEvent.getSource() == m_combineTimeZonesCheckbox )
        {
            boolean isSelected =
                itemEvent.getStateChange() == ItemEvent.SELECTED;

            m_combineTimeZonesSeparatorLabel.setEnabled( isSelected );
            m_combineTimeZonesSeparatorTextfield.setEnabled( isSelected );
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
        else if( actionEvent.getSource() == m_dateTimeFormatCombobox )
        {
            boolean isUserDefined =
                m_dateTimeFormatCombobox.getSelectedItem().equals(
                    Messages.LOCALE_USER_DEFINED );

            m_dateTimeFormatUserDefinedLabel.setEnabled( isUserDefined );
            m_dateTimeFormatUserDefinedTextfield.setEnabled( isUserDefined );
        }
        else
            super.actionPerformed( actionEvent );
    }


    private void storeProperties()
    {
        Properties.setProperty(
            Properties.KEY_DATE_TIME_FORMAT,
            Messages.convertDateTimeLocaleToPropertyValue(
                m_dateTimeFormatCombobox.getSelectedItem().toString() ) );

        Properties.setProperty(
            Properties.KEY_USER_DEFINED,
            m_dateTimeFormatUserDefinedTextfield.getText() );

        Properties.setProperty(
            Properties.KEY_COMBINE,
            Boolean.toString(
                m_combineTimeZonesCheckbox.isSelected() ) );

        Properties.setProperty(
            Properties.KEY_SEPARATOR,
            m_combineTimeZonesSeparatorTextfield.getText() );

        Properties.setProperty(
            Properties.KEY_PREVIOUS_DAY,
            m_differentDayIndicatorPreviousDayTextfield.getText() );

        Properties.setProperty(
            Properties.KEY_NEXT_DAY,
            m_differentDayIndicatorNextDayTextfield.getText() );

        Properties.setProperty(
            Properties.KEY_SORT_DATE_TIME,
            Boolean.toString(
                m_sortByDateTimeCheckbox.isSelected() ) );
    }
}
