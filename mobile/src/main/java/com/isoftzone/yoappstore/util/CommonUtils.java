package com.isoftzone.yoappstore.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


import java.util.Date;

/**
 * Created by Admin on 24-08-2017.
 */

public class CommonUtils {
    public static final String FINAL_IMAGE_PATH = Environment
            .getExternalStorageDirectory().getPath() + "/SelectedImageTraxMobile";
    public static int GALLERY = 1, CAMERA = 2;

    public static File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new
                Date());
        File file = new File(FINAL_IMAGE_PATH, "IMG_" + timeStamp + ".jpg");

   /*     if (!file.isDirectory()) {
            file.mkdir();
        }*/

        return file;
    }

    public static File createPdfFileFile(String fileName) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File file = new File(FINAL_IMAGE_PATH, fileName);
        return file;
    }


    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static String getDeviceId(Context context) {
        String android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return android_id;
    }


    public static void getCameraImage(AppCompatActivity appCompatActivity) {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            appCompatActivity.startActivityForResult(intent, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> getDates(String dateString1, String dateString2) {
        ArrayList<String> dates = new ArrayList<String>();
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");

        Date date1 = null;
        Date date2 = null;

        try {
            date1 = df1.parse(dateString1);
            date2 = df1.parse(dateString2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        while (!cal1.after(cal2)) {
            dates.add(DateUtils.stringFromDate(cal1.getTime()));
            cal1.add(Calendar.DATE, 1);
        }
        return dates;
    }

    public static Bitmap decodeBase64ToBitmap(String imageString) {


        String arr[] = imageString.split(",");

        byte imageBytes[] = Base64.decode(arr[1], Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

        return decodedImage;
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

    public static void showPictureDialog(final AppCompatActivity appCompatActivity) {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(appCompatActivity);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary(appCompatActivity);
                                break;
                            case 1:
                                takePhotoFromCamera(appCompatActivity);
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public static Bitmap getBitmapFromPath(String imgPath) {
        Bitmap bitmap = null;
        File imgFile = new File(imgPath);
        if (imgFile.exists()) {
            bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        }
        return bitmap;
    }

    public static void choosePhotoFromGallary(AppCompatActivity appCompatActivity) {
        try {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            appCompatActivity.startActivityForResult(galleryIntent, GALLERY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void takePhotoFromCamera(AppCompatActivity appCompatActivity) {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            appCompatActivity.startActivityForResult(intent, CAMERA);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void selectFileDialog(final AppCompatActivity activity, final File file) {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(activity);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select File",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary(activity);
                                break;
                            case 1:
                                //   takePhotoFromCameraFragment(fragment);
                                startCamera(activity, file);
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }


    protected static void startCamera(AppCompatActivity appCompatActivity, File file) {
        try {

           /* Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 23);
            */


            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            //  File file = CommonUtils.createImageFile();
            //   fileUri = Uri.fromFile(file);
            boolean isDirectoryCreated = file.getParentFile().mkdirs();
            Log.e("isDirectoryCreated", "openCamera: : " + isDirectoryCreated);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri tempFileUri = FileProvider.getUriForFile(appCompatActivity.getApplicationContext(),
                        appCompatActivity.getPackageName() + ".provider", // As defined in Manifest
                        file);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempFileUri);
            } else {
                Uri tempFileUri = Uri.fromFile(file);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempFileUri);
            }
            appCompatActivity.startActivityForResult(cameraIntent, CAMERA);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void selectFileDialogFromUri(final AppCompatActivity appCompatActivity, final File file) {
        PhotosSheetDialog photosSheetDialog = new PhotosSheetDialog(appCompatActivity) {
            @Override
            protected void onCamera() {
                startCamera(appCompatActivity, file);
            }
            @Override
            protected void onGallery() {
                choosePhotoFromGallary(appCompatActivity);
            }
        };
        photosSheetDialog.show();
/*

        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(appCompatActivity);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select File",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary(appCompatActivity);
                                break;
                            case 1:
                                startCamera(appCompatActivity, file);
                                break;
                        }
                    }
                });
        pictureDialog.show();*/
    }

    public static String saveImage(Bitmap myBitmap, Context context) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + FINAL_IMAGE_PATH);
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }
        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(context,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.e("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }


    public static String saveImageReturnPath(Bitmap myBitmap, Context context, String fileName) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File f = null;
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + FINAL_IMAGE_PATH);
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }
        try {
            f = new File(wallpaperDirectory, fileName + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(context, new String[]{f.getPath()}, new String[]{"image/jpeg"}, null);
            fo.close();
            Log.e("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return f.getPath();
    }


    public static void shareBitMapImage(Context context, Bitmap bitmap) {
        String pathofBmp = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "TraxMobile", null);
        Uri bmpUri = Uri.parse(pathofBmp);
        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        intent.setType("image/*");
        context.startActivity(Intent.createChooser(intent, "Share Image Using"));
    }

    // Share text
    public static void shareText(Context context, String text) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");// Plain format text
        sharingIntent.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(Intent.createChooser(sharingIntent, "Share Text Using"));
    }

    public static void sentEmail(Context context, String data) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL, "Enter E-mail");
        intent.putExtra(Intent.EXTRA_SUBJECT, "TraxMobile Doc");
        intent.putExtra(Intent.EXTRA_TEXT, data);
        context.startActivity(Intent.createChooser(intent, "Send Email"));
    }

    public static void sentEmailWithAttachements(Context context, File data) {

        Uri path = Uri.fromFile(data);
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("vnd.android.cursor.dir/email");
        String to[] = {"asd@gmail.com"};
        emailIntent.putExtra(Intent.EXTRA_EMAIL, "Enter E-mail");
        emailIntent.putExtra(Intent.EXTRA_STREAM, path);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        context.startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }


    public static void openFile(Context context, String url) {
        //  File file=url;
        //  Uri uri = Uri.fromFile(file);

        Uri uri = Uri.parse(url);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
            // Word document
            intent.setDataAndType(uri, "application/msword");
        } else if (url.toString().contains(".pdf")) {
            // PDF file
            intent.setDataAndType(uri, "application/pdf");
        } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
            // Powerpoint file
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
            // Excel file
            intent.setDataAndType(uri, "application/vnd.ms-excel");
        } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
            // WAV audio file
            intent.setDataAndType(uri, "application/x-wav");
        } else if (url.toString().contains(".rtf")) {
            // RTF file
            intent.setDataAndType(uri, "application/rtf");
        } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
            // WAV audio file
            intent.setDataAndType(uri, "audio/x-wav");
        } else if (url.toString().contains(".gif")) {
            // GIF file
            intent.setDataAndType(uri, "image/gif");
        } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
            // JPG file
            intent.setDataAndType(uri, "image/jpeg");
        } else if (url.toString().contains(".txt")) {
            // Text file
            intent.setDataAndType(uri, "text/plain");
        } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
            // Video files
            intent.setDataAndType(uri, "video/*");
        } else {
            intent.setDataAndType(uri, "*/*");
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
