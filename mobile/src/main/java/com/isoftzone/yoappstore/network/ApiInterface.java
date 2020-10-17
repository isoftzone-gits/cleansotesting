package com.isoftzone.yoappstore.network;


import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Url;


public interface ApiInterface {


    @GET("getArunDataEmployee")
    Call<ResponseBody> getArunData();




    @POST("register")
    Call<ResponseBody> registration(@Body RequestBody parameters);


    @POST("updateProfile")
    Call<ResponseBody> updateProfile(@Body RequestBody parameters);


    @POST("verify_login")
    Call<ResponseBody> login(@Body RequestBody parameters);

    @POST("update_password")
    Call<ResponseBody> forgetpassword(@Body RequestBody parameters);


    @POST("changePassword")
    Call<ResponseBody> changePassword(@Body RequestBody parameters);


    @POST("verify_otp")
    Call<ResponseBody> verifyOtp(@Body RequestBody parameters);


    @POST("get_wallet_amount")
    Call<ResponseBody> get_wallet_amount(@Body RequestBody parameters);


    @POST("forgot_password")
    Call<ResponseBody> generateOtpByMobileNo(@Body RequestBody parameters);


    @GET("get_category")
    Call<ResponseBody> get_category();


    @POST("Get_wallet_details")
    Call<ResponseBody> getPaymentDetails(@Body RequestBody body);


    @POST("verify_coupon")
    Call<ResponseBody> verifyCouponCode(@Body RequestBody body);


    @POST("get_sub_category")
    Call<ResponseBody> get_sub_category(@Body RequestBody requestBody);


    @GET("get_notification")
    Call<ResponseBody> getNotifications();


    @GET("app_version")
    Call<ResponseBody> app_version();

    @POST("get_shipping_rate")
    Call<ResponseBody> getShippingRate(@Body RequestBody requestBody);


    @POST("get_payment_token")
    Call<ResponseBody> callTokenService(@Body RequestBody requestBody);


    @POST("get_product")
    Call<ResponseBody> get_product(@Body RequestBody requestBody);


    @POST("search")
    Call<ResponseBody> getSearchProduct(@Body RequestBody requestBody);


    @POST("place_order")
    Call<ResponseBody> placeOrdr(@Body RequestBody requestBody);


    @POST("ongoing_order")
    Call<ResponseBody> ongoing_order(@Body RequestBody requestBody);


    @POST("cancel_order")
    Call<ResponseBody> cancel_order(@Body RequestBody requestBody);


    @POST("add_address")
    Call<ResponseBody> add_address(@Body RequestBody requestBody);


    @POST("verify_address")
    Call<ResponseBody> checkVerifyAddress(@Body RequestBody requestBody);


    @GET
    Call<ResponseBody> getLocationNameFromGoolge(@Url String url);


    @POST("get_address_by_userID")
    Call<ResponseBody> get_address_by_userID(@Body RequestBody requestBody);


    @POST("delete_address")
    Call<ResponseBody> delete_address(@Body RequestBody requestBody);


    @POST("getProfile")
    Call<ResponseBody> getProfile(@Body RequestBody requestBody);


    @POST("past_order")
    Call<ResponseBody> past_order(@Body RequestBody requestBody);

    //  @GET("me")
    @GET
    Call<ResponseBody> getOwnDetailsServerUrl(@Url String url, @Header("Authorization") String accessToken);


    @FormUrlEncoded
    @POST("oauth2/token")
    Call<ResponseBody> refreshToken(@Field("client_Id") String client_Id, @Field("grant_type") String grantType, @Field("refresh_token") String refresh_token);

    @FormUrlEncoded
    @POST
    Call<ResponseBody> refreshTokenServerUrl(@Url String url, @Field("client_Id") String client_Id, @Field("grant_type") String grantType, @Field("refresh_token") String refresh_token);


    @POST("signup/shop_info")
    Call<ResponseBody> signUpInvoices(@Body RequestBody parameters, @Header("Authorization") String accessToken);


    @FormUrlEncoded
    @POST("clinic/getclinic")
    Call<ResponseBody> getclinic(@Field("date") String date, @Field("doctor_id") String doctor_id);


    @FormUrlEncoded
    @POST("doctor/myappoiment")
    Call<ResponseBody> getMyAppointment(@Field("mobile") String mobile);


    @FormUrlEncoded
    @POST("doctor/cancelappoiment")
    Call<ResponseBody> cancelappoiment(@Field("mobile") String mobile, @Field("clinic_id") String clinic_id);


    @FormUrlEncoded
    @POST("doctor/getdoctorbyid")
    Call<ResponseBody> getdoctorbyid(@Field("doctor_id") String doctor_id);


    @FormUrlEncoded
    @POST("doctor/bookappoiment")
    Call<ResponseBody> bookappoiment(@Field("clinic_id") String clinic_id, @Field("doctor_id") String doctor_id, @Field("appoiment_time") String appoiment_time, @Field("name") String name,
                                     @Field("age") String age, @Field("gender") String gender, @Field("mobile") String mobile, @Field("treatment_type") String treatment_type);


    @FormUrlEncoded
    @POST("clinic/gettimeslot")
    Call<ResponseBody> getTimeSlot(@Field("clinic_id") String clinic_id, @Field("is_morning") String is_morning);


    @POST("getSubCategoryById")
    Call<ResponseBody> getSubCategory(@Body RequestBody parameters);

}
