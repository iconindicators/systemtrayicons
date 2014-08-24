import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EtchedBorder;


public class ShowCalendars extends JDialog implements ClipboardOwner
{
	private static final long serialVersionUID = 1L;

	private static final int BORDER_INDENT = 10;
	
	protected GregorianCalendar m_gregorianCalendar = new GregorianCalendar();
	

    private ShowCalendars() { super( (JDialog)null ); }


	public static ShowCalendars create()
    {
    	final ShowCalendars showDateTime = new ShowCalendars();

    	JLabel output = new JLabel( TrayIcon.getMessageString( true ) );
        output.setHorizontalAlignment( SwingConstants.CENTER );

        output.setBorder
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

        JButton copy = new JButton( Messages.getString( "ShowCalendars.0" ) ); //$NON-NLS-1$
        copy.setToolTipText( Messages.getString( "ShowCalendars.1" ) ); //$NON-NLS-1$
        copy.addActionListener
        ( 
    		new ActionListener() 
    		{ 
    			@Override
				public void actionPerformed( ActionEvent actionEvent ) 
    			{
    				Toolkit.getDefaultToolkit().getSystemClipboard().setContents( new StringSelection( TrayIcon.getMessageString( false ) ), showDateTime );
				}
			}
		);

        JButton close = new JButton( Messages.getString( "ShowCalendars.2" ) );  //$NON-NLS-1$
        close.addActionListener( new ActionListener() { @Override public void actionPerformed( ActionEvent actionEvent ) { showDateTime.dispose(); } } );

        GroupLayout layout = new GroupLayout( showDateTime.getContentPane() );
        showDateTime.getContentPane().setLayout( layout );
        layout.setAutoCreateGaps( true );
        layout.setAutoCreateContainerGaps( true );

        layout.setHorizontalGroup
        (
    		layout.createParallelGroup( Alignment.CENTER )
				.addComponent( output )
    			.addGroup
    			(
    				layout.createSequentialGroup()
    					.addComponent( copy )
    					.addPreferredGap( ComponentPlacement.UNRELATED )
    					.addComponent( close )
    			)
		);

        layout.setVerticalGroup
        (
    		layout.createSequentialGroup()
				.addComponent( output )
				.addPreferredGap( ComponentPlacement.UNRELATED )
    			.addGroup
    			(
    				layout.createParallelGroup()
    					.addComponent( copy )
    					.addComponent( close )
    			)
		);

        showDateTime.setTitle( Messages.getString( "ShowCalendars.3" ) );  //$NON-NLS-1$
        showDateTime.setIconImage( TrayIcon.getApplicationIconImage() );
        showDateTime.pack();
        showDateTime.setLocationRelativeTo( null );
        showDateTime.setModalityType( ModalityType.APPLICATION_MODAL );
        close.requestFocusInWindow();
        showDateTime.setVisible( true );

        return showDateTime;
    }


    @Override public void lostOwnership( Clipboard clipboard, Transferable transferable ) { /** Do nothing. */ }
}