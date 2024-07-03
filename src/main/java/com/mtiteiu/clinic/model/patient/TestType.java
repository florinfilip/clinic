package com.mtiteiu.clinic.model.patient;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TestType {

    CLINIC("Testare în clinică"),
    HOME("Testare la domiciliu"),
    UPU("Testare în UPU");

    private final String displayName;

    TestType(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static TestType fromValue(String value) {
        for (TestType testType : TestType.values()) {
            if (testType.getDisplayName().equalsIgnoreCase(value)) {
                return testType;
            }
        }
        throw new IllegalArgumentException("Invalid Religion value: " + value);
    }
}
