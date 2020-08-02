package com.thundersharp.cadmin.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.thundersharp.cadmin.core.globalmodels.ProjectIds;
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
    int mSelectedIndex;
    TextView textView;
    String projectid;
    Spinner projselectr;
    List<String> projectIds,getProjectname;
    SharedPreferences sharedPreferences;

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
        projectIds = new ArrayList<>();
        getProjectname = new ArrayList<>();
        projselectr= view.findViewById(R.id.projselectr);
        sharedPreferences= getActivity().getSharedPreferences("selected_org", Context.MODE_PRIVATE);
        progresswf=view.findViewById(R.id.progresswork_force);
        progresswf.setVisibility(View.GONE);
        imageView = view.findViewById(R.id.projectImage);
        textView = view.findViewById(R.id.tv);
        workforceModels = new ArrayList<>();
        rv_work_force= view.findViewById(R.id.rv_work_force);
        rv_work_force.setHasFixedSize(true);
        progresswf.setVisibility(View.VISIBLE);
        loadarraydata();

        //loadDataFromServer("key1",sharedPreferences.getString("selected","null"));


        return view;
    }

    private void loadarraydata() {
        projectIds.clear();
        FirebaseDatabase.getInstance()
                .getReference("organisation")
                .child(sharedPreferences.getString("selected","null"))
                .child("project_ids")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                projectIds.add(dataSnapshot.getKey());
                                getProjectname.add(dataSnapshot.getValue(String.class));
                            }

                            loadschooladapter();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadschooladapter() {

        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item,projectIds){
            public View getView(int position, View convertView, ViewGroup parent) {
                // Cast the spinner collapsed item (non-popup item) as a text view
                TextView tv = (TextView) super.getView(position, convertView, parent);

                // Set the text color of spinner item
                tv.setTextColor(Color.BLACK);
                tv.setText(projectIds.get(position)+" ("+getProjectname.get(position)+")");

                // Return the view
                return tv;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent){
                // Cast the drop down items (popup items) as text view
                TextView tv = (TextView) super.getDropDownView(position,convertView,parent);

                // Set the text color of drop down items
                tv.setTextColor(Color.BLACK);
                tv.setText(projectIds.get(position)+" ("+getProjectname.get(position)+")");

                // If this item is selected item
                if(position == mSelectedIndex){
                    // Set spinner selected popup item's text color
                    tv.setTextColor(Color.BLACK);
                }

                // Return the modified view
                return tv;
            }
        };

        // Set an item selection listener for spinner widget
        projselectr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // Set the value for selected index variable
                mSelectedIndex = i;
                projectid = projselectr.getSelectedItem().toString();
                Toast.makeText(getContext(),projectid,Toast.LENGTH_SHORT).show();
                loadDataFromServer(projectid,sharedPreferences.getString("selected","null"));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        arrayAdapter.setDropDownViewResource(R.layout.spinnervalue);
        projselectr.setAdapter(arrayAdapter);
    }

    public void loadDataFromServer(final String key,@NonNull final String orgid){
        //TODO UPDATE THE VALUES TO BE LOADED FROM SHARED PREFRENCES SAVED
        //TODO @GET FROM SPF @DESC (NOT NECESSARY)
        FirebaseDatabase.getInstance()
                .getReference("organisation")
                .child(orgid)
                .child("projects")
                .child(key)
                .child("description")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    textView.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageResource(R.drawable.sad);
                    progresswf.setVisibility(View.GONE);
                }
                if (snapshot.exists()){
                    textView.setVisibility(View.GONE);
                    imageView.setVisibility(View.GONE);
                    savemodel(snapshot.getValue(AddProject_model.class));

                    FirebaseDatabase.getInstance()
                            .getReference("organisation")
                            .child(orgid)
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
                                        progresswf.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

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