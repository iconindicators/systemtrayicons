import java.util.MissingResourceException;
import java.util.ResourceBundle;


public class Messages 
{
	private static final String BUNDLE_NAME = "i18n/messages"; //$NON-NLS-1$
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle( BUNDLE_NAME );

	
	private Messages() { /** Do nothing. */ }

	
	public static String getString( String key ) 
	{
		try { return RESOURCE_BUNDLE.getString( key ); } 
		catch( MissingResourceException missingResourceException ) { return '!' + key + '!'; }
	}
}