package com.thundersharp.cadmin.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.core.globalAdapters.ProjectDesc;
import com.thundersharp.cadmin.core.globalAdapters.WorkForceAdapter;
import com.thundersharp.cadmin.core.globalmodels.AddProject_model;
import com.thundersharp.cadmin.core.globalmodels.WorkforceModel;
import com.thundersharp.cadmin.ui.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

import static com.thundersharp.cadmin.ui.activity.MainActivity.floatingActionButton;

public class WorkForce extends Fragment {

    ProgressBar progresswf;
    private AddProject_model project;
    List<WorkforceModel> workforceModels;
    RecyclerView rv_work_force;
    ImageView imageView;

    /**
     * @ spinner to be added for for displaying 1 project at a time in a org. changes apply for manager/employee
     **/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        MainActivity.container.setBackgroundColor(getResources().getColor(R.color.huuuuu,getActivity().getTheme()));
        floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_chat_bubble_outline_24,getActivity().getTheme()));
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.navController.navigate(R.id.nav_chats);
            }
        });

        View view = inflater.inflate(R.layout.fragment_work_force, container, false);
        progresswf=view.findViewById(R.id.progresswork_force);
        progresswf.setVisibility(View.GONE);
        imageView = view.findViewById(R.id.imageView);
        workforceModels = new ArrayList<>();
        rv_work_force= view.findViewById(R.id.rv_work_force);
        rv_work_force.setHasFixedSize(true);
        progresswf.setVisibility(View.VISIBLE);
        loadDataFromServer("key1");
        progresswf.setVisibility(View.GONE);

        return view;
    }

    public void loadDataFromServer(final String key){
        //TODO UPDATE THE VALUES TO BE LOADED FROM SHARED PREFRENCES SAVED
        //TODO @GET FROM SPF @DESC (NOT NECESSARY)
        FirebaseDatabase.getInstance()
                .getReference("organisation")
                .child("1234567890")
                .child("projects")
                .child(key)
                .child("description")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    savemodel(snapshot.getValue(AddProject_model.class));
                    FirebaseDatabase.getInstance()
                            .getReference("organisation")
                            .child("1234567890")
                            .child("projects")
                            .child(key)
                            .child("TODO_id")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                            workforceModels.add(dataSnapshot.getValue(WorkforceModel.class));
                                        }
                                        WorkForceAdapter workForceAdapter = new WorkForceAdapter(getContext(),workforceModels,project);
                                        rv_work_force.setAdapter(workForceAdapter);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                } else {
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageResource(R.drawable.sad);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private  void savemodel(AddProject_model projectDesc){
        this.project=projectDesc;
    }
}