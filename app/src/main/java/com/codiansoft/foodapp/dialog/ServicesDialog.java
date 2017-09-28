package com.codiansoft.foodapp.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.codiansoft.foodapp.DeliveryRestaurantsActivity;
import com.codiansoft.foodapp.QuickServiceRestaurantsActivity;
import com.codiansoft.foodapp.R;
import com.codiansoft.foodapp.TakeawayRestaurantsActivity;

/**
 * Created by Codiansoft on 8/26/2017.
 */

public class ServicesDialog extends Dialog implements android.view.View.OnClickListener {
    public Activity act;
    public Dialog d;
    ImageView ivDelivery, ivQuickService, ivTakeaway, ivReservation;

    public ServicesDialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.act = a;
        Dialog dialog = new Dialog(act);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogSlideAnim;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_services);
        setCanceledOnTouchOutside(false);

        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, 700);
        initUI();
    }

    private void initUI() {
        ivDelivery = (ImageView) findViewById(R.id.ivDelivery);
        ivDelivery.setOnClickListener(this);
        ivQuickService = (ImageView) findViewById(R.id.ivQuickService);
        ivQuickService.setOnClickListener(this);
        ivTakeaway = (ImageView) findViewById(R.id.ivTakeaway);
        ivTakeaway.setOnClickListener(this);
        ivReservation = (ImageView) findViewById(R.id.ivReservation);
        ivReservation.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivDelivery:
                act.startActivity(new Intent(act, DeliveryRestaurantsActivity.class));
                dismiss();
                break;
            case R.id.ivQuickService:
                act.startActivity(new Intent(act, QuickServiceRestaurantsActivity.class));
                dismiss();
                break;
            case R.id.ivTakeaway:
                act.startActivity(new Intent(act, TakeawayRestaurantsActivity.class));
                dismiss();
                break;
            case R.id.ivReservation:
//                act.startActivity(new Intent(act, ReservationRestaurantsActivity.class));
                dismiss();
                break;
        }
    }
}
