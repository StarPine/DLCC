package com.dl.playfun.entity;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.dl.playfun.BR;
import com.dl.playfun.data.typeadapter.BooleanTypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author wulei
 */
public class UserDetailEntity extends BaseObservable {

    /**
     * nickname : 香港
     * is_collect : 0
     * remark_nickname : 小四
     * avatar : null
     * more_number : 10
     * city : 香港
     * age : 23
     * constellation : 金牛座
     * distance : 139.54km
     * is_vip : 0
     * certification : 0
     * program_ids : null
     * hope_object_ids : null
     * permanent_city_ids : 1,2,3,4,5
     * desc :
     * weight :
     * height :
     * is_weixin_show :
     * weixin :
     * news : {}
     * album : [{"id":1,"src":"http://ww.baidu.com","type":1,"is_burn":4,"is_red_package":1,"money":"1.00","created_at":"2020-05-07 14:45:04","burn_status":1}]
     * album_type : 2
     * album_pay_money : 10.00
     */

    private String nickname;
    @JsonAdapter(BooleanTypeAdapter.class)
    @SerializedName("is_collect")
    private Boolean isCollect;
    @SerializedName("remark_nickname")
    private String remarkNickname;
    private String avatar;
    @SerializedName("more_number")
    private Integer moreNumber;
    private String city;
    private Integer age;
    private String constellation;
    private Double distance;
    @SerializedName("is_online")
    private Integer isOnline;
    @SerializedName("calling_status")
    private int callingStatus;
    @SerializedName("is_accost")
    private int isAccost;

    /**
     * 男性则代表是否是vip，女性则代表是否为女神
     */
    @SerializedName("is_vip")
    private Integer isVip;
    /**
     * 真实身份验证，不区分男女
     */
    private Integer certification;
    @SerializedName("program_ids")
    private List<Integer> programIds;
    @SerializedName("hope_object_ids")
    private List<Integer> hopeObjectIds;
    @SerializedName("permanent_city_ids")
    private List<Integer> permanentCityIds;
    private String desc;
    private Integer weight;
    private Integer height;
    @SerializedName("is_weixin_show")
    private Integer isWeixinShow;
    @SerializedName("is_unlock_account")
    private Integer isUnlockAccount;
    @SerializedName("unlock_account_money")
    private Integer unlockAccountMoney;
    private String weixin;
    private String insgram;
    @SerializedName("topical_id")
    private Integer topicalId;
    private List<String> news;
    @SerializedName("album_type")
    private Integer albumType;
    @SerializedName("album_img_total")
    private Integer albumImgTotal;
    /**
     * 0相安无事，1拉黑对方 ，2被对方拉黑，3 双拉黑
     */
    @SerializedName("blacklist_status")
    private Integer blacklistStatus;


    /**
     * -1未审核  0等待 1通过  2拒绝
     */
    @SerializedName("album_apply")
    private Integer albumApply;
    @SerializedName("album_pay_money")
    private Integer albumPayMoney;
    private List<AlbumPhotoEntity> album;
    @SerializedName("occupation_id")
    private Integer occupationId;
    @SerializedName("is_blacklist")
    private Integer isBlacklist;
    /**
     * 0-女性
     * 1-男性
     */
    private Integer sex;
    @SerializedName("online_updated_at")
    private String offlineTime;
    @SerializedName("is_chat")
    @JsonAdapter(BooleanTypeAdapter.class)
    private Boolean isChat;
    @SerializedName("is_browse")
    @JsonAdapter(BooleanTypeAdapter.class)
    private boolean isBrowse;

    @SerializedName("use_type")
    private Integer useType;//新增判断是否是机器人
    /**
     * 是否允许连麦
     */
    @JsonAdapter(BooleanTypeAdapter.class)
    @SerializedName("is_connection")
    private boolean isConnectionMic;
    @SerializedName("is_view")
    private Integer isView;

    //录音
    private String sound;
    @SerializedName("sound_time")
    private Integer soundTime;

    //录音审核进度
    @SerializedName("sound_status")
    private Integer soundStatus;

    //动态数量
    @SerializedName("news_number")
    private Integer newsNumber;

