package com.isoftzone.yoappstore.activity;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.isoftzone.yoappstore.R;
import com.isoftzone.yoappstore.adapter.AddressListAdapter;
import com.isoftzone.yoappstore.baseactivity.BaseActivity;
import com.isoftzone.yoappstore.bean.AddressListBean;
import com.isoftzone.yoappstore.bean.SelectedProduct;
import com.isoftzone.yoappstore.databinding.ActivityAddressOptionsBinding;
import com.isoftzone.yoappstore.network.CommonInterfaces;
import com.isoftzone.yoappstore.network.MakeParamsHandler;
import com.isoftzone.yoappstore.network.RestApiManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddressOptionsActivity extends BaseActivity implements CommonInterfaces.getAllAddress,
        AddressListAdapter.Listner, CommonInterfaces.deleteAddress, CommonInterfaces.checkVerifyAddress {

    // your product will be delivered here , please place the pin accurately on the map

    private ActivityAddressOptionsBinding binding;
    private AddressListBean addressListBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_address_options);
        setCustomToolBar("", false, true);
        cartRelativeLayout.setVisibility(View.GONE);
        binding.addressRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        binding.addAddressLinearLayout.setOnClickListener(v ->
                startActivity(new Intent(AddressOptionsActivity.this, MapsActivity.class)));

        binding.nextTextView.setOnClickListener(v -> {
            if (SelectedProduct.getInstance().getSavedAddress() != null) {
                finish();
            } else {
                Toast.makeText(AddressOptionsActivity.this, "Please Select Address", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllAddress();
    }

    private void getAllAddress() {
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", userBean.getId());
            showDialog(this);
            RestApiManager.getAllAddress(MakeParamsHandler.getRequestBody(object.toString()), this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void successAllAddress(ArrayList<AddressListBean> beanArrayList) {
        dismissDialog();
        AddressListAdapter addressListAdapter = new AddressListAdapter(this, beanArrayList, this);
        binding.addressRecyclerView.setAdapter(addressListAdapter);
    }

    @Override
    public void successDeleteAddress(String address) {
        dismissDialog();
        Toast.makeText(this, "" + address, Toast.LENGTH_SHORT).show();
        getAllAddress();
    }

    @Override
    public void failure(String error) {

    }

    @Override
    public void onClickRow(AddressListBean addressListBean) {
        this.addressListBean = addressListBean;
        checkVerifyAddress(addressListBean);
    }

    private void checkVerifyAddress(AddressListBean addressListBean) {
        JSONObject object = new JSONObject();
        try {
            object.put("lat", addressListBean.getLat());
            object.put("long", addressListBean.getLong());
            Log.e("checkVerifyAddress", "==" + object.toString());
            showDialog(AddressOptionsActivity.this);
            RestApiManager.checkVerifyAddress(MakeParamsHandler.getRequestBody(object.toString()), this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void successisAddressVerify(String msg) {
        dismissDialog();
        if (msg.equalsIgnoreCase("1")) {
            SelectedProduct.getInstance().setSavedAddress(addressListBean);
        } else {
            Toast.makeText(this, "We are not deliver the products in this Location", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClickDelete(AddressListBean addressListBean) {
        delete_address(addressListBean);
    }

    private void delete_address(AddressListBean addressListBean) {
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", userBean.getId());
            object.put("address_id", addressListBean.getId());
            showDialog(this);
            RestApiManager.delete_address(MakeParamsHandler.getRequestBody(object.toString()), this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
