package com.db.newecom.ui.fragment;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.db.newecom.Api.ApiClient;
import com.db.newecom.Api.ApiInterface;
import com.db.newecom.Model.ProductList;
import com.db.newecom.R;
import com.db.newecom.Response.ProRP;
import com.db.newecom.Response.ProductRP;
import com.db.newecom.Utills.API;
import com.db.newecom.Utills.ConstantApi;
import com.db.newecom.Utills.EndlessRecyclerViewScrollListener;
import com.db.newecom.Utills.Events;
import com.db.newecom.Utills.GlobalBus;
import com.db.newecom.Utills.Method;
import com.db.newecom.adapters.ProductsAdapter;
import com.db.newecom.ui.activity.FilterActivity;
import com.db.newecom.ui.activity.MainActivity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "title";
    private static final String ARG_PARAM2 = "cat_id";
    private static final String ARG_PARAM3 = "subcat_id";
    private static final String ARG_PARAM4 = "type";
    private static final String ARG_PARAM5 = "search_keyword";

    // TODO: Rename and change types of parameters
    private String title, category_id, subcategory_id, type, filterType,
            search, catId, subCatId, filterId = "0", brandIds = "", sizes = "", sortBy = "",
            preMin = "", preMax = "";
    private Method method;
    private LinearLayout progressBar;
    private RelativeLayout empty_layout;
    private TextView empty_msg, total_products;
    private boolean isGridOption = false, isFilter = false;
    private ImageView grid_option, list_option;
    private LinearLayout ll_filter, ll_products_main;
    private RecyclerView rv_pro_fragment;
    private ProductsAdapter productsAdapter;
    private List<ProductList> productLists;
    private SwipeRefreshLayout swipeRefreshLayout;

    private Boolean isOver = false;
    private int paginationIndex = 1;
    private EndlessRecyclerViewScrollListener scrollListener;

    public ProductsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductsFragment newInstance(String param1, String param2, String param3, String param4,
                                               String param5) {
        ProductsFragment fragment = new ProductsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putString(ARG_PARAM4, param4);
        args.putString(ARG_PARAM5, param5);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_PARAM1);
            category_id = getArguments().getString(ARG_PARAM2);
            subcategory_id = getArguments().getString(ARG_PARAM3);
            type = getArguments().getString(ARG_PARAM4);
            search = getArguments().getString(ARG_PARAM5);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        GlobalBus.getBus().register(this);

        if (MainActivity.toolbar != null)
            MainActivity.toolbar.setTitle(title);

        method = new Method(getActivity());
        productLists = new ArrayList<>();

        //clear filter variable
        ConstantApi.brand_ids = "";
        ConstantApi.sizes = "";
        ConstantApi.pre_min = "";
        ConstantApi.pre_max = "";

        swipeRefreshLayout = view.findViewById(R.id.rf_layout_pro);
        progressBar = view.findViewById(R.id.ll_progress_products);
        ll_products_main = view.findViewById(R.id.ll_products_main);
        empty_layout = view.findViewById(R.id.empty_layout);
        empty_msg = view.findViewById(R.id.empty_msg);
        total_products = view.findViewById(R.id.total_products);
        grid_option = view.findViewById(R.id.grid_option);
        list_option = view.findViewById(R.id.list_option);
        ll_filter = view.findViewById(R.id.ll_filter);
        rv_pro_fragment = view.findViewById(R.id.rv_pro_fragment);

        swipeRefreshLayout.setColorSchemeColors(getActivity().getResources().getColor(R.color.dark_blue));

        GridLayoutManager layoutManagerGrid = new GridLayoutManager(getActivity(), 2);
        layoutManagerGrid.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (productsAdapter != null) {
                    if (productsAdapter.getItemViewType(position) == 0) {
                        return 2;
                    }
                    return 1;
                }
                return 1;
            }
        });
        rv_pro_fragment.setLayoutManager(layoutManagerGrid);
        loadMoreData(layoutManagerGrid);
        rv_pro_fragment.setFocusable(false);

