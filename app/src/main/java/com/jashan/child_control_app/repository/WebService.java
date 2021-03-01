package com.jashan.child_control_app.repository;

import com.jashan.child_control_app.model.User;

public interface WebService {
    public void getCurrentUserAndDo(AfterSuccess<User> afterSuccess);
    public void updateUser(User user);
    public void signIn(String email, String password );
    public void signOutCurrentUser();
    public void createUser(String email, String password);
}
