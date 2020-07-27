package com.thundersharp.cadmin.core.globalAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.core.globalmodels.AddProjectModel;

import java.util.List;

public class AddProjectViewAdapter extends RecyclerView.Adapter<AddProjectViewAdapter.ViewHolder> {

    private List<AddProjectModel> addProjectModels;
    private Context mContext;



    public AddProjectViewAdapter(List<AddProjectModel> addProjectModels, Context mContext) {
        this.addProjectModels = addProjectModels;
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

        final AddProjectModel project = addProjectModels.get(position);

        holder.projectName.setText(project.getProject_name());
        holder.projectDesc.setText(project.getDescribe());

    }

    @Override
    public int getItemCount() {
        return addProjectModels.size();
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
