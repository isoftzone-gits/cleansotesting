package com.isoftzone.yoappstore.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;

import com.isoftzone.yoappstore.R;

import java.util.Objects;

public class ProgressDialog extends Dialog {
    Context context;

    public ProgressDialog(Context context) {
        super(context);

        this.context = context;
        // This is the fragment_search_details XML file that describes your Dialog fragment_search_details
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.custom_progress);
        Objects.requireNonNull(this.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams lWindowParams = new WindowManager.LayoutParams();
        lWindowParams.copyFrom(this.getWindow().getAttributes());
        lWindowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        lWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        this.getWindow().setAttributes(lWindowParams);
        setCancelable(false);
    }
}