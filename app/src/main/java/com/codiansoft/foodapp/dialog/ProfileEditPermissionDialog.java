package com.codiansoft.foodapp.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
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
import com.codiansoft.foodapp.AccountActivity;
import com.codiansoft.foodapp.GlobalClass;
import com.codiansoft.foodapp.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Codiansoft on 9/11/2017.
 */

public class ProfileEditPermissionDialog extends Dialog implements android.view.View.OnClickListener {
    public Activity c;
    EditText etPassword;
    TextView tvSubmit, tvCancel;
  public static String passowrd="";
    public static boolean isPassswordSet=false;
    public ProfileEditPermissionDialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_profile_edit_permission);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvSubmit = (TextView) findViewById(R.id.tvSubmit);
        tvCancel = (TextView) findViewById(R.id.tvCancel);
        tvCancel.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
        tvSubmit.setClickable(false);

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!etPassword.getText().toString().equals("")){
                    tvSubmit.setTextColor(c.getResources().getColor(R.color.colorAccent));
                    tvSubmit.setClickable(true);
                }
                else {
                    tvSubmit.setTextColor(c.getResources().getColor(android.R.color.darker_gray));
                    tvSubmit.setClickable(false);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSubmit:
                passowrd=etPassword.getText().toString();
                isPassswordSet=true;
                break;
            case R.id.tvCancel:
                dismiss();
                break;

            default:
                break;
        }
        dismiss();
    }


}