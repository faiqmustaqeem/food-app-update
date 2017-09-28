package com.codiansoft.foodapp;

import android.content.DialogInterface;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class BasketActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {
    private GoogleMap mMap;
    RecyclerView rvBasketFoodItems;
    private BasketFoodItemsAdapter mAdapter;
    TextView tvSubtotal, tvTax, tvBookingFee, tvTotal, tvRestaurantName, tvRestaurantDesc;
    ImageView ivBack;
    GoogleMap myGoogleMap;
    Button bOrderNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_basket);
        getSupportActionBar().hide();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                GlobalClass.basketRestaurantName = null;
                GlobalClass.basketRestaurantDescription = null;
            } else {
                GlobalClass.basketRestaurantName = extras.getString("basketRestaurantName");
                GlobalClass.basketRestaurantDescription = extras.getString("basketRestaurantDescription");
            }
        } else {
            GlobalClass.basketRestaurantName = (String) savedInstanceState.getSerializable("basketRestaurantName");
            GlobalClass.basketRestaurantDescription = (String) savedInstanceState.getSerializable("basketRestaurantDescription");
        }

        initUI();
        bOrderNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BasketActivity.this, CheckoutActivity.class);
                startActivity(intent);
            }
        });
        rvBasketFoodItems.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(BasketActivity.this);
                adb.setTitle("Remove from Basket?");
                final int positionToRemove = position;
                adb.setNegativeButton("No", null);
                adb.setPositiveButton("Yes", new android.app.AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        GlobalClass.selectedFoodListInBasket.remove(position);
                        mAdapter.notifyDataSetChanged();

                        if (GlobalClass.selectedFoodListInBasket.size() == 0) {
                            tvSubtotal.setText("0.00");
                            tvTotal.setText("0.00");
                        } else {
                            calculateSubtotal();
                            calculateTotal();
                        }
                    }
                });
                adb.show();
            }
        }));
    }

    private void initUI() {
        bOrderNow = (Button) findViewById(R.id.bOrderNow);
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
    public void onMapReady(GoogleMap googleMap) {
        myGoogleMap = googleMap;
        setRestaurantLocation(0.0, 0.0);
    }

    private void setRestaurantLocation(double lat, double lng) {
        // Add a marker in Sydney, Australia, and move the camera.
//        LatLng restaurantLocation = new LatLng(-33.852, 151.211);
        LatLng restaurantLocation = new LatLng(lat, lat);
        myGoogleMap.addMarker(new MarkerOptions().position(restaurantLocation).title(GlobalClass.basketRestaurantName));
        myGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(restaurantLocation));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
        }
    }
}