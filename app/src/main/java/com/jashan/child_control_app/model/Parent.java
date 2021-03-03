package com.jashan.child_control_app.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Parent implements User {
    private String userName;
    private String userEmail;
    private String userId;
    private List<Child> children;
    private String type;

    public Parent(String name, String email, String userId) {
        this.userName = name;
        this.userEmail = email;
        this.userId = userId;
        this.children = new ArrayList<>();
        this.type = "Parent";
    }
    public Parent(String name, String email) {
        this(name,email,null);
    }

    public Parent(){}


    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String getUserEmail() {
        return userEmail;
    }

    @Override
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Child> getChildren() {
        return children;
    }

    public void setChildren(List<Child> children) {
        this.children = children;
    }

    @Override
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @NonNull
    @Override
    public String toString() {
        return userEmail+userName;
    }


}
