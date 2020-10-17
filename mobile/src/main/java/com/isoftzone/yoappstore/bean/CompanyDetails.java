package com.isoftzone.yoappstore.bean;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompanyDetails implements Serializable {

    private static CompanyDetails companyDetails = new CompanyDetails();

    public static CompanyDetails getInstance() {

        return companyDetails;
    }

    CompanyDetails details;


    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("theme_color")
    @Expose
    private String themeColor;
    @SerializedName("logo")
    @Expose
    private String logo;

    @SerializedName("login_type")
    @Expose
    private String login_type;

    @SerializedName("payment_setting")
    @Expose
    private String payment_setting = "0";

    // 0 means hide , 1== show


    @SerializedName("cash_on_delivery")
    @Expose
    private String cash_on_delivery;

    @SerializedName("self_pickup")
    @Expose
    private String self_pickup;


    @SerializedName("home_delivery")
    @Expose
    private String home_delivery;


    @SerializedName("payment_client_id")
    @Expose
    private String payment_client_id;

    @SerializedName("payment_client_secret")
    @Expose
    private String payment_client_secret;


    @SerializedName("payment_setting_name")
    @Expose
    private String payment_setting_name;

    @SerializedName("cash_on_delivery_name")
    @Expose
    private String cash_on_delivery_name;

    @SerializedName("self_pickup_name")
    @Expose
    private String self_pickup_name;

    @SerializedName("home_delivery_name")
    @Expose
    private String home_delivery_name;

    @SerializedName("product_view")
    @Expose
    private String product_view;

    @SerializedName("wallet_with_discount")
    @Expose
    private String wallet_with_discount;

    @SerializedName("subcat_list")
    @Expose
    private String subcat_grid = "0";


    WalletBeanDetail walletBeanDetail;
    CouponDetailBean couponDetail;

    public WalletBeanDetail getWalletBeanDetail() {
        return walletBeanDetail;
    }

    public void setWalletBeanDetail(WalletBeanDetail walletBeanDetail) {
        this.walletBeanDetail = walletBeanDetail;
    }

    public CouponDetailBean getCouponDetail() {
        return couponDetail;
    }

    public void setCouponDetail(CouponDetailBean couponDetail) {
        this.couponDetail = couponDetail;
    }

    public String getSubcat_grid() {
        return subcat_grid;
    }

    public void setSubcat_grid(String subcat_grid) {
        this.subcat_grid = subcat_grid;
    }

    public String getWallet_with_discount() {
        return wallet_with_discount;
    }

    public void setWallet_with_discount(String wallet_with_discount) {
        this.wallet_with_discount = wallet_with_discount;
    }

    public String getPayment_setting() {
        return payment_setting;
    }

    public void setPayment_setting(String payment_setting) {
        this.payment_setting = payment_setting;
    }

    public String getLogin_type() {
        return login_type;
    }

    public void setLogin_type(String login_type) {
        this.login_type = login_type;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getThemeColor() {
        return "#" + themeColor;
    }

    public void setThemeColor(String themeColor) {
        this.themeColor = themeColor;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }


    public CompanyDetails getDetails() {
        return details;
    }

    public void setDetails(CompanyDetails details) {
        this.details = details;
    }


    public String getCash_on_delivery() {
        return cash_on_delivery;
    }

    public void setCash_on_delivery(String cash_on_delivery) {
        this.cash_on_delivery = cash_on_delivery;
    }

    public String getSelf_pickup() {
        return self_pickup;
    }

    public void setSelf_pickup(String self_pickup) {
        this.self_pickup = self_pickup;
    }

    public String getHome_delivery() {
        return home_delivery;
    }

    public void setHome_delivery(String home_delivery) {
        this.home_delivery = home_delivery;
    }

    public String getPayment_client_id() {
        return payment_client_id;
    }

    public void setPayment_client_id(String payment_client_id) {
        this.payment_client_id = payment_client_id;
    }

    public String getPayment_client_secret() {
        return payment_client_secret;
    }

    public void setPayment_client_secret(String payment_client_secret) {
        this.payment_client_secret = payment_client_secret;
    }


    public String getPayment_setting_name() {
        return payment_setting_name;
    }

    public void setPayment_setting_name(String payment_setting_name) {
        this.payment_setting_name = payment_setting_name;
    }

    public String getCash_on_delivery_name() {
        return cash_on_delivery_name;
    }

    public void setCash_on_delivery_name(String cash_on_delivery_name) {
        this.cash_on_delivery_name = cash_on_delivery_name;
    }

    public String getSelf_pickup_name() {
        return self_pickup_name;
    }

    public void setSelf_pickup_name(String self_pickup_name) {
        this.self_pickup_name = self_pickup_name;
    }

    public String getHome_delivery_name() {
        return home_delivery_name;
    }

    public void setHome_delivery_name(String home_delivery_name) {
        this.home_delivery_name = home_delivery_name;
    }

    public String getProduct_view() {
        return product_view;
    }

    public void setProduct_view(String product_view) {
        this.product_view = product_view;
    }
}
