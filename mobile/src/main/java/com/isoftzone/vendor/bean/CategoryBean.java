package com.isoftzone.vendor.bean;

import java.io.Serializable;
import java.util.List;

public class CategoryBean implements Serializable {

    String imgPath;
    String uri;
    String categoryName, catDesc;
    int sequence;
    boolean isSubCatAvail = true;
    List<SubCategoryBean> subCategoryList;

    public CategoryBean(String imgPath, String uri, String categoryName, String catDesc, int sequence, boolean isSubCatAvail) {
        this.imgPath = imgPath;
        this.uri = uri;
        this.categoryName = categoryName;
        this.catDesc = catDesc;
        this.sequence = sequence;
        this.isSubCatAvail = isSubCatAvail;
    }

    public List<SubCategoryBean> getSubCategoryList() {
        return subCategoryList;
    }

    public void setSubCategoryList(List<SubCategoryBean> subCategoryList) {
        this.subCategoryList = subCategoryList;
    }

    public boolean isSubCatAvail() {
        return isSubCatAvail;
    }

    public void setSubCatAvail(boolean subCatAvail) {
        isSubCatAvail = subCatAvail;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCatDesc() {
        return catDesc;
    }

    public void setCatDesc(String catDesc) {
        this.catDesc = catDesc;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }
}
