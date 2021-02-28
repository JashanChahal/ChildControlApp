package com.jashan.child_control_app.parent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jashan.child_control_app.R;
import com.jashan.child_control_app.modal.Parent;
import com.jashan.child_control_app.modal.User;

public class ParentHomepage extends AppCompatActivity {
    final FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_homepage);
        TextView textView = findViewById(R.id.textView);
        String id = getIntent().getStringExtra("USER_ID");

        DatabaseReference query = database.getReference("users");
        query.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Parent parent = snapshot.getValue(Parent.class);
                Log.v("sf", snapshot.toString());
                if (parent != null) {
                    textView.setText(parent.getUserName());
                    Log.v("fs", parent.toString());
                } else {
                    textView.setText("null");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.v("Err", "failed");
            }
        });

    }
}