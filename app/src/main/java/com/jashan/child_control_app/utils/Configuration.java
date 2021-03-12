package com.jashan.child_control_app.utils;

import com.jashan.child_control_app.repository.FirebaseWebService;
import com.jashan.child_control_app.repository.WebService;

public class Configuration {
    private static int SPLASH_SCREEN_TIME = 2000;
    private static WebService WEB_SERVICE = new FirebaseWebService();
    private static String SHARED_PREFERENCE_FILE_LOCATION = "com.jashan.users";

    public static WebService getWebservice() {
        return WEB_SERVICE;
    }

    public  static  int getSplashScreenTime() {
        return  SPLASH_SCREEN_TIME;
    }

    public static  String getSharedPreferenceFileLocation() {
        return SHARED_PREFERENCE_FILE_LOCATION;
    }
}
