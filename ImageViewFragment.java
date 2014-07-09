package com.itskhanow.redditwallpaper.wallpaperchanger;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;


/**
 *
 *
 */
public class ImageViewFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String filePath = getArguments().getString(AppConstants.ARG_FILE);
        View rootView = inflater.inflate(R.layout.fragment_image_view, container, false);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.image_view);
        Picasso.with(getActivity().getApplicationContext()).load(new File(filePath)).into(imageView);
        return rootView;
    }

}
