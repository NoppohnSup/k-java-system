package com.example.kjavasystem.transaction.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "money_box_id")
    private String moneyBoxId;
    @Column(name = "total_money")
    private double totalMoney;
    @Column(name = "status")
    private String status;
    @Column(name = "created_at")
    private Timestamp createdAt;
    @Column(name = "updated_at")
    private Timestamp updatedAt;
    @Column(name = "sender_branch_id")
    private int senderBranchId;
    @Column(name = "receive_branch_id")
    private int receiveBranchId;
    @Column(name = "transfer_by")
    private int transferBy;
    @Column(name = "receiver_by")
    private int receiverBy;
    @Column(name = "created_by")
    private int createdBy;

    public Transaction() {
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getSenderBranchId() {
        return senderBranchId;
    }

    public void setSenderBranchId(int senderBranchId) {
        this.senderBranchId = senderBranchId;
    }

    public int getReceiveBranchId() {
        return receiveBranchId;
    }

    public void setReceiveBranchId(int receiveBranchId) {
        this.receiveBranchId = receiveBranchId;
    }

    public int getTransferBy() {
        return transferBy;
    }

    public void setTransferBy(int transferBy) {
        this.transferBy = transferBy;
    }

    public int getReceiverBy() {
        return receiverBy;
    }

    public void setReceiverBy(int receiverBy) {
        this.receiverBy = receiverBy;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }
}
