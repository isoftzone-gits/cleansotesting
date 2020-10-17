package com.isoftzone.yoappstore.util;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;

import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.isoftzone.yoappstore.R;
import com.isoftzone.yoappstore.activity.LoginActivity;
import com.isoftzone.yoappstore.network.CommonInterfaces;
import com.isoftzone.yoappstore.network.Constants;
import com.isoftzone.yoappstore.network.MakeParamsHandler;
import com.isoftzone.yoappstore.network.RestApiManager;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Admin on 26-05-2017.
 */

public class CustomDialog extends Dialog implements View.OnClickListener,
        CommonInterfaces.ForgetPasswordDelegate,
        CommonInterfaces.changePasswordDelegate,
        CommonInterfaces.generateOtpByMobileNo {

    private String forWhich;
    private AppCompatActivity context;
    private String someData;
    private EditText oldPasswordEditText, otpEditText, newPassEditText, confirmPassEditText;
    private EditText mobileEditText;
    private int color;
    protected ProgressDialog progressDialog;
    private EditText passwordForgetdEditText, confirmPassForgetPassEditText;
    private TextView generateOtpTextView;
    private TextView checkOtpTextView;
    private TextView timeTextView;
    private LinearLayout otpSectionLinearLayout, passwordSectionLinearLayout;
    private int time = 90;
    private CountDownTimer countDownTimer;
    private  RelativeLayout topView;

    public CustomDialog(@NonNull AppCompatActivity context, String forWhich, String someData, int color) {
        super(context);
        this.forWhich = forWhich;
        this.context = context;
        this.someData = someData;
        this.color = color;
    }

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView resendOtpTextView;
        ImageView closeImageView;
        if (forWhich.equalsIgnoreCase("forServices")) {
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        } else if (forWhich.equalsIgnoreCase(Constants.FOR_FORGET_PASSWORD)) {
            this.setContentView(R.layout.forgot_password);
            TextView titleTextView = findViewById(R.id.titleTextView);
            closeImageView = findViewById(R.id.closeImageView);
            mobileEditText = findViewById(R.id.mobileEditText);
            generateOtpTextView = findViewById(R.id.generateOtpTextView);
            resendOtpTextView = findViewById(R.id.resendOtpTextView);
            timeTextView = findViewById(R.id.timeTextView);
            otpEditText = findViewById(R.id.otpEditText);
            checkOtpTextView = findViewById(R.id.checkOtpTextView);  // verify button
            TextView submitTextView = findViewById(R.id.submitTextView);
            otpSectionLinearLayout = findViewById(R.id.otpSectionLinearLayout);
            passwordSectionLinearLayout = findViewById(R.id.passwordSectionLinearLayout);
            passwordForgetdEditText = findViewById(R.id.passwordForgetdEditText);
            confirmPassForgetPassEditText = findViewById(R.id.confirmPassForgetPassEditText);
             topView = findViewById(R.id.topView);
            checkOtpTextView.setOnClickListener(this);
            generateOtpTextView.setOnClickListener(this);
            resendOtpTextView.setOnClickListener(this);
            closeImageView.setOnClickListener(this);
            submitTextView.setOnClickListener(this);
            titleTextView.setText(someData);
            setTheamColor(generateOtpTextView);
            setTheamColor(resendOtpTextView);
            setTheamColor(checkOtpTextView);
            setTheamColor(submitTextView);
            setTheamColor(topView);

        } else if (forWhich.equalsIgnoreCase("changePassword")) {
            this.setContentView(R.layout.change_password);
            closeImageView = findViewById(R.id.closeImageView);
            TextView changeTextView = findViewById(R.id.changeTextView);
            oldPasswordEditText = findViewById(R.id.oldPasswordEditText);
            newPassEditText = findViewById(R.id.newPassEditText);
            confirmPassEditText = findViewById(R.id.confirmPassEditText);
            closeImageView.setOnClickListener(this);
            changeTextView.setOnClickListener(this);
        } else if (forWhich.equalsIgnoreCase("otp")) {
            this.setContentView(R.layout.otp_verify);
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            int width = metrics.widthPixels;
            int height = metrics.heightPixels;
            getWindow().setLayout((5 * width) / 5, (5 * height) / 5);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            closeImageView = findViewById(R.id.closeImageView);
            TextView verifyOtpTextView = findViewById(R.id.verifyOtpTextView);
            otpEditText = findViewById(R.id.otpEditText);
            closeImageView.setOnClickListener(this);
            verifyOtpTextView.setOnClickListener(this);
            resendOtpTextView = findViewById(R.id.resendOtpTextView);
            timeTextView = findViewById(R.id.timeTextView);
            resendOtpTextView.setOnClickListener(this);
        }

    }

    private void setTheamColor(View view){
        Drawable backgroundDrawable = DrawableCompat.wrap(view.getBackground()).mutate();
        DrawableCompat.setTint(backgroundDrawable, color);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.closeImageView:
                dismiss();
                break;
            case R.id.submitTextView:
                update_password();
                break;
            case R.id.changeTextView:
                changePassword();
                break;
            case R.id.verifyOtpTextView:
                verifyOtp();
                break;
            case R.id.generateOtpTextView:
                generateOtpByMobileNo();
                break;
            case R.id.resendOtpTextView:
                generateOtpByMobileNo();
                break;
            case R.id.checkOtpTextView:
                verifyOtp();
                break;

        }

    }



    private void generateOtpByMobileNo() {
        String mobile = mobileEditText.getText().toString().trim();
        if (mobile.equalsIgnoreCase("") || mobile.length() < 10) {
            Toast.makeText(context, "Please Enter Valid Mobile number", Toast.LENGTH_SHORT).show();
            return;
        }
        time = 90;
        countDownTimer = new CountDownTimer(90000, 1000) {
            public void onTick(long millisUntilFinished) {
                timeTextView.setText(checkDigit(time));
                if (Integer.parseInt(checkDigit(time)) <= 0) {
                    countDownTimer.cancel();
                }
                time--;
            }
            public void onFinish() {
                timeTextView.setText("");
            }
        }.start();
        try {
            JSONObject object = new JSONObject();
            object.put("phone_no", mobile);

            showDialog(context, Constants.PLEASE_WAIT);
            RestApiManager.generateOtpByMobileNo(MakeParamsHandler.getRequestBody(object.toString()), context, this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

    private void verifyOtp() {
        String otp = otpEditText.getText().toString().trim();
        if (otp.equalsIgnoreCase("")) {
            Toast.makeText(context, "Please Enter OTP", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            JSONObject object = new JSONObject();
            object.put("user_id", someData);
            object.put("otp", otp);
            showDialog(context, Constants.PLEASE_WAIT);
            RestApiManager.verifyOtp(MakeParamsHandler.getRequestBody(object.toString()), context, this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void changePassword() {
        String oldPass = oldPasswordEditText.getText().toString().trim();
        String newPass = newPassEditText.getText().toString().trim();
        if (oldPass.equalsIgnoreCase("") || newPass.equalsIgnoreCase("")) {
            Toast.makeText(context, "All Fields are mandatory*", Toast.LENGTH_SHORT).show();
            return;
        }
        String confirmPass = confirmPassEditText.getText().toString().trim();
        if (!confirmPass.equals(newPass)) {
            Toast.makeText(context, "Confirm password not match", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            JSONObject object = new JSONObject();
            object.put("user_id", someData);
            object.put("old_password", oldPass);
            object.put("new_password", newPass);
            showDialog(context, Constants.PLEASE_WAIT);
            RestApiManager.changePassword(MakeParamsHandler.getRequestBody(object.toString()), context, this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void update_password() {
        String newPas = passwordForgetdEditText.getText().toString().trim();
        String confirmPas = confirmPassForgetPassEditText.getText().toString().trim();
        if (!newPas.equalsIgnoreCase(confirmPas)) {
            Toast.makeText(context, "Password not match", Toast.LENGTH_SHORT).show();
            return;
        }
        showDialog(context, Constants.PLEASE_WAIT);  // showing progress dialog for waiting
        RestApiManager.forgetPassword(MakeParamsHandler.getRequestBody(getMakeParameter()), context, this);
    }

    private String getMakeParameter() {
        JSONObject jsonObject = new JSONObject();
        String confirmPas = confirmPassForgetPassEditText.getText().toString().trim();
        try {
            jsonObject.put("user_id", someData);
            jsonObject.put("password", confirmPas);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
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

    @Override
    public void forgetSuccess(String resp) {
        dismissDialog();
        Toast.makeText(context, "" + resp, Toast.LENGTH_SHORT).show();
        dismiss();
    }

    @Override
    public void changePasswordSuccess(String resp) {
        dismissDialog();
        Toast.makeText(context, "" + resp, Toast.LENGTH_LONG).show();
        dismiss();
    }

    @Override
    public void verifyOtpResp(String resp) {
        dismissDialog();
        Toast.makeText(context, "" + resp, Toast.LENGTH_LONG).show();
        if (forWhich.equalsIgnoreCase(Constants.FOR_FORGET_PASSWORD)) {
            passwordSectionLinearLayout.setVisibility(View.VISIBLE);
            mobileEditText.setEnabled(false);
            generateOtpTextView.setEnabled(false);
            otpEditText.setEnabled(false);
            checkOtpTextView.setEnabled(false);
        } else {
            Toast.makeText(context, "Please Login", Toast.LENGTH_LONG).show();
            context.startActivity(new Intent(context, LoginActivity.class));
            context.finish();
            dismiss();
        }
    }


    @Override
    public void forgetFailure(String failedResp) {
        dismissDialog();
        Toast.makeText(context, "" + failedResp, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void successGenerateOtp(String user_id) {
        dismissDialog();
        Toast.makeText(context, "Please Enter OTP", Toast.LENGTH_SHORT).show();
        mobileEditText.setEnabled(false);
        generateOtpTextView.setEnabled(false);
        otpSectionLinearLayout.setVisibility(View.VISIBLE);
        this.someData = user_id;
    }

    @Override
    public void failure(String error) {
        dismissDialog();
        Toast.makeText(context, "" + error, Toast.LENGTH_SHORT).show();
    }

}
