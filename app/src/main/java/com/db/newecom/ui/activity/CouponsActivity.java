package com.db.newecom.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.db.newecom.Api.ApiClient;
import com.db.newecom.Api.ApiInterface;
import com.db.newecom.Model.CouponsList;
import com.db.newecom.R;
import com.db.newecom.Response.CouponsRP;
import com.db.newecom.Utills.API;
import com.db.newecom.Utills.ConstantApi;
import com.db.newecom.Utills.Events;
import com.db.newecom.Utills.GlobalBus;
import com.db.newecom.Utills.Method;
import com.db.newecom.adapters.CouponAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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

public class CouponsActivity extends AppCompatActivity {

    private Method method;
    private ProgressBar progressBar;
    private List<CouponsList> couponsLists;
    private RelativeLayout empty_layout;
    private TextView empty_msg;
    private RecyclerView rv_coupon;
    private CouponAdapter couponAdapter;
    private String cartIds, type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupons);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        method = new Method(this);

        GlobalBus.getBus().register(this);

        couponsLists = new ArrayList<>();

        cartIds = getIntent().getStringExtra("cart_ids");
        type = getIntent().getStringExtra("type");

        progressBar = findViewById(R.id.progressBar_coupon);
        empty_layout = findViewById(R.id.empty_layout);
        empty_msg = findViewById(R.id.empty_msg);
        rv_coupon = findViewById(R.id.rv_coupons);

        rv_coupon.setVisibility(View.GONE);

        if (method.isNetworkAvailable(this)) {
            if (method.isLogin())
                coupons(method.userId());
            else {
                progressBar.setVisibility(View.GONE);
                empty_layout.setVisibility(View.VISIBLE);
                method.alertBox(getResources().getString(R.string.login_msg));
            }
        } else {
            progressBar.setVisibility(View.GONE);
            empty_layout.setVisibility(View.VISIBLE);
            method.alertBox(getResources().getString(R.string.no_internet_connection));
        }
    }

    private void coupons(String user_id) {

        couponsLists.clear();
        progressBar.setVisibility(View.VISIBLE);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(this));
        jsObj.addProperty("user_id", user_id);
        jsObj.addProperty("cart_ids", cartIds);
        jsObj.addProperty("cart_type", "main_cart");
        ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<CouponsRP> call = apiService.getCoupons(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<CouponsRP>() {
            @Override
            public void onResponse(@NotNull Call<CouponsRP> call, @NotNull Response<CouponsRP> response) {

                try {

                    CouponsRP couponsRP = response.body();

                    assert couponsRP != null;
                    if (couponsRP.getStatus().equals("1")) {

                        if (couponsRP.getCouponsLists().size() == 0) {
                            empty_layout.setVisibility(View.VISIBLE);
                            empty_msg.setText(getResources().getString(R.string.no_items));
                        } else {
                            rv_coupon.setVisibility(View.VISIBLE);
                            couponsLists.addAll(couponsRP.getCouponsLists());
                        }

                        if (couponsLists.size() != 0) {
                            couponAdapter = new CouponAdapter(CouponsActivity.this, couponsLists, cartIds);
                            rv_coupon.setAdapter(couponAdapter);
                        } else {
                            empty_layout.setVisibility(View.VISIBLE);
                            empty_msg.setText(getResources().getString(R.string.no_items));
                        }

                    } else {
                        empty_layout.setVisibility(View.VISIBLE);
                        method.alertBox(couponsRP.getMessage());
                    }

                } catch (Exception e) {
                    Log.d(ConstantApi.exceptionError, e.toString());
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }

                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(@NotNull Call<CouponsRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e(ConstantApi.failApi, t.toString());
                progressBar.setVisibility(View.GONE);
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

    @Subscribe
    public void getData(Events.OnBackData onBackData) {
        onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
        GlobalBus.getBus().unregister(this);
        super.onDestroy();
    }

}