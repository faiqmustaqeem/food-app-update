package com.codiansoft.foodapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.codiansoft.foodapp.model.CityModel;
import com.codiansoft.foodapp.model.CountryModel;
import com.codiansoft.foodapp.model.StateModel;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private LoginButton FBLoginButton;
    private CallbackManager callbackManager;
    Button bSignUp;
    EditText etFullName, etContact, etEmail, etHomeAddress, etOfficeAddress, etPassword;
    DatePicker dpBirthday;
    ProgressDialog progressDialog;

    String countryName, countryId;
    String stateName , stateId;
    String cityName , cityId;
    CircleImageView ivProfilePic;
    final HashMap<String,String> listCountryModel=new HashMap<>();
    final HashMap<String,String> listStateModel=new HashMap<>();
    final HashMap<String,String> listCityModel=new HashMap<>();

    Spinner spinnerCountry , spinnerState , spinnerCity;

    Bitmap bitmapProfilePic;
    String imageBase64;

    final int REQUEST_CODE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        callbackManager = CallbackManager.Factory.create();
        initUI();



        countryName ="" ;
        countryId="";
        stateName="" ;
        stateId="";
        cityName="" ;
        cityId="";


        ivProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
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

        fetchCountries();

    }

    private void initUI()
    {
        ivProfilePic=findViewById(R.id.ivProfilePic);
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
        spinnerCountry=findViewById(R.id.spinnerCountry);
        spinnerState=findViewById(R.id.spinnerState);
        spinnerCity=findViewById(R.id.spinnerCity);
        bSignUp.setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bSignUp:
                signUp();
                break;

        }
    }
    boolean validate()
    {
        if(etFullName.getText().toString().equals(""))
        {
            Toast.makeText(SignUpActivity.this , "Please enter your name " , Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(etEmail.getText().toString().equals(""))
        {
            Toast.makeText(SignUpActivity.this , "Please enter your Email " , Toast.LENGTH_SHORT).show();
            return false;
        }
        if(etContact.getText().toString().equals(""))
        {
            Toast.makeText(SignUpActivity.this , "Please enter your Contact Number" , Toast.LENGTH_SHORT).show();
            return false;
        }
        if(etPassword.getText().toString().equals(""))
        {
            Toast.makeText(SignUpActivity.this , "Please enter passowrd " , Toast.LENGTH_SHORT).show();
            return false;
        }

        if(etHomeAddress.getText().toString().equals(""))
        {
            Toast.makeText(SignUpActivity.this , "Please enter your Home Address " , Toast.LENGTH_SHORT).show();
            return false;
        }
        if(countryId.equals(""))
        {
            Toast.makeText(SignUpActivity.this , "Please select country " , Toast.LENGTH_SHORT).show();
            return false;
        }
        if(stateId.equals(""))
        {
            Toast.makeText(SignUpActivity.this , "Please select state " , Toast.LENGTH_SHORT).show();
            return false;
        }
        if(cityId.equals(""))
        {
            Toast.makeText(SignUpActivity.this , "Please select city " , Toast.LENGTH_SHORT).show();
            return false;
        }




        return true;
    }
    private void signUp() {

        if(validate())
        {

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

                                    SharedPreferences settings = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = settings.edit();
                                    editor.putString("userID", result.getJSONObject("user_details").getString("id"));
                                    editor.putString("apiSecretKey", result.getString("api_secret"));
                                    editor.commit();

                                    GlobalClass.api_secret=result.getString("api_secret");

                                    progressDialog.hide();
                                    Intent i = new Intent(SignUpActivity.this, MainActivity.class);
                                    startActivity(i);
                                    ActivityCompat.finishAffinity(SignUpActivity.this);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                    // SignUpActivity.this.startActivity(i);
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
                    params.put("first_name", etFullName.getText().toString());
                    Log.e("first_name",etFullName.getText().toString());
                    params.put("last_name", etFullName.getText().toString());
                    params.put("email", etEmail.getText().toString());
                    params.put("password", etPassword.getText().toString());
                    params.put("phone", etContact.getText().toString());
                    params.put("date_ob", dpBirthday.getDayOfMonth() + "-" + (dpBirthday.getMonth()+1) + "-" + dpBirthday.getYear());
                    params.put("address", etHomeAddress.getText().toString());
                    params.put("office_address", etOfficeAddress.getText().toString());

                    params.put("country_id",countryId);
                    params.put("state_id",stateId);
                    params.put("city_id",cityId);
                    params.put("user_image",imageBase64);

                    Log.e("params",params.toString());

                    return params;
                }
            };
            queue.add(postRequest);
        }

    }
    private void fetchCountries() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgressNumberFormat(null);
        progressDialog.setProgressPercentFormat(null);
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.FETCH_COUNTRIES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONArray countries = new JSONArray(response);
                            listCountryModel.clear();
                            List<String> countriesList=new ArrayList<>();
                            for(int i=0 ; i < countries.length() ; i++)
                            {
                               JSONObject country = countries.getJSONObject(i);
                                String name=country.getString("name");
                                String id=country.getString("id");
                                listCountryModel.put(name , id);
                                countriesList.add(name);


                            }
                            progressDialog.hide();
                            ArrayAdapter adapter = new ArrayAdapter(SignUpActivity.this,android.R.layout.simple_spinner_item,countriesList);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerCountry.setAdapter(adapter);
                            spinnerCountry.setSelected(true);
                            spinnerCountry.setSelection(0);
                            spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    countryName=adapterView.getItemAtPosition(i).toString();
                                  //  Log.e("country",countryName);
                                   // Log.e("hashmap_size",String.valueOf(listCountryModel.size()));
                                    countryId=listCountryModel.get(countryName).toString();

                                    Log.e("countryId", countryId);

                                    if(!countryId.equals("")) {
                                        Log.e("country", countryId);
                                        fetchStates();
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });



