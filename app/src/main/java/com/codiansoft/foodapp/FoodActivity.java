package com.codiansoft.foodapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.codiansoft.foodapp.adapter.FoodAdapter;
import com.codiansoft.foodapp.adapter.ViewPagerAdapter;
import com.codiansoft.foodapp.model.FoodModel;
import com.codiansoft.foodapp.model.FragmentOneDataModel;
import com.codiansoft.foodapp.model.SelectedFoodModel;
import com.codiansoft.foodapp.model.TabTimesModel;
import com.google.common.collect.Lists;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.codiansoft.foodapp.fragment.RestaurantFragmentOne.fragOneItems;

public class FoodActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvAdd, tvSubtract;
    TextView tvFoodName, tvQuantity, tvTotal, tvFoodDescription;
    ImageView ivFoodImage;
    ConstraintLayout clAddToCart;

    RecyclerView rvFoodChoices;
    ArrayList<FoodModel> foodsChoices;
    public static SelectedFoodModel selectedFoodModel;
    public static String selectedFoodChoicesString = "";
    FoodModel foodModel;
    private FoodAdapter adapter;
    String foodID, foodTitle, foodDescription, foodPrice, foodImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        getSupportActionBar().hide();

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                foodID = null;
                foodTitle = null;
                foodDescription = null;
                foodPrice = null;
                foodImage = null;
            } else {
                foodID = extras.getString("foodID");
                foodTitle = extras.getString("foodTitle");
                foodDescription = extras.getString("foodDescription");
                foodPrice = extras.getString("foodPrice");
                foodImage = extras.getString("foodImage");
            }
        } else
            {
            foodID = (String) savedInstanceState.getSerializable("foodID");
            foodTitle = (String) savedInstanceState.getSerializable("foodTitle");
            foodDescription = (String) savedInstanceState.getSerializable("foodDescription");
            foodPrice = (String) savedInstanceState.getSerializable("foodPrice");
            foodImage = (String) savedInstanceState.getSerializable("foodImage");
        }

        initUI();
        initChoices();
    }

    private void initChoices()
    {
        Log.e("choices", "api _secret "+GlobalClass.api_secret);
        Log.e("choices", "selected_restaurant "+GlobalClass.selectedRestaurantID);
        Log.e("choices", "item_id "+GlobalClass.selectedRestaurantItemId);
        Log.e("choices", "branch+id "+GlobalClass.selectedRestaurantBranchID);
        Log.e("choices",  "variation "+GlobalClass.variation);


        foodsChoices = new ArrayList<FoodModel>();

        //parameters constructor  FoodModel(String sectionTitle, String sectionCategory, boolean isRequired)
//        foodModel = new FoodModel("Drinks", "single", false);
//        foodsChoices.add(foodModel);
//        Constructor parameters FoodModel(String foodItemID, String title, String price, String category)
//        foodModel = new FoodModel("1", "Pepsi", "0", "single");
//        foodsChoices.add(foodModel);
//        foodModel = new FoodModel("2", "Sprite", "0", "single");
//        foodsChoices.add(foodModel);
//        foodModel = new FoodModel("3", "Mountain Dew", "0", "single");
//        foodsChoices.add(foodModel);
//        foodModel = new FoodModel("3", "pakola", "0", "single");
//        foodsChoices.add(foodModel);
//
//        foodModel = new FoodModel("Side choices", "multiple", true);
//        foodsChoices.add(foodModel);
//
//        foodModel = new FoodModel("1", "Bottled Water", "10", "multiple");
//        foodsChoices.add(foodModel);
//        foodModel = new FoodModel("2", "Chocolate Cookie", "11", "multiple");
//        foodsChoices.add(foodModel);
//        foodModel = new FoodModel("3", "Chicken Teriyaki Salad ", "12", "multiple");
//        foodsChoices.add(foodModel);
//



        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.ITEM_CHOICES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONObject Jobject = new JSONObject(response);
                            JSONObject result = Jobject.getJSONObject("result");

                            if (result.getString("status").equals("success")) {


                                JSONArray variationArray;
                                if(GlobalClass.variation.equals("1"))
                                {
                                    variationArray=result.getJSONArray("item_variations");
                                    foodModel = new FoodModel("Variation", "variation", true);
                                    foodsChoices.add(foodModel);
                                    for(int i=0 ; i < variationArray.length() ; i++)
                                    {
                                        JSONObject oneVariation=variationArray.getJSONObject(i);
                                        foodModel = new FoodModel(oneVariation.getString("item_id"), oneVariation.getString("item_name")+" ( "+oneVariation.getString("variation")+" )", oneVariation.getString("price"), "variation");
                                        foodsChoices.add(foodModel);

                                    }

                                }

                                JSONArray drinksArray=result.getJSONArray("drinks");
                                foodModel = new FoodModel("Drinks", "single", false);
                                foodsChoices.add(foodModel);
                                for (int i =0 ; i < drinksArray.length() ; i++ )
                                {
                                  JSONObject oneDrink =  drinksArray.getJSONObject(i);
                                    //  Constructor parameters FoodModel(String foodItemID, String title, String price, String category)
                                    foodModel = new FoodModel(oneDrink.getString("id"), oneDrink.getString("drinks"), oneDrink.getString("price"), "single");
                                    foodsChoices.add(foodModel);
                                }


                                JSONArray sideChoiceArray=result.getJSONArray("side_choice");
                                foodModel = new FoodModel("Side choices", "multiple", true);
                                foodsChoices.add(foodModel);

                                for(int i=0 ; i < sideChoiceArray.length() ; i++)
                                {
                                    JSONObject one_side_choice=sideChoiceArray.getJSONObject(i);
                                    foodModel = new FoodModel(one_side_choice.getString("id"), one_side_choice.getString("choice"), one_side_choice.getString("price"), "multiple");
                                    foodsChoices.add(foodModel);

                                }



                                adapter = new FoodAdapter(FoodActivity.this, foodsChoices);
                                rvFoodChoices.setAdapter(adapter);



                            }

                        } catch (Exception ee) {
                            Toast.makeText(FoodActivity.this, "" + ee.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(FoodActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
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



                params.put("api_secret", GlobalClass.api_secret);
                params.put("item_id", GlobalClass.selectedRestaurantItemId);
                params.put("restaurant_id", GlobalClass.selectedRestaurantID);
                params.put("branch_id", GlobalClass.selectedRestaurantBranchID);
                params.put("variation",GlobalClass.variation);


                return params;
            }
        };
        queue.add(postRequest);

    }

    private void initUI() {
        ivFoodImage = (ImageView) findViewById(R.id.ivFoodImage);
        tvFoodName = (TextView) findViewById(R.id.tvFoodName);
        tvFoodDescription = (TextView) findViewById(R.id.tvFoodDescription);

        tvTotal = (TextView) findViewById(R.id.tvTotal);
        tvAdd = (TextView) findViewById(R.id.tvAdd);
        tvAdd.setOnClickListener(this);
        tvSubtract = (TextView) findViewById(R.id.tvSubtract);
        tvSubtract.setOnClickListener(this);
        tvQuantity = (TextView) findViewById(R.id.tvQuantity);
        clAddToCart = (ConstraintLayout) findViewById(R.id.clAddToCart);
        clAddToCart.setOnClickListener(this);
        tvQuantity.setText("1");
        tvFoodName.setText(foodTitle);
        tvFoodDescription.setText(foodDescription);
        tvTotal.setText(foodPrice);
        Glide.with(this).load(foodImage).centerCrop().into(ivFoodImage);

        rvFoodChoices = (RecyclerView) findViewById(R.id.rvFoodChoices);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        rvFoodChoices.setLayoutManager(layoutManager);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvAdd:
                tvQuantity.setText((Integer.parseInt(tvQuantity.getText().toString()) + 1) + "");
                tvSubtract.setClickable(true);
                tvTotal.setText((Integer.parseInt(tvQuantity.getText().toString()) * Double.parseDouble(tvTotal.getText().toString())) + "");
                break;
            case R.id.tvSubtract:
                if (Integer.parseInt(tvQuantity.getText().toString()) > 1)
                    tvQuantity.setText((Integer.parseInt(tvQuantity.getText().toString()) - 1) + "");
                if (tvQuantity.getText().toString().equals("1")) tvSubtract.setClickable(false);
                break;
            case R.id.clAddToCart:
                if (GlobalClass.selectedFoodListInBasket.size() != 0) {
                    selectedFoodModel = new SelectedFoodModel(foodModel.getFoodItemID(), tvFoodName.getText().toString(), tvTotal.getText().toString(), tvQuantity.getText().toString(), selectedFoodChoicesString);

                    GlobalClass.restaurantInBasket = "";
                    GlobalClass.selectedFoodListInBasket.add(selectedFoodModel);

                    Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show();
                    finish();
                    selectedFoodChoicesString = "";
                } else {
                    android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(FoodActivity.this);
                    adb.setTitle("You can only order items from one menu at a strTime");
                    adb.setMessage("Clear your basket if you'd still like to order this item");
                    adb.setNegativeButton("CANCEL", null);
                    adb.setPositiveButton("CLEAR BASKET AND ADD THIS ITEM", new android.app.AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            GlobalClass.basketRestaurantName = "";
                            GlobalClass.basketRestaurantDescription = "";
                            GlobalClass.selectedFoodListInBasket.clear();

                            selectedFoodModel = new SelectedFoodModel(foodModel.getFoodItemID(), tvFoodName.getText().toString(), tvTotal.getText().toString(), tvQuantity.getText().toString(), selectedFoodChoicesString);
                            GlobalClass.selectedFoodListInBasket.add(selectedFoodModel);

                            Toast.makeText(FoodActivity.this, "Added to cart", Toast.LENGTH_SHORT).show();
                            finish();
                            selectedFoodChoicesString = "";
                        }
                    });
                    adb.show();
                }
                break;
        }
    }
}