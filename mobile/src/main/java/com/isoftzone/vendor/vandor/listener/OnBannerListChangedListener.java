package com.isoftzone.vendor.vandor.listener;


import com.isoftzone.vendor.bean.Banners;

import java.util.ArrayList;

public interface OnBannerListChangedListener {
    void onNoteListChanged(ArrayList<Banners> bannersList);

    void onEdit(Banners banners);

    void onClickRow(Banners banners);
}
