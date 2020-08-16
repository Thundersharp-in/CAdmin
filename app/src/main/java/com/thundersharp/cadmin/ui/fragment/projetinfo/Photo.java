package com.thundersharp.cadmin.ui.fragment.projetinfo;

import android.app.ProgressDialog;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.core.globalAdapters.GallaryAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.thundersharp.cadmin.ui.activity.MainActivity.floatingActionButton;


public class Photo extends Fragment {

    List<String> url;
    RecyclerView gallaryrecuycler;
    String org_id,project_key;
    SharedPreferences sharedPreferences;
    ImageView imageView;
    TextView textView;
    FloatingActionButton addImages;
    ProgressDialog progressDialog;
    FirebaseStorage storage;
    FirebaseDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_photo, container, false);

//        floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_outline_add_photo_alternate_24,getActivity().getTheme()));
//
//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                //selectPdf();
//
//            }
//        });

        url = new ArrayList<>();
        sharedPreferences =getActivity().getSharedPreferences("selected_org",Context.MODE_PRIVATE);
        gallaryrecuycler= view.findViewById(R.id.gallaryrecuycler);
        imageView = view.findViewById(R.id.imageView_photo);
        addImages = view.findViewById(R.id.project_images_add);
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        textView = view.findViewById(R.id.tv_photo);
        gallaryrecuycler.setHasFixedSize(true);
        gallaryrecuycler.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        org_id = sharedPreferences.getString("selected",null);

        project_key = ProjectDetails.project_key;

        if (org_id == null || project_key == null){
            Toast.makeText(getActivity(),"null objects",Toast.LENGTH_SHORT).show();
        }else {

            loadImagesfromServer(org_id,project_key);
        }
        addImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImages();
            }
        });
        return view;
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

    private void selectImages() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select images"),1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            uploadImageFiles(data.getData());
        }
    }

    private void uploadImageFiles(Uri data) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading video");
        progressDialog.setProgress(0);
        progressDialog.show();

        final String fileName = System.currentTimeMillis()+"";
        StorageReference storageReference = storage.getReference();
        storageReference.child("images").child(fileName).putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String url = uri.toString();
                                DatabaseReference reference = database.getReference("organisation")
                                        .child(org_id)
                                        .child("projects")
                                        .child(project_key)
                                        .child("images");
                                reference.child(fileName).setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            progressDialog.dismiss();
                                            Toast.makeText(getActivity(), "File Uploaded", Toast.LENGTH_SHORT).show();

                                        }else {
                                            progressDialog.dismiss();
                                            Toast.makeText(getContext(), "File not uploaded", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                int currentProgress = (int) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                progressDialog.setProgress(currentProgress);
            }
        });
    }


}