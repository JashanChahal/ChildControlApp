package com.jashan.child_control_app.modal;

public class Child implements User {
    private String name;
    private String parentEmail;
    private Parent parent;

    public Child(String name, String email) {
        this.name = name;
        this.parentEmail = email;
    }

    @Override
    public String getUserName() {
        return this.name;
    }

    @Override
    public String getUserEmail() {
        return this.parentEmail;
    }

    @Override
    public String setUserName(String userName) {
        return null;
    }

    @Override
    public String setUserEmail(String userEmail) {
        return null;
    }


}
