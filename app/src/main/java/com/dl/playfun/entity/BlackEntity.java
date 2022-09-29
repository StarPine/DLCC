package com.dl.playfun.entity;

import com.google.gson.annotations.SerializedName;

public class BlackEntity {

    /**
     * id : 1
     * blacklist_user_id : 4
     * created_at : 2020-05-07 16:04:19
     * user : {"id":4,"nickname":"香港","avatar":null}
     */

    private int id;
    @SerializedName("blacklist_user_id")
    private int blacklistUserId;
    @SerializedName("created_at")
    private String createdAt;
    private UserBean user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBlacklistUserId() {
        return blacklistUserId;
    }

    public void setBlacklistUserId(int blacklistUserId) {
        this.blacklistUserId = blacklistUserId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public class UserBean {
        /**
         * id : 4
         * nickname : 香港
         * avatar : null
         */

        private int id;
        private String nickname;
        private Object avatar;
        private Integer certification;
        @SerializedName("is_vip")
        private Integer isVip;
        private Integer sex;

        public Integer getSex() {
            return sex;
        }

        public void setSex(Integer sex) {
            this.sex = sex;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public Object getAvatar() {
            return avatar;
        }

        public void setAvatar(Object avatar) {
            this.avatar = avatar;
        }

        public Integer getCertification() {
            return certification;
        }

        public void setCertification(Integer certification) {
            this.certification = certification;
        }

        public Integer getIsVip() {
            return isVip;
        }

        public void setIsVip(Integer isVip) {
            this.isVip = isVip;
        }
    }


}
