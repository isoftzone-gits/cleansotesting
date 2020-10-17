package com.isoftzone.yoappstore.activity;

import androidx.databinding.DataBindingUtil;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.gocashfree.cashfreesdk.CFPaymentService;
import com.isoftzone.yoappstore.R;
import com.isoftzone.yoappstore.baseactivity.BaseActivity;
import com.isoftzone.yoappstore.bean.AddressListBean;
import com.isoftzone.yoappstore.bean.CompanyDetails;
import com.isoftzone.yoappstore.bean.CouponDetailBean;
import com.isoftzone.yoappstore.bean.ProductBean;
import com.isoftzone.yoappstore.bean.SelectedProduct;
import com.isoftzone.yoappstore.bean.WalletBeanDetail;
import com.isoftzone.yoappstore.databinding.ActivityPaymentOptionBinding;
import com.isoftzone.yoappstore.network.CommonInterfaces;
import com.isoftzone.yoappstore.network.JSonParser;
import com.isoftzone.yoappstore.network.MakeParamsHandler;
import com.isoftzone.yoappstore.network.RestApiManager;
import com.isoftzone.yoappstore.util.AlertUtils;
import com.isoftzone.yoappstore.util.SharedPref;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_APP_ID;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CUSTOMER_EMAIL;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CUSTOMER_NAME;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CUSTOMER_PHONE;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_ORDER_AMOUNT;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_ORDER_ID;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_ORDER_NOTE;

