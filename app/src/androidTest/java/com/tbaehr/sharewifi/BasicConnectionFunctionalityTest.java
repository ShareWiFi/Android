package com.tbaehr.sharewifi;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import com.tbaehr.sharewifi.features.connectWithWifi.WifiConnector;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class BasicConnectionFunctionalityTest {

    private WifiConnector wifiConnector;

    @Before
    public void setUp() {
        wifiConnector = new WifiConnector();
    }

    @Test
    public void testConnectAndDisconnect() {
        int netId = wifiConnector.connectToAP("home_sweet_home", "WLANPW");
        Log.i("ShareWiFiCon", "netId = "+netId);
        Assert.assertTrue(netId != -1);

        wifiConnector.removeAP(netId);
    }

}