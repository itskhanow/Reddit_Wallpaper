package com.itskhanow.redditwallpaper.wallpaperchanger;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.HashSet;

public class MainActivity extends ActionBarActivity {
    protected SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences(BuildConfig.PACKAGE_NAME, Context.MODE_PRIVATE);

        // If it's the first run, add some default subreddits
        if (prefs.getBoolean(AppConstants.PREF_FIRST_RUN, true)) {
            HashSet<String> subreddits = new HashSet<String>();
            subreddits.add("wallpaper");
            subreddits.add("wallpapers");
            SharedPreferences.Editor editor = prefs.edit();
            editor.putStringSet(AppConstants.PREF_SUBREDDITS, subreddits);
            editor.putBoolean(AppConstants.PREF_SERVICE_STARTED, false);
            editor.putBoolean(AppConstants.PREF_FIRST_RUN, false);
            editor.commit();
        }

        startServices();

        // TODO: Implement sidebar to manage subreddits
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                startService(new Intent(getApplicationContext(), ImagePoolUpdater.class));
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startServices() {
        if (!prefs.getBoolean(AppConstants.PREF_SERVICE_STARTED, false)) {
            ServiceManager.startServices(this);
        }
    }

}
