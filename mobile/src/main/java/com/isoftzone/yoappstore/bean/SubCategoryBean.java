package com.isoftzone.yoappstore.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SubCategoryBean implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("sub_cat_name")
    @Expose
    private String subCatName;
    @SerializedName("sub_cat_desc")
    @Expose
    private String subCatDesc;
    @SerializedName("sub_cat_image")
    @Expose
    private String subCatImage;

    boolean isSelected = false;


    @SerializedName("thumbnail_image")
    @Expose
    private String thumbnail_image;


    public SubCategoryBean() {
    }

    public SubCategoryBean(String id, String categoryId, String subCatName, String subCatDesc, String subCatImage) {
        this.id = id;
        this.categoryId = categoryId;
        this.subCatName = subCatName;
        this.subCatDesc = subCatDesc;
        this.subCatImage = subCatImage;
    }


    public String getThumbnail_image() {
        return thumbnail_image;
    }

    public void setThumbnail_image(String thumbnail_image) {
        this.thumbnail_image = thumbnail_image;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
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

    public String getSubCatName() {
        return subCatName;
    }

    public void setSubCatName(String subCatName) {
        this.subCatName = subCatName;
    }

    public String getSubCatDesc() {
        return subCatDesc;
    }

    public void setSubCatDesc(String subCatDesc) {
        this.subCatDesc = subCatDesc;
    }

    public String getSubCatImage() {
        return subCatImage;
    }

    public void setSubCatImage(String subCatImage) {
        this.subCatImage = subCatImage;
    }

}