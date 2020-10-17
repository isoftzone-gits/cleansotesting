package com.isoftzone.yoappstore.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.isoftzone.yoappstore.R;
import com.isoftzone.yoappstore.bean.AttributesBean;
import com.isoftzone.yoappstore.bean.ProductBean;
import com.isoftzone.yoappstore.bean.SelectedProduct;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

public abstract class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MyViewHolder> {

    private ArrayList<ProductBean> productBeanArrayList;
    private Context context;
    protected ImageLoader imageLoader;
    private Listner listner;

    public interface Listner {
        void onClickUpdateCount();
        void onClickAdd(ProductBean productBean);
        void onClickAddNow(ProductBean productBean);
        void onReload(ProductBean productBean);
    }

    public ItemsAdapter(Context context, ArrayList<ProductBean> productBeanArrayList, ImageLoader imageLoader, Listner listner) {
        this.productBeanArrayList = productBeanArrayList;
        this.imageLoader = imageLoader;
        this.context = context;
        this.listner = listner;
    }

    @NonNull
    @Override
    public ItemsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final ProductBean bean = productBeanArrayList.get(position);
        holder.titleTextView.setText(bean.getProductName());
        if (bean.getAttributes() != null && bean.getAttributes().get(0) != null)
            holder.descTextView.setText(bean.getAttributes().get(0).getProductAttributes());
        holder.qtyTextView.setText("" + bean.getQtyActual());
        if ((position + 1) == productBeanArrayList.size() - 5) {
            listner.onReload(bean);
        }

        if (bean.getStock().trim().equalsIgnoreCase("0")) {
            holder.linearHome.setBackgroundColor(context.getResources().getColor(R.color.verylightgray));
            holder.imgCardView.setCardBackgroundColor(context.getResources().getColor(R.color.verylightgray));
            holder.outStockLinearLayout.setVisibility(View.VISIBLE);
            holder.blurrImageView.setVisibility(View.VISIBLE);
        } else {
            holder.linearHome.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.imgCardView.setCardBackgroundColor(context.getResources().getColor(R.color.white));
            holder.outStockLinearLayout.setVisibility(View.GONE);
            holder.blurrImageView.setVisibility(View.GONE);
        }

