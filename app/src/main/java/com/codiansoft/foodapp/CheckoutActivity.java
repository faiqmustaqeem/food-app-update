package com.codiansoft.foodapp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.CountryPickerListener;
import com.simplify.android.sdk.Card;
import com.simplify.android.sdk.CardEditor;
import com.simplify.android.sdk.CardToken;
import com.simplify.android.sdk.Simplify;

import java.util.List;

public class CheckoutActivity extends AppCompatActivity {
    Simplify simplify;
    private CountryPicker mCountryPicker;
    TextView tvSelectedCountry;
    EditText etCardNumber;
    EditText etMonth;
    EditText etYear;
    EditText etCVV;
    EditText etZIP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        getSupportActionBar().hide();
        simplify = new Simplify();
        simplify.setApiKey("sbpb_NjhjYmNiNjAtM2NmYi00NzllLWFhOWMtMWQ2NjU2N2QzZDg0");

        initUI();


        mCountryPicker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode,
                                        int flagDrawableResID) {
                tvSelectedCountry.setText(name);
                Drawable img = getResources().getDrawable(flagDrawableResID);
                tvSelectedCountry.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                mCountryPicker.dismiss();
            }
        });


        tvSelectedCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCountryPicker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
            }
        });


        // init card editor

//        final CardEditor cardEditor = (CardEditor) findViewById(R.id.card_editor);

        final Button checkoutButton = (Button) findViewById(R.id.bAddPayment);

        // add state change listener

/*        cardEditor.addOnStateChangedListener(new CardEditor.OnStateChangedListener() {

            @Override

            public void onStateChange(CardEditor cardEditor) {

                // isValid() == true : card editor contains valid and complete card information

                checkoutButton.setEnabled(cardEditor.isValid());

            }

        });*/

        // add checkout button click listener

        checkoutButton.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                /*if (validInputFields()) {
                    Card card = new Card()
                            .setNumber(etCardNumber.getText().toString())
                            .setExpMonth(etMonth.getText().toString())
                            .setExpYear(etYear.getText().toString())
                            .setCvc(etCVV.getText().toString())
                            .setAddressZip(etZIP.getText().toString());
                    // tokenize the card
                    simplify.createCardToken(card, new CardToken.Callback() {
                        @Override
                        public void onSuccess(CardToken cardToken) {
                            // ...
                            Log.e("CARD TOKEN", cardToken.toString());
                            Toast.makeText(CheckoutActivity.this, "" + cardToken.toString(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            // ...
                            Toast.makeText(CheckoutActivity.this, "" + throwable.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }*/
                Intent i = new Intent(CheckoutActivity.this, ThankYouActivity.class);
                startActivity(i);
            }
        });
    }

    private boolean validInputFields() {
        if (etCardNumber.getText().toString().equals("")) {
            etCardNumber.setError("This is required");
            return false;
        } else if (etMonth.getText().toString().equals("")) {
            etMonth.setError("This is required");
            return false;
        } else if (etYear.getText().toString().equals("")) {
            etYear.setError("This is required");
            return false;
        } else if (etCVV.getText().toString().equals("")) {
            etCVV.setError("This is required");
            return false;
        } else if (etZIP.getText().toString().equals("")) {
            etZIP.setError("This is required");
            return false;
        } else return true;
    }

    private void initUI() {
        List<Country> countries = Country.getAllCountries(); //List of all countries
//        Country[] countries = Country.COUNTRIES; //Array of all countries sorted by ISO code
        Country country = Country.getCountryFromSIM(this); //Get user country based on SIM card
        mCountryPicker = CountryPicker.newInstance("Select Country");
        etCardNumber = (EditText) findViewById(R.id.etCardNumber);
        etMonth = (EditText) findViewById(R.id.etMonth);
        etYear = (EditText) findViewById(R.id.etYear);
        etCVV = (EditText) findViewById(R.id.etCVV);
        etZIP = (EditText) findViewById(R.id.etZIP);
        tvSelectedCountry = (TextView) findViewById(R.id.tvSelectedCountry);
        tvSelectedCountry.setText(country.getName());
        Drawable img = getResources().getDrawable(country.getFlag());
        tvSelectedCountry.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
    }
}
