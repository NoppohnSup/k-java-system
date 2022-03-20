package com.example.kjavasystem.transaction.request;

public class TransactionReceiveRequest {
    private int receiveBy;
    private int transactionId;
    private String moneyBoxId;
    private int receiveBranchId;
    private double totalMoney;

    public TransactionReceiveRequest() {
    }

    public int getReceiveBranchId() {
        return receiveBranchId;
    }

    public void setReceiveBranchId(int receiveBranchId) {
        this.receiveBranchId = receiveBranchId;
    }

    public int getReceiveBy() {
        return receiveBy;
    }

    public void setReceiveBy(int receiveBy) {
        this.receiveBy = receiveBy;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getMoneyBoxId() {
        return moneyBoxId;
    }

    public void setMoneyBoxId(String moneyBoxId) {
        this.moneyBoxId = moneyBoxId;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }
}
