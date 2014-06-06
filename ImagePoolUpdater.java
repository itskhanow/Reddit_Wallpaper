package com.itskhanow.redditwallpaper.wallpaperchanger;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;


/**
 *
 */
public class ImagePoolUpdater extends IntentService {
    public static final String BROADCAST_UPDATED = "com.itskhanow.redditwallpaper.wallpaperchanger.action.UPDATED";
    public static final String DIR = Environment.getExternalStorageDirectory().toString() + "/reddit_wallpaper";

    public ImagePoolUpdater() {
        super("ImagePoolUpdater");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null && connected()) {
            // TODO: Get subreddits from sharedpreferences
            String subreddits = "http://www.reddit.com/r/wallpapers/.json";
            try {
                JSONObject listing = new JSONObject(getListing(subreddits));
                JSONArray links = listing.getJSONObject("data").getJSONArray("children");
                for (int i = 0; i < links.length(); i++) {
                    JSONObject thingData = links.getJSONObject(i).getJSONObject("data");
                    String thingID = thingData.getString("id");
                    String thingURL = thingData.getString("url");
                    saveImage(thingID, thingURL);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // TODO: Broadcast image pool updated
        }
    }

    protected boolean connected() {
        ConnectivityManager cmgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cmgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    protected InputStream downloadStream(String location) {
        InputStream stream = null;
        try {
            URL url = new URL(location);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            stream = new BufferedInputStream(urlConnection.getInputStream());
            urlConnection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stream;
    }

    protected String getListing(String url) {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(downloadStream(url)));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    protected void saveImage(String id, String url) {
        String filename = id + ".jpg";
        File file = new File(DIR, filename);
        if (!file.exists()) {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                Bitmap bmp = BitmapFactory.decodeStream(downloadStream(url));
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
