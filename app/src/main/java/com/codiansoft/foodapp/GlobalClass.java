package com.codiansoft.foodapp;

import com.codiansoft.foodapp.model.SelectedFoodModel;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by Codiansoft on 8/22/2017.
 */

public class GlobalClass {
    public static final String SIGNUP_URL = "http://codiansoft.com/FoodApp_update/api/register";
    public static final String LOGIN_URL = "http://codiansoft.com/FoodApp_update/api/login";
    public static final String MORE_RESTAURANTS_URL = "http://codiansoft.com/FoodApp_update/api/restaurantList";
    public static final String RESTAURANT_MENU_URL = "http://codiansoft.com/FoodApp_update/api/restaurantMenu_list4";
    public static final String MOST_POPULAR_RESTAURANTS_URL = "http://codiansoft.com/FoodApp_update/api/most_popular";
    public static final String ALL_SLIDERS_RESTAURANTS_URL = "http://codiansoft.com/FoodApp_update/api/homeScreen";
    public static final String FETCH_USER_DETAILS_URL = "http://codiansoft.com/FoodApp_update/api/user_account";
    public static final String SERVICES_RESTAURANTS_URL = "http://codiansoft.com/FoodApp_update/api/restaurant_services";
    public static final String UPDATE_PROFILE="http://codiansoft.com/FoodApp_update/api/edit_user_account";
    public static final String SEARCH_RESTAURANT="http://codiansoft.com/FoodApp_update/api/search_homeScreen";
    public static final String CATEGORIES="http://codiansoft.com/FoodApp_update/api/search_via_categories";
    public static final String SEARCH_RESTAURANT_VIA_CATEGIES_RESULT="http://codiansoft.com/FoodApp_update/api/search_via_categories_result";
   public static final String FETCH_COUNTRIES="http://codiansoft.com/FoodApp_update/api/fetchCountries";
    public static final String FETCH_STATES="http://codiansoft.com/FoodApp_update/api/fetchStates";
    public static final String FETCH_CITIES="http://codiansoft.com/FoodApp_update/api/fetchCities";
    public static final String ITEM_CHOICES="http://codiansoft.com/FoodApp_update/api/item_choices";
    public static final String CHECK_PASSWORD="http://codiansoft.com/FoodApp_update/api/confirm_pass";
    public static final String FETCH_TABLES="http://codiansoft.com/FoodApp_update/api/restaurants_tables";
    public static final String TABLE_RESERVATION="http://codiansoft.com/FoodApp_update/api/table_reservation";


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
