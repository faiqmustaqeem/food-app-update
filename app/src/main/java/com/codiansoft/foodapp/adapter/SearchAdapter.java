package com.codiansoft.foodapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codiansoft.foodapp.R;
import com.codiansoft.foodapp.model.RestaurantsModel;

import java.util.List;

/**
 * Created by CodianSoft on 01/01/2018.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {

    private List<RestaurantsModel> restaurantsModelList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
//        public TextView title, year, genre;
ImageView image ;
        TextView name , duration , type;

        public MyViewHolder(View view) {
            super(view);
//            title = (TextView) view.findViewById(R.id.title);
//            genre = (TextView) view.findViewById(R.id.genre);
//            year = (TextView) view.findViewById(R.id.year);
            image=view.findViewById(R.id.ivRestaurantPic);
            name=view.findViewById(R.id.tvTitle);
            type=view.findViewById(R.id.tvFoodType);
            duration=view.findViewById(R.id.tvDuration);

        }
    }

    public SearchAdapter(Context context, List<RestaurantsModel> list)
    {
        this.context=context;
        this.restaurantsModelList=list;
    }
    @Override
    public SearchAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.restaurants_rv_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SearchAdapter.MyViewHolder holder, int position) {
        RestaurantsModel restaurantsModel=restaurantsModelList.get(position);
        holder.name.setText(restaurantsModel.getName());
        holder.type.setText(restaurantsModel.getType());
        holder.duration.setText(restaurantsModel.getDuration());
        Glide.with(context).load(restaurantsModel.getPic()).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return restaurantsModelList.size();
    }
}
