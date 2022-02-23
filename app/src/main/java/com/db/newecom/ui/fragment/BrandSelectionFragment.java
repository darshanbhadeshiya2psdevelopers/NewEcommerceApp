package com.db.newecom.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.db.newecom.Api.ApiClient;
import com.db.newecom.Api.ApiInterface;
import com.db.newecom.Model.BrandFilterList;
import com.db.newecom.R;
import com.db.newecom.Response.BrandFilterRP;
import com.db.newecom.Utills.API;
import com.db.newecom.Utills.ConstantApi;
import com.db.newecom.Utills.Method;
import com.db.newecom.adapters.BrandSelectAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BrandSelectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BrandSelectionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "filter_type";
    private static final String ARG_PARAM2 = "search";
    private static final String ARG_PARAM3 = "id";

    // TODO: Rename and change types of parameters
    private String filter_type, search, id;

    private Method method;
    private LinearLayout progressBar;
    private RelativeLayout rl_brand_filter;
    private EditText search_brand;
    private List<BrandFilterList> brandFilterLists;
    private TextView no_data;
    private RecyclerView rv_brand_select;
    private BrandSelectAdapter brandSelectAdapter;

    public BrandSelectionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BrandSelectionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BrandSelectionFragment newInstance(String param1, String param2, String param3) {
        BrandSelectionFragment fragment = new BrandSelectionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            filter_type = getArguments().getString(ARG_PARAM1);
            search = getArguments().getString(ARG_PARAM2);
            id = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_brand_selection, container, false);

        method = new Method(getActivity());
        brandFilterLists = new ArrayList<>();

        progressBar = view.findViewById(R.id.ll_progress_filter2);
        no_data = view.findViewById(R.id.tv_no_data);
        rl_brand_filter = view.findViewById(R.id.rl_brand_filter);
        search_brand = view.findViewById(R.id.search_brand);
        rv_brand_select = view.findViewById(R.id.rv_brand_select);

        rl_brand_filter.setVisibility(View.GONE);
        
        if (method.isNetworkAvailable(getActivity())) {
            brandFilter(filter_type, search, id);
        } else {
            progressBar.setVisibility(View.GONE);
            method.alertBox(getResources().getString(R.string.no_internet_connection));
        }
        
        return view;
    }

    private void brandFilter(String filter_type, String search, String id) {

        if (getActivity() != null) {

            progressBar.setVisibility(View.VISIBLE);

            JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(getActivity()));
            jsObj.addProperty("type", filter_type);
            jsObj.addProperty("id", id);
            jsObj.addProperty("brand_ids", ConstantApi.brand_ids_temp);
            if (filter_type.equals("recent_viewed_products")) {
                jsObj.addProperty("user_id", method.userId());
            }
            if (filter_type.equals("search")) {
                jsObj.addProperty("keyword", search);
            }
            ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
            Call<BrandFilterRP> call = apiService.getBrandFilter(API.toBase64(jsObj.toString()));
            call.enqueue(new Callback<BrandFilterRP>() {
                @Override
                public void onResponse(@NotNull Call<BrandFilterRP> call, @NotNull Response<BrandFilterRP> response) {


                    if (getActivity() != null) {
                        try {

                            BrandFilterRP brandFilterRP = response.body();

                            assert brandFilterRP != null;
                            if (brandFilterRP.getStatus().equals("1")) {

                                if (brandFilterRP.getBrandFilterLists().size() != 0) {

                                    brandFilterLists.addAll(brandFilterRP.getBrandFilterLists());

                                    if (brandFilterLists.size() != 0) {
                                        rl_brand_filter.setVisibility(View.VISIBLE);
                                        brandSelectAdapter = new BrandSelectAdapter(getActivity(), brandFilterLists);
                                        rv_brand_select.setAdapter(brandSelectAdapter);
                                    } else {
                                        no_data.setVisibility(View.VISIBLE);
                                    }

                                    search_brand.addTextChangedListener(new TextWatcher() {
                                        @Override
                                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                            Log.d("data_app", "");
                                        }

                                        @Override
                                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                                            brandSelectAdapter.getFilter().filter(s.toString());
                                        }

                                        @Override
                                        public void afterTextChanged(Editable s) {
                                            Log.d("data_app", "");
                                        }
                                    });

                                }

                            } else {
                                no_data.setVisibility(View.VISIBLE);
                                method.alertBox(brandFilterRP.getMessage());
                            }


                        } catch (Exception e) {
                            Log.d(ConstantApi.exceptionError, e.toString());
                            method.alertBox(getResources().getString(R.string.failed_try_again));
                        }
                    }

                    progressBar.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(@NotNull Call<BrandFilterRP> call, @NotNull Throwable t) {
                    // Log error here since request failed
                    Log.e(ConstantApi.failApi, t.toString());
                    progressBar.setVisibility(View.GONE);
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }
            });

        }

    }
}