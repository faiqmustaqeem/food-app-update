package com.codiansoft.foodapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codiansoft.foodapp.FoodActivity;
import com.codiansoft.foodapp.R;
import com.codiansoft.foodapp.RecyclerItemClickListener;
import com.codiansoft.foodapp.adapter.MyOrdersRVAdapter;
import com.codiansoft.foodapp.adapter.MyOrdersViewPagerAdapter;
import com.codiansoft.foodapp.model.MyOrdersModel;

import java.util.ArrayList;

/**
 * Created by Codiansoft on 9/12/2017.
 */

public class MyOrdersUpcomingFragment extends Fragment {
    public static final String TAG = "MyOrdersHistoryFragment";
    View view;
    RecyclerView rvHistoryOrders;
    ArrayList<MyOrdersModel> historyOrdersList;
    private MyOrdersRVAdapter adapter;
    MyOrdersModel historyOrderModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
/*      EditText v = new EditText(getActivity());
        v.setText("Hello Fragment!");*/
        view = inflater.inflate(R.layout.fragment_my_orders_history, container, false);
        rvHistoryOrders = (RecyclerView) view.findViewById(R.id.rvHistoryOrders);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        };

        rvHistoryOrders.setLayoutManager(layoutManager);

        initHistoryOrders();

        rvHistoryOrders.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent foodIntent = new Intent(getActivity(), FoodActivity.class);
                foodIntent.putExtra("foodID", historyOrdersList.get(position).getDealID());
                foodIntent.putExtra("foodTitle", historyOrdersList.get(position).getTitle());
                foodIntent.putExtra("foodDescription", historyOrdersList.get(position).getDescription());
                foodIntent.putExtra("foodPrice", historyOrdersList.get(position).getPrice());
                foodIntent.putExtra("foodImage", historyOrdersList.get(position).getImageUrl());
                startActivity(foodIntent);
            }
        }));

        return view;
    }

    private void initHistoryOrders() {
        historyOrdersList = new ArrayList<>();
        historyOrderModel = new MyOrdersModel("3", "Large Sandwich", "20-30 MIN", "600", "Fast Food", "https://www.carrentals.co.uk/blog/wp-content/uploads/2012/08/Large-Sandwich.jpg", "All nice taste of a great sandwich in each bite", "upcoming");
        historyOrdersList.add(historyOrderModel);

        adapter = new MyOrdersRVAdapter(getActivity(), historyOrdersList);
        rvHistoryOrders.setAdapter(adapter);
    }
}