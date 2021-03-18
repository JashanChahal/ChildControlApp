package com.jashan.child_control_app.activities.parent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jashan.child_control_app.R;
import com.jashan.child_control_app.model.CallDetail;
import com.jashan.child_control_app.model.ChildInformation;

import java.util.List;

public class CallLogsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_logs);

       ChildInformation childInformation = (ChildInformation) getIntent().getSerializableExtra("ChildInformation");
       if (childInformation == null ) return;
        List<CallDetail> callDetails = childInformation.getCallDetails();
        LinearLayout linearLayout = findViewById(R.id.linear_calllogs);
        for (CallDetail callDetail : callDetails) {
           View callLogsLayout = getLayoutInflater().inflate(R.layout.call_log_layout,null,true);
           TextView name = callLogsLayout.findViewById(R.id.call_log_name);
           TextView number = callLogsLayout.findViewById(R.id.call_log_number);
           name.setText(callDetail.getName());
           number.setText(callDetail.getPhoneNumber());
           linearLayout.addView(callLogsLayout);
        }
    }
}