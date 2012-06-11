import java.applet.Applet;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.GregorianCalendar;


public class StardateApplet extends Applet
{
	public static final String PARAM_NAME_SHOW_ISSUE = "SHOW_ISSUE";  //$NON-NLS-1$
	public static final String PARAM_NAME_PAD_INTEGER = "PAD_INTEGER";  //$NON-NLS-1$
	public static final String PARAM_NAME_UPDATE_PERIOD_MILLIS = "UPDATE_PERIOD";  //$NON-NLS-1$
	public static final String PARAM_NAME_BACKGROUND_RED = "BACKGROUND_RED";  //$NON-NLS-1$
	public static final String PARAM_NAME_BACKGROUND_GREEN = "BACKGROUND_GREEN";  //$NON-NLS-1$
	public static final String PARAM_NAME_BACKGROUND_BLUE = "BACKGROUND_BLUE";  //$NON-NLS-1$
	public static final String PARAM_NAME_FOREGROUND_RED = "FOREGROUND_RED";  //$NON-NLS-1$
	public static final String PARAM_NAME_FOREGROUND_GREEN = "FOREGROUND_GREEN";  //$NON-NLS-1$
	public static final String PARAM_NAME_FOREGROUND_BLUE = "FOREGROUND_BLUE";  //$NON-NLS-1$
	public static final String PARAM_NAME_PEN_RED = "PEN_RED";  //$NON-NLS-1$
	public static final String PARAM_NAME_PEN_GREEN = "PEN_GREEN";  //$NON-NLS-1$
	public static final String PARAM_NAME_PEN_BLUE = "PEN_BLUE";  //$NON-NLS-1$
	public static final String PARAM_NAME_FONT_NAME = "FONT_NAME";  //$NON-NLS-1$
	public static final String PARAM_NAME_FONT_STYLE = "FONT_STYLE";  //$NON-NLS-1$
	public static final String PARAM_NAME_FONT_SIZE = "FONT_SIZE";  //$NON-NLS-1$
	public static final String PARAM_NAME_TEXT_BEFORE = "TEXT_BEFORE";  //$NON-NLS-1$
	public static final String PARAM_NAME_TEXT_AFTER = "TEXT_AFTER";  //$NON-NLS-1$
	
	public static final boolean PARAM_VALUE_SHOW_ISSUE_DEFAULT = true; 
	public static final boolean PARAM_VALUE_PAD_INTEGER_DEFAULT = true; 
	public static final int PARAM_VALUE_UPDATE_PERIOD_MILLIS_MINIMUM = 100; // 100 milliseconds
	public static final int PARAM_VALUE_UPDATE_PERIOD_MILLIS_DEFAULT = 60000;  // 1 minute
	public static final int PARAM_VALUE_BACKGROUND_RED_DEFAULT = 255; 
	public static final int PARAM_VALUE_BACKGROUND_GREEN_DEFAULT = 255; 
	public static final int PARAM_VALUE_BACKGROUND_BLUE_DEFAULT = 255; 
	public static final int PARAM_VALUE_FOREGROUND_RED_DEFAULT = 255; 
	public static final int PARAM_VALUE_FOREGROUND_GREEN_DEFAULT = 255; 
	public static final int PARAM_VALUE_FOREGROUND_BLUE_DEFAULT = 255; 
	public static final int PARAM_VALUE_PEN_RED_DEFAULT = 255; 
	public static final int PARAM_VALUE_PEN_GREEN_DEFAULT = 255; 
	public static final int PARAM_VALUE_PEN_BLUE_DEFAULT = 255; 
	public static final String PARAM_VALUE_FONT_NAME_DEFAULT = null; 
	public static final String PARAM_VALUE_FONT_STYLE_PLAIN = "PLAIN";  //$NON-NLS-1$
	public static final String PARAM_VALUE_FONT_STYLE_BOLD = "BOLD";  //$NON-NLS-1$
	public static final String PARAM_VALUE_FONT_STYLE_ITALIC = "ITALIC";  //$NON-NLS-1$
	public static final String PARAM_VALUE_FONT_STYLE_BOLDITALIC = "BOLDITALIC";  //$NON-NLS-1$
	protected static final int PARAM_VALUE_FONT_STYLE_DEFAULT = Font.PLAIN; 
	public static final int PARAM_VALUE_FONT_SIZE_MINIMUM = 1; 
	public static final int PARAM_VALUE_FONT_SIZE_DEFAULT = 10; 
	public static final String PARAM_VALUE_TEXT_BEFORE_DEFAULT = "";  //$NON-NLS-1$
	public static final String PARAM_VALUE_TEXT_AFTER_DEFAULT = "";  //$NON-NLS-1$
	
