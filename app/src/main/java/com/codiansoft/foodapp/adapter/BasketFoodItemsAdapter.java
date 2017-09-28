package com.codiansoft.foodapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codiansoft.foodapp.GlobalClass;
import com.codiansoft.foodapp.R;
import com.codiansoft.foodapp.model.SelectedFoodModel;

import java.util.ArrayList;

/**
 * Created by Codiansoft on 9/6/2017.
 */

public class BasketFoodItemsAdapter extends RecyclerView.Adapter<BasketFoodItemsAdapter.MyViewHolder> {

    public BasketFoodItemsAdapter() {

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvItemTitle, tvItemChoices, tvItemPrice, tvQuantity;


        public MyViewHolder(View view) {
            super(view);
            tvItemTitle = (TextView) view.findViewById(R.id.tvItemTitle);
            tvItemChoices = (TextView) view.findViewById(R.id.tvItemChoices);
            tvItemPrice = (TextView) view.findViewById(R.id.tvItemPrice);
            tvQuantity = (TextView) view.findViewById(R.id.tvQuantity);
        }
    }

    @Override
    public BasketFoodItemsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.basket_rv_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BasketFoodItemsAdapter.MyViewHolder holder, int position) {
        SelectedFoodModel dataModel = GlobalClass.selectedFoodListInBasket.get(position);
        holder.tvItemTitle.setText(dataModel.getTitle());
        holder.tvItemChoices.setText(dataModel.getChoices());
        holder.tvItemPrice.setText(dataModel.getPrice());
        holder.tvQuantity.setText(dataModel.getQuantity());
    }

    @Override
    public int getItemCount() {
        return GlobalClass.selectedFoodListInBasket.size();
    }
}
