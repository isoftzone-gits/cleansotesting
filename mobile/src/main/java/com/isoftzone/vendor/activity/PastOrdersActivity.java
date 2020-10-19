package com.isoftzone.vendor.activity;

import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.isoftzone.vendor.R;
import com.isoftzone.vendor.adapter.PastOrdersAdapter;
import com.isoftzone.vendor.baseactivity.BaseActivity;
import com.isoftzone.vendor.bean.OrdersBean;
import com.isoftzone.vendor.databinding.ActivityPastOrdersBinding;
import com.isoftzone.vendor.network.CommonInterfaces;
import com.isoftzone.vendor.network.MakeParamsHandler;
import com.isoftzone.vendor.network.RestApiManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PastOrdersActivity extends BaseActivity implements CommonInterfaces.ongoing_order, PastOrdersAdapter.Listner {

    ActivityPastOrdersBinding binding;
    PastOrdersAdapter ordersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_past_orders);
        setCustomToolBar("Past Orders", false, true);

        cartRelativeLayout.setVisibility(View.GONE);
        binding.ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        past_order();
    }


    private void past_order() {
        try {
            JSONObject object = new JSONObject();
            object.put("user_id", userBean.getId());
            showDialog(this);
            RestApiManager.past_order(MakeParamsHandler.getRequestBody(object.toString()), this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void successGetOrders(ArrayList<OrdersBean> ordersBeanArrayList) {
        dismissDialog();
        ordersAdapter = new PastOrdersAdapter(ordersBeanArrayList, imageLoader, this, this);
        binding.ordersRecyclerView.setAdapter(ordersAdapter);
    }

    @Override
    public void failure(String error) {
        dismissDialog();
    }

    @Override
    public void onClickCancelOrder(OrdersBean ordersBean) {

    }
}
