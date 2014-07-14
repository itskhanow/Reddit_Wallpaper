package com.itskhanow.redditwallpaper.wallpaperchanger;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName(BuildConfig.PACKAGE_NAME);
        addPreferencesFromResource(R.xml.settings);
    }

}
