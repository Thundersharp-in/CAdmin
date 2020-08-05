package com.thundersharp.cadmin.ui.fragment.projetinfo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.core.globalAdapters.TabAdapter;
import com.thundersharp.cadmin.core.globalmodels.AddProject_model;
import com.thundersharp.cadmin.ui.activity.MainActivity;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.thundersharp.cadmin.ui.activity.MainActivity.floatingActionButton;

public class ProjectDetails extends Fragment {

    TextView projtittle,descwhole,project_users,no_of_todo;
    ImageView completed_iv;
    Button edit_proj,mail_manager;
    TabLayout tabLayout;
    ViewPager viewPager;
    AddProject_model addProject_model;
    CircleImageView org_logo2;
    public static String project_key;
    String proj_name,proj_desc,proj_id,org_ids,org_image;
    Boolean status;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.project_info, container, false);

        MainActivity.container.setBackground(null);
        floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_outline_group_add_24,getActivity().getTheme()));
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //MainActivity.navController.navigate(R.id.nav_chats);
            }
        });

        tabLayout = root.findViewById(R.id.sliding_tabs);
        viewPager = root.findViewById(R.id.viewpager);

        projtittle = root.findViewById(R.id.projtittle);
        descwhole = root.findViewById(R.id.descwhole);
        project_users = root.findViewById(R.id.project_users);
        no_of_todo = root.findViewById(R.id.no_of_todo);
        completed_iv = root.findViewById(R.id.completed_iv);
        mail_manager = root.findViewById(R.id.mail_manager);
        edit_proj = root.findViewById(R.id.edit_proj);
        org_logo2 = root.findViewById(R.id.org_logo2);

        mail_manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater1 = getLayoutInflater();
                View alert = inflater1.inflate(R.layout.email_to_manager, null);
                builder.setView(alert);
                builder.setCancelable(true);

                final EditText editTo = alert.findViewById(R.id.et_to);
                final EditText editSub = alert.findViewById(R.id.et_subject);
                final EditText editMessage = alert.findViewById(R.id.et_message);
                final Button send = alert.findViewById(R.id.send_txt);
                final Button cancel = alert.findViewById(R.id.cancel_text);

                 final Dialog dialog = builder.create();

                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (editTo.getText().toString().isEmpty()) {
                            editTo.setError("required");
                            return;

                        } else if (editSub.getText().toString().isEmpty()) {
                            editSub.setError("required");
                            return;

                        } else if (editMessage.getText().toString().isEmpty()) {
                            editMessage.setError("required");
                            return;

                        } else {
                            dialog.dismiss();
                            Intent intent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("mailto:"+editTo.getText().toString()));
                            intent.putExtra(Intent.EXTRA_SUBJECT, editSub.getText().toString());
                            intent.putExtra(Intent.EXTRA_TEXT, editMessage.getText().toString());
                            startActivity(intent);
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
        });

        Bundle bundle =this.getArguments();
        project_key="null";
        if (getArguments()!=null){
            proj_name=bundle.getString("proj_name");
            proj_desc=bundle.getString("proj_desc");
            proj_id=bundle.getString("proj_id");
            org_ids=bundle.getString("org_id");
            org_image=bundle.getString("org_image");
            project_key=bundle.getString("proj_id");
            status=bundle.getBoolean("proj_status");

            edit_proj.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateData(proj_id);
                }

            });
            setDetails(proj_name,proj_desc,proj_id,org_ids,org_image,status);
        }else {
            Toast.makeText(getContext(),"no data found", Toast.LENGTH_SHORT).show();
        }

        TabAdapter tabAdapter = new TabAdapter(getParentFragmentManager());
        tabAdapter.addFragment(new Photo(), null);
        tabAdapter.addFragment(new Files(), null);
        tabAdapter.addFragment(new Video(), null);


        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_outline_photo_library_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_outline_file_copy_24);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_outline_video_library_24);

        return root;
    }

    private void updateData(final String proj_id) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.update_project_details,null);

        builder.setView(view);
        builder.setCancelable(true);

        final Button update_button = view.findViewById(R.id.update_btn);
        final Button can = view.findViewById(R.id.update_cancel);
        final TextInputLayout update_name = view.findViewById(R.id.update_proj_name);
        final TextInputLayout update_desc = view.findViewById(R.id.update_proj_desc);

        final Dialog dialog = builder.create();

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
                            .child(org_ids)
                            .child("projects")
                            .child(proj_id)
                            .child("description")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                        addProject_model = snapshot.getValue(AddProject_model.class);

                                        AddProject_model model = new AddProject_model(
                                                update_name.getEditText().getText().toString(),
                                                update_desc.getEditText().getText().toString(),
                                                proj_id,
                                                org_ids,
                                                false);
                                        UpdateProj(model,proj_id);
                                    }
                                }

                                private void UpdateProj(final AddProject_model model, String proj_id) {
                                    FirebaseDatabase.getInstance()
                                            .getReference("organisation")
                                            .child(org_ids)
                                            .child("projects")
                                            .child(proj_id)
                                            .child("description")
                                            .setValue(model)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        dialog.dismiss();
                                                        Toast.makeText(getActivity(), "Update Done", Toast.LENGTH_SHORT).show();
                                                        projtittle.setText(model.getProjectName());
                                                        descwhole.setText(model.getProjectDesc());
                                                    }
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
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
        builder.show();
    }

    private void setDetails(String proj_name, String proj_desc, String proj_id, String org_ids,String org_image,Boolean status)
    {
        projtittle.setText(proj_name);
        descwhole.setText(proj_desc);
        Glide.with(getContext()).load(org_image).into(org_logo2);
        if (status.equals(true)){
            completed_iv.setImageResource(R.drawable.remove);
        }else {
            completed_iv.setImageResource(R.drawable.remove);
        }

        FirebaseDatabase.getInstance()
                .getReference("organisation")
                .child(org_ids)
                .child("projects")
                .child(proj_id)
                .child("users_uid")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    long users=snapshot.getChildrenCount();
                    project_users.setText(String.valueOf(users));

                }else {
                    Toast.makeText(getActivity(), "No users are there", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
