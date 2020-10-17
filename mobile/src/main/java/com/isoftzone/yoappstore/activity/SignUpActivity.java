package com.isoftzone.yoappstore.activity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;


import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.databinding.DataBindingUtil;

import com.isoftzone.yoappstore.R;
import com.isoftzone.yoappstore.baseactivity.BaseActivity;
import com.isoftzone.yoappstore.bean.UserProfileBean;
import com.isoftzone.yoappstore.databinding.ActivitySignUpBinding;
import com.isoftzone.yoappstore.network.CommonInterfaces;
import com.isoftzone.yoappstore.network.Constants;
import com.isoftzone.yoappstore.network.MakeParamsHandler;
import com.isoftzone.yoappstore.network.RestApiManager;
import com.isoftzone.yoappstore.util.CommonUtils;
import com.isoftzone.yoappstore.util.CustomDialog;
import com.isoftzone.yoappstore.util.ImageFilePath;
import com.isoftzone.yoappstore.util.SharedPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

public class SignUpActivity extends BaseActivity implements CommonInterfaces.RegistrationDelegate,
        CommonInterfaces.getProfile, CommonInterfaces.updateProfile {

    private String pathImg = "NONE";
    private Uri fileUri;
    String type = "login";
    ActivitySignUpBinding binding;
    String gender = "";
    ArrayList<String> stringsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        type = getIntent().getStringExtra("via");
        config();
    }

    private void config() {
        binding.addCartCardView.setCardBackgroundColor(themeColor());

        this.binding.signupTextView.setOnClickListener(this);
        this.binding.dobTextView.setOnClickListener(this);
        this.binding.anniversaryTextView.setOnClickListener(this);
        binding.imgRelativeLayout.setOnClickListener(this);

        stringsList = new ArrayList<>();
        stringsList.add("Male");
        stringsList.add("Female");

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, R.layout.size_spinner_items, stringsList);
        binding.genderSpinner.setAdapter(genderAdapter);
        binding.genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gender = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        if (type.equalsIgnoreCase("profile")) {
            setCustomToolBar("Profile", false, true);
            binding.signupTextView.setText("Update Profile");

            binding.firstNameEditText.setVisibility(View.VISIBLE);
            binding.mobileEditText.setVisibility(View.VISIBLE);
            binding.userEmailIdEditText.setVisibility(View.VISIBLE);

            binding.dobTextView.setVisibility(View.VISIBLE);
            binding.anniversaryTextView.setVisibility(View.VISIBLE);
            binding.genderSpinner.setVisibility(View.VISIBLE);
            binding.addressEditText.setVisibility(View.VISIBLE);

            binding.passTextInputLayout.setVisibility(View.GONE);
            binding.confirmPassTextInputLayout.setVisibility(View.GONE);

            binding.referralCodeTextInputLayout.setVisibility(View.GONE);
            binding.referralCodeShopTextInputLayout.setVisibility(View.GONE);

            binding.userEmailIdEditText.setEnabled(false);
            binding.mobileEditText.setEnabled(false);

            getProfile();
        } else {
            setCustomToolBar("Registration", false, true);
            binding.signupTextView.setText("Registration");
            binding.genderSpinner.setVisibility(View.VISIBLE);
            binding.referralCodeTextInputLayout.setVisibility(View.VISIBLE);
            binding.referralCodeShopTextInputLayout.setVisibility(View.VISIBLE);
        }
        cartRelativeLayout.setVisibility(View.GONE);

        String deviceUniqueIdentifier = null;
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (null != tm) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                Log.e("deviceUniqueIdentifier", "=" + deviceUniqueIdentifier);
                return;
            }
            deviceUniqueIdentifier = tm.getDeviceId();
        }

        if (null == deviceUniqueIdentifier || 0 == deviceUniqueIdentifier.length()) {
            deviceUniqueIdentifier = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        SharedPref.getPrefsHelper().savePref(Constants.DEVICEID, deviceUniqueIdentifier);
    }

    private void getProfile() {
        try {
            JSONObject object = new JSONObject();
            object.put("user_id", userBean.getId());
            showDialog(this);
            RestApiManager.getProfile(MakeParamsHandler.getRequestBody(object.toString()), this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.signupTextView:
                if (type.equalsIgnoreCase("profile")) {
                    profileUpdate();
                } else {
                    registerUser();
                }
                break;
            case R.id.imgRelativeLayout:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 2);
                } else {
                    File file = CommonUtils.createImageFile();
                    fileUri = Uri.fromFile(file);
                    CommonUtils.selectFileDialogFromUri(this, file);
                }
                break;
            case R.id.dobTextView:
                int mYear, mMonth, mDay, mHour, mMinute;
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                binding.dobTextView.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
                break;
            case R.id.anniversaryTextView:
                int mYear1, mMonth1, mDay1, mHour1, mMinute1;
                final Calendar c1 = Calendar.getInstance();
                mYear1 = c1.get(Calendar.YEAR);
                mMonth1 = c1.get(Calendar.MONTH);
                mDay1 = c1.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog1 = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                binding.anniversaryTextView.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, mYear1, mMonth1, mDay1);
                datePickerDialog1.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog1.show();
                break;
        }
    }

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
                        binding.logoImageView.setImageURI(Uri.parse(pathImg));
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
                binding.logoImageView.setImageURI(Uri.parse(pathImg));
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled file selection", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public static String getImageFilePathToByteArray(String filePath) {
        String encodeString = null;
        try {
            Bitmap bm = BitmapFactory.decodeFile(filePath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
            byte[] byteArrayImage = baos.toByteArray();
            encodeString = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encodeString;
    }

    private void profileUpdate() {
        String firstName = binding.firstNameEditText.getText().toString().trim();
        if (firstName.equalsIgnoreCase("")) {
            Toast.makeText(this, "Name is not empty", Toast.LENGTH_SHORT).show();
            return;
        }
        showDialog(this);
        RestApiManager.updateProfile(MakeParamsHandler.getRequestBody(getMakeParameterProfile()), this, this);
    }


    private String getMakeParameterProfile() {
        JSONObject jsonObject = new JSONObject();
        String firstName = binding.firstNameEditText.getText().toString().trim();
        String address = binding.addressEditText.getText().toString().trim();
        String dob = binding.dobTextView.getText().toString().trim();
        String anniversary = binding.anniversaryTextView.getText().toString().trim();

        String firmImage = "";
        if (!pathImg.equalsIgnoreCase("NONE")) {
            firmImage = getImageFilePathToByteArray(pathImg);
            firmImage = "data:image/jpeg;base64," + firmImage;
            Log.e("64ImgStr=", "firmImage=" + firmImage);
        }
        Log.e("outside=", "outside=" + firmImage);

        try {
            jsonObject.put("user_id", userBean.getId());
            jsonObject.put("name", firstName);
            jsonObject.put("address", address);
            jsonObject.put("dob", dob);
            jsonObject.put("gender", gender);
            jsonObject.put("anniversary", anniversary);
            jsonObject.put("image", firmImage);

            if (SharedPref.getPrefsHelper().getPref("address") != null) {
                String address1 = SharedPref.getPrefsHelper().getPref("address").toString().trim();
                try {
                    JSONObject object1 = new JSONObject(address1);
                    jsonObject.put("place_name", object1.getString("name"));
                    jsonObject.put("lat", object1.getDouble("lat"));
                    jsonObject.put("long", object1.getDouble("lng"));
                    //  jsonObject.put("address", object1.getString("address"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                jsonObject.put("lat", "0");
                jsonObject.put("long", "0");
            }
            jsonObject.put("device_token", SharedPref.getPrefsHelper().getPref(Constants.DEVICETOKEN));
            Log.e("jsonObject=", "Update=" + jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    private boolean passwordLength() {
        boolean isValidate = true;
        String password = binding.passwordEditText.getText().toString().trim();
        if (password.length() <= 5) {
            isValidate = false;
            return isValidate;
        }
        return isValidate;
    }

    private void registerUser() {
        String firstName = binding.firstNameEditText.getText().toString().trim();
        String mobile = binding.mobileEditText.getText().toString().trim();
        String emailId = binding.userEmailIdEditText.getText().toString().trim();
        String password = binding.passwordEditText.getText().toString().trim();
        String address = binding.addressEditText.getText().toString().trim();
        if (firstName.equalsIgnoreCase("")) {
            Toast.makeText(this, "Please Enter Full Name*", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mobile.equalsIgnoreCase("") || mobile.length() < 10) {
            Toast.makeText(this, "Please Enter Valid Mobile number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (emailId.equalsIgnoreCase("") || !emailId.contains("@")) {
            Toast.makeText(this, "Please Enter Valid Email Address", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.equalsIgnoreCase("")) {
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }
        String confirmPass = binding.confirmPassEditText.getText().toString().trim();
        if (!confirmPass.equals(password)) {
            Toast.makeText(this, "Confirm password not match", Toast.LENGTH_SHORT).show();
            return;
        }
        if (passwordLength() == false) {
            //PopUpWindow.dError(this, "Password Length Should be minimum 6 characters");
            Toast.makeText(this, "Password Length Should be minimum 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }
        showDialog(this);
        RestApiManager.registration(MakeParamsHandler.getRequestBody(getMakeParameter()), this, this);
    }

    private String getMakeParameter() {
        JSONObject jsonObject = new JSONObject();
        String password = binding.passwordEditText.getText().toString().trim();
        String emailId = binding.userEmailIdEditText.getText().toString().trim();
        String mobile = binding.mobileEditText.getText().toString().trim();
        String firstName = binding.firstNameEditText.getText().toString().trim();
        String address = binding.addressEditText.getText().toString().trim();
        String firmImage = "";
        if (!pathImg.equalsIgnoreCase("NONE")) {
            firmImage = getImageFilePathToByteArray(pathImg);
            firmImage = "data:image/jpeg;base64," + firmImage;
            Log.e("64ImgStr=", "firmImage=" + firmImage);
        }
        String referralCode = binding.referralCodeEditText.getText().toString().trim();
        String referralCodeShop = binding.referralCodeShopEditText.getText().toString().trim();
        try {
            jsonObject.put("name", firstName);
            jsonObject.put("address", address);
            jsonObject.put("email", emailId);
            jsonObject.put("phone_no", mobile);
            jsonObject.put("password", password);
            jsonObject.put("image", firmImage);
            jsonObject.put("lat", "0");
            jsonObject.put("long", "0");
            jsonObject.put("referralCode", referralCode);
            jsonObject.put("referralCodeShop", referralCodeShop);
            jsonObject.put("device_token", SharedPref.getPrefsHelper().getPref(Constants.DEVICETOKEN));
            Log.e("jsonObject=", "Regis=" + jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @Override
    public void regisSuccess(String msg, String user_id) {
        dismissDialog();
        Toast.makeText(this, "" + msg, Toast.LENGTH_SHORT).show();
        CustomDialog customDialog = new CustomDialog(this, "otp", user_id,themeColor());
        customDialog.show();
    }

    @Override
    public void updateProfileSuccess(String msg) {
        dismissDialog();
        Toast.makeText(this, "" + msg, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void regisFailure(String failedResp) {
        dismissDialog();
        Toast.makeText(this, "" + failedResp, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void successGetProfile(UserProfileBean bean) {
        dismissDialog();
        int pos = 0;
        for (int i = 0; i < stringsList.size(); i++) {
            if (bean.getGender() != null && stringsList.get(i).equalsIgnoreCase(bean.getGender().trim())) {
                pos = i;
                break;
            }
        }
        binding.genderSpinner.setSelection(pos);
        binding.firstNameEditText.setText((bean.getName() == null ? "" : bean.getName()));
        binding.addressEditText.setText((bean.getAddress() == null ? "" : bean.getAddress()));
        binding.mobileEditText.setText((bean.getPhoneNo() == null ? "" : bean.getPhoneNo()));
        binding.dobTextView.setText((bean.getdOB() == null ? "" : bean.getdOB()));
        binding.anniversaryTextView.setText((bean.getAnniversary() == null ? "" : bean.getAnniversary()));
        binding.userEmailIdEditText.setText((bean.getEmail() == null ? "" : bean.getEmail()));
        if (bean.getImage() != null && !bean.getImage().trim().equalsIgnoreCase("")) {
            imageLoader.displayImage(bean.getImage(), binding.logoImageView);
        }
    }

    @Override
    public void failure(String error) {
        dismissDialog();
    }
}
