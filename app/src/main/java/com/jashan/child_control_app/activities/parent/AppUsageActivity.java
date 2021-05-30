package com.jashan.child_control_app.activities.parent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jashan.child_control_app.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AppUsageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_usage);

        LinearLayout linearLayout = findViewById(R.id.app_usage_linear_layout);
        Map<String,String> usageStat = new HashMap<>();

        addUsageLayoutToLinearLayout(linearLayout,usageStat);

    }

    private void addUsageLayoutToLinearLayout(LinearLayout linearLayout, Map<String,String> usageStat) {
        for (Map.Entry<String, String> usage : usageStat.entrySet()) {
            View appUsage = getLayoutInflater().inflate(R.layout.app_usage_layout, null, true);
            TextView appName = appUsage.findViewById(R.id.app_name);
            TextView screenTime = appUsage.findViewById(R.id.screen_time);

            appName.setText(usage.getKey());
            screenTime.setText(usage.getValue());
            linearLayout.addView(appUsage);
        }


    }
}