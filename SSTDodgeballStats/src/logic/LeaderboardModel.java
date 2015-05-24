/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

/**
 *
 * @author mama
 */
public class LeaderboardModel extends AbstractTableModel {
    private ArrayList<Object[]> table;
    private String[] columns;
    
    public LeaderboardModel(String[] columns) {
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