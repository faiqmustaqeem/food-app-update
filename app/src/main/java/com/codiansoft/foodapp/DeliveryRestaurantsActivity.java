package com.codiansoft.foodapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.codiansoft.foodapp.adapter.RestaurantsRVAdapter;
import com.codiansoft.foodapp.model.RestaurantsModel;

import java.util.ArrayList;
import java.util.List;

public class DeliveryRestaurantsActivity extends AppCompatActivity {

    RecyclerView rvDeliveryRestaurants;
    List<RestaurantsModel> deliveryRestaurantslist = new ArrayList<>();
    private RestaurantsRVAdapter adapter;
    RestaurantsModel restaurantsModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_restaurants);
        getSupportActionBar().setTitle("Delivery Restaurants");
        rvDeliveryRestaurants = (RecyclerView) findViewById(R.id.rvDeliveryRestaurants);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        };

        rvDeliveryRestaurants.setLayoutManager(layoutManager);

        initRestaurantsList();

    }

    private void initRestaurantsList() {
        deliveryRestaurantslist = new ArrayList<>();
        restaurantsModel = new RestaurantsModel("", "", "", "", "","");
        deliveryRestaurantslist.add(restaurantsModel);

        restaurantsModel = new RestaurantsModel("1", "Nando's", "http://franchiseexpert.in/wp-content/uploads/2014/11/Nandos.jpg", "20-30 MIN", "Fast Food","1");
        deliveryRestaurantslist.add(restaurantsModel);
        restaurantsModel = new RestaurantsModel("2", "Domino's", "http://www.mbjairport.com/content/312/dominos_f575x300.jpg", "10-15 MIN", "Fast Food","1");
        deliveryRestaurantslist.add(restaurantsModel);
        restaurantsModel = new RestaurantsModel("3", "Burger King", "https://www.welivesecurity.com/wp-content/uploads/2016/05/descuento-burger-king-623x432.jpg", "40-50 MIN", "Fast Food","1");
        deliveryRestaurantslist.add(restaurantsModel);
        restaurantsModel = new RestaurantsModel("4", "Pizza Hut", "http://members.wisdells.com/member-media/rest/PizzaHut.jpg", "20-30 MIN", "Fast Food","1");
        deliveryRestaurantslist.add(restaurantsModel);
        restaurantsModel = new RestaurantsModel("5", "Sub Way", "https://onefatfrog23.files.wordpress.com/2013/08/subway01_full.jpg", "30-40 MIN", "Fast Food","1");
        deliveryRestaurantslist.add(restaurantsModel);

        adapter = new RestaurantsRVAdapter(this, deliveryRestaurantslist);
        rvDeliveryRestaurants.setAdapter(adapter);
    }
}
