package com.codiansoft.foodapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codiansoft.foodapp.dialog.ProfileEditPermissionDialog;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.hbb20.CountryCodePicker;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {
    CountryCodePicker ccp;
    ImageView ivBack;
    EditText etFirstName, etLastName, etEmail, etMobileNumber;
    TextView tvEditOrUpdate, tvSignOut;
    public static boolean canEdit = false;
    ProfileEditPermissionDialog d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        getSupportActionBar().hide();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initUI();

        d.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (canEdit) {
                    makeFieldsEditable();
                    tvEditOrUpdate.setText("SAVE");
                }
            }
        });
    }

    private void initUI() {
        d = new ProfileEditPermissionDialog(AccountActivity.this);
        canEdit = false;
        tvEditOrUpdate = (TextView) findViewById(R.id.tvEditOrUpdate);
        tvSignOut = (TextView) findViewById(R.id.tvSignOut);
        etMobileNumber = (EditText) findViewById(R.id.etMobileNumber);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etFirstName = (EditText) findViewById(R.id.etFirstName);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        ivBack.setOnClickListener(this);
        tvEditOrUpdate.setOnClickListener(this);
        tvSignOut.setOnClickListener(this);
        ccp.setCcpClickable(false);

        tvEditOrUpdate.setText("EDIT");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.tvEditOrUpdate:
                if (canEdit) {
                    updateProfile();
                } else {
                    d.show();
                }
                break;
            case R.id.tvSignOut:
                if (isLoggedInWithFB()) {
                    new AlertDialog.Builder(AccountActivity.this)
                            .setTitle("Are you sure to log out?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    LoginManager.getInstance().logOut();

                                    Intent logoutIntent = new Intent(AccountActivity.this, LogInActivity.class);
                                    dialog.dismiss();
                                    ActivityCompat.finishAffinity(AccountActivity.this);
                                    logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    AccountActivity.this.startActivity(logoutIntent);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Whatever...
                                }
                            }).show();
                } else if (isLoggedIn()){
                    new AlertDialog.Builder(AccountActivity.this)
                            .setTitle("Are you sure to log out?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    SharedPreferences settings = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = settings.edit();
                                    editor.putString("apiSecretKey", "");
                                    editor.putString("userID", "");
                                    editor.commit();

                                    Intent logoutIntent = new Intent(AccountActivity.this, LogInActivity.class);
                                    dialog.dismiss();
                                    ActivityCompat.finishAffinity(AccountActivity.this);
                                    logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    AccountActivity.this.startActivity(logoutIntent);
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

    private boolean isLoggedIn() {
        SharedPreferences settings = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        String userID = settings.getString("userID", "defaultValue");
        if (userID.equals("")) return false;
        else return true;
    }

    private void updateProfile() {

    }

    public boolean isLoggedInWithFB() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    private void makeFieldsEditable() {
        ccp.setCcpClickable(true);
        etFirstName.setEnabled(true);
        etLastName.setEnabled(true);
        etEmail.setEnabled(true);
        etMobileNumber.setEnabled(true);
    }

}
