package com.thundersharp.cadmin.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.core.globalmodels.UserData;
import com.thundersharp.cadmin.core.globalmodels.org_details_model;
import com.thundersharp.cadmin.ui.activity.Login_reg;
import com.thundersharp.cadmin.ui.activity.MainActivity;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.thundersharp.cadmin.ui.activity.MainActivity.floatingActionButton;

public class Profile extends Fragment {

    CircleImageView profile_image;
    TextView designation, name, bio;
    ProgressBar work_progress;
    EditText edit_username
            ,edit_useremail
            ,edit_userphoneNo
            ,edit_userbio
            ,edit_userOrganisations
            ,edit_userUid;
    RatingBar user_rating;
    FirebaseAuth auth;
    FirebaseUser current_user;
    DatabaseReference reference;
    Uri profile_uri;
    boolean editable = false,somethingchanged = false,profilepicchanged = false;
    SharedPreferences sharedPreferences,organisation;
    Button logout;
    String image,orgs;
    ProgressBar progressprofile;
    StorageReference  storageReference;
    UserData userData;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_profile, container, false);

        sharedPreferences = getActivity().getSharedPreferences("logindata", Context.MODE_PRIVATE);
        organisation=getActivity().getSharedPreferences("organisations",Context.MODE_PRIVATE);
        MainActivity.container.setBackground(null);
        floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_edit_24,getActivity().getTheme()));

        profile_image=root.findViewById(R.id.profile_image);
        designation=root.findViewById(R.id.designation);
        name=root.findViewById(R.id.name);
        bio=root.findViewById(R.id.bio);
        work_progress=root.findViewById(R.id.work_progress);
        user_rating=root.findViewById(R.id.user_rating);
        edit_username=root.findViewById(R.id.edit_username);
        edit_useremail=root.findViewById(R.id.edit_useremail);
        edit_userphoneNo=root.findViewById(R.id.edit_userphoneNo);
        edit_userbio=root.findViewById(R.id.edit_userbio);
        edit_userOrganisations=root.findViewById(R.id.edit_userOrganisations);
        edit_userUid=root.findViewById(R.id.edit_userUid);
        progressprofile = root.findViewById(R.id.progressprofile);

        progressprofile.setVisibility(View.GONE);

        storageReference= FirebaseStorage.getInstance().getReference("Profile Images");
        auth=FirebaseAuth.getInstance();
        current_user=auth.getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("users");


        if (!sharedPreferences.getBoolean("exists", false)){
            progressprofile.setVisibility(View.VISIBLE);
            loadDatafromDatabase();
            progressprofile.setVisibility(View.GONE);
            Toast.makeText(getActivity(),"Loaded from database",Toast.LENGTH_SHORT).show();
        } else {
            progressprofile.setVisibility(View.VISIBLE);
            orgs=organisation.getString("org","no organisation");
            edit_userOrganisations.setText(orgs);
            userData = loadDatafromPrefs();
            name.setText(userData.getName());
            edit_username.setText(userData.getName());
            bio.setText(userData.getBio());
            edit_userbio.setText(userData.getBio());
            edit_useremail.setText(userData.getEmail());
            edit_userphoneNo.setText(userData.getPhone_no());
            edit_userUid.setText(userData.getUid());
            Toast.makeText(getContext(),userData.getEmail(),Toast.LENGTH_SHORT).show();
            progressprofile.setVisibility(View.GONE);
        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editable){

                    progressprofile.setVisibility(View.VISIBLE);
                    editable=false;
                    edit_username.setEnabled(false);
                    edit_userbio.setEnabled(false);

                    if (somethingchanged || profilepicchanged){
                        UserData userData1 = new UserData(
                                edit_userbio.getText().toString(),
                                "dob",
                                edit_useremail.getText().toString(),
                                userData.getImage_uri(),
                                edit_username.getText().toString(),
                                edit_userphoneNo.getText().toString(),
                                FirebaseAuth.getInstance().getUid(),
                                userData.getUri_ref());
                        savedatatoSharedPref(userData1);
                        savetoDatabase(userData1);

                    }else {
                        FirebaseDatabase.getInstance().getReference("users")
                                .child(FirebaseAuth.getInstance().getUid())
                                .child("organisations")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()){
                                            edit_userOrganisations.setText("");
                                            for (DataSnapshot snapshot1:snapshot.getChildren()){
                                                // org.add(snapshot1.getKey());
                                                edit_userOrganisations.append(snapshot1.getKey()+"\n");
                                            }
                                            orgs=edit_userOrganisations.getText().toString();
                                            saveorgtosaredpref(orgs);
                                            SharedPreferences.Editor editor1=organisation.edit();
                                            editor1.clear();
                                            editor1.putString("org",orgs);
                                            editor1.apply();
                                        }
                                        else {
                                            Toast.makeText(getActivity(), "snapshort not exist", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(getActivity(), error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }

                    floatingActionButton.setImageDrawable(getResources()
                            .getDrawable(R.drawable.ic_baseline_edit_24,getActivity().getTheme()));
                    Toast.makeText(getContext(),"Profile edit saved",Toast.LENGTH_SHORT).show();
                    progressprofile.setVisibility(View.GONE);
                }
                else {

                    editable = true;
                    edit_username.setEnabled(true);
                    edit_userbio.setEnabled(true);

                    floatingActionButton.setImageDrawable(getResources()
                            .getDrawable(R.drawable.ic_baseline_save_24,getActivity().getTheme()));
                    Toast.makeText(getContext(),"Profile edit enabled",Toast.LENGTH_SHORT).show();
                }

            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editable){
                    Intent photoPicker=new Intent(Intent.ACTION_PICK);
                    photoPicker.setType("image/*");
                    startActivityForResult(photoPicker,1);
                }else {
                    Snackbar.make(getView(),"Profile pic selection canceled",Snackbar.LENGTH_LONG).show();
                }

            }
        });


        edit_userbio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {somethingchanged = true;

            }
        });

        edit_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                somethingchanged = true;
            }
        });

        edit_userbio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editable){
                    Toast.makeText(getActivity(),"",Toast.LENGTH_SHORT).show();

                    edit_userbio.requestFocus();
                    edit_userbio.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            Toast.makeText(getActivity(),charSequence.toString(),Toast.LENGTH_SHORT).show();
                            somethingchanged = true;
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });

                }else {
                    Snackbar.make(getView(),"Description editable is not enabled !",Snackbar.LENGTH_LONG).show();
                }
            }
        });



        logout = root.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                        .setCancelable(false)
                        .setTitle("Logout !!")
                        .setMessage("Do you really want to logout from your account?")
                        .setPositiveButton("LOGOUT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseAuth.getInstance().signOut();
                        getActivity().finish();
                        startActivity(new Intent(getActivity(), Login_reg.class));

                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();

            }
        });

        return root;
    }


    private void loadDatafromDatabase() {
        FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getUid())
                .child("organisations")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot snapshot1:snapshot.getChildren()){
                               // org.add(snapshot1.getKey());
                                edit_userOrganisations.append(snapshot1.getKey()+"\n");
                                Toast.makeText(getActivity(), snapshot1.getKey(), Toast.LENGTH_SHORT).show();
                            }
                            orgs=edit_userOrganisations.getText().toString();
                            saveorgtosaredpref(orgs);

                        }
                        else {
                            Toast.makeText(getActivity(), "snapshort not exist", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        FirebaseDatabase.getInstance()
                .getReference("users")
                .child(FirebaseAuth.getInstance().getUid())
                .child("personal_data")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    userData = snapshot.getValue(UserData.class);
                    savedatatoSharedPref(userData);
                    name.setText(userData.getName());
                    edit_username.setText(userData.getName());
                    bio.setText(userData.getBio());
                    edit_userbio.setText(userData.getBio());
                    edit_useremail.setText(userData.getEmail());
                    edit_userphoneNo.setText(userData.getPhone_no());
                    edit_userUid.setText(userData.getUid());
                    profile_image.setImageURI(Uri.parse(userData.getImage_uri()));
                    Toast.makeText(getContext(),userData.getEmail(),Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(),"SERVER ERROR CODE : 404",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void saveorgtosaredpref(String orgs) {

        SharedPreferences.Editor editor1=organisation.edit();
        editor1.clear();
        editor1.putString("org",orgs);
        editor1.apply();

    }

    private void savetoDatabase( @NonNull  final UserData userData){
        if (profilepicchanged){

            StorageReference userRef = storageReference.child("/profilepics"+FirebaseAuth.getInstance().getUid()+".jpg");
            userRef.putFile(Uri.parse(userData.getImage_uri())).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(),e.getCause().getMessage().toString(),Toast.LENGTH_LONG).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final String storgePath = taskSnapshot.getMetadata().getPath();
                    taskSnapshot
                            .getStorage()
                            .getDownloadUrl()
                            .addOnFailureListener(getActivity(), new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(),e.getCause().getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }).addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            final UserData userData1;


                            userData1= new UserData(
                                    edit_userbio.getText().toString(),
                                    "dob",
                                    edit_useremail.getText().toString(),
                                    url,
                                    edit_username.getText().toString(),
                                    edit_userphoneNo.getText().toString(),
                                    FirebaseAuth.getInstance().getUid(),
                                    storgePath);

                            FirebaseDatabase
                                    .getInstance()
                                    .getReference("users")
                                    .child(userData1.getUid())
                                    .child("personal_data").setValue(userData1)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                savedatatoSharedPref(userData1);
                                            }
                                        }
                                    });
                            edit_userOrganisations.setText("");
                            FirebaseDatabase.getInstance().getReference("users")
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .child("organisations")
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()){
                                                for (DataSnapshot snapshot1:snapshot.getChildren()){

                                                    edit_userOrganisations.append(snapshot1.getKey()+"\n");
                                                }
                                                orgs=edit_userOrganisations.getText().toString();
                                               saveorgtosaredpref(orgs);
                                            }else {
                                                Toast.makeText(getActivity(), "snapshort not exist", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                        }

                    });
                }
            });
        }else {
            final UserData userData1;


            userData1= new UserData(
                    edit_userbio.getText().toString(),
                    "dob",
                    edit_useremail.getText().toString(),
                    userData.getImage_uri(),
                    edit_username.getText().toString(),
                    edit_userphoneNo.getText().toString(),
                    FirebaseAuth.getInstance().getUid(),
                    userData.getUri_ref());

            FirebaseDatabase
                    .getInstance()
                    .getReference("users")
                    .child(userData1.getUid())
                    .child("personal_data").setValue(userData1)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                savedatatoSharedPref(userData1);
                            }
                        }
                    });
            edit_userOrganisations.setText("");
            FirebaseDatabase.getInstance().getReference("users")
                    .child(FirebaseAuth.getInstance().getUid())
                    .child("organisations")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                for (DataSnapshot snapshot1:snapshot.getChildren()){
                                    edit_userOrganisations.append(snapshot1.getKey()+"\n");

                                }
                                orgs=edit_userOrganisations.getText().toString();
                                saveorgtosaredpref(orgs);

                            }else {
                                Toast.makeText(getActivity(), "snapshort not exist", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }


    }



    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==getActivity().RESULT_OK) {
            profile_uri = data.getData();
            profile_image.setImageURI(profile_uri);
            image=profile_uri.toString();
            profilepicchanged = true;
        } else {
            if (profile_uri==null){
                profile_uri=Uri.parse("https://www.thundersharp.in/logo.png");
                profile_image.setImageURI(profile_uri);
            }else {
                profile_image.setImageURI(profile_uri);

            }
            Toast.makeText(getContext()," You haven't selected the profile image", Toast.LENGTH_SHORT).show();
        }
    }
    public void savedatatoSharedPref(UserData userData){
        Gson gson = new Gson();
        String datamodel = gson.toJson(userData);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putBoolean("exists",true);
        editor.putString("data",datamodel);
        editor.apply();

        SharedPreferences.Editor editor1=organisation.edit();
        editor1.clear();
        editor1.putString("org",orgs);
        editor1.apply();
    }
    private UserData loadDatafromPrefs() {
        String data;
        Gson gson = new Gson();
        data = sharedPreferences.getString("data","no data");
        Type type = new TypeToken<UserData>(){}.getType();

        return gson.fromJson(data,type);
    }

}