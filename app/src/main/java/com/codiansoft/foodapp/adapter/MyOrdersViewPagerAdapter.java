package com.codiansoft.foodapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codiansoft.foodapp.fragment.MyOrdersHistoryFragment;
import com.codiansoft.foodapp.fragment.MyOrdersUpcomingFragment;

import java.util.ArrayList;

/**
 * Created by Codiansoft on 9/12/2017.
 */

public class MyOrdersViewPagerAdapter extends FragmentPagerAdapter {
    ArrayList<String> tabPageTitles;

    public MyOrdersViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        if (position == 0) {
            fragment = new MyOrdersHistoryFragment();
        } else if (position == 1) {
            fragment = new MyOrdersUpcomingFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) return "HISTORY";
        else return "UPCOMING";
    }
}