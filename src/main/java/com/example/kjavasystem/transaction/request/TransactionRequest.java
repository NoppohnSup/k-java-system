package com.example.kjavasystem.transaction.request;

public class TransactionRequest {
    private int cratedBy;
    private double totalMoney;
    private String status;
    private int receiveBranchId;
    private int senderBranchId;

    public TransactionRequest() {
    }

    public int getCratedBy() {
        return cratedBy;
    }

    public void setCratedBy(int cratedBy) {
        this.cratedBy = cratedBy;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getReceiveBranchId() {
        return receiveBranchId;
    }

    public void setReceiveBranchId(int receiveBranchId) {
        this.receiveBranchId = receiveBranchId;
    }

    public int getSenderBranchId() {
        return senderBranchId;
    }

    public void setSenderBranchId(int senderBranchId) {
        this.senderBranchId = senderBranchId;
    }
}
