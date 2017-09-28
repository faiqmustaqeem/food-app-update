package com.codiansoft.foodapp.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.codiansoft.foodapp.AccountActivity;
import com.codiansoft.foodapp.R;


/**
 * Created by Codiansoft on 9/11/2017.
 */

public class ProfileEditPermissionDialog extends Dialog implements android.view.View.OnClickListener {
    public Activity c;
    EditText etPassword;
    TextView tvSubmit, tvCancel;

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
                AccountActivity.canEdit = true;
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