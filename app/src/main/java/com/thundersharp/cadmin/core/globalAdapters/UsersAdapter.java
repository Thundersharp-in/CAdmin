package com.thundersharp.cadmin.core.globalAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.core.globalmodels.ManagerModels;
import com.thundersharp.cadmin.core.globalmodels.UserData;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder>{
    Context context;
    List<ManagerModels> userDataList;

    public UsersAdapter(Context context, List<ManagerModels> userDataList) {
        this.context = context;
        this.userDataList = userDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_usersof_org,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final ManagerModels data = userDataList.get(position);
        holder.users_data.setText(data.getInfo());
        holder.users_uid.setText(data.getUseruid());
    }

    @Override
    public int getItemCount() {
        return userDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView users_uid,users_data;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            users_uid=itemView.findViewById(R.id.users_uid);
            users_data=itemView.findViewById(R.id.users_data);
        }
    }
}
