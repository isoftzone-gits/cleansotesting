package com.isoftzone.yoappstore.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.isoftzone.yoappstore.R;

import java.util.ArrayList;
import java.util.List;

public class FilterSortingDialog extends Dialog implements View.OnClickListener {

    private String forWhich;
    private Context context;
    private String someData;

    private TextView searchTextView;

    private TextView closeTextView;
    private TextView clearFilterTextView;

    private TextView cancelTextView;
    ListnerSorting listnerSorting;

    public interface ListnerSorting {
        void sortByAtoZ();
        void sortByZtoA();
        void sortByLowToHigh();
        void sortByHighToLow();
    }

    public FilterSortingDialog(@NonNull Context context, String forWhich, String someData, ListnerSorting listnerSorting) {
        super(context);
        this.forWhich = forWhich;
        this.context = context;
        this.someData = someData;
        this.listnerSorting = listnerSorting;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        this.setCancelable(true);

        if (forWhich.equalsIgnoreCase("forSorting")) {
            this.setContentView(R.layout.sort_dialogxml);
            getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            getWindow().setGravity(Gravity.TOP);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            this.setCanceledOnTouchOutside(true);
            this.setCancelable(true);

            findViewById(R.id.atozLinearLayout).setOnClickListener(this);
            findViewById(R.id.ztoaLinearLayout).setOnClickListener(this);
            findViewById(R.id.lowToHighLinearLayout).setOnClickListener(this);
            findViewById(R.id.highToLowLinearLayout).setOnClickListener(this);

            this.cancelTextView = findViewById(R.id.cancelTextView);
            this.cancelTextView.setOnClickListener(this);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelTextView:
                dismiss();
                break;
            case R.id.atozLinearLayout:
                listnerSorting.sortByAtoZ();
                dismiss();
                break;
            case R.id.ztoaLinearLayout:
                listnerSorting.sortByZtoA();
                dismiss();
                break;
            case R.id.lowToHighLinearLayout:
                listnerSorting.sortByLowToHigh();
                dismiss();
                break;
            case R.id.highToLowLinearLayout:
                listnerSorting.sortByHighToLow();
                dismiss();
                break;
        }
    }


}