package com.db.newecom.adapters;

import android.app.Activity;
import android.content.Intent;
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
import com.db.newecom.Model.HomeCatSubProList;
import com.db.newecom.Model.ProductList;
import com.db.newecom.R;
import com.db.newecom.Utills.ConstantApi;
import com.db.newecom.ui.activity.ProductDetailsActivity;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class HomeProOfCatAdapter extends RecyclerView.Adapter<HomeProOfCatAdapter.ViewHolder> {

    private Activity activity;
    private String sign;
    private List<HomeCatSubProList> homeCatSubProLists;
    private List<ProductList> productLists1;
    private List<ProductList> productLists2;
    private List<ProductList> productLists3;
    private List<ProductList> productLists4;
    private List<ProductList> productLists5;

    public HomeProOfCatAdapter(Activity activity, List<HomeCatSubProList> homeCatSubProLists) {
        this.activity = activity;
        this.homeCatSubProLists = homeCatSubProLists;
        productLists1 = new ArrayList<>();
        productLists2 = new ArrayList<>();
        productLists3 = new ArrayList<>();
        productLists4 = new ArrayList<>();
        productLists5 = new ArrayList<>();
        sign = ConstantApi.currency + " ";
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.item_home_proofcat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.home_catpro_name.setText(homeCatSubProLists.get(position).getTitle());
        holder.home_catpro_view_all.setText("View all Products");
        holder.home_catpro_view_all.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("title", homeCatSubProLists.get(position).getTitle());
            bundle.putString("cat_id", homeCatSubProLists.get(position).getId());
            bundle.putString("type", "home_cat");
            Navigation.findNavController(view).navigate(R.id.navigate_to_products_fragment, bundle);
        });

        if (position == 0 && getItemCount() > 0){

            productLists1.clear();

            if (homeCatSubProLists.get(position).getProductLists().size() > 2){

                productLists1.addAll(homeCatSubProLists.get(position).getProductLists());

                Glide.with(activity).load(productLists1.get(0).getProduct_image())
                        .placeholder(R.drawable.placeholder_img)
                        .into(holder.pro1_img);

                Glide.with(activity).load(productLists1.get(1).getProduct_image())
                        .placeholder(R.drawable.placeholder_img)
                        .into(holder.pro2_img);

                Glide.with(activity).load(productLists1.get(2).getProduct_image())
                        .placeholder(R.drawable.placeholder_img)
                        .into(holder.pro3_img);

                holder.pro1_name.setText(productLists1.get(0).getProduct_title());
                holder.pro2_name.setText(productLists1.get(1).getProduct_title());
                holder.pro3_name.setText(productLists1.get(2).getProduct_title());

                holder.pro1_price.setText(sign + productLists1.get(0).getProduct_sell_price());
                holder.pro2_price.setText(sign + productLists1.get(1).getProduct_sell_price());
                holder.pro3_price.setText(sign + productLists1.get(2).getProduct_sell_price());

                holder.product1_cardview.setOnClickListener(view ->
                        activity.startActivity(new Intent(activity, ProductDetailsActivity.class)
                                .putExtra("title",productLists1.get(0).getProduct_title())
                                .putExtra("product_id", productLists1.get(0).getId())));
                holder.product2_cardview.setOnClickListener(view ->
                        activity.startActivity(new Intent(activity, ProductDetailsActivity.class)
                                .putExtra("title",productLists1.get(1).getProduct_title())
                                .putExtra("product_id", productLists1.get(1).getId())));
                holder.product3_cardview.setOnClickListener(view ->
                        activity.startActivity(new Intent(activity, ProductDetailsActivity.class)
                                .putExtra("title",productLists1.get(2).getProduct_title())
                                .putExtra("product_id", productLists1.get(2).getId())));

            }
            else {
                holder.product1_cardview.setVisibility(View.GONE);
                holder.product2_cardview.setVisibility(View.GONE);
                holder.product3_cardview.setVisibility(View.GONE);
            }

        } else if (position == 1 && getItemCount() > 1){

            productLists2.clear();

            if (homeCatSubProLists.get(position).getProductLists().size() > 2){

                productLists2.addAll(homeCatSubProLists.get(position).getProductLists());

                Glide.with(activity).load(productLists2.get(0).getProduct_image())
                        .placeholder(R.drawable.placeholder_img)
                        .into(holder.pro1_img);

                Glide.with(activity).load(productLists2.get(1).getProduct_image())
                        .placeholder(R.drawable.placeholder_img)
                        .into(holder.pro2_img);

                Glide.with(activity).load(productLists2.get(2).getProduct_image())
                        .placeholder(R.drawable.placeholder_img)
                        .into(holder.pro3_img);

                holder.pro1_name.setText(productLists2.get(0).getProduct_title());
                holder.pro2_name.setText(productLists2.get(1).getProduct_title());
                holder.pro3_name.setText(productLists2.get(2).getProduct_title());

                holder.pro1_price.setText(sign + productLists2.get(0).getProduct_sell_price());
                holder.pro2_price.setText(sign + productLists2.get(1).getProduct_sell_price());
                holder.pro3_price.setText(sign + productLists2.get(2).getProduct_sell_price());

                holder.product1_cardview.setOnClickListener(view ->
                        activity.startActivity(new Intent(activity, ProductDetailsActivity.class)
                                .putExtra("title",productLists2.get(0).getProduct_title())
                                .putExtra("product_id", productLists2.get(0).getId())));
                holder.product2_cardview.setOnClickListener(view ->
                        activity.startActivity(new Intent(activity, ProductDetailsActivity.class)
                                .putExtra("title",productLists2.get(1).getProduct_title())
                                .putExtra("product_id", productLists2.get(1).getId())));
                holder.product3_cardview.setOnClickListener(view ->
                        activity.startActivity(new Intent(activity, ProductDetailsActivity.class)
                                .putExtra("title",productLists2.get(2).getProduct_title())
                                .putExtra("product_id", productLists2.get(2).getId())));

            }
            else {
                holder.product1_cardview.setVisibility(View.GONE);
                holder.product2_cardview.setVisibility(View.GONE);
                holder.product3_cardview.setVisibility(View.GONE);
            }

        } else if (position == 2 && getItemCount() > 2){

            productLists3.clear();

            if (homeCatSubProLists.get(position).getProductLists().size() > 2){

                productLists3.addAll(homeCatSubProLists.get(position).getProductLists());

                Glide.with(activity).load(productLists3.get(0).getProduct_image())
                        .placeholder(R.drawable.placeholder_img)
                        .into(holder.pro1_img);

                Glide.with(activity).load(productLists3.get(1).getProduct_image())
                        .placeholder(R.drawable.placeholder_img)
                        .into(holder.pro2_img);

                Glide.with(activity).load(productLists3.get(2).getProduct_image())
                        .placeholder(R.drawable.placeholder_img)
                        .into(holder.pro3_img);

                holder.pro1_name.setText(productLists3.get(0).getProduct_title());
                holder.pro2_name.setText(productLists3.get(1).getProduct_title());
                holder.pro3_name.setText(productLists3.get(2).getProduct_title());

                holder.pro1_price.setText(sign + productLists3.get(0).getProduct_sell_price());
                holder.pro2_price.setText(sign + productLists3.get(1).getProduct_sell_price());
                holder.pro3_price.setText(sign + productLists3.get(2).getProduct_sell_price());

                holder.product1_cardview.setOnClickListener(view ->
                        activity.startActivity(new Intent(activity, ProductDetailsActivity.class)
                                .putExtra("title",productLists3.get(0).getProduct_title())
                                .putExtra("product_id", productLists3.get(0).getId())));
                holder.product2_cardview.setOnClickListener(view ->
                        activity.startActivity(new Intent(activity, ProductDetailsActivity.class)
                                .putExtra("title",productLists3.get(1).getProduct_title())
                                .putExtra("product_id", productLists3.get(1).getId())));
                holder.product3_cardview.setOnClickListener(view ->
                        activity.startActivity(new Intent(activity, ProductDetailsActivity.class)
                                .putExtra("title",productLists3.get(2).getProduct_title())
                                .putExtra("product_id", productLists3.get(2).getId())));
            }
            else {
                holder.product1_cardview.setVisibility(View.GONE);
                holder.product2_cardview.setVisibility(View.GONE);
                holder.product3_cardview.setVisibility(View.GONE);
            }

        } else if (position == 3 && getItemCount() > 3){

            productLists4.clear();

            if (homeCatSubProLists.get(position).getProductLists().size() > 2) {

                productLists4.addAll(homeCatSubProLists.get(position).getProductLists());

                Glide.with(activity).load(productLists4.get(0).getProduct_image())
                        .placeholder(R.drawable.placeholder_img)
                        .into(holder.pro1_img);

                Glide.with(activity).load(productLists4.get(1).getProduct_image())
                        .placeholder(R.drawable.placeholder_img)
                        .into(holder.pro2_img);

                Glide.with(activity).load(productLists4.get(2).getProduct_image())
                        .placeholder(R.drawable.placeholder_img)
                        .into(holder.pro3_img);

                holder.pro1_name.setText(productLists4.get(0).getProduct_title());
                holder.pro2_name.setText(productLists4.get(1).getProduct_title());
                holder.pro3_name.setText(productLists4.get(2).getProduct_title());

                holder.pro1_price.setText(sign + productLists4.get(0).getProduct_sell_price());
                holder.pro2_price.setText(sign + productLists4.get(1).getProduct_sell_price());
                holder.pro3_price.setText(sign + productLists4.get(2).getProduct_sell_price());

                holder.product1_cardview.setOnClickListener(view ->
                        activity.startActivity(new Intent(activity, ProductDetailsActivity.class)
                                .putExtra("title",productLists4.get(0).getProduct_title())
                                .putExtra("product_id", productLists4.get(0).getId())));
                holder.product2_cardview.setOnClickListener(view ->
                        activity.startActivity(new Intent(activity, ProductDetailsActivity.class)
                                .putExtra("title",productLists4.get(1).getProduct_title())
                                .putExtra("product_id", productLists4.get(1).getId())));
                holder.product3_cardview.setOnClickListener(view ->
                        activity.startActivity(new Intent(activity, ProductDetailsActivity.class)
                                .putExtra("title",productLists4.get(2).getProduct_title())
                                .putExtra("product_id", productLists4.get(2).getId())));
            }
            else {
                holder.product1_cardview.setVisibility(View.GONE);
                holder.product2_cardview.setVisibility(View.GONE);
                holder.product3_cardview.setVisibility(View.GONE);
            }


        } else if (position == 4 && getItemCount() > 4){

            productLists5.clear();

            if (homeCatSubProLists.get(position).getProductLists().size() > 2) {

                productLists5.addAll(homeCatSubProLists.get(position).getProductLists());

                Glide.with(activity).load(productLists5.get(0).getProduct_image())
                        .placeholder(R.drawable.placeholder_img)
                        .into(holder.pro1_img);

                Glide.with(activity).load(productLists5.get(1).getProduct_image())
                        .placeholder(R.drawable.placeholder_img)
                        .into(holder.pro2_img);

                Glide.with(activity).load(productLists5.get(2).getProduct_image())
                        .placeholder(R.drawable.placeholder_img)
                        .into(holder.pro3_img);

                holder.pro1_name.setText(productLists5.get(0).getProduct_title());
                holder.pro2_name.setText(productLists5.get(1).getProduct_title());
                holder.pro3_name.setText(productLists5.get(2).getProduct_title());

                holder.pro1_price.setText(sign + productLists5.get(0).getProduct_sell_price());
                holder.pro2_price.setText(sign + productLists5.get(1).getProduct_sell_price());
                holder.pro3_price.setText(sign + productLists5.get(2).getProduct_sell_price());

                holder.product1_cardview.setOnClickListener(view ->
                        activity.startActivity(new Intent(activity, ProductDetailsActivity.class)
                                .putExtra("title",productLists5.get(0).getProduct_title())
                                .putExtra("product_id", productLists5.get(0).getId())));
                holder.product2_cardview.setOnClickListener(view ->
                        activity.startActivity(new Intent(activity, ProductDetailsActivity.class)
                                .putExtra("title",productLists5.get(1).getProduct_title())
                                .putExtra("product_id", productLists5.get(1).getId())));
                holder.product3_cardview.setOnClickListener(view ->
                        activity.startActivity(new Intent(activity, ProductDetailsActivity.class)
                                .putExtra("title",productLists5.get(2).getProduct_title())
                                .putExtra("product_id", productLists5.get(2).getId())));

            }
            else {
                holder.product1_cardview.setVisibility(View.GONE);
                holder.product2_cardview.setVisibility(View.GONE);
                holder.product3_cardview.setVisibility(View.GONE);
            }

        }

    }

    @Override
    public int getItemCount() {
        return homeCatSubProLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private MaterialCardView product1_cardview, product2_cardview, product3_cardview;
        private TextView home_catpro_name, home_catpro_view_all, pro1_name, pro1_price, pro2_name, pro2_price,
                pro3_name, pro3_price;
        private ImageView pro1_img, pro2_img, pro3_img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            product1_cardview = itemView.findViewById(R.id.product1_cardview);
            product2_cardview = itemView.findViewById(R.id.product2_cardview);
            product3_cardview = itemView.findViewById(R.id.product3_cardview);
            home_catpro_name = itemView.findViewById(R.id.home_catpro_name);
            home_catpro_view_all = itemView.findViewById(R.id.home_catpro_view_all);
            pro1_name = itemView.findViewById(R.id.pro1_name);
            pro2_name = itemView.findViewById(R.id.pro2_name);
            pro3_name = itemView.findViewById(R.id.pro3_name);
            pro1_price = itemView.findViewById(R.id.pro1_price);
            pro2_price = itemView.findViewById(R.id.pro2_price);
            pro3_price = itemView.findViewById(R.id.pro3_price);
            pro1_img = itemView.findViewById(R.id.pro1_img);
            pro2_img = itemView.findViewById(R.id.pro2_img);
            pro3_img = itemView.findViewById(R.id.pro3_img);
        }
    }
}
