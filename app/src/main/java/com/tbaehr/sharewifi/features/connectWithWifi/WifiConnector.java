package com.tbaehr.sharewifi.features.connectWithWifi;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.List;

/**
 * Created by tbaehr on 14.01.16.
 */
public class WifiConnector {

    private WifiManager wifiManager;

    private List<ScanResult> scanResultList;

    private static String connectedSsidName;

    public WifiConnector(Context context) {
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        scanResultList = wifiManager.getScanResults();
    }

    /**
     * @param ssid
     * @param passkey
     *
     * Original code from Stackoverflow, user AnujAroshA:
     * http://stackoverflow.com/questions/6517314/android-wifi-connection-programmatically#
     */
    public void connectToAP(String ssid, String passkey) {
        WifiConfiguration wifiConfiguration = new WifiConfiguration();

        String networkSSID = ssid;
        String networkPass = passkey;

        for (ScanResult result : scanResultList) {
            if (result.SSID.equals(networkSSID)) {

                String securityMode = getScanResultSecurity(result);

                if (securityMode.equalsIgnoreCase("OPEN")) {

                    wifiConfiguration.SSID = "\"" + networkSSID + "\"";
                    wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                    int res = wifiManager.addNetwork(wifiConfiguration);
                    //Log.d(TAG, "# add Network returned " + res);

                    boolean b = wifiManager.enableNetwork(res, true);
                    //Log.d(TAG, "# enableNetwork returned " + b);

                    wifiManager.setWifiEnabled(true);

                } else if (securityMode.equalsIgnoreCase("WEP")) {

                    wifiConfiguration.SSID = "\"" + networkSSID + "\"";
                    wifiConfiguration.wepKeys[0] = "\"" + networkPass + "\"";
                    wifiConfiguration.wepTxKeyIndex = 0;
                    wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                    wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                    int res = wifiManager.addNetwork(wifiConfiguration);
                    //Log.d(TAG, "### 1 ### add Network returned " + res);

                    boolean b = wifiManager.enableNetwork(res, true);
                    //Log.d(TAG, "# enableNetwork returned " + b);

                    wifiManager.setWifiEnabled(true);
                }

                wifiConfiguration.SSID = "\"" + networkSSID + "\"";
                wifiConfiguration.preSharedKey = "\"" + networkPass + "\"";
                wifiConfiguration.hiddenSSID = true;
                wifiConfiguration.status = WifiConfiguration.Status.ENABLED;
                wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.WPA);

                int res = wifiManager.addNetwork(wifiConfiguration);
                //Log.d(TAG, "### 2 ### add Network returned " + res);

                wifiManager.enableNetwork(res, true);

                boolean changeHappen = wifiManager.saveConfiguration();

                if(res != -1 && changeHappen){
                    //Log.d(TAG, "### Change happen");

                    connectedSsidName = networkSSID;

                }else{
                    //Log.d(TAG, "*** Change NOT happen");
                }

                wifiManager.setWifiEnabled(true);
            }
        }
    }

    public String getScanResultSecurity(ScanResult scanResult) {
        //Log.i(TAG, "* getScanResultSecurity");

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
