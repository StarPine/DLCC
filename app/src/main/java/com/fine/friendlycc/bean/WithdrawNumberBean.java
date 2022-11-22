package com.fine.friendlycc.bean;

import com.google.gson.annotations.SerializedName;

public class WithdrawNumberBean {

    @SerializedName("total_balance")
    private String totalBalance;
    private String money;
    private String balance;

    public String getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(String totalBalance) {
        this.totalBalance = totalBalance;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}