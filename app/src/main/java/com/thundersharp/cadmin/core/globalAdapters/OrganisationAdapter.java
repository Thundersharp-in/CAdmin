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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.core.globalmodels.Organisations;
import com.thundersharp.cadmin.core.globalmodels.org_details_model;
import com.thundersharp.cadmin.ui.activity.MainActivity;
import com.thundersharp.cadmin.ui.fragment.OrginasationDetails;

import java.util.List;

public class OrganisationAdapter extends RecyclerView.Adapter<OrganisationAdapter.CustomViewHolder> {

    private Context context;
    private List<org_details_model> data;
    private List<Organisations> organisations;
    private Boolean manager;

    public OrganisationAdapter(Context context, List<org_details_model> data , List<Organisations> organisations,Boolean manager) {
        this.context = context;
        this.data = data;
        this.organisations=organisations;
        this.manager=manager;
    }


    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_organisation,parent,false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder holder, final int position) {
        final org_details_model model=data.get(position);
       // Boolean val=manager;
        holder.ismanager=manager;

        Glide.with(context).load(model.getCompany_logo()).into(holder.org_logo);

        holder.org_name.setText(model.getOrganisation_name());
        holder.org_id.setText(model.getOrganisation_id());

        holder.manager.setText(model.getOrganiser_name());

       /*
        holder.reference1.child(model.getOrganisation_id())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            holder.ismanager=(Boolean) snapshot.getValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        */

        final SharedPreferences preferences=context.getSharedPreferences("selected_org",Context.MODE_PRIVATE);
        final SharedPreferences preferences1=context.getSharedPreferences("isManager",Context.MODE_PRIVATE);
        String checked=preferences.getString("selected","null");
        //final Boolean isManager=preferences1.getBoolean("manager",false);

        if (checked.equalsIgnoreCase("null")){
            if (position == 0){
                holder.radioButton.setChecked(true);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("selected",model.getOrganisation_id());
                editor.apply();
                SharedPreferences.Editor editor1 = preferences1.edit();
                editor1.putBoolean("selected",holder.ismanager);
                editor1.apply();

            }else {
                holder.radioButton.setChecked(false);
            }
        }else {

            if (checked.equalsIgnoreCase(model.getOrganisation_id())){
                holder.radioButton.setChecked(true);
            }else {
                holder.radioButton.setChecked(false);
            }
        }

        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("selected",model.getOrganisation_id());
                editor.apply();
                SharedPreferences.Editor editor1 = preferences1.edit();
                editor1.putBoolean("selected",holder.ismanager);
                editor1.apply();
                notifyDataSetChanged();
                notifyItemChanged(position);

            }
        });



    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView org_logo;
        TextView org_name,org_id,manager;
        DatabaseReference reference1,reference2;
        FirebaseUser mCurrent;
        String user_uid;
        RadioButton radioButton;
        Boolean ismanager=false;

        public CustomViewHolder(View itemView) {
            super(itemView);
            mCurrent= FirebaseAuth.getInstance().getCurrentUser();
            user_uid=mCurrent.getUid();
            org_logo=itemView.findViewById(R.id.org_logo);

            org_name=itemView.findViewById(R.id.org_name);
            org_id=itemView.findViewById(R.id.org_id);
            radioButton = itemView.findViewById(R.id.radioorg);

            manager=itemView.findViewById(R.id.manager);

            reference1= FirebaseDatabase.getInstance().getReference()
                    .child("users")
                    .child(FirebaseAuth.getInstance().getUid())
                    .child("organisations");
            reference2=FirebaseDatabase
                    .getInstance()
                    .getReference()
                    .child("organisation");
            itemView.setOnClickListener(this);
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
