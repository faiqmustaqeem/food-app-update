package com.codiansoft.foodapp.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.codiansoft.foodapp.R;

/**
 * Created by Codiansoft on 9/27/2017.
 */

public class UploadActivityPostDialog extends Dialog implements android.view.View.OnClickListener {

    public Activity act;
    public Dialog d;
    TextView tvCancel, tvSubmit;
    Bitmap postImage;
    ImageView ivPostImage;

    public UploadActivityPostDialog(Activity a, Bitmap postImage) {
        super(a);
        // TODO Auto-generated constructor stub
        this.act = a;
        this.postImage = postImage;
        Dialog dialog = new Dialog(act);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogSlideAnim;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_post_activity);
        setCanceledOnTouchOutside(false);

        Window window = getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        initUI();
    }

    private void initUI() {
        ivPostImage = (ImageView) findViewById(R.id.ivPostImage);
        tvCancel = (TextView) findViewById(R.id.tvCancel);
        tvCancel.setOnClickListener(this);
        tvSubmit = (TextView) findViewById(R.id.tvSubmit);
        tvSubmit.setOnClickListener(this);
        ivPostImage.setImageBitmap(postImage);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCancel:
                dismiss();
                break;
            case R.id.tvReserve:

                break;
        }
    }
}
