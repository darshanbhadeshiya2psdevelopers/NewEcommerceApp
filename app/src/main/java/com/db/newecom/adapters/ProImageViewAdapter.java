package com.db.newecom.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.db.newecom.Model.ProImageList;
import com.db.newecom.R;
import java.util.List;

public class ProImageViewAdapter extends PagerAdapter {

    private Activity activity;
    private List<ProImageList> proImageLists;

    public ProImageViewAdapter(Activity activity, List<ProImageList> proImageLists) {
        this.activity = activity;
        this.proImageLists = proImageLists;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_slider, container, false);

        ImageView imageView = view.findViewById(R.id.imageView_slider_adapter);

        Glide.with(activity).load(proImageLists.get(position).getProduct_image_thumb())
                .placeholder(R.drawable.bordered_bg).into(imageView);

        container.addView(view);
        return view;

    }

    @Override
    public int getCount() {
        return proImageLists.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
