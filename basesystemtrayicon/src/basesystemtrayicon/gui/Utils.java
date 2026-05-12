package basesystemtrayicon.gui;


import java.awt.Desktop;
import java.awt.Dialog.ModalityType;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import basesystemtrayicon.Messages;


public class Utils
{
    private static final int BORDER_INDENT = 10;

    /** Number of columns to set for a JTextField. */
    public static final int NUMBER_OF_COLUMNS = 5;


    public static final Image getImage( String image )
    {
        return new
            ImageIcon(
                ClassLoader.getSystemResource( image ) ).getImage();
    }


   /**
    * Creates a "label" written in HTML, which may contain clickable links.
    *
    * @param htmlMessage The message in HTML.
    *
    * @return A "label" which can be passed in to a message box.
    *
    * References
    *     http://explodingpixels.wordpress.com/2008/10/28/make-jeditorpane-use-the-system-font
    *     http://www.devdaily.com/blog/post/jfc-swing/how-add-style-stylesheet-jeditorpane-example-code
    *     http://www.apl.jhu.edu/~hall/java/Swing-Tutorial/Swing-Tutorial-JEditorPane.html
    */
    public static JEditorPane createURLLabel
    (
        String htmlMessage,
        final Image applicationIconImage
    )
    {
        Font font = UIManager.getFont( "Label.font" );
        String rgb = Integer.toHexString( new JPanel().getBackground().getRGB() );
        rgb = rgb.substring( 2, rgb.length() );
        String bodyRule =
            "body { background: #"
            +
            rgb
            +
            "; font-family: "
            +
            font.getFamily()
            +
            "; font-size: "
            +
            font.getSize() + "pt; }";
        
        JEditorPane jEditorPane =
            new JEditorPane(
                new HTMLEditorKit().getContentType(),
                htmlMessage );

        ( (HTMLDocument)jEditorPane.getDocument() ).getStyleSheet().addRule( bodyRule );
        jEditorPane.setEditable( false );
        jEditorPane.setBorder( null );
        jEditorPane.setOpaque( false );
        jEditorPane.addHyperlinkListener(
            new HyperlinkListener()
            {
                @Override
                public void hyperlinkUpdate( HyperlinkEvent hyperlinkEvent )
                {
                    boolean canBrowse =
                        Desktop.isDesktopSupported()
                        &&
                        Desktop.getDesktop().isSupported( Desktop.Action.BROWSE );

                    if( canBrowse )
                    {
                        HyperlinkEvent.EventType eventType = hyperlinkEvent.getEventType();

                        if( eventType == HyperlinkEvent.EventType.ACTIVATED )
                        {
                            try
                            {
                                Desktop.getDesktop().browse( hyperlinkEvent.getURL().toURI() );
                            }
                            catch( IOException | URISyntaxException exception )
                            {
                                DialogMessage.showError(
                                    Messages.getString( "Utils.0" ),
                                    Messages.getString( "Utils.1" ),
                                    exception,
                                    applicationIconImage );
                            }
                        }
                    }
                    else
                    {
                      DialogMessage.showError(
                          Messages.getString( "Utils.0" ),
                          Messages.getString( "Utils.1" ),
                          null,
                          applicationIconImage );
                    }
                }
            }
        );

        return jEditorPane;
    }


    public static CompoundBorder createBorder()
    {
        return
            BorderFactory.createCompoundBorder(
                BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(
                        BORDER_INDENT,
                        0,
                        BORDER_INDENT,
                        0 ),
                    BorderFactory.createEtchedBorder( EtchedBorder.LOWERED ) ),
                BorderFactory.createEmptyBorder(
                    BORDER_INDENT,
                    BORDER_INDENT,
                    BORDER_INDENT,
                    BORDER_INDENT ) );
    }


    public static void initialiseDialog(
        JDialog dialog,
        String title,
        Image applicationIconImage )
    {
        dialog.setIconImage( applicationIconImage );
        dialog.setModalityType( ModalityType.APPLICATION_MODAL );
        dialog.setTitle( title );

        Utils.setEscapeKeyToClose( dialog );
    }


    public static void postDialog( JDialog dialog )
    {
        dialog.pack();
        dialog.setDefaultCloseOperation( WindowConstants.DISPOSE_ON_CLOSE );
        dialog.setLocationRelativeTo( null );
        dialog.setMinimumSize( dialog.getSize() ); 
        dialog.setVisible( true );
    }


    private static void setEscapeKeyToClose( final JDialog dialog )
    {
        final AbstractAction escapeAction =
            new AbstractAction()
            {
                private static final long serialVersionUID = 1L;
  
                @Override
                public void actionPerformed( ActionEvent actionEvent )
                {
                    dialog.dispose();
                }
            };

        String escapeKey = "ESCAPE_KEY";

        dialog.getRootPane().getActionMap().put( escapeKey, escapeAction );

        dialog.getRootPane().getInputMap(
            JComponent.WHEN_IN_FOCUSED_WINDOW ).put(
                KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 ),
                escapeKey );
    }
}
