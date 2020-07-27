package com.thundersharp.cadmin.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.core.globalAdapters.AddProjectViewAdapter;
import com.thundersharp.cadmin.core.globalmodels.Projects;
import com.thundersharp.cadmin.ui.Model.AddProject_model;
import com.thundersharp.cadmin.ui.activity.MainActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.thundersharp.cadmin.ui.activity.MainActivity.floatingActionButton;

public class ProjectsFragment extends Fragment {

    RecyclerView recyclerView;
    List<AddProject_model> data;
    List<Projects> list;
    SharedPreferences preferences,sharedPreferencesProjList;
    FloatingActionButton refresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_projects, container, false);
        MainActivity.container.setBackground(null);

        floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_playlist_add_24, getActivity().getTheme()));
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //Toast.makeText(getContext(),"Add projects comming soon",Toast.LENGTH_SHORT).show();
                MainActivity.navController.navigate(R.id.nav_add_project);
            }
        });

        data = new ArrayList<>();
        list = new ArrayList<>();
        preferences = getActivity().getSharedPreferences("proj", Context.MODE_PRIVATE);
        sharedPreferencesProjList = getActivity().getSharedPreferences("all_projects",Context.MODE_PRIVATE);
        recyclerView = view.findViewById(R.id.adding_recyclerView);
        refresh = view.findViewById(R.id.refresh);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchProfileFromServer();
            }
        });

        List<Projects> datapref = loadDataOrgfromPrefs();

        if (datapref == null){
            Toast.makeText(getActivity(), "profile server", Toast.LENGTH_SHORT).show();
            fetchProfileFromServer();

        }else {
            Toast.makeText(getActivity(), "data server", Toast.LENGTH_SHORT).show();
            fetchListofAllProject(loadDataOrgfromPrefs());
        }

        return view;
    }

    private void fetchListofAllProject(@NonNull List<Projects> projects){
        final List<AddProject_model> dataorg = new ArrayList<>();

        for (int i = 0; i<projects.size(); i++){
            Toast.makeText(getContext(),projects.get(i).getProjectKey(),Toast.LENGTH_SHORT).show();
            FirebaseDatabase.getInstance()
                    .getReference("organisation")
                    .child(projects.get(i).getProjectKey())
                    .child("projects")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                dataorg.add(snapshot.getValue(AddProject_model.class));
                                savefetchListofAllProjects(dataorg);
                                Toast.makeText(getActivity(), String.valueOf(dataorg.size()), Toast.LENGTH_SHORT).show();
                            }
                            AddProjectViewAdapter addProjectViewAdapter = new AddProjectViewAdapter(getActivity(),dataorg);
                            recyclerView.setAdapter(addProjectViewAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }

    private void savefetchListofAllProjects(List<AddProject_model> addProjectModels){
        Gson gson = new Gson();
        String data=gson.toJson(addProjectModels);
        SharedPreferences.Editor editor = sharedPreferencesProjList.edit();
        editor.clear();
        editor.putString("proj",data);
        editor.apply();
    }

    private void fetchProfileFromServer(){
        FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getUid())
                .child("projects")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                Projects projects = new Projects(dataSnapshot.getKey(),dataSnapshot.getValue(Boolean.class));
                                list.add(projects);
                            }
                            SavetoSharedPrefs(list);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private List<Projects> loadDataOrgfromPrefs(){

        String data;
        Gson gson = new Gson();
        data = preferences.getString("id","null");
        if (data.equalsIgnoreCase("null")){
            Type type = new TypeToken<ArrayList<Projects>>(){}.getType();

            return gson.fromJson(data,type);
        } else {
            return null;
        }
    }

    private void SavetoSharedPrefs(List<Projects> projectsList){
        Gson gson = new Gson();
        List<Projects> dataprevious = getData();
        if (dataprevious == null){
            String data = gson.toJson(projectsList);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.putString("id",data);
            editor.apply();
            fetchListofAllProject(projectsList);

        }else {
            dataprevious.addAll(projectsList);

            String data = gson.toJson(dataprevious);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.putString("id",data);
            editor.apply();

            fetchListofAllProject(projectsList);
        }
    }

    private List<Projects> getData(){
        Gson gson = new Gson();
        List<Projects> dummy;
        if (preferences.getString("id","null").equals("null")){
            String data = preferences.getString("id","null");
            Type type = new TypeToken<ArrayList<Projects>>(){}.getType();
            dummy = gson.fromJson(data,type);

        }else {
            dummy = new ArrayList<>();
        }

        return dummy;
    }
}