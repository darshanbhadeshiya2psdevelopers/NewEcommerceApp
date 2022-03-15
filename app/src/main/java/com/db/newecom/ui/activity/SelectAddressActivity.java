package com.db.newecom.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.db.newecom.Api.ApiClient;
import com.db.newecom.Api.ApiInterface;
import com.db.newecom.Model.Address;
import com.db.newecom.R;
import com.db.newecom.Response.AddressListRP;
import com.db.newecom.Response.ChangeAddressRP;
import com.db.newecom.Utills.API;
import com.db.newecom.Utills.ConstantApi;
import com.db.newecom.Utills.Events;
import com.db.newecom.Utills.GlobalBus;
import com.db.newecom.Utills.Method;
import com.db.newecom.adapters.SelectAddressAdapter;
import com.db.newecom.interfaces.OnClickSend;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
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

public class SelectAddressActivity extends AppCompatActivity {

    private Method method;
    private OnClickSend onClickSend;
    private String addressId;
    private LinearLayout progressBar;
    private ProgressDialog progressDialog;
    private RelativeLayout empty_layout;
    private RecyclerView rv_select_address;
    private SelectAddressAdapter selectAddressAdapter;
    private CardView add_address_btn;
    private TextView deliver_here_btn, empty_msg;
    private List<Address> addressItemLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        GlobalBus.getBus().register(this);

        method = new Method(this);

        progressDialog = new ProgressDialog(this, R.style.ProgressDialogStyle);

        onClickSend = (id, type, size, position) -> addressId = id;

        addressItemLists = new ArrayList<>();

        progressBar = findViewById(R.id.ll_progress_select_add);
        empty_layout = findViewById(R.id.empty_layout);
        empty_msg = findViewById(R.id.empty_msg);
        rv_select_address = findViewById(R.id.rv_select_address);
        add_address_btn = findViewById(R.id.add_address_btn);
        deliver_here_btn = findViewById(R.id.deliver_here_btn);

        rv_select_address.setVisibility(View.GONE);

        add_address_btn.setOnClickListener(view ->
                startActivity(new Intent(this,Add_Address_Activity.class)
                        .putExtra("type", "add_change_address")
                        .putExtra("isEdit", false)
                        .putExtra("address_id", "")));

        deliver_here_btn.setOnClickListener(v -> {
            if (!addressId.equals("")) {
                changeAddress(addressId, method.userId());
            } else {
                progressBar.setVisibility(View.GONE);
                empty_layout.setVisibility(View.VISIBLE);
                empty_msg.setText(getResources().getString(R.string.no_address_found));
                //method.alertBox(getResources().getString(R.string.no_address_found));
            }
        });

        if (method.isNetworkAvailable(this)) {
            if (method.isLogin()) {
                addressList(method.userId());
            } else {
                method.alertBox(getResources().getString(R.string.login_msg));
                empty_layout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        } else {
            progressBar.setVisibility(View.GONE);
            empty_layout.setVisibility(View.VISIBLE);
            method.alertBox(getResources().getString(R.string.no_internet_connection));
        }

    }

    private void addressList(String user_id) {

        addressItemLists.clear();
        progressBar.setVisibility(View.VISIBLE);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(this));
        jsObj.addProperty("user_id", user_id);
        ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<AddressListRP> call = apiService.getAddressList(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<AddressListRP>() {
            @Override
            public void onResponse(@NotNull Call<AddressListRP> call, @NotNull Response<AddressListRP> response) {

                try {

                    AddressListRP addressListRP = response.body();
                    addressItemLists.addAll(addressListRP.getAddressItemLists());

                    assert addressListRP != null;
                    if (addressListRP.getStatus().equals("1")) {

                        rv_select_address.setVisibility(View.VISIBLE);

                        if (addressItemLists.size() != 0) {
                            selectAddressAdapter = new SelectAddressAdapter(SelectAddressActivity.this, onClickSend, addressItemLists);
                            rv_select_address.setAdapter(selectAddressAdapter);
                        } else {
                            empty_layout.setVisibility(View.VISIBLE);
                            empty_msg.setText(getResources().getString(R.string.no_address_found));
                        }

                    } else {
                        empty_layout.setVisibility(View.VISIBLE);
                        method.alertBox(addressListRP.getMessage());
                    }

                } catch (Exception e) {
                    Log.d(ConstantApi.exceptionError, e.toString());
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }

                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(@NotNull Call<AddressListRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e(ConstantApi.failApi, t.toString());
                empty_layout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                method.alertBox(getResources().getString(R.string.failed_try_again));
            }
        });

    }

    private void changeAddress(String address_id, String user_id) {

        progressDialog.show();
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(this));
        jsObj.addProperty("address_id", address_id);
        jsObj.addProperty("user_id", user_id);
        ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<ChangeAddressRP> call = apiService.getChangeAddress(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<ChangeAddressRP>() {
            @Override
            public void onResponse(@NotNull Call<ChangeAddressRP> call, @NotNull Response<ChangeAddressRP> response) {

                try {

                    ChangeAddressRP changeAddressRP = response.body();

                    assert changeAddressRP != null;
                    if (changeAddressRP.getStatus().equals("1")) {

                        if (changeAddressRP.getSuccess().equals("1")) {

                            Events.UpdateDeliveryAddress updateDeliveryAddress = new Events.UpdateDeliveryAddress(address_id, changeAddressRP.getAddress(),
                                    changeAddressRP.getName(), changeAddressRP.getMobile_no(), changeAddressRP.getAddress_type());
                            GlobalBus.getBus().post(updateDeliveryAddress);
                            onBackPressed();
                        }

                        Toast.makeText(SelectAddressActivity.this, changeAddressRP.getMsg(), Toast.LENGTH_SHORT).show();

                    } else {
                        method.alertBox(changeAddressRP.getMessage());
                    }

                } catch (Exception e) {
                    Log.d(ConstantApi.exceptionError, e.toString());
                }

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(@NotNull Call<ChangeAddressRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                progressDialog.dismiss();
                Log.e(ConstantApi.failApi, t.toString());
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