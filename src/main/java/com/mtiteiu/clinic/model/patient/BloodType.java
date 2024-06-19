package com.mtiteiu.clinic.model.patient;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum BloodType {
    O_POSITIVE("O+"),
    O_NEGATIVE("O-"),
    A_POSITIVE("A+"),
    A_NEGATIVE("A-"),
    AB_POSITIVE("AB+"),
    AB_NEGATIVE("AB-");

    private final String displayName;

    BloodType(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return this.displayName;
    }

    @JsonCreator
    public static BloodType fromValue(String value) {
        for (BloodType bloodType : BloodType.values()) {
            if (bloodType.getDisplayName().equalsIgnoreCase(value)) {
                return bloodType;
            }
        }
        throw new IllegalArgumentException("Invalid BloodType value: " + value);
    }
}
