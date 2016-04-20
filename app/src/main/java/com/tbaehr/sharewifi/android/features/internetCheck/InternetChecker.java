package com.tbaehr.sharewifi.android.features.internetCheck;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

/**
 * Created by tbaehr on 19.04.16.
 */
public class InternetChecker {

    // for method isPingCommandSuccessful
    private static final int PING_COUNT = 3;
    private static final int PING_TIMEOUT_IN_SECONDS = 30;
    private static final String PING_COMMAND = String.format(Locale.getDefault(), "ping -c %d -W %d 8.8.8.8", PING_COUNT, PING_TIMEOUT_IN_SECONDS);
    private static final int EXIT_CODE_SUCCESS = 0;
    private static final int EXIT_CODE_NO_REPLY = 1;
    private static final int EXIT_CODE_OTHER_ERROR = 2;

    // for backup method canReachGoogle()
    private static final String PING_URL = "http://www.google.com";
    private static final int TIME_OUT_DURATION_IN_MILLIS = 30000;

    /**
     * Do not call this on UI thread!
     *
     * @return true if internet is available (can reach Google)
     */
    public boolean isInternetAvailable() {
        return isPingCommandSuccessful();
    }

    private boolean isPingCommandSuccessful() {
        try {
            Process p = Runtime.getRuntime().exec(PING_COMMAND);
            int code = p.waitFor();

            String pingResult = "";
            BufferedReader in = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
                pingResult += inputLine;
            }
            in.close();

            return code == EXIT_CODE_SUCCESS;
        } catch (Exception exception) {
            return canReachGoogle();
        }
    }

    /**
     * Backup method if the ping by terminal does not work properly.
     *
     * @return true if can connect with Google
     */
    private boolean canReachGoogle() {
        try {
            URL url = new URL(PING_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(TIME_OUT_DURATION_IN_MILLIS);
            connection.connect();
            int code = connection.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK || code == HttpURLConnection.HTTP_NO_CONTENT) {
                return true;
            }
        } catch (IOException ignore) {
            // ;
        }
        return false;
    }

}
