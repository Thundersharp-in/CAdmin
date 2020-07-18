package com.thundersharp.cadmin.ui.startup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.facebook.login.Login;
import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.ui.activity.Login_reg;

public class Startup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(Startup.this, Login_reg.class));
                finish();
            }
        },500);
    }
}