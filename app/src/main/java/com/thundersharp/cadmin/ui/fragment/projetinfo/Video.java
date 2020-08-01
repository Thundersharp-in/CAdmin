package com.thundersharp.cadmin.ui.fragment.projetinfo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.thundersharp.cadmin.R;

public class Video extends Fragment {

    ImageView imageView;
    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        imageView = view.findViewById(R.id.imageView_video);
        textView = view.findViewById(R.id.tv_video);

        imageView.setVisibility(View.VISIBLE);
        imageView.setImageResource(R.drawable.sad);
        textView.setVisibility(View.VISIBLE);
        return view;
    }
}