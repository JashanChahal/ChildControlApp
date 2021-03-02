package com.jashan.child_control_app.activities.parent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.jashan.child_control_app.R;
import com.jashan.child_control_app.activities.StartUp;

import com.jashan.child_control_app.repository.FirebaseWebService;
import com.jashan.child_control_app.repository.WebService;

public class ParentHomepage extends AppCompatActivity {
    private SharedPreferences pref;
    private WebService webService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_homepage);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        bottomNavigationView.setSelectedItemId(R.id.nav_dashboard);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentDashboard()).commit();


        pref = getSharedPreferences("com.jashan.users", MODE_PRIVATE);
        webService = new FirebaseWebService();
        setListenerOnLogoutButton();
    }



    private void setListenerOnLogoutButton() {
        TextView logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webService.signOut();
                Intent intent = new Intent(ParentHomepage.this, StartUp.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        }
    };
}