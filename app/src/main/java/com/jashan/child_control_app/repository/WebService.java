package com.jashan.child_control_app.repository;

import com.jashan.child_control_app.model.User;

public interface WebService {
    public void getCurrentUserAndDo(AfterCompletion<User> afterCompletion);
    public void updateUser(User user);
    public void signIn(String email, String password );
    public void signOut();
    public WebService createUser(User user, String password);
    public void addAfterCompletion(AfterCompletion<User> afterCompletion);
}
