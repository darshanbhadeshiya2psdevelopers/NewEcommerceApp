package com.db.newecom.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.db.newecom.Api.ApiClient;
import com.db.newecom.Api.ApiInterface;
import com.db.newecom.R;
import com.db.newecom.Response.PolicyRP;
import com.db.newecom.Utills.API;
import com.db.newecom.Utills.Method;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PolicyActivity extends AppCompatActivity {

    private Method method;
    private RelativeLayout empty_layout;
    private String policy_type;
    private WebView webView;
    private ProgressBar progressBar_policy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);

        policy_type = getIntent().getStringExtra("policy_type");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        method = new Method(this);

        webView = findViewById(R.id.policy_webview);
        empty_layout = findViewById(R.id.empty_layout);
        progressBar_policy = findViewById(R.id.progressBar_policy);

        webView.setVisibility(View.GONE);

        switch (policy_type) {

            case "term_of_use":
                getSupportActionBar().setTitle(R.string.terms_of_use);
                break;
            case "privacy_policy":
                getSupportActionBar().setTitle(R.string.privacy_policy);
                break;
            case "refund_policy":
                getSupportActionBar().setTitle(R.string.return_policy);
                break;
            case "cancel_policy":
                getSupportActionBar().setTitle(R.string.cancellation_policy);
                break;
        }

        if (method.isNetworkAvailable(this)) {
            getpolicy(policy_type);
        } else {
            empty_layout.setVisibility(View.VISIBLE);
            progressBar_policy.setVisibility(View.GONE);
            method.alertBox(getResources().getString(R.string.no_internet_connection));
        }

    }

    private void getpolicy(String policy_type) {

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(PolicyActivity.this));
        jsObj.addProperty("type", policy_type);
        ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<PolicyRP> call = apiService.getPolicy(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<PolicyRP>() {
            @Override
            public void onResponse(Call<PolicyRP> call, Response<PolicyRP> response) {
                try {
                    PolicyRP policyRP = response.body();
                    assert policyRP != null;

                    if (policyRP.getStatus().equals("1")){

                        webView.setVisibility(View.VISIBLE);

                        webView.setBackgroundColor(Color.TRANSPARENT);
                        webView.setFocusableInTouchMode(false);
                        webView.setFocusable(false);
                        webView.getSettings().setDefaultTextEncodingName("UTF-8");
                        String mimeType = "text/html";
                        String encoding = "utf-8";

                        String text = "<html><head>"
                                + "<style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/poppins_medium.ttf\");}body{font-family: MyFont;color:#636363;line-height:1.3;}"
                                + "a {color:#1968C8;}"
                                + "</style></head>"
                                + "<body>"
                                + policyRP.getContent()
                                + "</body></html>";

                        webView.loadDataWithBaseURL(null, text, mimeType, encoding, null);
                    }
                    else {
                        empty_layout.setVisibility(View.VISIBLE);
                    }

                    progressBar_policy.setVisibility(View.GONE);

                }
                catch (Exception e){
                    Log.d("exception_error", e.toString());
                    empty_layout.setVisibility(View.VISIBLE);
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }
            }

            @Override
            public void onFailure(Call<PolicyRP> call, Throwable t) {
                Log.e("fail_api", t.toString());
                empty_layout.setVisibility(View.VISIBLE);
                progressBar_policy.setVisibility(View.GONE);
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