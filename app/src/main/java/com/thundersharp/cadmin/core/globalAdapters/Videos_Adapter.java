package com.thundersharp.cadmin.core.globalAdapters;


import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.thundersharp.cadmin.R;

import java.util.List;

public  class Videos_Adapter extends RecyclerView.Adapter<Videos_Adapter.ViewHolder>{
    Context context;
    List<String> videos_url;
    String url;

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
        url = videos_url.get(position);

    }

    @Override
    public int getItemCount() {
        if (videos_url != null){
            return videos_url.size();
        }else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView project_videos;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            project_videos = itemView.findViewById(R.id.project_videos);//  item_org_image

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view1 = LayoutInflater.from(context).inflate(R.layout.full_screen_video_view,null);
            VideoView videoView = view1.findViewById(R.id.full_screen_video);
            final ProgressBar progressBar = view1.findViewById(R.id.progressvideo);
            progressBar.setVisibility(View.VISIBLE);

            MediaController mediaController=new MediaController(context);
            videoView.setVideoPath(videos_url.get(getAdapterPosition()));
            mediaController.setMediaPlayer(videoView);
            videoView.setMediaController(mediaController);
            videoView.requestFocus();
            videoView.start();
            progressBar.setVisibility(View.GONE);

            builder.setCancelable(true);
            builder.setView(view1);
            builder.show();
        }
    }
}