public class PaymentOptionActivity extends BaseActivity implements
        CommonInterfaces.getShippingRate,
        CommonInterfaces.callTokenServiceDelegate,
        CommonInterfaces.placeOrdr,
        CommonInterfaces.getPaymentDetails,
        CommonInterfaces.getCouponDetails {

    ActivityPaymentOptionBinding binding;
    float totalAmt = 0;
    float amtNetTotal = 0;
    float amountCashfreeFinal = 0;
    private static final String TAG = "PaymentOptionActivity";
    String couponid = "", couponamount = "", couponcode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment_option);

        setCustomToolBar("", false, true);
        cartRelativeLayout.setVisibility(View.GONE);
        binding.checkoutCardView.setCardBackgroundColor(themeColor());
        //  totalAmt = getIntent().getFloatExtra("totalAmt", 0);
        //    CompanyDetails.getInstance().setCouponDetail(null);
        calculateAmount();
        binding.netAmountTextView.setText("PAY  ₹" + new DecimalFormat(".##").format(amtNetTotal));


        binding.selfRadioButton.setText(CompanyDetails.getInstance().getDetails().getSelf_pickup_name());
        binding.deliveryRadioButton.setText(CompanyDetails.getInstance().getDetails().getHome_delivery_name());

        binding.codRadioButton.setText(CompanyDetails.getInstance().getDetails().getCash_on_delivery_name());
        binding.payNowRadioButton.setText(CompanyDetails.getInstance().getDetails().getPayment_setting_name());
        //  CompanyDetails.getInstance().setCouponDetail(couponDetail);


        if (SelectedProduct.getInstance().getDeliveryType().equalsIgnoreCase("self")) {
            binding.selfRadioButton.setChecked(true);
        }

        if (SelectedProduct.getInstance().getDeliveryType().equalsIgnoreCase("home")) {
            binding.deliveryRadioButton.setChecked(true);
        }

        binding.selfRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.paymentSectionLinearLayout.setVisibility(View.VISIBLE);
                SelectedProduct.getInstance().setDeliveryType("self");
                SelectedProduct.getInstance().setShippingRate(0);
                calculateAmount();
            }
        });


        if (SelectedProduct.getInstance().getPayVia().equalsIgnoreCase("cod")) {
            binding.codRadioButton.setChecked(true);
        }

        binding.deliveryRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.paymentSectionLinearLayout.setVisibility(View.GONE);
                SelectedProduct.getInstance().setDeliveryType("home");
                startActivity(new Intent(PaymentOptionActivity.this, AddressOptionsActivity.class));
            }
        });


        binding.codLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedId = binding.deliveryTypeRadioGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                //   RadioButton deliveryType = (RadioButton) findViewById(selectedId);
                binding.codRadioButton.setChecked(true);

                SelectedProduct.getInstance().setPayVia("COD");

                // startActivity(new Intent(PaymentOptionActivity.this, CartActivity.class));
                // finish();
                // Toast.makeText(PaymentOptionActivity.this, "You are All set please do place order", Toast.LENGTH_SHORT).show();
            }
        });

        binding.nextTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!SelectedProduct.getInstance().getDeliveryType().equalsIgnoreCase("")) {

                    if (SelectedProduct.getInstance().getDeliveryType().equalsIgnoreCase("self")) {

                        if (!SelectedProduct.getInstance().getPayVia().equalsIgnoreCase("")) {
                            startActivity(new Intent(PaymentOptionActivity.this, CartActivity.class));
                            finish();
                        } else {
                            Toast.makeText(PaymentOptionActivity.this, "Please Select Payment Option", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        if (SelectedProduct.getInstance().getSavedAddress() != null) {
                            if (!SelectedProduct.getInstance().getPayVia().equalsIgnoreCase("")) {
                                startActivity(new Intent(PaymentOptionActivity.this, CartActivity.class));
                                finish();
                            } else {
                                Toast.makeText(PaymentOptionActivity.this, "Please Select Payment Option", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            startActivity(new Intent(PaymentOptionActivity.this, AddressOptionsActivity.class));
                            Toast.makeText(PaymentOptionActivity.this, "Please Select Delivery Address", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    Toast.makeText(PaymentOptionActivity.this, "Please Select Delivery Type", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.payNowLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // binding.payNowRadioButton.setChecked(true);

                callTokenService();
                //  String token = "rq9JCN4MzUIJiOicGbhJCLiQ1VKJiOiAXe0Jye.9V9JSOlRTO0MjZ2IjNjVWNiojI0xWYz9lIsMDM0YzM2ITO1EjOiAHelJCLiIlTJJiOik3YuVmcyV3QyVGZy9mIsUjOiQnb19WbBJXZkJ3biwiI1ADMwIXZkJ3TiojIklkclRmcvJye.2ZUssUHLVi7ETq0ojyJxHFEOMGj6mvMy0W6_wfJzyXxzqG2BrBAli2h8-2cCvMdi-M";
                // callCashFreeSdk(token, "Order0005");
            }
        });


        binding.payNowLinearLayout.setVisibility(CompanyDetails.getInstance().getDetails().getPayment_setting().trim().equalsIgnoreCase("0") ? View.GONE : View.VISIBLE);

        binding.selfRadioButton.setVisibility(CompanyDetails.getInstance().getDetails().getSelf_pickup().trim().equalsIgnoreCase("0") ? View.GONE : View.VISIBLE);
        binding.deliveryRadioButton.setVisibility(CompanyDetails.getInstance().getDetails().getHome_delivery().trim().equalsIgnoreCase("0") ? View.GONE : View.VISIBLE);
        binding.codLinearLayout.setVisibility(CompanyDetails.getInstance().getDetails().getCash_on_delivery().trim().equalsIgnoreCase("0") ? View.GONE : View.VISIBLE);

        // // state country city place order service

        getPaymentDetails();

        binding.couponApplyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyCouponCode();
            }
        });

        binding.editCouponTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.couponApplyTextView.setVisibility(View.VISIBLE);
                binding.editCouponTextView.setVisibility(View.GONE);
                binding.couponSavingTextView.setVisibility(View.GONE);
                binding.couponSavingTextView.setText("");
                binding.couponEditText.setEnabled(true);
                binding.couponEditText.setText("");
                binding.couponEditText.setBackgroundResource(R.drawable.bg_grayline);
                CompanyDetails.getInstance().setCouponDetail(null);
                calculateAmount();
            }
        });

     /*   if (CompanyDetails.getInstance().getCouponDetail() != null && CompanyDetails.getInstance().getCouponDetail().getCouponCode() != null) {
            binding.couponEditText.setText(CompanyDetails.getInstance().getCouponDetail().getCouponCode());
            binding.couponApplyTextView.setVisibility(View.GONE);
            binding.editCouponTextView.setVisibility(View.VISIBLE);
            binding.couponEditText.setEnabled(false);
            binding.couponEditText.setBackgroundResource(R.drawable.bggreen_border);

        }*/

    }


    private void verifyCouponCode() {

        if (binding.couponEditText.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please Enter Valid Coupon code", Toast.LENGTH_SHORT).show();
            return;
        }

        if (SharedPref.getPrefsHelper().getPref("user") == null || SharedPref.getPrefsHelper().getPref("user").toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }

        try {
            JSONObject object = new JSONObject();
            object.put("user_id", userBean.getId());
            object.put("coupon_code", binding.couponEditText.getText().toString().trim());
            showDialog(this);

            RestApiManager.verifyCouponCode(MakeParamsHandler.getRequestBody(object.toString()), this, this);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void callTokenService() {

        if (SharedPref.getPrefsHelper().getPref("user") == null || SharedPref.getPrefsHelper().getPref("user").toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }

        if (SelectedProduct.getInstance().getSelectedProductList().size() <= 0) {
            SelectedProduct.getInstance().getSelectedProductList().clear();
            Toast.makeText(this, "Please Select Item , we are unable to get cart Item , if you have added please go back and try again", Toast.LENGTH_LONG).show();
            return;
        }

        if (SelectedProduct.getInstance().getDeliveryType().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please Select Delivery Type", Toast.LENGTH_LONG).show();
            return;
        }

        try {

            if (SharedPref.getPrefsHelper().getPref("user") != null) {
                JSONObject object = new JSONObject();

                userBean = JSonParser.stringJSonToUserLoggedInfo();

                object.put("user_id", userBean.getId());
                //  object.put("amount", "" + new DecimalFormat(".##").format(SelectedProduct.getInstance().getShippingRate() + amtNetTotal));

                object.put("amount", "" + String.format("%.2f", amountCashfreeFinal));

                Log.e("callTokenServiceParam=", "==" + object.toString());

                showDialog(this);
                RestApiManager.callTokenService(MakeParamsHandler.getRequestBody(object.toString()), this);
            } else {
                Toast.makeText(this, "We are not identify you please go back and try again", Toast.LENGTH_LONG).show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    float walletAmtOnly = 0;

    private void calculateAmount() {

        totalAmt = 0;

        for (int i = 0; i < SelectedProduct.getInstance().getSelectedProductList().size(); i++) {
            float innerValue = (Float.parseFloat(SelectedProduct.getInstance().getSelectedProductList().get(i).getCurrentSelectedPrice()) * SelectedProduct.getInstance().getSelectedProductList().get(i).getQtyActual());
            totalAmt = totalAmt + innerValue;
        }

        if (SelectedProduct.getInstance().getDeliveryType().trim().equalsIgnoreCase("home")) {
            this.amtNetTotal = (totalAmt);
            binding.deliveryChargesTextView.setText("Delivery Charge  ₹ " + SelectedProduct.getInstance().getShippingRate());

            float res = 0;

            if (CompanyDetails.getInstance().getWalletBeanDetail() != null) {
                float finalAmt = (amtNetTotal);  // + SelectedProduct.getInstance().getShippingRate()
                float redimPercent = Float.parseFloat(CompanyDetails.getInstance().getWalletBeanDetail().getRedeemPer());

                res = (finalAmt / 100) * redimPercent;

                float usedAmt = Float.parseFloat(CompanyDetails.getInstance().getWalletBeanDetail().getUsed_amount());
                float totalAmt = Float.parseFloat(CompanyDetails.getInstance().getWalletBeanDetail().getTotal_amount());

                if (totalAmt > 0) {
                    res = res;
                } else {
                    res = 0;
                }
                walletAmtOnly = res;
                binding.walletAmtLessTextView.setVisibility(res <= 0 ? View.GONE : View.VISIBLE);
                binding.walletAmtLessTextView.setText("Wallet  ₹ " + (new DecimalFormat(".##").format(res)));
            } else {
                binding.walletAmtLessTextView.setVisibility(View.GONE);
            }

            CompanyDetails.getInstance().getWalletBeanDetail().setCalculatePercentAmount("" + res);

            binding.netTotalAmountTextView.setText("Total Payable Amount ₹ " + (new DecimalFormat(".##").format((SelectedProduct.getInstance().getShippingRate() + amtNetTotal) - res)));

            amountCashfreeFinal = ((SelectedProduct.getInstance().getShippingRate() + amtNetTotal) - res);
            //  this.amtNetTotal = ((SelectedProduct.getInstance().getShippingRate() + amtNetTotal) - res);

            Log.e("amountCashfreeFinal", "=" + amountCashfreeFinal);

        } else {

            this.amtNetTotal = (totalAmt);
            binding.deliveryChargesTextView.setText("Delivery Charge  ₹ " + "0");
            float res = 0;
            if (CompanyDetails.getInstance().getWalletBeanDetail() != null) {
                float finalAmt = (amtNetTotal);
                float redimPercent = Float.parseFloat(CompanyDetails.getInstance().getWalletBeanDetail().getRedeemPer());
                res = (finalAmt / 100) * redimPercent;

                float usedAmt = Float.parseFloat(CompanyDetails.getInstance().getWalletBeanDetail().getUsed_amount());
                float totalAmt = Float.parseFloat(CompanyDetails.getInstance().getWalletBeanDetail().getTotal_amount());
                float remainingAmt = totalAmt - usedAmt;
                if (totalAmt > 0) {
                    if (remainingAmt >= res) {
                        res = res;
                    } else {
                        res = remainingAmt;
                    }
                } else {
                    res = 0;
                }
                walletAmtOnly = res;
                binding.walletAmtLessTextView.setVisibility(res <= 0 ? View.GONE : View.VISIBLE);
                binding.walletAmtLessTextView.setText("Wallet  ₹ " + (new DecimalFormat(".##").format(res)));
            } else {
                binding.walletAmtLessTextView.setVisibility(View.GONE);
            }

            CompanyDetails.getInstance().getWalletBeanDetail().setCalculatePercentAmount("" + res);

            binding.netTotalAmountTextView.setText("Total Payable Amount ₹ " + (new DecimalFormat(".##").format(amtNetTotal - res)));
            amountCashfreeFinal = (amtNetTotal - res);
            // this.amtNetTotal = (totalAmt - res);

            Log.e("amountCashfreeFinal", "=" + amountCashfreeFinal);
        }

        binding.bottomAmtLinearLayout.setVisibility(View.VISIBLE);

     /*   if (CompanyDetails.getInstance().getDetails().getWallet_with_discount().equalsIgnoreCase("1")) {
            binding.couponSecLinearLayout.setVisibility(View.VISIBLE);
            applyCouponIfValid();
        } else {
            binding.couponSecLinearLayout.setVisibility(View.GONE);
        }*/

        applyCouponIfValid();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!SelectedProduct.getInstance().getDeliveryType().equalsIgnoreCase("")) {
            binding.paymentSectionLinearLayout.setVisibility(View.VISIBLE);
            SelectedProduct.getInstance().setShippingRate(0);
            calculateAmount();
        }
        if (SelectedProduct.getInstance().getDeliveryType().equalsIgnoreCase("home")) {
            if (SelectedProduct.getInstance().getSavedAddress() != null) {
                double lat1 = Double.parseDouble(SelectedProduct.getInstance().getSavedAddress().getLat());
                double lng1 = Double.parseDouble(SelectedProduct.getInstance().getSavedAddress().getLong());
                getShippingRate(lat1, lng1);
            }
        }
    }


    private void getPaymentDetails() {

        //  showDialog(this);
        if (userBean != null) {
            try {
                JSONObject object = new JSONObject();
                object.put("user_id", userBean.getId());
                //  showDialog(this);

                RestApiManager.getPaymentDetails(MakeParamsHandler.getRequestBody(object.toString()), this, this);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private void getShippingRate(Double lat, Double lng) {
        // {"lat":"22.7244","long":"75.8839"}
        try {
            JSONObject object = new JSONObject();
            object.put("lat", lat);
            object.put("long", lng);

            showDialog(this);

            Log.e("Param Shiping=", "=" + object.toString());

            RestApiManager.getShippingRate(MakeParamsHandler.getRequestBody(object.toString()), this);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void successGetShippingRate(int shipping_rate) {
        dismissDialog();
        SelectedProduct.getInstance().setShippingRate(shipping_rate);

        calculateAmount();
    }

    @Override
    public void successGetTokenOrderId(String token, String orderId) {
        dismissDialog();
        callCashFreeSdk(token, orderId);
    }

    private void callCashFreeSdk(String token, String orderId) {


     /* For PROD   Your app id
        53740780e259b5c40b289debb04735
        Your Secret Key
        05ba88ca3dcbc2715c5df3b49eef7345353308c6
*/

          /* For DEV   Your app id
        167286af2ee404367248b9c9582761
        Your Secret Key
        1a8e231be6b729bd41a656d85f44bcc605f0399e
*/

        String stage = "PROD"; // PROD   TEST

        String appId = CompanyDetails.getInstance().getDetails().getPayment_client_id().trim();  //"422072c143d126035914d75c070224";
        // String orderId = "Order0003";
        String orderAmount = "" + new DecimalFormat(".##").format(amountCashfreeFinal);
        Log.d("appId", "==" + appId);
        String orderNote = "YoApp Payment";
        String customerName = userBean.getName();
        String customerPhone = userBean.getPhoneNo();
        String customerEmail = userBean.getEmail();

        Map<String, String> params = new HashMap<>();

        params.put(PARAM_APP_ID, appId);
        params.put(PARAM_ORDER_ID, orderId);
        params.put(PARAM_ORDER_AMOUNT, "" + orderAmount);
        params.put(PARAM_ORDER_NOTE, orderNote);
        params.put(PARAM_CUSTOMER_NAME, customerName);
        params.put(PARAM_CUSTOMER_PHONE, customerPhone);
        params.put(PARAM_CUSTOMER_EMAIL, customerEmail);


        for (Map.Entry entry : params.entrySet()) {
            Log.d("CFSKDSample1", entry.getKey() + " " + entry.getValue());
        }

        CFPaymentService cfPaymentService = CFPaymentService.getCFPaymentServiceInstance();
        cfPaymentService.setOrientation(0);

        cfPaymentService.doPayment(this, params, token, stage, "#784BD2", "#FFFFFF", false);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Same request code for all payment APIs.
        Log.e(TAG, "ReqCode : " + CFPaymentService.REQ_CODE);
        Log.e(TAG, "API Response : ");
        //Prints all extras. Replace with app logic.
        if (data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                for (String key : bundle.keySet()) {
                    if (bundle.getString(key) != null) {
                        Log.e(TAG, key + " : " + bundle.getString(key));
                    }
                }

                if (bundle.getString("txStatus") != null && bundle.getString("txStatus").equalsIgnoreCase("SUCCESS")) {
                    checkValidation(bundle);
                } else {
                    Toast.makeText(this, "Transaction Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }

        // if success call place Order API here



   /*
      CashFree SDK response

        ReqCode : 9919
        paymentMode : CREDIT_CARD
        orderId : Order0005
        txTime : 2020-05-21 12:33:04
        referenceId : 337770
        type : CashFreeResponse
        txMsg : Transaction Successful
        signature : bygpSUxKVf3ikAwzoJKfo83Y7HmMqFrriid6ggqIXTE=
        orderAmount : 5.00
        txStatus : SUCCESS  */
    }

    private void checkValidation(Bundle bundle) {
        if (SharedPref.getPrefsHelper().getPref("user") == null || SharedPref.getPrefsHelper().getPref("user").toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }

        if (SelectedProduct.getInstance().getSelectedProductList().size() <= 0) {
            SelectedProduct.getInstance().getSelectedProductList().clear();
            Toast.makeText(this, "Please Select Item , we are unable to get cart Item , if you have added please go back and try again", Toast.LENGTH_LONG).show();
            return;
        }

        if (!SelectedProduct.getInstance().getDeliveryType().equalsIgnoreCase("") && SelectedProduct.getInstance().getDeliveryType().equalsIgnoreCase("home")) {
            if (SelectedProduct.getInstance().getSavedAddress() != null) {
                placeOrdr("Delivery", bundle);
            } else {
                Toast.makeText(this, "Please Select Address", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, AddressOptionsActivity.class));
            }
        } else if (!SelectedProduct.getInstance().getDeliveryType().equalsIgnoreCase("") && SelectedProduct.getInstance().getDeliveryType().equalsIgnoreCase("self")) {
            placeOrdr("Self Pickup", bundle);
        } else {
            Toast.makeText(this, "Please Select Delivery Type", Toast.LENGTH_LONG).show();
            //   startActivity(new Intent(this, PaymentOptionActivity.class));
            //  finish();
        }
    }

    @Override
    public void successPlaceOrder(String msg) {
        dismissDialog();
        Toast.makeText(this, "" + msg, Toast.LENGTH_SHORT).show();
        SelectedProduct.getInstance().getSelectedProductList().clear();
        SelectedProduct.getInstance().setDeliveryType("");
        SelectedProduct.getInstance().setPayVia("");
        cartCountTextView.setText("" + SelectedProduct.getInstance().getSelectedProductList().size());
        SharedPref.getPrefsHelper().delete("cartJson");
        startActivity(new Intent(this, UpcomingOrdersActivity.class));
        finish();
    }

    @Override
    public void successPaymentDetails(WalletBeanDetail walletBeanDetail) {
        dismissDialog();
        CompanyDetails.getInstance().setWalletBeanDetail(walletBeanDetail);
        calculateAmount();
    }

    @Override
    public void successCouponDetails(CouponDetailBean couponDetail) {
        dismissDialog();
        CompanyDetails.getInstance().setCouponDetail(couponDetail);
        //  applyCouponIfValid();
        calculateAmount();
    }

    private void applyCouponIfValid() {
        if (CompanyDetails.getInstance().getCouponDetail() != null) {
            float walletAmt = 0;
            if (CompanyDetails.getInstance().getWalletBeanDetail().getCalculatePercentAmount() != null &&
                    !CompanyDetails.getInstance().getWalletBeanDetail().getCalculatePercentAmount().equalsIgnoreCase("")) {
                walletAmt = Float.parseFloat(CompanyDetails.getInstance().getWalletBeanDetail().getCalculatePercentAmount());
            }

            float minimumAmount = Float.parseFloat(CompanyDetails.getInstance().getCouponDetail().getMinAmount());

            if (amtNetTotal >= minimumAmount) {
                if (SelectedProduct.getInstance().getDeliveryType().trim().equalsIgnoreCase("home")) {
                    //  binding.netTotalAmountTextView.setText("Total Payable Amount ₹ " + (new DecimalFormat(".##").format((SelectedProduct.getInstance().getShippingRate() + amtNetTotal) - res)));
                    CouponDetailBean couponDetail = CompanyDetails.getInstance().getCouponDetail();

                    if (couponDetail.getPerCheck().equalsIgnoreCase("1")) {
                        // use discount
                        float afterCouponDiscount = (amtNetTotal / 100) * Integer.parseInt(couponDetail.getDiscountPer());

                        couponid = couponDetail.getId();
                        couponamount = "" + afterCouponDiscount;
                        couponcode = couponDetail.getCouponCode();

                        if (afterCouponDiscount > Float.parseFloat(couponDetail.getPer_min_amount())) {
                            couponamount = couponDetail.getPer_min_amount();
                        }

                        //   Log.e("couponamount", "=" + couponamount);

                        Log.e("freeFinalApplyCopon", "=" + amountCashfreeFinal);

                        binding.netTotalAmountTextView.setText("Total Payable Amount ₹ " + (new DecimalFormat(".##").format((SelectedProduct.getInstance().getShippingRate() + amtNetTotal) - (walletAmt + Float.parseFloat(couponamount)))));
                        amountCashfreeFinal = ((SelectedProduct.getInstance().getShippingRate() + amtNetTotal) - (walletAmt + Float.parseFloat(couponamount)));
                        binding.couponSavingTextView.setVisibility(View.VISIBLE);
                        binding.couponSavingTextView.setText("Coupon Applied : " + (new DecimalFormat(".##").format(Float.parseFloat(couponamount)) + " Saved"));


                    } else {
                        float afterCouponDiscountAmt = Float.parseFloat(couponDetail.getDiscountAmt());

                        couponid = couponDetail.getId();
                        couponamount = "" + afterCouponDiscountAmt;
                        couponcode = couponDetail.getCouponCode();

                        if (afterCouponDiscountAmt > Float.parseFloat(couponDetail.getPer_min_amount())) {
                            couponamount = couponDetail.getPer_min_amount();
                        }
                        binding.netTotalAmountTextView.setText("Total Payable Amount ₹ " + (new DecimalFormat(".##").format((SelectedProduct.getInstance().getShippingRate() + amtNetTotal) - (walletAmt + Float.parseFloat(couponamount)))));
                        amountCashfreeFinal = ((SelectedProduct.getInstance().getShippingRate() + amtNetTotal) - (walletAmt + Float.parseFloat(couponamount)));
                        binding.couponSavingTextView.setVisibility(View.VISIBLE);
                        binding.couponSavingTextView.setText("Coupon Applied : " + (new DecimalFormat(".##").format(Float.parseFloat(couponamount)) + " Saved"));

                    }

                    successAlert();
                    // Toast.makeText(this, "Coupon Applied", Toast.LENGTH_SHORT).show();
                } else {
                    CouponDetailBean couponDetail = CompanyDetails.getInstance().getCouponDetail();
                    if (couponDetail.getPerCheck().equalsIgnoreCase("1")) {
                        // use discount
                        float afterCouponDiscount = (amtNetTotal / 100) * Integer.parseInt(couponDetail.getDiscountPer());

                        couponid = couponDetail.getId();
                        couponamount = "" + afterCouponDiscount;
                        couponcode = couponDetail.getCouponCode();

                        if (afterCouponDiscount > Float.parseFloat(couponDetail.getPer_min_amount())) {
                            couponamount = couponDetail.getPer_min_amount();
                        }
                        binding.couponSavingTextView.setVisibility(View.VISIBLE);
                        binding.couponSavingTextView.setText("Coupon Applied : " + (new DecimalFormat(".##").format(Float.parseFloat(couponamount)) + " Saved"));

                        binding.netTotalAmountTextView.setText("Total Payable Amount ₹ " + (new DecimalFormat(".##").format(amtNetTotal - (walletAmt + Float.parseFloat(couponamount)))));
                        amountCashfreeFinal = (amtNetTotal - (walletAmt + Float.parseFloat(couponamount)));
                    } else {
                        float afterCouponDiscountAmt = Float.parseFloat(couponDetail.getDiscountAmt());

                        couponid = couponDetail.getId();
                        couponamount = "" + afterCouponDiscountAmt;
                        couponcode = couponDetail.getCouponCode();

                        if (afterCouponDiscountAmt > Float.parseFloat(couponDetail.getPer_min_amount())) {
                            couponamount = couponDetail.getPer_min_amount();
                        }

                        binding.couponSavingTextView.setVisibility(View.VISIBLE);
                        binding.couponSavingTextView.setText("Coupon Applied : " + (new DecimalFormat(".##").format(Float.parseFloat(couponamount)) + " Saved"));

                        binding.netTotalAmountTextView.setText("Total Payable Amount ₹ " + (new DecimalFormat(".##").format(amtNetTotal - (walletAmt + Float.parseFloat(couponamount)))));
                        amountCashfreeFinal = (amtNetTotal - (walletAmt + Float.parseFloat(couponamount)));
                    }
                    successAlert();
                    // Toast.makeText(this, "Coupon Applied", Toast.LENGTH_SHORT).show();
                }
            } else {
                AlertUtils.withSingleButton(this, "Alert", "This Coupon code is only applicable of minimum purchase of Rs " + minimumAmount, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, "OK");
            }

        }

        Log.e("freeFinalApplyCopon", "=" + amountCashfreeFinal);
    }


    private void successAlert() {

        binding.couponApplyTextView.setVisibility(View.GONE);
        binding.editCouponTextView.setVisibility(View.VISIBLE);
        binding.couponEditText.setEnabled(false);
        binding.couponEditText.setBackgroundResource(R.drawable.bggreen_border);
        binding.couponEditText.setText(CompanyDetails.getInstance().getCouponDetail().getCouponCode());

        if (CompanyDetails.getInstance().getCouponDetail().isAlertShow()) {
            CompanyDetails.getInstance().getCouponDetail().setAlertShow(false);
            AlertUtils.withSingleButtonSuccess(this, "Voila", "Your Coupon code is Applied Successfully , You saved Rs. " + (new DecimalFormat(".##").format((walletAmtOnly + Float.parseFloat(couponamount)))), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }, "OK");
        }
    }

    @Override
    public void failureInValidCoupon(String error) {
        dismissDialog();
        Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void failure(String error) {
        dismissDialog();
        Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();
    }


    private void placeOrdr(String type, Bundle bundle) {

        if (SelectedProduct.getInstance().getSelectedProductList().size() > 0) {

            ArrayList<ProductBean> arrayList = SelectedProduct.getInstance().getSelectedProductList();
            try {
                JSONObject object = new JSONObject();

                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < arrayList.size(); i++) {

                    ProductBean bean = arrayList.get(i);
                    JSONObject objectInner = new JSONObject();

                    objectInner.put("category_id", bean.getCategoryId());
                    objectInner.put("id", bean.getId());
                    objectInner.put("sub_cat_id", bean.getSubCatId());
                    objectInner.put("product_name", bean.getProductName());
                    objectInner.put("product_description", bean.getProductDescription());
                    objectInner.put("product_price", bean.getCurrentSelectedPrice());
                    objectInner.put("product_attribute", bean.getProduct_attribute());
                    objectInner.put("product_image", bean.getProductImage().get(0));
                    objectInner.put("attr_id", bean.getAttrId());
                    objectInner.put("qtyTotal", bean.getQtyActual());
                    objectInner.put("naration", bean.getNarration());
                    objectInner.put("totalAmt", (Float.parseFloat(bean.getCurrentSelectedPrice()) * bean.getQtyActual()));
                    objectInner.put("extra_img", bean.getExtra_img());

                    jsonArray.put(i, objectInner);
                }


                object.put("items", jsonArray);

                object.put("user_id", userBean.getId());

                if (SelectedProduct.getInstance().getSavedAddress() != null) {
                    object.put("place_name", SelectedProduct.getInstance().getSavedAddress().getAreaName());
                    object.put("lat", SelectedProduct.getInstance().getSavedAddress().getLat());
                    object.put("lng", SelectedProduct.getInstance().getSavedAddress().getLong());
                    object.put("address", getFormatAddress());
                    object.put("address_id", SelectedProduct.getInstance().getSavedAddress().getId());
                } else {
                    object.put("place_name", "");
                    object.put("lat", "0");
                    object.put("lng", "0");
                    object.put("address", "");
                    object.put("address_id", "");
                }
                object.put("delivery_type", type);
                object.put("shipping_rate", SelectedProduct.getInstance().getShippingRate());

                object.put("paymentMode", bundle.getString("paymentMode"));
                object.put("orderId", bundle.getString("orderId"));
                object.put("txTime", bundle.getString("txTime"));
                object.put("referenceId", bundle.getString("referenceId"));
                object.put("type", bundle.getString("type"));
                object.put("txMsg", bundle.getString("txMsg"));
                object.put("signature", bundle.getString("signature"));
                object.put("orderAmount", bundle.getString("orderAmount"));
                object.put("txStatus", bundle.getString("txStatus"));

                if (CompanyDetails.getInstance().getWalletBeanDetail() != null) {
                    object.put("wallet_id", 0);
                    object.put("wallet_amount", CompanyDetails.getInstance().getWalletBeanDetail().getCalculatePercentAmount());
                } else {
                    object.put("wallet_id", 0);
                    object.put("wallet_amount", 0);
                }

                object.put("couponid", couponid);
                object.put("couponamount", couponamount);
                object.put("couponcode", couponcode);

                Log.e("PlaceOrdr", "=" + object.toString());

                showDialog(this);
                RestApiManager.placeOrdr(MakeParamsHandler.getRequestBody(object.toString()), this);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Please Select Items", Toast.LENGTH_SHORT).show();
        }

    }

    private String getFormatAddress() {

        AddressListBean bean = SelectedProduct.getInstance().getSavedAddress();

        String address = "";
        if (bean.getHouseNo() != null && !bean.getHouseNo().equalsIgnoreCase("")) {
            address = address + " House No. " + bean.getHouseNo();
        }

        if (bean.getFloor() != null && !bean.getFloor().equalsIgnoreCase("")) {
            address = address + ", Floor " + bean.getFloor();
        }

        if (bean.getAddress() != null && !bean.getAddress().equalsIgnoreCase("")) {
            address = address + ", " + bean.getAddress();
        }

        if (bean.getAreaName() != null && !bean.getAreaName().equalsIgnoreCase("")) {
            address = address + " , " + bean.getAreaName();
        }

        if (bean.getCity() != null && !bean.getCity().equalsIgnoreCase("")) {
            address = address + " , " + bean.getCity();
        }

        if (bean.getLandmark() != null && !bean.getLandmark().equalsIgnoreCase("")) {
            address = address + " , " + bean.getLandmark();
        }
        return address;
    }
}
