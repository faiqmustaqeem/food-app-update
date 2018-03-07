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
import com.codiansoft.foodapp.GlobalClass;
import com.codiansoft.foodapp.R;
import com.codiansoft.foodapp.RestaurantActivity;
import com.codiansoft.foodapp.RestaurantReservationActivity;
import com.codiansoft.foodapp.model.RestaurantsModel;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by salal-khan on 7/3/2017.
 */

public class RestaurantsRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<RestaurantsModel> restaurant;
    Context sContext;

    public RestaurantsRVAdapter(Context context, List<RestaurantsModel> quizzes) {
        this.restaurant = quizzes;
        sContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        if (viewType == 0) {
            //inflate your layout and pass it to view holder
            final LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
            final View v = layoutInflater.inflate(R.layout.restaurants_rv_header, viewGroup, false);
            return new MyHeaderViewHolder(v);
        } else if (viewType == 1) {
            //inflate your layout and pass it to view holder
            final LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
            final View v = layoutInflater.inflate(R.layout.restaurants_rv_item, viewGroup, false);
            return new MyViewHolder(v);

        }

        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int i) {
        Log.d("Size", String.valueOf(restaurant.size()));

        if (holder instanceof MyHeaderViewHolder) {

        } else {
//            Glide.with(sContext).load(restaurant.get(i).getPic()).into(((MyViewHolderSingle) holder).pic);
            Picasso.with(sContext).load(restaurant.get(i).getPic()).into(((MyViewHolder) holder).pic);
            ((MyViewHolder) holder).title.setText(restaurant.get(i).getName());
            ((MyViewHolder) holder).duration.setText(restaurant.get(i).getDuration());
            ((MyViewHolder) holder).type.setText(restaurant.get(i).getType());

            ((MyViewHolder) holder).pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (GlobalClass.restaurantServiceType.equals("reservation")) {
                            Intent restaurantIntent = new Intent(sContext, RestaurantReservationActivity.class);
                            restaurantIntent.putExtra("restaurantID", restaurant.get(i).getId());
                            restaurantIntent.putExtra("restaurantTitle", restaurant.get(i).getName());
                            restaurantIntent.putExtra("restaurantDuration", restaurant.get(i).getDuration());
                            restaurantIntent.putExtra("restaurantImage", restaurant.get(i).getPic());
                            restaurantIntent.putExtra("restaurantDescription", restaurant.get(i).getType());
                            GlobalClass.selectedRestaurantID=restaurant.get(i).getId();
                            GlobalClass.selectedRestaurantBranchID=restaurant.get(i).getBranchId();
                            sContext.startActivity(restaurantIntent);
                        } else {
                            Intent restaurantIntent = new Intent(sContext, RestaurantActivity.class);
                            restaurantIntent.putExtra("restaurantID", restaurant.get(i).getId());
                            restaurantIntent.putExtra("restaurantTitle", restaurant.get(i).getName());
                            restaurantIntent.putExtra("restaurantDuration", restaurant.get(i).getDuration());
                            restaurantIntent.putExtra("restaurantImage", restaurant.get(i).getPic());
                            restaurantIntent.putExtra("restaurantDescription", restaurant.get(i).getType());
                            GlobalClass.selectedRestaurantID=restaurant.get(i).getId();
                            GlobalClass.selectedRestaurantBranchID=restaurant.get(i).getBranchId();
                            sContext.startActivity(restaurantIntent);
                        }
                    } catch (NullPointerException e) {
                        Intent restaurantIntent = new Intent(sContext, RestaurantActivity.class);
                        restaurantIntent.putExtra("restaurantID", restaurant.get(i).getId());
                        restaurantIntent.putExtra("restaurantTitle", restaurant.get(i).getName());
                        restaurantIntent.putExtra("restaurantDuration", restaurant.get(i).getDuration());
                        restaurantIntent.putExtra("restaurantImage", restaurant.get(i).getPic());
                        restaurantIntent.putExtra("restaurantDescription", restaurant.get(i).getType());
                        GlobalClass.selectedRestaurantID=restaurant.get(i).getId();
                        GlobalClass.selectedRestaurantBranchID=restaurant.get(i).getBranchId();
                        sContext.startActivity(restaurantIntent);
                    }
                }
            });
        }

