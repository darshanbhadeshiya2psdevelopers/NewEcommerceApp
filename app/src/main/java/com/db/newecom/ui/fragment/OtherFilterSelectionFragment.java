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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.db.newecom.Api.ApiClient;
import com.db.newecom.Api.ApiInterface;
import com.db.newecom.R;
import com.db.newecom.Response.SizeFilterRP;
import com.db.newecom.Utills.API;
import com.db.newecom.Utills.ConstantApi;
import com.db.newecom.Utills.Method;
import com.db.newecom.adapters.FilterSelectAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OtherFilterSelectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OtherFilterSelectionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "filter_type";
    private static final String ARG_PARAM2 = "search";
    private static final String ARG_PARAM3 = "id";

    // TODO: Rename and change types of parameters
    private String filterType, search, id;

    private Method method;
    private ProgressBar progressBar;
    private RelativeLayout rl_other_filter;
    private EditText search_other;
    private TextView no_data;
    private RecyclerView rv_filter_select;
    private FilterSelectAdapter filterSelectAdapter;

    public OtherFilterSelectionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OtherFilterSelectionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OtherFilterSelectionFragment newInstance(String param1, String param2, String param3) {
        OtherFilterSelectionFragment fragment = new OtherFilterSelectionFragment();
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
            filterType = getArguments().getString(ARG_PARAM1);
            search = getArguments().getString(ARG_PARAM2);
            id = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_other_filter_selection, container, false);

        method = new Method(getActivity());

        progressBar = view.findViewById(R.id.progressbar_filter3);
        no_data = view.findViewById(R.id.tv_no_data);
        rl_other_filter = view.findViewById(R.id.rl_other_filter);
        search_other = view.findViewById(R.id.search_other);
        rv_filter_select = view.findViewById(R.id.rv_filter_select);

        rl_other_filter.setVisibility(View.GONE);

        if (method.isNetworkAvailable(getActivity())) {
            sizeFilter(filterType, search, id);
        } else {
            progressBar.setVisibility(View.GONE);
            method.alertBox(getResources().getString(R.string.no_internet_connection));
        }
        
        return view;
    }

    private void sizeFilter(String filterType, String search, String id) {

        if (getActivity() != null) {

            progressBar.setVisibility(View.VISIBLE);

            JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(getActivity()));
            jsObj.addProperty("type", filterType);
            jsObj.addProperty("id", id);
            jsObj.addProperty("sizes", ConstantApi.sizes_temp);
            jsObj.addProperty("brand_ids", ConstantApi.brand_ids_temp);
            if (filterType.equals("recent_viewed_products")) {
                jsObj.addProperty("user_id", method.userId());
            }
            if (filterType.equals("search")) {
                jsObj.addProperty("keyword", search);
            }
            ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
            Call<SizeFilterRP> call = apiService.getSizeFilter(API.toBase64(jsObj.toString()));
            call.enqueue(new Callback<SizeFilterRP>() {
                @Override
                public void onResponse(@NotNull Call<SizeFilterRP> call, @NotNull Response<SizeFilterRP> response) {


                    if (getActivity() != null) {
                        try {

                            SizeFilterRP sizeFilterRP = response.body();

                            assert sizeFilterRP != null;
                            if (sizeFilterRP.getStatus().equals("1")) {

                                if (sizeFilterRP.getSizeFilterLists().size() != 0) {

                                    rl_other_filter.setVisibility(View.VISIBLE);

                                    filterSelectAdapter = new FilterSelectAdapter(getActivity(), sizeFilterRP.getSizeFilterLists());
                                    rv_filter_select.setAdapter(filterSelectAdapter);


                                    search_other.addTextChangedListener(new TextWatcher() {
                                        @Override
                                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                            Log.d("data_app", "");
                                        }

                                        @Override
                                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                                            filterSelectAdapter.getFilter().filter(s.toString());
                                        }

                                        @Override
                                        public void afterTextChanged(Editable s) {
                                            Log.d("data_app", "");
                                        }
                                    });

                                } else {
                                    no_data.setVisibility(View.VISIBLE);
                                }

                            } else {
                                no_data.setVisibility(View.VISIBLE);
                                method.alertBox(sizeFilterRP.getMessage());
                            }

                        } catch (Exception e) {
                            Log.d(ConstantApi.exceptionError, e.toString());
                            method.alertBox(getResources().getString(R.string.failed_try_again));
                        }
                    }

                    progressBar.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(@NotNull Call<SizeFilterRP> call, @NotNull Throwable t) {
                    // Log error here since request failed
                    Log.e(ConstantApi.failApi, t.toString());
                    no_data.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }
            });

        }

    }
}