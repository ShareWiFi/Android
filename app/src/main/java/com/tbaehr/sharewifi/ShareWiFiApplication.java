package com.tbaehr.sharewifi;

import android.app.Application;
import android.content.Context;

/**
 * Created by tbaehr on 13.02.16.
 */
public class ShareWiFiApplication extends Application {

    public final static String[] blackList = new String[] {"Telekom", "Telekom_Fon"};

    private static Context context;

    public ShareWiFiApplication() {
        context = this;
    }

    public static Context getAppContext() {
        return context;
    }

}
