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
package com.tbaehr.sharewifi.features.connectWithWifi;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.tbaehr.sharewifi.ShareWiFiApplication;
import com.tbaehr.sharewifi.model.SharedWifiConfiguration;

import java.util.List;

/**
 * Created by tbaehr on 14.01.16.
 */
public class WifiConnector {

    private final static String TAG = "ShareWiFiCon";

    private WifiManager wifiManager;

    private List<ScanResult> scanResultList;

    private static String connectedSsidName;

    public WifiConnector() {
        Context context = ShareWiFiApplication.getAppContext();
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        scanResultList = wifiManager.getScanResults();
    }

    public void removeAP(int netId) {
        wifiManager.removeNetwork(netId);
    }

    /**
     * TODO: SSID is not unique!
     *
     * @param networkSSID
     */
    public boolean removeAP(String networkSSID) {
        int netId = 0;
        for (WifiConfiguration c : wifiManager.getConfiguredNetworks()) {
            netId++;

            if (c.SSID.equals("\""+networkSSID+"\"")) {
                wifiManager.removeNetwork(netId);
                return true;
            }
        }

        return false;
    }

    private WifiConfiguration createAPConfiguration(String networkSSID, String networkPasskey, String securityMode) {
        WifiConfiguration wifiConfiguration = new WifiConfiguration();

        wifiConfiguration.SSID = "\"" + networkSSID + "\"";

        if (securityMode.equalsIgnoreCase("OPEN")) {

            wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

        } else if (securityMode.equalsIgnoreCase("WEP")) {

            wifiConfiguration.wepKeys[0] = "\"" + networkPasskey + "\"";
            wifiConfiguration.wepTxKeyIndex = 0;
            wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);

        } else if (securityMode.equalsIgnoreCase("PSK")) {

            wifiConfiguration.preSharedKey = "\"" + networkPasskey + "\"";
            wifiConfiguration.hiddenSSID = true;
            wifiConfiguration.status = WifiConfiguration.Status.ENABLED;
            wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.WPA);

        } else {
            Log.i(TAG, "# Unsupported security mode: "+securityMode);

            return null;
        }

        return wifiConfiguration;

    }

    public int saveWifiConfiguration(String networkSSID, String networkPasskey, String securityMode) {
        WifiConfiguration wifiConfiguration = createAPConfiguration(networkSSID, networkPasskey, securityMode);

        int res = wifiManager.addNetwork(wifiConfiguration);
        Log.d(TAG, "# addNetwork returned " + res);

        return res;
    }

    /**
     * Connect with Wi-Fi network using its ssid and passkey.
     *
     * This method will automatically detect the security mode.
     * It supports OPEN, WEP, and PSK
     *
     * @param networkSSID
     * @param networkPasskey
     *
     * Original code from Stackoverflow, user AnujAroshA:
     * http://stackoverflow.com/questions/6517314/android-wifi-connection-programmatically#
     */
    public int connectToAP(String networkSSID, String networkPasskey) {
        for (ScanResult result : scanResultList) {

            if (result.SSID.equals(networkSSID)) {

                String securityMode = getScanResultSecurity(result);

                WifiConfiguration wifiConfiguration = createAPConfiguration(networkSSID, networkPasskey, securityMode);

                int res = wifiManager.addNetwork(wifiConfiguration);
                Log.d(TAG, "# addNetwork returned " + res);

                boolean b = wifiManager.enableNetwork(res, true);
                Log.d(TAG, "# enableNetwork returned " + b);

                wifiManager.setWifiEnabled(true);

                boolean changeHappen = wifiManager.saveConfiguration();

                if (res != -1 && changeHappen) {
                    Log.d(TAG, "# Change happen");
                    connectedSsidName = networkSSID;
                } else {
                    Log.d(TAG, "# Change NOT happen");
                }

                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String mac = wifiInfo.getMacAddress();
                Log.d(TAG, "# MacAddress = "+mac);

                return res;
            }
        }

        return -1;
    }

    public String getScanResultSecurity(ScanResult scanResult) {

        final String cap = scanResult.capabilities;
        final String[] securityModes = { "WEP", "PSK", "EAP" };

        for (int i = securityModes.length - 1; i >= 0; i--) {
            if (cap.contains(securityModes[i])) {
                return securityModes[i];
            }
        }

        return "OPEN";
    }

}
