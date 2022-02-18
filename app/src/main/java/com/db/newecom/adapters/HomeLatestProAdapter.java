package com.db.newecom.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.db.newecom.Model.ProductList;
import com.db.newecom.R;
import com.db.newecom.Utills.ConstantApi;
import com.db.newecom.ui.activity.ProductDetailsActivity;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class HomeLatestProAdapter extends RecyclerView.Adapter<HomeLatestProAdapter.ViewHolder> {

    private Activity activity;
    private List<ProductList> latestLists;
    private String sign;

    public HomeLatestProAdapter(Activity activity, List<ProductList> latestLists) {
        this.activity = activity;
        this.latestLists = latestLists;
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

        Glide.with(activity).load(latestLists.get(position).getProduct_image())
                .placeholder(R.drawable.placeholder_img)
                .into(holder.home_latestpro_img);

        holder.home_latestpro_name.setText(latestLists.get(position).getProduct_title());
        holder.home_latestpro_price.setText(sign + latestLists.get(position).getProduct_sell_price());

        holder.cardview_latest_pro.setOnClickListener(view ->
                activity.startActivity(new Intent(activity, ProductDetailsActivity.class)
                        .putExtra("title",latestLists.get(position).getProduct_title())
                        .putExtra("product_id", latestLists.get(position).getId())));
    }

    @Override
    public int getItemCount() {
        return latestLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private MaterialCardView cardview_latest_pro;
        private ImageView home_latestpro_img;
        private TextView home_latestpro_name, home_latestpro_price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            home_latestpro_img = itemView.findViewById(R.id.home_latestpro_img);
            home_latestpro_name = itemView.findViewById(R.id.home_latestpro_name);
            home_latestpro_price = itemView.findViewById(R.id.home_latestpro_price);
            cardview_latest_pro = itemView.findViewById(R.id.cardview_latest_pro);
        }
    }
}
