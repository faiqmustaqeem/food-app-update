package com.codiansoft.foodapp.fragment.help;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codiansoft.foodapp.R;

/**
 * Created by Codiansoft on 9/13/2017.
 */

public class AccountSettingsFragment extends Fragment {

    public static final String TAG = "AccountSettingsFragment";

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
/*        EditText v = new EditText(getActivity());
        v.setText("Hello Fragment!");*/
        view = inflater.inflate(R.layout.fragment_help_account_settings, container, false);
        initUI();

        return view;
    }

    private void initUI() {

    }
}