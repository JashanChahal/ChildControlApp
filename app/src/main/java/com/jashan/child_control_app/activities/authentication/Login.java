package com.jashan.child_control_app.activities.authentication;

import androidx.appcompat.app.AppCompatActivity;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.jashan.child_control_app.R;
import com.jashan.child_control_app.activities.StartUp;

import com.jashan.child_control_app.model.User;
import com.jashan.child_control_app.repository.AfterCompletion;
import com.jashan.child_control_app.repository.WebService;
import com.jashan.child_control_app.utils.ActivityTransition;
import com.jashan.child_control_app.utils.Configuration;

import java.util.Objects;

public class Login extends AppCompatActivity {
    TextInputLayout emailTextInputLayout;
    TextInputLayout passwordTextInputLayout;
    WebService webService;
    ProgressBar progressBar;

    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailTextInputLayout = findViewById(R.id.login_email);
        passwordTextInputLayout = findViewById(R.id.login_password);

        progressBar = findViewById(R.id.progressBar2);

        pref = getSharedPreferences("com.jashan.users", MODE_PRIVATE);

        webService = Configuration.getWebservice();
    }

    public void goBackToStartUp(View view) {
        ActivityTransition.goBack(this, StartUp.class);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.transition.enter_back, R.transition.back);
    }

    public void goToRegister(View view) {
        ActivityTransition.goToActivity(this, Register.class);
    }

    public void login(View view) {
        String email = Objects.requireNonNull(emailTextInputLayout.getEditText()).getText().toString();
        String password = Objects.requireNonNull(passwordTextInputLayout.getEditText()).getText().toString();

        progressBar.setVisibility(View.VISIBLE);

         webService.signIn(email, password, new AfterCompletion<User>() {
           @Override
           public void onSuccess(User user) {
               ActivityTransition.goToUserHomepage(Login.this,user,pref,progressBar);
           }

           @Override
           public void onFailure(Exception e) {
               progressBar.setVisibility(View.GONE);
               Toast.makeText(Login.this, "Email or Password is wrong", Toast.LENGTH_LONG).show();
           }
       });
    }
}

