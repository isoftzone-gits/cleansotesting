package com.isoftzone.vendor.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.isoftzone.vendor.R;
import com.isoftzone.vendor.adapter.UsedTransactionAdapter;
import com.isoftzone.vendor.adapter.WalletListAdapter;
import com.isoftzone.vendor.baseactivity.BaseActivity;
import com.isoftzone.vendor.bean.CompanyDetails;
import com.isoftzone.vendor.bean.RedeemAmount;
import com.isoftzone.vendor.bean.SelectedProduct;
import com.isoftzone.vendor.bean.WalletBean;
import com.isoftzone.vendor.databinding.ActivityWalletBinding;
import com.isoftzone.vendor.network.CommonInterfaces;
import com.isoftzone.vendor.network.MakeParamsHandler;
import com.isoftzone.vendor.network.RestApiManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class WalletActivity extends BaseActivity implements CommonInterfaces.get_wallet {

    ActivityWalletBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wallet);
        config();
        setCustomToolBar("", false, true);
        getWallet();

        binding.walletLabelLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.walletAmountLineTextView.setVisibility(View.VISIBLE);
                binding.walletRecyclerView.setVisibility(View.VISIBLE);
                binding.transacLineTextView.setVisibility(View.GONE);
                binding.usedTransRecyclerView.setVisibility(View.GONE);
                binding.walletAmountLabelTextView.setTextColor(getResources().getColor(R.color.darkBlue));
                binding.transacLabelTextView.setTextColor(getResources().getColor(R.color.darkGray));
            }
        });


        binding.transacLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.walletAmountLineTextView.setVisibility(View.GONE);
                binding.walletRecyclerView.setVisibility(View.GONE);
                binding.transacLineTextView.setVisibility(View.VISIBLE);
                binding.usedTransRecyclerView.setVisibility(View.VISIBLE);

                binding.walletAmountLabelTextView.setTextColor(getResources().getColor(R.color.darkGray));
                binding.transacLabelTextView.setTextColor(getResources().getColor(R.color.darkBlue));
            }
        });

    }

    private void config() {
        binding.walletRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.usedTransRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getWallet() {
        try {
            JSONObject object = new JSONObject();
            object.put("user_id", userBean.getId());

            Log.e("getWallet=", "==" + object.toString());

            showDialog(this);
            RestApiManager.getWallet(MakeParamsHandler.getRequestBody(object.toString()), this);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        cartCountTextView.setText("" + SelectedProduct.getInstance().getSelectedProductList().size());
        cartCountTextView.setVisibility(SelectedProduct.getInstance().getSelectedProductList().size() > 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void successWalletList(String used_amount, ArrayList<RedeemAmount> redeemAmounts, ArrayList<WalletBean> walletList) {
        dismissDialog();
        this.walletList = walletList;
        WalletListAdapter walletListAdapter = new WalletListAdapter(this, this.walletList, imageLoader);
        binding.walletRecyclerView.setAdapter(walletListAdapter);
        totalAmt(used_amount);

        UsedTransactionAdapter usedTransactionAdapter = new UsedTransactionAdapter(this, redeemAmounts, imageLoader);
        binding.usedTransRecyclerView.setAdapter(usedTransactionAdapter);
    }


    float totalAmt = 0;
    float usedTotalAmt = 0;
    ArrayList<WalletBean> walletList;

    private void totalAmt(String used_amount) {

    /*    for (int i = 0; i < walletList.size(); i++) {
            totalAmt = totalAmt + Float.parseFloat(walletList.get(i).getWalletAmount());
            if (walletList.get(i).getRedeemAmount() != null) {
                usedTotalAmt = usedTotalAmt + Float.parseFloat(walletList.get(i).getRedeemAmount());
            }
        }*/

        //binding.totalWalletAmtTextView.setText("₹ " + (totalAmt - usedTotalAmt));

        float usedAmt = Float.parseFloat(used_amount);
        float totalAmt = Float.parseFloat(CompanyDetails.getInstance().getWalletBeanDetail().getTotal_amount());

        float checkAmt = (totalAmt - usedAmt);
        if (checkAmt > 0) {
            binding.totalWalletAmtTextView.setText("₹ " + new DecimalFormat(".##").format(checkAmt));
            binding.totalWalletAmtExpiredTextView.setVisibility(View.GONE);
        } else {
            binding.totalWalletAmtTextView.setText("₹ " + "0");
            binding.totalWalletAmtExpiredTextView.setVisibility(View.VISIBLE);
            binding.totalWalletAmtExpiredTextView.setText("Expired Amount ₹" + CompanyDetails.getInstance().getWalletBeanDetail().getExpire_amount());
        }
    }

    @Override
    public void failure(String error) {
        dismissDialog();

    }
}
