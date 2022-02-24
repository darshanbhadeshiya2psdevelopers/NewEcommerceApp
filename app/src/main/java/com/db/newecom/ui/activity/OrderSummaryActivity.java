package com.db.newecom.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.db.newecom.Api.ApiClient;
import com.db.newecom.Api.ApiInterface;
import com.db.newecom.Model.CartList;
import com.db.newecom.R;
import com.db.newecom.Response.OrderSummaryRP;
import com.db.newecom.Response.RemoveCouponRP;
import com.db.newecom.Utills.API;
import com.db.newecom.Utills.ConstantApi;
import com.db.newecom.Utills.Events;
import com.db.newecom.Utills.GlobalBus;
import com.db.newecom.Utills.Method;
import com.db.newecom.adapters.CartAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderSummaryActivity extends AppCompatActivity {

    private Method method;
    private LinearLayout progressBar;
    private ProgressDialog progressDialog;
    private RelativeLayout empty_layout, you_save_view;
    private FrameLayout rl_layout;
    private TextView address_user_name, address_type, address_mobile, address, total_amount, you_save,
            delivery_charge, amount_payable, total_amount_payable, total_cart_items, empty_msg;
    private LinearLayout ll_address;
    private LottieAnimationView empty_animation;
    private RecyclerView rv_order_summary;
    private CartAdapter cartAdapter;
    private List<CartList> cartLists;
    private String sign, type, productId, productSize, couponId, addressId;
    private boolean isCartEmpty = false;
    private Button confirm_btn, change_address_btn, apply_coupon_btn, remove_coupon_btn, empty_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        GlobalBus.getBus().register(this);

        method = new Method(this);

        progressDialog = new ProgressDialog(this);

        cartLists = new ArrayList<>();

        sign = ConstantApi.currency + " ";

        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        productId = intent.getStringExtra("product_id");
        productSize = intent.getStringExtra("product_size");

        progressBar = findViewById(R.id.ll_progress_order_sum);
        empty_layout = findViewById(R.id.empty_layout);
        rl_layout = findViewById(R.id.rl_layout);
        address_user_name = findViewById(R.id.address_user_name);
        address_type = findViewById(R.id.address_type);
        address_mobile = findViewById(R.id.address_mobile);
        address = findViewById(R.id.address);
        total_amount = findViewById(R.id.total_amount);
        you_save = findViewById(R.id.you_save);
        you_save_view = findViewById(R.id.you_save_view);
        delivery_charge = findViewById(R.id.delivery_charge);
        ll_address = findViewById(R.id.ll_address);
        amount_payable = findViewById(R.id.amount_payable);
        total_amount_payable = findViewById(R.id.total_amount_payable);
        total_cart_items = findViewById(R.id.total_cart_items);
        empty_msg = findViewById(R.id.empty_msg);
        empty_animation = findViewById(R.id.empty_animation);
        empty_btn = findViewById(R.id.empty_btn);
        change_address_btn = findViewById(R.id.change_address_btn);
        rv_order_summary = findViewById(R.id.rv_order_summary);
        confirm_btn = findViewById(R.id.confirm_btn);
        apply_coupon_btn = findViewById(R.id.apply_coupon_btn);
        remove_coupon_btn = findViewById(R.id.remove_coupon_btn);

        rl_layout.setVisibility(View.GONE);

        if (method.isNetworkAvailable(this)) {

            if (method.isLogin()){
                order(method.userId());
            }
            else {
                empty_layout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                empty_msg.setText(getResources().getString(R.string.login_msg));
                empty_animation.setAnimation("login.json");
                empty_btn.setVisibility(View.VISIBLE);
                empty_btn.setText(getResources().getString(R.string.go_to_login));
                empty_btn.setOnClickListener(view ->
                        startActivity(new Intent(this, LoginActivity.class)));
            }

        } else {
            empty_layout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            method.alertBox(getResources().getString(R.string.no_internet_connection));
        }

    }

    @SuppressLint("SetTextI18n")
    @Subscribe
    public void getData(Events.CouponCodeAmount couponCodeAmount) {

        total_amount.setText(sign + couponCodeAmount.getPrice());
        total_amount_payable.setText(sign + couponCodeAmount.getPayable_amt());
        amount_payable.setText(sign + couponCodeAmount.getPayable_amt());
        if (couponCodeAmount.getYou_save_msg().equals("")) {
            you_save.setVisibility(View.GONE);
            you_save_view.setVisibility(View.GONE);
        } else {
            you_save.setVisibility(View.VISIBLE);
            you_save_view.setVisibility(View.VISIBLE);
            you_save.setText(couponCodeAmount.getYou_save_msg());
        }

        apply_coupon_btn.setVisibility(View.GONE);
        remove_coupon_btn.setVisibility(View.VISIBLE);

        couponId = couponCodeAmount.getCoupon_id();

    }

    @SuppressLint("SetTextI18n")
    @Subscribe
    public void geString(Events.UpdateCartOrderSum updateCartOrderSum) {

        if (cartLists.size() == 0) {
            rv_order_summary.setVisibility(View.GONE);
            empty_layout.setVisibility(View.VISIBLE);
        }

        if (cartAdapter != null) {

            String item = getResources().getString(R.string.products) + " " + updateCartOrderSum.getTotal_item();

            total_cart_items.setText(item);
            total_amount.setText(sign + updateCartOrderSum.getPrice());
            amount_payable.setText(sign + updateCartOrderSum.getPayable_amt());

            if (method.isNumeric(updateCartOrderSum.getDelivery_charge())) {
                delivery_charge.setText("+ " + sign + updateCartOrderSum.getDelivery_charge());
            } else {
                delivery_charge.setTextColor(getResources().getColor(R.color.green));
                delivery_charge.setText(updateCartOrderSum.getDelivery_charge());
            }
            total_amount_payable.setText(sign + updateCartOrderSum.getPayable_amt());
            if (updateCartOrderSum.getYou_save_msg().equals("")) {
                you_save.setVisibility(View.GONE);
                you_save_view.setVisibility(View.GONE);
            } else {
                you_save.setVisibility(View.VISIBLE);
                you_save_view.setVisibility(View.VISIBLE);
                you_save.setText(updateCartOrderSum.getYou_save_msg());
            }

        }
    }


    @SuppressLint("SetTextI18n")
    @Subscribe
    public void getData(Events.UpdateDeliveryAddress updateDeliveryAddress) {

        ll_address.setVisibility(View.VISIBLE);

        addressId = updateDeliveryAddress.getAddressId();
        address_user_name.setText(updateDeliveryAddress.getName());
        address.setText(updateDeliveryAddress.getAddress());
        address_mobile.setText(updateDeliveryAddress.getMobileNo());
        address_type.setText(updateDeliveryAddress.getAddressType());

    }

    private void order(String userId) {

        progressBar.setVisibility(View.VISIBLE);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(this));
        if (type.equals("buy_now")) {
            jsObj.addProperty("buy_now", "1");
            jsObj.addProperty("product_id", productId);
            jsObj.addProperty("product_size", productSize);
        } else {
            jsObj.addProperty("buy_now", "0");
        }
        jsObj.addProperty("user_id", userId);
        jsObj.addProperty("device_id", method.getDeviceId());
        ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<OrderSummaryRP> call = apiService.getOrderDetail(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<OrderSummaryRP>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NotNull Call<OrderSummaryRP> call, @NotNull Response<OrderSummaryRP> response) {

                try {

                    OrderSummaryRP orderSummaryRP = response.body();
                    assert orderSummaryRP != null;

                    if (orderSummaryRP.getStatus().equals("1")) {

                        if (orderSummaryRP.getSuccess().equals("1")) {

                            couponId = orderSummaryRP.getCoupon_id();
                            addressId = orderSummaryRP.getAddress_id();

                            cartLists.addAll(orderSummaryRP.getCartLists());

                            Events.CartItem cartItem = new Events.CartItem(orderSummaryRP.getCart_items());
                            GlobalBus.getBus().post(cartItem);

                            if (couponId.equals("0")) {
                                apply_coupon_btn.setVisibility(View.VISIBLE);
                            } else {
                                remove_coupon_btn.setVisibility(View.VISIBLE);
                            }

                            if (orderSummaryRP.getAddress().equals("")) {
                                ll_address.setVisibility(View.GONE);
                            }

                            String item = getResources().getString(R.string.products) + " " + orderSummaryRP.getTotal_item();

                            address_user_name.setText(orderSummaryRP.getName());
                            address.setText(orderSummaryRP.getAddress());
                            address_mobile.setText(orderSummaryRP.getMobile_no());
                            address_type.setText(orderSummaryRP.getAddress_type());
                            total_cart_items.setText(item);
                            total_amount.setText(sign + orderSummaryRP.getPrice());
                            if (method.isNumeric(orderSummaryRP.getDelivery_charge())) {
                                delivery_charge.setText("+ " + sign + orderSummaryRP.getDelivery_charge());
                            } else {
                                delivery_charge.setTextColor(getResources().getColor(R.color.green));
                                delivery_charge.setText(orderSummaryRP.getDelivery_charge());
                            }
                            amount_payable.setText(sign + orderSummaryRP.getPayable_amt());
                            total_amount_payable.setText(sign + orderSummaryRP.getPayable_amt());

                            if (orderSummaryRP.getYou_save_msg().equals("")) {
                                you_save.setVisibility(View.GONE);
                                you_save_view.setVisibility(View.GONE);
                            } else {
                                you_save.setVisibility(View.VISIBLE);
                                you_save_view.setVisibility(View.VISIBLE);
                                you_save.setText(orderSummaryRP.getYou_save_msg());
                            }

                            if (cartLists.size() != 0) {

                                cartAdapter = new CartAdapter(OrderSummaryActivity.this, false, cartLists);
                                rv_order_summary.setAdapter(cartAdapter);

                                rl_layout.setVisibility(View.VISIBLE);

                            } else {
                                empty_layout.setVisibility(View.VISIBLE);
                            }


                            change_address_btn.setOnClickListener(v -> startActivity(new Intent(OrderSummaryActivity.this, SelectAddressActivity.class)));

//                            confirm_btn.setOnClickListener(v -> {
//                                if (!addressId.equals("")) {
//                                    startActivity(new Intent(OrderSummaryActivity.this, PaymentActivity.class)
//                                            .putExtra("type", type)
//                                            .putExtra("coupon_id", couponId)
//                                            .putExtra("address_id", addressId)
//                                            .putExtra("cart_ids", orderSummaryRP.getCart_ids()));
//                                } else {
//                                    method.alertBox(getResources().getString(R.string.please_add_address));
//                                }
//                            });

                            if (couponId.equals("0"))
                                apply_coupon_btn.setVisibility(View.VISIBLE);
                            else
                                remove_coupon_btn.setVisibility(View.VISIBLE);

                            apply_coupon_btn.setOnClickListener(v -> {

                                    startActivity(new Intent(OrderSummaryActivity.this, CouponsActivity.class)
                                            .putExtra("cart_ids", orderSummaryRP.getCart_ids())
                                            .putExtra("type", type));
                            });

                            remove_coupon_btn.setOnClickListener(v -> removeCoupon(method.userId(), couponId));

                            confirm_btn.setOnClickListener(view -> {
                                startActivity(new Intent(OrderSummaryActivity.this, PaymentActivity.class)
                                        .putExtra("total_items", orderSummaryRP.getTotal_item())
                                        .putExtra("coupon_id", couponId)
                                        .putExtra("cart_ids", orderSummaryRP.getCart_ids())
                                        .putExtra("address_id", addressId)
                                        .putExtra("total", orderSummaryRP.getPayable_amt()));
                            });

                        } else {
                            isCartEmpty = true;
                            empty_msg.setText(orderSummaryRP.getMsg());
                            empty_layout.setVisibility(View.VISIBLE);
                        }

                    } else {
                        method.alertBox(orderSummaryRP.getMessage());
                    }

                } catch (Exception e) {
                    Log.d(ConstantApi.exceptionError, e.toString());
                    empty_layout.setVisibility(View.VISIBLE);
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }

                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(@NotNull Call<OrderSummaryRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e(ConstantApi.failApi, t.toString());
                empty_layout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                method.alertBox(getResources().getString(R.string.failed_try_again));
            }
        });

    }

    public void removeCoupon(String userId, String sendCouponId) {

        progressDialog.show();
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(this));
        jsObj.addProperty("user_id", userId);
        jsObj.addProperty("cart_type", "main_cart");
        jsObj.addProperty("coupon_id", sendCouponId);
        ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<RemoveCouponRP> call = apiService.getRemoveCouponDetail(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<RemoveCouponRP>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NotNull Call<RemoveCouponRP> call, @NotNull Response<RemoveCouponRP> response) {

                try {

                    RemoveCouponRP removeCouponRP = response.body();

                    assert removeCouponRP != null;
                    if (removeCouponRP.getStatus().equals("1")) {

                        if (removeCouponRP.getSuccess().equals("1")) {

                            couponId = "0";

                            total_amount.setText(sign + removeCouponRP.getPrice());
                            amount_payable.setText(sign + removeCouponRP.getPayable_amt());
                            total_amount_payable.setText(sign + removeCouponRP.getPayable_amt());

                            if (removeCouponRP.getYou_save_msg().equals("")) {
                                you_save.setVisibility(View.GONE);
                                you_save_view.setVisibility(View.GONE);
                            } else {
                                you_save.setVisibility(View.VISIBLE);
                                you_save_view.setVisibility(View.VISIBLE);
                                you_save.setText(removeCouponRP.getYou_save_msg());
                            }

                            remove_coupon_btn.setVisibility(View.GONE);
                            apply_coupon_btn.setVisibility(View.VISIBLE);

                        } else if (removeCouponRP.getSuccess().equals("2")) {
                            method.alertBox(removeCouponRP.getMsg());
                        } else {
                            method.alertBox(removeCouponRP.getMsg());
                        }

                    } else {
                        method.alertBox(removeCouponRP.getMessage());
                    }

                } catch (Exception e) {
                    Log.d(ConstantApi.exceptionError, e.toString());
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(@NotNull Call<RemoveCouponRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e(ConstantApi.failApi, t.toString());
                progressDialog.dismiss();
                method.alertBox(getResources().getString(R.string.failed_try_again));
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home)
            onBackPressed();
        else
            return super.onOptionsItemSelected(item);

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (isCartEmpty) {
            startActivity(new Intent(OrderSummaryActivity.this, MainActivity.class));
            finishAffinity();
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
//        destroyData(method.userId(), method.getDeviceId());
        Log.d("onDestroy_orderSummery", "onDestroy_orderSummery");
        GlobalBus.getBus().unregister(this);
        super.onDestroy();
    }
}