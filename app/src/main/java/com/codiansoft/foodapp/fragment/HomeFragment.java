package com.codiansoft.foodapp.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.codiansoft.foodapp.GlobalClass;
import com.codiansoft.foodapp.R;
import com.codiansoft.foodapp.adapter.RestaurantsRVAdapter;
import com.codiansoft.foodapp.adapter.DealsSliderAdapter;
import com.codiansoft.foodapp.adapter.RestaurantsSliderAdapter;
import com.codiansoft.foodapp.model.DealsModel;
import com.codiansoft.foodapp.model.RestaurantsModel;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator;

import static com.codiansoft.foodapp.MainActivity.myLat;
import static com.codiansoft.foodapp.MainActivity.myLng;

/**
 * Created by salal-khan on 7/1/2017.
 */

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    public static final String TAG = "HomeFragment";
    View view;
    private static ViewPager dealsPager;
    CircleIndicator dealsIndicator;
    private static ViewPager featuredPager;
    CircleIndicator featuredIndicator;
    private static ViewPager newPager;
    CircleIndicator newIndicator;
    private static ViewPager popularPager;
    CircleIndicator popularIndicator;
    private static ViewPager underThirtyPager;
    CircleIndicator underThirtyIndicator;
    private static ViewPager friendsFavPager;
    CircleIndicator friendsFavIndicator;
    private static int currentPage = 0;

    private ArrayList<DealsModel> popularDealsList = new ArrayList<DealsModel>();
    private ArrayList<RestaurantsModel> featuredRestaurantsList = new ArrayList<RestaurantsModel>();
    private ArrayList<RestaurantsModel> newRestaurantsList = new ArrayList<RestaurantsModel>();
    private ArrayList<RestaurantsModel> popularRestaurantsList = new ArrayList<RestaurantsModel>();
    private ArrayList<RestaurantsModel> underThirtyRestaurantsList = new ArrayList<RestaurantsModel>();
    private ArrayList<RestaurantsModel> friendsFavRestaurantsList = new ArrayList<RestaurantsModel>();


    DealsModel popularDealsModel;
    RestaurantsModel featuredRestaurantsModel, newRestaurantsModel, popularRestaurantsModel, underThirtyRestaurantsModel, friendsFavRestaurantsModel;


    private SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView rvRestaurants;
    List<RestaurantsModel> restaurantslist = new ArrayList<>();
    RestaurantsModel restaurantsModel;
    private RestaurantsRVAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
