package com.db.newecom.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.db.newecom.Api.ApiClient;
import com.db.newecom.Api.ApiInterface;
import com.db.newecom.R;
import com.db.newecom.Response.AddressListRP;
import com.db.newecom.Utills.API;
import com.db.newecom.Utills.Events;
import com.db.newecom.Utills.GlobalBus;
import com.db.newecom.Utills.Method;
import com.db.newecom.adapters.AddressAdapter;
import com.db.newecom.ui.activity.Add_Address_Activity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddressesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddressesFragment extends Fragment {

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
    private TextView empty_msg;
    private RecyclerView rv_addresses;
    private AddressAdapter addressAdapter;
    private CardView add_address_btn;

    public AddressesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddressesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddressesFragment newInstance(String param1, String param2) {
        AddressesFragment fragment = new AddressesFragment();
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
        View view = inflater.inflate(R.layout.fragment_addresses, container, false);

        GlobalBus.getBus().register(this);

        method = new Method(getActivity());
        progressBar = view.findViewById(R.id.ll_progress_myAdd_fragment);
        empty_layout = view.findViewById(R.id.empty_layout);
        empty_msg = view.findViewById(R.id.empty_msg);
        rv_addresses = view.findViewById(R.id.rv_addresses);
        add_address_btn = view.findViewById(R.id.add_address_btn);

        empty_msg.setText("No Addresses");

        add_address_btn.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), Add_Address_Activity.class)
                        .putExtra("type", "add_my_address")
                        .putExtra("isEdit", false)));

        if (method.isNetworkAvailable(getActivity())) {
            if (method.isLogin()) {
                addressList(method.userId());
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
    public void getData(Events.EventMYAddress eventMYAddress) {
        if (!eventMYAddress.getType().equals("removeAddress")) {
            addressList(method.userId());
        }  //textViewTotal.setText(eventMYAddress.getAddressCount() + " " + getResources().getString(R.string.address));

    }


    private void addressList(String userId) {

        if (getActivity() != null) {

            progressBar.setVisibility(View.VISIBLE);

            JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(getActivity()));
            jsObj.addProperty("user_id", userId);
            ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
            Call<AddressListRP> call = apiService.getAddressList(API.toBase64(jsObj.toString()));
            call.enqueue(new Callback<AddressListRP>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(@NotNull Call<AddressListRP> call, @NotNull Response<AddressListRP> response) {

                    if (getActivity() != null) {
                        try {

                            AddressListRP addressListRP = response.body();

                            assert addressListRP != null;

                            if (addressListRP.getStatus().equals("1")){

                                if (addressListRP.getAddressItemLists().size() != 0) {
                                    empty_layout.setVisibility(View.GONE);

                                    addressAdapter = new AddressAdapter(getActivity(), addressListRP.getAddressItemLists());
                                    rv_addresses.setAdapter(addressAdapter);

                                } else {
                                    empty_layout.setVisibility(View.VISIBLE);
                                }

                            } else {
                                empty_layout.setVisibility(View.VISIBLE);
                                method.alertBox(addressListRP.getMessage());
                            }

                        } catch (Exception e) {
                            Log.d("exception_error", e.toString());
                            empty_layout.setVisibility(View.VISIBLE);
                            method.alertBox(getResources().getString(R.string.failed_try_again));
                        }
                    }
                    progressBar.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(@NotNull Call<AddressListRP> call, @NotNull Throwable t) {
                    // Log error here since request failed
                    Log.e("fail_api", t.toString());
                    progressBar.setVisibility(View.GONE);
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