package com.thundersharp.cadmin.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.thundersharp.cadmin.calculators.CalculatorNorm;
import com.thundersharp.cadmin.ui.activity.MainActivity;
import com.thundersharp.cadmin.R;

import static com.thundersharp.cadmin.ui.activity.MainActivity.floatingActionButton;

public class Finance extends Fragment {

    RelativeLayout calci,calnorm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_finance, container, false);
        MainActivity.container.setBackground(null);
        floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_chat_bubble_outline_24,getActivity().getTheme()));
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Chat comming soon",Toast.LENGTH_SHORT).show();
            }
        });

        calci = view.findViewById(R.id.calci);
        calnorm=view.findViewById(R.id.calnorm);
        calci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CalculatorNorm.class));
            }
        });

        calnorm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Calendar.class));
            }
        });

        return view;
    }
}