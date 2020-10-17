package com.isoftzone.yoappstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.isoftzone.yoappstore.R;
import com.isoftzone.yoappstore.bean.Item;
import com.isoftzone.yoappstore.util.circleimage;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.DecimalFormat;
import java.util.List;

public class PlacedOrderItemsAdapter extends RecyclerView.Adapter<PlacedOrderItemsAdapter.MyViewHolder> {

    private List<Item> itemList;
    private Context context;
    protected ImageLoader imageLoader;

    public PlacedOrderItemsAdapter(List<Item> itemList, ImageLoader imageLoader, Context context) {
        this.itemList = itemList;
        this.imageLoader = imageLoader;
        this.context = context;
    }

    @NonNull
    @Override
    public PlacedOrderItemsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_placeorder_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Item bean = itemList.get(position);
        holder.titleTextView.setText(bean.getProductName());
        holder.qtyTotalTextView.setText("QTY : " + bean.getQtyTotal());
        holder.totalAmtTextView.setText("TOTAL ₹ " + new DecimalFormat(".##").format(Float.parseFloat(bean.getTotalAmt())));
        String imgPath = bean.getProductImage().trim().replaceAll(" ", "%20");
        imageLoader.displayImage(imgPath, holder.iconImageView);
    }

    @Override
    public int getItemCount() {
        if (itemList != null) {
            return itemList.size();
        } else {
            return 0;
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView, totalAmtTextView, qtyTotalTextView;
        ImageView iconImageView;
        public MyViewHolder(View view) {
            super(view);
            this.qtyTotalTextView = view.findViewById(R.id.qtyTotalTextView);
            this.totalAmtTextView = view.findViewById(R.id.totalAmtTextView);
            this.titleTextView = view.findViewById(R.id.titleTextView);
            this.iconImageView = view.findViewById(R.id.iconImageView);
        }
    }
}
