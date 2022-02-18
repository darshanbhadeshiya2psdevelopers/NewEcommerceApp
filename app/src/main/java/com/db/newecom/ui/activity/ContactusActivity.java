package com.db.newecom.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.db.newecom.Api.ApiClient;
import com.db.newecom.Api.ApiInterface;
import com.db.newecom.Model.ContactSubject;
import com.db.newecom.R;
import com.db.newecom.Response.ContactRP;
import com.db.newecom.Response.DataRP;
import com.db.newecom.Utills.API;
import com.db.newecom.Utills.Method;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import android.annotation.SuppressLint;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactusActivity extends AppCompatActivity {

    private Method method;
    private EditText et_contact_name, et_contact_email, et_contact_msg;
    private Spinner spinner;
    private Button submit_btn;
    private List<ContactSubject> contactSubjects;
    private ProgressDialog progressDialog;
    private String contactType, contactId, name, email, message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactus);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        method = new Method(this);
        contactSubjects = new ArrayList<>();

        progressDialog = new ProgressDialog(this);

        et_contact_name = findViewById(R.id.et_contact_name);
        et_contact_email = findViewById(R.id.et_contact_email);
        et_contact_msg = findViewById(R.id.et_contact_msg);
        spinner = findViewById(R.id.spinner_contact_us);
        submit_btn = findViewById(R.id.submit_btn);

        if (method.isNetworkAvailable(this)) {
            if (method.isLogin()) {
                getContact(method.userId());
            } else {
                getContact("");
            }
        } else {
            method.alertBox(getResources().getString(R.string.no_internet_connection));
        }
    }

    private boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void getContact(String user_id) {

        contactSubjects.clear();

        progressDialog.show();
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(ContactusActivity.this));
        jsObj.addProperty("user_id", user_id);
        ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<ContactRP> call = apiService.getContactSub(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<ContactRP>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<ContactRP> call, @NonNull Response<ContactRP> response) {

                try {
                    ContactRP contactRP = response.body();
                    assert contactRP != null;

                    if (contactRP.getStatus().equals("1")){
                        et_contact_name.setText(contactRP.getName());
                        et_contact_email.setText(contactRP.getEmail());

                        contactSubjects.add(new ContactSubject("","Select Subject"));
                        contactSubjects.addAll(contactRP.getContactSubjects());

                        List<String> strings = new ArrayList<>();
                        for (int i = 0; i < contactSubjects.size(); i++) {
                            strings.add(contactSubjects.get(i).getSubject());
                        }

                        // Creating adapter for spinner_cat
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(ContactusActivity.this, android.R.layout.simple_spinner_item, strings);
                        // Drop down layout style - list view with radio button
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        // attaching data adapter to spinner_cat
                        spinner.setAdapter(dataAdapter);

                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if (i == 0) {
                                    ((TextView) adapterView.getChildAt(0)).setTextColor(getResources().getColor(R.color.dark_grey));
                                } else {
                                    ((TextView) adapterView.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorPrimary));
                                }
                                contactType = contactSubjects.get(i).getSubject();
                                contactId = contactSubjects.get(i).getId();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                Log.d("data_app","");
                            }
                        });

                        submit_btn.setOnClickListener(view -> {

                            name = et_contact_name.getText().toString();
                            email = et_contact_email.getText().toString();
                            message = et_contact_msg.getText().toString();

                            checkValidations();
                        });
                    } else {
                        method.alertBox(contactRP.getMessage());
                    }
                }
                catch (Exception e){
                    Log.d("exception_error", e.toString());
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<ContactRP> call, @NonNull Throwable t) {
                Log.e("fail_api", t.toString());
                progressDialog.dismiss();
                method.alertBox(getResources().getString(R.string.failed_try_again));
            }
        });

    }

    private void checkValidations() {
        et_contact_name.setError(null);
        et_contact_email.setError(null);
        et_contact_msg.setError(null);

        if (contactType.equals("Select Subject") || contactType.equals("") || contactType.isEmpty()) {
            Toast.makeText(this, "Please Select Subject", Toast.LENGTH_LONG).show();
        } else if (name.equals("") || name.isEmpty()) {
            et_contact_name.requestFocus();
            et_contact_name.setError(getResources().getString(R.string.please_enter_name));
        } else if (email.equals("") || email.isEmpty()) {
            et_contact_email.requestFocus();
            et_contact_email.setError(getResources().getString(R.string.please_enter_email));
        } else if (!isValidMail(email)) {
            et_contact_email.requestFocus();
            et_contact_email.setError(getResources().getString(R.string.please_enter_valid_email));
        } else if (message.equals("") || message.isEmpty()) {
            et_contact_msg.requestFocus();
            et_contact_msg.setError(getResources().getString(R.string.please_enter_message));
        } else {

            et_contact_name.clearFocus();
            et_contact_email.clearFocus();
            et_contact_msg.clearFocus();

            if (method.isNetworkAvailable(this)) {
                contactUs(email, name, message, contactId);
            } else {
                method.alertBox(getResources().getString(R.string.no_internet_connection));
            }
        }
    }

    private void contactUs(String email, String name, String message, String contactId) {

        progressDialog.show();
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(this));
        jsObj.addProperty("contact_email", email);
        jsObj.addProperty("contact_name", name);
        jsObj.addProperty("contact_msg", message);
        jsObj.addProperty("contact_subject", contactId);

        ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<DataRP> call = apiService.submitContact(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<DataRP>() {
            @Override
            public void onResponse(Call<DataRP> call, Response<DataRP> response) {

                try {

                    DataRP dataRP = response.body();

                    assert dataRP != null;
                    if (dataRP.getSuccess().equals("1")) {

                        if (!method.isLogin()){
                            et_contact_name.setText("");
                            et_contact_email.setText("");
                        }
                        et_contact_msg.setText("");

                        spinner.setSelection(0);

                    }

                    method.alertBox(dataRP.getMsg());

                } catch (Exception e) {
                    Log.d("exception_error", e.toString());
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<DataRP> call, @NonNull Throwable t) {
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