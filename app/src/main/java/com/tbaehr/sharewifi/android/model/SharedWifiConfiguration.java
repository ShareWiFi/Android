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
package com.tbaehr.sharewifi.android.model;

import android.net.wifi.WifiConfiguration;

/**
 * Created by tbaehr on 14.01.16.
 */
public class SharedWifiConfiguration {

    /**
     * The owner of this Wi-Fi configuration.
     */
    private IUser owner;

    private WifiConfiguration wifiConfiguration;

    private String ssid;

    private String passkey;

    private SecurityMode securityMode;

    private String macAddress;

    public static SharedWifiConfiguration createFromString(String serialization, String privateKey) {

        // TODO: Implementation

        return null;
    }

    public String serialize(String publicKey) {
        StringBuilder sb = new StringBuilder();

        // TODO: Implementation

        return sb.toString();
    }

}
