package com.mtiteiu.clinic.model.patient;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ProfessionalStatus {
    EMPLOYED("Angajat"),
    UNEMPLOYED("Șomer"),
    SELF_EMPLOYED("Liber Profesionist"),
    STUDENT("Student"),
    RETIRED("Pensionar"),
    CONTRACTOR("Contractor"),
    OTHER("Altul");

    private final String displayName;

    ProfessionalStatus(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return this.displayName;
    }

    @JsonCreator
    public static ProfessionalStatus fromValue(String value) {
        for (ProfessionalStatus professionalStatus : ProfessionalStatus.values()) {
            if (professionalStatus.getDisplayName().equalsIgnoreCase(value)) {
                return professionalStatus;
            }
        }
        throw new IllegalArgumentException("Invalid ProfessionalStatus value: " + value);
    }
}