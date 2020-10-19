package com.isoftzone.vendor.vandor.activity;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;


import com.isoftzone.vendor.R;
import com.isoftzone.vendor.activity.ItemsActivity;
import com.isoftzone.vendor.vandor.adapter.CategoryAdapter;
import com.isoftzone.vendor.adapter.SlidingImageAdapter;
import com.isoftzone.vendor.baseactivity.BaseActivity;
import com.isoftzone.vendor.bean.Banners;
import com.isoftzone.vendor.bean.CompanyDetails;
import com.isoftzone.vendor.bean.MainCategoryBean;
import com.isoftzone.vendor.bean.SimpleItemTouchHelperCallback;
import com.isoftzone.vendor.bean.SingletonManager;
import com.isoftzone.vendor.bean.SubCategoryBean;
import com.isoftzone.vendor.databinding.ActivityCategoryAddBinding;
import com.isoftzone.vendor.dialog.CategoryDialog;
import com.isoftzone.vendor.dialog.CommonDialog;
import com.isoftzone.vendor.util.ImageFilePath;
import com.isoftzone.vendor.vandor.listener.OnCustomerListChangedListener;
import com.isoftzone.vendor.vandor.listener.OnStartDragListener;

import java.util.ArrayList;

public class CategoryAddActivity extends BaseActivity implements OnStartDragListener, OnCustomerListChangedListener, CategoryDialog.Listner {

