package com.thundersharp.cadmin.ui.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.core.globalAdapters.TabAdapter;
import com.thundersharp.cadmin.core.globalmodels.Organisations;
import com.thundersharp.cadmin.core.globalmodels.org_details_model;
import com.thundersharp.cadmin.ui.activity.MainActivity;
import com.thundersharp.cadmin.ui.fragment.organisationinfo.Photos;
import com.thundersharp.cadmin.ui.fragment.organisationinfo.Users;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.thundersharp.cadmin.ui.activity.MainActivity.floatingActionButton;

public class OrginasationDetails extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;
    TextView detail_org_name,descwhole1,no_of_employee,no_of_projects,no_of_managers;
    Button btn_mail_manager,edit_org;
    //Organisations orgs;
    CircleImageView org_logo12;
    String org_name,org_desc,org_id,org_image,organiser_id;
    int users;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_orginasation_details, container, false);
        MainActivity.container.setBackground(null);
        floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_delete_outline_24,getActivity().getTheme()));
       // final org_details_model data = savedInstanceState.getParcelable("data");
       // final Organisations orgs=savedInstanceState.getParcelable("orgs");


        org_logo12=root.findViewById(R.id.org_logo12);
        detail_org_name=root.findViewById(R.id.detail_org_name);
        descwhole1=root.findViewById(R.id.descwhole1);
        no_of_employee=root.findViewById(R.id.no_of_employee);
        no_of_projects=root.findViewById(R.id.no_of_projects);
        no_of_managers=root.findViewById(R.id.no_of_managers);
        btn_mail_manager=root.findViewById(R.id.btn_mail_manager);
        edit_org=root.findViewById(R.id.edit_org);
        users=0;

        Bundle bundle =this.getArguments();
        if (getArguments()!=null){
            org_name=bundle.getString("org_name");
            org_desc=bundle.getString("org_desc");
            org_id=bundle.getString("org_id");
            org_image=bundle.getString("org_image");
            organiser_id=bundle.getString("organiser_id");

           setDetails(org_name,org_desc,org_id,org_image,organiser_id);
        }else {
            Toast.makeText(getContext(),"no data found", Toast.LENGTH_SHORT).show();
        }

        //data=(org_details_model) bundle.getSerializable("data") ;//savedInstanceState.getSerializable("data")
        //orgs=(Organisations)bundle.getSerializable("orgs");



        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // if (orgs.isManager()){
                    //Alert dilog here
                    //Toast.makeText(getActivity(), "You have deleted the noe from everywhere", Toast.LENGTH_SHORT).show();
               // }else{
                 //   Toast.makeText(getActivity(),"Sorry you can't delete this organisation !",Toast.LENGTH_SHORT).show();
                //}
/*
                FirebaseDatabase.getInstance().getReference("users").child("organisations").child(data.getOrganisation_id()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {


                    }
                });
*/
                MainActivity.navController.navigate(R.id.nav_organisation);
            }
        });
        tabLayout = root.findViewById(R.id.sliding_tabs1);
        viewPager = root.findViewById(R.id.viewpager1);

        TabAdapter tabAdapter = new TabAdapter(getParentFragmentManager());
        tabAdapter.addFragment(new Users(),null);
        tabAdapter.addFragment(new Photos(),null);


        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_supervised_user_circle_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_menu_gallery);

        return root;
    }

    private void setDetails(String org_name, String org_desc, final String org_id,final String org_image, String organiser_id) {
        String organiser_id1=organiser_id;
        String image_view=org_image;
        detail_org_name.setText(org_name);
        descwhole1.setText(org_desc);
        Glide.with(getContext()).load(org_image).into(org_logo12);
        FirebaseDatabase.getInstance()
                .getReference("organisation")
                .child(org_id)
                .child("projects")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot snapshot1:snapshot.getChildren()){
                        long projects_no=snapshot1.getChildrenCount();
                        no_of_projects.setText(Long.toString(projects_no));
                    }
                }
                else {
                    no_of_projects.setText("0");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     *for (final DataSnapshot snapshot2:snapshot1.getChildren()){
     *                         FirebaseDatabase.getInstance().getReference("organisation").child(org_id).child("projects").child(snapshot2.getKey()).child("users_uid").addListenerForSingleValueEvent(new ValueEventListener() {
     *                             @Override
     *                             public void onDataChange(@NonNull DataSnapshot snapshot) {
     *                                 for (DataSnapshot snapshot3:snapshot2.getChildren()){
     *                                     // long i=snapshot3.getChildrenCount();
     *                                     users=users+1;
     *                                 }
     *
     *                             }
     *
     *                             @Override
     *                             public void onCancelled(@NonNull DatabaseError error) {
     *
     *                             }
     *                         });
     *                         users=users+1;
     *                     }
     *                     users=users+1;
     *                     no_of_employee.setText(users);
     */
}