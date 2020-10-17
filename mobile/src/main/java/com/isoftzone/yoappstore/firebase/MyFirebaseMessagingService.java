package com.isoftzone.yoappstore.firebase;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.isoftzone.yoappstore.R;
import com.isoftzone.yoappstore.activity.SplashActivity;

import com.isoftzone.yoappstore.network.Constants;
import com.isoftzone.yoappstore.util.DateUtils;
import com.isoftzone.yoappstore.util.MainApplication;
import com.isoftzone.yoappstore.util.SharedPref;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onNewToken(@NonNull String refreshedToken) {
        super.onNewToken(refreshedToken);

        Log.e(TAG, "Refreshed token: " + refreshedToken);

        SharedPref.getPrefsHelper().savePref(Constants.DEVICETOKEN, refreshedToken);
        //Displaying token on logcat


    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.e("remoteMessageONLYMSG", "remoteMessage==" + remoteMessage);

        Log.e("remoteMessageONLYURL", "getData=" + remoteMessage.getData());

        Log.e("remoteMessageONLYURL", "message" + remoteMessage.getData().get("message"));

        String type = remoteMessage.getData().get("type");
        String title = remoteMessage.getData().get("title");
        String imageUrl = remoteMessage.getData().get("image");
        String msgData = remoteMessage.getData().get("message");

        Log.e("remoteMessageONLYMSG", "remoteMessage" + msgData);

        // {type=announcment, image=http://yoappstore.com/login/asset/uploads/ic_backtrax.png, title=Announcement title, message=testmsg}

        createNotification(type, title, msgData, imageUrl);

    /*    String CHANNEL_ID = "channel_idUnique";
        CharSequence channelName = "Some Channel";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //  CharSequence name = getString(R.string.channel_name);
            String description = msgData;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            channel.setDescription(description);

            //  channel.enableVibration(true);
            // channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});


            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }


        Intent intent = new Intent(this, UpcomingOrdersActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.launcher_iconapp)
                .setContentTitle(msgData)
                .setContentText(msgData)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(222, builder.build());

*/
    }

    private void createNotification(String type, String title, String msg, String imageUrl) {
        //  NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        String CHANNEL_ID = "my_channel_01";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);

        Intent intent = null;

        Log.e("MainApplication", "==" + MainApplication.appStatus);

        if (MainApplication.appStatus.equalsIgnoreCase("Open")) {
            intent = new Intent("com.trax.firebase.notification");
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        } else {
            intent = new Intent(this, SplashActivity.class);
            intent.putExtra("isNotification", true);
            intent.putExtra("type", type);
            intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        }


        PendingIntent intentPending = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(intentPending);
        builder.setTicker(type);
        // Sets the small icon for the ticker
        builder.setSmallIcon(R.drawable.launcher_iconapp);
      /*  builder.setDefaults(
                Notification.DEFAULT_SOUND
                        | Notification.DEFAULT_VIBRATE
                        | Notification.DEFAULT_LIGHTS
        );*/
        // END_INCLUDE(ticker)
        // BEGIN_INCLUDE(buildNotification)
        // Cancel the notification when clicked
        builder.setAutoCancel(true);

        // Build the notification
        Notification notification = builder.build();

        // END_INCLUDE(buildNotification)

        // BEGIN_INCLUDE(customLayout)
        // Inflate the notification layout as RemoteViews
        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.custom_notificationxml);

        // Set text on a TextView in the RemoteViews programmatically.
        final String time = DateFormat.getTimeInstance().format(new Date()).toString();
        String datetime = DateUtils.getCurrentDateTimeDDMMYYYY();

        Date date = new Date();
        int dateint = date.getDate();
        int dayint = date.getDay();
        int hoursint = date.getHours();
        int minsint = date.getMinutes();
        int secndsint = date.getSeconds();
        secndsint = dateint + hoursint + minsint + secndsint;
        Log.e(TAG, "timefornotifuniq: " + secndsint);
        //  final String text = getResources().getString(msg, time);

        contentView.setTextViewText(R.id.msgtv, msg);
        contentView.setTextViewText(R.id.text_viewtitle, title);

        contentView.setTextViewText(R.id.dateTextView, datetime);

        //  contentView.setImageViewUri(R.id.imageview,Uri.parse(url1));

        InputStream in;
        Bitmap myBitmap = null;
        if (!imageUrl.equalsIgnoreCase("")) {
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                myBitmap = BitmapFactory.decodeStream(in);
                contentView.setImageViewBitmap(R.id.imageview, myBitmap);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        notification.contentView = contentView;

        // Add a big content view to the notification if supported.
        // Support for expanded notifications was added in API level 16.
        // (The normal contentView is shown when the notification is collapsed, when expanded the
        // big content view set here is displayed.)
        if (Build.VERSION.SDK_INT >= 16) {
            // Inflate and set the layout for the expanded notification view
            RemoteViews expandedView = new RemoteViews(getPackageName(), R.layout.custom_notificationxml);

            expandedView.setTextViewText(R.id.msgtv, msg);
            expandedView.setTextViewText(R.id.text_viewtitle, title);
            expandedView.setTextViewText(R.id.dateTextView, datetime);

            InputStream in1;
            Bitmap myBitmap1 = null;
            if (!imageUrl.equalsIgnoreCase("")) {
                try {
                    URL url3 = new URL(imageUrl);
                    HttpURLConnection connection1 = (HttpURLConnection) url3.openConnection();
                    connection1.setDoInput(true);
                    connection1.connect();
                    in1 = connection1.getInputStream();
                    myBitmap1 = BitmapFactory.decodeStream(in1);
                    expandedView.setImageViewBitmap(R.id.imageview, myBitmap1);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            notification.bigContentView = expandedView;
        }


        // END_INCLUDE(customLayout)

        //  notification.flags |= Notification.FLAG_NO_CLEAR; //Do not clear the notification
        notification.flags |= Notification.DEFAULT_LIGHTS; // LED
        // notification.defaults |= Notification.DEFAULT_VIBRATE; //Vibration
        //  notification.flags |= Notification.DEFAULT_SOUND; // Sound
        //   notification.defaults |= Notification.PRIORITY_LOW; //Do not clear the notification   PRIORITY_HIGH
        // START_INCLUDE(notify)
        // Use the NotificationManager to show the notification
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //  nm.notify(secndsint, notification);

     /*   if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);

            builder.setChannelId(CHANNEL_ID);
            nm.createNotificationChannel(notificationChannel);
        }*/

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            CharSequence name = "my_channel";
            String Description = "This is yoappstore";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            //mChannel.setSound(null,null);
         /*   mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);*/
            nm.createNotificationChannel(mChannel);
        }


        nm.notify(secndsint, notification);


        // END_INCLUDE(notify)
    }


}

