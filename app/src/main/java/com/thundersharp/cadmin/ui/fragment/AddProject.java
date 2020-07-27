package com.thundersharp.cadmin.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.core.globalmodels.Projects;
import com.thundersharp.cadmin.core.globalmodels.UserData;
import com.thundersharp.cadmin.ui.Model.AddProject_model;
import com.thundersharp.cadmin.ui.activity.MainActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.thundersharp.cadmin.ui.activity.MainActivity.floatingActionButton;

public class AddProject extends Fragment {


    TextInputLayout p_name, p_desc;
    Button add_project;
    String project_description,project_name,project_uid;
    FirebaseAuth mAuth;
    SharedPreferences preferences;
    List<Projects> projectsList;
    DatabaseReference mRef;
    UserData userData;
    FirebaseUser mUser;
    SharedPreferences sharedPreference, sharedPreferencesProfile;
    public AddProject_model addProjectModel;

    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_project,container,false);

        MainActivity.container.setBackground(null);

        sharedPreferencesProfile = getActivity().getSharedPreferences("logindata", Context.MODE_PRIVATE);
        preferences = getActivity().getSharedPreferences("proj",Context.MODE_PRIVATE);
        sharedPreference = getActivity().getSharedPreferences("all_projects",Context.MODE_PRIVATE);

        floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_save_24,getActivity().getTheme()));

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.navController.navigate(R.id.nav_proj);
            }
        });

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Data Uploading...");

        userData = loadDataProfileFromPrefs();
        projectsList = new ArrayList<>();

        p_name = view.findViewById(R.id.project_input_name);
        p_desc = view.findViewById(R.id.project_input_desc);
        add_project = view.findViewById(R.id.btn_project_add);

        project_description = "";
        project_name = "";
        project_uid = "";

        add_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                mUser = FirebaseAuth.getInstance().getCurrentUser();
                project_uid = mUser.getUid();

                project_name = p_name.getEditText().getText().toString();
                project_description = p_desc.getEditText().getText().toString();

                addProjectModel = new AddProject_model(
                        project_name,
                        project_description,
                        gen());

                createProject(addProjectModel);

            }
        });
        return view;
    }


    private void createProject(final AddProject_model model) {
        FirebaseDatabase.getInstance()
                .getReference("organisation")
                .child(model.getProject_id())
                .child("projects")
                .setValue(model)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            savetoUsers(model.getProject_id());
                        } else {
                            fetchfromdatabase();
                        }
                    }
                });
    }

    private void savetoUsers(@NonNull final String key){
        FirebaseDatabase.getInstance()
                .getReference("users")
                .child(FirebaseAuth.getInstance().getUid())
                .child("projects")
                .child(key)
                .setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    projectsList.clear();
                    Projects projects1 = new Projects(key,true);
                    projectsList.add(projects1);
                    SavetoSharedPrefs(projectsList);
                    MainActivity.navController.navigate(R.id.nav_proj);

                }else {
                    Toast.makeText(getContext(), "Internal Error:"+task.getException().getCause().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void SavetoSharedPrefs(List<Projects> projectsList){
        Gson gson = new Gson();
        List<Projects> dataPrevious = getData();
        if (dataPrevious == null){
            String data = gson.toJson(projectsList);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.putString("id",data);
            editor.apply();

        }else {
            dataPrevious.addAll(projectsList);

            String data = gson.toJson(dataPrevious);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.putString("id",data);
            editor.apply();
            fetchListofAllProj(projectsList);
        }
    }

    //TODO ADD FUNCTION TO SAVE DATA TO SHARED PREFS FROM DATABASE  check the usage of this code

    private void fetchfromdatabase(){
        FirebaseDatabase.getInstance()
                .getReference("users")
                .child(FirebaseAuth.getInstance().getUid())
                .child("projects").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Projects projects1 = new Projects(dataSnapshot.getKey(),dataSnapshot.getValue(Boolean.class));
                    projectsList.add(projects1);
                }
                SavetoSharedPrefs(projectsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void fetchListofAllProj(List<Projects> projects){
        final List<AddProject_model> modelList = new ArrayList<>();

        for (int i=0; i<projects.size(); i++){
            FirebaseDatabase.getInstance()
                    .getReference("organisation")
                    .child(projects.get(i).getProjectKey())
                    .child("projects")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                modelList.add(snapshot.getValue(AddProject_model.class));
                                storetosharedpref(modelList);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }

    private void storetosharedpref(List<AddProject_model> addProject_models){
        Gson gson = new Gson();
        String data = gson.toJson(addProject_models);
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.clear();
        editor.putString("proj",data);
        editor.apply();
    }

    private List<Projects> getData(){
        Gson gson = new Gson();

        if (!preferences.getString("id","null").equals("null")){
            String data = preferences.getString("id","null");
            Type type = new TypeToken<ArrayList<Projects>>(){}.getType();
            return gson.fromJson(data,type);

        }else {
            return null;
        }
    }

    private String gen(){
        Random r = new Random(System.currentTimeMillis());
        return  String.valueOf(100000 + r.nextInt(20000000));
    }

    private UserData loadDataProfileFromPrefs() {
        String data;
        Gson gson = new Gson();
        data = sharedPreferencesProfile.getString("data","no data");
        Type type = new TypeToken<UserData>(){}.getType();

        return gson.fromJson(data,type);
    }
}