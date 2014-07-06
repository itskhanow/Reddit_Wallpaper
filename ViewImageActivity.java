package com.itskhanow.redditwallpaper.wallpaperchanger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 *
 */
public class ViewImageActivity extends FragmentActivity {

    ViewPager viewPager;
    ImageViewPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_image);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        pagerAdapter = new ImageViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(getIntent().getIntExtra("position", 0));
        Log.i("ViewImageActivity", "Position: " + viewPager.getCurrentItem());

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

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
            intent.putExtra("image", pagerAdapter.getImagePath(viewPager.getCurrentItem()));
            startService(intent);
        }
    }

}
