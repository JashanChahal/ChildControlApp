package com.jashan.child_control_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.jashan.child_control_app.common.Login;

public class StartUp extends AppCompatActivity {
    private boolean isLoggedIn = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);
//        if (isLoggedIn) {
//            Intent intent = new Intent(this,HomePageActivity.class);
//            startActivity(intent);
//        } else {
//            startActivity(new Intent(this, Login.class));
//        }
    }
}