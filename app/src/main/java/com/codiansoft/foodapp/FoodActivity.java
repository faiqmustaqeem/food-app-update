package com.codiansoft.foodapp;

import android.content.DialogInterface;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codiansoft.foodapp.adapter.FoodAdapter;
import com.codiansoft.foodapp.model.FoodModel;
import com.codiansoft.foodapp.model.SelectedFoodModel;

import java.util.ArrayList;

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
        } else {
            foodID = (String) savedInstanceState.getSerializable("foodID");
            foodTitle = (String) savedInstanceState.getSerializable("foodTitle");
            foodDescription = (String) savedInstanceState.getSerializable("foodDescription");
            foodPrice = (String) savedInstanceState.getSerializable("foodPrice");
            foodImage = (String) savedInstanceState.getSerializable("foodImage");
        }

        initUI();
        initChoices();
    }

    private void initChoices() {
        foodsChoices = new ArrayList<FoodModel>();

        foodModel = new FoodModel("Drinks", "single", false);
        foodsChoices.add(foodModel);

        foodModel = new FoodModel("1", "Pepsi", "0", "single");
        foodsChoices.add(foodModel);
        foodModel = new FoodModel("2", "Sprite", "0", "single");
        foodsChoices.add(foodModel);
        foodModel = new FoodModel("3", "Mountain Dew", "0", "single");
        foodsChoices.add(foodModel);
        foodModel = new FoodModel("3", "Coca Cola", "0", "single");
        foodsChoices.add(foodModel);

        foodModel = new FoodModel("Side choices", "multiple", true);
        foodsChoices.add(foodModel);

        foodModel = new FoodModel("1", "Bottled Water", "10", "multiple");
        foodsChoices.add(foodModel);
        foodModel = new FoodModel("2", "Chocolate Cookie", "11", "multiple");
        foodsChoices.add(foodModel);
        foodModel = new FoodModel("3", "Chicken Teriyaki Salad ", "12", "multiple");
        foodsChoices.add(foodModel);

        adapter = new FoodAdapter(this, foodsChoices);
        rvFoodChoices.setAdapter(adapter);
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
                    adb.setTitle("You can only order items from one menu at a time");
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