package com.thundersharp.cadmin.ui.fragment.projetinfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.core.globalAdapters.TabAdapter;

public class ProjectDetails extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.project_info, container, false);

        tabLayout = root.findViewById(R.id.sliding_tabs);
        viewPager = root.findViewById(R.id.viewpager);


       TabAdapter tabAdapter = new TabAdapter(getParentFragmentManager());
        tabAdapter.addFragment(new Photo(), null);
        tabAdapter.addFragment(new Files(), null);
        tabAdapter.addFragment(new Video(), null);

        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_outline_photo_library_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_outline_file_copy_24);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_outline_video_library_24);

        return root;
    }
}
