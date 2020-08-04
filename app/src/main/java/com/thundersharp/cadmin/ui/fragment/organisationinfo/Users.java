package com.thundersharp.cadmin.ui.fragment.organisationinfo;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.core.globalAdapters.UsersAdapter;
import com.thundersharp.cadmin.core.globalmodels.Organisations;
import com.thundersharp.cadmin.core.globalmodels.UserData;
import com.thundersharp.cadmin.ui.fragment.OrginasationDetails;

import java.util.ArrayList;
import java.util.List;

public class Users extends Fragment {

    ImageView org_imageView;
    TextView org_textView;
    String org_id;
    RecyclerView rv_org_users;
    List<String> list;
   // Button btn_add_org_user;

    public Users() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View root=inflater.inflate(R.layout.fragment_users, container, false);
        org_imageView = root.findViewById(R.id.org_imageView_users);
        org_textView = root.findViewById(R.id.org_tv_users);
        rv_org_users=root.findViewById(R.id.rv_org_users);
       // btn_add_org_user=root.findViewById(R.id.btn_add_org_user);
        org_id = OrginasationDetails.org_id;
        list=new ArrayList<>();
        fetchData(org_id);

        return root;
    }

    private void fetchData(final String org_id) {

        FirebaseDatabase.getInstance()
                .getReference("organisation")
                .child(org_id)
                .child("org_users")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()){
                            org_imageView.setVisibility(View.VISIBLE);
                            org_imageView.setImageResource(R.drawable.sad);
                            org_textView.setVisibility(View.VISIBLE);
                            org_imageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setTitle("User Uid here");
                                    View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.add_user_to_org,null);
                                    final EditText  user_uid_here=view1.findViewById(R.id.txt_user_uid);
                                    final Button btn_verify_users=view1.findViewById(R.id.btn_verify_users);
                                    builder.setCancelable(true);
                                    builder.setView(view1);
                                    btn_verify_users.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            final String user_uid =user_uid_here.getEditableText().toString();
                                            FirebaseDatabase.getInstance()
                                                    .getReference("users")
                                                    .child(user_uid)
                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull final DataSnapshot snapshot) {
                                                            if (snapshot.exists()){

                                                                FirebaseDatabase.getInstance().getReference("users")
                                                                        .child(user_uid)
                                                                        .child("organisations")
                                                                        .child(org_id).child(org_id).setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()){
                                                                            Toast.makeText(getActivity(), "Uploaded to user", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Toast.makeText(getActivity(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });

                                                                //TODO Add to user node
                                                                //Toast.makeText(getActivity(), "sent request to user ", Toast.LENGTH_SHORT).show();
                                                                FirebaseDatabase.getInstance()
                                                                        .getReference("organisation")
                                                                        .child(org_id)
                                                                        .child("org_users")
                                                                        .child("1")
                                                                        .setValue(user_uid).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()){
                                                                            Toast.makeText(getActivity(), "User Added", Toast.LENGTH_SHORT).show();
                                                                            for (DataSnapshot snapshot1:snapshot.getChildren()){
                                                                                list.add(snapshot1.getValue(String.class));
                                                                                org_imageView.setVisibility(View.GONE);
                                                                                org_imageView.setImageResource(R.drawable.sad);
                                                                                org_textView.setVisibility(View.GONE);
                                                                            }

                                                                            //TODO cancel dilog cancel
                                                                        }else{
                                                                            Toast.makeText(getActivity(), "Something went wrong !", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {

                                                                    }
                                                                });
                                                            }else {
                                                                Toast.makeText(getActivity(), "OOPS! Sorry no user exists for this uid try something else !", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                        }
                                    });
                                    builder.show();
                                }
                            });
                        }
                        else if (snapshot.exists()){
                            org_imageView.setVisibility(View.GONE);
                            org_textView.setVisibility(View.GONE);
                            //btn_add_org_user.setVisibility(View.GONE);
                            list.clear();
                            for (DataSnapshot snapshot1 : snapshot.getChildren()){
                                list.add(snapshot1.getValue(String.class));
                            }

                        }else {

                           // btn_add_org_user.setVisibility(View.VISIBLE);
                            org_imageView.setVisibility(View.VISIBLE);
                            org_imageView.setImageResource(R.drawable.sad);
                            org_textView.setVisibility(View.VISIBLE);
                            /**
                             *  btn_add_org_user.setOnClickListener(new View.OnClickListener() {
                             *                                 @Override
                             *                                 public void onClick(View v) {
                             *
                             *                                    // Toast.makeText(getActivity(), "no users found", Toast.LENGTH_SHORT).show();
                             *                                     //TODO alert dilog for adding  users
                             *
                             *                                 }
                             *                             });
                             */

                        }

                        UsersAdapter usersAdapter=new UsersAdapter(getActivity(),list);
                        rv_org_users.setAdapter(usersAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}