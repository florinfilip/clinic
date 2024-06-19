package com.mtiteiu.clinic.model.patient;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Diet {
    MEDITERANEAN("Mediteraneană"),
    VEGAN("Vegetarian/Vegan"),
    NORMAL("Normală"),
    OTHER("Alta");

    private final String dietName;

    Diet(String displayName) {
        this.dietName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return this.dietName;
    }

    @JsonCreator
    public static Diet fromValue(String value) {
        for (Diet diet : Diet.values()) {
            if (diet.getDisplayName().equalsIgnoreCase(value)) {
                return diet;
            }
        }
        throw new IllegalArgumentException("Invalid Diet value: " + value);
    }
}
