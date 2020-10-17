package com.isoftzone.yoappstore.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.isoftzone.yoappstore.R;
import com.isoftzone.yoappstore.adapter.ImageDialogSliderAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class CustomDialogForImage extends Dialog implements View.OnClickListener {

    private String forWhich;
    private Context context;
    private ImageLoader imageLoader;
    private List<String> bannersArrayList;
    private int selectedPosition;


    public CustomDialogForImage(@NonNull Context context, String forWhich, List<String> bannersArrayList, int selectedPosition, ImageLoader imageLoader) {
        super(context);
        this.forWhich = forWhich;
        this.context = context;
        this.bannersArrayList = bannersArrayList;
        this.selectedPosition = selectedPosition;
        this.imageLoader = imageLoader;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        this.setCancelable(true);
        if (forWhich.equalsIgnoreCase("forEnlargeImage")) {
            this.setContentView(R.layout.big_dialogxml);

            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            int width = metrics.widthPixels;
            int height = metrics.heightPixels;
            getWindow().setLayout((7 * width) / 7, (4 * height) / 4);

            this.setCanceledOnTouchOutside(true);
            this.setCancelable(true);

            ViewPager detailViewPager = findViewById(R.id.detailViewPager);
            ImageDialogSliderAdapter sliderAdapter = new ImageDialogSliderAdapter(context, bannersArrayList, imageLoader);
            detailViewPager.setAdapter(sliderAdapter);
            detailViewPager.setCurrentItem(selectedPosition);
            LinearLayout sliderDots = findViewById(R.id.sliderDots);
            ImageView closeWindowImageView = findViewById(R.id.closeWindowImageView);
            closeWindowImageView.setOnClickListener(this);

            if (bannersArrayList.size() > 0) {
                final int NUM_PAGES = bannersArrayList.size();

                final ImageView[] dots = new ImageView[NUM_PAGES];
                for (int i = 0; i < NUM_PAGES; i++) {
                    dots[i] = new ImageView(context);
                    dots[i].setImageDrawable(ContextCompat.getDrawable(context, R.drawable.non_active_dot));
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(8, 0, 8, 0);
                    sliderDots.addView(dots[i], params);
                }
                dots[0].setImageDrawable(ContextCompat.getDrawable(context, R.drawable.active_dot));
                detailViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageSelected(int position) {
                        for (int i = 0; i < NUM_PAGES; i++) {
                            dots[i].setImageDrawable(ContextCompat.getDrawable(context, R.drawable.non_active_dot));
                        }
                        dots[position].setImageDrawable(ContextCompat.getDrawable(context, R.drawable.active_dot));
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.closeWindowImageView:
            case R.id.cancelTextView:
                dismiss();
                break;

        }
    }

}
