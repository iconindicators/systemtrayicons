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
import javax.swing.SwingConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.LayoutStyle.ComponentPlacement;


public class RenameTimeZones extends JDialog
{
	private JButton m_ok = null;
    private Vector<JTextField> m_timeZoneTextfields = null;


    private RenameTimeZones() { super(); }


    public static RenameTimeZones create()
    {
    	final RenameTimeZones renameTimeZones = new RenameTimeZones();

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
        renameTimeZones.m_ok.addActionListener( renameTimeZones.new OKActionListener() );

        JButton cancel = new JButton( Messages.getString( "RenameTimeZones.2" ) ); 
    	cancel.addActionListener( new ActionListener() { public void actionPerformed( ActionEvent actionEvent ) { renameTimeZones.dispose(); } } );
        
        GroupLayout layout = new GroupLayout( renameTimeZones.getContentPane() );
        renameTimeZones.getContentPane().setLayout( layout );
        layout.setAutoCreateGaps( true );
        layout.setAutoCreateContainerGaps( true );

        layout.linkSize( SwingConstants.HORIZONTAL, renameTimeZones.m_ok, cancel );
    	for( int i = 1; i < renameTimeZones.m_timeZoneTextfields.size(); i++ )
            layout.linkSize( SwingConstants.VERTICAL, renameTimeZones.m_timeZoneTextfields.get( i ), renameTimeZones.m_timeZoneTextfields.get( i - 1 ) );

        SequentialGroup sequentialGroup = null;
    	ParallelGroup parallelGroup = null;

    	// Horizontal Group.
    	sequentialGroup = layout.createSequentialGroup();

    	parallelGroup = layout.createParallelGroup();
    	for( int i = 0; i < timeZoneLabels.size(); i++ )
    		parallelGroup.addComponent( timeZoneLabels.get( i ) );

    	sequentialGroup.addGroup( parallelGroup );

    	parallelGroup = layout.createParallelGroup();
    	for( int i = 0; i < renameTimeZones.m_timeZoneTextfields.size(); i++ )
    		parallelGroup.addComponent( renameTimeZones.m_timeZoneTextfields.get( i ) );

    	sequentialGroup.addGroup( parallelGroup );

    	parallelGroup = layout.createParallelGroup();

    	parallelGroup.addGroup( sequentialGroup );
    	
    	parallelGroup.addGroup
		(
			Alignment.CENTER,	
			layout.createSequentialGroup()
				.addComponent( renameTimeZones.m_ok )
				.addPreferredGap( ComponentPlacement.UNRELATED )
				.addComponent( cancel )
		);

		layout.setHorizontalGroup( parallelGroup );

        // Vertical Group.
    	sequentialGroup = layout.createSequentialGroup();
    	for( int i = 0; i < timeZones.size(); i++ )
        	sequentialGroup.addGroup
        	(  
    			layout.createParallelGroup()
    				.addComponent( timeZoneLabels.get(i ) )
    				.addComponent( renameTimeZones.m_timeZoneTextfields.get( i ) )
        	);

    	sequentialGroup.addPreferredGap( ComponentPlacement.UNRELATED );

    	sequentialGroup.addGroup
    	( 
    		layout.createParallelGroup()
    			.addComponent( renameTimeZones.m_ok )
    			.addComponent( cancel )
    	);

        layout.setVerticalGroup( sequentialGroup );

        renameTimeZones.setTitle( Messages.getString( "RenameTimeZones.3" ) ); 
		renameTimeZones.setIconImage( new ImageIcon( TrayIcon.getTrayIconImage() ).getImage() );
		renameTimeZones.setModalityType( ModalityType.APPLICATION_MODAL );
		renameTimeZones.pack();
		int originX = ( Toolkit.getDefaultToolkit().getScreenSize().width - renameTimeZones.getWidth() ) / 2;
        int originY = ( Toolkit.getDefaultToolkit().getScreenSize().height - renameTimeZones.getHeight() ) / 2;
        renameTimeZones.setLocation( originX, originY );
        renameTimeZones.addComponentListener( new ComponentListener( renameTimeZones, false, true ) );
        renameTimeZones.setVisible( true );

		return renameTimeZones;
    }

    
    private class OKActionListener implements ActionListener
    {
    	public void actionPerformed( ActionEvent actionEvent )
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

	    	RenameTimeZones.this.dispose();
    	}
    }
}
