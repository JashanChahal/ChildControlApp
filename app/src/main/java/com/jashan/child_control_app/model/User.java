package com.jashan.child_control_app.model;


import java.io.Serializable;

public interface User extends Serializable {
    public String getUserName();
    public String getUserEmail();
    public void setUserName(String userName);
    public void setUserEmail(String userEmail);
    public String getUserId();
    public void setUserId(String uid);
    public String getType();
}
