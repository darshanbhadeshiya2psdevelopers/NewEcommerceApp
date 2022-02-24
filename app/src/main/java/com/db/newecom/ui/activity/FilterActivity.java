package com.db.newecom.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.db.newecom.Api.ApiClient;
import com.db.newecom.Api.ApiInterface;
import com.db.newecom.Model.FilterList;
import com.db.newecom.R;
import com.db.newecom.Response.FilterRP;
import com.db.newecom.Utills.API;
import com.db.newecom.Utills.ConstantApi;
import com.db.newecom.Utills.Events;
import com.db.newecom.Utills.GlobalBus;
import com.db.newecom.Utills.Method;
import com.db.newecom.adapters.FilterListAdapter;
import com.db.newecom.adapters.SortByAdapter;
import com.db.newecom.interfaces.FilterType;
import com.db.newecom.ui.fragment.BrandSelectionFragment;
import com.db.newecom.ui.fragment.OtherFilterSelectionFragment;
import com.db.newecom.ui.fragment.PriceSelectionFragment;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilterActivity extends AppCompatActivity {

    private Method method;
    private Menu menu;
    private LinearLayout progressBar;
    private RelativeLayout empty_layout;
    private LinearLayout ll_filter_main, ll_sortby;
    private RecyclerView rv_sortby, rv_filter;
    private TextView apply_btn;
    private FilterListAdapter filterListAdapter;
    private SortByAdapter sortByAdapter;
    private String sortBy;
    private List<FilterList> filterLists;
    private FilterType filterType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        String filterType = intent.getStringExtra("filter_type");//send to api filter type and get price and brand
        String search = intent.getStringExtra("search");
        String id = intent.getStringExtra("id");
        String sort = intent.getStringExtra("sort");

        method = new Method(this);

        filterLists = new ArrayList<>();

        progressBar = findViewById(R.id.ll_progress_filter);
        empty_layout = findViewById(R.id.empty_layout);
        ll_filter_main = findViewById(R.id.ll_filter_main);
        ll_sortby = findViewById(R.id.ll_sortby);
        apply_btn = findViewById(R.id.apply_btn);
        rv_sortby = findViewById(R.id.rv_sortby);
        rv_filter = findViewById(R.id.rv_filter);

        if (method.isNetworkAvailable(this)) {
            checkFilter(filterType, search, id, sort);
        } else {
            progressBar.setVisibility(View.GONE);
            empty_layout.setVisibility(View.VISIBLE);
            ll_filter_main.setVisibility(View.GONE);
            method.alertBox(getResources().getString(R.string.no_internet_connection));
        }

    }

    private void checkFilter(String type, String search, String id, String sort) {

        progressBar.setVisibility(View.VISIBLE);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(this));
        jsObj.addProperty("type", type);

        if (type != "today_deal" || type != "latest_products")
            jsObj.addProperty("id", id);

        jsObj.addProperty("sort", sort);
        if (type.equals("recent_viewed_products")) {
            jsObj.addProperty("user_id", method.userId());
        }
        if (type.equals("search")) {
            jsObj.addProperty("keyword", search);
        }
        ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<FilterRP> call = apiService.getFilterType(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<FilterRP>() {
            @Override
            public void onResponse(@NotNull Call<FilterRP> call, @NotNull Response<FilterRP> response) {

                try {

                    FilterRP filterRP = response.body();
                    assert filterRP != null;

                    if (filterRP.getStatus().equals("1")) {

                        ConstantApi.brand_ids_temp = ConstantApi.brand_ids;
                        ConstantApi.sizes_temp = ConstantApi.sizes;

                        ConstantApi.pre_min_temp = ConstantApi.pre_min;
                        ConstantApi.pre_max_temp = ConstantApi.pre_max;

                        filterLists.add(new FilterList(getResources().getString(R.string.price), "price", "true"));

                        if (filterRP.getBrand_filter().equals("1")) {
                            filterLists.add(new FilterList(getResources().getString(R.string.brands), "brand", filterRP.getBrand_filter()));
                        }
                        if (filterRP.getSize_status().equals("1")) {
                            filterLists.add(new FilterList(getResources().getString(R.string.size), "size", filterRP.getSize_status()));
                        }

                        filterType = (title_filter, type_filter, selectData) -> {

                            if (type_filter.equals("filter_list")) {
                                if (selectData.equals("price")) {

                                    PriceSelectionFragment priceSelectionFragment = new PriceSelectionFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("filter_type", type);
                                    bundle.putString("search", search);
                                    bundle.putString("id", id);
                                    bundle.putString("sort", sort);

                                    priceSelectionFragment.setArguments(bundle);
                                    getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.nav_host_fragment_filter, priceSelectionFragment)
                                            .commitAllowingStateLoss();


                                } else if (selectData.equals("brand")) {

                                    BrandSelectionFragment brandSelectionFragment = new BrandSelectionFragment();

                                    Bundle bundle = new Bundle();
                                    bundle.putString("filter_type", type);
                                    bundle.putString("search", search);
                                    bundle.putString("id", id);
                                    bundle.putString("sort", sort);
                                    brandSelectionFragment.setArguments(bundle);
                                    getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.nav_host_fragment_filter, brandSelectionFragment)
                                            .commitAllowingStateLoss();


                                } else {

                                    OtherFilterSelectionFragment sizeSelectionFragment = new OtherFilterSelectionFragment();

                                    Bundle bundle = new Bundle();
                                    bundle.putString("filter_type", type);
                                    bundle.putString("search", search);
                                    bundle.putString("id", id);
                                    bundle.putString("sort", sort);

                                    sizeSelectionFragment.setArguments(bundle);
                                    getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.nav_host_fragment_filter, sizeSelectionFragment)
                                            .commitAllowingStateLoss();
                                }
                            } else {
                                sortBy = selectData;
                            }

                        };

                        sortByAdapter = new SortByAdapter(FilterActivity.this, "filter_sortBy", filterRP.getFilterSortByLists(), filterType);
                        rv_sortby.setAdapter(sortByAdapter);

                        filterListAdapter = new FilterListAdapter(FilterActivity.this, "filter_list", filterLists, filterType);
                        rv_filter.setAdapter(filterListAdapter);

                        ll_filter_main.setVisibility(View.VISIBLE);

                        PriceSelectionFragment priceSelectionFragment = new PriceSelectionFragment();

                        Bundle bundle = new Bundle();
                        bundle.putString("filter_type", type);
                        bundle.putString("search", search);
                        bundle.putString("id", id);
                        bundle.putString("sort", sort);

                        priceSelectionFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.nav_host_fragment_filter, priceSelectionFragment)
                                .commitAllowingStateLoss();

                        apply_btn.setEnabled(true);

                        apply_btn.setOnClickListener(v -> {
                            onBackPressed();
                            ConstantApi.brand_ids = ConstantApi.brand_ids_temp;
                            ConstantApi.brand_ids_temp = "";

                            ConstantApi.sizes = ConstantApi.sizes_temp;
                            ConstantApi.sizes_temp = "";

                            ConstantApi.pre_min = ConstantApi.pre_min_temp;
                            ConstantApi.pre_max = ConstantApi.pre_max_temp;

                            ConstantApi.pre_min_temp = "";
                            ConstantApi.pre_max_temp = "";

                            Events.Filter filter = new Events.Filter(id, ConstantApi.brand_ids, ConstantApi.sizes, sortBy, ConstantApi.pre_min, ConstantApi.pre_max);
                            GlobalBus.getBus().post(filter);
                        });

                    } else {
                        empty_layout.setVisibility(View.VISIBLE);
                        ll_filter_main.setVisibility(View.GONE);
                        method.alertBox(filterRP.getMessage());
                    }

                } catch (Exception e) {
                    Log.d(ConstantApi.exceptionError, e.toString());
                    empty_layout.setVisibility(View.VISIBLE);
                    ll_filter_main.setVisibility(View.GONE);
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }

                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(@NotNull Call<FilterRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e(ConstantApi.failApi, t.toString());
                empty_layout.setVisibility(View.VISIBLE);
                ll_filter_main.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                method.alertBox(getResources().getString(R.string.failed_try_again));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.filter_activity_menu, menu);

        this.menu = menu;
        MenuItem item = menu.findItem(R.id.clear_filter);
        item.setOnMenuItemClickListener(menuItem -> {
            ConstantApi.brand_ids_temp = "";
            ConstantApi.sizes_temp = "";
            ConstantApi.pre_min_temp = "";
            ConstantApi.pre_max_temp = "";
            Toast.makeText(this, "All Filters are cleared", Toast.LENGTH_SHORT).show();
            return false;
        });
        return super.onCreateOptionsMenu(menu);
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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}