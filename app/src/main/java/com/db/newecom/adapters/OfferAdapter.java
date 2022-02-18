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

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.ViewHolder> {

    private Activity activity;
    private List<OfferList> offerLists;

    public OfferAdapter(Activity activity, List<OfferList> offerLists) {
        this.activity = activity;
        this.offerLists = offerLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.item_offer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(activity).load(offerLists.get(position).getOffer_image_thumb())
                .placeholder(R.drawable.placeholder_img)
                .into(holder.offer_img);

        holder.cardview_offer.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("title", offerLists.get(position).getOffer_title());
            bundle.putString("cat_id", offerLists.get(position).getId());
            bundle.putString("type", "offers");
            Navigation.findNavController(view).navigate(R.id.navigate_to_products_fragment, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return offerLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private MaterialCardView cardview_offer;
        private ImageView offer_img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardview_offer = itemView.findViewById(R.id.cardview_offer);
            offer_img = itemView.findViewById(R.id.offer_img);
        }
    }
}
