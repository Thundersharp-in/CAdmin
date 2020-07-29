package com.thundersharp.cadmin.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thundersharp.cadmin.core.globalAdapters.HomeOrgAdapter;
import com.thundersharp.cadmin.core.globalAdapters.TabAdapter;
import com.thundersharp.cadmin.core.globalmodels.org_details_model;
import com.thundersharp.cadmin.ui.activity.MainActivity;
import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.ui.fragment.projetinfo.Files;
import com.thundersharp.cadmin.ui.fragment.projetinfo.Photo;
import com.thundersharp.cadmin.ui.fragment.projetinfo.Video;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.thundersharp.cadmin.ui.activity.MainActivity.floatingActionButton;

public class HomeFragment extends Fragment  {

    //TabLayout tabLayout;
    //ViewPager viewPager;
    CardView relq,c1,c3,c4;
    ImageView fn1,fn2,fn3,fn4;
    RecyclerView recyclervieworg;
    Animation fadein,fadeout,clockwise;
    HomeOrgAdapter homeOrgAdapter;
    SharedPreferences sharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_chat_bubble_outline_24,getActivity().getTheme()));
        MainActivity.container.setBackground(null);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.navController.navigate(R.id.nav_chats);
            }
        });

        fadein = AnimationUtils.loadAnimation(getActivity(),R.anim.fadein);
        fadeout = AnimationUtils.loadAnimation(getActivity(),R.anim.fadeout);
        clockwise = AnimationUtils.loadAnimation(getActivity(),R.anim.clockwise_rotate);
        recyclervieworg=root.findViewById(R.id.recyclervieworg);
        sharedPreferences = getActivity().getSharedPreferences("all_organisation", Context.MODE_PRIVATE);

        relq = root.findViewById(R.id.relq);
        c1 = root.findViewById(R.id.c1);
        c3 = root.findViewById(R.id.c3);
        c4 = root.findViewById(R.id.c4);

        fn1 =root.findViewById(R.id.fn1);
        fn2 = root.findViewById(R.id.fn2);
        fn3 = root.findViewById(R.id.fn3);
        fn4 = root.findViewById(R.id.fn4);

        loadOrganisation();
        relq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.navController.navigate(R.id.nav_work_force);
            }
        });

        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.navController.navigate(R.id.nav_organisation);
            }
        });

        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.navController.navigate(R.id.nav_cal);
            }
        });

        c4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.navController.navigate(R.id.nav_proj);
            }
        });



        //tabLayout = root.findViewById(R.id.sliding_tabs);
        //viewPager = root.findViewById(R.id.viewpager);


/*        TabAdapter tabAdapter = new TabAdapter(getParentFragmentManager());
        tabAdapter.addFragment(new Photo(), null);
        tabAdapter.addFragment(new Files(), null);
        tabAdapter.addFragment(new Video(), null);

        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_outline_photo_library_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_outline_file_copy_24);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_outline_video_library_24);*/

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        final ObjectAnimator oa1 = ObjectAnimator.ofFloat(c1, "scaleX", 1f, 0f);
        final ObjectAnimator oa2 = ObjectAnimator.ofFloat(c1, "scaleX", 0f, 1f);
        oa1.setInterpolator(new DecelerateInterpolator());
        oa2.setInterpolator(new AccelerateDecelerateInterpolator());
        oa1.setDuration(400);
        oa2.setDuration(400);
        oa1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //imageView.setImageResource(R.drawable.frontSide);
                oa2.start();
            }
        });
        oa1.start();



        final ObjectAnimator oa3 = ObjectAnimator.ofFloat(relq, "scaleX", 1f, 0f);
        final ObjectAnimator oa4 = ObjectAnimator.ofFloat(relq, "scaleX", 0f, 1f);
        oa3.setInterpolator(new DecelerateInterpolator());
        oa4.setInterpolator(new AccelerateDecelerateInterpolator());
        oa3.setDuration(400);
        oa4.setDuration(400);
        oa3.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                fn1.startAnimation(clockwise);
                //imageView.setImageResource(R.drawable.frontSide);
                oa4.start();
            }
        });
        oa3.start();


        final ObjectAnimator oa5 = ObjectAnimator.ofFloat(c3, "scaleX", 1f, 0f);
        final ObjectAnimator oa6 = ObjectAnimator.ofFloat(c3, "scaleX", 0f, 1f);
        oa5.setInterpolator(new DecelerateInterpolator());
        oa6.setInterpolator(new AccelerateDecelerateInterpolator());
        oa5.setDuration(500);
        oa6.setDuration(500);
        oa5.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //imageView.setImageResource(R.drawable.frontSide);
                oa6.start();
            }
        });
        oa5.start();


        final ObjectAnimator oa7 = ObjectAnimator.ofFloat(c4, "scaleX", 1f, 0f);
        final ObjectAnimator oa8 = ObjectAnimator.ofFloat(c4, "scaleX", 0f, 1f);
        oa7.setInterpolator(new DecelerateInterpolator());
        oa8.setInterpolator(new AccelerateDecelerateInterpolator());
        oa7.setDuration(400);
        oa8.setDuration(400);
        oa7.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //imageView.setImageResource(R.drawable.frontSide);
                fn4.startAnimation(fadein);
                fadein.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        fn4.startAnimation(fadeout);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                oa8.start();
            }
        });
        oa7.start();

    }

    private void loadOrganisation(){
        List<org_details_model> org_details_models = loadOrgdetailfromPrefs();
        if (org_details_models !=null){
            homeOrgAdapter = new HomeOrgAdapter(getActivity(),org_details_models);
            recyclervieworg.setAdapter(homeOrgAdapter);

        }else {
            Toast.makeText(getActivity(),"No organisations",Toast.LENGTH_SHORT).show();
        }

    }

    private List<org_details_model> loadOrgdetailfromPrefs(){
        String data;
        Gson gson=new Gson();
        data=sharedPreferences.getString("org","null");
        if (!data.equals("null")){  //equalsIgnoreCase("null")
            Type type = new TypeToken<ArrayList<org_details_model>>(){}.getType();
            return gson.fromJson(data,type);
        }else return null;
    }
}