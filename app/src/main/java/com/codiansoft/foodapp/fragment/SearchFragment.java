package com.codiansoft.foodapp.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.codiansoft.foodapp.MainActivity;
import com.codiansoft.foodapp.R;
import com.codiansoft.foodapp.adapter.RestaurantsRVAdapter;
import com.codiansoft.foodapp.adapter.SearchCategoriesAdapter;
import com.codiansoft.foodapp.model.RestaurantsModel;
import com.codiansoft.foodapp.model.SearchCategoriesModel;

import java.util.ArrayList;
import java.util.List;

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
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, foodTypes);
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
                restaurantsModel = new RestaurantsModel("", "", "", "", "");
                searchedRestaurantslist.add(restaurantsModel);

                fetchRestaurants();
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
                restaurantsModel = new RestaurantsModel("", "", "", "", "");
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

    public static void fetchRestaurants() {
        restaurantsModel = new RestaurantsModel("1", "Nando's", "http://franchiseexpert.in/wp-content/uploads/2014/11/Nandos.jpg", "20-30 MIN", "Fast Food");
        searchedRestaurantslist.add(restaurantsModel);
        restaurantsModel = new RestaurantsModel("2", "Domino's", "http://www.mbjairport.com/content/312/dominos_f575x300.jpg", "10-15 MIN", "Fast Food");
        searchedRestaurantslist.add(restaurantsModel);
        restaurantsModel = new RestaurantsModel("3", "Burger King", "https://www.welivesecurity.com/wp-content/uploads/2016/05/descuento-burger-king-623x432.jpg", "40-50 MIN", "Fast Food");
        searchedRestaurantslist.add(restaurantsModel);
        restaurantsModel = new RestaurantsModel("4", "Pizza Hut", "http://members.wisdells.com/member-media/rest/PizzaHut.jpg", "20-30 MIN", "Fast Food");
        searchedRestaurantslist.add(restaurantsModel);
        restaurantsModel = new RestaurantsModel("5", "Sub Way", "https://onefatfrog23.files.wordpress.com/2013/08/subway01_full.jpg", "30-40 MIN", "Fast Food");
        searchedRestaurantslist.add(restaurantsModel);

        searchRVadapter = new RestaurantsRVAdapter(activity, searchedRestaurantslist);
        rvSearchedRestaurants.setAdapter(searchRVadapter);
    }

    private void initCategories() {
        searchCategoriesModel = new SearchCategoriesModel("1", "https://www.bbcgoodfood.com/sites/default/files/styles/category_retina/public/recipe-collections/collection-image/2013/05/spinach-sweet-potato-samosas.jpg?itok=7HrGeSyW", "Vegetarian");
        searchCategoriesModelArrayList.add(searchCategoriesModel);
        searchCategoriesModel = new SearchCategoriesModel("1", "https://ediblebajaarizona.com/wp-content/uploads/2017/07/ak-4-300x300.jpg", "Desert");
        searchCategoriesModelArrayList.add(searchCategoriesModel);
        searchCategoriesModel = new SearchCategoriesModel("1", "http://lh5.googleusercontent.com/-XMpDp3DRo_0/VHihX4MYnYI/AAAAAAAAAFU/EvbzYI_3D-k/s1600/beverages.jpg", "beverages");
        searchCategoriesModelArrayList.add(searchCategoriesModel);
        searchCategoriesModel = new SearchCategoriesModel("1", "https://assets.londonist.com/uploads/2017/04/i875/longevity_noodles-_image_by_chinatown_www-chinatown-co-uk.jpg", "Chinese");
        searchCategoriesModelArrayList.add(searchCategoriesModel);
        searchCategoriesModel = new SearchCategoriesModel("1", "https://michiganavemag.com/get/files/image/migration/9007_content_Italian-Cuisine-Chicago-5.jpg", "Italian");
        searchCategoriesModelArrayList.add(searchCategoriesModel);

        searchCategoriesModel = new SearchCategoriesModel("1", "https://www.bbcgoodfood.com/sites/default/files/styles/category_retina/public/recipe-collections/collection-image/2013/05/spinach-sweet-potato-samosas.jpg?itok=7HrGeSyW", "Vegetarian");
        searchCategoriesModelArrayList.add(searchCategoriesModel);
        searchCategoriesModel = new SearchCategoriesModel("1", "https://ediblebajaarizona.com/wp-content/uploads/2017/07/ak-4-300x300.jpg", "Desert");
        searchCategoriesModelArrayList.add(searchCategoriesModel);
        searchCategoriesModel = new SearchCategoriesModel("1", "http://lh5.googleusercontent.com/-XMpDp3DRo_0/VHihX4MYnYI/AAAAAAAAAFU/EvbzYI_3D-k/s1600/beverages.jpg", "beverages");
        searchCategoriesModelArrayList.add(searchCategoriesModel);
        searchCategoriesModel = new SearchCategoriesModel("1", "https://assets.londonist.com/uploads/2017/04/i875/longevity_noodles-_image_by_chinatown_www-chinatown-co-uk.jpg", "Chinese");
        searchCategoriesModelArrayList.add(searchCategoriesModel);
        searchCategoriesModel = new SearchCategoriesModel("1", "https://michiganavemag.com/get/files/image/migration/9007_content_Italian-Cuisine-Chicago-5.jpg", "Italian");
        searchCategoriesModelArrayList.add(searchCategoriesModel);

        searchCategoriesModel = new SearchCategoriesModel("1", "https://www.bbcgoodfood.com/sites/default/files/styles/category_retina/public/recipe-collections/collection-image/2013/05/spinach-sweet-potato-samosas.jpg?itok=7HrGeSyW", "Vegetarian");
        searchCategoriesModelArrayList.add(searchCategoriesModel);
        searchCategoriesModel = new SearchCategoriesModel("1", "https://ediblebajaarizona.com/wp-content/uploads/2017/07/ak-4-300x300.jpg", "Desert");
        searchCategoriesModelArrayList.add(searchCategoriesModel);
        searchCategoriesModel = new SearchCategoriesModel("1", "http://lh5.googleusercontent.com/-XMpDp3DRo_0/VHihX4MYnYI/AAAAAAAAAFU/EvbzYI_3D-k/s1600/beverages.jpg", "beverages");
        searchCategoriesModelArrayList.add(searchCategoriesModel);
        searchCategoriesModel = new SearchCategoriesModel("1", "https://assets.londonist.com/uploads/2017/04/i875/longevity_noodles-_image_by_chinatown_www-chinatown-co-uk.jpg", "Chinese");
        searchCategoriesModelArrayList.add(searchCategoriesModel);
        searchCategoriesModel = new SearchCategoriesModel("1", "https://michiganavemag.com/get/files/image/migration/9007_content_Italian-Cuisine-Chicago-5.jpg", "Italian");
        searchCategoriesModelArrayList.add(searchCategoriesModel);

        gridViewAdapter = new SearchCategoriesAdapter(getContext(), searchCategoriesModelArrayList);
        gvSearchCategories.setAdapter(gridViewAdapter);
    }
}