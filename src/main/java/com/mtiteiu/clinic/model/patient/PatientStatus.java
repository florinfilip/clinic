package com.mtiteiu.clinic.model.patient;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


public enum PatientStatus {
    HEALTHY("Sănătos"),
    LOW_RISK("Risc Scăzut"),
    MEDIUM_RISK("Risc Mediu"),
    HIGH_RISK("Risc Ridicat");

    private final String displayName;

    PatientStatus(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static PatientStatus fromValue(String value) {
        for (PatientStatus status : PatientStatus.values()) {
            if (status.getDisplayName().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid Gender value: " + value);
    }
}