package com.itskhanow.redditwallpaper.wallpaperchanger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ImageGridFragment extends Fragment {
    protected GridView gridView;
    protected ImageAdapter imageAdapter;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshGrid();
        }
    };

    public ImageGridFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_image_grid, container, false);
        gridView = (GridView) rootView.findViewById(R.id.gridview);
        refreshGrid();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent iViewImage = new Intent(getActivity().getApplicationContext(), ImageViewActivity.class);
                iViewImage.putExtra(AppConstants.INTENT_EXTRA_POSITION, position);
                startActivity(iViewImage);
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(receiver, new IntentFilter(AppConstants.BROADCAST_IMAGEPOOL_UPDATED));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
    }

    protected void refreshGrid() {
        imageAdapter = new ImageAdapter(getActivity().getApplicationContext());
        gridView.setAdapter(imageAdapter);
    }

}
