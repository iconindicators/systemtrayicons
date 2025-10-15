import java.awt.Desktop;
import java.awt.Font;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.MessageFormat;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;


public class MessageDialog 
{
	public static void showMessageDialog( String title, Object message, int messageType, int optionType )
	{
		JDialog messageDialog = new JOptionPane( message, messageType, optionType ).createDialog( title ); 
		messageDialog.setIconImage( TrayIcon.getApplicationIconImage() );
		messageDialog.setLocationRelativeTo( null );
		messageDialog.setVisible( true );
		messageDialog.dispose();
	}


	/**
	 * Creates a "label" written in HTML, which may contain clickable links.
	 * 
	 * @param htmlMessage The message in HTML.
	 * 
	 * @return A "label" which can be passed in to a message box.
	 */
    public static JEditorPane createURLLabel( String htmlMessage )
    {
    	// Ensure we use the system font (Windows uses Times New Roman by default).
    	// Ensure the editor pane has the same colour as a panel (Nimbus is all white).
    	// References:
        //  http://explodingpixels.wordpress.com/2008/10/28/make-jeditorpane-use-the-system-font
    	//  http://www.devdaily.com/blog/post/jfc-swing/how-add-style-stylesheet-jeditorpane-example-code
    	//	http://www.apl.jhu.edu/~hall/java/Swing-Tutorial/Swing-Tutorial-JEditorPane.html
    	Font font = UIManager.getFont( "Label.font" ); //$NON-NLS-1$
    	String rgb = Integer.toHexString( new JPanel().getBackground().getRGB() );
    	rgb = rgb.substring( 2, rgb.length() );
        String bodyRule = "body { background: #" + rgb + "; font-family: " + font.getFamily() + "; font-size: " + font.getSize() + "pt; }"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

        JEditorPane jEditorPane = new JEditorPane( new HTMLEditorKit().getContentType(), htmlMessage );
        ( (HTMLDocument)jEditorPane.getDocument() ).getStyleSheet().addRule( bodyRule );
        jEditorPane.setEditable( false );
        jEditorPane.setBorder( null );
    	jEditorPane.setOpaque( false );
    	jEditorPane.addHyperlinkListener
    	(
    		new HyperlinkListener()
    		{
    			@Override
				public void hyperlinkUpdate( HyperlinkEvent hyperlinkEvent )
    			{
    				if( Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported( Desktop.Action.BROWSE ) )
    				{
    					HyperlinkEvent.EventType eventType = hyperlinkEvent.getEventType();
    					if( eventType == HyperlinkEvent.EventType.ACTIVATED )
    					{
    						try { Desktop.getDesktop().browse( hyperlinkEvent.getURL().toURI() ); }
    						catch( URISyntaxException uriSyntaxException ) { showMessageDialog( null, new MessageFormat( Messages.getString("MessageDialog.6") ).format( new Object[] { hyperlinkEvent.getURL() } ), JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION ); } //$NON-NLS-1$
    						catch( IOException ioException ) { showMessageDialog( null, Messages.getString("MessageDialog.8"), JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION ); } //$NON-NLS-1$
    					}
    				}
    				else { showMessageDialog( null, Messages.getString("MessageDialog.7"), JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION ); } //$NON-NLS-1$
    			}
    		}
    	);

    	return jEditorPane;
    }
}