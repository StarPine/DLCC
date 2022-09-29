package com.dl.playfun.entity;

import com.google.gson.annotations.SerializedName;

/**
 * 报名消息
 */
public class SignMessageEntity {

    private int id;
    @SerializedName("topical_id")
    private int topicalId;
    @SerializedName("created_at")
    private String createdAt;
    private String content;
    private ItemUserEntity user;
    private TopicalBean topical;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTopicalId() {
        return topicalId;
    }

    public void setTopicalId(int topicalId) {
        this.topicalId = topicalId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ItemUserEntity getUser() {
        return user;
    }

    public void setUser(ItemUserEntity user) {
        this.user = user;
    }

    public TopicalBean getTopical() {
        return topical;
    }

    public void setTopical(TopicalBean topical) {
        this.topical = topical;
    }

    public static class TopicalBean {
        /**
         * id : 1
         * start_date : 2020-05-07
         * end_time : 1
         * address : 上海
         */

        private int id;
        @SerializedName("start_date")
        private String startDate;
        @SerializedName("end_time")
        private int endTime;
        private String address;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public int getEndTime() {
            return endTime;
        }

        public void setEndTime(int endTime) {
            this.endTime = endTime;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}
