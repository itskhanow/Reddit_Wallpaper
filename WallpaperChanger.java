package com.itskhanow.redditwallpaper.wallpaperchanger;

import android.app.IntentService;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.WindowManager;

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
        // TODO: Resize image to fit
        if (intent != null) {
            if (intent.hasExtra(AppConstants.INTENT_EXTRA_IMAGE)) {
                setWallpaper(intent.getStringExtra(AppConstants.INTENT_EXTRA_IMAGE));
            } else {
                SharedPreferences prefs = getSharedPreferences(BuildConfig.PACKAGE_NAME, Context.MODE_PRIVATE);
                if (prefs.getBoolean(AppConstants.PREF_CHANGE_WALLPAPER, false)) {
                    File dir = new File(Environment.getExternalStorageDirectory().toString() + AppConstants.IMAGE_DIR);
                    File[] images = dir.listFiles();
                    int i = new Random().nextInt(images.length);
                    setWallpaper(images[i].getAbsolutePath());
                }
            }
        }
    }

    public void setWallpaper(String path) {
        // Get height of device
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        int outWidth;
        int outHeight = metrics.heightPixels;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        // Set width to ratio
        outWidth = (options.outWidth * outHeight) / options.outHeight;
        options.inJustDecodeBounds = false;
        // Create new scaled bitmap whose height is same as device height
        Bitmap bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(path), outWidth, outHeight, false);
        // Set wallpaper
        try {
            WallpaperManager.getInstance(getApplicationContext()).setBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
