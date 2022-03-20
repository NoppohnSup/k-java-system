package com.example.kjavasystem.management.reqeust;

public class UpdateBankMoneyRequest {
    private int branchId;
    private int employeeId;
    private double money;

    public UpdateBankMoneyRequest() {
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }
}
