package com.jashan.child_control_app.repository;


import android.util.Log;


import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.jashan.child_control_app.model.Child;
import com.jashan.child_control_app.model.Parent;
import com.jashan.child_control_app.model.User;

import java.util.HashMap;
import java.util.Map;


public class FirebaseWebService implements WebService {
    private final FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private final FirebaseDatabase database;
    private AuthenticationDetails authenticationDetails;
    private static User user;
    private Query query;

    public FirebaseWebService() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


    }

    @Override
    public void getCurrentUserAndDo(AfterCompletion<User> afterCompletion) {
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            afterCompletion.onFailure(new UserNotFoundException("User does not exists"));
            return;
        }
        String uid = currentUser.getUid();
        DatabaseReference ref = database.getReference("users");
        // Get users/uid from firebase database
        ref.child(uid).get()
                .addOnSuccessListener((dataSnapshot) -> {
                            String type = (String) dataSnapshot.child("type").getValue();
                            if (type.equals("Parent"))
                                FirebaseWebService.user = dataSnapshot.getValue(Parent.class);
                            else if (type.equals("Child")) {
                                FirebaseWebService.user = dataSnapshot.getValue(Child.class);
                            }
                            afterCompletion.onSuccess(FirebaseWebService.user);
                        }
                ).addOnFailureListener(afterCompletion::onFailure);
    }

    @Override
    public WebService getUserEquals(String key, String value) {
        return this;
    }

    @Override
    public void queryUserByKeyValue(String key, String value, AfterCompletion<User> afterCompletion) {
        DatabaseReference databaseReference = database.getReference("users");

        databaseReference
                .orderByChild(key)
                .equalTo(value.toLowerCase()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String type = snapshot.getChildren().iterator().next().child("type").getValue().toString();
                Log.v("obj", type);
                Class userClass = type.equals("Parent") ? Parent.class : Child.class;
                User user = (User) snapshot.getChildren().iterator().next().getValue(userClass);
                afterCompletion.onSuccess(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.v("testing", "failed");
            }
        });
    }


    @Override
    public WebService createUser(User user, String password) {
        authenticationDetails = new AuthenticationDetails(user, password);
        return this;
    }


    public void addAfterCompletion(AfterCompletion<User> afterCompletion) {
        mAuth.createUserWithEmailAndPassword(authenticationDetails.getUser().getUserEmail(), authenticationDetails.getPassword())
                .addOnCompleteListener((task) -> {
                    if (task.isSuccessful()) {
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        User user = authenticationDetails.getUser();
                        user.setUserId(currentUser.getUid());

                        writeUserToDataBase(user);

                        afterCompletion.onSuccess(user);
                    } else {
                        afterCompletion.onFailure(new IllegalArgumentException("User not created"));

                    }
                });
    }

    private void writeUserToDataBase(User user) {
        DatabaseReference ref = database.getReference();
        ref.child("users").child(user.getUserId()).setValue(user);
    }

    @Override
    public void updateUser(User user) {
        Map<String, Object> update = new HashMap<>();
        update.put("/users/" + user.getUserId(), user);
        FirebaseDatabase.getInstance().getReference().updateChildren(update);

    }

    @Override
    public void signIn(String email, String password, AfterCompletion<User> afterCompletion) {
        Log.v("Email", email);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            getCurrentUserAndDo(new AfterCompletion<User>() {
                                @Override
                                public void onSuccess(User user) {
                                    afterCompletion.onSuccess(user);
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    Log.v("UserNotFound","Current not found");
                                    signOut();
                                    afterCompletion.onFailure(new UserNotFoundException());
                                }
                            });

                        } else {
                            afterCompletion.onFailure(new UserNotFoundException());
                        }
                    }
                });
    }

    @Override
    public void signOut() {
        mAuth.signOut();
    }

    private static class AuthenticationDetails {
        private User user;
        private String password;

        AuthenticationDetails(User user, String password) {
            this.user = user;
            this.password = password;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public String getPassword() {
            return password;
        }

    }

    private class Query {
        private String key;
        private String value;

        public Query(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
