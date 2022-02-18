package com.db.newecom.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.db.newecom.Model.Address;
import com.db.newecom.R;
import com.db.newecom.interfaces.OnClickSend;
import com.db.newecom.ui.activity.Add_Address_Activity;

import java.util.List;

public class SelectAddressAdapter extends RecyclerView.Adapter<SelectAddressAdapter.ViewHolder> {

    private Activity activity;
    private OnClickSend onClickSend;
    private List<Address> addressItemLists;
    private int lastSelectedPosition = 0;

    public SelectAddressAdapter(Activity activity, OnClickSend onClickSend, List<Address> addressItemLists) {
        this.activity = activity;
        this.onClickSend = onClickSend;
        this.addressItemLists = addressItemLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_select_address, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.address_user_name.setText(addressItemLists.get(position).getName());
        holder.address_type.setText(addressItemLists.get(position).getAddress_type());
        holder.mobile.setText(addressItemLists.get(position).getMobile_no());
        holder.address.setText(addressItemLists.get(position).getAddress());

        holder.radiobtn_address.setChecked(lastSelectedPosition == position);

        if (lastSelectedPosition == position) {
            holder.edit_address_btn.setVisibility(View.VISIBLE);
            onClickSend.onClick(addressItemLists.get(position).getId(), "", "", position);
        } else {
            holder.edit_address_btn.setVisibility(View.GONE);
        }

        holder.edit_address_btn.setOnClickListener(view ->
                activity.startActivity(new Intent(activity, Add_Address_Activity.class)
                        .putExtra("type", "edit_change_add")
                        .putExtra("isEdit", true)
                        .putExtra("address_id", addressItemLists.get(position).getId())));
    }

    @Override
    public int getItemCount() {
        return addressItemLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout rl_select_address;
        private RadioButton radiobtn_address;
        private TextView address_user_name, address_type, mobile, address;
        private ImageButton edit_address_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rl_select_address = itemView.findViewById(R.id.rl_select_address);
            radiobtn_address = itemView.findViewById(R.id.radiobtn_address);
            address_user_name = itemView.findViewById(R.id.address_user_name);
            address_type = itemView.findViewById(R.id.address_type);
            mobile = itemView.findViewById(R.id.mobile);
            address = itemView.findViewById(R.id.address);
            edit_address_btn = itemView.findViewById(R.id.edit_address_btn);

            rl_select_address.setOnClickListener(view -> {
                lastSelectedPosition = getAdapterPosition();
                notifyDataSetChanged();
            });

            radiobtn_address.setOnClickListener(view -> {
                lastSelectedPosition = getAdapterPosition();
                notifyDataSetChanged();
            });

        }
    }
}
