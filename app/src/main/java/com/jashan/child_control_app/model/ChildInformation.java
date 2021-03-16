package com.jashan.child_control_app.model;

import java.util.List;

public class ChildInformation {
    private List<CallDetail> callDetails;

    public ChildInformation(List<CallDetail> callDetails) {
        this.callDetails = callDetails;
    }

    public ChildInformation() {
    }

    public List<CallDetail> getCallDetails() {
        return callDetails;
    }

    public void setCallDetails(List<CallDetail> callDetails) {
        this.callDetails = callDetails;
    }
}
