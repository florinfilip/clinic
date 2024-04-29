package com.mtiteiu.clinic.model.patient;

public enum Religion {
    PROTESTANT("Protestantism"),
    CATOLIC("Catolicism"),
    ORTODOX("Ortodoxism"),
    ISLAM("Islam"),
    HINDUISM("Hinduism"),
    BUDISM("Budism"),
    IUDAISM("Iudaism"),
    BAHAI("Bahai"),
    SHINTO("Shinto");
    private final String displayName;

    Religion(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
