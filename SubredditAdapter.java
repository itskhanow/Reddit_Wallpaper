package com.itskhanow.redditwallpaper.wallpaperchanger;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
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
        mContext.sendBroadcast(new Intent(AppConstants.BROADCAST_SUBREDDIT_UPDATED));
    }

    public void removeSubreddit(int position) {
        subreddits.remove(position);
        mContext.sendBroadcast(new Intent(AppConstants.BROADCAST_SUBREDDIT_UPDATED));
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewGroup view = (ViewGroup) convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = (ViewGroup) inflater.inflate(R.layout.layout_subreddit, null);
        }
        TextView textView = (TextView) view.findViewById(R.id.textView_subreddit);
        ImageButton button = (ImageButton) view.findViewById(R.id.button_remove);
        textView.setText(subreddits.get(position));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeSubreddit(position);
            }
        });
        return view;
    }

    public void persist() {
        Set<String> set = new HashSet<String>();
        set.addAll(subreddits);
        prefs.edit().putStringSet(AppConstants.PREF_SUBREDDITS, set).apply();
    }
}
