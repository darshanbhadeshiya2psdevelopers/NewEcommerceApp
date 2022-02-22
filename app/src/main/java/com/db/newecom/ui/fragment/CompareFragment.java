package com.db.newecom.ui.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.db.newecom.Api.ApiClient;
import com.db.newecom.Api.ApiInterface;
import com.db.newecom.Model.CompareProList;
import com.db.newecom.R;
import com.db.newecom.Response.AddToCartRP;
import com.db.newecom.Response.CompareRP;
import com.db.newecom.Response.DataRP;
import com.db.newecom.Utills.API;
import com.db.newecom.Utills.ConstantApi;
import com.db.newecom.Utills.Events;
import com.db.newecom.Utills.GlobalBus;
import com.db.newecom.Utills.Method;
import com.db.newecom.ui.activity.CartActivity;
import com.db.newecom.ui.activity.LoginActivity;
import com.db.newecom.ui.activity.ProductDetailsActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CompareFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompareFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1, sign;
    private String mParam2;
    private Method method;
    private View view;
    private RelativeLayout empty_layout;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    private ScrollView scrollView;
    private ImageView empty_image, compare_pro_img1, compare_pro_img2, compare_pro_img3;
    private TextView empty_msg, compare_pro_name1, compare_pro_name2, compare_pro_name3, compare_pro_price1,
            compare_pro_price2, compare_pro_price3, availability1, availability2, availability3, color_1,
            color_2, color_3, brand_1, brand_2, brand_3, cart_items;
    private Button empty_btn, add_to_cart_btn1, add_to_cart_btn2, add_to_cart_btn3, remove_btn1,
            remove_btn2, remove_btn3, add_pro_btn;
    private RatingBar compare_pro_rate1, compare_pro_rate2, compare_pro_rate3;
    private LinearLayout ll_compare_pro, ll_pro_1, ll_pro_2, ll_pro_3, ll_compare_availability, ll_availability_3,
            ll_compare_desc, ll_desc_3, ll_colors, ll_color_3, ll_brands, ll_brand_3, ll_buttons, ll_buttons_3,
            ll_add_pro;
    private WebView webView1, webView2, webView3;

    public CompareFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CompareFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CompareFragment newInstance(String param1, String param2) {
        CompareFragment fragment = new CompareFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_compare, container, false);

        GlobalBus.getBus().register(this);
        method = new Method(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        sign = ConstantApi.currency + " ";

        initialize(view);

        empty_image.setImageResource(R.drawable.ic_baseline_compare_arrows_24);
        empty_msg.setText(R.string.empty_compare);
        empty_btn.setVisibility(View.GONE);

        scrollView.setVisibility(View.GONE);

        String mimeType = "text/html";
        String encoding = "utf-8";

        webView1.setBackgroundColor(Color.TRANSPARENT);
        webView1.setFocusableInTouchMode(false);
        webView1.setFocusable(false);
        webView1.getSettings().setDefaultTextEncodingName("UTF-8");

        webView2.setBackgroundColor(Color.TRANSPARENT);
        webView2.setFocusableInTouchMode(false);
        webView2.setFocusable(false);
        webView2.getSettings().setDefaultTextEncodingName("UTF-8");

        webView3.setBackgroundColor(Color.TRANSPARENT);
        webView3.setFocusableInTouchMode(false);
        webView3.setFocusable(false);
        webView3.getSettings().setDefaultTextEncodingName("UTF-8");

        if (method.isNetworkAvailable(getActivity())) {
            if (method.isLogin()) {
                getCompareProducts(method.userId());
            } else {
                progressBar.setVisibility(View.GONE);
                empty_layout.setVisibility(View.VISIBLE);
                empty_msg.setText(getActivity().getResources().getString(R.string.login_msg));
                empty_image.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_baseline_login_24));
                empty_btn.setVisibility(View.VISIBLE);
                empty_btn.setText(getActivity().getResources().getString(R.string.go_to_login));
                empty_btn.setOnClickListener(view1 -> {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                });
            }
        } else {
            progressBar.setVisibility(View.GONE);
            empty_layout.setVisibility(View.VISIBLE);
            method.alertBox(getResources().getString(R.string.no_internet_connection));
        }

        return view;
    }

    @SuppressLint("SetTextI18n")
    @Subscribe
    public void geString(Events.CartItem cartItem){
        if (cartItem.getCart_item().equals("0"))
            cart_items.setVisibility(View.GONE);
        else {
            cart_items.setVisibility(View.VISIBLE);
            cart_items.setText(cartItem.getCart_item());
        }
    }

    private void getCompareProducts(String user_id){

        if (getActivity() != null){
            progressBar.setVisibility(View.VISIBLE);

            JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(getActivity()));
            jsObj.addProperty("user_id", user_id);
            ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
            Call<CompareRP> call = apiService.getCompareData(API.toBase64(jsObj.toString()));
            call.enqueue(new Callback<CompareRP>() {
                @Override
                public void onResponse(Call<CompareRP> call, Response<CompareRP> response) {
                    if (getActivity() != null){
                        try {
                            CompareRP compareRP = response.body();
                            assert compareRP != null;

                            if (compareRP.getStatus().equals("1")){

                                scrollView.setVisibility(View.VISIBLE);

                                if (compareRP.getTotal_products().equals("0")) {
                                    scrollView.setVisibility(View.GONE);
                                    empty_layout.setVisibility(View.VISIBLE);
                                }
                                else if (compareRP.getTotal_products().equals("1")) {
                                    getProduct1(compareRP.getProductLists());
                                    showOnlyOneProduct();
                                }
                                else if (compareRP.getTotal_products().equals("2")) {
                                    getProduct1(compareRP.getProductLists());
                                    getProduct2(compareRP.getProductLists());
                                    showOnlyTwoProLayouts();
                                }
                                else if (compareRP.getTotal_products().equals("3")) {
                                    getProduct1(compareRP.getProductLists());
                                    getProduct2(compareRP.getProductLists());
                                    getProduct3(compareRP.getProductLists());
                                }

                            }
                            else {
                                progressBar.setVisibility(View.GONE);
                                empty_layout.setVisibility(View.VISIBLE);
                                method.alertBox(compareRP.getMessage());
                            }

                        }
                        catch (Exception e){
                            Log.d(ConstantApi.failApi, e.toString());
                            empty_layout.setVisibility(View.VISIBLE);
                            method.alertBox(getResources().getString(R.string.failed_try_again));
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<CompareRP> call, Throwable t) {
                    Log.e(ConstantApi.failApi, t.toString());
                    empty_layout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }
            });
        }
    }

    private void getProduct1(List<CompareProList> compareProLists){

        Glide.with(getActivity()).load(compareProLists.get(0).getProduct_image())
                .placeholder(R.drawable.placeholder_img).into(compare_pro_img1);
        compare_pro_name1.setText(compareProLists.get(0).getProduct_title());
        compare_pro_rate1.setRating(Float.parseFloat(compareProLists.get(0).getRate_avg()));
        if (compareProLists.get(0).getYou_save().equals("0.00"))
            compare_pro_price1.setText(sign + compareProLists.get(0).getProduct_mrp());
        else
            compare_pro_price1.setText(sign + compareProLists.get(0).getProduct_sell_price());
        if (compareProLists.get(0).getProduct_qty().equals("0")) {
            availability1.setText(getActivity().getResources().getString(R.string.out_of_stock));
            availability1.setTextColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
        }
        else {
            availability1.setText(getActivity().getResources().getString(R.string.in_stock));
            availability1.setTextColor(getActivity().getResources().getColor(R.color.green));
        }

        String text = "<html><head>"
                + "<style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/poppins_medium.ttf\");}body{font-family: MyFont;color:#363636;line-height:1.4;}"
                + "a {color:#363636;text-decoration:none;}"
                + "</style></head>"
                + "<body>"
                + compareProLists.get(0).getProduct_desc()
                + "</body></html>";

        webView1.loadDataWithBaseURL(null, text, "text/html", "utf-8", null);

        color_1.setText(compareProLists.get(0).getProduct_color());
        brand_1.setText(compareProLists.get(0).getBrand_name());

        ll_pro_1.setOnClickListener(v ->
                getActivity().startActivity(new Intent(getActivity(), ProductDetailsActivity.class)
                        .putExtra("title",compareProLists.get(0).getProduct_title())
                        .putExtra("product_id", compareProLists.get(0).getId())));

        remove_btn1.setOnClickListener(v ->
                removeFromCompare(compareProLists.get(0).getId()));

        if (compareProLists.get(0).getProduct_qty().equals("0"))
            add_to_cart_btn1.setEnabled(false);

        if (compareProLists.get(0).getProduct_status().equals("0"))
            add_to_cart_btn1.setEnabled(false);

        add_to_cart_btn1.setOnClickListener(v ->
                addToCart(compareProLists.get(0).getId(), method.userId(), compareProLists.get(0).getProduct_default_size()));
    }

    private void getProduct2(List<CompareProList> compareProLists){

        Glide.with(getActivity()).load(compareProLists.get(1).getProduct_image())
                .placeholder(R.drawable.placeholder_img).into(compare_pro_img2);
        compare_pro_name2.setText(compareProLists.get(1).getProduct_title());
        compare_pro_rate2.setRating(Float.parseFloat(compareProLists.get(1).getRate_avg()));
        if (compareProLists.get(1).getYou_save().equals("0.00"))
            compare_pro_price2.setText(sign + compareProLists.get(1).getProduct_mrp());
        else
            compare_pro_price2.setText(sign + compareProLists.get(1).getProduct_sell_price());
        if (compareProLists.get(1).getProduct_qty().equals("0")) {
            availability2.setText(getActivity().getResources().getString(R.string.out_of_stock));
            availability2.setTextColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
        }
        else {
            availability2.setText(getActivity().getResources().getString(R.string.in_stock));
            availability2.setTextColor(getActivity().getResources().getColor(R.color.green));
        }

        String text = "<html><head>"
                + "<style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/poppins_medium.ttf\");}body{font-family: MyFont;color:#363636;line-height:1.4;}"
                + "a {color:#363636;text-decoration:none;}"
                + "</style></head>"
                + "<body>"
                + compareProLists.get(1).getProduct_desc()
                + "</body></html>";

        webView2.loadDataWithBaseURL(null, text, "text/html", "utf-8", null);

        color_2.setText(compareProLists.get(1).getProduct_color());
        brand_2.setText(compareProLists.get(1).getBrand_name());

        ll_pro_2.setOnClickListener(v ->
                getActivity().startActivity(new Intent(getActivity(), ProductDetailsActivity.class)
                        .putExtra("title",compareProLists.get(1).getProduct_title())
                        .putExtra("product_id", compareProLists.get(1).getId())));

        remove_btn2.setOnClickListener(v ->
                removeFromCompare(compareProLists.get(1).getId()));

        if (compareProLists.get(1).getProduct_qty().equals("0"))
            add_to_cart_btn2.setEnabled(false);

        if (compareProLists.get(1).getProduct_status().equals("0"))
            add_to_cart_btn2.setEnabled(false);

        add_to_cart_btn2.setOnClickListener(v ->
                addToCart(compareProLists.get(1).getId(), method.userId(), compareProLists.get(1).getProduct_default_size()));
    }

    private void getProduct3(List<CompareProList> compareProLists){

        Glide.with(getActivity()).load(compareProLists.get(2).getProduct_image())
                .placeholder(R.drawable.placeholder_img).into(compare_pro_img3);
        compare_pro_name3.setText(compareProLists.get(2).getProduct_title());
        compare_pro_rate3.setRating(Float.parseFloat(compareProLists.get(2).getRate_avg()));
        if (compareProLists.get(2).getYou_save().equals("0.00"))
            compare_pro_price3.setText(sign + compareProLists.get(2).getProduct_mrp());
        else
            compare_pro_price3.setText(sign + compareProLists.get(2).getProduct_sell_price());
        if (compareProLists.get(2).getProduct_qty().equals("0")) {
            availability3.setText(getActivity().getResources().getString(R.string.out_of_stock));
            availability3.setTextColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
        }
        else {
            availability3.setText(getActivity().getResources().getString(R.string.in_stock));
            availability3.setTextColor(getActivity().getResources().getColor(R.color.green));
        }

        String text = "<html><head>"
                + "<style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/poppins_medium.ttf\");}body{font-family: MyFont;color:#363636;line-height:1.4;}"
                + "a {color:#363636;text-decoration:none;}"
                + "</style></head>"
                + "<body>"
                + compareProLists.get(2).getProduct_desc()
                + "</body></html>";

        webView3.loadDataWithBaseURL(null, text, "text/html", "utf-8", null);

        color_3.setText(compareProLists.get(2).getProduct_color());
        brand_3.setText(compareProLists.get(2).getBrand_name());

        ll_pro_3.setOnClickListener(v ->
                getActivity().startActivity(new Intent(getActivity(), ProductDetailsActivity.class)
                        .putExtra("title",compareProLists.get(2).getProduct_title())
                        .putExtra("product_id", compareProLists.get(2).getId())));

        remove_btn3.setOnClickListener(v ->
                removeFromCompare(compareProLists.get(2).getId()));

        if (compareProLists.get(2).getProduct_qty().equals("0"))
            add_to_cart_btn3.setEnabled(false);

        if (compareProLists.get(2).getProduct_status().equals("0"))
            add_to_cart_btn3.setEnabled(false);

        add_to_cart_btn3.setOnClickListener(v ->
                addToCart(compareProLists.get(2).getId(), method.userId(), compareProLists.get(2).getProduct_default_size()));
    }



    private void showOnlyOneProduct() {

        showOnlyTwoProLayouts();

        ll_add_pro.setVisibility(View.VISIBLE);
        add_pro_btn.setOnClickListener(v -> getActivity().onBackPressed());
        compare_pro_img2.setVisibility(View.GONE);
        compare_pro_name2.setVisibility(View.GONE);
        compare_pro_rate2.setVisibility(View.GONE);
        compare_pro_price2.setVisibility(View.GONE);
        String text = "<html><head>"
                + "<style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/poppins_medium.ttf\")}body{font-family: MyFont;color: " + getResources().getColor(R.color.grey) + "line-height:1.6}"
                + "a {color:" + getResources().getColor(R.color.grey) + "text-decoration:none}"
                + "</style></head>"
                + "<body><center>"
                + "product description"
                + "</center></body></html>";

        webView2.loadDataWithBaseURL(null, text, "text/html", "utf-8", null);
        availability2.setTextColor(getActivity().getResources().getColor(R.color.dark_grey));
        availability2.setText("Product availability");
        color_2.setText("product color");
        brand_2.setText("product brand");
        add_to_cart_btn2.setVisibility(View.GONE);
        remove_btn2.setVisibility(View.GONE);
    }

    private void showOnlyTwoProLayouts() {

        ll_compare_pro.setWeightSum(2f);
        ll_pro_3.setVisibility(View.GONE);

        ll_compare_availability.setWeightSum(2f);
        ll_availability_3.setVisibility(View.GONE);

        ll_compare_desc.setWeightSum(2f);
        ll_desc_3.setVisibility(View.GONE);

        ll_colors.setWeightSum(2f);
        ll_pro_3.setVisibility(View.GONE);

        ll_compare_pro.setWeightSum(2f);
        ll_color_3.setVisibility(View.GONE);

        ll_brands.setWeightSum(2f);
        ll_brand_3.setVisibility(View.GONE);

        ll_buttons.setWeightSum(2f);
        ll_buttons_3.setVisibility(View.GONE);
    }

    private void addToCart(String product_id, String user_id, String product_size){

        if (getActivity() != null){

            ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.show();
            progressDialog.setMessage(getActivity().getResources().getString(R.string.processing));
            progressDialog.setCancelable(false);

            JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(getActivity()));
            jsObj.addProperty("product_id", product_id);
            jsObj.addProperty("user_id", user_id);
            jsObj.addProperty("product_qty", "1");
            jsObj.addProperty("buy_now", "false");
            jsObj.addProperty("product_size", product_size);
            ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
            Call<AddToCartRP> call = apiService.addToCart(API.toBase64(jsObj.toString()));
            call.enqueue(new Callback<AddToCartRP>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(@NotNull Call<AddToCartRP> call, @NotNull Response<AddToCartRP> response) {

                    if (getActivity() != null){

                        try {

                            AddToCartRP addToCartRP = response.body();

                            assert addToCartRP != null;
                            if (addToCartRP.getStatus().equals("1")) {

                                if (addToCartRP.getSuccess().equals("1")) {

                                    Snackbar.make(view, addToCartRP.getMsg(), Snackbar.LENGTH_LONG)
                                            .setBackgroundTint(getActivity().getResources().getColor(R.color.snackBar_color))
                                            .setTextColor(getActivity().getResources().getColor(R.color.white))
                                            .setActionTextColor(getActivity().getResources().getColor(R.color.white))
                                            .setAction("View Cart", v1 -> {
                                                getActivity().startActivity(new Intent(getActivity(), CartActivity.class));
                                            }).show();

                                    Events.CartItem cartItem = new Events.CartItem(addToCartRP.getTotal_item());
                                    GlobalBus.getBus().post(cartItem);
                                }
                                else {
                                    Toast.makeText(getActivity(), addToCartRP.getMsg(), Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                method.alertBox(addToCartRP.getMessage());
                            }

                        } catch (Exception e) {
                            Log.d(ConstantApi.exceptionError, e.toString());
                            method.alertBox(getActivity().getResources().getString(R.string.failed_try_again));
                        }

                        progressDialog.dismiss();

                    }
                }

                @Override
                public void onFailure(@NotNull Call<AddToCartRP> call, @NotNull Throwable t) {
                    // Log error here since request failed
                    Log.e(ConstantApi.failApi, t.toString());
                    progressDialog.dismiss();
                    method.alertBox(getActivity().getResources().getString(R.string.failed_try_again));
                }
            });

        }

    }

    private void removeFromCompare(String product_id){

        if (getActivity() != null) {

            progressDialog.show();
            progressDialog.setMessage(getResources().getString(R.string.loading));
            progressDialog.setCancelable(false);

            JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(getActivity()));
            jsObj.addProperty("user_id", method.userId());
            jsObj.addProperty("product_id", product_id);
            ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
            Call<DataRP> call = apiService.addRemoveCompare(API.toBase64(jsObj.toString()));
            call.enqueue(new Callback<DataRP>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(@NotNull Call<DataRP> call, @NotNull Response<DataRP> response) {

                    try {

                        DataRP dataRP = response.body();
                        assert dataRP != null;

                        if (dataRP.getStatus().equals("1")) {

                            if (dataRP.getSuccess().equals("1")) {

                                Snackbar.make(view, dataRP.getMsg(), Snackbar.LENGTH_LONG)
                                        .setBackgroundTint(getActivity().getResources().getColor(R.color.snackBar_color))
                                        .setTextColor(getActivity().getResources().getColor(R.color.white)).show();

                                scrollView.setVisibility(View.GONE);
                                getCompareProducts(method.userId());

                            }
                            else {
                                Toast.makeText(getActivity(), dataRP.getMsg(), Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            method.alertBox(dataRP.getMessage());
                        }

                    } catch (Exception e) {
                        Log.d(ConstantApi.exceptionError, e.toString());
                        method.alertBox(getResources().getString(R.string.failed_try_again));
                    }

                    progressDialog.dismiss();

                }

                @Override
                public void onFailure(@NotNull Call<DataRP> call, @NotNull Throwable t) {
                    // Log error here since request failed
                    Log.e(ConstantApi.failApi, t.toString());
                    progressDialog.dismiss();
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }
            });
        }

    }

    private void initialize(View view) {

        cart_items = getActivity().findViewById(R.id.order_count);

        empty_layout = view.findViewById(R.id.empty_layout);
        empty_image = view.findViewById(R.id.empty_image);
        empty_msg = view.findViewById(R.id.empty_msg);
        empty_btn = view.findViewById(R.id.empty_btn);

        scrollView = view.findViewById(R.id.scrollview_compare);
        progressBar = view.findViewById(R.id.progressBar_compare);

        ll_add_pro = view.findViewById(R.id.ll_add_pro);
        ll_compare_pro = view.findViewById(R.id.ll_compare_pro);
        ll_pro_1 = view.findViewById(R.id.ll_pro_1);
        ll_pro_2 = view.findViewById(R.id.ll_pro_2);
        ll_pro_3 = view.findViewById(R.id.ll_pro_3);
        ll_compare_availability = view.findViewById(R.id.ll_compare_availability);
        ll_availability_3 = view.findViewById(R.id.ll_availability_3);
        ll_compare_desc = view.findViewById(R.id.ll_compare_desc);
        ll_desc_3 = view.findViewById(R.id.ll_desc_3);
        ll_colors = view.findViewById(R.id.ll_colors);
        ll_color_3 = view.findViewById(R.id.ll_color_3);
        ll_brands = view.findViewById(R.id.ll_brands);
        ll_brand_3 = view.findViewById(R.id.ll_brand_3);
        ll_buttons = view.findViewById(R.id.ll_buttons);
        ll_buttons_3 = view.findViewById(R.id.ll_buttons_3);

        compare_pro_img1 = view.findViewById(R.id.compare_pro_img1);
        compare_pro_img2 = view.findViewById(R.id.compare_pro_img2);
        compare_pro_img3 = view.findViewById(R.id.compare_pro_img3);
        compare_pro_name1 = view.findViewById(R.id.compare_pro_name1);
        compare_pro_name2 = view.findViewById(R.id.compare_pro_name2);
        compare_pro_name3 = view.findViewById(R.id.compare_pro_name3);
        compare_pro_rate1 = view.findViewById(R.id.compare_pro_rate1);
        compare_pro_rate2 = view.findViewById(R.id.compare_pro_rate2);
        compare_pro_rate3 = view.findViewById(R.id.compare_pro_rate3);
        compare_pro_price1 = view.findViewById(R.id.compare_pro_price1);
        compare_pro_price2 = view.findViewById(R.id.compare_pro_price2);
        compare_pro_price3 = view.findViewById(R.id.compare_pro_price3);
        availability1 = view.findViewById(R.id.availability1);
        availability2 = view.findViewById(R.id.availability2);
        availability3 = view.findViewById(R.id.availability3);
        webView1 = view.findViewById(R.id.desc_webView1);
        webView2 = view.findViewById(R.id.desc_webView2);
        webView3 = view.findViewById(R.id.desc_webView3);
        color_1 = view.findViewById(R.id.color_1);
        color_2 = view.findViewById(R.id.color_2);
        color_3 = view.findViewById(R.id.color_3);
        brand_1 = view.findViewById(R.id.brand_1);
        brand_2 = view.findViewById(R.id.brand_2);
        brand_3 = view.findViewById(R.id.brand_3);
        add_pro_btn = view.findViewById(R.id.add_pro_btn);
        add_to_cart_btn1 = view.findViewById(R.id.add_to_cart_btn1);
        add_to_cart_btn2 = view.findViewById(R.id.add_to_cart_btn2);
        add_to_cart_btn3 = view.findViewById(R.id.add_to_cart_btn3);
        remove_btn1 = view.findViewById(R.id.remove_btn1);
        remove_btn2 = view.findViewById(R.id.remove_btn2);
        remove_btn3 = view.findViewById(R.id.remove_btn3);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Unregister the registered event.
        GlobalBus.getBus().unregister(this);
    }

}