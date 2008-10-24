import javax.swing.JDialog;
import javax.swing.JOptionPane;


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
}
