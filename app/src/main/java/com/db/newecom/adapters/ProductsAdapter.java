package com.db.newecom.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.db.newecom.Api.ApiClient;
import com.db.newecom.Api.ApiInterface;
import com.db.newecom.Model.ProductList;
import com.db.newecom.R;
import com.db.newecom.Response.FavouriteRP;
import com.db.newecom.Utills.API;
import com.db.newecom.Utills.ConstantApi;
import com.db.newecom.Utills.Method;
import com.db.newecom.ui.activity.LoginActivity;
import com.db.newecom.ui.activity.ProductDetailsActivity;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsAdapter extends RecyclerView.Adapter {

    private Method method;
    private Activity activity;
    private boolean isGird;
    private List<ProductList> productLists;
    private String sign;
    private ProgressDialog progressDialog;

    private final int VIEW_TYPE_LOADING = 0;
    private final int VIEW_TYPE_ITEM = 1;

    public ProductsAdapter(Activity activity, List<ProductList> productLists, boolean isGird) {
        this.activity = activity;
        this.productLists = productLists;
        this.isGird = isGird;
        method = new Method(activity);
        sign = ConstantApi.currency + " ";
        progressDialog = new ProgressDialog(activity, R.style.ProgressDialogStyle);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_ITEM) {
            if (isGird) {
                View view = LayoutInflater.from(activity).inflate(R.layout.item_pro_grid, parent, false);
                return new ViewHolder(view);
            } else {
                View view = LayoutInflater.from(activity).inflate(R.layout.item_pro_list, parent, false);
                return new ViewHolder(view);
            }
        } else if (viewType == VIEW_TYPE_LOADING) {
            View v = LayoutInflater.from(activity).inflate(R.layout.item_progress, parent, false);
            return new ProgressViewHolder(v);
        }
        return null;

//        if (viewType == 0){
//            View view = LayoutInflater.from(activity).inflate(R.layout.item_pro_grid, parent, false);
//            return new ViewHolder(view);
//        }
//        else if (viewType == 1){
//            View view = LayoutInflater.from(activity).inflate(R.layout.item_pro_list, parent, false);
//            return new ViewHolder(view);
//        }
//        else if (viewType == 2){
//            View v = LayoutInflater.from(activity).inflate(R.layout.item_progress, parent, false);
//            return new ProgressViewHolder(v);
//        }
//        return null;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder.getItemViewType() == VIEW_TYPE_ITEM) {

            final ViewHolder viewHolder = (ViewHolder) holder;

            viewHolder.pro_cardview.setOnClickListener(view ->
                    activity.startActivity(new Intent(activity, ProductDetailsActivity.class)
                            .putExtra("title",productLists.get(position).getProduct_title())
                            .putExtra("product_id", productLists.get(position).getId())));

            Glide.with(activity).load(productLists.get(position).getProduct_image())
                    .placeholder(R.drawable.placeholder_img)
                    .into(viewHolder.pro_img);

            viewHolder.pro_name.setText(productLists.get(position).getProduct_title());
            viewHolder.pro_short_desc.setText(productLists.get(position).getProduct_desc());

            if (productLists.get(position).getYou_save().equals("0.00")) {
                viewHolder.pro_selling_price.setText(sign + productLists.get(position).getProduct_mrp());
                viewHolder.pro_original_price.setVisibility(View.GONE);
                viewHolder.pro_offer_applied.setVisibility(View.GONE);
            }
            else {
                viewHolder.pro_original_price.setPaintFlags(
                        viewHolder.pro_original_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG
                );
                viewHolder.pro_selling_price.setText(sign + productLists.get(position).getProduct_sell_price());
                viewHolder.pro_original_price.setText(sign + productLists.get(position).getProduct_mrp());
                viewHolder.pro_offer_applied.setText(productLists.get(position).getYou_save_per());
            }

            if (productLists.get(position).getProduct_qty().equals("0")) {
                viewHolder.out_of_stock_txt.setVisibility(View.VISIBLE);
                viewHolder.out_of_stock_txt.bringToFront();
                viewHolder.out_of_stock_txt.setText(activity.getResources().getString(R.string.out_of_stock));
            }

            if (productLists.get(position).getProduct_status().equals("0")){
                viewHolder.unavailable_txt.setVisibility(View.VISIBLE);
                viewHolder.unavailable_view.setVisibility(View.VISIBLE);
                viewHolder.out_of_stock_txt.setVisibility(View.GONE);
                viewHolder.unavailable_view.bringToFront();
                viewHolder.unavailable_txt.bringToFront();
                viewHolder.product_save_btn.bringToFront();
                viewHolder.unavailable_txt.setText(activity.getResources().getString(R.string.currently_unavailable));

            }

            if (!isGird) {
                viewHolder.ratingBar.setRating(Float.parseFloat(productLists.get(position).getRate_avg()));
                viewHolder.pro_rating_count.setText(productLists.get(position).getTotal_rate());
            }

            if (productLists.get(position).getIs_favorite().equals("1"))
                viewHolder.product_save_btn.setImageTintList(ColorStateList.valueOf(activity.getResources().getColor(R.color.red)));
            else
                viewHolder.product_save_btn.setImageTintList(ColorStateList.valueOf(activity.getResources().getColor(R.color.grey)));

            viewHolder.product_save_btn.setOnClickListener(v -> {

                if (method.isNetworkAvailable(activity)){
                    if (method.isLogin())
                        favourite(method.userId(), productLists.get(position).getId(), viewHolder);
                    else {
                        Method.login(true);
                        activity.startActivity(new Intent(activity, LoginActivity.class));
                    }
                }else
                    Toast.makeText(activity,
                            activity.getResources().getString(R.string.no_internet_connection),
                            Toast.LENGTH_SHORT).show();

            });

        }
    }

    private void favourite(String userId, String id, @NonNull ViewHolder holder) {

        progressDialog.show();
        progressDialog.setMessage(activity.getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(activity));
        jsObj.addProperty("product_id", id);
        jsObj.addProperty("user_id", userId);
        ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<FavouriteRP> call = apiService.addToFavourite(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<FavouriteRP>() {
            @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
            @Override
            public void onResponse(@NotNull Call<FavouriteRP> call, @NotNull Response<FavouriteRP> response) {

                try {

                    FavouriteRP favouriteRP = response.body();

                    assert favouriteRP != null;
                    if (favouriteRP.getStatus().equals("1")) {

                        if (favouriteRP.getSuccess().equals("1")) {

                            if (favouriteRP.getIs_favorite().equals("1")) {
                                holder.product_save_btn.setImageTintList(ColorStateList.valueOf(activity.getResources().getColor(R.color.red)));
                            } else {
                                holder.product_save_btn.setImageTintList(ColorStateList.valueOf(activity.getResources().getColor(R.color.grey)));
                            }
                        }

                        Toast.makeText(activity, favouriteRP.getMsg(), Toast.LENGTH_SHORT).show();

                    } else {
                        method.alertBox(favouriteRP.getMessage());
                    }

                } catch (Exception e) {
                    Log.d(ConstantApi.exceptionError, e.toString());
                    method.alertBox(activity.getString(R.string.failed_try_again));
                }

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(@NotNull Call<FavouriteRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e(ConstantApi.failApi, t.toString());
                progressDialog.dismiss();
                method.alertBox(activity.getString(R.string.failed_try_again));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (productLists.size() != 0) {
            return productLists.size() + 1;
        } else {
            return productLists.size();
        }
    }

    public void hideHeader() {
        ProgressViewHolder.progress_pro_item.setVisibility(View.GONE);
    }

    private boolean isHeader(int position) {
        return position == productLists.size();
    }

    @Override
    public int getItemViewType(int position) {
        return isHeader(position) ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private MaterialCardView pro_cardview;
        private View unavailable_view;
        private ImageView pro_img;
        private TextView pro_name, pro_short_desc, pro_rating_count, pro_original_price, pro_selling_price,
                pro_offer_applied, out_of_stock_txt, unavailable_txt;
        private RatingBar ratingBar;
        private ImageButton product_save_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            unavailable_view = itemView.findViewById(R.id.unavailable_view);
            pro_img = itemView.findViewById(R.id.product_img);
            pro_name = itemView.findViewById(R.id.product_name);
            pro_short_desc = itemView.findViewById(R.id.product_desc);
            pro_cardview = itemView.findViewById(R.id.pro_cardview);
            pro_original_price = itemView.findViewById(R.id.pro_original_price);
            pro_selling_price = itemView.findViewById(R.id.product_sell_price);
            pro_offer_applied = itemView.findViewById(R.id.product_discount_txt);
            out_of_stock_txt = itemView.findViewById(R.id.out_of_stock_txt);
            unavailable_txt = itemView.findViewById(R.id.unavailable_txt);
            pro_rating_count = itemView.findViewById(R.id.product_rating_count);
            ratingBar = itemView.findViewById(R.id.product_rating_bar);
            product_save_btn = itemView.findViewById(R.id.product_save_btn);
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public static LottieAnimationView progress_pro_item;

        public ProgressViewHolder(View v) {
            super(v);
            progress_pro_item = v.findViewById(R.id.progress_pro_item);
        }
    }
}
