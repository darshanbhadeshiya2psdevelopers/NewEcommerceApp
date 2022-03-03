package com.db.newecom.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.db.newecom.Api.ApiClient;
import com.db.newecom.Api.ApiInterface;
import com.db.newecom.R;
import com.db.newecom.Response.MyOrderRP;
import com.db.newecom.Utills.API;
import com.db.newecom.Utills.Method;
import com.db.newecom.adapters.HomeMyOrdersAdapter;
import com.db.newecom.ui.activity.LoginActivity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrdersFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Method method;
    private LinearLayout progressBar;
    private RelativeLayout empty_layout;
    private LottieAnimationView empty_animation;
    private TextView empty_msg;
    private Button empty_btn;
    private RecyclerView rv_order;
    private HomeMyOrdersAdapter ordersAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    public OrdersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrdersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrdersFragment newInstance(String param1, String param2) {
        OrdersFragment fragment = new OrdersFragment();
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
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        method = new Method(getActivity());

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_myOrder);
        progressBar = view.findViewById(R.id.ll_progress_orders);
        empty_layout = view.findViewById(R.id.empty_layout);
        empty_animation = view.findViewById(R.id.empty_animation);
        empty_msg = view.findViewById(R.id.empty_msg);
        empty_btn = view.findViewById(R.id.empty_btn);
        rv_order = view.findViewById(R.id.rv_order);

        swipeRefreshLayout.setColorSchemeColors(getActivity().getResources().getColor(R.color.dark_blue));

        empty_animation.setAnimation("no_orders.json");
        empty_msg.setText(R.string.empty_order);
        empty_btn.setVisibility(View.VISIBLE);
        empty_btn.setOnClickListener(v -> getActivity().onBackPressed());

        if (method.isNetworkAvailable(getActivity())){

            if (method.isLogin()){
                getAllOrders(method.userId());
            }
            else {
                progressBar.setVisibility(View.GONE);
                empty_layout.setVisibility(View.VISIBLE);
                empty_animation.setAnimation("login.json");
                empty_msg.setText(getActivity().getResources().getString(R.string.login_msg));
                empty_btn.setVisibility(View.VISIBLE);
                empty_btn.setText("go to login");
                empty_btn.setOnClickListener(v -> startActivity(new Intent(getActivity(), LoginActivity.class)));
                rv_order.setVisibility(View.GONE);
            }

        }
        else {
            progressBar.setVisibility(View.GONE);
            empty_layout.setVisibility(View.VISIBLE);
            rv_order.setVisibility(View.GONE);
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
        }

        swipeRefreshLayout.setOnRefreshListener(() -> {

            rv_order.setVisibility(View.GONE);

            if (method.isNetworkAvailable(getActivity())){

                if (method.isLogin()){
                    getAllOrders(method.userId());
                }
                else {
                    progressBar.setVisibility(View.GONE);
                    empty_layout.setVisibility(View.VISIBLE);
                    empty_animation.setAnimation("login.json");
                    empty_msg.setText(getActivity().getResources().getString(R.string.login_msg));
                    empty_btn.setVisibility(View.VISIBLE);
                    empty_btn.setText("go to login");
                    empty_btn.setOnClickListener(v -> startActivity(new Intent(getActivity(), LoginActivity.class)));
                    rv_order.setVisibility(View.GONE);
                }

            }
            else {
                progressBar.setVisibility(View.GONE);
                empty_layout.setVisibility(View.VISIBLE);
                rv_order.setVisibility(View.GONE);
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        progressBar.setVisibility(View.VISIBLE);
        rv_order.setVisibility(View.GONE);

        if (method.isNetworkAvailable(getActivity())){

            if (method.isLogin()){
                getAllOrders(method.userId());
            }
            else {
                progressBar.setVisibility(View.GONE);
                empty_layout.setVisibility(View.VISIBLE);
                empty_animation.setAnimation("login.json");
                empty_msg.setText(getActivity().getResources().getString(R.string.login_msg));
                empty_btn.setVisibility(View.VISIBLE);
                empty_btn.setText("go to login");
                empty_btn.setOnClickListener(v -> startActivity(new Intent(getActivity(), LoginActivity.class)));
                rv_order.setVisibility(View.GONE);
            }

        }
        else {
            progressBar.setVisibility(View.GONE);
            empty_layout.setVisibility(View.VISIBLE);
            rv_order.setVisibility(View.GONE);
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
        }
    }

    private void getAllOrders(String userId) {

        if (getActivity() != null) {

            progressBar.setVisibility(View.VISIBLE);

            JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(getActivity()));
            jsObj.addProperty("user_id", userId);
            ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
            Call<MyOrderRP> call = apiService.getMyOderList(API.toBase64(jsObj.toString()));
            call.enqueue(new Callback<MyOrderRP>() {
                @Override
                public void onResponse(Call<MyOrderRP> call, Response<MyOrderRP> response) {
                    if (getActivity() != null) {
                        try {
                            MyOrderRP myOrderRP = response.body();

                            assert myOrderRP != null;
                            if (myOrderRP.getStatus().equals("1")) {

                                swipeRefreshLayout.setRefreshing(false);

                                progressBar.setVisibility(View.GONE);
                                empty_layout.setVisibility(View.GONE);

                                if (myOrderRP.getMyOrderLists().size() != 0) {

                                    rv_order.setVisibility(View.VISIBLE);

                                    ordersAdapter = new HomeMyOrdersAdapter(getActivity(), myOrderRP.getMyOrderLists(), false);
                                    rv_order.setAdapter(ordersAdapter);

                                } else {
                                    rv_order.setVisibility(View.GONE);
                                    empty_layout.setVisibility(View.VISIBLE);
                                    empty_msg.setText(getActivity().getResources().getString(R.string.no_orders));
                                    empty_btn.setVisibility(View.VISIBLE);
                                    empty_btn.setText(getActivity().getResources().getString(R.string.continue_shopping));
                                    empty_btn.setOnClickListener(v -> getActivity().onBackPressed());
                                }
                            }
                            else {
                                empty_layout.setVisibility(View.VISIBLE);
                                rv_order.setVisibility(View.GONE);
                                method.alertBox(myOrderRP.getMessage());
                            }
                        }
                        catch (Exception e){
                            Log.d("exception_error", e.toString());
                            empty_layout.setVisibility(View.VISIBLE);
                            rv_order.setVisibility(View.GONE);
                            method.alertBox(getResources().getString(R.string.failed_try_again));
                        }

                        progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<MyOrderRP> call, Throwable t) {
                    Log.e("fail_api", t.toString());
                    progressBar.setVisibility(View.GONE);
                    empty_layout.setVisibility(View.VISIBLE);
                    rv_order.setVisibility(View.GONE);
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }
            });

        }

    }
}