        if (bean.getAttributes().size() > 0) {
            holder.costTextView.setText("₹ " + bean.getAttributes().get(0).getProductPrice());
            if (bean.getAttributes().get(0).getSell_price() != null) {
                float salePrice = Float.parseFloat(bean.getAttributes().get(0).getSell_price());
                float productPrice = Float.parseFloat(bean.getAttributes().get(0).getProductPrice());
                if (salePrice >= productPrice) {
                    holder.costTextView.setText("₹ " + bean.getAttributes().get(0).getProductPrice());
                    holder.salePriceTextView.setVisibility(View.GONE);
                } else {
                    holder.costTextView.setPaintFlags(holder.costTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.costTextView.setText("₹ " + bean.getAttributes().get(0).getProductPrice());
                    holder.salePriceTextView.setText("₹ " + bean.getAttributes().get(0).getSell_price());
                    holder.salePriceTextView.setVisibility(View.VISIBLE);
                }
            } else {
                holder.salePriceTextView.setVisibility(View.GONE);
            }
        }
        ArrayAdapter<AttributesBean> adapter = new ArrayAdapter<>(context, R.layout.size_spinner_items, bean.getAttributes());
        holder.sizeSpinner.setAdapter(adapter);
        holder.sizeSpinner.setSelection(bean.getSelectedAttPos());
        holder.sizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AttributesBean attributesBean = (AttributesBean) parent.getItemAtPosition(position);
                bean.setSelectedAttPos(position);
                bean.setAttrId(attributesBean.getId());
                bean.setSelectedSize(attributesBean.getProductAttributes());
                bean.setCurrentSelectedPrice(attributesBean.getSell_price());
                bean.setProduct_attribute(attributesBean.getProductAttributes());
                holder.costTextView.setText("₹ " + attributesBean.getProductPrice());
                holder.costTextView.setPaintFlags(holder.costTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                if (attributesBean.getSell_price() != null) {

                    float salePrice = Float.parseFloat(attributesBean.getSell_price());
                    float productPrice = Float.parseFloat(attributesBean.getProductPrice());

                    if (salePrice == productPrice) {
                        holder.costTextView.setText("₹ " + attributesBean.getProductPrice());
                        holder.salePriceTextView.setVisibility(View.GONE);
                    } else {
                        holder.costTextView.setText("₹ " + attributesBean.getProductPrice());
                        holder.salePriceTextView.setText("₹ " + attributesBean.getSell_price());
                        holder.salePriceTextView.setVisibility(View.VISIBLE);
                    }

                } else {
                    holder.salePriceTextView.setText("₹ " + attributesBean.getProductPrice());
                    holder.salePriceTextView.setVisibility(View.GONE);
                }

                if (bean.isSelected()) {
                    holder.totalTextView.setText("Total ₹ " + (bean.getQtyActual() * Float.parseFloat(bean.getCurrentSelectedPrice())));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        if (bean.getThumbnail_image() != null) {
            String imgPath = bean.getThumbnail_image().trim().replaceAll(" ", "%20");
            imageLoader.displayImage(imgPath, holder.imgImageView, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    holder.imgImageView.setVisibility(View.GONE);
                    holder.avatarImageView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    holder.imgImageView.setVisibility(View.GONE);
                    holder.avatarImageView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    holder.avatarImageView.setVisibility(View.GONE);
                    holder.imgImageView.setVisibility(View.VISIBLE);
                    holder.imgImageView.setImageBitmap(loadedImage);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });
        }

        holder.minusTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = bean.getQtyActual();
                if (qty > 1) {
                    qty--;
                    bean.setQtyActual(qty);
                    SelectedProduct.getInstance().addSingleProduct(bean);
                    notifyDataSetChanged();
                }
            }
        });

        holder.plusTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = bean.getQtyActual();
                qty++;
                bean.setQtyActual(qty);
                bean.setSelected(true);
                SelectedProduct.getInstance().addSingleProduct(bean);
                notifyDataSetChanged();
                listner.onClickUpdateCount();
            }
        });

        holder.layoutAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bean.getStock().trim().equalsIgnoreCase("0")) {
                    int selectedPos = 0;
                    bean.getAttributes().get(selectedPos);
                    bean.setSelectedAttPos(0);
                    bean.setAttrId(bean.getAttributes().get(selectedPos).getId());
                    bean.setSelectedSize(bean.getAttributes().get(selectedPos).getProductAttributes());
                    bean.setCurrentSelectedPrice(bean.getAttributes().get(selectedPos).getSell_price() == null ? bean.getAttributes().get(selectedPos).getProductPrice() : bean.getAttributes().get(selectedPos).getSell_price());
                    bean.setProduct_attribute(bean.getAttributes().get(selectedPos).getProductAttributes());
                    bean.setNarration("");
                    SelectedProduct.getInstance().addSingleProductDuplicateAlso(bean);
                    holder.layoutAdd.setVisibility(View.INVISIBLE);
                    holder.layoutAddMore.setVisibility(View.VISIBLE);
                    addToCart();
                } else {
                    Toast.makeText(context, "Item is not available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.linearHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.onClickAdd(bean);
            }
        });

        if (bean.getSelectedAttPos() > 1){
            holder.layoutAdd.setVisibility(View.VISIBLE);
            holder.layoutAdd.setVisibility(View.GONE);
                }else {
            holder.layoutAdd.setVisibility(View.GONE);
            holder.layoutAdd.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return productBeanArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgImageView, avatarImageView, blurrImageView;
        private TextView titleTextView;
        private RelativeLayout layoutAddMore;
        private TextView minusTextView;
        private TextView qtyTextView;
        private TextView plusTextView;
        private TextView descTextView, totalTextView;
        private TextView costTextView, salePriceTextView;
        private LinearLayout linearHome, outStockLinearLayout, layoutAdd;
        private Spinner sizeSpinner;
        private CardView imgCardView;

        public MyViewHolder(View view) {
            super(view);
            this.linearHome = view.findViewById(R.id.linearHome);
            this.layoutAdd = view.findViewById(R.id.layoutAdd);
            this.descTextView = view.findViewById(R.id.descTextView);
            this.plusTextView = view.findViewById(R.id.plusTextView);
            this.qtyTextView = view.findViewById(R.id.qtyTextView);
            this.minusTextView = view.findViewById(R.id.minusTextView);
            this.titleTextView = view.findViewById(R.id.titleTextView);
            this.totalTextView = view.findViewById(R.id.totalTextView);
            this.costTextView = view.findViewById(R.id.costTextView);
            this.salePriceTextView = view.findViewById(R.id.salePriceTextView);
            this.imgImageView = view.findViewById(R.id.imgImageView);
            this.avatarImageView = view.findViewById(R.id.avatarImageView);
            this.sizeSpinner = view.findViewById(R.id.sizeSpinner);
            this.blurrImageView = view.findViewById(R.id.blurrImageView);
            this.imgCardView = view.findViewById(R.id.imgCardView);
            this.outStockLinearLayout = view.findViewById(R.id.outStockLinearLayout);
            this.layoutAddMore = view.findViewById(R.id.layoutAddMore);
        }
    }

    protected abstract void addToCart();
}
