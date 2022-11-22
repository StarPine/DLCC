package com.fine.friendlycc.bean;

public class SystemConfigContentBean {

    private String FormGiveChatDay;
    private String GetAccountMoney;
    private String GetAccountMoneyVip;
    private String GiveChatNumber;
    private String NewUserChatDay;
    private String RobotAutoReply;
    private String SayHelloMoney;
    private String ToGiveChatDay;

    private Integer ReadLineTime;

    private Integer GetUserHomeMoney;//解锁用户主页所需钻石

    private Integer recommendClose = 0; //每日推荐弹窗(开启关闭)
    private Integer recommendCloseTime = 15; //每日推荐弹窗-自动关闭倒计时（15s）
    private Integer recommendOneTime = 30; //第一次出现弹窗时间（登录后30s）
    private Integer recommendTwoTime = 300; //第二次出现弹窗时间（登录后5min）

    private Integer GetViewUseBrowseMoney;//解锁男性-谁查看过我消耗钻石


    public Integer getGetViewUseBrowseMoney() {
        return GetViewUseBrowseMoney;
    }

    public void setGetViewUseBrowseMoney(Integer getViewUseBrowseMoney) {
        GetViewUseBrowseMoney = getViewUseBrowseMoney;
    }

    public Integer getRecommendClose() {
        return recommendClose;
    }

    public void setRecommendClose(Integer recommendClose) {
        this.recommendClose = recommendClose;
    }

    public Integer getRecommendCloseTime() {
        return recommendCloseTime;
    }

    public void setRecommendCloseTime(Integer recommendCloseTime) {
        this.recommendCloseTime = recommendCloseTime;
    }

    public Integer getRecommendOneTime() {
        return recommendOneTime;
    }

    public void setRecommendOneTime(Integer recommendOneTime) {
        this.recommendOneTime = recommendOneTime;
    }

    public Integer getRecommendTwoTime() {
        return recommendTwoTime;
    }

    public void setRecommendTwoTime(Integer recommendTwoTime) {
        this.recommendTwoTime = recommendTwoTime;
    }

    public Integer getGetUserHomeMoney() {
        return GetUserHomeMoney;
    }

    public void setGetUserHomeMoney(Integer getUserHomeMoney) {
        GetUserHomeMoney = getUserHomeMoney;
    }

    public Integer getReadLineTime() {
        return ReadLineTime;
    }

    public void setReadLineTime(Integer readLineTime) {
        ReadLineTime = readLineTime;
    }

    public String getFormGiveChatDay() {
        return FormGiveChatDay;
    }

    public void setFormGiveChatDay(String formGiveChatDay) {
        FormGiveChatDay = formGiveChatDay;
    }

    public String getGetAccountMoney() {
        return GetAccountMoney;
    }

    public void setGetAccountMoney(String getAccountMoney) {
        GetAccountMoney = getAccountMoney;
    }

    public String getGetAccountMoneyVip() {
        return GetAccountMoneyVip;
    }

    public void setGetAccountMoneyVip(String getAccountMoneyVip) {
        GetAccountMoneyVip = getAccountMoneyVip;
    }

    public String getGiveChatNumber() {
        return GiveChatNumber;
    }

    public void setGiveChatNumber(String giveChatNumber) {
        GiveChatNumber = giveChatNumber;
    }

    public String getNewUserChatDay() {
        return NewUserChatDay;
    }

    public void setNewUserChatDay(String newUserChatDay) {
        NewUserChatDay = newUserChatDay;
    }

    public String getRobotAutoReply() {
        return RobotAutoReply;
    }

    public void setRobotAutoReply(String robotAutoReply) {
        RobotAutoReply = robotAutoReply;
    }

    public String getSayHelloMoney() {
        return SayHelloMoney;
    }

    public void setSayHelloMoney(String sayHelloMoney) {
        SayHelloMoney = sayHelloMoney;
    }

    public String getToGiveChatDay() {
        return ToGiveChatDay;
    }

    public void setToGiveChatDay(String toGiveChatDay) {
        ToGiveChatDay = toGiveChatDay;
    }
}