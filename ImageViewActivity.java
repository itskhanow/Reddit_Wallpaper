package com.itskhanow.redditwallpaper.wallpaperchanger;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 *
 */
public class ImageViewActivity extends FragmentActivity {

    ViewPager viewPager;
    ImageViewPagerAdapter pagerAdapter;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_image);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        pagerAdapter = new ImageViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(getIntent().getIntExtra(AppConstants.INTENT_EXTRA_POSITION, 0));

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT > 16) {
            viewPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
            viewPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_set_wallpaper:
                setWallpaper();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setWallpaper() {
        if (viewPager != null) {
            viewPager.getCurrentItem();
            Intent intent = new Intent(getApplicationContext(), WallpaperChanger.class);
            intent.putExtra(AppConstants.INTENT_EXTRA_IMAGE, pagerAdapter.getImagePath(viewPager.getCurrentItem()));
            startService(intent);
        }
    }

}
