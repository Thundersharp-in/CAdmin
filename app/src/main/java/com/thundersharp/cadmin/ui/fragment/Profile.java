package com.thundersharp.cadmin.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.core.globalmodels.UserData;
import com.thundersharp.cadmin.ui.activity.Login_reg;
import com.thundersharp.cadmin.ui.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.thundersharp.cadmin.ui.activity.MainActivity.floatingActionButton;

public class Profile extends Fragment {

    CircleImageView profile_image;
    TextView designation, name, bio;
    ProgressBar work_progress;
    EditText edit_username,edit_useremail,edit_userphoneNo,edit_userbio,edit_userOrganisations,edit_userUid;
    RatingBar user_rating;
    FirebaseAuth auth;
    List<UserData> data;
    FirebaseUser current_user;
    DatabaseReference reference;
    boolean editable = false;
    Button logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_profile, container, false);
        MainActivity.container.setBackground(null);
        floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_edit_24,getActivity().getTheme()));

        profile_image=root.findViewById(R.id.profile_image);
        designation=root.findViewById(R.id.designation);
        name=root.findViewById(R.id.name);
        bio=root.findViewById(R.id.bio);
        work_progress=root.findViewById(R.id.work_progress);
        user_rating=root.findViewById(R.id.user_rating);
        edit_username=root.findViewById(R.id.edit_username);
        edit_useremail=root.findViewById(R.id.edit_useremail);
        edit_userphoneNo=root.findViewById(R.id.edit_userphoneNo);
        edit_userbio=root.findViewById(R.id.edit_userbio);
        edit_userOrganisations=root.findViewById(R.id.edit_userOrganisations);
        edit_userUid=root.findViewById(R.id.edit_userUid);

        data=new ArrayList<>();

        auth=FirebaseAuth.getInstance();
        current_user=auth.getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("users");
        reference.child(current_user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    UserData model = snapshot.getValue(UserData.class);
                    data.add(model);
                    name.setText(model.getName());
                    edit_username.setText(model.getName());
                    bio.setText(model.getBio());
                    edit_userbio.setText(model.getBio());
                    edit_useremail.setText(model.getEmail());
                    edit_userphoneNo.setText(model.getPhone_no());
                    edit_userUid.setText(model.getUid());
                   Toast.makeText(getContext(), "Something is found", Toast.LENGTH_SHORT).show();
                   /*
                    DatabaseReference Ref=reference.child(current_user.getUid()).child("organisations");
                    Ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (int i =0 ;i< snapshot.getChildrenCount(); i++){
                                edit_userOrganisations.setText(snapshot.getKey().toString()+"\n ");
                                String desig="";
                                String org="Just User";
                                if (snapshot.getValue().toString().equals("true")){
                                    desig="yes";
                                    org="manager at"+ snapshot.getKey();
                                    designation.setText(org);
                                    break;
                                }else{
                                    designation.setText("User");
                                }
                            }

                        }



                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    */
                    name.setText(model.getName());
                  //  profile_image.setImageURI(Uri.parse(model.getImage_uri()));
                    /*
                    assert model.getImage_uri()!=null;
                    if (model.getImage_uri().isEmpty()){
                       profile_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_person_24));
                    } else if (!model.getImage_uri().isEmpty()) {
                        profile_image.setImageURI(Uri.parse(model.getImage_uri()));
                    }

                     */

                    //work_progress are to be done
                    //user_rating are to be done
                }else{
                    Toast.makeText(getContext(), "Oops ! Some error ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editable){
                    editable=false;
                    floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_edit_24,getActivity().getTheme()));
                    Toast.makeText(getContext(),"Profile edit disabled",Toast.LENGTH_SHORT).show();

                }else {
                    editable = true;
                    Toast.makeText(getContext(),"Profile edit enabled",Toast.LENGTH_SHORT).show();
                    floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_save_24,getActivity().getTheme()));
                }

            }
        });

        logout = root.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                        .setCancelable(false)
                        .setTitle("Logout !!")
                        .setMessage("Do you really want to logout from your account?")
                        .setPositiveButton("LOGOUT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseAuth.getInstance().signOut();
                        getActivity().finish();
                        startActivity(new Intent(getActivity(), Login_reg.class));

                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();

            }
        });

        return root;
    }

}