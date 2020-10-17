package com.isoftzone.yoappstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.isoftzone.yoappstore.R;
import com.isoftzone.yoappstore.bean.AddressListBean;

import java.util.ArrayList;

public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.MyViewHolder> {

    private ArrayList<AddressListBean> list;
    private Context context;
    private Listner listner;
    int selectedPos = -1;

    public interface Listner {
        void onClickRow(AddressListBean addressListBean);
        void onClickDelete(AddressListBean addressListBean);
    }

    public AddressListAdapter(Context context, ArrayList<AddressListBean> list, Listner listner) {
        this.list = list;
        this.context = context;
        this.listner = listner;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_address, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final AddressListBean bean = list.get(position);
        holder.titleTextView.setText(" " + bean.getAddrType());
        String address = "";
        if (bean.getHouseNo() != null && !bean.getHouseNo().equalsIgnoreCase("")) {
            address = address + bean.getHouseNo();
        }
        if (bean.getFloor() != null && !bean.getFloor().equalsIgnoreCase("")) {
            address = address + ", " + bean.getFloor();
        }
        if (bean.getAddress() != null && !bean.getAddress().equalsIgnoreCase("")) {
            address = address + ", " + bean.getAddress();
        }
        if (bean.getAreaName() != null && !bean.getAreaName().equalsIgnoreCase("")) {
            address = address + " , " + bean.getAreaName();
        }
        if (bean.getCity() != null && !bean.getCity().equalsIgnoreCase("")) {
            address = address + " , " + bean.getCity();
        }
        if (bean.getLandmark() != null && !bean.getLandmark().equalsIgnoreCase("")) {
            address = address + " , " + bean.getLandmark();
        }
        holder.fullAddressTextView.setText(address);
        if (bean.isSelected()) {
            selectedPos = position;
            holder.radioButton.setChecked(true);
        } else {
            holder.radioButton.setChecked(false);
        }

        holder.deleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.onClickDelete(bean);
            }
        });

        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.onClickRow(bean);
            }
        });
        holder.mainLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPos != -1) {
                    list.get(selectedPos).setSelected(false);
                }
                selectedPos = position;
                bean.setSelected(true);
                notifyDataSetChanged();
                listner.onClickRow(bean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView, deleteTextView, fullAddressTextView;
        private LinearLayout mainLinearLayout;
        RadioButton radioButton;
        public MyViewHolder(View view) {
            super(view);
            this.mainLinearLayout = view.findViewById(R.id.mainLinearLayout);
            this.titleTextView = view.findViewById(R.id.titleTextView);
            this.deleteTextView = view.findViewById(R.id.deleteTextView);
            this.fullAddressTextView = view.findViewById(R.id.fullAddressTextView);
            this.radioButton = view.findViewById(R.id.radioButton);
        }
    }
}
