package com.jashan.child_control_app.activities.parent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jashan.child_control_app.R;
import com.jashan.child_control_app.model.Parent;

public class FragmentProfile extends Fragment {
    final private FirebaseDatabase database = FirebaseDatabase.getInstance();
    final private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private SharedPreferences pref;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_profile,container,false);
    }

    @Override
    public void onStart() {
        super.onStart();
        String id = user.getUid();

        TextView userName = getView().findViewById(R.id.profile_username);
        TextView email = getView().findViewById(R.id.profile_email);
        TextView children = getView().findViewById(R.id.profile_children);
        ProgressBar progressBar = getView().findViewById(R.id.progressBar3);
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
                    children.setText(parent.getChildren().toString());
                } else {
                    Log.v("sf", "null");
                }
            }
        });
    }
}
