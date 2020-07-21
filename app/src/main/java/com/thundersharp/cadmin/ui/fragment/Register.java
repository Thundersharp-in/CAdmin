package com.thundersharp.cadmin.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.ui.activity.Login_reg;
import com.thundersharp.cadmin.ui.activity.MainActivity;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class Register extends Fragment {

    TextInputLayout text_input_name,text_input_email,text_input_password,text_input_c_password,text_input_phone,text_input_org;
    Button bot_reg;
    TextView already_user;
    RadioButton male, female;
    FirebaseAuth mAuth;
    DatabaseReference mReference;
    CircleImageView imageView;
    static int PReqCode = 1;
    static int REQUEST_CODE = 1;
    Uri pickedImageUri;
    String gender = "";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.organisation_profile,container,false);
        text_input_name = view.findViewById(R.id.text_input_name);
        text_input_email=view.findViewById(R.id.text_input_email);
        text_input_c_password=view.findViewById(R.id.text_input_c_password);
        text_input_password=view.findViewById(R.id.text_input_password);
        text_input_phone=view.findViewById(R.id.text_input_phone);
        text_input_org = view.findViewById(R.id.text_input_org);
        bot_reg=view.findViewById(R.id.bot_reg);
        imageView = view.findViewById(R.id.profile_image);
        already_user=view.findViewById(R.id.already_user);
        male = view.findViewById(R.id.radio_male);
        female = view.findViewById(R.id.radio_female);
        mAuth=FirebaseAuth.getInstance();
        mReference= FirebaseDatabase.getInstance().getReference();

        if (Build.VERSION.SDK_INT >= 22){
            checkAndRequestForPermission();
        }
        else {
            openGallery();
        }

        if (male.isChecked()){
            gender ="Male";
        } else if (female.isChecked()){
            gender = "Female";
        }

        final String name = text_input_name.getEditText().getText().toString();
        final String email = text_input_email.getEditText().getText().toString();
        final String password = text_input_password.getEditText().getText().toString();
        final String cpassword = text_input_c_password.getEditText().getText().toString();
        final String phone = text_input_phone.getEditText().getText().toString();
        final String organization = text_input_org.getEditText().getText().toString();



        bot_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (name.equals("")) {
                    text_input_name.setError("Required");
                    text_input_name.requestFocus();
                } else if (email.equals("")) {
                    text_input_email.setError("Required");
                    text_input_email.requestFocus();
                } else if (password.equals("")) {
                    text_input_password.setError("Password Required");
                    text_input_password.requestFocus();
                } else if(cpassword.equals("")){
                    text_input_c_password.setError("Password Required");
                    text_input_c_password.requestFocus();
                } else if (phone.equals("")){
                    text_input_phone.setError("Phone Number Required");
                    text_input_phone.requestFocus();
                } else {
                    setRegister(name, email, password);

                }
            }

            private void setRegister(final String name, final String email, final String password) {

                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(getActivity(),new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Account created", Toast.LENGTH_SHORT).show();
                            updateUserInfo(name,pickedImageUri,mAuth.getCurrentUser());
                        }
                    }
                });
            }

            private void updateUserInfo(final String name, Uri pickedImageUri, final FirebaseUser currentUser) {
                StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("user_photos");
                final StorageReference imageFilePath = mStorage.child(pickedImageUri.getLastPathSegment());
                imageFilePath.putFile(pickedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                                                            .setDisplayName(name)
                                                                             .setPhotoUri(uri)
                                                                             .build();
                                currentUser.updateProfile(profileUpdate)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            HashMap<String, Object> hashMap = new HashMap<>();
                                            hashMap.put("id",getId());
                                            hashMap.put("name",name);
                                            hashMap.put("email",email);
                                            hashMap.put("password",password);
                                            hashMap.put("imageUrl",imageView);
                                            hashMap.put("gender",gender);
                                            hashMap.put("phone_no",phone);
                                            hashMap.put("organisation",organization);
                                            mReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        Toast.makeText(getContext(), "Register Complete", Toast.LENGTH_SHORT).show();
                                                        Intent homeIntent = new Intent(getContext(),MainActivity.class);
                                                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        startActivity(homeIntent);
                                                    }
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        already_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginFragment loginFragment = new loginFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.containerlog,loginFragment).commit();
            }
        });

        return view;
    }

    private void openGallery() {
        //TODO: open gallery intent and wait for user to pick an image!

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUEST_CODE);
    }

    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(getActivity(), "Please accept for required permission", Toast.LENGTH_SHORT).show();
            }
            else
            {
                ActivityCompat.requestPermissions(getActivity(),
                                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PReqCode);
            }
        }
        else
            openGallery();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE && data != null){
            pickedImageUri = data.getData();
            imageView.setImageURI(pickedImageUri);
        }

    }


}
