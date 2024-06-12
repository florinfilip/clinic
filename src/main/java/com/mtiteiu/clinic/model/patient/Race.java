package com.mtiteiu.clinic.model.patient;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Race {
    ASIAN("Asian"),
    AFRO_AMERICAN("Afro-American"),
    NATIVE_AMERICAN("Nativ-American"),
    CAUCASIAN("Caucazian"),
    HISPANIC("Hispanic"),
    MULTIRACIAL("Multiracial"),
    OTHER("Alta");

    private final String displayName;

    Race(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return this.displayName;
    }

    @JsonCreator
    public static Race fromValue(String value) {
        for (Race race : Race.values()) {
            if (race.getDisplayName().equalsIgnoreCase(value)) {
                return race;
            }
        }
        throw new IllegalArgumentException("Invalid Race value: " + value);
    }
}
