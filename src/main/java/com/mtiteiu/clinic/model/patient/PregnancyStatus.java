package com.mtiteiu.clinic.model.patient;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PregnancyStatus {
    NO("Nu"),
    PREGNANT("Sarcină"),
    BREAST_FEEDING("Alăptare");

    private final String displayName;

    PregnancyStatus(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static PregnancyStatus fromValue(String value) {
        for (PregnancyStatus gen : PregnancyStatus.values()) {
            if (gen.getDisplayName().equalsIgnoreCase(value)) {
                return gen;
            }
        }
        throw new IllegalArgumentException("Invalid Gender value: " + value);
    }
}
