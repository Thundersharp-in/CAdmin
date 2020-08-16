package com.thundersharp.cadmin.ui.fragment.organisationinfo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

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
import com.thundersharp.cadmin.ui.fragment.OrginasationDetails;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class Photos extends Fragment {

    List<String> url;
    RecyclerView org_photo_rv;
    ImageView org_imageView;
    TextView org_textView;
    String org_id;
    FloatingActionButton addImages;
    ProgressDialog progressDialog;
    FirebaseStorage storage;
    FirebaseDatabase database;
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
        addImages = view.findViewById(R.id.org_image_add);

        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        url=new ArrayList<>();

        addImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImages();
            }
        });

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

    private void showImages() {
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
                                        .child("org_images");
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