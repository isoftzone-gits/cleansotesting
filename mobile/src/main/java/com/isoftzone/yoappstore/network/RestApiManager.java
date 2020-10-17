package com.isoftzone.yoappstore.network;

import android.content.Context;
import android.util.Log;

import com.isoftzone.yoappstore.bean.CouponDetailBean;
import com.isoftzone.yoappstore.bean.WalletBeanDetail;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestApiManager {

    public static void login(RequestBody parameters, final Context context,
                             final CommonInterfaces.LoginDelegate delegate) {

        Call<ResponseBody> responseBodyCall = ApiClient.getInstance().getApiService().login(parameters);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.e("respoLogin", "==res" + response.raw());
                    String res = response.body().string();
                    Log.e("respoLogin", "res==" + res);
                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        if (jsonObject.getString("rccode").equalsIgnoreCase("1")) {
                            delegate.loginSuccess(jsonObject);
                        } else {
                            delegate.loginFailure(jsonObject.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.loginFailure("Login failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {
                    delegate.loginFailure(call.execute().message().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.loginFailure("Login failed. Try again");
                }
            }
        });
    }

    public static void registration(RequestBody parameters,
                                    final Context context,
                                    final CommonInterfaces.RegistrationDelegate delegate) {
        Call<ResponseBody> responseBodyCall = ApiClient.getInstance().getApiService().registration(parameters);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    // Log.e("respoRegis", "==res" + response.body().string());

                    String res = response.body().string();
                    Log.e("respoRegis", "==res" + res);
// userid  1 == verfiy
                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        if (jsonObject.getString("rccode").equalsIgnoreCase("1")) {
                            //   JSonParser.userDetailParse(res);
                            delegate.regisSuccess(jsonObject.getString("message"), jsonObject.getString("user_id"));
                        } else {
                            delegate.regisFailure(jsonObject.getString("message"));
                            //  PopUpWindow.dError(context, "Failed " + jsonObject.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.regisFailure("Registration failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {
                    delegate.regisFailure(call.execute().message().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.regisFailure("Login failed. Try again");
                }
            }
        });
    }

    public static void updateProfile(RequestBody parameters,
                                     final Context context,
                                     final CommonInterfaces.updateProfile delegate) {
        Call<ResponseBody> responseBodyCall = ApiClient.getInstance().getApiService().updateProfile(parameters);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.e("updateProfile", "==res" + response.toString());

                    String res = response.body().string();
                    Log.e("updateProfile", "==res" + res);

                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        if (jsonObject.getString("rccode").equalsIgnoreCase("200")) {
                            //   JSonParser.userDetailParse(res);
                            delegate.updateProfileSuccess(jsonObject.getString("message"));
                        } else {
                            delegate.regisFailure(jsonObject.getString("message"));
                            //  PopUpWindow.dError(context, "Failed " + jsonObject.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.regisFailure("Login failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {
                    delegate.regisFailure(call.execute().message().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.regisFailure("Login failed. Try again");
                }
            }
        });
    }


    public static void forgetPassword(RequestBody parameters,
                                      final Context context,
                                      final CommonInterfaces.ForgetPasswordDelegate delegate) {


        Call<ResponseBody> responseBodyCall = ApiClient.getInstance().getApiService().forgetpassword(parameters);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.e("respoForgetPass", "==res" + response.toString());
                    String res = response.body().string();
                    Log.e("respoForgetPass", "res==" + res);
                    //  JSonParser.userDetailParse(res);

                    JSONObject jsonObject = new JSONObject(res);
                    if (jsonObject.getString("rccode").equalsIgnoreCase("1")) {
                        //  Toast.makeText(context, "Success Password Changed", Toast.LENGTH_SHORT).show();
                        delegate.forgetSuccess(jsonObject.getString("message"));

                    } else {
                        delegate.forgetFailure(jsonObject.getString("message"));
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.forgetFailure("failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {
                    delegate.forgetFailure(call.execute().message().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.forgetFailure("failed. Try again");
                }
            }
        });
    }


    public static void changePassword(RequestBody parameters,
                                      final Context context,
                                      final CommonInterfaces.changePasswordDelegate delegate) {


        Call<ResponseBody> responseBodyCall = ApiClient.getInstance().getApiService().changePassword(parameters);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.e("respoForgetPass", "==res" + response.toString());
                    String res = response.body().string();
                    Log.e("respoForgetPass", "res==" + res);

                    JSONObject object = new JSONObject(res);

                    delegate.changePasswordSuccess(object.getString("message"));
                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.forgetFailure("failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {
                    delegate.forgetFailure(call.execute().message().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.forgetFailure("failed. Try again");
                }
            }
        });
    }


    public static void verifyOtp(RequestBody parameters,
                                 final Context context,
                                 final CommonInterfaces.changePasswordDelegate delegate) {


        Call<ResponseBody> responseBodyCall = ApiClient.getInstance().getApiService().verifyOtp(parameters);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.e("verifyOtp", "==res" + response.toString());
                    String res = response.body().string();
                    Log.e("verifyOtp", "res==" + res);
                    //  JSonParser.userDetailParse(res);

                    JSONObject object = new JSONObject(res);

                    if (object.getInt("rccode") == 1) {
                        delegate.verifyOtpResp(object.getString("message"));
                    } else {
                        delegate.forgetFailure(object.getString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.forgetFailure("failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {
                    delegate.forgetFailure(call.execute().message().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.forgetFailure("failed. Try again");
                }
            }
        });
    }


    public static void generateOtpByMobileNo(RequestBody parameters,
                                             final Context context,
                                             final CommonInterfaces.generateOtpByMobileNo delegate) {


        Call<ResponseBody> responseBodyCall = ApiClient.getInstance().getApiService().generateOtpByMobileNo(parameters);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.e("generateOtpByMobileNo", "==res" + response.toString());
                    String res = response.body().string();
                    Log.e("generateOtpByMobileNo", "res==" + res);

                    JSONObject object = new JSONObject(res);
                    if (object.getInt("rccode") == 1) {
                        delegate.successGenerateOtp(object.getString("user_id"));
                    } else {
                        delegate.failure(object.getString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.failure("failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {
                    delegate.failure(call.execute().message().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.failure("failed. Try again");
                }
            }
        });
    }


    public static void get_category(final Context context,
                                    final CommonInterfaces.get_category delegate) {
        Call<ResponseBody> responseBodyCall = ApiClient.getInstance().getApiService().get_category();
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.e("get_category", "==res" + response.raw());

                    String res = response.body().string();
                    Log.e("get_category", "==res" + res);

                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        delegate.successMainCategory(JSonParser.stringJSonToGetAllCategories(jsonObject.getJSONArray("category").toString()),
                                JSonParser.stringJSonToGetAllBanners(jsonObject.getJSONArray("banners").toString()),
                                JSonParser.stringJSonToCompanyDetail(jsonObject.getJSONObject("details").toString()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.failure(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {
                    delegate.failure(call.execute().message().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.failure(t.getMessage());
                }
            }
        });
    }


    public static void getPaymentDetails(RequestBody body, final Context context,
                                         final CommonInterfaces.getPaymentDetails delegate) {

        Call<ResponseBody> responseBodyCall = ApiClient.getInstance().getApiService().getPaymentDetails(body);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.e("getPaymentDetails", "==res" + response.raw());

                    String res = response.body().string();
                    Log.e("getPaymentDetails", "==res" + res);

                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        WalletBeanDetail detail = JSonParser.stringJSonToWalletDetail(jsonObject.getJSONObject("wallet_amount").toString());
                        delegate.successPaymentDetails(detail);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.failure(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {
                    delegate.failure(call.execute().message().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.failure(t.getMessage());
                }
            }
        });
    }


    public static void verifyCouponCode(RequestBody body, final Context context,
                                        final CommonInterfaces.getCouponDetails delegate) {

        Call<ResponseBody> responseBodyCall = ApiClient.getInstance().getApiService().verifyCouponCode(body);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.e("verifyCouponCode", "==res" + response.raw());

                    String res = response.body().string();
                    Log.e("verifyCouponCode", "==res" + res);
                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        if (jsonObject.getInt("rccode") == 1) {
                            CouponDetailBean detail = JSonParser.stringJSonToCouponDetail(jsonObject.getJSONObject("counpon_detail").toString());
                            delegate.successCouponDetails(detail);
                        } else {
                            delegate.failureInValidCoupon(jsonObject.getString("message"));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.failureInValidCoupon(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {
                    delegate.failureInValidCoupon(call.execute().message().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.failureInValidCoupon(t.getMessage());
                }
            }
        });
    }

    public static void get_sub_category(RequestBody requestBody,
                                        final CommonInterfaces.get_sub_category delegate) {

        Call<ResponseBody> responseBodyCall = ApiClient.getInstance().getApiService().get_sub_category(requestBody);

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.e("get_category", "==res" + response.toString());

                    String res = response.body().string();
                    Log.e("get_category", "==res" + res);

                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        delegate.successSubCategory(JSonParser.stringJSonToGetAllSubCategories(jsonObject.getJSONArray("sub_category").toString()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        delegate.failure(e.getMessage());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.failure(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {
                    delegate.failure(call.execute().message().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.failure(t.getMessage());
                }
            }
        });
    }


    public static void getNotifications(RequestBody requestBody,
                                        final CommonInterfaces.getNotifications delegate) {

        Call<ResponseBody> responseBodyCall = ApiClient.getInstance().getApiService().getNotifications();

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.e("getNotifications", "==res" + response.toString());

                    String res = response.body().string();
                    Log.e("getNotifications", "==res" + res);

                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        delegate.successAllNotifications(JSonParser.stringJSonToGetAllNotifications(jsonObject.getJSONArray("notification").toString()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        delegate.failure(e.getMessage());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.failure(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {
                    delegate.failure(call.execute().message().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.failure(t.getMessage());
                }
            }
        });
    }


    public static void getAppUpdateInfo(final Context context, final CommonInterfaces.getAppUpdateInfo delegate) {

        Call<ResponseBody> responseBodyCall = ApiClient.getInstance().getApiService().app_version();
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    String res = response.body().string();
                    JSONObject jsonObject = new JSONObject(res);
                    if (jsonObject.getString("rccode").equalsIgnoreCase("1")) {
                        String versioncode = jsonObject.getJSONObject("version").getString("versioncode");
                        String versionname = jsonObject.getJSONObject("version").getString("versionname");
                        delegate.successAppUpdateInfo("", versionname, versioncode);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.failure("Update app failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {
                    delegate.failure(call.execute().message().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.failure("Update app failed failed. Try again");
                }
            }
        });
    }


    public static void getShippingRate(RequestBody requestBody,
                                       final CommonInterfaces.getShippingRate delegate) {

        Call<ResponseBody> responseBodyCall = ApiClient.getInstance().getApiService().getShippingRate(requestBody);

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.e("getShippingRate", "==message" + response.message());
                    Log.e("getShippingRate", "==errorBody" + response.errorBody());
                    Log.e("getShippingRate", "==code" + response.code());
                    String res = response.body().string();
                    Log.e("getShippingRate", "==res" + res);

                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        delegate.successGetShippingRate(jsonObject.getInt("shipping_rate"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        delegate.failure(e.getMessage());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.failure(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {
                    delegate.failure(call.execute().message().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.failure(t.getMessage());
                }
            }
        });
    }


    public static void callTokenService(RequestBody requestBody,
                                        final CommonInterfaces.callTokenServiceDelegate delegate) {

        Call<ResponseBody> responseBodyCall = ApiClient.getInstance().getApiService().callTokenService(requestBody);

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.e("callTokenService", "==message" + response.message());
                    Log.e("callTokenService", "==errorBody" + response.errorBody());
                    Log.e("callTokenService", "==code" + response.code());
                    String res = response.body().string();
                    Log.e("callTokenService", "==res" + res);

                    try {

                        JSONObject jsonObject = new JSONObject(res);
                        delegate.successGetTokenOrderId(jsonObject.getJSONObject("data").getString("token"), jsonObject.getJSONObject("data").getString("order_id"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                        delegate.failure("Failed");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.failure(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {
                    delegate.failure(call.execute().message().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.failure(t.getMessage());
                }
            }
        });
    }


    public static void getWallet(RequestBody requestBody,
                                 final CommonInterfaces.get_wallet delegate) {

        Call<ResponseBody> responseBodyCall = ApiClient.getInstance().getApiService().get_wallet_amount(requestBody);

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.e("getWallet", "==res" + response.toString());
                    Log.e("message", "==res" + response.message());
                    Log.e("raw", "==res" + response.raw());

                    String res = response.body().string();
                    Log.e("getWallet", "==res" + res);

                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        String used_amount = jsonObject.getString("used_amount");
                        String redeem_amount = jsonObject.getJSONArray("redeem_amount").toString();
                        delegate.successWalletList(used_amount, JSonParser.stringJSonToGetUsedAmount(redeem_amount), JSonParser.stringJSonToGetWallets(jsonObject.getJSONArray("wallet_amount").toString()));

                    } catch (JSONException e) {
                        e.printStackTrace();
                        delegate.failure("No Transactions");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.failure(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {
                    delegate.failure(call.execute().message().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.failure(t.getMessage());
                }
            }
        });
    }


    public static void get_product(RequestBody requestBody,
                                   final CommonInterfaces.get_product delegate) {

        Call<ResponseBody> responseBodyCall = ApiClient.getInstance().getApiService().get_product(requestBody);

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.e("get_product", "==res" + response.toString());
                    Log.e("message", "==res" + response.message());
                    Log.e("raw", "==res" + response.raw());

                    String res = response.body().string();
                    Log.e("get_product", "==res" + res);

                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        delegate.successProduct(JSonParser.stringJSonToGetAllProducts(jsonObject.getJSONArray("products").toString()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        delegate.failure("No Product Available");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.failure(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {
                    delegate.failure(call.execute().message().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.failure(t.getMessage());
                }
            }
        });
    }


    public static void getSearchProduct(RequestBody requestBody,
                                        final CommonInterfaces.get_product delegate) {

        Call<ResponseBody> responseBodyCall = ApiClient.getInstance().getApiService().getSearchProduct(requestBody);

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.e("getSearchProduct", "==res" + response.toString());
                    Log.e("message", "==res" + response.message());
                    Log.e("raw", "==res" + response.raw());

                    String res = response.body().string();
                    Log.e("getSearchProduct", "==res" + res);

                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        delegate.successProduct(JSonParser.stringJSonToGetAllProducts(jsonObject.getJSONArray("products").toString()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        delegate.failure("No Product Available");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.failure(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {
                    delegate.failure(call.execute().message().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.failure(t.getMessage());
                }
            }
        });
    }


    public static void placeOrdr(RequestBody requestBody,
                                 final CommonInterfaces.placeOrdr delegate) {

        Call<ResponseBody> responseBodyCall = ApiClient.getInstance().getApiService().placeOrdr(requestBody);

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.e("raw", "==res" + response.raw());
                    Log.e("get_product", "==res" + response.message());
                    Log.e("errorBody", "==res" + response.errorBody());
                    Log.e("body", "==res" + response.body());

                    String res = response.body().string();
                    Log.e("get_product", "==res" + res);
                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        delegate.successPlaceOrder(jsonObject.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        delegate.failure("Failed");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.failure(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {
                    delegate.failure(call.execute().message().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.failure(t.getMessage());
                }
            }
        });
    }


    public static void ongoing_order(RequestBody requestBody,
                                     final CommonInterfaces.ongoing_order delegate) {

        Call<ResponseBody> responseBodyCall = ApiClient.getInstance().getApiService().ongoing_order(requestBody);

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.e("ongoing_order", "==res" + response.toString());

                    String res = response.body().string();
                    Log.e("ongoing_order", "==res" + res);
                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        delegate.successGetOrders(JSonParser.stringJSonToGetAllOrders(jsonObject.getJSONArray("orders").toString()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        delegate.failure("Failed to get orders");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.failure(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {
                    delegate.failure(call.execute().message().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.failure(t.getMessage());
                }
            }
        });
    }


    public static void cancel_order(RequestBody requestBody,
                                    final CommonInterfaces.cancel_order delegate) {

        Call<ResponseBody> responseBodyCall = ApiClient.getInstance().getApiService().cancel_order(requestBody);

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.e("cancel_order", "==res" + response.toString());
                    String res = response.body().string();
                    Log.e("cancel_order", "==res" + res);

                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        delegate.successCancelOrders(jsonObject.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        delegate.failure("Failed to get orders");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.failure(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {
                    delegate.failure(call.execute().message().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.failure(t.getMessage());
                }
            }
        });
    }


    public static void add_address(RequestBody requestBody,
                                   final CommonInterfaces.add_address delegate) {

        Call<ResponseBody> responseBodyCall = ApiClient.getInstance().getApiService().add_address(requestBody);

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.e("add_address", "==res" + response.toString());

                    String res = response.body().string();
                    Log.e("add_address", "==res" + res);

                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        delegate.successAddAddress(jsonObject.getString("message"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                        delegate.failure("Failed to get orders");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.failure(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {
                    delegate.failure(call.execute().message().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.failure(t.getMessage());
                }
            }
        });
    }


    public static void checkVerifyAddress(RequestBody requestBody,
                                          final CommonInterfaces.checkVerifyAddress delegate) {

        Call<ResponseBody> responseBodyCall = ApiClient.getInstance().getApiService().checkVerifyAddress(requestBody);

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.e("checkVerifyAddress", "==res" + response.toString());

                    String res = response.body().string();
                    Log.e("checkVerifyAddress", "==res" + res);

                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        delegate.successisAddressVerify(jsonObject.getString("statis"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        delegate.failure("Failed to get verify");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.failure(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {
                    delegate.failure(call.execute().message().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.failure(t.getMessage());
                }
            }
        });
    }

    public static void getLocationNameFromGoolge(String url,
                                                 final CommonInterfaces.getLocationNameFromGoolge delegate) {

        Call<ResponseBody> responseBodyCall = ApiClient.getInstance().getApiService().getLocationNameFromGoolge(url);

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.e("locaNameGoogle", "==res" + response.toString());

                    String res = response.body().string();
                    Log.e("locaNameGoogle", "==res" + res);

                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        String cityStatCountry = jsonObject.getJSONObject("plus_code").getString("compound_code");
                        delegate.successGoogleAddress(cityStatCountry, JSonParser.stringJSonToGetAllSuggestedAddbyGoogle(jsonObject.getJSONArray("results").toString()));

                    } catch (JSONException e) {
                        e.printStackTrace();
                        delegate.failure("Failed to get orders");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.failure(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {
                    delegate.failure(call.execute().message().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.failure(t.getMessage());
                }
            }
        });
    }


    public static void getAllAddress(RequestBody requestBody,
                                     final CommonInterfaces.getAllAddress delegate) {

        Call<ResponseBody> responseBodyCall = ApiClient.getInstance().getApiService().get_address_by_userID(requestBody);

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.e("getAllAddress", "==res" + response.toString());

                    String res = response.body().string();
                    Log.e("getAllAddress", "==res" + res);

                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        delegate.successAllAddress(JSonParser.stringJSonToGetAllAddress(jsonObject.getJSONArray("address").toString()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        delegate.failure("Failed to get orders");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.failure(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {
                    delegate.failure(call.execute().message().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.failure(t.getMessage());
                }
            }
        });
    }


    public static void delete_address(RequestBody requestBody,
                                      final CommonInterfaces.deleteAddress delegate) {

        Call<ResponseBody> responseBodyCall = ApiClient.getInstance().getApiService().delete_address(requestBody);

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.e("getAllAddress", "==res" + response.toString());

                    String res = response.body().string();
                    Log.e("getAllAddress", "==res" + res);
                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        delegate.successDeleteAddress(jsonObject.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        delegate.failure("Failed to get orders");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.failure(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {
                    delegate.failure(call.execute().message().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.failure(t.getMessage());
                }
            }
        });
    }


    public static void getProfile(RequestBody requestBody,
                                  final CommonInterfaces.getProfile delegate) {

        Call<ResponseBody> responseBodyCall = ApiClient.getInstance().getApiService().getProfile(requestBody);

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.e("profile", "==res" + response.toString());

                    String res = response.body().string();
                    Log.e("profile", "==res" + res);
                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        delegate.successGetProfile(JSonParser.stringJSonToUserProfileInfo(jsonObject.getJSONObject("user").toString()));

                    } catch (JSONException e) {
                        e.printStackTrace();
                        delegate.failure("Failed to get orders");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.failure(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {
                    delegate.failure(call.execute().message().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.failure(t.getMessage());
                }
            }
        });
    }

    public static void past_order(RequestBody requestBody,
                                  final CommonInterfaces.ongoing_order delegate) {

        Call<ResponseBody> responseBodyCall = ApiClient.getInstance().getApiService().past_order(requestBody);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.e("ongoing_order", "==res" + response.toString());
                    String res = response.body().string();
                    Log.e("ongoing_order", "==res" + res);
                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        delegate.successGetOrders(JSonParser.stringJSonToGetAllOrders(jsonObject.getJSONArray("orders").toString()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        delegate.failure("Failed to get orders");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.failure(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {
                    delegate.failure(call.execute().message().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    delegate.failure(t.getMessage());
                }
            }
        });
    }
}
