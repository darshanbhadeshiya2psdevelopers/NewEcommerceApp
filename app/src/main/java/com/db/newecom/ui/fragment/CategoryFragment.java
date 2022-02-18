package com.db.newecom.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.db.newecom.Api.ApiClient;
import com.db.newecom.Api.ApiInterface;
import com.db.newecom.R;
import com.db.newecom.Response.CategoryRP;
import com.db.newecom.Utills.API;
import com.db.newecom.Utills.ConstantApi;
import com.db.newecom.Utills.Method;
import com.db.newecom.adapters.CategoryAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Method method;
    private ProgressBar progressBar;
    private RelativeLayout empty_layout;
    private TextView empty_msg;
    private RecyclerView rv_category;
    private CategoryAdapter categoryAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    public CategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryFragment newInstance(String param1, String param2) {
        CategoryFragment fragment = new CategoryFragment();
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
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        method = new Method(getActivity());

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_cat);
        progressBar = view.findViewById(R.id.progressBar_cat);
        empty_layout = view.findViewById(R.id.empty_layout);
        empty_msg = view.findViewById(R.id.empty_msg);
        rv_category = view.findViewById(R.id.rv_category);

        swipeRefreshLayout.setColorSchemeColors(getActivity().getResources().getColor(R.color.dark_blue));

        if (method.isNetworkAvailable(getActivity())){
            if (method.isLogin())
                getCategories(method.userId());
            else
                getCategories("0");
        }
        else {
            rv_category.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            empty_layout.setVisibility(View.VISIBLE);
            method.alertBox(getActivity().getResources().getString(R.string.no_internet_connection));
        }

        swipeRefreshLayout.setOnRefreshListener(() -> {

            rv_category.setVisibility(View.GONE);

            if (method.isNetworkAvailable(getActivity())){
                if (method.isLogin())
                    getCategories(method.userId());
                else
                    getCategories("0");
            }
            else {
                rv_category.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                empty_layout.setVisibility(View.VISIBLE);
                method.alertBox(getActivity().getResources().getString(R.string.no_internet_connection));
            }
        });

        return view;
    }

    private void getCategories(String userId) {

        if (getActivity() != null){

            progressBar.setVisibility(View.VISIBLE);
            rv_category.setVisibility(View.GONE);

            JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(getActivity()));
            jsObj.addProperty("user_id", userId);
            ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
            Call<CategoryRP> call = apiService.getCategory(API.toBase64(jsObj.toString()));
            call.enqueue(new Callback<CategoryRP>() {
                @Override
                public void onResponse(@NotNull Call<CategoryRP> call, @NotNull Response<CategoryRP> response) {

                    if (getActivity() != null) {

                        try {
                            CategoryRP categoryRP = response.body();
                            assert categoryRP != null;

                            if (categoryRP.getStatus().equals("1")) {

                                swipeRefreshLayout.setRefreshing(false);

                                if (categoryRP.getCategoryLists().size() == 0) {
                                    empty_layout.setVisibility(View.VISIBLE);
                                    rv_category.setVisibility(View.GONE);
                                    empty_msg.setText(getActivity().getResources().getString(R.string.no_items));

                                } else {
                                    empty_layout.setVisibility(View.GONE);
                                    rv_category.setVisibility(View.VISIBLE);

                                    categoryAdapter = new CategoryAdapter(getActivity(), categoryRP.getCategoryLists());
                                    rv_category.setAdapter(categoryAdapter);
                                }

                            } else {
                                empty_layout.setVisibility(View.VISIBLE);
                                rv_category.setVisibility(View.GONE);
                                method.alertBox(categoryRP.getMessage());
                            }

                        } catch (Exception e) {
                            Log.d(ConstantApi.exceptionError, e.toString());
                            empty_layout.setVisibility(View.VISIBLE);
                            rv_category.setVisibility(View.GONE);
                            method.alertBox(getResources().getString(R.string.failed_try_again));
                        }
                    }

                    progressBar.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(@NotNull Call<CategoryRP> call, @NotNull Throwable t) {
                    // Log error here since request failed
                    Log.e(ConstantApi.failApi, t.toString());
                    empty_layout.setVisibility(View.VISIBLE);
                    rv_category.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }
            });

        }

    }
}