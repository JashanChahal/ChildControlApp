package com.jashan.child_control_app.model;


import java.io.Serializable;

public class CallDetail implements Serializable {
    private String phoneNumber;
    private String name;
    private String date;
    private String duration;

    public CallDetail(String phoneNumber, String name, String date, String duration) {
        this.phoneNumber = phoneNumber;
        this.name = name == null ? "Unknown" : name;
        this.date = date;
        this.duration = duration;
    }

    public CallDetail() {
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? "Unkown" : name;
    }

    public String getDate() {
        return date;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "CallDetail{" +
                "Phone Number='" + phoneNumber + '\'' +
                ", Name='" + name + '\'' +
                ", Date=" + date + '\'' +
                ", Duration=" + duration +
                '}';
    }
}
