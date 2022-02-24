package com.db.newecom.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.db.newecom.Api.ApiClient;
import com.db.newecom.Api.ApiInterface;
import com.db.newecom.Model.CartList;
import com.db.newecom.R;
import com.db.newecom.Response.RemoveCartRP;
import com.db.newecom.Response.UpdateCartRP;
import com.db.newecom.Utills.API;
import com.db.newecom.Utills.ConstantApi;
import com.db.newecom.Utills.Events;
import com.db.newecom.Utills.GlobalBus;
import com.db.newecom.Utills.Method;
import com.db.newecom.ui.activity.ProductDetailsActivity;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Method method;
    private Activity activity;
    private boolean showBtns;
    private List<CartList> cartLists;
    private String sign;
    private ProgressDialog progressDialog;

    public CartAdapter(Activity activity, boolean showBtns, List<CartList> cartLists) {
        this.activity = activity;
        this.showBtns = showBtns;
        this.cartLists = cartLists;
        method = new Method(activity);
        progressDialog = new ProgressDialog(activity);
        sign = ConstantApi.currency + " ";
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.cardview_cart_item.setOnClickListener(view ->
                activity.startActivity(new Intent(activity, ProductDetailsActivity.class)
                        .putExtra("title",cartLists.get(position).getProduct_title())
                        .putExtra("product_id", cartLists.get(position).getProduct_id())));

        if (!showBtns){
            holder.pro_delete_btn.setVisibility(View.GONE);
            holder.qty_remove_btn.setVisibility(View.GONE);
            holder.qty_add_btn.setVisibility(View.GONE);
        }
        else {
            if (cartLists.get(position).getProduct_status().equals("0")) {
                holder.unavailable_view.setVisibility(View.VISIBLE);
                holder.unavailable_view.bringToFront();
                holder.pro_status.setVisibility(View.VISIBLE);
                holder.pro_status.bringToFront();
                holder.pro_status.setText(cartLists.get(position).getProduct_status_lbl());
                holder.pro_delete_btn.bringToFront();
                holder.pro_delete_btn.setImageTintList(ColorStateList.valueOf(activity.getResources().getColor(R.color.dark_grey)));
                holder.qty_add_btn.setEnabled(false);
                holder.qty_remove_btn.setEnabled(false);
            }
        }

        if (cartLists.get(position).getProduct_qty().equals("0")) {
            holder.unavailable_view.setVisibility(View.VISIBLE);
            holder.unavailable_view.bringToFront();
            holder.pro_status.setVisibility(View.VISIBLE);
            holder.pro_status.bringToFront();
            holder.pro_status.setText(activity.getResources().getString(R.string.out_of_stock));
            holder.pro_delete_btn.bringToFront();
            holder.pro_delete_btn.setImageTintList(ColorStateList.valueOf(activity.getResources().getColor(R.color.dark_grey)));
            holder.qty_add_btn.setEnabled(false);
            holder.qty_remove_btn.setEnabled(false);
        }

        holder.cart_pro_name.setText(cartLists.get(position).getProduct_title());

        Glide.with(activity).load(cartLists.get(position).getProduct_image())
                .placeholder(R.drawable.placeholder_img)
                .into(holder.cart_pro_img);

        if (!cartLists.get(position).getProduct_size().equals(""))
            holder.cart_pro_size.setText(cartLists.get(position).getProduct_size());
        else
            holder.ll_size.setVisibility(View.GONE);

        if (cartLists.get(position).getOriginal_sell_price().equals("0.00"))
            holder.cart_pro_price.setText(sign + cartLists.get(position).getOriginal_price());
        else
            holder.cart_pro_price.setText(sign + cartLists.get(position).getOriginal_sell_price());

        if (cartLists.get(position).getProduct_sell_price().equals("0.00"))
            holder.cart_pro_total.setText(sign + cartLists.get(position).getProduct_mrp());
        else
            holder.cart_pro_total.setText(sign + cartLists.get(position).getProduct_sell_price());

        holder.Qty = Integer.parseInt(cartLists.get(position).getProduct_cart_qty());

        holder.cart_pro_qty.setText(String.valueOf(holder.Qty));

        holder.qty_add_btn.setOnClickListener(v -> {
            if (holder.Qty < Integer.parseInt(cartLists.get(position).getMax_unit_buy())) {
                holder.Qty = holder.Qty + 1;
                holder.cart_pro_qty.setText(String.valueOf(holder.Qty));
                cartItemUpdate(holder.Qty, cartLists.get(position).getProduct_id(), method.userId(), String.valueOf(holder.Qty), cartLists.get(position).getProduct_size(), position);
            } else {
                String msg = activity.getResources().getString(R.string.max_value) + " " +
                        cartLists.get(position).getMax_unit_buy() + " " +
                        activity.getResources().getString(R.string.purchase_item);
                method.alertBox(msg);
            }
        });

        holder.qty_remove_btn.setOnClickListener(v -> {
            if (holder.Qty > 1) {
                holder.Qty = holder.Qty - 1;
                holder.cart_pro_qty.setText(String.valueOf(holder.Qty));
                cartItemUpdate(holder.Qty, cartLists.get(position).getProduct_id(), method.userId(), String.valueOf(holder.Qty), cartLists.get(position).getProduct_size(), position);
            } else {
                method.alertBox(activity.getResources().getString(R.string.minimum_order_value_one));
            }
        });

        holder.pro_delete_btn.setOnClickListener(v -> {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity, R.style.DialogTitleTextStyle);
            builder.setMessage(activity.getResources().getString(R.string.delete_cart_msg));
            builder.setCancelable(false);
            builder.setPositiveButton(activity.getResources().getString(R.string.yes),
                    (arg0, arg1) -> removeItem(cartLists.get(position).getId(), method.userId(), position));
            builder.setNegativeButton(activity.getResources().getString(R.string.no),
                    (dialog, which) -> dialog.dismiss());

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
    }

    private void removeItem(String cart_id, String userId, int position) {

        progressDialog.show();
        progressDialog.setMessage(activity.getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(activity));
        jsObj.addProperty("cart_id", cart_id);
        jsObj.addProperty("user_id", userId);
        ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<RemoveCartRP> call = apiService.removeCartItem(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<RemoveCartRP>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NotNull Call<RemoveCartRP> call, @NotNull Response<RemoveCartRP> response) {


                try {

                    RemoveCartRP removeCartRP = response.body();

                    assert removeCartRP != null;
                    if (removeCartRP.getStatus().equals("1")) {

                        if (removeCartRP.getSuccess().equals("1")) {

                            cartLists.remove(position);
                            notifyDataSetChanged();

                            Toast.makeText(activity, removeCartRP.getMsg(), Toast.LENGTH_SHORT).show();

                            Events.UpdateCart updateCart = new Events.UpdateCart(removeCartRP.getTotal_item(), removeCartRP.getPrice(), removeCartRP.getDelivery_charge(),
                                    removeCartRP.getPayable_amt(), removeCartRP.getYou_save_msg(), removeCartRP.getCart_empty_msg());
                            GlobalBus.getBus().post(updateCart);

                            Events.CartItem cartItem = new Events.CartItem(updateCart.getTotal_item());
                            GlobalBus.getBus().post(cartItem);

                        }

                    } else {
                        method.alertBox(removeCartRP.getMessage());
                    }

                } catch (Exception e) {
                    Log.d(ConstantApi.exceptionError, e.toString());
                    method.alertBox(activity.getResources().getString(R.string.failed_try_again));
                }

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(@NotNull Call<RemoveCartRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e(ConstantApi.failApi, t.toString());
                progressDialog.dismiss();
                method.alertBox(activity.getResources().getString(R.string.failed_try_again));
            }
        });

    }

    private void cartItemUpdate(int qty, String productId, String userId, String productQty, String productSize, int position){

        progressDialog.show();
        progressDialog.setMessage(activity.getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        if (method.isNetworkAvailable(activity)){

            JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(activity));
            jsObj.addProperty("product_id", productId);
            jsObj.addProperty("user_id", userId);
            jsObj.addProperty("product_qty", productQty);
            jsObj.addProperty("buy_now", "false");
            jsObj.addProperty("product_size", productSize);
            ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
            Call<UpdateCartRP> call = apiService.getCartItemUpdate(API.toBase64(jsObj.toString()));
            call.enqueue(new Callback<UpdateCartRP>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(@NotNull Call<UpdateCartRP> call, @NotNull Response<UpdateCartRP> response) {

                    try {

                        UpdateCartRP updateCartRP = response.body();

                        assert updateCartRP != null;
                        if (updateCartRP.getStatus().equals("1")) {

                            Toast.makeText(activity, updateCartRP.getMsg(), Toast.LENGTH_SHORT).show();

                            if (updateCartRP.getSuccess().equals("1")) {

                                cartLists.get(position).setProduct_qty(String.valueOf(qty));
                                cartLists.get(position).setProduct_sell_price(updateCartRP.getProduct_sell_price());
                                cartLists.get(position).setProduct_mrp(updateCartRP.getProduct_mrp());
                                cartLists.get(position).setYou_save_per(updateCartRP.getYou_save_per());
                                cartLists.get(position).setProduct_size(productSize);

                                notifyDataSetChanged();

                                Toast.makeText(activity, updateCartRP.getMsg(), Toast.LENGTH_SHORT).show();

                                Events.UpdateCart updateCart = new Events.UpdateCart(updateCartRP.getTotal_item(), updateCartRP.getPrice(), updateCartRP.getDelivery_charge(),
                                        updateCartRP.getPayable_amt(), updateCartRP.getYou_save_msg(), updateCartRP.getMsg());
                                GlobalBus.getBus().post(updateCart);

                            }

                        } else {
                            method.alertBox(updateCartRP.getMessage());
                        }

                    } catch (Exception e) {
                        Log.d(ConstantApi.exceptionError, e.toString());
                        method.alertBox(activity.getResources().getString(R.string.failed_try_again));
                    }

                    progressDialog.dismiss();

                }

                @Override
                public void onFailure(@NotNull Call<UpdateCartRP> call, @NotNull Throwable t) {
                    // Log error here since request failed
                    Log.e(ConstantApi.failApi, t.toString());
                    progressDialog.dismiss();
                    method.alertBox(activity.getResources().getString(R.string.failed_try_again));
                }
            });

        }
        else {
            method.alertBox(activity.getResources().getString(R.string.no_internet_connection));
        }

    }

    @Override
    public int getItemCount() {
        return cartLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private int Qty = 0;
        private View unavailable_view;
        private MaterialCardView cardview_cart_item;
        private ImageView cart_pro_img, pro_delete_btn;
        private TextView cart_pro_name, cart_pro_price, cart_pro_size, cart_pro_qty, cart_pro_total, pro_status;
        private ImageButton qty_remove_btn, qty_add_btn;
        private LinearLayout ll_size;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            unavailable_view = itemView.findViewById(R.id.unavailable_view);
            pro_status = itemView.findViewById(R.id.pro_status);
            cardview_cart_item = itemView.findViewById(R.id.cardview_cart_item);
            cart_pro_img = itemView.findViewById(R.id.cart_pro_img);
            pro_delete_btn = itemView.findViewById(R.id.pro_delete_btn);
            cart_pro_name = itemView.findViewById(R.id.cart_pro_name);
            cart_pro_price = itemView.findViewById(R.id.cart_pro_price);
            ll_size = itemView.findViewById(R.id.ll_size);
            cart_pro_size = itemView.findViewById(R.id.cart_pro_size);
            cart_pro_qty = itemView.findViewById(R.id.cart_pro_qty);
            cart_pro_total = itemView.findViewById(R.id.cart_pro_total);
            qty_remove_btn = itemView.findViewById(R.id.qty_remove_btn);
            qty_add_btn = itemView.findViewById(R.id.qty_add_btn);

        }
    }
}
