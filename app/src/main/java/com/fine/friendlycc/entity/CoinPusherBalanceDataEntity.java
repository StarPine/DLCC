package com.fine.friendlycc.entity;

/**
 * Author: 彭石林
 * Time: 2022/9/1 10:38
 * Description: 推币机-用户当前账户余额
 */
public class CoinPusherBalanceDataEntity {
    //钻石余额
    private int totalDiamond;
    //金币余额
    private int totalGold;

    public int getTotalDiamond() {
        return totalDiamond;
    }

    public void setTotalDiamond(int totalDiamond) {
        this.totalDiamond = totalDiamond;
    }

    public int getTotalGold() {
        return totalGold;
    }

    public void setTotalGold(int totalGold) {
        this.totalGold = totalGold;
    }
}
