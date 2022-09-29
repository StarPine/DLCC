package com.tencent.qcloud.tuicore.custom.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Author: 彭石林
 * Time: 2022/9/14 18:59
 * Description: 发送图片、视频信息类
 */
public class MediaGalleryEditEntity implements Serializable {
    //IM聊天信息ID
    private String msgKeyId;
    //是否是视频
    private boolean isVideoSetting;
    //是否快照
    private boolean stateSnapshot;
    //是否付费
    private boolean statePay;
    //是否解锁
    private boolean stateUnlockPhoto;
    //解锁金额
    private BigDecimal unlockPrice;
    //oss相对地址
    private String srcPath;
    //对方ID
    private Integer toUserId;
    //聊天收益
    private BigDecimal msgRenvenue;
    //是否当前用户发送
    private boolean selfSend;

    //价格配置id，当类型为付费资源时，该值为必填
    private Integer configId;
    //价格配置下标，当类型为付费资源时，该值为必填
    private String configIndex;
    //安卓端本地资源绝对地址
    private String androidLocalSrcPath;

    //是否已读查看
    private boolean isReadLook;

    public boolean isReadLook() {
        return isReadLook;
    }

    public void setReadLook(boolean readLook) {
        isReadLook = readLook;
    }

    public String getAndroidLocalSrcPath() {
        return androidLocalSrcPath;
    }

    public void setAndroidLocalSrcPath(String androidLocalSrcPath) {
        this.androidLocalSrcPath = androidLocalSrcPath;
    }

    public boolean isSelfSend() {
        return selfSend;
    }

    public void setSelfSend(boolean selfSend) {
        this.selfSend = selfSend;
    }

    public Integer getConfigId() {
        return configId;
    }

    public void setConfigId(Integer configId) {
        this.configId = configId;
    }

    public String getConfigIndex() {
        return configIndex;
    }

    public void setConfigIndex(String configIndex) {
        this.configIndex = configIndex;
    }

    public BigDecimal getMsgRenvenue() {
        return msgRenvenue;
    }

    public void setMsgRenvenue(BigDecimal msgRenvenue) {
        this.msgRenvenue = msgRenvenue;
    }

    public Integer getToUserId() {
        return toUserId;
    }

    public void setToUserId(Integer toUserId) {
        this.toUserId = toUserId;
    }

    public String getMsgKeyId() {
        return msgKeyId;
    }

    public void setMsgKeyId(String msgKeyId) {
        this.msgKeyId = msgKeyId;
    }

    public boolean isVideoSetting() {
        return isVideoSetting;
    }

    public void setVideoSetting(boolean videoSetting) {
        isVideoSetting = videoSetting;
    }

    public boolean isStateSnapshot() {
        return stateSnapshot;
    }

    public void setStateSnapshot(boolean stateSnapshot) {
        this.stateSnapshot = stateSnapshot;
    }

    public boolean isStatePay() {
        return statePay;
    }

    public void setStatePay(boolean statePay) {
        this.statePay = statePay;
    }

    public boolean isStateUnlockPhoto() {
        return stateUnlockPhoto;
    }

    public void setStateUnlockPhoto(boolean stateUnlockPhoto) {
        this.stateUnlockPhoto = stateUnlockPhoto;
    }

    public BigDecimal getUnlockPrice() {
        return unlockPrice;
    }

    public void setUnlockPrice(BigDecimal unlockPrice) {
        this.unlockPrice = unlockPrice;
    }

    public String getSrcPath() {
        return srcPath;
    }

    public void setSrcPath(String srcPath) {
        this.srcPath = srcPath;
    }

    @Override
    public String toString() {
        return "MediaGalleryEditEntity{" +
                "isVideoSetting=" + isVideoSetting +
                ", stateSnapshot=" + stateSnapshot +
                ", statePay=" + statePay +
                ", stateUnlockPhoto=" + stateUnlockPhoto +
                ", unlockPrice=" + unlockPrice +
                ", srcPath='" + srcPath + '\'' +
                '}';
    }
}
