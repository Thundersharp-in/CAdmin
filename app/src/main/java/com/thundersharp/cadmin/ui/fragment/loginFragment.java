package com.thundersharp.cadmin.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.OAuthProvider;
import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.ui.activity.MainActivity;

import java.util.concurrent.Executor;


public class loginFragment extends Fragment {

    public String emailstring;
    public String passwordstring;
    TextInputLayout email,password;
    private static final String TAG="FacebookLogin";
    private static final int RC_SIGN_IN=123;
    private FirebaseAuth fAuth;
    private Context context;
    OAuthProvider.Builder provider;
    OAuthProvider.Builder provider1;
    private AccessToken token;
    RelativeLayout relativeLayout;
    Animation show_fab_1,hide_fab_1;
    AnimationDrawable animationDrawable;
    Button forgot,loginbutton,signup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        relativeLayout = view.findViewById(R.id.rellogin);
        animationDrawable =(AnimationDrawable)relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(3000);
        animationDrawable.setExitFadeDuration(2000);
        context = getActivity();


        fAuth=FirebaseAuth.getInstance();
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        forgot=view.findViewById(R.id.forgot);
        loginbutton=view.findViewById(R.id.loginbutton);
        signup = view.findViewById(R.id.signupuser);

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getEditText().getText().toString().isEmpty()){
                    email.getEditText().setError("Required");
                    email.getEditText().requestFocus();
                }else if (password.getEditText().getText().toString().isEmpty()){
                    password.getEditText().setError("Required");
                    password.getEditText().requestFocus();
                }else {
                    loginwithemail(email.getEditText().getText().toString(),password.getEditText().getText().toString());
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register register=new Register();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.containerlog,register).commit();


            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.MyDialogTheme);
                View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.forgot_acount,null);
                builder.setTitle("Find your account");
                builder.setMessage("Find your account by providing us your email address.");
                builder.setCancelable(true);
                builder.setView(view1);
                builder.setNegativeButton("DISMISS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setPositiveButton("SEARCH", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        //loginWith(email,password);


        return view;
    }

    private void loginWith(String email, String password) {

        provider=OAuthProvider.newBuilder("twitter.com");
        //twitter
        fAuth.startActivityForSignInWithProvider(getActivity(),provider.build())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        //email password


        //facebook
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        fAuth.signInWithCredential(credential).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

            }
        });


    }

    private void loginwithemail(@NonNull String username,@NonNull String passwordstring){
        fAuth.signInWithEmailAndPassword(username,passwordstring)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Toast.makeText(getActivity(), "Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });

    }

    //google
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        fAuth.signInWithCredential(credential)
                .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        animationDrawable.start();
    }
}