/**
 * The MIT License (MIT) Copyright (c) 2016 Timo Bähr
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or s
 * ubstantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.tbaehr.sharewifi.features.notificationOnConnect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.tbaehr.sharewifi.ShareWiFiApplication;

/**
 * Created by tbaehr on 14.02.16.
 */
public class ConnectionBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Edge Case 1: NetworkInfo not available || Not connected
        NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if (info == null || !info.isConnected() || !info.isAvailable()) {
            NotificationBuilder.getInstance().hideShareDialog();
            return;
        }

        // Edge Case 2: Internet connection not available
        // TODO: Internet check
        if (false) {
            NotificationBuilder.getInstance().hideShareDialog();
            return;
        }

        // Edge case 3: Known network || Network in blackList
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String ssid = wifiInfo.getSSID().replace("\"", "");
//        String mac = wifiInfo.getMacAddress();
        if (onBlacklist(ssid) || isKnownNetwork(ssid)) {
            NotificationBuilder.getInstance().hideShareDialog();
            return;
        }

        // Show the share dialog notification
        NotificationBuilder.getInstance().showShareDialog(ssid);

    }

    private boolean isKnownNetwork(String ssid) {
        return false;
    }

    private boolean onBlacklist(String ssid) {
        return contains(ShareWiFiApplication.blackList, ssid.replace("\"",""));
    }

    private boolean contains(String[] blacklist, String ssid) {
        for (String s : blacklist) {
            if (ssid.equals(s)) {
                return true;
            }
        }
        return  false;
    }
}
