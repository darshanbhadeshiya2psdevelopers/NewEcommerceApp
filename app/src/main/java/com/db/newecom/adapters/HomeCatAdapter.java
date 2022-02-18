package com.db.newecom.adapters;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.db.newecom.Model.CategoryList;
import com.db.newecom.R;

import java.util.List;

public class HomeCatAdapter extends RecyclerView.Adapter<HomeCatAdapter.ViewHolder> {

    private Activity activity;
    private List<CategoryList> categoryLists;

    public HomeCatAdapter(Activity activity, List<CategoryList> categoryLists) {
        this.activity = activity;
        this.categoryLists = categoryLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_home_category, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (position == 0){
            holder.home_cat_img.setImageResource(R.drawable.all_categories);
            holder.home_cat_name.setText("All Categories");
            holder.home_cat_ll.setOnClickListener(view ->
                    Navigation.findNavController(view).navigate(R.id.nav_cat));
        }
        else {
            Glide.with(activity).load(categoryLists.get(position).getCategory_image_thumb())
                    .placeholder(R.drawable.ic_baseline_category_24)
                    .into(holder.home_cat_img);
            holder.home_cat_name.setText(categoryLists.get(position).getCategory_name());
            holder.home_cat_ll.setOnClickListener(view -> {

                Bundle args = new Bundle();
                args.putString("title", categoryLists.get(position).getCategory_name());
                args.putString("cat_id", categoryLists.get(position).getId());

                if (categoryLists.get(position).getSub_cat_status().equals("0")) {
                    args.putString("type", "home_cat");
                    Navigation.findNavController(view).navigate(R.id.navigate_to_products_fragment, args);
                }
                else
                    Navigation.findNavController(view).navigate(R.id.navigate_to_subcategory_fragment,args);
            });
        }
    }

    @Override
    public int getItemCount() {
        return categoryLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout home_cat_ll;
        private ImageView home_cat_img;
        private TextView home_cat_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            home_cat_ll = itemView.findViewById(R.id.home_cat_ll);
            home_cat_img = itemView.findViewById(R.id.home_cat_img);
            home_cat_name = itemView.findViewById(R.id.home_cat_name);

        }
    }
}
