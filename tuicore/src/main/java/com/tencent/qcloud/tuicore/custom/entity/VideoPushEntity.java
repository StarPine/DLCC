package com.tencent.qcloud.tuicore.custom.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 修改备注：
 *
 * @Name： PlayCC
 * @Description：
 * @Author： liaosf
 * @Date： 2022/9/8 16:20
 */
public class VideoPushEntity implements Serializable {

    /**
     * videoCallPushLogId : 1
     * userProfile : {"id":2542,"imId":"user_2542","nickname":"girlline","avatar":"images/avatar/fbb955d538f54b3684498e18c8bf7b51.jpg","sex":0,"isVip":0,"certification":0,"cityId":3,"cityName":"新北市","age":27,"constellation":"未知","occupationId":11,"occupation":"","status":0}
     * seconds : 10
     */

    private int videoCallPushLogId;
    private UserProfileBean userProfile;
    private int seconds;
    private int isShake;

    public int getVideoCallPushLogId() {
        return videoCallPushLogId;
    }

    public UserProfileBean getUserProfile() {
        return userProfile;
    }

    public int getSeconds() {
        return seconds;
    }

    public int getIsShake() {
        return isShake;
    }

    public static class UserProfileBean implements Serializable {
        /**
         * id : 2542
         * imId : user_2542
         * nickname : girlline
         * avatar : images/avatar/fbb955d538f54b3684498e18c8bf7b51.jpg
         * sex : 0
         * isVip : 0
         * certification : 0
         * cityId : 3
         * cityName : 新北市
         * age : 27
         * constellation : 未知
         * occupationId : 11
         * occupation :
         * status : 0
         */

        private int id;
        private String imId;
        private String nickname;
        private String avatar;
        private int sex;
        private int isVip;
        private int certification;
        private int cityId;
        private String cityName;
        private int age;
        private String constellation;
        private int occupationId;
        private String occupation;
        private int status;

        public int getId() {
            return id;
        }

        public String getImId() {
            return imId;
        }

        public String getNickname() {
            return nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public int getSex() {
            return sex;
        }

        public int getIsVip() {
            return isVip;
        }

        public int getCertification() {
            return certification;
        }

        public int getCityId() {
            return cityId;
        }

        public String getCityName() {
            return cityName;
        }

        public int getAge() {
            return age;
        }

        public String getConstellation() {
            return constellation;
        }

        public int getOccupationId() {
            return occupationId;
        }

        public String getOccupation() {
            return occupation;
        }

        public int getStatus() {
            return status;
        }
    }

}
