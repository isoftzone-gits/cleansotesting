package com.isoftzone.vendor.vandor.adapter;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.isoftzone.vendor.R;
import com.isoftzone.vendor.bean.MainCategoryBean;
import com.isoftzone.vendor.vandor.activity.CategoryAddActivity;
import com.isoftzone.vendor.vandor.listener.ItemTouchHelperAdapter;
import com.isoftzone.vendor.vandor.listener.ItemTouchHelperViewHolder;
import com.isoftzone.vendor.vandor.listener.OnCustomerListChangedListener;
import com.isoftzone.vendor.vandor.listener.OnStartDragListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Collections;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private ArrayList<MainCategoryBean> mCustomers;
    private CategoryAddActivity mContext;
    private OnStartDragListener mDragStartListener;
    private OnCustomerListChangedListener mListChangedListener;
    private ImageLoader imageLoader;

    public CategoryAdapter(CategoryAddActivity context, ArrayList<MainCategoryBean> mainBeanArrayList, OnStartDragListener dragLlistener,
                           OnCustomerListChangedListener listChangedListener, ImageLoader imageLoader) {
        this.mContext = context;
        this.mCustomers = mainBeanArrayList;
        this.mDragStartListener = dragLlistener;
        this.mListChangedListener = listChangedListener;
        this.imageLoader = imageLoader;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        private TextView cateNameTextView, sequenceTextView, descTextView;
        private ImageView handleView, deleteImageView, imgImageView, editImageView;
        private LinearLayout mainLinearLayout;
        private CardView mainCardView;

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
            this.mainCardView = itemView.findViewById(R.id.mainCardView);
        }

        @Override
        public void onItemSelected() {
            mainCardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.verylightgray));
        }

        @Override
        public void onItemClear() {
            mainCardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return mCustomers.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_category, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final MainCategoryBean bean = mCustomers.get(position);
        // holder.imgImageView.setImageURI(Uri.parse(bean.getCategoryImage()));

        imageLoader.displayImage(bean.getCategoryImage(), holder.imgImageView);

        holder.cateNameTextView.setText("Name : " + bean.getCategoryName());
        holder.descTextView.setText("Desc : " + bean.getCategoryDescription());
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
                if (mCustomers.size() == 1) mContext.finish();
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
                if (bean.getCategoryName() != null && !bean.getCategoryName().equalsIgnoreCase("") && bean.getCategoryImage() != null && bean.getCategoryImage() != null) {
                    mListChangedListener.onClickRow(bean);
                } else {
                    Toast.makeText(mContext, "Please Add Category Name and Image", Toast.LENGTH_SHORT).show();
                }
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
