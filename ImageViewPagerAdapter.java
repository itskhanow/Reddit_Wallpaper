package com.itskhanow.redditwallpaper.wallpaperchanger;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.io.File;

/**
 *
 */
public class ImageViewPagerAdapter extends FragmentStatePagerAdapter {
    private File[] images;

    public ImageViewPagerAdapter(FragmentManager fm) {
        super(fm);
        File dir = new File(Environment.getExternalStorageDirectory().toString() + "/reddit_wallpaper/");
        if (dir.isDirectory()) {
            images = dir.listFiles();
        }
    }

    @Override
    public Fragment getItem(int i) {
        ImageViewFragment fragment = new ImageViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("file_path", images[i].getAbsolutePath());
        Log.i("getItem", "Path: " + bundle.getString("file_path"));
        fragment.setArguments(bundle);
        return fragment;
    }

    public String getImagePath(int pos) {
        return images[pos].getAbsolutePath();
    }

    @Override
    public int getCount() {
        return images.length;
    }
}
