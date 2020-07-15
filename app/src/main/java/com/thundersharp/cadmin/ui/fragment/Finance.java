package com.thundersharp.cadmin.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.thundersharp.cadmin.calculators.CalculatorNorm;
import com.thundersharp.cadmin.ui.activity.MainActivity;
import com.thundersharp.cadmin.R;

public class Finance extends Fragment {

    RelativeLayout calci;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_finance, container, false);
        MainActivity.container.setBackground(null);

        calci = view.findViewById(R.id.calci);
        calci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CalculatorNorm.class));
            }
        });

        return view;
    }
}