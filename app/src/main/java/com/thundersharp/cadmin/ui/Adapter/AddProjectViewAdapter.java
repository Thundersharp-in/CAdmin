package com.thundersharp.cadmin.ui.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.core.chats.core.users.getall.GetUsersContract;
import com.thundersharp.cadmin.ui.Model.AddProject;

import java.util.ArrayList;
import java.util.List;

public class AddProjectViewAdapter extends RecyclerView.Adapter<AddProjectViewAdapter.ViewHolder> {

    private ArrayList<AddProject> addProjects;
    private Context mContext;



    public AddProjectViewAdapter(ArrayList<AddProject> addProjects, Context mContext) {
        this.addProjects = addProjects;
        this.mContext = mContext;
    }

    public AddProjectViewAdapter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.add_project_view,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final AddProject project = addProjects.get(position);

        holder.projectName.setText(project.getProjectName());
        holder.projectDesc.setText(project.getProjectDesc());

    }

    @Override
    public int getItemCount() {
        return addProjects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView projectName;
        TextView projectDesc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            projectName = itemView.findViewById(R.id.projectView_name);
            projectDesc = itemView.findViewById(R.id.projectView_projectBio);
        }
    }
}
