package com.codiansoft.foodapp.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.codiansoft.foodapp.R;
import com.codiansoft.foodapp.adapter.MyOrdersViewPagerAdapter;
import com.codiansoft.foodapp.adapter.ViewPagerAdapter;

/**
 * Created by Codiansoft on 9/12/2017.
 */

public class MyOrdersFragment extends Fragment {
    public static final String TAG = "MyOrdersFragment";
    View view;
    TabLayout tabLayout;
    ViewPager viewPager;
    MyOrdersViewPagerAdapter viewPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
/*        EditText v = new EditText(getActivity());
        v.setText("Hello Fragment!");*/
        view = inflater.inflate(R.layout.fragment_my_orders, container, false);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPagerAdapter = new MyOrdersViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.clearFocus();
        tabLayout.setupWithViewPager(viewPager);


        return view;
    }
}