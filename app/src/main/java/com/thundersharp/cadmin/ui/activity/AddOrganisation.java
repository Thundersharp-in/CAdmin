package com.thundersharp.cadmin.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.notes.model.org_details_model;

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddOrganisation extends AppCompatActivity {

    CircleImageView upload_org_logo;
    EditText upload_org_name,upload_org_desc;
    Button btn_upload_org;
    Uri org_logo_uri;
    String logo_url,org_name,org_description,organiser_name,organiser_uid;
    FirebaseAuth mAuth;
    DatabaseReference mRef;
    FirebaseUser mUser;
    public org_details_model org_details_model_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_organisation);

        upload_org_logo=findViewById(R.id.upload_org_logo);
        upload_org_name=findViewById(R.id.upload_org_name);
        upload_org_desc=findViewById(R.id.upload_org_desc);
        btn_upload_org=findViewById(R.id.btn_upload_org);
    }

    public void btn_upload_org(View view) {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Data Uploading ...");
        progressDialog.show();
        org_name=upload_org_name.getText().toString();
        org_description=upload_org_desc.getText().toString();
        organiser_name="Name of organiser ";
        organiser_uid="organiser uid";
        long l3=1000000000;
        long l4=9000000000L;
        Random r = new Random(System.currentTimeMillis());
        long l= l3 + r.nextInt((int) l4);
        String l1= String.valueOf(l);
        org_details_model_list=new org_details_model(org_description,logo_url,l1,org_name,organiser_name,organiser_uid);
        mRef= FirebaseDatabase.getInstance().getReference("organisation1");
        mRef.child(l1).child("description").setValue(org_details_model_list).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    Toast.makeText(AddOrganisation.this, "Data Uploaded", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    finish();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(AddOrganisation.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void upload_org_logo(View view) {
        Intent photoPicker=new Intent(Intent.ACTION_PICK);
        photoPicker.setType("image/*");
        startActivityForResult(photoPicker,1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            org_logo_uri = data.getData();
            logo_url=org_logo_uri.toString();
            upload_org_logo.setImageURI(org_logo_uri);

        } else {
            Toast.makeText(this, " You haven't selected the logo", Toast.LENGTH_SHORT).show();
        }
    }
}