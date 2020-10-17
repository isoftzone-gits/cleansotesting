package com.isoftzone.yoappstore.util;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.isoftzone.yoappstore.R;

public abstract class PhotosSheetDialog extends BottomSheetDialog implements View.OnClickListener {

    private Context context;

    protected PhotosSheetDialog(@NonNull Context context) {
        super(context, R.style.ThemeBottomSheetDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_sheet_image_selection);
        WindowManager.LayoutParams lWindowParams = new WindowManager.LayoutParams();
        lWindowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        lWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        this.getWindow().setAttributes(lWindowParams);
        this.findViewById(R.id.tvCancel).setOnClickListener(this);
        this.findViewById(R.id.layout_for_camera).setOnClickListener(this);
        this.findViewById(R.id.layout_for_gallery).setOnClickListener(this);
        this.getWindow().getAttributes().windowAnimations = R.style.BottomSheetAnimation;
    }

    protected abstract void onCamera();

    protected abstract void onGallery();

/*
    protected abstract void onEdit();

    protected abstract void onDelete();
*/

    @Override
    public void onClick(View v) {
        dismiss();
        switch (v.getId()) {
            case R.id.layout_for_camera:
                onCamera();
                break;
            case R.id.layout_for_gallery:
                onGallery();
                break;
         /*   case R.id.layout_for_edit:
                onEdit();
                break;
            case R.id.layout_for_delete:
                onDelete();
                break;*/
        }
    }

}

