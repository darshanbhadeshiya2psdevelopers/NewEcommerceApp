package com.db.newecom.adapters;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.db.newecom.Model.CategoryList;
import com.db.newecom.R;

import java.util.List;

public class SearchCatAdapter extends RecyclerView.Adapter<SearchCatAdapter.ViewHolder> {

    private Activity activity;
    private List<CategoryList> categoryList;

    public SearchCatAdapter(Activity activity, List<CategoryList> categoryList) {
        this.activity = activity;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public SearchCatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_search_cat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchCatAdapter.ViewHolder holder, int position) {

        holder.search_cat_name.setText(categoryList.get(position).getCategory_name());

        holder.ll_search_cat.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putString("title", categoryList.get(position).getCategory_name());
            args.putString("cat_id", categoryList.get(position).getId());

            if (categoryList.get(position).getSub_cat_status().equals("0")) {
                args.putString("type", "home_cat");
                Navigation.findNavController(v).navigate(R.id.navigate_to_products_fragment, args);
            }
            else
                Navigation.findNavController(v).navigate(R.id.navigate_to_subcategory_fragment,args);
        });

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout ll_search_cat;
        private TextView search_cat_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ll_search_cat = itemView.findViewById(R.id.ll_search_cat);
            search_cat_name = itemView.findViewById(R.id.search_cat_name);
        }
    }
}
