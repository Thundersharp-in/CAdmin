package com.thundersharp.cadmin.ui.fragment.projetinfo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.core.globalAdapters.GallaryAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.thundersharp.cadmin.ui.activity.MainActivity.floatingActionButton;


public class Photo extends Fragment {

    List<String> url;
    RecyclerView gallaryrecuycler;
    String org_id,project_key;
    SharedPreferences sharedPreferences;
    ImageView imageView;
    TextView textView;
    Uri uri;
    FloatingActionButton add_project_photo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_photo, container, false);

        //todo add photo
        //todo delete photo

        floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_outline_add_photo_alternate_24,getActivity().getTheme()));

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        url = new ArrayList<>();
        sharedPreferences =getActivity().getSharedPreferences("selected_org",Context.MODE_PRIVATE);
        gallaryrecuycler= view.findViewById(R.id.gallaryrecuycler);
        imageView = view.findViewById(R.id.imageView_photo);
        textView = view.findViewById(R.id.tv_photo);
        add_project_photo=view.findViewById(R.id.add_project_photo);
        gallaryrecuycler.setHasFixedSize(true);
        gallaryrecuycler.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        org_id = sharedPreferences.getString("selected",null);

        project_key = ProjectDetails.project_key;


        if (org_id == null || project_key == null){
            Toast.makeText(getActivity(),"null objects",Toast.LENGTH_SHORT).show();
        }else {

            loadImagesfromServer(org_id,project_key);
            add_project_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent photopicker=new Intent(Intent.ACTION_PICK);
                    photopicker.setType("image/*");
                    startActivityForResult(photopicker,1);
                }
            });
        }
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==getActivity().RESULT_OK){
            uri=data.getData();
           // imageView.setImageURI(uri);
        }
    }

    public void loadImagesfromServer(@NonNull String org_key, @NonNull String project_key){
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
                        if (!snapshot.exists()){
                            imageView.setVisibility(View.VISIBLE);
                            imageView.setImageResource(R.drawable.sad);
                            textView.setVisibility(View.VISIBLE);
                        }
                        if (snapshot.exists()){
                            imageView.setVisibility(View.GONE);
                            textView.setVisibility(View.GONE);
                            for (DataSnapshot snapshot1 : snapshot.getChildren()){
                                url.add(snapshot1.getValue(String.class));
                            }
                            Toast.makeText(getActivity(),""+url.size(),Toast.LENGTH_SHORT).show();
                            GallaryAdapter gallaryAdapter = new GallaryAdapter(getActivity(),url);
                            gallaryAdapter.notifyDataSetChanged();
                            gallaryrecuycler.setAdapter(gallaryAdapter);

                        }else {
                            Toast.makeText(getActivity(),"something went wrong !",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getActivity(),error.getMessage().toString(),Toast.LENGTH_SHORT).show();
                    }
                });
    }
}