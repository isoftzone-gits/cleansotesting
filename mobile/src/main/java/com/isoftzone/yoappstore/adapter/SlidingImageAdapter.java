package com.isoftzone.yoappstore.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcelable;

import androidx.viewpager.widget.PagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;


import com.isoftzone.yoappstore.R;
import com.isoftzone.yoappstore.bean.Banners;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

public class SlidingImageAdapter extends PagerAdapter {

    private LayoutInflater inflater;
    private ArrayList<Banners> bannersArrayList;
    private Context context;
    private ImageLoader imageLoader;

    public SlidingImageAdapter(Context context, ArrayList<Banners> bannersArrayList, ImageLoader imageLoader) {
        this.context = context;
        this.bannersArrayList = bannersArrayList;
        this.imageLoader = imageLoader;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return bannersArrayList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {

        View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);
        assert imageLayout != null;
        final ImageView imageView = imageLayout.findViewById(R.id.image);
        final ProgressBar progressProgressBar = imageLayout.findViewById(R.id.progressProgressBar);

        imageLoader.displayImage(bannersArrayList.get(position).getImage(), imageView, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                progressProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                progressProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                progressProgressBar.setVisibility(View.GONE);
                imageView.setImageBitmap(loadedImage);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                progressProgressBar.setVisibility(View.GONE);
            }
        });
        view.addView(imageLayout, 0);
        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
