package com.isoftzone.yoappstore.bean;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ProductBean implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("sub_cat_id")
    @Expose
    private String subCatId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_description")
    @Expose
    private String productDescription;
    /*    @SerializedName("product_image")
        @Expose
        private String productImage;*/
    @SerializedName("attributes")
    @Expose
    private List<AttributesBean> attributes = null;

    @SerializedName("product_image")
    @Expose
    private List<String> productImage = null;

    @SerializedName("thumbnail_image")
    @Expose
    private String thumbnail_image;

    @SerializedName("stock")
    @Expose
    private String stock;  // 0=out of Stock

    String extra_img;
    String narration;

    String currentSelectedPrice = "0";
    String attrId;
    String selectedSize;
    int selectedAttPos = -1;
    boolean isSelected = false;

    String product_attribute;

    int qtyActual = 1;

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getThumbnail_image() {
        return thumbnail_image;
    }

    public void setThumbnail_image(String thumbnail_image) {
        this.thumbnail_image = thumbnail_image;
    }

    public String getExtra_img() {
        return extra_img;
    }

    public void setExtra_img(String extra_img) {
        this.extra_img = extra_img;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getSelectedSize() {
        return selectedSize;
    }

    public String getProduct_attribute() {
        return product_attribute;
    }

    public void setProduct_attribute(String product_attribute) {
        this.product_attribute = product_attribute;
    }

    public void setSelectedSize(String selectedSize) {
        this.selectedSize = selectedSize;
    }

    public String getAttrId() {
        return attrId;
    }

    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }

    public int getSelectedAttPos() {
        return selectedAttPos;
    }

    public void setSelectedAttPos(int selectedAttPos) {
        this.selectedAttPos = selectedAttPos;
    }

    public String getCurrentSelectedPrice() {
        return currentSelectedPrice;
    }

    public void setCurrentSelectedPrice(String currentSelectedPrice) {
        this.currentSelectedPrice = currentSelectedPrice;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getQtyActual() {
        return qtyActual;
    }

    public void setQtyActual(int qtyActual) {
        this.qtyActual = qtyActual;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getSubCatId() {
        return subCatId;
    }

    public void setSubCatId(String subCatId) {
        this.subCatId = subCatId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public List<String> getProductImage() {
        return productImage;
    }

    public void setProductImage(List<String> productImage) {
        this.productImage = productImage;
    }

    public List<AttributesBean> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<AttributesBean> attributes) {
        this.attributes = attributes;
    }


}