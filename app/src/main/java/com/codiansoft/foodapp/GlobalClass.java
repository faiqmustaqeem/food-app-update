package com.codiansoft.foodapp;

import com.codiansoft.foodapp.model.SelectedFoodModel;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by Codiansoft on 8/22/2017.
 */

public class GlobalClass {
    public static final String BASE_URL="http://admiria.pk/Food/api/";
    public static final String SIGNUP_URL = BASE_URL+"register";
    public static final String LOGIN_URL = BASE_URL+"login";
    public static final String MORE_RESTAURANTS_URL = BASE_URL+"restaurantList";
    public static final String RESTAURANT_MENU_URL = BASE_URL+"restaurantMenu_list4";
    public static final String MOST_POPULAR_RESTAURANTS_URL = BASE_URL+"most_popular";
    public static final String ALL_SLIDERS_RESTAURANTS_URL = BASE_URL+"homeScreen";
    public static final String FETCH_USER_DETAILS_URL = BASE_URL+"user_account";
    public static final String SERVICES_RESTAURANTS_URL = BASE_URL+"restaurant_services";
    public static final String UPDATE_PROFILE=BASE_URL+"edit_user_account";
    public static final String SEARCH_RESTAURANT=BASE_URL+"search_homeScreen";
    public static final String CATEGORIES=BASE_URL+"search_via_categories";
    public static final String SEARCH_RESTAURANT_VIA_CATEGIES_RESULT=BASE_URL+"search_via_categories_result";
   public static final String FETCH_COUNTRIES=BASE_URL+"fetchCountries";
    public static final String FETCH_STATES=BASE_URL+"fetchStates";
    public static final String FETCH_CITIES=BASE_URL+"fetchCities";
    public static final String ITEM_CHOICES=BASE_URL+"item_choices";
    public static final String CHECK_PASSWORD=BASE_URL+"confirm_pass";
    public static final String FETCH_TABLES=BASE_URL+"restaurants_tables";
    public static final String TABLE_RESERVATION=BASE_URL+"table_reservation";


    public static String basketRestaurantName = "", basketRestaurantDescription = "";
    public static String basketExtraNotes;
    public static String restaurantServiceType;
    public static ArrayList<SelectedFoodModel> selectedFoodListInBasket = new ArrayList<SelectedFoodModel>();
    public static String restaurantInBasket = "";

    public static String selectedRestaurantID = "";
    public static String selectedRestaurantBranchID = "";
    public static String api_secret="";
    public static String selectedRestaurantItemId="";
    public static String variation ="";
    public static String table_id="";
    public static String table_number="";

}
