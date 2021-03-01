package com.jashan.child_control_app.model;

import androidx.annotation.NonNull;

public class Child implements User {
    private String userName;
    private String userEmail;
    private String parentEmail;
    private String type;

    public Child(String name, String email, String parentEmail) {
        this.userName = name;
        this.userEmail = email;
        this.parentEmail = parentEmail;
        this.type = "Child";
    }
    public Child(){}
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
        return null;
    }

    public String getType() {
        return type;
    }

    @Override
    public String setUserEmail(String userEmail) {
        return null;
    }

    public String getParentEmail() {
        return parentEmail;
    }

    public void setParentEmail(String parentEmail) {
        this.parentEmail = parentEmail;
    }

    @NonNull
    @Override
    public String toString() {
        return this.userName;
    }
}
