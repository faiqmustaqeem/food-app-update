package com.codiansoft.foodapp;

import com.codiansoft.foodapp.model.SelectedFoodModel;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by Codiansoft on 8/22/2017.
 */

public class GlobalClass {
    public static final String SIGNUP_URL = "http://codiansoft.com/FoodApp/api/register";
    public static final String LOGIN_URL = "http://codiansoft.com/FoodApp/api/login";
    public static final String MORE_RESTAURANTS_URL = "http://codiansoft.com/FoodApp/api/restaurantList";
    public static String basketRestaurantName = "", basketRestaurantDescription = "";
    public static String restaurantServiceType;
    public static ArrayList<SelectedFoodModel> selectedFoodListInBasket = new ArrayList<SelectedFoodModel>();
    public static String restaurantInBasket = "";
    public static String selectedRestaurantID = "";
}
