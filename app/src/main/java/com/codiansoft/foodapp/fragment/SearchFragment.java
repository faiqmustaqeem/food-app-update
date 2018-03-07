package com.codiansoft.foodapp.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
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
import com.codiansoft.foodapp.AccountActivity;
import com.codiansoft.foodapp.GlobalClass;
import com.codiansoft.foodapp.MainActivity;
import com.codiansoft.foodapp.R;
import com.codiansoft.foodapp.adapter.RestaurantsRVAdapter;
import com.codiansoft.foodapp.adapter.SearchCategoriesAdapter;
import com.codiansoft.foodapp.model.RestaurantsModel;
import com.codiansoft.foodapp.model.SearchCategoriesModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by salal-khan on 7/1/2017.
 */

public class SearchFragment extends Fragment {
    public static Activity activity;

    public static final String TAG = "SearchFragment";
    public static GridView gvSearchCategories;
    View view;
    ArrayList<SearchCategoriesModel> searchCategoriesModelArrayList;
    SearchCategoriesModel searchCategoriesModel;
    AutoCompleteTextView actvSearch;
    SearchCategoriesAdapter gridViewAdapter;

    public static RecyclerView rvSearchedRestaurants;
    public static List<RestaurantsModel> searchedRestaurantslist = new ArrayList<>();
    public static RestaurantsRVAdapter searchRVadapter;
    public static RestaurantsModel restaurantsModel;
    RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = getActivity();
/*        EditText v = new EditText(getActivity());
        v.setText("Hello Fragment!");*/
        view = inflater.inflate(R.layout.fragment_search, container, false);

        rvSearchedRestaurants = (RecyclerView) view.findViewById(R.id.rvSearchedRestaurants);
        mLayoutManager = new LinearLayoutManager(getActivity());

        rvSearchedRestaurants.setLayoutManager(mLayoutManager);

        gvSearchCategories = (GridView) view.findViewById(R.id.gvSearchCategories);
        searchCategoriesModelArrayList = new ArrayList<SearchCategoriesModel>();

