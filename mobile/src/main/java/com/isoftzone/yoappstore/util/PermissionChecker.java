package com.isoftzone.yoappstore.util;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;


import com.isoftzone.yoappstore.activity.DashBoardActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Admin on 06-10-2017.
 */

public class PermissionChecker {
    AppCompatActivity appCompatActivity;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 100;
    private String TAG = "tag";

    public PermissionChecker(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }

    public boolean checkAndRequestPermissions() {

        int camerapermission = ContextCompat.checkSelfPermission(appCompatActivity, android.Manifest.permission.READ_PHONE_STATE);
        int writepermission = ContextCompat.checkSelfPermission(appCompatActivity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionLocation = ContextCompat.checkSelfPermission(appCompatActivity, android.Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionRecordAudio = ContextCompat.checkSelfPermission(appCompatActivity, android.Manifest.permission.READ_EXTERNAL_STORAGE);


        int VIBRATE = ContextCompat.checkSelfPermission(appCompatActivity, android.Manifest.permission.VIBRATE);
        int SYSTEM_ALERT_WINDOW = ContextCompat.checkSelfPermission(appCompatActivity, android.Manifest.permission.SYSTEM_ALERT_WINDOW);
        int WAKE_LOCK = ContextCompat.checkSelfPermission(appCompatActivity, android.Manifest.permission.WAKE_LOCK);
        int CAMERA = ContextCompat.checkSelfPermission(appCompatActivity, android.Manifest.permission.CAMERA);
        int ACCESS_NETWORK_STATE = ContextCompat.checkSelfPermission(appCompatActivity, android.Manifest.permission.ACCESS_NETWORK_STATE);
        int ACCESS_WIFI_STATE = ContextCompat.checkSelfPermission(appCompatActivity, android.Manifest.permission.ACCESS_WIFI_STATE);
        int WRITE_SETTINGS = ContextCompat.checkSelfPermission(appCompatActivity, android.Manifest.permission.WRITE_SETTINGS);
        int CALL_PHONE = ContextCompat.checkSelfPermission(appCompatActivity, android.Manifest.permission.CALL_PHONE);

        int BLUETOOTH = ContextCompat.checkSelfPermission(appCompatActivity, android.Manifest.permission.BLUETOOTH);
        int BLUETOOTH_ADMIN = ContextCompat.checkSelfPermission(appCompatActivity, android.Manifest.permission.BLUETOOTH_ADMIN);


        List<String> listPermissionsNeeded = new ArrayList<>();

        if (camerapermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_PHONE_STATE);
        }
        if (writepermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (permissionRecordAudio != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (BLUETOOTH != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.BLUETOOTH);
        }
        if (BLUETOOTH_ADMIN != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.BLUETOOTH_ADMIN);
        }

        //////
        if (VIBRATE != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.VIBRATE);
        }
        if (SYSTEM_ALERT_WINDOW != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.SYSTEM_ALERT_WINDOW);
        }
        if (WAKE_LOCK != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WAKE_LOCK);
        }
        if (CAMERA != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        }
        if (ACCESS_NETWORK_STATE != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_NETWORK_STATE);
        }
        if (ACCESS_WIFI_STATE != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_WIFI_STATE);
        }
        if (WRITE_SETTINGS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_SETTINGS);
        }
        if (CALL_PHONE != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CALL_PHONE);
        }


        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(appCompatActivity, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                int VIBRATE = ContextCompat.checkSelfPermission(appCompatActivity, android.Manifest.permission.VIBRATE);
                int SYSTEM_ALERT_WINDOW = ContextCompat.checkSelfPermission(appCompatActivity, android.Manifest.permission.SYSTEM_ALERT_WINDOW);
                int WAKE_LOCK = ContextCompat.checkSelfPermission(appCompatActivity, android.Manifest.permission.WAKE_LOCK);

                int CAMERA = ContextCompat.checkSelfPermission(appCompatActivity, android.Manifest.permission.CAMERA);
                int ACCESS_NETWORK_STATE = ContextCompat.checkSelfPermission(appCompatActivity, android.Manifest.permission.ACCESS_NETWORK_STATE);
                int ACCESS_WIFI_STATE = ContextCompat.checkSelfPermission(appCompatActivity, android.Manifest.permission.ACCESS_WIFI_STATE);
                int WRITE_SETTINGS = ContextCompat.checkSelfPermission(appCompatActivity, android.Manifest.permission.WRITE_SETTINGS);
                int CALL_PHONE = ContextCompat.checkSelfPermission(appCompatActivity, android.Manifest.permission.CALL_PHONE);


                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(android.Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

         /*       perms.put(android.Manifest.permission.VIBRATE, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.SYSTEM_ALERT_WINDOW, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.WAKE_LOCK, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.ACCESS_NETWORK_STATE, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.ACCESS_WIFI_STATE, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.WRITE_SETTINGS, PackageManager.PERMISSION_GRANTED);
*/
                perms.put(android.Manifest.permission.BLUETOOTH, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.BLUETOOTH_ADMIN, PackageManager.PERMISSION_GRANTED);


                perms.put(android.Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.CALL_PHONE, PackageManager.PERMISSION_GRANTED);


                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

                     /*       && perms.get(android.Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.SYSTEM_ALERT_WINDOW) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.WAKE_LOCK) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED
                            */

                            && perms.get(android.Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED

                            && perms.get(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "sms & location services permission granted");
                        // process the normal flow

                        otherScreenSend();

                        //else any one or both the permissions are not granted
                    } else {
                        Log.d(TAG, "Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(appCompatActivity, android.Manifest.permission.READ_PHONE_STATE)
                                || ActivityCompat.shouldShowRequestPermissionRationale(appCompatActivity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                || ActivityCompat.shouldShowRequestPermissionRationale(appCompatActivity, android.Manifest.permission.ACCESS_FINE_LOCATION)
                                || ActivityCompat.shouldShowRequestPermissionRationale(appCompatActivity, android.Manifest.permission.READ_EXTERNAL_STORAGE)

                                //    || ActivityCompat.shouldShowRequestPermissionRationale(appCompatActivity, android.Manifest.permission.VIBRATE)
                                //    || ActivityCompat.shouldShowRequestPermissionRationale(appCompatActivity, android.Manifest.permission.SYSTEM_ALERT_WINDOW)
                                //    || ActivityCompat.shouldShowRequestPermissionRationale(appCompatActivity, android.Manifest.permission.WAKE_LOCK)
                                //    || ActivityCompat.shouldShowRequestPermissionRationale(appCompatActivity, android.Manifest.permission.ACCESS_NETWORK_STATE)
                                //   || ActivityCompat.shouldShowRequestPermissionRationale(appCompatActivity, android.Manifest.permission.ACCESS_WIFI_STATE)
                                //     || ActivityCompat.shouldShowRequestPermissionRationale(appCompatActivity, android.Manifest.permission.WRITE_SETTINGS)

                                || ActivityCompat.shouldShowRequestPermissionRationale(appCompatActivity, android.Manifest.permission.BLUETOOTH)
                                || ActivityCompat.shouldShowRequestPermissionRationale(appCompatActivity, android.Manifest.permission.BLUETOOTH_ADMIN)


                                || ActivityCompat.shouldShowRequestPermissionRationale(appCompatActivity, android.Manifest.permission.CAMERA)
                                || ActivityCompat.shouldShowRequestPermissionRationale(appCompatActivity, android.Manifest.permission.CALL_PHONE)) {
                            showDialogOK("Service Permissions are required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    appCompatActivity.finish();
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            //  explain("You need to give some mandatory permissions to continue. Do you want to go to app settings?");
                            //proceed with logic by disabling the related features or quit the app.
                            otherScreenSend();
                        }
                    }
                }
            }
        }
    }


    private void otherScreenSend() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            /*    if (SharedPref.getPrefsHelper().getPref("user") != null && !SharedPref.getPrefsHelper().getPref("user").toString().trim().equalsIgnoreCase("")) {
                    appCompatActivity.startActivity(new Intent(appCompatActivity, DashBoardActivity.class));
                    appCompatActivity.finish();
                } else {
                    appCompatActivity.startActivity(new Intent(appCompatActivity, LoginActivity.class));
                    appCompatActivity.finish();
                }*/
                appCompatActivity.startActivity(new Intent(appCompatActivity, DashBoardActivity.class));
                appCompatActivity.finish();
            }
        }, 1000);
    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(appCompatActivity)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }


}
