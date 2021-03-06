package com.codiansoft.foodapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.codiansoft.foodapp.adapter.ResFragOneAdapter;
import com.codiansoft.foodapp.adapter.ResFragTwoAdapter;
import com.codiansoft.foodapp.adapter.ViewPagerAdapter;
import com.codiansoft.foodapp.model.FragmentOneDataModel;
import com.codiansoft.foodapp.model.FragmentTwoDataModel;
import com.codiansoft.foodapp.model.TabTimesModel;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.google.common.collect.Lists;
import com.nineoldandroids.view.ViewHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.codiansoft.foodapp.fragment.RestaurantFragmentOne.fragOneAdapter;
import static com.codiansoft.foodapp.fragment.RestaurantFragmentOne.fragOneItems;
import static com.codiansoft.foodapp.fragment.RestaurantFragmentOne.fragOneLayoutManager;
import static com.codiansoft.foodapp.fragment.RestaurantFragmentOne.position;
import static com.codiansoft.foodapp.fragment.RestaurantFragmentOne.recycler_view1;
import static com.codiansoft.foodapp.fragment.RestaurantFragmentTwo.fragTwoAdapter;
import static com.codiansoft.foodapp.fragment.RestaurantFragmentTwo.fragTwoItems;

public class RestaurantActivity extends AppCompatActivity implements ObservableScrollViewCallbacks, View.OnClickListener {
    public static List<List<FragmentOneDataModel>> allMenu;
    public static ArrayList<TabTimesModel> tabTimes = new ArrayList<TabTimesModel>();
    ObservableListView listView;
    NestedScrollView scrollView;
    public static ImageView mImageView;
    public static CardView cvRestaurantDetails;
    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    FloatingActionButton fabScrollNavigation;
    TextView tvViewCart, tvRestaurantName, tvRestaurantDescription, tvBasketItemsQuantity;
    LinearLayout llTopBar;
    ImageView ivShare, ivBack, ivTopBarBack, ivTopBarShare;
    ArrayList<String> tabPageTitles = new ArrayList<>();
    ArrayList<String> tabSubCategory = new ArrayList<>();
    int tabsQty = 3;
    public static TextView tvFromTimeMain, tvToTimeMain;

    String restaurantID, restaurantTitle, restaurantDescription, restaurantDuration, restaurantImage, branch, lat, lng;

    SwipeRefreshLayout swipe_refresh_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_restaurant);
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

//        allMenu.clear();

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();

            if (extras == null) {
                restaurantID = null;
                restaurantTitle = null;
                restaurantDuration = null;
                restaurantImage = null;
                restaurantDescription = null;
            } else {
                restaurantID = extras.getString("restaurantID");
                restaurantTitle = extras.getString("restaurantTitle");
                restaurantDuration = extras.getString("restaurantDuration");
                restaurantImage = extras.getString("restaurantImage");
                restaurantDescription = extras.getString("restaurantDescription");
            }
        } else {
            restaurantID = (String) savedInstanceState.getSerializable("restaurantID");
            restaurantTitle = (String) savedInstanceState.getSerializable("restaurantTitle");
            restaurantDuration = (String) savedInstanceState.getSerializable("restaurantDuration");
            restaurantImage = (String) savedInstanceState.getSerializable("restaurantImage");
            restaurantDescription = (String) savedInstanceState.getSerializable("restaurantDescription");
        }

        getSupportActionBar().hide();
        initUI();
        viewPager = (ViewPager) findViewById(R.id.viewPager);

