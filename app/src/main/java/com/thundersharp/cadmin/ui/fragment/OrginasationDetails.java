package com.thundersharp.cadmin.ui.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.core.globalAdapters.TabAdapter;
import com.thundersharp.cadmin.core.globalAdapters.UsersAdapter;
import com.thundersharp.cadmin.core.globalmodels.Organisations;
import com.thundersharp.cadmin.core.globalmodels.org_details_model;
import com.thundersharp.cadmin.ui.activity.MainActivity;
import com.thundersharp.cadmin.ui.fragment.organisationinfo.Photos;
import com.thundersharp.cadmin.ui.fragment.organisationinfo.Users;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.thundersharp.cadmin.ui.activity.MainActivity.floatingActionButton;

public class OrginasationDetails extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;
    TextView detail_org_name,descwhole1,no_of_employee,no_of_projects,no_of_managers;
    Button btn_mail_manager,edit_org;
    LinearLayout layout_work_force;
    //Organisations orgs;
    CircleImageView org_logo12;
    String org_name,org_desc,org_image,organiser_id,no_of_workforce;
    public static String org_id;
    ProgressDialog progressDialog;
    Boolean manager=false;
    List<String> managers;
    int users;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_orginasation_details, container, false);
        MainActivity.container.setBackground(null);
        floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_delete_outline_24,getActivity().getTheme()));
       // final org_details_model data = savedInstanceState.getParcelable("data");
       // final Organisations orgs=savedInstanceState.getParcelable("orgs");
        managers=new ArrayList<>();
        layout_work_force=root.findViewById(R.id.layout_work_force);
        org_logo12=root.findViewById(R.id.org_logo12);
        detail_org_name=root.findViewById(R.id.detail_org_name);
        descwhole1=root.findViewById(R.id.descwhole1);
        no_of_employee=root.findViewById(R.id.no_of_employee);
        no_of_projects=root.findViewById(R.id.no_of_projects);
        no_of_managers=root.findViewById(R.id.no_of_managers);
        btn_mail_manager=root.findViewById(R.id.btn_mail_manager);
        edit_org=root.findViewById(R.id.edit_org);
        users=0;
        no_of_employee.setText("0");
        org_id = "null";

       // total_no_of_workforce();
        Bundle bundle =this.getArguments();
        if (getArguments()!=null){
            org_name=bundle.getString("org_name");
            org_desc=bundle.getString("org_desc");
            org_id=bundle.getString("org_id");
            org_image=bundle.getString("org_image");
            organiser_id=bundle.getString("organiser_id");
            org_id = bundle.getString("org_id");

           setDetails(org_name,org_desc,org_id,org_image,organiser_id);
            fetchWorkForce(org_id,no_of_workforce);

            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // deletingorganisations(org_id);
                    deleteorgfromorgnode(org_id);
                }
            });


        }else {
            Toast.makeText(getContext(),"no data found", Toast.LENGTH_SHORT).show();

        }


        tabLayout = root.findViewById(R.id.sliding_tabs1);
        viewPager = root.findViewById(R.id.viewpager1);

        TabAdapter tabAdapter = new TabAdapter(getParentFragmentManager());
        tabAdapter.addFragment(new Users(),null);
        tabAdapter.addFragment(new Photos(),null);


        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_supervised_user_circle_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_menu_gallery);


        return root;
    }

    private void deleteorgfromorgnode(final String org_id) {
        FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getUid())
                .child("organisations")
                .child(org_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String manager=String.valueOf(snapshot.getValue());
                    if (manager.equals("true")){
                        deleteorg(org_id);
                    }
                }else {
                    Toast.makeText(getActivity(), "Sorry you are not the member of this company !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void deletingorganisations(final String org_id) {
        FirebaseDatabase.getInstance().getReference("organisation")
                .child(org_id)
                .child("managers")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot snapshot1:snapshot.getChildren()){
                                //managers.add(snapshot1.getKey());
                                if (FirebaseAuth.getInstance().getUid().equals(snapshot1.getKey())){
                                    //manager=true;
                                   // deleteorg(org_id,true);
                                    break;

                                    //manager=false;
                                   // deleteorg(org_id,false);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    //TODO delete projects from user node

    private void deleteorg(final String org_id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete");
        builder.setMessage("Are You Really Going To Delete This Data  ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setTitle("Deleting");
                progressDialog.setMessage("Deleting please wait!!!");
                progressDialog.show();
                FirebaseDatabase.getInstance().getReference("organisation")
                        .child(org_id)
                        .child("org_users")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    for (DataSnapshot snapshot1:snapshot.getChildren()){
                                        FirebaseDatabase.getInstance().getReference("users")
                                                .child(snapshot1.getKey())
                                                .child("organisations")
                                                .child(org_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Toast.makeText(getActivity(), "All members are removed from this ", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }else {
                                    Toast.makeText(getActivity(), "This organisation doesn't contain any member", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                FirebaseDatabase.getInstance().getReference("organisation")
                        .child(org_id)
                        .child("managers")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    for (DataSnapshot snapshot1:snapshot.getChildren()){
                                        FirebaseDatabase.getInstance().getReference("users")
                                                .child(snapshot1.getKey())
                                                .child("organisations")
                                                .child(org_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getActivity(), "All managers are removed from this organisation ", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }else {
                                    Toast.makeText(getActivity(), "This organisation doesn't contain any manager", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                FirebaseDatabase.getInstance().getReference()
                        .child("organisation")
                        .child(org_id).removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    dialog.dismiss();
                                    progressDialog.dismiss();
                                    MainActivity.navController.navigate(R.id.nav_organisation);
                                    Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
        /*
          if (key){
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Delete");
                    builder.setMessage("Are You Really Going To Delete This Data  ?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {
                            progressDialog = new ProgressDialog(getActivity());
                            progressDialog.setTitle("Deleting");
                            progressDialog.setMessage("Deleting please wait!!!");
                            progressDialog.show();
                            FirebaseDatabase.getInstance().getReference("organisation")
                                    .child(org_id)
                                    .child("org_users")
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                        for (DataSnapshot snapshot1:snapshot.getChildren()){
                                            FirebaseDatabase.getInstance().getReference("users")
                                                    .child(snapshot1.getKey())
                                                    .child("organisations")
                                                    .child(org_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        //TODO somthing for manager node
                                                    }
                                                }
                                            });
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            FirebaseDatabase.getInstance().getReference("organisation")
                                    .child(org_id)
                                    .child("managers")
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()){
                                                for (DataSnapshot snapshot1:snapshot.getChildren()){
                                                    FirebaseDatabase.getInstance().getReference("users")
                                                            .child(snapshot1.getKey())
                                                            .child("organisations")
                                                            .child(org_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()){

                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                            FirebaseDatabase.getInstance().getReference()
                                    .child("organisation")
                                    .child(org_id).removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                dialog.dismiss();
                                                progressDialog.dismiss();
                                                MainActivity.navController.navigate(R.id.nav_organisation);
                                                Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();

    }
});
        }else {
        Toast.makeText(getContext(),"You are not a manager consult to manager for this action to be perrformed" ,Toast.LENGTH_SHORT).show();
        }
         */
        /*
                if (orgs.isManager()){
                    Alert dilog here
                    Toast.makeText(getActivity(), "You have deleted the noe from everywhere", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(),"Sorry you can't delete this organisation !",Toast.LENGTH_SHORT).show();
                }

                FirebaseDatabase.getInstance().getReference("users").child("organisations")
                        .child(data.getOrganisation_id()).removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {


                    }
                });
                */

    }


    private void fetchWorkForce(String org_id, final String no_of_workforce) {
        FirebaseDatabase.getInstance().getReference("organisation")
                .child(org_id)
                .child("org_users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    long l=snapshot.getChildrenCount();
                    no_of_employee.setText(String.valueOf(l));
                    //TODO
                }else {
                    Toast.makeText(getActivity(), "no users found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
       //TODO ADDING USERS
    }

    private void setDetails(String org_name, String org_desc, final String org_id,final String org_image, String organiser_id) {
        String organiser_id1=organiser_id;
        String image_view=org_image;
        detail_org_name.setText(org_name);
        descwhole1.setText(org_desc);
        Glide.with(getContext()).load(org_image).into(org_logo12);
        FirebaseDatabase.getInstance()
                .getReference("organisation")
                .child(org_id)
                .child("projects")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot snapshot1:snapshot.getChildren()){
                        long projects_no=snapshot1.getChildrenCount();
                        no_of_projects.setText(Long.toString(projects_no));
                        layout_work_force.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //add dilog
                                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle("User Uid here");
                                View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.add_user_to_org,null);
                                final EditText user_uid_here=view1.findViewById(R.id.txt_user_uid);
                                final Button btn_verify_users=view1.findViewById(R.id.btn_verify_users);
                                builder.setCancelable(true);
                                builder.setView(view1);
                                btn_verify_users.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        final String user_uid =user_uid_here.getEditableText().toString();
                                        FirebaseDatabase.getInstance().getReference("users").child(user_uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                                                if (snapshot.exists()){
                                                    //Toast.makeText(getActivity(), "sent request to user ", Toast.LENGTH_SHORT).show();

                                                    FirebaseDatabase.getInstance()
                                                            .getReference("organisation")
                                                            .child(org_id)
                                                            .child("org_users").addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                            String user_no="0";
                                                            if (snapshot.exists()){
                                                                for (DataSnapshot snapshot11:snapshot.getChildren()){
                                                                    user_no=snapshot11.getKey();
                                                                }
                                                                int next_user=Integer.parseInt(user_no)+1;
                                                                FirebaseDatabase.getInstance()
                                                                        .getReference("organisation")
                                                                        .child(org_id)
                                                                        .child("org_users")
                                                                        .child(String.valueOf(next_user))
                                                                        .setValue(user_uid).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()){
                                                                            savetousernode(org_id,user_uid);

                                                                            Toast.makeText(getActivity(), "User Added", Toast.LENGTH_SHORT).show();

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

                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

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
                }
                else {
                    no_of_projects.setText("0");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void savetousernode(String org_id, String user_uid) {

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

    }

    /**
     *for (final DataSnapshot snapshot2:snapshot1.getChildren()){
     *                         FirebaseDatabase.getInstance().getReference("organisation").child(org_id).child("projects").child(snapshot2.getKey()).child("users_uid").addListenerForSingleValueEvent(new ValueEventListener() {
     *                             @Override
     *                             public void onDataChange(@NonNull DataSnapshot snapshot) {
     *                                 for (DataSnapshot snapshot3:snapshot2.getChildren()){
     *                                     // long i=snapshot3.getChildrenCount();
     *                                     users=users+1;
     *                                 }
     *
     *                             }
     *
     *                             @Override
     *                             public void onCancelled(@NonNull DatabaseError error) {
     *
     *                             }
     *                         });
     *                         users=users+1;
     *                     }
     *                     users=users+1;
     *                     no_of_employee.setText(users);
     */
}