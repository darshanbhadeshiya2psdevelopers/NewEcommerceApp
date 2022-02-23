package com.db.newecom.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.db.newecom.Api.ApiClient;
import com.db.newecom.Api.ApiInterface;
import com.db.newecom.R;
import com.db.newecom.Response.PriceSelectionRP;
import com.db.newecom.Utills.API;
import com.db.newecom.Utills.ConstantApi;
import com.db.newecom.Utills.Method;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PriceSelectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PriceSelectionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "filter_type";
    private static final String ARG_PARAM2 = "search";
    private static final String ARG_PARAM3 = "id";
    private static final String ARG_PARAM4 = "sort";

    // TODO: Rename and change types of parameters
    private String filter_type, search, id = "", sort;

    private Method method;
    private String sign;
    private LinearLayout progressBar;
    private CrystalRangeSeekbar rangeSeekBar;
    private TextView textViewMin, textViewMax;
    private LinearLayout ll_price_main;

    public PriceSelectionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PriceSelectionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PriceSelectionFragment newInstance(String param1, String param2, String param3, String param4) {
        PriceSelectionFragment fragment = new PriceSelectionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putString(ARG_PARAM4, param4);
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
            sort = getArguments().getString(ARG_PARAM4);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_price_selection, container, false);

        method = new Method(getActivity());

        sign = ConstantApi.currency;

        ll_price_main = view.findViewById(R.id.ll_price_main);
        progressBar = view.findViewById(R.id.ll_progress_filter1);
        textViewMin = view.findViewById(R.id.min_price);
        textViewMax = view.findViewById(R.id.max_price);
        rangeSeekBar = view.findViewById(R.id.rangeSeekBar);

        ll_price_main.setVisibility(View.GONE);

        if (method.isNetworkAvailable(getActivity())) {
            priceFilter(filter_type, search, id);
        } else {
            method.alertBox(getResources().getString(R.string.no_internet_connection));
        }

        return view;
    }

    private void priceFilter(String type, String search, String id) {

        if (getActivity() != null) {

            progressBar.setVisibility(View.VISIBLE);

            JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(getActivity()));
            jsObj.addProperty("type", type);
            jsObj.addProperty("id", id);
            jsObj.addProperty("pre_min", ConstantApi.pre_min_temp);
            jsObj.addProperty("pre_max", ConstantApi.pre_max_temp);
            if (type.equals("recent_viewed_products")) {
                jsObj.addProperty("user_id", method.userId());
            }
            if (type.equals("search")) {
                jsObj.addProperty("keyword", search);
            }
            ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
            Call<PriceSelectionRP> call = apiService.getPriceSelection(API.toBase64(jsObj.toString()));
            call.enqueue(new Callback<PriceSelectionRP>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(@NotNull Call<PriceSelectionRP> call, @NotNull Response<PriceSelectionRP> response) {


                    if (getActivity() != null) {
                        try {

                            PriceSelectionRP priceSelectionRP = response.body();

                            assert priceSelectionRP != null;
                            if (priceSelectionRP.getStatus().equals("1")) {

                                ll_price_main.setVisibility(View.VISIBLE);

                                textViewMin.setText(sign + " " + priceSelectionRP.getPre_price_min());
                                textViewMax.setText(sign + " " + priceSelectionRP.getPre_price_max());

                                rangeSeekBar.setMinValue(Float.parseFloat(priceSelectionRP.getPrice_min()));
                                rangeSeekBar.setMaxValue(Float.parseFloat(priceSelectionRP.getPrice_max()));

                                rangeSeekBar.setMinStartValue(Float.parseFloat(priceSelectionRP.getPre_price_min()));
                                rangeSeekBar.setMaxStartValue(Float.parseFloat(priceSelectionRP.getPre_price_max()));

                                rangeSeekBar.apply();

                                rangeSeekBar.setOnRangeSeekbarChangeListener((minValue, maxValue) -> {

                                    textViewMin.setText(sign + " " + minValue);
                                    textViewMax.setText(sign + " " + maxValue);

                                    ConstantApi.pre_min_temp = String.valueOf(minValue);
                                    ConstantApi.pre_max_temp = String.valueOf(maxValue);

                                });

                            } else {
                                method.alertBox(priceSelectionRP.getMessage());
                            }

                        } catch (Exception e) {
                            Log.d(ConstantApi.exceptionError, e.toString());
                            method.alertBox(getResources().getString(R.string.failed_try_again));
                        }
                    }

                    progressBar.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(@NotNull Call<PriceSelectionRP> call, @NotNull Throwable t) {
                    // Log error here since request failed
                    Log.e(ConstantApi.failApi, t.toString());
                    progressBar.setVisibility(View.GONE);
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }
            });

        }
    }

}