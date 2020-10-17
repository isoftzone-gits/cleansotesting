package com.isoftzone.yoappstore.bean;

public class SizeBean {


    String sizeType;
    int cost;

    public SizeBean(String sizeType, int cost) {
        this.sizeType = sizeType;
        this.cost = cost;
    }

    public String getSizeType() {
        return sizeType;
    }

    public void setSizeType(String sizeType) {
        this.sizeType = sizeType;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return sizeType;
    }
}
