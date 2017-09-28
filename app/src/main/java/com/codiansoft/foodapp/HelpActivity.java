package com.codiansoft.foodapp;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.codiansoft.foodapp.fragment.help.HelpFragment;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // make fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_help);

        getSupportActionBar().setTitle("How can we help?");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Fragment helpFragment = getSupportFragmentManager().findFragmentByTag(HelpFragment.TAG);

        if (savedInstanceState == null) {
            helpFragment = new HelpFragment();
            android.support.v4.app.FragmentTransaction ftHome = getSupportFragmentManager().beginTransaction();
            ftHome.add(R.id.container, helpFragment).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Respond to the action bar's Up/Home button
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
