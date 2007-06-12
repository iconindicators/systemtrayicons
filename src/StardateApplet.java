import java.applet.Applet;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.GregorianCalendar;


public class StardateApplet extends Applet
{
	public static final String PARAM_NAME_SHOW_ISSUE = "SHOW_ISSUE"; 
	public static final String PARAM_NAME_FRACTIONAL_DIGITS = "FRACTIONAL_DIGITS"; 
	public static final String PARAM_NAME_UPDATE_PERIOD_MILLIS = "UPDATE_PERIOD"; 
	public static final String PARAM_NAME_BACKGROUND_RED = "BACKGROUND_RED"; 
	public static final String PARAM_NAME_BACKGROUND_GREEN = "BACKGROUND_GREEN"; 
	public static final String PARAM_NAME_BACKGROUND_BLUE = "BACKGROUND_BLUE"; 
	public static final String PARAM_NAME_FOREGROUND_RED = "FOREGROUND_RED"; 
	public static final String PARAM_NAME_FOREGROUND_GREEN = "FOREGROUND_GREEN"; 
	public static final String PARAM_NAME_FOREGROUND_BLUE = "FOREGROUND_BLUE"; 
	public static final String PARAM_NAME_PEN_RED = "PEN_RED"; 
	public static final String PARAM_NAME_PEN_GREEN = "PEN_GREEN"; 
	public static final String PARAM_NAME_PEN_BLUE = "PEN_BLUE"; 
	public static final String PARAM_NAME_FONT_NAME = "FONT_NAME"; 
	public static final String PARAM_NAME_FONT_STYLE = "FONT_STYLE"; 
	public static final String PARAM_NAME_FONT_SIZE = "FONT_SIZE"; 
	public static final String PARAM_NAME_TEXT_BEFORE = "TEXT_BEFORE"; 
	public static final String PARAM_NAME_TEXT_AFTER = "TEXT_AFTER"; 
	
	public static final boolean PARAM_VALUE_SHOW_ISSUE_DEFAULT = true; 
	public static final int PARAM_VALUE_FRACTIONAL_DIGITS_MINIMUM = 1; 
	public static final int PARAM_VALUE_FRACTIONAL_DIGITS_DEFAULT = 2; 
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
	public static final String PARAM_VALUE_FONT_STYLE_PLAIN = "PLAIN"; 
	public static final String PARAM_VALUE_FONT_STYLE_BOLD = "BOLD"; 
	public static final String PARAM_VALUE_FONT_STYLE_ITALIC = "ITALIC"; 
	public static final String PARAM_VALUE_FONT_STYLE_BOLDITALIC = "BOLDITALIC"; 
	protected static final int PARAM_VALUE_FONT_STYLE_DEFAULT = Font.PLAIN; 
	public static final int PARAM_VALUE_FONT_SIZE_MINIMUM = 1; 
	public static final int PARAM_VALUE_FONT_SIZE_DEFAULT = 10; 
	public static final String PARAM_VALUE_TEXT_BEFORE_DEFAULT = ""; 
	public static final String PARAM_VALUE_TEXT_AFTER_DEFAULT = ""; 
	
	private transient Stardate m_stardate = null;
	private transient UpdaterThread m_updaterThread = null;
	private boolean m_showIssue = PARAM_VALUE_SHOW_ISSUE_DEFAULT;
	private int m_fractionalDigits = PARAM_VALUE_FRACTIONAL_DIGITS_DEFAULT;
	private int m_updatePeriod = PARAM_VALUE_UPDATE_PERIOD_MILLIS_DEFAULT;
	private Color m_backgroundColour = new Color( PARAM_VALUE_BACKGROUND_RED_DEFAULT, PARAM_VALUE_BACKGROUND_GREEN_DEFAULT, PARAM_VALUE_BACKGROUND_BLUE_DEFAULT );
	private Color m_foregroundColour = new Color( PARAM_VALUE_FOREGROUND_RED_DEFAULT, PARAM_VALUE_FOREGROUND_GREEN_DEFAULT, PARAM_VALUE_FOREGROUND_BLUE_DEFAULT );
	private Color m_penColour = new Color( PARAM_VALUE_PEN_RED_DEFAULT, PARAM_VALUE_PEN_GREEN_DEFAULT, PARAM_VALUE_PEN_BLUE_DEFAULT );
	private String m_textBefore = PARAM_VALUE_TEXT_BEFORE_DEFAULT;  
	private String m_textAfter = PARAM_VALUE_TEXT_AFTER_DEFAULT;
	private String m_font = PARAM_VALUE_FONT_NAME_DEFAULT;  
	private int m_fontStyle = PARAM_VALUE_FONT_STYLE_DEFAULT;
	private int m_fontSize = PARAM_VALUE_FONT_SIZE_DEFAULT;


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

	
	public String getAppletInfo()
	{
		return "Stardate Display."; 
	}


