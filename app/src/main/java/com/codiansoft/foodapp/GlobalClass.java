package com.codiansoft.foodapp;

import com.codiansoft.foodapp.model.SelectedFoodModel;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by Codiansoft on 8/22/2017.
 */

public class GlobalClass {
    public static final String SIGNUP_URL = "http://codiansoft.com/FoodApp/api/register";
    public static final String LOGIN_URL = "http://codiansoft.com/FoodApp_update/api/login";
    public static final String MORE_RESTAURANTS_URL = "http://codiansoft.com/FoodApp_update/api/restaurantList";
    public static final String RESTAURANT_MENU_URL = "http://codiansoft.com/FoodApp_update/api/restaurantMenu_list";
    public static final String MOST_POPULAR_RESTAURANTS_URL = "http://codiansoft.com/FoodApp_update/api/most_popular";
    public static final String ALL_SLIDERS_RESTAURANTS_URL = "http://codiansoft.com/FoodApp_update/api/homeScreen";
    public static final String FETCH_USER_DETAILS_URL = "http://codiansoft.com/FoodApp_update/api/user_account";
    public static final String SERVICES_RESTAURANTS_URL = "http://codiansoft.com/FoodApp_update/api/restaurant_services";
    public static String basketRestaurantName = "", basketRestaurantDescription = "";
    public static String basketExtraNotes;
    public static String restaurantServiceType;
    public static ArrayList<SelectedFoodModel> selectedFoodListInBasket = new ArrayList<SelectedFoodModel>();
    public static String restaurantInBasket = "";
    public static String selectedRestaurantID = "";
    public static String selectedRestaurantBranchID = "";
}
