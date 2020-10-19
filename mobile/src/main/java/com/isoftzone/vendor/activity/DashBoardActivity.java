package com.isoftzone.vendor.activity;

import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageInfo;
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
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.isoftzone.vendor.R;
import com.isoftzone.vendor.adapter.HomeAdapter;
import com.isoftzone.vendor.adapter.HomeGridAdapter;
import com.isoftzone.vendor.adapter.SlidingImageAdapter;
import com.isoftzone.vendor.baseactivity.BaseActivity;
import com.isoftzone.vendor.bean.Banners;
import com.isoftzone.vendor.bean.CompanyDetails;
import com.isoftzone.vendor.bean.MainCategoryBean;
import com.isoftzone.vendor.bean.ProductBean;
import com.isoftzone.vendor.bean.SelectedProduct;
import com.isoftzone.vendor.bean.SingletonManager;
import com.isoftzone.vendor.bean.WalletBeanDetail;
import com.isoftzone.vendor.network.CommonInterfaces;
import com.isoftzone.vendor.network.MakeParamsHandler;
import com.isoftzone.vendor.network.RestApiManager;
import com.isoftzone.vendor.dialog.CommonDialog;
import com.isoftzone.vendor.util.ImageFilePath;
import com.isoftzone.vendor.util.InternetDedecter;
import com.isoftzone.vendor.util.SharedPref;
import com.isoftzone.vendor.vandor.activity.BannerSequenceActivity;
import com.isoftzone.vendor.vandor.activity.CategoryAddActivity;

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
    private int PICK_IMAGESBANNERS = 995;
    private ArrayList<MainCategoryBean> homeModelArrayList = new ArrayList<>();

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

        findViewById(R.id.editBannerImageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkUserIsLogin()) return;
                startActivity(new Intent(DashBoardActivity.this, BannerSequenceActivity.class));
            }
        });
        findViewById(R.id.addBannerImageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkUserIsLogin()) return;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                }
                startActivityForResult(intent, PICK_IMAGESBANNERS);
            }
        });

        FloatingActionMenu menuFab = findViewById(R.id.menuFab);

     /*   menuFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuFab.close(false);
            }
        });*/

        FloatingActionButton fabAddCate = findViewById(R.id.fabAddCate);
        fabAddCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuFab.close(false);
                if (checkUserIsLogin()) return;
                startActivity(new Intent(DashBoardActivity.this, CategoryAddActivity.class));
            }
        });

        FloatingActionButton fabEditCate = findViewById(R.id.fabEditCate);
        fabEditCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuFab.close(false);
                if (checkUserIsLogin()) return;
               startActivity(new Intent(DashBoardActivity.this, CategoryAddActivity.class).putExtra("beanObj", homeModelArrayList));
               // Toast.makeText(DashBoardActivity.this, "Under development mode", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean checkUserIsLogin(){
        if (SharedPref.getPrefsHelper().getPref("user") == null || SharedPref.getPrefsHelper().getPref("user").toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(DashBoardActivity.this, "Please login first", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(DashBoardActivity.this, LoginActivity.class));
           return true;
        }else {
            return false;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
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
        this.homeModelArrayList.addAll(homeModelArrayList);
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

        for (int i = 0; i < bannersArrayList.size(); i++) {
            SingletonManager.getSingletonManager().getBannersArrayList().add(new Banners("", bannersArrayList.get(i).getImage(), "", "", (i + 1)));
        }

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
