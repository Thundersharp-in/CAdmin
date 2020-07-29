package com.thundersharp.cadmin.core.globalAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.core.globalmodels.org_details_model;

import java.util.List;

public class HomeOrgAdapter extends RecyclerView.Adapter<HomeOrgAdapter.CustomViewHolder> {

    private Context context;
    private List<org_details_model> data;

    public HomeOrgAdapter(Context context, List<org_details_model> data ) {
        this.context = context;
        this.data = data;

    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_org,parent,false);


        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder holder, int position) {
        org_details_model model=data.get(position);
        Glide.with(context).load(model.getCompany_logo()).into(holder.orglogo);
        holder.org_name.setText(model.getOrganisation_name());
        holder.orgid.setText(model.getOrganisation_id());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView orglogo;
        TextView org_name,orgid;
        RadioButton radioselector;

        public CustomViewHolder(View itemView) {
            super(itemView);

            orglogo = itemView.findViewById(R.id.orglogo);
            org_name = itemView.findViewById(R.id.org_name);
            orgid = itemView.findViewById(R.id.orgid);
            radioselector = itemView.findViewById(R.id.radioselector);

        }

        @Override
        public void onClick(View v) {

        }
    }
}
