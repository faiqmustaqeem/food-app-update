package com.codiansoft.foodapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codiansoft.foodapp.fragment.FriendsFragment;
import com.codiansoft.foodapp.fragment.HomeFragment;
import com.codiansoft.foodapp.fragment.MyOrdersFragment;
import com.codiansoft.foodapp.fragment.SearchFragment;
import com.codiansoft.foodapp.fragment.ServiceRestaurantsFragment;
import com.codiansoft.foodapp.fragment.UserSettingsFragment;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnSuccessListener;

import static com.codiansoft.foodapp.fragment.SearchFragment.gvSearchCategories;
import static com.codiansoft.foodapp.fragment.SearchFragment.rvSearchedRestaurants;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    ImageView ivOptions, ivHome, ivMeals, ivSearch, ivFriendsActivities, ivProfile, ivOrders;
    TextView tvHeader;
    ConstraintLayout clSlideDialog;
    LinearLayout llTopBar, llServices;
    Animation servicesSlideUp, servicesSlideDown, slideUp, slideDown;
    TextView tvServiceDelivery, tvServiceQuick, tvServiceTakeaway, tvServiceReservation;

    // for location
    private LocationCallback mLocationCallback;
    FusedLocationProviderClient mFusedLocationClient = null;
    LocationRequest mLocationRequest = null;
    boolean mRequestingLocationUpdates = true;
    protected GoogleApiClient mGoogleApiClient;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    public static double myLat;
    public static double myLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // make fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        initUI();

        //for location
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mRequestingLocationUpdates = false;
        buildGoogleApiClient();

        // for location on/off check
        locationSettingsRequest();

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        Fragment homeFragment = getSupportFragmentManager().findFragmentByTag(HomeFragment.TAG);

        if (savedInstanceState == null) {
            homeFragment = new HomeFragment();
            android.support.v4.app.FragmentTransaction ftHome = getSupportFragmentManager().beginTransaction();
            ftHome.add(R.id.container, homeFragment).commit();
        }

        servicesSlideDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                llServices.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            myLat = location.getLatitude();
                            myLng = location.getLongitude();
                            Toast.makeText(MainActivity.this, "last loc: " + location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...
                    Toast.makeText(MainActivity.this, "" + location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_SHORT).show();
                }
            }
        };

    }

    // for location on/off check
    private void locationSettingsRequest() {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    // for location
    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    // for location
    private void createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void initUI() {
        tvServiceDelivery = (TextView) findViewById(R.id.tvServiceDelivery);
        tvServiceQuick = (TextView) findViewById(R.id.tvServiceQuick);
        tvServiceTakeaway = (TextView) findViewById(R.id.tvServiceTakeaway);
        tvServiceReservation = (TextView) findViewById(R.id.tvServiceReservation);
        tvServiceDelivery.setOnClickListener(this);
        tvServiceQuick.setOnClickListener(this);
        tvServiceTakeaway.setOnClickListener(this);
        tvServiceReservation.setOnClickListener(this);

        slideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        slideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);

        servicesSlideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.services_slide_up);
        servicesSlideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.services_slide_down);

        clSlideDialog = (ConstraintLayout) findViewById(R.id.clSlideDialog);
        clSlideDialog.bringToFront();
        llTopBar = (LinearLayout) findViewById(R.id.llTopBar);
        llTopBar.bringToFront();
        llServices = (LinearLayout) findViewById(R.id.llServices);
        llServices.bringToFront();
        ivOrders = (ImageView) findViewById(R.id.ivOrders);
        ivProfile = (ImageView) findViewById(R.id.ivProfile);
        ivHome = (ImageView) findViewById(R.id.ivHome);
        ivMeals = (ImageView) findViewById(R.id.ivMeals);
        ivFriendsActivities = (ImageView) findViewById(R.id.ivFriendsActivities);
        ivSearch = (ImageView) findViewById(R.id.ivSearch);
        ivOptions = (ImageView) findViewById(R.id.ivOptions);
        tvHeader = (TextView) findViewById(R.id.tvHeader);
        tvHeader.bringToFront();

        ivProfile.setOnClickListener(this);
        ivHome.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
        ivMeals.setOnClickListener(this);
        ivOptions.setOnClickListener(this);
        ivFriendsActivities.setOnClickListener(this);
        ivOrders.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        if (llServices.getVisibility() == View.VISIBLE)
            llServices.startAnimation(servicesSlideDown);
        if (clSlideDialog.getVisibility() == View.VISIBLE) clSlideDialog.startAnimation(slideUp);

        switch (view.getId()) {
            case R.id.ivHome:
                ivMeals.setImageResource(R.drawable.ic_meals);

                ivHome.setColorFilter(ContextCompat.getColor(this, R.color.selected_icon_color));
                ivMeals.setColorFilter(null);
                ivFriendsActivities.setColorFilter(null);
                ivProfile.setColorFilter(null);
                ivOrders.setColorFilter(null);
                ivFriendsActivities.setColorFilter(null);

                Fragment homeFragment = fm.findFragmentByTag(HomeFragment.TAG);

                homeFragment = new HomeFragment();
                fm.beginTransaction()
                        .replace(R.id.container, homeFragment, HomeFragment.TAG)
//                        .addToBackStack(null)  // uncomment this line if you want to be able to return to the prev. fragment with "back" button
                        .commit();
                tvHeader.setText("Restaurants");

                break;
            case R.id.ivMeals:

                /*SearchFragment mealsFragment = (SearchFragment) fm.findFragmentByTag(SearchFragment.TAG);

                mealsFragment = new SearchFragment();
                fm.beginTransaction()
                        .replace(R.id.container, mealsFragment, SearchFragment.TAG)
//                        .addToBackStack(null)  // uncomment this line if you want to be able to return to the prev. fragment with "back" button
                        .commit();

                tvHeader.setText("Services");*/

                /*ServicesDialog servicesDialog = new ServicesDialog(this);
                servicesDialog.show();*/

/*
//Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(this, ivOptions, Gravity.RIGHT, R.attr.actionOverflowMenuStyle, 0);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.services_popup_menu, popup.getMenu());

                popup.getMenu().getItem(0).setIcon(R.drawable.ic_delivery);
                popup.getMenu().getItem(1).setIcon(R.drawable.ic_quick_service);
                popup.getMenu().getItem(2).setIcon(R.drawable.ic_takeaway);
                popup.getMenu().getItem(3).setIcon(R.drawable.ic_reservation);

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.service_delivery:

                                break;

                            case R.id.service_quick_service:

                                break;

                            case R.id.service_takeaway:

                                break;

                            case R.id.service_reservation:

                                break;
                        }
                        return true;
                    }
                });

                popup.show();//showing popup menu
                */

                if (llServices.getVisibility() == View.GONE) {
                    llServices.setVisibility(View.VISIBLE);
                    llServices.startAnimation(servicesSlideUp);

                } else {
                    llServices.startAnimation(servicesSlideDown);

                }

                break;
            case R.id.ivFriendsActivities:
                if (isLoggedInWithFB()) {
                    ivMeals.setImageResource(R.drawable.ic_meals);

                    ivFriendsActivities.setColorFilter(ContextCompat.getColor(this, R.color.selected_icon_color));
                    ivHome.setColorFilter(null);
                    ivMeals.setColorFilter(null);
                    ivFriendsActivities.setColorFilter(null);
                    ivProfile.setColorFilter(null);
                    ivOrders.setColorFilter(null);

                    Fragment friendsFragment = fm.findFragmentByTag(FriendsFragment.TAG);

                    friendsFragment = new FriendsFragment();
                    fm.beginTransaction()
                            .replace(R.id.container, friendsFragment, FriendsFragment.TAG)
//                        .addToBackStack(null)  // uncomment this line if you want to be able to return to the prev. fragment with "back" button
                            .commit();
                    tvHeader.setText("Friends Activities");

                } else if (isLoggedIn()) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Please log in with Facebook")
                            .setMessage("You must log in with Facebook to see friends' activities\nDo you want to sign out and log in with Facebook?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    SharedPreferences settings = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = settings.edit();
                                    editor.putString("apiSecretKey", "");
                                    editor.putString("userID", "");
                                    editor.commit();

                                    Intent logoutIntent = new Intent(MainActivity.this, LogInActivity.class);
                                    dialog.dismiss();
                                    ActivityCompat.finishAffinity(MainActivity.this);
                                    logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    MainActivity.this.startActivity(logoutIntent);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Whatever...
                                }
                            }).show();
                }








                break;
            case R.id.ivSearch:
                ivMeals.setImageResource(R.drawable.ic_meals);

                ivHome.setColorFilter(null);
                ivMeals.setColorFilter(null);
                ivProfile.setColorFilter(null);
                ivFriendsActivities.setColorFilter(null);
                ivOrders.setColorFilter(null);
                ivFriendsActivities.setColorFilter(null);

                Fragment searchFragment = fm.findFragmentByTag(SearchFragment.TAG);

                searchFragment = new SearchFragment();
                fm.beginTransaction()
                        .replace(R.id.container, searchFragment, SearchFragment.TAG)
                        .commit();

                tvHeader.setText("Search Restaurants");
                break;
            case R.id.ivOptions:
                /*MainOptionsDialog optionsDialog = new MainOptionsDialog(this);
                optionsDialog.show();*/

                if (clSlideDialog.getVisibility() == View.INVISIBLE) {
                    clSlideDialog.startAnimation(slideDown);
                    clSlideDialog.setVisibility(View.VISIBLE);
                } else {
                    clSlideDialog.startAnimation(slideUp);

                }
                slideUp.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        clSlideDialog.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                break;
            case R.id.tvServiceDelivery:
                GlobalClass.restaurantServiceType = "delivery";
                ivHome.setColorFilter(null);
                ivMeals.setColorFilter(ContextCompat.getColor(this, R.color.selected_icon_color));
                ivFriendsActivities.setColorFilter(null);
                ivProfile.setColorFilter(null);
                ivOrders.setColorFilter(null);
                ivFriendsActivities.setColorFilter(null);

                Fragment deliveryServiceRestaurantsFragment = fm.findFragmentByTag(ServiceRestaurantsFragment.TAG);

                deliveryServiceRestaurantsFragment = new ServiceRestaurantsFragment();
                fm.beginTransaction()
                        .replace(R.id.container, deliveryServiceRestaurantsFragment, ServiceRestaurantsFragment.TAG)
