/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import com.google.gdata.client.spreadsheet.ListQuery;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.CustomElementCollection;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Helper class for Google Sheets API.
 *
 * @author Nathan Ott and Fatih Ridha
 */
public class GoogleUtils {

    private static final String CLIENT_ID = "447928638167-81noiu489746foec1onf3cfnf8vcb078.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "WFt1H2rOwFPwwlnj2KIR5fB9";
    private static final String REFRESH_TOKEN = "1/kSb9BOy_RU2-W73b3tixsJhi-b8OG9AyKBSOOUTvKxgMEudVrK5jSpoR30zcRFq6";
    private static final String LEADERBOARD_URL = "https://spreadsheets.google.com/feeds/list/19ZGRCaLC46T3mMHSHt3eFNLv0Yu0A5S_ZK83qP9FzWE/od6/public/full";
    private static final String SCHEDULE_URL = "https://spreadsheets.google.com/feeds/list/19ZGRCaLC46T3mMHSHt3eFNLv0Yu0A5S_ZK83qP9FzWE/o9paq1g/public/full";
    private static final String MATCHES_URL = "https://spreadsheets.google.com/feeds/list/19ZGRCaLC46T3mMHSHt3eFNLv0Yu0A5S_ZK83qP9FzWE/ovx6byi/public/full";

    private static SpreadsheetService service;

    /**
     * Creates a new service client.
     */
    public static void initialize() {
        service = new SpreadsheetService("SSTDodgeballStats");
    }

    /**
     * Authenticates the service with SSTDodgeball credentials.
     * 
     * @throws IOException 
     */
    public static void authenticate() throws IOException {
        NetHttpTransport transport = new NetHttpTransport();
        JacksonFactory jsonFactory = new JacksonFactory();
        String accessToken
                = new GoogleRefreshTokenRequest(transport, jsonFactory, REFRESH_TOKEN, CLIENT_ID, CLIENT_SECRET)
                .execute().getAccessToken();
        Credential cred
                = new GoogleCredential.Builder().setClientSecrets(CLIENT_ID, CLIENT_SECRET)
                .setJsonFactory(jsonFactory).setTransport(transport).build()
                .setAccessToken(accessToken)
                .setRefreshToken(REFRESH_TOKEN);
        service.setOAuth2Credentials(cred);
    }
    
    /**
     * Resets service credentials.
     */
    public static void deauthenticate() {
        service.setOAuth2Credentials(new GoogleCredential.Builder().build());
    }

    /**
     * Gets table data from the specified sheet in the form of an ArrayList of HashMaps.
     * 
     * @param sheet sheet to get data from
     * @return list of data in HashMap form per row of the table
     * @throws IOException
     * @throws ServiceException 
     */
    public static ArrayList<HashMap<String, Object>> getData(int sheet)
            throws IOException, ServiceException {
        ArrayList<HashMap<String, Object>> dataSet = new ArrayList();
        URL url;
        switch (sheet) {
            default: //0
                url = new URL(LEADERBOARD_URL);
                break;
            case 1:
                url = new URL(SCHEDULE_URL);
                break;
            case 2:
                url = new URL(MATCHES_URL);
                break;
        }
        ListFeed feed = service.getFeed(url, ListFeed.class);
        for (ListEntry entry : feed.getEntries()) {
            HashMap<String, Object> data = new LinkedHashMap();
            CustomElementCollection headers = entry.getCustomElements();
            for (String header : headers.getTags()) {
                data.put(header.replace("-", ""), headers.getValue(header));
            }
            dataSet.add(data);
        }
        return dataSet;
    }

    /**
     * Adds data to the end of the specified sheet.
     * 
     * @param sheet sheet to add to
     * @param dataSet data to add
     * @throws IOException
     * @throws ServiceException 
     */
    public static void addData(int sheet, ArrayList<HashMap<String, Object>> dataSet)
            throws IOException, ServiceException {
        URL url;
        switch (sheet) {
            default: //0
                url = new URL(LEADERBOARD_URL.replace("public", "private"));
                break;
            case 1:
                url = new URL(SCHEDULE_URL.replace("public", "private"));
                break;
            case 2:
                url = new URL(MATCHES_URL.replace("public", "private"));
                break;
        }
        for (HashMap<String, Object> data : dataSet) {
            ListEntry entry = new ListEntry();
            for (String header : data.keySet()) {
                entry.getCustomElements().setValueLocal(header, data.get(header).toString());
            }
            service.insert(url, entry);
        }
    }

    /**
     * Updates the specified sheet with new data
     * 
     * @param sheet sheet to update
     * @param dataSet data to input
     * @throws IOException
     * @throws ServiceException 
     */
    public static void updateData(int sheet, ArrayList<HashMap<String, Object>> dataSet)
            throws IOException, ServiceException {
        URL url;
        switch (sheet) {
            default: //0
                url = new URL(LEADERBOARD_URL.replace("public", "private"));
                break;
            case 1:
                url = new URL(SCHEDULE_URL.replace("public", "private"));
                break;
            case 2:
                url = new URL(MATCHES_URL.replace("public", "private"));
                break;
        }
        for (HashMap<String, Object> data : dataSet) {
            System.out.println(data);
            String query;
            switch (sheet) {
                default: //0
                    if (!data.containsKey("player")) {
                        continue;
                    }
                    query = "player = \"" + data.get("player") + "\"";
                    break;
                case 1:
                    if (!data.containsKey("match")) {
                        continue;
                    }
                    query = "match = \"" + data.get("match") + "\"";
                    break;
                case 2:
                    if (!data.containsKey("match") || !data.containsKey("player")) {
                        continue;
                    }
                    query = "match = \"" + data.get("match") + "\""
                            + " and player = \"" + data.get("player") + "\"";
                    break;
            }
            ListQuery listQuery = new ListQuery(url);
            listQuery.setSpreadsheetQuery(query);
            ListFeed feed = service.getFeed(listQuery, ListFeed.class);
            if (feed.getEntries().size() > 0) {
                ListEntry entry = feed.getEntries().get(0);
                for (String header : data.keySet()) {
                    entry.getCustomElements().setValueLocal(header, data.get(header).toString());
                }
                entry.update();
            }
        }
    }
}
