package com.isoftzone.vendor.vandor.adapter;

import android.app.Activity;
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
import com.isoftzone.vendor.bean.SubCategoryBean;
import com.isoftzone.vendor.vandor.listener.ItemTouchHelperAdapter;
import com.isoftzone.vendor.vandor.listener.ItemTouchHelperViewHolder;
import com.isoftzone.vendor.vandor.listener.OnStartDragSubCatListener;
import com.isoftzone.vendor.vandor.listener.OnSubCatListChangedListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Collections;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private ArrayList<SubCategoryBean> mCustomers;
    private Activity mContext;
    private OnStartDragSubCatListener mDragStartListener;
    private OnSubCatListChangedListener mListChangedListener;
    private ImageLoader imageLoader;

    public SubCategoryAdapter(Activity context, ArrayList<SubCategoryBean> mainBeanArrayList, OnStartDragSubCatListener dragLlistener,
                              OnSubCatListChangedListener listChangedListener, ImageLoader imageLoader) {
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

        final SubCategoryBean bean = mCustomers.get(position);
       // holder.imgImageView.setImageURI(Uri.parse(bean.getUri()));

        imageLoader.displayImage(bean.getSubCatImage(),holder.imgImageView);

        holder.cateNameTextView.setText("Name : " + bean.getSubCatName());
        holder.descTextView.setText("Desc : " + bean.getSubCatDesc());
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
                if (bean.getSubCatName() != null && bean.getSubCatImage() != null ) {
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
