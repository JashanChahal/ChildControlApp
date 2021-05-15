package com.jashan.child_control_app.activities.parent;

import android.content.Context;

import com.jashan.child_control_app.repository.NotificationService;
import com.jashan.child_control_app.utils.Configuration;

import org.json.JSONObject;

public class RequestService {
    private static NotificationService notificationService = Configuration.getNotificationService();;

    static void requestLocation(Context context){
        notificationService.send("LL", "", new JSONObject(),context);
    }

    static void requestScreenShot(Context context){
        notificationService.send("SS", "", new JSONObject(),context);
    }

    static void appUsage(Context context){
        notificationService.send("AU", "", new JSONObject(),context);
    }
}
