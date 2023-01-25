package com.umc.withme.domain.constant;

public enum Gender {

    MALE, FEMALE;

    public static Gender caseFreeValueOf(String name) {
        String lowerCaseName = name.toLowerCase();
        return lowerCaseName.equals("male") ? MALE : FEMALE;
    }
}
