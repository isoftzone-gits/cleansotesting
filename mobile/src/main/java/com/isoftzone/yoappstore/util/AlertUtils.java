package com.isoftzone.yoappstore.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.ProgressBar;

import com.isoftzone.yoappstore.R;


public class AlertUtils {

    private static ProgressBar bar;

    public static void withSingleButton(Context _context,
                                        String title,
                                        String message,
                                        DialogInterface.OnClickListener onYesListener, String positiveText) {
        new AlertDialog.Builder(_context, R.style.AlertDialogTheme)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveText, onYesListener)
                .show();
    }


    public static void withSingleButtonSuccess(Context _context,
                                               String title,
                                               String message,
                                               DialogInterface.OnClickListener onYesListener, String positiveText) {
        new AlertDialog.Builder(_context, R.style.AlertDialogTheme)
                .setTitle(title)
                .setMessage(message)
                .setIcon(R.drawable.success)
                .setPositiveButton(positiveText, onYesListener)
                .show();
    }

    public static AlertDialog withSingleButtonReturnObj(Context _context,
                                                        String title,
                                                        String message,
                                                        DialogInterface.OnClickListener onYesListener, String positiveText) {
        AlertDialog alertDialog = new AlertDialog.Builder(_context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveText, onYesListener)
                .show();
        return alertDialog;
    }

    public static void withTwoButtons(Context _context,
                                      String title,
                                      String message,
                                      DialogInterface.OnClickListener onYesListener, String positiveText,
                                      DialogInterface.OnClickListener onNoListener, String negativeText) {


        new AlertDialog.Builder(_context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveText, onYesListener)
                .setNegativeButton(negativeText, onNoListener)
                .show();
    }


    public static void showProgressDialog(Context context) {

        bar = new ProgressBar(context, null, android.R.attr.progressBarStyleSmall);
        bar.setIndeterminate(true);
        bar.setClickable(false);
        bar.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
        bar.animate();
    }

    public static void hideProgressDialog() {
        if (bar != null) {
            bar.invalidate();
        }
    }

}
