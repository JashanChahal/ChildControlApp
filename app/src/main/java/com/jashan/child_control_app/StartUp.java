package com.jashan.child_control_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.jashan.child_control_app.common.Login;
import com.jashan.child_control_app.common.Register;
import com.jashan.child_control_app.utils.ActivityTransition;

public class StartUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);

    }

    public void goToLogin(View view) {
        ActivityTransition.goToActivity(this, Login.class);
    }

    public void goToRegister(View view) {
        ActivityTransition.goToActivity(this, Register.class);
    }
}