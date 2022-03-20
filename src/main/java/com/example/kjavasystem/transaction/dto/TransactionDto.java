package com.example.kjavasystem.transaction.dto;


public class TransactionDto {
    private int id;
    private String moneyBoxId;
    private double totalMoney;
    private String status;
    private String receiverBranch;
    private String senderBranch;

    public TransactionDto() {
    }

    public String getReceiverBranch() {
        return receiverBranch;
    }

    public void setReceiverBranch(String receiverBranch) {
        this.receiverBranch = receiverBranch;
    }

    public String getSenderBranch() {
        return senderBranch;
    }

    public void setSenderBranch(String senderBranch) {
        this.senderBranch = senderBranch;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
