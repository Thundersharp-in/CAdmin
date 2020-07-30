package com.thundersharp.cadmin.core.globalAdapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.core.globalmodels.AddProject_model;
import com.thundersharp.cadmin.ui.activity.MainActivity;

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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        AddProject_model project = data.get(position);

        holder.projectName.setText(project.getProjectName());
        holder.projectDesc.setText(project.getProjectDesc());
        holder.project_id.setText("Id: "+project.getProject_id());

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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            projectName = itemView.findViewById(R.id.projectView_name);
            projectDesc = itemView.findViewById(R.id.projectView_projectBio);
            project_id = itemView.findViewById(R.id.text_organiserUid);

            mCurrent = FirebaseAuth.getInstance().getCurrentUser();
            project_uid = mCurrent.getUid();
            reference1= FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid()).child("projects");
            reference2=FirebaseDatabase.getInstance().getReference().child("organisation");

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            Bundle bundle = new Bundle();
            bundle.putString("project_id",data.get(getAdapterPosition()).project_id);
            MainActivity.navController.navigate(R.id.nav_proj_info,bundle);

        }
    }
}
