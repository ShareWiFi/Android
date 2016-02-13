package com.tbaehr.sharewifi.features.connectWithWifi;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
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
        WifiConfiguration wifiConfiguration = new WifiConfiguration();

        for (ScanResult result : scanResultList) {

            if (result.SSID.equals(networkSSID)) {

                String securityMode = getScanResultSecurity(result);

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
                    return -1;
                }


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

                wifiManager.setWifiEnabled(true);

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
