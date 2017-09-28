package com.codiansoft.foodapp.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codiansoft.foodapp.R;
import com.codiansoft.foodapp.model.FragmentOneDataModel;

import java.util.ArrayList;

/**
 * Created by Codiansoft on 8/17/2017.
 */

public class ResFragOneAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    private ArrayList<FragmentOneDataModel> fragOneItems;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle, tvPrice, tvDescription;
        ImageView ivImage;

        public MyViewHolder(View view) {
            super(view);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            tvDescription = (TextView) view.findViewById(R.id.tvDescription);
            tvPrice = (TextView) view.findViewById(R.id.tvPrice);
            ivImage = (ImageView) view.findViewById(R.id.ivImage);
        }
    }

    public class SectionViewHolder extends RecyclerView.ViewHolder {
        public TextView tvSectionTitle;

        public SectionViewHolder(View itemView) {
            super(itemView);
            tvSectionTitle = (TextView) itemView.findViewById(R.id.tvSectionTitle);
        }
    }


    public ResFragOneAdapter(FragmentActivity mContext, ArrayList<FragmentOneDataModel> fragOneItems) {
        this.fragOneItems = fragOneItems;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

/*        if (viewType == 0) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.res_frag_one_item, parent, false);
        }
        return new MyViewHolderSingle(itemView);*/


        if (viewType == 0) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.res_frag_one_item, parent, false);
            return new MyViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.res_frag_one_section_item, parent, false);
            return new SectionViewHolder(itemView);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FragmentOneDataModel fragOneItem = fragOneItems.get(position);

        if (fragOneItem.isRow()) {
            MyViewHolder h = (MyViewHolder) holder;

            h.tvTitle.setText(fragOneItem.getTitle());
            h.tvDescription.setText(fragOneItem.getDescription());
            h.tvPrice.setText(fragOneItem.getPrice());
            Glide.with(mContext).load(fragOneItem.getImageURL()).into(h.ivImage);
        } else {
            SectionViewHolder h = (SectionViewHolder) holder;
            h.tvSectionTitle.setText(fragOneItem.getSectionTitle());
        }
    }

    @Override
    public int getItemCount() {
        return fragOneItems.size();
    }

    @Override
    public int getItemViewType(int position) {
//        super.getItemViewType(position);
        FragmentOneDataModel item = fragOneItems.get(position);
        if (item.isRow()) {
            return 0;
        } else {
            return 1;
        }
    }
}