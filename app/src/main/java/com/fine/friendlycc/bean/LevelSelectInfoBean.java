package com.fine.friendlycc.bean;

import java.math.BigDecimal;
import java.util.List;

/**
 * Author: 彭石林
 * Time: 2022/6/21 15:12
 * Description: This is LevelSelectInfoBean
 */
public class LevelSelectInfoBean {
    private Integer userLevel;
    private BigDecimal userCharmValue;
    private List<LevelInfo> levelList;
    private List<LevelTips> levelTipsList;


    public Integer getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(Integer userLevel) {
        this.userLevel = userLevel;
    }

    public BigDecimal getUserCharmValue() {
        return userCharmValue;
    }

    public void setUserCharmValue(BigDecimal userCharmValue) {
        this.userCharmValue = userCharmValue;
    }

    public List<LevelInfo> getLevelList() {
        return levelList;
    }

    public void setLevelList(List<LevelInfo> levelList) {
        this.levelList = levelList;
    }

    public List<LevelTips> getLevelTipsList() {
        return levelTipsList;
    }

    public void setLevelTipsList(List<LevelTips> levelTipsList) {
        this.levelTipsList = levelTipsList;
    }

    /**
     * 等级信息
     *
     * @author ccz
     */
    public class LevelInfo {

        /**
         * 等级
         */
        private Integer level;

        /**
         * 等级名称
         */
        private String levelName;

        /**
         * 等级图片
         */
        private String levelImage;

        /**
         * 等级小图标
         */
        private String levelIcon;

        /**
         * 从魅力值(包含)
         */
        private Integer fromCharmValue;

        /**
         * 到魅力值(不包含)
         */
        private Integer toCharmValue;

        /**
         * 讯息钻石范围
         */
        private LevelPageInfoBean.CoinsRangeInfo chatCoinsRange;

        /**
         * 语音钻石范围
         */
        private LevelPageInfoBean.CoinsRangeInfo voiceCoinsRange;

        /**
         * 视讯钻石范围
         */
        private LevelPageInfoBean.CoinsRangeInfo videoCoinsRange;

        public Integer getLevel() {
            return level;
        }

        public void setLevel(Integer level) {
            this.level = level;
        }

        public String getLevelName() {
            return levelName;
        }

        public void setLevelName(String levelName) {
            this.levelName = levelName;
        }

        public String getLevelImage() {
            return levelImage;
        }

        public void setLevelImage(String levelImage) {
            this.levelImage = levelImage;
        }

        public String getLevelIcon() {
            return levelIcon;
        }

        public void setLevelIcon(String levelIcon) {
            this.levelIcon = levelIcon;
        }

        public Integer getFromCharmValue() {
            return fromCharmValue;
        }

        public void setFromCharmValue(Integer fromCharmValue) {
            this.fromCharmValue = fromCharmValue;
        }

        public Integer getToCharmValue() {
            return toCharmValue;
        }

        public void setToCharmValue(Integer toCharmValue) {
            this.toCharmValue = toCharmValue;
        }

        public LevelPageInfoBean.CoinsRangeInfo getChatCoinsRange() {
            return chatCoinsRange;
        }

        public void setChatCoinsRange(LevelPageInfoBean.CoinsRangeInfo chatCoinsRange) {
            this.chatCoinsRange = chatCoinsRange;
        }

        public LevelPageInfoBean.CoinsRangeInfo getVoiceCoinsRange() {
            return voiceCoinsRange;
        }

        public void setVoiceCoinsRange(LevelPageInfoBean.CoinsRangeInfo voiceCoinsRange) {
            this.voiceCoinsRange = voiceCoinsRange;
        }

        public LevelPageInfoBean.CoinsRangeInfo getVideoCoinsRange() {
            return videoCoinsRange;
        }

        public void setVideoCoinsRange(LevelPageInfoBean.CoinsRangeInfo videoCoinsRange) {
            this.videoCoinsRange = videoCoinsRange;
        }
    }

    //等级提示
    public class LevelTips {
        public int level;
        public String tips;

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public String getTips() {
            return tips;
        }

        public void setTips(String tips) {
            this.tips = tips;
        }
    }

}