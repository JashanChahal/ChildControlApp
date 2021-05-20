package com.jashan.child_control_app.activities.parent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jashan.child_control_app.R;
import com.jashan.child_control_app.activities.StartUp;


import com.jashan.child_control_app.repository.NotificationService;
import com.jashan.child_control_app.repository.WebService;
import com.jashan.child_control_app.utils.Configuration;

import java.util.Objects;

public class ParentHomepage extends AppCompatActivity {
    private WebService webService;
    private NotificationService notificationService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_homepage);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        bottomNavigationView.setSelectedItemId(R.id.nav_dashboard);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentDashboard()).commit();


        webService = Configuration.getWebservice();
        setListenerOnLogoutButton();
        openNotificationChannel();

        webService = Configuration.getWebservice();
        setListenerOnLogoutButton();

        notificationService = Configuration.getNotificationService();
        notificationService.subscribeToNotificationTopic("client");


    }


    private void setListenerOnLogoutButton() {
        TextView logout = findViewById(R.id.logout);
        logout.setOnClickListener(view -> {
            webService.signOut();
            Intent intent = new Intent(ParentHomepage.this, StartUp.class);
            startActivity(intent);
            finish();
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = item -> {
        Fragment selectedFragment = null;
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        ImageView toobarIcon = findViewById(R.id.toolbar_icon);

        switch (item.getItemId()) {
            case R.id.nav_dashboard:
                selectedFragment = new FragmentDashboard();
                toolbarTitle.setText("Dashboard");
                toobarIcon.setImageResource(R.drawable.ic_baseline_dashboard);
                break;
            case R.id.nav_settings:
                selectedFragment = new FragmentSettings();
                toolbarTitle.setText("Settings");
                toobarIcon.setImageResource(R.drawable.ic_baseline_settings);
                break;
            case R.id.nav_profile:
                selectedFragment = new FragmentProfile();
                toolbarTitle.setText("Profile");
                toobarIcon.setImageResource(R.drawable.ic_baseline_person);
                break;
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, Objects.requireNonNull(selectedFragment)).commit();
        return true;
    };


    private void openNotificationChannel() {
        NotificationChannel channel = new NotificationChannel("mychannel", "mychannel", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);
    }

}