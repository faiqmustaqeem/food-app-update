package com.codiansoft.foodapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.codiansoft.foodapp.dialog.ProfileEditPermissionDialog;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.hbb20.CountryCodePicker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {
    SweetAlertDialog pDialog;
    CountryCodePicker ccp;
    ImageView ivBack, ivProfilePic;
    EditText etFirstName, etLastName, etEmail, etMobileNumber;
    TextView tvEditOrUpdate, tvSignOut;
    public static boolean canEdit = false;
    ProfileEditPermissionDialog d;

    Bitmap bitmapProfilePic;
    String imageBase64;
    final int REQUEST_CODE=1;

    boolean isProfilePicChanged=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        getSupportActionBar().hide();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initUI();
        fetchUserDetails();
        d.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                Log.e("dismiss" , "dismiss");

                if (ProfileEditPermissionDialog.isPassswordSet)
                    checkPassword();


            }
        });


    }

    private void fetchUserDetails() {
        pDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.FETCH_USER_DETAILS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONObject Jobject = new JSONObject(response);
                            JSONObject result = Jobject.getJSONObject("result");
                            if (result.getString("status").equals("success")) {
                                JSONObject userDetails = result.getJSONObject("userInfo");
                                String fullName = userDetails.getString("username");
                                etFirstName.setText(fullName);
                                etLastName.setText(userDetails.getString("last_name"));
                                etEmail.setText(userDetails.getString("email"));
                                etMobileNumber.setText(userDetails.getString("phone"));
                                Glide.with(AccountActivity.this).load(userDetails.getString("picture")).into(ivProfilePic);
                            }
                        } catch (Exception ee) {
                            Toast.makeText(AccountActivity.this, "" + ee.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        pDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(AccountActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof AuthFailureError) {

                            //TODO
                        } else if (error instanceof ServerError) {

                            //TODO
                        } else if (error instanceof NetworkError) {

                            //TODO
                        } else if (error instanceof ParseError) {

                            //TODO
                        }
                        pDialog.dismiss();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences settings = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
                String userID = settings.getString("userID", "defaultValue");
                String apiSecretKey = settings.getString("apiSecretKey", "defaultValue");

                params.put("api_secret", apiSecretKey);

                Log.e("api_secret",params.toString());

                return params;
            }
        };
        queue.add(postRequest);
    }

    private void initUI() {
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(android.R.color.darker_gray);
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);

        d = new ProfileEditPermissionDialog(AccountActivity.this);
        canEdit = false;
        tvEditOrUpdate = (TextView) findViewById(R.id.tvEditOrUpdate);
        tvSignOut = (TextView) findViewById(R.id.tvSignOut);
        etMobileNumber = (EditText) findViewById(R.id.etMobileNumber);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etFirstName = (EditText) findViewById(R.id.etFirstName);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        ivProfilePic = (ImageView) findViewById(R.id.ivProfilePic);
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
        pDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.UPDATE_PROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONObject Jobject = new JSONObject(response);
                            JSONObject result = Jobject.getJSONObject("result");
                            if (result.getString("status").equals("success")) {
                                JSONObject userDetails = result.getJSONObject("userInfo");
                                String fullName = userDetails.getString("username");
                                etFirstName.setText(fullName);
                               // etEmail.setText(userDetails.getString("email"));
                                etMobileNumber.setText(userDetails.getString("phone"));
                               // Glide.with(AccountActivity.this).load(userDetails.getString("picture")).placeholder(R.drawable.ic_profile_pic).centerCrop().into(ivProfilePic);
                                Toast.makeText(AccountActivity.this , "Profile Updated ! " , Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(AccountActivity.this , result.getString("status") , Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception ee) {
                            Toast.makeText(AccountActivity.this, "" + ee.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        pDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(AccountActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof AuthFailureError) {

                            //TODO
                        } else if (error instanceof ServerError) {

                            //TODO
                        } else if (error instanceof NetworkError) {

                            //TODO
                        } else if (error instanceof ParseError) {

                            //TODO
                        }
                        pDialog.dismiss();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences settings = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
                String userID = settings.getString("userID", "defaultValue");

                String apiSecretKey = settings.getString("apiSecretKey", "defaultValue");
                String first_name=etFirstName.getText().toString();
                String last_name=etLastName.getText().toString();
                String phone=etMobileNumber.getText().toString();
                params.put("api_secret", apiSecretKey);
                params.put("first_name",first_name);
                params.put("last_name",last_name);
                params.put("phone",phone);
                if(isProfilePicChanged)
                {
                    params.put("user_image",imageBase64);
                }
                else {
                    params.put("user_image","");
                }
                Log.e("api_secret",params.toString());
                return params;

            }
        };
        queue.add(postRequest);
    }

    public boolean isLoggedInWithFB() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

     void makeFieldsEditable() {
        ccp.setCcpClickable(true);
        etFirstName.setEnabled(true);
        etLastName.setEnabled(true);
       // etEmail.setEnabled(true);
        etMobileNumber.setEnabled(true);
        ivProfilePic.setClickable(true);
        ivProfilePic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            try {
                // get uri from Intent
                Uri uri = data.getData();
                // get bitmap from uri
                bitmapProfilePic = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                // store bitmap to file
                File filename = new File(Environment.getExternalStorageDirectory(), "imageName.jpg");
                FileOutputStream out = new FileOutputStream(filename);
                bitmapProfilePic.compress(Bitmap.CompressFormat.JPEG, 60, out);

                Glide.clear(ivProfilePic);
                ivProfilePic.setImageBitmap(bitmapProfilePic);

                out.flush();
                out.close();
                // get base64 string from file
                imageBase64 = getStringImage(filename);
                isProfilePicChanged=true;

                Log.e("iamgebase64",imageBase64);
                // use base64 for your next step.
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getStringImage(File file) {
        try {
            FileInputStream fin = new FileInputStream(file);
            byte[] imageBytes = new byte[(int) file.length()];
            fin.read(imageBytes, 0, imageBytes.length);
            fin.close();
            ivProfilePic.setImageBitmap(bitmapProfilePic);
            return Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } catch (Exception ex) {
            Toast.makeText(this, "Image size is too large to upload", Toast.LENGTH_SHORT).show();
        }
        return null;
    }
    private void checkPassword() {

        Log.e("check","check");
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.CHECK_PASSWORD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e("response" , response);
                        try {
                            JSONObject Jobject = new JSONObject(response);
                            JSONObject result = Jobject.getJSONObject("result");

                            if (result.getString("status").equals("success")) {
                                Log.e("response" , result.getString("response"));
                                if(result.getString("response").equals("password matched"))
                                {
                                    canEdit = true;
                                    if (canEdit)
                                    {
                                        makeFieldsEditable();
                                        tvEditOrUpdate.setText("SAVE");
                                    }
                                }
                                else
                                {
                                    canEdit = false;
                                    Toast.makeText(AccountActivity.this , "Incorrect Password" , Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                canEdit = false;
                                Toast.makeText(AccountActivity.this, "Incorrect Password" , Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception ee) {
                            Toast.makeText(AccountActivity.this, "" + ee.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        //  pDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(AccountActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof AuthFailureError) {

                            //TODO
                        } else if (error instanceof ServerError) {

                            //TODO
                        } else if (error instanceof NetworkError) {

                            //TODO
                        } else if (error instanceof ParseError) {

                            //TODO
                        }
                        // pDialog.dismiss();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() {



                SharedPreferences settings =  getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
                String userID = settings.getString("userID", "defaultValue");

                String apiSecretKey = settings.getString("apiSecretKey", "defaultValue");

                Map<String, String> params = new HashMap<String, String>();


                params.put("api_secret", apiSecretKey);
                params.put("password",ProfileEditPermissionDialog.passowrd);
                Log.e("params",params.toString());

                return params;
            }
        };
        queue.add(postRequest);
    }
}
