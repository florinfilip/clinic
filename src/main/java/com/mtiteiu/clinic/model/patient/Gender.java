package com.mtiteiu.clinic.model.patient;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Gender {
    MALE("Masculin"),
    FEMALE("Feminin"),
    OTHER("Altul");


    private final String displayName;

    Gender(String gender) {
        this.displayName = gender;
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