    //当前用户im id
    @SerializedName("imId")
    private String ImUserId;
    //对方用户 IM iD
    @SerializedName("toImId")
    private String ImToUserId;

    @Bindable
    public Integer getBlacklistStatus() {
        return blacklistStatus;
    }

    public void setBlacklistStatus(Integer blacklistStatus) {
        this.blacklistStatus = blacklistStatus;
        notifyPropertyChanged(BR.blacklistStatus);
    }

    public int getIsAccost() {
        return isAccost;
    }

    public void setIsAccost(int isAccost) {
        this.isAccost = isAccost;
    }

    public Integer getUnlockAccountMoney() {
        return unlockAccountMoney;
    }

    public void setUnlockAccountMoney(Integer unlockAccountMoney) {
        this.unlockAccountMoney = unlockAccountMoney;
    }

    public int getCallingStatus() {
        return callingStatus;
    }

    public void setCallingStatus(int callingStatus) {
        this.callingStatus = callingStatus;
    }

    public void setSoundStatus(Integer soundStatus) {
        this.soundStatus = soundStatus;
    }

    public Integer getNewsNumber() {
        return newsNumber;
    }

    public void setNewsNumber(Integer newsNumber) {
        this.newsNumber = newsNumber;
    }

    public Integer getSoundStatus() {
        return soundStatus;
    }

    public Integer getSoundTime() {
        return soundTime;
    }

    public void setSoundTime(Integer soundTime) {
        this.soundTime = soundTime;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public Integer getUseType() {
        return useType;
    }

    public void setUseType(Integer useType) {
        this.useType = useType;
    }

    public Integer getIsView() {
        return isView;
    }

    public void setIsView(Integer isView) {
        this.isView = isView;
    }

    @Bindable
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
        notifyPropertyChanged(BR.nickname);
    }

    @Bindable
    public Boolean getCollect() {
        return isCollect;
    }

    public void setCollect(Boolean collect) {
        isCollect = collect;
        notifyPropertyChanged(BR.collect);
    }

    @Bindable
    public String getRemarkNickname() {
        return remarkNickname;
    }

    public void setRemarkNickname(String remarkNickname) {
        this.remarkNickname = remarkNickname;
        notifyPropertyChanged(BR.remarkNickname);
    }

