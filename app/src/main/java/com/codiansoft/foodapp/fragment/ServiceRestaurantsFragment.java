package com.codiansoft.foodapp.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.codiansoft.foodapp.R;
import com.codiansoft.foodapp.adapter.RestaurantsRVAdapter;
import com.codiansoft.foodapp.model.RestaurantsModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Codiansoft on 8/30/2017.
 */

public class ServiceRestaurantsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    public static final String TAG = "ServiceRestaurantsFragment";
    View view;

    private SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView rvRestaurants;
    List<RestaurantsModel> restaurantslist = new ArrayList<>();
    RestaurantsModel restaurantsModel;
    private RestaurantsRVAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
/*        EditText v = new EditText(getActivity());
        v.setText("Hello Fragment!");*/
        view = inflater.inflate(R.layout.fragment_service_restaurant, container, false);
        init();
        return view;
    }

    private void init() {

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        rvRestaurants = (RecyclerView) view.findViewById(R.id.rvRestaurants);

/*        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);*/

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

//                                        fetchRestaurantsFromServer();

                                        restaurantslist = new ArrayList<>();
                                        restaurantsModel = new RestaurantsModel("", "", "", "", "");
                                        restaurantslist.add(restaurantsModel);

                                        restaurantsModel = new RestaurantsModel("1", "McDonald's", "https://cdn-jpg1.thedailymeal.com/sites/default/files/styles/tdm_slideshow_large/public/images/Mcdonalds_logo96.jpg?itok=Mw_7SQGy", "20-30 MIN", "Fast Food");
                                        restaurantslist.add(restaurantsModel);
                                        restaurantsModel = new RestaurantsModel("2", "KFC PAK", "https://www.onlyitaewon.com/wp-content/uploads/2016/08/KFC-Itaewon-2.jpg", "10-15 MIN", "Fast Food");
                                        restaurantslist.add(restaurantsModel);
                                        restaurantsModel = new RestaurantsModel("3", "Burger King", "https://www.welivesecurity.com/wp-content/uploads/2016/05/descuento-burger-king-623x432.jpg", "40-50 MIN", "Fast Food");
                                        restaurantslist.add(restaurantsModel);
                                        restaurantsModel = new RestaurantsModel("4", "Pizza Hut", "http://members.wisdells.com/member-media/rest/PizzaHut.jpg", "20-30 MIN", "Fast Food");
                                        restaurantslist.add(restaurantsModel);
                                        restaurantsModel = new RestaurantsModel("5", "Sub Way", "https://onefatfrog23.files.wordpress.com/2013/08/subway01_full.jpg", "30-40 MIN", "Fast Food");
                                        restaurantslist.add(restaurantsModel);

                                        adapter = new RestaurantsRVAdapter(getActivity(), restaurantslist);
                                        rvRestaurants.setAdapter(adapter);

                                        swipeRefreshLayout.setRefreshing(false);
                                    }
                                }
        );
    }

    private void fetchRestaurantsFromServer() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest postRequest = new StringRequest(Request.Method.POST, "webserviceurl",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {

                            restaurantslist = new ArrayList<>();
                            JSONObject Jobject = new JSONObject(response);
                            JSONObject result = Jobject.getJSONObject("result");
                            if (result.get("status").equals("success")) {
                                JSONObject data = Jobject.getJSONObject("data");
                                JSONArray usersarray = data.getJSONArray("all users");

                                if (usersarray.length() > 0) {
                                    for (int i = 0; i < usersarray.length(); i++) {
                                        JSONObject Data = usersarray.getJSONObject(i);
                                        restaurantsModel = new RestaurantsModel(Data.getString("user_id"), Data.getString("name"), Data.getString("restaurant_pic"), Data.getString("duration"), Data.getString("type"));
                                        restaurantslist.add(restaurantsModel);
                                    }
                                }

                                adapter = new RestaurantsRVAdapter(getContext(), restaurantslist);
                                rvRestaurants.setAdapter(adapter);

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
                /*params.put("username", ""+ AppConstants.PROFILE_NAME);
                params.put("page_number", 1 + "");*/
/*
                SharedPreferences settings = getActivity().getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
                String userID = settings.getString("userID", "defaultValue");
                String apiSecretKey = settings.getString("apiSecretKey", "defaultValue");*/
/*
                params.put("api_secret", apiSecretKey);
                params.put("username", "");
                params.put("page_number", "");
*/
//                "26b4b8f30842b5915138fc5ce53da6443e38fc9d2e"

                return params;
            }
        };
        queue.add(postRequest);

    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);

        swipeRefreshLayout.setRefreshing(false);
    }
}