package com.isoftzone.yoappstore.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CouponDetailBean {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("coupon_code")
    @Expose
    private String couponCode;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("per_check")
    @Expose
    private String perCheck;
    @SerializedName("min_amount")
    @Expose
    private String minAmount;
    @SerializedName("discount_per")
    @Expose
    private String discountPer;
    @SerializedName("discount_amt")
    @Expose
    private String discountAmt;
    @SerializedName("created_by")
    @Expose
    private String createdBy;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("per_min_amount")
    @Expose
    private String per_min_amount;

    boolean isAlertShow = true;

    public boolean isAlertShow() {
        return isAlertShow;
    }

    public void setAlertShow(boolean alertShow) {
        isAlertShow = alertShow;
    }

    public String getPer_min_amount() {
        return per_min_amount;
    }

    public void setPer_min_amount(String per_min_amount) {
        this.per_min_amount = per_min_amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPerCheck() {
        return perCheck;
    }

    public void setPerCheck(String perCheck) {
        this.perCheck = perCheck;
    }

    public String getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(String minAmount) {
        this.minAmount = minAmount;
    }

    public String getDiscountPer() {
        return discountPer;
    }

    public void setDiscountPer(String discountPer) {
        this.discountPer = discountPer;
    }

    public String getDiscountAmt() {
        return discountAmt;
    }

    public void setDiscountAmt(String discountAmt) {
        this.discountAmt = discountAmt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
