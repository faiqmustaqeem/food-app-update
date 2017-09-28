package com.codiansoft.foodapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codiansoft.foodapp.FoodActivity;
import com.codiansoft.foodapp.R;
import com.codiansoft.foodapp.model.DealsModel;

import java.util.ArrayList;

/**
 * Created by salal-khan on 7/1/2017.
 */

public class DealsSliderAdapter extends PagerAdapter {

    private ArrayList<DealsModel> popularDealsList;
    private LayoutInflater inflater;
    private Context context;

    public DealsSliderAdapter(Context context, ArrayList<DealsModel> popularDealsList) {
        this.context = context;
        this.popularDealsList = popularDealsList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return popularDealsList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View myImageLayout = inflater.inflate(R.layout.deals_slider, view, false);
        TextView tvTitle = (TextView) myImageLayout.findViewById(R.id.tvDealTitle);
        TextView tvType = (TextView) myImageLayout.findViewById(R.id.tvFoodType);
        TextView tvPrice = (TextView) myImageLayout.findViewById(R.id.tvPrice);
        TextView tvDuration = (TextView) myImageLayout.findViewById(R.id.tvDuration);
        ImageView myImage = (ImageView) myImageLayout.findViewById(R.id.image);
//        myImage.setImageResource(images.get(position));
//        Glide.with(context).load(popularDealsList.get(position).getImageUrl()).into(myImage);
//        Glide.with(view.getContext()).load(popularDealsList.get(position).getImageUrl()).placeholder(R.drawable.pic1).dontAnimate().into(myImage);
        Glide.with(context).load(popularDealsList.get(position).getImageUrl()).into(myImage);
        tvTitle.setText(popularDealsList.get(position).getTitle());
        tvDuration.setText(popularDealsList.get(position).getDuration());
        tvPrice.setText(popularDealsList.get(position).getPrice());
        tvType.setText(popularDealsList.get(position).getType());

        view.addView(myImageLayout, 0);

        myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, FoodActivity.class);
                i.putExtra("foodID", popularDealsList.get(position).getDealID());
                i.putExtra("foodTitle", popularDealsList.get(position).getTitle());
                i.putExtra("foodDescription", popularDealsList.get(position).getDescription());
                i.putExtra("foodPrice", popularDealsList.get(position).getPrice());
                i.putExtra("foodImage", popularDealsList.get(position).getImageUrl());
                context.startActivity(i);
            }
        });

        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}