package com.isoftzone.yoappstore.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalletBeanDetail {


    //  {"rccode":1,"message":"SUCCESS","wallet_amount":{"redeem_per":"10","id":"33","total_amount":"150","used_amount":"30"}}

/*    @SerializedName("id")
    @Expose
    private String id;*/


    @SerializedName("redeem_per")
    @Expose
    private String redeemPer;

    @SerializedName("total_amount")
    @Expose
    private String total_amount;

    @SerializedName("used_amount")
    @Expose
    String used_amount;


    @SerializedName("expire_amount")
    @Expose
    String expire_amount;

    String calculatePercentAmount = "";


    public String getCalculatePercentAmount() {
        return calculatePercentAmount;
    }

    public void setCalculatePercentAmount(String calculatePercentAmount) {
        this.calculatePercentAmount = calculatePercentAmount;
    }

    public String getTotal_amount() {
        return total_amount == null ? "0" : total_amount;
        // return "500";
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getUsed_amount() {
        return used_amount == null ? "0" : used_amount;
    }

    public void setUsed_amount(String used_amount) {
        this.used_amount = used_amount;
    }


    public String getRedeemPer() {
        // return "10";
        return redeemPer == null ? "0" : redeemPer;
    }

    public void setRedeemPer(String redeemPer) {
        this.redeemPer = redeemPer;
    }


    public String getExpire_amount() {
        return expire_amount;
    }

    public void setExpire_amount(String expire_amount) {
        this.expire_amount = expire_amount;
    }
}