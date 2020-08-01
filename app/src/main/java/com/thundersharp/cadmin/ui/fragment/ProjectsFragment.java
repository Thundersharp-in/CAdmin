package com.thundersharp.cadmin.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
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
import com.thundersharp.cadmin.core.globalmodels.AddProject_model;
import com.thundersharp.cadmin.ui.activity.MainActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.thundersharp.cadmin.ui.activity.MainActivity.floatingActionButton;


public class ProjectsFragment extends Fragment {

    ImageView imageView;
    ProgressBar progressproj;
    RelativeLayout cont;
    RecyclerView recyclerView;
    List<AddProject_model> data;
    List<Projects> list;
    SwipeRefreshLayout refresh_proj;
    SharedPreferences
            preferences,
            sharedPreferencesProjList,
            sharedPreferencesorg;

//    FloatingActionButton refresh;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_projects, container, false);
        MainActivity.container.setBackground(null);
        progressproj=view.findViewById(R.id.progress_proj);
        cont = view.findViewById(R.id.cont);
        progressproj.setVisibility(View.GONE);
        refresh_proj=view.findViewById(R.id.refresh_project);
        refresh_proj.setRefreshing(true);
        floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_playlist_add_24, getActivity().getTheme()));
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (sharedPreferencesorg.getString("selected",null)== null){
                    progressproj.setVisibility(View.VISIBLE);
                    Snackbar.make(view,"You don't have any project !",Snackbar.LENGTH_LONG).show();

                    // TODO add sneekbar
                    Snackbar.make(cont,"No organisation found create one first",Snackbar.LENGTH_LONG).setAction("CREATE", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            MainActivity.navController.navigate(R.id.nav_org);
                        }
                    }).setActionTextColor(getResources().getColor(R.color.white)).show();

                    progressproj.setVisibility(View.GONE);
                }else {
                    MainActivity.navController.navigate(R.id.nav_add_project);
                }

            }
        });

        data = new ArrayList<>();
        list = new ArrayList<>();
        preferences = getActivity().getSharedPreferences("proj", Context.MODE_PRIVATE);
        sharedPreferencesProjList = getActivity().getSharedPreferences("all_projects",Context.MODE_PRIVATE);
        sharedPreferencesorg = getActivity().getSharedPreferences("selected_org",Context.MODE_PRIVATE);
        recyclerView = view.findViewById(R.id.adding_recyclerView);
        refresh_proj.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                progressproj.setVisibility(View.VISIBLE);
                fetchProfileFromServer();
                progressproj.setVisibility(View.GONE);
            }
        });
                     /* refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressproj.setVisibility(View.VISIBLE);
                fetchProfileFromServer();
                progressproj.setVisibility(View.GONE);
            }
        });


 */

        if (sharedPreferencesorg.getString("selected",null)!= null){
            List<Projects> datapref = loadDataOrgfromPrefs();
            progressproj.setVisibility(View.VISIBLE);
            if (datapref == null){

                Toast.makeText(getActivity(), "server", Toast.LENGTH_SHORT).show();
                fetchProfileFromServer();


            }else {
                Toast.makeText(getActivity(), "Loaded from shared prefs", Toast.LENGTH_SHORT).show();
                fetchListofAllProject(loadDataOrgfromPrefs());
            }
            refresh_proj.setRefreshing(false);
            progressproj.setVisibility(View.GONE);
        }else {
            progressproj.setVisibility(View.VISIBLE);
            //TODO add sneekbar
            progressproj.setVisibility(View.GONE);
            refresh_proj.setRefreshing(false);
        }

        return view;
    }

    private void fetchListofAllProject(@NonNull List<Projects> projects){

        final List<AddProject_model> dataorg = new ArrayList<>();

        for (int i = 0; i<projects.size(); i++){

           //checking the project keys

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
                                dataorg.add(snapshot.getValue(AddProject_model.class));
                                savefetchListofAllProjects(dataorg);
                                //checking the size
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
                                Projects projects = new Projects(dataSnapshot.getKey(),dataSnapshot.getValue(String.class));
                                list.add(projects);
                                progressproj.setVisibility(View.GONE);
                            }
                            SavetoSharedPrefs(list);
                        }
                        else {
                            imageView.setVisibility(View.VISIBLE);
                            imageView.setImageResource(R.drawable.sad);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressproj.setVisibility(View.GONE);
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