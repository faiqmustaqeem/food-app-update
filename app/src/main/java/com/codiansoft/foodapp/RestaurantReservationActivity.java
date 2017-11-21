package com.codiansoft.foodapp;

import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codiansoft.foodapp.adapter.ReservationTableAdapter;
import com.codiansoft.foodapp.dialog.ReserveTableDialog;
import com.codiansoft.foodapp.model.ReservationTableModel;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;

public class RestaurantReservationActivity extends AppCompatActivity implements ObservableScrollViewCallbacks, View.OnClickListener {
    ObservableListView listView;
    NestedScrollView scrollView;
    public static ImageView mImageView;
    public static CardView cvRestaurantDetails;
    RecyclerView rvTables;

    TextView tvViewCart, tvRestaurantName, tvRestaurantDescription, tvBasketItemsQuantity;
    LinearLayout llTopBar;
    ImageView ivShare, ivBack, ivTopBarBack, ivTopBarShare;

    public static String restaurantID, restaurantTitle, restaurantDescription, restaurantDuration, restaurantImage;

    ReservationTableAdapter mAdapter;
    ArrayList<ReservationTableModel> reservationTablesList = new ArrayList<ReservationTableModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_reservation_restaurants);
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                restaurantID = null;
                restaurantTitle = null;
                restaurantDuration = null;
                restaurantImage = null;
                restaurantDescription = null;
            } else {
                restaurantID = extras.getString("restaurantID");
                restaurantTitle = extras.getString("restaurantTitle");
                restaurantDuration = extras.getString("restaurantDuration");
                restaurantImage = extras.getString("restaurantImage");
                restaurantDescription = extras.getString("restaurantDescription");
            }
        } else {
            restaurantID = (String) savedInstanceState.getSerializable("restaurantID");
            restaurantTitle = (String) savedInstanceState.getSerializable("restaurantTitle");
            restaurantDuration = (String) savedInstanceState.getSerializable("restaurantDuration");
            restaurantImage = (String) savedInstanceState.getSerializable("restaurantImage");
            restaurantDescription = (String) savedInstanceState.getSerializable("restaurantDescription");
        }

        getSupportActionBar().hide();
        initUI();
        fetchTables();

        ArrayList<String> items = new ArrayList<String>();
        for (int i = 1; i <= 100; i++) {
            items.add("Item " + i);
        }
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items));


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ivTopBarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        rvTables.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), rvTables, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                /*ReserveTableDialog d = new ReserveTableDialog(RestaurantReservationActivity.this, reservationTablesList.get(position).getID(), reservationTablesList.get(position).getTitle(), reservationTablesList.get(position).getCapacity());
                d.show();*/
                Intent intent = new Intent(RestaurantReservationActivity.this, ReserveTableActivity.class);
                startActivity(intent);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void fetchTables() {
        reservationTablesList.add(new ReservationTableModel("1", "Table - 1", "1", "7"));
        reservationTablesList.add(new ReservationTableModel("2", "Table - 2", "0", "2"));
        reservationTablesList.add(new ReservationTableModel("3", "Table - 3", "1", "10"));
        reservationTablesList.add(new ReservationTableModel("4", "Table - 4", "0", "7"));
        reservationTablesList.add(new ReservationTableModel("5", "Table - 5", "0", "7"));
        mAdapter = new ReservationTableAdapter(this, reservationTablesList);
        rvTables.setAdapter(mAdapter);
    }

    private void initUI() {
        rvTables = (RecyclerView) findViewById(R.id.rvTables);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvTables.setLayoutManager(mLayoutManager);
        rvTables.setItemAnimator(new DefaultItemAnimator());


        tvBasketItemsQuantity = (TextView) findViewById(R.id.tvBasketItemsQuantity);
        tvRestaurantName = (TextView) findViewById(R.id.tvRestaurantName);
        tvRestaurantName.setText(restaurantTitle);

        tvRestaurantDescription = (TextView) findViewById(R.id.tvRestaurantDescription);
        tvRestaurantDescription.setText(restaurantDescription);
        llTopBar = (LinearLayout) findViewById(R.id.llTopBar);
        llTopBar.bringToFront();
        ivTopBarShare = (ImageView) findViewById(R.id.ivTopBarShare);
        ivTopBarShare.setOnClickListener(this);
        ivShare = (ImageView) findViewById(R.id.ivShare);
        ivShare.bringToFront();
        ivShare.setOnClickListener(this);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        ivBack.bringToFront();
        listView = (ObservableListView) findViewById(R.id.list);
        scrollView = (NestedScrollView) findViewById(R.id.scrollView);
        ivTopBarBack = (ImageView) findViewById(R.id.ivTopBarBack);
        ivTopBarShare = (ImageView) findViewById(R.id.ivTopBarShare);
        mImageView = (ImageView) findViewById(R.id.image);
        cvRestaurantDetails = (CardView) findViewById(R.id.cvRestaurantDetails);
        Glide.with(this).load(restaurantImage).into(mImageView);
        listView.setScrollViewCallbacks(this);

        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                ViewHelper.setTranslationY(mImageView, scrollY / -8);
                ViewHelper.setTranslationY(cvRestaurantDetails, scrollY / -2);
//                ViewHelper.setTranslationY(viewPager, scrollY / -2);
                if (scrollY > oldScrollY) {
//                    Log.i(TAG, "Scroll DOWN");
                    llTopBar.setVisibility(View.VISIBLE);
                }
                if (scrollY < oldScrollY) {
//                    Log.i(TAG, "Scroll UP");
                }
                if (scrollY == 0) {
//                    Log.i(TAG, "TOP SCROLL");
                    llTopBar.setVisibility(View.GONE);
                }
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
//                    Log.i(TAG, "BOTTOM SCROLL");
                }
            }

        });
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
/*        ViewHelper.setTranslationY(mImageView, scrollY / -2);
        ViewHelper.setTranslationY(cvRestaurantDetails, scrollY / -2);
        ViewHelper.setTranslationY(tabLayout, scrollY / -2);
        ViewHelper.setTranslationY(viewPager, scrollY / -2);*/
//        viewPager.getLayoutParams().height = viewPager.getHeight() + 100;
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivShare:
            case R.id.ivTopBarShare:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share this Restaurant"));
                break;
        }
    }
}