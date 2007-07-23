import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.Collator;
import java.util.Vector;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;


public class AddRemoveTimeZones extends JDialog implements ActionListener
{
	private CheckboxList m_addRemoveCheckboxList = null;
    private JButton m_close = null;


    private AddRemoveTimeZones() { super(); }


    public static AddRemoveTimeZones create()
    {
		AddRemoveTimeZones addRemoveTimeZones = new AddRemoveTimeZones();
		
        addRemoveTimeZones.m_addRemoveCheckboxList = new CheckboxList( new Vector<String>( UserTimeZones.getAvailableTimeZones() ), Properties.getInstance().getPropertyList( Properties.PROPERTY_TIME_ZONES_SELECTED ) );
        addRemoveTimeZones.m_addRemoveCheckboxList.setVisibleRowCount( 15 );
        addRemoveTimeZones.m_addRemoveCheckboxList.setSelectedIndex( 0 );
        JScrollPane scrollPane = new JScrollPane( addRemoveTimeZones.m_addRemoveCheckboxList );

        addRemoveTimeZones.m_close = new JButton( Messages.getString( "AddRemoveTimeZones.0" ) ); 
        addRemoveTimeZones.m_close.addActionListener( addRemoveTimeZones );

        GroupLayout layout = new GroupLayout( addRemoveTimeZones.getContentPane() );
        addRemoveTimeZones.getContentPane().setLayout( layout );
        layout.setAutoCreateGaps( true );
        layout.setAutoCreateContainerGaps( true );

        layout.setHorizontalGroup
        (
    		layout.createParallelGroup( GroupLayout.Alignment.CENTER )
    			.addComponent( scrollPane )
    			.addComponent( addRemoveTimeZones.m_close )
    	);

        layout.setVerticalGroup
        (
    		layout.createSequentialGroup()
    			.addComponent( scrollPane )
    			.addComponent( addRemoveTimeZones.m_close )
		);

        addRemoveTimeZones.setTitle( Messages.getString( "AddRemoveTimeZones.1" ) ); 
		addRemoveTimeZones.setIconImage( new ImageIcon( TrayIcon.getTrayIconImage() ).getImage() );
		addRemoveTimeZones.setModalityType( ModalityType.APPLICATION_MODAL );
		addRemoveTimeZones.pack();
		int originX = ( Toolkit.getDefaultToolkit().getScreenSize().width - addRemoveTimeZones.getWidth() ) / 2;
        int originY = ( Toolkit.getDefaultToolkit().getScreenSize().height - addRemoveTimeZones.getHeight() ) / 2;
        addRemoveTimeZones.setLocation( originX, originY );
        
		return addRemoveTimeZones;
    }


    public void actionPerformed( ActionEvent actionEvent )
	{
    	if( actionEvent.getSource() == m_close )
		{
			if( m_addRemoveCheckboxList.getSelectedItems().isEmpty() )
			{
        		JOptionPane.showMessageDialog
        		( 
        			null, 
        			Messages.getString( "AddRemoveTimeZones.2" ),
        			Messages.getString( "AddRemoveTimeZones.3" ), 
        			JOptionPane.OK_OPTION
        		);

        		return;
			}
			
			m_close.removeActionListener( this );

			Vector<String> existingTimeZones = Properties.getInstance().getPropertyList( Properties.PROPERTY_TIME_ZONES_SELECTED );
			Vector<String> existingTimeZonesDisplayable = Properties.getInstance().getPropertyList( Properties.PROPERTY_TIME_ZONES_SELECTED_DISPLAY_NAMES );
			Vector<String> newTimeZones = m_addRemoveCheckboxList.getSelectedItems();
			Vector<String> newTimeZonesDisplayable = new Vector<String>( newTimeZones.size() );

			// We need to retain the display names of the existing time zones.
			for( int i = 0; i < newTimeZones.size(); i++ )
			{
				int j = 0;
				for( ; j < existingTimeZones.size(); j++ )
				{
					if( Collator.getInstance().equals( newTimeZones.get( i ), existingTimeZones.get( j ) ) )
					{
						newTimeZonesDisplayable.add( existingTimeZonesDisplayable.get( j ) );
						break;
					}
				}
				
				if( j == existingTimeZones.size() )
					newTimeZonesDisplayable.add( newTimeZones.get( i ) );
			}

			Properties.getInstance().setPropertyList( Properties.PROPERTY_TIME_ZONES_SELECTED, newTimeZones );
			Properties.getInstance().setPropertyList( Properties.PROPERTY_TIME_ZONES_SELECTED_DISPLAY_NAMES, newTimeZonesDisplayable );
			Properties.getInstance().store();
			
			dispose();
		}
	}
}
