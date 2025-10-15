package basesystemtrayicon.gui;


import java.awt.Image;

import javax.swing.JDialog;
import javax.swing.JOptionPane;


public class DialogMessage
{
    /**
     * Show an error message.
     * Prints throwable message and stack trace.
     *
     * @param title The title.
     * @param message The text message.
     * @param throwable May be null.
     * @param applicationIconImage Application icon image; may be null.
     */
    public static void showError(
        String title,
        Object message,
        Throwable throwable,
        Image applicationIconImage )
    {
        showMessage(
            title,
            message,
            JOptionPane.ERROR_MESSAGE, 
            applicationIconImage );

        if( throwable != null )
        {
            if( throwable.getMessage() != null )
                System.err.println( throwable.getMessage() );

            throwable.printStackTrace();
        }
    }


    /**
     * Show an informational message.
     *
     * @param title The text title.
     * @param message The text message.
     * @param applicationIconImage Application icon image.
     */
    public static void showInformation(
        String title,
        Object message,
        Image applicationIconImage )
    {
        showMessage(
            title,
            message,
            JOptionPane.INFORMATION_MESSAGE, 
            applicationIconImage );
    }


    /**
     * Show an plain message.
     *
     * @param title The text title.
     * @param message The text message.
     * @param applicationIconImage Application icon image.
     */
    public static void showPlain(
        String title,
        Object message,
        Image applicationIconImage )
    {
        showMessage(
            title,
            message,
            JOptionPane.PLAIN_MESSAGE,
            applicationIconImage );
    }


    /**
     * Generic show message helper.
     *
     * @param title Text title.
     * @param message Text message.
     * @param messageType Message type: see JOptionPane.
     * @param applicationIconImage Application icon image; may be null.
     */
    private static void showMessage(
        String title,
        Object message,
        int messageType,
        Image applicationIconImage )
    {
        JDialog messageDialog =
            new JOptionPane(
                message,
                messageType,
                JOptionPane.DEFAULT_OPTION ).createDialog( title );

        if( applicationIconImage != null )
            messageDialog.setIconImage( applicationIconImage );

        messageDialog.setLocationRelativeTo( null );
        messageDialog.setVisible( true );
        messageDialog.dispose();
    }
}
