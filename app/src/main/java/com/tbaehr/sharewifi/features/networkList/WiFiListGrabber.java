package com.tbaehr.sharewifi.features.networkList;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.tbaehr.sharewifi.ShareWiFiApplication;
import com.tbaehr.sharewifi.model.viewmodel.WiFiNetwork;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tbaehr on 05.03.16.
 */
public class WiFiListGrabber {

    private WifiManager wifiManager;

    public WiFiListGrabber() {
        Context context = ShareWiFiApplication.getAppContext();
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    public List<WiFiNetwork> getNetworksInRange() {
        List<ScanResult> scanResultList = wifiManager.getScanResults();

        HashMap<String, WiFiNetwork> networkMap = new HashMap<>();

        Map<String, WiFiNetwork> knownNetworks = getKnownNetworks();

        for (ScanResult scanResult : scanResultList) {
            String ssid = scanResult.SSID != null ? scanResult.SSID : scanResult.BSSID;

            // TODO: There can be more than one hidden Wi-Fi
            WiFiNetwork knownNetwork = knownNetworks.get(ssid);
            WiFiNetwork.ShareStatus shareStatus =  knownNetwork == null ? WiFiNetwork.ShareStatus.UNKNOWN : knownNetwork.getShareStatus();

            WiFiNetwork wiFiNetwork = new WiFiNetwork(scanResult, shareStatus);
            networkMap.put(ssid, wiFiNetwork);
        }

        List<WiFiNetwork> networkList = new ArrayList<>();
        networkList.addAll(networkMap.values());
        Collections.sort(networkList);

        return networkList;
    }

    public List<WiFiNetwork> getNetworksOutOfRange() {
        List<WiFiNetwork> networkList = new ArrayList<>();
        networkList.addAll(getKnownNetworks().values());
        networkList.removeAll(getNetworksInRange());

        // dummy implementation - must be removed....
        final WiFiNetwork[] values = new WiFiNetwork[] {
                new WiFiNetwork("guest-wifi", true, WiFiNetwork.SignalStrength.NONE, false, WiFiNetwork.Quality.GOOD, WiFiNetwork.ShareStatus.SHARED_BY_ME_WITHIN_GROUPS),
                new WiFiNetwork("WiFiAtWork", true, WiFiNetwork.SignalStrength.NONE, false, WiFiNetwork.Quality.GOOD, WiFiNetwork.ShareStatus.SHARED_BY_ME_WITH_MY_DEVICES),
                new WiFiNetwork("FamilyRocks", true, WiFiNetwork.SignalStrength.NONE, false, WiFiNetwork.Quality.GOOD, WiFiNetwork.ShareStatus.SHARED_WITH_ME_WITHIN_GROUPS),
                new WiFiNetwork("Free-WiFi", false, WiFiNetwork.SignalStrength.NONE, false, WiFiNetwork.Quality.BAD, WiFiNetwork.ShareStatus.SHARED_WITH_ME_WITH_EVERYONE),
                new WiFiNetwork("Saturn-Kunden", true, WiFiNetwork.SignalStrength.NONE, false, WiFiNetwork.Quality.GOOD, WiFiNetwork.ShareStatus.SHARED_WITH_ME_WITH_EVERYONE),
                new WiFiNetwork("TopSecretWiFi", true, WiFiNetwork.SignalStrength.NONE, false, WiFiNetwork.Quality.GOOD, WiFiNetwork.ShareStatus.NOT_SHARED),
                new WiFiNetwork("Hotel Billich", true, WiFiNetwork.SignalStrength.NONE, false, WiFiNetwork.Quality.BAD, WiFiNetwork.ShareStatus.SHARED_WITH_ME_WITH_EVERYONE),
        };

        final ArrayList<WiFiNetwork> list = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            list.add(values[i]);
        }
        // must be deleted up to here

        return list;
    }

    private Map<String, WiFiNetwork> getKnownNetworks() {
        Map<String, WiFiNetwork> knownNetworksList = new HashMap<>();

        // TODO: Implementation

        // dummy implementation - must be removed....
        WifiManager wifiManager = (WifiManager) ShareWiFiApplication.getAppContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String ssid = wifiInfo.getSSID();
        if (!ssid.isEmpty()) {
            ssid = ssid.substring(1,ssid.length()-1);
        }
        knownNetworksList.put(ssid, new WiFiNetwork(ssid, true, WiFiNetwork.SignalStrength.PERFECT, true, WiFiNetwork.Quality.GOOD, WiFiNetwork.ShareStatus.NOT_SHARED));
        // .. up to here

        return knownNetworksList;
    }

}
