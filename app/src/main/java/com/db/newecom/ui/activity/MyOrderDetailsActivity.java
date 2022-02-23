package com.db.newecom.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.db.newecom.Api.ApiClient;
import com.db.newecom.Api.ApiInterface;
import com.db.newecom.R;
import com.db.newecom.Response.CancelOrderProRP;
import com.db.newecom.Response.MyOrderDetailRP;
import com.db.newecom.Response.RRSubmitRP;
import com.db.newecom.Utills.API;
import com.db.newecom.Utills.ConstantApi;
import com.db.newecom.Utills.Events;
import com.db.newecom.Utills.GlobalBus;
import com.db.newecom.Utills.Method;
import com.db.newecom.adapters.HomeMyOrdersAdapter;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

import io.github.lizhangqu.coreprogress.ProgressHelper;
import io.github.lizhangqu.coreprogress.ProgressUIListener;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyOrderDetailsActivity extends AppCompatActivity {

    private Method method;
    private LinearLayout progressBar;
    private RelativeLayout empty_layout, rl_add_review;
    private ScrollView scrollView_main;
    private Toolbar toolbar;
    private FrameLayout cart_btn;
    private RelativeLayout rl_order_product, rl_discount, rl_paymentID;
    private RecyclerView rv_other_pro;
    private HomeMyOrdersAdapter ordersAdapter;
    private Button invoice_download_btn, cancel_order_btn, claim_order_btn, claim_pro_btn,
            dialog_cancel_pro_btn, dialog_cancel_order_btn, rate_dialog_btn;
    private TextView order_count, tv_order_id, order_pro_name1, order_pro_desc1, order_pro_price1, order_pro_qty1,
            tv_placed_status, tv_packed_status, tv_packed_status_dt, tv_shipped_status, tv_shipped_status_dt,
            tv_delivered_status, tv_delivered_status_dt, tv_address_username, tv_address_user_email, tv_address,
            tv_address_user_mobile, tv_total_amount, tv_discount, discount, tv_delivery_charge, tv_amount_payable,
            tv_payment_mode, payment_id, tv_payment_id, tv_cancel_reason, tv_refundStatus, cancel_this_pro,
            rate_dialog_pro_name, rate_dialog_pro_desc;
    private RatingBar rate_dialog_ratingbar;
    private ImageView order_pro_img1, pro_img, rate_dialog_pro_image;
    private View line_packed_status, round_packed_status, line_shipped_status, round_shipped_status,
            line_delivery_status, round_delivery_status;
    private MaterialCardView cardView_otheritems_in_order, cardView_cancel_order_detail;
    private String orderUniqueId, productId, sign, reason, review_msg;
    private Dialog dialog, dialog1, dialog2;
    private ProgressDialog progressDialog;
    private TextView order_unique_id, order_unique_id2, pro_name, pro_desc, pro_price, pro_qty;
    private EditText et_cancel_reason, et_cancel_reason2, et_rate_dialog_msg;
    private int getRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_details);

        GlobalBus.getBus().register(this);

        orderUniqueId = getIntent().getStringExtra("order_unique_id");
        productId = getIntent().getStringExtra("product_id");

        method = new Method(this);
        progressDialog = new ProgressDialog(this);
        sign = ConstantApi.currency + " ";

        initializeIDs();

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        scrollView_main.setVisibility(View.GONE);
        cardView_cancel_order_detail.setVisibility(View.GONE);

        if (method.isNetworkAvailable(this)) {
            if (method.isLogin()) {
                orderDetail(method.userId(), productId, orderUniqueId);
            } else {
                method.alertBox(getResources().getString(R.string.you_have_not_login));
                empty_layout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        } else {
            progressBar.setVisibility(View.GONE);
            empty_layout.setVisibility(View.VISIBLE);
            method.alertBox(getResources().getString(R.string.no_internet_connection));
        }

        cart_btn.setOnClickListener(view ->
                startActivity(new Intent(this, CartActivity.class)));

        cancel_order_btn.setOnClickListener(v -> dialog.show());

    }

    @Subscribe
    public void geString(Events.RatingUpdate ratingUpdate) {

        et_rate_dialog_msg.setText(ratingUpdate.getRate_msg());

        et_rate_dialog_msg.requestFocus();
    }

    @Subscribe
    public void geString(Events.CancelOrder cancelOrder) {
        scrollView_main.setVisibility(View.GONE);
        empty_layout.setVisibility(View.GONE);
        orderDetail(method.userId(), cancelOrder.getProductId(), cancelOrder.getOrderUniqueId());
    }

    @Subscribe
    public void geString(Events.ClaimOrder claimOrder) {
        scrollView_main.setVisibility(View.GONE);
        empty_layout.setVisibility(View.GONE);
        orderDetail(method.userId(), claimOrder.getProductId(), claimOrder.getOrderUniqueId());
    }

    @SuppressLint("SetTextI18n")
    @Subscribe
    public void geString(Events.CartItem cartItem){
        if (cartItem.getCart_item().equals("0"))
            order_count.setVisibility(View.GONE);
        else {
            order_count.setVisibility(View.VISIBLE);
            order_count.setText(cartItem.getCart_item());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        scrollView_main.setVisibility(View.GONE);
        cardView_cancel_order_detail.setVisibility(View.GONE);

        if (method.isNetworkAvailable(this)) {
            if (method.isLogin()) {
                orderDetail(method.userId(), productId, orderUniqueId);
            } else {
                method.alertBox(getResources().getString(R.string.you_have_not_login));
                empty_layout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        } else {
            progressBar.setVisibility(View.GONE);
            empty_layout.setVisibility(View.VISIBLE);
            method.alertBox(getResources().getString(R.string.no_internet_connection));
        }
    }

    private void initializeIDs() {

        progressBar = findViewById(R.id.ll_progress_order_details);
        empty_layout = findViewById(R.id.empty_layout);
        scrollView_main= findViewById(R.id.scrollview_order);
        order_count = findViewById(R.id.order_count);
        tv_order_id = findViewById(R.id.tv_order_id);
        order_pro_name1 = findViewById(R.id.order_pro_name1);
        order_pro_desc1 = findViewById(R.id.order_pro_desc1);
        order_pro_price1 = findViewById(R.id.order_pro_price1);
        order_pro_qty1 = findViewById(R.id.order_pro_qty1);
        tv_placed_status = findViewById(R.id.tv_placed_status);
        tv_packed_status = findViewById(R.id.tv_packed_status);
        tv_packed_status_dt = findViewById(R.id.tv_packed_status_dt);
        tv_shipped_status = findViewById(R.id.tv_shipped_status);
        tv_shipped_status_dt = findViewById(R.id.tv_shipped_status_dt);
        tv_delivered_status= findViewById(R.id.tv_delivered_status);
        tv_delivered_status_dt = findViewById(R.id.tv_delivered_status_dt);
        tv_address_username = findViewById(R.id.tv_address_username);
        tv_address_user_email = findViewById(R.id.tv_address_user_email);
        tv_address = findViewById(R.id.tv_address);
        tv_address_user_mobile = findViewById(R.id.tv_address_user_mobile);
        tv_total_amount= findViewById(R.id.tv_total_amount);
        tv_discount = findViewById(R.id.tv_discount);
        discount = findViewById(R.id.discount);
        tv_delivery_charge = findViewById(R.id.tv_delivery_charge);
        tv_amount_payable = findViewById(R.id.tv_amount_payable);
        tv_payment_mode = findViewById(R.id.tv_payment_mode);
        payment_id = findViewById(R.id.payment_id);
        tv_payment_id = findViewById(R.id.tv_payment_id);
        rv_other_pro = findViewById(R.id.rv_other_pro);
        tv_cancel_reason = findViewById(R.id.tv_cancel_reason);
        tv_refundStatus = findViewById(R.id.tv_refundStatus);
        toolbar = findViewById(R.id.toolbar);
        cart_btn = findViewById(R.id.cart_btn);
        rl_order_product = findViewById(R.id.rl_order_product);
        rl_discount = findViewById(R.id.rl_discount);
        rl_paymentID = findViewById(R.id.rl_paymentID);
        rv_other_pro = findViewById(R.id.rv_other_pro);
        invoice_download_btn = findViewById(R.id.invoice_download_btn);
        cancel_this_pro = findViewById(R.id.cancel_this_pro);
        cancel_order_btn = findViewById(R.id.cancel_order_btn);
        claim_pro_btn = findViewById(R.id.claim_pro_btn);
        claim_order_btn = findViewById(R.id.claim_order_btn);
        order_pro_img1 = findViewById(R.id.order_pro_img1);
        line_packed_status = findViewById(R.id.line_packed_status);
        round_packed_status = findViewById(R.id.round_packed_status);
        line_shipped_status = findViewById(R.id.line_shipped_status);
        line_delivery_status = findViewById(R.id.line_delivery_status);
        round_shipped_status = findViewById(R.id.round_shipped_status);
        round_delivery_status = findViewById(R.id.round_delivery_status);
        cardView_otheritems_in_order = findViewById(R.id.cardView_otheritems_in_order);
        cardView_cancel_order_detail = findViewById(R.id.cardView_cancel_order_detail);
        rl_add_review = findViewById(R.id.rl_add_review);

        dialog1 = new Dialog(this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.product_rating_dialog);
        dialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog1.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        rate_dialog_pro_image = dialog1.findViewById(R.id.rate_pro_img);
        rate_dialog_pro_name = dialog1.findViewById(R.id.rate_pro_name);
        rate_dialog_pro_desc = dialog1.findViewById(R.id.rate_pro_desc);
        rate_dialog_ratingbar = dialog1.findViewById(R.id.rating_bar_of_dialog);
        et_rate_dialog_msg = dialog1.findViewById(R.id.et_review_msg);
        rate_dialog_btn = dialog1.findViewById(R.id.rate_submit_btn);

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.order_cancel_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        dialog2 = new Dialog(this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.setContentView(R.layout.product_cancel_dialog);
        dialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog2.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        dialog_cancel_order_btn = dialog.findViewById(R.id.dialog_cancel_order_btn);
        order_unique_id = dialog.findViewById(R.id.order_unique_id);
        et_cancel_reason = dialog.findViewById(R.id.et_cancel_reason);

        order_unique_id2 = dialog2.findViewById(R.id.order_unique_id);
        dialog_cancel_pro_btn = dialog2.findViewById(R.id.dialog_cancel_pro_btn);
        pro_img = dialog2.findViewById(R.id.pro_img);
        pro_name = dialog2.findViewById(R.id.pro_name);
        pro_desc = dialog2.findViewById(R.id.pro_desc);
        pro_price = dialog2.findViewById(R.id.pro_price);
        pro_qty = dialog2.findViewById(R.id.pro_qty);
        et_cancel_reason2 = dialog2.findViewById(R.id.et_cancel_reason);


    }

    private void orderDetail(String userId, String productId, String orderUniqueId) {

        progressBar.setVisibility(View.VISIBLE);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(this));
        jsObj.addProperty("user_id", userId);
        jsObj.addProperty("product_id", productId);
        jsObj.addProperty("order_unique_id", orderUniqueId);
        ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<MyOrderDetailRP> call = apiService.getMyOrderDetail(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<MyOrderDetailRP>() {
            @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
            @Override
            public void onResponse(@NotNull Call<MyOrderDetailRP> call, @NotNull Response<MyOrderDetailRP> response) {

                try {

                    MyOrderDetailRP myOrderDetailRP = response.body();
                    assert myOrderDetailRP != null;

                    if (myOrderDetailRP.getStatus().equals("1")) {

                        if (myOrderDetailRP.getSuccess().equals("1")) {

                            scrollView_main.setVisibility(View.VISIBLE);

                            if (!myOrderDetailRP.getCart_items().equals("0")){
                                order_count.setVisibility(View.VISIBLE);
                                order_count.setText(myOrderDetailRP.getCart_items());
                            }

                            Glide.with(MyOrderDetailsActivity.this).load(myOrderDetailRP.getProduct_image())
                                    .placeholder(R.drawable.placeholder_img).into(order_pro_img1);

                            Glide.with(MyOrderDetailsActivity.this).load(myOrderDetailRP.getProduct_image())
                                    .placeholder(R.drawable.placeholder_img).into(pro_img);

                            Glide.with(MyOrderDetailsActivity.this).load(myOrderDetailRP.getProduct_image())
                                    .placeholder(R.drawable.placeholder_img).into(rate_dialog_pro_image);

                            String order = getResources().getString(R.string.order_id) + myOrderDetailRP.getOrder_unique_id();
                            tv_order_id.setText(order);
                            order_unique_id.setText(order);
                            order_unique_id2.setText(order);

                            order_pro_name1.setText(myOrderDetailRP.getProduct_title());
                            pro_name.setText(myOrderDetailRP.getProduct_title());
                            rate_dialog_pro_name.setText(myOrderDetailRP.getProduct_title());

                            if (!myOrderDetailRP.getProduct_size().equals("") && !myOrderDetailRP.getProduct_color().equals("")) {
                                String sizeColor = getResources().getString(R.string.size) + " "
                                        + myOrderDetailRP.getProduct_size() + ", " +
                                        getResources().getString(R.string.color) + " " + myOrderDetailRP.getProduct_color();
                                order_pro_desc1.setText(sizeColor);
                                pro_desc.setText(sizeColor);
                                rate_dialog_pro_desc.setText(sizeColor);
                            } else {
                                if (!myOrderDetailRP.getProduct_size().equals("")) {
                                    order_pro_desc1.setText(getResources().getString(R.string.size) + " " + myOrderDetailRP.getProduct_size());
                                    pro_desc.setText(getResources().getString(R.string.size) + " " + myOrderDetailRP.getProduct_size());
                                    rate_dialog_pro_desc.setText(getResources().getString(R.string.size) + " " + myOrderDetailRP.getProduct_size());
                                } else if (!myOrderDetailRP.getProduct_color().equals("")) {
                                    order_pro_desc1.setText(getResources().getString(R.string.color) + " " + myOrderDetailRP.getProduct_color());
                                    pro_desc.setText(getResources().getString(R.string.color) + " " + myOrderDetailRP.getProduct_color());
                                    rate_dialog_pro_desc.setText(getResources().getString(R.string.color) + " " + myOrderDetailRP.getProduct_color());
                                } else {
                                    order_pro_desc1.setVisibility(View.GONE);
                                    pro_desc.setVisibility(View.GONE);
                                    rate_dialog_pro_desc.setVisibility(View.GONE);
                                }
                            }

                            order_pro_price1.setText(sign + myOrderDetailRP.getSelling_price());
                            pro_price.setText(sign + myOrderDetailRP.getSelling_price());
                            order_pro_qty1.setText("Quantity : " + myOrderDetailRP.getProduct_qty());
                            pro_qty.setText("Quantity : " + myOrderDetailRP.getProduct_qty());

                            tv_address_username.setText(myOrderDetailRP.getName());
                            tv_address_user_email.setText(myOrderDetailRP.getEmail());
                            tv_address.setText(myOrderDetailRP.getAddress());
                            tv_address_user_mobile.setText(myOrderDetailRP.getMobile_no());

                            tv_total_amount.setText(sign + myOrderDetailRP.getOpd_price());

                            if (myOrderDetailRP.getOpd_discount().equals("0.00")){
                                rl_discount.setVisibility(View.GONE);
                            } else {
                                tv_discount.setTextColor(getResources().getColor(R.color.green));
                                tv_discount.setText("- " + sign + myOrderDetailRP.getOpd_discount());
                            }

                            if (method.isNumeric(myOrderDetailRP.getOpd_delivery())) {
                                tv_delivery_charge.setText("+ " + sign + myOrderDetailRP.getDelivery_charge());
                            } else {
                                tv_delivery_charge.setTextColor(getResources().getColor(R.color.green));
                                tv_delivery_charge.setText(myOrderDetailRP.getDelivery_charge());
                            }

                            tv_amount_payable.setText(sign + myOrderDetailRP.getOpd_amountPayable());
                            tv_payment_mode.setText(myOrderDetailRP.getPayment_mode());

                            if (myOrderDetailRP.getPayment_id().equals("0")) {
                                rl_paymentID.setVisibility(View.GONE);
                            } else {
                                tv_payment_id.setText(myOrderDetailRP.getPayment_id());
                            }

                            tv_placed_status.setText(myOrderDetailRP.getOrderTrackingLists().get(0).getDatetime());
                            tv_delivered_status_dt.setText("Exp. " + myOrderDetailRP.getDelivery_datetime());

                            if (myOrderDetailRP.getOrderTrackingLists().get(1).getStatus_title().equals("Packed")) {
                                line_packed_status.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                                round_packed_status.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                                tv_packed_status_dt.setText(myOrderDetailRP.getOrderTrackingLists().get(1).getDatetime());
                            }
                            else if (myOrderDetailRP.getOrderTrackingLists().get(1).getStatus_title().equals("Shipped")) {
                                line_packed_status.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                                round_packed_status.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                                line_shipped_status.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                                round_shipped_status.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                                tv_shipped_status_dt.setText(myOrderDetailRP.getOrderTrackingLists().get(1).getDatetime());
                            }
                            else if (myOrderDetailRP.getOrderTrackingLists().get(1).getStatus_title().equals("Delivered")) {
                                line_packed_status.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                                round_packed_status.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                                line_shipped_status.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                                round_shipped_status.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                                line_delivery_status.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                                round_delivery_status.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                                tv_delivered_status_dt.setText(myOrderDetailRP.getOrderTrackingLists().get(1).getDatetime());
                            }
                            else if (myOrderDetailRP.getOrderTrackingLists().get(1).getStatus_title().equals("Cancelled")) {
                                line_packed_status.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                                round_packed_status.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                                tv_delivered_status_dt.setText(myOrderDetailRP.getOrderTrackingLists().get(1).getDatetime());
                                tv_packed_status.setText("Cancelled");
                                tv_packed_status_dt.setVisibility(View.GONE);
                                cardView_cancel_order_detail.setVisibility(View.VISIBLE);
                                line_shipped_status.setVisibility(View.GONE);
                                line_delivery_status.setVisibility(View.GONE);
                                round_shipped_status.setVisibility(View.GONE);
                                round_delivery_status.setVisibility(View.GONE);
                                tv_shipped_status.setVisibility(View.GONE);
                                tv_shipped_status_dt.setVisibility(View.GONE);
                                tv_delivered_status.setVisibility(View.GONE);
                                tv_delivered_status_dt.setVisibility(View.GONE);

                            }

                            if (myOrderDetailRP.getCurrent_order_status().equals("true")) {
                                invoice_download_btn.setVisibility(View.VISIBLE);
                                rl_add_review.setVisibility(View.VISIBLE);
//                                viewRating.setVisibility(View.VISIBLE);
//                                relRating.setVisibility(View.VISIBLE);
//                                ratingView.setRating(Float.parseFloat(myOrderDetailRP.getMy_rating()));
                            } else {
                                invoice_download_btn.setVisibility(View.GONE);
                                rl_add_review.setVisibility(View.GONE);
//                                viewRating.setVisibility(View.GONE);
//                                relRating.setVisibility(View.GONE);
                            }

                            tv_cancel_reason.setText(myOrderDetailRP.getReason());
                            tv_refundStatus.setText(myOrderDetailRP.getRefund_status());

                            if (myOrderDetailRP.getCancel_product().equals("true") &&
                                    myOrderDetailRP.getOrder_other_items_status().equals("true")) {
                                //cardView_cancel_order_detail.setVisibility(View.GONE);
                                cancel_order_btn.setVisibility(View.VISIBLE);

                            } else {
                                //cardView_cancel_order_detail.setVisibility(View.VISIBLE);
                                cancel_order_btn.setVisibility(View.GONE);
                            }

//                            if (myOrderDetailRP.getCancel_order_amt().equals("")) {
//                                relCancelOrderPrice.setVisibility(View.GONE);
//                            } else {
//                                relCancelOrderPrice.setVisibility(View.VISIBLE);
//                                textViewOpdCancelOrderPrice.setText(sign + myOrderDetailRP.getCancel_order_amt());
//                            }

                            if (myOrderDetailRP.getCancel_product().equals("true")) {
                                cancel_this_pro.setVisibility(View.VISIBLE);
                            } else {
                                cancel_this_pro.setVisibility(View.GONE);
                            }
                            if (myOrderDetailRP.getOrder_other_items_status().equals("true")) {
                                cancel_order_btn.setVisibility(View.VISIBLE);
                            } else {
                                cancel_order_btn.setVisibility(View.GONE);
                            }

                            if (myOrderDetailRP.getIs_claim().equals("true")) {
                                claim_pro_btn.setVisibility(View.VISIBLE);
                            } else {
                                claim_pro_btn.setVisibility(View.GONE);
                            }
                            if (myOrderDetailRP.getIs_order_claim().equals("true")) {
                                claim_order_btn.setVisibility(View.VISIBLE);
                            } else {
                                claim_order_btn.setVisibility(View.GONE);
                            }

                            if (myOrderDetailRP.getMyOrderLists().size() != 0) {
                                cardView_otheritems_in_order.setVisibility(View.VISIBLE);
                                ordersAdapter = new HomeMyOrdersAdapter(MyOrderDetailsActivity.this, myOrderDetailRP.getMyOrderLists(), true);
                                rv_other_pro.setAdapter(ordersAdapter);
                            } else {
                                cardView_otheritems_in_order.setVisibility(View.GONE);
                            }

                            //relMyOrder.setOnClickListener(v -> onClick.click(0, "my_order_detail", myOrderDetailRP.getProduct_title(), myOrderDetailRP.getProduct_id(), "", "", ""));

//                            relRating.setOnClickListener(v -> startActivity(new Intent(getActivity(), RatingReview.class)
//                                    .putExtra("product_id", myOrderDetailRP.getProduct_id())));

                            rl_order_product.setOnClickListener(view ->
                                    startActivity(new Intent(MyOrderDetailsActivity.this, ProductDetailsActivity.class)
                                            .putExtra("title", myOrderDetailRP.getProduct_title())
                                            .putExtra("product_id", myOrderDetailRP.getProduct_id())));

                            invoice_download_btn.setOnClickListener(v -> Dexter.withContext(MyOrderDetailsActivity.this)
                                    .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    .withListener(new PermissionListener() {
                                        @Override
                                        public void onPermissionGranted(PermissionGrantedResponse response1) {
                                            downloadInvoice(myOrderDetailRP.getOrder_unique_id(), myOrderDetailRP.getDownload_invoice());
                                        }

                                        @Override
                                        public void onPermissionDenied(PermissionDeniedResponse response1) {
                                            // check for permanent denial of permission
                                            if (response1.isPermanentlyDenied()) {
                                                // navigate user to app settings
                                            }
                                            method.alertBox(getResources().getString(R.string.cannot_use_save_permission));
                                        }

                                        @Override
                                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                            token.continuePermissionRequest();
                                        }
                                    }).check());

                            cancel_order_btn.setOnClickListener(view -> {
                                if (myOrderDetailRP.getBank_status().equals("1"))
                                    dialog.show();
                                else
                                    startActivity(new Intent(MyOrderDetailsActivity.this, Add_Bank_Activity.class));
                            });

                            dialog_cancel_order_btn.setOnClickListener(view ->
                                    cancelProOrOrder(myOrderDetailRP.getOrder_id(), method.userId(), "0", myOrderDetailRP.getBank_id()));

                            cancel_this_pro.setOnClickListener(view -> {
                                if (myOrderDetailRP.getBank_status().equals("1"))
                                    dialog2.show();
                                else
                                    startActivity(new Intent(MyOrderDetailsActivity.this, Add_Bank_Activity.class));
                            });

                            rate_dialog_ratingbar.setRating(Integer.parseInt(myOrderDetailRP.getMy_rating()));

                            dialog_cancel_pro_btn.setOnClickListener(view ->
                                    cancelProOrOrder(myOrderDetailRP.getOrder_id(), method.userId(), productId, myOrderDetailRP.getBank_id()));

                            rl_add_review.setOnClickListener(v -> dialog1.show());

                            rate_dialog_btn.setOnClickListener(v ->
                                    submitRatingReview(method.userId(), myOrderDetailRP.getProduct_id()));

//                            textViewProRefundClaim.setOnClickListener(v -> {
//                                CancelOrderFragment cancelOrderFragment = new CancelOrderFragment();
//                                Bundle bundle = new Bundle();
//                                bundle.putString("type", "claim_product");
//                                bundle.putString("order_id", myOrderDetailRP.getOrder_id());
//                                bundle.putString("order_unique_id", myOrderDetailRP.getOrder_unique_id());
//                                bundle.putString("product_id", myOrderDetailRP.getProduct_id());
//                                bundle.putString("payment_type", "isCancel");
//                                cancelOrderFragment.setArguments(bundle);
//                                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.frameLayout_main, cancelOrderFragment,
//                                        getResources().getString(R.string.cancel_order)).addToBackStack(getResources().getString(R.string.cancel_order))
//                                        .commitAllowingStateLoss();
//                            });
//
//                            textViewOrderRefundClaim.setOnClickListener(v -> {
//                                CancelOrderFragment cancelOrderFragment = new CancelOrderFragment();
//                                Bundle bundle = new Bundle();
//                                bundle.putString("type", "claim_order");
//                                bundle.putString("order_id", myOrderDetailRP.getOrder_id());
//                                bundle.putString("order_unique_id", myOrderDetailRP.getOrder_unique_id());
//                                bundle.putString("product_id", myOrderDetailRP.getProduct_id());
//                                bundle.putString("payment_type", "isCancel");
//                                cancelOrderFragment.setArguments(bundle);
//                                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.frameLayout_main, cancelOrderFragment,
//                                        getResources().getString(R.string.cancel_order)).addToBackStack(getResources().getString(R.string.cancel_order))
//                                        .commitAllowingStateLoss();
//                            });

                        } else {
                            empty_layout.setVisibility(View.VISIBLE);
                            method.alertBox(myOrderDetailRP.getMsg());
                        }

                    } else {
                        empty_layout.setVisibility(View.VISIBLE);
                        method.alertBox(myOrderDetailRP.getMessage());
                    }

                } catch (Exception e) {
                    Log.d(ConstantApi.exceptionError, e.toString());
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }

                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(@NotNull Call<MyOrderDetailRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e("fail_data", t.toString());
                empty_layout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                method.alertBox(getResources().getString(R.string.failed_try_again));
            }
        });

    }

    public void cancelProOrOrder(String order_id, String user_id, String product_id, String bank_id){

        if (product_id.equals("0"))
            reason = et_cancel_reason.getText().toString();
        else
            reason = et_cancel_reason2.getText().toString();

        if (reason.equals(""))
            Toast.makeText(MyOrderDetailsActivity.this,
                    "Please Enter Reason For Cancellation...", Toast.LENGTH_SHORT).show();
        else {
            progressDialog.show();
            progressDialog.setMessage(getResources().getString(R.string.loading));
            progressDialog.setCancelable(false);

            JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(this));
            jsObj.addProperty("user_id", user_id);
            jsObj.addProperty("order_id", order_id);
            jsObj.addProperty("product_id", product_id);
            jsObj.addProperty("reason", reason);
            jsObj.addProperty("bank_id", bank_id);
            ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
            Call<CancelOrderProRP> call = apiService.cancelOrderORProduct(API.toBase64(jsObj.toString()));
            call.enqueue(new Callback<CancelOrderProRP>() {
                @Override
                public void onResponse(@NotNull Call<CancelOrderProRP> call, @NotNull Response<CancelOrderProRP> response) {


                    try {
                        CancelOrderProRP cancelOrderProRP = response.body();
                        assert cancelOrderProRP != null;

                        if (cancelOrderProRP.getStatus().equals("1")) {

                            if (cancelOrderProRP.getSuccess().equals("1")) {
                                Events.CancelOrder cancelOrder = new Events.CancelOrder(productId, orderUniqueId, "", cancelOrderProRP.getMyOrderLists());
                                GlobalBus.getBus().post(cancelOrder);

                                Toast.makeText(MyOrderDetailsActivity.this, cancelOrderProRP.getMsg(), Toast.LENGTH_SHORT).show();
                            } else {
                                method.alertBox(cancelOrderProRP.getMsg());
                            }

                        } else {
                            method.alertBox(cancelOrderProRP.getMessage());
                        }
                        dialog.dismiss();
                        dialog2.dismiss();

                    } catch (Exception e) {
                        Log.d(ConstantApi.exceptionError, e.toString());
                        dialog.dismiss();
                        dialog2.dismiss();
                        method.alertBox(getResources().getString(R.string.failed_try_again));
                    }

                    progressDialog.dismiss();

                }

                @Override
                public void onFailure(@NotNull Call<CancelOrderProRP> call, @NotNull Throwable t) {
                    // Log error here since request failed
                    Log.e(ConstantApi.failApi, t.toString());
                    dialog.dismiss();
                    dialog2.dismiss();
                    progressDialog.dismiss();
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }
            });

        }

    }

    public void submitRatingReview(String user_id, String product_id){

        getRating = (int) rate_dialog_ratingbar.getRating();

        review_msg = et_rate_dialog_msg.getText().toString();

        if (getRating == 0){
            Toast.makeText(this, getResources().getString(R.string.please_give_rating),
                    Toast.LENGTH_SHORT).show();
        }
        else if (review_msg.equals("")){
            Toast.makeText(this, getResources().getString(R.string.please_enter_review),
                    Toast.LENGTH_SHORT).show();
        }
        else {
            progressDialog.show();
            progressDialog.setMessage(getResources().getString(R.string.loading));
            progressDialog.setCancelable(false);

            JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(this));
            jsObj.addProperty("user_id", user_id);
            jsObj.addProperty("product_id", product_id);
            jsObj.addProperty("review_desc", review_msg);
            jsObj.addProperty("rate", getRating);
            ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
            Call<RRSubmitRP> call = apiService.submitRatingReview(API.toBase64(jsObj.toString()));
            call.enqueue(new Callback<RRSubmitRP>() {
                @Override
                public void onResponse(Call<RRSubmitRP> call, Response<RRSubmitRP> response) {

                    try {
                        RRSubmitRP rrSubmitRP = response.body();
                        assert rrSubmitRP != null;

                        if (rrSubmitRP.getStatus().equals("1")) {

                            if (rrSubmitRP.getSuccess().equals("1")) {

                                Events.RatingUpdate ratingUpdate = new Events.RatingUpdate(getRating, review_msg);
                                GlobalBus.getBus().post(ratingUpdate);

                                dialog1.dismiss();

                            }

                            Toast.makeText(MyOrderDetailsActivity.this, rrSubmitRP.getMsg(), Toast.LENGTH_SHORT).show();

                        } else {
                            dialog1.dismiss();
                            method.alertBox(rrSubmitRP.getMessage());
                        }

                    } catch (Exception e) {
                        Log.d(ConstantApi.exceptionError, e.toString());
                        dialog1.dismiss();
                        method.alertBox(getResources().getString(R.string.failed_try_again));
                    }

                    progressDialog.dismiss();

                }

                @Override
                public void onFailure(Call<RRSubmitRP> call, Throwable t) {
                    Log.e(ConstantApi.failApi, t.toString());
                    dialog1.dismiss();
                    progressDialog.dismiss();
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }
            });
        }
    }

    public void downloadInvoice(String id, String file_path) {

        progressDialog.show();
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        try {

            String file_name = id + ".pdf";
            String storage_path = Environment.getExternalStorageDirectory() + File.separator + "Download" + File.separator + getResources().getString(R.string.app_name) + " Invoice" + File.separator;
            File file = new File(storage_path);
            if (!file.exists()) {
                file.mkdirs();
            }

            final String CANCEL_TAG = "c_dig" + id;

            okhttp3.OkHttpClient client = new okhttp3.OkHttpClient();
            okhttp3.Request.Builder builder = new okhttp3.Request.Builder()
                    .url(file_path)
                    .addHeader("Accept-Encoding", "identity")
                    .get()
                    .tag(CANCEL_TAG);

            okhttp3.Call call = client.newCall(builder.build());
            call.enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                    Log.e("TAG", "=============onFailure===============");
                    e.printStackTrace();
                    Log.d("error_downloading", e.toString());
                    progressDialog.dismiss();
                }

                @Override
                public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
                    Log.e("TAG", "=============onResponse===============");
                    Log.e("TAG", "request headers:" + response.request().headers());
                    Log.e("TAG", "response headers:" + response.headers());
                    assert response.body() != null;
                    final okhttp3.ResponseBody responseBody = ProgressHelper.withProgress(response.body(), new ProgressUIListener() {

                        //if you don't need this method, don't override this method. It isn't an abstract method, just an empty method.
                        @Override
                        public void onUIProgressStart(long totalBytes) {
                            super.onUIProgressStart(totalBytes);
                            Log.e("TAG", "onUIProgressStart:" + totalBytes);
                        }

                        @Override
                        public void onUIProgressChanged(long numBytes, long totalBytes, float percent, float speed) {
                            Log.e("TAG", "=============start===============");
                            Log.e("TAG", "numBytes:" + numBytes);
                            Log.e("TAG", "totalBytes:" + totalBytes);
                            Log.e("TAG", "percent:" + percent);
                            Log.e("TAG", "speed:" + speed);
                            Log.e("TAG", "============= end ===============");
                        }

                        //if you don't need this method, don't override this method. It isn't an abstract method, just an empty method.
                        @Override
                        public void onUIProgressFinish() {
                            super.onUIProgressFinish();
                            Log.e("TAG", "onUIProgressFinish:");
                            method.alertBox(getResources().getString(R.string.download_invoice_msg));
                            progressDialog.dismiss();

                            try {
                                MediaScannerConnection.scanFile(getApplicationContext(), new String[]{storage_path + File.separator + file_name},
                                        null,
                                        (path, uri) -> {

                                        });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });


                    try {

                        BufferedSource source = responseBody.source();
                        File outFile = new File(storage_path + File.separator + file_name);
                        BufferedSink sink = Okio.buffer(Okio.sink(outFile));
                        source.readAll(sink);
                        sink.flush();
                        source.close();

                    } catch (Exception e) {
                        Log.d("show_data", e.toString());
                        progressDialog.dismiss();
                        method.alertBox("Failed Try again");
                    }

                }
            });
        } catch (Exception e) {
            progressDialog.dismiss();
            method.alertBox("Failed Try again");
        }
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
    protected void onDestroy() {
        super.onDestroy();
        GlobalBus.getBus().unregister(this);
    }

}