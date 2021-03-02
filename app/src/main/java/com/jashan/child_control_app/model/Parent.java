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

    @Override
    public String getType() {
        return type;
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

    public void setChildren(List<Child> children) {
        this.children = children;
    }
    public List<Child> getChildren(){
        return this.children;
    }

    public void setUserId(String uid) {
        this.userId = uid;
    }
    public String getUserId() {
        return this.userId;
    }
    @NonNull
    @Override
    public String toString() {
        return userEmail+userName;
    }


}