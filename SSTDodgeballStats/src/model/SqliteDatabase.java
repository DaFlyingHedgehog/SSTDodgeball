/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Handler for a SQLite database connection
 *
 * @author Nathan Ott and Fatih Ridha
 */
public class SqliteDatabase {

    private String database;
    private Connection conn;

    /**
     * Create a new handler for a SQLite database connection.
     *
     * @param database name of initial database (.db) to connect to
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public SqliteDatabase(String database) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        this.database = database;
    }

    /**
     * Establish a connection to the current database;
     *
     * @throws SQLException
     */
    public void connect() throws SQLException {
        disconnect();
        conn = DriverManager.getConnection("jdbc:sqlite:" + database + ".db");
    }

    /**
     * Establish a connection to the specified database.
     *
     * @param database name of .db file
     * @throws SQLException
     */
    public void connect(String database) throws SQLException {
        this.database = database;
        connect();
    }

    /**
     * Create a new table with the specified name and columns.
     *
     * @param name table name
     * @param columns set of table column names; must have at least 1 value
     * @throws SQLException
     */
    public void createTable(String name, String[] columns) throws SQLException {
        Statement stat = conn.createStatement();
        String command = "CREATE TABLE IF NOT EXISTS " + name + " (" + columns[0];
        for (int i = 1; i < columns.length; i++) {
            command += ", " + columns[i];
        }
        command += ");";
        stat.executeUpdate(command);
    }

    /**
     * Delete a specified table.
     *
     * @param name table name
     * @throws SQLException
     */
    public void deleteTable(String name) throws SQLException {
        Statement stat = conn.createStatement();
        stat.executeUpdate("DROP TABLE IF EXISTS " + name + ";");
    }

    /**
     * Insert a collection of data into a specified table.
     *
     * @param table name of existing table
     * @param dataSet collection of the data to store; data keys must match in
     * number and name to table columns
     * @return a boolean indicating if the operation was successful, i.e., the
     * table exists and the dataset is correct
     * @throws SQLException
     */
    public boolean insert(String table, ArrayList<HashMap<String, Object>> dataSet) throws SQLException {
        ResultSet rs = conn.getMetaData().getColumns(null, null, table, null);
        ArrayList<String> columns = new ArrayList();
        if (!rs.next()) {
            return false; //No such table exists
        }
        columns.add(rs.getString(4));
        String command = "INSERT INTO " + table + " VALUES (?";
        while (rs.next()) {
            columns.add(rs.getString(4));
            command += ", ?";
        }
        command += ");";
        rs.close();
        PreparedStatement prep = conn.prepareStatement(command);
        for (HashMap<String, Object> data : dataSet) {
            if (columns.size() != data.keySet().size()) {
                return false; //Data keys do not match number of table columns
            }
            for (int i = 0; i < columns.size(); i++) {
                String column = columns.get(i);
                if (!data.keySet().contains(column)) {
                    return false; //Data keyset does not contain table column
                }
                String value = data.get(column).toString();
                prep.setString(i + 1, value);
            }
            prep.addBatch();
        }
        conn.setAutoCommit(false);
        prep.executeBatch();
        conn.setAutoCommit(true);
        return true;
    }

    /**
     * Retrieve data from a specified table.
     *
     * @param table name of existing table
     * @return a list of data stored in a HashMap corresponding to table columns
     * and values
     * @throws SQLException
     */
    public ArrayList<HashMap<String, Object>> getData(String table) throws SQLException {
        System.out.println(table);
        ArrayList<HashMap<String, Object>> dataSet = new ArrayList();
        if (!conn.getMetaData().getTables(null, null, table, null).isAfterLast()) {
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery("SELECT * FROM " + table + ";");
            while (rs.next()) {
                HashMap<String, Object> data = new LinkedHashMap();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    Object value = rs.getString(i);
                    try {
                        value = Double.parseDouble(value.toString());
                    } catch (Exception b) {
                    }
                    data.put(rs.getMetaData().getColumnName(i), value);
                }
                System.out.println(data);
                dataSet.add(data);
            }
            rs.close();
        }
        return dataSet;
    }

    /**
     * Disconnect from the current database.
     *
     * @throws SQLException
     */
    public void disconnect() throws SQLException {
        if (conn != null) {
            conn.close();
        }
    }
}
