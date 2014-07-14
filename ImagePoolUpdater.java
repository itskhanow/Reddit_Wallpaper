package com.itskhanow.redditwallpaper.wallpaperchanger;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;

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
import java.util.ArrayList;


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
        SharedPreferences prefs = getSharedPreferences(BuildConfig.PACKAGE_NAME, Context.MODE_PRIVATE);
        if (intent != null && connected(prefs)) {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(getString(R.string.notification_text))
                    .setSmallIcon(R.drawable.ic_action_download_image);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int notificationID = 1;
            notificationBuilder.setProgress(1, 0, true);
            notificationManager.notify(notificationID, notificationBuilder.build());

            File dir = new File(DIR);
            if (!dir.exists()) {
                //noinspection ResultOfMethodCallIgnored
                dir.mkdir();
            }

            String subredditsURL = buildURL(prefs);
            try {
                JSONObject listing = new JSONObject(getListing(subredditsURL));
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
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            notificationBuilder.setProgress(0, 0, false);
            notificationManager.notify(notificationID, notificationBuilder.build());
            notificationManager.cancel(notificationID);
        }
        if (prefs.getBoolean(AppConstants.PREF_CLEAR_OLD, false)) {
            clearOld();
        }
        sendBroadcast(new Intent(AppConstants.BROADCAST_IMAGEPOOL_UPDATED));
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

    protected boolean connected(SharedPreferences prefs) {
        boolean connected = false;
        ConnectivityManager cmgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // Check if WiFI is connected or user doesn't want wifi only
        if (cmgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()
                || !prefs.getBoolean(AppConstants.PREF_WIFI_ONLY, false)) {
            // Check if there is a connection at all
            if (cmgr.getActiveNetworkInfo().isConnected()) {
                connected = true;
            }
        }
        return connected;
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

    private String buildURL(SharedPreferences prefs) {
        StringBuilder url = new StringBuilder("http://www.reddit.com/r/");
        ArrayList<String> subreddits = new ArrayList<String>(prefs.getStringSet(AppConstants.PREF_SUBREDDITS, null));
        for (String sub : subreddits) {
            url.append(sub).append("+");
        }
        url.append(".json");
        return url.toString();
    }

}
