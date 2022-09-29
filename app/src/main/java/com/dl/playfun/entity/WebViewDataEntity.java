package com.dl.playfun.entity;

import java.io.Serializable;

/**
 * Author: 彭石林
 * Time: 2022/9/23 20:04
 * Description: H5交互数据实体层
 */
public class WebViewDataEntity implements Serializable{
    //用户信息
    private UserDataEntity userInfo;

    //当前配置信息
    private SettingInfo settingInfo;

    public SettingInfo getSettingInfo() {
        return settingInfo;
    }

    public void setSettingInfo(SettingInfo settingInfo) {
        this.settingInfo = settingInfo;
    }

    public UserDataEntity getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserDataEntity userInfo) {
        this.userInfo = userInfo;
    }

    public static class SettingInfo implements Serializable {
        //语言
        private String currentLanguage;
        //版本号
        private String currentVersion;
        //当前用户token
        private String currentToken;
        private String appId;
        //ios暗黑模式 true为暗黑  false 非暗黑
        private boolean isDark;

        public String getCurrentLanguage() {
            return currentLanguage;
        }

        public void setCurrentLanguage(String currentLanguage) {
            this.currentLanguage = currentLanguage;
        }

        public String getCurrentVersion() {
            return currentVersion;
        }

        public void setCurrentVersion(String currentVersion) {
            this.currentVersion = currentVersion;
        }

        public String getCurrentToken() {
            return currentToken;
        }

        public void setCurrentToken(String currentToken) {
            this.currentToken = currentToken;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public boolean isDark() {
            return isDark;
        }

        public void setDark(boolean dark) {
            isDark = dark;
        }
    }
}
