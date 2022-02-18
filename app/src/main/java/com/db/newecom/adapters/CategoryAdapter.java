package com.db.newecom.adapters;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.db.newecom.Model.CategoryList;
import com.db.newecom.Model.SubCategoryList;
import com.db.newecom.R;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private Activity activity;
    boolean issubcategory = false;
    private List<CategoryList> categoryLists;
    private List<SubCategoryList> subCategoryLists;

    public CategoryAdapter(Activity activity, boolean issubcategory, List<SubCategoryList> subCategoryLists) {
        this.activity = activity;
        this.issubcategory = issubcategory;
        this.subCategoryLists = subCategoryLists;
    }

    public CategoryAdapter(Activity activity, List<CategoryList> categoryLists) {
        this.activity = activity;
        this.categoryLists = categoryLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (issubcategory){

            Glide.with(activity).load(subCategoryLists.get(position).getSub_category_image_thumb())
                    .placeholder(R.drawable.placeholder_img)
                    .into(holder.category_img);

            holder.category_name.setText(subCategoryLists.get(position).getSub_category_name());

            holder.cardview_category.setOnClickListener(view -> {
                Bundle args = new Bundle();
                args.putString("title", subCategoryLists.get(position).getSub_category_name());
                args.putString("cat_id", subCategoryLists.get(position).getCategory_id());
                args.putString("subcat_id", subCategoryLists.get(position).getId());
                args.putString("type", "sub_cat_pro");
                Navigation.findNavController(view).navigate(R.id.navigate_to_products_fragment,args);
            });
        }
        else {

            Glide.with(activity).load(categoryLists.get(position).getCategory_image_thumb())
                    .placeholder(R.drawable.placeholder_img)
                    .into(holder.category_img);

            holder.category_name.setText(categoryLists.get(position).getCategory_name());
            holder.cardview_category.setOnClickListener(view -> {

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
        if (issubcategory)
            return subCategoryLists.size();
        else
            return categoryLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private MaterialCardView cardview_category;
        private ImageView category_img;
        private TextView category_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardview_category = itemView.findViewById(R.id.cardview_category);
            category_img = itemView.findViewById(R.id.category_img);
            category_name = itemView.findViewById(R.id.category_name);
        }
    }
}
