package com.thundersharp.cadmin.ui.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MotionEventCompat;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.ui.fragment.loginFragment;

public class Login_reg extends AppCompatActivity {

   public static FrameLayout frameLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_reg);

        frameLayout = findViewById(R.id.containerlog);
        loginFragment loginFragment = new loginFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.containerlog,loginFragment).commit();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Login_reg.this);
        builder.setTitle("Sure to exit !!");
        builder.setMessage("Do you really want to exit the application !!");
        builder.setPositiveButton("EXIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

}