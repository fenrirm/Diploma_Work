package com.shevchenko.DiplomaWorkStudyPlatform.enums;

public enum Role {
    STUDENT("STUDENT"),
    TEACHER("TEACHER");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Role fromString(String text) {
        for (Role role : Role.values()) {
            if (role.value.equalsIgnoreCase(text)) {
                return role;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }
}
