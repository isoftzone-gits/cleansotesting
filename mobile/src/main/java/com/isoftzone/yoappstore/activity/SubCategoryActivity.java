package com.isoftzone.yoappstore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.isoftzone.yoappstore.R;
import com.isoftzone.yoappstore.adapter.HeaderSubCategoryAdapter;
import com.isoftzone.yoappstore.adapter.SubCatAdapter;
import com.isoftzone.yoappstore.baseactivity.BaseActivity;
import com.isoftzone.yoappstore.bean.CompanyDetails;
import com.isoftzone.yoappstore.bean.MainCategoryBean;
import com.isoftzone.yoappstore.bean.SelectedProduct;
import com.isoftzone.yoappstore.bean.SubCategoryBean;
import com.isoftzone.yoappstore.network.CommonInterfaces;
import com.isoftzone.yoappstore.network.MakeParamsHandler;
import com.isoftzone.yoappstore.network.RestApiManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SubCategoryActivity extends BaseActivity implements SubCatAdapter.Listner,
        CommonInterfaces.get_sub_category, TextWatcher, HeaderSubCategoryAdapter.Listner {

    private RecyclerView subCatRecyclerView;
    private SubCatAdapter adapter;
    private String catId;
    private ArrayList<SubCategoryBean> subList;
    private ArrayList<SubCategoryBean> arrayListTemporary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        setCustomToolBar(CompanyDetails.getInstance().getDetails().getCompanyName(), false, true);
        catId = getIntent().getStringExtra("catId");
        ArrayList<MainCategoryBean> catList = (ArrayList<MainCategoryBean>) getIntent().getSerializableExtra("catList");
        this.subCatRecyclerView = findViewById(R.id.subCatRecyclerView);
        RecyclerView headerRecyclerView = findViewById(R.id.headerRecyclerView);

        EditText searchEditText = findViewById(R.id.searchEditText);
        headerRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        searchEditText.addTextChangedListener(this);
        arrayListTemporary = new ArrayList<>();

        if (catList != null) {
            for (int i = 0; i < catList.size(); i++) {
                if (catList.get(i).getId().equalsIgnoreCase(catId)) {
                    catList.get(i).setSelected(true);
                    break;
                }
            }
            HeaderSubCategoryAdapter headerSubCategoryAdapter = new HeaderSubCategoryAdapter(this, catList, imageLoader, this);
            headerRecyclerView.setAdapter(headerSubCategoryAdapter);
        }

        get_sub_category();
    }

    private void get_sub_category() {
        try {
            JSONObject object = new JSONObject();
            object.put("cat_id", catId);
            showDialog(this);
            RestApiManager.get_sub_category(MakeParamsHandler.getRequestBody(object.toString()), this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        cartCountTextView.setText("" + SelectedProduct.getInstance().getSelectedProductList().size());
        cartCountTextView.setVisibility(SelectedProduct.getInstance().getSelectedProductList().size() > 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void successSubCategory(ArrayList<SubCategoryBean> subList) {
        dismissDialog();
        this.subList = subList;
        if (CompanyDetails.getInstance().getDetails().getSubcat_grid().equalsIgnoreCase("0")) {
            this.subCatRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
            adapter = new SubCatAdapter(subList, imageLoader, this, this);
            subCatRecyclerView.setAdapter(adapter);
        } else {
            this.subCatRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            adapter = new SubCatAdapter(subList, imageLoader, this, this);
            subCatRecyclerView.setAdapter(adapter);
        }

    }

    @Override
    public void failure(String error) {
        dismissDialog();
    }

    @Override
    public void onClickSubCat(int pos, String subCatId, SubCategoryBean bean) {
        startActivity(new Intent(this, ItemsActivity.class).putExtra("catId", bean.getCategoryId())
                .putExtra("sub_catId", bean.getId()).putExtra("subList", subList).putExtra("hasSubCate", true));
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        filterData(String.valueOf(s));
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public void filterData(String query) {
        if (subList != null) {
            arrayListTemporary.clear();
            if (query.equals("")) {
                arrayListTemporary.addAll(subList);
                adapter = new SubCatAdapter(arrayListTemporary, imageLoader, this, this);
                subCatRecyclerView.setAdapter(adapter);
            } else {
                if (subList != null) {
                    for (int i = 0; i < subList.size(); i++) {
                        if (subList.get(i).getSubCatName().toLowerCase().contains(query)) {
                            arrayListTemporary.add(subList.get(i));
                        }
                    }
                }
                adapter = new SubCatAdapter(arrayListTemporary, imageLoader, this, this);
                subCatRecyclerView.setAdapter(adapter);
            }
        }
    }

    @Override
    public void onClickRow(String catId) {
        this.catId = catId;
        arrayListTemporary.clear();
        if (subList != null) {
            subList.clear();
        }
        get_sub_category();
    }
}
