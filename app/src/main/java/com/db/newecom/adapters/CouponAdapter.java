package com.db.newecom.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.db.newecom.Api.ApiClient;
import com.db.newecom.Api.ApiInterface;
import com.db.newecom.Model.CouponsList;
import com.db.newecom.R;
import com.db.newecom.Response.ApplyCouponRP;
import com.db.newecom.Utills.API;
import com.db.newecom.Utills.ConstantApi;
import com.db.newecom.Utills.Events;
import com.db.newecom.Utills.GlobalBus;
import com.db.newecom.Utills.Method;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.ViewHolder> {

    private Method method;
    private Activity activity;
    private List<CouponsList> couponsLists;
    private ProgressDialog progressDialog;
    private String cart_ids;

    public CouponAdapter(Activity activity, List<CouponsList> couponsLists, String cart_ids) {
        this.activity = activity;
        this.couponsLists = couponsLists;
        this.cart_ids = cart_ids;
        method = new Method(activity);
        progressDialog = new ProgressDialog(activity, R.style.ProgressDialogStyle);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.item_coupon, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(activity).load(couponsLists.get(position).getCoupon_image_thumb())
                .placeholder(R.drawable.placeholder_img).into(holder.coupon_img);

        holder.coupon_code.setText(couponsLists.get(position).getCoupon_code());

        holder.coupon_max_amount.setText(couponsLists.get(position).getCoupon_amt());

        holder.apply_coupon_btn.setOnClickListener(view ->
                applycoupon(method.userId(), couponsLists.get(position).getId(), cart_ids));

    }

    private void applycoupon(String userId, String id, String cart_ids) {

        progressDialog.show();
        progressDialog.setMessage(activity.getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(activity));
        jsObj.addProperty("user_id", userId);
        jsObj.addProperty("coupon_id", id);
        jsObj.addProperty("cart_ids", cart_ids);
        jsObj.addProperty("cart_type", "main_cart");

        ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<ApplyCouponRP> call = apiService.getApplyCouponsDetail(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<ApplyCouponRP>() {
            @SuppressLint({"SetTextI18n", "SetJavaScriptEnabled"})
            @Override
            public void onResponse(@NotNull Call<ApplyCouponRP> call, @NotNull Response<ApplyCouponRP> response) {

                try {

                    ApplyCouponRP applyCouponRP = response.body();
                    assert applyCouponRP != null;

                    if (applyCouponRP.getStatus().equals("1")) {

                        if (applyCouponRP.getSuccess().equals("1")) {

                            activity.onBackPressed();

                            Events.OnBackData onBackData = new Events.OnBackData("");
                            GlobalBus.getBus().post(onBackData);

                            Events.CouponCodeAmount couponCodeAmount = new Events.CouponCodeAmount(applyCouponRP.getCoupon_id(), applyCouponRP.getPrice(), applyCouponRP.getPayable_amt(), applyCouponRP.getYou_save_msg());
                            GlobalBus.getBus().post(couponCodeAmount);

                        } else if (applyCouponRP.getSuccess().equals("2")) {
                            method.alertBox(applyCouponRP.getMsg());
                        } else {
                            method.alertBox(applyCouponRP.getMsg());
                        }

                    } else {
                        method.alertBox(applyCouponRP.getMessage());
                    }

                } catch (Exception e) {
                    Log.d(ConstantApi.exceptionError, e.toString());
                    method.alertBox(activity.getResources().getString(R.string.failed_try_again));
                }

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(@NotNull Call<ApplyCouponRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e(ConstantApi.failApi, t.toString());
                progressDialog.dismiss();
                method.alertBox(activity.getResources().getString(R.string.failed_try_again));
            }
        });

    }

    @Override
    public int getItemCount() {
        return couponsLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView coupon_img;
        private TextView coupon_code, coupon_max_amount;
        private Button apply_coupon_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            coupon_img = itemView.findViewById(R.id.coupon_img);
            coupon_code = itemView.findViewById(R.id.coupon_code);
            coupon_max_amount = itemView.findViewById(R.id.coupon_max_amount);
            apply_coupon_btn = itemView.findViewById(R.id.apply_coupon_btn);
        }
    }
}
