package com.thundersharp.cadmin.ui.startup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.facebook.login.Login;
import com.google.firebase.auth.FirebaseAuth;
import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.ui.activity.Login_reg;
import com.thundersharp.cadmin.ui.activity.MainActivity;

public class Startup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (FirebaseAuth.getInstance().getCurrentUser() !=null && FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()){
                    startActivity(new Intent(Startup.this, MainActivity.class));
                    finish();
                }else {
                startActivity(new Intent(Startup.this, Login_reg.class));
                finish();
                }
            }
        },500);
    }
}