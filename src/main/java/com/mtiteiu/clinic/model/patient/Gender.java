package com.mtiteiu.clinic.model.patient;

public enum Gender {
    MALE("Masculin"),
    FEMALE("Feminin"),
    OTHER("Altul");

    Gender(String gender) {
        this.gender = gender;
    }

    private final String gender;

    public String getGender() {
        return gender;
    }
}
