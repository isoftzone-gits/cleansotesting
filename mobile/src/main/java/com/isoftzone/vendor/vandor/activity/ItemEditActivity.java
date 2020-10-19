package com.isoftzone.vendor.vandor.activity;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import com.isoftzone.vendor.R;
import com.isoftzone.vendor.vandor.adapter.ProductCategoryAdapter;
import com.isoftzone.vendor.baseactivity.BaseActivity;
import com.isoftzone.vendor.bean.MainCategoryBean;
import com.isoftzone.vendor.bean.ProductBean;
import com.isoftzone.vendor.bean.SimpleItemTouchHelperCallback;
import com.isoftzone.vendor.bean.SingletonManager;
import com.isoftzone.vendor.bean.SubCategoryBean;
import com.isoftzone.vendor.databinding.ActivitySubCateBinding;
import com.isoftzone.vendor.dialog.CategoryDialog;
import com.isoftzone.vendor.vandor.listener.OnProductListChangedListener;
import com.isoftzone.vendor.vandor.listener.OnStartDragSubCatListener;
import com.isoftzone.vendor.network.CommonInterfaces;
import com.isoftzone.vendor.network.MakeParamsHandler;
import com.isoftzone.vendor.network.RestApiManager;
import com.isoftzone.vendor.util.ImageFilePath;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ItemEditActivity extends BaseActivity implements OnStartDragSubCatListener, CategoryDialog.Listner, CommonInterfaces.get_sub_category, CommonInterfaces.get_product, OnProductListChangedListener {

    private ActivitySubCateBinding binding;
    private int PICK_IMAGES = 996;
    private int PICK_IMAGESINGLE = 997;
    private ArrayList<ProductBean> subCategoryList;
    private ProductCategoryAdapter subCategoryAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private SubCategoryBean subCategoryBean;
    private CategoryDialog dialog;
    private SubCategoryBean beanObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sub_cate);
        subCategoryList = new ArrayList<>();

        beanObj = (SubCategoryBean) getIntent().getSerializableExtra("beanObj");
        if (beanObj != null) {
            binding.toolbarTitleTextView.setText(beanObj.getSubCatName());
        }

        binding.fabFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPickImageIntent();
            }
        });

        binding.backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.subCategoryRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        binding.subCategoryRecyclerView.setLayoutManager(mLayoutManager);

        binding.donTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subCategoryAdapter.notifyDataSetChanged();
            }
        });

        if (beanObj.getId() == null) {
            getPickImageIntent();
            setAdapter();
        } else {
            get_product();
        }

    }

    private void get_product() {
        try {
            JSONObject object = new JSONObject();
            object.put("cat_id", beanObj.getCategoryId());
            object.put("s_cat_id", beanObj.getId());
            object.put("pagenum", 1);
            Log.e("get_product=", "==" + object.toString());
            showDialog(this);
            RestApiManager.get_product(MakeParamsHandler.getRequestBody(object.toString()), this);
        } catch (JSONException e) {
            e.printStackTrace();
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
                        subCategoryList.add(new ProductBean(uri.toString(), "", "", (i + 1)));
                        Log.e("Name of Items", "=" + actualPath);
                    }
                } else if (data.getData() != null) {
                    Uri uri = data.getData();
                    String actualPath = ImageFilePath.getPath(this, uri);
                    subCategoryList.add(new ProductBean(uri.toString(), "", "", (subCategoryList.size() + 1)));
                }
            }

            if (requestCode == PICK_IMAGESINGLE) {
                Uri uri = data.getData();
                String actualPath = ImageFilePath.getPath(this, uri);
                if (uri != null) subCategoryBean.setUri(uri.toString());
                subCategoryBean.setSubCatImage(actualPath);
                dialog.show();
                dialog.setImageEditSubCate(subCategoryBean);
            }
        }
        subCategoryAdapter.notifyDataSetChanged();

        if (subCategoryList.size() > 0) {
            binding.ifNoCateTextView.setVisibility(View.GONE);
        } else {
            binding.ifNoCateTextView.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onNoteListChanged(ArrayList<ProductBean> subCategoryBeans) {

    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onClickDone(String catName, String desc) {
        subCategoryAdapter.notifyDataSetChanged();
    }

    // not used
    @Override
    public void onClickEditImage(MainCategoryBean categoryBean) {

    }

    @Override
    public void onClickEditSubCateImage(SubCategoryBean subCategoryBean) {
        this.subCategoryBean = subCategoryBean;
        getPickImageSingleIntent();
    }

    public void getPickImageSingleIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        }
        startActivityForResult(intent, PICK_IMAGESINGLE);
    }

    @Override
    public void successProduct(ArrayList<ProductBean> productBeanArrayList) {
        dismissDialog();
        this.subCategoryList = productBeanArrayList;
        binding.ifNoCateTextView.setVisibility(View.GONE);
        setAdapter();
    }

    private void setAdapter() {
        subCategoryAdapter = new ProductCategoryAdapter(this, subCategoryList, this, this, imageLoader);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(subCategoryAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(binding.subCategoryRecyclerView);
        binding.subCategoryRecyclerView.setAdapter(subCategoryAdapter);
        scrollShowHide();
    }

    @Override
    public void successSubCategory(ArrayList<SubCategoryBean> subList) {

    }

    @Override
    public void failure(String error) {
        dismissDialog();
    }

    private void scrollShowHide() {
        binding.subCategoryRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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


    @Override
    public void onEdit(ProductBean subCategoryBean) {
        dialog = new CategoryDialog(this, "product", "", subCategoryBean, this, imageLoader);
        dialog.show();
    }

    @Override
    public void onClickRow(ProductBean subCategoryBean) {

    }
}