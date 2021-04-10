package com.jashan.child_control_app.model;

import androidx.annotation.NonNull;


public class Child implements User {
    private String userName;
    private String userEmail;
    private String parentEmail;
    private String type;
    private String userId;
    private ChildInformation childInformation;

    public Child(String name, String email, String parentEmail, String userId) {
        this.userName = name;
        this.userEmail = email;
        this.parentEmail = parentEmail;
        this.userId = userId;
        this.type = "Child";
        this.childInformation = null;
    }

    public Child(String name, String email, String parentEmail) {
        this(name, email, parentEmail, null);
    }

    public Child() {
    }

    @Override
    public String getUserId() {
        return this.userId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ChildInformation getChildInformation() {
        return childInformation;
    }

    public void setChildInformation(ChildInformation childInformation) {
        this.childInformation = childInformation;
    }

    @Override
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String getUserName() {
        return this.userName;
    }

    @Override
    public String getUserEmail() {
        return this.userEmail;
    }

    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getType() {
        return type;
    }

    @Override
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
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
