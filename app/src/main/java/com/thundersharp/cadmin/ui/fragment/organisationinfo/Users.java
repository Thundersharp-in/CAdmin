package com.thundersharp.cadmin.ui.fragment.organisationinfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.core.globalmodels.UserData;
import com.thundersharp.cadmin.ui.fragment.OrginasationDetails;

import java.util.ArrayList;
import java.util.List;

public class Users extends Fragment {

    RecyclerView rv_users;
    Button btn_add_user_to_org;
    List<UserData> users;
    String org_id;
    public Users() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View root=inflater.inflate(R.layout.fragment_users, container, false);

        org_id = OrginasationDetails.org_id;
        rv_users=root.findViewById(R.id.users_added);
        btn_add_user_to_org=root.findViewById(R.id.btn_add_user_to_org);
        users=new ArrayList<>();



        return root;
    }
}