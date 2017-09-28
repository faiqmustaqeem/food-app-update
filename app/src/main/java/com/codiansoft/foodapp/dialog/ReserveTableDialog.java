package com.codiansoft.foodapp.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.codiansoft.foodapp.GlobalClass;
import com.codiansoft.foodapp.R;
import com.codiansoft.foodapp.RestaurantActivity;
import com.codiansoft.foodapp.RestaurantReservationActivity;

import java.util.Calendar;

/**
 * Created by Codiansoft on 9/21/2017.
 */

public class ReserveTableDialog extends Dialog implements android.view.View.OnClickListener {

    public Activity act;
    public Dialog d;
    TextView tvCancel, tvReserve, tvTableTitle, tvCapacity;
    String id, title, capacity;
    DatePicker dpReservationDate;

    public ReserveTableDialog(Activity a, String id, String title, String capacity) {
        super(a);
        // TODO Auto-generated constructor stub
        this.act = a;
        this.id = id;
        this.title = title;
        this.capacity = capacity;
        Dialog dialog = new Dialog(act);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogSlideAnim;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_reserve_table);
        setCanceledOnTouchOutside(false);

        Window window = getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        initUI();
    }

    private void initUI() {
        dpReservationDate = (DatePicker) findViewById(R.id.dpReservationDate);
        dpReservationDate.setMinDate(System.currentTimeMillis() - 1000);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        dpReservationDate.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
//                Log.d("Date", "Year=" + year + " Month=" + (month + 1) + " day=" + dayOfMonth);
//                Toast.makeText(act, ""+ "Year=" + year + " Month=" + (month + 1) + " day=" + dayOfMonth, Toast.LENGTH_SHORT).show();

            }
        });


        tvCapacity = (TextView) findViewById(R.id.tvCapacity);
        tvTableTitle = (TextView) findViewById(R.id.tvTableTitle);
        tvCancel = (TextView) findViewById(R.id.tvCancel);
        tvCancel.setOnClickListener(this);
        tvReserve = (TextView) findViewById(R.id.tvReserve);
        tvReserve.setOnClickListener(this);

        tvTableTitle.setText(title);
        tvCapacity.setText(capacity + " people");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCancel:
                dismiss();
                break;
            case R.id.tvReserve:

                if (tvReserve.getText().toString().equals("DONE")) {
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(act, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(act);
                    }
                    builder.setTitle("Reserve Menu as well")
                            .setMessage("Do you want to select food items now?")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent restaurantIntent = new Intent(act, RestaurantActivity.class);
                                    restaurantIntent.putExtra("restaurantID", RestaurantReservationActivity.restaurantID);
                                    restaurantIntent.putExtra("restaurantTitle", RestaurantReservationActivity.restaurantTitle);
                                    restaurantIntent.putExtra("restaurantDuration", RestaurantReservationActivity.restaurantDuration);
                                    restaurantIntent.putExtra("restaurantImage", RestaurantReservationActivity.restaurantImage);
                                    restaurantIntent.putExtra("restaurantDescription", RestaurantReservationActivity.restaurantDescription);
                                    act.startActivity(restaurantIntent);
                                }
                            })
                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                } else {
                    reserveTable();
                }
                break;
        }
    }

    private void reserveTable() {
        tvReserve.setText("DONE");
    }
}
