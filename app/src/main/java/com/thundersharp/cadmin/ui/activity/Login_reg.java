package com.thundersharp.cadmin.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.ui.fragment.loginFragment;

public class Login_reg extends AppCompatActivity {

    FrameLayout frameLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_reg);

        frameLayout = findViewById(R.id.containerlog);
        loginFragment loginFragment = new loginFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.containerlog,loginFragment).commit();
    }
}