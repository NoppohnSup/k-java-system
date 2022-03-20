package com.example.kjavasystem.transaction.response;

public class TransactionResponse {
    private int transactionId;
    private String moneyBoxId;

    public TransactionResponse() {
    }

    public String getMoneyBoxId() {
        return moneyBoxId;
    }

    public void setMoneyBoxId(String moneyBoxId) {
        this.moneyBoxId = moneyBoxId;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }
}
