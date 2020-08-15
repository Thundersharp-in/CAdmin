package com.thundersharp.cadmin.ui.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.core.globalAdapters.ManagerModelsAdapter;
import com.thundersharp.cadmin.core.globalAdapters.TabAdapter;
import com.thundersharp.cadmin.core.globalmodels.UserData;
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
    TextView detail_org_name, descwhole1, no_of_employee, no_of_projects, no_of_managers;
    Button btn_mail_manager, edit_org;
    LinearLayout layout_work_force,managerr;
    CircleImageView org_logo12;
    String org_name, org_desc, org_image, organiser_id;
    String logo,logoref,managername,managerid;
    public static String org_id;
    ProgressDialog progressDialog;
    org_details_model org_details_model1;
    UserData userData;
    int users;

    @SuppressLint("CutPasteId")
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_orginasation_details, container, false);
        MainActivity.container.setBackground(null);
        floatingActionButton
                .setImageDrawable(getResources()
                        .getDrawable(R.drawable.ic_baseline_delete_outline_24,
                                getActivity()
                                        .getTheme()));

        layout_work_force = root.findViewById(R.id.layout_work_force);
        org_logo12 = root.findViewById(R.id.org_logo12);
        detail_org_name = root.findViewById(R.id.detail_org_name);
        descwhole1 = root.findViewById(R.id.descwhole1);
        no_of_employee = root.findViewById(R.id.no_of_employee);
        no_of_projects = root.findViewById(R.id.no_of_projects);
        no_of_managers = root.findViewById(R.id.no_of_managers);
        btn_mail_manager = root.findViewById(R.id.btn_mail_manager);
        edit_org = root.findViewById(R.id.edit_org);
        managerr=root.findViewById(R.id.managers);
        users = 0;
        detail_org_name.setText("no organisation name");
        descwhole1.setText("no description to show");

        no_of_employee.setText("0");
        no_of_projects.setText("0");
        no_of_managers.setText("0");
        org_id = "null";


        tabLayout = root.findViewById(R.id.sliding_tabs1);
        viewPager = root.findViewById(R.id.viewpager1);

        TabAdapter tabAdapter = new TabAdapter(getParentFragmentManager());
        tabAdapter.addFragment(new Users(), null);
        tabAdapter.addFragment(new Photos(), null);


        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_supervised_user_circle_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_menu_gallery);

        final Bundle bundle = this.getArguments();
        if (getArguments() != null) {
            org_name = bundle.getString("org_name");
            org_desc = bundle.getString("org_desc");
            org_id = bundle.getString("org_id");
            org_image = bundle.getString("org_image");
            organiser_id = bundle.getString("organiser_id");

            setDetails(org_name, org_desc, org_image);
            fetchWorkForce(org_id);
            fetchManagers(org_id);
            fetchprojects(org_id);

            edit_org.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { updateData(org_id,org_image,org_desc,org_name);
                }
            });

            managerr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { uploadmanager(org_id);
                }
            });

            layout_work_force.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { addusertoorg(org_id);
                }
            });
            //todo mail manager to be done
            btn_mail_manager.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) { btnmailmanager(org_id);
                }
            });

            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { deleteorgfromorgnode(org_id);
                }
            });

        }
        else {
            Toast.makeText(getContext(), "no data found", Toast.LENGTH_SHORT).show();
            MainActivity.navController.navigate(R.id.nav_organisation);
        }
        return root;
    }

    private void setDetails(String org_name, String org_desc, final String org_image) {
        detail_org_name.setText(org_name);
        descwhole1.setText(org_desc);
        Glide.with(getContext()).load(org_image).into(org_logo12);
        /* layout_work_force.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        builder.setTitle("User Uid here");
                                        View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.add_user_to_org, null);
                                        final EditText user_uid_here = view1.findViewById(R.id.txt_user_uid);
                                        final Button btn_verify_users = view1.findViewById(R.id.btn_verify_users);
                                        builder.setCancelable(true);
                                        builder.setView(view1);
                                        btn_verify_users.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                final String user_uid = user_uid_here.getEditableText().toString();
                                                FirebaseDatabase.getInstance()
                                                        .getReference("users")
                                                        .child(user_uid)
                                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull final DataSnapshot snapshot) {
                                                        if (snapshot.exists()) {

                                                            FirebaseDatabase.getInstance()
                                                                    .getReference("organisation")
                                                                    .child(org_id)
                                                                    .child("org_users")
                                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    if (snapshot.exists()){
                                                                        FirebaseDatabase.getInstance()
                                                                                .getReference("users")
                                                                                .child(user_uid)
                                                                                .child("personal_data")
                                                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                    @Override
                                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                        if (snapshot.exists()){
                                                                                            userData=snapshot.getValue(UserData.class);
                                                                                            String users_data =userData.getName()+"\n"+userData.getEmail();
                                                                                            users_upload(user_uid,users_data,org_id);
                                                                                        }
                                                                                    }

                                                                                    @Override
                                                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                                                    }
                                                                                });

                                                                    }
                                                                }
                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                        }
                                                            });

                                                        } else {
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


                                 */

    }

    private void fetchprojects(String org_id) {
        FirebaseDatabase
                .getInstance()
                .getReference("organisation")
                .child(org_id)
                .child("projects")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            no_of_projects.setText(String.valueOf(snapshot.getChildrenCount()));
                        }else {
                            no_of_projects.setText("0");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void fetchManagers(String org_id) {
        FirebaseDatabase.getInstance()
                .getReference("organisation")
                .child(org_id)
                .child("managers")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            long managers=snapshot.getChildrenCount();
                            no_of_managers.setText(String.valueOf(managers));
                        }else{
                            no_of_managers.setText("0");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void fetchWorkForce(String org_id) {
        FirebaseDatabase
                .getInstance()
                .getReference("organisation")
                .child(org_id)
                .child("org_users")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            long l = snapshot.getChildrenCount();
                            no_of_employee.setText(String.valueOf(l));
                        } else {
                            no_of_employee.setText("0");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void updateData(final String org_id,final String org_image ,final String org_desc,final String org_name) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                final LayoutInflater inflater1 = getLayoutInflater();
                View alertLayout = inflater1.inflate(R.layout.update_organisation, null);

                alert.setView(alertLayout);
                alert.setCancelable(true);

                final Button update_button = alertLayout.findViewById(R.id.update_btn);
                final Button can = alertLayout.findViewById(R.id.update_cancel);
                final TextInputLayout update_name = alertLayout.findViewById(R.id.update_name);
                final TextInputLayout update_desc = alertLayout.findViewById(R.id.update_desc);
                final ImageView imageView = alertLayout.findViewById(R.id.update_logo);//TODO update org image
                update_name.getEditText().setText(org_name);
                update_desc.getEditText().setText(org_desc);
                final Dialog dialog = alert.create();
                update_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (update_name.getEditText().getText().toString().isEmpty()){
                            update_name.setError("required!");
                            update_name.requestFocus();
                            return;
                        } else if (update_desc.getEditText().getText().toString().isEmpty()){
                            update_desc.setError("required!");
                            update_desc.requestFocus();
                            return;
                        }else {

                            FirebaseDatabase.getInstance()
                                    .getReference("organisation")
                                    .child(org_id)
                                    .child("description")
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()){
                                                dialog.dismiss();
                                                org_details_model1 = snapshot.getValue(org_details_model.class);
                                                logo=  org_details_model1.getCompany_logo();
                                                logoref=org_details_model1.getLogo_ref();
                                                managername=org_details_model1.getOrganiser_name();
                                                managerid=org_details_model1.getOrganiser_uid();
                                                 final org_details_model orgDetailsModel = new org_details_model(
                                                        update_name.getEditText().getText().toString(),
                                                        logo,
                                                        logoref,
                                                        org_id,
                                                        update_desc.getEditText().getText().toString(),
                                                        managername,
                                                        managerid);
                                                FirebaseDatabase.getInstance()
                                                        .getReference("organisation")
                                                        .child(org_id)
                                                        .child("description")
                                                        .setValue(orgDetailsModel)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()){
                                                                    detail_org_name.setText(orgDetailsModel.getOrganisation_name());
                                                                    descwhole1.setText(orgDetailsModel.getCompany_description());
                                                                    Toast.makeText(getActivity(), "Organisation details are successfully updated ", Toast.LENGTH_SHORT).show();
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

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                        }
                    }
                });

                can.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                alert.show();

            }

    private void uploadmanager(final String org_id) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Enter Manager Uid here");
        View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.add_user_to_org,null);
        final EditText user_uid_here=view1.findViewById(R.id.txt_user_uid);
        final Button btn_verify_users=view1.findViewById(R.id.btn_verify_users);
        builder.setCancelable(true);
        builder.setView(view1);
        btn_verify_users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_uid_here.getText().toString().isEmpty()){
                    user_uid_here.setError("Required !");
                    user_uid_here.requestFocus();
                }else {
                    final String user_uid = user_uid_here.getEditableText().toString();
                    FirebaseDatabase
                            .getInstance()
                            .getReference("users")
                            .child(user_uid)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){

                                        FirebaseDatabase
                                                .getInstance()
                                                .getReference("users")
                                                .child(user_uid)
                                                .child("personal_data")
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if (snapshot.exists()){
                                                            userData=snapshot.getValue(UserData.class);
                                                            String users_data =userData.getName()+"\n"+userData.getEmail()+"\n"+userData.getPhone_no()+"\n"+userData.getUid();
                                                            manager(org_id,users_data,user_uid);
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });


                                        /*
                                         FirebaseDatabase.getInstance().getReference("organisation")
                .child(model.getOrganisation_id())
                .child("managers")
                .child(userData.getUid())
                .setValue(userData.getName()+"\n"+userData.getEmail()+"\n"+userData.getPhone_no()+"\n"+userData.getUid())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getActivity(), "Now you are the manager if this group", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getActivity(), "OOPS ! Sorry you can't create this group", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
                                         */

                                    }else {
                                        Toast.makeText(getActivity(), "No users exist !", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }

            }
        });
        builder.show();
    }

    private void manager(final String org_id, String users_data, final String user_uid) {
        FirebaseDatabase
                .getInstance()
                .getReference("organisation")
                .child(org_id)
                .child("managers")
                .child(user_uid)
                .setValue(users_data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    FirebaseDatabase.getInstance()
                            .getReference("users")
                            .child(user_uid)
                            .child("organisations")
                            .child(org_id)
                            .setValue(true)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(getActivity(), "All things are done ", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(getActivity(), "This user have problem", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }else {
                    Toast.makeText(getActivity(), "Something went wrong try another ", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addusertoorg(final String org_id) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Enter Manager Uid here");
        View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.add_user_to_org,null);
        final EditText user_uid_here=view1.findViewById(R.id.txt_user_uid);
        final Button btn_verify_users=view1.findViewById(R.id.btn_verify_users);
        builder.setCancelable(true);
        builder.setView(view1);
        btn_verify_users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_uid_here.getText().toString().isEmpty()){
                    user_uid_here.setError("Required !");
                    user_uid_here.requestFocus();
                }else {
                    final String user_uid = user_uid_here.getEditableText().toString();
                    FirebaseDatabase
                            .getInstance()
                            .getReference("users")
                            .child(user_uid)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){

                                        FirebaseDatabase
                                                .getInstance()
                                                .getReference("users")
                                                .child(user_uid)
                                                .child("personal_data")
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if (snapshot.exists()){
                                                            userData=snapshot.getValue(UserData.class);
                                                            String users_data =userData.getName()+"\n"+userData.getEmail()+"\n"+userData.getPhone_no()+"\n"+userData.getUid();
                                                            user(org_id,users_data,user_uid);
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });


                                        /*
                                         FirebaseDatabase.getInstance().getReference("organisation")
                .child(model.getOrganisation_id())
                .child("managers")
                .child(userData.getUid())
                .setValue(userData.getName()+"\n"+userData.getEmail()+"\n"+userData.getPhone_no()+"\n"+userData.getUid())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getActivity(), "Now you are the manager if this group", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getActivity(), "OOPS ! Sorry you can't create this group", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
                                         */

                                    }else {
                                        Toast.makeText(getActivity(), "No users exist !", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }

            }
        });
        builder.show();
    }

    private void user(final String org_id, String users_data, final String user_uid) {
        FirebaseDatabase
                .getInstance()
                .getReference("organisation")
                .child(org_id)
                .child("org_users")
                .child(user_uid)
                .setValue(users_data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            FirebaseDatabase.getInstance()
                                    .getReference("users")
                                    .child(user_uid)
                                    .child("organisations")
                                    .child(org_id)
                                    .setValue(false)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(getActivity(), "All things are done ", Toast.LENGTH_SHORT).show();
                                            }else {
                                                Toast.makeText(getActivity(), "This user have problem", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }else {
                            Toast.makeText(getActivity(), "Something went wrong try another ", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteorgfromorgnode(final String org_id) {
        FirebaseDatabase
                .getInstance()
                .getReference("users")
                .child(FirebaseAuth.getInstance().getUid())
                .child("organisations")
                .child(org_id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String manager = String.valueOf(snapshot.getValue());
                            if (manager.equals("true")) {
                                deleteorg(org_id);
                            }else {
                                Toast.makeText(getActivity(),"You are not the manager of this organisation ",Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Sorry you are not the member of this company !", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getActivity(),error.getMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                });
    }

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
                FirebaseDatabase
                        .getInstance()
                        .getReference("organisation")
                        .child(org_id)
                        .child("managers")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    for (DataSnapshot snapshot1:snapshot.getChildren()){
                                        FirebaseDatabase
                                                .getInstance()
                                                .getReference("organisation")
                                                .child(org_id)
                                                .child("managers")
                                                .child(snapshot1.getKey())
                                                .removeValue();
                                    }
                                }else {
                                    Toast.makeText(getActivity(), "no manager found", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                FirebaseDatabase
                        .getInstance()
                        .getReference("organisation")
                        .child(org_id)
                        .child("org_users")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                        FirebaseDatabase
                                                .getInstance()
                                                .getReference("users")
                                                .child(snapshot1.getKey())
                                                .child("organisations")
                                                .child(org_id).removeValue();


                                    }
                                } else {
                                    Toast.makeText(getActivity(), "This organisation doesn't contain any member", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                FirebaseDatabase
                        .getInstance()
                        .getReference("organisation")
                        .child(org_id)
                        .child("projects")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    for (DataSnapshot snapshot1:snapshot.getChildren()){
                                        FirebaseDatabase.getInstance()
                                                .getReference("organisation")
                                                .child(org_id)
                                                .child("projects")
                                                .child(snapshot1.getKey())
                                                .child("users_uid")
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if (snapshot.exists()){
                                                            for (DataSnapshot snapshot11:snapshot.getChildren()){
                                                                FirebaseDatabase.getInstance()
                                                                        .getReference("users")
                                                                        .child(snapshot11.getKey())
                                                                        .child("projects")
                                                                        .removeValue();
                                                            }

                                                        }else {
                                                            Toast.makeText(getActivity(), "No users found", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                    }
                                }else {
                                    Toast.makeText(getActivity(), "No projects are there", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                FirebaseDatabase
                        .getInstance()
                        .getReference("organisation")
                        .child(org_id)
                        .removeValue();
                dialog.dismiss();
                progressDialog.dismiss();
                MainActivity.navController.navigate(R.id.nav_organisation);
                Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();

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

    private void btnmailmanager(final String org_id) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater1 = getLayoutInflater();
        View alert = inflater1.inflate(R.layout.email_to_manager, null);
        builder.setView(alert);
        builder.setCancelable(true);
        final Dialog dialog = builder.create();

        final List<String> list;
        final EditText editTo = alert.findViewById(R.id.et_to);
        final Button send = alert.findViewById(R.id.send_txt);
        final Button cancel = alert.findViewById(R.id.cancel_text);
        final RecyclerView recyclerView = alert.findViewById(R.id.all_managers);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        list = new ArrayList<>();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTo.getText().toString().isEmpty()) {
                    editTo.setError("required");
                    return;
                }
                else {
                    FirebaseDatabase.getInstance()
                            .getReference("organisation")
                            .child(org_id)
                            .child("managers")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                        //TODO not showing manager name
                                        for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                                            Toast.makeText(getContext(), dataSnapshot.getKey().toString(), Toast.LENGTH_SHORT).show();
                                            list.add((String) dataSnapshot.getValue());
                                        }
                                    }
                                    final ManagerModelsAdapter adapter = new ManagerModelsAdapter(getContext(),list);
                                    recyclerView.setAdapter(adapter);
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

}