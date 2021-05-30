package com.jashan.child_control_app.activities.parent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jashan.child_control_app.R;
import com.jashan.child_control_app.model.Child;
import com.jashan.child_control_app.model.User;
import com.jashan.child_control_app.utils.Configuration;

public class ChildDetailsActivity extends AppCompatActivity {
    private Child child;
    private Button appUsage;
    private Button callLogs;
    private Button currentLocation;
    private Button screenShot;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_details);

        initialiseInstanceVariables();

        Configuration.registerActivityToBroadCastManager(this, broadcastReceiver, "openMap");

        setProfileOfUser();

        setOnClickListeners();
    }

    private void initialiseInstanceVariables() {
        this.appUsage = findViewById(R.id.app_usage_btn);
        this.callLogs = findViewById(R.id.call_logs_btn);
        this.currentLocation = findViewById(R.id.location_btn);
        this.screenShot = findViewById(R.id.screenshot_btn);
        this.child = getChild();
        this.progressBar = findViewById(R.id.notification_progress);
    }

    private Child getChild() {
        Child child = (Child) getIntent().getSerializableExtra("child");
        if (child == null) {
            Log.d("Child", "NULL");
        }
        return child;
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            progressBar.setVisibility(View.INVISIBLE);
            String latitude = intent.getStringExtra("latitude");
            String longitude = intent.getStringExtra("longitude");
            Configuration.openMap(ChildDetailsActivity.this, latitude, longitude);
        }
    };

    private void setProfileOfUser() {
        TextView userNameTitle = findViewById(R.id.username_title);
        TextView userName = findViewById(R.id.profile_username);
        TextView email = findViewById(R.id.profile_email);

        userNameTitle.setText(child.getUserName());

        userName.setText(child.getUserName());
        // Change Color
        userName.setTextColor(getResources().getColor(R.color.black, getTheme()));

        email.setText(child.getUserEmail());
        // Change Color
        email.setTextColor(getResources().getColor(R.color.black, getTheme()));

    }

    private void setOnClickListeners() {
        appUsage.setOnClickListener((view) -> {
            RequestService.appUsage(this);
         Intent intent  = new Intent(this,AppUsageActivity.class);
         startActivity(intent);
        });

        callLogs.setOnClickListener((view) -> {
            Intent intent = new Intent(this, CallLogsActivity.class);
            intent.putExtra("ChildInformation", child.getChildInformation());
            startActivity(intent);

        });
        screenShot.setOnClickListener((view) ->{
            RequestService.requestScreenShot(this);
        });
        currentLocation.setOnClickListener((view) -> {
            progressBar.setVisibility(View.VISIBLE);
            RequestService.requestLocation(this);
        });
    }

}