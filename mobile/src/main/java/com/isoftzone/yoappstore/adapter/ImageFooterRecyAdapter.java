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
import com.isoftzone.yoappstore.bean.AttributesBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class ImageFooterRecyAdapter extends RecyclerView.Adapter<ImageFooterRecyAdapter.MyViewHolder> {
    private List<AttributesBean> attributesBeanList;
    private Context context;
    public Listner listner;

    public interface Listner {
        void onClickItemsSize(AttributesBean bean, int position);
    }

    public ImageFooterRecyAdapter(Context context, List<AttributesBean> attributesBeanList, Listner listner) {
        this.attributesBeanList = attributesBeanList;
        this.context = context;
        this.listner = listner;
    }

    @NonNull
    @Override
    public ImageFooterRecyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.size_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final AttributesBean bean = attributesBeanList.get(position);
        holder.titleTextView.setText(bean.getProductAttributes());
        holder.mainLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.onClickItemsSize(bean, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return attributesBeanList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private LinearLayout mainLinearLayout;
        public MyViewHolder(View view) {
            super(view);
            this.mainLinearLayout = view.findViewById(R.id.mainLinearLayout);
            this.titleTextView = view.findViewById(R.id.titleTextView);
        }
    }
}
