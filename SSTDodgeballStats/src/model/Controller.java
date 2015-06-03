/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Interfaces the GUI with Google and SQLite utilities.
 *
 * @author Nathan Ott and Fatih Ridha
 */
public class Controller {

    private final static String[] leaderboardColumns = {"player", "team", "throws", "hits", "catches", "hout", "cout",
        "sbonus", "hit", "games", "pointsgame", "totalpoints",};
    private final static String[] scheduleColumns = {"match", "team1", "team2", "date", "score"};
    private final static String[] matchColumns = {"match", "player", "team", "throws", "hits",
        "catches", "hout", "cout", "sbonus"};

    private static boolean authenticated;
    private static SqliteDatabase sql;

    /**
     * Initializes SQLite and Google utilities.
     */
    public static void initialize() {
        try {
            sql = new SqliteDatabase("data");
            newTables(false);
            GoogleUtils.initialize();
        } catch (Exception e) {e.printStackTrace();}
    }

    /**
     * Creates SQLite tables for new uploads if they do not already exist. If (@code delete), overwrites the existing tables.
     * 
     * @param delete overwrites existing tabels if true
     */
    private static void newTables(boolean delete) {
        try {
            sql.connect();

            if (delete) {
                sql.deleteTable("leaderboard_new");
                sql.deleteTable("schedule_new");
                sql.deleteTable("matches_new");
            }

            sql.createTable("leaderboard_new", leaderboardColumns);
            sql.createTable("schedule_new", scheduleColumns);
            sql.createTable("matches_new", matchColumns);

            sql.disconnect();
        } catch (Exception e) {e.printStackTrace();}
    }

    /**
     * Gets data from Google Spreadsheets and stores it into new tables and overwrites old data.
     */
    public static void sync() {
        try {
            sql.connect();

            sql.deleteTable("leaderboard");
            sql.createTable("leaderboard", leaderboardColumns);
            sql.insert("leaderboard", GoogleUtils.getData(0));
            sql.insert("leaderboard", sql.getData("leaderboard_new"));

            sql.deleteTable("schedule");
            sql.createTable("schedule", scheduleColumns);
            sql.insert("schedule", GoogleUtils.getData(1));
            sql.insert("schedule", sql.getData("schedule_new"));

            sql.deleteTable("matches");
            sql.createTable("matches", matchColumns);
            sql.insert("matches", GoogleUtils.getData(2));
            sql.insert("matches", sql.getData("matches_new"));

            sql.disconnect();
        } catch (Exception e) {e.printStackTrace();}
    }

    /**
     * Gets data from specified SQLite table.
     * 
     * @param table 1 for leaderboard data, 2 for schedule data, 3 for match data
     * @return list of the data in HashMap form per each table row
     */
    public static ArrayList<HashMap<String, Object>> getData(int table) {
        ArrayList<HashMap<String, Object>> data = new ArrayList();
        try {
            sql.connect();

            switch (table) {
                default:
                    data = sql.getData("leaderboard");
                    break;
                case 1:
                    data = sql.getData("schedule");
                    break;
                case 2:
                    data = sql.getData("matches");
                    break;
            }
            sql.disconnect();
        } catch (Exception e) {e.printStackTrace();}
        return data;
    }

    /**
     * Attempts to authenticate the Google service using the password.
     * 
     * @param pass password to check
     * @return true if successful authentication, false otherwise
     */
    public static boolean authenticate(String pass) {
        if (authenticated) {
            return true;
        }
        if (pass.equals("test123")) {
            try {GoogleUtils.authenticate();}
            catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            authenticated = true;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Stores new data in the specified table as well as its _new table to prepare for upload.
     * 
     * @param table table to store
     * @param data table data to store
     */
    public static void storeData(String table, ArrayList<HashMap<String, Object>> data) {
        try {
            sql.connect();

            sql.insert(table, data);
            sql.insert(table + "_new", data);

            sql.disconnect();
        } catch (Exception e) {e.printStackTrace();}
    }

    /**
     * Retrieves data from the _new tables and upload them to Google Spreadsheets, then erases data from the _new tables to prevent duplicate uploads.
     */
    public static void uploadData() {
        if (authenticated) {
            try {
                sql.connect();

                GoogleUtils.addData(0, sql.getData("leaderboard_new"));
                GoogleUtils.addData(1, sql.getData("schedule_new"));
                GoogleUtils.addData(2, sql.getData("matches_new"));
                newTables(true);

                sql.disconnect();
                GoogleUtils.deauthenticate();
            } catch (Exception e) {e.printStackTrace();}
        }
    }
}
