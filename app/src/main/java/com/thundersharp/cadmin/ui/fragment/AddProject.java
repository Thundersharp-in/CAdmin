package com.thundersharp.cadmin.ui.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.core.globalmodels.AddProjectModel;

public class AddProject extends Fragment {

    TextInputLayout pName,projectDesc;
    ImageView addImage;
    TextView textView;
    Button add,can;
    String name,desc;
    public AddProjectModel modelList;
    DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_add_project, container, false);

        pName=view.findViewById(R.id.project_input_name);
        projectDesc=view.findViewById(R.id.project_input_desc);
        addImage =view.findViewById(R.id.add_image);
        textView = view.findViewById(R.id.groupView);
        add = view.findViewById(R.id.btn_project_add);
        can = view.findViewById(R.id.btn_cancel);


         add.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 name=pName.getEditText().getText().toString();
                desc= projectDesc.getEditText().getText().toString();

                 final ProgressDialog progressDialog=new ProgressDialog(getContext());
                 progressDialog.setMessage("Data Uploading ...");
                 progressDialog.show();
                 reference= FirebaseDatabase.getInstance().getReference("organisation").child("org_id").child("projects").push();
                 modelList=new AddProjectModel(name,desc,reference.toString(),"org_id","false");

                 uploadProjectDetail(modelList);
                 progressDialog.dismiss();
             }
         });

        return view;
    }

    private void uploadProjectDetail(AddProjectModel modelList) {
        reference.child("description").setValue(modelList).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getActivity(),"Data Finally Uploaded",Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}