package com.isoftzone.vendor.bean;

import java.util.ArrayList;

public class SingletonManager {

    private static SingletonManager singletonManager = new SingletonManager();

    public static SingletonManager getSingletonManager() {
        return singletonManager;
    }

    ArrayList<Banners> bannersArrayList;


    private SingletonManager() {
        bannersArrayList=new ArrayList<>();
    }


    public ArrayList<Banners> getBannersArrayList() {
        return bannersArrayList;
    }

    public void setBannersArrayList(ArrayList<Banners> bannersArrayList) {
        this.bannersArrayList = bannersArrayList;
    }

}
