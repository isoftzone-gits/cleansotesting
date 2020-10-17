package com.isoftzone.yoappstore.network;


import com.isoftzone.yoappstore.bean.AddressListBean;
import com.isoftzone.yoappstore.bean.Banners;
import com.isoftzone.yoappstore.bean.CompanyDetails;
import com.isoftzone.yoappstore.bean.CouponDetailBean;
import com.isoftzone.yoappstore.bean.MainCategoryBean;
import com.isoftzone.yoappstore.bean.NotificationBean;
import com.isoftzone.yoappstore.bean.OrdersBean;
import com.isoftzone.yoappstore.bean.ProductBean;
import com.isoftzone.yoappstore.bean.RedeemAmount;
import com.isoftzone.yoappstore.bean.SubCategoryBean;
import com.isoftzone.yoappstore.bean.UserProfileBean;
import com.isoftzone.yoappstore.bean.WalletBean;
import com.isoftzone.yoappstore.bean.WalletBeanDetail;
import com.isoftzone.yoappstore.bean.googleresp.GoogleResponseBean;

import org.json.JSONObject;

import java.util.ArrayList;


public class CommonInterfaces {

    public interface LoginDelegate {
        void loginSuccess(JSONObject jsonObject);

        void loginFailure(String error);
    }

    public interface RegistrationDelegate {

        void regisSuccess(String msg, String user_id);

        void regisFailure(String failedResp);
    }

    public interface getAppUpdateInfo {

        void successAppUpdateInfo(String resp, String versionName, String versionCode);

        void failure(String failedResp);
    }


    public interface updateProfile {

        void updateProfileSuccess(String msg);

        void regisFailure(String failedResp);
    }

    public interface ForgetPasswordDelegate {

        void forgetSuccess(String resp);

        void forgetFailure(String failedResp);
    }

    public interface changePasswordDelegate {

        void changePasswordSuccess(String resp);

        void verifyOtpResp(String resp);

        void forgetFailure(String failedResp);
    }

    public interface get_category {
        void successMainCategory(ArrayList<MainCategoryBean> mainList, ArrayList<Banners> bannersArrayList, CompanyDetails companyDetails);

        void failure(String error);
    }


    public interface generateOtpByMobileNo {
        void successGenerateOtp(String user_id);

        void failure(String error);
    }


    public interface get_sub_category {
        void successSubCategory(ArrayList<SubCategoryBean> subList);

        void failure(String error);
    }


    public interface getNotifications {
        void successAllNotifications(ArrayList<NotificationBean> notificationBeanArrayList);

        void failure(String error);
    }


    public interface getPaymentDetails {
        void successPaymentDetails(WalletBeanDetail walletBeanDetail);

        void failure(String error);
    }


    public interface getCouponDetails {
        void successCouponDetails(CouponDetailBean couponDetail);

        void failureInValidCoupon(String error);
    }

    public interface getShippingRate {
        void successGetShippingRate(int shipping_rate);

        void failure(String error);
    }


    public interface callTokenServiceDelegate {
        void successGetTokenOrderId(String token, String orderId);

        void failure(String error);
    }


    public interface get_product {
        void successProduct(ArrayList<ProductBean> productBeanArrayList);

        void failure(String error);
    }

    public interface get_wallet {
        void successWalletList(String used_amount, ArrayList<RedeemAmount> redeemAmounts, ArrayList<WalletBean> walletList);

        void failure(String error);
    }


    public interface placeOrdr {
        void successPlaceOrder(String msg);

        void failure(String error);
    }


    public interface ongoing_order {
        void successGetOrders(ArrayList<OrdersBean> ordersBeanArrayList);

        void failure(String error);
    }


    public interface cancel_order {
        void successCancelOrders(String msg);

        void failure(String error);
    }

    public interface add_address {
        void successAddAddress(String msg);

        void failure(String error);
    }


    public interface checkVerifyAddress {
        void successisAddressVerify(String msg);

        void failure(String error);
    }


    public interface getLocationNameFromGoolge {
        void successGoogleAddress(String cityStatCountry, ArrayList<GoogleResponseBean> beanArrayListGoogle);

        void failure(String error);
    }


    public interface getAllAddress {
        void successAllAddress(ArrayList<AddressListBean> beanArrayList);

        void failure(String error);
    }

    public interface deleteAddress {
        void successDeleteAddress(String address);

        void failure(String error);
    }


    public interface getProfile {
        void successGetProfile(UserProfileBean userProfileBean);

        void failure(String error);
    }

    public interface getclinic {
        //  void successClinicCategory(ArrayList<AllClinicBean> clinicList);

        void failure(String error);
    }


    public interface RecyclerClickDelegate {
        void recyclerItemDelegate(String data);

    }


 /*   public interface ArticleListDelegate {
        void successCategoryWiseList(ArrayList<ArticlesCategoryWiseBean> categoryWiseBeanArrayList);

        void failure(String error);
    }*/


}
