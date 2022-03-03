package com.db.newecom.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.db.newecom.R;
import com.db.newecom.ui.fragment.ProductDetailFragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

public class ProductDetailsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FrameLayout cart_btn;
    private String title, product_id;
    public TextView order_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = getIntent().getStringExtra("title");
        product_id = getIntent().getStringExtra("product_id");

        order_count = findViewById(R.id.order_count);
        cart_btn = findViewById(R.id.cart_btn);

        cart_btn.setOnClickListener(view ->
                startActivity(new Intent(this, CartActivity.class)));

        // ATTENTION: This was auto-generated to handle app links.
        handleIntent();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent();
    }

    private void handleIntent() {
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
        if (appLinkData != null){
            String slug = appLinkData.getLastPathSegment();
            getdata(slug,"", "");
        }else
            getdata("", title, product_id);
    }

    private void getdata(String slug, String title, String product_id){

        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("product_id", product_id);
        bundle.putString("slug", slug);

        Fragment fragment = new ProductDetailFragment();
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().add(R.id.framelayout_for_pro_detail, fragment).commit();
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