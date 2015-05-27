package logic;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

/**
 *
 * @author SpeedyOtt
 */
public class ScheduleModel extends AbstractTableModel {
    private ArrayList<Object[]> table;
    private String[] columns;
    
    public ScheduleModel(String[] columns) {
        table = new ArrayList<>();
        this.columns = columns;
    }
    
    public void addPlayer(Object[] stats) {
        table.add(stats);
        fireTableDataChanged();
    }
    
    public void clear() {
        table = new ArrayList<>();
        fireTableDataChanged();
    }
    
    @Override
    public int getRowCount() {
        return table.size();
    }
    
    @Override
    public Class getColumnClass(int column) {
        switch (column) {
            case 0:
                return Integer.class;   
            default:
                return String.class;
        }
    }
    
    @Override
    public int getColumnCount() {
        return columns.length;
    }
    
    @Override
    public String getColumnName(int column) {
        return columns[column];
    }
    
    @Override
    public Object getValueAt(int row, int column) {
        return table.get(row)[column];
    }
}
