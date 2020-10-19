package com.isoftzone.vendor.vandor.activity;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.isoftzone.vendor.R;
import com.isoftzone.vendor.network.CommonInterfaces;
import com.isoftzone.vendor.network.Constants;
import com.isoftzone.vendor.network.MakeParamsHandler;
import com.isoftzone.vendor.network.RestApiManager;
import com.isoftzone.vendor.util.SharedPref;
import com.isoftzone.vendor.vandor.adapter.BannerSequenceAdapter;
import com.isoftzone.vendor.baseactivity.BaseActivity;
import com.isoftzone.vendor.bean.Banners;
import com.isoftzone.vendor.bean.CompanyDetails;
import com.isoftzone.vendor.bean.SimpleItemTouchHelperCallback;
import com.isoftzone.vendor.bean.SingletonManager;
import com.isoftzone.vendor.databinding.ActivityBannerSequenceBinding;
import com.isoftzone.vendor.util.ImageFilePath;
import com.isoftzone.vendor.vandor.listener.OnBannerListChangedListener;
import com.isoftzone.vendor.vandor.listener.OnStartDragSubCatListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class BannerSequenceActivity extends BaseActivity implements OnBannerListChangedListener, OnStartDragSubCatListener, CommonInterfaces.updateBanner {

    private ActivityBannerSequenceBinding binding;
    private BannerSequenceAdapter sequenceAdapter;
    private ItemTouchHelper mItemTouchHelper;
    public static int PICK_IMAGES = 880;
    public static int PICK_IMAGESINGLE = 881;
    private Banners banners;
    ;
    private String pathImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_banner_sequence);
        binding.bannersRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        binding.bannersRecyclerView.setLayoutManager(mLayoutManager);
        sequenceAdapter = new BannerSequenceAdapter(this, SingletonManager.getSingletonManager().getBannersArrayList(), this, this, imageLoader);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(sequenceAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(binding.bannersRecyclerView);

        binding.bannersRecyclerView.setAdapter(sequenceAdapter);
        binding.donTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sequenceAdapter.notifyDataSetChanged();
                finish();
            }
        });

        if (CompanyDetails.getInstance().getDetails() != null) {
            binding.toolbarTitleTextView.setText(CompanyDetails.getInstance().getDetails().getCompanyName());
        }

        binding.backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.fabFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPickImageIntent();
            }
        });

        scrollShowHide();
    }

    private void getPickImageIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }
        startActivityForResult(intent, PICK_IMAGES);
    }

    @Override
    public void onNoteListChanged(ArrayList<Banners> bannersList) {

    }

    @Override
    public void onEdit(Banners banners) {
        this.banners = banners;
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
    public void onClickRow(Banners banners) {

    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
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
                        SingletonManager.getSingletonManager().getBannersArrayList().add(new Banners(actualPath, uri.toString(), "", "", (i + 1)));
                        Log.e("Name of Items", "=" + actualPath);
                        imageUpdate();
                    }
                } else if (data.getData() != null) {
                    Uri uri = data.getData();
                    pathImg = ImageFilePath.getPath(this, uri);
                    pathImg = ImageFilePath.compressImage(pathImg);
                    SingletonManager.getSingletonManager().getBannersArrayList().add(new Banners(pathImg, uri.toString(), "", "", (SingletonManager.getSingletonManager().getBannersArrayList().size() + 1)));
                    imageUpdate();
                }
            }
            if (requestCode == PICK_IMAGESINGLE) {
                Uri uri = data.getData();
                String pathImg = ImageFilePath.getPath(this, uri);
                if (uri != null)
                    this.banners.setImage(uri.toString());
            }
        }
        sequenceAdapter.notifyDataSetChanged();
    }

    private void scrollShowHide() {
        binding.bannersRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

    private void imageUpdate() {
        showDialog(this);
        RestApiManager.updateBanner(MakeParamsHandler.getRequestBody(getMakeParameter()), this, this);
    }

    private String getMakeParameter() {
        JSONObject jsonObject = new JSONObject();
        String firmImage = "";
        if (!pathImg.equalsIgnoreCase("NONE")) {
            firmImage = getImageFilePathToByteArray(pathImg);
            firmImage = "data:image/jpeg;base64," + firmImage;
            Log.e("64ImgStr=", "firmImage=" + firmImage);
        }
        try {
            jsonObject.put("image", firmImage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static String getImageFilePathToByteArray(String filePath) {
        String encodeString = null;
        try {
            Bitmap bm = BitmapFactory.decodeFile(filePath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
            byte[] byteArrayImage = baos.toByteArray();
            encodeString = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encodeString;
    }

    @Override
    public void updateBannerSuccess(String msg) {
        dismissDialog();
        Toast.makeText(this, "Update", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateFailure(String failedResp) {
        dismissDialog();
        Toast.makeText(this, "Not update", Toast.LENGTH_SHORT).show();
    }
}