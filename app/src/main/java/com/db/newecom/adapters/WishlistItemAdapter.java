package com.db.newecom.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.db.newecom.Api.ApiClient;
import com.db.newecom.Api.ApiInterface;
import com.db.newecom.Model.ProductList;
import com.db.newecom.R;
import com.db.newecom.Response.AddToCartRP;
import com.db.newecom.Response.DataRP;
import com.db.newecom.Utills.API;
import com.db.newecom.Utills.ConstantApi;
import com.db.newecom.Utills.Events;
import com.db.newecom.Utills.GlobalBus;
import com.db.newecom.Utills.Method;
import com.db.newecom.ui.activity.CartActivity;
import com.db.newecom.ui.activity.ProductDetailsActivity;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WishlistItemAdapter extends RecyclerView.Adapter<WishlistItemAdapter.ViewHolder> {

    private View view;
    private Method method;
    private Activity activity;
    private boolean itemforProfile;
    private List<ProductList> productLists;
    private String sign;

    public WishlistItemAdapter(Activity activity, boolean itemforProfile, List<ProductList> productLists) {
        this.activity = activity;
        this.itemforProfile = itemforProfile;
        this.productLists = productLists;
        sign = ConstantApi.currency + " ";
        method = new Method(activity);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = LayoutInflater.from(activity).inflate(R.layout.item_favourite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (itemforProfile)
            holder.ll_btns.setVisibility(View.GONE);

        holder.fav_pro_cardview.setOnClickListener(view ->
                activity.startActivity(new Intent(activity, ProductDetailsActivity.class)
                        .putExtra("title",productLists.get(position).getProduct_title())
                        .putExtra("product_id", productLists.get(position).getId())));

        holder.pro_original_price.setPaintFlags(
                holder.pro_original_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        holder.fav_add_to_cart_btn.setOnClickListener(view ->
                addToCart(productLists.get(position).getId(), method.userId(), productLists.get(position).getProduct_default_size()));

        Glide.with(activity).load(productLists.get(position).getProduct_image_square())
                .placeholder(R.drawable.placeholder_img)
                .into(holder.pro_img);
        holder.pro_name.setText(productLists.get(position).getProduct_title());

        holder.pro_short_desc.setText(productLists.get(position).getProduct_desc());

        holder.ratingBar.setRating(Float.parseFloat(productLists.get(position).getRate_avg()));

        holder.pro_rating_count.setText(productLists.get(position).getTotal_rate());

        if (productLists.get(position).getYou_save().equals("0.00")) {
            holder.pro_selling_price.setText(sign + productLists.get(position).getProduct_mrp());
            holder.pro_original_price.setVisibility(View.GONE);
            holder.pro_offer_applied.setVisibility(View.GONE);
        }
        else {
            holder.pro_selling_price.setText(sign + productLists.get(position).getProduct_sell_price());
            holder.pro_original_price.setText(sign + productLists.get(position).getProduct_mrp());
            holder.pro_offer_applied.setText(productLists.get(position).getYou_save_per());
        }

        if (productLists.get(position).getProduct_qty().equals("0")) {
            holder.pro_status.setVisibility(View.VISIBLE);
            holder.pro_status.setText(activity.getResources().getString(R.string.out_of_stock));
            holder.fav_add_to_cart_btn.setEnabled(false);
        }

        if (productLists.get(position).getProduct_status().equals("0")){
            holder.pro_status.setVisibility(View.VISIBLE);
            holder.view.setVisibility(View.VISIBLE);
            holder.view.bringToFront();
            holder.pro_status.setText(activity.getResources().getString(R.string.currently_unavailable));
            holder.fav_add_to_cart_btn.setEnabled(false);
        }

        holder.fav_remove_btn.setOnClickListener(v -> {
            if (method.isNetworkAvailable(activity)) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity, R.style.DialogTitleTextStyle);
                builder.setMessage(activity.getResources().getString(R.string.delete_wishlist_msg));
                builder.setCancelable(false);
                builder.setPositiveButton(activity.getResources().getString(R.string.yes),
                        (arg0, arg1) -> removeProduct(productLists.get(position).getId(), method.userId(), position));
                builder.setNegativeButton(activity.getResources().getString(R.string.no),
                        (dialog, which) -> dialog.dismiss());

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
            else {
                Toast.makeText(activity, activity.getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void addToCart(String product_id, String user_id, String product_size){

        ProgressDialog progressDialog = new ProgressDialog(activity, R.style.ProgressDialogStyle);
        progressDialog.show();
        progressDialog.setMessage(activity.getResources().getString(R.string.processing));
        progressDialog.setCancelable(false);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(activity));
        jsObj.addProperty("product_id", product_id);
        jsObj.addProperty("user_id", user_id);
        jsObj.addProperty("product_qty", "1");
        jsObj.addProperty("buy_now", "false");
        jsObj.addProperty("product_size", product_size);
        ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<AddToCartRP> call = apiService.addToCart(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<AddToCartRP>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NotNull Call<AddToCartRP> call, @NotNull Response<AddToCartRP> response) {


                try {

                    AddToCartRP addToCartRP = response.body();

                    assert addToCartRP != null;
                    if (addToCartRP.getStatus().equals("1")) {

                        if (addToCartRP.getSuccess().equals("1")) {

                            Snackbar.make(view, addToCartRP.getMsg(), Snackbar.LENGTH_LONG)
                                    .setBackgroundTint(activity.getResources().getColor(R.color.snackBar_color))
                                    .setTextColor(activity.getResources().getColor(R.color.white))
                                    .setActionTextColor(activity.getResources().getColor(R.color.white))
                                    .setAction("View Cart", v1 -> {
                                        activity.startActivity(new Intent(activity, CartActivity.class));
                                    }).show();

                            Events.CartItem cartItem = new Events.CartItem(addToCartRP.getTotal_item());
                            GlobalBus.getBus().post(cartItem);
                        }
                        else {
                            Toast.makeText(activity, addToCartRP.getMsg(), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        method.alertBox(addToCartRP.getMessage());
                    }

                } catch (Exception e) {
                    Log.d(ConstantApi.exceptionError, e.toString());
                    method.alertBox(activity.getResources().getString(R.string.failed_try_again));
                }

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(@NotNull Call<AddToCartRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e(ConstantApi.failApi, t.toString());
                progressDialog.dismiss();
                method.alertBox(activity.getResources().getString(R.string.failed_try_again));
            }
        });

    }

    private void removeProduct(String id, String userId, int position) {

        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.show();
        progressDialog.setMessage(activity.getResources().getString(R.string.processing));
        progressDialog.setCancelable(false);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(activity));
        jsObj.addProperty("product_id", id);
        jsObj.addProperty("user_id", userId);
        ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<DataRP> call = apiService.removeWishlistPro(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<DataRP>() {
            @Override
            public void onResponse(Call<DataRP> call, Response<DataRP> response) {

                try {
                    DataRP dataRP = response.body();
                    assert dataRP != null;

                    if (dataRP.getStatus().equals("1")){
                        if (dataRP.getSuccess().equals("1")) {
                            Toast.makeText(activity, dataRP.getMsg(), Toast.LENGTH_SHORT).show();
                            productLists.remove(position);
                            notifyDataSetChanged();
                        } else {
                            method.alertBox(dataRP.getMsg());
                        }
                    } else {
                        method.alertBox(dataRP.getMessage());
                    }
                }
                catch (Exception e){
                    Log.d(ConstantApi.exceptionError, e.toString());
                    method.alertBox(activity.getResources().getString(R.string.failed_try_again));
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<DataRP> call, Throwable t) {

                Log.e(ConstantApi.failApi, t.toString());
                progressDialog.dismiss();
                method.alertBox(activity.getResources().getString(R.string.failed_try_again));
            }
        });

    }

    @Override
    public int getItemCount() {

        if (itemforProfile)
            if (productLists.size() == 1)
                return 1;
            else
                return 2;
        return productLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View view;
        private MaterialCardView fav_pro_cardview;
        private ImageView pro_img;
        private TextView pro_name, pro_short_desc, pro_rating_count, pro_original_price, pro_selling_price,
                pro_offer_applied, pro_status;
        private RatingBar ratingBar;
        private LinearLayout ll_btns;
        private Button fav_remove_btn, fav_add_to_cart_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            view = itemView.findViewById(R.id.unavailable_view);
            pro_status = itemView.findViewById(R.id.pro_status);
            fav_pro_cardview = itemView.findViewById(R.id.fav_pro_cardview);
            pro_img = itemView.findViewById(R.id.fav_product_img);
            pro_name = itemView.findViewById(R.id.fav_product_name);
            pro_short_desc = itemView.findViewById(R.id.fav_product_desc);
            pro_rating_count = itemView.findViewById(R.id.fav_product_rating_count);
            pro_original_price = itemView.findViewById(R.id.fav_pro_original_price);
            pro_selling_price = itemView.findViewById(R.id.fav_product_sell_price);
            pro_offer_applied = itemView.findViewById(R.id.fav_product_discount_txt);
            ratingBar = itemView.findViewById(R.id.fav_product_rating_bar);
            ll_btns = itemView.findViewById(R.id.ll_btns);
            fav_remove_btn = itemView.findViewById(R.id.fav_remove_btn);
            fav_add_to_cart_btn = itemView.findViewById(R.id.fav_add_to_cart_btn);
        }
    }
}
