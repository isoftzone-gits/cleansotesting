package com.isoftzone.yoappstore.baseactivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.isoftzone.yoappstore.R;
import com.isoftzone.yoappstore.activity.LoginActivity;
import com.isoftzone.yoappstore.activity.NotificationActivity;
import com.isoftzone.yoappstore.activity.PastOrdersActivity;
import com.isoftzone.yoappstore.activity.ReviewItemsActivity;
import com.isoftzone.yoappstore.activity.SignUpActivity;
import com.isoftzone.yoappstore.activity.UpcomingOrdersActivity;
import com.isoftzone.yoappstore.activity.WalletActivity;
import com.isoftzone.yoappstore.activity.WebViewActivity;
import com.isoftzone.yoappstore.adapter.DrawerAdapter;
import com.isoftzone.yoappstore.bean.CompanyDetails;
import com.isoftzone.yoappstore.bean.DrawerBean;
import com.isoftzone.yoappstore.bean.DrawerChildBean;
import com.isoftzone.yoappstore.bean.SelectedProduct;
import com.isoftzone.yoappstore.bean.UserBean;
import com.isoftzone.yoappstore.network.Constants;
import com.isoftzone.yoappstore.network.JSonParser;
import com.isoftzone.yoappstore.util.CommonUtils;
import com.isoftzone.yoappstore.util.CustomDialog;
import com.isoftzone.yoappstore.util.SharedPref;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class BaseActivity extends AppCompatActivity implements View.OnClickListener, DrawerAdapter.Listner {

    protected DrawerLayout mDrawer;
    protected ImageView menuLeftDrawer, logoImageView, walletImageView;
    public ImageView backImageView, notificationImageView;
    protected Toolbar toolBar;
    protected TextView toolbarTitleTextView, cartCountTextView, drawerFirmTitleTextView;
    protected RelativeLayout cartRelativeLayout;
    public ImageLoader imageLoader;

    public RecyclerView drawerRecyclerView;
    public DrawerAdapter drawerAdapter;
    public UserBean userBean;
    private com.isoftzone.yoappstore.util.ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageLoader = ImageLoader.getInstance();
        progressDialog = new com.isoftzone.yoappstore.util.ProgressDialog(this);
        if (SharedPref.getPrefsHelper().getPref("user") != null) {
            userBean = JSonParser.stringJSonToUserLoggedInfo();
        }
    }


    protected void setToolBar(String title, boolean isMenuShow, boolean showBackArrow) {
        toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        mDrawer = findViewById(R.id.drawer_layout);
        drawerRecyclerView = findViewById(R.id.drawerRecyclerView);
        drawerFirmTitleTextView = findViewById(R.id.drawerFirmTitleTextView);
        LinearLayout drawerMainLinearLayout = findViewById(R.id.drawerMainLinearLayout);
        menuLeftDrawer = toolBar.findViewById(R.id.menuLeftDrawer);
        backImageView = toolBar.findViewById(R.id.backImageView);
        notificationImageView = toolBar.findViewById(R.id.notificationImageView);
        toolbarTitleTextView = toolBar.findViewById(R.id.toolbarTitleTextView);
        cartCountTextView = toolBar.findViewById(R.id.cartCountTextView);
        logoImageView = findViewById(R.id.logoImageView);
        walletImageView = findViewById(R.id.walletImageView);
        walletImageView.setOnClickListener(this);
        menuLeftDrawer.setOnClickListener(this);
        backImageView.setOnClickListener(this);
        cartCountTextView.setOnClickListener(this);
        notificationImageView.setOnClickListener(this);
        notificationImageView.setVisibility(View.VISIBLE);
        walletImageView.setVisibility(View.VISIBLE);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        if (CompanyDetails.getInstance().getDetails() != null) {
            toolbarTitleTextView.setText(CompanyDetails.getInstance().getDetails().getCompanyName());
            drawerFirmTitleTextView.setText(CompanyDetails.getInstance().getDetails().getCompanyName());
        } else {
            toolbarTitleTextView.setText("");
            drawerFirmTitleTextView.setText("");
        }

        if (logoImageView != null) {
            logoImageView.setImageResource(R.drawable.launcher_iconapp);
        }

        menuLeftDrawer.setVisibility(isMenuShow ? View.VISIBLE : View.GONE);
        backImageView.setVisibility(showBackArrow ? View.VISIBLE : View.GONE);
        drawerRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        int serverColor = Color.parseColor(CompanyDetails.getInstance().getDetails().getThemeColor());
        drawerMainLinearLayout.setBackgroundColor(serverColor);
        changeAppThemeColor();

    }

    protected int themeColor() {
        return Color.parseColor(CompanyDetails.getInstance().getDetails().getThemeColor());
    }

    protected void setTheamColor(View view){
        Drawable backgroundDrawable = DrawableCompat.wrap(view.getBackground()).mutate();
        DrawableCompat.setTint(backgroundDrawable, themeColor());
    }

    private void setDrawerItems() {
        ArrayList<DrawerBean> drawerBeanArrayList = new ArrayList<>();
        ArrayList<DrawerChildBean> companyGroup = new ArrayList<>();
        companyGroup.add(new DrawerChildBean("Past Orders", R.drawable.order, 1));
        companyGroup.add(new DrawerChildBean("Upcoming Orders", R.drawable.refund, 2));
        drawerBeanArrayList.add(new DrawerBean("Orders", companyGroup, false));
        ArrayList<DrawerChildBean> legalInfo = new ArrayList<>();
        legalInfo.add(new DrawerChildBean("Payment Options", R.drawable.payment_option, 6));
        legalInfo.add(new DrawerChildBean("Privacy Policy", R.drawable.privacy, 7));
        legalInfo.add(new DrawerChildBean("Terms Conditions", R.drawable.term_condition, 8));
        legalInfo.add(new DrawerChildBean("Refund Policy", R.drawable.refund, 9));
        legalInfo.add(new DrawerChildBean("About", R.drawable.about_us, 5));
        legalInfo.add(new DrawerChildBean("Contact Us", R.drawable.contact_us, 10));
        drawerBeanArrayList.add(new DrawerBean("Legal Information", legalInfo, false));
        ArrayList<DrawerChildBean> profileGroup = new ArrayList<>();
        if (SharedPref.getPrefsHelper().getPref("user") != null && !SharedPref.getPrefsHelper().getPref("user").toString().trim().equalsIgnoreCase("")) {
            profileGroup.add(new DrawerChildBean("Profile", R.drawable.profile, 3));
            profileGroup.add(new DrawerChildBean("Change Password", R.drawable.password, 4));
            profileGroup.add(new DrawerChildBean("Share & Earn", R.drawable.share, 13));
            profileGroup.add(new DrawerChildBean("Logout", R.drawable.logout, 11));
        } else {
            profileGroup.add(new DrawerChildBean("Login", R.drawable.logout, 12));
        }
        drawerBeanArrayList.add(new DrawerBean("App Setting", profileGroup, false));
        if (drawerRecyclerView != null) {
            drawerAdapter = new DrawerAdapter(this, drawerBeanArrayList, this);
            drawerRecyclerView.setAdapter(drawerAdapter);
        }
    }

    protected void setCustomToolBar(String title, boolean isMenuShow, boolean showBackArrow) {

        toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        cartRelativeLayout = toolBar.findViewById(R.id.cartRelativeLayout);
        menuLeftDrawer = toolBar.findViewById(R.id.menuLeftDrawer);
        backImageView = toolBar.findViewById(R.id.backImageView);
        toolbarTitleTextView = toolBar.findViewById(R.id.toolbarTitleTextView);
        cartCountTextView = toolBar.findViewById(R.id.cartCountTextView);
        menuLeftDrawer.setOnClickListener(this);
        backImageView.setOnClickListener(this);
        cartCountTextView.setOnClickListener(this);

        if (CompanyDetails.getInstance().getDetails() != null) {
            toolbarTitleTextView.setText(CompanyDetails.getInstance().getDetails().getCompanyName());
        } else {
            toolbarTitleTextView.setText("");
        }

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        menuLeftDrawer.setVisibility(isMenuShow ? View.VISIBLE : View.GONE);
        backImageView.setVisibility(showBackArrow ? View.VISIBLE : View.GONE);
        changeAppThemeColor();
    }

    protected void changeAppThemeColor() {
        int serverColor = Color.parseColor(CompanyDetails.getInstance().getDetails().getThemeColor());
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(serverColor);
        }
        toolBar.setBackgroundColor(serverColor);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDrawerItems();
        if (SharedPref.getPrefsHelper().getPref("user") != null) {
            userBean = JSonParser.stringJSonToUserLoggedInfo();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void showDialog(Activity context) {
        progressDialog.show();
    }


    protected void dismissDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menuLeftDrawer:
                mDrawer.openDrawer(GravityCompat.START);
                break;
            case R.id.backImageView:
                finish();
                break;
            case R.id.walletImageView:
                if (SharedPref.getPrefsHelper().getPref("user") == null || SharedPref.getPrefsHelper().getPref("user").toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, LoginActivity.class));
                    return;
                }
                startActivity(new Intent(this, WalletActivity.class));
                break;
            case R.id.notificationImageView:
                if (SharedPref.getPrefsHelper().getPref("user") == null || SharedPref.getPrefsHelper().getPref("user").toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, LoginActivity.class));
                    return;
                }
                startActivity(new Intent(this, NotificationActivity.class));
                break;
            case R.id.cartCountTextView:
                if (SelectedProduct.getInstance().getSelectedProductList().size() > 0) {
                    startActivity(new Intent(this, ReviewItemsActivity.class));
                } else {
                    Toast.makeText(this, "Please select at least one item", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onClickChildParent(DrawerChildBean bean) {
        switch (bean.getId()) {
            case 1:
                if (SharedPref.getPrefsHelper().getPref("user") == null || SharedPref.getPrefsHelper().getPref("user").toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, LoginActivity.class));
                    return;
                }
                startActivity(new Intent(this, PastOrdersActivity.class));
                break;
            case 2:
                if (SharedPref.getPrefsHelper().getPref("user") == null || SharedPref.getPrefsHelper().getPref("user").toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, LoginActivity.class));
                    return;
                }
                startActivity(new Intent(this, UpcomingOrdersActivity.class));
                break;
            case 3:
                if (SharedPref.getPrefsHelper().getPref("user") == null || SharedPref.getPrefsHelper().getPref("user").toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, LoginActivity.class));
                    return;
                }
                startActivity(new Intent(this, SignUpActivity.class).putExtra("via", "profile"));
                break;
            case 4:
                CustomDialog customDialog = new CustomDialog(this, "changePassword", userBean.getId(), themeColor());
                customDialog.show();
                break;
            case 5:
                startActivity(new Intent(this, WebViewActivity.class).putExtra("url", Constants.APIMAIN_URL + "get_about_us"));
                break;
            case 6:
                startActivity(new Intent(this, WebViewActivity.class).putExtra("url", Constants.APIMAIN_URL + "get_payment_options"));
                break;
            case 7:
                startActivity(new Intent(this, WebViewActivity.class).putExtra("url", Constants.APIMAIN_URL + "get_privacy_policy"));
                break;
            case 8:
                startActivity(new Intent(this, WebViewActivity.class).putExtra("url", Constants.APIMAIN_URL + "get_terms_conditions"));
                break;
            case 9:
                startActivity(new Intent(this, WebViewActivity.class).putExtra("url", Constants.APIMAIN_URL + "get_refund_policy"));
                break;
            case 10:
                startActivity(new Intent(this, WebViewActivity.class).putExtra("url", Constants.APIMAIN_URL + "get_contact_us"));
                break;
            case 12:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case 11:
                SharedPref.getPrefsHelper().delete("user");
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case 13:
                final String appPackageName = getPackageName();
                String linkUrl = "https://play.google.com/store/apps/details?id=" + appPackageName;
                String shareText = "Share and Earn \n Download the app via play store\n" + linkUrl + " and register yourself and use this referral code " + userBean.getUnique_id();
                CommonUtils.shareText(this, shareText);
                break;
        }
    }
}