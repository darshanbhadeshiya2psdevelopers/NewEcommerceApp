package com.db.newecom.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
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

public class HomeTodaysDealAdapter extends RecyclerView.Adapter<HomeTodaysDealAdapter.ViewHolder> {

    private Activity activity;
    private List<ProductList> productLists;
    private String sign;

    public HomeTodaysDealAdapter(Activity activity, List<ProductList> productLists) {
        this.activity = activity;
        this.productLists = productLists;
        sign = ConstantApi.currency + " ";
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.item_home_todaysdeal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(activity).load(productLists.get(position).getProduct_image())
                .placeholder(R.drawable.placeholder_img)
                .into(holder.home_deal_img);

        holder.home_deal_pro_name.setText(productLists.get(position).getProduct_title());

        if (productLists.get(position).getProduct_mrp().equals(productLists.get(position).getProduct_sell_price()))
            holder.home_deal_pro_mrp.setVisibility(View.GONE);
        else {
            holder.home_deal_pro_mrp.setPaintFlags(holder.home_deal_pro_mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.home_deal_pro_mrp.setText(sign + productLists.get(position).getProduct_mrp());
        }

        holder.home_deal_pro_price.setText(sign + productLists.get(position).getProduct_sell_price());

        holder.cardview_todays_deal.setOnClickListener(view ->
                activity.startActivity(new Intent(activity, ProductDetailsActivity.class)
                        .putExtra("title",productLists.get(position).getProduct_title())
                        .putExtra("product_id", productLists.get(position).getId())));
    }

    @Override
    public int getItemCount() {
        return productLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView home_deal_img;
        private TextView home_deal_pro_name, home_deal_pro_mrp, home_deal_pro_price;
        private MaterialCardView cardview_todays_deal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardview_todays_deal = itemView.findViewById(R.id.cardview_todays_deal);
            home_deal_img = itemView.findViewById(R.id.home_deal_img);
            home_deal_pro_name = itemView.findViewById(R.id.home_deal_pro_name);
            home_deal_pro_mrp = itemView.findViewById(R.id.home_deal_pro_mrp);
            home_deal_pro_price = itemView.findViewById(R.id.home_deal_pro_price);

        }
    }
}
