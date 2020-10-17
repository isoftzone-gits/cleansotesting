package com.isoftzone.yoappstore.adapter;

import android.content.Context;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.isoftzone.yoappstore.R;
import com.isoftzone.yoappstore.bean.AttributesBean;
import com.isoftzone.yoappstore.bean.SizeBean;

import java.util.List;

public class SizeSpinnerAdapter extends ArrayAdapter<SizeBean> {

    private final LayoutInflater mInflater;
    private final Context mContext;
    private List<AttributesBean> beanList;
    private final int mResource;

    public SizeSpinnerAdapter(@NonNull Context context, @LayoutRes int resource, List<AttributesBean> beanList) {
        super(context, resource);

        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        this.beanList = beanList;

    }


    @Override
    public int getCount() {
        return beanList.size();
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView,
                                @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public @NonNull
    View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent) {
        final View view = mInflater.inflate(mResource, parent, false);

        AttributesBean bean = beanList.get(position);

        TextView titleTextView = (TextView) view.findViewById(R.id.titleTextView);
        titleTextView.setText(bean.getProductAttributes());

        return view;
    }
}