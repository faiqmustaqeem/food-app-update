package com.codiansoft.foodapp.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codiansoft.foodapp.AboutActivity;
import com.codiansoft.foodapp.AccountActivity;
import com.codiansoft.foodapp.HelpActivity;
import com.codiansoft.foodapp.R;

/**
 * Created by Codiansoft on 9/11/2017.
 */

public class UserSettingsFragment extends Fragment implements View.OnClickListener {
    TextView tvSettings, tvAbout, tvHelp;
    public static final String TAG = "UserSettingsFragment";
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_setting, container, false);
        tvSettings = (TextView) view.findViewById(R.id.tvSettings);
        tvSettings.setOnClickListener(this);
        tvAbout = (TextView) view.findViewById(R.id.tvAbout);
        tvHelp = (TextView) view.findViewById(R.id.tvHelp);
        tvAbout.setOnClickListener(this);
        tvHelp.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvSettings:
                Intent i = new Intent(getActivity(), AccountActivity.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getActivity().startActivity(i, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                } else {
                    getActivity().startActivity(i);
                }
                break;
            case R.id.tvAbout:
                Intent aboutIntent = new Intent(getActivity(), AboutActivity.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getActivity().startActivity(aboutIntent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                } else {
                    getActivity().startActivity(aboutIntent);
                }
                break;
            case R.id.tvHelp:
                Intent helpIntent = new Intent(getActivity(), HelpActivity.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getActivity().startActivity(helpIntent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                } else {
                    getActivity().startActivity(helpIntent);
                }
                break;
        }
    }
}