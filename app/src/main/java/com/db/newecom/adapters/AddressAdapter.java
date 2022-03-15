package com.db.newecom.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.db.newecom.Api.ApiClient;
import com.db.newecom.Api.ApiInterface;
import com.db.newecom.Model.Address;
import com.db.newecom.R;
import com.db.newecom.Response.AddEditRemoveAddressRP;
import com.db.newecom.Utills.API;
import com.db.newecom.Utills.Events;
import com.db.newecom.Utills.GlobalBus;
import com.db.newecom.Utills.Method;
import com.db.newecom.ui.activity.Add_Address_Activity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    private Method method;
    private Activity activity;
    private List<Address> addresses;

    public AddressAdapter(Activity activity, List<Address> addresses) {
        this.activity = activity;
        this.addresses = addresses;
        method = new Method(activity);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.item_address, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.address_user_name.setText(addresses.get(position).getName());
        holder.address_type.setText(addresses.get(position).getAddress_type());
        holder.address.setText(addresses.get(position).getAddress());
        holder.mobile.setText(addresses.get(position).getMobile_no());

        holder.address_edit_btn.setOnClickListener(v ->
                activity.startActivity(new Intent(activity, Add_Address_Activity.class)
                .putExtra("isEdit", true)
                .putExtra("address_id", addresses.get(position).getId())
                .putExtra("type", "normal")));

        holder.address_remove_btn.setOnClickListener(v -> {
            if (method.isLogin()) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity, R.style.DialogTitleTextStyle);
                builder.setMessage(activity.getResources().getString(R.string.delete_address_msg));
                builder.setCancelable(false);
                builder.setPositiveButton(activity.getResources().getString(R.string.yes),
                        (arg0, arg1) -> deleteAddress(addresses.get(position).getId(), method.userId(), position));
                builder.setNegativeButton(activity.getResources().getString(R.string.no),
                        (dialog, which) -> dialog.dismiss());

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return addresses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView address_user_name, address_type, mobile, address;
        private Button address_edit_btn, address_remove_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            address_user_name = itemView.findViewById(R.id.address_user_name);
            address_type = itemView.findViewById(R.id.address_type);
            mobile = itemView.findViewById(R.id.mobile);
            address = itemView.findViewById(R.id.address);
            address_edit_btn = itemView.findViewById(R.id.address_edit_btn);
            address_remove_btn = itemView.findViewById(R.id.address_remove_btn);

        }
    }

    private void deleteAddress(String id, String userId, int position) {

        ProgressDialog progressDialog = new ProgressDialog(activity, R.style.ProgressDialogStyle);
        progressDialog.show();
        progressDialog.setMessage(activity.getResources().getString(R.string.processing));
        progressDialog.setCancelable(false);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(activity));
        jsObj.addProperty("id", id);
        jsObj.addProperty("user_id", userId);
        ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<AddEditRemoveAddressRP> call = apiService.deleteAddress(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<AddEditRemoveAddressRP>() {
            @Override
            public void onResponse(@NotNull Call<AddEditRemoveAddressRP> call, @NotNull Response<AddEditRemoveAddressRP> response) {

                try {

                    AddEditRemoveAddressRP addEditRemoveAddressRP = response.body();

                    assert addEditRemoveAddressRP != null;

                    if (addEditRemoveAddressRP.getStatus().equals("1")){
                        if (addEditRemoveAddressRP.getSuccess().equals("1")) {
                            Toast.makeText(activity, addEditRemoveAddressRP.getMsg(), Toast.LENGTH_SHORT).show();
                            Events.EventMYAddress eventMYAddress =
                                    new Events.EventMYAddress(addEditRemoveAddressRP.getAddress(),
                                            addEditRemoveAddressRP.getAddress_count(),"removeAddress");
                            GlobalBus.getBus().post(eventMYAddress);
                            addresses.remove(position);
                            notifyDataSetChanged();
                        } else {
                            method.alertBox(addEditRemoveAddressRP.getMsg());
                        }
                    } else {
                        method.alertBox(addEditRemoveAddressRP.getMessage());
                    }

                } catch (Exception e) {
                    Log.d("exception_error", e.toString());
                    method.alertBox(activity.getResources().getString(R.string.failed_try_again));
                }

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(@NotNull Call<AddEditRemoveAddressRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e("fail_api", t.toString());
                progressDialog.dismiss();
                method.alertBox(activity.getResources().getString(R.string.failed_try_again));
            }

        });

    }

}
