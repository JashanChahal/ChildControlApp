package com.jashan.child_control_app.common;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.jashan.child_control_app.R;
import com.jashan.child_control_app.StartUp;
import com.jashan.child_control_app.utils.ActivityTransition;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    public void goBackToStartUp(View view) {
        ActivityTransition.goBack(this, StartUp.class);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.transition.enter_back,R.transition.back);
    }

    public void goToRegister(View view) {
            ActivityTransition.goToActivity(this,Register.class);
    }


}