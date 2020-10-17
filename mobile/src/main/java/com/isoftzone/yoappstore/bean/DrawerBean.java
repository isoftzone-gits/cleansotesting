package com.isoftzone.yoappstore.bean;


import java.io.Serializable;
import java.util.ArrayList;

public class DrawerBean implements Serializable {

    String title;
    ArrayList<DrawerChildBean> drawerChilList;
    boolean isExpand = false;

    public DrawerBean(String title, ArrayList<DrawerChildBean> drawerChilList, boolean isExpand) {
        this.title = title;
        this.drawerChilList = drawerChilList;
        this.isExpand = isExpand;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<DrawerChildBean> getDrawerChilList() {
        return drawerChilList;
    }

    public void setDrawerChilList(ArrayList<DrawerChildBean> drawerChilList) {
        this.drawerChilList = drawerChilList;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }
}

