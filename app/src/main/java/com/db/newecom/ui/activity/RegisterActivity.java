package com.db.newecom.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import com.db.newecom.Api.ApiClient;
import com.db.newecom.Api.ApiInterface;
import com.db.newecom.R;
import com.db.newecom.Response.RegisterRP;
import com.db.newecom.Utills.API;
import com.db.newecom.Utills.Method;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText et_name, et_email, et_password, et_cpassword, et_mobile;
    private String name, email, password, conformPassword, mobile;
    private ImageView close_btn;
    private TextView login_txt;
    private Button register_btn;

    private Method method;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        close_btn = findViewById(R.id.close_btn);
        login_txt = findViewById(R.id.login_txt);

        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_cpassword = findViewById(R.id.et_cpassword);
        et_mobile = findViewById(R.id.et_mobile);
        register_btn = findViewById(R.id.register_btn);

        method = new Method(RegisterActivity.this);

        progressDialog = new ProgressDialog(this, R.style.ProgressDialogStyle);

        close_btn.setOnClickListener(view -> onBackPressed());
        login_txt.setOnClickListener(view -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        register_btn.setOnClickListener(view -> {

            name = et_name.getText().toString();
            email = et_email.getText().toString();
            password = et_password.getText().toString();
            conformPassword = et_cpassword.getText().toString();
            mobile = et_mobile.getText().toString();

            checkValidations();
        });

    }

    private boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidMobile(String mobile) {
        if(!Pattern.matches("[a-zA-Z]+", mobile)) {
            return mobile.length() > 6 && mobile.length() <= 13;
        }
        return false;
    }

    private void checkValidations() {

        et_name.setError(null);
        et_email.setError(null);
        et_password.setError(null);
        et_cpassword.setError(null);
        et_mobile.setError(null);

        if (name.equals("")) {
            et_name.requestFocus();
            et_name.setError(getResources().getString(R.string.please_enter_name));
        } else if (email.equals("")) {
            et_email.requestFocus();
            et_email.setError(getResources().getString(R.string.please_enter_email));
        } else if (!isValidMail(email)) {
            et_email.requestFocus();
            et_email.setError(getResources().getString(R.string.please_enter_valid_email));
        } else if (password.equals("")) {
            et_password.requestFocus();
            et_password.setError(getResources().getString(R.string.please_enter_password));
        } else if (conformPassword.equals("")) {
            et_cpassword.requestFocus();
            et_cpassword.setError(getResources().getString(R.string.please_enter_confirm_password));
        } else if (!password.equals(conformPassword)) {
            Toast.makeText(this, "Password and confirm password does not match", Toast.LENGTH_SHORT).show();
        } else if (mobile.equals("")) {
            et_mobile.requestFocus();
            et_mobile.setError(getResources().getString(R.string.please_enter_phone));
        } else if (!isValidMobile(mobile)) {
            et_mobile.requestFocus();
            et_mobile.setError(getResources().getString(R.string.please_enter_valid_phone));
        } else {
            et_name.clearFocus();
            et_email.clearFocus();
            et_password.clearFocus();
            et_cpassword.clearFocus();
            et_mobile.clearFocus();

            if (method.isNetworkAvailable(this))
                Register(name, email, password, mobile);
            else
                Toast.makeText(this, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("HardwareIds")
    private void Register(String send_name, String send_email, String send_password, String send_mobile) {

        progressDialog.show();
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(RegisterActivity.this));
        jsObj.addProperty("name", send_name);
        jsObj.addProperty("email", send_email);
        jsObj.addProperty("password", send_password);
        jsObj.addProperty("phone", send_mobile);
        jsObj.addProperty("device_id", method.getDeviceId());
        jsObj.addProperty("type", "normal");
        jsObj.addProperty("register_platform", "android");
        ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<RegisterRP> call = apiService.getRegister(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<RegisterRP>() {
            @Override
            public void onResponse(@NotNull Call<RegisterRP> call, @NotNull Response<RegisterRP> response) {

                try {

                    RegisterRP registerRP = response.body();

                    assert registerRP != null;

                    if (registerRP.getSuccess().equals("1")) {

                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    }

                    Toast.makeText(RegisterActivity.this, registerRP.getMsg(), Toast.LENGTH_LONG).show();

                } catch (Exception e) {
                    Log.d("exception_error", e.toString());
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(@NotNull Call<RegisterRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e("fail_api", t.toString());
                progressDialog.dismiss();
                method.alertBox(getResources().getString(R.string.failed_try_again));
            }
        });

    }
}