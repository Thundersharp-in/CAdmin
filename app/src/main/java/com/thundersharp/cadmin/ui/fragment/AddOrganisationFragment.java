package com.thundersharp.cadmin.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.core.globalmodels.org_details_model;
import com.thundersharp.cadmin.ui.activity.MainActivity;

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.thundersharp.cadmin.ui.activity.MainActivity.floatingActionButton;


public class AddOrganisationFragment extends Fragment {

    CircleImageView upload_org_logo;
    EditText upload_org_name,upload_org_desc,upload_org_motto;
    Button btn_upload_org;
    Uri org_logo_uri;
    String logo_url,org_name,org_description,organiser_name,organiser_uid;
    FirebaseAuth mAuth;
    DatabaseReference mRef;
    FirebaseUser mUser;
    public org_details_model org_details_model_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_add_organisation, container, false);
        MainActivity.container.setBackground(null);
        floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_save_24,getActivity().getTheme()));

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.navController.navigate(R.id.nav_organisation);
            }
        });

        upload_org_logo=view.findViewById(R.id.upload_org_logo);
        upload_org_name=view.findViewById(R.id.upload_org_name);
                                                                  // upload_org_motto= view.findViewById(R.id.upload_org_motto);
        upload_org_desc=view.findViewById(R.id.upload_org_desc);
        btn_upload_org=view.findViewById(R.id.btn_upload_org);
        logo_url="";
        org_description="";
        org_name="";
        organiser_name="";
        organiser_uid="";
        upload_org_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPicker=new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                startActivityForResult(photoPicker,1);
            }
        });
        btn_upload_org.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 final ProgressDialog progressDialog=new ProgressDialog(getContext());
                 progressDialog.setMessage("Data Uploading ...");
                 progressDialog.show();
                 mUser=FirebaseAuth.getInstance().getCurrentUser();
                 organiser_uid=mUser.getUid();
                 organiser_name=mUser.getDisplayName();
                 org_name=upload_org_name.getText().toString();
                 org_description=upload_org_desc.getText().toString();
                 // organiser_name="Name of organiser";
                 //organiser_uid="organiser uid";
                 long l3=1000000000;
                 long l4=9000000000L;
                 Random r = new Random(System.currentTimeMillis());
                 long l= l3 + r.nextInt((int) l4);
                 final String l1= String.valueOf(l);
                 DatabaseReference reference1=FirebaseDatabase.getInstance().getReference().child("users")
                         .child(organiser_uid).child("personal_data").child("name");
                 reference1.addValueEventListener(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                         if (dataSnapshot.exists()) {

                             String organiser_name=dataSnapshot.getValue().toString();
                             org_details_model_list=new org_details_model(org_description,logo_url,l1,org_name,organiser_name,organiser_uid);
                             mRef= FirebaseDatabase.getInstance().getReference("organisation1");
                             mRef.child(l1).child("description").setValue(org_details_model_list).addOnCompleteListener(new OnCompleteListener<Void>() {
                                 @Override
                                 public void onComplete(@NonNull Task<Void> task) {

                                     if (task.isSuccessful()){
                                         Toast.makeText(getContext(), "Data Uploaded", Toast.LENGTH_SHORT).show();
                                         DatabaseReference reference=FirebaseDatabase.getInstance().getReference()
                                                 .child("users");
                                         reference.child(organiser_uid).child("organisations").child(l1).setValue("true")
                                                 .addOnFailureListener(new OnFailureListener() {
                                                     @Override
                                                     public void onFailure(@NonNull Exception e) {
                                                         Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                                     }
                                                 }).addOnCompleteListener(new OnCompleteListener<Void>() {
                                             @Override
                                             public void onComplete(@NonNull Task<Void> task) {
                                                 Toast.makeText(getContext(), "Data successfully uploaded", Toast.LENGTH_SHORT).show();
                                             }
                                         });
                                         SharedPreferences pref =getActivity().getSharedPreferences("org", Context.MODE_PRIVATE);
                                         SharedPreferences.Editor editor = pref.edit();
                                         editor.putString("id",l1.trim());
                                         editor.apply();
                                         progressDialog.dismiss();
                                         Organisation fragment=new Organisation();
                                         FragmentManager manage=getFragmentManager();
                                         FragmentTransaction transaction=manage.beginTransaction();
                                         transaction.replace(R.id.org_manager,fragment);
                                         transaction.commit();
                                     }

                                 }
                             }).addOnFailureListener(new OnFailureListener() {
                                 @Override
                                 public void onFailure(@NonNull Exception e) {
                                     progressDialog.dismiss();
                                     Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                 }
                             });
                         }

                     }

                     @Override
                     public void onCancelled(@NonNull DatabaseError databaseError) {

                     }
                 });

            }
        });
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==getActivity().RESULT_OK) {
            org_logo_uri = data.getData();
            logo_url=org_logo_uri.toString();
            upload_org_logo.setImageURI(org_logo_uri);
        } else {
            Toast.makeText(getContext(), " You haven't selected the logo", Toast.LENGTH_SHORT).show();
        }
    }
}