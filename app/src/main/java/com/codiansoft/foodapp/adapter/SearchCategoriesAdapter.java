package com.codiansoft.foodapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codiansoft.foodapp.R;
import com.codiansoft.foodapp.model.RestaurantsModel;
import com.codiansoft.foodapp.model.SearchCategoriesModel;

import java.util.ArrayList;

import static com.codiansoft.foodapp.fragment.SearchFragment.fetchRestaurants;
import static com.codiansoft.foodapp.fragment.SearchFragment.gvSearchCategories;
import static com.codiansoft.foodapp.fragment.SearchFragment.restaurantsModel;
import static com.codiansoft.foodapp.fragment.SearchFragment.rvSearchedRestaurants;
import static com.codiansoft.foodapp.fragment.SearchFragment.searchRVadapter;
import static com.codiansoft.foodapp.fragment.SearchFragment.searchedRestaurantslist;

/**
 * Created by Codiansoft on 8/28/2017.
 */

public class SearchCategoriesAdapter extends BaseAdapter implements Filterable {
    ArrayList<SearchCategoriesModel> searchCategoriesModelArrayList;
    SearchCategoriesModel searchCategoriesModel;
    private ArrayList<SearchCategoriesModel> mDisplayedValues;    // Values to be displayed

    Context context;
    private static LayoutInflater inflater = null;

    public SearchCategoriesAdapter(Context context, ArrayList<SearchCategoriesModel> searchCategoriesModelArrayList) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.searchCategoriesModelArrayList = searchCategoriesModelArrayList;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return searchCategoriesModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return searchCategoriesModelArrayList.get(position).getTitle();
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }


    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                mDisplayedValues = (ArrayList<SearchCategoriesModel>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<SearchCategoriesModel> FilteredArrList = new ArrayList<SearchCategoriesModel>();

                if (searchCategoriesModelArrayList == null) {
                    searchCategoriesModelArrayList = new ArrayList<SearchCategoriesModel>(mDisplayedValues); // saves the original data in mOriginalValues
                }
                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = searchCategoriesModelArrayList.size();
                    results.values = searchCategoriesModelArrayList;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < searchCategoriesModelArrayList.size(); i++) {
                        String data = searchCategoriesModelArrayList.get(i).getTitle();
                        if (data.toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrList.add(new SearchCategoriesModel(searchCategoriesModelArrayList.get(i).getID(), searchCategoriesModelArrayList.get(i).getImage(), searchCategoriesModelArrayList.get(i).getTitle()));
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }


    public class Holder {
        TextView tvCategoryTitle;
        ImageView ivCategoryImage;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = new Holder();
        final View rowView;

        rowView = inflater.inflate(R.layout.search_categories_item, null);
        holder.tvCategoryTitle = (TextView) rowView.findViewById(R.id.tvCategoryTitle);
        holder.ivCategoryImage = (ImageView) rowView.findViewById(R.id.ivCategoryImage);

        final SearchCategoriesModel searchCategoriesModel = searchCategoriesModelArrayList.get(position);

        holder.tvCategoryTitle.setText(searchCategoriesModel.getTitle());
        Glide.with(context).load(searchCategoriesModel.getImage()).into(holder.ivCategoryImage);

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(rowView.getWindowToken(), 0);
                gvSearchCategories.setVisibility(View.GONE);
                rvSearchedRestaurants.setVisibility(View.VISIBLE);

                Toast.makeText(context, ""+searchCategoriesModelArrayList.get(position).getTitle(), Toast.LENGTH_SHORT).show();

                searchedRestaurantslist = new ArrayList<>();
                restaurantsModel = new RestaurantsModel("", "", "", "", "");
                searchedRestaurantslist.add(restaurantsModel);

                fetchRestaurants();

            }
        });
        return rowView;
    }
}