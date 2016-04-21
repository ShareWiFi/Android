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
package com.tbaehr.sharewifi.android.features.notificationOnConnect;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.view.View;

import com.tbaehr.sharewifi.android.R;
import com.tbaehr.sharewifi.android.ShareWiFiApplication;
import com.tbaehr.sharewifi.android.features.shareDialog.ShareOptionsActivity;
import com.tbaehr.sharewifi.android.model.viewmodel.WiFiNetwork;

/**
 * Created by tbaehr on 05.03.16.
 */
public class NotificationBuilder {

    private static NotificationBuilder mNotificationBuilder;

    private static Notification mNotification;

    private NotificationBuilder() {}

    public static NotificationBuilder getInstance() {
        if (mNotificationBuilder == null) {
            mNotificationBuilder = new NotificationBuilder();
        }

        return mNotificationBuilder;
    }

    public void showShareDialog(String ssid) {
        // only create a notification once
        if (mNotification != null) {
            return;
        }

        Context context = ShareWiFiApplication.getAppContext();

        // TODO: Find the local (!!) share status for this network
        int shareStatus = WiFiNetwork.ShareStatus.UNKNOWN.toInteger();

        Intent openShareViewIntent = new Intent(context, ShareOptionsActivity.class);
        openShareViewIntent.putExtra(ShareOptionsActivity.EXTRA_NETWORKNAME, ssid);
        openShareViewIntent.putExtra(ShareOptionsActivity.EXTRA_SELECTED_SHARE_OPTION, shareStatus);
        openShareViewIntent.putExtra(ShareOptionsActivity.EXTRA_OPENED_OVER_NOTIFICATION, true);

        PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), openShareViewIntent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ShareWiFiApplication.getAppContext());
        mBuilder.setSmallIcon(R.drawable.shared_by_me_light_gray);
        mBuilder.setContentTitle(context.getString(R.string.sharedialog_title, ssid));
        mBuilder.setContentText(context.getString(R.string.sharedialog_notification_subtitle));
        mBuilder.setContentIntent(pIntent);
        mBuilder.setAutoCancel(true);

        mNotification = mBuilder.build();

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, mNotification);
    }

    public void hideShareDialog() {
        Context context = ShareWiFiApplication.getAppContext();

        if (mNotification != null && mNotification.visibility == View.VISIBLE) {
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(0);
            mNotification = null;
        }
    }

}
