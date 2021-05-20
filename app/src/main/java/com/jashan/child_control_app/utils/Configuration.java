package com.jashan.child_control_app.utils;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.jashan.child_control_app.repository.FirebaseMessagingService;
import com.jashan.child_control_app.repository.FirebaseWebService;
import com.jashan.child_control_app.repository.NotificationService;
import com.jashan.child_control_app.repository.WebService;

public class Configuration {
    private static int SPLASH_SCREEN_TIME = 2000;
    private static WebService WEB_SERVICE = new FirebaseWebService();
    private static String SHARED_PREFERENCE_FILE_LOCATION = "com.jashan.users";
    private static NotificationService NOTIFICATION_SERVICE = new FirebaseMessagingService();

    public static WebService getWebservice() {
        return WEB_SERVICE;
    }


    public static NotificationService getNotificationService() {
        return NOTIFICATION_SERVICE;
    }

    public static int getSplashScreenTime() {
        return SPLASH_SCREEN_TIME;
    }

    public static String getSharedPreferenceFileLocation() {
        return SHARED_PREFERENCE_FILE_LOCATION;
    }
    public static void openMap(Context context, String latitude, String longitude) {
        String location = "geo:0,0?q="+latitude+","+longitude+"?z=20";
        Uri gmmIntentUri = Uri.parse(location);
        Log.d("Map",location);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW,gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(mapIntent);
        }
    }

    public static void registerActivityToBroadCastManager(Activity context, BroadcastReceiver broadcastReceiver, String intentFilter) {
        LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceiver,
                new IntentFilter(intentFilter));

    }
}
