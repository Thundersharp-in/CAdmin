package com.thundersharp.cadmin.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.ui.Adapter.AddProjectViewAdapter;
import com.thundersharp.cadmin.ui.Model.AddProject;
import com.thundersharp.cadmin.ui.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.thundersharp.cadmin.ui.activity.MainActivity.floatingActionButton;

public class Projects extends Fragment {

    RecyclerView recyclerView;
    ArrayList<AddProject> projectArrayList;
    RecyclerView.Adapter adapter;


    ItemTouchHelper.SimpleCallback itemTouchHelperCallback =
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    projectArrayList.remove(viewHolder.getAdapterPosition());
                    Toast.makeText(getContext(), "Deleted...", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                }
            };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_projects, container, false);
        MainActivity.container.setBackground(null);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        createSubjectList();
        bindSubjectList();
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);


        floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_playlist_add_24, getActivity().getTheme()));
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //Toast.makeText(getContext(),"Add projects comming soon",Toast.LENGTH_SHORT).show();
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater1 = getLayoutInflater();
                View alertLayout = inflater1.inflate(R.layout.add_project_layout, null);

                builder.setView(alertLayout);
                builder.setCancelable(true);

                final TextInputLayout pName = alertLayout.findViewById(R.id.project_input_name);
                final TextInputLayout projectDesc = alertLayout.findViewById(R.id.project_input_desc);
                final ImageView addImage = alertLayout.findViewById(R.id.add_image);

                final FloatingActionButton fab1 = alertLayout.findViewById(R.id.fab1);
                final TextView textView = alertLayout.findViewById(R.id.groupView);
                final Button add = alertLayout.findViewById(R.id.btn_project_add);
                final Button can = alertLayout.findViewById(R.id.btn_cancel);

                final Dialog dialog = builder.create();

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        add.setClickable(false);
                        can.setClickable(false);

                        if (pName.getEditText().getText().toString().equalsIgnoreCase("")) {
                            pName.setError("Project name is required!");
                            pName.requestFocus();
                            add.setClickable(true);
                            can.setClickable(true);
                            return;
                        } else if (projectDesc.getEditText().getText().toString().equalsIgnoreCase("")) {
                            projectDesc.setError("Project name is required!");
                            projectDesc.requestFocus();
                            add.setClickable(true);
                            can.setClickable(true);
                            return;
                        }
                        projectArrayList.add(new AddProject(pName.getEditText().getText().toString(),
                                projectDesc.getEditText().getText().toString()));
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });

                can.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                addImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO: open gallery intent and wait for user to pick an image!

                        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        galleryIntent.setType("image/*");
                        startActivityForResult(galleryIntent, 1);
                    }
                });

                fab1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                        LayoutInflater inflater2 = getLayoutInflater();
                        View view1 = inflater2.inflate(R.layout.all_users, null);

                        alert.setView(view1);
                        alert.setCancelable(true);
                        alert.show();
                    }
                });
                dialog.show();
            }
        });

        return view;
    }


    private void bindSubjectList() {
        adapter = new AddProjectViewAdapter(projectArrayList, getContext());
        recyclerView.setAdapter(adapter);
    }

    private void createSubjectList() {
        projectArrayList = new ArrayList<AddProject>();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
        }
    }
}