package com.thundersharp.cadmin.ui.fragment.organisationinfo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.core.globalAdapters.UsersAdapter;
import com.thundersharp.cadmin.core.globalmodels.UserData;

import java.util.ArrayList;
import java.util.List;

public class FindUsers extends Fragment {

    RecyclerView allUsers;
    MyAdapter adapter;
    List<UserData> userList;
    String org_id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.add_users, container, false);

        allUsers = root.findViewById(R.id.org_add_user);
        allUsers.setHasFixedSize(true);
        allUsers.setLayoutManager(new LinearLayoutManager(getContext()));

        userList = new ArrayList<>();

        //getAllUsers();
        return root;
    }

    private void getAllUsers() {

    }

}
