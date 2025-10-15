import java.awt.Component;
import java.awt.Dimension;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ConcurrentSkipListSet;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


/**
 * Provides a list containing check boxes.
 */
public class CheckboxList extends JList
{
	private static final long serialVersionUID = 1L;

    protected Hashtable<String, Object> m_itemsChecked = null; // We only want the key; don't care about the value.
    protected Vector<String> m_listData = null;
    protected int m_currentRow = -1;


    /**
     * Constructor for a list of check boxes.
     *
     * @param listData A vector of the item names that will be used for each check box.
     * @param itemChecked A vector of the items which are checked.
     */
    public CheckboxList( Vector<String> listData, Vector<String> itemsChecked )
    {
        super();

    	if( listData == null ) throw new IllegalArgumentException( "List data cannot be null." ); //$NON-NLS-1$

        if( itemsChecked == null ) throw new IllegalArgumentException( "Items checked cannot be null." ); //$NON-NLS-1$

        m_listData = listData;
        
        m_itemsChecked = new Hashtable<String, Object>( listData.size() );
        for( int i = 0; i < itemsChecked.size(); i++ )
        	m_itemsChecked.put( itemsChecked.get( i ), "" ); //$NON-NLS-1$

        setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        setListData( listData );
        setCellRenderer( new CheckboxRenderer() );
        addListSelectionListener( new CheckboxListener() );
    }

    
    public Vector<String> getSelectedItems()
    {
    	ConcurrentSkipListSet<String> selectedItems = new ConcurrentSkipListSet<String>();
    	Enumeration<String> keys = m_itemsChecked.keys();
    	while( keys.hasMoreElements() )
    		selectedItems.add( keys.nextElement() );

    	Vector<String> selectedItemsAsVector = new Vector<String>( selectedItems.size() );
    	Iterator<String> iterator = selectedItems.iterator();
    	while( iterator.hasNext() )
    		selectedItemsAsVector.add( iterator.next() );
    	
    	return selectedItemsAsVector;
    }

    
    protected class CheckboxListener implements ListSelectionListener
    {
    	@Override public void valueChanged( ListSelectionEvent listSelectionEvent )
        {
            if( ! listSelectionEvent.getValueIsAdjusting() )
            {
                int selectedRow = getSelectedIndex();
                if( selectedRow != -1 )
                {
                    // This allows the user to select a row (that wasn't selected before), 
                    // but doesn't toggle the check box of that row until they click a second time.
                    clearSelection();

                    if( m_currentRow != selectedRow )
                        m_currentRow = selectedRow;
                    else
                    {
                    	String selectedItem = m_listData.get( m_currentRow );

        				if( m_itemsChecked.containsKey( selectedItem ) )
                    		m_itemsChecked.remove( selectedItem );
                    	else
                    		m_itemsChecked.put( selectedItem, "" ); //$NON-NLS-1$
                    }
                }
            }
        }
    }


    protected class CheckboxRenderer implements ListCellRenderer
    {
    	@Override public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus )
        {
            JPanel panel = new JPanel();
            panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS ) );
            panel.add( Box.createRigidArea( new Dimension( 5, 0 ) ) );

            JCheckBox checkbox = new JCheckBox( value.toString(), m_itemsChecked.containsKey( value.toString() ) );
            panel.add( checkbox );

            if( index == m_currentRow )
            {
                checkbox.setBackground( getSelectionBackground() );
                checkbox.setForeground( getSelectionForeground() );
                panel.setBackground( getSelectionBackground() );
                panel.setForeground( getSelectionForeground() );
            }
            else
            {
                panel.setBackground( getBackground() );
                panel.setForeground( getForeground() );
                checkbox.setBackground( getBackground() );
                checkbox.setForeground( getForeground() );
            }

            return panel;
        }
    }
}