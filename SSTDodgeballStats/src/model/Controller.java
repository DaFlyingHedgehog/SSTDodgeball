/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author mama
 */
public class Controller
{
    private static boolean authenticated;
    private static SqliteDatabase sql;
    
    public static boolean isAuthenticated() {return authenticated;}
    
    public static void initialize() throws Exception {
        sql = new SqliteDatabase("data");
        GoogleUtils.initialize();
    }
    
    public static void sync() throws Exception
    {
        sql.connect();
        ArrayList<HashMap<String, String>> leaderboardData = GoogleUtils.getData(0);
        sql.newTable("leaderboard", leaderboardData.get(0).keySet());
        sql.insert("leaderboard", leaderboardData);
        
        ArrayList<HashMap<String, String>> scheduleData = GoogleUtils.getData(1);
        sql.newTable("schedule", scheduleData.get(0).keySet());
        sql.insert("schedule", scheduleData);
        
        ArrayList<HashMap<String, String>> matchData = GoogleUtils.getData(2);
        sql.newTable("matches", matchData.get(0).keySet());
        sql.insert("matches", matchData);
        sql.disconnect();
    }
    
    public static ArrayList<HashMap<String, Object>> getData(int table) throws Exception {
        sql.connect();
        ArrayList<HashMap<String, Object>> data;
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
        return data;
    }
    
    public static boolean authenticate(String pass) throws Exception {
        if (pass.equals("test123")) {
            GoogleUtils.authenticate();
            authenticated = true;
            return true;
        } else {
            return false;
        }
    }
}
