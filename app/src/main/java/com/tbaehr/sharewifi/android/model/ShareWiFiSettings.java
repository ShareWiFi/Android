package com.tbaehr.sharewifi.android.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.tbaehr.sharewifi.android.R;

/**
 * Created by tbaehr on 19.04.16.
 */
public class ShareWiFiSettings {

    private final String KEY_NOTIFICATIONS_ENABLED;

    private final String KEY_INFO_TOUR_COMPLETED = "info_tour_completed";

    private SharedPreferences mSharedPreferences;

    public ShareWiFiSettings(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        KEY_NOTIFICATIONS_ENABLED = context.getString(R.string.key_settings_notifications_notify_share);
    }

    public void setNotificationsEnabled(boolean enabled) {
        putBoolean(KEY_NOTIFICATIONS_ENABLED, enabled);
    }

    public boolean isNotificationsEnabled() {
        return getBoolean(KEY_NOTIFICATIONS_ENABLED, true);
    }

    public void setInfoTourCompleted() {
        putBoolean(KEY_INFO_TOUR_COMPLETED, true);
    }

    public boolean isInfoTourCompleted() {
        return getBoolean(KEY_INFO_TOUR_COMPLETED, false);
    }

    private void putBoolean(String key, boolean value) {
        mSharedPreferences.edit().putBoolean(key, value).apply();
    }

    private boolean getBoolean(String key, boolean defaultValue) {
        return mSharedPreferences.getBoolean(key, defaultValue);
    }
}
