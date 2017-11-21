package com.codiansoft.foodapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codiansoft.foodapp.adapter.BasketFoodItemsAdapter;

public class ConfirmOrderActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView rvBasketFoodItems;
    private BasketFoodItemsAdapter mAdapter;
    TextView tvSubtotal, tvTax, tvBookingFee, tvTotal, tvRestaurantName, tvRestaurantDesc, tvAdditionalNotes;
    ImageView ivBack;
    Button bProceed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_confirm_order);
        getSupportActionBar().hide();

        initUI();
        bProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConfirmOrderActivity.this, CheckoutActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initUI() {
        tvAdditionalNotes = (TextView) findViewById(R.id.tvAdditionalNotes);
        bProceed = (Button) findViewById(R.id.bProceed);
        tvRestaurantName = (TextView) findViewById(R.id.tvRestaurantName);
        tvRestaurantDesc = (TextView) findViewById(R.id.tvRestaurantDesc);
        tvRestaurantName.setText(GlobalClass.basketRestaurantName);
        tvRestaurantDesc.setText(GlobalClass.basketRestaurantDescription);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        ivBack.setOnClickListener(this);
        tvSubtotal = (TextView) findViewById(R.id.tvSubtotal);
        tvTax = (TextView) findViewById(R.id.tvTax);
        tvBookingFee = (TextView) findViewById(R.id.tvBookingFee);
        tvTotal = (TextView) findViewById(R.id.tvTotal);
        rvBasketFoodItems = (RecyclerView) findViewById(R.id.rvBasketFoodItems);
        mAdapter = new BasketFoodItemsAdapter();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvBasketFoodItems.setLayoutManager(mLayoutManager);
        rvBasketFoodItems.setItemAnimator(new DefaultItemAnimator());
        rvBasketFoodItems.setAdapter(mAdapter);

        tvAdditionalNotes.setText(GlobalClass.basketExtraNotes);
        tvRestaurantName.setText(GlobalClass.basketRestaurantName);
        tvRestaurantDesc.setText(GlobalClass.basketRestaurantDescription);

        calculateSubtotal();
        calculateTotal();
    }

    private void calculateTotal() {
        double total = 0;
        total = total + Double.parseDouble(tvSubtotal.getText().toString()) + Double.parseDouble(tvTax.getText().toString()) + Double.parseDouble(tvBookingFee.getText().toString());
        tvTotal.setText(total + "");
        if (tvSubtotal.getText().toString().equals("0.0") | tvSubtotal.getText().toString().equals("0.00"))
            tvTotal.setText("0");
    }

    private void calculateSubtotal() {
        double subtotal = 0;
        for (int i = 0; i < GlobalClass.selectedFoodListInBasket.size(); i++) {
            subtotal = subtotal + Double.parseDouble(GlobalClass.selectedFoodListInBasket.get(i).getPrice());
        }
        tvSubtotal.setText(subtotal + "");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.bProceed:
                Intent intent = new Intent(ConfirmOrderActivity.this, ThankYouActivity.class);
                startActivity(intent);
                break;
        }
    }
}