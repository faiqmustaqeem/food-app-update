package com.codiansoft.foodapp.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
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
    public static String serviceRestaurantType;
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


        Log.e("start_size" , restaurantslist.size()+"");
        adapter = new RestaurantsRVAdapter(getActivity(), restaurantslist);
        rvRestaurants.setAdapter(adapter);


        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);



                                        fetchRestaurantsFromServer();


                                        swipeRefreshLayout.setRefreshing(false);
                                    }
                                }
        );
    }

    private void fetchRestaurantsFromServer() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.SERVICES_RESTAURANTS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e("response" , response);
                        try {


                            JSONObject Jobject = new JSONObject(response);
                            JSONObject result = Jobject.getJSONObject("result");
                            if (result.getString("status").equals("success")) {

                                JSONArray restaurants = null;

                                if (serviceRestaurantType.equals("4")) {
                                    restaurants = result.getJSONArray("quick_service_restaurants");
                                } else if (serviceRestaurantType.equals("3")) {
                                    restaurants = result.getJSONArray("reservation_restaurants");
                                } else if (serviceRestaurantType.equals("2")) {
                                    restaurants = result.getJSONArray("takeaway_restaurants");
                                } else if (serviceRestaurantType.equals("1")) {
                                    restaurants = result.getJSONArray("delivery_restaurants");
                                }
                                Log.e("size" , restaurantslist.size()+"");
                                if (restaurants.length() > 0) {
                                    restaurantsModel = new RestaurantsModel("", "", "", "", "","");
                                    restaurantslist.add(restaurantsModel);
                                    for (int i = 0; i < restaurants.length(); i++) {
                                        JSONObject Data = restaurants.getJSONObject(i);
                                        Log.e("restaurant_id", Data.getString("restaurant_id"));
                                        restaurantsModel = new RestaurantsModel(Data.getString("restaurant_id"), Data.getString("name"), Data.getString("logo"), Data.getString("timing"), Data.getString("desc"),Data.getString("branch_id"));
                                        restaurantslist.add(restaurantsModel);
                                        Log.e("size" , restaurantslist.size()+"");
                                    }
                                    adapter.notifyDataSetChanged();
//                                    adapter = new RestaurantsRVAdapter(getActivity(), restaurantslist);
//                                    rvRestaurants.setAdapter(adapter);
                                }



                            }
                        } catch (Exception ee) {

                            Log.e("error" , ee.toString());
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
                SharedPreferences settings = getActivity().getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
                String userID = settings.getString("userID", "defaultValue");
                String apiSecretKey = settings.getString("apiSecretKey", "defaultValue");
                params.put("api_secret", apiSecretKey);
                params.put("type", serviceRestaurantType);
                Log.e("params" , params.toString());

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