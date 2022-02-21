package com.db.newecom.ui.fragment;

import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.db.newecom.Api.ApiClient;
import com.db.newecom.Api.ApiInterface;
import com.db.newecom.Model.BrandList;
import com.db.newecom.Model.CategoryList;
import com.db.newecom.Model.HomeCatSubProList;
import com.db.newecom.Model.MyOrders;
import com.db.newecom.Model.OfferList;
import com.db.newecom.Model.ProductList;
import com.db.newecom.Model.SliderList;
import com.db.newecom.R;
import com.db.newecom.Response.HomeRP;
import com.db.newecom.Utills.API;
import com.db.newecom.Utills.EnchantedViewPager;
import com.db.newecom.Utills.Method;
import com.db.newecom.adapters.HomeBrandAdapter;
import com.db.newecom.adapters.HomeCatAdapter;
import com.db.newecom.adapters.HomeLatestProAdapter;
import com.db.newecom.adapters.HomeMyOrdersAdapter;
import com.db.newecom.adapters.HomeOfferAdapter;
import com.db.newecom.adapters.HomeProOfCatAdapter;
import com.db.newecom.adapters.HomeTodaysDealAdapter;
import com.db.newecom.adapters.SliderAdapter;
import com.db.newecom.ui.activity.MainActivity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Method method;
    private ProgressBar progressBar;
    private Menu menu;
    private RelativeLayout empty_layout, rl_home_sliders, relIndicator;
    private LinearLayout search_layout, ll_offer_home, ll_myorders_home, ll_todaysdeal_home, ll_brands_home,
            ll_latest_pro_home, ll_viewall_offer, ll_viewall_order, ll_viewall_todaysdeal, ll_viewall_latestpro,
            ll_explore_cat;
    private NestedScrollView scrollView;
    private SearchView searchview;
    boolean searchVisible = false;

    private SwipeRefreshLayout swipeRefreshLayout;

    private List<CategoryList> categoryLists;
    private List<MyOrders> myOrderLists;
    private List<BrandList> brandLists;
    private List<SliderList> sliderLists;
    private List<ProductList> todayDealLists;
    private List<ProductList> latestLists;
    private List<ProductList> topRatedLists;
    private List<HomeCatSubProList> homeCatSubProLists;
    private List<ProductList> recentList;
    private List<OfferList> offerLists;

    private EnchantedViewPager enchantedViewPager;
    private TextView welcome_user_msg;
    private RecyclerView rv_cat_home, rv_offer_home, rv_myorder_home, rv_todaysdeal_home, rv_brand_home,
            rv_latestpro_home, rv_proofcat_home;
    private SliderAdapter sliderAdapter;
    private HomeCatAdapter homeCatAdapter;
    private HomeOfferAdapter homeOfferAdapter;
    private HomeMyOrdersAdapter homeMyOrdersAdapter;
    private HomeTodaysDealAdapter homeTodaysDealAdapter;
    private HomeBrandAdapter homeBrandAdapter;
    private HomeLatestProAdapter homeLatestProAdapter;
    private HomeProOfCatAdapter homeProOfCatAdapter;

    private Timer timer;
    private Runnable update;
    private final long delayMs = 1500;//delay in milliseconds before task is to be executed
    private final long periodMs = 3000;
    private final Handler handler = new Handler();

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        method = new Method(getActivity());

        categoryLists = new ArrayList<>();
        myOrderLists = new ArrayList<>();
        brandLists = new ArrayList<>();
        sliderLists = new ArrayList<>();
        todayDealLists = new ArrayList<>();
        latestLists = new ArrayList<>();
        topRatedLists = new ArrayList<>();
        homeCatSubProLists = new ArrayList<>();
        recentList = new ArrayList<>();
        offerLists = new ArrayList<>();

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_home);
        progressBar = view.findViewById(R.id.progressBar_home);
        empty_layout = view.findViewById(R.id.empty_layout);
        welcome_user_msg = view.findViewById(R.id.welcome_user_msg);
        ll_viewall_offer = view.findViewById(R.id.ll_viewall_offer);
        ll_viewall_order = view.findViewById(R.id.ll_viewall_order);
        ll_viewall_todaysdeal = view.findViewById(R.id.ll_viewall_todaysdeal);
        ll_viewall_latestpro = view.findViewById(R.id.ll_viewall_latestpro);
        ll_explore_cat = view.findViewById(R.id.ll_explore_cat);
        rl_home_sliders = view.findViewById(R.id.rl_home_sliders);
        enchantedViewPager = view.findViewById(R.id.viewPager_home);
        relIndicator = view.findViewById(R.id.rel_indicator_home);
        ll_offer_home = view.findViewById(R.id.ll_offer_home);
        ll_myorders_home = view.findViewById(R.id.ll_myorders_home);
        ll_todaysdeal_home = view.findViewById(R.id.ll_todaysdeal_home);
        ll_brands_home = view.findViewById(R.id.ll_brands_home);
        ll_latest_pro_home = view.findViewById(R.id.ll_latest_pro_home);
        scrollView = view.findViewById(R.id.scrollview);
        search_layout = view.findViewById(R.id.search_layout);
        searchview = view.findViewById(R.id.searchview);

        swipeRefreshLayout.setColorSchemeColors(getActivity().getResources().getColor(R.color.dark_blue));

