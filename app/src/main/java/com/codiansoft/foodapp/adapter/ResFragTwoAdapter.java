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
import com.codiansoft.foodapp.model.FragmentTwoDataModel;

import java.util.ArrayList;

/**
 * Created by Codiansoft on 8/21/2017.
 */

public class ResFragTwoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    private ArrayList<FragmentTwoDataModel> fragTwoItems;

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
        private TextView tvSectionTitle;

        public SectionViewHolder(View itemView) {
            super(itemView);
            tvSectionTitle = (TextView) itemView.findViewById(R.id.tvSectionTitle);
        }
    }


    public ResFragTwoAdapter(FragmentActivity mContext, ArrayList<FragmentTwoDataModel> fragTwoItems) {
        this.fragTwoItems = fragTwoItems;
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
            return new ResFragTwoAdapter.MyViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.res_frag_one_section_item, parent, false);
            return new ResFragTwoAdapter.SectionViewHolder(itemView);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FragmentTwoDataModel fragTwoItem = fragTwoItems.get(position);

        if (fragTwoItem.isRow()) {
            ResFragTwoAdapter.MyViewHolder h = (ResFragTwoAdapter.MyViewHolder) holder;

            h.tvTitle.setText(fragTwoItem.getTitle());
            h.tvDescription.setText(fragTwoItem.getDescription());
            h.tvPrice.setText(fragTwoItem.getPrice());
            Glide.with(mContext).load(fragTwoItem.getImageURL()).into(h.ivImage);
        } else {
            ResFragTwoAdapter.SectionViewHolder h = (ResFragTwoAdapter.SectionViewHolder) holder;
            h.tvSectionTitle.setText(fragTwoItem.getSectionTitle());
        }
    }

    @Override
    public int getItemCount() {
        return fragTwoItems.size();
    }

    @Override
    public int getItemViewType(int position) {
//        super.getItemViewType(position);
        FragmentTwoDataModel item = fragTwoItems.get(position);
        if (item.isRow()) {
            return 0;
        } else {
            return 1;
        }
    }
}
