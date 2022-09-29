package com.tencent.custom;

import java.math.BigDecimal;

/**
 * Author: 彭石林
 * Time: 2022/9/12 15:43
 * Description: TIM发送付费照片 - 视频模块
 */
public class VideoGalleryPayEntity {
    //是否是付费视频 默认false
    private boolean stateVideoPay;
    //是否解锁视频
    private boolean stateUnlockVideo;
    //解锁金额
    private BigDecimal unlockPrice;
    //oss文件信息
    private String srcPath;
    //安卓端本地资源绝对地址
    private String androidLocalSrcPath;
    //价格配置id，当类型为付费资源时，该值为必填
    private Integer configId;
    //价格配置下标，当类型为付费资源时，该值为必填
    private String configIndexString;

    public Integer getConfigId() {
        return configId;
    }

    public void setConfigId(Integer configId) {
        this.configId = configId;
    }

    public String getConfigIndex() {
        return configIndexString;
    }

    public void setConfigIndex(String configIndex) {
        this.configIndexString = configIndex;
    }

    public String getAndroidLocalSrcPath() {
        return androidLocalSrcPath;
    }

    public void setAndroidLocalSrcPath(String androidLocalSrcPath) {
        this.androidLocalSrcPath = androidLocalSrcPath;
    }

    public boolean isStateVideoPay() {
        return stateVideoPay;
    }

    public void setStateVideoPay(boolean stateVideoPay) {
        this.stateVideoPay = stateVideoPay;
    }

    public boolean isStateUnlockVideo() {
        return stateUnlockVideo;
    }

    public void setStateUnlockVideo(boolean stateUnlockVideo) {
        this.stateUnlockVideo = stateUnlockVideo;
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

}
