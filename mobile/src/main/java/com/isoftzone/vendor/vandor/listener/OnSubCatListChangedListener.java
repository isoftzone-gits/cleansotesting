package com.isoftzone.vendor.vandor.listener;

import com.isoftzone.vendor.bean.SubCategoryBean;

import java.util.ArrayList;

public interface OnSubCatListChangedListener {
    void onNoteListChanged(ArrayList<SubCategoryBean> subCategoryBeans);
    void onEdit(SubCategoryBean subCategoryBean);
    void onClickRow(SubCategoryBean subCategoryBean);
}