/*        tabLayout.addTab(tabLayout.newTab().setText("BREAKFAST").setIcon(android.R.drawable.arrow_up_float));
        tabLayout.addTab(tabLayout.newTab().setText("MENU").setIcon(android.R.drawable.btn_minus));
        tabLayout.addTab(tabLayout.newTab().setText("LUNCH AND DINNER").setIcon(android.R.drawable.arrow_down_float));*/


        fetchRestaurantMenu();


        /*tabPageTitles.add("Breakfast");
        tabPageTitles.add("Lunch and Diner");
        tabPageTitles.add("Menu");
        tabPageTitles.add("Lunch and Diner");*/


        ArrayList<String> items = new ArrayList<String>();
        for (int i = 1; i <= 100; i++) {
            items.add("Item " + i);
        }
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items));


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvFromTimeMain.setText(tabTimes.get(position).getFromTime());
                tvToTimeMain.setText(tabTimes.get(position).getToTime());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                viewPager.getParent().requestDisallowInterceptTouchEvent(false);
            }
        });


        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                viewPager.getParent().requestDisallowInterceptTouchEvent(true);
            }
        });


//        getFragOneItems();
//        getFragTwoItems();

        fabScrollNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(RestaurantActivity.this, fabScrollNavigation);

//                popup.getMenu().add(groupId, itemId, order, title);

                /*if (viewPager.getCurrentItem() == 0) {
                    for (int i = 0; i < fragOneItems.size(); i++) {
                        if (!fragOneItems.get(i).isRow()) {
                            popup.getMenu().add(1, i, i, fragOneItems.get(i).getSectionTitle());
                        }
                    }
                } else if (viewPager.getCurrentItem() == 1) {
                    for (int i = 0; i < fragTwoItems.size(); i++) {
                        if (!fragTwoItems.get(i).isRow()) {
                            popup.getMenu().add(1, i, i, fragTwoItems.get(i).getSectionTitle());
                        }
                    }
                }*/
                for (int i = 0; i < allMenu.get(viewPager.getCurrentItem()).size(); i++) {
                    if (!allMenu.get(viewPager.getCurrentItem()).get(i).isRow()) {
                        popup.getMenu().add(1, i, i, allMenu.get(viewPager.getCurrentItem()).get(i).getSectionTitle());
                    }
                }

