package com.isoftzone.yoappstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.isoftzone.yoappstore.R;
import com.isoftzone.yoappstore.bean.ProductBean;
import com.isoftzone.yoappstore.bean.SelectedProduct;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    private ArrayList<ProductBean> productBeanArrayList;
    private Context context;
    protected ImageLoader imageLoader;
    Listner listner;

    public interface Listner {
        void onClickRow(ProductBean productBean);
        void findNumItems();
        void onClickPlusMinus(ProductBean productBean);
    }

    public CartAdapter(ArrayList<ProductBean> productBeanArrayList, ImageLoader imageLoader, Context context, Listner listner) {
        this.productBeanArrayList = productBeanArrayList;
        this.imageLoader = imageLoader;
        this.context = context;
        this.listner = listner;
    }

    @NonNull
    @Override
    public CartAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final ProductBean bean = productBeanArrayList.get(position);
        holder.titleTextView.setText(bean.getProductName());
        holder.qtyTextView.setText("" + bean.getQtyActual());
        holder.totalAmtTextView.setText("Total ₹ " + (bean.getQtyActual() * Float.parseFloat(bean.getCurrentSelectedPrice())));
        holder.qtyTotalTextView.setText("Qty: " + bean.getQtyActual());

        if (bean.getProductImage() != null && bean.getProductImage().size() > 0) {
            String imgPath = bean.getProductImage().get(0).trim().replaceAll(" ", "%20");
            imageLoader.displayImage(imgPath, holder.iconImageView);
        }

        holder.minusTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = bean.getQtyActual();
                if (qty > 1) {
                    qty = qty - 1;
                    bean.setQtyActual(qty);
                    SelectedProduct.getInstance().increaseDecreaseQty(bean);
                    notifyDataSetChanged();
                }
                listner.onClickPlusMinus(bean);

            }
        });

        holder.plusTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = bean.getQtyActual();
                qty = qty + 1;
                bean.setQtyActual(qty);
                SelectedProduct.getInstance().increaseDecreaseQty(bean);
                notifyDataSetChanged();
                listner.onClickPlusMinus(bean);
            }
        });

        holder.removeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bean.setSelected(false);
                bean.setQtyActual(1);
                SelectedProduct.getInstance().removeSingleProduct(bean);
                notifyDataSetChanged();
                listner.findNumItems();
            }
        });

        holder.linearHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.onClickRow(bean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productBeanArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTextView, totalAmtTextView, qtyTotalTextView;
        private TextView minusTextView;
        private TextView qtyTextView;
        private TextView plusTextView;
        private TextView removeTextView;
        private LinearLayout linearHome;
        private ImageView iconImageView;

        public MyViewHolder(View view) {
            super(view);
            this.linearHome = view.findViewById(R.id.linearHome);
            this.removeTextView = view.findViewById(R.id.removeTextView);
            this.qtyTotalTextView = view.findViewById(R.id.qtyTotalTextView);
            this.totalAmtTextView = view.findViewById(R.id.totalAmtTextView);
            this.plusTextView = view.findViewById(R.id.plusTextView);
            this.qtyTextView = view.findViewById(R.id.qtyTextView);
            this.minusTextView = view.findViewById(R.id.minusTextView);
            this.titleTextView = view.findViewById(R.id.titleTextView);
            this.iconImageView = view.findViewById(R.id.iconImageView);
        }
    }
}
