package com.jashan.child_control_app.model;


import java.io.Serializable;

public class CallDetail implements Serializable {
    private String phoneNumber;
    private String name;
    private String date;
    private String duration;
    private String callType;

    public CallDetail(String phoneNumber, String name, String date, String duration, String callType) {
        this.phoneNumber = phoneNumber;
        this.name = name == null ? "Unknown" : name;
        this.date = date;
        this.duration = duration;
        this.callType = callType;
    }

    public CallDetail() {
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
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
    public String getFormatedDuration() {
        int durationInt = Integer.parseInt(duration);
        int durationMin = durationInt / 60 ;
        int durationSec = durationInt % 60;

        return  durationMin + "min, "+ durationSec+"sec";
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