//                Toast.makeText(RestaurantActivity.this, "" + viewPager.getCurrentItem(), Toast.LENGTH_SHORT).show();

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {


                        for (int j = 0; j < allMenu.get(position).size(); j++) {
                            if (!allMenu.get(position).get(j).isRow()) {
                                Log.e("check0",item.getTitle().toString().toLowerCase());
                                Log.e("check1",allMenu.get(position).get(j).getSectionTitle().toLowerCase());

                                if (item.getTitle().toString().toLowerCase().equals(allMenu.get(position).get(j).getSectionTitle().toLowerCase())) {
                                       Log.e("check2",j+"");
                                        //fragOneLayoutManager.scrollToPosition(j);
                                    //fragOneLayoutManager.scrollToPosition(j);
                                 //   fragOneLayoutManager.scrollToPositionWithOffset(j,2);
                                    //recycler_view1.scrollVerticallyToPosition(j);
                                      //  recycler_view1.smoothScrollToPosition(j);
                                          //  recycler_view1.scrollToPosition(j);
//                                        float y = recycler_view1.getChildAt(i).getY();
//                                        scrollView.smoothScrollTo(0, (int) y);

                                    /*final int finalJ = j;
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            recycler_view1.smoothScrollToPosition(finalJ);
                                            fragOneLayoutManager.scrollToPosition(finalJ);
                                            recycler_view1.scrollVerticallyToPosition(finalJ);
                                        }
                                    }, 200);*/

                                    break;
                                }
                            }
                        }











                        /*switch (item.getItemId()) {
                            case 1:
                                if (viewPager.getCurrentItem() == 0) {
                                    Toast.makeText(RestaurantActivity.this, "first", Toast.LENGTH_SHORT).show();
                                    for (int i = 0; i < fragOneItems.size(); i++) {
                                        if (item.getTitle().equals(fragOneItems.get(i).getSectionTitle())) {
                                            fragOneLayoutManager.scrollToPosition(i);
                                            break;
                                        }
                                    }
                                } else if (viewPager.getCurrentItem() == 1) {
                                    for (int i = 0; i < fragTwoItems.size(); i++) {
                                        if (item.getTitle().equals(fragTwoItems.get(i).getSectionTitle())) {
                                            fragTwoLayoutManager.scrollToPosition(i);
                                            break;
                                        }
                                    }
                                }
                                break;

                            case 2:
                                if (viewPager.getCurrentItem() == 0) {
                                    for (int i = 0; i < fragOneItems.size(); i++) {
                                        if (item.getTitle().equals(fragOneItems.get(i).getSectionTitle())) {
                                            fragOneLayoutManager.scrollToPosition(i);
                                            break;
                                        }
                                    }
                                } else if (viewPager.getCurrentItem() == 1) {
                                    for (int i = 0; i < fragTwoItems.size(); i++) {
                                        if (item.getTitle().equals(fragTwoItems.get(i).getSectionTitle())) {
                                            fragTwoLayoutManager.scrollToPosition(i);
                                            break;
                                        }
                                    }
                                }
                                Toast.makeText(RestaurantActivity.this, "second", Toast.LENGTH_SHORT).show();
                                break;
                        }*/
                        return true;
                    }
                });
                popup.show();//showing popup menu
            }
        });

        tvViewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RestaurantActivity.this, BasketActivity.class);
                String restaurantName = tvRestaurantName.getText().toString();
                String restaurantDescription = tvRestaurantDescription.getText().toString();
                i.putExtra("basketRestaurantName", restaurantName);
                i.putExtra("basketRestaurantDescription", restaurantDescription);
                startActivity(i);
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ivTopBarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private void fetchRestaurantMenu() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.RESTAURANT_MENU_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONObject Jobject = new JSONObject(response);
                            JSONObject result = Jobject.getJSONObject("result");
                            if (result.getString("status").equals("success")) {
                                branch = result.getJSONObject("restaurant_details").getString("branch");
                                lat = result.getJSONObject("restaurant_details").getString("lat");
                                lng = result.getJSONObject("restaurant_details").getString("lng");

                                tabsQty = result.getJSONObject("restaurant_details").getJSONArray("category_names").length();

                                for (int a = 0; a < tabsQty; a++) {

                                    tabPageTitles.add(result.getJSONObject("restaurant_details").getJSONArray("category_names").getJSONObject(a).getString("app_category"));
                                }


                                JSONArray menuCategories = result.getJSONArray("categories");
                                allMenu = new ArrayList<List<FragmentOneDataModel>>(tabsQty);
                                if (menuCategories.length() > 0) {
                                    for (int i = 0; i < menuCategories.length(); i++) {                 // all tabs objects with items array

                                        //tab timings
                                        tabTimes.add(new TabTimesModel(result.getJSONObject("restaurant_details").getJSONArray("category_names").getJSONObject(i).getString("to_time"), result.getJSONObject("restaurant_details").getJSONArray("category_names").getJSONObject(i).getString("from_time")));


                                        tabSubCategory.clear();
                                        tabSubCategory.add(result.getJSONObject("restaurant_details").getJSONArray("category_names").getJSONObject(i).getString("sub_categories"));


//                                        for (int j = 0; j < result.getJSONObject("restaurant_details").getJSONArray("category_names").getJSONObject(i).getJSONArray("sub_categories").length(); j++) {
//                                            tabSubCategory.add(result.getJSONObject("restaurant_details").getJSONArray("category_names").getJSONObject(i).getJSONArray("sub_categories").getString(j));
//                                        }

                                        fragOneItems = new ArrayList<FragmentOneDataModel>();
                                        fragOneItems.add(new FragmentOneDataModel(tabSubCategory.get(0), true));

                                        JSONObject menuTab = menuCategories.getJSONObject(i);
                                        List<String> tabsList = Lists.newArrayList(menuTab.keys());
                                        JSONArray tabItems = menuTab.getJSONArray(tabPageTitles.get(i));

                                        for (int j = 0; j < tabItems.length(); j++) {                   //single tab items array
//                                            tabItems.getJSONObject(i).getString("id");
                                            if (tabSubCategory.contains(tabItems.getJSONObject(j).getString("sub_category"))) {
                                                fragOneItems.add(new FragmentOneDataModel(tabItems.getJSONObject(j).getString("id"), tabItems.getJSONObject(j).getString("item"), tabItems.getJSONObject(j).getString("desc"), tabItems.getJSONObject(j).getString("price"), tabItems.getJSONObject(j).getString("picture"), tabItems.getJSONObject(j).getString("restaurant_id"), tabItems.getJSONObject(j).getString("branch_id"), tabItems.getJSONObject(j).getString("variation")));
                                            } else {
                                                fragOneItems.add(new FragmentOneDataModel(tabItems.getJSONObject(j).getString("sub_category"), true));
                                                fragOneItems.add(new FragmentOneDataModel(tabItems.getJSONObject(j).getString("id"), tabItems.getJSONObject(j).getString("item"), tabItems.getJSONObject(j).getString("desc"), tabItems.getJSONObject(j).getString("price"), tabItems.getJSONObject(j).getString("picture"), tabItems.getJSONObject(j).getString("restaurant_id"), tabItems.getJSONObject(j).getString("branch_id"), tabItems.getJSONObject(j).getString("variation")));
                                            }

//                                            fragOneAdapter = new ResFragOneAdapter(RestaurantActivity.this, fragOneItems);
                                        }
                                        allMenu.add(fragOneItems);
                                    }
                                }

                                viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), tabsQty, tabPageTitles);
                                viewPager.setAdapter(viewPagerAdapter);
                                viewPager.setOffscreenPageLimit(2);
                                viewPager.clearFocus();
                                tabLayout.setupWithViewPager(viewPager);
                                tvFromTimeMain.setText(tabTimes.get(0).getFromTime());
                                tvToTimeMain.setText(tabTimes.get(0).getToTime());
                            }

                        } catch (Exception ee) {
                            Toast.makeText(RestaurantActivity.this, "" + ee.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(RestaurantActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof AuthFailureError) {

                            //TODO
                        } else if (error instanceof ServerError) {

                            //TODO
                        } else if (error instanceof NetworkError) {

                            //TODO
                        } else if (error instanceof ParseError) {

                            //TODO
                        }
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences settings = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
                String userID = settings.getString("userID", "defaultValue");
                String apiSecretKey = settings.getString("apiSecretKey", "defaultValue");
                GlobalClass.api_secret=apiSecretKey;
                params.put("api_secret", apiSecretKey);


                params.put("restaurant_id", GlobalClass.selectedRestaurantID);
//                params.put("restaurant_id", restaurantID);


                params.put("branch_id", GlobalClass.selectedRestaurantBranchID);

                Log.e("params" , params.toString());
                return params;
            }
        };
        queue.add(postRequest);
    }

