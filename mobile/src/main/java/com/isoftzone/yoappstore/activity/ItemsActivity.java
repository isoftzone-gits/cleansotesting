package com.isoftzone.yoappstore.activity;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.isoftzone.yoappstore.R;
import com.isoftzone.yoappstore.adapter.HeaderAdapter;
import com.isoftzone.yoappstore.adapter.ItemsAdapter;
import com.isoftzone.yoappstore.adapter.SizeRecyAdapter;
import com.isoftzone.yoappstore.baseactivity.BaseActivity;
import com.isoftzone.yoappstore.bean.AttributesBean;
import com.isoftzone.yoappstore.bean.CompanyDetails;
import com.isoftzone.yoappstore.bean.ProductBean;
import com.isoftzone.yoappstore.bean.SelectedProduct;
import com.isoftzone.yoappstore.bean.SubCategoryBean;
import com.isoftzone.yoappstore.databinding.ActivityItemsBinding;
import com.isoftzone.yoappstore.network.CommonInterfaces;
import com.isoftzone.yoappstore.network.MakeParamsHandler;
import com.isoftzone.yoappstore.network.RestApiManager;
import com.isoftzone.yoappstore.util.CommonUtils;
import com.isoftzone.yoappstore.util.FilterSortingDialog;
import com.isoftzone.yoappstore.util.ImageFilePath;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ItemsActivity extends BaseActivity implements HeaderAdapter.Listner, CommonInterfaces.get_product,
        ItemsAdapter.Listner, SizeRecyAdapter.Listner, TextWatcher, FilterSortingDialog.ListnerSorting {

    private ActivityItemsBinding binding;
    private ItemsAdapter itemsAdapter;
    private String catId, sub_catId;
    private ArrayList<ProductBean> productBeanArrayList, arrayListTemporary;
    private Uri fileUri;
    private boolean hasSubCate = false;
    private int pageNum = 1;
    private boolean isSort = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_items);

        setCustomToolBar(CompanyDetails.getInstance().getDetails().getCompanyName(), false, true);
        binding.checkoutCardView.setCardBackgroundColor(themeColor());
        hasSubCate = getIntent().getBooleanExtra("hasSubCate", false);
        catId = getIntent().getStringExtra("catId");

        productBeanArrayList = new ArrayList<>();
        binding.searchEditText.addTextChangedListener(this);
        arrayListTemporary = new ArrayList<>();
        binding.itemsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        binding.headerRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        if (hasSubCate) {
            sub_catId = getIntent().getStringExtra("sub_catId");
            ArrayList<SubCategoryBean> subList = (ArrayList<SubCategoryBean>) getIntent().getSerializableExtra("subList");
            binding.headerRecyclerView.setVisibility(View.VISIBLE);
            for (int i = 0; i < subList.size(); i++) {
                if (subList.get(i).getId().equalsIgnoreCase(sub_catId)) {
                    subList.get(i).setSelected(true);
                    break;
                }
            }
            HeaderAdapter headerAdapter = new HeaderAdapter(this, subList, imageLoader, this);
            binding.headerRecyclerView.setAdapter(headerAdapter);
        } else {
            binding.headerRecyclerView.setVisibility(View.GONE);
        }

        setItemsAdapter();

        binding.checkoutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SelectedProduct.getInstance().getSelectedProductList().size() > 0) {
                    startActivity(new Intent(ItemsActivity.this, ReviewItemsActivity.class));
                    finish();
                } else {
                    Toast.makeText(ItemsActivity.this, "Please Select at least one item", Toast.LENGTH_LONG).show();
                }
            }
        });

        binding.sortTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSort = true;
                if (arrayListTemporary != null) {
                    arrayListTemporary.clear();
                }
                itemsAdapter = new ItemsAdapter(ItemsActivity.this, productBeanArrayList, imageLoader, ItemsActivity.this) {
                    @Override
                    protected void addToCart() {
                        cartCountTextView.setText("" + SelectedProduct.getInstance().getSelectedProductList().size());
                        cartCountTextView.setVisibility(SelectedProduct.getInstance().getSelectedProductList().size() > 0 ? View.VISIBLE : View.GONE);
                    }
                };
                binding.itemsRecyclerView.setAdapter(itemsAdapter);

                FilterSortingDialog sortingDialog = new FilterSortingDialog(ItemsActivity.this, "forSorting", "", ItemsActivity.this);
                sortingDialog.show();
            }
        });

        cartCountTextView.setOnClickListener(null);
        cartCountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SelectedProduct.getInstance().getSelectedProductList().size() > 0) {
                    startActivity(new Intent(ItemsActivity.this, ReviewItemsActivity.class));
                    finish();
                } else {
                    Toast.makeText(ItemsActivity.this, "Please select at least one item", Toast.LENGTH_SHORT).show();
                }
            }
        });

        get_product();
    }

    private void get_product() {
        try {
            JSONObject object = new JSONObject();
            object.put("cat_id", catId);

            if (hasSubCate) {
                object.put("s_cat_id", sub_catId);
            }
            object.put("pagenum", pageNum);
            Log.e("get_product=", "==" + object.toString());

            showDialog(this);
            RestApiManager.get_product(MakeParamsHandler.getRequestBody(object.toString()), this);

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
    public void onClickRow(String catId, String subCatId) {
        pageNum = 1;
        this.catId = catId;
        this.sub_catId = subCatId;
        this.productBeanArrayList.clear();
        this.arrayListTemporary.clear();
        this.itemsAdapter.notifyDataSetChanged();
        get_product();
    }

    @Override
    public void successProduct(ArrayList<ProductBean> productBeanArrayList) {
        dismissDialog();
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
        if (itemsAdapter != null) {
            itemsAdapter.notifyDataSetChanged();
        }
    }

    private void setItemsAdapter() {
        itemsAdapter = new ItemsAdapter(this, this.productBeanArrayList, imageLoader, this) {
            @Override
            protected void addToCart() {
                cartCountTextView.setText("" + SelectedProduct.getInstance().getSelectedProductList().size());
                cartCountTextView.setVisibility(SelectedProduct.getInstance().getSelectedProductList().size() > 0 ? View.VISIBLE : View.GONE);
            }
        };
        binding.itemsRecyclerView.setAdapter(itemsAdapter);
    }

    @Override
    public void failure(String error) {
        dismissDialog();
        if (productBeanArrayList.size() <= 0) {
            Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClickUpdateCount() {
        cartCountTextView.setText("" + SelectedProduct.getInstance().getSelectedProductList().size());
    }

    ProductBean productBean;

    @Override
    public void onClickAdd(ProductBean productBean) {
        startActivity(new Intent(this, ProductDetailActivity.class).putExtra("productDetail", productBean));
        finish();
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
        if (!isSort) {
            pageNum = pageNum + 1;
            get_product();
        }
    }

    ImageView imageImageView;
    ProductBean dialogBeanProduct;
    TextView salePriceTextView, costTextView, totalTextView;
    EditText narrationEditText;


    @Override
    public void onClickItemsSize(AttributesBean attributesBean, int position) {

        dialogBeanProduct.setSelectedAttPos(position);
        dialogBeanProduct.setAttrId(attributesBean.getId());
        dialogBeanProduct.setSelectedSize(attributesBean.getProductAttributes());
        dialogBeanProduct.setCurrentSelectedPrice(attributesBean.getSell_price() == null ? attributesBean.getProductPrice() : attributesBean.getSell_price());
        dialogBeanProduct.setProduct_attribute(attributesBean.getProductAttributes());
        dialogBeanProduct.setNarration(narrationEditText.getText().toString().trim());
        costTextView.setText("₹ " + attributesBean.getProductPrice());

        if (attributesBean.getSell_price() != null) {

            float salePrice = Float.parseFloat(attributesBean.getSell_price());
            float productPrice = Float.parseFloat(attributesBean.getProductPrice());

            if (salePrice <= productPrice) {
                costTextView.setPaintFlags(costTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                costTextView.setText("₹ " + attributesBean.getProductPrice());
                salePriceTextView.setText("₹ " + attributesBean.getSell_price());
                salePriceTextView.setVisibility(View.VISIBLE);
            } else {
                costTextView.setTextColor(getResources().getColor(R.color.black));
                costTextView.setTextSize(14);
                costTextView.setText("₹ " + attributesBean.getProductPrice());
                salePriceTextView.setVisibility(View.INVISIBLE);
            }
        } else {
            costTextView.setTextColor(getResources().getColor(R.color.black));
            costTextView.setTextSize(14);
            salePriceTextView.setText("₹ " + attributesBean.getProductPrice());
            salePriceTextView.setVisibility(View.GONE);
        }
        if (dialogBeanProduct.isSelected()) {
            totalTextView.setText("Total ₹ " + (dialogBeanProduct.getQtyActual() * Float.parseFloat(dialogBeanProduct.getCurrentSelectedPrice())));
            SelectedProduct.getInstance().addSingleProduct(dialogBeanProduct);
        }
    }

    private String pathImg = "NONE";

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 2) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                File file = CommonUtils.createImageFile();
                fileUri = Uri.fromFile(file);
                CommonUtils.selectFileDialogFromUri(this, file);

            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CommonUtils.GALLERY) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {
                        pathImg = ImageFilePath.getPath(this, data.getData());
                        pathImg = ImageFilePath.compressImage(pathImg);
                        imageImageView.setImageURI(Uri.parse(pathImg));
                        Log.e("uriString=", "filename=" + pathImg);
                        String firmImage = "";
                        if (!pathImg.equalsIgnoreCase("NONE")) {
                            firmImage = CommonUtils.getImageFilePathToByteArray(pathImg);
                            firmImage = "data:image/jpeg;base64," + firmImage;

                            Log.e("64ImgStr=", "firmImage=" + firmImage);
                            productBean.setExtra_img(firmImage);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled file selection", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == CommonUtils.CAMERA) {
            if (resultCode == Activity.RESULT_OK) {

                //  try {
                pathImg = ImageFilePath.getPath(this, fileUri);
                pathImg = ImageFilePath.compressImage(pathImg);
                imageImageView.setImageURI(Uri.parse(pathImg));

                Log.e("uriString=", "filename=" + pathImg);

                String firmImage = "";
                if (!pathImg.equalsIgnoreCase("NONE")) {
                    firmImage = CommonUtils.getImageFilePathToByteArray(pathImg);
                    firmImage = "data:image/jpeg;base64," + firmImage;

                    Log.e("64ImgStr=", "firmImage=" + firmImage);
                    productBean.setExtra_img(firmImage);
                }


            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled file selection", Toast.LENGTH_SHORT).show();
            }
        }

    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        isSort = false;
        filterData(String.valueOf(s));
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public void filterData(String query) {

        if (productBeanArrayList != null) {
            arrayListTemporary.clear();
            if (query.equals("")) {
                arrayListTemporary.addAll(productBeanArrayList);
                itemsAdapter = new ItemsAdapter(this, arrayListTemporary, imageLoader, this) {
                    @Override
                    protected void addToCart() {
                        cartCountTextView.setText("" + SelectedProduct.getInstance().getSelectedProductList().size());
                        cartCountTextView.setVisibility(SelectedProduct.getInstance().getSelectedProductList().size() > 0 ? View.VISIBLE : View.GONE);
                    }
                };
                binding.itemsRecyclerView.setAdapter(itemsAdapter);
            } else {
                if (productBeanArrayList != null) {
                    for (int i = 0; i < productBeanArrayList.size(); i++) {
                        if (productBeanArrayList.get(i).getProductName().toLowerCase().contains(query)) {
                            arrayListTemporary.add(productBeanArrayList.get(i));
                        }
                    }
                }
                itemsAdapter = new ItemsAdapter(this, arrayListTemporary, imageLoader, this) {
                    @Override
                    protected void addToCart() {
                        cartCountTextView.setText("" + SelectedProduct.getInstance().getSelectedProductList().size());
                        cartCountTextView.setVisibility(SelectedProduct.getInstance().getSelectedProductList().size() > 0 ? View.VISIBLE : View.GONE);
                    }
                };
                binding.itemsRecyclerView.setAdapter(itemsAdapter);

            }
        }
    }

    @Override
    public void sortByAtoZ() {
        if (productBeanArrayList != null) {
            Collections.sort(productBeanArrayList, new Comparator<ProductBean>() {
                @Override
                public int compare(ProductBean item, ProductBean t1) {
                    String s1 = item.getProductName();
                    String s2 = t1.getProductName();
                    return s1.compareToIgnoreCase(s2);
                }
            });
            itemsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void sortByZtoA() {
        if (productBeanArrayList != null) {
            Collections.sort(productBeanArrayList, new Comparator<ProductBean>() {
                @Override
                public int compare(ProductBean item, ProductBean t1) {
                    String s1 = item.getProductName();
                    String s2 = t1.getProductName();
                    return s2.compareToIgnoreCase(s1);
                }
            });
            itemsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void sortByLowToHigh() {
        if (productBeanArrayList != null) {
            Collections.sort(productBeanArrayList, (item, t1) -> {
                Float s1 = Float.parseFloat(item.getAttributes().get(0).getProductPrice());
                Float s2 = Float.parseFloat(t1.getAttributes().get(0).getProductPrice());
                return s1.compareTo(s2);
            });
            itemsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void sortByHighToLow() {
        if (productBeanArrayList != null) {
            Collections.sort(productBeanArrayList, new Comparator<ProductBean>() {
                @Override
                public int compare(ProductBean item, ProductBean t1) {

                    Float s1 = Float.parseFloat(item.getAttributes().get(0).getProductPrice());
                    Float s2 = Float.parseFloat(t1.getAttributes().get(0).getProductPrice());
                    return s2.compareTo(s1);
                }
            });
            itemsAdapter.notifyDataSetChanged();
        }
    }
}
