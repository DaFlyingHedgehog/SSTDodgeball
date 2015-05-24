/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;
import com.google.gdata.client.spreadsheet.*;
import com.google.gdata.data.*;
import com.google.gdata.data.batch.*;
import com.google.gdata.data.spreadsheet.*;
import com.google.gdata.util.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import java.net.*;
import java.util.ArrayList;

/**
 *
 * @author mama
 */
public class SSTDodgeballStats
{
    private static final String VIEW_URL
            = "https://spreadsheets.google.com/feeds/list/19ZGRCaLC46T3mMHSHt3eFNLv0Yu0A5S_ZK83qP9FzWE/od6/public/full";
//            = "https://spreadsheets.google.com/feeds/list/1c7Eb4bvOoQ6DCAvvg-sxFe_ViUHvtyjmvnMV_S-jRC4/od6/public/full";
    private static final String EDIT_URL
            = "https://spreadsheets.google.com/feeds/list/19ZGRCaLC46T3mMHSHt3eFNLv0Yu0A5S_ZK83qP9FzWE/od6/private/full";
    private static SpreadsheetService service;
    
    public static void initialize() {
        try {Class.forName("org.sqlite.JDBC");}
        catch (Exception e) {e.printStackTrace();}
        service = new SpreadsheetService("SSTDodgeballStats");
    }
    
    public static void sync()
    {
        try {
            ListFeed listFeed = service.getFeed(new URL(VIEW_URL), ListFeed.class);
            Connection conn = DriverManager.getConnection("jdbc:sqlite:data.db");
            Statement stat = conn.createStatement();
            stat.executeUpdate("DROP TABLE IF EXISTS stats;");
            stat.executeUpdate("CREATE TABLE stats (player, team, score);");
            PreparedStatement prep = conn.prepareStatement("INSERT INTO stats VALUES (?, ?, ?);");
            for (ListEntry row : listFeed.getEntries()) {
                prep.setString(1, row.getCustomElements().getValue("player"));
                prep.setString(2, row.getCustomElements().getValue("team"));
                prep.setString(3, row.getCustomElements().getValue("score"));
                prep.addBatch();
            }
            conn.setAutoCommit(false);
            prep.executeBatch();
            conn.setAutoCommit(true);

            conn.close();  
            /*ListEntry row = new ListEntry();
            row.getCustomElements().setValueLocal("bradybunch", "Joe");
            service.setUserCredentials("sstdodgeball@gmail.com", "jerrychen");
            WorksheetEntry ws = getWorkSheet("Test Sheet");
            listFeedUrl = ws.getListFeedUrl();
            System.out.println(listFeedUrl);
            row = service.insert(listFeedUrl, row);*/
        } catch (Exception e) {e.printStackTrace();}
    }
    
    public static ArrayList<ArrayList<String>> getData() {
        ArrayList<ArrayList<String>> data = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:data.db");
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery("SELECT * FROM stats;");
            while (rs.next()) {
                ArrayList<String> player = new ArrayList<>();
                player.add(rs.getString("player"));
                player.add(rs.getString("team"));
                player.add(rs.getString("score"));
                data.add(player);
            }
            rs.close();
            conn.close();
        } catch (Exception e) {e.printStackTrace();}
        return data;
    }
}
