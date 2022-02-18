package com.db.newecom.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.db.newecom.Model.SliderList;
import com.db.newecom.Utills.EnchantedViewPager;
import com.db.newecom.Utills.Method;
import com.db.newecom.R;
import java.util.List;

public class SliderAdapter extends PagerAdapter {

    private Method method;
    private Activity activity;
    private String type;
    private List<SliderList> sliderLists;
    private LayoutInflater inflater;

    public SliderAdapter(Activity activity, String type, List<SliderList> sliderLists) {
        this.activity = activity;
        this.type = type;
        this.sliderLists = sliderLists;
        method = new Method(activity);

        inflater = activity.getLayoutInflater();
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View imageLayout = inflater.inflate(R.layout.item_slider, container, false);
        assert imageLayout != null;

        ImageView imageView = imageLayout.findViewById(R.id.imageView_slider_adapter);
        imageLayout.setTag(EnchantedViewPager.ENCHANTED_VIEWPAGER_POSITION + position);

        Glide.with(activity).load(sliderLists.get(position).getBanner_image())
                .placeholder(R.drawable.bordered_bg).into(imageView);

        imageView.setOnClickListener(v -> {

            Bundle args = new Bundle();
            args.putString("title", sliderLists.get(position).getBanner_title());
            args.putString("cat_id", sliderLists.get(position).getId());
            args.putString("type", "banners");
            Navigation.findNavController(imageLayout).navigate(R.id.navigate_to_products_fragment,args);

        });

        container.addView(imageLayout, 0);
        return imageLayout;
    }

    @Override
    public int getCount() {
        return sliderLists.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        (container).removeView((View) object);
    }
}
