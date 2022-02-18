package com.db.newecom.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.db.newecom.Model.FilterList;
import com.db.newecom.R;
import com.db.newecom.interfaces.FilterType;

import java.util.List;

public class FilterListAdapter extends RecyclerView.Adapter<FilterListAdapter.ViewHolder> {

    private Activity activity;
    private int lastSelectedPosition = 0;
    private String type;
    private FilterType filterType;
    private List<FilterList> filterLists;

    public FilterListAdapter(Activity activity, String type, List<FilterList> filterLists, FilterType filterType) {
        this.activity = activity;
        this.type = type;
        this.filterLists = filterLists;
        this.filterType = filterType;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_filter_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tv_filter_list.setText(filterLists.get(position).getName());

        holder.rl_filter_list.setOnClickListener(view -> {
            lastSelectedPosition = position;
            notifyDataSetChanged();
            filterType.select(filterLists.get(position).getName(), type, filterLists.get(position).getType());
        });


        if (lastSelectedPosition == position){
            holder.rl_filter_list.setBackgroundColor(activity.getResources().getColor(R.color.white));
            holder.tv_filter_list.setTextColor(activity.getResources().getColor(R.color.colorPrimary));
        }
        else{
            holder.rl_filter_list.setBackgroundColor(activity.getResources().getColor(R.color.background_color));
            holder.tv_filter_list.setTextColor(activity.getResources().getColor(R.color.black));
        }
    }

    @Override
    public int getItemCount() {
        return filterLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_filter_list;
        private RelativeLayout rl_filter_list;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_filter_list = itemView.findViewById(R.id.tv_filter_list);
            rl_filter_list = itemView.findViewById(R.id.rl_filter_list);
        }
    }
}
