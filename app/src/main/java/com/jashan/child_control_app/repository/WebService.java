package com.jashan.child_control_app.repository;

import com.jashan.child_control_app.model.User;

public interface WebService {
    public User getCurrentUser();
    public void updateUser(User user);
}
