/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;
import com.google.gdata.client.spreadsheet.*;
import com.google.gdata.data.*;
import com.google.gdata.data.batch.*;
import com.google.gdata.data.spreadsheet.*;
import com.google.gdata.util.*;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.gdata.util.ServiceException;

import java.net.URL;
import java.io.*;
import java.util.*;

/**
 *
 * @author mama
 */
public class Test {
    private static final String SPREADSHEET_SERVICE_URL
         = "https://spreadsheets.google.com/feeds/spreadsheets/private/full";
    static String ID = "447928638167-81noiu489746foec1onf3cfnf8vcb078.apps.googleusercontent.com";
    static String SECRET = "WFt1H2rOwFPwwlnj2KIR5fB9";
    static String REFRESH_TOKEN = "1/kSb9BOy_RU2-W73b3tixsJhi-b8OG9AyKBSOOUTvKxgMEudVrK5jSpoR30zcRFq6";
    
    public static void main(String[] args) throws Exception {
        NetHttpTransport transport = new NetHttpTransport();
        JacksonFactory jsonFactory = new JacksonFactory();
        
        /*String redirect = "urn:ietf:wg:oauth:2.0:oob";
        ArrayList<String> SCOPE = new ArrayList<>();
        SCOPE.add("https://spreadsheets.google.com/feeds");
        // Step 1: Authorize -->
        String authorizationUrl =
            new GoogleAuthorizationCodeRequestUrl(ID, redirect, SCOPE).build();

        // Point or redirect your user to the authorizationUrl.
        System.out.println("Go to the following link in your browser:");
        System.out.println(authorizationUrl);

        // Read the authorization code from the standard input stream.
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("What is the authorization code?");
        String code = in.readLine();
        // End of Step 1 <--

        // Step 2: Exchange -->
        GoogleTokenResponse response =
            new GoogleAuthorizationCodeTokenRequest(transport, jsonFactory, ID, SECRET,
                code, redirect).execute();
        // End of Step 2 <--
//*/
        // Build a new GoogleCredential instance and return it.
        String accessToken = 
                new GoogleRefreshTokenRequest(transport, jsonFactory, REFRESH_TOKEN, ID, SECRET)
                .execute().getAccessToken();
        Credential cred = new GoogleCredential.Builder().setClientSecrets(ID, SECRET)
                .setJsonFactory(jsonFactory).setTransport(transport).build()
                .setAccessToken(accessToken/*/response.getAccessToken()*/)
                .setRefreshToken(REFRESH_TOKEN/*/response.getRefreshToken()*/);
        System.out.println("Access token: " + accessToken);
        System.out.println("Response token: " + REFRESH_TOKEN);
        SpreadsheetService service = new SpreadsheetService("SSTDodgeballStats");
        service.setOAuth2Credentials(cred);
        URL SPREADSHEET_FEED_URL = new URL("https://spreadsheets.google.com/feeds/spreadsheets/private/full");
        SpreadsheetFeed sf = service.getFeed(SPREADSHEET_FEED_URL, SpreadsheetFeed.class);
        WorksheetEntry sheet = sf.getEntries().get(0).getWorksheets().get(1);
        System.out.println(sheet.getListFeedUrl());
        ListFeed lf = service.getFeed(sheet.getListFeedUrl(), ListFeed.class);
        System.out.println(sheet.getListFeedUrl());
        System.out.println(lf.getEntries().get(0).getCustomElements());
        ListEntry first = lf.getEntries().get(4);
        System.out.println(first.getCustomElements().getTags());
    }
}
