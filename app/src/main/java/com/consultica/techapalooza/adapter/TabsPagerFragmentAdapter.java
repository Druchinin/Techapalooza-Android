package com.consultica.techapalooza.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.consultica.techapalooza.fragment.NewsFeedFragment;

/**
 * Created by user on 28.09.2015.
 */
public class TabsPagerFragmentAdapter extends FragmentPagerAdapter {

    private String[] tabs;

    public TabsPagerFragmentAdapter(FragmentManager fm) {
        super(fm);
        tabs = new String[] {
                "News Feed",
                "Schedule",
                "My Ticket",
                "Bands",
                "Venue"
        };
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return NewsFeedFragment.getInstance();
            case 1:
                return NewsFeedFragment.getInstance();
            case 2:
                return NewsFeedFragment.getInstance();
            case 3:
                return NewsFeedFragment.getInstance();
            case 4:
                return NewsFeedFragment.getInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabs.length;
    }
}