//        if (isGridOption)
//            setGridOption();
//        else
//            setListOption();

        isGridOption = true;

        grid_option.setImageTintList(
                ColorStateList.valueOf(getActivity().getResources().getColor(R.color.colorPrimary)));
        list_option.setImageTintList(
                ColorStateList.valueOf(getActivity().getResources().getColor(R.color.dark_grey)));

        grid_option.setOnClickListener(v -> setGridOption());
        list_option.setOnClickListener(v -> setListOption());

        if (method.isNetworkAvailable(getActivity())){
//            if (method.isLogin())
//                getProducts(method.userId());
//            else
//                getProducts("0");
            callData();
        }
        else {
            ll_products_main.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            empty_layout.setVisibility(View.VISIBLE);
            method.alertBox(getActivity().getResources().getString(R.string.no_internet_connection));
        }

        //swipeRefreshLayout.setEnabled(false);

        swipeRefreshLayout.setOnRefreshListener(() -> {

            ll_products_main.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            //productLists.clear();

            if (method.isNetworkAvailable(getActivity())){
                loadMoreData(rv_pro_fragment.getLayoutManager());
                callData();
            }
            else {
                ll_products_main.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                empty_layout.setVisibility(View.VISIBLE);
                method.alertBox(getActivity().getResources().getString(R.string.no_internet_connection));
            }
        });

        return view;
    }

    @Subscribe
    public void geString(Events.Filter filter) {
        isFilter = true;
        empty_layout.setVisibility(View.GONE);
        if (method.isNetworkAvailable(getActivity())) {

            filterId = filter.getId();
            brandIds = filter.getBrand_ids();
            sizes = filter.getSize();
            sortBy = filter.getSortBy();
            preMin = filter.getPre_min();
            preMax = filter.getPre_max();

            productLists.clear();
            if (productsAdapter != null) {
                productsAdapter.notifyDataSetChanged();
            }

            scrollListener.resetState();
            paginationIndex = 1;
            isOver = false;
            productsAdapter = null;
            filter(filterId, brandIds, sizes, sortBy, preMin, preMax);

        } else {
            method.alertBox(getResources().getString(R.string.no_internet_connection));
        }
    }

    public void loadMoreData(RecyclerView.LayoutManager layoutManager) {
        scrollListener = new EndlessRecyclerViewScrollListener((LinearLayoutManager) layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (!isOver) {
                    new Handler().postDelayed(() -> {
                        paginationIndex++;
                        if (isFilter) {
                            filter(filterId, brandIds, sizes, sortBy, preMin, preMax);
                        } else {
                            callData();
                        }
                    }, 1000);
                } else {
                    productsAdapter.hideHeader();
                }
            }
        };
        rv_pro_fragment.addOnScrollListener(scrollListener);
    }

    private void callData() {
        if (method.isNetworkAvailable(getActivity())) {
            if (method.isLogin())
                getProducts(method.userId());
            else
                getProducts("0");
        } else {
            method.alertBox(getResources().getString(R.string.no_internet_connection));
        }
    }

    private void getProducts(String userId) {

        if (getActivity() != null){

            if (productsAdapter == null) {
                productLists.clear();
                progressBar.setVisibility(View.VISIBLE);
            }

            JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(getActivity()));
            jsObj.addProperty("user_id", userId);
            jsObj.addProperty("page", paginationIndex);
            ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
            Call<ProductRP> call;
            switch (type){
                case "home_cat":
                    filterType = "productList_cat";
                    jsObj.addProperty("cat_id", category_id);
                    jsObj.addProperty("sub_cat_id", "");
                    call = apiService.getCatSubPro(API.toBase64(jsObj.toString()));
                    break;
                case "offers":
                    filterType = "offer";
                    jsObj.addProperty("offer_id", category_id);
                    call = apiService.getOfferPro(API.toBase64(jsObj.toString()));
                    break;
                case "banners":
                    filterType = "banner";
                    jsObj.addProperty("banner_id", category_id);
                    call = apiService.getSliderPro(API.toBase64(jsObj.toString()));
                    break;
                case "todays_deal":
                    filterType = "today_deal";
                    call = apiService.getTodayDealPro(API.toBase64(jsObj.toString()));
                    break;
                case "brands":
                    filterType = "brand";
                    jsObj.addProperty("brand_id", category_id);
                    call = apiService.getBrandPro(API.toBase64(jsObj.toString()));
                    break;
                case "latest_pro":
                    filterType = "latest_products";
                    call = apiService.getLatestPro(API.toBase64(jsObj.toString()));
                    break;
                case "sub_cat_pro":
                    filterType = "productList_cat_sub";
                    jsObj.addProperty("cat_id", category_id);
                    jsObj.addProperty("sub_cat_id", subcategory_id);
                    call = apiService.getCatSubPro(API.toBase64(jsObj.toString()));
                    break;
                case "search":
                    filterType = "search";
                    jsObj.addProperty("keyword", search);
                    call = apiService.getSearchPro(API.toBase64(jsObj.toString()));
                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + type);
            }

            call.enqueue(new Callback<ProductRP>() {
                @Override
                public void onResponse(@NotNull Call<ProductRP> call, @NotNull Response<ProductRP> response) {

                    if (getActivity() != null) {

                        try {
                            ProductRP productRP = response.body();
                            assert productRP != null;

                            if (productRP.getStatus().equals("1")) {

                                swipeRefreshLayout.setRefreshing(false);

                                ll_products_main.setVisibility(View.VISIBLE);

                                total_products.setText("Total Items : " + productRP.getTotal_products());

                                if (productRP.getProductLists().size() == 0) {
                                    if (productsAdapter != null) {
                                        productsAdapter.hideHeader();
                                        isOver = true;
                                    }
                                } else {
                                    productLists.addAll(productRP.getProductLists());
                                }

                                if (productsAdapter == null) {

                                    if (productLists.size() != 0) {

                                        productsAdapter = new ProductsAdapter(getActivity(), productLists, isGridOption);
                                        rv_pro_fragment.setAdapter(productsAdapter);

                                        ll_filter.setOnClickListener(v -> {
                                            String id = "";

                                            if (filterType != "today_deal" || filterType != "latest_products") {
                                                if (filterType.equals("productList_cat_sub")) {
                                                    id = subcategory_id;
                                                } else {
                                                    id = category_id;
                                                }
                                            }
                                            startActivity(new Intent(getActivity(), FilterActivity.class)
                                                    .putExtra("filter_type", filterType)//using which type of filter like as banner brand etc..
                                                    .putExtra("search", search)
                                                    .putExtra("id", id)
                                                    .putExtra("sort", "newest"));
                                        });

                                    } else {
                                        empty_layout.setVisibility(View.VISIBLE);
                                        ll_products_main.setVisibility(View.GONE);
                                        empty_msg.setText(getActivity().getResources().getString(R.string.no_items));
                                    }

                                } else {
                                    productsAdapter.notifyDataSetChanged();
                                }


//                                if (productRP.getProductLists().size() == 0) {
//                                    empty_layout.setVisibility(View.VISIBLE);
//                                    ll_products_main.setVisibility(View.GONE);
//                                    empty_msg.setText(getActivity().getResources().getString(R.string.no_items));
//
//                                    if (productsAdapter != null) {
//                                        productsAdapter.hideHeader();
//                                        isOver = true;
//                                    }
//
//                                } else {
//                                    empty_layout.setVisibility(View.GONE);
//                                    ll_products_main.setVisibility(View.VISIBLE);
//
//                                    total_products.setText("Total Items : " + productRP.getTotal_products());
//
//                                    productLists.addAll(productRP.getProductLists());
//                                    //rv_pro_fragment.setItemViewCacheSize(productRP.getProductLists().size());
//
//                                    ll_filter.setOnClickListener(v -> {
//                                        String id = "";
//
//                                        if (filterType != "today_deal" || filterType != "latest_products") {
//                                            if (filterType.equals("productList_cat_sub")) {
//                                                id = subcategory_id;
//                                            } else {
//                                                id = category_id;
//                                            }
//                                        }
//                                        startActivity(new Intent(getActivity(), FilterActivity.class)
//                                                .putExtra("filter_type", filterType)//using which type of filter like as banner brand etc..
//                                                .putExtra("search", search)
//                                                .putExtra("id", id)
//                                                .putExtra("sort", "newest"));
//                                    });
//                                }

                            } else {
                                empty_layout.setVisibility(View.VISIBLE);
                                ll_products_main.setVisibility(View.GONE);
                                method.alertBox(productRP.getMessage());
                            }

                        } catch (Exception e) {
                            Log.d(ConstantApi.exceptionError, e.toString());
                            empty_layout.setVisibility(View.VISIBLE);
                            ll_products_main.setVisibility(View.GONE);
                            method.alertBox(getResources().getString(R.string.failed_try_again));
                            isOver = true;
                        }
                    }

                    progressBar.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(@NotNull Call<ProductRP> call, @NotNull Throwable t) {
                    // Log error here since request failed
                    Log.e(ConstantApi.failApi, t.toString());
                    empty_layout.setVisibility(View.VISIBLE);
                    ll_products_main.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                    isOver = true;
                }
            });

        }

    }

    public void setGridOption(){

        isGridOption = true;

        grid_option.setImageTintList(
                ColorStateList.valueOf(getActivity().getResources().getColor(R.color.colorPrimary)));
        list_option.setImageTintList(
                ColorStateList.valueOf(getActivity().getResources().getColor(R.color.dark_grey)));
//        productsAdapter = new ProductsAdapter(getActivity(),"grid", productLists);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (productsAdapter != null) {
                    if (productsAdapter.getItemViewType(position) == 0) {
                        return 2;
                    }
                    return 1;
                }
                return 1;
            }
        });
        rv_pro_fragment.setLayoutManager(gridLayoutManager);
        loadMoreData(gridLayoutManager);
        productsAdapter = new ProductsAdapter(getActivity(), productLists, isGridOption);
        rv_pro_fragment.setAdapter(productsAdapter);

