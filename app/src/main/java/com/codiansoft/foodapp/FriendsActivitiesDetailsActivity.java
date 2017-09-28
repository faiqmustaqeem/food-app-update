package com.codiansoft.foodapp;

import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.codiansoft.foodapp.adapter.FriendsActivityPagerAdapter;
import com.codiansoft.foodapp.model.FriendsActivitiesDetailsModel;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class FriendsActivitiesDetailsActivity extends AppCompatActivity {
    String friendUserID;
    private static ViewPager mPager;
    private static int currentPage = 0;
    ArrayList<FriendsActivitiesDetailsModel> friendsActivityArrayList = new ArrayList<>();
    CircleIndicator indicator;
    ProgressBar progressBar;
    public static boolean pausePagerImagesSlide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_friends_activities_details);
        friendUserID = getIntent().getExtras().getString("friendUserID");

        pausePagerImagesSlide = false;

        initUI();

        initFriendActivitiesList();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == friendsActivityArrayList.size()) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };

        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(!pausePagerImagesSlide){
                    handler.post(Update);
                }
            }
        }, 4000, 4000);
    }

    private void initFriendActivitiesList() {
        friendsActivityArrayList.add(new FriendsActivitiesDetailsModel("1", "https://i.pinimg.com/736x/f5/7d/f2/f57df26b1758b63f7c0ab77f9bb3ecb7--heidelberg-laguna-beach.jpg", "Great taste!!"));
        friendsActivityArrayList.add(new FriendsActivitiesDetailsModel("2", "http://i.dailymail.co.uk/i/pix/2017/02/21/08/3D79B41300000578-4244526-image-a-16_1487666071075.jpg", "Enjoying the meal :)"));
        friendsActivityArrayList.add(new FriendsActivitiesDetailsModel("3", "https://media-cdn.tripadvisor.com/media/photo-s/0c/de/c8/27/loving-his-hoagie-he.jpg", "this restaurant have delicious ham burgers..!"));
        mPager.setAdapter(new FriendsActivityPagerAdapter(this, friendsActivityArrayList));
        indicator.setViewPager(mPager);
    }

    private void initUI() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mPager = (ViewPager) findViewById(R.id.pager);
        indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.bringToFront();
        progressBar.bringToFront();
    }

    @Override
    protected void onResume() {
        super.onResume();
        pausePagerImagesSlide = false;
    }
}