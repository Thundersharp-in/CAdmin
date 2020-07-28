package com.thundersharp.cadmin.ui.activity;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.core.globalmodels.UserData;
import com.thundersharp.cadmin.core.globalmodels.org_details_model;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    /***** Property of THUNDRSHAP inc. shuld not be modified or reproduced without permission *****/

    private AppBarConfiguration mAppBarConfiguration;
    public static Toolbar toolbar;
    private static final int REQUEST_CODE_CALENDAR = 0;
    BottomNavigationView bottomNavigationView;
    public static FloatingActionButton floatingActionButton;
    View fragment;
    RelativeLayout content_main;
    public static RelativeLayout container;
    public static NavController navController;
    FrameLayout frame;
    UserData userData;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = getSharedPreferences("logindata",MODE_PRIVATE);

        Gson gson = new Gson();
        String data = sharedPreferences.getString("data","null");
        Type type = new TypeToken<UserData>(){}.getType();
        userData = gson.fromJson(data,type);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        bottomNavigationView = findViewById(R.id.bottom_bar);
        fragment = findViewById(R.id.nav_host_fragment);
        content_main = findViewById(R.id.content_main);
        floatingActionButton = findViewById(R.id.fab);
        container=findViewById(R.id.containermain);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_profile,
                R.id.nav_work_force,
                R.id.nav_organisation,
                R.id.nav_cal,
                R.id.nav_proj,
                R.id.nav_finance,
                R.id.nav_settings)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View headerview = navigationView.getHeaderView(0);
        ImageView profile = (ImageView) headerview.findViewById(R.id.imageView);
        TextView name = headerview.findViewById(R.id.name_main);
        name.setText(userData.getName());
        TextView email = headerview.findViewById(R.id.email_main);
        email.setText(userData.getEmail());
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!navController.getCurrentDestination().getLabel().toString().equalsIgnoreCase("Your profile")){
                    navController.navigate(R.id.nav_profile);
                    drawer.closeDrawers();
                }

            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Chat comming soon",Toast.LENGTH_SHORT).show();
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.navigation_dashboard){
                    navController.navigate(R.id.nav_notes);

                }else if (menuItem.getItemId() ==R.id.nav_home){
                    navController.navigate(R.id.nav_home);

                }
                return false;
            }
        });

        bottomNavigationView.post(new Runnable() {
            @Override
            public void run() {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                layoutParams.setMargins(0,0,0,bottomNavigationView.getMeasuredHeight()+5);
                layoutParams.addRule(RelativeLayout.BELOW,R.id.t);
                content_main.setLayoutParams(layoutParams);

            }
        });

        if (!checkCalendarPermissions()){
            requestCalendarPermissions();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case REQUEST_CODE_CALENDAR:

                break;

        }
    }

    protected NavOptions getNavOptions() {

        NavOptions options = new NavOptions.Builder()
                .setEnterAnim(R.anim.left_to_right)
                .setExitAnim(R.anim.nav_default_exit_anim)
                .setPopEnterAnim(R.anim.nav_default_pop_enter_anim)
                .setPopExitAnim(R.anim.nav_default_pop_exit_anim)
                .build();

        return options;
    }

    @VisibleForTesting
    protected boolean checkCalendarPermissions() {

        return (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CALENDAR) |
                ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_CALENDAR)) ==
                PackageManager.PERMISSION_GRANTED;
    }




    @VisibleForTesting
    protected void requestCalendarPermissions() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{
                        Manifest.permission.READ_CALENDAR,
                        Manifest.permission.WRITE_CALENDAR},
                REQUEST_CODE_CALENDAR);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}