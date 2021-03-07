package com.jashan.child_control_app.activities.child;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jashan.child_control_app.R;
import com.jashan.child_control_app.activities.StartUp;
import com.jashan.child_control_app.repository.FirebaseWebService;
import com.jashan.child_control_app.repository.WebService;

public class ChildHomepage extends AppCompatActivity {
    TextView textView;
    WebService webService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_homepage);
        webService = new FirebaseWebService();
        setListenerOnLogoutButton();
        textView = findViewById(R.id.childTextView);
        textView.setText("This application is monitoring your phone");
    }

    private void setListenerOnLogoutButton() {
        TextView logout = findViewById(R.id.logout_child);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webService.signOut();
                Intent intent = new Intent(ChildHomepage.this, StartUp.class);
                startActivity(intent);
                finish();
            }
        });
    }
}