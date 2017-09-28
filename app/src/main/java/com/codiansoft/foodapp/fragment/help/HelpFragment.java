package com.codiansoft.foodapp.fragment.help;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codiansoft.foodapp.R;
import com.codiansoft.foodapp.fragment.HomeFragment;

/**
 * Created by Codiansoft on 9/13/2017.
 */

public class HelpFragment extends Fragment {
    TextView tvAccountAndPayment;
    public static final String TAG = "HelpFragment";

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
/*        EditText v = new EditText(getActivity());
        v.setText("Hello Fragment!");*/
        view = inflater.inflate(R.layout.fragment_help, container, false);
        initUI();

        tvAccountAndPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment accountAndPaymentHelpFragment = getFragmentManager().findFragmentByTag(AccountAndPaymentHelpFragment.TAG);

                accountAndPaymentHelpFragment = new AccountAndPaymentHelpFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, accountAndPaymentHelpFragment, AccountAndPaymentHelpFragment.TAG)
                        .addToBackStack(null)  // uncomment this line if you want to be able to return to the prev. fragment with "back" button
                        .commit();
            }
        });

        return view;
    }

    private void initUI() {
        tvAccountAndPayment = (TextView) view.findViewById(R.id.tvAccountAndPayment);

    }
}