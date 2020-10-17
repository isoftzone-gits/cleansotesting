package com.isoftzone.yoappstore.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalletBean {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("wallet_type")
    @Expose
    private String walletType;
    @SerializedName("referal_id")
    @Expose
    private String referalId;
    @SerializedName("wallet_amount")
    @Expose
    private String walletAmount;

    @SerializedName("expire_date")
    @Expose
    private String expireDate;


    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("redeem_amount")
    @Expose
    private String redeemAmount;
    @SerializedName("referal_to")
    @Expose
    private String referalTo;

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWalletType() {
        return walletType;
    }

    public void setWalletType(String walletType) {
        this.walletType = walletType;
    }

    public String getReferalId() {
        return referalId;
    }

    public void setReferalId(String referalId) {
        this.referalId = referalId;
    }

    public String getWalletAmount() {
        return walletAmount;
    }

    public void setWalletAmount(String walletAmount) {
        this.walletAmount = walletAmount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getRedeemAmount() {
        return redeemAmount;
    }

    public void setRedeemAmount(String redeemAmount) {
        this.redeemAmount = redeemAmount;
    }

    public String getReferalTo() {
        return referalTo;
    }

    public void setReferalTo(String referalTo) {
        this.referalTo = referalTo;
    }
}
