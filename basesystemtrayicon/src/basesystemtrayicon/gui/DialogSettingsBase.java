package basesystemtrayicon.gui;


import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;

import basesystemtrayicon.Messages;
import basesystemtrayicon.PropertiesBase;


public abstract class DialogSettingsBase
extends JDialog
implements ActionListener, ItemListener
{
    private static final long serialVersionUID = 1L;

    protected JCheckBox m_runOnStartup =
        new JCheckBox( Messages.getString( "DialogSettingsBase.4" ) );

    protected JButton m_ok =
        new JButton( Messages.getString( "DialogSettingsBase.0" ) );

    protected JButton m_cancel =
        new JButton( Messages.getString( "DialogSettingsBase.1" ) );

    protected Image m_applicationIconImage;


    protected DialogSettingsBase( Image applicationIconImage )
    {
        super( (JDialog)null );

        m_applicationIconImage = applicationIconImage;

        Utils.initialiseDialog(
            this,
            Messages.getString( "DialogSettingsBase.6" ),
            applicationIconImage );

        m_ok.addActionListener( this );
        m_cancel.addActionListener( this );
    }


    @Override
    public void actionPerformed( ActionEvent actionEvent )
    {
        if( actionEvent.getSource() == m_ok )
            storeProperties();

        DialogSettingsBase.this.dispose(); 
    }


    private void storeProperties()
    {
        if( ! PropertiesBase.store() )
            DialogMessage.showError(
                Messages.getString( "DialogSettingsBase.5" ),
                Messages.getString( "DialogSettingsBase.2" ),
                null,
                m_applicationIconImage );

        boolean updatedRegistry =
            basesystemtrayicon.SystemStart.setRunOnSystemStart(
                m_runOnStartup.isSelected() );

        if( ! updatedRegistry )
            DialogMessage.showError(
                Messages.getString( "DialogSettingsBase.5" ),
                Messages.getString( "DialogSettingsBase.3" ),
                null,
                m_applicationIconImage );
    }
}
