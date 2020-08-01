package com.thundersharp.cadmin.ui.fragment.projetinfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.core.globalAdapters.GallaryAdapter;

import java.util.ArrayList;
import java.util.List;


public class Photo extends Fragment {

    List<String> url;
    RecyclerView gallaryrecuycler;
    String org_id,project_key;
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_photo, container, false);

        url = new ArrayList<>();
        sharedPreferences =getActivity().getSharedPreferences("selected_org",Context.MODE_PRIVATE);
        gallaryrecuycler= view.findViewById(R.id.gallaryrecuycler);
        gallaryrecuycler.setHasFixedSize(true);
        gallaryrecuycler.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        org_id = sharedPreferences.getString("selected",null);

        project_key = ProjectDetails.project_key;

        if (org_id == null || project_key == null){
            Toast.makeText(getActivity(),"null objects",Toast.LENGTH_SHORT).show();
        }else {

            loadImagesfromServer(org_id,project_key);
        }
        return view;
    }

    public void loadImagesfromServer(@NonNull String org_key,@NonNull String project_key){
        url.clear();

        FirebaseDatabase
                .getInstance()
                .getReference("organisation")
                .child(org_key)
                .child("projects")
                .child(project_key)
                .child("images")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot snapshot1 : snapshot.getChildren()){
                                url.add(snapshot1.getValue(String.class));

                            }
                            Toast.makeText(getActivity(),""+url.size(),Toast.LENGTH_SHORT).show();
                            GallaryAdapter gallaryAdapter = new GallaryAdapter(getActivity(),url);
                            gallaryrecuycler.setAdapter(gallaryAdapter);

                        }else {

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}