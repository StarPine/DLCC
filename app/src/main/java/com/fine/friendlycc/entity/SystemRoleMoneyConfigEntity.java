package com.fine.friendlycc.entity;

public class SystemRoleMoneyConfigEntity {

    //ImMoney 私聊价格 ImgTime 阅后即时间 ImNumber 聊天次数 NewsMoney 发动态价格 TopicalMoney 发节目价格 BrowseHomeNumber 可浏览主页次
    private int ImMoney;
    private int ImgTime;
    private int ImNumber;
    private int NewsMoney;
    private int TopicalMoney;
    private int BrowseHomeNumber;


    private Integer ViewMessagesNumber;
    private Integer SendMessagesNumber;


    public Integer getViewMessagesNumber() {
        return ViewMessagesNumber;
    }

    public void setViewMessagesNumber(Integer viewMessagesNumber) {
        ViewMessagesNumber = viewMessagesNumber;
    }

    public Integer getSendMessagesNumber() {
        return SendMessagesNumber;
    }

    public void setSendMessagesNumber(Integer sendMessagesNumber) {
        SendMessagesNumber = sendMessagesNumber;
    }

    public int getImMoney() {
        return ImMoney;
    }

    public void setImMoney(int ImMoney) {
        this.ImMoney = ImMoney;
    }

    public int getImgTime() {
        return ImgTime;
    }

    public void setImgTime(int ImgTime) {
        this.ImgTime = ImgTime;
    }

    public int getImNumber() {
        return ImNumber;
    }

    public void setImNumber(int ImNumber) {
        this.ImNumber = ImNumber;
    }

    public int getNewsMoney() {
        return NewsMoney;
    }

    public void setNewsMoney(int NewsMoney) {
        this.NewsMoney = NewsMoney;
    }

    public int getTopicalMoney() {
        return TopicalMoney;
    }

    public void setTopicalMoney(int TopicalMoney) {
        this.TopicalMoney = TopicalMoney;
    }

    public int getBrowseHomeNumber() {
        return BrowseHomeNumber;
    }

    public void setBrowseHomeNumber(int BrowseHomeNumber) {
        this.BrowseHomeNumber = BrowseHomeNumber;
    }
}
