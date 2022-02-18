package com.db.newecom.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.db.newecom.R;
import com.db.newecom.Utills.ConstantApi;
import com.db.newecom.Utills.Method;
import com.db.newecom.adapters.ProImageViewAdapter;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class ProImageViewerActivity extends AppCompatActivity {

    private Method method;
    private ImageView back_btn;
    private ViewPager viewPager;
    private int selectedPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_image_viewer);

        method = new Method(this);

        back_btn = findViewById(R.id.back_btn);
        viewPager = findViewById(R.id.viewpager_pro_imageView);

        back_btn.setOnClickListener(view -> onBackPressed());

        selectedPosition = getIntent().getIntExtra("position", 0);

        ProImageViewAdapter proViewAdapter = new ProImageViewAdapter(this, ConstantApi.proImageLists);
        viewPager.setAdapter(proViewAdapter);

        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        setCurrentItem(selectedPosition);
    }

    private void setCurrentItem(int position) {
        viewPager.setCurrentItem(position, false);
    }

    //	page change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            selectedPosition = position;
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            Log.d("data_app", "");
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            Log.d("data_app", "");
        }
    };

}