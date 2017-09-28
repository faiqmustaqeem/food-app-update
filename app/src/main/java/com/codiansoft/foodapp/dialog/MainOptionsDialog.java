package com.codiansoft.foodapp.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.codiansoft.foodapp.LogInActivity;
import com.codiansoft.foodapp.R;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

/**
 * Created by Codiansoft on 8/15/2017.
 */

public class MainOptionsDialog extends Dialog implements android.view.View.OnClickListener {

    public Activity act;
    public Dialog d;
    TextView tvLogOut;


    public MainOptionsDialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.act = a;
        Dialog dialog = new Dialog(act);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogSlideAnim;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_main_options);
        setCanceledOnTouchOutside(false);

        Window window = getWindow();
        window.setGravity(Gravity.TOP);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, 500);
        initUI();
    }

    private void initUI() {
        tvLogOut = (TextView) findViewById(R.id.tvLogOut);
        tvLogOut.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvLogOut:
                if (isLoggedIn()) {
                    new AlertDialog.Builder(act)
                            .setTitle("Are you sure to log out?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    LoginManager.getInstance().logOut();

                                    Intent logoutIntent = new Intent(act, LogInActivity.class);
                                    dismiss();
                                    ActivityCompat.finishAffinity(act);
                                    logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    act.startActivity(logoutIntent);
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
        }
    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }
}
