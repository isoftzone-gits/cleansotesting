package com.isoftzone.yoappstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.isoftzone.yoappstore.R;
import com.isoftzone.yoappstore.bean.AddressListBean;
import com.isoftzone.yoappstore.bean.WalletBean;
import com.isoftzone.yoappstore.util.DateUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class WalletListAdapter extends RecyclerView.Adapter<WalletListAdapter.MyViewHolder> {

    private ArrayList<WalletBean> list;
    private Context context;
    private Listner listner;
    ImageLoader imageLoader;

    public interface Listner {
        void onClickRow(AddressListBean addressListBean);
    }

    public WalletListAdapter(Context context, ArrayList<WalletBean> list, ImageLoader imageLoader) {

        this.list = list;
        this.context = context;
        this.listner = listner;
        this.imageLoader = imageLoader;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_wallet, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final WalletBean bean = list.get(position);
        if (bean.getRedeemAmount() != null && !bean.getRedeemAmount().equalsIgnoreCase("")) {
            float amtremain = (Float.parseFloat(bean.getWalletAmount()) - Float.parseFloat(bean.getRedeemAmount()));
            holder.amtTextView.setText("  ₹ " + amtremain + "  ");
        } else {
            holder.amtTextView.setText("  ₹ " + bean.getWalletAmount() + "  ");
        }
        Date expDate = DateUtils.dateStringFromDate(bean.getExpireDate());
        Date currentDate = Calendar.getInstance().getTime();
        if (expDate.after(currentDate)) {
            holder.expiredOnTextView.setTextColor(context.getResources().getColor(R.color.dangerRed));
            holder.mainLinearLayout.setBackgroundResource(R.drawable.background_shape_white);
            holder.expiredOnTextView.setText("Expire on : " + DateUtils.modifyDateLayout(bean.getExpireDate()));
            holder.expiredOnTextView.setTextColor(context.getResources().getColor(R.color.fontGreen));
        } else {
            holder.amtTextView.setTextColor(context.getResources().getColor(R.color.midGray));
            holder.expiredOnTextView.setText("Expired");
            holder.expiredOnTextView.setTextColor(context.getResources().getColor(R.color.dangerRed));
            holder.mainLinearLayout.setBackgroundResource(R.drawable.background_shapevlightgray);
        }

        holder.toTextView.setText("Type : " + bean.getWalletType());
        if (bean.getWalletType() != null && !bean.getWalletType().equalsIgnoreCase("") && bean.getWalletType().equalsIgnoreCase("registration")) {
            holder.titleTextView.setText("You get the amount For Registration");
        } else {
            holder.titleTextView.setText("You get the amount to refer to " + bean.getReferalTo());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTextView, expiredOnTextView, amtTextView, toTextView;
        private LinearLayout mainLinearLayout;

        public MyViewHolder(View view) {
            super(view);
            this.mainLinearLayout = view.findViewById(R.id.mainLinearLayout);
            this.titleTextView = view.findViewById(R.id.titleTextView);
            this.amtTextView = view.findViewById(R.id.amtTextView);
            this.toTextView = view.findViewById(R.id.toTextView);
            this.expiredOnTextView = view.findViewById(R.id.expiredOnTextView);
        }
    }
}
