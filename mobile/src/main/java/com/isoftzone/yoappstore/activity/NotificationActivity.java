package com.isoftzone.yoappstore.activity;


import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.isoftzone.yoappstore.R;
import com.isoftzone.yoappstore.adapter.NotificationsAdapter;
import com.isoftzone.yoappstore.baseactivity.BaseActivity;
import com.isoftzone.yoappstore.bean.CompanyDetails;
import com.isoftzone.yoappstore.bean.NotificationBean;
import com.isoftzone.yoappstore.bean.SelectedProduct;
import com.isoftzone.yoappstore.databinding.ActivityNotificationBinding;
import com.isoftzone.yoappstore.network.CommonInterfaces;
import com.isoftzone.yoappstore.network.MakeParamsHandler;
import com.isoftzone.yoappstore.network.RestApiManager;

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
