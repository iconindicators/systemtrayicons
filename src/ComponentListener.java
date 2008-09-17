import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ComponentEvent;


public class ComponentListener implements java.awt.event.ComponentListener
{
    private boolean m_restrictWidth = false;
    private boolean m_restrictHeight = false;
    private int m_width = 0;
    private int m_height = 0;


    public ComponentListener( Window window, boolean restrictWidth, boolean restrictHeight )
    {
        m_restrictWidth = restrictWidth;
        m_restrictHeight = restrictHeight;
        m_width = window.getSize().width;
        m_height = window.getSize().height;
    }


    public void componentResized( ComponentEvent componentEvent )
    {
        if( ! ( componentEvent.getComponent() instanceof Window ) )
            return;

        Window window = (Window)componentEvent.getComponent();

        // Ensure that the width never falls below the minimum width.
        if( window.getWidth() < m_width )
        {
            window.setSize( new Dimension( m_width, window.getHeight() ) );
            centreWindowAboutScreen( window );
        }

        // Check if the width is to be restricted.
        if( m_restrictWidth && window.getWidth() != m_width )
        {
            window.setSize( new Dimension( m_width, window.getHeight() ) );
            centreWindowAboutScreen( window );
        }

        // Ensure that the height never falls below the minimum height.
        if( window.getHeight() < m_height )
        {
            window.setSize( new Dimension( window.getWidth(), m_height ) );
            centreWindowAboutScreen( window );
        }

        // Check if the height is to be restricted.
        if( m_restrictHeight && window.getHeight() != m_height )
        {
            window.setSize( new Dimension( window.getWidth(), m_height ) );
            centreWindowAboutScreen( window );
        }
    }


    public void componentHidden( ComponentEvent componentEvent ) { /** Do nothing. */ }


    public void componentMoved( ComponentEvent componentEvent ) { /** Do nothing. */ }


    public void componentShown( ComponentEvent componentEvent ) { /** Do nothing. */ }
    
    
    private static void centreWindowAboutScreen( Container container )
    {
        if( container == null ) throw new IllegalArgumentException( "Container cannot be null." ); //$NON-NLS-1$

        int width = container.getWidth();
        if( width <= 0 ) width = container.getSize().width;

        int height = container.getHeight();
        if( height <= 0 ) height = container.getSize().height;

        int originX = ( Toolkit.getDefaultToolkit().getScreenSize().width - width ) / 2;
        int originY = ( Toolkit.getDefaultToolkit().getScreenSize().height - height ) / 2;
        
        container.setLocation( originX, originY );
    }
}
