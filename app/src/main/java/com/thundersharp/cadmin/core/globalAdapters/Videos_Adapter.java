package com.thundersharp.cadmin.core.globalAdapters;


import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.thundersharp.cadmin.R;

import java.util.List;

public  class Videos_Adapter extends RecyclerView.Adapter<Videos_Adapter.ViewHolder>{
    Context context;
    List<String> videos_url;

    public Videos_Adapter(Context context, List<String> videos_url) {
        this.context = context;
        this.videos_url = videos_url;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.project_videos,parent,false); //item_org_images
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String url = videos_url.get(position);

    }

    @Override
    public int getItemCount() {
        if (videos_url != null){
            return videos_url.size();
        }else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {

        ImageView project_videos;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            project_videos = itemView.findViewById(R.id.project_videos);//  item_org_image


        }

    }
}

