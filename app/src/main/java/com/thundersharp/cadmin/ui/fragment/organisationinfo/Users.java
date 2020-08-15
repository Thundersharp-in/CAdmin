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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.core.globalAdapters.UsersAdapter;
import com.thundersharp.cadmin.core.globalmodels.ManagerModels;
import com.thundersharp.cadmin.core.globalmodels.Organisations;
import com.thundersharp.cadmin.core.globalmodels.UserData;
import com.thundersharp.cadmin.ui.fragment.OrginasationDetails;

import java.util.ArrayList;
import java.util.List;

public class Users extends Fragment {

    ImageView org_imageView; //if no data
    TextView org_textView;  //if no data
    String org_id;
    RecyclerView rv_org_users;
    List<ManagerModels> managerModelsList;
    UserData userList;

    public Users() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View root=inflater.inflate(R.layout.fragment_users, container, false);
        org_imageView = root.findViewById(R.id.org_imageView_users);
        org_textView = root.findViewById(R.id.org_tv_users);
        rv_org_users=root.findViewById(R.id.rv_org_users);
       // showUser = root.findViewById(R.id.add_user);
       // btn_add_org_user=root.findViewById(R.id.btn_add_org_user);
        org_id = OrginasationDetails.org_id;

        managerModelsList=new ArrayList<>();
        fetchData(org_id);

        return root;

    }

    private void fetchData(final String org_id) {
        FirebaseDatabase.getInstance()
                .getReference("organisation")
                .child(org_id)
                .child("org_users")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            org_imageView.setVisibility(View.GONE);
                            org_textView.setVisibility(View.GONE);
                            for (DataSnapshot snapshot1 : snapshot.getChildren()){
                                ManagerModels users = new ManagerModels(snapshot1.getKey(),snapshot1.getValue(String.class));
                                managerModelsList.add(users);
                            }

                        } else
                            if (!snapshot.exists()) {
                            org_imageView.setVisibility(View.VISIBLE);
                            org_imageView.setImageResource(R.drawable.sad);
                            org_textView.setVisibility(View.VISIBLE);

                            org_imageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setTitle(" Enter User Uid here");
                                    View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.add_user_to_org,null);
                                    final EditText  user_uid_here=view1.findViewById(R.id.txt_user_uid);
                                    final Button btn_verify_users=view1.findViewById(R.id.btn_verify_users);
                                    builder.setCancelable(true);
                                    builder.setView(view1);

                                    btn_verify_users.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            final String user_uid =user_uid_here.getEditableText().toString();
                                            if (user_uid.isEmpty()){
                                                user_uid_here.setError("Required !");
                                                user_uid_here.requestFocus();
                                            }else {
                                                checkdatafromusers(org_id,user_uid);
                                            }
                                        }
                                    });
                                    builder.show();


                                }
                            });

                        }else {
                            org_imageView.setVisibility(View.VISIBLE);
                            org_imageView.setImageResource(R.drawable.sad);
                            org_textView.setVisibility(View.VISIBLE);
                            Toast.makeText(getActivity(), "OOPS! Something went wrong ....  !", Toast.LENGTH_SHORT).show();
                        }

                        UsersAdapter usersAdapter=new UsersAdapter(getActivity(),managerModelsList);
                        rv_org_users.setAdapter(usersAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getActivity(), error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void checkdatafromusers(final String org_id,final String user_uid) {
        FirebaseDatabase
                .getInstance()
                .getReference("users")
                .child(user_uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){

                            FirebaseDatabase
                                    .getInstance().getReference("users")
                                    .child(user_uid)
                                    .child("organisations")
                                    .child(org_id)
                                    .setValue(false)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                setDatatoOrg(org_id,user_uid);
                                            }else {
                                                Toast.makeText(getActivity(), "OOps ! something went wrong try again !", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }else {
                            Toast.makeText(getActivity(), "No such user found to be registered with this application try another !", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getActivity(), error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setDatatoOrg(final String org_id, String user_uid) {

        FirebaseDatabase
                .getInstance()
                .getReference("users")
                .child(user_uid)
                .child("personal_data")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                     if (snapshot.exists()){
                         for (DataSnapshot snapshot1:snapshot.getChildren()){
                             userList=snapshot1.getValue(UserData.class);

                             String uid=userList.getUid();
                             String userdata=userList.getName()+"\n"+userList.getEmail()+"\n"+userList.getPhone_no()+"\n"+userList.getUid();;

                             FirebaseDatabase.getInstance().getReference("organisation")
                                     .child(org_id)
                                     .child("org_users")
                                     .child(uid)
                                     .setValue(userdata)
                                     .addOnCompleteListener(new OnCompleteListener<Void>() {
                                         @Override
                                         public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(getActivity(), "All values set ", Toast.LENGTH_SHORT).show();
                                            }
                                         }
                                     }).addOnFailureListener(new OnFailureListener() {
                                 @Override
                                 public void onFailure(@NonNull Exception e) {
                                     Toast.makeText(getActivity(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                 }
                             });
                             org_imageView.setVisibility(View.GONE);
                             org_imageView.setImageResource(R.drawable.sad);
                             org_textView.setVisibility(View.GONE);
                         }

                     }  else {
                         Toast.makeText(getActivity(), "user data missing", Toast.LENGTH_SHORT).show();
                     }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getActivity(), error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}