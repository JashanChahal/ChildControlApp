package com.jashan.child_control_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ProgressBar;

import com.jashan.child_control_app.R;
import com.jashan.child_control_app.model.User;
import com.jashan.child_control_app.repository.AfterCompletion;
import com.jashan.child_control_app.repository.WebService;
import com.jashan.child_control_app.utils.ActivityTransition;
import com.jashan.child_control_app.utils.Configuration;

/*
* Activity visible to user at the start of the Application
*/

public class SplashScreen extends AppCompatActivity {
    private WebService webService;
    private SharedPreferences pref;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        webService = Configuration.getWebservice();
        pref = getSharedPreferences(Configuration.getSharedPreferenceFileLocation(), MODE_PRIVATE);
        progressBar = findViewById(R.id.progressBar3);

        transitionToMainAppPageAfter(Configuration.getSplashScreenTime());

    }

    private void transitionToMainAppPageAfter(int time) {
        new Handler(Looper.getMainLooper()).postDelayed(this::goToMainAppActivity, time);
    }

    private void goToMainAppActivity() {
        webService.getCurrentUserAndDo(new AfterCompletion<User>() {
            @Override
            public void onSuccess(User user) {
                // User exist. Go to Homepage
                ActivityTransition.goToUserHomepage(SplashScreen.this, user, pref, progressBar);
            }

            @Override
            public void onFailure(Exception e) {
                // User does not exist. Go To startup screen for authentication
                ActivityTransition.goToActivity(SplashScreen.this, StartUp.class);
            }
        });
    }

}