	public String[][] getParameterInfo()
	{
		String parameterInfo[][] = 
		{
			{ PARAM_NAME_SHOW_ISSUE, "boolean", "Toggle display of the issue." },
			{ PARAM_NAME_FRACTIONAL_DIGITS, "integer", "Number of fractional digits to display, greater than zero." },
			{ PARAM_NAME_UPDATE_PERIOD_MILLIS, "integer", "The period in milliseconds between screen updates." },
			{ PARAM_NAME_BACKGROUND_RED, "integer", "The red colour component of the background colour." },
			{ PARAM_NAME_BACKGROUND_GREEN, "integer", "The green colour component of the background colour." },
			{ PARAM_NAME_BACKGROUND_BLUE, "integer", "The blue colour component of the background colour." },
			{ PARAM_NAME_FOREGROUND_RED, "integer", "The red colour component of the foreground colour." },
			{ PARAM_NAME_FOREGROUND_GREEN, "integer", "The green colour component of the foreground colour." },
			{ PARAM_NAME_FOREGROUND_BLUE, "integer", "The blue colour component of the foreground colour." },
			{ PARAM_NAME_PEN_RED, "integer", "The red colour component of the pen." },
			{ PARAM_NAME_PEN_GREEN, "integer", "The green colour component of the pen." },
			{ PARAM_NAME_PEN_BLUE, "integer", "The blue colour component of the pen." },
			{ PARAM_NAME_FONT_NAME, "String", "The name of the font to use." },
			{ PARAM_NAME_FONT_STYLE, "String", "PLAIN, BOLD, ITALIC, BOLDITALIC" },
			{ PARAM_NAME_FONT_SIZE, "int", "The font size." },
			{ PARAM_NAME_TEXT_BEFORE, "String", "User text to insert before the Stardate." },
			{ PARAM_NAME_TEXT_AFTER, "String", "User text to add after the Stardate." }
		};

		return parameterInfo;		
	}


	public void paint( Graphics graphics ) 
	{
		String issue = Integer.toString( m_stardate.getStardateIssue() );
		String integer = Integer.toString( m_stardate.getStardateInteger() );

		String fraction = Integer.toString( m_stardate.getStardateFraction() );
		fraction = fraction.substring( 0, Math.min( m_fractionalDigits, fraction.length() ) );

		graphics.drawRect( 0, 0, getWidth() - 1, getHeight() - 1 );
		graphics.setColor( m_penColour );
		if( m_font != null )
			graphics.setFont( new Font( m_font, m_fontStyle, m_fontSize ) );

		String output = null;
		if( m_showIssue )
			output = m_textBefore + "[ " + issue + " ] " + integer + "." + fraction + m_textAfter;
		else
			output = m_textBefore + integer + "." + fraction + m_textAfter;

		graphics.drawString( output, graphics.getFontMetrics().charWidth( ' ' ), graphics.getFontMetrics().getHeight() );
	}

	
	private void initialiseParameters()
	{
		try { m_showIssue = Boolean.valueOf( getParameter( PARAM_NAME_SHOW_ISSUE ) ).booleanValue(); }
		catch( Exception e ) { }

		try { m_fractionalDigits = Integer.valueOf( getParameter( PARAM_NAME_FRACTIONAL_DIGITS ) ).intValue(); }
		catch( Exception e ) { }
		if( m_fractionalDigits < PARAM_VALUE_FRACTIONAL_DIGITS_MINIMUM )
			m_fractionalDigits = PARAM_VALUE_FRACTIONAL_DIGITS_DEFAULT;

		try { m_updatePeriod = Integer.valueOf( getParameter( PARAM_NAME_UPDATE_PERIOD_MILLIS ) ).intValue(); }
		catch( Exception e ) { }
		if( m_updatePeriod < PARAM_VALUE_UPDATE_PERIOD_MILLIS_MINIMUM )
			m_updatePeriod = PARAM_VALUE_UPDATE_PERIOD_MILLIS_DEFAULT;
 
		try
		{
			int red = Integer.valueOf( getParameter( PARAM_NAME_BACKGROUND_RED ) ).intValue();
			int green = Integer.valueOf( getParameter( PARAM_NAME_BACKGROUND_GREEN ) ).intValue();
			int blue = Integer.valueOf( getParameter( PARAM_NAME_BACKGROUND_BLUE ) ).intValue();
			m_backgroundColour = new Color( red, green, blue );
		}
		catch( Exception e ) { }
 
		try
		{
			int red = Integer.valueOf( getParameter( PARAM_NAME_FOREGROUND_RED ) ).intValue();
			int green = Integer.valueOf( getParameter( PARAM_NAME_FOREGROUND_GREEN ) ).intValue();
			int blue = Integer.valueOf( getParameter( PARAM_NAME_FOREGROUND_BLUE ) ).intValue();
			m_foregroundColour = new Color( red, green, blue );
		}
		catch( Exception e ) { }
 
		try
		{
			int red = Integer.valueOf( getParameter( PARAM_NAME_PEN_RED ) ).intValue();
			int green = Integer.valueOf( getParameter( PARAM_NAME_PEN_GREEN ) ).intValue();
			int blue = Integer.valueOf( getParameter( PARAM_NAME_PEN_BLUE ) ).intValue();
			m_penColour = new Color( red, green, blue );
		}
		catch( Exception e ) { }

		try { m_textBefore = getParameter( PARAM_NAME_TEXT_BEFORE ); }
		catch( Exception e ) { }

		try { m_textAfter = getParameter( PARAM_NAME_TEXT_AFTER ); }
		catch( Exception e ) { }

		try { m_font = getParameter( PARAM_NAME_FONT_NAME ); }
		catch( Exception e ) { m_font = null; }

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
		catch( Exception e ) { m_fontStyle = PARAM_VALUE_FONT_STYLE_DEFAULT; }
		
		if( m_fontSize < PARAM_VALUE_FONT_SIZE_MINIMUM )
			m_updatePeriod = PARAM_VALUE_FONT_SIZE_DEFAULT; 

		try { m_fontSize = Integer.valueOf( getParameter( PARAM_NAME_FONT_SIZE ) ).intValue(); }
		catch( Exception e ) { }
		if( m_fontSize < PARAM_VALUE_FONT_SIZE_MINIMUM )
			m_updatePeriod = PARAM_VALUE_FONT_SIZE_DEFAULT; 
	}


	private class UpdaterThread extends Thread
	{
		public void run() 
		{
			while( true )
			{
				m_stardate.setGregorian( new GregorianCalendar() );
				repaint();
				try { sleep( m_updatePeriod ); } catch( Exception exception ) { }
			}							
		}
	}
}
