package com.tbaehr.sharewifi.android;

import android.app.Application;
import android.content.Context;

/**
 * Created by tbaehr on 13.02.16.
 */
public class ShareWiFiApplication extends Application {

    /**
     * These hotspot networks does not need Wi-Fi credentials.
     * Some Telekom customers might have access to the Internet
     * but sharing them does not help anyone. Both networks are
     * managed by the Telekom "Online Manager". Hence, ShareWiFi
     * ignores them.
     */
    public final static String[] unsharableNetworks = new String[] {"Telekom", "Telekom_FON"};

    private static Context context;

    public ShareWiFiApplication() {
        context = this;
    }

    public static Context getAppContext() {
        return context;
    }

}
