package com.dl.playfun.entity;

/**
 * Author: 彭石林
 * Time: 2022/7/5 19:07
 * Description: This is MqBroadcastGiftEntity
 */
public class MqBroadcastGiftEntity {

    public MqBroadcastGiftUserEntity fromUser;

    public MqBroadcastGiftUserEntity toUser;

    public String giftName;

    public String imagePath;

    public String svgaPath;

    public int amount;

    public MqBroadcastGiftUserEntity getFromUser() {
        return fromUser;
    }

    public void setFromUser(MqBroadcastGiftUserEntity fromUser) {
        this.fromUser = fromUser;
    }

    public MqBroadcastGiftUserEntity getToUser() {
        return toUser;
    }

    public void setToUser(MqBroadcastGiftUserEntity toUser) {
        this.toUser = toUser;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getSvgaPath() {
        return svgaPath;
    }

    public void setSvgaPath(String svgaPath) {
        this.svgaPath = svgaPath;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "MqBroadcastGiftEntity{" +
                "fromUser=" + fromUser.toString() +
                ", toUser=" + toUser +
                ", giftName='" + giftName + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", svgaPath='" + svgaPath + '\'' +
                ", amount=" + amount +
                '}';
    }
}