/*        EditText v = new EditText(getActivity());
        v.setText("Hello Fragment!");*/
        view = inflater.inflate(R.layout.fragment_home, container, false);
        initUI();
        return view;
    }

    private void fetchAllSlidersRestaurants() {
        /*newRestaurantsModel = new RestaurantsModel("1", "Restaurant-1", "http://www.tamiu.edu/stucenter/visit/images/foodcourt.jpg", "20-30 MIN", "1");
        newRestaurantsList.add(newRestaurantsModel);
        newRestaurantsModel = new RestaurantsModel("1", "Restaurant-1", "http://www.ucbuilding.com/src/images/gallery/4/1446657001_l.jpg", "20-30 MIN", "1");
        newRestaurantsList.add(newRestaurantsModel);
        newPager.setAdapter(new RestaurantsSliderAdapter(getActivity(), newRestaurantsList));
        newIndicator.setViewPager(newPager);*/
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.ALL_SLIDERS_RESTAURANTS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONObject Jobject = new JSONObject(response);
                            JSONObject result = Jobject.getJSONObject("result");
                            if (result.getString("status").equals("success")) {

                                JSONArray newRestaurantsArray = result.getJSONArray("new_on_foodApp");
                                if (newRestaurantsArray.length() > 0) {
                                    for (int i = 0; i < newRestaurantsArray.length(); i++) {
                                        JSONObject resObj = newRestaurantsArray.getJSONObject(i);
                                        newRestaurantsModel = new RestaurantsModel(resObj.getString("id"), resObj.getString("name"), resObj.getString("logo"), resObj.getString("timing"), resObj.getString("desc"),resObj.getString("branch_id"));
                                        newRestaurantsList.add(newRestaurantsModel);
                                    }
                                }
                                newPager.setAdapter(new RestaurantsSliderAdapter(getActivity(), newRestaurantsList));
                                newIndicator.setViewPager(newPager);


                                JSONArray popularRestaurantsArray = result.getJSONArray("most_popular");
                                if (popularRestaurantsArray.length() > 0) {
                                    for (int i = 0; i < popularRestaurantsArray.length(); i++) {
                                        JSONObject resObj = popularRestaurantsArray.getJSONObject(i);
                                        popularRestaurantsModel = new RestaurantsModel(resObj.getString("id"), resObj.getString("name"), resObj.getString("logo"), resObj.getString("timing"), resObj.getString("desc"),resObj.getString("branch_id"));
                                        popularRestaurantsList.add(newRestaurantsModel);
                                    }
                                }
                                popularPager.setAdapter(new RestaurantsSliderAdapter(getActivity(), popularRestaurantsList));
                                popularIndicator.setViewPager(popularPager);


                                JSONArray featuredRestaurantsArray = result.getJSONArray("top_featured_restaurants");
                                if (featuredRestaurantsArray.length() > 0) {
                                    for (int i = 0; i < featuredRestaurantsArray.length(); i++) {
                                        JSONObject resObj = featuredRestaurantsArray.getJSONObject(i);
                                        featuredRestaurantsModel = new RestaurantsModel(resObj.getString("id"), resObj.getString("name"), resObj.getString("logo"), "20-30 MIN", resObj.getString("desc"),resObj.getString("branch_id"));
                                        featuredRestaurantsList.add(newRestaurantsModel);
                                    }
                                }
                                featuredPager.setAdapter(new RestaurantsSliderAdapter(getActivity(), featuredRestaurantsList));
                                featuredIndicator.setViewPager(dealsPager);


                                JSONArray foodDealsArray = result.getJSONArray("top_food_deals");
                                if (featuredRestaurantsArray.length() > 0) {
                                    for (int i = 0; i < featuredRestaurantsArray.length(); i++) {
                                        JSONObject resObj = featuredRestaurantsArray.getJSONObject(i);
                                        popularDealsModel = new DealsModel("2", "Deal 2", "30-40 MIN", "950", "Fast Food", "http://cf2.foodista.com/sites/default/files/styles/featured/public/field/image/dry%20mee%20siam%20017.JPG", "Description description description description");
                                        popularDealsList.add(popularDealsModel);
                                    }
                                }
                                dealsPager.setAdapter(new DealsSliderAdapter(getActivity(), popularDealsList));
                                dealsIndicator.setViewPager(dealsPager);


                                swipeRefreshLayout.setRefreshing(false);
                            }
                        } catch (Exception ee) {
                                Log.e("error",ee.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_SHORT).show();
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
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences settings = getActivity().getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
                String userID = settings.getString("userID", "defaultValue");
                String apiSecretKey = settings.getString("apiSecretKey", "defaultValue");

                params.put("api_secret", apiSecretKey);
                return params;
            }
        };
        queue.add(postRequest);


    }

    private void initUI() {
        dealsPager = (ViewPager) view.findViewById(R.id.deals_pager);
        featuredPager = (ViewPager) view.findViewById(R.id.featured_pager);
        newPager = (ViewPager) view.findViewById(R.id.new_pager);
        popularPager = (ViewPager) view.findViewById(R.id.popular_pager);
        underThirtyPager = (ViewPager) view.findViewById(R.id.under_thirty_pager);
        friendsFavPager = (ViewPager) view.findViewById(R.id.friends_fav_pager);

        dealsIndicator = (CircleIndicator) view.findViewById(R.id.deals_indicator);
        featuredIndicator = (CircleIndicator) view.findViewById(R.id.featured_indicator);
        newIndicator = (CircleIndicator) view.findViewById(R.id.new_indicator);
        popularIndicator = (CircleIndicator) view.findViewById(R.id.popular_indicator);
        underThirtyIndicator = (CircleIndicator) view.findViewById(R.id.under_thirty_indicator);
        friendsFavIndicator = (CircleIndicator) view.findViewById(R.id.friends_fav_indicator);

        initDealsSlider();

        initRestaurantsSliders();

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        rvRestaurants = (RecyclerView) view.findViewById(R.id.rvRestaurants);

/*        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);*/

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        rvRestaurants.setLayoutManager(layoutManager);


        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                /*if (currentPage == XMEN.length) {
                    currentPage = 0;
                }
                dealsPager.setCurrentItem(currentPage++, true);*/
            }
        };
        /*Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2500, 2500);*/

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);


                                        restaurantslist = new ArrayList<>();
                                        restaurantsModel = new RestaurantsModel("", "", "", "", "","");
                                        restaurantslist.add(restaurantsModel);

                                        fetchRestaurantsFromServer();
                                        fetchAllSlidersRestaurants();

                                        restaurantsModel = new RestaurantsModel("1", "McDonald's", "https://cdn-jpg1.thedailymeal.com/sites/default/files/styles/tdm_slideshow_large/public/images/Mcdonalds_logo96.jpg?itok=Mw_7SQGy", "20-30 MIN", "Fast Food","1");
                                        restaurantslist.add(restaurantsModel);
                                        restaurantsModel = new RestaurantsModel("2", "KFC PAK", "https://www.onlyitaewon.com/wp-content/uploads/2016/08/KFC-Itaewon-2.jpg", "10-15 MIN", "Fast Food","1");
                                        restaurantslist.add(restaurantsModel);
                                        restaurantsModel = new RestaurantsModel("3", "Burger King", "https://www.welivesecurity.com/wp-content/uploads/2016/05/descuento-burger-king-623x432.jpg", "40-50 MIN", "Fast Food","1");
                                        restaurantslist.add(restaurantsModel);
                                        restaurantsModel = new RestaurantsModel("4", "Pizza Hut", "http://members.wisdells.com/member-media/rest/PizzaHut.jpg", "20-30 MIN", "Fast Food","1");
                                        restaurantslist.add(restaurantsModel);
                                        restaurantsModel = new RestaurantsModel("5", "Sub Way", "https://onefatfrog23.files.wordpress.com/2013/08/subway01_full.jpg", "30-40 MIN", "Fast Food","1");
                                        restaurantslist.add(restaurantsModel);


                                    }
                                }
        );
    }

    private void initRestaurantsSliders() {
        /*featuredRestaurantsModel = new RestaurantsModel("1", "Restaurant-1", "http://www.thesungazette.com/wp-content/uploads/2016/03/re-Bus-LindsayFoods-open.png", "20-30 MIN", "1");
        featuredRestaurantsList.add(featuredRestaurantsModel);
        featuredRestaurantsModel = new RestaurantsModel("1", "Restaurant-1", "http://www.ucbuilding.com/src/images/gallery/4/1446657022_l.jpg", "20-30 MIN", "1");
        featuredRestaurantsList.add(featuredRestaurantsModel);
        featuredPager.setAdapter(new RestaurantsSliderAdapter(getActivity(), featuredRestaurantsList));
        featuredIndicator.setViewPager(dealsPager);*/

        /*newRestaurantsModel = new RestaurantsModel("1", "Restaurant-1", "http://www.tamiu.edu/stucenter/visit/images/foodcourt.jpg", "20-30 MIN", "1");
        newRestaurantsList.add(newRestaurantsModel);
        newRestaurantsModel = new RestaurantsModel("1", "Restaurant-1", "http://www.ucbuilding.com/src/images/gallery/4/1446657001_l.jpg", "20-30 MIN", "1");
        newRestaurantsList.add(newRestaurantsModel);
        newPager.setAdapter(new RestaurantsSliderAdapter(getActivity(), newRestaurantsList));
        newIndicator.setViewPager(newPager);*/

/*
        popularRestaurantsModel = new RestaurantsModel("1", "Restaurant-1", "https://media-cdn.tripadvisor.com/media/photo-s/01/c0/e4/bf/food-court.jpg", "20-30 MIN", "1");
        popularRestaurantsList.add(popularRestaurantsModel);
        popularRestaurantsModel = new RestaurantsModel("1", "Restaurant-1", "https://images4.persgroep.net/rcs/xFvDCbC8UCs-Gu8GLd1I8IBN2Lg/diocontent/108025095/_fitwidth/694/?appId=21791a8992982cd8da851550a453bd7f&quality=0.9", "20-30 MIN", "1");
        popularRestaurantsList.add(popularRestaurantsModel);
        popularPager.setAdapter(new RestaurantsSliderAdapter(getActivity(), popularRestaurantsList));
        popularIndicator.setViewPager(popularPager);
*/
//        fetchMostPopularRestaurants();

        underThirtyRestaurantsModel = new RestaurantsModel("1", "Restaurant-1", "http://www.kfcyumcenter.com/assets/img/MillerLiteLounge.jpg", "20-30 MIN", "1","1");
        underThirtyRestaurantsList.add(underThirtyRestaurantsModel);
        underThirtyRestaurantsModel = new RestaurantsModel("1", "Restaurant-1", "http://www.foodcenter.nl/db/WAS45cc54f66d464/poort2.jpg", "20-30 MIN", "1","1");
        underThirtyRestaurantsList.add(underThirtyRestaurantsModel);
        underThirtyPager.setAdapter(new RestaurantsSliderAdapter(getActivity(), underThirtyRestaurantsList));
        underThirtyIndicator.setViewPager(underThirtyPager);

        friendsFavRestaurantsModel = new RestaurantsModel("1", "Restaurant-1", "https://whyhunger.org/media/k2/galleries/2321/141207_Austin_SustainableFoodCenter0156.jpg", "20-30 MIN", "1","1");
        friendsFavRestaurantsList.add(friendsFavRestaurantsModel);
        friendsFavRestaurantsModel = new RestaurantsModel("1", "Restaurant-1", "http://www.raytheon.com/news/rtnwcm/groups/gallery/documents/image/wildernesswarriors3_body_img01.jpg", "20-30 MIN", "1","1");
        friendsFavRestaurantsList.add(friendsFavRestaurantsModel);
        friendsFavPager.setAdapter(new RestaurantsSliderAdapter(getActivity(), friendsFavRestaurantsList));
        friendsFavIndicator.setViewPager(friendsFavPager);
    }

    private void fetchMostPopularRestaurants() {

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.MOST_POPULAR_RESTAURANTS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {

                            restaurantslist = new ArrayList<>();
                            JSONObject Jobject = new JSONObject(response);
                            JSONObject result = Jobject.getJSONObject("result");
                            if (result.getString("status").equals("success")) {
                                JSONArray restaurantsArray = result.getJSONArray("response");

                                popularRestaurantsModel = new RestaurantsModel("1", "Restaurant-1", "https://media-cdn.tripadvisor.com/media/photo-s/01/c0/e4/bf/food-court.jpg", "20-30 MIN", "1","1");
                                popularRestaurantsList.add(popularRestaurantsModel);
                                popularRestaurantsModel = new RestaurantsModel("1", "Restaurant-1", "https://images4.persgroep.net/rcs/xFvDCbC8UCs-Gu8GLd1I8IBN2Lg/diocontent/108025095/_fitwidth/694/?appId=21791a8992982cd8da851550a453bd7f&quality=0.9", "20-30 MIN", "1","1");
                                popularRestaurantsList.add(popularRestaurantsModel);
                                popularPager.setAdapter(new RestaurantsSliderAdapter(getActivity(), popularRestaurantsList));
                                popularIndicator.setViewPager(popularPager);

                                swipeRefreshLayout.setRefreshing(false);
                            }
                        } catch (Exception ee) {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_SHORT).show();
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
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences settings = getActivity().getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
                String userID = settings.getString("userID", "defaultValue");
                String apiSecretKey = settings.getString("apiSecretKey", "defaultValue");

                params.put("api_secret", apiSecretKey);
                params.put("restaurant_id", GlobalClass.selectedRestaurantID);
                params.put("branch_id", "1");

                return params;
            }
        };
        queue.add(postRequest);
    }

    private void initDealsSlider() {
        popularDealsModel = new DealsModel("2", "Deal 2", "30-40 MIN", "950", "Fast Food", "http://cf2.foodista.com/sites/default/files/styles/featured/public/field/image/dry%20mee%20siam%20017.JPG", "Description description description description");
        popularDealsList.add(popularDealsModel);
        popularDealsModel = new DealsModel("1", "Deal 1", "20-30 MIN", "550", "Fast Food", "http://naturalsociety.com/wp-content/uploads/food_burger_fries_fast_food_735_350.jpg", "Description description description descriptionDescription description description description");
        popularDealsList.add(popularDealsModel);

        dealsPager.setAdapter(new DealsSliderAdapter(getActivity(), popularDealsList));
        dealsIndicator.setViewPager(dealsPager);
    }

    private void fetchRestaurantsFromServer() {

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.MORE_RESTAURANTS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {

                            restaurantslist = new ArrayList<>();
                            JSONObject Jobject = new JSONObject(response);
                            JSONObject result = Jobject.getJSONObject("result");
                            if (result.getString("status").equals("success")) {
                                JSONArray restaurantsArray = result.getJSONArray("response");

                                if (restaurantsArray.length() > 0) {
                                    for (int i = 0; i < restaurantsArray.length(); i++) {
                                        JSONObject Data = restaurantsArray.getJSONObject(i);
                                        float distanceInMeters = calculateDistance(new LatLng(myLat, myLng), new LatLng(Double.parseDouble(Data.getString("lat")), Double.parseDouble(Data.getString("lng"))));
                                        restaurantsModel = new RestaurantsModel(Data.getString("id"), Data.getString("name"), Data.getString("logo"), distanceInMeters + "M away", Data.getString("desc"),Data.getString("branch_id"));
                                        restaurantslist.add(restaurantsModel);
                                    }
                                }
                                adapter = new RestaurantsRVAdapter(getActivity(), restaurantslist);
                                rvRestaurants.setAdapter(adapter);

                                swipeRefreshLayout.setRefreshing(false);
                            }
                        } catch (Exception ee) {
                            Toast.makeText(getActivity(), "" + ee.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_SHORT).show();
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
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences settings = getActivity().getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
                String userID = settings.getString("userID", "defaultValue");
                String apiSecretKey = settings.getString("apiSecretKey", "defaultValue");

                params.put("api_secret", apiSecretKey);

                return params;
            }
        };
        queue.add(postRequest);
    }

    private float calculateDistance(LatLng myLatLng, LatLng resLatLng) {
        Location myLocation = new Location("");
        myLocation.setLatitude(myLatLng.latitude);
        myLocation.setLongitude(myLatLng.longitude);

        Location resLocation = new Location("");
        myLocation.setLatitude(resLatLng.latitude);
        myLocation.setLongitude(resLatLng.longitude);

        float distanceInMeters = resLocation.distanceTo(myLocation);

        return distanceInMeters;
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);

        dealsPager.invalidate();

        swipeRefreshLayout.setRefreshing(false);
    }
}