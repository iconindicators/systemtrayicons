public class CheckboxListEvent
{
    protected int m_row;
    protected boolean m_checkboxChecked;
    protected boolean m_rowSelected;


    public CheckboxListEvent( int row, boolean checkboxChecked, boolean rowSelected )
    {
        m_row = row;
        m_checkboxChecked = checkboxChecked;
        m_rowSelected = rowSelected;
    }


    public int getRow() { return m_row; }


    public boolean isChecked() { return m_checkboxChecked; }


    public boolean isSelected() { return m_rowSelected; }
}