    @Bindable
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
        notifyPropertyChanged(BR.avatar);
    }

    public Integer getMoreNumber() {
        return moreNumber;
    }

    public void setMoreNumber(Integer moreNumber) {
        this.moreNumber = moreNumber;
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

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Integer getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Integer isOnline) {
        this.isOnline = isOnline;
    }

    public Integer getIsVip() {
        return isVip;
    }

    public void setIsVip(Integer isVip) {
        this.isVip = isVip;
    }

    public Integer getCertification() {
        return certification;
    }

    public void setCertification(Integer certification) {
        this.certification = certification;
    }

    public List<Integer> getProgramIds() {
        return programIds;
    }

    public void setProgramIds(List<Integer> programIds) {
        this.programIds = programIds;
    }

    public List<Integer> getHopeObjectIds() {
        return hopeObjectIds;
    }

    public void setHopeObjectIds(List<Integer> hopeObjectIds) {
        this.hopeObjectIds = hopeObjectIds;
    }

    public List<Integer> getPermanentCityIds() {
        return permanentCityIds;
    }

    public void setPermanentCityIds(List<Integer> permanentCityIds) {
        this.permanentCityIds = permanentCityIds;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getIsWeixinShow() {
        return isWeixinShow;
    }

    public void setIsWeixinShow(Integer isWeixinShow) {
        this.isWeixinShow = isWeixinShow;
    }

    @Bindable
    public Integer getIsUnlockAccount() {
        return isUnlockAccount;
    }

    public void setIsUnlockAccount(Integer isUnlockAccount) {
        this.isUnlockAccount = isUnlockAccount;
        notifyPropertyChanged(BR.isUnlockAccount);
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public String getInsgram() {
        return insgram;
    }

    public void setInsgram(String insgram) {
        this.insgram = insgram;
    }

    public Integer getTopicalId() {
        return topicalId;
    }

    public void setTopicalId(Integer topicalId) {
        this.topicalId = topicalId;
    }

    public List<String> getNews() {
        return news;
    }

    public void setNews(List<String> news) {
        this.news = news;
    }

    @Bindable
    public Integer getAlbumType() {
        return albumType;
    }

    public void setAlbumType(Integer albumType) {
        this.albumType = albumType;
        notifyPropertyChanged(BR.albumType);
    }

    public Integer getAlbumImgTotal() {
        return albumImgTotal;
    }

    public void setAlbumImgTotal(Integer albumImgTotal) {
        this.albumImgTotal = albumImgTotal;
    }

    @Bindable
    public Integer getAlbumApply() {
        return albumApply;
    }

    public void setAlbumApply(Integer albumApply) {
        this.albumApply = albumApply;
        notifyPropertyChanged(BR.albumApply);
    }

    public Integer getAlbumPayMoney() {
        return albumPayMoney;
    }

    public void setAlbumPayMoney(Integer albumPayMoney) {
        this.albumPayMoney = albumPayMoney;
    }

    public List<AlbumPhotoEntity> getAlbum() {
        return album;
    }

    public void setAlbum(List<AlbumPhotoEntity> album) {
        this.album = album;
    }

    public Integer getOccupationId() {
        return occupationId;
    }

    public void setOccupationId(Integer occupationId) {
        this.occupationId = occupationId;
    }

    public Integer getIsBlacklist() {
        return isBlacklist;
    }

    public void setIsBlacklist(Integer isBlacklist) {
        this.isBlacklist = isBlacklist;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public boolean isMale() {
        return sex == 1;
    }

    public String getOfflineTime() {
        return offlineTime;
    }

    public void setOfflineTime(String offlineTime) {
        this.offlineTime = offlineTime;
    }

    public boolean isConnectionMic() {
        return isConnectionMic;
    }

    public void setConnectionMic(boolean connectionMic) {
        isConnectionMic = connectionMic;
    }

    public boolean isBrowse() {
        return isBrowse;
    }

    public void setBrowse(boolean browse) {
        isBrowse = browse;
    }

    @Bindable
    public Boolean getChat() {
        return isChat;
    }

    public void setChat(Boolean chat) {
        isChat = chat;
        notifyPropertyChanged(BR.chat);
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

    @Override
    public String toString() {
        return "UserDetailEntity{" +
                "nickname='" + nickname + '\'' +
                ", isCollect=" + isCollect +
                ", remarkNickname='" + remarkNickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", moreNumber=" + moreNumber +
                ", city='" + city + '\'' +
                ", age=" + age +
                ", constellation='" + constellation + '\'' +
                ", distance=" + distance +
                ", isOnline=" + isOnline +
                ", callingStatus=" + callingStatus +
                ", isVip=" + isVip +
                ", certification=" + certification +
                ", programIds=" + programIds +
                ", hopeObjectIds=" + hopeObjectIds +
                ", permanentCityIds=" + permanentCityIds +
                ", desc='" + desc + '\'' +
                ", weight=" + weight +
                ", height=" + height +
                ", isWeixinShow=" + isWeixinShow +
                ", isUnlockAccount=" + isUnlockAccount +
                ", unlockAccountMoney=" + unlockAccountMoney +
                ", weixin='" + weixin + '\'' +
                ", insgram='" + insgram + '\'' +
                ", topicalId=" + topicalId +
                ", news=" + news +
                ", albumType=" + albumType +
                ", albumImgTotal=" + albumImgTotal +
                ", blacklistStatus=" + blacklistStatus +
                ", albumApply=" + albumApply +
                ", albumPayMoney=" + albumPayMoney +
                ", album=" + album +
                ", occupationId=" + occupationId +
                ", isBlacklist=" + isBlacklist +
                ", sex=" + sex +
                ", offlineTime='" + offlineTime + '\'' +
                ", isChat=" + isChat +
                ", isBrowse=" + isBrowse +
                ", useType=" + useType +
                ", isConnectionMic=" + isConnectionMic +
                ", isView=" + isView +
                ", sound='" + sound + '\'' +
                ", soundTime=" + soundTime +
                ", soundStatus=" + soundStatus +
                ", newsNumber=" + newsNumber +
                ", ImUserId='" + ImUserId + '\'' +
                ", ImToUserId='" + ImToUserId + '\'' +
                '}';
    }
}
