package com.isoftzone.vendor.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Banners {

    @SerializedName("image")
    @Expose
    private String image;

    public Banners(String actualPath, String toString, String s, String s1, int i) {
        this.image = toString;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
