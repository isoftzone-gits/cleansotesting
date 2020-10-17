package com.isoftzone.yoappstore.adapter;

import android.content.Context;
import android.content.Intent;
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

public class PastOrdersAdapter extends RecyclerView.Adapter<PastOrdersAdapter.MyViewHolder> {

    private ArrayList<OrdersBean> ordersBeanArrayList;
    private Context context;
    protected ImageLoader imageLoader;
    Listner listner;

    public interface Listner {
        void onClickCancelOrder(OrdersBean ordersBean);
    }

    public PastOrdersAdapter(ArrayList<OrdersBean> ordersBeanArrayList, ImageLoader imageLoader, Context context, Listner listner) {
        this.ordersBeanArrayList = ordersBeanArrayList;
        this.imageLoader = imageLoader;
        this.context = context;
        this.listner = listner;
    }

    @NonNull
    @Override
    public PastOrdersAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pastorders_row, parent, false);
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

        if (bean.getStatus().equalsIgnoreCase("placed")) {
            holder.placedTextView.setTextColor(context.getResources().getColor(R.color.fontGreen));
            holder.progressTextView.setTextColor(context.getResources().getColor(R.color.darkGray));
            holder.dispatchedextView.setTextColor(context.getResources().getColor(R.color.darkGray));
            holder.deliveredTextView.setTextColor(context.getResources().getColor(R.color.darkGray));
            holder.firstImageView.setBackgroundResource(R.drawable.graybg);
            holder.secondImageView.setBackgroundResource(R.drawable.graybg);
            holder.thirdImageView.setBackgroundResource(R.drawable.graybg);
        } else if (bean.getStatus().equalsIgnoreCase("progress")) {
            holder.placedTextView.setTextColor(context.getResources().getColor(R.color.fontGreen));
            holder.progressTextView.setTextColor(context.getResources().getColor(R.color.fontGreen));
            holder.dispatchedextView.setTextColor(context.getResources().getColor(R.color.darkGray));
            holder.deliveredTextView.setTextColor(context.getResources().getColor(R.color.darkGray));
            holder.firstImageView.setBackgroundResource(R.drawable.greenbg);
            holder.secondImageView.setBackgroundResource(R.drawable.graybg);
            holder.thirdImageView.setBackgroundResource(R.drawable.graybg);
        } else if (bean.getStatus().equalsIgnoreCase("dispatched")) {
            holder.placedTextView.setTextColor(context.getResources().getColor(R.color.fontGreen));
            holder.progressTextView.setTextColor(context.getResources().getColor(R.color.fontGreen));
            holder.dispatchedextView.setTextColor(context.getResources().getColor(R.color.fontGreen));
            holder.deliveredTextView.setTextColor(context.getResources().getColor(R.color.darkGray));
            holder.firstImageView.setBackgroundResource(R.drawable.greenbg);
            holder.secondImageView.setBackgroundResource(R.drawable.greenbg);
            holder.thirdImageView.setBackgroundResource(R.drawable.graybg);
        }
        holder.itemsTextView.setText("Number of items : " + bean.getItems().size());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, OrderDetailActivity.class).putExtra("order", bean).putExtra("comeFrom", "past"));
            }
        });

        holder.headerLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean.isCollapse()) {
                    bean.setCollapse(false);
                } else {
                    bean.setCollapse(true);
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return ordersBeanArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView addressTextView, itemsTextView;
        RecyclerView itemsRecyclerView;
        private TextView dateTextView, orderStatusTextView, progressTextView, placedTextView, dispatchedextView, deliveredTextView;
        private LinearLayout headerLinearLayout;
        ImageView arrowImageView, firstImageView, secondImageView, thirdImageView;

        public MyViewHolder(View view) {
            super(view);
            this.headerLinearLayout = view.findViewById(R.id.headerLinearLayout);
            this.dateTextView = view.findViewById(R.id.dateTextView);
            this.arrowImageView = view.findViewById(R.id.arrowImageView);
            this.addressTextView = view.findViewById(R.id.addressTextView);
            this.placedTextView = view.findViewById(R.id.placedTextView);
            this.progressTextView = view.findViewById(R.id.progressTextView);
            this.dispatchedextView = view.findViewById(R.id.dispatchedextView);
            this.deliveredTextView = view.findViewById(R.id.deliveredTextView);
            this.firstImageView = view.findViewById(R.id.firstImageView);
            this.secondImageView = view.findViewById(R.id.secondImageView);
            this.thirdImageView = view.findViewById(R.id.thirdImageView);
            this.itemsRecyclerView = view.findViewById(R.id.itemsRecyclerView);
            this.itemsTextView = view.findViewById(R.id.itemsTextView);
            orderStatusTextView = view.findViewById(R.id.orderStatusTextView);
            this.itemsRecyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        }
    }
}
