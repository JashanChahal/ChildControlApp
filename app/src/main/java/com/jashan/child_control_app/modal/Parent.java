package com.jashan.child_control_app.modal;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Parent implements User {
    private String userName;
    private String userEmail;
    private List<Child> children;

    public Parent(String name, String email) {
        this.userName = name;
        this.userEmail = email;
        this.children = new ArrayList<>();
    }
    public Parent(){}
    @Override
    public String getUserName() {
        return this.userName;
    }

    @Override
    public String getUserEmail() {
        return this.userEmail;
    }

    @Override
    public String setUserName(String userName) {
        return this.userName = userName;
    }

    @Override
    public String setUserEmail( String email) {
        return this.userName = email;
    }

    public void addChildren(Child child) {
        this.children.add(child);
    }

    @NonNull
    @Override
    public String toString() {
        return userEmail+userName;
    }
}
