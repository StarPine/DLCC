package com.fine.friendlycc.entity;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.fine.friendlycc.BR;
import com.fine.friendlycc.utils.ListUtils;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TopicalListEntity extends BaseObservable {

    /**
     * id : 1
     * theme_id : 1
     * address : 上海
     * hope_object : 1,2,3,4,5
     * start_date : 2020-05-07
     * end_time : 全天
     * created_at : 2020-05-07 15:21:59
     * images : null
     * user_id : 5
     * user : {"id":5,"nickname":"广州","sex":0,"is_vip":0,"certification":0}
     * broadcast : {"id":1,"is_comment":0,"give_count":0,"broadcastable_id":1}
     */

    private int id;
    @SerializedName("theme_id")
    private int themeId;
    private String address;
    @SerializedName("hope_object")
    private List<Integer> hopeObject;
    @SerializedName("start_date")
    private String startDate;
    @SerializedName("end_time")
    private Integer endTime;
    @SerializedName("created_at")
    private String createdAt;
    private List<String> images;
    @SerializedName("user_id")
    private int userId;
    private BaseUserBeanEntity user;
    private BroadcastBeanEntity broadcast;
    @SerializedName("is_give")
    private int isGive;
    @SerializedName("give_count")
    private int giveCount;
    private List<GiveUserBeanEntity> give_user;
    private List<CommentEntity> comment;
    @SerializedName("is_end")
    private int isEnd;
    private List<SignsBeanEntity> signs;
    @SerializedName("sign_count")
    private int signCount;
    @SerializedName("is_sign")
    private int isSign;
    @SerializedName("address_name")
    private String addressName;
    private Double latitude;
    private Double longitude;
    @SerializedName("city_id")
    private Integer cityId;
    private Integer giveSize;
    private String describe;

    private Integer pId;//上级ID-为了实现显示邀请我的用户指定并添加标签

    //视频MP4
    private String video;
    //评论总数
    @SerializedName("comment_number")
    private Integer commentNumber;

    //内容
    private String content;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

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

    public Integer getpId() {
        return pId;
    }

    public void setpId(Integer pId) {
        this.pId = pId;
    }

    @Bindable
    public Integer getGiveSize() {
        if (giveSize == null) {
            if (ListUtils.isEmpty(give_user)) {
                return 0;
            } else {
                return give_user.size();
            }
        } else {
            return giveSize;
        }
    }

    public void setGiveSize(Integer giveSize) {
        this.giveSize = giveSize;
        notifyPropertyChanged(BR.giveSize);
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

    @Bindable
    public int getIsSign() {
        return isSign;
    }

    public void setIsSign(int isSign) {
        this.isSign = isSign;
        notifyPropertyChanged(BR.isSign);
    }

    @Bindable
    public int getSignCount() {
        return signCount;
    }

    public void setSignCount(int signCount) {
        this.signCount = signCount;
        notifyPropertyChanged(BR.signCount);
    }

    public List<SignsBeanEntity> getSigns() {
        return signs;
    }

    public void setSigns(List<SignsBeanEntity> signs) {
        this.signs = signs;
    }

    @Bindable
    public int getIsEnd() {
        return isEnd;
    }

    public void setIsEnd(int isEnd) {
        this.isEnd = isEnd;
        notifyPropertyChanged(BR.isEnd);
    }

    @Bindable
    public int getIsGive() {
        return isGive;
    }

    public void setIsGive(int isGive) {
        this.isGive = isGive;
        notifyPropertyChanged(BR.isGive);
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
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

    public int getGiveCount() {
        return giveCount;
    }

    public void setGiveCount(int giveCount) {
        this.giveCount = giveCount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Integer> getHopeObject() {
        return hopeObject;
    }

    public void setHopeObject(List<Integer> hopeObject) {
        this.hopeObject = hopeObject;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BaseUserBeanEntity getUser() {
        return user;
    }

    public void setUser(BaseUserBeanEntity user) {
        this.user = user;
    }

    public BroadcastBeanEntity getBroadcast() {
        return broadcast;
    }

    public void setBroadcast(BroadcastBeanEntity broadcast) {
        this.broadcast = broadcast;
    }

    @Bindable
    public List<CommentEntity> getComment() {
        return comment;
    }

    public void setComment(List<CommentEntity> comment) {
        this.comment = comment;
        notifyPropertyChanged(BR.comment);
    }


    @Bindable
    public List<GiveUserBeanEntity> getGive_user() {
        return give_user;
    }

    public void setGive_user(List<GiveUserBeanEntity> give_user) {
        this.give_user = give_user;
        notifyPropertyChanged(BR.give_user);
    }

}
