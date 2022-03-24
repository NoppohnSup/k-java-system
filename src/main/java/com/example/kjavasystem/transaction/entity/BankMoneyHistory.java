package com.example.kjavasystem.transaction.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "bank_money_history")
public class BankMoneyHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "bank_money_id")
    private int bankMoneyId;
    @Column(name = "total_money")
    private double totalMoney;
    @Column(name = "created_at")
    private Timestamp createdAt;
    @Column(name = "updated_at")
    private Timestamp updatedAt;
    @Column(name = "updated_by")
    private int updatedBy;
    @Column(name = "branch_id")
    private int branchId;
    @Column(name = "transaction_id")
    private int transactionId;

    public BankMoneyHistory() {
    }

    public int getBankMoneyId() {
        return bankMoneyId;
    }

    public void setBankMoneyId(int bankMoneyId) {
        this.bankMoneyId = bankMoneyId;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
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

    public int getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(int updatedBy) {
        this.updatedBy = updatedBy;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }
}
