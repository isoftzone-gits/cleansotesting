package com.isoftzone.yoappstore.util;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by TBW Android 1 on 3/6/2017.
 */
public class InternetDedecter {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
