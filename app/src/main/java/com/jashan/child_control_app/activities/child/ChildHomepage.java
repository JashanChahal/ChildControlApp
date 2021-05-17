package com.jashan.child_control_app.activities.child;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jashan.child_control_app.R;
import com.jashan.child_control_app.activities.StartUp;
import com.jashan.child_control_app.activities.parent.CallLogsActivity;
import com.jashan.child_control_app.model.CallDetail;
import com.jashan.child_control_app.model.Child;
import com.jashan.child_control_app.model.ChildInformation;
import com.jashan.child_control_app.model.Parent;
import com.jashan.child_control_app.model.User;
import com.jashan.child_control_app.repository.AfterCompletion;
import com.jashan.child_control_app.repository.FirebaseWebService;
import com.jashan.child_control_app.repository.WebService;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        getCallDetails();
    }

    private void getCallDetails() {

        int LOG_COUNT = 10;
        Cursor cursor = getApplicationContext().getContentResolver().
                query(CallLog.Calls.CONTENT_URI,null,null,null);
        cursor.moveToLast();
        List<CallDetail> callDetailList = new ArrayList<>();
        do  {
            String name = getCallAttribute(cursor,CallLog.Calls.CACHED_NAME);
            String number = getCallAttribute(cursor,CallLog.Calls.NUMBER);

            String duration = getCallAttribute(cursor,CallLog.Calls.DURATION);
            String date = formatDate(getCallAttribute(cursor, CallLog.Calls.DATE));
            String callType = getCallAttribute(cursor,CallLog.Calls.TYPE);
            CallDetail callDetail = new CallDetail(number,name,date, duration,callType);
            callDetailList.add(callDetail);
            LOG_COUNT--;
        } while (cursor.moveToPrevious() && LOG_COUNT > 0 );
        updateCallDetails(callDetailList);
        cursor.close();
    }

    private void updateCallDetails(List<CallDetail> callDetailList) {
        webService.getCurrentUserAndDo(new AfterCompletion<User>() {
            @Override
            public void onSuccess(User user) {
                Child child = (Child) user;
                if (child.getChildInformation() == null) {
                    child.setChildInformation(new ChildInformation());
                }
                ChildInformation childInformation =   child.getChildInformation();
                childInformation.setCallDetails(callDetailList);
                webService.updateUser(child);
                updateChildInParent(child);
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }
    private void updateChildInParent(Child child) {
        webService.queryUserByKeyValue("userEmail", child.getParentEmail(), new AfterCompletion<User>() {
            @Override
            public void onSuccess(User user) {
                Parent parent = (Parent) user;
                if (parent != null) {
                    List<Child> children = parent.getChildren();
                    for (int idx = 0; idx <children.size(); idx++) {
                       if (children.get(idx).getUserEmail().equals(child.getUserEmail())){
                           children.set(idx,child);
                       }
                    };
                    webService.updateUser(parent);
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    private  String getCallAttribute(Cursor cursor,String query) {
       return cursor.getString(cursor.getColumnIndex(query));
    }

    private String formatDate(String timeStamp) {
        Timestamp timestamp = new Timestamp(Long.parseLong(timeStamp));
        Date date = new Date(timestamp.getTime());
        SimpleDateFormat dateFormat =   new SimpleDateFormat("MM/dd/yyyy");
        return  dateFormat.format(date);
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