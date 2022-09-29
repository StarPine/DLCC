package com.dl.playfun.event;

/**
 * Author: 彭石林
 * Time: 2022/9/5 17:24
 * Description: 推币机消费者事件
 */
public class CoinPusherGamePlayingEvent {
    //当前事件状态： CustomConstants.CoinPusher
    private String state;
    //中奖落币数值
    private Integer goldNumber;
    //用户当前总余额
    private Integer totalGold;

    public CoinPusherGamePlayingEvent(String state) {
        this.state = state;
    }

    public CoinPusherGamePlayingEvent(String state, Integer goldNumber,Integer totalGold) {
        this.state = state;
        this.goldNumber = goldNumber;
        this.totalGold = totalGold;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getGoldNumber() {
        return goldNumber;
    }

    public void setGoldNumber(Integer goldNumber) {
        this.goldNumber = goldNumber;
    }

    public Integer getTotalGold() {
        return totalGold;
    }

    public void setTotalGold(Integer totalGold) {
        this.totalGold = totalGold;
    }
}
