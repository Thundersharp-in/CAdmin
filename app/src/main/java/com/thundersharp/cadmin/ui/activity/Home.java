package com.thundersharp.cadmin.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.thundersharp.cadmin.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home extends AppCompatActivity {
    CircleImageView orglogo;
    TextView orgtitle,manager,desc;
    Button opnbot,contbot;
    DatabaseReference mDatabaseReference;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        orglogo = findViewById(R.id.orglogo);
        orgtitle = findViewById(R.id.orgtitle);
        manager = findViewById(R.id.manager);
        desc = findViewById(R.id.desc);
        opnbot =findViewById(R.id.opnbot);
        contbot=findViewById(R.id.contbot);
        mAuth=FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();



    }
}