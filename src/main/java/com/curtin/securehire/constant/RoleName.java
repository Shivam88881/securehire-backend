package com.curtin.securehire.constant;

public enum RoleName {
    ADMIN("admin"),
    CANDIDATE("candidate"),
    RECRUITER("recruiter");

    private final String value;

    RoleName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
