package com.codiansoft.foodapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codiansoft.foodapp.FoodActivity;
import com.codiansoft.foodapp.R;
import com.codiansoft.foodapp.model.MyOrdersModel;

import java.util.ArrayList;

/**
 * Created by Codiansoft on 9/19/2017.
 */

public class MyOrdersRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<MyOrdersModel> historyOrderModelArrayList;
    Context sContext;

    public MyOrdersRVAdapter(Context context, ArrayList<MyOrdersModel> historyOrderModelArrayList) {
        this.historyOrderModelArrayList = historyOrderModelArrayList;
        sContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        //inflate your layout and pass it to view holder
        final LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        final View v = layoutInflater.inflate(R.layout.my_order_rv_item, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int i) {
        Log.d("Size", String.valueOf(historyOrderModelArrayList.size()));

//            Picasso.with(sContext).load(historyOrderModelArrayList.get(i).getImageUrl()).centerCrop().into(((MyViewHolder) holder).pic);
        Glide.with(sContext).load(historyOrderModelArrayList.get(i).getImageUrl()).centerCrop().into(((MyViewHolder) holder).pic);
        ((MyViewHolder) holder).title.setText(historyOrderModelArrayList.get(i).getTitle());
        ((MyViewHolder) holder).description.setText(historyOrderModelArrayList.get(i).getDescription());
        ((MyViewHolder) holder).type.setText(historyOrderModelArrayList.get(i).getType());
        ((MyViewHolder) holder).tvPrice.setText(historyOrderModelArrayList.get(i).getPrice());

        if(!historyOrderModelArrayList.get(i).getStatus().equals("completed")) ((MyViewHolder) holder).tvStatusDot.setVisibility(View.VISIBLE);
        else ((MyViewHolder) holder).tvStatusDot.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        Log.d("Size", String.valueOf(historyOrderModelArrayList.size()));
        return historyOrderModelArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return 0;
        return 1;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView title;
        TextView type;
        TextView description;
        TextView tvPrice;
        TextView tvStatusDot;
        ImageView pic;

        MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tvTitle);
            description = (TextView) itemView.findViewById(R.id.tvDescription);
            type = (TextView) itemView.findViewById(R.id.tvFoodType);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            tvStatusDot = (TextView) itemView.findViewById(R.id.tvStatusDot);
            pic = (ImageView) itemView.findViewById(R.id.ivFoodImage);
        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }
}
