package com.isoftzone.yoappstore.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.isoftzone.yoappstore.R;
import com.isoftzone.yoappstore.activity.OrderDetailActivity;
import com.isoftzone.yoappstore.bean.OrdersBean;
import com.isoftzone.yoappstore.util.DateUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.MyViewHolder> {

    private ArrayList<OrdersBean> ordersBeanArrayList;
    private Context context;
    protected ImageLoader imageLoader;
    Listner listner;

    public interface Listner {
        void onClickCancelOrder(OrdersBean ordersBean);
        void onClickOrderDetail(OrdersBean ordersBean);
    }

    public OrdersAdapter(ArrayList<OrdersBean> ordersBeanArrayList, ImageLoader imageLoader, Context context, Listner listner) {
        this.ordersBeanArrayList = ordersBeanArrayList;
        this.imageLoader = imageLoader;
        this.context = context;
        this.listner = listner;
    }

    @NonNull
    @Override
    public OrdersAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final OrdersBean bean = ordersBeanArrayList.get(position);
        if (bean.getDeliveryType() != null && bean.getDeliveryType().equalsIgnoreCase("Self Pickup")) {
            holder.addressTextView.setText(bean.getDeliveryType());
        } else {
            holder.addressTextView.setText(bean.getAddress());
        }
        holder.dateTextView.setText(DateUtils.newDateMDYLayoutStyle(bean.getCreatedAt()));
        holder.orderStatusTextView.setText(bean.getStatus());
        if (bean.isCollapse()) {
            holder.itemsRecyclerView.setVisibility(View.GONE);
        } else {
            holder.itemsRecyclerView.setVisibility(View.VISIBLE);
        }
        if (bean.getTxStatus() != null && bean.getTxStatus().trim().equalsIgnoreCase("SUCCESS")) {
            holder.paymentStatusTextView.setVisibility(View.VISIBLE);
            holder.paymentViaTextView.setVisibility(View.VISIBLE);
            holder.paymentDateTextView.setVisibility(View.VISIBLE);
            holder.paymentStatusTextView.setText("Payment Status : " + bean.getTxStatus());
            holder.paymentViaTextView.setText("Payment Through : " + (bean.getPaymentMode() != null ? bean.getPaymentMode() : ""));
            if (bean.getTxtime() != null && !bean.getTxtime().trim().equalsIgnoreCase("")) {
                holder.paymentDateTextView.setText("Payment Date : " + ((bean.getTxtime() != null && !bean.getTxtime().trim().equalsIgnoreCase("")) ? DateUtils.dateFormate(bean.getTxtime()) : ""));
            } else {
                holder.paymentDateTextView.setVisibility(View.GONE);
            }
            Log.e("getTxtime", "="+bean.getTxtime());
        } else {
            holder.paymentStatusTextView.setVisibility(View.GONE);
            holder.paymentViaTextView.setVisibility(View.GONE);
            holder.paymentDateTextView.setVisibility(View.GONE);
        }

        if (bean.getStatus().equalsIgnoreCase("placed")) {
            holder.cancelTextView.setVisibility(View.VISIBLE);
            holder.placedTextView.setTextColor(context.getResources().getColor(R.color.fontGreen));
            holder.progressTextView.setTextColor(context.getResources().getColor(R.color.darkGray));
            holder.dispatchedextView.setTextColor(context.getResources().getColor(R.color.darkGray));
            holder.deliveredTextView.setTextColor(context.getResources().getColor(R.color.darkGray));
            holder.firstImageView.setBackgroundResource(R.drawable.graybg);
            holder.secondImageView.setBackgroundResource(R.drawable.graybg);
            holder.thirdImageView.setBackgroundResource(R.drawable.graybg);

        } else if (bean.getStatus().equalsIgnoreCase("progress")) {
            holder.cancelTextView.setVisibility(View.GONE);
            holder.placedTextView.setTextColor(context.getResources().getColor(R.color.fontGreen));
            holder.progressTextView.setTextColor(context.getResources().getColor(R.color.fontGreen));
            holder.dispatchedextView.setTextColor(context.getResources().getColor(R.color.darkGray));
            holder.deliveredTextView.setTextColor(context.getResources().getColor(R.color.darkGray));
            holder.firstImageView.setBackgroundResource(R.drawable.greenbg);
            holder.secondImageView.setBackgroundResource(R.drawable.graybg);
            holder.thirdImageView.setBackgroundResource(R.drawable.graybg);
        } else if (bean.getStatus().equalsIgnoreCase("dispatched")) {
            holder.cancelTextView.setVisibility(View.GONE);
            holder.placedTextView.setTextColor(context.getResources().getColor(R.color.fontGreen));
            holder.progressTextView.setTextColor(context.getResources().getColor(R.color.fontGreen));
            holder.dispatchedextView.setTextColor(context.getResources().getColor(R.color.fontGreen));
            holder.deliveredTextView.setTextColor(context.getResources().getColor(R.color.darkGray));
            holder.firstImageView.setBackgroundResource(R.drawable.greenbg);
            holder.secondImageView.setBackgroundResource(R.drawable.greenbg);
            holder.thirdImageView.setBackgroundResource(R.drawable.graybg);
        } else {
            holder.cancelTextView.setVisibility(View.GONE);
            holder.placedTextView.setTextColor(context.getResources().getColor(R.color.fontGreen));
            holder.progressTextView.setTextColor(context.getResources().getColor(R.color.fontGreen));
            holder.dispatchedextView.setTextColor(context.getResources().getColor(R.color.fontGreen));
            holder.deliveredTextView.setTextColor(context.getResources().getColor(R.color.fontGreen));
            holder.firstImageView.setBackgroundResource(R.drawable.greenbg);
            holder.secondImageView.setBackgroundResource(R.drawable.greenbg);
            holder.thirdImageView.setBackgroundResource(R.drawable.greenbg);
        }

        holder.totalItemsTextView.setText("Number of items : " + bean.getItems().size());
        holder.cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.onClickCancelOrder(bean);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.onClickOrderDetail(bean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ordersBeanArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView addressTextView;
        RecyclerView itemsRecyclerView;
        private TextView dateTextView, progressTextView, placedTextView, dispatchedextView, deliveredTextView;
        ImageView arrowImageView, firstImageView, secondImageView, thirdImageView;
        TextView cancelTextView, orderStatusTextView, totalItemsTextView;
        TextView paymentStatusTextView, paymentViaTextView, paymentDateTextView;

        public MyViewHolder(View view) {
            super(view);
            this.dateTextView = view.findViewById(R.id.dateTextView);
            this.arrowImageView = view.findViewById(R.id.arrowImageView);
            this.addressTextView = view.findViewById(R.id.addressTextView);
            this.placedTextView = view.findViewById(R.id.placedTextView);
            this.progressTextView = view.findViewById(R.id.progressTextView);
            this.dispatchedextView = view.findViewById(R.id.dispatchedextView);
            this.deliveredTextView = view.findViewById(R.id.deliveredTextView);
            this.totalItemsTextView = view.findViewById(R.id.totalItemsTextView);
            this.firstImageView = view.findViewById(R.id.firstImageView);
            this.secondImageView = view.findViewById(R.id.secondImageView);
            this.thirdImageView = view.findViewById(R.id.thirdImageView);
            this.itemsRecyclerView = view.findViewById(R.id.itemsRecyclerView);
            this.cancelTextView = view.findViewById(R.id.cancelTextView);
            orderStatusTextView = view.findViewById(R.id.orderStatusTextView);
            this.itemsRecyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
            this.paymentStatusTextView = view.findViewById(R.id.paymentStatusTextView);
            this.paymentViaTextView = view.findViewById(R.id.paymentViaTextView);
            this.paymentDateTextView = view.findViewById(R.id.paymentDateTextView);
        }
    }
}
