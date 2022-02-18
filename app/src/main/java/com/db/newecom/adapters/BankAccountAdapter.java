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
import com.db.newecom.Model.Bank;
import com.db.newecom.R;
import com.db.newecom.Response.AddEditRemoveBankRP;
import com.db.newecom.Utills.API;
import com.db.newecom.Utills.Events;
import com.db.newecom.Utills.GlobalBus;
import com.db.newecom.Utills.Method;
import com.db.newecom.ui.activity.Add_Bank_Activity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BankAccountAdapter extends RecyclerView.Adapter<BankAccountAdapter.ViewHolder> {

    private Activity activity;
    private Method method;
    private List<Bank> bankDetailLists;

    public BankAccountAdapter(Activity activity, List<Bank> bankDetailLists) {
        this.activity = activity;
        this.bankDetailLists = bankDetailLists;
        method = new Method(activity);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.item_bank_account, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.bank_acc_edit_btn.setOnClickListener(view ->
                activity.startActivity(new Intent(activity, Add_Bank_Activity.class)
                .putExtra("isEdit", true)));

        holder.bank_name.setText(bankDetailLists.get(position).getBank_name());
        holder.bank_account_no.setText(bankDetailLists.get(position).getAccount_no());
        holder.bank_holder_name.setText(bankDetailLists.get(position).getBank_holder_name());
        holder.ifsc.setText(bankDetailLists.get(position).getBank_ifsc());
        holder.bank_holder_mobile.setText(bankDetailLists.get(position).getBank_holder_phone());
        holder.bank_account_type.setText(bankDetailLists.get(position).getAccount_type());
        holder.bank_holder_email.setText(bankDetailLists.get(position).getBank_holder_email());

        holder.bank_acc_edit_btn.setOnClickListener(v ->
            activity.startActivity(new Intent(activity, Add_Bank_Activity.class)
                    .putExtra("isEdit", true)
                    .putExtra("bank_id", bankDetailLists.get(position).getId())));

        holder.bank_acc_remove_btn.setOnClickListener(v -> {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity, R.style.DialogTitleTextStyle);
            builder.setMessage(activity.getResources().getString(R.string.delete_bank_msg));
            builder.setCancelable(false);
            builder.setPositiveButton(activity.getResources().getString(R.string.yes),
                    (arg0, arg1) -> deleteBankDetail(bankDetailLists.get(position).getId(), method.userId(), position));
            builder.setNegativeButton(activity.getResources().getString(R.string.no),
                    (dialog, which) -> dialog.dismiss());

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return bankDetailLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView bank_name, ifsc, bank_account_no, bank_account_type, bank_holder_name,
                bank_holder_mobile, bank_holder_email;
        private Button bank_acc_edit_btn, bank_acc_remove_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            bank_name = itemView.findViewById(R.id.bank_name);
            ifsc = itemView.findViewById(R.id.ifsc);
            bank_account_no = itemView.findViewById(R.id.bank_account_no);
            bank_account_type = itemView.findViewById(R.id.bank_account_type);
            bank_holder_name = itemView.findViewById(R.id.bank_holder_name);
            bank_holder_mobile = itemView.findViewById(R.id.bank_holder_mobile);
            bank_holder_email = itemView.findViewById(R.id.bank_holder_email);
            bank_acc_edit_btn = itemView.findViewById(R.id.bank_acc_edit_btn);
            bank_acc_remove_btn = itemView.findViewById(R.id.bank_acc_remove_btn);


        }
    }

    private void deleteBankDetail(String bankId, String userId, int position) {

        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.show();
        progressDialog.setMessage(activity.getResources().getString(R.string.processing));
        progressDialog.setCancelable(false);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(activity));
        jsObj.addProperty("bank_id", bankId);
        jsObj.addProperty("user_id", userId);
        ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<AddEditRemoveBankRP> call = apiService.deleteBank(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<AddEditRemoveBankRP>() {
            @Override
            public void onResponse(@NotNull Call<AddEditRemoveBankRP> call, @NotNull Response<AddEditRemoveBankRP> response) {

                try {

                    AddEditRemoveBankRP addEditRemoveBankRP = response.body();

                    assert addEditRemoveBankRP != null;
                    if (addEditRemoveBankRP.getStatus().equals("1")) {
                        if (addEditRemoveBankRP.getSuccess().equals("1")) {
                            Toast.makeText(activity, addEditRemoveBankRP.getMsg(), Toast.LENGTH_SHORT).show();
                            Events.EventBankDetail eventBankDetail = new Events.EventBankDetail(addEditRemoveBankRP.getBank_count(), addEditRemoveBankRP.getBank_details(), "removeBank");
                            GlobalBus.getBus().post(eventBankDetail);
                            bankDetailLists.remove(position);
                            notifyDataSetChanged();
                        } else {
                            method.alertBox(addEditRemoveBankRP.getMsg());
                        }

                    } else {
                        method.alertBox(addEditRemoveBankRP.getMessage());
                    }

                } catch (Exception e) {
                    Log.d("exception_error", e.toString());
                    method.alertBox(activity.getResources().getString(R.string.failed_try_again));
                }

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(@NotNull Call<AddEditRemoveBankRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e("fail_api", t.toString());
                progressDialog.dismiss();
                method.alertBox(activity.getResources().getString(R.string.failed_try_again));
            }

        });

    }
}
