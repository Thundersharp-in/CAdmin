package com.thundersharp.cadmin.core.globalAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.thundersharp.cadmin.R;

import java.util.List;

public class OrgImageAdapter  extends RecyclerView.Adapter<OrgImageAdapter.ViewHolder>{
    /**
     *
     * extends Adapter
     *
     *  private List<File> images;
     *     private Context context;
     *
     *     public OrgImageAdapter(List<File> images, Context context) {
     *         this.images = images;
     *         this.context = context;
     *     }
     *
     *     @NonNull
     *     @Override
     *     public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
     *         LayoutInflater inflater = LayoutInflater.from(context);
     *         View view = inflater.inflate(R.layout.grid_image,parent,false);
     *         return new ViewHolder(view);
     *     }
     *
     *     @Override
     *     public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
     *
     *     }
     *
     *     @Override
     *     public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
     *         super.onViewRecycled(holder);
     *     }
     *
     *     @Override
     *     public int getItemCount() {
     *         return 0;
     *     }
     *
     *     public class ViewHolder extends RecyclerView.ViewHolder{
     *         public ImageView org_images;
     *         public ViewHolder(@NonNull View itemView){
     *             super(itemView);
     *             org_images=(ImageView) itemView.findViewById(R.id.iv_orgPhoto);
     *         }
     *     }
     */

    Context context;
    List<ImageView> MainImageUploadInfoList;

    public OrgImageAdapter(Context context, List<ImageView> mainImageUploadInfoList) {
        this.context = context;
        MainImageUploadInfoList = mainImageUploadInfoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_image, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context).load(MainImageUploadInfoList.get(position)).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return MainImageUploadInfoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView =itemView.findViewById(R.id.imageView);

        }
    }

}
