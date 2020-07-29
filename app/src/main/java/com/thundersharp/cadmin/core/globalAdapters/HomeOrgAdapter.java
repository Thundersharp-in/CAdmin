package com.thundersharp.cadmin.core.globalAdapters;

import android.content.Context;
import android.content.SharedPreferences;
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
        SharedPreferences preferences=context.getSharedPreferences("selected_org",Context.MODE_PRIVATE);
        String checked=preferences.getString("org_id","null");
        if (checked.equals("null")){
            if (holder.radioselector.isChecked()){
                SharedPreferences preferences1=context.getSharedPreferences("selected_org",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences1.edit();
                editor.putString("org_id",holder.orgid.toString());
                editor.apply();
                holder.radioselector.setChecked(true);
            }else {
                holder.radioselector.setChecked(data.get(0).equals(true));
                SharedPreferences preferences1=context.getSharedPreferences("selected_org",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences1.edit();
                editor.putString("org_id",holder.orgid.toString());
                editor.apply();
            }
           // holder.radioselector.isChecked();
        }else if (checked.equals(holder.orgid.toString())){
            if (holder.orgid.equals(checked)){
                holder.radioselector.setChecked(true);
            }else {
                holder.radioselector.setChecked(false);
            }
            /*
            SharedPreferences preferences1=context.getSharedPreferences("selected_org",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=preferences1.edit();
            editor.putString("org_id",holder.orgid.toString());
            editor.apply();


             */
        }
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
