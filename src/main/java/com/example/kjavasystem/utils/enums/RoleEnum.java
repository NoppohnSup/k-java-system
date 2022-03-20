package com.example.kjavasystem.utils.enums;

public enum RoleEnum {
    TRANSPORT_STAFF(1, "transport staff"),
    CASH_CENTER_STAFF(2, "cash center staff");

    private int roleId;
    private String message;

    public int getRoleId() {
        return roleId;
    }

    public String getMessage() {
        return message;
    }

    RoleEnum(int roleId, String message) {
        this.roleId = roleId;
        this.message = message;
    }
}
