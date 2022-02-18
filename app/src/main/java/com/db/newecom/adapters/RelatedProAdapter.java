package com.db.newecom.adapters;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.db.newecom.Model.ProductList;
import com.db.newecom.R;
import com.db.newecom.Utills.ConstantApi;
import com.db.newecom.ui.fragment.ProductDetailFragment;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class RelatedProAdapter extends RecyclerView.Adapter<RelatedProAdapter.ViewHolder> {

    private Activity activity;
    private List<ProductList> productLists;
    private String sign;

    public RelatedProAdapter(Activity activity, List<ProductList> productLists) {
        this.activity = activity;
        this.productLists = productLists;
        sign = ConstantApi.currency + " ";
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.item_home_latestpro, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(activity).load(productLists.get(position).getProduct_image())
                .placeholder(R.drawable.placeholder_img)
                .into(holder.related_pro_img);

        holder.related_pro_name.setText(productLists.get(position).getProduct_title());
        holder.related_pro_price.setText(sign + productLists.get(position).getProduct_sell_price());


        holder.cardview_related_pro.setOnClickListener(view -> {

            Bundle args = new Bundle();
            args.putString("title", productLists.get(position).getProduct_title());
            args.putString("product_id", productLists.get(position).getId());
            Fragment fragment = new ProductDetailFragment();
            fragment.setArguments(args);
            AppCompatActivity activity1 = (AppCompatActivity) activity;
            activity1.getSupportFragmentManager().beginTransaction()
                    .add(R.id.framelayout_for_pro_detail, fragment).addToBackStack(null).commit();

        });

    }

    @Override
    public int getItemCount() {
        return productLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private MaterialCardView cardview_related_pro;
        private ImageView related_pro_img;
        private TextView related_pro_name, related_pro_price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardview_related_pro = itemView.findViewById(R.id.cardview_latest_pro);
            related_pro_img = itemView.findViewById(R.id.home_latestpro_img);
            related_pro_name = itemView.findViewById(R.id.home_latestpro_name);
            related_pro_price = itemView.findViewById(R.id.home_latestpro_price);
        }
    }
}
