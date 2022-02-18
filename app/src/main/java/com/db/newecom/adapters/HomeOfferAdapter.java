package com.db.newecom.adapters;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.db.newecom.Model.OfferList;
import com.db.newecom.R;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class HomeOfferAdapter extends RecyclerView.Adapter<HomeOfferAdapter.ViewHolder> {

    private Activity activity;
    private List<OfferList> offerLists;

    public HomeOfferAdapter(Activity activity, List<OfferList> offerLists) {
        this.activity = activity;
        this.offerLists = offerLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_home_offer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(activity).load(offerLists.get(position).getOffer_image_thumb())
                .placeholder(R.drawable.placeholder_img)
                .into(holder.home_offer_img);

        holder.cardview_home_offer.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("title", offerLists.get(position).getOffer_title());
            bundle.putString("type", "offers");
            bundle.putString("cat_id", offerLists.get(position).getId());
            Navigation.findNavController(view).navigate(R.id.navigate_to_products_fragment, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return offerLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private MaterialCardView cardview_home_offer;
        private ImageView home_offer_img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardview_home_offer = itemView.findViewById(R.id.cardview_home_offer);
            home_offer_img = itemView.findViewById(R.id.home_offer_img);
        }
    }
}
