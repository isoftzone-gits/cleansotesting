package com.isoftzone.yoappstore.activity;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.isoftzone.yoappstore.R;
import com.isoftzone.yoappstore.adapter.OrdersAdapter;
import com.isoftzone.yoappstore.baseactivity.BaseActivity;
import com.isoftzone.yoappstore.bean.OrdersBean;
import com.isoftzone.yoappstore.databinding.ActivityUpcomingOrdersBinding;
import com.isoftzone.yoappstore.network.CommonInterfaces;
import com.isoftzone.yoappstore.network.MakeParamsHandler;
import com.isoftzone.yoappstore.network.RestApiManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class UpcomingOrdersActivity extends BaseActivity implements CommonInterfaces.ongoing_order,
        OrdersAdapter.Listner, CommonInterfaces.cancel_order {

    OrdersAdapter ordersAdapter;
    ActivityUpcomingOrdersBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_upcoming_orders);
        setCustomToolBar("Upcoming Orders", false, true);
        cartRelativeLayout.setVisibility(View.GONE);
        binding.ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ongoing_order();
    }


    private void ongoing_order() {
        try {
            JSONObject object = new JSONObject();
            object.put("user_id", userBean.getId());
            showDialog(this);
            RestApiManager.ongoing_order(MakeParamsHandler.getRequestBody(object.toString()), this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void successGetOrders(ArrayList<OrdersBean> ordersBeanArrayList) {
        dismissDialog();
        Collections.reverse(ordersBeanArrayList);
        ordersAdapter = new OrdersAdapter(ordersBeanArrayList, imageLoader, this, this);
        binding.ordersRecyclerView.setAdapter(ordersAdapter);
    }

    @Override
    public void successCancelOrders(String msg) {
        dismissDialog();
        Toast.makeText(this, "" + msg, Toast.LENGTH_SHORT).show();
        ongoing_order();
    }

    @Override
    public void failure(String error) {
        dismissDialog();
    }

    @Override
    public void onClickCancelOrder(OrdersBean ordersBean) {
        confirmCancelOrder(ordersBean);
    }

    @Override
    public void onClickOrderDetail(OrdersBean ordersBean) {
        startActivity(new Intent(this, OrderDetailActivity.class).putExtra("order", ordersBean).putExtra("comeFrom", "upcoming"));
    }


    public void confirmCancelOrder(final OrdersBean ordersBean) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure,You want to cancel Order");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                        cancel_order(ordersBean);
                    }
                });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private void cancel_order(OrdersBean ordersBean) {
        try {
            JSONObject object = new JSONObject();
            object.put("order_id", ordersBean.getId());
            showDialog(this);
            RestApiManager.cancel_order(MakeParamsHandler.getRequestBody(object.toString()), this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
