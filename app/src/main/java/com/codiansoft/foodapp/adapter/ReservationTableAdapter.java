package com.codiansoft.foodapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codiansoft.foodapp.R;
import com.codiansoft.foodapp.model.ReservationTableModel;

import java.util.ArrayList;

/**
 * Created by Codiansoft on 9/21/2017.
 */

public class ReservationTableAdapter extends RecyclerView.Adapter<ReservationTableAdapter.MyViewHolder> {

    private ArrayList<ReservationTableModel> tablesList;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, tvCapacity;
        LinearLayout llTableItem;
        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tvTableTitle);
            llTableItem = (LinearLayout) view.findViewById(R.id.llTableItem);
            tvCapacity = (TextView) view.findViewById(R.id.tvCapacity);
        }
    }

    public ReservationTableAdapter(Context context, ArrayList<ReservationTableModel> tablesList) {
        this.tablesList = tablesList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reservation_table_rv_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ReservationTableModel reservationTableModel = tablesList.get(position);
        holder.title.setText(reservationTableModel.getTitle());
       // holder.tvCapacity.setText("Capacity: " + reservationTableModel.getCapacity());
    }

    @Override
    public int getItemCount() {
        return tablesList.size();
    }
}