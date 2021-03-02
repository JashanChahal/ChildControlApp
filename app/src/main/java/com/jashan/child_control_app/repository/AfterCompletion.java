package com.jashan.child_control_app.repository;

public  abstract class AfterCompletion<T> {
    public  abstract  void onSuccess(T t);
    public abstract  void onFailure(Exception e);
}
