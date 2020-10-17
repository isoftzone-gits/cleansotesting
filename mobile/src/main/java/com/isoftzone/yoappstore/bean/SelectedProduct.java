package com.isoftzone.yoappstore.bean;

import com.google.gson.Gson;
import com.isoftzone.yoappstore.util.SharedPref;

import java.util.ArrayList;

public class SelectedProduct {

    private static SelectedProduct product = new SelectedProduct();

    ArrayList<ProductBean> selectedProductList;

    int shippingRate = 0;
    String deliveryType = "";
    String payVia = "";

    AddressListBean savedAddress;


    public static SelectedProduct getInstance() {

        return product;
    }

    private SelectedProduct() {
        selectedProductList = new ArrayList<>();
    }

    public ArrayList<ProductBean> getSelectedProductList() {
        return selectedProductList;
    }

    public void setSelectedProductList(ArrayList<ProductBean> selectedProductList) {
        this.selectedProductList = selectedProductList;
    }

    public AddressListBean getSavedAddress() {
        return savedAddress;
    }

    public void setSavedAddress(AddressListBean savedAddress) {
        this.savedAddress = savedAddress;
    }

    public void addSingleProduct(ProductBean bean) {

        boolean isProductFind = false;
        ProductBean productFindBean = null;
        for (int i = 0; i < selectedProductList.size(); i++) {
            ProductBean productBean = selectedProductList.get(i);
            if (productBean.getId().trim().equalsIgnoreCase(bean.getId().trim())) {
                isProductFind = true;
                productFindBean = productBean;
            }
        }

        if (isProductFind) {
            //  selectedProductList.remove(productFindBean);
            //  selectedProductList.add(bean);


            productFindBean.setId(bean.getId());
            productFindBean.setCategoryId(bean.getCategoryId());
            productFindBean.setSubCatId(bean.getSubCatId());
            productFindBean.setProductName(bean.getProductName());
            productFindBean.setProductDescription(bean.getProductDescription());
            productFindBean.setProductImage(bean.getProductImage());

            productFindBean.setAttributes(bean.getAttributes());

            productFindBean.setCurrentSelectedPrice(bean.getCurrentSelectedPrice());
            productFindBean.setAttrId(bean.getAttrId());
            productFindBean.setSelectedSize(bean.getSelectedSize());

            productFindBean.setSelectedAttPos(bean.getSelectedAttPos());
            productFindBean.setSelected(bean.isSelected());
            productFindBean.setQtyActual(bean.getQtyActual());

            productFindBean.setExtra_img(bean.getExtra_img());
            productFindBean.setNarration(bean.getNarration());

            productFindBean.setNarration(bean.getNarration());

        } else {
            selectedProductList.add(bean);
        }

    }

    public void addSingleProductDuplicateAlso(ProductBean bean) {

        if (isProductFind(bean)) {

            if (isProductFindWithAttrId(bean)) {
                for (int i = 0; i < selectedProductList.size(); i++) {
                    ProductBean productBean = selectedProductList.get(i);
                    if (productBean.getId().equalsIgnoreCase(bean.getId()) && productBean.getAttrId().equalsIgnoreCase(bean.getAttrId())) {
                        int mainQty = productBean.getQtyActual()/* + bean.getQtyActual()*/;
                        productBean.setQtyActual(mainQty);
                        break;
                    }
                }
            } else {
                selectedProductList.add(bean);
            }

        } else {
            selectedProductList.add(bean);
        }
        addCartInPref();
    }


    public void addSingleProductDuplicateAlsoT(ProductBean bean) {

        if (isProductFind(bean)) {

            if (isProductFindWithAttrId(bean)) {
                for (int i = 0; i < selectedProductList.size(); i++) {
                    ProductBean productBean = selectedProductList.get(i);
                    if (productBean.getId().equalsIgnoreCase(bean.getId()) && productBean.getAttrId().equalsIgnoreCase(bean.getAttrId())) {
                        int mainQty = productBean.getQtyActual() ;
                        productBean.setQtyActual(mainQty);
                        break;
                    }
                }
            } else {
                selectedProductList.add(bean);
            }

        } else {
            selectedProductList.add(bean);
        }
        addCartInPref();
    }

    private void addCartInPref() {
    /*    Gson gson = new Gson();
        String cartJson = gson.toJson(selectedProductList);
        SharedPref.getPrefsHelper().delete("cartJson");
        SharedPref.getPrefsHelper().savePref("cartJson", cartJson);*/
    }


    public void increaseDecreaseQty(ProductBean bean) {
        for (int i = 0; i < selectedProductList.size(); i++) {
            ProductBean productBean = selectedProductList.get(i);
            if (productBean.getId().equalsIgnoreCase(bean.getId()) && productBean.getAttrId().equalsIgnoreCase(bean.getAttrId())) {
                //  int mainQty = productBean.getQtyActual() + bean.getQtyActual();
                productBean.setQtyActual(bean.getQtyActual());
                break;
            }
        }

        addCartInPref();
    }


    public boolean isProductFindWithAttrId(ProductBean bean) {

        boolean isProductFind = false;
        for (int i = 0; i < selectedProductList.size(); i++) {
            ProductBean productBean = selectedProductList.get(i);
            if (productBean.getId().trim().equalsIgnoreCase(bean.getId().trim()) && productBean.getAttrId().equalsIgnoreCase(bean.getAttrId())) {
                isProductFind = true;
                break;
            }
        }

        return isProductFind;
    }


    public boolean isProductFind(ProductBean bean) {

        boolean isProductFind = false;
        ProductBean productFindBean = null;
        for (int i = 0; i < selectedProductList.size(); i++) {
            ProductBean productBean = selectedProductList.get(i);
            if (productBean.getId().trim().equalsIgnoreCase(bean.getId().trim())) {
                isProductFind = true;
                productFindBean = productBean;
                break;
            }
        }

        return isProductFind;
    }


    public void removeSingleProduct(ProductBean bean) {

        boolean isProductFind = false;
        ProductBean productFindBean = null;
        for (int i = 0; i < selectedProductList.size(); i++) {
            ProductBean productBean = selectedProductList.get(i);
            if (productBean.getId().trim().equalsIgnoreCase(bean.getId().trim())) {
                isProductFind = true;
                productFindBean = productBean;
            }
        }

        if (isProductFind) {
            selectedProductList.remove(productFindBean);
        }
        addCartInPref();
    }

    public String getPayVia() {
        return payVia;
    }

    public void setPayVia(String payVia) {
        this.payVia = payVia;
    }

    public int getShippingRate() {
        return shippingRate;
    }

    public void setShippingRate(int shippingRate) {
        this.shippingRate = shippingRate;
    }


    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }
}
