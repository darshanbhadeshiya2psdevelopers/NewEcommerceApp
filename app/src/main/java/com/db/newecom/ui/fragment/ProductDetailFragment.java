package com.db.newecom.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.db.newecom.R;
import com.db.newecom.Response.AddToCartRP;
import com.db.newecom.Response.DataRP;
import com.db.newecom.Response.FavouriteRP;
import com.db.newecom.Response.IsAddRP;
import com.db.newecom.Response.ProDetailRP;
import com.db.newecom.Response.RRSubmitRP;
import com.db.newecom.Utills.API;
import com.db.newecom.Utills.ConstantApi;
import com.db.newecom.Utills.Events;
import com.db.newecom.Utills.GlobalBus;
import com.db.newecom.Utills.Method;
import com.db.newecom.adapters.ProViewAdapter;
import com.db.newecom.adapters.ProductColorAdapter;
import com.db.newecom.adapters.ProductSizeAdapter;
import com.db.newecom.adapters.RelatedProAdapter;
import com.db.newecom.adapters.ReviewAdapter;
import com.db.newecom.ui.activity.Add_Address_Activity;
import com.db.newecom.ui.activity.CartActivity;
import com.db.newecom.ui.activity.LoginActivity;
import com.db.newecom.ui.activity.OrderSummaryActivity;
import com.db.newecom.ui.activity.ReviewsActivity;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductDetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "title";
    private static final String ARG_PARAM2 = "product_id";
    private static final String ARG_PARAM3 = "pro_size_position";

    // TODO: Rename and change types of parameters
    private String title, product_id, sign, tagData = "data_app", size, review_msg;

    private Method method;
    private ProgressBar progressBar, rate_progress_1, rate_progress_2, rate_progress_3,
            rate_progress_4, rate_progress_5;
    private ProgressDialog progressDialog;
    private int columnWidth, productSize_position = 0;
    private ScrollView scrollView;
    private MaterialCardView bottom_btns_cardview;
    private ViewPager viewpager_product_img;
    private ImageButton product_save_btn, product_share_btn;
    private RatingBar pro_detail_ratingbar, rate_dialog_ratingbar;
    private RecyclerView rv_colors_pro_detail, rv_size_pro_detail, rv_related_pro_detail, rv_review_pro_detail;
    private WebView webView_feature_pro_detail;
    private ProductColorAdapter productColorAdapter;
    private ProductSizeAdapter productSizeAdapter;
    private RelatedProAdapter relatedProAdapter;
    private ReviewAdapter reviewAdapter;
    private TextView pro_detail_name, pro_detail_desc, pro_detail_ratingcount, pro_detail_sell_price,
            pro_detail_original_price, pro_detail_discount, pro_detail_color, add_to_cart_btn, buy_now_btn,
            view_all_review_txt, pro_detail_overall_rate, pro_status_msg, rate_5_count, rate_4_count, rate_3_count,
            rate_2_count, rate_1_count, rate_dialog_pro_name, rate_dialog_pro_desc, cart_items;
    private RelativeLayout empty_layout, rel_indicator_pro, rl_add_to_compare, rl_add_review;
    private LinearLayout ll_related_pro_detail, ll_colors, ll_size;
    private View view;
    private Dialog dialog;
    private EditText et_rate_dialog_msg;
    private ImageView rate_dialog_pro_image;
    private Button rate_dialog_btn;
    private int getRating;

    public ProductDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductDetailFragment newInstance(String param1, String param2, int param3) {
        ProductDetailFragment fragment = new ProductDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putInt(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_PARAM1);
            product_id = getArguments().getString(ARG_PARAM2);
            productSize_position = getArguments().getInt(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_product_detail, container, false);

        GlobalBus.getBus().register(this);
        method = new Method(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        sign = ConstantApi.currency + " ";

        init(view);

        columnWidth = (method.getScreenWidth());
        viewpager_product_img.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, columnWidth));

        if (method.isNetworkAvailable(getActivity())) {
            if (method.isLogin()) {
                detail(method.userId(), product_id);
            } else {
                detail("0", product_id);
            }
        } else {
            progressBar.setVisibility(View.GONE);
            scrollView.setVisibility(View.GONE);
            empty_layout.setVisibility(View.VISIBLE);
            bottom_btns_cardview.setVisibility(View.GONE);
            method.alertBox(getResources().getString(R.string.no_internet_connection));
        }

        view_all_review_txt.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), ReviewsActivity.class)
                        .putExtra("type", "pro review")
                        .putExtra("product_id", product_id)));

        rl_add_to_compare.setOnClickListener(v -> {
            if (method.isNetworkAvailable(getActivity())){
                if (method.isLogin()) {
                    addToCompare(method.userId(), product_id);
                } else {
                    Method.login(true);
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            } else {
                progressBar.setVisibility(View.GONE);
                scrollView.setVisibility(View.GONE);
                empty_layout.setVisibility(View.VISIBLE);
                bottom_btns_cardview.setVisibility(View.GONE);
                method.alertBox(getResources().getString(R.string.no_internet_connection));
            }
        });

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.product_rating_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        rate_dialog_pro_image = dialog.findViewById(R.id.rate_pro_img);
        rate_dialog_pro_name = dialog.findViewById(R.id.rate_pro_name);
        rate_dialog_pro_desc = dialog.findViewById(R.id.rate_pro_desc);
        rate_dialog_ratingbar = dialog.findViewById(R.id.rating_bar_of_dialog);
        et_rate_dialog_msg = dialog.findViewById(R.id.et_review_msg);
        rate_dialog_btn = dialog.findViewById(R.id.rate_submit_btn);

        return view;
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            Log.d(tagData, "");
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            Log.d(tagData, "");
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            Log.d(tagData, "");
        }
    };

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

    @SuppressLint("SetTextI18n")
    @Subscribe
    public void geString(Events.RatingUpdate ratingUpdate){

        rate_dialog_ratingbar.setRating(ratingUpdate.getRate());

        et_rate_dialog_msg.setText(ratingUpdate.getRate_msg());

        et_rate_dialog_msg.requestFocus();

        scrollView.setVisibility(View.GONE);
        bottom_btns_cardview.setVisibility(View.GONE);

        if (method.isNetworkAvailable(getActivity())) {
            if (method.isLogin()) {
                detail(method.userId(), product_id);
            } else {
                detail("0", product_id);
            }
        } else {
            progressBar.setVisibility(View.GONE);
            scrollView.setVisibility(View.GONE);
            empty_layout.setVisibility(View.VISIBLE);
            bottom_btns_cardview.setVisibility(View.GONE);
            method.alertBox(getResources().getString(R.string.no_internet_connection));
        }
    }

    private void detail(String userId, String product_id) {

        if (getActivity() != null) {

            progressBar.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
            bottom_btns_cardview.setVisibility(View.GONE);

            JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(getActivity()));
            jsObj.addProperty("user_id", userId);
            jsObj.addProperty("id", product_id);
            ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
            Call<ProDetailRP> call = apiService.getProDetail(API.toBase64(jsObj.toString()));
            call.enqueue(new Callback<ProDetailRP>() {
                @Override
                public void onResponse(Call<ProDetailRP> call, Response<ProDetailRP> response) {

                    if (getActivity() != null){

                        try {

                            ProDetailRP proDetailRP = response.body();
                            assert proDetailRP != null;

                            if (proDetailRP.getStatus().equals("1")){

                                if (proDetailRP.getSuccess().equals("1")){

                                    scrollView.setVisibility(View.VISIBLE);
                                    bottom_btns_cardview.setVisibility(View.VISIBLE);

                                    if (!proDetailRP.getCart_items().equals("0")) {
                                        cart_items.setVisibility(View.VISIBLE);
                                        cart_items.setText(proDetailRP.getCart_items());
                                    }

                                    if (proDetailRP.getProduct_qty().equals("0")){
                                        add_to_cart_btn.setVisibility(View.GONE);
                                        buy_now_btn.setVisibility(View.GONE);
                                        pro_status_msg.setVisibility(View.VISIBLE);
                                        pro_status_msg.setText(getActivity().getResources().getString(R.string.out_of_stock));
                                    }

                                    if (proDetailRP.getProduct_status().equals("0")) {
                                        add_to_cart_btn.setVisibility(View.GONE);
                                        buy_now_btn.setVisibility(View.GONE);
                                        pro_status_msg.setVisibility(View.VISIBLE);
                                        pro_status_msg.setText(proDetailRP.getProduct_status_lbl());
                                    }

                                    if (proDetailRP.getIs_favorite().equals("1")) {
                                        product_save_btn.setImageTintList(ColorStateList.valueOf(getActivity().getResources().getColor(R.color.red)));
                                    } else {
                                        product_save_btn.setImageTintList(ColorStateList.valueOf(getActivity().getResources().getColor(R.color.grey)));
                                    }

                                    product_save_btn.setOnClickListener(v -> {

                                        if (method.isNetworkAvailable(getActivity())){
                                            if (method.isLogin())
                                                favourite(userId, proDetailRP.getId());
                                            else {
                                                Method.login(true);
                                                startActivity(new Intent(getActivity(), LoginActivity.class));
                                            }
                                        }else
                                            Toast.makeText(getActivity(),
                                                    getActivity().getResources().getString(R.string.no_internet_connection),
                                                    Toast.LENGTH_SHORT).show();

                                    });

                                    pro_detail_name.setText(proDetailRP.getProduct_title());
                                    pro_detail_desc.setText(proDetailRP.getProduct_desc());
                                    pro_detail_ratingbar.setRating(Float.parseFloat(proDetailRP.getRate_avg()));
                                    pro_detail_ratingcount.setText(proDetailRP.getTotal_rate());
                                    pro_detail_sell_price.setText(sign + proDetailRP.getProduct_sell_price());
                                    pro_detail_color.setText(" " + proDetailRP.getColor_code());

                                    pro_detail_overall_rate.setText(proDetailRP.getOver_all_rate());
                                    rate_5_count.setText(proDetailRP.getFive_rate_count());
                                    rate_4_count.setText(proDetailRP.getFour_rate_count());
                                    rate_3_count.setText(proDetailRP.getThree_rate_count());
                                    rate_2_count.setText(proDetailRP.getTwo_rate_count());
                                    rate_1_count.setText(proDetailRP.getOne_rate_count());

                                    rate_progress_1.setProgress(Integer.parseInt(proDetailRP.getFive_rate_per()));
                                    rate_progress_2.setProgress(Integer.parseInt(proDetailRP.getFour_rate_per()));
                                    rate_progress_3.setProgress(Integer.parseInt(proDetailRP.getThree_rate_per()));
                                    rate_progress_4.setProgress(Integer.parseInt(proDetailRP.getTwo_rate_per()));
                                    rate_progress_5.setProgress(Integer.parseInt(proDetailRP.getOne_rate_per()));

                                    if (proDetailRP.getYou_save().equals("0.00")) {
                                        pro_detail_original_price.setVisibility(View.GONE);
                                        pro_detail_discount.setVisibility(View.GONE);
                                    } else {
                                        pro_detail_discount.setText(proDetailRP.getYou_save_per());
                                        pro_detail_original_price.setText(sign + proDetailRP.getProduct_mrp());
                                        pro_detail_original_price.setPaintFlags(pro_detail_original_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                    }

                                    webView_feature_pro_detail.setBackgroundColor(Color.TRANSPARENT);
                                    webView_feature_pro_detail.setFocusableInTouchMode(false);
                                    webView_feature_pro_detail.setFocusable(false);
                                    webView_feature_pro_detail.getSettings().setDefaultTextEncodingName("UTF-8");
                                    webView_feature_pro_detail.getSettings().setJavaScriptEnabled(true);
                                    webView_feature_pro_detail.setNestedScrollingEnabled(false);

                                    String mimeType = "text/html";
                                    String encoding = "utf-8";

                                    String table_css = "";
                                    if (proDetailRP.getProduct_features().contains("<table")) {
                                        table_css = "file:///android_asset/css/table.css";
                                    }

                                    String text_feature = "<html><head>"
                                            + "<style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/poppins_medium.ttf\")}body{font-family: MyFont;color: " + getResources().getColor(R.color.dark_grey) + "line-height:1.6}"
                                            + "a {color:" + getResources().getColor(R.color.dark_grey) + "text-decoration:none}"
                                            + "</style>"
                                            + "<link rel=\"stylesheet\" href=\"" + table_css + "\">"
                                            + "</head>"
                                            + "<body>"
                                            + "<div class=\"description-content\">"
                                            + proDetailRP.getProduct_features()
                                            + "</div>"
                                            + "</body></html>";

                                    webView_feature_pro_detail.loadDataWithBaseURL("blarg://ignored", text_feature, mimeType, encoding, "");

                                    if (proDetailRP.getProImageLists().size() != 0) {
                                        viewpager_product_img.addOnPageChangeListener(viewPagerPageChangeListener);
                                        ProViewAdapter proViewAdapter = new ProViewAdapter(getActivity(), "pro_image",proDetailRP.getProImageLists());
                                        viewpager_product_img.setAdapter(proViewAdapter);
                                    }

                                    if (proDetailRP.getProductLists().size() != 0) {

                                        ll_related_pro_detail.setVisibility(View.VISIBLE);
                                        relatedProAdapter = new RelatedProAdapter(getActivity(), proDetailRP.getProductLists());
                                        rv_related_pro_detail.setAdapter(relatedProAdapter);

                                    } else {
                                        ll_related_pro_detail.setVisibility(View.GONE);
                                    }

                                    if (proDetailRP.getIs_color().equals("1")){
                                        if (proDetailRP.getProColorLists().size() != 0){
                                            ll_colors.setVisibility(View.VISIBLE);
                                            productColorAdapter = new ProductColorAdapter(getActivity(), proDetailRP.getProColorLists(), proDetailRP.getId());
                                            rv_colors_pro_detail.setAdapter(productColorAdapter);
                                        }
                                        else {
                                            ll_colors.setVisibility(View.GONE);
                                        }
                                    }
                                    else {
                                        ll_colors.setVisibility(View.GONE);
                                    }

                                    if (proDetailRP.getIs_size().equals("1")){
                                        if (proDetailRP.getProSizeLists().size() != 0){
                                            ll_size.setVisibility(View.VISIBLE);
                                            productSizeAdapter = new ProductSizeAdapter(getActivity(), proDetailRP.getProSizeLists(), productSize_position, title, product_id);
                                            rv_size_pro_detail.setAdapter(productSizeAdapter);

                                            size = proDetailRP.getProSizeLists().get(productSize_position).getProduct_size();

                                        }
                                        else {
                                            ll_size.setVisibility(View.GONE);
                                        }
                                    }
                                    else {
                                        ll_size.setVisibility(View.GONE);
                                    }

                                    if (proDetailRP.getProReviewLists().size() != 0) {

                                        rv_review_pro_detail.setVisibility(View.VISIBLE);
                                        rv_review_pro_detail.setNestedScrollingEnabled(false);

                                        reviewAdapter = new ReviewAdapter(getActivity(), proDetailRP.getProReviewLists());
                                        rv_review_pro_detail.setAdapter(reviewAdapter);

                                        view_all_review_txt.setText(getResources().getString(R.string.view_all) + " " + "(" + proDetailRP.getProReviewLists().size() + ")");

                                    } else {
                                        view_all_review_txt.setVisibility(View.GONE);
                                        rv_review_pro_detail.setVisibility(View.GONE);
                                    }

                                    product_share_btn.setOnClickListener(v -> {
                                        Intent intent = new Intent(Intent.ACTION_SEND);
                                        intent.setType("text/plain");
                                        intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                                        intent.putExtra(Intent.EXTRA_TEXT, proDetailRP.getShare_link());
                                        startActivity(Intent.createChooser(intent, getResources().getString(R.string.choose_one)));
                                    });

                                    add_to_cart_btn.setOnClickListener(v -> {
                                        if (method.isNetworkAvailable(getActivity())) {
                                            if (method.isLogin()) {
                                                addToCart(proDetailRP.getId(), userId, size);
                                            }
                                            else {
                                                Method.login(true);
                                                startActivity(new Intent(getActivity(), LoginActivity.class));
                                            }
                                        } else {
                                            method.alertBox(getResources().getString(R.string.no_internet_connection));
                                        }
                                    });

                                    buy_now_btn.setOnClickListener(v -> {

                                        if (method.isNetworkAvailable(getActivity())) {
                                            if (method.isLogin()) {
                                                isAddress(userId, proDetailRP.getId(), size);
                                            } else {
                                                Method.login(true);
                                                startActivity(new Intent(getActivity(), LoginActivity.class));
                                            }
                                        } else {
                                            method.alertBox(getResources().getString(R.string.no_internet_connection));
                                        }

                                    });

                                    Glide.with(getActivity()).load(proDetailRP.getProduct_image())
                                            .placeholder(R.drawable.placeholder_img)
                                            .into(rate_dialog_pro_image);

                                    rate_dialog_pro_name.setText(proDetailRP.getProduct_title());
                                    rate_dialog_pro_desc.setText(proDetailRP.getProduct_desc());

                                    rl_add_review.setOnClickListener(v -> dialog.show());

                                    rate_dialog_btn.setOnClickListener(v ->
                                        submitRatingReview(method.userId(), product_id));

                                }
                                else {
                                    empty_layout.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                    scrollView.setVisibility(View.GONE);
                                    bottom_btns_cardview.setVisibility(View.GONE);
                                    method.alertBox(proDetailRP.getMsg());
                                }

                            }
                            else {
                                empty_layout.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                scrollView.setVisibility(View.GONE);
                                bottom_btns_cardview.setVisibility(View.GONE);
                                method.alertBox(proDetailRP.getMessage());
                            }

                        }
                        catch (Exception e){
                            Log.d(ConstantApi.exceptionError, e.toString());
                            empty_layout.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            scrollView.setVisibility(View.GONE);
                            bottom_btns_cardview.setVisibility(View.GONE);
                            method.alertBox(getResources().getString(R.string.failed_try_again));
                        }

                    }
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<ProDetailRP> call, Throwable t) {
                    Log.e(ConstantApi.failApi, t.toString());
                    empty_layout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    scrollView.setVisibility(View.GONE);
                    bottom_btns_cardview.setVisibility(View.GONE);
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }
            });

        }

    }

    private void favourite(String user_id, String product_id){

        progressDialog.show();
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        if (getActivity() != null) {

            JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(getActivity()));
            jsObj.addProperty("product_id", product_id);
            jsObj.addProperty("user_id", user_id);
            ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
            Call<FavouriteRP> call = apiService.addToFavourite(API.toBase64(jsObj.toString()));
            call.enqueue(new Callback<FavouriteRP>() {
                @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
                @Override
                public void onResponse(@NotNull Call<FavouriteRP> call, @NotNull Response<FavouriteRP> response) {

                    try {

                        FavouriteRP favouriteRP = response.body();

                        assert favouriteRP != null;
                        if (favouriteRP.getStatus().equals("1")) {

                            if (favouriteRP.getSuccess().equals("1")) {

                                if (favouriteRP.getIs_favorite().equals("1")) {
                                    product_save_btn.setImageTintList(ColorStateList.valueOf(getActivity().getResources().getColor(R.color.red)));
                                } else {
                                    product_save_btn.setImageTintList(ColorStateList.valueOf(getActivity().getResources().getColor(R.color.grey)));
                                }
                            }

                            Toast.makeText(getActivity(), favouriteRP.getMsg(), Toast.LENGTH_SHORT).show();

                        } else {
                            method.alertBox(favouriteRP.getMessage());
                        }

                    } catch (Exception e) {
                        Log.d(ConstantApi.exceptionError, e.toString());
                        method.alertBox(getResources().getString(R.string.failed_try_again));
                    }

                    progressDialog.dismiss();

                }

                @Override
                public void onFailure(@NotNull Call<FavouriteRP> call, @NotNull Throwable t) {
                    // Log error here since request failed
                    Log.e(ConstantApi.failApi, t.toString());
                    progressDialog.dismiss();
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }
            });

        }

    }

    private void addToCompare(String user_id, String product_id){

        if (getActivity() != null) {

            progressDialog.show();
            progressDialog.setMessage(getResources().getString(R.string.loading));
            progressDialog.setCancelable(false);

            JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(getActivity()));
            jsObj.addProperty("user_id", user_id);
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

    private void addToCart(String product_id, String user_id, String product_size){

        if (getActivity() != null) {

            progressDialog.show();
            progressDialog.setMessage(getResources().getString(R.string.loading));
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
                        method.alertBox(getResources().getString(R.string.failed_try_again));
                    }

                    progressDialog.dismiss();

                }

                @Override
                public void onFailure(@NotNull Call<AddToCartRP> call, @NotNull Throwable t) {
                    // Log error here since request failed
                    Log.e(ConstantApi.failApi, t.toString());
                    progressDialog.dismiss();
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }
            });
        }

    }

    private void isAddress(String user_id, String product_id, String product_size){

        progressDialog.show();
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        if (getActivity() != null) {

            JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(getActivity()));
            jsObj.addProperty("user_id", user_id);
            ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
            Call<IsAddRP> call = apiService.isAddress(API.toBase64(jsObj.toString()));
            call.enqueue(new Callback<IsAddRP>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(@NotNull Call<IsAddRP> call, @NotNull Response<IsAddRP> response) {

                    try {

                        IsAddRP isAddRP = response.body();
                        assert isAddRP != null;

                        if (isAddRP.getStatus().equals("1")) {
                            if (isAddRP.getIs_address_avail().equals("1")) {
                                startActivity(new Intent(getActivity(), OrderSummaryActivity.class)
                                        .putExtra("type", "buy_now")
                                        .putExtra("product_size", product_size)
                                        .putExtra("product_id", product_id));

                                //Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                            } else {
                                //add to address then after go to the order summary page
                                startActivity(new Intent(getActivity(), Add_Address_Activity.class)
                                        .putExtra("type", "check_address")
                                        .putExtra("type_order", "buy_now")
                                        .putExtra("product_size", product_size)
                                        .putExtra("product_id", product_id));
                                Toast.makeText(getActivity(), "no Addresses", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            method.alertBox(isAddRP.getMessage());
                        }

                    } catch (Exception e) {
                        Log.d(ConstantApi.exceptionError, e.toString());
                        method.alertBox(getResources().getString(R.string.failed_try_again));
                    }

                    progressDialog.dismiss();

                }

                @Override
                public void onFailure(@NotNull Call<IsAddRP> call, @NotNull Throwable t) {
                    // Log error here since request failed
                    Log.e(ConstantApi.failApi, t.toString());
                    progressDialog.dismiss();
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }
            });
        }
    }

    public void submitRatingReview(String user_id, String product_id){

        getRating = (int) rate_dialog_ratingbar.getRating();

        review_msg = et_rate_dialog_msg.getText().toString();

        if (getRating == 0){
            Toast.makeText(getActivity(),
                    getActivity().getResources().getString(R.string.please_give_rating),
                    Toast.LENGTH_SHORT).show();
        }
        else if (review_msg.equals("")){
            Toast.makeText(getActivity(),
                    getActivity().getResources().getString(R.string.please_enter_review),
                    Toast.LENGTH_SHORT).show();
        }
        else {
            progressDialog.show();
            progressDialog.setMessage(getResources().getString(R.string.loading));
            progressDialog.setCancelable(false);

            if (getActivity() != null){

                JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(getActivity()));
                jsObj.addProperty("user_id", user_id);
                jsObj.addProperty("product_id", product_id);
                jsObj.addProperty("review_desc", review_msg);
                jsObj.addProperty("rate", getRating);
                ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
                Call<RRSubmitRP> call = apiService.submitRatingReview(API.toBase64(jsObj.toString()));
                call.enqueue(new Callback<RRSubmitRP>() {
                    @Override
                    public void onResponse(Call<RRSubmitRP> call, Response<RRSubmitRP> response) {

                        try {
                            RRSubmitRP rrSubmitRP = response.body();
                            assert rrSubmitRP != null;

                            if (rrSubmitRP.getStatus().equals("1")) {

                                if (rrSubmitRP.getSuccess().equals("1")) {

                                    Events.RatingUpdate ratingUpdate = new Events.RatingUpdate(getRating, review_msg);
                                    GlobalBus.getBus().post(ratingUpdate);

                                    dialog.dismiss();

                                }

                                Toast.makeText(getActivity(), rrSubmitRP.getMsg(), Toast.LENGTH_SHORT).show();

                            } else {
                                dialog.dismiss();
                                method.alertBox(rrSubmitRP.getMessage());
                            }

                        } catch (Exception e) {
                            Log.d(ConstantApi.exceptionError, e.toString());
                            dialog.dismiss();
                            method.alertBox(getResources().getString(R.string.failed_try_again));
                        }

                        progressDialog.dismiss();

                    }

                    @Override
                    public void onFailure(Call<RRSubmitRP> call, Throwable t) {
                        Log.e(ConstantApi.failApi, t.toString());
                        dialog.dismiss();
                        progressDialog.dismiss();
                        method.alertBox(getResources().getString(R.string.failed_try_again));
                    }
                });

            }
        }
    }

    private void init(View view) {

        progressBar = view.findViewById(R.id.progressBar_proDetail);
        rate_progress_1 = view.findViewById(R.id.rate_progress_1);
        rate_progress_2 = view.findViewById(R.id.rate_progress_2);
        rate_progress_3 = view.findViewById(R.id.rate_progress_3);
        rate_progress_4 = view.findViewById(R.id.rate_progress_4);
        rate_progress_5 = view.findViewById(R.id.rate_progress_5);
        scrollView = view.findViewById(R.id.main_scrollview);
        bottom_btns_cardview = view.findViewById(R.id.bottom_btns_cardview);
        viewpager_product_img = view.findViewById(R.id.viewpager_product_img);
        product_save_btn = view.findViewById(R.id.product_save_btn);
        product_share_btn = view.findViewById(R.id.product_share_btn);
        pro_detail_ratingbar = view.findViewById(R.id.pro_detail_ratingbar);
        webView_feature_pro_detail = view.findViewById(R.id.webView_feature_pro_detail);
        pro_detail_name = view.findViewById(R.id.pro_detail_name);
        pro_detail_desc = view.findViewById(R.id.pro_detail_desc);
        pro_detail_ratingcount = view.findViewById(R.id.pro_detail_ratingcount);
        pro_detail_sell_price = view.findViewById(R.id.pro_detail_sell_price);
        pro_detail_discount = view.findViewById(R.id.pro_detail_discount);
        pro_detail_color = view.findViewById(R.id.pro_detail_color);
        pro_detail_overall_rate = view.findViewById(R.id.pro_detail_overall_rate);
        pro_status_msg = view.findViewById(R.id.pro_status_msg);
        rate_5_count = view.findViewById(R.id.rate_5_count);
        rate_4_count = view.findViewById(R.id.rate_4_count);
        rate_3_count = view.findViewById(R.id.rate_3_count);
        rate_2_count = view.findViewById(R.id.rate_2_count);
        rate_1_count = view.findViewById(R.id.rate_1_count);
        empty_layout = view.findViewById(R.id.empty_layout);
        rel_indicator_pro = view.findViewById(R.id.rel_indicator_pro);
        ll_colors = view.findViewById(R.id.ll_colors);
        ll_size = view.findViewById(R.id.ll_size);
        ll_related_pro_detail = view.findViewById(R.id.ll_related_pro_detail);
        rv_colors_pro_detail = view.findViewById(R.id.rv_colors_pro_detail);
        rv_size_pro_detail = view.findViewById(R.id.rv_size_pro_detail);
        rv_related_pro_detail = view.findViewById(R.id.rv_related_pro_detail);
        rv_review_pro_detail = view.findViewById(R.id.rv_review_pro_detail);
        pro_detail_original_price = view.findViewById(R.id.pro_detail_original_price);
        add_to_cart_btn = view.findViewById(R.id.add_to_cart_btn);
        buy_now_btn = view.findViewById(R.id.buy_now_btn);
        view_all_review_txt = view.findViewById(R.id.view_all_review_txt);
        rl_add_to_compare = view.findViewById(R.id.rl_add_to_compare);
        rl_add_review = view.findViewById(R.id.rl_add_review);

        cart_items = getActivity().findViewById(R.id.order_count);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Unregister the registered event.
        GlobalBus.getBus().unregister(this);
    }
}