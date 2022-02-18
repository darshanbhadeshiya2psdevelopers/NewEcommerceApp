package com.db.newecom.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.db.newecom.Api.ApiClient;
import com.db.newecom.Api.ApiInterface;
import com.db.newecom.R;
import com.db.newecom.Response.FaqRP;
import com.db.newecom.Utills.API;
import com.db.newecom.Utills.Method;
import com.db.newecom.adapters.FaqAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FAQActivity extends AppCompatActivity {

    private Method method;
    private String type;
    private RecyclerView rv_faq;
    private ProgressBar progressBar_faq;
    private RelativeLayout empty_layout;
    private Button empty_btn;
    private FaqAdapter faqAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqactivity);

        method = new Method(FAQActivity.this);

        type = getIntent().getStringExtra("type");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        progressBar_faq = findViewById(R.id.progressBar_faq);
        empty_layout = findViewById(R.id.empty_layout);
        rv_faq = findViewById(R.id.rv_faq);

        switch (type){
            case "faq":
                getSupportActionBar().setTitle(R.string.faq);
                break;

            case "payment":
                getSupportActionBar().setTitle(R.string.payment);
                break;

        }

        if (method.isNetworkAvailable(this)) {
            faq(type);
        } else {
            progressBar_faq.setVisibility(View.GONE);
            empty_layout.setVisibility(View.VISIBLE);
            method.alertBox(getResources().getString(R.string.no_internet_connection));
        }

    }

    private void faq(String type) {

        progressBar_faq.setVisibility(View.VISIBLE);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(FAQActivity.this));
        jsObj.addProperty("type", type);
        ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<FaqRP> call = apiService.getFaq(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<FaqRP>() {
            @Override
            public void onResponse(Call<FaqRP> call, Response<FaqRP> response) {

                try {
                    FaqRP faqRp = response.body();
                    assert faqRp != null;

                    if (faqRp.getFaqLists().size() != 0){
                        faqAdapter = new FaqAdapter(FAQActivity.this, faqRp.getFaqLists());
                        rv_faq.setAdapter(faqAdapter);
                    }
                    else
                        empty_layout.setVisibility(View.VISIBLE);

                } catch (Exception e) {
                    Log.d("exception_error", e.toString());
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                    empty_layout.setVisibility(View.VISIBLE);
                }

                progressBar_faq.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<FaqRP> call, Throwable t) {
                Log.e("fail_api", t.toString());
                empty_layout.setVisibility(View.VISIBLE);
                progressBar_faq.setVisibility(View.GONE);
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