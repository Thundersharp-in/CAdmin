package com.thundersharp.cadmin.ui.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
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
import com.thundersharp.cadmin.core.globalmodels.AddProject_model;
import com.thundersharp.cadmin.core.globalmodels.org_details_model;
import com.thundersharp.cadmin.ui.activity.MainActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.thundersharp.cadmin.ui.activity.MainActivity.floatingActionButton;

public class AddProject extends Fragment {


    TextInputLayout p_name, p_desc;
    Button add_project;
    RelativeLayout containerppp;
    String project_description,project_name;
    FirebaseAuth mAuth;
    SharedPreferences preferences;
    List<Projects> projectsList;
    DatabaseReference mRef;
    UserData userData;
    FirebaseUser mUser;
    SharedPreferences sharedPreference, sharedPreferencesProfile;
    public AddProject_model addProjectModel;
    SharedPreferences sharedPreferencesorg;
    AlertDialog.Builder builder;
    Dialog dialog;
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_project,container,false);

        MainActivity.container.setBackground(null);
        p_name=view.findViewById(R.id.add_project_name);
        p_desc=view.findViewById(R.id.add_project_desc);
        add_project=view.findViewById(R.id.buttoncreatep);
        containerppp = view.findViewById(R.id.containerppp);
        projectsList = new ArrayList<>();

        builder=new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.process,null,false);
        builder.setView(view1);
        dialog = builder.create();

        sharedPreferencesorg = getActivity().getSharedPreferences("selected_org",Context.MODE_PRIVATE);
        sharedPreferencesProfile = getActivity().getSharedPreferences("logindata", Context.MODE_PRIVATE);
        preferences = getActivity().getSharedPreferences("proj",Context.MODE_PRIVATE);
        sharedPreference = getActivity().getSharedPreferences("all_projects",Context.MODE_PRIVATE);

        floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_delete_outline_24,getActivity().getTheme()));
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
                builder.setTitle("Delete");
                builder.setMessage("Are You Really Going To Delete This Data???");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        progressDialog = new ProgressDialog(getActivity());
                        progressDialog.setTitle("Deleting");
                        progressDialog.setMessage("Deleting please wait!!!");
                        progressDialog.show();

                        FirebaseDatabase.getInstance().getReference()
                                .child("organisation")
                                .child("org_id").child("projects").removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            dialog.dismiss();
                                            progressDialog.dismiss();
                                            Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();

                MainActivity.navController.navigate(R.id.nav_proj);
            }
        });

        Toast.makeText(getActivity(),String.valueOf(sharedPreferencesorg.getString("selected","null")),Toast.LENGTH_SHORT).show();

        add_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                project_name=p_name.getEditText().getText().toString();
                project_description=p_desc.getEditText().getText().toString();
                if (project_name.length()>30){
                    p_name.getEditText().setError("More than 30 characters !");
                    p_name.getEditText().requestFocus();
                    dialog.dismiss();

                } else if (project_name.isEmpty()) {
                    p_name.getEditText().setError("Required !");
                    p_name.getEditText().requestFocus();
                    dialog.dismiss();

                }else if (project_description.isEmpty()){
                    p_desc.getEditText().setError("Required !");
                    p_desc.getEditText().requestFocus();
                    dialog.dismiss();

                }else {
                    if (sharedPreferencesorg.getString("selected",null)!=null){
                        addProjectModel = new AddProject_model(
                                project_name,
                                project_description,
                                gen(),
                                sharedPreferencesorg.getString("selected",null),
                                false);

                        createProject(addProjectModel);
                    }else {
                        Snackbar.make(getView(),"No organisation found create one first",Snackbar.LENGTH_LONG).setAction("CREATE", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MainActivity.navController.navigate(R.id.nav_org);
                            }
                        }).setActionTextColor(855600).show();
                    }

                }

            }
        });


        return view;
    }


    private void createProject(final AddProject_model model) {

        String org_id = sharedPreferencesorg.getString("selected",null);
        if (org_id == null){
            Toast.makeText(getContext(),"Failed to get your organisation please select it from the list of created/joined organisation from home or organisations",Toast.LENGTH_LONG).show();
        }else {
            FirebaseDatabase.getInstance()
                    .getReference("organisation")
                    .child(org_id)
                    .child("projects")
                    .child(model.getProject_id())
                    .child("description")
                    .setValue(model)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                //progressDialog.dismiss();
                                savetoUsers(model.getProject_id());
                            } else {
                                fetchfromdatabase();
                            }
                        }
                    });

        }
    }


    private void savetoUsers(@NonNull final String key){

        FirebaseDatabase.getInstance()
                .getReference("users")
                .child(FirebaseAuth.getInstance().getUid())
                .child("projects")
                .child(key)
                .setValue(sharedPreferencesorg.getString("selected","null")).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    projectsList.clear();
                    Projects projects1 = new Projects(key,sharedPreferencesorg.getString("selected","null"));
                    projectsList.add(projects1);
                    SavetoSharedPrefs(projectsList);
                    dialog.dismiss();
                    MainActivity.navController.navigate(R.id.nav_proj);

                }else {
                    dialog.dismiss();
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
                .child("projects")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Projects projects1 = new Projects(dataSnapshot.getKey(),dataSnapshot.getValue(String.class));
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
                    .child(sharedPreferencesorg.getString("selected",null))
                    .child("projects")
                    .child(projects.get(i).getProjectKey())
                    .child("description")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                modelList.add(snapshot.getValue(AddProject_model.class));
                                Toast.makeText(getActivity(),String.valueOf("trurgfhcgfdfgchjfhdffnjhgghghhgfgfhgfhgfhghgvh"+snapshot.getValue(AddProject_model.class).projectDesc),Toast.LENGTH_SHORT).show();
                                storetosharedpref(modelList);
                            }else {
                                Toast.makeText(getActivity(), "No such project found", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }

    private void storetosharedpref(List<AddProject_model> addProject_models){
        List<AddProject_model> projectdetails = getPreviousProjects();
        if (projectdetails==null|| projectdetails.isEmpty()){
            Gson gson = new Gson();
            String data=gson.toJson(addProject_models);
            SharedPreferences.Editor editor = sharedPreference.edit();
            editor.clear();
            editor.putString("proj",data);
            editor.apply();
        }else {
            Gson gson = new Gson();
            addProject_models.addAll(projectdetails);
            String data = gson.toJson(addProject_models);
            SharedPreferences.Editor editor = sharedPreference.edit();
            editor.clear();
            editor.putString("proj",data);
            editor.apply();

        }
    }

    private List<AddProject_model> getPreviousProjects() {
        Gson gson =new Gson();

        if (!sharedPreference.getString("proj","null").equals("null")){
            String data = sharedPreference.getString("proj","null");
            Type type = new TypeToken<ArrayList<AddProject_model>>(){}.getType();
            return gson.fromJson(data,type);

        }else return null;
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