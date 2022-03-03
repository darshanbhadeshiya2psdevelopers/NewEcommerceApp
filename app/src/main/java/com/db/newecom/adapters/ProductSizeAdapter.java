package com.db.newecom.adapters;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.db.newecom.Model.ProSizeList;
import com.db.newecom.R;
import com.db.newecom.ui.fragment.ProductDetailFragment;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class ProductSizeAdapter extends RecyclerView.Adapter<ProductSizeAdapter.ViewHolder> {

    private Activity activity;
    private List<ProSizeList> proSizeLists;
    private int lastSelectedPosition;
    private String title, product_id, slug;

    public ProductSizeAdapter(Activity activity, List<ProSizeList> proSizeLists, int lastSelectedPosition,
                              String title, String product_id, String slug) {
        this.activity = activity;
        this.proSizeLists = proSizeLists;
        this.lastSelectedPosition = lastSelectedPosition;
        this.title = title;
        this.product_id = product_id;
        this.slug = slug;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.item_pro_size, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.pro_size.setText(proSizeLists.get(position).getProduct_size());

        if (lastSelectedPosition == position) {
            holder.cardview_pro_size.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
            holder.pro_size.setTextColor(activity.getResources().getColor(R.color.white));
        }else {
            holder.cardview_pro_size.setBackgroundColor(activity.getResources().getColor(R.color.white));
            holder.pro_size.setTextColor(activity.getResources().getColor(R.color.dark_grey));
        }

    }

    @Override
    public int getItemCount() {
        return proSizeLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private MaterialCardView cardview_pro_size;
        private TextView pro_size;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardview_pro_size = itemView.findViewById(R.id.cardview_pro_size);
            pro_size = itemView.findViewById(R.id.pro_size);

            cardview_pro_size.setOnClickListener(v -> {

                lastSelectedPosition = getAdapterPosition();

                Bundle bundle = new Bundle();
                bundle.putInt("pro_size_position", lastSelectedPosition);
                bundle.putString("title", title);
                bundle.putString("product_id", product_id);
                bundle.putString("slug", slug);
                Fragment fragment = new ProductDetailFragment();
                fragment.setArguments(bundle);
                AppCompatActivity activity1 = (AppCompatActivity) activity;

                if (lastSelectedPosition != 0){
                    activity1.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.framelayout_for_pro_detail, fragment).commit();
                    notifyDataSetChanged();
                }
            });

        }
    }
}
