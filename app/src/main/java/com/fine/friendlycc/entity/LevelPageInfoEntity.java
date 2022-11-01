package com.fine.friendlycc.entity;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

/**
 * Author: 彭石林
 * Time: 2022/6/21 11:59
 * Description: 用户等级权益相关类
 */
public class LevelPageInfoEntity {
    //等级权益列表
    private LevelSelectInfoEntity levelSelectInfo;
    //提示文案如：每天只能修改1次
    private String modifyRestrictionTips;

    //用户等级信息
    private UserLevelInfo userLevelInfo;
    //文字聊天
    private LevelCoinSelectInfo chatSelectInfo;
    //语音聊天
    @SerializedName("voiceSelectInfo")
    private LevelCoinSelectInfo audioSelectInfo;
    //视频聊天
    private LevelCoinSelectInfo videoSelectInfo;

    public LevelSelectInfoEntity getLevelSelectInfo() {
        return levelSelectInfo;
    }

    public void setLevelSelectInfo(LevelSelectInfoEntity levelSelectInfo) {
        this.levelSelectInfo = levelSelectInfo;
    }

    public String getModifyRestrictionTips() {
        return modifyRestrictionTips;
    }

    public void setModifyRestrictionTips(String modifyRestrictionTips) {
        this.modifyRestrictionTips = modifyRestrictionTips;
    }

    public UserLevelInfo getUserLevelInfo() {
        return userLevelInfo;
    }

    public void setUserLevelInfo(UserLevelInfo userLevelInfo) {
        this.userLevelInfo = userLevelInfo;
    }

    public LevelCoinSelectInfo getChatSelectInfo() {
        return chatSelectInfo;
    }

    public void setChatSelectInfo(LevelCoinSelectInfo chatSelectInfo) {
        this.chatSelectInfo = chatSelectInfo;
    }

    public LevelCoinSelectInfo getAudioSelectInfo() {
        return audioSelectInfo;
    }

    public void setAudioSelectInfo(LevelCoinSelectInfo audioSelectInfo) {
        this.audioSelectInfo = audioSelectInfo;
    }

    public LevelCoinSelectInfo getVideoSelectInfo() {
        return videoSelectInfo;
    }

    public void setVideoSelectInfo(LevelCoinSelectInfo videoSelectInfo) {
        this.videoSelectInfo = videoSelectInfo;
    }

    /**
     * 用户等级信息
     *
     * @author ccz
     */
    public class UserLevelInfo {

        /**
         * 等级
         */
        private Integer level;

        /**
         * 魅力值
         */
        private BigDecimal charmValue;

        /**
         * 讯息钻石
         */
        private Integer chatCoins;

        /**
         * 讯息水晶
         */
        private BigDecimal chatProfits;

        /**
         * 语音钻石
         */
        private Integer voiceCoins;

        /**
         * 语音水晶
         */
        private BigDecimal voiceProfits;

        /**
         * 视讯钻石
         */
        private Integer videoCoins;

        /**
         * 视讯水晶
         */
        private BigDecimal videoProfits;

        public Integer getLevel() {
            return level;
        }

        public void setLevel(Integer level) {
            this.level = level;
        }

        public BigDecimal getCharmValue() {
            return charmValue;
        }

        public void setCharmValue(BigDecimal charmValue) {
            this.charmValue = charmValue;
        }

        public Integer getChatCoins() {
            return chatCoins;
        }

        public void setChatCoins(Integer chatCoins) {
            this.chatCoins = chatCoins;
        }

        public BigDecimal getChatProfits() {
            return chatProfits;
        }

        public void setChatProfits(BigDecimal chatProfits) {
            this.chatProfits = chatProfits;
        }

        public Integer getVoiceCoins() {
            return voiceCoins;
        }

        public void setVoiceCoins(Integer voiceCoins) {
            this.voiceCoins = voiceCoins;
        }

        public BigDecimal getVoiceProfits() {
            return voiceProfits;
        }

        public void setVoiceProfits(BigDecimal voiceProfits) {
            this.voiceProfits = voiceProfits;
        }

        public Integer getVideoCoins() {
            return videoCoins;
        }

        public void setVideoCoins(Integer videoCoins) {
            this.videoCoins = videoCoins;
        }

        public BigDecimal getVideoProfits() {
            return videoProfits;
        }

        public void setVideoProfits(BigDecimal videoProfits) {
            this.videoProfits = videoProfits;
        }
    }

    /**
     * 钻石范围信息
     *
     * @author ccz
     */
    public class CoinsRangeInfo {
        private Integer from;
        private Integer to;

        public Integer getFrom() {
            return from;
        }

        public void setFrom(Integer from) {
            this.from = from;
        }

        public Integer getTo() {
            return to;
        }

        public void setTo(Integer to) {
            this.to = to;
        }
    }
}
