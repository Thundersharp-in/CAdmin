package com.thundersharp.cadmin.ui.fragment.projetinfo;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.MediaController;
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
import com.thundersharp.cadmin.core.globalAdapters.Videos_Adapter;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.thundersharp.cadmin.ui.activity.MainActivity.floatingActionButton;

public class Video extends Fragment {

    ImageView imageView;
    TextView textView;
   // FloatingActionButton fabVideo;
    RecyclerView videoRecycler;
    FirebaseStorage storage;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    String org_id,project_key;
    SharedPreferences sharedPreferences;
    MediaController mediaController;
    Uri uri;
    List<String> url;
    FloatingActionButton videoAdds;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_video, container, false);

        //floatingActionButton.setImageDrawable(getResources()
        // .getDrawable(R.drawable.ic_baseline_slow_motion_video_24,
        //getActivity().getTheme()));

//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                selectVideo();
//
//            }
//        });

        imageView = view.findViewById(R.id.imageView_video);
        textView = view.findViewById(R.id.tv_video);
        videoAdds = view.findViewById(R.id.project_videos_add);
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

        videoAdds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectVideo();
            }
        });

        mediaController = new MediaController(getContext());

        url = new ArrayList<>();
        sharedPreferences =getActivity().getSharedPreferences("selected_org", Context.MODE_PRIVATE);
        videoRecycler= view.findViewById(R.id.recyclerVideo);
        videoRecycler.setHasFixedSize(true);
        videoRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));


        org_id = sharedPreferences.getString("selected",null);

        project_key = ProjectDetails.project_key;

        if (org_id == null || project_key == null){
            Toast.makeText(getActivity(),"null objects",Toast.LENGTH_SHORT).show();
        }else {

            loadVideofromServer(org_id,project_key);
        }

        return view;
    }

    private void loadVideofromServer(String org_id, String project_key) {
        url.clear();

        FirebaseDatabase
                .getInstance()
                .getReference("organisation")
                .child(org_id)
                .child("projects")
                .child(project_key)
                .child("videoFiles")
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
                            Videos_Adapter videos_adapter = new Videos_Adapter(getActivity(),url);
                            videos_adapter.notifyDataSetChanged();
                            videoRecycler.setAdapter(videos_adapter);
                            videos_adapter.notifyDataSetChanged();

                        }else {

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    private void selectVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"select video file"),1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uploadVideoFile(data.getData());
        }
    }

    private void uploadVideoFile(Uri data) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading video");
        progressDialog.setProgress(0);
        progressDialog.show();

        final String fileName = System.currentTimeMillis()+"";
        StorageReference storageReference = storage.getReference();
        storageReference.child("VideoFiles").child(fileName).putFile(data)
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
                                        .child("videoFiles");
                                reference.child(fileName).setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            progressDialog.dismiss();
                                            Toast.makeText(getContext(), "File Successfully Uploaded", Toast.LENGTH_SHORT).show();
                                        } else {
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
                progressDialog.dismiss();
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