	protected transient Stardate m_stardate = null;
	private transient UpdaterThread m_updaterThread = null;
	private boolean m_showIssue = PARAM_VALUE_SHOW_ISSUE_DEFAULT;
	private boolean m_padInteger = PARAM_VALUE_PAD_INTEGER_DEFAULT;
	protected int m_updatePeriod = PARAM_VALUE_UPDATE_PERIOD_MILLIS_DEFAULT;
	private Color m_backgroundColour = new Color( PARAM_VALUE_BACKGROUND_RED_DEFAULT, PARAM_VALUE_BACKGROUND_GREEN_DEFAULT, PARAM_VALUE_BACKGROUND_BLUE_DEFAULT );
	private Color m_foregroundColour = new Color( PARAM_VALUE_FOREGROUND_RED_DEFAULT, PARAM_VALUE_FOREGROUND_GREEN_DEFAULT, PARAM_VALUE_FOREGROUND_BLUE_DEFAULT );
	private Color m_penColour = new Color( PARAM_VALUE_PEN_RED_DEFAULT, PARAM_VALUE_PEN_GREEN_DEFAULT, PARAM_VALUE_PEN_BLUE_DEFAULT );
	private String m_textBefore = PARAM_VALUE_TEXT_BEFORE_DEFAULT;  
	private String m_textAfter = PARAM_VALUE_TEXT_AFTER_DEFAULT;
	private String m_font = PARAM_VALUE_FONT_NAME_DEFAULT;  
	private int m_fontStyle = PARAM_VALUE_FONT_STYLE_DEFAULT;
	private int m_fontSize = PARAM_VALUE_FONT_SIZE_DEFAULT;


    @Override
	public void init() 
    {
    	initialiseParameters();

    	m_stardate = new Stardate();
    	m_stardate.setGregorian( new GregorianCalendar() );
		
    	m_updaterThread = new UpdaterThread();
		m_updaterThread.start();
		
		setBackground( m_backgroundColour );
		setForeground( m_foregroundColour );
	}

	
	@Override
	public String getAppletInfo() { return "Stardate Display."; } //$NON-NLS-1$


	@Override
	public String[][] getParameterInfo()
	{
		String parameterInfo[][] = 
		{
			{ PARAM_NAME_SHOW_ISSUE, "boolean", "Toggle display of the issue." },  //$NON-NLS-1$//$NON-NLS-2$
			{ PARAM_NAME_PAD_INTEGER, "boolean", "Pad the integer part with leading zeros." },  //$NON-NLS-1$//$NON-NLS-2$
			{ PARAM_NAME_UPDATE_PERIOD_MILLIS, "integer", "The period in milliseconds between screen updates." },  //$NON-NLS-1$//$NON-NLS-2$
			{ PARAM_NAME_BACKGROUND_RED, "integer", "The red colour component of the background colour." },  //$NON-NLS-1$//$NON-NLS-2$
			{ PARAM_NAME_BACKGROUND_GREEN, "integer", "The green colour component of the background colour." }, //$NON-NLS-1$ //$NON-NLS-2$
			{ PARAM_NAME_BACKGROUND_BLUE, "integer", "The blue colour component of the background colour." },  //$NON-NLS-1$//$NON-NLS-2$
			{ PARAM_NAME_FOREGROUND_RED, "integer", "The red colour component of the foreground colour." },  //$NON-NLS-1$//$NON-NLS-2$
			{ PARAM_NAME_FOREGROUND_GREEN, "integer", "The green colour component of the foreground colour." },  //$NON-NLS-1$//$NON-NLS-2$
			{ PARAM_NAME_FOREGROUND_BLUE, "integer", "The blue colour component of the foreground colour." },  //$NON-NLS-1$//$NON-NLS-2$
			{ PARAM_NAME_PEN_RED, "integer", "The red colour component of the pen." },  //$NON-NLS-1$//$NON-NLS-2$
			{ PARAM_NAME_PEN_GREEN, "integer", "The green colour component of the pen." },  //$NON-NLS-1$//$NON-NLS-2$
			{ PARAM_NAME_PEN_BLUE, "integer", "The blue colour component of the pen." },  //$NON-NLS-1$//$NON-NLS-2$
			{ PARAM_NAME_FONT_NAME, "String", "The name of the font to use." },  //$NON-NLS-1$//$NON-NLS-2$
			{ PARAM_NAME_FONT_STYLE, "String", "PLAIN, BOLD, ITALIC, BOLDITALIC" },  //$NON-NLS-1$//$NON-NLS-2$
			{ PARAM_NAME_FONT_SIZE, "int", "The font size." },  //$NON-NLS-1$//$NON-NLS-2$
			{ PARAM_NAME_TEXT_BEFORE, "String", "User text to insert before the Stardate." },  //$NON-NLS-1$//$NON-NLS-2$
			{ PARAM_NAME_TEXT_AFTER, "String", "User text to add after the Stardate." }  //$NON-NLS-1$//$NON-NLS-2$
		};

		return parameterInfo;		
	}


