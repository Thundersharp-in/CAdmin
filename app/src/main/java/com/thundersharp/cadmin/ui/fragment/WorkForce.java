package com.thundersharp.cadmin.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thundersharp.cadmin.ui.activity.MainActivity;
import com.thundersharp.cadmin.R;

public class WorkForce extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        MainActivity.container.setBackground(null);
        return inflater.inflate(R.layout.fragment_work_force, container, false);
    }
}