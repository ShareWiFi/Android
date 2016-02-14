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
package com.tbaehr.sharewifi;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import com.tbaehr.sharewifi.features.handleWifiConfigurations.WifiConfigurationHandler;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class BasicConnectionFunctionalityTest {

    private final static String ssid1 = "guest-wifi";

    private final static String passkey1 = "internet";

    private WifiConfigurationHandler wifiConfigurationHandler;

    @Before
    public void setUp() {
        wifiConfigurationHandler = new WifiConfigurationHandler();
    }

    @Test
    public void testSuccessfulConnect() {
        int netId = wifiConfigurationHandler.connectToAP(ssid1, passkey1);
        Assert.assertTrue(netId != -1);
    }

    @Test
    public void testUnsucessfulConnect() {
        int netId = wifiConfigurationHandler.connectToAP("saturn-kunden", "internet");
        Assert.assertTrue(netId == -1);
    }

    @Test
    public void testSaveConfiguration() {
        int netId = wifiConfigurationHandler.saveWifiConfiguration("saturn-kunden", "internet", "PSK");
        Assert.assertTrue(netId != -1);
    }

    @Test
    public void testForgetConfiguration1() {
        int netId = wifiConfigurationHandler.connectToAP(ssid1, passkey1);
        Assert.assertTrue(netId != -1);

        wifiConfigurationHandler.removeAP(netId);
    }

    @Test
    public void testForgetConfiguration2() {
        int netId = wifiConfigurationHandler.connectToAP(ssid1, passkey1);
        Assert.assertTrue(netId != -1);

        Assert.assertTrue(wifiConfigurationHandler.removeAP(ssid1));
    }

}