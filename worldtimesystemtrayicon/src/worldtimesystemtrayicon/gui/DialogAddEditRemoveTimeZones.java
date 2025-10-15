package worldtimesystemtrayicon.gui;


import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import basesystemtrayicon.Messages;
import basesystemtrayicon.gui.DialogMessage;
import basesystemtrayicon.gui.Utils;
import worldtimesystemtrayicon.Properties;
import worldtimesystemtrayicon.TimeZoneUtils;


public class DialogAddEditRemoveTimeZones
extends JDialog
implements ActionListener, TableModelListener
{
    private static final long serialVersionUID = 1L;

    private Image m_applicationIconImage;

    private JTable m_table;
    private TableModel m_tableModel;

    private JButton
        m_ok,
        m_cancel;


    public DialogAddEditRemoveTimeZones( Image applicationIconImage )
    {
        super( (JDialog)null );

        m_applicationIconImage = applicationIconImage;

        Utils.initialiseDialog(
            this,
            Messages.getString( "DialogAddEditRemoveTimeZones.3" ),
            applicationIconImage );

        m_tableModel = new TableModel();
        m_tableModel.addTableModelListener( this );

        m_table = new JTable( m_tableModel );
        m_table.setToolTipText(
            Messages.getString( "DialogAddEditRemoveTimeZones.7" ) );

        m_table.setPreferredScrollableViewportSize(
            new Dimension(
                m_table.getPreferredScrollableViewportSize().width,
                m_table.getRowHeight() * 15 ) ); // Maximum number of visible rows.

        m_table.scrollRectToVisible( m_table.getCellRect( 0, 0, true ) );

        JTableHeader tableHeader = m_table.getTableHeader();
        DefaultTableCellRenderer defaultTableCellRenderer =
            ( ( DefaultTableCellRenderer )tableHeader.getDefaultRenderer() );

        defaultTableCellRenderer.setHorizontalAlignment( SwingConstants.CENTER );

        JScrollPane scrollPane = new JScrollPane( m_table ); 

        m_ok =
            new JButton(
                Messages.getString( "DialogAddEditRemoveTimeZones.1" ) );

        m_ok.addActionListener( this );

        m_cancel =
            new JButton(
                Messages.getString( "DialogAddEditRemoveTimeZones.2" ) );

        m_cancel.addActionListener( this );

        GroupLayout layout = new GroupLayout( getContentPane() );
        getContentPane().setLayout( layout );
        layout.setAutoCreateGaps( true );
        layout.setAutoCreateContainerGaps( true );
        layout.linkSize( SwingConstants.HORIZONTAL, m_ok, m_cancel );

        layout.setHorizontalGroup(
            layout.createParallelGroup()
                .addComponent( scrollPane )
                .addGroup(
                    Alignment.CENTER,
                    layout.createSequentialGroup()
                        .addComponent( m_ok )
                        .addPreferredGap( ComponentPlacement.UNRELATED )
                        .addComponent( m_cancel ) ) );

            layout.setVerticalGroup(
                layout.createSequentialGroup()
                    .addComponent( scrollPane )
                    .addPreferredGap( ComponentPlacement.UNRELATED )
                    .addGroup(
                        layout.createParallelGroup()
                            .addComponent( m_ok )
                            .addComponent( m_cancel ) ) );

        float width = scrollPane.getPreferredSize().width;
        float[] percentages = new float[] { 0.20f, 0.45f, 0.35f };
        TableColumnModel tableColumnModel = m_table.getColumnModel();
        for( int i = 0; i < 3; i++ )
            tableColumnModel.getColumn( i ).setPreferredWidth(
                Math.round( width * percentages[ i ] ) );

        Utils.postDialog( this );
    }


    @Override
    public void actionPerformed( ActionEvent actionEvent )
    {
        if( actionEvent.getSource() == m_ok )
        {
            Vector<String> userTimeZones = new Vector<>();
            Vector<String> userTimeZonesDisplayNames = new Vector<>();
            boolean emptyDisplayName = false;
            boolean duplicate = false;
            int i = 0;
            for( ; i < m_tableModel.getRowCount(); i++ )
                if( m_tableModel.getValueAt( i, 0 ).equals( Boolean.TRUE ) )
                {
                    String displayName =
                        m_tableModel.getValueAt( i, 2 ).toString().trim();

                    if( displayName.length() == 0 )
                    {
                        emptyDisplayName = true;
                        break;
                    }

                    if( userTimeZonesDisplayNames.contains( displayName ) )
                    {
                        duplicate = true;
                        break;
                    }

                    userTimeZones.add(
                        m_tableModel.getValueAt( i, 1 ).toString() );

                    userTimeZonesDisplayNames.add( displayName );
                }

            if( duplicate )
            {
                m_table.scrollRectToVisible( m_table.getCellRect( i, 2, true ) );
                m_table.setRowSelectionInterval( i, i );
                DialogMessage.showInformation(
                    Messages.getString( "DialogAddEditRemoveTimeZones.13" ),
                    Messages.getString( "DialogAddEditRemoveTimeZones.12" ),
                    m_applicationIconImage );                
            }
            else if( emptyDisplayName )
            {
                m_table.scrollRectToVisible( m_table.getCellRect( i, 2, true ) );
                m_table.setRowSelectionInterval( i, i );
                DialogMessage.showInformation(
                    Messages.getString( "DialogAddEditRemoveTimeZones.11" ),
                    Messages.getString( "DialogAddEditRemoveTimeZones.10" ),
                    m_applicationIconImage );
            }
            else if( userTimeZones.size() == 0 )
            {
                DialogMessage.showInformation(
                    Messages.getString( "DialogAddEditRemoveTimeZones.9" ),
                    Messages.getString( "DialogAddEditRemoveTimeZones.8" ),
                    m_applicationIconImage );
            }
            else
            {
                Properties.setPropertyList(
                    Properties.KEY_TIME_ZONES,
                    userTimeZones );

                Properties.setPropertyList(
                    Properties.KEY_TIME_ZONES_DISPLAY_NAMES,
                    userTimeZonesDisplayNames );

                Properties.store();
                dispose(); 
            }
        }
        else if( actionEvent.getSource() == m_cancel )
            dispose(); 
    }


    protected static class TableModel extends AbstractTableModel
    {
        private static final long serialVersionUID = 1L;

        private String[] m_columnNames = {
            Messages.getString( "DialogAddEditRemoveTimeZones.4" ),
            Messages.getString( "DialogAddEditRemoveTimeZones.5" ),
            Messages.getString( "DialogAddEditRemoveTimeZones.6" ) };

        private Vector<String> m_userTimeZones =
            Properties.getPropertyList( Properties.KEY_TIME_ZONES );

        private Vector<String> m_userTimeZonesDisplayNames =
            Properties.getPropertyList(
                Properties.KEY_TIME_ZONES_DISPLAY_NAMES );

        private Object[][] m_data;


        public TableModel()
        {
            super();

            String[] systemTimeZones = TimeZoneUtils.getSystemTimeZones();
            m_data = new Object[ systemTimeZones.length ][ 3 ];

            for( int i = 0; i < systemTimeZones.length; i++ )
            {
                String systemTimeZone = systemTimeZones[ i ];

                Object[] row =
                    new Object[] {
                        Boolean.FALSE,
                        systemTimeZone,
                        "" };

                for( int j = 0; j < m_userTimeZones.size(); j++ )
                    if( systemTimeZone.equals( m_userTimeZones.get( j ) ) )
                    {
                        row =
                            new Object[] {
                                Boolean.TRUE,
                                systemTimeZone,
                                m_userTimeZonesDisplayNames.get( j ) };

                        break;
                    }

                m_data[ i ] = row;
            }
        }


        @Override
        public int getColumnCount()
        {
            return 3;
        }


        @Override
        public int getRowCount()
        {
            return m_data.length;
        }


        @Override
        public String getColumnName( int column )
        {
            return m_columnNames[ column ];
        }
 

        @Override
        public Object getValueAt( int row, int column )
        {
            return m_data[ row ][ column ];
        }


        @Override
        public Class<?> getColumnClass( int column )
        {
            Class<?> clazz = String.class;
            if( column == 0 )
                clazz = Boolean.class;

            return clazz;
        }


        @Override
        public boolean isCellEditable( int row, int column )
        {
            boolean editable;
            if( column == 0 )
                editable = true;

            else if( column == 1 )
                editable = false;

            else // Column == 2
                editable = getValueAt( row, 0 ).equals( Boolean.TRUE );

            return editable;
        }


        @Override
        public void setValueAt( Object value, int row, int column )
        {
            m_data[ row ][ column ] = value;
            fireTableCellUpdated( row, column );
        }
    }


    @Override
    public void tableChanged( TableModelEvent tableModelEvent )
    {
        if( tableModelEvent.getColumn() == 0 )
        {   
            TableModel tableModel = (TableModel)tableModelEvent.getSource();
            int row = tableModelEvent.getFirstRow();
            boolean checked =
                ( ( Boolean )tableModel.getValueAt( row, 0 ) ).booleanValue();

            if( checked )
                tableModel.setValueAt( tableModel.getValueAt( row, 1 ), row, 2 );

            else
                tableModel.setValueAt( "", row, 2 );
        }
    }
}
