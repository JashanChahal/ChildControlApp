package com.jashan.child_control_app.repository;

import com.jashan.child_control_app.model.User;

public interface WebService {
    void getCurrentUserAndDo(AfterCompletion<User> afterCompletion);

    void updateUser(User user);

    void signIn(String email, String password, AfterCompletion<User> afterCompletion);

    void signOut();

    WebService createUser(User user, String password);

    void addAfterCompletion(AfterCompletion<User> afterCompletion);

    WebService getUserEquals(String key, String value);

    void queryUserByKeyValue(String key, String value, AfterCompletion<User> afterCompletion);

}
