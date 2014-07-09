package com.itskhanow.redditwallpaper.wallpaperchanger;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class SubredditAdapter extends BaseAdapter {

    private Context mContext;
    private SharedPreferences prefs;
    private ArrayList<String> subreddits;


    public SubredditAdapter(Context c) {
        mContext = c;
        prefs = mContext.getSharedPreferences(BuildConfig.PACKAGE_NAME, Context.MODE_PRIVATE);
        subreddits = new ArrayList<String>(prefs.getStringSet(AppConstants.PREF_SUBREDDITS, null));
    }

    public void addSubreddit(String sub) {
        subreddits.add(sub);
    }

    public void removeSubreddit(int position) {
        subreddits.remove(position);
    }

    @Override
    public int getCount() {
        if (subreddits != null) {
            return subreddits.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return subreddits.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) convertView;
        if (view == null) {
            view = new TextView(mContext);
        }
        view.setText(subreddits.get(position));
        // TODO: Add remove button
        return view;
    }

    public void persist() {
        Set<String> set = new HashSet<String>();
        set.addAll(subreddits);
        prefs.edit().putStringSet(AppConstants.PREF_SUBREDDITS, set).apply();
    }
}
