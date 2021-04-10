package com.jashan.child_control_app.activities.parent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.CallLog;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jashan.child_control_app.R;
import com.jashan.child_control_app.model.CallDetail;
import com.jashan.child_control_app.model.ChildInformation;

import java.util.List;
import java.util.Objects;

public class CallLogsActivity extends AppCompatActivity {
    private ImageView backIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_logs);

        backIcon = findViewById(R.id.toolbar_icon);
        setListenerOnBackClick();

        ChildInformation childInformation = Objects.requireNonNull(getChildInformation());

        renderCallDetailList(childInformation);

    }

    private void setListenerOnBackClick() {
        backIcon.setOnClickListener(v -> CallLogsActivity.super.onBackPressed());
    }

    private ChildInformation getChildInformation() {
        return (ChildInformation) getIntent().getSerializableExtra("ChildInformation");
    }

    private void renderCallDetailList(ChildInformation childInformation) {
        List<CallDetail> callDetails = childInformation.getCallDetails();
        LinearLayout linearLayout = findViewById(R.id.linear_calllogs);

        createAndAddCallLogLayout(callDetails, linearLayout);
    }

    private void createAndAddCallLogLayout(List<CallDetail> callDetails, LinearLayout linearLayout) {
        for (CallDetail callDetail : callDetails) {
            View callLogsLayout = createCallLogLayout(callDetail);
            linearLayout.addView(callLogsLayout);
        }
    }

    private View createCallLogLayout(CallDetail callDetail) {
        View callLogsLayout = getLayoutInflater().inflate(R.layout.call_log_layout, null, true);

        TextView name = callLogsLayout.findViewById(R.id.call_log_name);
        TextView number = callLogsLayout.findViewById(R.id.call_log_number);
        TextView duration = callLogsLayout.findViewById(R.id.call_log_duration);
        ImageView icon = callLogsLayout.findViewById(R.id.call_log_icon);

        name.setText(callDetail.getName());
        number.setText(callDetail.getPhoneNumber());
        duration.setText(callDetail.getFormatedDuration());
        icon.setImageResource(getCallLogImageResourceId(callDetail));

        return callLogsLayout;
    }

    private int getCallLogImageResourceId(CallDetail callDetail) {
        int resourceId;
        int callType = Integer.parseInt(callDetail.getCallType());

        if (callType == CallLog.Calls.MISSED_TYPE) {
            resourceId = R.drawable.ic_baseline_phone_missed;
        } else if (callType == CallLog.Calls.INCOMING_TYPE) {
            resourceId = R.drawable.ic_baseline_phone_incoming;
        } else {
            resourceId = R.drawable.ic_baseline_phone_outgoing;
        }

        return resourceId;
    }


}