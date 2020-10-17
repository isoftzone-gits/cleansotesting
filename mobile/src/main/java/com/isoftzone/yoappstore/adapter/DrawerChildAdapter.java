package com.isoftzone.yoappstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.isoftzone.yoappstore.R;
import com.isoftzone.yoappstore.bean.DrawerChildBean;


import java.util.ArrayList;

public class DrawerChildAdapter extends RecyclerView.Adapter<DrawerChildAdapter.Myholder> {
    Context context;
    ArrayList<DrawerChildBean> drawerBeanArrayList;
    Listner listner;

    public interface Listner {
        public void onClickItems(DrawerChildBean bean);
    }

    public DrawerChildAdapter(Context context, ArrayList<DrawerChildBean> drawerBeanArrayList, Listner listner) {
        this.context = context;
        this.drawerBeanArrayList = drawerBeanArrayList;
        this.listner = listner;
    }

    static class Myholder extends RecyclerView.ViewHolder {
        private ImageView iconImageView;
        private TextView titleTextView;
        private LinearLayout mainLinearLayout;
        public Myholder(View itemView) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.iconImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            mainLinearLayout = itemView.findViewById(R.id.mainLinearLayout);
        }
    }

    @Override
    public Myholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_child_drawer, parent, false);
        return new Myholder(itemView);
    }

    @Override
    public void onBindViewHolder(final Myholder holder, final int position) {
        final DrawerChildBean bean = drawerBeanArrayList.get(position);
        holder.titleTextView.setText(bean.getTitle());
        holder.iconImageView.setImageResource(bean.getIcon());
        holder.mainLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.onClickItems(bean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return drawerBeanArrayList.size();
    }
}
