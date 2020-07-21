package com.thundersharp.cadmin.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.thundersharp.cadmin.ui.activity.MainActivity;
import com.thundersharp.cadmin.R;

import static com.thundersharp.cadmin.ui.activity.MainActivity.floatingActionButton;
import static com.thundersharp.cadmin.ui.activity.MainActivity.navController;

public class Organisation_cal extends Fragment {

    FloatingActionButton fabchat;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        MainActivity.container.setBackground(null);
        floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_black_24dp,getActivity().getTheme()));
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Add event in calendar coming soon",Toast.LENGTH_SHORT).show();
            }
        });


        fabchat= root.findViewById(R.id.fabchat);
        fabchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.nav_chats);
            }
        });
        return root;
    }
}