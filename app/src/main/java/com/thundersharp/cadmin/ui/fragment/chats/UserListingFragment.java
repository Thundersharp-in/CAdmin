package com.thundersharp.cadmin.ui.fragment.chats;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.core.chats.adapters.UserListingPagerAdapter;
import com.thundersharp.cadmin.ui.activity.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */

public class UserListingFragment extends Fragment {
    private Toolbar mToolbar;
    private TabLayout mTabLayoutUserListing;
    private ViewPager mViewPagerUserListing;
    View view;
    // private LogoutPresenter mLogoutPresenter;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, UserListingFragment.class);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, int flags) {
        Intent intent = new Intent(context, UserListingFragment.class);
        intent.setFlags(flags);
        context.startActivity(intent);
    }

    public UserListingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_listing, container, false);
        MainActivity.floatingActionButton.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_baseline_open_in_new_24));
        MainActivity.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"New chat",Toast.LENGTH_SHORT).show();
            }
        });
        bindViews();
        init();

        return view;
    }

    private void bindViews() {
        mToolbar = view.findViewById(R.id.toolbar);
        mTabLayoutUserListing = view.findViewById(R.id.tab_layout_user_listing);
        mViewPagerUserListing = view.findViewById(R.id.view_pager_user_listing);
    }

    private void init() {
        // set the toolbar

        // set the view pager adapter
        UserListingPagerAdapter userListingPagerAdapter = new UserListingPagerAdapter(getFragmentManager());
        mViewPagerUserListing.setAdapter(userListingPagerAdapter);

        // attach tab layout with view pager
        mTabLayoutUserListing.setupWithViewPager(mViewPagerUserListing);

        // mLogoutPresenter = new LogoutPresenter(this);
    }
}
