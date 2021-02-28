package com.jashan.child_control_app.modal;

import java.util.ArrayList;
import java.util.List;

public class Parent implements  User {
    private String name;
    private  String email;
    private List<Child> children;

    public Parent(String name, String email) {
        this.name = name;
        this.email = email;
        this.children = new ArrayList<>();
    }

    @Override
    public String getUserName() {
        return this.name;
    }

    @Override
    public String getUserEmail() {
        return this.email;
    }


}
