package com.isoftzone.vendor.vandor.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.isoftzone.vendor.R;
import com.isoftzone.vendor.bean.ProductBean;
import com.isoftzone.vendor.vandor.listener.ItemTouchHelperAdapter;
import com.isoftzone.vendor.vandor.listener.ItemTouchHelperViewHolder;
import com.isoftzone.vendor.vandor.listener.OnProductListChangedListener;
import com.isoftzone.vendor.vandor.listener.OnStartDragSubCatListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.Collections;

public class ProductCategoryAdapter extends RecyclerView.Adapter<ProductCategoryAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private ArrayList<ProductBean> mCustomers;
    private Activity mContext;
    private OnStartDragSubCatListener mDragStartListener;
    private OnProductListChangedListener mListChangedListener;
    private ImageLoader imageLoader;

    public ProductCategoryAdapter(Activity context, ArrayList<ProductBean> mainBeanArrayList, OnStartDragSubCatListener dragLlistener,
                                  OnProductListChangedListener listChangedListener, ImageLoader imageLoader) {
        this.mContext = context;
        this.mCustomers = mainBeanArrayList;
        this.imageLoader = imageLoader;
        mDragStartListener = dragLlistener;
        mListChangedListener = listChangedListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        private TextView cateNameTextView, sequenceTextView, descTextView;
        private ImageView handleView, deleteImageView, imgImageView, editImageView;
        private LinearLayout mainLinearLayout;

        public ViewHolder(View view) {
            super(view);
            this.mainLinearLayout = itemView.findViewById(R.id.mainLinearLayout);
            this.handleView = itemView.findViewById(R.id.handle);
            this.imgImageView = itemView.findViewById(R.id.imgImageView);
            this.cateNameTextView = itemView.findViewById(R.id.cateNameTextView);
            this.sequenceTextView = itemView.findViewById(R.id.sequenceTextView);
            this.descTextView = itemView.findViewById(R.id.descTextView);
            this.deleteImageView = itemView.findViewById(R.id.deleteImageView);
            this.editImageView = itemView.findViewById(R.id.editImageView);
        }

        @Override
        public void onItemSelected() {
            mainLinearLayout.setBackgroundResource(R.drawable.background_shapevlightgray);
        }

        @Override
        public void onItemClear() {
            mainLinearLayout.setBackgroundResource(R.drawable.background_shapevlightgray);
        }
    }

    @Override
    public int getItemCount() {
        return mCustomers.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_subcategory, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final ProductBean bean = mCustomers.get(position);
       // holder.imgImageView.setImageURI(Uri.parse(bean.getUri()));
        if (bean.getThumbnail_image() != null) {
            String imgPath = bean.getThumbnail_image().trim().replaceAll(" ", "%20");
            imageLoader.displayImage(imgPath, holder.imgImageView, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    holder.imgImageView.setVisibility(View.GONE);
                   // holder.avatarImageView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    holder.imgImageView.setVisibility(View.GONE);
                //    holder.avatarImageView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                //    holder.avatarImageView.setVisibility(View.GONE);
                    holder.imgImageView.setVisibility(View.VISIBLE);
                    holder.imgImageView.setImageBitmap(loadedImage);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });
        }


        holder.cateNameTextView.setText("Name : " + bean.getProductName());
        holder.descTextView.setText("Desc : " + bean.getProductDescription());
        holder.sequenceTextView.setText("Sequence : " + (position + 1));

        holder.handleView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });

        holder.deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCustomers.size()==1)mContext.finish();
                mCustomers.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.editImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListChangedListener.onEdit(bean);
            }
        });

        holder.mainLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean.getProductName() != null && bean.getThumbnail_image() != null ) {
                    mListChangedListener.onClickRow(bean);

                } else {
                    Toast.makeText(mContext, "Please Add SubCategory Name and Image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        try{
            Collections.swap(mCustomers, fromPosition, toPosition);
            mListChangedListener.onNoteListChanged(mCustomers);
            notifyItemMoved(fromPosition, toPosition);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onItemDismiss(int position) {

    }
}
