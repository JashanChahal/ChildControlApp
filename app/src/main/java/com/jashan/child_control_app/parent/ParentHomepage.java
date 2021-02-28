package com.jashan.child_control_app.parent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
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
        String id = getIntent().getStringExtra("UID");
        TextView userName = findViewById(R.id.profile_username);
        TextView email = findViewById(R.id.profile_email);
        TextView children = findViewById(R.id.profile_children);
        ProgressBar progressBar = findViewById(R.id.progressBar3);
        DatabaseReference query = database.getReference("users");
        progressBar.setVisibility(View.VISIBLE);
        query.child("parent/"+id).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot snapshot) {
                progressBar.setVisibility(View.GONE);
                Parent parent = snapshot.getValue(Parent.class);

                if (parent != null) {
                    userName.setText(parent.getUserName());
                    email.setText(parent.getUserEmail());
                } else {
                    Log.v("sf", "null");
                }
            }
        });


    }
}