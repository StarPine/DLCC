package com.fine.friendlycc.bean;

import java.util.List;

/**
 * 等级钻石数选择框信息
 *
 * @author ccz
 */
public class LevelCoinSelectInfo {

    /**
     * 等级选项
     */
    private List<LevelCoinOptionInfo> options;

    /**
     * 第一个选项钻石数
     */
    private Integer firstCoins;

    /**
     * 选中的等级选项钻石数
     */
    private Integer selectedCoins;

    /**
     * 已解锁的选项钻石数
     */
    private Integer unlockedCoins;

    /**
     * 是否可修改:0不可修改;1可以修改
     */
    private Integer enableChange;

    public List<LevelCoinOptionInfo> getOptions() {
        return options;
    }

    public void setOptions(List<LevelCoinOptionInfo> options) {
        this.options = options;
    }

    public Integer getFirstCoins() {
        return firstCoins;
    }

    public void setFirstCoins(Integer firstCoins) {
        this.firstCoins = firstCoins;
    }

    public Integer getSelectedCoins() {
        return selectedCoins;
    }

    public void setSelectedCoins(Integer selectedCoins) {
        this.selectedCoins = selectedCoins;
    }

    public Integer getUnlockedCoins() {
        return unlockedCoins;
    }

    public void setUnlockedCoins(Integer unlockedCoins) {
        this.unlockedCoins = unlockedCoins;
    }

    public Integer getEnableChange() {
        return enableChange;
    }

    public void setEnableChange(Integer enableChange) {
        this.enableChange = enableChange;
    }


}