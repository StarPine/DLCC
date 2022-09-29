package com.dl.playfun.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BroadcastEntity {

    /**
     * id : 1
     * user_id : 5
     * is_comment : 0
     * give_count : 1
     * created_at : 2020-05-07 15:21:59
     * nickname : 小四
     * certification : null
     * avatar : null
     * sex : 0
     * is_vip : 0
     * topical : {"id":1,"theme_id":1,"address":1,"hope_object":[1,2,3,4,5],"start_date":"2020-05-07","end_time":1,"sign_count":2,"is_give":1,"images":["images/微信图片_20200419083113.png","https://www.apiview.com"],"news":{"id":1,"content":"3","is_give":1,"images":["images/微信图片_20200419083113.png","https://www.apiview.com"]}}
     */

    private int id;
    @SerializedName("user_id")
    private int userId;
    @SerializedName("is_comment")
    private int isComment;
    @SerializedName("give_count")
    private int giveCount;
    @SerializedName("created_at")
    private String createdAt;
    private String nickname;
    private int certification;
    private String avatar;
    private int sex;
    @SerializedName("is_vip")
    private int isVip;
    @SerializedName("game_channel")
    private String gameChannel;
    private TopicalBean topical;
    private NewsBean news;

    @SerializedName("calling_status")
    private int callingStatus;
    @SerializedName("is_online")
    private int isOnline;

    //当前用户im id
    @SerializedName("imId")
    private String ImUserId;
    //对方用户 IM iD
    @SerializedName("toImId")
    private String ImToUserId;

    public String getImUserId() {
        return ImUserId;
    }

    public void setImUserId(String imUserId) {
        ImUserId = imUserId;
    }

    public String getImToUserId() {
        return ImToUserId;
    }

    public void setImToUserId(String imToUserId) {
        ImToUserId = imToUserId;
    }

    public int getCallingStatus() {
        return callingStatus;
    }

    public void setCallingStatus(int callingStatus) {
        this.callingStatus = callingStatus;
    }

    public int getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(int isOnline) {
        this.isOnline = isOnline;
    }

    public String getGameChannel() {
        return gameChannel;
    }

    public void setGameChannel(String gameChannel) {
        this.gameChannel = gameChannel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getIsComment() {
        return isComment;
    }

    public void setIsComment(int isComment) {
        this.isComment = isComment;
    }

    public int getGiveCount() {
        return giveCount;
    }

    public void setGiveCount(int giveCount) {
        this.giveCount = giveCount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getCertification() {
        return certification;
    }

    public void setCertification(int certification) {
        this.certification = certification;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getIsVip() {
        return isVip;
    }

    public void setIsVip(int isVip) {
        this.isVip = isVip;
    }

    public TopicalBean getTopical() {
        return topical;
    }

    public void setTopical(TopicalBean topical) {
        this.topical = topical;
    }

    public NewsBean getNews() {
        return news;
    }

    public void setNews(NewsBean news) {
        this.news = news;
    }

    public static class TopicalBean {
        /**
         * id : 1
         * theme_id : 1
         * address : 1
         * hope_object : [1,2,3,4,5]
         * start_date : 2020-05-07
         * end_time : 1
         * sign_count : 2
         * is_give : 1
         * images : ["images/微信图片_20200419083113.png","https://www.apiview.com"]
         * news : {"id":1,"content":"3","is_give":1,"images":["images/微信图片_20200419083113.png","https://www.apiview.com"]}
         */

        private int id;
        @SerializedName("theme_id")
        private int themeId;
        private String address;
        @SerializedName("start_date")
        private String startDate;
        @SerializedName("end_time")
        private Integer endTime;
        @SerializedName("sign_count")
        private int signCount;
        @SerializedName("is_give")
        private int isGive;
        @SerializedName("hope_object")
        private List<Integer> hopeObject;
        private List<String> images;
        private List<GiveUserBean> giveUserBean;
        private List<CommentEntity> comment;
        @SerializedName("is_sign")
        private int isSign;
        @SerializedName("address_name")
        private String addressName;
        private Double latitude;
        private Double longitude;
        @SerializedName("city_id")
        private Integer cityId;
        @SerializedName("is_end")
        private int isEnd;
        private String describe;

        //视频MP4
        private String video;
        //约会内容
        private String content;
        //评论总数
        @SerializedName("comment_number")
        private Integer commentNumber;

        public Integer getCommentNumber() {
            return commentNumber;
        }

        public void setCommentNumber(Integer commentNumber) {
            this.commentNumber = commentNumber;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }

        public String getAddressName() {
            return addressName;
        }

        public void setAddressName(String addressName) {
            this.addressName = addressName;
        }

        public Double getLatitude() {
            return latitude;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public Double getLongitude() {
            return longitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }

        public Integer getCityId() {
            return cityId;
        }

        public void setCityId(Integer cityId) {
            this.cityId = cityId;
        }

        public int getIsSign() {
            return isSign;
        }

        public void setIsSign(int isSign) {
            this.isSign = isSign;

        }


        public List<CommentEntity> getComment() {
            return comment;
        }

        public void setComment(List<CommentEntity> comment) {
            this.comment = comment;
        }

        public List<GiveUserBean> getGiveUserBean() {
            return giveUserBean;
        }

        public void setGiveUserBean(List<GiveUserBean> giveUserBean) {
            this.giveUserBean = giveUserBean;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getThemeId() {
            return themeId;
        }

        public void setThemeId(int themeId) {
            this.themeId = themeId;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public Integer getEndTime() {
            return endTime;
        }

        public void setEndTime(Integer endTime) {
            this.endTime = endTime;
        }

        public int getSignCount() {
            return signCount;
        }

        public void setSignCount(int signCount) {
            this.signCount = signCount;
        }

        public int getIsGive() {
            return isGive;
        }

        public void setIsGive(int isGive) {
            this.isGive = isGive;
        }

        public List<Integer> getHopeObject() {
            return hopeObject;
        }

        public void setHopeObject(List<Integer> hopeObject) {
            this.hopeObject = hopeObject;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        public int getIsEnd() {
            return isEnd;
        }

        public void setIsEnd(int isEnd) {
            this.isEnd = isEnd;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }
    }

    public static class NewsBean {
        /**
         * id : 1
         * content : 3
         * is_give : 1
         * images : ["images/微信图片_20200419083113.png","https://www.apiview.com"]
         */

        private int id;
        private String content;
        @SerializedName("is_give")
        private int isGive;
        private List<String> images;
        private List<GiveUserBean> give_user;
        //        private GiveUserBean giveUserBean;
        private List<CommentEntity> comment;
        //评论总数
        @SerializedName("comment_number")
        private Integer commentNumber;

        //视频MP4
        private String video;
        //心情
        @SerializedName("news_type")
        private Integer newsType;

        public Integer getCommentNumber() {
            return commentNumber;
        }

        public void setCommentNumber(Integer commentNumber) {
            this.commentNumber = commentNumber;
        }

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }

        public Integer getNewsType() {
            return newsType;
        }

        public void setNewsType(Integer newsType) {
            this.newsType = newsType;
        }

        public List<CommentEntity> getComment() {
            return comment;
        }

        public void setComment(List<CommentEntity> comment) {
            this.comment = comment;
        }

//        public GiveUserBean getGiveUserBean() {
//            return giveUserBean;
//        }
//
//        public void setGiveUserBean(GiveUserBean giveUserBean) {
//            this.giveUserBean = giveUserBean;
//        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getIsGive() {
            return isGive;
        }

        public void setIsGive(int isGive) {
            this.isGive = isGive;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        public List<GiveUserBean> getGive_user() {
            return give_user;
        }

        public void setGive_user(List<GiveUserBean> give_user) {
            this.give_user = give_user;
        }


    }

    public static class GiveUserBean {
        /**
         * id : 5
         * avatar : null
         */

        @SerializedName("id")
        private int idX;
        private String avatar;

        public int getIdX() {
            return idX;
        }

        public void setIdX(int idX) {
            this.idX = idX;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }
}