	@Override
	public void paint( Graphics graphics ) 
	{
		graphics.drawRect( 0, 0, getWidth() - 1, getHeight() - 1 );
		graphics.setColor( m_penColour );
		if( m_font != null )
			graphics.setFont( new Font( m_font, m_fontStyle, m_fontSize ) );

		graphics.drawString
		(
			m_textBefore + m_stardate.toStardateString( m_padInteger, m_showIssue ) + m_textAfter,
			graphics.getFontMetrics().charWidth( ' ' ),
			graphics.getFontMetrics().getHeight() 
		);
	}

	
	private void initialiseParameters()
	{
		try { m_showIssue = Boolean.valueOf( getParameter( PARAM_NAME_SHOW_ISSUE ) ).booleanValue(); }
		catch( Exception exception ) { /** Do nothing. */ }

		try { m_padInteger = Boolean.valueOf( getParameter( PARAM_NAME_PAD_INTEGER ) ).booleanValue(); }
		catch( Exception exception ) { /** Do nothing. */ }

		try { m_updatePeriod = Integer.valueOf( getParameter( PARAM_NAME_UPDATE_PERIOD_MILLIS ) ).intValue(); }
		catch( Exception exception ) { /** Do nothing. */ }
		if( m_updatePeriod < PARAM_VALUE_UPDATE_PERIOD_MILLIS_MINIMUM )
			m_updatePeriod = PARAM_VALUE_UPDATE_PERIOD_MILLIS_DEFAULT;
 
		try
		{
			int red = Integer.valueOf( getParameter( PARAM_NAME_BACKGROUND_RED ) ).intValue();
			int green = Integer.valueOf( getParameter( PARAM_NAME_BACKGROUND_GREEN ) ).intValue();
			int blue = Integer.valueOf( getParameter( PARAM_NAME_BACKGROUND_BLUE ) ).intValue();
			m_backgroundColour = new Color( red, green, blue );
		}
		catch( Exception exception ) { /** Do nothing. */ }
 
		try
		{
			int red = Integer.valueOf( getParameter( PARAM_NAME_FOREGROUND_RED ) ).intValue();
			int green = Integer.valueOf( getParameter( PARAM_NAME_FOREGROUND_GREEN ) ).intValue();
			int blue = Integer.valueOf( getParameter( PARAM_NAME_FOREGROUND_BLUE ) ).intValue();
			m_foregroundColour = new Color( red, green, blue );
		}
		catch( Exception exception ) { /** Do nothing. */ }
 
		try
		{
			int red = Integer.valueOf( getParameter( PARAM_NAME_PEN_RED ) ).intValue();
			int green = Integer.valueOf( getParameter( PARAM_NAME_PEN_GREEN ) ).intValue();
			int blue = Integer.valueOf( getParameter( PARAM_NAME_PEN_BLUE ) ).intValue();
			m_penColour = new Color( red, green, blue );
		}
		catch( Exception exception ) { /** Do nothing. */ }

		try { m_textBefore = getParameter( PARAM_NAME_TEXT_BEFORE ); }
		catch( Exception exception ) { /** Do nothing. */ }

		try { m_textAfter = getParameter( PARAM_NAME_TEXT_AFTER ); }
		catch( Exception exception ) { /** Do nothing. */ }

		try { m_font = getParameter( PARAM_NAME_FONT_NAME ); }
		catch( Exception exception ) { m_font = null; }

		try
		{
			String fontStyle = getParameter( PARAM_NAME_FONT_STYLE );
			if( fontStyle.equalsIgnoreCase( PARAM_VALUE_FONT_STYLE_BOLD ) )
				m_fontStyle = Font.BOLD; 
			else if( fontStyle.equalsIgnoreCase( PARAM_VALUE_FONT_STYLE_ITALIC ) )
				m_fontStyle = Font.ITALIC; 
			else if( fontStyle.equalsIgnoreCase( PARAM_VALUE_FONT_STYLE_BOLDITALIC ) )
				m_fontStyle = Font.BOLD | Font.ITALIC; 
		}
		catch( Exception exception ) { m_fontStyle = PARAM_VALUE_FONT_STYLE_DEFAULT; }
		
		if( m_fontSize < PARAM_VALUE_FONT_SIZE_MINIMUM )
			m_updatePeriod = PARAM_VALUE_FONT_SIZE_DEFAULT; 

		try { m_fontSize = Integer.valueOf( getParameter( PARAM_NAME_FONT_SIZE ) ).intValue(); }
		catch( Exception exception ) { /** Do nothing. */ }
		if( m_fontSize < PARAM_VALUE_FONT_SIZE_MINIMUM )
			m_updatePeriod = PARAM_VALUE_FONT_SIZE_DEFAULT; 
	}


	protected class UpdaterThread extends Thread
	{
		@Override
		public void run() 
		{
			while( true )
			{
				m_stardate.setGregorian( new GregorianCalendar() );
				repaint();
				try { sleep( m_updatePeriod ); } catch( Exception exception ) { /** Do nothing. */ }
			}							
		}
	}
}