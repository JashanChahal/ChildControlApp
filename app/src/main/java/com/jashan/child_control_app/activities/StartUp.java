package com.jashan.child_control_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jashan.child_control_app.R;
import com.jashan.child_control_app.activities.authentication.Login;
import com.jashan.child_control_app.activities.authentication.Register;
import com.jashan.child_control_app.activities.parent.ParentHomepage;
import com.jashan.child_control_app.utils.ActivityTransition;

public class StartUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void goToLogin(View view) {
        ActivityTransition.goToActivity(this, Login.class);
    }

    public void goToRegister(View view) {
        ActivityTransition.goToActivity(this, Register.class);
    }
}