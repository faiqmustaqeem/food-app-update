package com.codiansoft.foodapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codiansoft.foodapp.R;
import com.codiansoft.foodapp.model.FoodModel;

import java.util.ArrayList;

import static com.codiansoft.foodapp.FoodActivity.selectedFoodChoicesString;

/**
 * Created by Codiansoft on 8/24/2017.
 */

public class FoodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    private ArrayList<FoodModel> foodChoices;
    private ArrayList<String> selectedTexts = new ArrayList<String>();

    public class MyViewHolderSingle extends RecyclerView.ViewHolder {
        RadioButton rbSingleChoice;
        TextView tvPrice, tvCurrencyCode;


        public MyViewHolderSingle(View view) {
            super(view);
            rbSingleChoice = (RadioButton) view.findViewById(R.id.rbSingleChoice);
            tvPrice=view.findViewById(R.id.tvPrice);
            tvCurrencyCode =  view.findViewById(R.id.tvCurrencyCode);
        }
    }

    // variation start

    public class MyViewHolderVariation extends RecyclerView.ViewHolder {
        RadioButton rbSingleChoice;
        TextView tvPrice, tvCurrencyCode;


        public MyViewHolderVariation(View view) {
            super(view);
            rbSingleChoice =  view.findViewById(R.id.rbSingleChoice);
            tvPrice=view.findViewById(R.id.tvPrice);
            tvCurrencyCode =  view.findViewById(R.id.tvCurrencyCode);
        }
    }
    // vriation end

    public class MyViewHolderMultiple extends RecyclerView.ViewHolder {
        CheckBox cbMultipleChoice;
        TextView tvPrice, tvCurrencyCode;

        public MyViewHolderMultiple(View view) {
            super(view);
            cbMultipleChoice = (CheckBox) view.findViewById(R.id.cbMultipleChoice);
            tvPrice = (TextView) view.findViewById(R.id.tvPrice);
            tvCurrencyCode = (TextView) view.findViewById(R.id.tvCurrencyCode);
        }
    }

    public class SectionViewHolder extends RecyclerView.ViewHolder {
        private TextView tvSectionTitle, tvRequired;

        public SectionViewHolder(View itemView) {
            super(itemView);
            tvSectionTitle = (TextView) itemView.findViewById(R.id.tvSectionTitle);
            tvRequired = (TextView) itemView.findViewById(R.id.tvRequired);
        }
    }

    public FoodAdapter(Context mContext, ArrayList<FoodModel> foodChoices) {
        this.foodChoices = foodChoices;
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
                    .inflate(R.layout.food_choice_item_single, parent, false);
            return new MyViewHolderSingle(itemView);
        } else if (viewType == 1) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.food_choice_item_multiple, parent, false);
            return new MyViewHolderMultiple(itemView);
        }else if(viewType==2)
        {
            View itemView=LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.variation_item_single,parent,false);
            return new MyViewHolderVariation(itemView);
        }

        else {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.food_choice_section_item, parent, false);
            return new FoodAdapter.SectionViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final FoodModel foodChoicesModel = foodChoices.get(position);

        if (foodChoicesModel.isRow()) {

            if (foodChoicesModel.getSectionCategory().equals("single")) {
                final MyViewHolderSingle h = (MyViewHolderSingle) holder;
                Log.e("category",foodChoicesModel.getSectionCategory());
                h.rbSingleChoice.setText(foodChoicesModel.getTitle());
                h.rbSingleChoice.setChecked(foodChoicesModel.getSingleChoiceSelection());
                h.tvPrice.setText(foodChoicesModel.getPrice());

                h.rbSingleChoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            foodChoicesModel.setSingleChoiceSelection(true);
                            for (int i = 0; i < foodChoices.size(); i++) {
                                if (i == position) continue;
                                else {
                                    FoodModel foodChoicesModel = foodChoices.get(i);
                                    foodChoicesModel.setSingleChoiceSelection(false);

                                    selectedTexts.add(foodChoicesModel.getTitle() + "\n");

                                    /*if (!selectedFoodChoicesString.contains(h.rbSingleChoice.getText().toString()))
                                        selectedFoodChoicesString = selectedFoodChoicesString + h.rbSingleChoice.getText().toString() + "\n";*/
                                }
                            }
                            notifyDataSetChanged();
                        } else {
                            selectedTexts.remove(foodChoicesModel.getTitle() + "\n");
                        }
                    }
                });

            }
           else  if (foodChoicesModel.getSectionCategory().equals("variation")) {
                final MyViewHolderVariation h = (MyViewHolderVariation) holder;
                Log.e("category",foodChoicesModel.getSectionCategory());
                h.rbSingleChoice.setText(foodChoicesModel.getTitle());
                h.rbSingleChoice.setChecked(foodChoicesModel.getSingleChoiceVariation());
                h.tvPrice.setText(foodChoicesModel.getPrice());

                h.rbSingleChoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            foodChoicesModel.setSingleChoiceVariation(true);
                            for (int i = 0; i < foodChoices.size(); i++) {
                                if (i == position) continue;
                                else {
                                    FoodModel foodChoicesModel = foodChoices.get(i);
                                    foodChoicesModel.setSingleChoiceVariation(false);

                                    selectedTexts.add(foodChoicesModel.getTitle() + "\n");

                                    /*if (!selectedFoodChoicesString.contains(h.rbSingleChoice.getText().toString()))
                                        selectedFoodChoicesString = selectedFoodChoicesString + h.rbSingleChoice.getText().toString() + "\n";*/
                                }
                            }
                            notifyDataSetChanged();
                        } else {
                            selectedTexts.remove(foodChoicesModel.getTitle() + "\n");
                        }
                    }
                });

            }
            else if (foodChoicesModel.getSectionCategory().equals("multiple")) {
                final MyViewHolderMultiple h = (MyViewHolderMultiple) holder;

                if (foodChoicesModel.getPrice().equals("0")) {
                    h.tvPrice.setVisibility(View.GONE);
                    h.tvCurrencyCode.setVisibility(View.GONE);
                } else {
                    h.tvPrice.setText(foodChoicesModel.getPrice());
                }
                h.cbMultipleChoice.setText(foodChoicesModel.getTitle());

                h.cbMultipleChoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (!selectedFoodChoicesString.contains(h.cbMultipleChoice.getText().toString()))
                            selectedFoodChoicesString = selectedFoodChoicesString + h.cbMultipleChoice.getText().toString() + "\n";
                    }
                });
            }

        } else {
            SectionViewHolder h = (FoodAdapter.SectionViewHolder) holder;
            h.tvSectionTitle.setText(foodChoicesModel.getSectionTitle());
            if (foodChoicesModel.isSectionRequired()) h.tvRequired.setVisibility(View.VISIBLE);
            else h.tvRequired.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return foodChoices.size();
    }

    @Override
    public int getItemViewType(int position) {
//        super.getItemViewType(position);
        FoodModel item = foodChoices.get(position);
        if (item.isRow() & item.getSectionCategory().equals("single")) {
            return 0;
        } else if (item.isRow() & item.getSectionCategory().equals("multiple")) {
            return 1;
        } else if(item.isRow() & item.getSectionCategory().equals("variation")){
            return 2;
        }
        else
            return 3;
    }
}
