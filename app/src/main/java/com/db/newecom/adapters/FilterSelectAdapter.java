package com.db.newecom.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.db.newecom.Model.SizeFilterList;
import com.db.newecom.R;
import com.db.newecom.Utills.ConstantApi;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;
import java.util.List;

public class FilterSelectAdapter extends RecyclerView.Adapter<FilterSelectAdapter.ViewHolder> {

    private Activity activity;
    private List<SizeFilterList> sizeFilterLists, sizeLists;

    public FilterSelectAdapter(Activity activity, List<SizeFilterList> sizeFilterLists) {
        this.activity = activity;
        this.sizeLists = sizeFilterLists;
        this.sizeFilterLists = sizeFilterLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.item_filter_select, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (sizeLists.get(position).getSelected().equals("true")) {
            holder.checkBox.setOnCheckedChangeListener(null);
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setOnCheckedChangeListener(null);
            holder.checkBox.setChecked(false);
        }

        holder.tv_filter_select.setText(sizeLists.get(position).getSize());

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                ConstantApi.sizes_temp = ConstantApi.sizes_temp.concat(sizeLists.get(position).getSize().concat(","));
            } else {
                try {
                    ConstantApi.sizes_temp = ConstantApi.sizes_temp.replace(sizeLists.get(position).getSize() + ",", "");
                } catch (Exception e) {
                    Log.d("error",e.toString());
                }
            }

        });

    }

    @Override
    public int getItemCount() {
        return sizeFilterLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private MaterialCheckBox checkBox;
        private TextView tv_filter_select;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.checkBox_filter_select);
            tv_filter_select = itemView.findViewById(R.id.tv_filter_select);
        }
    }

    public Filter getFilter() {

        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                sizeLists = (List<SizeFilterList>) results.values;
                notifyDataSetChanged();

            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                constraint = constraint.toString().toLowerCase();

                List<SizeFilterList> filterLists = new ArrayList<>();

                // perform your search here using the searchConstraint String.
                for (int i = 0; i < sizeFilterLists.size(); i++) {
                    String dataNames = sizeFilterLists.get(i).getSize();
                    if (dataNames.toLowerCase().startsWith(constraint.toString())) {
                        filterLists.add(sizeFilterLists.get(i));
                        for (int j = 0; j < filterLists.size(); j++) {
                            if (ConstantApi.sizes_temp.contains(filterLists.get(j).getSize().concat(","))) {
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
