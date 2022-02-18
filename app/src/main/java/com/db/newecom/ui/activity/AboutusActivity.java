package com.db.newecom.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.db.newecom.Api.ApiClient;
import com.db.newecom.Api.ApiInterface;
import com.db.newecom.BuildConfig;
import com.db.newecom.R;
import com.db.newecom.Response.AboutRP;
import com.db.newecom.Utills.API;
import com.db.newecom.Utills.Method;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutusActivity extends AppCompatActivity {

    private Method method;
    private ProgressBar progressBar;
    private RelativeLayout empty_layout;
    private LinearLayout ll_about_main;
    private ImageView app_logo, about_fb, about_insta, about_twitter, about_yt;
    private TextView app_name, app_tagline, web_url_txt, email_txt, contact_no_txt, app_developed_by;
    private WebView webview_about;
    private CardView website, email, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        method = new Method(AboutusActivity.this);

        progressBar = findViewById(R.id.progressBar_about);
        empty_layout = findViewById(R.id.empty_layout);
        ll_about_main = findViewById(R.id.ll_about_main);
        app_logo = findViewById(R.id.app_logo);
        about_fb = findViewById(R.id.about_fb);
        about_insta = findViewById(R.id.about_insta);
        about_twitter = findViewById(R.id.about_twitter);
        about_yt = findViewById(R.id.about_yt);
        app_name = findViewById(R.id.app_name);
        app_tagline = findViewById(R.id.app_tagline);
        web_url_txt = findViewById(R.id.web_url_txt);
        email_txt = findViewById(R.id.email_txt);
        contact_no_txt = findViewById(R.id.contact_no_txt);
        webview_about = findViewById(R.id.webview_about);
        app_developed_by = findViewById(R.id.app_developed_by);
        website = findViewById(R.id.website);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);

        ll_about_main.setVisibility(View.GONE);

        if (method.isNetworkAvailable(this)) {
            aboutUs();
        } else {
            progressBar.setVisibility(View.GONE);
            method.alertBox(getResources().getString(R.string.no_internet_connection));
        }
    }

    private void aboutUs() {

        progressBar.setVisibility(View.VISIBLE);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(AboutusActivity.this));
        ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<AboutRP> call = apiService.getAboutUs(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<AboutRP>() {
            @Override
            public void onResponse(Call<AboutRP> call, Response<AboutRP> response) {

                try {
                    AboutRP aboutUsRp = response.body();
                    assert aboutUsRp != null;

                    if (aboutUsRp.getStatus().equals("1")){

                        ll_about_main.setVisibility(View.VISIBLE);
                        empty_layout.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);

                        app_developed_by.setText(BuildConfig.Developed_by);

                        app_name.setText(aboutUsRp.getApp_author());
                        web_url_txt.setText(aboutUsRp.getApp_website());
                        email_txt.setText(aboutUsRp.getApp_email());
                        contact_no_txt.setText(aboutUsRp.getApp_contact());

                        website.setOnClickListener(view ->
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(aboutUsRp.getApp_website()))));
                        email.setOnClickListener(view -> {
                            Intent emailIntent = new Intent(Intent.ACTION_VIEW);
                            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{aboutUsRp.getApp_email()});
                            emailIntent.setData(Uri.parse("mailto:"));
                            startActivity(emailIntent);
                        });
                        phone.setOnClickListener(view -> {
                            Intent callIntent = new Intent(Intent.ACTION_DIAL);
                            callIntent.setData(Uri.parse("tel:"+ aboutUsRp.getApp_contact()));
                            startActivity(callIntent);
                        });

                        if (!aboutUsRp.getFacebook().equals(""))
                            about_fb.setOnClickListener(view ->
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(aboutUsRp.getFacebook()))));

                        if (!aboutUsRp.getInstagram().equals(""))
                            about_insta.setOnClickListener(view ->
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(aboutUsRp.getInstagram()))));

                        if (!aboutUsRp.getTwitter().equals(""))
                            about_twitter.setOnClickListener(view ->
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(aboutUsRp.getTwitter()))));

                        if (!aboutUsRp.getYoutube().equals(""))
                            about_yt.setOnClickListener(view ->
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(aboutUsRp.getYoutube()))));

                        webview_about.setBackgroundColor(Color.TRANSPARENT);
                        webview_about.setFocusableInTouchMode(false);
                        webview_about.setFocusable(false);
                        webview_about.getSettings().setDefaultTextEncodingName("UTF-8");
                        String mimeType = "text/html";
                        String encoding = "utf-8";

                        String text = "<html><head>"
                                + "<style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/poppins_medium.ttf\")}body{font-family: MyFont;color: " + getResources().getColor(R.color.dark_grey) + "line-height:1.6}"
                                + "a {color:" + getResources().getColor(R.color.dark_grey) + "text-decoration:none}"
                                + "</style></head>"
                                + "<body>"
                                + aboutUsRp.getApp_description()
                                + "</body></html>";

                        webview_about.loadDataWithBaseURL(null, text, mimeType, encoding, null);

                    }
                    else {
                        progressBar.setVisibility(View.GONE);
                        empty_layout.setVisibility(View.VISIBLE);
                    }

                }
                catch (Exception e){
                    Log.d("exception_error", e.toString());
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }

            }

            @Override
            public void onFailure(Call<AboutRP> call, Throwable t) {
                Log.e("fail_api", t.toString());
                empty_layout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                method.alertBox(getResources().getString(R.string.failed_try_again));
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home)
            onBackPressed();
        else
            return super.onOptionsItemSelected(item);

        return true;
    }
}