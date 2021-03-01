package com.jashan.child_control_app.repository;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jashan.child_control_app.model.Child;
import com.jashan.child_control_app.model.Parent;
import com.jashan.child_control_app.model.User;

public class FirebaseWebService  implements  WebService{
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database;
    private static User user;
    public FirebaseWebService() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();


    }

    @Override
    public void getCurrentUserAndDo( AfterSuccess<User> afterSuccess) {
        String uid = currentUser.getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        ref.child(uid).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                String type = (String) dataSnapshot.child("type").getValue();
                if (type.equals("Parent"))
                    FirebaseWebService.user = dataSnapshot.getValue(Parent.class);
                else if (type.equals("Child")) {
                    FirebaseWebService.user = dataSnapshot.getValue(Child.class);
                }
                afterSuccess.doThis(FirebaseWebService.user);
            }
        });
    }

    @Override
    public void createUser(String email, String password){

    }

    @Override
    public void updateUser(User user) {

    }

    @Override
    public void signIn(String email, String password) {

    }

    @Override
    public void signOutCurrentUser() {

    }
}
