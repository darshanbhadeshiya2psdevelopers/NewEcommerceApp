package com.db.newecom.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.db.newecom.Model.FilterSortByList;
import com.db.newecom.R;
import com.db.newecom.interfaces.FilterType;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class SortByAdapter extends RecyclerView.Adapter<SortByAdapter.ViewHolder> {

    private Activity activity;
    private String type;
    private FilterType filterType;
    private List<FilterSortByList> filterSortByLists;

    public SortByAdapter(Activity activity, String type, List<FilterSortByList> filterSortByLists, FilterType filterType) {
        this.activity = activity;
        this.type = type;
        this.filterType = filterType;
        this.filterSortByLists = filterSortByLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_filter_sortby, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.sortby_type.setText(filterSortByLists.get(position).getTitle());

        holder.cardview_sortby.setOnClickListener(v -> {
            for (int i = 0; i < filterSortByLists.size(); i++) {
                if (i == position) {
                    filterSortByLists.get(i).setSelect(true);
                }else {
                    filterSortByLists.get(i).setSelect(false);
                }
            }
            notifyDataSetChanged();
        });

        if (filterSortByLists.get(position).isSelect()) {
            holder.cardview_sortby.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
            holder.sortby_type.setTextColor(activity.getResources().getColor(R.color.white));
            filterType.select(filterSortByLists.get(position).getTitle(), type, filterSortByLists.get(position).getSort());
        }else {
            filterSortByLists.get(position).setSelect(false);
            holder.cardview_sortby.setBackgroundColor(activity.getResources().getColor(R.color.white));
            holder.sortby_type.setTextColor(activity.getResources().getColor(R.color.dark_grey));
        }
    }

    @Override
    public int getItemCount() {
        return filterSortByLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private MaterialCardView cardview_sortby;
        private TextView sortby_type;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardview_sortby = itemView.findViewById(R.id.cardview_sortby);
            sortby_type = itemView.findViewById(R.id.sortby_type);
        }
    }
}