//        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                Rect scrollBounds = new Rect();
//                scrollView.getHitRect(scrollBounds);
//                if (!search_layout.getLocalVisibleRect(scrollBounds) || scrollBounds.height() < search_layout.getHeight()) {
//                    searchVisible = true;
//                    menu.findItem(R.id.action_search).setVisible(true);
//                } else {
//                    searchVisible = false;
//                    menu.findItem(R.id.action_search).setVisible(false);
//                }
//                getActivity().invalidateOptionsMenu();
//            }
//        });

        MainActivity mainActivity = new MainActivity();

        if (method.isLogin())
            welcome_user_msg.setText("Welcome, " + method.userName() + "...");

        search_layout.setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.navigate_to_search_fragment));
        searchview.setOnSearchClickListener(v -> {
            searchview.setIconified(true);
            searchview.onActionViewCollapsed();
            Navigation.findNavController(view).navigate(R.id.navigate_to_search_fragment);
        });

        Bundle bundle = new Bundle();

        ll_viewall_offer.setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.nav_offer));

        ll_viewall_order.setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.nav_orders));

        ll_viewall_todaysdeal.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.nav_deal);
        });

        ll_viewall_latestpro.setOnClickListener(v -> {
            bundle.putString("title", getActivity().getResources().getString(R.string.latest_products));
            bundle.putString("type", "latest_pro");
            Navigation.findNavController(view).navigate(R.id.navigate_to_products_fragment, bundle);
        });

        ll_explore_cat.setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.nav_cat));

        rv_cat_home = view.findViewById(R.id.rv_cat_home);
        rv_offer_home = view.findViewById(R.id.rv_offer_home);
        rv_myorder_home = view.findViewById(R.id.rv_myorder_home);
        rv_todaysdeal_home = view.findViewById(R.id.rv_todaysdeal_home);
        rv_brand_home = view.findViewById(R.id.rv_brand_home);
        rv_latestpro_home = view.findViewById(R.id.rv_latestpro_home);
        rv_proofcat_home = view.findViewById(R.id.rv_proofcat_home);

        rv_todaysdeal_home.setNestedScrollingEnabled(false);

        if (method.isNetworkAvailable(getActivity())){
            if (method.isLogin())
                home(method.userId());
            else
                home("0");
        }
        else {
            progressBar.setVisibility(View.GONE);
            empty_layout.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
        }

        swipeRefreshLayout.setOnRefreshListener(() -> {

            scrollView.setVisibility(View.GONE);

            if (method.isNetworkAvailable(getActivity())){
                if (method.isLogin())
                    home(method.userId());
                else
                    home("0");
            }
            else {
                progressBar.setVisibility(View.GONE);
                empty_layout.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.GONE);
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void home(String userId) {

        if (getActivity() != null) {

            sliderLists.clear();
            categoryLists.clear();
            brandLists.clear();
            todayDealLists.clear();
            recentList.clear();
            homeCatSubProLists.clear();
            offerLists.clear();
            myOrderLists.clear();
            latestLists.clear();
            topRatedLists.clear();

            scrollView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(getActivity()));
            jsObj.addProperty("user_id", userId);
            ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
            Call<HomeRP> call = apiService.getHome(API.toBase64(jsObj.toString()));
            call.enqueue(new Callback<HomeRP>() {
                @Override
                public void onResponse(Call<HomeRP> call, Response<HomeRP> response) {
                    if (getActivity() != null) {
                        try {
                            HomeRP homeRP = response.body();

                            assert homeRP != null;
                            if (homeRP.getStatus().equals("1")) {

                                swipeRefreshLayout.setRefreshing(false);

                                scrollView.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                empty_layout.setVisibility(View.GONE);

                                sliderLists.addAll(homeRP.getSliderLists());
                                myOrderLists.addAll(homeRP.getMyOrderLists());
                                categoryLists.add((CategoryList) null);
                                categoryLists.addAll(homeRP.getCategoryLists());
                                brandLists.addAll(homeRP.getBrandLists());
                                todayDealLists.addAll(homeRP.getTodayLists());
                                latestLists.addAll(homeRP.getLatestLists());
//                                topRatedLists.addAll(homeRP.getTopRatedLists());
                                homeCatSubProLists.addAll(homeRP.getHomeCatSubProLists());

                                offerLists.addAll(homeRP.getOfferLists());
//                                recentList.addAll(homeRP.getRecentViewList());

                                if (sliderLists.size() != 0) {

                                    int columnWidth = method.getScreenWidth();
                                    enchantedViewPager.setLayoutParams(new RelativeLayout.LayoutParams(columnWidth, columnWidth / 2));

                                    enchantedViewPager.useScale();
                                    enchantedViewPager.removeAlpha();

                                    if (sliderLists.size() > 1) {
                                        relIndicator.setVisibility(View.VISIBLE);
                                    } else {
                                        relIndicator.setVisibility(View.GONE);
                                    }

                                    sliderAdapter = new SliderAdapter(getActivity(), "slider", sliderLists);
                                    enchantedViewPager.setAdapter(sliderAdapter);

                                    update = () -> {
                                        if (enchantedViewPager.getCurrentItem() == (sliderAdapter.getCount() - 1)) {
                                            enchantedViewPager.setCurrentItem(0, true);
                                        } else {
                                            enchantedViewPager.setCurrentItem(enchantedViewPager.getCurrentItem() + 1, true);
                                        }
                                    };

                                    if (sliderAdapter.getCount() > 1) {
                                        timer = new Timer(); // This will create a new Thread
                                        timer.schedule(new TimerTask() { // task to be scheduled
                                            @Override
                                            public void run() {
                                                handler.post(update);
                                            }
                                        }, delayMs, periodMs);
                                    }

                                } else {
                                    rl_home_sliders.setVisibility(View.GONE);
                                }

                                if (categoryLists.size() != 0) {
                                    homeCatAdapter = new HomeCatAdapter(getActivity(), categoryLists);
                                    rv_cat_home.setAdapter(homeCatAdapter);
                                } else {
                                    rv_cat_home.setVisibility(View.GONE);
                                }

                                if (offerLists.size() != 0) {
                                    homeOfferAdapter = new HomeOfferAdapter(getActivity(), offerLists);
                                    rv_offer_home.setAdapter(homeOfferAdapter);
                                } else {
                                    ll_offer_home.setVisibility(View.GONE);
                                }

                                if (myOrderLists.size() != 0) {
                                    homeMyOrdersAdapter = new HomeMyOrdersAdapter(getActivity(), myOrderLists, false);
                                    rv_myorder_home.setAdapter(homeMyOrdersAdapter);
                                } else {
                                    ll_myorders_home.setVisibility(View.GONE);
                                }

                                if (todayDealLists.size() != 0) {
                                    homeTodaysDealAdapter = new HomeTodaysDealAdapter(getActivity(), todayDealLists);
                                    rv_todaysdeal_home.setAdapter(homeTodaysDealAdapter);
                                } else {
                                    ll_todaysdeal_home.setVisibility(View.GONE);
                                }

                                if (brandLists.size() != 0) {
                                    homeBrandAdapter = new HomeBrandAdapter(getActivity(), brandLists);
                                    rv_brand_home.setAdapter(homeBrandAdapter);
                                } else {
                                    ll_brands_home.setVisibility(View.GONE);
                                }

                                if (latestLists.size() != 0) {
                                    homeLatestProAdapter = new HomeLatestProAdapter(getActivity(), latestLists);
                                    rv_latestpro_home.setAdapter(homeLatestProAdapter);
                                } else {
                                    ll_latest_pro_home.setVisibility(View.GONE);
                                }

                                if (homeCatSubProLists.size() != 0) {
                                    rv_proofcat_home.setVisibility(View.VISIBLE);
                                    homeProOfCatAdapter = new HomeProOfCatAdapter(getActivity(), homeCatSubProLists);
                                    rv_proofcat_home.setAdapter(homeProOfCatAdapter);
                                } else {
                                    rv_proofcat_home.setVisibility(View.GONE);
                                }
                            }
                            else {
                                method.alertBox(homeRP.getMessage());
                            }
                        }
                        catch (Exception e){
                            Log.d("exception_error", e.toString());
                            scrollView.setVisibility(View.GONE);
                            empty_layout.setVisibility(View.VISIBLE);
                            method.alertBox("exception_error");
                        }

                        progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<HomeRP> call, Throwable t) {
                    Log.e("fail_api", t.toString());
                    progressBar.setVisibility(View.GONE);
                    scrollView.setVisibility(View.GONE);
                    empty_layout.setVisibility(View.VISIBLE);
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }
            });

        }

    }

//    @Override
//    public void onPrepareOptionsMenu(@NonNull Menu menu) {
//        this.menu = menu;
//        super.onPrepareOptionsMenu(menu);
//        menu.findItem(R.id.action_search).setVisible(searchVisible);
//    }
}