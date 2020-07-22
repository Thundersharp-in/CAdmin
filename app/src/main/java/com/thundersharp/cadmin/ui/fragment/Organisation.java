package com.thundersharp.cadmin.ui.fragment;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.notes.model.org_details_model;
import com.thundersharp.cadmin.ui.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

import static com.thundersharp.cadmin.ui.activity.MainActivity.floatingActionButton;

public class Organisation extends Fragment {

    TextView company_name,company_detail,manager_name,manager_uid,company_id;
    ImageView company_logo;
    //Button project_done;
    FloatingActionButton add_project;
    DatabaseReference mRef,mRef1;
    List<org_details_model> data;
    RelativeLayout org1;

   // FrameLayout frame;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
       // org_details_model homepagemodel = (org_details_model) getIntent().getSerializableExtra("match_data");
        MainActivity.container.setBackground(null);
        floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_black_24dp,getActivity().getTheme()));
        org1=root.findViewById(R.id.org1);
        company_name=root.findViewById(R.id.company_name);
        company_detail=root.findViewById(R.id.company_detail);
        company_logo=root.findViewById(R.id.company_logo);
        add_project=root.findViewById(R.id.add_project);
        manager_name=root.findViewById(R.id.manager_name);
        manager_uid=root.findViewById(R.id.manager_uid);
        company_id=root.findViewById(R.id.company_id);
        org1.setVisibility(View.GONE);
        data=new ArrayList<>();
        SharedPreferences preferences= this.getActivity().getSharedPreferences("org",0);

                //requireActivity().getSharedPreferences("id",0);
        final String l1 =preferences.getString("id","No value");
        mRef= FirebaseDatabase.getInstance().getReference("organisation1");
        mRef1=
                mRef
                .child(l1)
                .child("description");

        mRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if (dataSnapshot.exists()){
                   org_details_model model = dataSnapshot.getValue(org_details_model.class);
                   data.add(model);
                   org1.setVisibility(View.VISIBLE);
                   company_name.setText(model.getOrganisation_name());
                   company_detail.setText(model.getCompany_description());
                   String logo=model.getCompany_logo();
                   company_logo.setImageURI(Uri.parse(logo));
                   manager_name.setText(model.getOrganiser_name());
                   Toast.makeText(getContext(), "All Extracted", Toast.LENGTH_SHORT).show();
               }else {
                   Toast.makeText(getContext(), "none extracted", Toast.LENGTH_SHORT).show();
               }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.navController.navigate(R.id.nav_org);
            }
        });


        return root;
    }

    @Override
    public void onStart() {
        org1.setVisibility(View.GONE);
        super.onStart();

    }
}