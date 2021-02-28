package com.jashan.child_control_app.parent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.jashan.child_control_app.R;

public class ParentHomepage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_homepage);
        TextView textView = findViewById(R.id.textView);
        textView.setText(getIntent().getStringExtra("USER_ID"));
    }
}