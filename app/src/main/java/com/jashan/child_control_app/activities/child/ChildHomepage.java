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
import com.jashan.child_control_app.model.CallDetail;
import com.jashan.child_control_app.repository.FirebaseWebService;
import com.jashan.child_control_app.repository.WebService;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

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

        while (cursor.moveToPrevious() && LOG_COUNT > 0 ) {
            String name = getCallAttribute(cursor,"name");
            String number = getCallAttribute(cursor,"formatted_number");
            String duration = getCallAttribute(cursor,"duration");
            String date = formatDate(getCallAttribute(cursor,"date"));
            CallDetail callDetail = new CallDetail(number,name,date, duration);
            Log.i("Jashan's call", callDetail.toString());
            LOG_COUNT--;
        }
        cursor.close();
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