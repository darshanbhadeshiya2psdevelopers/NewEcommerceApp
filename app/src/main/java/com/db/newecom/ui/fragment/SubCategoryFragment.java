package com.db.newecom.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.db.newecom.Api.ApiClient;
import com.db.newecom.Api.ApiInterface;
import com.db.newecom.R;
import com.db.newecom.Response.SubCategoryRP;
import com.db.newecom.Utills.API;
import com.db.newecom.Utills.ConstantApi;
import com.db.newecom.Utills.Method;
import com.db.newecom.adapters.CategoryAdapter;
import com.db.newecom.ui.activity.MainActivity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SubCategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubCategoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "title";
    private static final String ARG_PARAM2 = "cat_id";

    // TODO: Rename and change types of parameters
    private String title, category_id;

    private Method method;
    private LinearLayout progressBar;
    private RelativeLayout empty_layout;
    private TextView empty_msg;
    private RecyclerView rv_sub_category;
    private CategoryAdapter subCategoryAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    public SubCategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment SubCategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SubCategoryFragment newInstance(String param1, String param2) {
        SubCategoryFragment fragment = new SubCategoryFragment();
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
            title = getArguments().getString(ARG_PARAM1);
            category_id = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sub_category, container, false);

        if (MainActivity.toolbar != null)
            MainActivity.toolbar.setTitle(title);

        method = new Method(getActivity());

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_subCat);
        progressBar = view.findViewById(R.id.ll_progress_sub_cat);
        empty_layout = view.findViewById(R.id.empty_layout);
        empty_msg = view.findViewById(R.id.empty_msg);
        rv_sub_category = view.findViewById(R.id.rv_sub_category);

        swipeRefreshLayout.setColorSchemeColors(getActivity().getResources().getColor(R.color.dark_blue));

        if (method.isNetworkAvailable(getActivity())){
            if (method.isLogin())
                getSubCategories(method.userId(), category_id);
            else
                getSubCategories("0", category_id);
        }
        else {
            rv_sub_category.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            empty_layout.setVisibility(View.VISIBLE);
            method.alertBox(getActivity().getResources().getString(R.string.no_internet_connection));
        }

        swipeRefreshLayout.setOnRefreshListener(() -> {

            rv_sub_category.setVisibility(View.GONE);

            if (method.isNetworkAvailable(getActivity())){
                if (method.isLogin())
                    getSubCategories(method.userId(), category_id);
                else
                    getSubCategories("0", category_id);
            }
            else {
                rv_sub_category.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                empty_layout.setVisibility(View.VISIBLE);
                method.alertBox(getActivity().getResources().getString(R.string.no_internet_connection));
            }
        });

        return view;
    }

    private void getSubCategories(String userId, String category_id) {

        if (getActivity() != null){

            progressBar.setVisibility(View.VISIBLE);
            rv_sub_category.setVisibility(View.GONE);

            JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(getActivity()));
            jsObj.addProperty("user_id", userId);
            jsObj.addProperty("cat_id", category_id);
            ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
            Call<SubCategoryRP> call = apiService.getSubCategory(API.toBase64(jsObj.toString()));
            call.enqueue(new Callback<SubCategoryRP>() {
                @Override
                public void onResponse(@NotNull Call<SubCategoryRP> call, @NotNull Response<SubCategoryRP> response) {

                    if (getActivity() != null) {

                        try {
                            SubCategoryRP subCategoryRP = response.body();
                            assert subCategoryRP != null;

                            if (subCategoryRP.getStatus().equals("1")) {

                                swipeRefreshLayout.setRefreshing(false);

                                if (subCategoryRP.getSubCategoryLists().size() == 0) {
                                    empty_layout.setVisibility(View.VISIBLE);
                                    rv_sub_category.setVisibility(View.GONE);
                                    empty_msg.setText(getActivity().getResources().getString(R.string.no_items));

                                } else {
                                    empty_layout.setVisibility(View.GONE);
                                    rv_sub_category.setVisibility(View.VISIBLE);

                                    subCategoryAdapter = new CategoryAdapter(getActivity(), true, subCategoryRP.getSubCategoryLists());
                                    rv_sub_category.setAdapter(subCategoryAdapter);
                                }

                            } else {
                                empty_layout.setVisibility(View.VISIBLE);
                                rv_sub_category.setVisibility(View.GONE);
                                method.alertBox(subCategoryRP.getMessage());
                            }

                        } catch (Exception e) {
                            Log.d(ConstantApi.exceptionError, e.toString());
                            empty_layout.setVisibility(View.VISIBLE);
                            rv_sub_category.setVisibility(View.GONE);
                            method.alertBox(getResources().getString(R.string.failed_try_again));
                        }
                    }

                    progressBar.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(@NotNull Call<SubCategoryRP> call, @NotNull Throwable t) {
                    // Log error here since request failed
                    Log.e(ConstantApi.failApi, t.toString());
                    empty_layout.setVisibility(View.VISIBLE);
                    rv_sub_category.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }
            });

        }
    }
}