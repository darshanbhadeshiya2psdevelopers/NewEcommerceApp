package com.db.newecom.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.db.newecom.Api.ApiClient;
import com.db.newecom.Api.ApiInterface;
import com.db.newecom.R;
import com.db.newecom.Response.DataRP;
import com.db.newecom.Response.RRSubmitRP;
import com.db.newecom.Response.RatingReviewRP;
import com.db.newecom.Utills.API;
import com.db.newecom.Utills.ConstantApi;
import com.db.newecom.Utills.Events;
import com.db.newecom.Utills.GlobalBus;
import com.db.newecom.Utills.Method;
import com.db.newecom.ui.activity.ProductDetailsActivity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;

import me.chensir.expandabletextview.ExpandableTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyReviewAdapter extends RecyclerView.Adapter<MyReviewAdapter.ViewHolder> {

    private Method method;
    private ProgressDialog progressDialog;
    private Activity activity;
    private boolean isProfile;
    private List<RatingReviewRP> myReviewsList;
    private int getRating;
    private String review_msg;

    public MyReviewAdapter(Activity activity, boolean isProfile, List<RatingReviewRP> myReviewsList) {
        this.activity = activity;
        this.isProfile = isProfile;
        this.myReviewsList = myReviewsList;
        method = new Method(activity);
        progressDialog = new ProgressDialog(activity, R.style.ProgressDialogStyle);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.item_my_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(activity).load(myReviewsList.get(position).getProduct_image())
                .placeholder(R.drawable.placeholder_img).into(holder.review_pro_img);

        Glide.with(activity).load(myReviewsList.get(position).getProduct_image())
                .placeholder(R.drawable.placeholder_img).into(holder.rate_dialog_pro_image);

        holder.review_pro_name.setText(myReviewsList.get(position).getProduct_title());

        holder.rate_dialog_pro_name.setText(myReviewsList.get(position).getProduct_title());

        holder.ratingBar.setRating(Float.parseFloat(myReviewsList.get(position).getRate()));

        holder.rate_dialog_ratingbar.setRating(Float.parseFloat(myReviewsList.get(position).getRate()));

        holder.expandableTextView.setText(myReviewsList.get(position).getRating_desc());

        holder.et_rate_dialog_msg.setText(myReviewsList.get(position).getRating_desc());

        holder.rate_date.setText(myReviewsList.get(position).getRating_date());

        holder.review_pro_img.setOnClickListener(view -> {
            activity.startActivity(new Intent(activity, ProductDetailsActivity.class)
                    .putExtra("title", myReviewsList.get(position).getProduct_title())
                    .putExtra("product_id", myReviewsList.get(position).getProduct_id()));
        });

        holder.review_pro_name.setOnClickListener(view -> {
            activity.startActivity(new Intent(activity, ProductDetailsActivity.class)
                    .putExtra("title", myReviewsList.get(position).getProduct_title())
                    .putExtra("product_id", myReviewsList.get(position).getProduct_id()));
        });

        if (isProfile){
            holder.review_delete_btn.setVisibility(View.GONE);
            holder.review_edit_btn.setVisibility(View.GONE);
        }

        holder.review_delete_btn.setOnClickListener(view ->
                deleteReview(method.userId(), myReviewsList.get(position).getReview_id(), position));

        holder.et_rate_dialog_msg.requestFocus();

        holder.review_edit_btn.setOnClickListener(view -> holder.dialog.show());

        holder.rate_dialog_btn.setOnClickListener(view ->
                submitEditRatingReview(holder ,method.userId(), myReviewsList.get(position).getProduct_id()));
    }

    private void deleteReview(String userId, String review_id, int position) {

        progressDialog.show();
        progressDialog.setMessage(activity.getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(activity));
        jsObj.addProperty("user_id", userId);
        jsObj.addProperty("id", review_id);
        ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<DataRP> call = apiService.deleteMyReview(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<DataRP>() {
            @Override
            public void onResponse(Call<DataRP> call, Response<DataRP> response) {
                try {

                    DataRP dataRP = response.body();

                    assert dataRP != null;
                    if (dataRP.getStatus().equals("1")) {

                        if (dataRP.getSuccess().equals("1")) {

                            myReviewsList.remove(position);
                            notifyDataSetChanged();

                        }

                        Toast.makeText(activity, dataRP.getMsg(), Toast.LENGTH_SHORT).show();

                    } else {
                        method.alertBox(dataRP.getMessage());
                    }

                } catch (Exception e) {
                    Log.d(ConstantApi.exceptionError, e.toString());
                    method.alertBox(activity.getResources().getString(R.string.failed_try_again));
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<DataRP> call, Throwable t) {
                // Log error here since request failed
                Log.e(ConstantApi.failApi, t.toString());
                progressDialog.dismiss();
                method.alertBox(activity.getResources().getString(R.string.failed_try_again));
            }
        });

    }

    public void submitEditRatingReview(ViewHolder holder ,String user_id, String product_id){

        getRating = (int) holder.rate_dialog_ratingbar.getRating();

        review_msg = holder.et_rate_dialog_msg.getText().toString();

        if (getRating == 0){
            Toast.makeText(activity,
                    activity.getResources().getString(R.string.please_give_rating),
                    Toast.LENGTH_SHORT).show();
        }
        else if (review_msg.equals("")){
            Toast.makeText(activity,
                    activity.getResources().getString(R.string.please_enter_review),
                    Toast.LENGTH_SHORT).show();
        }
        else {
            progressDialog.show();
            progressDialog.setMessage(activity.getResources().getString(R.string.loading));
            progressDialog.setCancelable(false);

            JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(activity));
            jsObj.addProperty("user_id", user_id);
            jsObj.addProperty("product_id", product_id);
            jsObj.addProperty("review_desc", review_msg);
            jsObj.addProperty("rate", getRating);
            ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
            Call<RRSubmitRP> call = apiService.submitRatingReview(API.toBase64(jsObj.toString()));
            call.enqueue(new Callback<RRSubmitRP>() {
                @Override
                public void onResponse(Call<RRSubmitRP> call, Response<RRSubmitRP> response) {

                    try {
                        RRSubmitRP rrSubmitRP = response.body();
                        assert rrSubmitRP != null;

                        if (rrSubmitRP.getStatus().equals("1")) {

                            if (rrSubmitRP.getSuccess().equals("1")) {

                                Events.RatingUpdate ratingUpdate = new Events.RatingUpdate(getRating, review_msg);
                                GlobalBus.getBus().post(ratingUpdate);

                                holder.dialog.dismiss();
                                notifyDataSetChanged();

                            }

                            Toast.makeText(activity, rrSubmitRP.getMsg(), Toast.LENGTH_SHORT).show();

                        } else {
                            holder.dialog.dismiss();
                            method.alertBox(rrSubmitRP.getMessage());
                        }

                    } catch (Exception e) {
                        Log.d(ConstantApi.exceptionError, e.toString());
                        holder.dialog.dismiss();
                        method.alertBox(activity.getResources().getString(R.string.failed_try_again));
                    }

                    progressDialog.dismiss();

                }

                @Override
                public void onFailure(Call<RRSubmitRP> call, Throwable t) {
                    Log.e(ConstantApi.failApi, t.toString());
                    holder.dialog.dismiss();
                    progressDialog.dismiss();
                    method.alertBox(activity.getResources().getString(R.string.failed_try_again));
                }
            });
        }
    }

    @Override
    public int getItemCount() {

        if (isProfile)
            return 1;
        else
            return myReviewsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView review_pro_img, review_delete_btn, review_edit_btn;
        private TextView review_pro_name, rate_date;
        private RatingBar ratingBar;
        private ExpandableTextView expandableTextView;
        private Dialog dialog;
        private ImageView rate_dialog_pro_image;
        private TextView rate_dialog_pro_name, rate_dialog_pro_desc;
        private RatingBar rate_dialog_ratingbar;
        private EditText et_rate_dialog_msg;
        private Button rate_dialog_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.product_rating_dialog);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            rate_dialog_pro_image = dialog.findViewById(R.id.rate_pro_img);
            rate_dialog_pro_name = dialog.findViewById(R.id.rate_pro_name);
            rate_dialog_pro_desc = dialog.findViewById(R.id.rate_pro_desc);
            rate_dialog_ratingbar = dialog.findViewById(R.id.rating_bar_of_dialog);
            et_rate_dialog_msg = dialog.findViewById(R.id.et_review_msg);
            rate_dialog_btn = dialog.findViewById(R.id.rate_submit_btn);

            rate_dialog_pro_desc.setVisibility(View.GONE);

            review_pro_img = itemView.findViewById(R.id.review_pro_img);
            review_delete_btn = itemView.findViewById(R.id.review_delete_btn);
            review_edit_btn = itemView.findViewById(R.id.review_edit_btn);
            review_pro_name = itemView.findViewById(R.id.review_pro_name);
            ratingBar = itemView.findViewById(R.id.review_pro_rating_bar);
            expandableTextView = itemView.findViewById(R.id.my_review_txt);
            rate_date = itemView.findViewById(R.id.rate_date);

        }
    }
}
