package com.thundersharp.cadmin.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.core.globalAdapters.OrganisationAdapter;
import com.thundersharp.cadmin.core.globalmodels.Organisations;
import com.thundersharp.cadmin.core.globalmodels.UserData;
import com.thundersharp.cadmin.core.globalmodels.org_details_model;
import com.thundersharp.cadmin.ui.activity.MainActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.http.GET;

import static com.thundersharp.cadmin.ui.activity.MainActivity.floatingActionButton;

public class Organisation extends Fragment {

    TextView company_name,company_detail,manager_name;
    ImageView company_logo;
    Button project_done;
    RecyclerView project_rv;
    FloatingActionButton add_project;
    DatabaseReference mRef,mRef1;
    List<org_details_model> data;
    List<Organisations>finalorg;
    SharedPreferences preferences,sharedPreferencesOrglist;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
       // org_details_model homepagemodel = (org_details_model) getIntent().getSerializableExtra("match_data");
        MainActivity.container.setBackground(null);
        floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_black_24dp,getActivity().getTheme()));
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.navController.navigate(R.id.nav_org);
            }
        });

        data=new ArrayList<>();
        finalorg = new ArrayList<>();
        preferences= getActivity().getSharedPreferences("org", Context.MODE_PRIVATE);
        sharedPreferencesOrglist = getActivity().getSharedPreferences("all_organisation",Context.MODE_PRIVATE);
        project_rv=root.findViewById(R.id.project_rv);

        List<Organisations> datapref = loadDataOrgfromPrefs();
        //Toast.makeText(getActivity(),String.valueOf(datapref.size()),Toast.LENGTH_SHORT).show();
        if (datapref == null){
            Toast.makeText(getActivity(),"profile server",Toast.LENGTH_SHORT).show();
            fetchProfilefromsever();
        }else {
            Toast.makeText(getActivity(),"data server",Toast.LENGTH_SHORT).show();
            //fetchListofAllOrganisation(datapref);
            fetchListofAllOrganisation(loadDataOrgfromPrefs());

        }




        return root;
    }

    private void fetchListofAllOrganisation(@NonNull List<Organisations> organisations) {

        final List<org_details_model> dataorg = new ArrayList<>();

        for (int i=0;i<organisations.size();i++){

            FirebaseDatabase
                    .getInstance()
                    .getReference("organisation")
                    .child(organisations.get(i).getOrganisationKey())
                    .child("description")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        dataorg.add(snapshot.getValue(org_details_model.class));
                        Toast.makeText(getActivity(),String.valueOf(dataorg.size()),Toast.LENGTH_SHORT).show();
                        OrganisationAdapter organisationAdapter = new OrganisationAdapter(getActivity(),dataorg);
                        project_rv.setAdapter(organisationAdapter);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }

        //Toast.makeText(getActivity(),String.valueOf(dataorg.size()),Toast.LENGTH_SHORT).show();


    }

    private void savefetchListofAllOrganisation(List<org_details_model> org_details_models){

        Gson gson = new Gson();
        String data=gson.toJson(org_details_models);
        SharedPreferences.Editor editor = sharedPreferencesOrglist.edit();
        editor.clear();
        editor.putString("org",data);
        editor.apply();

    }


    private void fetchProfilefromsever(){
        FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getUid())
                .child("organisations")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Organisations organisations = new Organisations(dataSnapshot.getKey(),dataSnapshot.getValue(Boolean.class));
                        finalorg.add(organisations);
                    }
                    SavetoSharedPrefs(finalorg);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private List<Organisations> loadDataOrgfromPrefs(){

        String data;
        Gson gson = new Gson();
        data = preferences.getString("id","null");
        if (data.equalsIgnoreCase("null")){
            Type type = new TypeToken<ArrayList<Organisations>>(){}.getType();
            return gson.fromJson(data,type);
        }else return null;

    }

    private void SavetoSharedPrefs(List<Organisations> organisations){

        Gson gson = new Gson();
        List<Organisations> dataprevious = getData();
        if (dataprevious == null){

            String data = gson.toJson(organisations);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.putString("id",data);
            editor.apply();
            fetchListofAllOrganisation(organisations);

        }else {

            dataprevious.addAll(organisations);

            String data = gson.toJson(dataprevious);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.putString("id",data);
            editor.apply();

            fetchListofAllOrganisation(organisations);
        }

    }


    private List<Organisations> getData(){

        Gson gson =new Gson();
        List<Organisations> dummy;

        if (preferences.getString("id","null").equals("null")){
            String data = preferences.getString("id","null");
            Type type = new TypeToken<ArrayList<Organisations>>(){}.getType();
            dummy =  gson.fromJson(data,type);

        }else{
            dummy = new ArrayList<>();
        }

        return dummy;
    }


}