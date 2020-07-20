package com.thundersharp.cadmin.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.ui.activity.Login_reg;

public class Register extends Fragment {

    TextInputLayout text_input_name,text_input_email,text_input_password,text_input_c_password,text_input_phone,text_input_org;
    Button bot_reg;
    TextView already_user;
    FirebaseAuth mAuth;
    DatabaseReference mReference;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.organisation_profile,container,false);
        text_input_name = view.findViewById(R.id.text_input_name);
        text_input_email=view.findViewById(R.id.text_input_email);
        text_input_c_password=view.findViewById(R.id.text_input_c_password);
        text_input_password=view.findViewById(R.id.text_input_password);
        text_input_phone=view.findViewById(R.id.text_input_phone);
        bot_reg=view.findViewById(R.id.bot_reg);
        already_user=view.findViewById(R.id.already_user);
        mAuth=FirebaseAuth.getInstance();
        mReference= FirebaseDatabase.getInstance().getReference();


        final String name = text_input_name.getEditText().getText().toString();
        final String email = text_input_email.getEditText().getText().toString();
        final String password = text_input_password.getEditText().getText().toString();
        final String cpassword = text_input_c_password.getEditText().getText().toString();
        final String phone = text_input_phone.getEditText().getText().toString();



        bot_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (name.equals("")) {
                    text_input_name.setError("Required");
                    text_input_name.requestFocus();
                } else if (email.equals("")) {
                    text_input_email.setError("Required");
                    text_input_email.requestFocus();
                } else if (password.equals("")) {
                    text_input_password.setError("Password Required");
                    text_input_password.requestFocus();
                } else if(cpassword.equals("")){
                    text_input_c_password.setError("Password Required");
                    text_input_c_password.requestFocus();
                } else if (phone.equals("")){
                    text_input_phone.setError("Password Required");
                    text_input_phone.requestFocus();
                } else {





                }


            }
        });




        already_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Login_reg.class));
            }
        });



        return view;





    }
}
