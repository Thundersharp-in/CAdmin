package com.thundersharp.cadmin.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.core.globalmodels.Organisations;
import com.thundersharp.cadmin.core.globalmodels.UserData;
import com.thundersharp.cadmin.core.globalmodels.org_details_model;
import com.thundersharp.cadmin.ui.activity.MainActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.thundersharp.cadmin.ui.activity.MainActivity.floatingActionButton;


public class AddOrganisationFragment extends Fragment {

    CircleImageView upload_org_logo;
    TextInputLayout upload_org_name,upload_org_desc,upload_org_motto;
    Button btn_upload_org;
    Uri org_logo_uri;
    String logo_url,org_name,org_description,organiser_name,organiser_uid;
    SharedPreferences pref;
    List<Organisations> organisations;
    UserData userData;
    FirebaseUser mUser;
    SharedPreferences sharedPreferencesprofile,sharedPreference;
    public org_details_model org_details_model_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        sharedPreferencesprofile = getActivity().getSharedPreferences("logindata", Context.MODE_PRIVATE);
        pref =getActivity().getSharedPreferences("org", Context.MODE_PRIVATE);
        sharedPreference = getActivity().getSharedPreferences("all_organisation",Context.MODE_PRIVATE);

        View view=inflater.inflate(R.layout.fragment_add_organisation, container, false);
        MainActivity.container.setBackground(null);
        floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_save_24,getActivity().getTheme()));

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.navController.navigate(R.id.nav_organisation);
            }
        });

        userData = loadDataProfilefromPrefs();
        organisations = new ArrayList<>();

        upload_org_logo=view.findViewById(R.id.upload_org_logo);
        upload_org_name=view.findViewById(R.id.upload_org_name);
                                                                  // upload_org_motto= view.findViewById(R.id.upload_org_motto);
        upload_org_desc=view.findViewById(R.id.upload_org_desc);
        btn_upload_org=view.findViewById(R.id.btn_upload_org);
        logo_url="";
        org_description="";
        org_name="";

        organiser_uid="";

        upload_org_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPicker=new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                startActivityForResult(photoPicker,1);
            }
        });
        btn_upload_org.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 final ProgressDialog progressDialog=new ProgressDialog(getContext());
                 progressDialog.setMessage("Data Uploading ...");
                 progressDialog.show();
                 mUser=FirebaseAuth.getInstance().getCurrentUser();
                 organiser_uid=mUser.getUid();

                 org_name=upload_org_name.getEditText().getText().toString();
                 org_description=upload_org_desc.getEditText().getText().toString();

                 org_details_model_list=new org_details_model(
                        org_description,
                        logo_url,
                        gen(),
                        org_name,
                        userData.getName(),
                        organiser_uid);

                 createorganisation(org_details_model_list);


            }
        });
        return view;
    }

    private void createorganisation(final org_details_model model){

        FirebaseDatabase.getInstance()
                .getReference("organisation")
                .child(model.getOrganisation_id())
                .child("description")
                .setValue(model)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    savetoUsers(model.getOrganisation_id());
                }else{
                    fetchfromdatabase();
                }
            }
        });

    }

    private void savetoUsers(@NonNull final String key) {
        FirebaseDatabase.getInstance()
                .getReference("users")
                .child(FirebaseAuth.getInstance().getUid())
                .child("organisations")
                .child(key)
                .setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    organisations.clear();
                    Organisations organisations1 = new Organisations(key,true);
                    organisations.add(organisations1);
                    SavetoSharedPrefs(organisations);
                    MainActivity.navController.navigate(R.id.nav_organisation);

                }else {
                    Toast.makeText(getContext(),"INTERNAL ERROR : "+task.getException().getCause().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==getActivity().RESULT_OK) {
            org_logo_uri = data.getData();
            logo_url=org_logo_uri.toString();
            upload_org_logo.setImageURI(org_logo_uri);
        } else {
            Toast.makeText(getContext(), " You haven't selected the logo", Toast.LENGTH_SHORT).show();
        }
    }
    /*    private void company(org_details_model model){
        Gson gson = new Gson();
        String data = gson.toJson(model);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putString("org_details",data);
        editor.apply();
    }*/

    private void SavetoSharedPrefs(List<Organisations> organisations){
        Gson gson = new Gson();
        List<Organisations> dataprevious = getData();
        if (dataprevious == null){
           //fetchfromdatabase();
            String data = gson.toJson(organisations);
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.putString("id",data);
            editor.apply();

        }
        else {

            dataprevious.addAll(organisations);

            String data = gson.toJson(dataprevious);
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.putString("id",data);
            editor.apply();
            fetchlistofAllOrg(organisations);
        }

    }

    //TODO ADD FUNCTION TO SAVE DATA TO SHARED PREFS FROM DATABASE  check the usage of this code


    private void fetchfromdatabase(){
        FirebaseDatabase.getInstance()
                .getReference("users")
                .child(FirebaseAuth.getInstance().getUid())
                .child("organisations").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    //String key=snapshot1.getKey();
                    //data(key);
                    Organisations organisations1 = new Organisations(snapshot1.getKey(),snapshot1.getValue(Boolean.class));
                    organisations.add(organisations1);
                }
                SavetoSharedPrefs(organisations);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void fetchlistofAllOrg(List<Organisations> organisations){

        final List<org_details_model> org = new ArrayList<>();

        for (int i=0;i<organisations.size();i++){

            FirebaseDatabase
                    .getInstance()
                    .getReference("organisation")
                    .child(organisations.get(i).getOrganisationKey())
                    .child("description")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                org.add(snapshot.getValue(org_details_model.class));
                                storetosharedpref(org);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }

    private void storetosharedpref(List<org_details_model> org_detail){
        Gson gson = new Gson();
        String data=gson.toJson(org_detail);
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.clear();
        editor.putString("org",data);
        editor.apply();
    }

    private List<Organisations> getData(){

        Gson gson =new Gson();

        if (!pref.getString("id","null").equals("null")){
            String data = pref.getString("id","null");
            Type type = new TypeToken<ArrayList<Organisations>>(){}.getType();
            return gson.fromJson(data,type);

        }else return null;

    }

    private String gen() {
        Random r = new Random(System.currentTimeMillis());
        return String.valueOf( 1000000 + r.nextInt(2000000));
    }

    private UserData loadDataProfilefromPrefs(){
        String data;
        Gson gson = new Gson();
        data = sharedPreferencesprofile.getString("data","no data");
        Type type = new TypeToken<UserData>(){}.getType();

        return gson.fromJson(data,type);
    }
}