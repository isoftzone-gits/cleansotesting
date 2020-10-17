package com.isoftzone.yoappstore.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.isoftzone.yoappstore.R;
import com.isoftzone.yoappstore.bean.Banners;
import com.isoftzone.yoappstore.bean.CompanyDetails;
import com.isoftzone.yoappstore.bean.MainCategoryBean;
import com.isoftzone.yoappstore.network.CommonInterfaces;
import com.isoftzone.yoappstore.network.RestApiManager;
import com.isoftzone.yoappstore.util.CommonDialog;
import com.isoftzone.yoappstore.util.PermissionChecker;
import com.isoftzone.yoappstore.util.SharedPref;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity implements CommonInterfaces.get_category {

    private PermissionChecker permissionChecker;
    private String type = "";
    boolean isFromNotification = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        permissionChecker = new PermissionChecker(this);
        isFromNotification = getIntent().getBooleanExtra("isNotification", false);
        type = getIntent().getStringExtra("type");
        if (isNetworkAvailable(this)) {
            getMainCategory();
        } else {
            animOne();
            networkDialog();
        }
    }

    private void networkDialog() {
        CommonDialog dialog = new CommonDialog(this, getString(R.string.internet_info), "showY", "OK", "") {
            @Override
            protected void onYes() {
                dismiss();
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

    private void getMainCategory() {
        RestApiManager.get_category(this, this);
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager
                cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }

    private void animOne() {
        TextView tvFirst = findViewById(R.id.tvFirst);
        YoYo.with(Techniques.SlideInLeft)
                .withListener(new android.animation.Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(android.animation.Animator animation) {
                        tvFirst.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(android.animation.Animator animation) {
                        animTwo();
                    }

                    @Override
                    public void onAnimationCancel(android.animation.Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(android.animation.Animator animation) {

                    }
                })
                .duration(700)
                .playOn(tvFirst);
    }

    public void animTwo() {
        TextView tvTwo = findViewById(R.id.tvTwo);
        YoYo.with(Techniques.SlideInLeft)
                .withListener(new android.animation.Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(android.animation.Animator animation) {
                        tvTwo.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(android.animation.Animator animation) {
                        animThree();
                    }

                    @Override
                    public void onAnimationCancel(android.animation.Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(android.animation.Animator animation) {

                    }
                })
                .duration(700)
                .playOn(tvTwo);
    }

    public void animThree() {
        LinearLayout layoutLogo = findViewById(R.id.layoutLogo);
        YoYo.with(Techniques.BounceInLeft)
                .withListener(new android.animation.Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(android.animation.Animator animation) {
                        layoutLogo.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(android.animation.Animator animation) {
                        if (isNetworkAvailable(SplashActivity.this)) otherScreenSend();
                    }

                    @Override
                    public void onAnimationCancel(android.animation.Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(android.animation.Animator animation) {

                    }
                })
                .duration(700)
                .playOn(layoutLogo);
    }


    private void otherScreenSend() {
        if (SharedPref.getPrefsHelper().getPref("user") != null && !SharedPref.getPrefsHelper().getPref("user").toString().trim().equalsIgnoreCase("")) {
            startActivity(new Intent(SplashActivity.this, DashBoardActivity.class));
            finish();
        } else {
            if (CompanyDetails.getInstance().getDetails() != null) {
                if (CompanyDetails.getInstance().getDetails().getLogin_type().equalsIgnoreCase("1")) {
                    startActivity(new Intent(SplashActivity.this, DashBoardActivity.class).putExtra("isFromNotification", isFromNotification).putExtra("type", type));
                    finish();
                } else if (CompanyDetails.getInstance().getDetails().getLogin_type().equalsIgnoreCase("2")) {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class).putExtra("isComesFromSplash", true));
                    finish();
                } else if (CompanyDetails.getInstance().getDetails().getLogin_type().equalsIgnoreCase("3")) {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class).putExtra("isComesFromSplash", true));
                    finish();
                } else if (CompanyDetails.getInstance().getDetails().getLogin_type().equalsIgnoreCase("4")) {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class).putExtra("isComesFromSplash", true));
                    finish();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionChecker.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void successMainCategory(ArrayList<MainCategoryBean> mainList, ArrayList<Banners> bannersArrayList, CompanyDetails companyDetails) {
        CompanyDetails.getInstance().setDetails(companyDetails);
        animOne();
        //otherScreenSend();
    }

    @Override
    public void failure(String error) {

    }
}
