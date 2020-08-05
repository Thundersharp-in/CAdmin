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
import com.thundersharp.cadmin.core.globalmodels.UserData;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder>{
    Context context;
    List<String> userDataList;

    public UsersAdapter(Context context, List<String> userDataList) {
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
        final String data = userDataList.get(position);
        FirebaseDatabase.getInstance()
                .getReference("users")
                .child(data)
                .child("personal_data")
                .child("name")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                  //  UserData userData=dataList.get(position);
                    holder.users_data.setText(snapshot.getValue().toString());
                }
               // holder.users_uid.setText(data.getUid());
                //holder.users_data.setText(data.getName() +"\n" + data.getUid()+"\n"+data.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
     holder.users_uid.setText(data);
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
