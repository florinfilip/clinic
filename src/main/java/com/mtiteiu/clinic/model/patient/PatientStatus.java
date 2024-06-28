package com.mtiteiu.clinic.model.patient;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


public enum PatientStatus {
    STABLE("Stabil"),
    CRITICAL("Critical"),
    OTHER("Altul");

    private final String displayName;

    PatientStatus(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static Gender fromValue(String value) {
        for (Gender gen : Gender.values()) {
            if (gen.getDisplayName().equalsIgnoreCase(value)) {
                return gen;
            }
        }
        throw new IllegalArgumentException("Invalid Gender value: " + value);
    }
}
