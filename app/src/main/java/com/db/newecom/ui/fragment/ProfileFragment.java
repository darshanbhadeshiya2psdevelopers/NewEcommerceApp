package com.db.newecom.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.db.newecom.Api.ApiClient;
import com.db.newecom.Api.ApiInterface;
import com.db.newecom.R;
import com.db.newecom.Response.MyRatingReviewRP;
import com.db.newecom.Response.ProductRP;
import com.db.newecom.Response.UserProfileRP;
import com.db.newecom.Utills.API;
import com.db.newecom.Utills.Method;
import com.db.newecom.adapters.HomeMyOrdersAdapter;
import com.db.newecom.adapters.MyReviewAdapter;
import com.db.newecom.adapters.WishlistItemAdapter;
import com.db.newecom.ui.activity.LoginActivity;
import com.db.newecom.ui.activity.MainActivity;
import com.db.newecom.ui.activity.ReviewsActivity;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Method method;
    private RelativeLayout empty_layout;
    private LinearLayout empty_orders, empty_wishlist, empty_reviews;
    private ImageView empty_image, profile_type;
    private ProgressBar progressBar;
    private ScrollView main_scrollview;
    private CircleImageView user_img;
    private TextView empty_msg, user_name, user_email, user_mobile, all_order_txt, all_wishlist_txt, all_review_txt,
            all_address_txt, all_bank_txt, add_address_txt, add_bank_account_txt, address_user_name,
            address_type, address_mobile, address, bank_name, ifsc, bank_account_no, bank_account_type,
            bank_holder_name, bank_holder_mobile, bank_holder_email;
    private Button empty_btn, edit_profile_btn, logout_btn;
    private RecyclerView rv_my_order, rv_my_wishlist, rv_my_review;
    private HomeMyOrdersAdapter myOrdersAdapter;
    private WishlistItemAdapter wishlistItemAdapter;
    private MyReviewAdapter myReviewAdapter;
    private MaterialCardView item_address, item_bank_account;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        method = new Method(getActivity());

        empty_layout = view.findViewById(R.id.empty_layout);
        main_scrollview = view.findViewById(R.id.main_scrollview);
        empty_image = view.findViewById(R.id.empty_image);
        empty_msg = view.findViewById(R.id.empty_msg);
        empty_btn = view.findViewById(R.id.empty_btn);
        empty_orders = view.findViewById(R.id.empty_orders);
        empty_wishlist = view.findViewById(R.id.empty_wishlist);
        empty_reviews = view.findViewById(R.id.empty_reviews);

        progressBar = view.findViewById(R.id.progressBar_profile);
        user_img = view.findViewById(R.id.user_img);
        user_name = view.findViewById(R.id.user_name);
        user_email = view.findViewById(R.id.user_email);
        user_mobile = view.findViewById(R.id.user_mobile);
        profile_type = view.findViewById(R.id.profile_type);
        address_user_name = view.findViewById(R.id.address_user_name);
        address_type = view.findViewById(R.id.address_type);
        address_mobile = view.findViewById(R.id.address_mobile);
        address = view.findViewById(R.id.address);
        bank_name = view.findViewById(R.id.bank_name);
        ifsc = view.findViewById(R.id.ifsc);
        bank_account_no = view.findViewById(R.id.bank_account_no);
        bank_account_type = view.findViewById(R.id.bank_account_type);
        bank_holder_name = view.findViewById(R.id.bank_holder_name);
        bank_holder_mobile = view.findViewById(R.id.bank_holder_mobile);
        bank_holder_email = view.findViewById(R.id.bank_holder_email);

        edit_profile_btn = view.findViewById(R.id.edit_profile_btn);
        logout_btn = view.findViewById(R.id.logout_btn);
        all_order_txt = view.findViewById(R.id.all_order_txt);
        all_wishlist_txt = view.findViewById(R.id.all_wishlist_txt);
        all_review_txt = view.findViewById(R.id.all_review_txt);
        all_address_txt = view.findViewById(R.id.all_address_txt);
        all_bank_txt = view.findViewById(R.id.all_bank_txt);
        add_address_txt = view.findViewById(R.id.add_address_txt);
        add_bank_account_txt = view.findViewById(R.id.add_bank_account_txt);
        rv_my_order = view.findViewById(R.id.rv_my_order);
        rv_my_wishlist = view.findViewById(R.id.rv_my_wishlist);
        rv_my_review = view.findViewById(R.id.rv_my_review);
        item_address= view.findViewById(R.id.item_address);
        item_bank_account = view.findViewById(R.id.item_bank_account);

        main_scrollview.setVisibility(View.GONE);

        if (method.isNetworkAvailable(getActivity())) {
            if (method.isLogin()) {

                if (method.getLoginType().equals("facebook")) {
                    profile_type.setVisibility(View.VISIBLE);
                    profile_type.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.facebook_logo));
                }
                if (method.getLoginType().equals("google")){
                    profile_type.setVisibility(View.VISIBLE);
                    profile_type.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.google_logo));
                }
                profile(method.userId());
            } else {
                empty_layout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                empty_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_login_24));
                empty_msg.setText(getActivity().getResources().getString(R.string.login_msg));
                empty_btn.setText(getResources().getString(R.string.go_to_login));
                empty_btn.setVisibility(View.VISIBLE);
                empty_btn.setOnClickListener(v ->
                        startActivity(new Intent(getActivity(), LoginActivity.class)));
            }
        } else {
            method.alertBox(getResources().getString(R.string.no_internet_connection));
            empty_layout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }

        edit_profile_btn.setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.navigate_to_editprofile_fragment));
        all_order_txt.setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.navigate_to_order_fragment));
        all_wishlist_txt.setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.navigate_to_fav_fragment));
        all_review_txt.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), ReviewsActivity.class)
                        .putExtra("type", "my review")));
        all_address_txt.setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.navigate_to_addresses_fragment));
        item_address.setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.navigate_to_addresses_fragment));
        add_address_txt.setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.navigate_to_addresses_fragment));
        all_bank_txt.setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.navigate_to_bank_acc_fragment));
        item_bank_account.setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.navigate_to_bank_acc_fragment));
        add_bank_account_txt.setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.navigate_to_bank_acc_fragment));
        logout_btn.setOnClickListener(v ->
                logout());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        main_scrollview.setVisibility(View.GONE);

        if (method.isNetworkAvailable(getActivity())) {
            if (method.isLogin()) {
                profile(method.userId());
            }
        } else {
            progressBar.setVisibility(View.GONE);
            empty_layout.setVisibility(View.VISIBLE);
            method.alertBox(getResources().getString(R.string.no_internet_connection));
        }
    }

    private void profile(String userId) {

        if (getActivity() != null) {

            progressBar.setVisibility(View.VISIBLE);

            JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(getActivity()));
            jsObj.addProperty("user_id", userId);
            ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
            Call<UserProfileRP> call = apiService.getProfile(API.toBase64(jsObj.toString()));
            call.enqueue(new Callback<UserProfileRP>() {
                @SuppressLint("UseCompatLoadingForDrawables")
                @Override
                public void onResponse(@NotNull Call<UserProfileRP> call, @NotNull Response<UserProfileRP> response) {

                    if (getActivity() != null) {

                        try {

                            UserProfileRP userProfileRP = response.body();

                            assert userProfileRP != null;
                            if (userProfileRP.getStatus().equals("1")) {

                                if (userProfileRP.getSuccess().equals("1")) {

                                    main_scrollview.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                    empty_layout.setVisibility(View.GONE);

                                    Glide.with(getActivity().getApplicationContext()).load(userProfileRP.getUser_image())
                                            .placeholder(R.drawable.default_profile).into(user_img);

//                                    user_img.setOnClickListener(v -> startActivity(new Intent(getActivity(), ShowImage.class)
//                                            .putExtra("path", userProfileRP.getUser_image())));

                                    user_name.setText(userProfileRP.getUser_name());
                                    user_email.setText(userProfileRP.getUser_email());
                                    user_mobile.setText(userProfileRP.getUser_phone());

                                    String stringAddress = getResources().getString(R.string.view_all) + " " + "(" + userProfileRP.getAddress_count() + ")";
                                    all_address_txt.setText(stringAddress);
                                    if (userProfileRP.getAddress_count().equals("0")) {
                                        item_address.setVisibility(View.GONE);
                                        add_address_txt.setVisibility(View.VISIBLE);
                                    } else {
                                        all_address_txt.setVisibility(View.VISIBLE);
                                        address_user_name.setText(userProfileRP.getAddress_name());
                                        address_mobile.setText(userProfileRP.getAddress_phone());
                                        address_type.setText(userProfileRP.getAddress_type());
                                        address.setText(userProfileRP.getAddress());
                                    }

                                    String stringBank = getResources().getString(R.string.view_all) + " " + "(" + userProfileRP.getBank_count() + ")";
                                    all_bank_txt.setText(stringBank);
                                    if (userProfileRP.getBank_count().equals("0")) {
                                        item_bank_account.setVisibility(View.GONE);
                                        add_bank_account_txt.setVisibility(View.VISIBLE);
                                    } else {
                                        all_bank_txt.setVisibility(View.VISIBLE);
                                        bank_name.setText(userProfileRP.getBank_name());
                                        ifsc.setText(userProfileRP.getBank_ifsc());
                                        bank_account_no.setText(userProfileRP.getAccount_no());
                                        bank_account_type.setText(userProfileRP.getBank_type());
                                        bank_holder_name.setText(userProfileRP.getBank_holder_name());
                                        bank_holder_mobile.setText(userProfileRP.getBank_holder_phone());
                                        bank_holder_email.setText(userProfileRP.getBank_holder_email());
                                    }

                                    if (userProfileRP.getMyOrderLists().size() != 0) {

                                        empty_orders.setVisibility(View.GONE);
                                        all_order_txt.setVisibility(View.VISIBLE);
                                        rv_my_order.setVisibility(View.VISIBLE);

                                        myOrdersAdapter = new HomeMyOrdersAdapter(getActivity(), userProfileRP.getMyOrderLists(), false);
                                        rv_my_order.setAdapter(myOrdersAdapter);

                                    } else {
                                        rv_my_order.setVisibility(View.GONE);
                                        all_order_txt.setVisibility(View.GONE);
                                        empty_orders.setVisibility(View.VISIBLE);
                                    }

                                } else {
                                    empty_layout.setVisibility(View.VISIBLE);
                                    empty_msg.setText(getActivity().getResources().getString(R.string.no_data));
                                    method.alertBox(userProfileRP.getMsg());
                                }

                            } else {
                                empty_layout.setVisibility(View.VISIBLE);
                                empty_msg.setText(getActivity().getResources().getString(R.string.no_data));
                                method.alertBox(userProfileRP.getMessage());
                            }

                        } catch (Exception e) {
                            Log.d("exception_error", e.toString());
                            empty_layout.setVisibility(View.VISIBLE);
                            empty_msg.setText(getActivity().getResources().getString(R.string.no_data));
                            method.alertBox(getResources().getString(R.string.failed_try_again));
                        }

                    }

                    progressBar.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(@NotNull Call<UserProfileRP> call, @NotNull Throwable t) {
                    // Log error here since request failed
                    Log.e("fail_api", t.toString());
                    empty_layout.setVisibility(View.VISIBLE);
                    empty_msg.setText(getActivity().getResources().getString(R.string.no_data));
                    progressBar.setVisibility(View.GONE);
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }
            });


            JsonObject jsObj2 = (JsonObject) new Gson().toJsonTree(new API(getActivity()));
            jsObj2.addProperty("user_id", userId);
            ApiInterface apiService2 = ApiClient.getRetrofit().create(ApiInterface.class);
            Call<ProductRP> call2 = apiService2.getFavourite(API.toBase64(jsObj2.toString()));
            call2.enqueue(new Callback<ProductRP>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(@NotNull Call<ProductRP> call, @NotNull Response<ProductRP> response) {


                    if (getActivity() != null) {
                        try {

                            ProductRP proRP = response.body();

                            assert proRP != null;
                            if (proRP.getStatus().equals("1")) {

                                progressBar.setVisibility(View.GONE);

                                if (proRP.getProductLists().size() != 0) {
                                    empty_wishlist.setVisibility(View.GONE);
                                    rv_my_wishlist.setVisibility(View.VISIBLE);
                                    all_wishlist_txt.setVisibility(View.VISIBLE);
                                    String wishlistitems = getResources().getString(R.string.view_all) + " " + "(" + proRP.getTotal_products() + ")";
                                    all_wishlist_txt.setText(wishlistitems);
                                    wishlistItemAdapter = new WishlistItemAdapter(getActivity(), true, proRP.getProductLists());
                                    rv_my_wishlist.setAdapter(wishlistItemAdapter);

                                }

                            } else {
                                method.alertBox(proRP.getMessage());
                            }

                        } catch (Exception e) {
                            Log.d("exception_error", e.toString());
                            method.alertBox(getResources().getString(R.string.failed_try_again));
                        }
                    }

                    progressBar.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(@NotNull Call<ProductRP> call, @NotNull Throwable t) {
                    // Log error here since request failed
                    Log.e("fail_api", t.toString());
                    progressBar.setVisibility(View.GONE);
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }
            });

            JsonObject jsObj3 = (JsonObject) new Gson().toJsonTree(new API(getActivity()));
            jsObj3.addProperty("user_id", userId);
            ApiInterface apiService3 = ApiClient.getRetrofit().create(ApiInterface.class);
            Call<MyRatingReviewRP> call3 = apiService3.getMyRatingReview(API.toBase64(jsObj3.toString()));
            call3.enqueue(new Callback<MyRatingReviewRP>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(@NotNull Call<MyRatingReviewRP> call, @NotNull Response<MyRatingReviewRP> response) {


                    if (getActivity() != null) {

                        try {

                            MyRatingReviewRP myRatingReviewRP = response.body();

                            assert myRatingReviewRP != null;
                            if (myRatingReviewRP.getStatus().equals("1")) {

                                if (myRatingReviewRP.getMyReviewsLists().size() != 0) {
                                    empty_reviews.setVisibility(View.GONE);
                                    rv_my_review.setVisibility(View.VISIBLE);
                                    all_review_txt.setVisibility(View.VISIBLE);
                                    String reviews = getResources().getString(R.string.view_all) + " " + "(" + myRatingReviewRP.getMyReviewsLists().size() + ")";
                                    all_review_txt.setText(reviews);

                                    myReviewAdapter = new MyReviewAdapter(getActivity(), true, myRatingReviewRP.getMyReviewsLists());
                                    rv_my_review.setAdapter(myReviewAdapter);

                                }

                            } else {
                                method.alertBox(myRatingReviewRP.getMessage());
                            }

                        } catch (Exception e) {
                            Log.d("exception_error", e.toString());
                            method.alertBox(getResources().getString(R.string.failed_try_again));
                        }
                    }

                    progressBar.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(@NotNull Call<MyRatingReviewRP> call, @NotNull Throwable t) {
                    // Log error here since request failed
                    Log.e("fail_api", t.toString());
                    progressBar.setVisibility(View.GONE);
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }
            });

        }

    }

    private void logout() {

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity(), R.style.DialogTitleTextStyle);
        builder.setCancelable(false);
        builder.setMessage(getResources().getString(R.string.logout_message));
        builder.setPositiveButton(getResources().getString(R.string.logout),
                (arg0, arg1) -> {
                    if (method.getLoginType().equals("google")) {

                        // Configure sign-in to request the user's ID, email address, and basic
                        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
                        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestProfile()
                                .requestEmail()
                                .build();

                        // Build a GoogleSignInClient with the options specified by gso.
                        //Google login
                        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

                        mGoogleSignInClient.signOut()
                                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        method.editor.putBoolean(method.prefLogin, false);
                                        method.editor.commit();
                                    }
                                });

                    } else if (method.getLoginType().equals("facebook")) {

                        LoginManager.getInstance().logOut();
                        method.editor.putBoolean(method.prefLogin, false);
                        method.editor.commit();

                    } else {
                        method.editor.putBoolean(method.prefLogin, false);
                        method.editor.commit();
                    }

                    startActivity(new Intent(getActivity(), MainActivity.class));
                    getActivity().finishAffinity();

                    Toast.makeText(getActivity(), "Logged Out Successfully...", Toast.LENGTH_SHORT).show();

                });
        builder.setNegativeButton(getResources().getString(R.string.cancel),
                (dialogInterface, i) -> {

                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

}