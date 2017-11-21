package com.codiansoft.foodapp;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.harrywhewell.scrolldatepicker.DayScrollDatePicker;
import com.harrywhewell.scrolldatepicker.OnDateSelectedListener;

import java.util.Date;

public class ReserveTableActivity extends AppCompatActivity {
    private DayScrollDatePicker mDayPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_table);
        initUI();

        mDayPicker.getSelectedDate(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@Nullable Date date) {
                if(date != null){
                    // do something with selected date
                }
            }
        });
    }

    private void initUI() {
        mDayPicker = (DayScrollDatePicker) findViewById(R.id.day_date_picker);
        mDayPicker.setStartDate(10, 10, 2017);
        mDayPicker.setEndDate(30, 12, 2030);
    }
}