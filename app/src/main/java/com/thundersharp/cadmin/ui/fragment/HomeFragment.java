package com.thundersharp.cadmin.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.MediaRouteButton;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.core.globalAdapters.ExpandableListAdapter;
import com.thundersharp.cadmin.core.globalAdapters.HomeOrgAdapter;
import com.thundersharp.cadmin.core.globalmodels.AddProject_model;
import com.thundersharp.cadmin.core.globalmodels.org_details_model;
import com.thundersharp.cadmin.ui.activity.MainActivity;
import com.thundersharp.cadmin.ui.fragment.projetinfo.ProjectDetails;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.thundersharp.cadmin.ui.activity.MainActivity.floatingActionButton;

public class HomeFragment extends Fragment  {


    CardView relq,c1,c3,c4;
    ImageView fn1,fn2,fn3,fn4;
    RecyclerView recyclervieworg;
    Animation fadein,fadeout,clockwise;
    HomeOrgAdapter homeOrgAdapter;
    SharedPreferences sharedPreferences,sf1;
    ExpandableListView faqholder;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String,List<String>> listHash;
    ProgressBar progressBar;
    TextView txt_proj_name,txt_proj_id,txt_org_id,txt_desc;
    TextView textView,textView1;
    ImageView imageView,imageView1;
    ImageView org_image;
    Button btn_proj_detail;
    String proj_name,proj_id,org_id,proj_desc;
    CardView cv;

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
        sf1=getActivity().getSharedPreferences("last_visited_proj", MODE_PRIVATE);
        progressBar=root.findViewById(R.id.progresshome);
        progressBar.setVisibility(View.GONE);
        relq = root.findViewById(R.id.relq);
        c1 = root.findViewById(R.id.c1);
        c3 = root.findViewById(R.id.c3);
        c4 = root.findViewById(R.id.c4);
        faqholder=root.findViewById(R.id.faqholder);

        fn1 =root.findViewById(R.id.fn1);
        fn2 = root.findViewById(R.id.fn2);
        fn3 = root.findViewById(R.id.fn3);
        fn4 = root.findViewById(R.id.fn4);

        txt_proj_name =root.findViewById(R.id.txt_proj_name);
        txt_proj_id =root.findViewById(R.id.txt_proj_id);
        txt_org_id =root.findViewById(R.id.txt_org_id);
        txt_desc =root.findViewById(R.id.txt_desc);
        org_image =root.findViewById(R.id.org_image);
        btn_proj_detail =root.findViewById(R.id.btn_proj_detail);
        cv =root.findViewById(R.id.cv);
        textView = root.findViewById(R.id.tv1);
        imageView = root.findViewById(R.id.imageView1);
        textView1 = root.findViewById(R.id.tv2);
        imageView1 = root.findViewById(R.id.imageView2);
        loadOrganisation();
        if (sf1.getString("proj_name","no project till").equals("no project till")){
            textView1.setVisibility(View.VISIBLE);
            imageView1.setVisibility(View.VISIBLE);
        }else {
            textView1.setVisibility(View.GONE);
            imageView1.setVisibility(View.GONE);
            proj_name=sf1.getString("proj_name","no project till");
            proj_id=sf1.getString("proj_id","no project till");
            org_id=sf1.getString("org_id","no project till");
            proj_desc=sf1.getString("proj_desc","no project till");
            FirebaseDatabase
                    .getInstance()
                    .getReference("organisation")
                    .child(org_id)
                    .child("description")
                    .child("company_logo")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                org_image.setImageURI(Uri.parse(snapshot.getValue().toString()));
                            }else {
                                org_image=null;
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
            txt_proj_name.setText(proj_name);
            txt_proj_id.setText(proj_id);
            txt_org_id.setText(org_id);
            txt_desc.setText(proj_desc);
            btn_proj_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProjectDetails details=new ProjectDetails();
                    Bundle bundle = new Bundle();
                    // AddProject_model detail=data.get(getAdapterPosition());
                    bundle.putString("proj_name",proj_name);
                    bundle.putString("proj_desc",proj_desc);
                    bundle.putString("proj_id",proj_id);
                    bundle.putString("org_id",org_id);
                    bundle.putString("org_image",org_image.toString());//TODO images not set
                    bundle.putBoolean("proj_status",false);//TODO status not set
                    details.setArguments(bundle);
                    MainActivity.navController.navigate(R.id.nav_proj_info,bundle);
                }
            });

        }

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

        initData();
        listAdapter = new ExpandableListAdapter(getActivity(),listDataHeader,listHash);
        faqholder.setAdapter(listAdapter);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        progressBar.setVisibility(View.VISIBLE);
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
        progressBar.setVisibility(View.GONE);
    }

    private void loadOrganisation(){
        textView.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);
        List<org_details_model> org_details_models = loadOrgdetailfromPrefs();
        if (org_details_models !=null){
            textView.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
            homeOrgAdapter = new HomeOrgAdapter(getActivity(),org_details_models);
            recyclervieworg.setAdapter(homeOrgAdapter);
        }else {
            textView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageResource(R.drawable.sad);
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

    private void initData() {

        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();

        listDataHeader.add("How to add new Organisation ?");
        listDataHeader.add("How to add new Project ?");
        listDataHeader.add("If you added your projects and not able to see that...");
        listDataHeader.add("How to check the status of your work");
        listDataHeader.add("About Finance Section");
        listDataHeader.add("How to add events ");

        List<String> edmtDev = new ArrayList<>();
        edmtDev.add("To add new Organisation you can go to the organisation section then 'add organisation' section will open and there you can add your organisation.");

        List<String> androidStudio = new ArrayList<>();
        androidStudio.add("To add new project go to the project section or click on all projects then 'add project' section will appear on the screen and you can add your projects");


        List<String> xamarin = new ArrayList<>();
        xamarin.add("if you have added your projects and are not able to see that then just scroll up and check the organisation section");


        List<String> uwp = new ArrayList<>();
        uwp.add("to check d status of your work go to the work section and there you will get all the progress report of your work");

        List<String> uwp1 = new ArrayList<>();
        uwp1.add("In finance section we have added many basic features for users like : \n" +
                "• basic calculator\n" +
                "• scientific calculator\n" +
                "• currency converter\n" +
                "• unit converter\n" +
                "• calendar");

        List<String> uwp2 = new ArrayList<>();
        uwp2.add("To add events first go to 'organisation calendar section' . Click on the floating button and then event section will appear there.");


        listHash.put(listDataHeader.get(0),edmtDev);
        listHash.put(listDataHeader.get(1),androidStudio);
        listHash.put(listDataHeader.get(2),xamarin);
        listHash.put(listDataHeader.get(3),uwp);
        listHash.put(listDataHeader.get(4),uwp1);
        listHash.put(listDataHeader.get(5),uwp2);
    }

}