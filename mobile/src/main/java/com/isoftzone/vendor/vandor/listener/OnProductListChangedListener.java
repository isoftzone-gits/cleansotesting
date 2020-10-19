package com.isoftzone.vendor.vandor.listener;


import com.isoftzone.vendor.bean.ProductBean;

import java.util.ArrayList;

public interface OnProductListChangedListener {
    void onNoteListChanged(ArrayList<ProductBean> subCategoryBeans);

    void onEdit(ProductBean subCategoryBean);

    void onClickRow(ProductBean subCategoryBean);
}
