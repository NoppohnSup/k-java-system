package com.example.kjavasystem.management.response;

public class TotalMoneyResponse {
    private int branchId;
    private double totalMoney;

    public TotalMoneyResponse() {
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }
}
