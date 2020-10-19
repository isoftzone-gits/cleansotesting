package com.isoftzone.vendor.network;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.isoftzone.vendor.bean.AddressListBean;
import com.isoftzone.vendor.bean.Banners;
import com.isoftzone.vendor.bean.CompanyDetails;
import com.isoftzone.vendor.bean.CouponDetailBean;
import com.isoftzone.vendor.bean.MainCategoryBean;
import com.isoftzone.vendor.bean.NotificationBean;
import com.isoftzone.vendor.bean.OrdersBean;
import com.isoftzone.vendor.bean.ProductBean;
import com.isoftzone.vendor.bean.RedeemAmount;
import com.isoftzone.vendor.bean.SubCategoryBean;
import com.isoftzone.vendor.bean.UserBean;
import com.isoftzone.vendor.bean.UserProfileBean;
import com.isoftzone.vendor.bean.WalletBean;
import com.isoftzone.vendor.bean.WalletBeanDetail;
import com.isoftzone.vendor.bean.googleresp.GoogleResponseBean;
import com.isoftzone.vendor.util.SharedPref;


import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Admin on 13-06-2017.
 */

public class JSonParser {

    public static ArrayList<MainCategoryBean> stringJSonToGetAllCategories(String jsonString) {

        ArrayList<MainCategoryBean> beanArrayList = new ArrayList<>();

        Type listType = new TypeToken<ArrayList<MainCategoryBean>>() {
        }.getType();
        beanArrayList = new GsonBuilder().create().fromJson(jsonString, listType);

        return beanArrayList;
    }

    public static ArrayList<Banners> stringJSonToGetAllBanners(String jsonString) {

        ArrayList<Banners> beanArrayList = new ArrayList<>();

        Type listType = new TypeToken<ArrayList<Banners>>() {
        }.getType();
        beanArrayList = new GsonBuilder().create().fromJson(jsonString, listType);

        return beanArrayList;
    }


    public static ArrayList<SubCategoryBean> stringJSonToGetAllSubCategories(String jsonString) {

        ArrayList<SubCategoryBean> beanArrayList = new ArrayList<>();

        Type listType = new TypeToken<ArrayList<SubCategoryBean>>() {
        }.getType();
        beanArrayList = new GsonBuilder().create().fromJson(jsonString, listType);

        return beanArrayList;
    }


    public static ArrayList<NotificationBean> stringJSonToGetAllNotifications(String jsonString) {

        ArrayList<NotificationBean> beanArrayList = new ArrayList<>();

        Type listType = new TypeToken<ArrayList<NotificationBean>>() {
        }.getType();
        beanArrayList = new GsonBuilder().create().fromJson(jsonString, listType);

        return beanArrayList;
    }


    public static ArrayList<ProductBean> stringJSonToGetAllProducts(String jsonString) {

        ArrayList<ProductBean> beanArrayList = new ArrayList<>();

        Type listType = new TypeToken<ArrayList<ProductBean>>() {
        }.getType();
        beanArrayList = new GsonBuilder().create().fromJson(jsonString, listType);

        return beanArrayList;
    }


    public static ArrayList<WalletBean> stringJSonToGetWallets(String jsonString) {

        ArrayList<WalletBean> beanArrayList = new ArrayList<>();

        Type listType = new TypeToken<ArrayList<WalletBean>>() {
        }.getType();
        beanArrayList = new GsonBuilder().create().fromJson(jsonString, listType);

        return beanArrayList;
    }

    public static ArrayList<RedeemAmount> stringJSonToGetUsedAmount(String jsonString) {

        ArrayList<RedeemAmount> beanArrayList = new ArrayList<>();

        Type listType = new TypeToken<ArrayList<RedeemAmount>>() {
        }.getType();
        beanArrayList = new GsonBuilder().create().fromJson(jsonString, listType);

        return beanArrayList;
    }

    public static ArrayList<OrdersBean> stringJSonToGetAllOrders(String jsonString) {

        ArrayList<OrdersBean> beanArrayList = new ArrayList<>();

        Type listType = new TypeToken<ArrayList<OrdersBean>>() {
        }.getType();
        beanArrayList = new GsonBuilder().create().fromJson(jsonString, listType);

        return beanArrayList;
    }


    public static ArrayList<AddressListBean> stringJSonToGetAllAddress(String jsonString) {

        ArrayList<AddressListBean> beanArrayList = new ArrayList<>();

        Type listType = new TypeToken<ArrayList<AddressListBean>>() {
        }.getType();
        beanArrayList = new GsonBuilder().create().fromJson(jsonString, listType);

        return beanArrayList;
    }


    public static ArrayList<GoogleResponseBean> stringJSonToGetAllSuggestedAddbyGoogle(String jsonString) {

        ArrayList<GoogleResponseBean> beanArrayList = new ArrayList<>();

        Type listType = new TypeToken<ArrayList<GoogleResponseBean>>() {
        }.getType();
        beanArrayList = new GsonBuilder().create().fromJson(jsonString, listType);

        return beanArrayList;
    }

    public static UserBean stringJSonToUserLoggedInfo() {

        String userDetail = SharedPref.getPrefsHelper().getPref("user").toString().trim();

        UserBean userBean;

        Type listType = new TypeToken<UserBean>() {
        }.getType();
        userBean = new GsonBuilder().create().fromJson(userDetail, listType);

        return userBean;
    }


    public static CompanyDetails stringJSonToCompanyDetail(String data) {

        Type listType = new TypeToken<CompanyDetails>() {
        }.getType();
        CompanyDetails userBean = new GsonBuilder().create().fromJson(data, listType);

        return userBean;
    }

    public static CouponDetailBean stringJSonToCouponDetail(String data) {

        Type listType = new TypeToken<CouponDetailBean>() {
        }.getType();
        CouponDetailBean userBean = new GsonBuilder().create().fromJson(data, listType);

        return userBean;
    }

    public static WalletBeanDetail stringJSonToWalletDetail(String data) {

        Type listType = new TypeToken<WalletBeanDetail>() {
        }.getType();
        WalletBeanDetail detail = new GsonBuilder().create().fromJson(data, listType);

        return detail;
    }


    public static UserProfileBean stringJSonToUserProfileInfo(String data) {


        UserProfileBean userBean;

        Type listType = new TypeToken<UserProfileBean>() {
        }.getType();
        userBean = new GsonBuilder().create().fromJson(data, listType);

        return userBean;
    }

/*
    public static DrInfoBean stringJSonToGetDrInfo(String jsonString) {

        DrInfoBean drInfoBean;

        Type listType = new TypeToken<DrInfoBean>() {
        }.getType();
        drInfoBean = new GsonBuilder().create().fromJson(jsonString, listType);

        return drInfoBean;
    }*/

/*


    public static ArrayList<SubCatBean> stringJSonToGetSubCategories(String jsonString) {

        ArrayList<SubCatBean> beanArrayList = new ArrayList<>();

        Type listType = new TypeToken<ArrayList<SubCatBean>>() {
        }.getType();
        beanArrayList = new GsonBuilder().create().fromJson(jsonString, listType);

        return beanArrayList;
    }*/


}
