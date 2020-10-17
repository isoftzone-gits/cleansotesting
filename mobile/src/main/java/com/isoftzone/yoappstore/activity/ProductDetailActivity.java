package com.isoftzone.yoappstore.activity;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.isoftzone.yoappstore.R;
import com.isoftzone.yoappstore.adapter.ProductDetailSliderAdapter;
import com.isoftzone.yoappstore.adapter.SizeRecyAdapter;
import com.isoftzone.yoappstore.baseactivity.BaseActivity;
import com.isoftzone.yoappstore.bean.AttributesBean;
import com.isoftzone.yoappstore.bean.CompanyDetails;
import com.isoftzone.yoappstore.bean.ProductBean;
import com.isoftzone.yoappstore.bean.SelectedProduct;
import com.isoftzone.yoappstore.databinding.ActivityProductDetailBinding;
import com.isoftzone.yoappstore.util.CommonUtils;
import com.isoftzone.yoappstore.util.ImageFilePath;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class ProductDetailActivity extends BaseActivity implements SizeRecyAdapter.Listner {

    private ActivityProductDetailBinding binding;
    private ProductBean bean;
    private Uri fileUri;
    private SizeRecyAdapter sizeRecyAdapter;
    private boolean isCommentOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_detail);
        setCustomToolBar(CompanyDetails.getInstance().getDetails().getCompanyName(), false, true);
        config();
    }

    private void config() {
        binding.checkoutCardView.setCardBackgroundColor(themeColor());
        binding.addCartCardView.setCardBackgroundColor(themeColor());

        bean = (ProductBean) getIntent().getSerializableExtra("productDetail");

        assert bean != null;
        if (!bean.getStock().trim().equalsIgnoreCase("0")) {
            binding.outStockTextView.setVisibility(View.GONE);
            binding.addCartCardView.setVisibility(View.VISIBLE);
        } else {
            binding.outStockTextView.setVisibility(View.VISIBLE);
            binding.addCartCardView.setVisibility(View.GONE);
        }
        ProductDetailSliderAdapter slidingImageAdapter = new ProductDetailSliderAdapter(this, bean.getProductImage(), imageLoader);
        binding.detailViewPager.setAdapter(slidingImageAdapter);

        binding.sizeRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        binding.imageRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        sizeRecyAdapter = new SizeRecyAdapter(this, bean.getAttributes(), bean.getAttrId(), this);
        binding.sizeRecyclerView.setAdapter(sizeRecyAdapter);

        binding.titleTextView.setText(bean.getProductName());
        binding.descTextView.setText(bean.getProductDescription());

        binding.qtyTextView.setText("" + bean.getQtyActual());

        if (bean.getSelectedAttPos() != -1) {
            binding.layoutAdd.setVisibility(View.VISIBLE);
            binding.addCartCardView.setVisibility(View.GONE);

         /*   int selectedPos = 0;
            bean.getAttributes().get(selectedPos);
            bean.setSelectedAttPos(0);
            bean.setAttrId(bean.getAttributes().get(selectedPos).getId());
            bean.setSelectedSize(bean.getAttributes().get(selectedPos).getProductAttributes());
            bean.setCurrentSelectedPrice(bean.getAttributes().get(selectedPos).getSell_price() == null ? bean.getAttributes().get(selectedPos).getProductPrice() : bean.getAttributes().get(selectedPos).getSell_price());
            bean.setProduct_attribute(bean.getAttributes().get(selectedPos).getProductAttributes());
            bean.setNarration(binding.narrationEditText.getText().toString().trim());*/
            SelectedProduct.getInstance().addSingleProductDuplicateAlso(bean);
        } else {
            binding.layoutAdd.setVisibility(View.GONE);
            binding.addCartCardView.setVisibility(View.VISIBLE);
        }

        binding.commentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCommentOpen) {
                    isCommentOpen = false;
                    binding.commentTextView.setText("Add additional comment");
                    binding.commentSectionLinearLayout.setVisibility(View.GONE);
                } else {
                    isCommentOpen = true;
                    binding.commentTextView.setText("Hide Comment");
                    binding.commentSectionLinearLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        binding.isAddedTextView.setVisibility(View.GONE);
        binding.totalTextView.setText("");

        if (bean.getAttributes().size() > 0) {

            int selectedPosition = 0;

            if (bean.getAttributes().get(selectedPosition).getSell_price() != null) {

                float salePrice = Float.parseFloat(bean.getAttributes().get(selectedPosition).getSell_price());
                float productPrice = Float.parseFloat(bean.getAttributes().get(selectedPosition).getProductPrice());

                if (salePrice < productPrice) {
                    binding.costTextView.setTextColor(getResources().getColor(R.color.black));
                    binding.costTextView.setTextSize(15);

                    binding.costTextView.setPaintFlags(binding.costTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    binding.costTextView.setText("₹ " + bean.getAttributes().get(selectedPosition).getProductPrice());
                    binding.salePriceTextView.setText("₹ " + bean.getAttributes().get(selectedPosition).getSell_price());
                    binding.salePriceTextView.setVisibility(View.VISIBLE);

                    binding.saveTextView.setVisibility(View.VISIBLE);
                    // binding.saveTextView.setText("₹ " + (productPrice - salePrice) + " SAVE");
                    binding.saveTextView.setText("₹ " + (new DecimalFormat(".00").format((productPrice - salePrice))) + " SAVE");
                } else {
                    binding.costTextView.setTextColor(getResources().getColor(R.color.dangerRed));
                    binding.costTextView.setTextSize(15);
                    binding.costTextView.setText("₹ " + bean.getAttributes().get(selectedPosition).getProductPrice());
                    binding.salePriceTextView.setText("₹ " + bean.getAttributes().get(selectedPosition).getSell_price());
                    binding.salePriceTextView.setVisibility(View.INVISIBLE);
                }

            } else {
                binding.costTextView.setTextColor(getResources().getColor(R.color.black));
                binding.costTextView.setTextSize(15);
                binding.costTextView.setText("₹ " + bean.getAttributes().get(selectedPosition).getProductPrice());
                binding.salePriceTextView.setVisibility(View.GONE);
            }
        }

        binding.narrationEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                bean.setNarration(binding.narrationEditText.getText().toString().trim());
                SelectedProduct.getInstance().addSingleProduct(bean);
            }
        });

        binding.checkoutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String narrtion = binding.narrationEditText.getText().toString().trim();
                bean.setNarration(narrtion);
                if (SelectedProduct.getInstance().getSelectedProductList().size() > 0) {
                    startActivity(new Intent(ProductDetailActivity.this, ReviewItemsActivity.class));
                    finish();
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Please Select at least one item", Toast.LENGTH_LONG).show();
                }
            }
        });

        cartCountTextView.setOnClickListener(null);
        cartCountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SelectedProduct.getInstance().getSelectedProductList().size() > 0) {
                    startActivity(new Intent(ProductDetailActivity.this, ReviewItemsActivity.class));
                    finish();
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Please Select at least one item", Toast.LENGTH_LONG).show();
                }
            }
        });


        ArrayAdapter<AttributesBean> adapter = new ArrayAdapter<AttributesBean>(this, R.layout.size_spinner_items, bean.getAttributes());
        binding.sizeSpinner.setAdapter(adapter);
        binding.sizeSpinner.setSelection(bean.getSelectedAttPos());
        binding.sizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AttributesBean attributesBean = (AttributesBean) parent.getItemAtPosition(position);
                bean.setSelectedAttPos(position);
                bean.setAttrId(attributesBean.getId());
                bean.setSelectedSize(attributesBean.getProductAttributes());
                bean.setCurrentSelectedPrice(attributesBean.getSell_price() == null ? attributesBean.getProductPrice() : attributesBean.getSell_price());
                bean.setProduct_attribute(attributesBean.getProductAttributes());
                bean.setNarration(binding.narrationEditText.getText().toString().trim());

                binding.costTextView.setText("₹ " + attributesBean.getProductPrice());

                if (attributesBean.getSell_price() != null) {
                    float salePrice = Float.parseFloat(attributesBean.getSell_price());
                    float productPrice = Float.parseFloat(attributesBean.getProductPrice());
                    if (salePrice < productPrice) {
                        binding.costTextView.setPaintFlags(binding.costTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        binding.costTextView.setText("₹ " + attributesBean.getProductPrice());
                        binding.salePriceTextView.setText("₹ " + attributesBean.getSell_price());
                        binding.salePriceTextView.setVisibility(View.VISIBLE);
                    } else {
                        binding.costTextView.setTextColor(getResources().getColor(R.color.black));
                        binding.costTextView.setTextSize(14);
                        binding.costTextView.setText("₹ " + attributesBean.getProductPrice());
                        binding.salePriceTextView.setVisibility(View.INVISIBLE);
                    }
                } else {
                    binding.costTextView.setTextColor(getResources().getColor(R.color.black));
                    binding.costTextView.setTextSize(14);
                    binding.salePriceTextView.setText("₹ " + attributesBean.getProductPrice());
                    binding.salePriceTextView.setVisibility(View.GONE);
                }

                if (bean.isSelected()) {
                    binding.totalTextView.setText("Total ₹ " + (bean.getQtyActual() * Float.parseFloat(bean.getCurrentSelectedPrice())));
                    SelectedProduct.getInstance().addSingleProduct(bean);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.closeLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.pickImageLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ActivityCompat.requestPermissions(ProductDetailActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 2);
                } else {
                    File file = CommonUtils.createImageFile();
                    fileUri = Uri.fromFile(file);
                    CommonUtils.selectFileDialogFromUri(ProductDetailActivity.this, file);
                }
            }
        });

        binding.minusTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* int qty = bean.getQtyActual();
                if (qty > 1) {
                    qty--;
                    bean.setQtyActual(qty);
                    binding.qtyTextView.setText("" + bean.getQtyActual());
                }
