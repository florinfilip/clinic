package com.mtiteiu.clinic.model.patient;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CivilStatus {
    MARRIED("Căsătorit"),
    UNMARRIED("Necăsătorit"),
    DIVORCED("Divorțat"),
    WIDOWED("Văduv"),
    ENGAGED("Logodit"),
    SEPARATED("Separat"),
    OTHER("Altul");

    private final String status;

    CivilStatus(String status) {
        this.status = status;
    }

        @JsonValue
    public String getStatus() {
        return status;
    }


    @JsonCreator
    public static CivilStatus fromValue(String value) {
        for (CivilStatus civilStatus : CivilStatus.values()) {
            if (civilStatus.getStatus().equalsIgnoreCase(value)) {
                return civilStatus;
            }
        }
        throw new IllegalArgumentException("Invalid Status value: " + value);
    }
}
