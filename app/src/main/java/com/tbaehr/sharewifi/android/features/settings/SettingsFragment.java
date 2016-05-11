package com.tbaehr.sharewifi.android.features.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.util.Log;

import com.tbaehr.sharewifi.android.R;

/**
 * Created by tbaehr on 08.05.16.
 */
public class SettingsFragment extends PreferenceFragment {

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
    }

    /**
     * This fragment contains a second-level set of preference that you
     * can get to by tapping an item in the first preferences fragment.
     */
    public static class Prefs1FragmentInner extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Can retrieve arguments from preference XML.
            Log.i("args", "Arguments: " + getArguments());

            // Load the preferences from an XML resource
            /*addPreferencesFromResource(R.xml.fragmented_preferences_inner);*/
        }
    }

}
