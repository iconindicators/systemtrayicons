import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;


public class DifferentDayIndicator extends JDialog
{
	private static final long serialVersionUID = 1L;
	private static final int NUMBER_OF_COLUMNS = 10;
	
	public static final String PREVIOUS_DAY_INDICATOR = "-"; //$NON-NLS-1$
	public static final String NEXT_DAY_INDICATOR = "+"; //$NON-NLS-1$


    private DifferentDayIndicator() { super( (JDialog)null ); }


    public static DifferentDayIndicator create()
    {
    	final DifferentDayIndicator differentDayIndicator = new DifferentDayIndicator();

    	JLabel previousDayLabel = new JLabel( Messages.getString( "DifferentDayIndicator.0" ) ); //$NON-NLS-1$
    	
    	final JTextField previousDayTextfield = new JTextField( Properties.getInstance().getProperty( Properties.PROPERTY_DIFFERENT_DAY_INDICATOR_PREVIOUS_DAY, PREVIOUS_DAY_INDICATOR, false ) );
    	previousDayTextfield.setColumns( NUMBER_OF_COLUMNS );

    	JLabel nextDayLabel = new JLabel( Messages.getString( "DifferentDayIndicator.1" ) ); //$NON-NLS-1$

    	final JTextField nextDayTextfield = new JTextField( Properties.getInstance().getProperty( Properties.PROPERTY_DIFFERENT_DAY_INDICATOR_NEXT_DAY, NEXT_DAY_INDICATOR, false ) );
    	nextDayTextfield.setColumns( NUMBER_OF_COLUMNS );

    	JButton ok = new JButton( Messages.getString( "DifferentDayIndicator.2" ) ); //$NON-NLS-1$
    	ok.addActionListener
    	( 
			new ActionListener() 
			{ 
				@Override 
				public void actionPerformed( ActionEvent actionEvent ) 
				{ 
		    		Properties.getInstance().setProperty( Properties.PROPERTY_DIFFERENT_DAY_INDICATOR_PREVIOUS_DAY, previousDayTextfield.getText() );
		    		Properties.getInstance().setProperty( Properties.PROPERTY_DIFFERENT_DAY_INDICATOR_NEXT_DAY, nextDayTextfield.getText() );
		    		Properties.getInstance().store();

					differentDayIndicator.dispose(); 
				} 
			} 
		);

    	JButton cancel = new JButton( Messages.getString( "DifferentDayIndicator.3" ) ); //$NON-NLS-1$
    	cancel.addActionListener( new ActionListener() { @Override public void actionPerformed( ActionEvent actionEvent ) { differentDayIndicator.dispose(); } } );
    	
    	GroupLayout layout = new GroupLayout( differentDayIndicator.getContentPane() );
        differentDayIndicator.getContentPane().setLayout( layout );
        layout.setAutoCreateGaps( true );
        layout.setAutoCreateContainerGaps( true );

        layout.linkSize( SwingConstants.HORIZONTAL, ok, cancel );
        layout.linkSize( SwingConstants.VERTICAL, previousDayTextfield, nextDayTextfield );

        layout.setHorizontalGroup
        (
    		layout.createParallelGroup( Alignment.CENTER )
    			.addGroup
    			(
	    			layout.createSequentialGroup()
		    			.addGroup
		    			(
		    				layout.createParallelGroup()
			    				.addComponent( previousDayLabel )
			    				.addComponent( nextDayLabel )
		    			)	    						
		    			.addGroup
		    			(
		    				layout.createParallelGroup()
			    				.addComponent( previousDayTextfield )
			    				.addComponent( nextDayTextfield )
		    			)	    						
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
    				layout.createParallelGroup()
    					.addComponent( previousDayLabel )
    					.addComponent( previousDayTextfield )
    			)
    			.addGroup
    			(
    				layout.createParallelGroup()
    					.addComponent( nextDayLabel )
    					.addComponent( nextDayTextfield )
    			)
				.addPreferredGap( ComponentPlacement.UNRELATED )
    			.addGroup
    			(
    				layout.createParallelGroup()
    					.addComponent( ok )
    					.addComponent( cancel )
    			)
		);

		differentDayIndicator.setTitle( Messages.getString( "DifferentDayIndicator.4" ) );  //$NON-NLS-1$
        differentDayIndicator.setIconImage( TrayIcon.getApplicationIconImage() );
        differentDayIndicator.pack();
        differentDayIndicator.setLocationRelativeTo( null );
        differentDayIndicator.setModalityType( ModalityType.APPLICATION_MODAL );
        differentDayIndicator.addComponentListener( new ComponentListener( differentDayIndicator, false, true ) );
        differentDayIndicator.setVisible( true );

        return differentDayIndicator;
    }
}
