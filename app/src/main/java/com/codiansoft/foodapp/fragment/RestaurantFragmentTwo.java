package com.codiansoft.foodapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.codiansoft.foodapp.FoodActivity;
import com.codiansoft.foodapp.R;
import com.codiansoft.foodapp.RecyclerItemClickListener;
import com.codiansoft.foodapp.adapter.ResFragTwoAdapter;
import com.codiansoft.foodapp.model.FragmentTwoDataModel;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import java.util.ArrayList;

/**
 * Created by Codiansoft on 8/17/2017.
 */

public class RestaurantFragmentTwo extends Fragment implements ObservableScrollViewCallbacks {

    ObservableRecyclerView recycler_view2;
    public static ProgressBar progressBar;
    public static ArrayList<FragmentTwoDataModel> fragTwoItems;
    public static ResFragTwoAdapter fragTwoAdapter;

    public static RecyclerView.LayoutManager fragTwoLayoutManager;
    FloatingActionButton fabScrollNavigation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.restaurant_fragment_two, container, false);
        recycler_view2 = (ObservableRecyclerView) rootView.findViewById(R.id.recycler_view2);
        fabScrollNavigation = (FloatingActionButton) rootView.findViewById(R.id.fabScrollNavigation);

        getItems();
        fragTwoLayoutManager = new LinearLayoutManager(getActivity());
        recycler_view2.setLayoutManager(fragTwoLayoutManager);
        recycler_view2.setItemAnimator(new DefaultItemAnimator());
        recycler_view2.setAdapter(fragTwoAdapter);
        recycler_view2.setScrollViewCallbacks((ObservableScrollViewCallbacks) getActivity());

        recycler_view2.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent i = new Intent(getActivity(), FoodActivity.class);
                i.putExtra("foodID", fragTwoItems.get(position).getID());
                i.putExtra("foodTitle", fragTwoItems.get(position).getTitle());
                i.putExtra("foodDescription", fragTwoItems.get(position).getDescription());
                i.putExtra("foodPrice", fragTwoItems.get(position).getPrice());
                i.putExtra("foodImage", fragTwoItems.get(position).getImageURL());
                startActivity(i);
            }
        }));

        fabScrollNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(getActivity(), fabScrollNavigation);

//                popup.getMenu().add(groupId, itemId, order, title);

                for (int i = 0; i < fragTwoItems.size(); i++) {
                    if (!fragTwoItems.get(i).isRow()) {
                        popup.getMenu().add(1, i, i, fragTwoItems.get(i).getSectionTitle());
                    }
                }

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        for (int i = 0; i < fragTwoItems.size(); i++) {
                            if (item.getTitle().equals(fragTwoItems.get(i).getSectionTitle())) {
                                fragTwoLayoutManager.scrollToPosition(i);
                                break;
                            }
                        }

                        /*switch (item.getItemId()) {
                            case 1:
                                Toast.makeText(getActivity(), "first", Toast.LENGTH_SHORT).show();

                                break;

                            case 2:
                                Toast.makeText(getActivity(), "second", Toast.LENGTH_SHORT).show();
                                break;
                        }*/
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });

        return rootView;
    }

    private void getItems() {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
//        ViewHelper.setTranslationY(mImageView, scrollY / -2);

//        ViewHelper.setTranslationY(tabLayout, scrollY / -2);
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }
}