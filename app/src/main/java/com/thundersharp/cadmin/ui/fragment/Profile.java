package com.thundersharp.cadmin.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    boolean editable = false;
    SharedPreferences sharedPreferences,organisation;
    Button logout;
    String image;
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
       // profile_uri=Uri.parse("https://www.thundersharp.in/logo.png");
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
        }
        else {
            progressprofile.setVisibility(View.VISIBLE);
            String organisations=organisation.getString("org","no organisation");
            edit_userOrganisations.setText(organisations);
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
                    edit_useremail.setEnabled(false);
                    edit_userphoneNo.setEnabled(false);
                    floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_edit_24,getActivity().getTheme()));
                    Toast.makeText(getContext(),"Profile edit disabled",Toast.LENGTH_SHORT).show();
                    if (image != null){
                        image=profile_uri.toString();
                        savetoDatabase(profile_uri);
                    }else {
                        image = "null";
                    }

                    //userData= new UserData(bio,"dob",email,image,name,phone,FirebaseAuth.getInstance().getUid());

                    progressprofile.setVisibility(View.GONE);
                }
                else {
                    progressprofile.setVisibility(View.VISIBLE);
                    editable = true;
                    loadDatafromDatabase();
                    edit_username.setEnabled(true);
                    edit_userbio.setEnabled(true);
                    edit_useremail.setEnabled(false);
                    edit_userphoneNo.setEnabled(false);

                    Toast.makeText(getContext(),"Profile edit enabled",Toast.LENGTH_SHORT).show();
                    floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_save_24,getActivity().getTheme()));
                    profile_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent photoPicker=new Intent(Intent.ACTION_PICK);
                            photoPicker.setType("image/*");
                            startActivityForResult(photoPicker,1);
                        }
                    });
                    progressprofile.setVisibility(View.GONE);

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

        FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getUid()).child("personal_data").addListenerForSingleValueEvent(new ValueEventListener() {
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
                    FirebaseDatabase.getInstance()
                            .getReference("users")
                            .child(FirebaseAuth.getInstance().getUid())
                            .child("organisations").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                edit_userOrganisations.setText("");
                                for (DataSnapshot data:snapshot.getChildren()){
                                    edit_userOrganisations.append(data.getKey().toString()+"\n ");
                                }

                            }else{
                                Toast.makeText(getContext(), " ERROR ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
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

    private void savetoDatabase( @NonNull final Uri uri){//@NonNull  final UserData userData,

        StorageReference userRef = storageReference.child("/"+FirebaseAuth.getInstance().getUid()+".jpg");
        userRef.putFile(uri).addOnFailureListener(new OnFailureListener() {
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
                        userData1= new UserData(edit_userbio.toString(),"dob",edit_useremail.toString(),url,edit_username.toString(),edit_userphoneNo.toString(),FirebaseAuth.getInstance().getUid(),storgePath);
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
                                            FirebaseDatabase.getInstance()
                                                    .getReference("users")
                                                    .child(userData1.getUid())
                                                    .child("organisations").addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.exists()){
                                                        edit_userOrganisations.setText("");
                                                        for (DataSnapshot data:snapshot.getChildren()){
                                                            edit_userOrganisations.append(data.getKey().toString()+"\n ");
                                                        }

                                                    }else{
                                                        Toast.makeText(getContext(), " ERROR ", Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }
                                    }
                                });
                    }

                });
            }
        });
        /*
        storageReference.putFile(Uri.parse(userData.getImage_uri())).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri urlImage=uriTask.getResult();
                image=urlImage.toString();
                Toast.makeText(getContext(),storageReference.toString(),Toast.LENGTH_LONG).show();
                UserData userData1;//=userData

                String name=edit_username.getText().toString();
                String bio=edit_userbio.getText().toString();
                String email=edit_useremail.getText().toString();
                String phone=edit_userphoneNo.getText().toString();
                userData1= new UserData(bio,"dob",email,image,name,phone,FirebaseAuth.getInstance().getUid());
                FirebaseDatabase
                        .getInstance()
                        .getReference("users")
                        .child(FirebaseAuth.getInstance().getUid())
                        .child("personal_data").setValue(userData1)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    savedatatoSharedPref(userData);
                                    FirebaseDatabase.getInstance()
                                            .getReference("users")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .child("organisations").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()){
                                                edit_userOrganisations.setText("");
                                                for (DataSnapshot data:snapshot.getChildren()){
                                                    edit_userOrganisations.append(data.getKey().toString()+"\n ");

                                                }

                                            }else{
                                                Toast.makeText(getContext(), " ERROR ", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }
                        });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

         */

    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==getActivity().RESULT_OK) {
            profile_uri = data.getData();
            profile_image.setImageURI(profile_uri);
            image=profile_uri.toString();
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
        editor1.putString("org",edit_userOrganisations.getText().toString());
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