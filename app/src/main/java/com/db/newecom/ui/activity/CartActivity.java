package com.db.newecom.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.db.newecom.Api.ApiClient;
import com.db.newecom.Api.ApiInterface;
import com.db.newecom.Model.CartList;
import com.db.newecom.R;
import com.db.newecom.Response.AppRP;
import com.db.newecom.Response.CartRP;
import com.db.newecom.Utills.API;
import com.db.newecom.Utills.ConstantApi;
import com.db.newecom.Utills.Events;
import com.db.newecom.Utills.GlobalBus;
import com.db.newecom.Utills.Method;
import com.db.newecom.adapters.CartAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {

    private Method method;
    private ProgressBar progressBar;
    private RelativeLayout empty_layout, rl_cart_main;
    private ImageView empty_image;
    private TextView empty_msg, total_amount, delivery_charge, amount_payable,
            total_amount_payable, total_cart_items;
    private Button empty_btn, confirm_btn;
    private RecyclerView rv_cart;
    private CartAdapter cartAdapter;
    private List<CartList> cartLists;
    private String sign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        GlobalBus.getBus().register(this);
        method = new Method(this);

        cartLists = new ArrayList<>();
        sign = ConstantApi.currency + " ";

        progressBar = findViewById(R.id.progressBar_cart);
        empty_layout = findViewById(R.id.empty_layout);
        rl_cart_main = findViewById(R.id.rl_cart_main);
        empty_image = findViewById(R.id.empty_image);
        empty_msg = findViewById(R.id.empty_msg);
        empty_btn = findViewById(R.id.empty_btn);
        total_amount = findViewById(R.id.total_amount);
        delivery_charge = findViewById(R.id.delivery_charge);
        amount_payable = findViewById(R.id.amount_payable);
        total_amount_payable = findViewById(R.id.total_amount_payable);
        total_cart_items = findViewById(R.id.total_cart_items);
        confirm_btn = findViewById(R.id.confirm_btn);
        rv_cart = findViewById(R.id.rv_cart);

        if (method.isNetworkAvailable(this)) {
            if (method.isLogin()) {
                cart(method.userId());
                appDetail(method.userId());
            } else {
                empty_layout.setVisibility(View.VISIBLE);
                rl_cart_main.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                empty_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_login_24));
                empty_msg.setText(getResources().getString(R.string.login_msg));
                empty_btn.setVisibility(View.VISIBLE);
                empty_btn.setText(getResources().getString(R.string.go_to_login));
                empty_btn.setOnClickListener(view ->
                        startActivity(new Intent(this, LoginActivity.class)));
            }
        } else {
            empty_layout.setVisibility(View.VISIBLE);
            rl_cart_main.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            method.alertBox(getResources().getString(R.string.no_internet_connection));
        }

    }

    @SuppressLint("SetTextI18n")
    @Subscribe
    public void geString(Events.UpdateCart updateCart) {

        if (cartLists.size() == 0) {
            rl_cart_main.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            empty_layout.setVisibility(View.VISIBLE);
            empty_image.setImageResource(R.drawable.ic_outline_add_shopping_cart_24);
            empty_msg.setText(R.string.empty_cart);
            empty_btn.setVisibility(View.VISIBLE);
            empty_btn.setOnClickListener(view -> {
                Intent intent = new Intent(CartActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                navigateUpTo(intent);
            });
        }

        if (cartAdapter != null) {

            String totalitems = updateCart.getTotal_item() + " Products";

            total_cart_items.setText(totalitems);
            total_amount.setText(sign + updateCart.getPrice());
            amount_payable.setText(sign + updateCart.getPayable_amt());
            if (method.isNumeric(updateCart.getDelivery_charge())) {
                delivery_charge.setText("+ " + sign + updateCart.getDelivery_charge());
            } else {
                delivery_charge.setText(updateCart.getDelivery_charge());
                delivery_charge.setTextColor(getResources().getColor(R.color.green));
            }
            total_amount_payable.setText(sign + updateCart.getPayable_amt());

            cart(method.userId());

        }
    }

    private void appDetail(String userId) {


        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(CartActivity.this));
        jsObj.addProperty("user_id", userId);
        ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<AppRP> call = apiService.getAppData(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<AppRP>() {
            @Override
            public void onResponse(Call<AppRP> call, Response<AppRP> response) {

                try {
                    AppRP appRP = response.body();
                    assert appRP != null;

                    if (appRP.getStatus().equals("1")) {

                        ConstantApi.currency = appRP.getApp_currency_code();


                    } else {
                        method.alertBox(appRP.getMessage());
                    }
                }
                catch (Exception e){
                    Log.d(ConstantApi.exceptionError, e.toString());
                    Toast.makeText(CartActivity.this, getResources().getString(R.string.something_wrong),
                            Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<AppRP> call, Throwable t) {
                Log.e(ConstantApi.failApi, t.toString());
                method.alertBox(getResources().getString(R.string.failed_try_again));

            }
        });

    }

    private void cart(String userId) {

        cartLists.clear();
        progressBar.setVisibility(View.VISIBLE);
        rl_cart_main.setVisibility(View.GONE);
        confirm_btn.setEnabled(true);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(this));
        jsObj.addProperty("user_id", userId);
        ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<CartRP> call = apiService.getCartList(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<CartRP>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NotNull Call<CartRP> call, @NotNull Response<CartRP> response) {

                try {

                    CartRP cartRP = response.body();

                    assert cartRP != null;
                    if (cartRP.getStatus().equals("1")) {

                        if (cartRP.getSuccess().equals("1")) {

                            cartLists.addAll(cartRP.getCartLists());

                            String totalitems = getResources().getString(R.string.products) + " " + cartRP.getTotal_item();

                            total_cart_items.setText(totalitems);
                            total_amount_payable.setText(sign + cartRP.getPayable_amt());
                            amount_payable.setText(sign + cartRP.getPayable_amt());
                            total_amount.setText(sign + cartRP.getPrice());

                            for (int i = 0;i < cartRP.getCartLists().size();i++){
                                if (cartRP.getCartLists().get(i).getProduct_status().equals("0") ||
                                        cartRP.getCartLists().get(i).getProduct_qty().equals("0")){
                                    confirm_btn.setEnabled(false);
                                    break;
                                }
                            }

                            if (method.isNumeric(cartRP.getDelivery_charge())) {
                                delivery_charge.setText("+ " + sign + cartRP.getDelivery_charge());
                            } else {
                                delivery_charge.setTextColor(getResources().getColor(R.color.green));
                                delivery_charge.setText(cartRP.getDelivery_charge());
                            }

//                            if (cartRP.getYou_save_msg().equals("")) {
//                                textViewSave.setVisibility(View.GONE);
//                                viewSave.setVisibility(View.GONE);
//                            } else {
//                                textViewSave.setVisibility(View.VISIBLE);
//                                viewSave.setVisibility(View.VISIBLE);
//                                textViewSave.setText(cartRP.getYou_save_msg());
//                            }

                            if (cartLists.size() != 0) {
                                rl_cart_main.setVisibility(View.VISIBLE);

                                cartAdapter = new CartAdapter(CartActivity.this, true, cartLists);
                                rv_cart.setAdapter(cartAdapter);

                            } else {
                                progressBar.setVisibility(View.GONE);
                                empty_layout.setVisibility(View.VISIBLE);
                                empty_image.setImageResource(R.drawable.ic_outline_add_shopping_cart_24);
                                empty_msg.setText(R.string.empty_cart);
                                empty_btn.setVisibility(View.VISIBLE);
                                empty_btn.setOnClickListener(view -> {
                                    Intent intent = new Intent(CartActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    navigateUpTo(intent);
                                });
                            }

                            Events.CartItem cartItem = new Events.CartItem(cartRP.getTotal_item());
                            GlobalBus.getBus().post(cartItem);

                            confirm_btn.setOnClickListener(v -> {
                                if (cartRP.getAddress_count().equals("0")) {
                                    //add to address then after go to the order summary page
                                    startActivity(new Intent(CartActivity.this, Add_Address_Activity.class)
                                            .putExtra("type", "check_address")
                                            .putExtra("type_order", "cart")
                                            .putExtra("product_id", "")
                                            .putExtra("product_size", ""));
                                } else {
                                    startActivity(new Intent(CartActivity.this, OrderSummaryActivity.class)
                                            .putExtra("type", "cart")
                                            .putExtra("product_id", "")
                                            .putExtra("product_size", ""));
                                }
                            });

                        } else {
                            progressBar.setVisibility(View.GONE);
                            empty_layout.setVisibility(View.VISIBLE);
                            empty_image.setImageResource(R.drawable.ic_outline_add_shopping_cart_24);
                            empty_msg.setText(cartRP.getMsg());
                            empty_btn.setVisibility(View.VISIBLE);
                            empty_btn.setOnClickListener(view -> {
                                Intent intent = new Intent(CartActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                navigateUpTo(intent);
                            });
                        }

                    } else {
                        empty_layout.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        method.alertBox(cartRP.getMessage());
                    }

                } catch (Exception e) {
                    Log.d(ConstantApi.exceptionError, e.toString());
                    empty_layout.setVisibility(View.VISIBLE);
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }

                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(@NotNull Call<CartRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e(ConstantApi.failApi, t.toString());
                empty_layout.setVisibility(View.VISIBLE);
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

    @Override
    protected void onDestroy() {
        GlobalBus.getBus().unregister(this);
        super.onDestroy();
    }
}