//                        .addToBackStack(null)  // uncomment this line if you want to be able to return to the prev. fragment with "back" button
                        .commit();
                tvHeader.setText("Delivery Restaurants");

                ivMeals.setBackgroundColor(Color.parseColor("#ffffff"));
                ivMeals.setImageResource(R.drawable.ic_delivery_large);

                break;
            case R.id.tvServiceQuick:
                GlobalClass.restaurantServiceType = "quick";
                ivHome.setColorFilter(null);
                ivMeals.setColorFilter(ContextCompat.getColor(this, R.color.selected_icon_color));
                ivFriendsActivities.setColorFilter(null);
                ivProfile.setColorFilter(null);
                ivOrders.setColorFilter(null);
                ivFriendsActivities.setColorFilter(null);

                Fragment quickServiceRestaurantsFragment = fm.findFragmentByTag(ServiceRestaurantsFragment.TAG);

                quickServiceRestaurantsFragment = new ServiceRestaurantsFragment();
                fm.beginTransaction()
                        .replace(R.id.container, quickServiceRestaurantsFragment, ServiceRestaurantsFragment.TAG)
//                        .addToBackStack(null)  // uncomment this line if you want to be able to return to the prev. fragment with "back" button
                        .commit();
                tvHeader.setText("Quick Service Restaurants");

                ivMeals.setBackgroundColor(Color.parseColor("#ffffff"));
                ivMeals.setImageResource(R.drawable.ic_quick_large);

                break;
            case R.id.tvServiceTakeaway:
                GlobalClass.restaurantServiceType = "takeaway";
                ivHome.setColorFilter(null);
                ivMeals.setColorFilter(ContextCompat.getColor(this, R.color.selected_icon_color));
                ivFriendsActivities.setColorFilter(null);
                ivProfile.setColorFilter(null);
                ivOrders.setColorFilter(null);
                ivFriendsActivities.setColorFilter(null);

                Fragment takeawayServiceRestaurantsFragment = fm.findFragmentByTag(ServiceRestaurantsFragment.TAG);

                takeawayServiceRestaurantsFragment = new ServiceRestaurantsFragment();
                fm.beginTransaction()
                        .replace(R.id.container, takeawayServiceRestaurantsFragment, ServiceRestaurantsFragment.TAG)
