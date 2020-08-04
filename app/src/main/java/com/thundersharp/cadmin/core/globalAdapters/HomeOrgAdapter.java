package com.thundersharp.cadmin.core.globalAdapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.thundersharp.cadmin.ui.activity.MainActivity;
import com.thundersharp.cadmin.ui.fragment.OrginasationDetails;

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
    public void onBindViewHolder(@NonNull final CustomViewHolder holder, final int position) {
        final org_details_model model=data.get(position);
        final SharedPreferences preferences=context.getSharedPreferences("selected_org",Context.MODE_PRIVATE);
        String checked=preferences.getString("selected","null");

        if (checked.equalsIgnoreCase("null")){
            if (position == 0){
                holder.radioselector.setChecked(true);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("selected",model.getOrganisation_id());
                editor.apply();
            }else {
                holder.radioselector.setChecked(false);
            }
        }else {

            if (checked.equalsIgnoreCase(model.getOrganisation_id())){
                holder.radioselector.setChecked(true);
            }else {
                holder.radioselector.setChecked(false);
            }
        }

        holder.radioselector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("selected",model.getOrganisation_id());
                editor.apply();
                notifyDataSetChanged();
                notifyItemChanged(position);
            }
        });

        Glide.with(context).load(model.getCompany_logo()).into(holder.orglogo);
        holder.org_name.setText(model.getOrganisation_name());
        holder.orgid.setText(model.getOrganisation_id());
    }
//check shared pref is null org name match if sav then checkrd else not checked
    //if not null get adapterposition =0
    @Override
    public int getItemCount() {
        return data.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView orglogo;
        TextView org_name,orgid;
        RadioButton radioselector;
        SharedPreferences sharedPreferences;

        public CustomViewHolder(View itemView) {
            super(itemView);

            orglogo = itemView.findViewById(R.id.orglogo);
            org_name = itemView.findViewById(R.id.org_name);
            orgid = itemView.findViewById(R.id.orgid);
            radioselector = itemView.findViewById(R.id.radioselector);
            sharedPreferences = context.getSharedPreferences("selected_org",Context.MODE_PRIVATE);
        }

        @Override
        public void onClick(View v) {

            OrginasationDetails detail=new OrginasationDetails();
            Bundle bundle=new Bundle();
            org_details_model details=data.get(getAdapterPosition());

            bundle.putString("org_name",details.getOrganisation_name());
            bundle.putString("org_desc",details.getCompany_description());
            bundle.putString("org_id",details.getOrganisation_id());
            bundle.putString("org_image",details.getCompany_logo());
            bundle.putString("organiser_id",details.getOrganiser_uid());

            detail.setArguments(bundle);

            MainActivity.navController.navigate(R.id.nav_org_details,bundle);
        }
    }
}
