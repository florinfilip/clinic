package com.mtiteiu.clinic.model.patient;

public enum Race {
    ASIAN("Asian"),
    AFRO_AMERICAN("Afro-American"),
    NATIVE_AMERICAN("Native-American"),
    CAUCASIAN("Caucasian"),
    HISPANIC("Hispanic"),
    MULTIRACIAL("Multiracial"),
    OTHER("Other");

    private final String displayName;

    Race(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }
}
