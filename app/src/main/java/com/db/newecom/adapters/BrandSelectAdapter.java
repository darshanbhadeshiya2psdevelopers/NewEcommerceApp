package com.db.newecom.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.db.newecom.Model.BrandFilterList;
import com.db.newecom.R;
import com.db.newecom.Utills.ConstantApi;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;
import java.util.List;

public class BrandSelectAdapter extends RecyclerView.Adapter<BrandSelectAdapter.ViewHolder> {

    private Activity activity;
    private List<BrandFilterList> brandFilterLists, brandLists;

    public BrandSelectAdapter(Activity activity, List<BrandFilterList> brandFilterLists) {
        this.activity = activity;
        this.brandLists = brandFilterLists;
        this.brandFilterLists = brandFilterLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.item_brand_select, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (brandFilterLists.get(position).getSelected().equals("true")) {
            holder.checkBox.setOnCheckedChangeListener(null);
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setOnCheckedChangeListener(null);
            holder.checkBox.setChecked(false);
        }

        String brand = brandFilterLists.get(position).getBrand_name() + " "
                + "(" + brandFilterLists.get(position).getTotal_cnt() + ")";

        holder.brand.setText(brand);

        Glide.with(activity).load(brandFilterLists.get(position).getBrand_image_thumb())
                .placeholder(R.drawable.placeholder_img).into(holder.brand_img);

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                ConstantApi.brand_ids_temp = ConstantApi.brand_ids_temp.concat(brandFilterLists.get(position).getId().concat(","));
            } else {
                try {
                    ConstantApi.brand_ids_temp = ConstantApi.brand_ids_temp.replace(brandFilterLists.get(position).getId() + ",", "");
                } catch (Exception e) {
                    Log.d("error", e.toString());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return brandFilterLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView brand_img;
        private MaterialCheckBox checkBox;
        private TextView brand;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            brand_img = itemView.findViewById(R.id.brand_img);
            checkBox = itemView.findViewById(R.id.brand_select_checkbox);
            brand = itemView.findViewById(R.id.tv_brand);

        }
    }

    public Filter getFilter() {

        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                brandLists = (List<BrandFilterList>) results.values;
                notifyDataSetChanged();

            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                constraint = constraint.toString().toLowerCase();

                List<BrandFilterList> filterLists = new ArrayList<>();

                // perform your search here using the searchConstraint String.
                for (int i = 0; i < brandFilterLists.size(); i++) {
                    String dataNames = brandFilterLists.get(i).getBrand_name();
                    if (dataNames.toLowerCase().startsWith(constraint.toString())) {
                        filterLists.add(brandFilterLists.get(i));
                        for (int j = 0; j < filterLists.size(); j++) {
                            if (ConstantApi.brand_ids_temp.contains(filterLists.get(j).getId().concat(","))) {
                                filterLists.get(j).setSelected("true");
                            } else {
                                filterLists.get(j).setSelected("false");
                            }
                        }
                    }
                }

                results.count = filterLists.size();
                results.values = filterLists;

                return results;
            }
        };

        return filter;
    }

}
