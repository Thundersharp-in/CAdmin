package com.thundersharp.cadmin.ui.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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

    TextInputLayout text_input_name,
            text_input_email,text_input_password,
            text_input_c_password,text_input_phone,
            text_input_org;
    Button bot_reg;
    TextView already_user;
    RadioButton male, female;
    NestedScrollView relativeLayout;
    AnimationDrawable animationDrawable;
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
        male = view.findViewById(R.id.radio_male);
        female = view.findViewById(R.id.radio_female);
        mAuth=FirebaseAuth.getInstance();
        mReference= FirebaseDatabase.getInstance().getReference();
        relativeLayout = view.findViewById(R.id.containerreg);
        animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(3000);
        animationDrawable.setExitFadeDuration(2000);

        if (Build.VERSION.SDK_INT >= 22){
            checkAndRequestForPermission();
        }
        else {
            //openGallery();
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


        relativeLayout.setOnTouchListener(new OnSwipeTouchListenerreg(getContext()){
            public void onSwipeTop() {

                //Toast.makeText(getContext(), "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                //Toast.makeText(getContext(), "right", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeLeft() {
                //Toast.makeText(getContext(), "left", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeBottom() {
                loginFragment register = new loginFragment();
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_up,R.anim.slide_out_up);
                fragmentTransaction.replace(R.id.containerlog, register).commit();
                //Toast.makeText(getContext(), "bottom", Toast.LENGTH_SHORT).show();
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
        else{

        }
            //openGallery();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE && data != null){
            pickedImageUri = data.getData();
            imageView.setImageURI(pickedImageUri);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        animationDrawable.start();
    }


}


class OnSwipeTouchListenerreg implements View.OnTouchListener {

    private final GestureDetector gestureDetector;

    public OnSwipeTouchListenerreg(Context ctx) {
        gestureDetector = new GestureDetector(ctx, new GestureListener());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                        result = true;
                    }
                } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom();
                    } else {
                        onSwipeTop();
                    }
                    result = true;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    public void onSwipeRight() {
    }

    public void onSwipeLeft() {
    }

    public void onSwipeTop() {
    }

    public void onSwipeBottom() {
    }
}
