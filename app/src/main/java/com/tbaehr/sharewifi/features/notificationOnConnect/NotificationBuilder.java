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
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.tbaehr.sharewifi.R;
import com.tbaehr.sharewifi.ShareActivity;
import com.tbaehr.sharewifi.ShareWiFiApplication;

/**
 * Created by tbaehr on 05.03.16.
 */
public class NotificationBuilder {

    private static NotificationBuilder notificationBuilder;

    private NotificationBuilder() {}

    public static NotificationBuilder getInstance() {
        if (notificationBuilder == null) {
            notificationBuilder = new NotificationBuilder();
        }

        return notificationBuilder;
    }

    public void showShareDialog(String ssid) {
        Context context = ShareWiFiApplication.getAppContext();

        Intent openShareViewIntent = new Intent(context, ShareActivity.class);
        openShareViewIntent.putExtra(ShareActivity.EXTRA_NETWORKNAME, ssid);
        openShareViewIntent.putExtra(ShareActivity.EXTRA_OPENED_OVER_NOTIFICATION, true);

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

    public void hideShareDialog() {
        Context context = ShareWiFiApplication.getAppContext();

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
    }

}
