package com.thundersharp.cadmin.core.globalAdapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.core.globalmodels.org_details_model;
import com.thundersharp.cadmin.ui.activity.MainActivity;

import java.util.List;

public class OrganisationAdapter extends RecyclerView.Adapter<OrganisationAdapter.CustomViewHolder> {

    private Context context;
    private List<org_details_model> data;

    public OrganisationAdapter(Context context, List<org_details_model> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_organisation,parent,false);


        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder holder, int position) {
        org_details_model model=data.get(position);

        Glide.with(context).load(model.getCompany_logo()).into(holder.org_logo);

        holder.org_name.setText(model.getOrganisation_name());
        holder.org_id.setText(model.getOrganisation_id());

        holder.manager.setText(model.getOrganiser_name());

/*
                                final DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("projects");
                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        holder.total_projects.setText((int) dataSnapshot.getChildrenCount());
                                        for (DataSnapshot item:dataSnapshot.getChildren()){
                                            DatabaseReference reference2=FirebaseDatabase.getInstance().getReference(String.valueOf(item)).child("description");
                                            reference2.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    DatabaseReference reference1=FirebaseDatabase.getInstance().getReference().child("description") .child("project_name");
                                                    reference1.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            holder.project_name.setText(dataSnapshot.getValue().toString());
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                           DatabaseReference reference1=FirebaseDatabase.getInstance().getReference(String.valueOf(item)).child("users_uid");
                                           reference1.addValueEventListener(new ValueEventListener() {
                                               @Override
                                               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                   if (dataSnapshot.exists()){
                                                       Long users=dataSnapshot.getChildrenCount();
                                                       holder.no_of_users.setText(users.toString());
                                                   }
                                               }

                                               @Override
                                               public void onCancelled(@NonNull DatabaseError databaseError) {

                                               }
                                           });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                 */
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView org_logo,org_logo1;
        TextView org_name,org_id,org_id1,project_name,no_of_users,manager,total_projects;
        DatabaseReference reference1,reference2;
        FirebaseUser mCurrent;
        String user_uid;

        public CustomViewHolder(View itemView) {
            super(itemView);
            mCurrent= FirebaseAuth.getInstance().getCurrentUser();
            user_uid=mCurrent.getUid();
            org_logo=itemView.findViewById(R.id.org_logo);

            org_name=itemView.findViewById(R.id.org_name);
            org_id=itemView.findViewById(R.id.org_id);



            manager=itemView.findViewById(R.id.manager);

            reference1= FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid()).child("organisations");
            reference2=FirebaseDatabase.getInstance().getReference().child("organisation");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Bundle bundle=new Bundle();
            bundle.putString("org_id",org_id.toString());
            MainActivity.navController.navigate(R.id.nav_proj,bundle);
        }
    }
}
