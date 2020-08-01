package com.thundersharp.cadmin.core.globalAdapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.core.globalmodels.AddProject_model;
import com.thundersharp.cadmin.core.globalmodels.org_details_model;
import com.thundersharp.cadmin.ui.activity.MainActivity;
import com.thundersharp.cadmin.ui.fragment.projetinfo.ProjectDetails;

import java.util.List;

public class AddProjectViewAdapter extends RecyclerView.Adapter<AddProjectViewAdapter.ViewHolder> {

    private Context mContext;
    private List<AddProject_model> data;

    public AddProjectViewAdapter(Context mContext, List<AddProject_model> data) {
        this.data = data;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.add_project_view,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        AddProject_model project = data.get(position);

        holder.projectName.setText(project.getProjectName());
        holder.projectDesc.setText(project.getProjectDesc());
        holder.project_id.setText("Id: "+project.getProject_id());
        final SharedPreferences preferences=mContext.getSharedPreferences("last_visited_proj",Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("proj_name",project.getProjectName());
        editor.putString("proj_id",project.getProject_id());
        editor.putString("org_id",project.getOrganisation_id());
        editor.putString("proj_desc",project.getProjectDesc());
        editor.apply();

        holder.reference2.child(project.getOrganisation_id()).child("description").child("company_logo").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    holder.org_image=snapshot.getValue().toString();
                    Glide.with(mContext).load(holder.org_image).into(holder.org_image1);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView projectName;
        TextView projectDesc;
        TextView project_id;
        DatabaseReference reference1,reference2;
        FirebaseUser mCurrent;
        String project_uid;
        ImageView org_image1;
        String org_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            projectName = itemView.findViewById(R.id.projectView_name);
            projectDesc = itemView.findViewById(R.id.projectView_projectBio);
            project_id = itemView.findViewById(R.id.text_organiserUid);
            org_image1=itemView.findViewById(R.id.org_image1);
            mCurrent = FirebaseAuth.getInstance().getCurrentUser();
            project_uid = mCurrent.getUid();
            reference1= FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid()).child("projects");
            reference2=FirebaseDatabase.getInstance().getReference().child("organisation");

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            ProjectDetails details=new ProjectDetails();
            Bundle bundle = new Bundle();
            AddProject_model detail=data.get(getAdapterPosition());

            bundle.putString("proj_name",detail.getProjectName());
            bundle.putString("proj_desc",detail.getProjectDesc());
            bundle.putString("proj_id",detail.getProject_id());
            bundle.putString("org_id",detail.getOrganisation_id());
            bundle.putString("org_image",org_image);

            details.setArguments(bundle);
            MainActivity.navController.navigate(R.id.nav_proj_info,bundle);

        }
    }
}