//                        .addToBackStack(null)  // uncomment this line if you want to be able to return to the prev. fragment with "back" button
                        .commit();
                tvHeader.setText("Takeaway Restaurants");

                ivMeals.setBackgroundColor(Color.parseColor("#ffffff"));
                ivMeals.setImageResource(R.drawable.ic_takeaway_large);

                break;
            case R.id.tvServiceReservation:
                GlobalClass.restaurantServiceType = "reservation";
                ivHome.setColorFilter(null);
                ivMeals.setColorFilter(ContextCompat.getColor(this, R.color.selected_icon_color));
                ivFriendsActivities.setColorFilter(null);
                ivProfile.setColorFilter(null);
                ivOrders.setColorFilter(null);
                ivFriendsActivities.setColorFilter(null);

                Fragment reservationServiceRestaurantsFragment = fm.findFragmentByTag(ServiceRestaurantsFragment.TAG);

                reservationServiceRestaurantsFragment = new ServiceRestaurantsFragment();
                fm.beginTransaction()
                        .replace(R.id.container, reservationServiceRestaurantsFragment, ServiceRestaurantsFragment.TAG)
//                        .addToBackStack(null)  // uncomment this line if you want to be able to return to the prev. fragment with "back" button
                        .commit();
                tvHeader.setText("Reservation Restaurants");

                ivMeals.setBackgroundColor(Color.parseColor("#ffffff"));
                ivMeals.setImageResource(R.drawable.ic_reservation_large);

                break;
            case R.id.ivProfile:
                ivMeals.setImageResource(R.drawable.ic_meals);

                ivHome.setColorFilter(null);
                ivMeals.setColorFilter(null);
                ivProfile.setColorFilter(ContextCompat.getColor(this, R.color.selected_icon_color));
                ivFriendsActivities.setColorFilter(null);
                ivOrders.setColorFilter(null);
                ivFriendsActivities.setColorFilter(null);

