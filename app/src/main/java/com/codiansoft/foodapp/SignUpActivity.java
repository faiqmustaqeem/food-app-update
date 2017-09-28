package com.codiansoft.foodapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private LoginButton FBLoginButton;
    private CallbackManager callbackManager;
    Button bSignUp;
    EditText etFullName, etContact, etEmail, etHomeAddress, etOfficeAddress, etPassword;
    DatePicker dpBirthday;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        callbackManager = CallbackManager.Factory.create();
        initUI();

        FBLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                SharedPreferences settings = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("FBUserID", loginResult.getAccessToken().getUserId());
                editor.putString("FBAuthToken", loginResult.getAccessToken().getToken());
                editor.commit();

                Intent restaurantsIntent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(restaurantsIntent);
                finish();
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(SignUpActivity.this, "Error " + exception.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initUI() {
        bSignUp = (Button) findViewById(R.id.bSignUp);
        FBLoginButton = (LoginButton) findViewById(R.id.FBLoginButton);
        FBLoginButton.setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);
        FBLoginButton.setReadPermissions("email, publish_actions");
        etFullName = (EditText) findViewById(R.id.etFullName);
        etContact = (EditText) findViewById(R.id.etContact);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etHomeAddress = (EditText) findViewById(R.id.etHomeAddress);
        etOfficeAddress = (EditText) findViewById(R.id.etOfficeAddress);
        etPassword = (EditText) findViewById(R.id.etPassword);
        dpBirthday = (DatePicker) findViewById(R.id.dpBirthday);
        bSignUp.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bSignUp:
                signUp();
                break;
        }
    }

    private void signUp() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgressNumberFormat(null);
        progressDialog.setProgressPercentFormat(null);
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.SIGNUP_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONObject Jobject = new JSONObject(response);
                            JSONObject result = Jobject.getJSONObject("result");
                            if (result.getString("status").equals("success")) {
                                JSONObject userdata = result.getJSONObject("userdata");

                                SharedPreferences settings = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putString("userID", userdata.getString("id"));
                                editor.commit();

                                progressDialog.hide();
                                Intent i = new Intent(SignUpActivity.this, MainActivity.class);
                                startActivity(i);
                                ActivityCompat.finishAffinity(SignUpActivity.this);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                SignUpActivity.this.startActivity(i);
                                finish();

                            } else {
                                Toast.makeText(SignUpActivity.this, "" + result.getString("response"), Toast.LENGTH_SHORT).show();
                                progressDialog.hide();
                            }
                        } catch (Exception ee) {
                            Toast.makeText(SignUpActivity.this, ee.toString(), Toast.LENGTH_SHORT).show();
                            progressDialog.hide();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        NetworkResponse response = error.networkResponse;
                        if (response != null && response.data != null) {
                            switch (response.statusCode) {
                                case 409:
                                    Toast.makeText(SignUpActivity.this, "Error " + 409, Toast.LENGTH_SHORT).show();
//                                    utilities.dialog("Already Exist", act);
                                    break;
                                case 400:
//                                    utilities.dialog("Connection Problem", act);
                                    Toast.makeText(SignUpActivity.this, "Error " + 400, Toast.LENGTH_SHORT).show();
                                    break;
                                default:
//                                    utilities.dialog("Connection Problem", act);
                                    Toast.makeText(SignUpActivity.this, "Unexpected Server Error", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        } else {
                            Toast.makeText(SignUpActivity.this, "Unexpected Connection Error", Toast.LENGTH_SHORT).show();
//                            finish();
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_name", etFullName.getText().toString());
                params.put("email", etEmail.getText().toString());
                params.put("password", etPassword.getText().toString());
                params.put("phone", etContact.getText().toString());
                params.put("date_ob", dpBirthday.getDayOfMonth() + "-" + dpBirthday.getMonth() + "-" + dpBirthday.getYear());
                params.put("address", etFullName.getText().toString());
                params.put("office_address", etFullName.getText().toString());
                return params;
            }
        };
        queue.add(postRequest);
    }
}
