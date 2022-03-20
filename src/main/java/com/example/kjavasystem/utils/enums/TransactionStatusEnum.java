package com.example.kjavasystem.utils.enums;

public enum TransactionStatusEnum {
    CREATED("created"),
    RECEIVE("receive");

    private String status;

    public String getStatus() {
        return status;
    }

    TransactionStatusEnum(String status) {
        this.status = status;
    }
}
