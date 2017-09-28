package com.codiansoft.foodapp.fragment.help;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codiansoft.foodapp.R;

/**
 * Created by Codiansoft on 9/13/2017.
 */

public class AccountAndPaymentHelpFragment extends Fragment implements View.OnClickListener {
    TextView tvAccountSetting;

    public static final String TAG = "AccountAndPaymentHelpFragment";

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
/*        EditText v = new EditText(getActivity());
        v.setText("Hello Fragment!");*/
        view = inflater.inflate(R.layout.fragment_account_and_payment, container, false);
        initUI();


        return view;
    }

    private void initUI() {
        tvAccountSetting = (TextView) view.findViewById(R.id.tvAccountSetting);
        tvAccountSetting.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvAccountSetting:
                AccountSettingsFragment accountSettingsFragment = new AccountSettingsFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, accountSettingsFragment, AccountSettingsFragment.TAG)
                        .addToBackStack(null)  // uncomment this line if you want to be able to return to the prev. fragment with "back" button
                        .commit();
                break;
        }
    }
}