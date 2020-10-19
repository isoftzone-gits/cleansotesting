package com.isoftzone.vendor.vandor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.isoftzone.vendor.R;
import com.isoftzone.vendor.bean.Banners;
import com.isoftzone.vendor.vandor.listener.ItemTouchHelperAdapter;
import com.isoftzone.vendor.vandor.listener.ItemTouchHelperViewHolder;
import com.isoftzone.vendor.vandor.listener.OnBannerListChangedListener;
import com.isoftzone.vendor.vandor.listener.OnStartDragSubCatListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Collections;

public class BannerSequenceAdapter extends RecyclerView.Adapter<BannerSequenceAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private ArrayList<Banners> mCustomers = null;
    private Context mContext;
    private OnStartDragSubCatListener mDragStartListener;
    private OnBannerListChangedListener mListChangedListener;
    private ImageLoader imageLoader;

    public BannerSequenceAdapter(Context context, ArrayList<Banners> mainBeanArrayList, OnStartDragSubCatListener dragLlistener,
                                 OnBannerListChangedListener listChangedListener, ImageLoader imageLoader) {
        this.mContext = context;
        this.mCustomers = mainBeanArrayList;
        mDragStartListener = dragLlistener;
        mListChangedListener = listChangedListener;
        this.imageLoader = imageLoader;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        private TextView sequenceTextView;
        private ImageView handleView, deleteImageView, imgImageView, editImageView;
        private LinearLayout mainLinearLayout;

        public ViewHolder(View view) {
            super(view);
            this.mainLinearLayout = itemView.findViewById(R.id.mainLinearLayout);
            this.handleView = itemView.findViewById(R.id.handle);
            this.imgImageView = itemView.findViewById(R.id.imgImageView);
            this.sequenceTextView = itemView.findViewById(R.id.sequenceTextView);
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_banner, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Banners bean = mCustomers.get(position);

        imageLoader.displayImage(bean.getImage(), holder.imgImageView);
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

    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mCustomers, fromPosition, toPosition);
        mListChangedListener.onNoteListChanged(mCustomers);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {

    }
}
