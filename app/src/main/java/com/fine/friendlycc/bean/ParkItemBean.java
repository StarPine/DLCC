package com.fine.friendlycc.bean;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.fine.friendlycc.BR;
import com.fine.friendlycc.data.typeadapter.BooleanTypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author wulei
 */
public class ParkItemBean extends BaseObservable {
    /**
     * id : 8
     * nickname : 佛山
     * sex : 0
     * avatar : null
     * city : 佛山
     * age : 26
     * constellation : 处女座
     * occupation : 保险
     * is_online : 1
     * is_vip : 0
     * img_number : 0
     * distance : 24.1km
     * is_collect : 0
     * album_type : 1
     */

    private int id;
    private String nickname;
    private Integer sex;
    private String avatar;
    private String city;
    private Integer age;
    private int certification;
    private String constellation;
    @SerializedName("occupation_id")
    private Integer occupationId;
    @SerializedName("is_online")
    private int isOnline;
    @SerializedName("is_vip")
    private int isVip;
    @SerializedName("img_number")
    private int imgNumber;
    private Double distance;
    @JsonAdapter(BooleanTypeAdapter.class)
    @SerializedName("is_collect")
    private Boolean isCollect;
    @SerializedName("album_type")
    private int albumType;
    @SerializedName("album_apply")
    private int albumApply;
    @SerializedName("online_updated_at")
    private String offlineTime;
    @SerializedName("game_channel")
    private String gameChannel;

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

    //是否搭讪过
    @SerializedName("is_accost")
    private Integer isAccost;

    //当前用户im id
    @SerializedName("imId")
    private String ImUserId;
    //对方用户 IM iD
    @SerializedName("toImId")
    private String ImToUserId;
    //在线状态
    @SerializedName("calling_status")
    private int callingStatus;

    //新版首页为做 item跟广告banner区分
    private Integer type;
    private List<AdItemBean> bannerList;


    public int getCallingStatus() {
        return callingStatus;
    }

    public void setCallingStatus(int callingStatus) {
        this.callingStatus = callingStatus;
    }

    public List<AdItemBean> getBannerList() {
        return bannerList;
    }

    public void setBannerList(List<AdItemBean> bannerList) {
        this.bannerList = bannerList;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

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

    public Integer getIsAccost() {
        if (isAccost == null) {
            return 0;
        }
        return isAccost;
    }

    public void setIsAccost(Integer isAccost) {
        this.isAccost = isAccost;
    }

    @Bindable
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
        notifyPropertyChanged(BR.nickname);
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public int getCertification() {
        return certification;
    }

    public void setCertification(int certification) {
        this.certification = certification;
    }

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public Integer getOccupationId() {
        return occupationId;
    }

    public void setOccupationId(Integer occupationId) {
        this.occupationId = occupationId;
    }

    public int getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(int isOnline) {
        this.isOnline = isOnline;
    }

    public int getIsVip() {
        return isVip;
    }

    public void setIsVip(int isVip) {
        this.isVip = isVip;
    }

    public int getImgNumber() {
        return imgNumber;
    }

    public void setImgNumber(int imgNumber) {
        this.imgNumber = imgNumber;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    @Bindable
    public Boolean getCollect() {
        return isCollect;
    }

    public void setCollect(Boolean collect) {
        isCollect = collect;
        notifyPropertyChanged(BR.collect);
    }

    public int getAlbumType() {
        return albumType;
    }

    public void setAlbumType(int albumType) {
        this.albumType = albumType;
    }

    public int getAlbumApply() {
        return albumApply;
    }

    public void setAlbumApply(int albumApply) {
        this.albumApply = albumApply;
    }

    public String getOfflineTime() {
        return offlineTime;
    }

    public void setOfflineTime(String offlineTime) {
        this.offlineTime = offlineTime;
    }
}