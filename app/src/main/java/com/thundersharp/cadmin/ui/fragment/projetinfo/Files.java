package com.thundersharp.cadmin.ui.fragment.projetinfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.ui.activity.MainActivity;

public class Files extends Fragment {

    ImageView imageView;
    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_files, container, false);
        MainActivity.container.setBackground(null);

        imageView = view.findViewById(R.id.imageView_files);
        textView = view.findViewById(R.id.tv_files);

        imageView.setVisibility(View.VISIBLE);
        imageView.setImageResource(R.drawable.sad);
        textView.setVisibility(View.VISIBLE);

        return view;
    }
}