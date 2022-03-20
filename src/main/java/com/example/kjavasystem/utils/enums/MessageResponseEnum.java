package com.example.kjavasystem.utils.enums;

public enum MessageResponseEnum {
    SUCCESS("Success"),
    FAIL("Fail");

    private String message;

    public String getMessage() {
        return message;
    }

    MessageResponseEnum(String message) {
        this.message = message;
    }
}
