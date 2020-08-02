package com.thundersharp.cadmin.ui.fragment.projetinfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.core.globalAdapters.TabAdapter;
import com.thundersharp.cadmin.ui.activity.MainActivity;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.thundersharp.cadmin.ui.activity.MainActivity.floatingActionButton;

public class ProjectDetails extends Fragment {

    TextView projtittle,descwhole,project_users,no_of_todo;
    ImageView completed_iv;
    Button edit_proj,mail_manager;
    TabLayout tabLayout;
    ViewPager viewPager;
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

    private void setDetails(String proj_name, String proj_desc, String proj_id, String org_ids,String org_image,Boolean status)
    {
        projtittle.setText(proj_name);
        descwhole.setText(proj_desc);
        Glide.with(getContext()).load(org_image).into(org_logo2);
        if (status.equals(true)){
            completed_iv.setImageResource(R.drawable.complted);
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