//        rv_pro_fragment.setLayoutManager(new GridLayoutManager(getActivity(), 2));
//        rv_pro_fragment.setAdapter(productsAdapter);
    }

    public void setListOption(){

        isGridOption = false;

        list_option.setImageTintList(
                ColorStateList.valueOf(getActivity().getResources().getColor(R.color.colorPrimary)));
        grid_option.setImageTintList(
                ColorStateList.valueOf(getActivity().getResources().getColor(R.color.dark_grey)));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rv_pro_fragment.setLayoutManager(layoutManager);
        loadMoreData(layoutManager);
        productsAdapter = new ProductsAdapter(getActivity(), productLists, isGridOption);
        rv_pro_fragment.setAdapter(productsAdapter);

//        productsAdapter = new ProductsAdapter(getActivity(),"list", productLists);
//        rv_pro_fragment.setLayoutManager(new LinearLayoutManager(getActivity()));
//        rv_pro_fragment.setAdapter(productsAdapter);
    }

    private void filter(String id, String brandIds, String sizes, String sort, String minPrice, String maxPrice) {

        if (getActivity() != null) {

            if (productsAdapter == null) {
                productLists.clear();
                progressBar.setVisibility(View.VISIBLE);
            }

            JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(getActivity()));
            jsObj.addProperty("type", filterType);
            jsObj.addProperty("id", id);
            jsObj.addProperty("brand_ids", brandIds);
            jsObj.addProperty("sizes", sizes);
            jsObj.addProperty("sort", sort);
            jsObj.addProperty("min_price", minPrice);
            jsObj.addProperty("max_price", maxPrice);
            jsObj.addProperty("page", paginationIndex);
            if (filterType.equals("recent_viewed_products")) {
                jsObj.addProperty("user_id", method.userId());
            }
            if (filterType.equals("search")) {
                jsObj.addProperty("keyword", search);
            }
            ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
            Call<ProRP> call = apiService.getFilterPro(API.toBase64(jsObj.toString()));
            call.enqueue(new Callback<ProRP>() {
                @Override
                public void onResponse(@NotNull Call<ProRP> call, @NotNull Response<ProRP> response) {


                    if (getActivity() != null) {
                        try {

                            ProRP proRP = response.body();
                            assert proRP != null;

                            if (proRP.getStatus().equals("1")) {

                                if (proRP.getSuccess().equals("1")){

                                    total_products.setText("Total Items : " + proRP.getTotal_products());

                                    if (proRP.getProductLists().size() == 0) {
                                        if (productsAdapter != null) {
                                            productsAdapter.hideHeader();
                                            isOver = true;
                                        }
                                    } else {
                                        productLists.addAll(proRP.getProductLists());
                                    }

                                    if (productsAdapter == null) {

                                        if (productLists.size() != 0) {

                                            productsAdapter = new ProductsAdapter(getActivity(), productLists,isGridOption);
                                            rv_pro_fragment.setAdapter(productsAdapter);

                                            ll_filter.setOnClickListener(v -> startActivity(new Intent(getActivity(), FilterActivity.class)
                                                    .putExtra("filter_type", filterType)
                                                    .putExtra("search", search)
                                                    .putExtra("id", id)
                                                    .putExtra("sort", sort)));

                                        } else {
                                            empty_layout.setVisibility(View.VISIBLE);
                                        }

                                    } else {
                                        productsAdapter.notifyDataSetChanged();
                                    }
                                }
                                //method.alertBox(proRP.getMsg());
                                Toast.makeText(getActivity(), proRP.getMsg(), Toast.LENGTH_SHORT).show();

                            } else {
                                empty_layout.setVisibility(View.VISIBLE);
                                method.alertBox(proRP.getMessage());
                            }

                        } catch (Exception e) {
                            Log.d(ConstantApi.exceptionError, e.toString());
                            method.alertBox(getResources().getString(R.string.failed_try_again));
                            isOver = true;
                        }
                    }

                    progressBar.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(@NotNull Call<ProRP> call, @NotNull Throwable t) {
                    // Log error here since request failed
                    Log.e(ConstantApi.failApi, t.toString());
                    progressBar.setVisibility(View.GONE);
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                    isOver = true;
                }
            });

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Unregister the registered event.
        GlobalBus.getBus().unregister(this);
    }
}