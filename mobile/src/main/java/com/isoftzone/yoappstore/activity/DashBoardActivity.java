package com.isoftzone.yoappstore.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.isoftzone.yoappstore.R;
import com.isoftzone.yoappstore.adapter.HomeAdapter;
import com.isoftzone.yoappstore.adapter.HomeGridAdapter;
import com.isoftzone.yoappstore.adapter.SlidingImageAdapter;
import com.isoftzone.yoappstore.baseactivity.BaseActivity;
import com.isoftzone.yoappstore.bean.Banners;
import com.isoftzone.yoappstore.bean.CompanyDetails;
import com.isoftzone.yoappstore.bean.MainCategoryBean;
import com.isoftzone.yoappstore.bean.ProductBean;
import com.isoftzone.yoappstore.bean.SelectedProduct;
import com.isoftzone.yoappstore.bean.WalletBeanDetail;
import com.isoftzone.yoappstore.network.CommonInterfaces;
import com.isoftzone.yoappstore.network.MakeParamsHandler;
import com.isoftzone.yoappstore.network.RestApiManager;
import com.isoftzone.yoappstore.util.CommonDialog;
import com.isoftzone.yoappstore.util.InternetDedecter;
import com.isoftzone.yoappstore.util.SharedPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class DashBoardActivity extends BaseActivity implements CommonInterfaces.RecyclerClickDelegate,
        CommonInterfaces.get_category, CommonInterfaces.getAppUpdateInfo, CommonInterfaces.getPaymentDetails {

    private ViewPager mPager;
    private android.widget.LinearLayout sliderDots;
    private RecyclerView homeRecyclerView, gridRecyclerView;
    private int currentPage = 0;
    private int NUM_PAGES = 0;
    private ImageView[] dots;
    private boolean doubleBackToExitPressedOnce = false;

    private String appPackageName = "";
    private String appVersionName = "";
    private String appVersionCode = "";

    String type = "";
    boolean isFromNotification = false;
    LinearLayout searchMainLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        setToolBar("", true, false);
        init();
        getVersionInfo();
        getMainCategory();
        getAppUpdateInfo();
        isAlreadyCartDataCheck();
    }

    private void isAlreadyCartDataCheck() {
        if (SharedPref.getPrefsHelper().getPref("cartJson") != null && !SharedPref.getPrefsHelper().getPref("cartJson").toString().equalsIgnoreCase("")) {
            Type listType = new TypeToken<ArrayList<ProductBean>>() {
            }.getType();
            ArrayList<ProductBean> selectedProductList = new GsonBuilder().create().fromJson(SharedPref.getPrefsHelper().getPref("cartJson").toString().trim(), listType);
            SelectedProduct.getInstance().getSelectedProductList().addAll(selectedProductList);
            cartCountTextView.setText("" + SelectedProduct.getInstance().getSelectedProductList().size());
            cartCountTextView.setVisibility(SelectedProduct.getInstance().getSelectedProductList().size() > 0 ? View.VISIBLE : View.GONE);
        }
    }

    private void getAppUpdateInfo() {
        if (InternetDedecter.isNetworkAvailable(this)) {
            RestApiManager.getAppUpdateInfo(this, this);
        }
    }

    @Override
    public void onBackPressed() {
        if (!doubleBackToExitPressedOnce) {
            this.doubleBackToExitPressedOnce = true;
            exitDailog();
        }
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private void exitDailog() {
        CommonDialog dialog = new CommonDialog(this, "Are you sure want to exit?", "showYN",themeColor()) {
            @Override
            protected void onYes() {
                dismiss();
                finish();
            }

            @Override
            protected void onNo() {
                dismiss();
                doubleBackToExitPressedOnce = false;
            }
        };
        dialog.setCancelable(false);
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    private void getVersionInfo() {
        int versionCode = -1;
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            appPackageName = getPackageName();
            appVersionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;
            appVersionCode = String.valueOf(versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Log.e("packageName", "packageName = " + appPackageName);
        Log.e("versionName", "versionName = " + appVersionName);
        Log.e("VersionCode", "VersionCode = " + appVersionCode);
    }

    private void init() {
        isFromNotification = getIntent().getBooleanExtra("isFromNotification", false);
        type = getIntent().getStringExtra("type");

        androidx.core.widget.NestedScrollView scrollNestedScrollView = findViewById(R.id.scrollNestedScrollView);
        this.homeRecyclerView = findViewById(R.id.homeRecyclerView);
        this.gridRecyclerView = findViewById(R.id.gridRecyclerView);
        this.sliderDots = findViewById(R.id.sliderDots);
        this.mPager = findViewById(R.id.pager);
        this.searchMainLinearLayout = findViewById(R.id.searchMainLinearLayout);

        homeRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        gridRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        scrollNestedScrollView.getParent().requestChildFocus(scrollNestedScrollView, scrollNestedScrollView);

        searchMainLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashBoardActivity.this, SearchActivity.class));
            }
        });

        if (isFromNotification) {
            if (userBean != null) {
                if (type.equalsIgnoreCase("Order Status")) {
                    startActivity(new Intent(this, UpcomingOrdersActivity.class));
                } else {
                    startActivity(new Intent(this, NotificationActivity.class));
                }
            } else {
                Toast.makeText(this, "Please Login First", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        cartCountTextView.setText("" + SelectedProduct.getInstance().getSelectedProductList().size());
        cartCountTextView.setVisibility(SelectedProduct.getInstance().getSelectedProductList().size() > 0 ? View.VISIBLE : View.GONE);
        getPaymentDetails();
    }

    private void getPaymentDetails() {
        if (userBean != null) {
            try {
                JSONObject object = new JSONObject();
                object.put("user_id", userBean.getId());
                Log.e("getPaymentDetails=", "=" + userBean.getId());
                RestApiManager.getPaymentDetails(MakeParamsHandler.getRequestBody(object.toString()), this, this);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void getMainCategory() {
        showDialog(this);
        RestApiManager.get_category(this, this);
    }

    private void sliderImages(final ArrayList<Banners> bannersArrayList) {
        NUM_PAGES = bannersArrayList.size();
        Log.e("NUM_PAGES", "=" + NUM_PAGES);
        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 4000, 4000);

        // For Slider Dots
        dots = new ImageView[NUM_PAGES];
        for (int i = 0; i < NUM_PAGES; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            sliderDots.addView(dots[i], params);
        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

    @Override
    public void recyclerItemDelegate(String data) {

    }

    @Override
    public void successMainCategory(ArrayList<MainCategoryBean> homeModelArrayList, ArrayList<Banners> bannersArrayList, CompanyDetails companyDetails) {
        dismissDialog();
        if (CompanyDetails.getInstance().getDetails().getProduct_view().equalsIgnoreCase("list")) {
            homeRecyclerView.setVisibility(View.VISIBLE);
            gridRecyclerView.setVisibility(View.GONE);
            HomeAdapter homeAdapter = new HomeAdapter(homeModelArrayList, imageLoader, this, this,themeColor());
            homeRecyclerView.setAdapter(homeAdapter);
        } else {
            gridRecyclerView.setVisibility(View.VISIBLE);
            homeRecyclerView.setVisibility(View.GONE);
            HomeGridAdapter homeGridAdapter = new HomeGridAdapter(homeModelArrayList, imageLoader, this, this);
            gridRecyclerView.setAdapter(homeGridAdapter);
        }
        SlidingImageAdapter slidingImageAdapter = new SlidingImageAdapter(this, bannersArrayList, imageLoader);
        mPager.setAdapter(slidingImageAdapter);
        sliderImages(bannersArrayList);
        CompanyDetails.getInstance().setDetails(companyDetails);
    }

    @Override
    public void successAppUpdateInfo(String resp, String versionName, String versionCode) {
        if (!versionCode.equalsIgnoreCase(appVersionCode)) {
            updateAppAlert();
        }
    }

    private void updateAppAlert() {
        CommonDialog dialog = new CommonDialog(this, "Please update the app to the latest version and continue using all the features without interruptions.", "showYN",themeColor()) {
            @Override
            protected void onYes() {
                dismiss();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                finish();
            }

            @Override
            protected void onNo() {
                dismiss();
            }
        };
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void successPaymentDetails(WalletBeanDetail walletBeanDetail) {
        CompanyDetails.getInstance().setWalletBeanDetail(walletBeanDetail);
    }

    @Override
    public void failure(String error) {
        dismissDialog();
        Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();
    }
}
