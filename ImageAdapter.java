package com.itskhanow.redditwallpaper.wallpaperchanger;

import android.content.Context;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

/**
 *
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private File dir;
    private File[] images;

    public ImageAdapter(Context c) {
        mContext = c;
        dir = new File(Environment.getExternalStorageDirectory().toString() + "/reddit_wallpaper/");
        if (dir.isDirectory()) {
            images = dir.listFiles();
        }
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public File getItem(int i) {
        return images[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ImageView view = (ImageView) convertView;
        if (view == null) {
            view = new ImageView(mContext);
            view.setLayoutParams(new GridView.LayoutParams(90, 90));
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setPadding(8, 8, 8, 8);
        }
        Picasso.with(mContext).load(getItem(position)).resize(90, 90).centerCrop().into(view);
        return view;
    }
}
