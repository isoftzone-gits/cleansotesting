package com.isoftzone.yoappstore.adapter;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.isoftzone.yoappstore.R;
import com.isoftzone.yoappstore.activity.ItemsActivity;
import com.isoftzone.yoappstore.activity.SubCategoryActivity;
import com.isoftzone.yoappstore.bean.MainCategoryBean;
import com.isoftzone.yoappstore.network.CommonInterfaces;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

    private ArrayList<MainCategoryBean> homeModelArrayList;
    private Context context;
    private CommonInterfaces.RecyclerClickDelegate recyclerClickDelegate;
    protected ImageLoader imageLoader;
    private int color;

    public HomeAdapter(ArrayList<MainCategoryBean> homeModelArrayList, ImageLoader imageLoader, Context context,
                       CommonInterfaces.RecyclerClickDelegate recyclerClickDelegate, int color) {

        this.homeModelArrayList = homeModelArrayList;
        this.imageLoader = imageLoader;
        this.context = context;
        this.recyclerClickDelegate = recyclerClickDelegate;
        this.color = color;
    }

    @NonNull
    @Override
    public HomeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_menu_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final MainCategoryBean homeModel = homeModelArrayList.get(position);
        holder.titleTextView.setText(homeModel.getCategoryName());
        holder.titleTextView.setTextColor(color);
        String imgPath = homeModel.getThumbnail_image().trim().replaceAll(" ", "%20");
        imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(imgPath, holder.homeRowIcon, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                holder.homeRowIcon.setImageBitmap(loadedImage);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
        holder.descTextView.setText(homeModel.getCategoryDescription());

        holder.linearHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (homeModel.isHas_sub_category()) {
                    context.startActivity(new Intent(context, SubCategoryActivity.class).putExtra("catId", homeModel.getId()).putExtra("catList", homeModelArrayList));
                } else {
                    context.startActivity(new Intent(context, ItemsActivity.class).putExtra("catId", homeModel.getId()).putExtra("hasSubCate", homeModel.isHas_sub_category()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return homeModelArrayList.size();
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
