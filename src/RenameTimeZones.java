import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.Collator;
import java.util.Vector;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;


public class RenameTimeZones extends JDialog implements ActionListener
{
	private JButton m_ok = null;
	private JButton m_cancel = null;
    private Vector<JTextField> m_timeZoneTextfields = null;


    private RenameTimeZones() { super(); }


    public static RenameTimeZones create()
    {
    	RenameTimeZones renameTimeZones = new RenameTimeZones();

        Vector<String> timeZones = Properties.getInstance().getPropertyList( Properties.PROPERTY_TIME_ZONES_SELECTED );
        Vector<String> timeZonesDisplay = Properties.getInstance().getPropertyList( Properties.PROPERTY_TIME_ZONES_SELECTED_DISPLAY_NAMES );
        Vector<JLabel> timeZoneLabels = new Vector<JLabel>( timeZones.size() );
        renameTimeZones.m_timeZoneTextfields = new Vector<JTextField>( timeZones.size() );
        for( int i = 0; i < timeZones.size(); i++ )
        {
        	timeZoneLabels.add( new JLabel( timeZones.get( i ) + ":" ) ); 
        	renameTimeZones.m_timeZoneTextfields.add( new JTextField( timeZonesDisplay.get( i ) ) );
        }
    	
        renameTimeZones.m_ok = new JButton( Messages.getString( "RenameTimeZones.1" ) ); 
        renameTimeZones.m_ok.addActionListener( renameTimeZones );
        
        renameTimeZones.m_cancel = new JButton( Messages.getString( "RenameTimeZones.2" ) ); 
        renameTimeZones.m_cancel.addActionListener( renameTimeZones );
        
        GroupLayout layout = new GroupLayout( renameTimeZones.getContentPane() );
        renameTimeZones.getContentPane().setLayout( layout );
        layout.setAutoCreateGaps( true );
        layout.setAutoCreateContainerGaps( true );

    	SequentialGroup sequentialGroup = null;
    	ParallelGroup parallelGroup = null;

    	// Horizontal Group.
    	sequentialGroup = layout.createSequentialGroup();

    	parallelGroup = layout.createParallelGroup( GroupLayout.Alignment.CENTER );
    	for( int i = 0; i < timeZoneLabels.size(); i++ )
        	parallelGroup.addComponent( timeZoneLabels.get( i ) );

    	parallelGroup.addComponent( renameTimeZones.m_ok );
    	
    	sequentialGroup.addGroup( parallelGroup );

    	parallelGroup = layout.createParallelGroup( GroupLayout.Alignment.CENTER );
    	for( int i = 0; i < renameTimeZones.m_timeZoneTextfields.size(); i++ )
        	parallelGroup.addComponent( renameTimeZones.m_timeZoneTextfields.get( i ) );

    	parallelGroup.addComponent( renameTimeZones.m_cancel );
    	
    	sequentialGroup.addGroup( parallelGroup );

        layout.setHorizontalGroup( sequentialGroup );

        // Vertical Group.
    	sequentialGroup = layout.createSequentialGroup();
    	for( int i = 0; i < timeZones.size(); i++ )
        	sequentialGroup.addGroup
        	(  
    			layout.createParallelGroup()
    				.addComponent( timeZoneLabels.get(i ) )
    				.addComponent( renameTimeZones.m_timeZoneTextfields.get( i ) )
        	);

    	sequentialGroup.addGroup
    	( 
    		layout.createParallelGroup()
    			.addComponent( renameTimeZones.m_ok )
    			.addComponent( renameTimeZones.m_cancel )
    	);
    	
        layout.setVerticalGroup( sequentialGroup );

        renameTimeZones.setTitle( Messages.getString( "RenameTimeZones.3" ) ); 
		renameTimeZones.setIconImage( new ImageIcon( TrayIcon.getTrayIconImage() ).getImage() );
		renameTimeZones.setModalityType( ModalityType.APPLICATION_MODAL );
		renameTimeZones.pack();
		int originX = ( Toolkit.getDefaultToolkit().getScreenSize().width - renameTimeZones.getWidth() ) / 2;
        int originY = ( Toolkit.getDefaultToolkit().getScreenSize().height - renameTimeZones.getHeight() ) / 2;
        renameTimeZones.setLocation( originX, originY );
        
		return renameTimeZones;
    }


    public void actionPerformed( ActionEvent actionEvent )
	{
		if( actionEvent.getSource() == m_ok )
		{
			// Check for empty values.
			for( int i = 0; i < m_timeZoneTextfields.size(); i++ )
			{
				if( m_timeZoneTextfields.get( i ).getText().length() == 0 )
				{
        			JOptionPane.showMessageDialog
        			( 
        				null, 
        				Messages.getString( "RenameTimeZones.4" ),  
        				Messages.getString( "RenameTimeZones.5" ), 
        				JOptionPane.ERROR_MESSAGE
        			);
        			
        			return;
				}
			}

			// Check for duplicates.
			for( int i = 0; i < m_timeZoneTextfields.size(); i++ )
			{
				String currentValue = m_timeZoneTextfields.get( i ).getText();
				for( int j = i + 1; j < m_timeZoneTextfields.size(); j++ )
					if( Collator.getInstance().equals( currentValue, m_timeZoneTextfields.get( j ).getText() )  )
					{
	        			JOptionPane.showMessageDialog
	        			( 
	        				null, 
	        				Messages.getString( "RenameTimeZones.6" ), 
	        				Messages.getString( "RenameTimeZones.7" ), 
	        				JOptionPane.ERROR_MESSAGE
	        			);
	        			
	        			return;
					}
			}

			Vector<String> timeZonesDisplay = new Vector<String>( m_timeZoneTextfields.size() );
			for( int i = 0; i < m_timeZoneTextfields.size(); i++ )
				timeZonesDisplay.add( m_timeZoneTextfields.get( i ).getText() );

			Properties.getInstance().setPropertyList( Properties.PROPERTY_TIME_ZONES_SELECTED_DISPLAY_NAMES, timeZonesDisplay );
	    	Properties.getInstance().store();
		}

		m_ok.removeActionListener( this );
		m_cancel.removeActionListener( this );
		dispose();
	}
}
