/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

/**
 * Generic table model for SSTDodgeballStats JTables.
 *
 * @author Nathan Ott and Fatih Ridha
 */
public class DodgeballTableModel extends AbstractTableModel {
    private ArrayList<Object[]> table;
    private String[] columns;
    
    /**
     * Creates a new table model with the specified columns.
     * 
     * @param columns array of column names
     */
    public DodgeballTableModel(String[] columns) {
        table = new ArrayList<>();
        this.columns = columns;
    }
    
    /**
     * Adds a new row of data to the table.
     * 
     * @param stats array of the data
     */
    public void addRow(Object[] stats) {
        table.add(stats);
        fireTableDataChanged();
    }
    
    /**
     * Clears the table.
     */
    public void clear() {
        table = new ArrayList<>();
        fireTableDataChanged();
    }
    
    /**
     * Gets the number of rows of the table.
     * 
     * @return number of rows.
     */
    @Override
    public int getRowCount() {
        return table.size();
    }
    
    /**
     * Gets the class of the specified column.
     * 
     * @param column column to check class for
     * @return 
     */
    @Override
    public Class getColumnClass(int column) {
        return table.get(0)[column].getClass();
    }
    
    /**
     * Gets the number of columns of the table.
     * 
     * @return number of columns.
     */
    @Override
    public int getColumnCount() {
        return columns.length;
    }
    
    /**
     * Gets the name of the specified column.
     * 
     * @param column column to check name for
     * @return name of column
     */
    @Override
    public String getColumnName(int column) {
        return columns[column];
    }
    
    /**
     * Gets the value at the specified cell.
     * 
     * @param row row of the cell
     * @param column column of the cell
     * @return value at the cell
     */
    @Override
    public Object getValueAt(int row, int column) {
        return table.get(row)[column];
    }
}
