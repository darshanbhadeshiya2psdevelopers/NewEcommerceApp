package com.db.newecom.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.db.newecom.Api.ApiClient;
import com.db.newecom.Api.ApiInterface;
import com.db.newecom.R;
import com.db.newecom.Response.BankDetailsRP;
import com.db.newecom.Utills.API;
import com.db.newecom.Utills.Events;
import com.db.newecom.Utills.GlobalBus;
import com.db.newecom.Utills.Method;
import com.db.newecom.adapters.BankAccountAdapter;
import com.db.newecom.ui.activity.Add_Bank_Activity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.Subscribe;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BankAccountsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BankAccountsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Method method;
    private RelativeLayout empty_layout;
    private TextView empty_msg;
    private ProgressBar progressBar;
    private CardView add_bank_btn;
    private RecyclerView rv_bank_accounts;
    private BankAccountAdapter bankAccountAdapter;

    public BankAccountsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BankAccountsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BankAccountsFragment newInstance(String param1, String param2) {
        BankAccountsFragment fragment = new BankAccountsFragment();
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
        View view = inflater.inflate(R.layout.fragment_bank_accounts, container, false);

        GlobalBus.getBus().register(this);
        method = new Method(getActivity());

        empty_layout = view.findViewById(R.id.empty_layout);
        empty_msg = view.findViewById(R.id.empty_msg);
        progressBar = view.findViewById(R.id.progressBar_myBank_fragment);
        add_bank_btn = view.findViewById(R.id.add_bank_btn);
        rv_bank_accounts = view.findViewById(R.id.rv_bank_accounts);

        empty_msg.setText("No Bank Accounts");

        add_bank_btn.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), Add_Bank_Activity.class)));

        if (method.isNetworkAvailable(getActivity())) {
            if (method.isLogin()) {
                bankList(method.userId());
            } else {
                progressBar.setVisibility(View.GONE);
                empty_layout.setVisibility(View.VISIBLE);
                method.alertBox(getResources().getString(R.string.you_have_not_login));
            }
        } else {
            progressBar.setVisibility(View.GONE);
            empty_layout.setVisibility(View.VISIBLE);
            method.alertBox(getResources().getString(R.string.no_internet_connection));
        }

        return view;
    }

    @Subscribe
    public void getData(Events.EventBankDetail eventBankDetail) {
        if (eventBankDetail.getType().equals("addEditBank")) {
            bankList(method.userId());
        }
    }

    private void bankList(String userId) {

        if (getActivity() != null) {

            progressBar.setVisibility(View.VISIBLE);

            JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(getActivity()));
            jsObj.addProperty("user_id", userId);
            ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
            Call<BankDetailsRP> call = apiService.getBankList(API.toBase64(jsObj.toString()));
            call.enqueue(new Callback<BankDetailsRP>() {
                @Override
                public void onResponse(Call<BankDetailsRP> call, Response<BankDetailsRP> response) {

                    if (getActivity() != null) {

                        try {
                            BankDetailsRP bankDetailRP = response.body();
                            assert bankDetailRP != null;

                            if (bankDetailRP.getStatus().equals("1")) {

                                if (bankDetailRP.getSuccess().equals("1")) {

                                    if (bankDetailRP.getBankDetailLists().size() != 0) {
                                        empty_layout.setVisibility(View.GONE);
                                        bankAccountAdapter = new BankAccountAdapter(getActivity(), bankDetailRP.getBankDetailLists());
                                        rv_bank_accounts.setAdapter(bankAccountAdapter);

                                    } else {
                                        empty_layout.setVisibility(View.VISIBLE);
                                    }

                                } else {
                                    Toast.makeText(getActivity(), bankDetailRP.getMsg(), Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                empty_layout.setVisibility(View.VISIBLE);
                                method.alertBox(bankDetailRP.getMessage());
                            }

                        } catch (Exception e) {
                            Log.d("exception_error", e.toString());
                            method.alertBox(getResources().getString(R.string.failed_try_again));
                        }

                    }

                    progressBar.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(Call<BankDetailsRP> call, Throwable t) {
                    // Log error here since request failed
                    Log.e("fail_api", t.toString());
                    empty_layout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
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