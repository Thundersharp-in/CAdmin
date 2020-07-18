package com.thundersharp.cadmin.chats.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.thundersharp.cadmin.ui.fragment.chats.UserFragment;


public class chatListingPagerAdapter extends FragmentPagerAdapter {
    private static final Fragment[] sFragmentschat = new Fragment[]{
            UserFragment.newInstance(UserFragment.TYPE_CHATS)
    };
    private static final String[] sTitleschat = new String[]{"Chats"};

    public chatListingPagerAdapter(FragmentManager fm) {
            super(fm);
        }


    @Override
        public Fragment getItem(int position) {
            return sFragmentschat[position];
        }
        @Override
        public int getCount() {
            return sFragmentschat.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return sTitleschat[position];
        }
}