//                Fragment userSettingsFragment = fm.findFragmentByTag(UserSettingsFragment.TAG);

                Fragment userSettingsFragment = new UserSettingsFragment();
                fm.beginTransaction()
                        .replace(R.id.container, userSettingsFragment, UserSettingsFragment.TAG)
//                        .addToBackStack(null)  // uncomment this line if you want to be able to return to the prev. fragment with "back" button
                        .commit();

                tvHeader.setText("Chester Bennington");

                /*Intent accountIntent = new Intent(this, AccountActivity.class);
                startActivity(accountIntent);*/
                break;
            case R.id.ivOrders:
                ivMeals.setImageResource(R.drawable.ic_meals);

                ivHome.setColorFilter(null);
                ivMeals.setColorFilter(null);
                ivOrders.setColorFilter(ContextCompat.getColor(this, R.color.selected_icon_color));
                ivProfile.setColorFilter(null);
                ivFriendsActivities.setColorFilter(null);

                Fragment myOrdersFragment = new MyOrdersFragment();
                fm.beginTransaction()
                        .replace(R.id.container, myOrdersFragment, MyOrdersFragment.TAG)
//                        .addToBackStack(null)  // uncomment this line if you want to be able to return to the prev. fragment with "back" button
                        .commit();

                tvHeader.setText("Orders");
                break;
        }
    }

    private boolean isLoggedIn() {
        SharedPreferences settings = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        String userID = settings.getString("userID", "defaultValue");
        if (userID.equals("")) return false;
        else return true;
    }

    private boolean isLoggedInWithFB() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    @Override
    public void onBackPressed() {
        if (rvSearchedRestaurants != null) {
            if (rvSearchedRestaurants.getVisibility() == View.VISIBLE) {
                rvSearchedRestaurants.setVisibility(View.GONE);
                gvSearchCategories.setVisibility(View.VISIBLE);
            } else finish();
        } else finish();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    protected void onResume() {
        super.onResume();

        //for location
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    //for location
    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //for location
        stopLocationUpdates();
    }

    //for location
    private void stopLocationUpdates() {
        if (mLocationCallback != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
// Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        locationSettingsRequest();//keep asking if imp or do whatever
                        break;
                }
                break;
        }
    }
}