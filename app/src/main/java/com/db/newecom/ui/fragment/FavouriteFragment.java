package com.db.newecom.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
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
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.db.newecom.Api.ApiClient;
import com.db.newecom.Api.ApiInterface;
import com.db.newecom.Model.ProductList;
import com.db.newecom.R;
import com.db.newecom.Response.ProductRP;
import com.db.newecom.Utills.API;
import com.db.newecom.Utills.Events;
import com.db.newecom.Utills.GlobalBus;
import com.db.newecom.Utills.Method;
import com.db.newecom.adapters.WishlistItemAdapter;
import com.db.newecom.ui.activity.LoginActivity;
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
 * Use the {@link FavouriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavouriteFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Method method;
    private RecyclerView rv_my_wishlist_all;
    private WishlistItemAdapter wishlistItemAdapter;
    private LinearLayout progressBar;
    private RelativeLayout empty_layout;
    private LottieAnimationView empty_animation;
    private TextView empty_msg, cart_items;
    private Button empty_btn;
    private List<ProductList> productLists;

    public FavouriteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavouriteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavouriteFragment newInstance(String param1, String param2) {
        FavouriteFragment fragment = new FavouriteFragment();
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
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);

        GlobalBus.getBus().register(this);
        method = new Method(getActivity());
        productLists = new ArrayList<>();

        rv_my_wishlist_all = view.findViewById(R.id.rv_my_wishlist_all);
        progressBar = view.findViewById(R.id.ll_progress_wishlist);
        empty_layout = view.findViewById(R.id.empty_layout);
        empty_animation = view.findViewById(R.id.empty_animation);
        empty_msg = view.findViewById(R.id.empty_msg);
        empty_btn = view.findViewById(R.id.empty_btn);
        cart_items = getActivity().findViewById(R.id.order_count);

        empty_animation.setAnimation("wishlist.json");

        if (method.isNetworkAvailable(getActivity())) {
            if (method.isLogin()) {
                favourite(method.userId());
            } else {
                progressBar.setVisibility(View.GONE);
                rv_my_wishlist_all.setVisibility(View.GONE);
                empty_layout.setVisibility(View.VISIBLE);
                empty_animation.setAnimation("login.json");
                empty_msg.setText(getActivity().getResources().getString(R.string.login_msg));
                empty_btn.setVisibility(View.VISIBLE);
                empty_btn.setText(getResources().getString(R.string.go_to_login));
                empty_btn.setOnClickListener(v ->
                        startActivity(new Intent(getActivity(), LoginActivity.class)));

            }
        } else {
            progressBar.setVisibility(View.GONE);
            rv_my_wishlist_all.setVisibility(View.GONE);
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

    private void favourite(String userId) {

        if (getActivity() != null) {

            productLists.clear();
            progressBar.setVisibility(View.VISIBLE);

            JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(getActivity()));
            jsObj.addProperty("user_id", userId);
//            jsObj.addProperty("page", paginationIndex);
            ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
            Call<ProductRP> call = apiService.getFavourite(API.toBase64(jsObj.toString()));
            call.enqueue(new Callback<ProductRP>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(@NotNull Call<ProductRP> call, @NotNull Response<ProductRP> response) {


                    if (getActivity() != null) {
                        try {

                            ProductRP proRP = response.body();

                            assert proRP != null;
                            if (proRP.getStatus().equals("1")) {

                                progressBar.setVisibility(View.GONE);

                                if (proRP.getProductLists().size() == 0) {

                                    rv_my_wishlist_all.setVisibility(View.GONE);
                                    empty_layout.setVisibility(View.VISIBLE);
                                    empty_msg.setText(R.string.empty_wishlist);
                                    empty_btn.setVisibility(View.VISIBLE);
                                    empty_btn.setOnClickListener(v -> getActivity().onBackPressed());

                                } else {
                                    empty_layout.setVisibility(View.GONE);
                                    rv_my_wishlist_all.setVisibility(View.VISIBLE);
                                    rv_my_wishlist_all.setItemViewCacheSize(proRP.getProductLists().size());
                                    wishlistItemAdapter = new WishlistItemAdapter(getActivity(), false, proRP.getProductLists());
                                    rv_my_wishlist_all.setAdapter(wishlistItemAdapter);

                                }

                            } else {
                                rv_my_wishlist_all.setVisibility(View.GONE);
                                empty_layout.setVisibility(View.VISIBLE);
                                method.alertBox(proRP.getMessage());
                            }

                        } catch (Exception e) {
                            Log.d("exception_error", e.toString());
                            rv_my_wishlist_all.setVisibility(View.GONE);
                            empty_layout.setVisibility(View.VISIBLE);
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
                    rv_my_wishlist_all.setVisibility(View.GONE);
                    empty_layout.setVisibility(View.VISIBLE);
                    method.alertBox(getResources().getString(R.string.failed_try_again));
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