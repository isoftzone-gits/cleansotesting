package com.isoftzone.yoappstore.activity;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.isoftzone.yoappstore.R;
import com.isoftzone.yoappstore.adapter.CartAdapter;
import com.isoftzone.yoappstore.baseactivity.BaseActivity;
import com.isoftzone.yoappstore.bean.CompanyDetails;
import com.isoftzone.yoappstore.bean.ProductBean;
import com.isoftzone.yoappstore.bean.SelectedProduct;
import com.isoftzone.yoappstore.bean.WalletBeanDetail;
import com.isoftzone.yoappstore.databinding.ActivityReviewItemsBinding;
import com.isoftzone.yoappstore.network.CommonInterfaces;
import com.isoftzone.yoappstore.network.MakeParamsHandler;
import com.isoftzone.yoappstore.network.RestApiManager;
import com.isoftzone.yoappstore.util.SharedPref;

import org.json.JSONException;
import org.json.JSONObject;

public class ReviewItemsActivity extends BaseActivity implements CartAdapter.Listner, CommonInterfaces.getPaymentDetails {

    private ActivityReviewItemsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_review_items);
        setCustomToolBar("Review", false, true);
        cartRelativeLayout.setVisibility(View.GONE);
        binding.cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.checkoutCardView.setCardBackgroundColor(themeColor());
        CartAdapter adapter = new CartAdapter(SelectedProduct.getInstance().getSelectedProductList(), imageLoader, this, this);
        binding.cartRecyclerView.setAdapter(adapter);

        binding.nextTextView.setOnClickListener(v -> {
            if (SharedPref.getPrefsHelper().getPref("user") == null || SharedPref.getPrefsHelper().getPref("user").toString().trim().equalsIgnoreCase("")) {
                Toast.makeText(ReviewItemsActivity.this, "Please login first", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ReviewItemsActivity.this, LoginActivity.class));
                return;
            }
            startActivity(new Intent(ReviewItemsActivity.this, PaymentOptionActivity.class));
            finish();
        });
        setNetAmount();
    }

    @Override
    public void onClickRow(ProductBean productBean) {
        startActivity(new Intent(this, ProductDetailActivity.class).putExtra("productDetail", productBean));
    }

    @Override
    public void findNumItems() {
        if (SelectedProduct.getInstance().getSelectedProductList().size() <= 0) {
            finish();
            Toast.makeText(this, "Please Select at least one Item", Toast.LENGTH_SHORT).show();
        }
        setNetAmount();
    }

    @Override
    public void onClickPlusMinus(ProductBean productBean) {
        setNetAmount();
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    @SuppressLint("SetTextI18n")
    private void setNetAmount() {
        float totalAmt = 0;
        int totalQty = 0;
        for (int i = 0; i < SelectedProduct.getInstance().getSelectedProductList().size(); i++) {
            float innerValue = (Float.parseFloat(SelectedProduct.getInstance().getSelectedProductList().get(i).getCurrentSelectedPrice()) * SelectedProduct.getInstance().getSelectedProductList().get(i).getQtyActual());
            totalAmt = totalAmt + innerValue;
            totalQty = totalQty + SelectedProduct.getInstance().getSelectedProductList().get(i).getQtyActual();
        }
        binding.totalAmtTextView.setText(" ₹ " + totalAmt);
        binding.totalQtyTextView.setText("" + totalQty);
    }

    @Override
    public void successPaymentDetails(WalletBeanDetail walletBeanDetail) {
        CompanyDetails.getInstance().setWalletBeanDetail(walletBeanDetail);
    }

    @Override
    public void failure(String error) {
        dismissDialog();
    }
}
