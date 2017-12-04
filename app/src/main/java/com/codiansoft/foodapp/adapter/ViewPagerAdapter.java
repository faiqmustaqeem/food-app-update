package com.codiansoft.foodapp.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codiansoft.foodapp.fragment.RestaurantFragmentOne;
import com.codiansoft.foodapp.fragment.RestaurantFragmentThree;
import com.codiansoft.foodapp.fragment.RestaurantFragmentTwo;

import java.util.ArrayList;

import static com.codiansoft.foodapp.fragment.RestaurantFragmentOne.fragmentPosition;

/**
 * Created by Codiansoft on 8/16/2017.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    int tabsQty;
    ArrayList<String> tabPageTitles;

    public ViewPagerAdapter(FragmentManager fm, int tabsQty, ArrayList<String> tabPageTitles) {
        super(fm);
        this.tabsQty = tabsQty;
        this.tabPageTitles = tabPageTitles;
    }

    @Override
    public Fragment getItem(int position) {
        fragmentPosition = position;
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        RestaurantFragmentOne frag = new RestaurantFragmentOne();
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public int getCount() {
        return tabsQty;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabPageTitles.get(position);
    }
}