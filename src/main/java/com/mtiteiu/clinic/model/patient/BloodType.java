package com.mtiteiu.clinic.model.patient;

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

    String getDisplayName() {
        return this.displayName;
    }
}
