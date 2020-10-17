package com.isoftzone.yoappstore.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.viewpager.widget.PagerAdapter;

import com.isoftzone.yoappstore.R;
import com.isoftzone.yoappstore.util.CustomDialogForImage;
import com.isoftzone.yoappstore.util.zoomage.ZoomageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

public class ImageDialogSliderAdapter extends PagerAdapter {
    private LayoutInflater inflater;
    private List<String> bannersArrayList;
    private Context context;
    private ImageLoader imageLoader;

    public ImageDialogSliderAdapter(Context context, List<String> bannersArrayList, ImageLoader imageLoader) {
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
    public Object instantiateItem(ViewGroup view, final int position) {
        View imageLayout = inflater.inflate(R.layout.dialog_sliderimgs, view, false);

        assert imageLayout != null;
        final ZoomageView enlargeImageView = imageLayout.findViewById(R.id.enlargeImageView);
        final ProgressBar progressProgressBar = imageLayout.findViewById(R.id.progressProgressBar);

        imageLoader.displayImage(bannersArrayList.get(position), enlargeImageView, new ImageLoadingListener() {
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
                enlargeImageView.setImageBitmap(loadedImage);
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
