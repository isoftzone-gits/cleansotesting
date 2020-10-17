package com.isoftzone.yoappstore.activity;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.isoftzone.yoappstore.R;
import com.isoftzone.yoappstore.adapter.ItemsAdapter;
import com.isoftzone.yoappstore.baseactivity.BaseActivity;
import com.isoftzone.yoappstore.bean.ProductBean;
import com.isoftzone.yoappstore.bean.SelectedProduct;
import com.isoftzone.yoappstore.databinding.ActivitySearchBinding;
import com.isoftzone.yoappstore.network.CommonInterfaces;
import com.isoftzone.yoappstore.network.MakeParamsHandler;
import com.isoftzone.yoappstore.network.RestApiManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends BaseActivity implements CommonInterfaces.get_product, ItemsAdapter.Listner {

    ActivitySearchBinding binding;
    ArrayList<ProductBean> productBeanArrayList;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);

        setCustomToolBar("Search", false, true);
        binding.productRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        productBeanArrayList = new ArrayList<>();
        binding.proceedTextView.setBackgroundColor(themeColor());

        itemsAdapter = new ItemsAdapter(this, this.productBeanArrayList, imageLoader, this) {
            @Override
            protected void addToCart() {
                cartCountTextView.setText("" + SelectedProduct.getInstance().getSelectedProductList().size());
                cartCountTextView.setVisibility(SelectedProduct.getInstance().getSelectedProductList().size() > 0 ? View.VISIBLE : View.GONE);
            }
        };
        binding.productRecyclerView.setAdapter(itemsAdapter);

        binding.searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String searchText = binding.searchEditText.getText().toString().trim();
                if (searchText.length() >= 3) {
                    getSearchProduct(searchText);
                }
            }
        });

        binding.proceedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SelectedProduct.getInstance().getSelectedProductList().size() > 0) {
                    startActivity(new Intent(SearchActivity.this, ReviewItemsActivity.class));
                    finish();
                } else {
                    Toast.makeText(SearchActivity.this, "Please Select at least one item", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cartCountTextView.setText("" + SelectedProduct.getInstance().getSelectedProductList().size());
        cartCountTextView.setVisibility(SelectedProduct.getInstance().getSelectedProductList().size() > 0 ? View.VISIBLE : View.GONE);
    }

    private void getSearchProduct(String searchText) {
        try {
            JSONObject object = new JSONObject();
            object.put("search", searchText);
            Log.e("search=", "==" + object.toString());
            //  showDialog(this);
            binding.progressProgressBar.setVisibility(View.VISIBLE);
            RestApiManager.getSearchProduct(MakeParamsHandler.getRequestBody(object.toString()), this);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void successProduct(ArrayList<ProductBean> productBeanArrayList) {
        binding.progressProgressBar.setVisibility(View.GONE);

        this.productBeanArrayList.clear();
        this.productBeanArrayList.addAll(productBeanArrayList);

        ArrayList<ProductBean> arrayList = SelectedProduct.getInstance().getSelectedProductList();
        for (int i = 0; i < productBeanArrayList.size(); i++) {
            for (int j = 0; j < arrayList.size(); j++) {
                if (productBeanArrayList.get(i).getId().equalsIgnoreCase(arrayList.get(j).getId())) {
                    productBeanArrayList.get(i).setSelected(true);
                    productBeanArrayList.get(i).setQtyActual(arrayList.get(j).getQtyActual());
                    break;
                }
            }
        }

        this.itemsAdapter.notifyDataSetChanged();
    }

    @Override
    public void failure(String error) {

    }

    @Override
    public void onClickUpdateCount() {

    }

    @Override
    public void onClickAdd(ProductBean productBean) {
       startActivity(new Intent(this, ProductDetailActivity.class).putExtra("productDetail", productBean));
    }

    @Override
    public void onClickAddNow(ProductBean productBean) {
        if (productBean.getSelectedAttPos() == -1) {
            int selectedPos = 0;
            productBean.getAttributes().get(selectedPos);
            productBean.setSelectedAttPos(0);
            productBean.setAttrId(productBean.getAttributes().get(selectedPos).getId());
            productBean.setSelectedSize(productBean.getAttributes().get(selectedPos).getProductAttributes());
            productBean.setCurrentSelectedPrice(productBean.getAttributes().get(selectedPos).getSell_price() == null ? productBean.getAttributes().get(selectedPos).getProductPrice() : productBean.getAttributes().get(selectedPos).getSell_price());
            productBean.setProduct_attribute(productBean.getAttributes().get(selectedPos).getProductAttributes());
            productBean.setNarration("");
        }
        SelectedProduct.getInstance().addSingleProductDuplicateAlso(productBean);
        cartCountTextView.setVisibility(View.VISIBLE);
        cartCountTextView.setText("" + SelectedProduct.getInstance().getSelectedProductList().size());
    }

    @Override
    public void onReload(ProductBean productBean) {

    }
}
