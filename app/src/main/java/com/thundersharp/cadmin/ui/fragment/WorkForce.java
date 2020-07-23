package com.thundersharp.cadmin.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.ui.activity.MainActivity;

import static com.thundersharp.cadmin.ui.activity.MainActivity.floatingActionButton;

public class WorkForce extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        MainActivity.container.setBackground(null);
        floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_chat_bubble_outline_24,getActivity().getTheme()));
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.navController.navigate(R.id.nav_chats);
            }
        });

        View view = inflater.inflate(R.layout.fragment_work_force, container, false);


        return view;
    }
}