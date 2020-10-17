package com.isoftzone.yoappstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.isoftzone.yoappstore.R;
import com.isoftzone.yoappstore.bean.AttributesBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class SizeRecyAdapter extends RecyclerView.Adapter<SizeRecyAdapter.MyViewHolder> {

    private List<AttributesBean> attributesBeanList;
    private Context context;

    public Listner listner;
    private String selectedAttrId;

    public interface Listner {
        void onClickItemsSize(AttributesBean bean, int position);
    }

    public SizeRecyAdapter(Context context, List<AttributesBean> attributesBeanList, String selectedAttrId, Listner listner) {
        this.attributesBeanList = attributesBeanList;
        this.context = context;
        this.listner = listner;
        this.selectedAttrId = selectedAttrId;
    }

    @NonNull
    @Override
    public SizeRecyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.size_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final AttributesBean bean = attributesBeanList.get(position);

        holder.titleTextView.setText(bean.getProductAttributes());

        if (selectedAttrId != null && bean.getId().equalsIgnoreCase(selectedAttrId)) {
            holder.mainLinearLayout.setBackgroundResource(R.drawable.bg_borderhighlight);
        } else {
            if (position == 0 && selectedAttrId == null) {
                holder.mainLinearLayout.setBackgroundResource(R.drawable.bg_borderhighlight);
            } else {
                holder.mainLinearLayout.setBackgroundResource(R.drawable.bg_grayline);
            }
        }

        holder.mainLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.onClickItemsSize(bean, position);
            }
        });
    }

    public void setAttrId(String selectedAttrId) {
        this.selectedAttrId = selectedAttrId;
    }

    @Override
    public int getItemCount() {
        return attributesBeanList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private LinearLayout mainLinearLayout;

        public MyViewHolder(View view) {
            super(view);
            this.mainLinearLayout = view.findViewById(R.id.mainLinearLayout);
            this.titleTextView = view.findViewById(R.id.titleTextView);
        }
    }
}
