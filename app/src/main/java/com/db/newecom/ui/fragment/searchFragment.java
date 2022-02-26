package com.db.newecom.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.db.newecom.Api.ApiClient;
import com.db.newecom.Api.ApiInterface;
import com.db.newecom.R;
import com.db.newecom.Response.CategoryRP;
import com.db.newecom.Utills.API;
import com.db.newecom.Utills.ConstantApi;
import com.db.newecom.Utills.Method;
import com.db.newecom.adapters.CategoryAdapter;
import com.db.newecom.adapters.SearchCatAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link searchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class searchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private MenuItem menuitem;

    private Method method;
    private LinearLayout progressBar, ll_search_cat_main;
    private RecyclerView rv_search_category;
    private SearchCatAdapter searchCatAdapter;
    private SearchView searchview;

    public searchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment searchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static searchFragment newInstance(String param1, String param2) {
        searchFragment fragment = new searchFragment();
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
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        method = new Method(getActivity());

        searchview = view.findViewById(R.id.searchview);
        progressBar = view.findViewById(R.id.ll_progress_search);
        ll_search_cat_main = view.findViewById(R.id.ll_search_cat_main);
        rv_search_category = view.findViewById(R.id.rv_search_category);

        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Bundle args = new Bundle();
                args.putString("title", query);
                args.putString("search_keyword", query);
                args.putString("type", "search");
                Navigation.findNavController(view).navigate(R.id.navigate_to_products_fragment,args);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

        if (method.isNetworkAvailable(getActivity())){
            if (method.isLogin())
                getCategories(method.userId());
            else
                getCategories("0");
        }
        else {
            ll_search_cat_main.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            method.alertBox(getActivity().getResources().getString(R.string.no_internet_connection));
        }

        return view;
    }

    private void getCategories(String userId) {

        if (getActivity() != null){

            progressBar.setVisibility(View.VISIBLE);
            ll_search_cat_main.setVisibility(View.GONE);

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

                                if (categoryRP.getCategoryLists().size() == 0) {
                                    ll_search_cat_main.setVisibility(View.GONE);

                                } else {
                                    ll_search_cat_main.setVisibility(View.VISIBLE);

                                    searchCatAdapter = new SearchCatAdapter(getActivity(), categoryRP.getCategoryLists());
                                    rv_search_category.setAdapter(searchCatAdapter);
                                }

                            } else {
                                ll_search_cat_main.setVisibility(View.GONE);
                                method.alertBox(categoryRP.getMessage());
                            }

                        } catch (Exception e) {
                            Log.d(ConstantApi.exceptionError, e.toString());
                            ll_search_cat_main.setVisibility(View.GONE);
                            method.alertBox(getResources().getString(R.string.failed_try_again));
                        }
                    }

                    progressBar.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(@NotNull Call<CategoryRP> call, @NotNull Throwable t) {
                    // Log error here since request failed
                    Log.e(ConstantApi.failApi, t.toString());
                    ll_search_cat_main.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }
            });

        }

    }

//    @Override
//    public void onPrepareOptionsMenu(@NonNull Menu menu) {
//        super.onPrepareOptionsMenu(menu);
//        menuitem = menu.findItem(R.id.action_search);
//        if (menuitem!=null)
//            menuitem.setVisible(false);
//    }

    @Override
    public void onResume() {
        super.onResume();
        searchview.setIconifiedByDefault(true);
        searchview.setFocusable(true);
        searchview.setIconified(false);
        searchview.requestFocusFromTouch();
    }
}