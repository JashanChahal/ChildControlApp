package com.jashan.child_control_app.repository;

import android.content.Context;

import org.json.JSONObject;

public interface NotificationService {

    void subscribeToNotificationTopic( String topicName);

    void send(String title, String message, JSONObject notificationBody, Context context);
}
