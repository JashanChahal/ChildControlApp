package com.jashan.child_control_app.common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.jashan.child_control_app.StartUp;
import com.jashan.child_control_app.parent.ParentHomepage;
import com.jashan.child_control_app.utils.ActivityTransition;

public class Login extends AppCompatActivity {
    TextInputLayout emailTextInputLayout;
    TextInputLayout passwordTextInputLayout;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailTextInputLayout = findViewById(R.id.login_email);
        passwordTextInputLayout = findViewById(R.id.login_password);

        mAuth = FirebaseAuth.getInstance();
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

    public void login(View view){
        String email = emailTextInputLayout.getEditText().getText().toString();
        String password = passwordTextInputLayout.getEditText().getText().toString();

        ProgressBar progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);

                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(Login.this, ParentHomepage.class);
                            intent.putExtra("UID",user.getUid());
                            startActivity(intent);
                        } else {
                            Toast.makeText(Login.this, "Email or Password is wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}