package com.thundersharp.cadmin.ui.fragment.organisationinfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.thundersharp.cadmin.R;

public class Users extends Fragment {

    ImageView org_imageView;
    TextView org_textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View root=inflater.inflate(R.layout.fragment_users, container, false);
        org_imageView = root.findViewById(R.id.org_imageView_users);
        org_textView = root.findViewById(R.id.org_tv_users);

        org_imageView.setVisibility(View.VISIBLE);
        org_imageView.setImageResource(R.drawable.sad);
        org_textView.setVisibility(View.VISIBLE);

        return root;
    }
}