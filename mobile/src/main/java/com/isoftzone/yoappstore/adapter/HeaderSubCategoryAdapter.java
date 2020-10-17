package com.isoftzone.yoappstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.isoftzone.yoappstore.R;
import com.isoftzone.yoappstore.bean.MainCategoryBean;
import com.isoftzone.yoappstore.bean.SubCategoryBean;
import com.isoftzone.yoappstore.util.circleimage;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class HeaderSubCategoryAdapter extends RecyclerView.Adapter<HeaderSubCategoryAdapter.MyViewHolder> {

    private ArrayList<MainCategoryBean> list;
    private Context context;
    private Listner listner;
    ImageLoader imageLoader;
    int selectedPos = -1;

    public interface Listner {
        void onClickRow(String catId);
    }

    public HeaderSubCategoryAdapter(Context context, ArrayList<MainCategoryBean> list, ImageLoader imageLoader, Listner listner) {
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
        final MainCategoryBean bean = list.get(position);
        holder.titleTextView.setText(bean.getCategoryName());
        String imgPath = bean.getCategoryImage().trim().replaceAll(" ", "%20");
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
                listner.onClickRow(bean.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView ;
        private LinearLayout mainLinearLayout;
        private circleimage iconImageView;

        public MyViewHolder(View view) {
            super(view);
            this.mainLinearLayout = view.findViewById(R.id.mainLinearLayout);
            this.titleTextView = view.findViewById(R.id.titleTextView);
            this.iconImageView = view.findViewById(R.id.iconImageView);
        }
    }
}
