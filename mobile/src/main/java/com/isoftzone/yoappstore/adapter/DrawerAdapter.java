package com.isoftzone.yoappstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.isoftzone.yoappstore.R;
import com.isoftzone.yoappstore.bean.DrawerBean;
import com.isoftzone.yoappstore.bean.DrawerChildBean;

import java.util.ArrayList;

public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.MyViewHolder> implements DrawerChildAdapter.Listner {

    private ArrayList<DrawerBean> drawerBeanArrayList;
    private Context context;
    Listner listner;

    public interface Listner {
        public void onClickChildParent(DrawerChildBean bean);
    }


    public DrawerAdapter(Context context, ArrayList<DrawerBean> drawerBeanArrayList, Listner listner) {
        this.drawerBeanArrayList = drawerBeanArrayList;
        this.context = context;
        this.listner = listner;
    }

    @NonNull
    @Override
    public DrawerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final DrawerBean bean = drawerBeanArrayList.get(position);
        holder.titleTextView.setText(bean.getTitle());

        if (bean.getDrawerChilList() != null && bean.getDrawerChilList().size() > 0) {
            holder.arrowImageView.setVisibility(View.VISIBLE);
            DrawerChildAdapter adapter = new DrawerChildAdapter(context, bean.getDrawerChilList(), this);
            holder.subItemRecyclerView.setAdapter(adapter);
        } else {
            holder.arrowImageView.setVisibility(View.GONE);
        }

        if (bean.isExpand()) {
            holder.subItemRecyclerView.setVisibility(View.VISIBLE);
        } else {
            holder.subItemRecyclerView.setVisibility(View.GONE);
        }

        holder.mainLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean.isExpand()) {
                    bean.setExpand(false);
                } else {
                    bean.setExpand(true);
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return drawerBeanArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RecyclerView subItemRecyclerView;
        private ImageView arrowImageView;
        private TextView titleTextView;
        private LinearLayout mainLinearLayout;

        public MyViewHolder(View view) {
            super(view);
            subItemRecyclerView = itemView.findViewById(R.id.subItemRecyclerView);
            arrowImageView = itemView.findViewById(R.id.arrowImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            mainLinearLayout = itemView.findViewById(R.id.mainLinearLayout);
            subItemRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        }
    }

    @Override
    public void onClickItems(DrawerChildBean bean) {
        listner.onClickChildParent(bean);
    }
}
