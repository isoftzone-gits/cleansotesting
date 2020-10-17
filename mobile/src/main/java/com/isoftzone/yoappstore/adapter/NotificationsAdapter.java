package com.isoftzone.yoappstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.isoftzone.yoappstore.R;
import com.isoftzone.yoappstore.bean.DrawerBean;
import com.isoftzone.yoappstore.bean.NotificationBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.MyViewHolder> {

    private ArrayList<NotificationBean> notificationArrayList;
    private Context context;
    protected ImageLoader imageLoader;

    public interface Listner {
        void onClickItems(DrawerBean bean, int position);
    }

    public NotificationsAdapter(Context context, ArrayList<NotificationBean> notificationArrayList) {
        this.notificationArrayList = notificationArrayList;
        this.imageLoader = imageLoader;
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_row, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final NotificationBean bean = notificationArrayList.get(position);
        holder.titleTextView.setText(bean.getTitle());
        holder.msgTextView.setText(bean.getMessage());
        String baseUrl = "http://yoappstore.com/login/asset/uploads/";
        String imgPath = bean.getImage().trim().replaceAll(" ", "%20");

        if (imgPath.equalsIgnoreCase("")) {
            holder.iconImageView.setVisibility(View.GONE);
        } else {
            holder.iconImageView.setVisibility(View.VISIBLE);
            imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(baseUrl + imgPath, holder.iconImageView);
        }

    }

    @Override
    public int getItemCount() {
        return notificationArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView, msgTextView;
        private ImageView iconImageView;

        public MyViewHolder(View view) {
            super(view);
            this.iconImageView = view.findViewById(R.id.iconImageView);
            this.titleTextView = view.findViewById(R.id.titleTextView);
            this.msgTextView = view.findViewById(R.id.msgTextView);
        }
    }
}
