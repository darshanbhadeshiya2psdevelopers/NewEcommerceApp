package com.db.newecom.adapters;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.db.newecom.Model.MyOrders;
import com.db.newecom.R;
import com.db.newecom.Utills.Method;
import com.db.newecom.ui.activity.MyOrderDetailsActivity;
import com.db.newecom.ui.activity.ProductDetailsActivity;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class HomeMyOrdersAdapter extends RecyclerView.Adapter<HomeMyOrdersAdapter.ViewHolder> {

    private Method method;
    private Activity activity;
    private List<MyOrders> myOrderLists;
    private boolean isFromOD;

    public HomeMyOrdersAdapter(Activity activity, List<MyOrders> myOrderLists, boolean isFromOD) {
        this.activity = activity;
        this.myOrderLists = myOrderLists;
        this.isFromOD = isFromOD;
        method = new Method(activity);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.item_home_myorder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (!isFromOD) {
            holder.cardview_my_order.setOnClickListener(view ->
                    activity.startActivity(new Intent(activity, MyOrderDetailsActivity.class)
                            .putExtra("order_unique_id", myOrderLists.get(position).getOrder_unique_id())
                            .putExtra("product_id", myOrderLists.get(position).getProduct_id())));
        }
        else {
            holder.cardview_my_order.setOnClickListener(view ->
                    activity.startActivity(new Intent(activity, ProductDetailsActivity.class)
                            .putExtra("title", myOrderLists.get(position).getProduct_title())
                            .putExtra("product_id", myOrderLists.get(position).getProduct_id())));
        }

        Glide.with(activity).load(myOrderLists.get(position).getProduct_image())
                .placeholder(R.drawable.placeholder_img)
                .into(holder.home_order_pro_img);

        if (myOrderLists.get(position).getCurrent_order_status().equals("true")) {
            holder.home_order_status_indicator.setBackgroundTintList(ColorStateList.valueOf
                    (activity.getResources().getColor(R.color.green)));
        } else {
            holder.home_order_status_indicator.setBackgroundTintList(ColorStateList.valueOf
                    (activity.getResources().getColor(R.color.red)));
            holder.home_order_status.setTextColor(activity.getResources().getColor(R.color.red));
        }

        holder.home_order_status.setText(myOrderLists.get(position).getOrder_status());
        holder.home_order_pro_name.setText(myOrderLists.get(position).getProduct_title());
    }

    @Override
    public int getItemCount() {
        return myOrderLists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private MaterialCardView cardview_my_order;
        private ImageView home_order_pro_img;
        private View home_order_status_indicator;
        private TextView home_order_pro_name, home_order_status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardview_my_order = itemView.findViewById(R.id.cardview_my_order);
            home_order_pro_img = itemView.findViewById(R.id.home_order_pro_img);
            home_order_pro_name = itemView.findViewById(R.id.home_order_pro_name);
            home_order_status_indicator = itemView.findViewById(R.id.home_order_status_indicator);
            home_order_status = itemView.findViewById(R.id.home_order_status);
        }
    }
}
