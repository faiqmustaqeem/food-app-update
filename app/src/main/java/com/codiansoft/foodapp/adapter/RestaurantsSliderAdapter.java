package com.codiansoft.foodapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codiansoft.foodapp.R;
import com.codiansoft.foodapp.RestaurantActivity;
import com.codiansoft.foodapp.model.RestaurantsModel;

import java.util.ArrayList;

/**
 * Created by Codiansoft on 8/29/2017.
 */

public class RestaurantsSliderAdapter extends PagerAdapter {

    private ArrayList<RestaurantsModel> restaurantList;
    private LayoutInflater inflater;
    private Context context;


    public RestaurantsSliderAdapter(Context context, ArrayList<RestaurantsModel> restaurantList) {
        this.context = context;
        this.restaurantList = restaurantList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return restaurantList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View myImageLayout = inflater.inflate(R.layout.reataurants_slider, view, false);
        ConstraintLayout clBottomBar = (ConstraintLayout) myImageLayout.findViewById(R.id.clBottomBar);
        clBottomBar.bringToFront();
        TextView tvTitle = (TextView) myImageLayout.findViewById(R.id.tvRestaurantTitle);
        TextView tvType = (TextView) myImageLayout.findViewById(R.id.tvRestaurantType);
        TextView tvDuration = (TextView) myImageLayout.findViewById(R.id.tvDuration);
        ImageView myImage = (ImageView) myImageLayout.findViewById(R.id.ivRestaurantImage);
//        myImage.setImageResource(images.get(position));
//        Glide.with(context).load(restaurantList.get(position).getImageUrl()).into(myImage);
//        Glide.with(view.getContext()).load(restaurantList.get(position).getImageUrl()).placeholder(R.drawable.pic1).dontAnimate().into(myImage);
        Glide.with(context).load(restaurantList.get(position).getPic()).into(myImage);
        tvTitle.setText(restaurantList.get(position).getName());
        tvDuration.setText(restaurantList.get(position).getDuration());
        tvType.setText(restaurantList.get(position).getType());

        view.addView(myImageLayout, 0);

        myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "" + position + restaurantList.get(position).getPic(), Toast.LENGTH_SHORT).show();
                Intent restaurantIntent = new Intent(context, RestaurantActivity.class);
                restaurantIntent.putExtra("restaurantID", restaurantList.get(position).getId());
                restaurantIntent.putExtra("restaurantTitle", restaurantList.get(position).getName());
                restaurantIntent.putExtra("restaurantDuration", restaurantList.get(position).getDuration());
                restaurantIntent.putExtra("restaurantImage", restaurantList.get(position).getPic());
                restaurantIntent.putExtra("restaurantDescription", restaurantList.get(position).getType());
                context.startActivity(restaurantIntent);
            }
        });

        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}