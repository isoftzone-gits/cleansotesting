package com.isoftzone.yoappstore.util;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;

import com.isoftzone.yoappstore.R;

import java.util.Objects;

public abstract class CommonDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private String message;
    private String checkSting;
    private int color = 0;
    private String yesName = "", noName = "";

    public CommonDialog(Context context, String message, String checkSting, int color) {
        super(context);
        this.context = context;
        this.message = message;
        this.checkSting = checkSting;
        this.color = color;
    }

    public CommonDialog(Context context, String message, String checkSting, String yesName, String noName) {
        super(context);
        this.context = context;
        this.message = message;
        this.checkSting = checkSting;
        this.yesName = yesName;
        this.noName = noName;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DisplayMetrics displayMetrics = new DisplayMetrics();

        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(this.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.common_dialog_layout);
        WindowManager.LayoutParams lWindowParams = new WindowManager.LayoutParams();
        lWindowParams.copyFrom(this.getWindow().getAttributes());
        this.getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        lWindowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        lWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        this.getWindow().setAttributes(lWindowParams);
        Objects.requireNonNull(this.getWindow()).getAttributes().windowAnimations = R.style.MsgDialogAnimation;
        TextView tvMsg = this.findViewById(R.id.tvMsg);
        this.findViewById(R.id.ll_parent).setOnClickListener(this);
        Button btnNo = this.findViewById(R.id.btnNo);
        btnNo.setOnClickListener(this);
        Button btnYes = this.findViewById(R.id.btnYes);
        btnYes.setOnClickListener(this);
        tvMsg.setText(message);

        if (color!=0) {
            TextView tvheader = this.findViewById(R.id.tvheader);
            setTheamColor(tvheader);
            setTheamColor(btnNo);
            setTheamColor(btnYes);
        }


        if (checkSting.endsWith("Refresh")) {
            this.findViewById(R.id.llYesNo).setVisibility(View.VISIBLE);
            btnYes.setVisibility(View.VISIBLE);
            btnNo.setVisibility(View.VISIBLE);
        } else if (checkSting.endsWith("showYN")) {
            this.findViewById(R.id.llYesNo).setVisibility(View.VISIBLE);
            btnYes.setVisibility(View.VISIBLE);
            btnNo.setVisibility(View.VISIBLE);
        } else if (checkSting.endsWith("showY")) {
            this.findViewById(R.id.llYesNo).setVisibility(View.VISIBLE);
            btnYes.setText("ok");
            btnYes.setVisibility(View.VISIBLE);
        }
        if (!yesName.equalsIgnoreCase("")) {
            btnYes.setText(yesName);
        }
        if (!noName.equalsIgnoreCase("")) {
            btnNo.setText(noName);
        }
    }

    private void setTheamColor(View view){
        Drawable backgroundDrawable = DrawableCompat.wrap(view.getBackground()).mutate();
        DrawableCompat.setTint(backgroundDrawable, color);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_parent:
                dismiss();
                break;
            case R.id.btnYes:
                onYes();
                break;
            case R.id.btnNo:
                onNo();
                break;
        }
    }

    protected abstract void onYes();

    protected abstract void onNo();
}



