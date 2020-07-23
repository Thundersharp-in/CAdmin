package com.thundersharp.cadmin.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.VisibleForTesting;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.thundersharp.cadmin.core.calculators.CalculatorNorm;
import com.thundersharp.cadmin.core.calendar.MainActivityCalander;
import com.thundersharp.cadmin.ui.activity.CurrencyConverter;
import com.thundersharp.cadmin.ui.activity.MainActivity;
import com.thundersharp.cadmin.R;

import static com.thundersharp.cadmin.ui.activity.MainActivity.floatingActionButton;

public class Finance extends Fragment {

    private static final int REQUEST_CODE_CALENDAR = 1;
    RelativeLayout calci,calnorm,convq;

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
                MainActivity.navController.navigate(R.id.nav_chats);
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
                if (checkCalendarPermissions()){
                    startActivity(new Intent(getActivity(), MainActivityCalander.class));
                }else {
                    requestCalendarPermissions();
                }

            }
        });

        convq=view.findViewById(R.id.convq);
        convq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CurrencyConverter.class));
            }
        });

        return view;
    }

    @VisibleForTesting
    protected boolean checkCalendarPermissions() {
        return (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CALENDAR) |
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_CALENDAR)) ==
                PackageManager.PERMISSION_GRANTED;
    }




    @VisibleForTesting
    protected void requestCalendarPermissions() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{
                        Manifest.permission.READ_CALENDAR,
                        Manifest.permission.WRITE_CALENDAR},
                REQUEST_CODE_CALENDAR);
    }
}