package com.thundersharp.cadmin.ui.fragment;

import android.annotation.SuppressLint;
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
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.notes.adapters.OrganisationAdapter;
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
    DatabaseReference mRef,mRef1,reference;
    List<org_details_model> data;
    FirebaseUser user;
    String uid;
    RelativeLayout org1;
    RecyclerView project_rv;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
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
        project_rv=root.findViewById(R.id.project_rv);

        user= FirebaseAuth.getInstance().getCurrentUser();
        uid=user.getUid();

        org1.setVisibility(View.GONE);

        data=new ArrayList<>();

        SharedPreferences preferences= this.getActivity().getSharedPreferences("org",0);
        final String l1 =preferences.getString("id","No value");

        final DatabaseReference reference2=FirebaseDatabase.getInstance().getReference("organisations");

        reference= FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("organisations");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             if (dataSnapshot.exists()){
                 reference2.addValueEventListener(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        // Toast.makeText(getContext(), "Something happend", Toast.LENGTH_SHORT).show();
                     }

                     @Override
                     public void onCancelled(@NonNull DatabaseError databaseError) {

                     }
                 });
             }  else {
                 Toast.makeText(getContext(), "Error 404", Toast.LENGTH_SHORT).show();
             }
                OrganisationAdapter adapter=new OrganisationAdapter(getActivity(),data);
             project_rv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if (l1!=null){
            reference.child(l1).addValueEventListener(new ValueEventListener() {
                @SuppressLint("RestrictedApi")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        String  value=dataSnapshot.getValue().toString();
                        if (value.equals("true")){
                            org1.setVisibility(View.VISIBLE);
                            add_project.setVisibility(View.VISIBLE);

                            mRef= FirebaseDatabase.getInstance().getReference().child("organisation1");
                            mRef1=
                                    mRef
                                            .child(l1)
                                            .child("description");


                            mRef1.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                                    if (dataSnapshot1.exists()){
                                        org_details_model model = dataSnapshot1.getValue(org_details_model.class);
                                        data.add(model);
                                        manager_uid.setText(model.getOrganiser_uid());
                                        company_id.setText(model.getOrganisation_id());
                                        company_name.setText(model.getOrganisation_name());
                                        company_detail.setText(model.getCompany_description());
                                        String logo=model.getCompany_logo();
                                        if (!logo.equals("")){
                                            company_logo.setImageURI(Uri.parse(logo));
                                        }
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


                        }else{
                            org1.setVisibility(View.VISIBLE);
                            add_project.setVisibility(View.GONE);
                            mRef= FirebaseDatabase.getInstance().getReference().child("organisation1");
                            mRef1=
                                    mRef
                                            .child(l1)
                                            .child("description");


                            mRef1.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                                    if (dataSnapshot1.exists()){
                                        org_details_model model = dataSnapshot1.getValue(org_details_model.class);
                                        data.add(model);
                                        manager_uid.setText(model.getOrganiser_uid());
                                        company_id.setText(model.getOrganisation_id());
                                        company_name.setText(model.getOrganisation_name());
                                        company_detail.setText(model.getCompany_description());
                                        String logo=model.getCompany_logo();
                                        if (!logo.equals("")){
                                            company_logo.setImageURI(Uri.parse(logo));
                                        }
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
                        }

                    }else {
                        Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else{
            Toast.makeText(getContext(), "You Don't have any organisation access !", Toast.LENGTH_SHORT).show();
        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.navController.navigate(R.id.nav_org);
            }
        });

        return root;
    }



    @SuppressLint("RestrictedApi")
    @Override
    public void onStart() {
        org1.setVisibility(View.GONE);
        add_project.setVisibility(View.GONE);
        super.onStart();

    }
}