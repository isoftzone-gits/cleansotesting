package com.isoftzone.vendor.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.isoftzone.vendor.R;
import com.isoftzone.vendor.bean.MainCategoryBean;
import com.isoftzone.vendor.bean.ProductBean;
import com.isoftzone.vendor.bean.SubCategoryBean;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CategoryDialog extends Dialog implements View.OnClickListener {

    private String forWhich;
    private AppCompatActivity context;
    private String someData;
    private ProgressDialog progressDialog;
    private MainCategoryBean categoryBean;
    private SubCategoryBean subCategoryBean;
    private ProductBean productBean;
    private Listner listner;
    private ImageView imgImageView;
    private ImageLoader imageLoader;
    private EditText catNameEditText, descEditText;

    public interface Listner {
        void onClickDone(String catName, String desc);

        void onClickEditImage(MainCategoryBean categoryBean);

        void onClickEditSubCateImage(SubCategoryBean subCategoryBean);
    }

    public CategoryDialog(@NonNull AppCompatActivity context, String forWhich, String someData, MainCategoryBean categoryBean, Listner listner, ImageLoader imageLoader) {
        super(context);
        this.forWhich = forWhich;
        this.context = context;
        this.someData = someData;
        this.categoryBean = categoryBean;
        this.listner = listner;
        this.imageLoader = imageLoader;
    }

    public CategoryDialog(@NonNull AppCompatActivity context, String forWhich, String someData, SubCategoryBean subCategoryBean, Listner listner, ImageLoader imageLoader) {
        super(context);
        this.forWhich = forWhich;
        this.context = context;
        this.someData = someData;
        this.subCategoryBean = subCategoryBean;
        this.listner = listner;
        this.imageLoader = imageLoader;
    }

    public CategoryDialog(@NonNull AppCompatActivity context, String forWhich, String someData, ProductBean subCategoryBean, Listner listner, ImageLoader imageLoader) {
        super(context);
        this.forWhich = forWhich;
        this.context = context;
        this.someData = someData;
        this.productBean = subCategoryBean;
        this.listner = listner;
        this.imageLoader = imageLoader;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (forWhich.equalsIgnoreCase("forCategory")) {
            this.setContentView(R.layout.category_dialogxml);
            getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            getWindow().setGravity(Gravity.CENTER);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            this.setCanceledOnTouchOutside(true);
            this.setCancelable(true);

            imgImageView = findViewById(R.id.imgImageView);
            catNameEditText = findViewById(R.id.catNameEditText);
            descEditText = findViewById(R.id.descEditText);
            TextView cancelTextView = findViewById(R.id.cancelTextView);
            TextView doneTextView = findViewById(R.id.doneTextView);

            ImageView editImageView = findViewById(R.id.editImageView);

            catNameEditText.setText(categoryBean.getCategoryName());
            descEditText.setText(categoryBean.getCategoryDescription());

            //  imgImageView.setImageURI(Uri.parse(categoryBean.getCategoryImage()));
            imageLoader.displayImage(categoryBean.getCategoryImage(), imgImageView);

            cancelTextView.setOnClickListener(this);
            doneTextView.setOnClickListener(this);
            editImageView.setOnClickListener(this);
        }
        if (forWhich.equalsIgnoreCase("forSubCategory")) {
            this.setContentView(R.layout.category_dialogxml);
            getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            getWindow().setGravity(Gravity.CENTER);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            this.setCanceledOnTouchOutside(true);
            this.setCancelable(true);

            imgImageView = findViewById(R.id.imgImageView);
            catNameEditText = findViewById(R.id.catNameEditText);
            descEditText = findViewById(R.id.descEditText);
            TextView cancelTextView = findViewById(R.id.cancelTextView);
            TextView doneTextView = findViewById(R.id.doneTextView);

            ImageView editImageView = findViewById(R.id.editImageView);

            catNameEditText.setText(subCategoryBean.getSubCatName());
            descEditText.setText(subCategoryBean.getSubCatDesc());

            // imgImageView.setImageURI(Uri.parse(subCategoryBean.getUri()));
            imageLoader.displayImage(subCategoryBean.getSubCatImage(), imgImageView);

            cancelTextView.setOnClickListener(this);
            doneTextView.setOnClickListener(this);
            editImageView.setOnClickListener(this);
        }

        if (forWhich.equalsIgnoreCase("product")) {
            this.setContentView(R.layout.category_dialogxml);
            getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            getWindow().setGravity(Gravity.CENTER);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            this.setCanceledOnTouchOutside(true);
            this.setCancelable(true);

            imgImageView = findViewById(R.id.imgImageView);
            catNameEditText = findViewById(R.id.catNameEditText);
            descEditText = findViewById(R.id.descEditText);
            TextView cancelTextView = findViewById(R.id.cancelTextView);
            TextView doneTextView = findViewById(R.id.doneTextView);

            ImageView editImageView = findViewById(R.id.editImageView);

            catNameEditText.setText(productBean.getProductName());
            if (!productBean.getProductDescription().isEmpty())
                descEditText.setText(productBean.getProductDescription());

            // imgImageView.setImageURI(Uri.parse(subCategoryBean.getUri()));
            imageLoader.displayImage(productBean.getThumbnail_image(), imgImageView);

            cancelTextView.setOnClickListener(this);
            doneTextView.setOnClickListener(this);
            editImageView.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancelTextView:
                dismiss();
                break;
            case R.id.doneTextView:
                String catName = catNameEditText.getText().toString().trim();
                String desc = descEditText.getText().toString().trim();
                if (forWhich.equalsIgnoreCase("forCategory")) {
                    if (catName.equalsIgnoreCase("")) {
                        Toast.makeText(context, "Please Enter Category name", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    categoryBean.setCategoryName(catName);
                    categoryBean.setCategoryDescription(desc);
                    listner.onClickDone(catName, desc);
                }
                if (forWhich.equalsIgnoreCase("product")) {
                    if (forWhich.equalsIgnoreCase("product")) {
                        if (catName.equalsIgnoreCase("")) {
                            Toast.makeText(context, "Please Enter Category name", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        productBean.setProductName(catName);
                        productBean.setProductDescription(desc);
                        listner.onClickDone(catName, desc);
                    }
                }
                if (forWhich.equalsIgnoreCase("forSubCategory")) {
                    if (catName.equalsIgnoreCase("")) {
                        Toast.makeText(context, "Please Enter Sub Category name", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    subCategoryBean.setSubCatName(catName);
                    subCategoryBean.setSubCatDesc(desc);
                    listner.onClickDone(catName, desc);
                }
                dismiss();
                break;
            case R.id.editImageView:
                if (forWhich.equalsIgnoreCase("forCategory")) {
                    listner.onClickEditImage(categoryBean);
                } else {
                    listner.onClickEditSubCateImage(subCategoryBean);
                }
                dismiss();
                break;
        }
    }

    public void setImageEdit(MainCategoryBean categoryBean) {
        this.categoryBean = categoryBean;
        imgImageView.setImageURI(Uri.parse(this.categoryBean.getCategoryImage()));
    }

    public void setImageEditSubCate(SubCategoryBean subCate) {
        this.subCategoryBean = subCate;
        imgImageView.setImageURI(Uri.parse(this.subCategoryBean.getSubCatImage()));
    }

    protected void showDialog(Context context, String msg) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(msg);
        progressDialog.show();
    }

    protected void dismissDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }


}