        // Get a reference to the AutoCompleteTextView in the layout
        actvSearch = (AutoCompleteTextView) view.findViewById(R.id.actvSearch);
        // Get the string array
        final String[] foodTypes = {"Vegetarian", "Desert", "Beverages", "Chinese", "Italian", "Vegetarian", "Desert", "Beverages", "Chinese"};
        // Create the adapter and set it to the AutoCompleteTextView
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, foodTypes);

        actvSearch.setThreshold(1);
        actvSearch.setAdapter(adapter);

        actvSearch.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
                if (foodTypes.length > 0) {
                    // show all suggestions
                    if (!actvSearch.getText().toString().equals(""))
                        adapter.getFilter().filter(null);
                    actvSearch.showDropDown();
                }
                return false;
            }
        });

        gvSearchCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                gvSearchCategories.setVisibility(View.GONE);
                rvSearchedRestaurants.setVisibility(View.VISIBLE);

                searchedRestaurantslist = new ArrayList<>();
                restaurantsModel = new RestaurantsModel("", "", "", "", "","");
                searchedRestaurantslist.add(restaurantsModel);

                String id=searchCategoriesModelArrayList.get(i).getID();
                fetchRestaurants(id);
            }
        });

        actvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                gvSearchCategories.setVisibility(View.GONE);
                rvSearchedRestaurants.setVisibility(View.VISIBLE);

                Toast.makeText(getActivity(), "" + actvSearch.getText().toString(), Toast.LENGTH_SHORT).show();

                searchedRestaurantslist = new ArrayList<>();
                restaurantsModel = new RestaurantsModel("", "", "", "", "","");
                searchedRestaurantslist.add(restaurantsModel);


            }
        });

        actvSearch.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (adapter != null) gridViewAdapter.getFilter().filter(s.toString());
            }
        });

        initCategories();

        gvSearchCategories.setVisibility(View.VISIBLE);


        return view;
    }

    public static void fetchRestaurants(final String categoryId) {

       // restaurantsModel = new RestaurantsModel("1", "Nando's", "http://franchiseexpert.in/wp-content/uploads/2014/11/Nandos.jpg", "20-30 MIN", "Fast Food");
//        searchedRestaurantslist.add(restaurantsModel);
//        restaurantsModel = new RestaurantsModel("2", "Domino's", "http://www.mbjairport.com/content/312/dominos_f575x300.jpg", "10-15 MIN", "Fast Food");
//        searchedRestaurantslist.add(restaurantsModel);
//        restaurantsModel = new RestaurantsModel("3", "Burger King", "https://www.welivesecurity.com/wp-content/uploads/2016/05/descuento-burger-king-623x432.jpg", "40-50 MIN", "Fast Food");
//        searchedRestaurantslist.add(restaurantsModel);
//        restaurantsModel = new RestaurantsModel("4", "Pizza Hut", "http://members.wisdells.com/member-media/rest/PizzaHut.jpg", "20-30 MIN", "Fast Food");
//        searchedRestaurantslist.add(restaurantsModel);
//        restaurantsModel = new RestaurantsModel("5", "Sub Way", "https://onefatfrog23.files.wordpress.com/2013/08/subway01_full.jpg", "30-40 MIN", "Fast Food");
//        searchedRestaurantslist.add(restaurantsModel);
//
//        searchRVadapter = new RestaurantsRVAdapter(activity, searchedRestaurantslist);
//        rvSearchedRestaurants.setAdapter(searchRVadapter);

        RequestQueue queue = Volley.newRequestQueue(activity);
        StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.SEARCH_RESTAURANT_VIA_CATEGIES_RESULT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONObject Jobject = new JSONObject(response);
                            JSONObject result = Jobject.getJSONObject("result");
                            if (result.getString("status").equals("success"))
                            {
                                JSONArray restaurants=result.getJSONArray("response");
                                    searchedRestaurantslist.clear();
                                for(int i=0 ; i< restaurants.length(); i++)
                                {
                                    JSONObject oneRestaurant=restaurants.getJSONObject(i);

                                    String restaurantId="" , restaurantName="" , restaurantPic="" , restaurantDuration="" , restaurantType="" , restaurantBranchId="";
                                    restaurantId=oneRestaurant.getString("restaurant_id");
                                    restaurantName=oneRestaurant.getString("name");
                                    restaurantPic=oneRestaurant.getString("logo");
                                    restaurantDuration=oneRestaurant.getString("timing");
                                    restaurantType=oneRestaurant.getString("desc");
                                    restaurantBranchId=oneRestaurant.getString("branch_id");

                                    RestaurantsModel restaurantsModel=new RestaurantsModel(restaurantId,restaurantName,restaurantPic,restaurantDuration,restaurantType,restaurantBranchId);

                                    searchedRestaurantslist.add(restaurantsModel);

                                }

                                        searchRVadapter = new RestaurantsRVAdapter(activity, searchedRestaurantslist);
                                    rvSearchedRestaurants.setAdapter(searchRVadapter);



                            }
                        } catch (Exception ee) {
                            Toast.makeText(activity, "" + ee.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        //pDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(activity, "Connection Error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof AuthFailureError) {

                            //TODO
                        } else if (error instanceof ServerError) {

                            //TODO
                        } else if (error instanceof NetworkError) {

                            //TODO
                        } else if (error instanceof ParseError) {

                            //TODO
                        }
//                        pDialog.dismiss();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences settings = activity.getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
                String userID = settings.getString("userID", "defaultValue");
                String apiSecretKey = settings.getString("apiSecretKey", "defaultValue");

                params.put("api_secret", apiSecretKey);
                params.put("type_id",categoryId);
                Log.e("api_secret",params.toString());
                return params;
            }
        };

        queue.add(postRequest);

    }

    private void initCategories() {

        RequestQueue queue = Volley.newRequestQueue(activity);
        StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.CATEGORIES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONObject Jobject = new JSONObject(response);
                            JSONObject result = Jobject.getJSONObject("result");
                            if (result.getString("status").equals("success")) {
                                JSONArray categories = result.getJSONArray("response");
                                searchCategoriesModelArrayList.clear();
                                for (int i=0 ; i < categories.length() ; i++)
                                {
                                    JSONObject category = categories.getJSONObject(i);

                                    String id=category.getString("id");
                                    String type=category.getString("food_type");
                                    String image=category.getString("image");
                                    SearchCategoriesModel searchCategoriesModel=new SearchCategoriesModel(id , image , type);
                                    searchCategoriesModelArrayList.add(searchCategoriesModel);

                                }

                            gridViewAdapter = new SearchCategoriesAdapter(getContext(), searchCategoriesModelArrayList);
                            gvSearchCategories.setAdapter(gridViewAdapter);


                            }
                        } catch (Exception ee) {
                            Toast.makeText(activity, "" + ee.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        //pDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(activity, "Connection Error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof AuthFailureError) {

                            //TODO
                        } else if (error instanceof ServerError) {

                            //TODO
                        } else if (error instanceof NetworkError) {

                            //TODO
                        } else if (error instanceof ParseError) {

                            //TODO
                        }
//                        pDialog.dismiss();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences settings = activity.getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
                String userID = settings.getString("userID", "defaultValue");
                String apiSecretKey = settings.getString("apiSecretKey", "defaultValue");

                params.put("api_secret", apiSecretKey);

                Log.e("api_secret",params.toString());
                return params;
            }
        };
        queue.add(postRequest);

    }

    private void fetchSearchedRestaurant() {
       // pDialog.show();

    }

}