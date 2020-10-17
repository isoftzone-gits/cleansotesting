package com.isoftzone.yoappstore.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.graphics.drawable.DrawableCompat;

import com.isoftzone.yoappstore.R;
import com.isoftzone.yoappstore.baseactivity.BaseActivity;
import com.isoftzone.yoappstore.bean.CompanyDetails;
import com.isoftzone.yoappstore.network.CommonInterfaces;
import com.isoftzone.yoappstore.network.Constants;
import com.isoftzone.yoappstore.network.MakeParamsHandler;
import com.isoftzone.yoappstore.network.RestApiManager;
import com.isoftzone.yoappstore.util.CustomDialog;
import com.isoftzone.yoappstore.util.SharedPref;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends BaseActivity implements CommonInterfaces.LoginDelegate, View.OnClickListener {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private ProgressDialog dialog;
    boolean isComesFromSplash = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        config();
    }

    private void config() {

        TextView loginTextView = findViewById(R.id.loginTextView);
        Drawable backgroundDrawable = DrawableCompat.wrap(loginTextView.getBackground()).mutate();
        DrawableCompat.setTint(backgroundDrawable, themeColor());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(themeColor());
        }

        isComesFromSplash = getIntent().getBooleanExtra("isComesFromSplash", false);
        this.passwordEditText = findViewById(R.id.passwordEditText);
        this.usernameEditText = findViewById(R.id.usernameEditText);
        TextView skipTextView = findViewById(R.id.skipTextView);
        findViewById(R.id.forgetPasswordTextView).setOnClickListener(this);
        findViewById(R.id.loginTextView).setOnClickListener(this);
        findViewById(R.id.signupTextView).setOnClickListener(this);
        skipTextView.setOnClickListener(this);
        String deviceUniqueIdentifier = null;

        if (null == deviceUniqueIdentifier || 0 == deviceUniqueIdentifier.length()) {
            deviceUniqueIdentifier = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        }

        Log.e("deviceUniqueIdentifier", "=" + deviceUniqueIdentifier);

        SharedPref.getPrefsHelper().savePref(Constants.DEVICEID, deviceUniqueIdentifier);

        Log.e("getLogin_type", "=" + CompanyDetails.getInstance().getDetails().getLogin_type());

        if (CompanyDetails.getInstance().getDetails().getLogin_type().equalsIgnoreCase("2")) {
            skipTextView.setVisibility(View.VISIBLE);
        } else {
            skipTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginTextView:
                login();
                break;
            case R.id.signupTextView:
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class).putExtra("via", "login"));
                finish();
                break;
            case R.id.forgetPasswordTextView:
                CustomDialog customDialog = new CustomDialog(LoginActivity.this, Constants.FOR_FORGET_PASSWORD, Constants.TITLE_FORGET_PASSWORD, themeColor());
                customDialog.show();
                break;
            case R.id.skipTextView:
                startActivity(new Intent(LoginActivity.this, DashBoardActivity.class));
                finish();
                break;

        }
    }


    protected void showDialog(Context context) {
        dialog = new ProgressDialog(context);
        dialog.setTitle("Please wait...");
        dialog.show();
    }

    protected void dismissDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }


    private boolean validate() {
        boolean isValidate = true;

        String password = passwordEditText.getText().toString().trim();
        String userId = usernameEditText.getText().toString().trim();
        if (userId.equalsIgnoreCase("") || password.equalsIgnoreCase("")) {
            isValidate = false;
        }

        return isValidate;
    }

    private void login() {
        if (!validate()) {
            Toast.makeText(this, "All Fields are Mandatory", Toast.LENGTH_SHORT).show();
            return;
        }
        showDialog(this);
        RestApiManager.login(MakeParamsHandler.getRequestBody(getMakeParameter()), this, this);
    }

    private String getMakeParameter() {
        JSONObject jsonObject = new JSONObject();
        String password = passwordEditText.getText().toString().trim();
        String username = usernameEditText.getText().toString().trim();

        try {
            jsonObject.put("email", username);
            jsonObject.put("password", password);
            jsonObject.put("device_token", SharedPref.getPrefsHelper().getPref(Constants.DEVICETOKEN));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("Login parms=", "=" + jsonObject.toString());

        return jsonObject.toString();
    }

    @Override
    public void loginSuccess(JSONObject jsonObject) {
        dismissDialog();
        try {
            if (jsonObject.getString("is_active").equalsIgnoreCase("1")) {

                if (CompanyDetails.getInstance().getDetails().getLogin_type().equalsIgnoreCase("4")) {
                    if (jsonObject.getString("is_verified").equalsIgnoreCase("1")) {
                        SharedPref.getPrefsHelper().savePref("user", jsonObject.getJSONObject("user").toString());

                        if (getIntent().getBooleanExtra("isComesFromSplash", false)) {
                            startActivity(new Intent(this, DashBoardActivity.class));
                            finish();
                        } else {
                            finish();
                        }
                    } else {
                        Toast.makeText(this, "Your Profile is not verified yet ,Please Contact to administrator", Toast.LENGTH_LONG).show();
                    }
                } else {
                    SharedPref.getPrefsHelper().savePref("user", jsonObject.getJSONObject("user").toString());
                    if (getIntent().getBooleanExtra("isComesFromSplash", false)) {
                        startActivity(new Intent(this, DashBoardActivity.class));
                        finish();
                    } else {
                        finish();
                    }
                }

            } else {
                CustomDialog customDialog = new CustomDialog(this, "otp", jsonObject.getJSONObject("user").getString("id"), themeColor());
                customDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void loginFailure(String failedResp) {
        dismissDialog();
        Toast.makeText(this, "" + failedResp, Toast.LENGTH_SHORT).show();
    }
}
