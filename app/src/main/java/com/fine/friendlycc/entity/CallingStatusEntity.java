package com.fine.friendlycc.entity;

import java.math.BigDecimal;

/**
 * @Name： PlayChat_Google
 * @Description：
 * @Author： liaosf
 * @Date： 2022/4/20 11:13
 * 修改备注：
 */
public class CallingStatusEntity {
    /**
     * roomId : 1901314475
     * roomStatus : 101
     * totalMinutes : 246
     * elapsedMinutes : 53
     * remainingMinutes : 193
     * frozenCoins : 0
     * payerCoinBalance : 12345
     * payeeProfits : 269.06
     */

    private Integer roomId;
    private Integer roomStatus;//房间状态，-1已取消，0未收到创建回调，101已创建房间，102已解散房间
    private Integer totalMinutes;//当前通话最多可通话分钟数
    private Integer elapsedMinutes;//已通话分钟数，包括当前这一分钟
    private Integer remainingMinutes;//剩余分钟数，不包括当前这一分钟
    private Integer frozenCoins;//已冻结钻石数量
    private Integer payerCoinBalance;//付款人钻石余额，已减去冻结钻石数量
    private BigDecimal payeeProfits;//收款人收益

    public CallingStatusEntity(Integer roomId, Integer roomStatus, Integer totalMinutes, Integer elapsedMinutes, Integer remainingMinutes, Integer frozenCoins, Integer payerCoinBalance, BigDecimal payeeProfits) {
        this.roomId = roomId;
        this.roomStatus = roomStatus;
        this.totalMinutes = totalMinutes;
        this.elapsedMinutes = elapsedMinutes;
        this.remainingMinutes = remainingMinutes;
        this.frozenCoins = frozenCoins;
        this.payerCoinBalance = payerCoinBalance;
        this.payeeProfits = payeeProfits;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Integer getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(Integer roomStatus) {
        this.roomStatus = roomStatus;
    }

    public Integer getTotalMinutes() {
        return totalMinutes;
    }

    public void setTotalMinutes(Integer totalMinutes) {
        this.totalMinutes = totalMinutes;
    }

    public Integer getElapsedMinutes() {
        return elapsedMinutes;
    }

    public void setElapsedMinutes(Integer elapsedMinutes) {
        this.elapsedMinutes = elapsedMinutes;
    }

    public Integer getRemainingMinutes() {
        return remainingMinutes;
    }

    public void setRemainingMinutes(Integer remainingMinutes) {
        this.remainingMinutes = remainingMinutes;
    }

    public Integer getFrozenCoins() {
        return frozenCoins;
    }

    public void setFrozenCoins(Integer frozenCoins) {
        this.frozenCoins = frozenCoins;
    }

    public Integer getPayerCoinBalance() {
        return payerCoinBalance;
    }

    public void setPayerCoinBalance(Integer payerCoinBalance) {
        this.payerCoinBalance = payerCoinBalance;
    }

    public BigDecimal getPayeeProfits() {
        return payeeProfits;
    }

    public void setPayeeProfits(BigDecimal payeeProfits) {
        this.payeeProfits = payeeProfits;
    }
}
