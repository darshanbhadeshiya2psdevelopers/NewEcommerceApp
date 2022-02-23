package com.db.newecom.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.db.newecom.Api.ApiClient;
import com.db.newecom.Api.ApiInterface;
import com.db.newecom.R;
import com.db.newecom.Response.MyRatingReviewRP;
import com.db.newecom.Response.ProReviewRP;
import com.db.newecom.Utills.API;
import com.db.newecom.Utills.ConstantApi;
import com.db.newecom.Utills.Events;
import com.db.newecom.Utills.GlobalBus;
import com.db.newecom.Utills.Method;
import com.db.newecom.adapters.MyReviewAdapter;
import com.db.newecom.adapters.ReviewAdapter;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewsActivity extends AppCompatActivity {

    private Method method;
    private ProgressBar rate_progress_1, rate_progress_2, rate_progress_3, rate_progress_4, rate_progress_5;
    private RelativeLayout empty_layout;
    private LinearLayout progressBar;
    private String type, product_id;
    private MaterialCardView cardview_all_rate;
    private RecyclerView rv_reviews;
    private ReviewAdapter reviewAdapter;
    private MyReviewAdapter myReviewAdapter;
    private ScrollView scrollview_main;
    private TextView overall_rate, rate_5_count, rate_4_count, rate_3_count, rate_2_count, rate_1_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        type = getIntent().getStringExtra("type");
        product_id = getIntent().getStringExtra("product_id");

        GlobalBus.getBus().register(this);

        method = new Method(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        progressBar = findViewById(R.id.ll_progress_reviews);
        empty_layout = findViewById(R.id.empty_layout);
        scrollview_main = findViewById(R.id.scrollview_main);
        cardview_all_rate = findViewById(R.id.cardview_all_rate);
        rv_reviews = findViewById(R.id.rv_reviews);
        rate_progress_1 = findViewById(R.id.rate_progress_1);
        rate_progress_2 = findViewById(R.id.rate_progress_2);
        rate_progress_3 = findViewById(R.id.rate_progress_3);
        rate_progress_4 = findViewById(R.id.rate_progress_4);
        rate_progress_5 = findViewById(R.id.rate_progress_5);
        overall_rate = findViewById(R.id.overall_rate);
        rate_5_count = findViewById(R.id.rate_5_count);
        rate_4_count = findViewById(R.id.rate_4_count);
        rate_3_count = findViewById(R.id.rate_3_count);
        rate_2_count = findViewById(R.id.rate_2_count);
        rate_1_count = findViewById(R.id.rate_1_count);

        scrollview_main.setVisibility(View.GONE);

        if (type.equals("my review")){
            getSupportActionBar().setTitle(R.string.my_reviews);
            cardview_all_rate.setVisibility(View.GONE);
        }
        else {
            getSupportActionBar().setTitle(R.string.product_review);
        }

        if (method.isNetworkAvailable(this)){

            if (type.equals("pro review")) {
                if (method.isLogin())
                    getProReviews(method.userId());
                getProReviews("0");
            }
            else {
                if (method.isLogin())
                    getMyReviews(method.userId());
                else
                    Toast.makeText(this, getResources().getString(R.string.you_have_not_login), Toast.LENGTH_SHORT).show();

            }
        }
        else {
            progressBar.setVisibility(View.GONE);
            empty_layout.setVisibility(View.VISIBLE);
            method.alertBox(getResources().getString(R.string.no_internet_connection));
        }
    }

    @SuppressLint("SetTextI18n")
    @Subscribe
    public void geString(Events.RatingUpdate ratingUpdate){

        scrollview_main.setVisibility(View.GONE);

        if (method.isNetworkAvailable(this)){

            if (type.equals("pro review")) {
                if (method.isLogin())
                    getProReviews(method.userId());
                getProReviews("0");
            }
            else {
                if (method.isLogin())
                    getMyReviews(method.userId());
                else
                    Toast.makeText(this, getResources().getString(R.string.you_have_not_login), Toast.LENGTH_SHORT).show();

            }
        }
        else {
            progressBar.setVisibility(View.GONE);
            empty_layout.setVisibility(View.VISIBLE);
            method.alertBox(getResources().getString(R.string.no_internet_connection));
        }
    }

    private void getMyReviews(String userId) {

        progressBar.setVisibility(View.VISIBLE);

        JsonObject jsObj3 = (JsonObject) new Gson().toJsonTree(new API(this));
        jsObj3.addProperty("user_id", userId);
        ApiInterface apiService3 = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<MyRatingReviewRP> call3 = apiService3.getMyRatingReview(API.toBase64(jsObj3.toString()));
        call3.enqueue(new Callback<MyRatingReviewRP>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NotNull Call<MyRatingReviewRP> call, @NotNull Response<MyRatingReviewRP> response) {

                try {

                    MyRatingReviewRP myRatingReviewRP = response.body();

                    assert myRatingReviewRP != null;
                    if (myRatingReviewRP.getStatus().equals("1")) {

                        if (myRatingReviewRP.getMyReviewsLists().size() != 0) {

                            scrollview_main.setVisibility(View.VISIBLE);
                            myReviewAdapter = new MyReviewAdapter(ReviewsActivity.this, false, myRatingReviewRP.getMyReviewsLists());
                            rv_reviews.setAdapter(myReviewAdapter);
                        }
                        else {
                            scrollview_main.setVisibility(View.GONE);
                            empty_layout.setVisibility(View.VISIBLE);
                        }

                    } else {
                        scrollview_main.setVisibility(View.GONE);
                        empty_layout.setVisibility(View.VISIBLE);
                        method.alertBox(myRatingReviewRP.getMessage());
                    }

                } catch (Exception e) {
                    Log.d("exception_error", e.toString());
                    empty_layout.setVisibility(View.VISIBLE);
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }

                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(@NotNull Call<MyRatingReviewRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e("fail_api", t.toString());
                progressBar.setVisibility(View.GONE);
                empty_layout.setVisibility(View.VISIBLE);
                method.alertBox(getResources().getString(R.string.failed_try_again));
            }
        });

    }

    private void getProReviews(String userId) {

        progressBar.setVisibility(View.VISIBLE);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(this));
        jsObj.addProperty("user_id", userId);
        jsObj.addProperty("product_id", product_id);
        ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<ProReviewRP> call = apiService.getProReviewDetail(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<ProReviewRP>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NotNull Call<ProReviewRP> call, @NotNull Response<ProReviewRP> response) {

                try {

                    ProReviewRP proReviewRP = response.body();
                    assert proReviewRP != null;

                    if (proReviewRP.getStatus().equals("1")) {

                        scrollview_main.setVisibility(View.VISIBLE);

                        overall_rate.setText(proReviewRP.getOver_all_rate());
                        rate_5_count.setText(proReviewRP.getFive_rate_count());
                        rate_4_count.setText(proReviewRP.getFour_rate_count());
                        rate_3_count.setText(proReviewRP.getThree_rate_count());
                        rate_2_count.setText(proReviewRP.getTwo_rate_count());
                        rate_1_count.setText(proReviewRP.getOne_rate_count());

                        rate_progress_1.setProgress(Integer.parseInt(proReviewRP.getFive_rate_per()));
                        rate_progress_2.setProgress(Integer.parseInt(proReviewRP.getFour_rate_per()));
                        rate_progress_3.setProgress(Integer.parseInt(proReviewRP.getThree_rate_per()));
                        rate_progress_4.setProgress(Integer.parseInt(proReviewRP.getTwo_rate_per()));
                        rate_progress_5.setProgress(Integer.parseInt(proReviewRP.getOne_rate_per()));

                        if (proReviewRP.getProReviewLists().size() == 0) {

                            progressBar.setVisibility(View.GONE);
                            scrollview_main.setVisibility(View.GONE);
                            empty_layout.setVisibility(View.VISIBLE);

                        } else {
                            reviewAdapter = new ReviewAdapter(ReviewsActivity.this, proReviewRP.getProReviewLists());
                            rv_reviews.setAdapter(reviewAdapter);
                        }

                    } else {
                        method.alertBox(proReviewRP.getMessage());
                        empty_layout.setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {
                    Log.d(ConstantApi.exceptionError, e.toString());
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }

                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(@NotNull Call<ProReviewRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e(ConstantApi.failApi, t.toString());
                progressBar.setVisibility(View.GONE);
                empty_layout.setVisibility(View.VISIBLE);
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
        super.onDestroy();
        GlobalBus.getBus().unregister(this);
    }
}