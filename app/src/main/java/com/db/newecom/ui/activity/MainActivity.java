package com.db.newecom.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.db.newecom.Api.ApiClient;
import com.db.newecom.Api.ApiInterface;
import com.db.newecom.R;
import com.db.newecom.Response.AppRP;
import com.db.newecom.Response.UserProfileRP;
import com.db.newecom.Utills.API;
import com.db.newecom.Utills.Events;
import com.db.newecom.Utills.GlobalBus;
import com.db.newecom.Utills.Method;
import com.db.newecom.Utills.ConstantApi;
import com.db.newecom.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.Subscribe;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Method method;
    private LinearLayout progressBar;
    private RelativeLayout empty_layout, rl_main;
    private CircleImageView user_image_home;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    public static Toolbar toolbar;
    private Menu menu;
    private BottomNavigationView bottomNavigationView;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar = binding.appBarMain.toolbar;
        setSupportActionBar(toolbar);

        GlobalBus.getBus().register(this);
        method = new Method(this);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        bottomNavigationView = binding.appBarMain.bottomNavbar;

        progressBar = binding.appBarMain.contentMain.llProgressMain;
        empty_layout = binding.appBarMain.contentMain.empty.emptyLayout;
        rl_main = binding.appBarMain.contentMain.rlMain;
        user_image_home = binding.appBarMain.userImageHome;


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_cat, R.id.nav_deal, R.id.nav_offer, R.id.nav_compare,
                R.id.nav_cart, R.id.nav_wishlist, R.id.nav_orders, R.id.nav_profile, R.id.nav_setting)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        user_image_home.setOnClickListener(view -> {
            if (method.isLogin())
                navController.navigate(R.id.nav_profile);
            else
                navController.navigate(R.id.navigate_to_login_activity);
        });

        bottomNavigationView.getOrCreateBadge(R.id.nav_cart).setVisible(false);
        bottomNavigationView.getOrCreateBadge(R.id.nav_cart).setBackgroundColor(getResources().getColor(R.color.red));

        bottomNavigationView.setOnNavigationItemReselectedListener(item -> {
            switch (item.getItemId()){
                case R.id.nav_home:
                    navController.navigate(R.id.nav_home);
                    break;
                case R.id.search_fragment:
                    navController.navigate(R.id.search_fragment);
                    break;
                case R.id.nav_wishlist:
                    navController.navigate(R.id.nav_wishlist);
                    break;
                case R.id.nav_compare:
                    navController.navigate(R.id.nav_compare);
                    break;
                case R.id.nav_cart:
                    navController.navigate(R.id.nav_cart);
                    break;
            }
        });

        if (method.isNetworkAvailable(this)) {
            if (method.isLogin()) {
                appDetail(method.userId());
                profile(method.userId());
            } else {
                appDetail("0");
            }
        } else {
            progressBar.setVisibility(View.GONE);
            rl_main.setVisibility(View.GONE);
            empty_layout.setVisibility(View.VISIBLE);
            method.alertBox(getResources().getString(R.string.no_internet_connection));
        }

    }

    @SuppressLint("SetTextI18n")
    @Subscribe
    public void geString(Events.CartItem cartItem){
        if (cartItem.getCart_item().equals("0")) {
            bottomNavigationView.getOrCreateBadge(R.id.nav_cart).setVisible(false);
        }
        else {
            bottomNavigationView.getOrCreateBadge(R.id.nav_cart).setVisible(true);
            bottomNavigationView.getOrCreateBadge(R.id.nav_cart).setNumber(Integer.parseInt(cartItem.getCart_item()));
        }
    }

    private void profile(String userId) {

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(this));
        jsObj.addProperty("user_id", userId);
        ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<UserProfileRP> call = apiService.getProfile(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<UserProfileRP>() {
            @Override
            public void onResponse(Call<UserProfileRP> call, Response<UserProfileRP> response) {
                try {

                    UserProfileRP userProfileRP = response.body();

                    assert userProfileRP != null;
                    if (userProfileRP.getStatus().equals("1")) {

                        if (userProfileRP.getSuccess().equals("1")) {

                            Glide.with(MainActivity.this).load(userProfileRP.getUser_image())
                                    .placeholder(R.drawable.default_profile).into(user_image_home);

                            if (!userProfileRP.getCart_items().equals("0")){
                                bottomNavigationView.getOrCreateBadge(R.id.nav_cart).setVisible(true);
                                bottomNavigationView.getOrCreateBadge(R.id.nav_cart)
                                        .setNumber(Integer.parseInt(userProfileRP.getCart_items()));
                            }
                            else
                                bottomNavigationView.getOrCreateBadge(R.id.nav_cart).setVisible(false);

                        } else {
                            method.alertBox(userProfileRP.getMsg());
                        }

                    } else {
                        method.alertBox(userProfileRP.getMessage());
                    }

                } catch (Exception e) {
                    Log.d("exception_error", e.toString());
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }
            }

            @Override
            public void onFailure(Call<UserProfileRP> call, Throwable t) {
                // Log error here since request failed
                Log.e("fail_api", t.toString());
                method.alertBox(getResources().getString(R.string.failed_try_again));
            }
        });

    }

    private void appDetail(String userId) {

        empty_layout.setVisibility(View.GONE);
        rl_main.setVisibility(View.VISIBLE);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(MainActivity.this));
        jsObj.addProperty("user_id", userId);
        ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<AppRP> call = apiService.getAppData(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<AppRP>() {
            @Override
            public void onResponse(Call<AppRP> call, Response<AppRP> response) {

                try {
                    AppRP appRP = response.body();
                    assert appRP != null;

                    if (appRP.getStatus().equals("1")) {

                        ConstantApi.currency = appRP.getApp_currency_code();

                    } else {
                        method.alertBox(appRP.getMessage());
                    }
                }
                catch (Exception e){
                    Log.d(ConstantApi.exceptionError, e.toString());
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.something_wrong),
                            Toast.LENGTH_SHORT).show();
                }

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<AppRP> call, Throwable t) {
                Log.e(ConstantApi.failApi, t.toString());
                rl_main.setVisibility(View.GONE);
                empty_layout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                method.alertBox(getResources().getString(R.string.failed_try_again));

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GlobalBus.getBus().unregister(this);
    }

    @Override
    public void onBackPressed() {

        int start = navController.getCurrentDestination().getId();

        if (start == R.id.nav_home){
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.DialogTitleTextStyle);
            builder.setMessage(getResources().getString(R.string.exit_msg));
            builder.setCancelable(false);
            builder.setPositiveButton(getResources().getString(R.string.yes),
                    (arg0, arg1) -> super.onBackPressed());
            builder.setNegativeButton(getResources().getString(R.string.no),
                    (dialog, which) -> dialog.dismiss());

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else
            super.onBackPressed();
    }
}