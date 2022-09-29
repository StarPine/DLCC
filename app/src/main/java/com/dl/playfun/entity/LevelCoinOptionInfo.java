package com.dl.playfun.entity;

import java.math.BigDecimal;

/**
 * 等级钻石数选项信息
 *
 * @author ccz
 */
public class LevelCoinOptionInfo {

    /**
     * 级别
     */
    private Integer level;

    /**
     * 钻石
     */
    private Integer coins;

    /**
     * 水晶
     */
    private BigDecimal profits;

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getCoins() {
        return coins;
    }

    public void setCoins(Integer coins) {
        this.coins = coins;
    }

    public BigDecimal getProfits() {
        return profits;
    }

    public void setProfits(BigDecimal profits) {
        this.profits = profits;
    }
}