package com.thundersharp.cadmin.ui.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.core.globalmodels.UserData;

import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class Register extends Fragment {

    TextInputLayout text_input_name,
            text_input_email,text_input_password,
            text_input_c_password,text_input_phone;
    EditText otp_verification;
    Button bot_reg, btn_verify, number_verified;
    TextView already_user;
    NestedScrollView relativeLayout;
    RelativeLayout getRelativeLayout;
    AnimationDrawable animationDrawable;
    ProgressBar progressreg;
    FirebaseAuth mAuth;
    DatabaseReference mReference;
    CircleImageView imageView;
    static int PReqCode = 1;
    static int REQUEST_CODE = 1;
    boolean codeverified=false,photo=false;
    Uri pickedImageUri;
    String verification_id,codeSent;
    PhoneAuthProvider.ForceResendingToken token;
    AuthCredential authCredential;
    TextInputLayout otpval;
    Button phoneverify, dismiss, update, verify, loginback;
    AlertDialog otpdialogopener;
    SharedPreferences sharedPreferences;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_register,container,false);
        sharedPreferences = getActivity().getSharedPreferences("logindata",Context.MODE_PRIVATE);
        text_input_name = view.findViewById(R.id.text_input_name);
        text_input_email=view.findViewById(R.id.text_input_email);
        text_input_c_password=view.findViewById(R.id.text_input_c_password);
        text_input_password=view.findViewById(R.id.text_input_password);
        text_input_phone=view.findViewById(R.id.text_input_phone);

        btn_verify = view.findViewById(R.id.bot_very);
        progressreg = view.findViewById(R.id.progressreg);
        progressreg.setVisibility(View.GONE);

        bot_reg=view.findViewById(R.id.bot_reg);
        imageView = view.findViewById(R.id.profile_image);
        mAuth=FirebaseAuth.getInstance();
        mReference= FirebaseDatabase.getInstance().getReference();
        getRelativeLayout = view.findViewById(R.id.tttt);
        relativeLayout = view.findViewById(R.id.containerreg);
        animationDrawable = (AnimationDrawable) getRelativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(3000);
        animationDrawable.setExitFadeDuration(2000);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 22){
                    checkAndRequestForPermission();
                }
                else {
                    openGallery();
                }
            }
        });

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (text_input_phone.getEditText().getText().toString().isEmpty() || text_input_phone.getEditText().getText().toString().length() != 10) {
                    text_input_phone.getEditText().setError("Enter a valid mobile number");
                    text_input_phone.getEditText().requestFocus();
                } else {

                    sendotptonumber("+91" + text_input_phone.getEditText().getText().toString());


                    View otpdialog = getLayoutInflater().inflate(R.layout.dialogotp, null);

                    otpval = otpdialog.findViewById(R.id.otp);
                    dismiss = otpdialog.findViewById(R.id.cancil);
                    update = otpdialog.findViewById(R.id.change);
                    verify = otpdialog.findViewById(R.id.submitotp);

                    TextView desotp = otpdialog.findViewById(R.id.textfff);
                    desotp.setText("Enter the otp which we sent you to " + text_input_phone.getEditText().getText().toString() + " to verify that it is your number if you have entered wrong number then click change button to correct it.");
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setCancelable(false);
                    builder.setView(otpdialog);
                    otpdialogopener = builder.create();
                    otpdialogopener.show();

                    dismiss.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            otpdialogopener.dismiss();
                        }
                    });

                    update.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            otpdialogopener.dismiss();
                            Toast.makeText(getActivity(), "Please update the phone number and click on verify", Toast.LENGTH_SHORT).show();
                        }
                    });

                    verify.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (otpval.getEditText().getText().toString().length() < 6) {
                                otpval.getEditText().setError("Minimum 6 digits required");
                            } else {
                                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, otpval.getEditText().getText().toString());
                                signInWithPhoneAuthCredential(credential);

                            }
                        }
                    });


                }

            }
        });


        bot_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressreg.setVisibility(View.VISIBLE);

                final String name = text_input_name.getEditText().getText().toString();
                final String email = text_input_email.getEditText().getText().toString();
                final String password = text_input_password.getEditText().getText().toString();
                final String cpassword = text_input_c_password.getEditText().getText().toString();
                final String phone = text_input_phone.getEditText().getText().toString();

                if (TextUtils.isEmpty(name)){
                    text_input_name.setError("Required!!");
                    text_input_name.requestFocus();
                    progressreg.setVisibility(View.GONE);

                }else if (TextUtils.isEmpty(email)){
                    text_input_email.setError("Required!!");
                    text_input_email.requestFocus();
                    progressreg.setVisibility(View.GONE);

                }else if (TextUtils.isEmpty(password)){
                    text_input_password.setError("Required!!");
                    text_input_password.requestFocus();
                    progressreg.setVisibility(View.GONE);

                }else if (TextUtils.isEmpty(cpassword)){
                    text_input_c_password.setError("Required!!");
                    text_input_c_password.requestFocus();
                    progressreg.setVisibility(View.GONE);

                }else if (TextUtils.isEmpty(phone)){
                    text_input_phone.setError("Required!!");
                    text_input_phone.requestFocus();
                    progressreg.setVisibility(View.GONE);

                }else if (password.length() < 6){
                    text_input_password.setError("Password is too short");
                    text_input_password.requestFocus();
                    progressreg.setVisibility(View.GONE);

                }else if (!password.equals(cpassword)){
                    text_input_password.setError("password is not same");
                    text_input_c_password.setError("password is not same");
                    text_input_c_password.requestFocus();
                    text_input_password.requestFocus();
                    progressreg.setVisibility(View.GONE);

                }else if (phone.length() < 10){
                    text_input_phone.setError("Valid number is required");
                    text_input_phone.requestFocus();
                    progressreg.setVisibility(View.GONE);
                }
                else if(!codeverified){
                    Toast.makeText(getActivity(),"Please verify otp first",Toast.LENGTH_SHORT).show();
                    progressreg.setVisibility(View.GONE);

                }else {
                    final UserData userData = new UserData("No bio updated",
                            "DOB not updated",
                            text_input_email.getEditText().getText().toString(),
                            "null",
                            text_input_name.getEditText().getText().toString(),
                            text_input_phone.getEditText().getText().toString(),
                            FirebaseAuth.getInstance().getUid());

                    authCredential = EmailAuthProvider
                            .getCredential(
                                    text_input_email.getEditText().getText().toString(),
                                    text_input_c_password.getEditText().getText().toString());

                    FirebaseAuth.getInstance()
                            .getCurrentUser()
                            .linkWithCredential(authCredential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                mAuth.getCurrentUser()
                                        .sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                        {

                                            FirebaseDatabase.getInstance()
                                                    .getReference("users")
                                                    .child(mAuth.getUid())
                                                    .child("personal_data")
                                                    .setValue(userData)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()){
                                                                savedatatoSharedPrefs(userData);
                                                                Toast.makeText(getContext(), "Register SuccessFull. Please check your email for verification code", Toast.LENGTH_SHORT).show();
                                                                loginFragment register = new loginFragment();
                                                                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                                                                fragmentTransaction.setCustomAnimations(R.anim.slide_in_up,R.anim.slide_out_up);
                                                                fragmentTransaction.replace(R.id.containerlog, register).commit();
                                                                FirebaseAuth.getInstance().signOut();
                                                                progressreg.setVisibility(View.GONE);

                                                            }else {
                                                                progressreg.setVisibility(View.GONE);
                                                                Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressreg.setVisibility(View.GONE);
                                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }
                    });

                }
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


    private void savedatatoSharedPrefs(UserData userData){
        Gson gson=new Gson();
        String data = gson.toJson(userData);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putString("data",data);
        editor.putBoolean("exists",true);
        editor.apply();
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

    @Override
    public void onResume() {
        super.onResume();
        animationDrawable.start();
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //TODO CHECK PHONE FOR UNIQUE VALUE

                            otpdialogopener.dismiss();
                            verify.setText("✓ Verified");
                            verify.setEnabled(false);
                            text_input_phone.setEnabled(false);
                            codeverified=true;

                        }
                    }
                });
    }

    private void sendotptonumber(String phone) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(phone, 60, TimeUnit.SECONDS, getActivity(), mCallbacks);

    }


    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            Toast.makeText(getContext(), "otp verified", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            codeSent = s;

            Toast.makeText(getContext(), "Otp Sent.  ", Toast.LENGTH_LONG).show();


        }
    };


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
