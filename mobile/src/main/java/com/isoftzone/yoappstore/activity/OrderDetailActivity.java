package com.isoftzone.yoappstore.activity;


import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.isoftzone.yoappstore.R;
import com.isoftzone.yoappstore.adapter.PlacedOrderItemsAdapter;
import com.isoftzone.yoappstore.baseactivity.BaseActivity;

import com.isoftzone.yoappstore.bean.CompanyDetails;
import com.isoftzone.yoappstore.bean.OrdersBean;
import com.isoftzone.yoappstore.bean.SelectedProduct;
import com.isoftzone.yoappstore.databinding.ActivityOrderDetailBinding;

public class OrderDetailActivity extends BaseActivity {

    OrdersBean bean;
    ActivityOrderDetailBinding binding;
    String comeFrom = "upcoming";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_detail);
        setCustomToolBar(CompanyDetails.getInstance().getDetails().getCompanyName(), false, true);

        bean = (OrdersBean) getIntent().getSerializableExtra("order");
        comeFrom = getIntent().getStringExtra("comeFrom");
        config();
    }

    private void config() {

        if (bean.getStatus().equalsIgnoreCase("placed")) {
            binding.placedTextView.setTextColor(getResources().getColor(R.color.fontGreen));
            binding.progressTextView.setTextColor(getResources().getColor(R.color.darkGray));
            binding.dispatchedextView.setTextColor(getResources().getColor(R.color.darkGray));
            binding.deliveredTextView.setTextColor(getResources().getColor(R.color.darkGray));
            binding.firstImageView.setBackgroundResource(R.drawable.graybg);
            binding.secondImageView.setBackgroundResource(R.drawable.graybg);
            binding.thirdImageView.setBackgroundResource(R.drawable.graybg);
        } else if (bean.getStatus().equalsIgnoreCase("progress")) {
            binding.placedTextView.setTextColor(getResources().getColor(R.color.fontGreen));
            binding.progressTextView.setTextColor(getResources().getColor(R.color.fontGreen));
            binding.dispatchedextView.setTextColor(getResources().getColor(R.color.darkGray));
            binding.deliveredTextView.setTextColor(getResources().getColor(R.color.darkGray));
            binding.firstImageView.setBackgroundResource(R.drawable.greenbg);
            binding.secondImageView.setBackgroundResource(R.drawable.graybg);
            binding.thirdImageView.setBackgroundResource(R.drawable.graybg);
        } else if (bean.getStatus().equalsIgnoreCase("dispatched")) {
            binding.placedTextView.setTextColor(getResources().getColor(R.color.fontGreen));
            binding.progressTextView.setTextColor(getResources().getColor(R.color.fontGreen));
            binding.dispatchedextView.setTextColor(getResources().getColor(R.color.fontGreen));
            binding.deliveredTextView.setTextColor(getResources().getColor(R.color.darkGray));
            binding.firstImageView.setBackgroundResource(R.drawable.greenbg);
            binding.secondImageView.setBackgroundResource(R.drawable.greenbg);
            binding.thirdImageView.setBackgroundResource(R.drawable.graybg);
        } else {
            binding.placedTextView.setTextColor(getResources().getColor(R.color.fontGreen));
            binding.progressTextView.setTextColor(getResources().getColor(R.color.fontGreen));
            binding.dispatchedextView.setTextColor(getResources().getColor(R.color.fontGreen));
            binding.deliveredTextView.setTextColor(getResources().getColor(R.color.fontGreen));
            binding.firstImageView.setBackgroundResource(R.drawable.greenbg);
            binding.secondImageView.setBackgroundResource(R.drawable.greenbg);
            binding.thirdImageView.setBackgroundResource(R.drawable.greenbg);
        }

        if (comeFrom.equalsIgnoreCase("past")) {
            binding.statusLinearLayout.setVisibility(View.GONE);
        } else {
            binding.statusLinearLayout.setVisibility(View.VISIBLE);
        }


        if (bean.getDeliveryType() != null && bean.getDeliveryType().equalsIgnoreCase("Self Pickup")) {
            binding.addressTextView.setText(bean.getDeliveryType());
        } else {
            binding.addressTextView.setText("Address : " + bean.getAddress());
        }

        binding.headerColorLinearLayout.setBackgroundColor(themeColor());

        binding.orderProductListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        PlacedOrderItemsAdapter adapter = new PlacedOrderItemsAdapter(bean.getItems(), imageLoader, this);
        binding.orderProductListRecyclerView.setAdapter(adapter);


        float totalAmt = 0;
        int totalQty = 0;

        for (int i = 0; i < bean.getItems().size(); i++) {
            totalQty = totalQty + Integer.parseInt(bean.getItems().get(i).getQtyTotal());
            totalAmt = totalAmt + Float.parseFloat(bean.getItems().get(i).getTotalAmt());
        }

        binding.totalQtyTextView.setText("" + totalQty);
        binding.netAmtTextView.setText(" ₹ " + totalAmt);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cartCountTextView.setText("" + SelectedProduct.getInstance().getSelectedProductList().size());
        cartCountTextView.setVisibility(SelectedProduct.getInstance().getSelectedProductList().size() > 0 ? View.VISIBLE : View.GONE);
    }
}
