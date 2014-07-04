package com.itskhanow.redditwallpaper.wallpaperchanger;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 *
 */
public class ServiceManager {
    public static void startServices(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("com.itskhanow.redditwallpaper.wallpaperchanger", Context.MODE_PRIVATE);
        // Set the AlarmManager to run the service that updates the image pool
        Intent iUpdateImagePool = new Intent(context, ImagePoolUpdater.class);
        PendingIntent piUpdateImagePool = PendingIntent.getService(context, 0, iUpdateImagePool, 0);
        AlarmManager amUpdateImagePool = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        amUpdateImagePool.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                AlarmManager.INTERVAL_HALF_DAY,
                piUpdateImagePool);

        // Set the AlarmManager to run the service to periodically change the wallpaper
        if (prefs.getBoolean("pref_change_wallpaper_periodically", false)) {
            Intent iChangeWallpaper = new Intent(context, WallpaperChanger.class);
            PendingIntent piChangeWallpaper = PendingIntent.getService(context, 0, iChangeWallpaper, 0);
            AlarmManager amChangeWallpaper = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            amChangeWallpaper.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                    prefs.getLong("pref_update_interval", AlarmManager.INTERVAL_HALF_HOUR),
                    prefs.getLong("pref_update_interval", AlarmManager.INTERVAL_HALF_HOUR),
                    piChangeWallpaper);
        }

        prefs.edit().putBoolean("pref_service_started", true).apply();
    }
}
