package com.itskhanow.redditwallpaper.wallpaperchanger;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.io.File;

/**
 *
 */
public class ImageViewPagerAdapter extends FragmentStatePagerAdapter {
    private File[] images;

    public ImageViewPagerAdapter(FragmentManager fm) {
        super(fm);
        File dir = new File(Environment.getExternalStorageDirectory().toString() + AppConstants.IMAGE_DIR);
        if (dir.isDirectory()) {
            images = dir.listFiles();
        }
    }

    @Override
    public Fragment getItem(int i) {
        ImageViewFragment fragment = new ImageViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.ARG_FILE, images[i].getAbsolutePath());
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
