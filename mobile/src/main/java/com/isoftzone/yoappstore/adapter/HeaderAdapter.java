package com.isoftzone.yoappstore.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.isoftzone.yoappstore.R;
import com.isoftzone.yoappstore.bean.SubCategoryBean;
import com.isoftzone.yoappstore.util.circleimage;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class HeaderAdapter extends RecyclerView.Adapter<HeaderAdapter.MyViewHolder> {

    private ArrayList<SubCategoryBean> list;
    private Context context;
    private Listner listner;
    ImageLoader imageLoader;
    int selectedPos = -1;

    public interface Listner {
        void onClickRow(String catId, String subCatId);
    }

    public HeaderAdapter(Context context, ArrayList<SubCategoryBean> list, ImageLoader imageLoader, Listner listner) {
        this.list = list;
        this.context = context;
        this.listner = listner;
        this.imageLoader = imageLoader;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final SubCategoryBean bean = list.get(position);
        holder.titleTextView.setText(bean.getSubCatName());
        String imgPath = bean.getSubCatImage().trim().replaceAll(" ", "%20");
        imageLoader.displayImage(imgPath, holder.iconImageView);

        if (bean.isSelected()) {
            selectedPos = position;
            holder.mainLinearLayout.setBackgroundResource(R.drawable.bg_borderhighlight);
            holder.titleTextView.setTextColor(context.getResources().getColor(R.color.dangerRed));
        } else {
            holder.mainLinearLayout.setBackgroundResource(R.drawable.bgheader);
            holder.titleTextView.setTextColor(context.getResources().getColor(R.color.darkGray));
        }

        holder.mainLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPos != -1) {
                    list.get(selectedPos).setSelected(false);
                }
                selectedPos = position;
                bean.setSelected(true);
                notifyDataSetChanged();
                listner.onClickRow(bean.getCategoryId(), bean.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTextView, lineTextView;
        private LinearLayout mainLinearLayout;
        private circleimage iconImageView;

        public MyViewHolder(View view) {
            super(view);
            this.mainLinearLayout = view.findViewById(R.id.mainLinearLayout);
            this.titleTextView = view.findViewById(R.id.titleTextView);
            this.lineTextView = view.findViewById(R.id.lineTextView);
            this.iconImageView = view.findViewById(R.id.iconImageView);
        }
    }
}
