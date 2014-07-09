package com.itskhanow.redditwallpaper.wallpaperchanger;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 *
 */
public class ImagePoolUpdater extends IntentService {
    public static final String DIR = Environment.getExternalStorageDirectory().toString() + AppConstants.IMAGE_DIR;

    public ImagePoolUpdater() {
        super("ImagePoolUpdater");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null && connected()) {
            SharedPreferences prefs = getSharedPreferences(BuildConfig.PACKAGE_NAME, Context.MODE_PRIVATE);
            File dir = new File(DIR);
            if (!dir.exists()) {
                //noinspection ResultOfMethodCallIgnored
                dir.mkdir();
            }

            // TODO: Get subreddits from sharedpreferences
            String subreddits = "http://www.reddit.com/r/wallpaper+wallpapers+minimalwallpaper.json";
            try {
                JSONObject listing = new JSONObject(getListing(subreddits));
                JSONArray links = listing.getJSONObject("data").getJSONArray("children");
                for (int i = 0; i < links.length(); i++) {
                    JSONObject thingData = links.getJSONObject(i).getJSONObject("data");
                    String thingID = thingData.getString("id");
                    String thingURL = thingData.getString("url");
                    boolean thingNSFW = thingData.getBoolean("over_18");
                    // Only save image if it's a direct link
                    // TODO: Create methods to grab images from within album link
                    // Only save image if NSFW is allowed or it's SFW
                    if ((thingURL.endsWith(".jpg") || thingURL.endsWith(".png"))
                            && (prefs.getBoolean(AppConstants.PREF_SHOW_NSFW, false) || !thingNSFW)) {
                        saveImage(thingID, thingURL);
                    }
                    // TODO: Show progress bar notification
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            sendBroadcast(new Intent(AppConstants.BROADCAST_UPDATED));
            if (prefs.getBoolean(AppConstants.PREF_CLEAR_OLD, false)) {
                clearOld();
            }
        }
    }

    protected void clearOld() {
        File[] images = new File(DIR).listFiles();
        for (File image : images) {
            // Check if image is older than a day
            if (image.lastModified() + 86400000 < System.currentTimeMillis()) {
                //noinspection ResultOfMethodCallIgnored
                image.delete();
            }
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
            OutputStream fos = null;
            try {
                fos = new BufferedOutputStream(new FileOutputStream(file));
                Bitmap bmp = BitmapFactory.decodeStream(downloadStream(url));
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null) fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
