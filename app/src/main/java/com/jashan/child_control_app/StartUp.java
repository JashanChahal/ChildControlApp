package com.jashan.child_control_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import com.jashan.child_control_app.common.Login;

public class StartUp extends AppCompatActivity {
    private boolean isLoggedIn = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);

    }

    public void renderLogin(View view) {
        Intent intent = new Intent(this,Login.class);
        Pair[] pairs = new Pair[1];

        pairs[0] = new Pair<View,String>(findViewById(R.id.login_btn),"transition_login");
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,pairs);

        startActivity(intent,options.toBundle());
    }
}