//


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

                return params;
            }
        };
        queue.add(postRequest);
    }
    private void fetchStates() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgressNumberFormat(null);
        progressDialog.setProgressPercentFormat(null);
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.FETCH_STATES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONArray states = new JSONArray(response);
                            listStateModel.clear();
                            List<String> statesList=new ArrayList<>();
                            for(int i=0 ; i < states.length() ; i++)
                            {
                                JSONObject state = states.getJSONObject(i);
                                String name=state.getString("name");
                                String id=state.getString("id");
                                listStateModel.put(name , id);
                                statesList.add(name);
                            }
                            progressDialog.hide();
                            ArrayAdapter adapter = new ArrayAdapter(SignUpActivity.this,android.R.layout.simple_spinner_item,statesList);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerState.setAdapter(adapter);
                            spinnerState.setSelected(true);
                            spinnerState.setSelection(0);
                            spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    stateName=adapterView.getItemAtPosition(i).toString();
                                    //  Log.e("country",countryName);
                                    // Log.e("hashmap_size",String.valueOf(listCountryModel.size()));
                                    stateId=listStateModel.get(stateName).toString();


                                    if(!stateId.equals("")) {

                                        fetchCities();
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });



//


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

                params.put("country_id",countryId);

                return params;

            }
        };
        queue.add(postRequest);
    }
    private void fetchCities() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgressNumberFormat(null);
        progressDialog.setProgressPercentFormat(null);
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.FETCH_CITIES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONArray cities = new JSONArray(response);
                            listCityModel.clear();
                            List<String> citiesList=new ArrayList<>();

                            for(int i=0 ; i < cities.length() ; i++)
                            {
                                JSONObject city = cities.getJSONObject(i);
                                String name=city.getString("name");
                                String id=city.getString("id");
                                listCityModel.put(name , id);
                                citiesList.add(name);
                            }
                            progressDialog.hide();
                            Log.e("cities",String.valueOf(citiesList.size()));
                            ArrayAdapter adapter = new ArrayAdapter(SignUpActivity.this,android.R.layout.simple_spinner_item,citiesList);

                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerCity.setAdapter(adapter);
                            spinnerCity.setSelected(true);
                            spinnerCity.setSelection(0);
                            spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    cityName=adapterView.getItemAtPosition(i).toString();

                                    cityId=listCityModel.get(cityName).toString();


                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });



//


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

                params.put("state_id",stateId);
                return params;
            }
        };
        queue.add(postRequest);
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

}
