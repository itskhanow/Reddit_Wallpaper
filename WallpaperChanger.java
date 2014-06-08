package com.itskhanow.redditwallpaper.wallpaperchanger;

import android.app.IntentService;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.Random;


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
            if (intent.hasExtra("image")) {
                try {
                    WallpaperManager.getInstance(getApplicationContext()).setBitmap(BitmapFactory.decodeFile(intent.getStringExtra("image")));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                File dir = new File(Environment.getExternalStorageDirectory().toString() + "/reddit_wallpaper/");
                File[] images = dir.listFiles();
                int i = new Random().nextInt(images.length);
                try {
                    WallpaperManager.getInstance(getApplicationContext()).setBitmap(BitmapFactory.decodeFile(images[i].getAbsolutePath()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
