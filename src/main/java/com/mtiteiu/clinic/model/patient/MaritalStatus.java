package com.mtiteiu.clinic.model.patient;

public enum MaritalStatus {
    MARRIED("Married"),
    DIVORCED("Divorced"),
    WIDOWED("Widowed"),
    ENGAGED("Engaged"),
    CIVIL_PARTNERSHIP("Civil Partnership"),
    SEPARATED("Separated"),
    OTHER("Other");

    private String status;

    MaritalStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
