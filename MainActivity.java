package com.itskhanow.redditwallpaper.wallpaperchanger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

public class MainActivity extends ActionBarActivity {
    protected GridView gridView;
    protected ImageAdapter imageAdapter;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshGrid();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startServices();

        gridView = (GridView) findViewById(R.id.gridview);
        refreshGrid();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent iViewImage = new Intent(getApplicationContext(), ViewImageActivity.class);
                iViewImage.putExtra(AppConstants.INTENT_EXTRA_POSITION, position);
                startActivity(iViewImage);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

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
        SharedPreferences prefs = getSharedPreferences(BuildConfig.PACKAGE_NAME, Context.MODE_PRIVATE);
        if (!prefs.getBoolean(AppConstants.PREF_SERVICE_STARTED, false)) {
            ServiceManager.startServices(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(AppConstants.BROADCAST_UPDATED));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    protected  void refreshGrid() {
        imageAdapter = new ImageAdapter(this);
        gridView.setAdapter(imageAdapter);
    }
}
