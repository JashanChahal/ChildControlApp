package com.jashan.child_control_app.repository;

import android.content.Context;

public interface NotificationService {

    void subscribeToNotificationTopic( String topicName);

    void send(String s, Context context);
}
