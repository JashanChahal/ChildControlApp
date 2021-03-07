package com.jashan.child_control_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jashan.child_control_app.R;
import com.jashan.child_control_app.utils.ActivityTransition;

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_SCREEN = 2000;
    private FirebaseAuth auth;
    private SharedPreferences pref;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        auth = FirebaseAuth.getInstance();
        pref = getSharedPreferences("com.jashan.users", MODE_PRIVATE);
        progressBar = findViewById(R.id.progressBar3);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentUser = auth.getCurrentUser();
                if (currentUser != null) {
                    ActivityTransition.fetchUserDetailsAndGoToHomePage(SplashScreen.this,progressBar,pref);
                } else {
                    ActivityTransition.goToActivity(SplashScreen.this,StartUp.class);
                }

            }
        }, SPLASH_SCREEN);
    }


}