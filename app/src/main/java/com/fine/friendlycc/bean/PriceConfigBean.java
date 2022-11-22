package com.fine.friendlycc.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Author: 彭石林
 * Time: 2021/12/20 14:35
 * Description: IM聊天价格配置
 */
public class PriceConfigBean {
    @SerializedName("is_follow")
    private Integer isFollow;
    private Current current;
    private Current other;
    //谁付费 0当前不付费 1付费
    @SerializedName("is_pay")
    private Integer isPay;

    public Integer getIsFollow() {
        return isFollow;
    }

    public void setIsFollow(Integer isFollow) {
        this.isFollow = isFollow;
    }

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }

    public Current getOther() {
        return other;
    }

    public void setOther(Current other) {
        this.other = other;
    }

    public Integer getIsPay() {
        return isPay;
    }

    public void setIsPay(Integer isPay) {
        this.isPay = isPay;
    }

    //男
    public class Current implements Serializable {
        private Integer balance;
        private Integer sex;
        @SerializedName("prop_total")
        private Integer propTotal;
        @SerializedName("refund_msg_number")
        private Integer refundMsgNumber;
        private Integer certification;

        private String videoProfitTips;
        private String videoTips;
        private String audioProfitTips;
        @SerializedName("text_price")
        private Integer textPrice;
        //发送照片、视频价格模板
        private MediaPayPerConfigBean mediaPayPerConfig;

        //发送付费照片、适配开关
        private MediaGallerySwitchBean mediaPayDenyPer;

        public MediaGallerySwitchBean getMediaPayDenyPer() {
            return mediaPayDenyPer;
        }

        public void setMediaPayDenyPer(MediaGallerySwitchBean mediaPayDenyPer) {
            this.mediaPayDenyPer = mediaPayDenyPer;
        }

        public MediaPayPerConfigBean getMediaPayPerConfig() {
            return mediaPayPerConfig;
        }

        public void setMediaPayPerConfig(MediaPayPerConfigBean mediaPayPerConfig) {
            this.mediaPayPerConfig = mediaPayPerConfig;
        }
        private int allowVideo;
        private int allowAudio;

        public String getVideoTips() {
            return videoTips;
        }

        public void setVideoTips(String videoTips) {
            this.videoTips = videoTips;
        }

        public Integer getBalance() {
            return balance;
        }

        public void setBalance(Integer balance) {
            this.balance = balance;
        }

        public Integer getSex() {
            return sex;
        }

        public void setSex(Integer sex) {
            this.sex = sex;
        }

        public Integer getPropTotal() {
            return propTotal;
        }

        public void setPropTotal(Integer propTotal) {
            this.propTotal = propTotal;
        }

        public Integer getRefundMsgNumber() {
            return refundMsgNumber;
        }

        public void setRefundMsgNumber(Integer refundMsgNumber) {
            this.refundMsgNumber = refundMsgNumber;
        }

        public Integer getCertification() {
            return certification;
        }

        public void setCertification(Integer certification) {
            this.certification = certification;
        }

        public String getVideoProfitTips() {
            return videoProfitTips;
        }

        public void setVideoProfitTips(String videoProfitTips) {
            this.videoProfitTips = videoProfitTips;
        }

        public String getAudioProfitTips() {
            return audioProfitTips;
        }

        public void setAudioProfitTips(String audioProfitTips) {
            this.audioProfitTips = audioProfitTips;
        }

        public Integer getTextPrice() {
            return textPrice;
        }

        public void setTextPrice(Integer textPrice) {
            this.textPrice = textPrice;
        }

        @Override
        public String toString() {
            return "Current{" +
                    "balance=" + balance +
                    ", sex=" + sex +
                    ", propTotal=" + propTotal +
                    ", refundMsgNumber=" + refundMsgNumber +
                    ", certification=" + certification +
                    ", videoProfitTips='" + videoProfitTips + '\'' +
                    ", audioProfitTips='" + audioProfitTips + '\'' +
                    ", textPrice=" + textPrice +
                    '}';
        }

        public int getAllowVideo() {
            return allowVideo;
        }

        public void setAllowVideo(int allowVideo) {
            this.allowVideo = allowVideo;
        }

        public int getAllowAudio() {
            return allowAudio;
        }

        public void setAllowAudio(int allowAudio) {
            this.allowAudio = allowAudio;
        }
    }

    @Override
    public String toString() {
        return "PriceConfigBean{" +
                "isFollow=" + isFollow +
                ", current=" + current +
                ", other=" + other +
                '}';
    }
}