package com.itskhanow.redditwallpaper.wallpaperchanger;

import android.content.Context;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

/**
 *
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private File[] images;

    public ImageAdapter(Context c) {
        mContext = c;
        File dir = new File(Environment.getExternalStorageDirectory().toString() + AppConstants.IMAGE_DIR);
        if (dir.isDirectory()) {
            images = dir.listFiles();
        }
    }

    @Override
    public int getCount() {
        if (images != null) {
            return images.length;
        } else {
            return 0;
        }
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
            //view.setLayoutParams(new GridView.LayoutParams(imageSize, imageSize));
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        Picasso.with(mContext).load(getItem(position)).resize(AppConstants.IMAGE_SIZE, AppConstants.IMAGE_SIZE).centerCrop().into(view);
        return view;
    }
}
