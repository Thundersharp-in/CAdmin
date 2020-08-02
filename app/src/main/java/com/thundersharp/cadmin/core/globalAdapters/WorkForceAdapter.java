package com.thundersharp.cadmin.core.globalAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.core.globalmodels.AddProject_model;
import com.thundersharp.cadmin.core.globalmodels.WorkforceModel;
import com.thundersharp.cadmin.ui.fragment.WorkForce;

import java.util.List;

import thundersharp.timeline.TimelineView;

public class WorkForceAdapter extends RecyclerView.Adapter<WorkForceAdapter.CustomViewHolder> {

    Context context;
    List<WorkforceModel> model;
    AddProject_model description;

    public WorkForceAdapter(Context context,List<WorkforceModel> data,AddProject_model projectDesc ){
        this.context = context;
        this.model=data;
        this.description = projectDesc;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_work_force,null);
        return new CustomViewHolder(view,viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        WorkforceModel workforceModel = model.get(position);
        holder.name_proj.setText(description.getProjectName());
        holder.id_proj.setText(workforceModel.getProject_Key());
        holder.desc_proj.setText(description.getProjectDesc());
        holder.starttime.setText(workforceModel.getStarttime());
        holder.end_time.setText(workforceModel.getEndtime());
        Glide.with(context).load(R.drawable.avatar1).into(holder.iv);
    }

    @Override
    public int getItemCount() {
        if (model !=null)
            return model.size();
        else
            return 0;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView iv;
        TextView starttime,name_proj,id_proj,desc_proj,end_time;
        Button evpand;
        TimelineView timelineView;

        public CustomViewHolder(@NonNull View itemView ,int viewtype) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv);
            //evpand = itemView.findViewById(R.id.recyclertodo);
            //evpand.setOnClickListener(this);
            starttime = itemView.findViewById(R.id.starttime);
            end_time = itemView.findViewById(R.id.end_time);
            desc_proj = itemView.findViewById(R.id.desc_proj);
            id_proj = itemView.findViewById(R.id.id_proj);
            name_proj = itemView.findViewById(R.id.name_proj);
            timelineView = itemView.findViewById(R.id.timeline);
            timelineView.initLine(viewtype);

        }

        @Override
        public void onClick(View view) {

        }
    }
}
