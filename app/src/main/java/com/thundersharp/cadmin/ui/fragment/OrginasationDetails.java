package com.thundersharp.cadmin.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
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
    CircleImageView org_logo1;
    TextView projtittle1,descwhole1,no_of_employee,no_of_projects,no_of_managers;
    Button btn_mail_manager,edit_org;
    org_details_model data;
    Organisations orgs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_orginasation_details, container, false);
        MainActivity.container.setBackground(null);
        floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_delete_outline_24,getActivity().getTheme()));

       // final org_details_model data = savedInstanceState.getParcelable("data");
       // final Organisations orgs=savedInstanceState.getParcelable("orgs");
         Bundle bundle = new Bundle();
        //bundle.getSerializable("data");
        data=(org_details_model) bundle.getSerializable("data") ;//savedInstanceState.getSerializable("data")
        orgs=(Organisations)bundle.getSerializable("orgs");

        if (data==null){
            Toast.makeText(getContext(),"no data found", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getContext(), data.getOrganisation_name().toString(), Toast.LENGTH_SHORT).show();
        }

        org_logo1=root.findViewById(R.id.org_logo1);
        projtittle1=root.findViewById(R.id.projtittle1);
        descwhole1=root.findViewById(R.id.descwhole1);
        no_of_employee=root.findViewById(R.id.no_of_employee);
        no_of_projects=root.findViewById(R.id.no_of_projects);
        no_of_managers=root.findViewById(R.id.no_of_managers);
        btn_mail_manager=root.findViewById(R.id.btn_mail_manager);
        edit_org=root.findViewById(R.id.edit_org);


       // projtittle1.setText(data.getOrganisation_name());
        //descwhole1.setText(data.getCompany_description());

        Toast.makeText(getContext(), projtittle1.getText().toString(), Toast.LENGTH_SHORT).show();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (orgs.isManager()){
                    //Toast.makeText(getActivity(), "You have deleted the noe from everywhere", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(),"Sorry you can't delete this organisation !",Toast.LENGTH_SHORT).show();
                }
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
}