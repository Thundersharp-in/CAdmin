package com.thundersharp.cadmin.core.globalAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thundersharp.cadmin.R;

import java.util.List;

public class ManagerModelsAdapter extends RecyclerView.Adapter<ManagerModelsAdapter.ViewHolders> {

    Context context;
    List<String> manager_name;

    public ManagerModelsAdapter(Context context, List<String> manager_name) {
        this.context = context;
        this.manager_name = manager_name;
    }

    @NonNull
    @Override
    public ViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.manager_list,parent,false);
        return new ViewHolders(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolders holder, int position) {
        String names = manager_name.get(position);
        holder.name.setText(names);
    }

    @Override
    public int getItemCount() {
        return manager_name.size();
    }

    public class ViewHolders extends RecyclerView.ViewHolder{
        TextView name;
        public ViewHolders(@NonNull View itemView) {
            super(itemView);
            name= itemView.findViewById(R.id.manager_name);
        }
    }
}
