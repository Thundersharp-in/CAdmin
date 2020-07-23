package com.thundersharp.cadmin.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.thundersharp.cadmin.ui.activity.Login_reg;
import com.thundersharp.cadmin.ui.activity.MainActivity;
import com.thundersharp.cadmin.R;

import static com.thundersharp.cadmin.ui.activity.MainActivity.floatingActionButton;

public class Profile extends Fragment {

    boolean editable = false;
    Button logout;

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

        logout = root.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                        .setCancelable(false)
                        .setTitle("Logout !!")
                        .setMessage("Do you really want to logout from your account?")
                        .setPositiveButton("LOGOUT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseAuth.getInstance().signOut();
                        getActivity().finish();
                        startActivity(new Intent(getActivity(), Login_reg.class));

                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();

            }
        });

        return root;
    }
}