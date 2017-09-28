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
import com.codiansoft.foodapp.model.FriendsActivitiesModel;

import java.util.ArrayList;

/**
 * Created by Codiansoft on 9/26/2017.
 */

public class FriendActivityAdapter extends RecyclerView.Adapter<FriendActivityAdapter.MyViewHolder> {

    private ArrayList<FriendsActivitiesModel> friendActivityList;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvFriendName, tvActivityTitle, tvDateAndTime;
        ImageView ivProfilePic;

        public MyViewHolder(View view) {
            super(view);
            tvFriendName = (TextView) view.findViewById(R.id.tvFriendName);
            tvActivityTitle = (TextView) view.findViewById(R.id.tvActivityTitle);
            tvDateAndTime = (TextView) view.findViewById(R.id.tvDateAndTime);
            ivProfilePic = (ImageView) view.findViewById(R.id.ivProfilePic);
        }
    }


    public FriendActivityAdapter(Context context, ArrayList<FriendsActivitiesModel> friendActivityList) {
        this.friendActivityList = friendActivityList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friends_activities_rv_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        FriendsActivitiesModel friendsActivitiesModel = friendActivityList.get(position);
        holder.tvFriendName.setText(friendsActivitiesModel.getFriendName());
        holder.tvActivityTitle.setText(friendsActivitiesModel.getActivityTitle());
        holder.tvDateAndTime.setText(friendsActivitiesModel.getActivityDate() + ", " + friendsActivitiesModel.getActivityTime());
        Glide.with(context).load(friendsActivitiesModel.getFriendPic()).into(holder.ivProfilePic);
    }

    @Override
    public int getItemCount() {
        return friendActivityList.size();
    }
}

