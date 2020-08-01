package com.thundersharp.cadmin.ui.fragment.organisationinfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.core.globalAdapters.GallaryAdapter;

import java.util.ArrayList;
import java.util.List;

public class Photos extends Fragment {

    List<String> url;
    RecyclerView org_photo_rv;
    String org_id;

    public Photos() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_photos, container, false);

        org_photo_rv=view.findViewById(R.id.org_photo_rv);
        url=new ArrayList<>();

        org_photo_rv.setHasFixedSize(true);
        org_photo_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        org_photo_rv.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));

        loadFromServer(org_id);
        return view;
    }

    private void loadFromServer(@NonNull String org_id) {
        FirebaseDatabase.getInstance()
                .getReference("organisation")
                .child(org_id)
                .child("org_images")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot snapshot1 : snapshot.getChildren()){
                                url.add(snapshot1.getValue(String.class));
                            }

                        }else {
                            Toast.makeText(getActivity(),"No photo exists",Toast.LENGTH_SHORT).show();
                        }
                        GallaryAdapter gallaryAdapter = new GallaryAdapter(getActivity(),url);
                        org_photo_rv.setAdapter(gallaryAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}