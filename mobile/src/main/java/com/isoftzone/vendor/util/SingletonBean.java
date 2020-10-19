package com.isoftzone.vendor.util;



public class SingletonBean {

    private static SingletonBean singletonBean = new SingletonBean();

    public static SingletonBean getInstance() {
        return singletonBean;
    }


    private SingletonBean() {

    }


}
