package com.db.newecom.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.db.newecom.Model.ProReviewList;
import com.db.newecom.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.chensir.expandabletextview.ExpandableTextView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private Activity activity;
    private List<ProReviewList> proReviewLists;

    public ReviewAdapter(Activity activity, List<ProReviewList> proReviewLists) {
        this.activity = activity;
        this.proReviewLists = proReviewLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.item_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(activity).load(proReviewLists.get(position).getUser_image())
                .placeholder(R.drawable.bordered_bg).into(holder.rate_user_img);

        holder.rate_user_name.setText(proReviewLists.get(position).getUser_name());
        holder.rate_date.setText(proReviewLists.get(position).getRate_date());
        holder.review_ratingbar.setRating(Float.parseFloat(proReviewLists.get(position).getUser_rate()));
        holder.expandableTextView.setText(proReviewLists.get(position).getRate_desc());
    }

    @Override
    public int getItemCount() {
        return proReviewLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView rate_user_img;
        private TextView rate_user_name, rate_date;
        private RatingBar review_ratingbar;
        private ExpandableTextView expandableTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rate_user_img = itemView.findViewById(R.id.rate_user_img);
            rate_user_name = itemView.findViewById(R.id.rate_user_name);
            rate_date = itemView.findViewById(R.id.rate_date);
            review_ratingbar = itemView.findViewById(R.id.review_ratingbar);
            expandableTextView = itemView.findViewById(R.id.review_txt);
        }
    }
}
