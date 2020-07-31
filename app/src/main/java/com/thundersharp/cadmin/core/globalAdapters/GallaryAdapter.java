package com.thundersharp.cadmin.core.globalAdapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;
import com.thundersharp.cadmin.R;

import java.util.List;

public class GallaryAdapter extends RecyclerView.Adapter<GallaryAdapter.ViewHolder>{

    Context context;
    List<String> imageuri;

    public GallaryAdapter(Context context , List<String> imageuri){
        this.context = context;
        this.imageuri = imageuri;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_image,parent,false);//grid_image item_org_images
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String url = imageuri.get(position);
        Glide.with(context).load(url).into(holder.src_icon);
    }

    @Override
    public int getItemCount() {
        if (imageuri != null){
            return imageuri.size();
        }else return 0;

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView src_icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            src_icon = itemView.findViewById(R.id.src_icon );//src_icon  item_org_image

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view1 = LayoutInflater.from(context).inflate(R.layout.full_screen_image_view,null);
            PhotoView photoView = view1.findViewById(R.id.photofullscreen);
            final ProgressBar progressBar = view1.findViewById(R.id.progressimade);
            progressBar.setVisibility(View.VISIBLE);

            Glide.with(context).load(imageuri.get(getAdapterPosition())).listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    progressBar.setVisibility(View.GONE);
                    return false;
                }
            }).into(photoView);

            builder.setCancelable(true);
            builder.setView(view1);
            builder.show();

        }
    }


}
