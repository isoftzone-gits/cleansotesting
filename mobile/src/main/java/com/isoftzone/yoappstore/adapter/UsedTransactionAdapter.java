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
import com.isoftzone.yoappstore.bean.RedeemAmount;
import com.isoftzone.yoappstore.bean.WalletBean;
import com.isoftzone.yoappstore.util.DateUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class UsedTransactionAdapter extends RecyclerView.Adapter<UsedTransactionAdapter.MyViewHolder> {

    private ArrayList<RedeemAmount> list;
    private Context context;
    private Listner listner;
    ImageLoader imageLoader;

    public interface Listner {
        void onClickRow(AddressListBean addressListBean);
        void onClickDelete(AddressListBean addressListBean);
    }

    public UsedTransactionAdapter(Context context, ArrayList<RedeemAmount> list, ImageLoader imageLoader) {
        this.list = list;
        this.context = context;
        this.listner = listner;
        this.imageLoader = imageLoader;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_usedtransaction, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final RedeemAmount bean = list.get(position);
        holder.titleTextView.setText("ORDER ID : " + bean.getOrderId());
        holder.amtTextView.setText("₹ " + new DecimalFormat(".##").format(Float.parseFloat(bean.getAmount())));
        holder.expiredOnTextView.setText("Date : " + DateUtils.modifyDateLayout(bean.getCreatedAt()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView, expiredOnTextView, amtTextView;

        public MyViewHolder(View view) {
            super(view);
            this.titleTextView = view.findViewById(R.id.titleTextView);
            this.amtTextView = view.findViewById(R.id.amtTextView);
            this.expiredOnTextView = view.findViewById(R.id.expiredOnTextView);
        }
    }
}
