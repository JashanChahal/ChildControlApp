package com.jashan.child_control_app.utils;

import android.app.Activity;
import android.content.Intent;

import com.jashan.child_control_app.R;

public class ActivityTransition {

    public static void goToActivity(Activity context, Class activityClass) {
        Intent intent = new Intent(context, activityClass);
        context.startActivity(intent);
        context.overridePendingTransition(R.transition.enter,R.transition.exit);
    }
    public static void goBack(Activity context, Class activityClass) {
        Intent intent = new Intent(context, activityClass);
        context.startActivity(intent);
        context.overridePendingTransition(R.transition.enter_back,R.transition.back);
    }
}
