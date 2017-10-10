package com.codiansoft.foodapp.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.request.target.Target;
import com.codiansoft.foodapp.FriendsActivitiesDetailsActivity;
import com.codiansoft.foodapp.R;
import com.codiansoft.foodapp.model.FriendsActivitiesDetailsModel;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.LikeView;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Codiansoft on 9/26/2017.
 */

public class FriendsActivityPagerAdapter extends PagerAdapter {
    private ArrayList<FriendsActivitiesDetailsModel> activityList;
    private LayoutInflater inflater;
    private Context context;

    public FriendsActivityPagerAdapter(Context context, ArrayList<FriendsActivitiesDetailsModel> activityList) {
        this.context = context;
        this.activityList = activityList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return activityList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        final FriendsActivitiesDetailsModel detailsModel = activityList.get(position);
        View myImageLayout = inflater.inflate(R.layout.friends_activity_details_pager_item, view, false);
        final ImageView ivActivityPic = (ImageView) myImageLayout.findViewById(R.id.ivActivityPic);
        TextView tvActivityText = (TextView) myImageLayout.findViewById(R.id.tvActivityText);
        final ImageView ivShare = (ImageView) myImageLayout.findViewById(R.id.ivShare);
        ivShare.bringToFront();
        final ShareButton fb_share_button = (ShareButton) myImageLayout.findViewById(R.id.fb_share_button);
        fb_share_button.bringToFront();

        TextView tvComment = (TextView) myImageLayout.findViewById(R.id.tvComment);
        TextView tvLike = (TextView) myImageLayout.findViewById(R.id.tvLike);

        ShareDialog shareDialog = new ShareDialog((Activity) context);

        tvActivityText.setText(detailsModel.getText());
        Glide.with(context).load(detailsModel.getImage()).centerCrop().into(ivActivityPic);

        final Bitmap[] bitmap = {null};

        fb_share_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FriendsActivitiesDetailsActivity.pausePagerImagesSlide = true;
                Bitmap b = null;
                b = ((GlideBitmapDrawable) ivActivityPic.getDrawable().getCurrent()).getBitmap();

                bitmap[0] = b;
            }
        });

        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(bitmap[0])
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();
        fb_share_button.setShareContent(content);

        tvComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FriendsActivitiesDetailsActivity.pausePagerImagesSlide = true;
            }
        });

        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FriendsActivitiesDetailsActivity.pausePagerImagesSlide = true;
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, detailsModel.getText());
                sendIntent.setType("text/plain");
                context.startActivity(Intent.createChooser(sendIntent, "Share this Post"));
            }
        });

        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}