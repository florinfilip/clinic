package com.mtiteiu.clinic.model.patient;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Religion {
    PROTESTANT("Protestant"),
    CATOLIC("Catolic"),
    ORTODOX("Ortodox"),
    ISLAM("Islamic"),
    HINDUISM("Hinduist"),
    BUDISM("Budist"),
    IUDAISM("Judaist"),
    OTHER("Alta");
    private final String displayName;

    Religion(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static Religion fromValue(String value) {
        for (Religion religion : Religion.values()) {
            if (religion.getDisplayName().equalsIgnoreCase(value)) {
                return religion;
            }
        }
        throw new IllegalArgumentException("Invalid Religion value: " + value);
    }
}
