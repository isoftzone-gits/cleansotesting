package com.isoftzone.yoappstore.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.isoftzone.yoappstore.R;
import com.isoftzone.yoappstore.adapter.CartAdapter;
import com.isoftzone.yoappstore.baseactivity.BaseActivity;
import com.isoftzone.yoappstore.bean.AddressListBean;
import com.isoftzone.yoappstore.bean.CompanyDetails;
import com.isoftzone.yoappstore.bean.CouponDetailBean;
import com.isoftzone.yoappstore.bean.ProductBean;
import com.isoftzone.yoappstore.bean.SelectedProduct;
import com.isoftzone.yoappstore.databinding.ActivityCartBinding;
import com.isoftzone.yoappstore.network.CommonInterfaces;
import com.isoftzone.yoappstore.network.JSonParser;
import com.isoftzone.yoappstore.network.MakeParamsHandler;
import com.isoftzone.yoappstore.network.RestApiManager;
import com.isoftzone.yoappstore.util.CommonUtils;
import com.isoftzone.yoappstore.util.ImageFilePath;
import com.isoftzone.yoappstore.util.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class CartActivity extends BaseActivity implements CommonInterfaces.placeOrdr,
        CartAdapter.Listner, CommonInterfaces.getShippingRate {

    private ActivityCartBinding binding;
    private CartAdapter adapter;
    float amtSendTotal = 0;
    float totalAmt = 0;
    private String couponid = "", couponamount = "", couponcode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart);
        setCustomToolBar("Cart", false, true);

        cartRelativeLayout.setVisibility(View.GONE);
        binding.cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        binding.headerColorLinearLayout.setBackgroundColor(themeColor());
        binding.placeOrderCardView.setCardBackgroundColor(themeColor());
        binding.changeLinearLayout.setBackgroundColor(themeColor());
           setAdapter();

        if (SelectedProduct.getInstance().getDeliveryType().contains("self")) {
            binding.addressBarLinearLayout.setVisibility(View.GONE);
            binding.addressTextView.setText("Self Pickup");
        } else {
            if (SelectedProduct.getInstance().getSavedAddress() != null) {
                binding.addressTextView.setText(getFormatAddress());
            }
            binding.addressBarLinearLayout.setVisibility(View.VISIBLE);
        }

        binding.changeAddressTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPref.getPrefsHelper().getPref("user") == null || SharedPref.getPrefsHelper().getPref("user").toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(CartActivity.this, "Please login first", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CartActivity.this, LoginActivity.class));
                    return;
                }
                startActivity(new Intent(CartActivity.this, PaymentOptionActivity.class));
                finish();
            }
        });

        binding.paymentLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this, PaymentOptionActivity.class).putExtra("totalAmt", amtSendTotal));
                finish();
            }
        });

        binding.checkoutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeOrderWithCheckAllValidations();
            }
        });

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

    private void placeOrderWithCheckAllValidations() {
        if (SharedPref.getPrefsHelper().getPref("user") == null || SharedPref.getPrefsHelper().getPref("user").toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(CartActivity.this, "Please login first", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(CartActivity.this, LoginActivity.class));
            return;
        }
        if (SelectedProduct.getInstance().getSelectedProductList().size() <= 0) {
            SelectedProduct.getInstance().getSelectedProductList().clear();
            Toast.makeText(CartActivity.this, "Please Select Item , we are unable to get cart Item , if you have added please go back and try again", Toast.LENGTH_LONG).show();
            return;
        }
        if (amtSendTotal <= 0) {
            Toast.makeText(CartActivity.this, "We are unable to get cart Item , if you have added please go back and try again", Toast.LENGTH_LONG).show();
            return;
        }
        if (!SelectedProduct.getInstance().getDeliveryType().equalsIgnoreCase("") && SelectedProduct.getInstance().getDeliveryType().equalsIgnoreCase("home")) {
            if (SelectedProduct.getInstance().getSavedAddress() != null) {
                placeOrdr("Delivery");
            } else {
                Toast.makeText(CartActivity.this, "Please Select Address", Toast.LENGTH_LONG).show();
                startActivity(new Intent(CartActivity.this, AddressOptionsActivity.class));
            }
        } else if (!SelectedProduct.getInstance().getDeliveryType().equalsIgnoreCase("") && SelectedProduct.getInstance().getDeliveryType().equalsIgnoreCase("self")) {
            placeOrdr("Self Pickup");
        } else {
            startActivity(new Intent(CartActivity.this, PaymentOptionActivity.class));
            finish();
        }
    }

    private void placeOrdr(String type) {
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
                    object.put("lat", "");
                    object.put("lng", "");
                    object.put("address", "");
                    object.put("address_id", "");
                }

                object.put("delivery_type", type);
                object.put("shipping_rate", SelectedProduct.getInstance().getShippingRate());
                object.put("paymentMode", "");
                object.put("orderId", "");
                object.put("txTime", "");
                object.put("referenceId", "");
                object.put("type", "");
                object.put("txMsg", "");
                object.put("signature", "");
                object.put("orderAmount", "");
                object.put("txStatus", "");
                if (CompanyDetails.getInstance().getWalletBeanDetail() != null) {
                    object.put("wallet_id", 0);  // CompanyDetails.getInstance().getWalletBeanDetail().getId()
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

    private void setAdapter() {
        adapter = new CartAdapter(SelectedProduct.getInstance().getSelectedProductList(), imageLoader, this, this);
        binding.cartRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SharedPref.getPrefsHelper().getPref("user") != null) {
            userBean = JSonParser.stringJSonToUserLoggedInfo();
        }
        cartCountTextView.setVisibility(SelectedProduct.getInstance().getSelectedProductList().size() > 0 ? View.VISIBLE : View.GONE);
        binding.payUsingNameTextView.setText(SelectedProduct.getInstance().getPayVia());

        if (SelectedProduct.getInstance().getDeliveryType().equalsIgnoreCase("home")) {
            if (SelectedProduct.getInstance().getSavedAddress() != null) {
                binding.addressTextView.setText(getFormatAddress());
                double lat1 = Double.parseDouble(SelectedProduct.getInstance().getSavedAddress().getLat());
                double lng1 = Double.parseDouble(SelectedProduct.getInstance().getSavedAddress().getLong());
                getShippingRate(lat1, lng1);
            }

        }
        setNetAmount();
    }

    private void getShippingRate(Double lat, Double lng) {
        try {
            JSONObject object = new JSONObject();
            object.put("lat", lat);
            object.put("long", lng);

            showDialog(this);
            RestApiManager.getShippingRate(MakeParamsHandler.getRequestBody(object.toString()), this);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void successPlaceOrder(String msg) {
        dismissDialog();
        Toast.makeText(this, "" + msg, Toast.LENGTH_SHORT).show();
        SelectedProduct.getInstance().getSelectedProductList().clear();
        SelectedProduct.getInstance().setDeliveryType("");
        SelectedProduct.getInstance().setPayVia("");
        CompanyDetails.getInstance().setCouponDetail(null);
        cartCountTextView.setText("" + SelectedProduct.getInstance().getSelectedProductList().size());
        SharedPref.getPrefsHelper().delete("cartJson");
        startActivity(new Intent(this, UpcomingOrdersActivity.class));
        finish();
    }

    @Override
    public void successGetShippingRate(int shipping_rate) {
        dismissDialog();
        SelectedProduct.getInstance().setShippingRate(shipping_rate);
        setNetAmount();
    }


    private void setNetAmount() {
        totalAmt = 0;

        for (int i = 0; i < SelectedProduct.getInstance().getSelectedProductList().size(); i++) {
            float innerValue = (Float.parseFloat(SelectedProduct.getInstance().getSelectedProductList().get(i).getCurrentSelectedPrice()) * SelectedProduct.getInstance().getSelectedProductList().get(i).getQtyActual());
            totalAmt = totalAmt + innerValue;
        }

        if (SelectedProduct.getInstance().getDeliveryType().trim().equalsIgnoreCase("home")) {
            this.amtSendTotal = (totalAmt + SelectedProduct.getInstance().getShippingRate());
            binding.totalAmtTextView.setText("₹ " + totalAmt);
            binding.shippingRateTextView.setText("₹ " + SelectedProduct.getInstance().getShippingRate());
            binding.netAmtTextView.setText("₹ " + (totalAmt + SelectedProduct.getInstance().getShippingRate()));

            float res = 0;
            if (CompanyDetails.getInstance().getWalletBeanDetail() != null) {

                float finalAmt = (totalAmt);  // + SelectedProduct.getInstance().getShippingRate()
                float redimPercent = Float.parseFloat(CompanyDetails.getInstance().getWalletBeanDetail().getRedeemPer());
                res = (finalAmt / 100) * redimPercent;

                float usedAmt = Float.parseFloat(CompanyDetails.getInstance().getWalletBeanDetail().getUsed_amount());
                float totalWalletAmt = Float.parseFloat(CompanyDetails.getInstance().getWalletBeanDetail().getTotal_amount());
                float remainingAmt = totalWalletAmt - usedAmt;

                if (totalWalletAmt < 0) {
                    res = 0;
                }
                binding.walletAmtLessTextView.setVisibility(res <= 0 ? View.INVISIBLE : View.VISIBLE);
                binding.walletLabelTextView.setVisibility(res <= 0 ? View.INVISIBLE : View.VISIBLE);
            } else {
                binding.walletAmtLessTextView.setVisibility(View.INVISIBLE);
                binding.walletLabelTextView.setVisibility(View.INVISIBLE);
            }
            CompanyDetails.getInstance().getWalletBeanDetail().setCalculatePercentAmount("" + res);
            binding.walletAmtLessTextView.setText("₹ " + (new DecimalFormat(".##").format(res)));
            binding.netAmtTextView.setText("₹ " + ((totalAmt + SelectedProduct.getInstance().getShippingRate()) - res));
            //  this.amtSendTotal = ((totalAmt + SelectedProduct.getInstance().getShippingRate()) - res);
        } else {
            this.amtSendTotal = (totalAmt);
            binding.totalAmtTextView.setText("₹ " + new DecimalFormat(".##").format(totalAmt));
            binding.shippingRateTextView.setText("₹ " + "0");
            float res = 0;
            if (CompanyDetails.getInstance().getWalletBeanDetail() != null) {
                float finalAmt = (totalAmt);
                float redimPercent = Float.parseFloat(CompanyDetails.getInstance().getWalletBeanDetail().getRedeemPer());
                res = (finalAmt / 100) * redimPercent;

                float usedAmt = Float.parseFloat(CompanyDetails.getInstance().getWalletBeanDetail().getUsed_amount());
                float totalWalletAmt = Float.parseFloat(CompanyDetails.getInstance().getWalletBeanDetail().getTotal_amount());
                float remainingAmt = totalWalletAmt - usedAmt;
                if (totalWalletAmt > 0) {
                    if (remainingAmt >= res) {
                        res = res;
                    } else {
                        res = remainingAmt;
                    }
                } else {
                    res = 0;
                }
                binding.walletAmtLessTextView.setVisibility(res <= 0 ? View.INVISIBLE : View.VISIBLE);
                binding.walletLabelTextView.setVisibility(res <= 0 ? View.INVISIBLE : View.VISIBLE);
            } else {
                binding.walletAmtLessTextView.setVisibility(View.INVISIBLE);
                binding.walletLabelTextView.setVisibility(View.INVISIBLE);
            }

            CompanyDetails.getInstance().getWalletBeanDetail().setCalculatePercentAmount("" + res);
            binding.walletAmtLessTextView.setText("₹ " + (new DecimalFormat(".##").format(res)));
            binding.netAmtTextView.setText("₹ " + new DecimalFormat(".##").format(totalAmt - res));
        }
        applyCouponIfValid();
    }


    private void applyCouponIfValid() {
        if (CompanyDetails.getInstance().getCouponDetail() != null) {
            float walletAmt = 0;
            if (CompanyDetails.getInstance().getWalletBeanDetail().getCalculatePercentAmount() != null &&
                    !CompanyDetails.getInstance().getWalletBeanDetail().getCalculatePercentAmount().equalsIgnoreCase("")) {
                walletAmt = Float.parseFloat(CompanyDetails.getInstance().getWalletBeanDetail().getCalculatePercentAmount());
            }

            float minimumAmount = Float.parseFloat(CompanyDetails.getInstance().getCouponDetail().getMinAmount());

            if (totalAmt >= minimumAmount) {

                if (SelectedProduct.getInstance().getDeliveryType().trim().equalsIgnoreCase("home")) {
                    //  binding.netTotalAmountTextView.setText("Total Payable Amount ₹ " + (new DecimalFormat(".##").format((SelectedProduct.getInstance().getShippingRate() + amtNetTotal) - res)));
                    CouponDetailBean couponDetail = CompanyDetails.getInstance().getCouponDetail();
                    if (couponDetail.getPerCheck().equalsIgnoreCase("1")) {
                        // use discount
                        float afterCouponDiscount = (totalAmt / 100) * Integer.parseInt(couponDetail.getDiscountPer());
                        binding.netAmtTextView.setText("Total Payable Amount ₹ " + (new DecimalFormat(".##").format((SelectedProduct.getInstance().getShippingRate() + totalAmt) - (walletAmt + afterCouponDiscount))));
                        //  amountCashfreeFinal = ((SelectedProduct.getInstance().getShippingRate() + amtNetTotal) - (walletAmt + afterCouponDiscount));
                        couponid = couponDetail.getId();
                        couponamount = "" + afterCouponDiscount;
                        couponcode = couponDetail.getCouponCode();
                    } else {
                        float afterCouponDiscountAmt = Float.parseFloat(couponDetail.getDiscountAmt());
                        binding.netAmtTextView.setText("Total Payable Amount ₹ " + (new DecimalFormat(".##").format((SelectedProduct.getInstance().getShippingRate() + totalAmt) - (walletAmt + afterCouponDiscountAmt))));
                        //  amountCashfreeFinal = ((SelectedProduct.getInstance().getShippingRate() + amtNetTotal) - (walletAmt + afterCouponDiscountAmt));
                        couponid = couponDetail.getId();
                        couponamount = "" + afterCouponDiscountAmt;
                        couponcode = couponDetail.getCouponCode();
                    }
                } else {
                    CouponDetailBean couponDetail = CompanyDetails.getInstance().getCouponDetail();
                    if (couponDetail.getPerCheck().equalsIgnoreCase("1")) {
                        // use discount
                        float afterCouponDiscount = (totalAmt / 100) * Integer.parseInt(couponDetail.getDiscountPer());
                        binding.netAmtTextView.setText("Total Payable Amount ₹ " + (new DecimalFormat(".##").format(totalAmt - (walletAmt + afterCouponDiscount))));
                        //  amountCashfreeFinal = (amtNetTotal - (walletAmt + afterCouponDiscount));
                        couponid = couponDetail.getId();
                        couponamount = "" + afterCouponDiscount;
                        couponcode = couponDetail.getCouponCode();
                    } else {
                        float afterCouponDiscountAmt = Float.parseFloat(couponDetail.getDiscountAmt());
                        binding.netAmtTextView.setText("Total Payable Amount ₹ " + (new DecimalFormat(".##").format(totalAmt - (walletAmt + afterCouponDiscountAmt))));
                        couponid = couponDetail.getId();
                        couponamount = "" + afterCouponDiscountAmt;
                        couponcode = couponDetail.getCouponCode();
                    }
                }
            }

            binding.couponAmtTextView.setText(" ₹ " + new DecimalFormat(".##").format(Float.parseFloat(couponamount)));

        } else {
            binding.couponAmtTextView.setVisibility(View.GONE);
            binding.couponLabelTextView.setVisibility(View.GONE);
        }
        float waltamt = CompanyDetails.getInstance().getWalletBeanDetail().getCalculatePercentAmount().equalsIgnoreCase("") ? 0 : Float.parseFloat(CompanyDetails.getInstance().getWalletBeanDetail().getCalculatePercentAmount());
        float totalSaving = Float.parseFloat((couponamount.equalsIgnoreCase("")?"0":couponamount)) + waltamt;

        if (totalSaving > 0) {
            binding.totalSavingTextView.setText(" ₹ " + new DecimalFormat(".##").format(totalSaving));
            binding.totalSavingLabelTextView.setVisibility(View.VISIBLE);
            binding.totalSavingTextView.setVisibility(View.VISIBLE);
        } else {
            binding.totalSavingLabelTextView.setVisibility(View.GONE);
            binding.totalSavingTextView.setVisibility(View.GONE);
        }
    }


    @Override
    public void failure(String error) {
        dismissDialog();
    }

    ProductBean productBean;

    ImageView imageImageView;


    @Override
    public void onClickRow(ProductBean productBean) {
        startActivity(new Intent(this, ProductDetailActivity.class).putExtra("productDetail", productBean));
    }

    @Override
    public void findNumItems() {
        if (SelectedProduct.getInstance().getSelectedProductList().size() <= 0) {
            finish();
            Toast.makeText(this, "Please Select Items in a Cart", Toast.LENGTH_SHORT).show();
        }
        setNetAmount();
    }

    @Override
    public void onClickPlusMinus(ProductBean productBean) {
        setNetAmount();
    }

    private String pathImg = "NONE";
    private Uri fileUri;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 2) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                File file = CommonUtils.createImageFile();
                fileUri = Uri.fromFile(file);
                CommonUtils.selectFileDialogFromUri(this, file);
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CommonUtils.GALLERY) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {
                        pathImg = ImageFilePath.getPath(this, data.getData());
                        pathImg = ImageFilePath.compressImage(pathImg);
                        imageImageView.setImageURI(Uri.parse(pathImg));
                        Log.e("uriString=", "filename=" + pathImg);
                        String firmImage = "";
                        if (!pathImg.equalsIgnoreCase("NONE")) {
                            firmImage = CommonUtils.getImageFilePathToByteArray(pathImg);
                            firmImage = "data:image/jpeg;base64," + firmImage;
                            Log.e("64ImgStr=", "firmImage=" + firmImage);
                            productBean.setExtra_img(firmImage);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled file selection", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == CommonUtils.CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                //  try {
                pathImg = ImageFilePath.getPath(this, fileUri);
                pathImg = ImageFilePath.compressImage(pathImg);
                imageImageView.setImageURI(Uri.parse(pathImg));

                String firmImage = "";
                if (!pathImg.equalsIgnoreCase("NONE")) {
                    firmImage = CommonUtils.getImageFilePathToByteArray(pathImg);
                    firmImage = "data:image/jpeg;base64," + firmImage;
                    Log.e("64ImgStr=", "firmImage=" + firmImage);
                    productBean.setExtra_img(firmImage);
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled file selection", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
