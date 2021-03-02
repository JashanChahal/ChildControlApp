package com.jashan.child_control_app.activities.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jashan.child_control_app.R;
import com.jashan.child_control_app.activities.StartUp;
import com.jashan.child_control_app.activities.parent.ParentHomepage;

import com.jashan.child_control_app.model.User;
import com.jashan.child_control_app.repository.AfterSuccess;
import com.jashan.child_control_app.repository.FirebaseWebService;
import com.jashan.child_control_app.repository.WebService;
import com.jashan.child_control_app.utils.ActivityTransition;

public class Login extends AppCompatActivity {
    TextInputLayout emailTextInputLayout;
    TextInputLayout passwordTextInputLayout;
    private FirebaseAuth mAuth;
    WebService webService;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailTextInputLayout = findViewById(R.id.login_email);
        passwordTextInputLayout = findViewById(R.id.login_password);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth != null) {

        }
        pref = getSharedPreferences("com.jashan.users", MODE_PRIVATE);
        webService = new FirebaseWebService();
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
        String email = emailTextInputLayout.getEditText().getText().toString();
        String password = passwordTextInputLayout.getEditText().getText().toString();

        ProgressBar progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            getCurrentUserAndSetPreferences(progressBar);
                            Intent intent = new Intent(Login.this, ParentHomepage.class);
                            intent.putExtra("UID", user.getUid());
                            startActivity(intent);
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(Login.this, "Email or Password is wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    private void getCurrentUserAndSetPreferences(ProgressBar progressBar) {
        webService = new FirebaseWebService();

        progressBar.setVisibility(View.VISIBLE);

        webService.getCurrentUserAndDo(new AfterSuccess<User>() {
            @Override
            public void doThis(User user) {
                progressBar.setVisibility(View.GONE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("userName", user.getUserName());
                editor.putString("userEmail", user.getUserEmail());
                editor.putString("type", user.getType());
                editor.apply();

            }
        });


    }
}