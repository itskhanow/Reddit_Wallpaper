package com.itskhanow.redditwallpaper.wallpaperchanger;

import android.app.IntentService;
import android.content.Intent;


/**
 *
 */
public class WallpaperChanger extends IntentService {

    public WallpaperChanger() {
        super("WallpaperChanger");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            // TODO: Change wallpaper
        }
    }

}
