package com.db.newecom.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.db.newecom.Api.ApiClient;
import com.db.newecom.Api.ApiInterface;
import com.db.newecom.R;
import com.db.newecom.Response.AddEditRemoveBankRP;
import com.db.newecom.Response.GetBankDetailRP;
import com.db.newecom.Utills.API;
import com.db.newecom.Utills.Events;
import com.db.newecom.Utills.GlobalBus;
import com.db.newecom.Utills.Method;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Add_Bank_Activity extends AppCompatActivity {

    private Method method;
    private ProgressDialog progressDialog;
    private boolean isEdit;

    private EditText et_bankName_add_bank, et_ifsc_add_bank, et_accountNo_add_bank, et_bankHolderName_add_bank,
            et_bankHolderMobile_add_bank, et_bankHolderEmail_add_bank;
    private Spinner spinner;
    private MaterialCheckBox checkBox;
    private Button save_btn;
    private String accountType = "", type = "add", bankId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bank);

        isEdit = getIntent().getBooleanExtra("isEdit", false);
        bankId = getIntent().getStringExtra("bank_id");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (isEdit){
                getSupportActionBar().setTitle(R.string.edit_bank_account);
                type = "edit";
            }
        }

        method = new Method(this);
        progressDialog = new ProgressDialog(this, R.style.ProgressDialogStyle);

        et_bankName_add_bank = findViewById(R.id.et_bankName_add_bank);
        et_ifsc_add_bank = findViewById(R.id.et_ifsc_add_bank);
        et_accountNo_add_bank = findViewById(R.id.et_accountNo_add_bank);
        et_bankHolderName_add_bank = findViewById(R.id.et_bankHolderName_add_bank);
        et_bankHolderMobile_add_bank = findViewById(R.id.et_bankHolderMobile_add_bank);
        et_bankHolderEmail_add_bank = findViewById(R.id.et_bankHolderEmail_add_bank);
        spinner = findViewById(R.id.spinner_add_bank);
        checkBox = findViewById(R.id.checkBox_add_bank);
        save_btn = findViewById(R.id.save_btn);

        List<String> strings = new ArrayList<>();
        strings.add(getResources().getString(R.string.select_bank_type));
        strings.add(getResources().getString(R.string.saving));
        strings.add(getResources().getString(R.string.current));

        // Creating adapter for spinner_cat
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, strings);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner_cat
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources()
                            .getColor(R.color.colorPrimary));
                if (position == 0) {
                    accountType = "select";
                } else if (position == 1) {
                    accountType = "saving";
                } else if (position == 2){
                    accountType = "current";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("data_app", parent.toString());
            }
        });

        save_btn.setOnClickListener(view -> {
            if (method.isNetworkAvailable(this))
                checkValidations();
            else
                Toast.makeText(this, getResources().getString(R.string.no_internet_connection),
                        Toast.LENGTH_SHORT).show();
        });

        if (isEdit) {
            if (method.isNetworkAvailable(this))
                getBankDetail(method.userId(), bankId);
            else
                Toast.makeText(this, getResources().getString(R.string.no_internet_connection),
                        Toast.LENGTH_SHORT).show();
        }
    }

    private void getBankDetail(String userId, String bankId) {

        progressDialog.show();
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(this));
        jsObj.addProperty("user_id", userId);
        jsObj.addProperty("bank_id", bankId);
        ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<GetBankDetailRP> call = apiService.getBankDetail(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<GetBankDetailRP>() {
            @Override
            public void onResponse(@NotNull Call<GetBankDetailRP> call, @NotNull Response<GetBankDetailRP> response) {

                try {
                    GetBankDetailRP getBankDetailRP = response.body();
                    assert getBankDetailRP != null;

                    if (getBankDetailRP.getStatus().equals("1")) {

                        et_bankName_add_bank.setText(getBankDetailRP.getBank_name());
                        et_accountNo_add_bank.setText(getBankDetailRP.getAccount_no());
                        et_ifsc_add_bank.setText(getBankDetailRP.getBank_ifsc());
                        et_bankHolderName_add_bank.setText(getBankDetailRP.getBank_holder_name());
                        et_bankHolderMobile_add_bank.setText(getBankDetailRP.getBank_holder_phone());
                        et_bankHolderEmail_add_bank.setText(getBankDetailRP.getBank_holder_email());

                        if (getBankDetailRP.getAccount_type().equals("saving")) {
                            spinner.setSelection(1);
                        } else {
                            spinner.setSelection(2);
                        }

                        if (getBankDetailRP.getIs_default().equals("true")) {
                            checkBox.setChecked(true);
                        } else {
                            checkBox.setChecked(false);
                        }

                    } else {
                        method.alertBox(getBankDetailRP.getMessage());
                    }

                    progressDialog.dismiss();

                } catch (Exception e) {
                    Log.d("exception_error", e.toString());
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(@NotNull Call<GetBankDetailRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e("fail_api", t.toString());
                progressDialog.dismiss();
                method.alertBox(getResources().getString(R.string.failed_try_again));
            }
        });

    }

    private boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void checkValidations() {

        String bankName = et_bankName_add_bank.getText().toString();
        String accNo = et_accountNo_add_bank.getText().toString();
        String ifsc = et_ifsc_add_bank.getText().toString();
        String name = et_bankHolderName_add_bank.getText().toString();
        String mobile = et_bankHolderMobile_add_bank.getText().toString();
        String email = et_bankHolderEmail_add_bank.getText().toString();

        et_bankName_add_bank.setError(null);
        et_accountNo_add_bank.setError(null);
        et_ifsc_add_bank.setError(null);
        et_bankHolderName_add_bank.setError(null);
        et_bankHolderMobile_add_bank.setError(null);
        et_bankHolderEmail_add_bank.setError(null);

        if (bankName.equals("") || bankName.isEmpty()) {
            et_bankName_add_bank.requestFocus();
            et_bankName_add_bank.setError(getResources().getString(R.string.please_enter_bank_name));
        } else if (ifsc.equals("") || ifsc.isEmpty()) {
            et_ifsc_add_bank.requestFocus();
            et_ifsc_add_bank.setError(getResources().getString(R.string.please_enter_ifsc));
        } else if (accNo.equals("") || accNo.isEmpty()) {
            et_accountNo_add_bank.requestFocus();
            et_accountNo_add_bank.setError(getResources().getString(R.string.please_enter_bank_acc_no));
        } else if (spinner.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please Select Account Type", Toast.LENGTH_SHORT).show();
        } else if (name.equals("") || name.isEmpty()) {
            et_bankHolderName_add_bank.requestFocus();
            et_bankHolderName_add_bank.setError(getResources().getString(R.string.please_enter_name));
        } else if (mobile.equals("") || mobile.isEmpty()) {
            et_bankHolderMobile_add_bank.requestFocus();
            et_bankHolderMobile_add_bank.setError(getResources().getString(R.string.please_enter_phone));
        } else if (email.equals("") || email.isEmpty()) {
            et_bankHolderEmail_add_bank.requestFocus();
            et_bankHolderEmail_add_bank.setError(getResources().getString(R.string.please_enter_email));
        } else if (!isValidMail(email)) {
            et_bankHolderEmail_add_bank.requestFocus();
            et_bankHolderEmail_add_bank.setError(getResources().getString(R.string.please_enter_valid_email));
        } else {

            et_bankName_add_bank.clearFocus();
            et_accountNo_add_bank.clearFocus();
            et_ifsc_add_bank.clearFocus();
            et_bankHolderName_add_bank.clearFocus();
            et_bankHolderMobile_add_bank.clearFocus();
            et_bankHolderEmail_add_bank.clearFocus();

            if (method.isNetworkAvailable(this)) {
                submitBankDetail(bankName, accNo, ifsc, name, mobile, email, type);
            } else {
                method.alertBox(getResources().getString(R.string.no_internet_connection));
            }
        }


    }

    private void submitBankDetail(String bankName, String accNo, String ifsc, String name, String mobile, String email, String typeSend) {

        progressDialog.show();
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(this));
        jsObj.addProperty("user_id", method.userId());
        jsObj.addProperty("bank_name", bankName);
        jsObj.addProperty("account_no", accNo);
        jsObj.addProperty("bank_ifsc", ifsc);
        jsObj.addProperty("account_type", accountType);
        jsObj.addProperty("name", name);
        jsObj.addProperty("phone", mobile);
        jsObj.addProperty("email", email);
        jsObj.addProperty("is_default", checkBox.isChecked());
        jsObj.addProperty("type", typeSend);
        if (typeSend.equals("edit")) {
            jsObj.addProperty("bank_id", bankId);
        }
        ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<AddEditRemoveBankRP> call = apiService.submitBankDetail(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<AddEditRemoveBankRP>() {
            @Override
            public void onResponse(@NotNull Call<AddEditRemoveBankRP> call, @NotNull Response<AddEditRemoveBankRP> response) {

                try {
                    AddEditRemoveBankRP addEditRemoveBankRP = response.body();
                    assert addEditRemoveBankRP != null;

                    if (addEditRemoveBankRP.getStatus().equals("1")) {

                        if (addEditRemoveBankRP.getSuccess().equals("1")) {
                            Toast.makeText(Add_Bank_Activity.this, addEditRemoveBankRP.getMsg(), Toast.LENGTH_SHORT).show();
                            onBackPressed();

                            Events.EventBankDetail eventBankDetail = new Events.EventBankDetail(addEditRemoveBankRP.getBank_count(), addEditRemoveBankRP.getBank_details(),"addEditBank");
                            GlobalBus.getBus().post(eventBankDetail);

                        }

                    } else {
                        method.alertBox(addEditRemoveBankRP.getMessage());
                    }

                } catch (Exception e) {
                    Log.d("exception_error", e.toString());
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(@NotNull Call<AddEditRemoveBankRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e("fail_api", t.toString());
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
}