*/
                int qty = bean.getQtyActual();
                if (qty > 1) {
                    qty--;
                    bean.setQtyActual(qty);
                    SelectedProduct.getInstance().addSingleProduct(bean);
                    binding.qtyTextView.setText("" + bean.getQtyActual());
                }
            }
        });

        binding.plusTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* int qty = bean.getQtyActual();
                qty++;
                bean.setQtyActual(qty);
                binding.qtyTextView.setText("" + bean.getQtyActual());*/

                int qty = bean.getQtyActual();
                qty++;
                bean.setQtyActual(qty);
                bean.setSelected(true);
                SelectedProduct.getInstance().addSingleProduct(bean);
                binding.qtyTextView.setText("" + bean.getQtyActual());

            }
        });

        binding.addCartCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bean.getStock().trim().equalsIgnoreCase("0")) {
                    int selectedPos = 0;
                    bean.getAttributes().get(selectedPos);
                    bean.setSelectedAttPos(0);
                    bean.setAttrId(bean.getAttributes().get(selectedPos).getId());
                    bean.setSelectedSize(bean.getAttributes().get(selectedPos).getProductAttributes());
                    bean.setCurrentSelectedPrice(bean.getAttributes().get(selectedPos).getSell_price() == null ? bean.getAttributes().get(selectedPos).getProductPrice() : bean.getAttributes().get(selectedPos).getSell_price());
                    bean.setProduct_attribute(bean.getAttributes().get(selectedPos).getProductAttributes());
                    bean.setNarration("");
                    SelectedProduct.getInstance().addSingleProductDuplicateAlso(bean);

                }

             /*   if (bean.getSelectedAttPos() == -1) {
                    int selectedPos = 0;
                    bean.getAttributes().get(selectedPos);
                    bean.setSelectedAttPos(0);
                    bean.setAttrId(bean.getAttributes().get(selectedPos).getId());
                    bean.setSelectedSize(bean.getAttributes().get(selectedPos).getProductAttributes());
                    bean.setCurrentSelectedPrice(bean.getAttributes().get(selectedPos).getSell_price() == null ? bean.getAttributes().get(selectedPos).getProductPrice() : bean.getAttributes().get(selectedPos).getSell_price());
                    bean.setProduct_attribute(bean.getAttributes().get(selectedPos).getProductAttributes());
                    bean.setNarration(binding.narrationEditText.getText().toString().trim());
                    SelectedProduct.getInstance().addSingleProductDuplicateAlso(bean);
                }*/

                cartCountTextView.setVisibility(View.VISIBLE);
                cartCountTextView.setText("" + SelectedProduct.getInstance().getSelectedProductList().size());

                binding.addCartCardView.setVisibility(View.GONE);
                binding.layoutAdd.setVisibility(View.VISIBLE);

            }
        });


        if (bean.getProductImage().size() > 0) {
            final int NUM_PAGES = bean.getProductImage().size();
            final ImageView dots[] = new ImageView[NUM_PAGES];
            for (int i = 0; i < NUM_PAGES; i++) {
                dots[i] = new ImageView(this);
                dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(8, 0, 8, 0);
                binding.sliderDots.addView(dots[i], params);
            }
            dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
            binding.detailViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {

                    for (int i = 0; i < NUM_PAGES; i++) {
                        dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                    }

                    dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }

    public boolean isProductFindWithAttrId(ProductBean bean) {

        boolean isProductFind = false;
        for (int i = 0; i < SelectedProduct.getInstance().getSelectedProductList().size(); i++) {
            ProductBean productBean = SelectedProduct.getInstance().getSelectedProductList().get(i);
            if (productBean.getId().trim().equalsIgnoreCase(bean.getId().trim()) && productBean.getAttrId().equalsIgnoreCase(bean.getAttrId())) {
                isProductFind = true;
                break;
            }
        }

        return isProductFind;
    }


    @Override
    protected void onResume() {
        super.onResume();
        cartCountTextView.setText("" + SelectedProduct.getInstance().getSelectedProductList().size());
        cartCountTextView.setVisibility(SelectedProduct.getInstance().getSelectedProductList().size() > 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClickItemsSize(AttributesBean attributesBean, int position) {

        bean.setSelectedAttPos(position);
        bean.setAttrId(attributesBean.getId());
        bean.setSelectedSize(attributesBean.getProductAttributes());
        bean.setCurrentSelectedPrice(attributesBean.getSell_price() == null ? attributesBean.getProductPrice() : attributesBean.getSell_price());
        bean.setProduct_attribute(attributesBean.getProductAttributes());
        bean.setNarration(binding.narrationEditText.getText().toString().trim());

        binding.costTextView.setText("₹ " + attributesBean.getProductPrice());

        if (attributesBean.getSell_price() != null) {

            float salePrice = Float.parseFloat(attributesBean.getSell_price());
            float productPrice = Float.parseFloat(attributesBean.getProductPrice());

            if (salePrice < productPrice) {
                binding.costTextView.setPaintFlags(binding.costTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                binding.costTextView.setTextColor(getResources().getColor(R.color.black));
                binding.costTextView.setText("₹ " + attributesBean.getProductPrice());
                binding.salePriceTextView.setText("₹ " + attributesBean.getSell_price());
                binding.salePriceTextView.setVisibility(View.VISIBLE);
                binding.saveTextView.setText("₹ " + (new DecimalFormat(".00").format((productPrice - salePrice))) + " SAVE");
                binding.saveTextView.setVisibility(View.VISIBLE);
            } else {
                binding.costTextView.setPaintFlags(binding.costTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                binding.costTextView.setTextColor(getResources().getColor(R.color.dangerRed));
                binding.costTextView.setTextSize(15);
                binding.costTextView.setText("₹ " + attributesBean.getProductPrice());
                binding.salePriceTextView.setVisibility(View.INVISIBLE);
                binding.saveTextView.setVisibility(View.GONE);
            }

        } else {
            binding.costTextView.setTextColor(getResources().getColor(R.color.black));
            binding.costTextView.setTextSize(15);
            binding.salePriceTextView.setText("₹ " + attributesBean.getProductPrice());
            binding.salePriceTextView.setVisibility(View.GONE);
        }


        if (bean.isSelected()) {
            binding.totalTextView.setText("Total ₹ " + (bean.getQtyActual() * Float.parseFloat(bean.getCurrentSelectedPrice())));
            SelectedProduct.getInstance().addSingleProduct(bean);
        }
        sizeRecyAdapter.setAttrId(bean.getAttrId());
        sizeRecyAdapter.notifyDataSetChanged();
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
                        binding.imageImageView.setImageURI(Uri.parse(pathImg));
                        Log.e("uriString=", "filename=" + pathImg);
                        String firmImage = "";
                        if (!pathImg.equalsIgnoreCase("NONE")) {
                            firmImage = CommonUtils.getImageFilePathToByteArray(pathImg);
                            firmImage = "data:image/jpeg;base64," + firmImage;

                            Log.e("64ImgStr=", "firmImage=" + firmImage);
                            bean.setExtra_img(firmImage);
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
                pathImg = ImageFilePath.getPath(this, fileUri);
                pathImg = ImageFilePath.compressImage(pathImg);
                binding.imageImageView.setImageURI(Uri.parse(pathImg));
                Log.e("uriString=", "filename=" + pathImg);

                String firmImage = "";
                if (!pathImg.equalsIgnoreCase("NONE")) {
                    firmImage = CommonUtils.getImageFilePathToByteArray(pathImg);
                    firmImage = "data:image/jpeg;base64," + firmImage;
                    Log.e("64ImgStr=", "firmImage=" + firmImage);
                    bean.setExtra_img(firmImage);
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled file selection", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