//        Glide.with(sContext).load("https://s-media-cache-ak0.pinimg.com/236x/f5/6b/7a/f56b7a0f79e93d919b5deb93c054f7cb.jpg?noindex=1").centerCrop().into(holder.userpic);

//        holder.noti_title.setText(restaurant.get(i).getNoti_name());
//holder.Readmore.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View view) {
//        ((DrawerActivity)sContext).getSupportFragmentManager().beginTransaction().
//                replace(R.id.fragment_placeholder, new FragmentDeviceDetails(), "FragmentDeviceDetails").addToBackStack("FragmentDeviceDetails").commit();

//    }
//});
//
//        if (!restaurant.get(i).getNoti_pic().equals("")) {
//          byte[] decodedString = Base64.decode(restaurant.get(i).getNoti_pic(), Base64.DEFAULT);
//           holder.noti_layout.setBackground(new BitmapDrawable(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length)));
//        }

    }

    @Override
    public int getItemCount() {
        Log.d("Size", String.valueOf(restaurant.size()));
        return restaurant.size();
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
        TextView duration;
        ImageView pic;

        MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tvTitle);
            duration = (TextView) itemView.findViewById(R.id.tvDuration);
            type = (TextView) itemView.findViewById(R.id.tvFoodType);
            pic = (ImageView) itemView.findViewById(R.id.ivRestaurantPic);
        }

        @Override
        public void onClick(View v) {
            /*switch (v.getId()) {
                case R.id.username:
                    Intent i1 = new Intent(sContext, ProfileActivity.class);
                    AppConstants.selectedUserID = restaurant.get(getAdapterPosition()).getId();
                    AppConstants.selectedUserName = restaurant.get(getAdapterPosition()).getName();
                    AppConstants.selectedUserProfilePic = restaurant.get(getAdapterPosition()).getPic();
                    sContext.startActivity(i1);
                    break;
                case R.id.profile_image:
                    Toast.makeText(sContext, "click" + restaurant.get(getPosition()).getName(), Toast.LENGTH_SHORT).show();
                    Intent i2 = new Intent(sContext, ProfileActivity.class);
                    AppConstants.selectedUserID = restaurant.get(getAdapterPosition()).getId();
                    AppConstants.selectedUserName = restaurant.get(getAdapterPosition()).getName();
                    AppConstants.selectedUserProfilePic = restaurant.get(getAdapterPosition()).getPic();
                    sContext.startActivity(i2);
                    break;
                case R.id.bFollow:
                    bFollow.setText("Following");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        AppConstants.selectedUserID = restaurant.get(getAdapterPosition()).getId();
                        followUnFollow();
                    }
                    break;
            }*/
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }


    public class MyHeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView title;
        TextView type;
        TextView duration;
        ImageView pic;

        MyHeaderViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tvTitle);
            duration = (TextView) itemView.findViewById(R.id.tvDuration);
            type = (TextView) itemView.findViewById(R.id.tvFoodType);
            pic = (ImageView) itemView.findViewById(R.id.ivRestaurantPic);
        }

        @Override
        public void onClick(View v) {
            /*switch (v.getId()) {
                case R.id.username:
                    Intent i1 = new Intent(sContext, ProfileActivity.class);
                    AppConstants.selectedUserID = restaurant.get(getAdapterPosition()).getId();
                    AppConstants.selectedUserName = restaurant.get(getAdapterPosition()).getName();
                    AppConstants.selectedUserProfilePic = restaurant.get(getAdapterPosition()).getPic();
                    sContext.startActivity(i1);
                    break;
                case R.id.profile_image:
                    Toast.makeText(sContext, "click" + restaurant.get(getPosition()).getName(), Toast.LENGTH_SHORT).show();
                    Intent i2 = new Intent(sContext, ProfileActivity.class);
                    AppConstants.selectedUserID = restaurant.get(getAdapterPosition()).getId();
                    AppConstants.selectedUserName = restaurant.get(getAdapterPosition()).getName();
                    AppConstants.selectedUserProfilePic = restaurant.get(getAdapterPosition()).getPic();
                    sContext.startActivity(i2);
                    break;
                case R.id.bFollow:
                    bFollow.setText("Following");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        AppConstants.selectedUserID = restaurant.get(getAdapterPosition()).getId();
                        followUnFollow();
                    }
                    break;
            }*/
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

}