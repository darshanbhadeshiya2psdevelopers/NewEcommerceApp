package com.db.newecom.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.db.newecom.Api.ApiClient;
import com.db.newecom.Api.ApiInterface;
import com.db.newecom.R;
import com.db.newecom.Response.PaymentMethodRP;
import com.db.newecom.Response.PaymentSubmitRP;
import com.db.newecom.Response.RazorPayRP;
import com.db.newecom.Utills.API;
import com.db.newecom.Utills.ConstantApi;
import com.db.newecom.Utills.Method;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {

    private Method method;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    private RelativeLayout rl_layout, empty_layout;
    private RadioGroup radioGroup_payments;
    private RadioButton razorpay, cash_on_delivery;
    private TextView empty_msg, total_amount, total_items, total_amount_payable, amount_payable, total_items_2,
            random_que;
    private Dialog dialog;
    private EditText et_answer;
    private Button continue_btn, dialog_continue_btn;
    private String sign ,total_item, coupon_id, cart_ids, address_id, total,
            Gateway = "", RazorPayID;
    private int answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        total_item = getIntent().getStringExtra("total_items");
        coupon_id = getIntent().getStringExtra("coupon_id");
        cart_ids = getIntent().getStringExtra("cart_ids");
        address_id = getIntent().getStringExtra("address_id");
        total = getIntent().getStringExtra("total");

        sign = ConstantApi.currency + " ";

        method = new Method(this);
        progressDialog = new ProgressDialog(this);

        progressBar = findViewById(R.id.progressBar_payment);
        rl_layout = findViewById(R.id.rl_layout);
        empty_layout = findViewById(R.id.empty_layout);
        radioGroup_payments = findViewById(R.id.radioGroup_payments);
        razorpay = findViewById(R.id.razorpay);
        cash_on_delivery = findViewById(R.id.cash_on_delivery);
        empty_msg = findViewById(R.id.empty_msg);
        total_amount = findViewById(R.id.total_amount);
        total_items = findViewById(R.id.total_items);
        total_amount_payable = findViewById(R.id.total_amount_payable);
        amount_payable = findViewById(R.id.amount_payable);
        total_items_2 = findViewById(R.id.total_items_2);
        continue_btn = findViewById(R.id.confirm_btn);

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.cod_confirm_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        random_que = dialog.findViewById(R.id.random_que);
        et_answer = dialog.findViewById(R.id.et_answer);
        dialog_continue_btn = dialog.findViewById(R.id.dialog_continue_btn);

        total_amount.setText(sign + total);
        total_amount_payable.setText(sign + total);
        amount_payable.setText(sign + total);

        String item;
        if (total_item.equals("1"))
            item = total_item + " Item";
        else
            item = total_item + " Items";

        total_items.setText(item);
        total_items_2.setText(item);

        razorpay = (RadioButton) radioGroup_payments.getChildAt(0);
        cash_on_delivery = (RadioButton) radioGroup_payments.getChildAt(1);

        razorpay.setVisibility(View.GONE);
        cash_on_delivery.setVisibility(View.GONE);
        rl_layout.setVisibility(View.GONE);

        if (method.isNetworkAvailable(this)){
            if (method.isLogin())
                checkpaymentmethods();
            else {
                empty_layout.setVisibility(View.VISIBLE);
                method.alertBox(getResources().getString(R.string.login_msg));
            }
        } else {
            empty_layout.setVisibility(View.VISIBLE);
            method.alertBox(getResources().getString(R.string.no_internet_connection));
        }

        radioGroup_payments.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rb = radioGroup.findViewById(i);

                if (rb.getId() == R.id.razorpay){
                    Gateway = "razorpay";

                } else if (rb.getId() == R.id.cash_on_delivery) {
                    Gateway = "cod";
                }
            }
        });

        continue_btn.setOnClickListener(view -> {
            if (method.isNetworkAvailable(this)){
                if (method.isLogin()) {
                    if (Gateway.equals("cod")) {

                        final int random1 , random2;
                        Random random = new Random();

                        random1 = random.nextInt((25 - 10) + 1) + 10;
                        random2 = random.nextInt((50 - 25) + 1) + 25;

                        answer = random1 + random2;

                        random_que.setText(random1 + " + " + random2 + " = ?");

                        et_answer.setError(null);
                        et_answer.clearFocus();

                        dialog.show();

                        dialog_continue_btn.setOnClickListener(v ->
                                makePayment(coupon_id, address_id, cart_ids, Gateway, "0", "0"));

                    }
                    else if (Gateway.equals("razorpay"))
                        razorPayPayment(cart_ids);
                    else
                        Toast.makeText(this, getResources().getString(R.string.select_payment_method), Toast.LENGTH_SHORT).show();
                }
                else {
                    rl_layout.setVisibility(View.GONE);
                    empty_layout.setVisibility(View.VISIBLE);
                    method.alertBox(getResources().getString(R.string.login_msg));
                }
            } else {
                rl_layout.setVisibility(View.GONE);
                empty_layout.setVisibility(View.VISIBLE);
                method.alertBox(getResources().getString(R.string.no_internet_connection));
            }
        });

    }

    private void checkpaymentmethods() {

        progressBar.setVisibility(View.VISIBLE);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(this));
        ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<PaymentMethodRP> call = apiService.getPaymentMethod(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<PaymentMethodRP>() {
            @Override
            public void onResponse(@NotNull Call<PaymentMethodRP> call, @NotNull Response<PaymentMethodRP> response) {

                try {

                    PaymentMethodRP paymentMethodRP = response.body();
                    assert paymentMethodRP != null;

                    if (paymentMethodRP.getStatus().equals("1")) {

                        if (paymentMethodRP.getSuccess().equals("1")) {

                            if (paymentMethodRP.getCod_status().equals("true")) {
                                cash_on_delivery.setVisibility(View.VISIBLE);
                            }

                            if (paymentMethodRP.getRazorpay_status().equals("true")) {
                                razorpay.setVisibility(View.VISIBLE);
                            }


                            rl_layout.setVisibility(View.VISIBLE);

                        } else {
                            empty_layout.setVisibility(View.VISIBLE);
                            method.alertBox(paymentMethodRP.getMsg());
                        }

                    } else {
                        empty_layout.setVisibility(View.VISIBLE);
                        method.alertBox(paymentMethodRP.getMessage());
                    }

                } catch (Exception e) {
                    Log.d(ConstantApi.exceptionError, e.toString());
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }

                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(@NotNull Call<PaymentMethodRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e(ConstantApi.failApi, t.toString());
                empty_layout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                method.alertBox(getResources().getString(R.string.failed_try_again));
            }
        });
    }

    private void makePayment(String couponId, String addressId, String cartIds, String gateway, String paymentId, String razorpayOrderId) {

        String Ans = et_answer.getText().toString();

        if (Ans.equals(String.valueOf(answer))){

            dialog.dismiss();

            progressDialog.show();
            progressDialog.setMessage(getResources().getString(R.string.processing));
            progressDialog.setCancelable(false);

            JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(PaymentActivity.this));
            jsObj.addProperty("user_id", method.userId());
            jsObj.addProperty("coupon_id", couponId);
            jsObj.addProperty("address_id", addressId);
            jsObj.addProperty("cart_ids", cartIds);
            jsObj.addProperty("gateway", gateway);
            jsObj.addProperty("payment_id", paymentId);
            jsObj.addProperty("razorpay_order_id", razorpayOrderId);
            jsObj.addProperty("cart_type", "main_cart");

            ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
            Call<PaymentSubmitRP> call = apiService.submitPayment(API.toBase64(jsObj.toString()));
            call.enqueue(new Callback<PaymentSubmitRP>() {
                @Override
                public void onResponse(@NotNull Call<PaymentSubmitRP> call, @NotNull Response<PaymentSubmitRP> response) {

                    try {
                        PaymentSubmitRP paymentSubmitRP = response.body();
                        assert paymentSubmitRP != null;
                        if (paymentSubmitRP.getStatus().equals("1")) {
                            if (paymentSubmitRP.getSuccess().equals("1")) {
                                method.conformDialog(paymentSubmitRP);
                            } else if (paymentSubmitRP.getSuccess().equals("2")) {
                                method.alertBox(paymentSubmitRP.getMsg());
                            } else {
                                method.alertBox(paymentSubmitRP.getMsg());
                            }
                        } else {
                            method.alertBox(paymentSubmitRP.getMessage());
                        }
                    } catch (Exception e) {
                        Log.d(ConstantApi.exceptionError, e.toString());
                        method.alertBox(getResources().getString(R.string.failed_try_again));
                    }

                    progressDialog.dismiss();

                }

                @Override
                public void onFailure(@NotNull Call<PaymentSubmitRP> call, @NotNull Throwable t) {
                    // Log error here since request failed
                    Log.e(ConstantApi.failApi, t.toString());
                    progressDialog.dismiss();
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }
            });

        } else if (Ans.equals("")){
            et_answer.requestFocus();
            et_answer.setError("Please Enter Answer");

        } else {
            Toast.makeText(this, getResources().getString(R.string.answer_wrong), Toast.LENGTH_SHORT).show();
            et_answer.setText("");
            et_answer.clearFocus();
            dialog.dismiss();
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

    public void razorPayPayment(String cartIds) {

        progressDialog.show();
        progressDialog.setMessage(getResources().getString(R.string.processing));
        progressDialog.setCancelable(false);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(PaymentActivity.this));
        jsObj.addProperty("user_id", method.userId());
        jsObj.addProperty("cart_ids", cartIds);
        jsObj.addProperty("cart_type", "main_cart");
        ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<RazorPayRP> call = apiService.getRazorPayData(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<RazorPayRP>() {
            @Override
            public void onResponse(@NotNull Call<RazorPayRP> call, @NotNull Response<RazorPayRP> response) {

                try {
                    RazorPayRP razorPayRP = response.body();
                    assert razorPayRP != null;

                    if (razorPayRP.getStatus().equals("1")) {

                        if (razorPayRP.getSuccess().equals("1")) {

                            Checkout checkout = new Checkout();
                            checkout.setKeyID(razorPayRP.getRazorpay_key_id());
                            RazorPayID = razorPayRP.getOrder_id();

                            JSONObject options = new JSONObject();
                            options.put("name", razorPayRP.getName());
                            options.put("description", razorPayRP.getDescription());
                            options.put("image", razorPayRP.getImage());
                            options.put("order_id", razorPayRP.getOrder_id());
                            options.put("theme.color", razorPayRP.getTheme_color());
                            options.put("currency", razorPayRP.getCurrency());
                            options.put("amount", razorPayRP.getAmount());
                            options.put("prefill.email", razorPayRP.getEmail());
                            options.put("prefill.contact", razorPayRP.getContact());
                            checkout.open(PaymentActivity.this, options);

                        } else if (razorPayRP.getSuccess().equals("2")) {
                            method.alertBox(razorPayRP.getMsg());
                        } else {
                            method.alertBox(razorPayRP.getMsg());
                        }

                    } else {
                        method.alertBox(razorPayRP.getMessage());
                    }

                } catch (Exception e) {
                    Log.d(ConstantApi.exceptionError, e.toString());
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(@NotNull Call<RazorPayRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e(ConstantApi.failApi, t.toString());
                progressDialog.dismiss();
                method.alertBox(getResources().getString(R.string.failed_try_again));
            }
        });

    }

    @Override
    public void onPaymentSuccess(String s) {
        makePayment(coupon_id, address_id, cart_ids, Gateway, s, RazorPayID);
    }

    @Override
    public void onPaymentError(int i, String s) {
        method.alertBox(s);
    }
}