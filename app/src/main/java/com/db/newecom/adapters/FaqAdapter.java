package com.db.newecom.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.db.newecom.Model.Faq;
import com.db.newecom.R;

import java.util.List;

public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.ViewHolder> {

    private Activity activity;
    private List<Faq> faqLists;

    public FaqAdapter(Activity activity, List<Faq> faqLists) {
        this.activity = activity;
        this.faqLists = faqLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.item_faq, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.faq_title.setText(faqLists.get(position).getQuestion());
        holder.faq_detail.setText(faqLists.get(position).getAnswer());
    }

    @Override
    public int getItemCount() {
        return faqLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView faq_title, faq_detail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            faq_title = itemView.findViewById(R.id.faq_title);
            faq_detail = itemView.findViewById(R.id.faq_detail);

        }
    }
}
