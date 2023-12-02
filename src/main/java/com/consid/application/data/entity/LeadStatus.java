package com.consid.application.data.entity;

public enum LeadStatus {
    CONTACTED("Contacted"),
    INTERVIEW_SCHEDULED("Interview Scheduled"),
    INTERVIEW_DONE("Interview Done");

    private final String statusString;

    LeadStatus(final String statusString) {
        this.statusString = statusString;
    }

    @Override
    public String toString() {
        return statusString;
    }
}
