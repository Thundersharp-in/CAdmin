package com.thundersharp.cadmin.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.thundersharp.cadmin.ui.activity.MainActivity;
import com.thundersharp.cadmin.R;

import static com.thundersharp.cadmin.ui.activity.MainActivity.floatingActionButton;

public class Profile extends Fragment {

    boolean editable = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_profile, container, false);
        MainActivity.container.setBackground(null);
        floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_edit_24,getActivity().getTheme()));
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editable){
                    editable=false;
                    floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_edit_24,getActivity().getTheme()));
                    Toast.makeText(getContext(),"Profile edit disabled",Toast.LENGTH_SHORT).show();

                }else {
                    editable = true;
                    Toast.makeText(getContext(),"Profile edit enabled",Toast.LENGTH_SHORT).show();
                    floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_save_24,getActivity().getTheme()));
                }

            }
        });

        return root;
    }
}