package com.isoftzone.vendor.bean;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MainCategoryBean implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("category_description")
    @Expose
    private String categoryDescription;
    @SerializedName("category_image")
    @Expose
    private String categoryImage;

    @SerializedName("has_sub_category")
    @Expose
    private boolean has_sub_category;


    @SerializedName("thumbnail_image")
    @Expose
    private String thumbnail_image;

    boolean isSelected = false;

    boolean isSubCatAvail = false;

    private int sequence;



    public MainCategoryBean(String imgPath, /*String uri, */String categoryName, String catDesc, int sequence, boolean isSubCatAvail) {
        this.categoryImage = imgPath;
      /*  this.uri = uri;*/
        this.categoryName = categoryName;
        this.categoryDescription = catDesc;
        this.sequence = sequence;
        this.isSubCatAvail = isSubCatAvail;
    }


    public boolean isSubCatAvail() {
        return isSubCatAvail;
    }

    public void setSubCatAvail(boolean subCatAvail) {
        isSubCatAvail = subCatAvail;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getThumbnail_image() {
        return thumbnail_image;
    }

    public void setThumbnail_image(String thumbnail_image) {
        this.thumbnail_image = thumbnail_image;
    }

    public boolean isHas_sub_category() {
        return has_sub_category;
    }

    public void setHas_sub_category(boolean has_sub_category) {
        this.has_sub_category = has_sub_category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

}