    private int PICK_IMAGES = 999;
    private int PICK_IMAGESBANNERS = 995;
    private int PICK_IMAGESINGLE = 998;
    private ActivityCategoryAddBinding binding;
    private ArrayList<MainCategoryBean> categoryList;
    private CategoryAdapter categoryAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private int NUM_PAGES = 0;
    private ImageView[] dots;
    private CategoryDialog dialog;
    private MainCategoryBean categoryBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_category_add);
        categoryList = new ArrayList<>();
        // setToolBar("", true, false);
        if (getIntent().getExtras() != null)
            categoryList = (ArrayList<MainCategoryBean>) getIntent().getSerializableExtra("beanObj");

        binding.fabFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPickImageIntent();
            }
        });

        if (CompanyDetails.getInstance().getDetails() != null) {
            binding.toolbarTitleTextView.setText(CompanyDetails.getInstance().getDetails().getCompanyName());
        }

        if (categoryList == null || categoryList.size() == 0)
            getPickImageIntent();
        else binding.ifNoCateTextView.setVisibility(View.GONE);

        binding.categoryRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        binding.categoryRecyclerView.setLayoutManager(mLayoutManager);

        categoryAdapter = new CategoryAdapter(this, categoryList, this, this, imageLoader);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(categoryAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(binding.categoryRecyclerView);
        binding.categoryRecyclerView.setAdapter(categoryAdapter);

        binding.donTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryAdapter.notifyDataSetChanged();
                finish();
            }
        });

        checkCameraPermission();

        binding.addBannerImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                }
                startActivityForResult(intent, PICK_IMAGESBANNERS);
            }
        });

        binding.editBannerImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CategoryAddActivity.this, BannerSequenceActivity.class));
            }
        });

        binding.backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        scrollShowHide();
    }

    private void setBanner() {
        SlidingImageAdapter slidingImageAdapter = new SlidingImageAdapter(this, SingletonManager.getSingletonManager().getBannersArrayList(), imageLoader);
        binding.pager.setAdapter(slidingImageAdapter);

        if (SingletonManager.getSingletonManager().getBannersArrayList() != null && SingletonManager.getSingletonManager().getBannersArrayList().size() > 0) {
            sliderImages(SingletonManager.getSingletonManager().getBannersArrayList());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setBanner();
    }

    private void sliderImages(final ArrayList<Banners> bannersArrayList) {
        //   final float density = getResources().getDisplayMetrics().density;
        NUM_PAGES = bannersArrayList.size();
        binding.sliderDots.removeAllViews();
        Log.e("NUM_PAGES", "=" + NUM_PAGES);

        dots = new ImageView[NUM_PAGES];
        for (int i = 0; i < NUM_PAGES; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            binding.sliderDots.addView(dots[i], params);
        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
        binding.pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < NUM_PAGES; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public boolean checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 2: {

                return;
            }
        }
    }

    public void getPickImageIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }
        startActivityForResult(intent, PICK_IMAGES);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGES) {
                if (data.getClipData() != null) {
                    ClipData mClipData = data.getClipData();
                    Log.e("Size of Items", "=" + mClipData.getItemCount());
                    for (int i = 0; i < mClipData.getItemCount(); i++) {
                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri uri = item.getUri();
                        String actualPath = ImageFilePath.getPath(this, uri);
                        categoryList.add(new MainCategoryBean(/*actualPath,*/ uri.toString(), "", "", (i + 1), true));
                        Log.e("Name of Items", "=" + actualPath);
                        categoryAdapter.notifyDataSetChanged();
                    }
                } else if (data.getData() != null) {
                    Uri uri = data.getData();
                    String actualPath = ImageFilePath.getPath(this, uri);
                    categoryList.add(new MainCategoryBean(/*actualPath,*/ uri.toString(), "", "", (categoryList.size() + 1), true));
                    categoryAdapter.notifyDataSetChanged();
                    Log.e("uri Oneimg=", "=" + uri);
                }
            }
            if (requestCode == PICK_IMAGESINGLE) {
                Uri uri = data.getData();
                String actualPath = ImageFilePath.getPath(this, uri);
                if (uri != null) categoryBean.setCategoryImage(uri.toString());
                categoryBean.setCategoryImage(actualPath);
                dialog.show();
                dialog.setImageEdit(categoryBean);
            }

            if (requestCode == PICK_IMAGESBANNERS) {
                if (data.getClipData() != null) {
                    ClipData mClipData = data.getClipData();
                    Log.e("Size of Items", "=" + mClipData.getItemCount());
                    for (int i = 0; i < mClipData.getItemCount(); i++) {
                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri uri = item.getUri();
                        String actualPath = ImageFilePath.getPath(this, uri);
                        SingletonManager.getSingletonManager().getBannersArrayList().add(new Banners(actualPath, uri.toString(), "", "", (i + 1)));
                        Log.e("Name of Items", "=" + actualPath);
                    }
                } else if (data.getData() != null) {
                    Uri uri = data.getData();
                    String actualPath = ImageFilePath.getPath(this, uri);
                    SingletonManager.getSingletonManager().getBannersArrayList().add(new Banners(actualPath, uri.toString(), "", "", (SingletonManager.getSingletonManager().getBannersArrayList().size() + 1)));
                }
                startActivity(new Intent(this, BannerSequenceActivity.class));
            }
        }
        if (categoryList.size() > 0) {
            binding.ifNoCateTextView.setVisibility(View.GONE);
        } else {
            binding.ifNoCateTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNoteListChanged(ArrayList<MainCategoryBean> customers) {

    }

    @Override
    public void onEdit(MainCategoryBean categoryBean) {
        dialog = new CategoryDialog(this, "forCategory", "", categoryBean, this, imageLoader);
        dialog.show();
    }

    @Override
    public void onClickRow(final MainCategoryBean categoryBean) {

        /*AlertUtils.withTwoButtons(this, "Add ?", "Subcategory or Product", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(CategoryAddActivity.this, "Coming Soon..", Toast.LENGTH_SHORT).show();
                categoryBean.setSubCatAvail(false);
            }
        }, "Product", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                categoryBean.setSubCatAvail(true);
                startActivity(new Intent(CategoryAddActivity.this, SubCateActivity.class).putExtra("beanObj", categoryBean));
            }
        }, "Subcategory");*/

        if (categoryBean.getId() != null && !categoryBean.getId().isEmpty()) {
            if (categoryBean.isHas_sub_category()) {
                startActivity(new Intent(this, SubCateActivity.class).putExtra("catId", categoryBean.getId()).putExtra("beanObj", categoryBean));
            } else {
                startActivity(new Intent(this, ItemsActivity.class).putExtra("catId", categoryBean.getId()).putExtra("hasSubCate", categoryBean.isHas_sub_category()));
            }

        } else {

            new CommonDialog(this, "You want to add subcategory ?", "showYN", themeColor()) {
                @Override
                protected void onYes() {
                    dismiss();
                    categoryBean.setSubCatAvail(true);
                    startActivity(new Intent(CategoryAddActivity.this, SubCateActivity.class).putExtra("beanObj", categoryBean));
                }

                @Override
                protected void onNo() {
                    dismiss();
                }
            }.show();
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onClickDone(String catName, String desc) {

        categoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClickEditImage(MainCategoryBean categoryBean) {
        this.categoryBean = categoryBean;
        getPickImageSingleIntent();
    }

    @Override
    public void onClickEditSubCateImage(SubCategoryBean subCategoryBean) {

    }

    private void getPickImageSingleIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        }
        startActivityForResult(intent, PICK_IMAGESINGLE);
    }

    private void scrollShowHide() {
        binding.categoryRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int lastCompletelyVisibleItemPosition = 0;
                        lastCompletelyVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                        try {
                            if (lastCompletelyVisibleItemPosition == SingletonManager.getSingletonManager().getBannersArrayList().size() - 1) {
                                binding.fabFloatingActionButton.setVisibility(View.GONE);
                            } else {
                                binding.fabFloatingActionButton.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 500);
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }
}