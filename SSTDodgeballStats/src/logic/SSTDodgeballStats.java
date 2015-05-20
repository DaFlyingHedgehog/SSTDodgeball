/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;
import com.google.gdata.client.authn.oauth.*;
import com.google.gdata.client.spreadsheet.*;
import com.google.gdata.data.*;
import com.google.gdata.data.batch.*;
import com.google.gdata.data.spreadsheet.*;
import com.google.gdata.util.*;

import java.io.IOException;
import java.net.*;
import java.util.*;

import javax.swing.JFrame;

/**
 *
 * @author mama
 */
public class SSTDodgeballStats
{
    private static final String TEAMS_SHEET_KEY = "1c7Eb4bvOoQ6DCAvvg-sxFe_ViUHvtyjmvnMV_S-jRC4";
    private static SpreadsheetService service;
    private static URL url;
    
    public static void initialize() {
        service = new SpreadsheetService("SSTDodgeballStats");
        try {url = FeedURLFactory.getDefault().getWorksheetFeedUrl(TEAMS_SHEET_KEY, "public", "basic");}
        catch (MalformedURLException e) {e.printStackTrace();}
    }
    
    public static String sync()
    throws AuthenticationException, IOException, ServiceException
    {
        WorksheetFeed feed = service.getFeed(url, WorksheetFeed.class);
        List<WorksheetEntry> worksheetList = feed.getEntries();
        WorksheetEntry worksheet = worksheetList.get(0);
        URL listFeedUrl = worksheet.getListFeedUrl();
        ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);
        for (ListEntry row : listFeed.getEntries()) {
            // Print the first column's cell value
            return row.getTitle().getPlainText();
            /*
            System.out.print(row.getTitle().getPlainText() + "\t");
            // Iterate over the remaining columns, and print each cell value
            for (String tag : row.getCustomElements().getTags()) {
                System.out.print(row.getCustomElements().getValue(tag) + "\t");
            }
            System.out.println();*/
        }
        return "";
    }
}
