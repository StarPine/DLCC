package com.fine.friendlycc.ui.message.chatdetail;

import java.io.Serializable;

public class CustomMessageData implements Serializable {
    // 经纬度位置
    final static int TYPE_LOCATION = 1001;
    // 阅后即焚
    final static int TYPE_BURN = 1002;
    // 现金红包
    final static int TYPE_RED_PACKAGE = 1003;
    // 钻石红包
    final static int TYPE_COIN_RED_PACKAGE = 1004;
    //图片消息
    final static int TYPE_CUSTOM_IMAGE = 2001;

    int type = 0;

    private String senderUserID;

    private String msgId;
    private Integer id;
    private String text;

    private double lat;
    private double lng;
    private String address;

    private int number;

    private String imgPath;

    //提供给ios用
    private float imgWidth;
    private float imgHeight;

    private CustomMessageData() {
    }

    public static CustomMessageData genLocationMessage(String name, String address, double lat, double lng) {
        CustomMessageData customMessageData = new CustomMessageData();
        customMessageData.setType(TYPE_LOCATION);
        customMessageData.setText(name);
        customMessageData.setAddress(address);
        customMessageData.setLat(lat);
        customMessageData.setLng(lng);
        return customMessageData;
    }

    public static CustomMessageData genCoinRedPackageMessage(int id, int number, String desc) {
        CustomMessageData customMessageData = new CustomMessageData();
        customMessageData.setType(TYPE_COIN_RED_PACKAGE);
        customMessageData.setId(id);
        customMessageData.setText(desc);
        customMessageData.setNumber(number);
        return customMessageData;
    }

    public static CustomMessageData genBurnMessage(String imgPath) {
        CustomMessageData customMessageData = new CustomMessageData();
        customMessageData.setType(TYPE_BURN);
        customMessageData.setImgPath(imgPath);
        return customMessageData;
    }

    public static CustomMessageData genCustomMessage(String imgPath, int type_custom) {
        CustomMessageData customMessageData = new CustomMessageData();
        customMessageData.setType(type_custom);
        customMessageData.setImgPath(imgPath);
        return customMessageData;
    }

    public String getSenderUserID() {
        return senderUserID;
    }

    public void setSenderUserID(String senderUserID) {
        this.senderUserID = senderUserID;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public float getImgWidth() {
        return imgWidth;
    }

    public void setImgWidth(float imgWidth) {
        this.imgWidth = imgWidth;
    }

    public float getImgHeight() {
        return imgHeight;
    }

    public void setImgHeight(float imgHeight) {
        this.imgHeight = imgHeight;
    }
}