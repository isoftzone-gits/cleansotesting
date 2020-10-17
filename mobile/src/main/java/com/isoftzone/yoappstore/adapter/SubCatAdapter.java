package com.isoftzone.yoappstore.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.isoftzone.yoappstore.R;
import com.isoftzone.yoappstore.bean.CompanyDetails;
import com.isoftzone.yoappstore.bean.SubCategoryBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class SubCatAdapter extends RecyclerView.Adapter<SubCatAdapter.MyViewHolder> {

    private ArrayList<SubCategoryBean> subCategoryBeanArrayList;
    private Context context;
    private Listner recyclerClickDelegate;
    protected ImageLoader imageLoader;

    public interface Listner {
        void onClickSubCat(int pos, String subCatId, SubCategoryBean bean);
    }

    public SubCatAdapter(ArrayList<SubCategoryBean> subCategoryBeanArrayList, ImageLoader imageLoader, Context context,
                         Listner recyclerClickDelegate) {

        this.subCategoryBeanArrayList = subCategoryBeanArrayList;
        this.imageLoader = imageLoader;
        this.context = context;
        this.recyclerClickDelegate = recyclerClickDelegate;
    }

    @NonNull
    @Override
    public SubCatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (CompanyDetails.getInstance().getDetails().getSubcat_grid().equalsIgnoreCase("0")) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subcat_row, parent, false);
            return new MyViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subcat_rowgrid, parent, false);
            return new MyViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final SubCategoryBean bean = subCategoryBeanArrayList.get(position);
        holder.titleTextView.setText(bean.getSubCatName());
        String imgPath = bean.getThumbnail_image().trim().replaceAll(" ", "%20");
        imageLoader.displayImage(imgPath, holder.homeRowIcon);
        holder.descTextView.setText(bean.getSubCatDesc());
        holder.linearHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerClickDelegate.onClickSubCat(position, bean.getId(), bean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subCategoryBeanArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTextView;
        private TextView descTextView;
        private ImageView homeRowIcon;
        private LinearLayout linearHome;

        public MyViewHolder(View view) {
            super(view);

            titleTextView = view.findViewById(R.id.titleTextView);
            descTextView = view.findViewById(R.id.descTextView);
            homeRowIcon = view.findViewById(R.id.homeRowIcon);
            linearHome = view.findViewById(R.id.linearHome);
        }
    }
}
