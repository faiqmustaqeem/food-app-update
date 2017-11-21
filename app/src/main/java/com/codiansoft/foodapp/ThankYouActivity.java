package com.codiansoft.foodapp;

import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class ThankYouActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_thank_you);
        getSupportActionBar().hide();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ThankYouActivity.this, MainActivity.class);
        ActivityCompat.finishAffinity(ThankYouActivity.this);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ThankYouActivity.this.startActivity(i);
    }
}