//    private void getFragTwoItems() {
//        fragTwoItems = new ArrayList<FragmentTwoDataModel>();
//        fragTwoAdapter = new ResFragTwoAdapter(this, fragTwoItems);
//
//        FragmentTwoDataModel item = new FragmentTwoDataModel("Our Popular", true);
//        fragTwoItems.add(item);
//
//        item = new FragmentTwoDataModel("12", "Buttermilk Crispy Chicken Sandwich", "Comes with medium beverage and 1 side choice", "8.39", "http://img.minq.com/vb/d54aaa20//71606437fe4afcde58f46ff1c2ef82c209bdeaeb.jpg");
//        fragTwoItems.add(item);
//        item = new FragmentTwoDataModel("13", "Cookie Tote", "Comes with medium beverage and 1 side choice", "4.99", "http://static.sfdict.com/sizedimage/sizedimage?width=300&height=300&url=http%3A%2F%2Fstatic.sfdict.com%2Fassets%2F7102-4024811-269.jpg");
//        fragTwoItems.add(item);
//
//        item = new FragmentTwoDataModel("14", "Signature Crafted Crispy Chicken Meal", "Comes with medium beverage and 1 side choice", "8.99", "https://metrouk2.files.wordpress.com/2016/11/fast-food-1.gif");
//        fragTwoItems.add(item);
//
//        item = new FragmentTwoDataModel("15", "Buttermilk Crispy Chicken Sandwich", "Comes with medium beverage and 1 side choice", "8.39", "http://www.referenceforbusiness.com/photos/fast-food-v1-679.jpg");
//        fragTwoItems.add(item);
//
//        item = new FragmentTwoDataModel("16", "Cookie Tote", "Comes with medium beverage and 1 side choice", "4.99", "http://i.dailymail.co.uk/i/pix/2017/01/24/17/3C782A3700000578-4152656-image-a-22_1485280039808.jpg");
//        fragTwoItems.add(item);
//
//        item = new FragmentTwoDataModel("17", "Double Beef n Cheese Burger", "Comes with medium beverage and 1 side choice", "8.99", "https://s-media-cache-ak0.pinimg.com/736x/07/02/41/0702414e760d9cbe7b17d3552b4a8b58--indian-fast-food-indian-snacks.jpg");
//        fragTwoItems.add(item);
//
//        item = new FragmentTwoDataModel("Our Special Dishes", true);
//        fragTwoItems.add(item);
//
//        item = new FragmentTwoDataModel("18", "Cookie Tote", "Comes with medium beverage and 1 side choice", "4.99", "http://static.sfdict.com/sizedimage/sizedimage?width=300&height=300&url=http%3A%2F%2Fstatic.sfdict.com%2Fassets%2F7102-4024811-269.jpg");
//        fragTwoItems.add(item);
//
//        item = new FragmentTwoDataModel("19", "Signature Crafted Crispy Chicken Meal", "Comes with medium beverage and 1 side choice", "8.99", "https://metrouk2.files.wordpress.com/2016/11/fast-food-1.gif");
//        fragTwoItems.add(item);
//
//        item = new FragmentTwoDataModel("20", "Buttermilk Crispy Chicken Sandwich", "Comes with medium beverage and 1 side choice", "8.39", "http://www.referenceforbusiness.com/photos/fast-food-v1-679.jpg");
//        fragTwoItems.add(item);
//
//        item = new FragmentTwoDataModel("21", "Cookie Tote", "Comes with medium beverage and 1 side choice", "4.99", "http://i.dailymail.co.uk/i/pix/2017/01/24/17/3C782A3700000578-4152656-image-a-22_1485280039808.jpg");
//        fragTwoItems.add(item);
//
//        item = new FragmentTwoDataModel("22", "Double Beef n Cheese Burger", "Comes with medium beverage and 1 side choice", "8.99", "https://s-media-cache-ak0.pinimg.com/736x/07/02/41/0702414e760d9cbe7b17d3552b4a8b58--indian-fast-food-indian-snacks.jpg");
//        fragTwoItems.add(item);
//
//        fragTwoAdapter.notifyDataSetChanged();
//
//    }
//
//    private void getFragOneItems() {
//        fragOneItems = new ArrayList<FragmentOneDataModel>();
//        fragOneAdapter = new ResFragOneAdapter(this, fragOneItems);
//
//        FragmentOneDataModel item = new FragmentOneDataModel("Most Popular", true);
//        fragOneItems.add(item);
//
//        item = new FragmentOneDataModel("1", "Buttermilk Crispy Chicken Sandwich", "Comes with medium beverage and 1 side choice", "8.39", "http://img.minq.com/vb/d54aaa20//71606437fe4afcde58f46ff1c2ef82c209bdeaeb.jpg");
//        fragOneItems.add(item);
//        item = new FragmentOneDataModel("2", "Cookie Tote", "Comes with medium beverage and 1 side choice", "4.99", "http://static.sfdict.com/sizedimage/sizedimage?width=300&height=300&url=http%3A%2F%2Fstatic.sfdict.com%2Fassets%2F7102-4024811-269.jpg");
//        fragOneItems.add(item);
//
//        item = new FragmentOneDataModel("3", "Signature Crafted Crispy Chicken Meal", "Comes with medium beverage and 1 side choice", "8.99", "https://metrouk2.files.wordpress.com/2016/11/fast-food-1.gif");
//        fragOneItems.add(item);
//
//        item = new FragmentOneDataModel("4", "Buttermilk Crispy Chicken Sandwich", "Comes with medium beverage and 1 side choice", "8.39", "http://www.referenceforbusiness.com/photos/fast-food-v1-679.jpg");
//        fragOneItems.add(item);
//
//        item = new FragmentOneDataModel("5", "Cookie Tote", "Comes with medium beverage and 1 side choice", "4.99", "http://i.dailymail.co.uk/i/pix/2017/01/24/17/3C782A3700000578-4152656-image-a-22_1485280039808.jpg");
//        fragOneItems.add(item);
//
//        item = new FragmentOneDataModel("6", "Double Beef n Cheese Burger", "Comes with medium beverage and 1 side choice", "8.99", "https://s-media-cache-ak0.pinimg.com/736x/07/02/41/0702414e760d9cbe7b17d3552b4a8b58--indian-fast-food-indian-snacks.jpg");
//        fragOneItems.add(item);
//
//        item = new FragmentOneDataModel("Special Dishes", true);
//        fragOneItems.add(item);
//
//        item = new FragmentOneDataModel("7", "Cookie Tote", "Comes with medium beverage and 1 side choice", "4.99", "http://static.sfdict.com/sizedimage/sizedimage?width=300&height=300&url=http%3A%2F%2Fstatic.sfdict.com%2Fassets%2F7102-4024811-269.jpg");
//        fragOneItems.add(item);
//
//        item = new FragmentOneDataModel("8", "Signature Crafted Crispy Chicken Meal", "Comes with medium beverage and 1 side choice", "8.99", "https://metrouk2.files.wordpress.com/2016/11/fast-food-1.gif");
//        fragOneItems.add(item);
//
//        item = new FragmentOneDataModel("9", "Buttermilk Crispy Chicken Sandwich", "Comes with medium beverage and 1 side choice", "8.39", "http://www.referenceforbusiness.com/photos/fast-food-v1-679.jpg");
//        fragOneItems.add(item);
//
//        item = new FragmentOneDataModel("10", "Cookie Tote", "Comes with medium beverage and 1 side choice", "4.99", "http://i.dailymail.co.uk/i/pix/2017/01/24/17/3C782A3700000578-4152656-image-a-22_1485280039808.jpg");
//        fragOneItems.add(item);
//
//        item = new FragmentOneDataModel("11", "Double Beef n Cheese Burger", "Comes with medium beverage and 1 side choice", "8.99", "https://s-media-cache-ak0.pinimg.com/736x/07/02/41/0702414e760d9cbe7b17d3552b4a8b58--indian-fast-food-indian-snacks.jpg");
//        fragOneItems.add(item);
//
//        fragOneAdapter.notifyDataSetChanged();
//    }

    private void initUI() {
        tvToTimeMain = (TextView) findViewById(R.id.tvToTime);
        tvFromTimeMain = (TextView) findViewById(R.id.tvFromTime);
        swipe_refresh_layout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        tvBasketItemsQuantity = (TextView) findViewById(R.id.tvBasketItemsQuantity);
        tvRestaurantName = (TextView) findViewById(R.id.tvRestaurantName);
        tvRestaurantName.setText(restaurantTitle);
        tvRestaurantDescription = (TextView) findViewById(R.id.tvRestaurantDescription);
        tvRestaurantDescription.setText(restaurantDescription);
        llTopBar = (LinearLayout) findViewById(R.id.llTopBar);
        llTopBar.bringToFront();
        ivTopBarShare = (ImageView) findViewById(R.id.ivTopBarShare);
        ivTopBarShare.setOnClickListener(this);
        ivShare = (ImageView) findViewById(R.id.ivShare);
        ivShare.bringToFront();
        ivShare.setOnClickListener(this);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        ivBack.bringToFront();
        listView = (ObservableListView) findViewById(R.id.list);
        scrollView = (NestedScrollView) findViewById(R.id.scrollView);
        ivTopBarBack = (ImageView) findViewById(R.id.ivTopBarBack);
        ivTopBarShare = (ImageView) findViewById(R.id.ivTopBarShare);
        mImageView = (ImageView) findViewById(R.id.image);
        cvRestaurantDetails = (CardView) findViewById(R.id.cvRestaurantDetails);
        fabScrollNavigation = (FloatingActionButton) findViewById(R.id.fabScrollNavigation);
        tvViewCart = (TextView) findViewById(R.id.tvViewCart);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
//        "https://media-cdn.tripadvisor.com/media/photo-s/0c/8c/05/0b/a-fine-dining-pakistani.jpg"
//        Picasso.with(this).load("https://www.nordicchoicehotels.com/globalassets/global/hotel-pictures/clarion-hotel/clarion-hotel-energy/food--beverage/restaurant-details-energy-hotel-stavanger.jpg?t=SmartScale%7C620x346").centerCrop();
        Glide.with(this).load(restaurantImage).into(mImageView);
        listView.setScrollViewCallbacks(this);

        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                ViewHelper.setTranslationY(mImageView, scrollY / -8);
                ViewHelper.setTranslationY(cvRestaurantDetails, scrollY / -2);
                ViewHelper.setTranslationY(tabLayout, scrollY / -2);
//                ViewHelper.setTranslationY(viewPager, scrollY / -2);


                if (scrollY > oldScrollY) {
//                    Log.i(TAG, "Scroll DOWN");
                    llTopBar.setVisibility(View.VISIBLE);
                }
                if (scrollY < oldScrollY) {
//                    Log.i(TAG, "Scroll UP");

                }

                if (scrollY == 0) {
//                    Log.i(TAG, "TOP SCROLL");
                    llTopBar.setVisibility(View.GONE);
                }

                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
//                    Log.i(TAG, "BOTTOM SCROLL");
                }
            }

        });

        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe_refresh_layout.setRefreshing(false);
            }
        });

        resetViewCartButton();


    }

    private void resetViewCartButton() {
        if (GlobalClass.selectedFoodListInBasket.size() > 0) {
            tvViewCart.setVisibility(View.VISIBLE);
            tvBasketItemsQuantity.setVisibility(View.VISIBLE);
            tvBasketItemsQuantity.setText("[ " + GlobalClass.selectedFoodListInBasket.size() + " ]");
        } else {
            tvViewCart.setVisibility(View.GONE);
            tvBasketItemsQuantity.setVisibility(View.GONE);
        }
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
/*        ViewHelper.setTranslationY(mImageView, scrollY / -2);
        ViewHelper.setTranslationY(cvRestaurantDetails, scrollY / -2);
        ViewHelper.setTranslationY(tabLayout, scrollY / -2);
        ViewHelper.setTranslationY(viewPager, scrollY / -2);*/
//        viewPager.getLayoutParams().height = viewPager.getHeight() + 100;
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        /*ActionBar ab = getSupportActionBar();
        if (scrollState == ScrollState.DOWN) {
            if (ab.isShowing()) {
                ab.hide();
            }
        } else if (scrollState == ScrollState.UP) {
            if (!ab.isShowing()) {
                ab.show();
            }
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        resetViewCartButton();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivShare:
            case R.id.ivTopBarShare:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share this Restaurant"));
                break;
        }
    }
}