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


public class ShowDateTime extends JDialog implements ClipboardOwner
{
	private static final long serialVersionUID = 1L;

	private static final int BORDER_INDENT = 10;
	
	protected GregorianCalendar m_gregorianCalendar = new GregorianCalendar();
	

    private ShowDateTime() { super( (JDialog)null ); }


	public static ShowDateTime create()
    {
    	final ShowDateTime showDateTime = new ShowDateTime();

    	JLabel output = new JLabel( Message.getMessageString( showDateTime.m_gregorianCalendar, true ) );
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

        JButton copy = new JButton( Messages.getString( "ShowDateTime.0" ) ); //$NON-NLS-1$
        copy.setToolTipText( Messages.getString( "ShowDateTime.1" ) ); //$NON-NLS-1$
        copy.addActionListener
        ( 
    		new ActionListener() 
    		{ 
    			public void actionPerformed( ActionEvent actionEvent ) 
    			{
    				Toolkit.getDefaultToolkit().getSystemClipboard().setContents( new StringSelection( Message.getMessageString( showDateTime.m_gregorianCalendar, false ) ), showDateTime );
				}
			}
		);

        JButton close = new JButton( Messages.getString( "ShowDateTime.2" ) );  //$NON-NLS-1$
        close.addActionListener( new ActionListener() { public void actionPerformed( ActionEvent actionEvent ) { showDateTime.dispose(); } } );

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

        showDateTime.setTitle( Messages.getString( "ShowDateTime.3" ) );  //$NON-NLS-1$
        showDateTime.setIconImage( TrayIcon.getTrayIconImage() );
        showDateTime.pack();
        showDateTime.setLocationRelativeTo( null );
        showDateTime.setModalityType( ModalityType.APPLICATION_MODAL );
        close.requestFocusInWindow();

        return showDateTime;
    }


    public void lostOwnership( Clipboard clipboard, Transferable transferable ) { /** Do nothing. */ }
}
