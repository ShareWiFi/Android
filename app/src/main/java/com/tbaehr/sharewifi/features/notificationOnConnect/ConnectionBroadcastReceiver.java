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

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v4.app.NotificationCompat;

import com.tbaehr.sharewifi.R;
import com.tbaehr.sharewifi.ShareActivity;
import com.tbaehr.sharewifi.ShareWiFiApplication;

/**
 * Created by tbaehr on 14.02.16.
 */
public class ConnectionBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if (info != null && info.isConnected() && info.isAvailable()) {
            // Retrieve network ssid and mac address
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            String ssid = wifiInfo.getSSID();
            String mac = wifiInfo.getMacAddress();

            if (!ssid.equals("<unknown ssid>")) {
                // Is the network already known (or is it a new one) ?
                // If it is unknown then show notification (except the user disabled notifications)
                // TODO: Complete implemenation

                Intent openShareViewIntent = new Intent(context, ShareActivity.class);
                PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), openShareViewIntent, 0);

                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(ShareWiFiApplication.getAppContext())
                                .setSmallIcon(R.drawable.ic_menu_shared)
                                .setContentTitle(context.getString(R.string.sharedialog_title).replace("§", ssid))
                                .setContentText(context.getString(R.string.sharedialog_notification_subtitle));
                mBuilder.addAction(R.drawable.ic_dialog_yes, context.getString(R.string.sharedialog_notification_option_yes), pIntent);
                mBuilder.addAction(R.drawable.ic_dialog_no, context.getString(R.string.sharedialog_notification_option_no), null);

                Notification notification = mBuilder.build();

                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

                notificationManager.notify(0, notification);
            }
        } else {
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            notificationManager.cancel(0);
        }

    }
}
