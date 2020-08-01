package com.thundersharp.cadmin.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.core.globalAdapters.OrganisationAdapter;
import com.thundersharp.cadmin.core.globalmodels.Organisations;
import com.thundersharp.cadmin.core.globalmodels.org_details_model;
import com.thundersharp.cadmin.ui.activity.MainActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.thundersharp.cadmin.ui.activity.MainActivity.floatingActionButton;

public class Organisation extends Fragment {

    RecyclerView project_rv;
    List<org_details_model> data;
    List<Organisations> finalorg;
    SharedPreferences preferences,sharedPreferencesOrglist;
    SwipeRefreshLayout refresh;
    ProgressBar progressorg;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
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
        refresh=root.findViewById(R.id.refresh);
        refresh.setRefreshing(true);
        progressorg=root.findViewById(R.id.progressorg);
        progressorg.setVisibility(View.GONE);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchProfilefromsever();
            }
        });

        List<Organisations> datapref = loadDataOrgfromPrefs();


        if (datapref == null){
            progressorg.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(),"profile server",Toast.LENGTH_SHORT).show();
            fetchProfilefromsever();
            progressorg.setVisibility(View.GONE);
        }else {
            progressorg.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(),"data server",Toast.LENGTH_SHORT).show();

            //fetchListofAllOrganisation(datapref);

            getorgdetailfromPref(loadDataOrgfromPrefs());
            refresh.setRefreshing(false);
            progressorg.setVisibility(View.GONE);
        }

        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equalsIgnoreCase("refreshPref")){
                    progressorg.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(),"command recieved",Toast.LENGTH_LONG).show();
                    List<Organisations> datapref = loadDataOrgfromPrefs();
                    getorgdetailfromPref(loadDataOrgfromPrefs());
                    getActivity().recreate();
                    progressorg.setVisibility(View.GONE);
                }
            }
        };

        getActivity().registerReceiver(broadcastReceiver,new IntentFilter("refreshPref"));

        return root;
    }

    private void fetchListofAllOrganisation(@NonNull final List<Organisations> organisations) {

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
                        //dataorg.clear();
                        dataorg.add(snapshot.getValue(org_details_model.class));
                        savefetchListofAllOrganisation(dataorg);
                        //Toast.makeText(getContext(),String.valueOf(dataorg.size()),Toast.LENGTH_SHORT).show();
                    }
                    OrganisationAdapter organisationAdapter = new OrganisationAdapter(getActivity(),dataorg,organisations);
                    project_rv.setAdapter(organisationAdapter);
                    refresh.setRefreshing(false);
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }
        //Toast.makeText(getActivity(),String.valueOf(dataorg.size()),Toast.LENGTH_SHORT).show();
    }

    private void getorgdetailfromPref(@NonNull  List<Organisations>  model){

        List<org_details_model> datapref = loadOrgdetailfromPrefs();

        if (datapref==null){
            //Data not found on shared prefs checking server
            fetchListofAllOrganisation(model);
        }else{
            //Data found on shared prefs
            refresh.setRefreshing(false);
            List<org_details_model> dataorg = new  ArrayList<>();
            Gson gson=new Gson();
            for (int i=0;i<model.size();i++){
                String data=sharedPreferencesOrglist.getString("org","null");
                Type type = new TypeToken<ArrayList<org_details_model>>(){}.getType();

                dataorg =  gson.fromJson(data,type);

                OrganisationAdapter organisationAdapter =new OrganisationAdapter(getActivity(),dataorg,model);
                project_rv.setAdapter(organisationAdapter);
        }

            //dataorg.add(loadOrgdetailfromPrefs().size(),org_details_model.class);
            // OrganisationAdapter organisationAdapter = new OrganisationAdapter(getActivity(),dataorg);
            //                    project_rv.setAdapter(organisationAdapter);
        }
    }


    private void fetchProfilefromsever(){

        finalorg.clear();
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
                }else {
                    SavetoSharedPrefs(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private List<org_details_model> loadOrgdetailfromPrefs(){
        String data;
        Gson gson=new Gson();
        data=sharedPreferencesOrglist.getString("org","null");
        if (!data.equals("null")){  //equalsIgnoreCase("null")
            Type type=new TypeToken<ArrayList<org_details_model>>(){}.getType();
            return gson.fromJson(data,type);
        }else return null;
    }

    private List<Organisations> loadDataOrgfromPrefs(){

        String data;
        Gson gson = new Gson();
        data = preferences.getString("id","null");

        if (!data.equals("null")){  //equalsIgnoreCase("null")
            Type type = new TypeToken<ArrayList<Organisations>>(){}.getType();
            return gson.fromJson(data,type);
        }else
            return null;

    }

    private List<Organisations> getData(){

        Gson gson =new Gson();
        List<Organisations> dummy;

        if (!preferences.getString("id","null").equals("null")){
            String data = preferences.getString("id","null");
            Type type = new TypeToken<ArrayList<Organisations>>(){}.getType();
            dummy =  gson.fromJson(data,type);

        }else{
            dummy = new ArrayList<>();
        }

        return dummy;
    }

    private void savefetchListofAllOrganisation(List<org_details_model> org_details_models){

        Gson gson = new Gson();

        String data=gson.toJson(org_details_models);
        SharedPreferences.Editor editor = sharedPreferencesOrglist.edit();
        editor.clear();
        editor.putString("org",data);
        editor.apply();
    }

    private void SavetoSharedPrefs(List<Organisations> organisations){

        Gson gson = new Gson();

        //List<Organisations> dataprevious = getData();

        String data = gson.toJson(organisations);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.putString("id",data);
        editor.apply();
        if (organisations == null){
            SharedPreferences.Editor editor3 = getActivity().getSharedPreferences("selected_org",Context.MODE_PRIVATE).edit();
            SharedPreferences.Editor editor2=sharedPreferencesOrglist.edit();
            SharedPreferences.Editor editor1 = preferences.edit();
            editor2.clear();
            editor2.apply();

            editor3.clear();
            editor3.apply();

            editor1.clear();
            editor1.apply();
            Toast.makeText(getActivity(),"No data to display",Toast.LENGTH_LONG).show();
            refresh.setRefreshing(false);

        }else fetchListofAllOrganisation(organisations);

   /*     if (dataprevious == null || dataprevious.isEmpty()){
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

            getorgdetailfromPref(organisations);
        }*/

    }

}