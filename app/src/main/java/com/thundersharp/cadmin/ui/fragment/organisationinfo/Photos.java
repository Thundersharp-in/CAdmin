package com.thundersharp.cadmin.ui.fragment.organisationinfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.thundersharp.cadmin.ui.fragment.OrginasationDetails;

import java.util.ArrayList;
import java.util.List;

public class Photos extends Fragment {

    List<String> url;
    RecyclerView org_photo_rv;
    ImageView org_imageView;
    TextView org_textView;
    String org_id,project_key;
    //SharedPreferences sharedPreferences;

    public Photos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_photos, container, false);
        org_id = OrginasationDetails.org_id;
        org_photo_rv=view.findViewById(R.id.org_photo_rv);
        org_imageView = view.findViewById(R.id.org_imageView_photos);
        org_textView = view.findViewById(R.id.org_tv_photos);
        url=new ArrayList<>();

        org_photo_rv.setHasFixedSize(true);
        org_photo_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        org_photo_rv.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));

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
                        if (!snapshot.exists()){
                            org_imageView.setVisibility(View.VISIBLE);
                            org_imageView.setImageResource(R.drawable.sad);
                            org_textView.setVisibility(View.VISIBLE);
                        }
                        if (snapshot.exists()){
                            org_textView.setVisibility(View.GONE);
                            org_imageView.setVisibility(View.GONE);
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