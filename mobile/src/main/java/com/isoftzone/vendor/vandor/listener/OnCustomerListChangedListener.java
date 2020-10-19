package com.isoftzone.vendor.vandor.listener;

import com.isoftzone.vendor.bean.MainCategoryBean;
import java.util.ArrayList;

public interface OnCustomerListChangedListener {
    void onNoteListChanged(ArrayList<MainCategoryBean> customers);
    void onEdit(MainCategoryBean categoryBean);
    void onClickRow(MainCategoryBean categoryBean);
}
