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
import java.sql.SQLException;

import java.net.URL;
import java.sql.DatabaseMetaData;
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
    private static boolean authenticated;
    
    public static boolean isAuthenticated() {return authenticated;}
    
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
            stat.executeUpdate("CREATE TABLE stats (player, team, throws, hits, catches, hout, cout, sbonus, hit, games, pointsgame, totalpoints);");
            PreparedStatement prep = conn.prepareStatement("INSERT INTO stats VALUES (?, ?, ?, ?, ?, ?,?,?,?,?,?,?);");
            for (ListEntry row : listFeed.getEntries()) {
                prep.setString(1, row.getCustomElements().getValue("player"));
                prep.setString(2, row.getCustomElements().getValue("team"));
                prep.setString(3, row.getCustomElements().getValue("throws"));
                prep.setString(4, row.getCustomElements().getValue("hits"));
                prep.setString(5, row.getCustomElements().getValue("catches"));
                prep.setString(6, row.getCustomElements().getValue("h-out"));
                prep.setString(7, row.getCustomElements().getValue("c-out"));
                prep.setString(8, row.getCustomElements().getValue("s-bonus"));
                prep.setString(9, row.getCustomElements().getValue("hit"));
                prep.setString(10, row.getCustomElements().getValue("games"));
                prep.setString(11, row.getCustomElements().getValue("pointsgame"));
                prep.setString(12, row.getCustomElements().getValue("totalpoints"));
                prep.addBatch();
            }
            conn.setAutoCommit(false);
            prep.executeBatch();
            conn.setAutoCommit(true);

            conn.close();
        } catch (Exception e) {e.printStackTrace();}
    }
    
    public static ArrayList<Object[]> getData() {
        ArrayList<Object[]> data = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:data.db");
            Statement stat = conn.createStatement();
            if (conn.getMetaData().getTables(null, null, "stats", null).next()) {
                ResultSet rs = stat.executeQuery("SELECT * FROM stats;");
                while (rs.next()) {
                    Object[] player = new Object[12];
                    player[0] = rs.getString("player");
                    player[1] = rs.getString("team");
                    player[2] = rs.getInt("throws");
                    player[3] = rs.getInt("hits");
                    player[4] = rs.getInt("catches");
                    player[5] = rs.getInt("hout");
                    player[6] = rs.getInt("cout");
                    player[7] = rs.getInt("sbonus");
                    player[8] = rs.getInt("hit");
                    player[9] = rs.getInt("games");
                    player[10] = rs.getDouble("pointsgame");
                    player[11] = rs.getInt("totalpoints");
                    data.add(player);
                }
                rs.close();
            }
            conn.close();
        } catch (SQLException e) {e.printStackTrace();}
        return data;
    }
    
    public boolean authenticate(String pass) {
        if (pass.equals("test123")) {
            try {service.setUserCredentials("sstdodgeball@gmail.com", "jerrychen");}
            catch (AuthenticationException e) {e.printStackTrace();}
            return true;
        } else {
            return false;
        }
    }
}
