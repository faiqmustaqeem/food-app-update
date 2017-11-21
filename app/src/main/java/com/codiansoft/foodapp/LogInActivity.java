package com.codiansoft.foodapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {
    Button bLogIn;
    TextView tvFacebookLogIn, tvSignUp;
    ProgressDialog progressDialog;
    EditText etEmail, etPassword;

    private LoginButton FBLoginButton;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        callbackManager = CallbackManager.Factory.create();
        initUI();

//        FBLoginButton.setReadPermissions(Arrays.asList("email"));
        FBLoginButton.setReadPermissions("email, publish_actions");

        FBLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                SharedPreferences settings = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("FBUserID", loginResult.getAccessToken().getUserId());
                editor.putString("FBAuthToken", loginResult.getAccessToken().getToken());
                editor.commit();

                Intent restaurantsIntent = new Intent(LogInActivity.this, MainActivity.class);
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
                Toast.makeText(LogInActivity.this, "Error " + exception.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initUI() {
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bLogIn = (Button) findViewById(R.id.bLogIn);
        tvFacebookLogIn = (TextView) findViewById(R.id.tvFacebookLogIn);
        tvFacebookLogIn.setOnClickListener(this);
        bLogIn.setOnClickListener(this);
        tvSignUp = (TextView) findViewById(R.id.tvSignUp);
        tvSignUp.setOnClickListener(this);

        FBLoginButton = (LoginButton) findViewById(R.id.FBLoginButton);
        FBLoginButton.setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);
        FBLoginButton.setReadPermissions("email, publish_actions");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bLogIn:
                if (validFields()) login();
                break;
            case R.id.tvFacebookLogIn:
                Intent i = new Intent(this, FacebookLogInActivity.class);
                startActivity(i);
                break;
            case R.id.tvSignUp:
                Intent signUpIntent = new Intent(this, SignUpActivity.class);
                startActivity(signUpIntent);
                break;
        }
    }

    private boolean validFields() {
        if (etEmail.getText().toString().equals("")) {
            etEmail.setError("Enter password");
            return false;
        }
         else if (etPassword.getText().toString().equals("")) {
            etPassword.setError("Enter password");
            return false;
        }
        return true;
    }

    private void login() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgressNumberFormat(null);
        progressDialog.setProgressPercentFormat(null);
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONObject Jobject = new JSONObject(response);
                            JSONObject result = Jobject.getJSONObject("result");
                            if (result.getString("status").equals("success")) {
                                JSONObject data = Jobject.getJSONObject("data");
                                JSONObject userdata = data.getJSONObject("user data");

                                SharedPreferences settings = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putString("apiSecretKey", data.getString("api_secret"));
                                editor.putString("userID", userdata.getString("id"));
                                editor.commit();

                                progressDialog.dismiss();
                                Intent restaurantsIntent = new Intent(LogInActivity.this, MainActivity.class);
                                startActivity(restaurantsIntent);
                                finish();
                            } else {
                                Toast.makeText(LogInActivity.this, "" + result.getString("response"), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        } catch (Exception ee) {
                            Toast.makeText(LogInActivity.this, ee.toString(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        NetworkResponse response = error.networkResponse;
                        if (response != null && response.data != null) {
                            switch (response.statusCode) {
                                case 409:
                                    Toast.makeText(LogInActivity.this, "Error " + 409, Toast.LENGTH_SHORT).show();
//                                    utilities.dialog("Already Exist", act);
                                    break;
                                case 400:
//                                    utilities.dialog("Connection Problem", act);
                                    Toast.makeText(LogInActivity.this, "Error " + 400, Toast.LENGTH_SHORT).show();
                                    break;
                                default:
//                                    utilities.dialog("Connection Problem", act);
                                    Toast.makeText(LogInActivity.this, "Unexpected Server Error", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        } else {
                            Toast.makeText(LogInActivity.this, "Unexpected Connection Error", Toast.LENGTH_SHORT).show();
//                            finish();
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", etEmail.getText().toString());
                params.put("password", etPassword.getText().toString());
                params.put("signin_type", "1");
                return params;
            }
        };
        queue.add(postRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
