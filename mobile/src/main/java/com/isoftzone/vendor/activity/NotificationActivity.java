package com.isoftzone.vendor.activity;


import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.isoftzone.vendor.R;
import com.isoftzone.vendor.adapter.NotificationsAdapter;
import com.isoftzone.vendor.baseactivity.BaseActivity;
import com.isoftzone.vendor.bean.NotificationBean;
import com.isoftzone.vendor.bean.SelectedProduct;
import com.isoftzone.vendor.databinding.ActivityNotificationBinding;
import com.isoftzone.vendor.network.CommonInterfaces;
import com.isoftzone.vendor.network.MakeParamsHandler;
import com.isoftzone.vendor.network.RestApiManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class NotificationActivity extends BaseActivity implements CommonInterfaces.getNotifications {

    ActivityNotificationBinding binding;
    NotificationsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification);

        setCustomToolBar("Notifications", false, true);

        this.binding.notificationsRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        get_notification();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cartCountTextView.setText("" + SelectedProduct.getInstance().getSelectedProductList().size());
        cartCountTextView.setVisibility(SelectedProduct.getInstance().getSelectedProductList().size() > 0 ? View.VISIBLE : View.GONE);
    }

    private void get_notification() {
        try {
            JSONObject object = new JSONObject();
            object.put("user_id", userBean.getId());
            showDialog(this);
            RestApiManager.getNotifications(MakeParamsHandler.getRequestBody(object.toString()), this);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void successAllNotifications(ArrayList<NotificationBean> notificationBeanArrayList) {
        dismissDialog();

        Collections.reverse(notificationBeanArrayList);
        adapter = new NotificationsAdapter(this, notificationBeanArrayList);
        binding.notificationsRecyclerView.setAdapter(adapter);
    }

    @Override
    public void failure(String error) {
        dismissDialog();
    }
}
