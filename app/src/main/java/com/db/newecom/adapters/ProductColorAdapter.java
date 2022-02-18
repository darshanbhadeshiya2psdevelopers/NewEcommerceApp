package com.db.newecom.adapters;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.db.newecom.Model.ProColorList;
import com.db.newecom.R;
import com.db.newecom.ui.fragment.ProductDetailFragment;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class ProductColorAdapter extends RecyclerView.Adapter<ProductColorAdapter.ViewHolder> {

    private Activity activity;
    private List<ProColorList> proColorLists;
    private String pro_id;

    public ProductColorAdapter(Activity activity, List<ProColorList> proColorLists, String pro_id) {
        this.activity = activity;
        this.proColorLists = proColorLists;
        this.pro_id = pro_id;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.item_pro_color, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (pro_id.equals(proColorLists.get(position).getColor_id()))
            holder.cardview_pro_color.setBackground(activity.getResources().getDrawable(R.drawable.red_bordered_bg));

        Glide.with(activity).load(proColorLists.get(position).getColor_image())
                .placeholder(R.drawable.placeholder_img)
                .into(holder.pro_color_img);

        Bundle bundle = new Bundle();
        bundle.putString("title", proColorLists.get(position).getProduct_name());
        bundle.putString("product_id", proColorLists.get(position).getColor_id());
        Fragment fragment = new ProductDetailFragment();
        fragment.setArguments(bundle);

        if (position != 0){
            holder.cardview_pro_color.setOnClickListener(view -> {

                AppCompatActivity activity1 = (AppCompatActivity) activity;
                activity1.getSupportFragmentManager().beginTransaction()
                        .add(R.id.framelayout_for_pro_detail, fragment).addToBackStack(null).commit();

            });
        }
    }

    @Override
    public int getItemCount() {
        return proColorLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private MaterialCardView cardview_pro_color;
        private ImageView pro_color_img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardview_pro_color = itemView.findViewById(R.id.cardview_pro_color);
            pro_color_img = itemView.findViewById(R.id.pro_color_img);
        }
    }
}
