package com.tbaehr.sharewifi.android.features.settings;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.tbaehr.sharewifi.android.R;

/**
 * Created by tbaehr on 08.05.16.
 */
public class SettingsFragment extends PreferenceFragment {

    private CheckBoxPreference mCheckBoxSourcesUnknownUsers, mCheckBoxSourcesNonEncrypted;
    private CheckBoxPreference mCheckBoxAutoConnectAlways, mCheckBoxAutoConnectConfirm, mCheckBoxAutoConnectGroups, mCheckBoxAutoConnectEncrypted;
    private CheckBoxPreference mCheckBoxNotificationsNotifyNetDetected, mCheckBoxNotificationsNotifyShare;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Make sure default values are applied.  In a real app, you would
        // want this in a shared function that is used to retrieve the
        // SharedPreferences wherever they are needed.
            /*PreferenceManager.setDefaultValues(getActivity(),
                    R.xml.advanced_preferences, false);*/

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.fragmented_preferences);

        // bind prefence views
        mCheckBoxSourcesUnknownUsers = (CheckBoxPreference) findPreference(getString(R.string.key_settings_sources_unknown_users));
        mCheckBoxSourcesNonEncrypted = (CheckBoxPreference) findPreference(getString(R.string.key_settings_sources_nonencrypted));

        mCheckBoxAutoConnectAlways = (CheckBoxPreference) findPreference(getString(R.string.key_settings_auto_connect_always));
        mCheckBoxAutoConnectConfirm = (CheckBoxPreference) findPreference(getString(R.string.key_settings_auto_connect_confirm));
        mCheckBoxAutoConnectGroups = (CheckBoxPreference) findPreference(getString(R.string.key_settings_auto_connect_groups));
        mCheckBoxAutoConnectEncrypted = (CheckBoxPreference) findPreference(getString(R.string.key_settings_auto_connect_encrypted_networks));

        mCheckBoxNotificationsNotifyNetDetected = (CheckBoxPreference) findPreference(getString(R.string.key_settings_notifications_notify_net_detected));
        mCheckBoxNotificationsNotifyShare = (CheckBoxPreference) findPreference(getString(R.string.key_settings_notifications_notify_share));

        // disable pref elements if necessary
        mCheckBoxNotificationsNotifyNetDetected.setEnabled(!mCheckBoxAutoConnectAlways.isChecked());

        // set click listener if necessary
        mCheckBoxAutoConnectAlways.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (((Boolean) newValue)) {
                    mCheckBoxAutoConnectConfirm.setChecked(false);
                    mCheckBoxAutoConnectEncrypted.setChecked(true);
                    mCheckBoxAutoConnectGroups.setChecked(true);
                    mCheckBoxNotificationsNotifyNetDetected.setChecked(false);
                    mCheckBoxNotificationsNotifyNetDetected.setEnabled(false);
                } else {
                    mCheckBoxAutoConnectConfirm.setChecked(true);
                    mCheckBoxAutoConnectEncrypted.setChecked(false);
                    mCheckBoxAutoConnectGroups.setChecked(false);
                    mCheckBoxNotificationsNotifyNetDetected.setChecked(true);
                    mCheckBoxNotificationsNotifyNetDetected.setEnabled(true);
                }
                return true;
            }
        });
        mCheckBoxAutoConnectConfirm.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean checked = (Boolean) newValue;
                mCheckBoxNotificationsNotifyNetDetected.setChecked(checked);
                return true;
            }
        });

        mCheckBoxNotificationsNotifyNetDetected.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean checked = (Boolean) newValue;
                mCheckBoxAutoConnectConfirm.setChecked(checked);
                return true;
            }
        });
    }

}
