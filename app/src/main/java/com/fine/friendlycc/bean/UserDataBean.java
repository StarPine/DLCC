package com.fine.friendlycc.bean;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.data.typeadapter.BooleanTypeAdapter;
import com.fine.friendlycc.utils.StringUtil;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
* @Desc TODO(用户数据信息+token信息合并)
* @author 彭石林
* @parame 
* @return 
* @Date 2022/4/4
*/
public class UserDataBean extends BaseObservable implements Serializable {
    private String token;
    private String userID;
    private String userSig;
    @SerializedName("is_contract")
    private int isContract;

    @SerializedName("is_new_user")
    private Integer isNewUser;
    @SerializedName("is_bind_game")
    private Integer isBindGame;


    /**
     * id : 5
     * nickname : 广州
     * avatar : null
     * birthday : 1991-03-10
     * occupation : 媒体出版
     * program_ids : null
     * hope_object_ids : null
     * permanent_city_ids : 1,2,3,4,5
     * desc : 找真心朋友
     * weight : 60
     * height : 170
     * is_weixin_show : 1
     * weixin : qq123
     */

    private int id;
    private String nickname;
    private String avatar;
    @Bindable
    private String birthday;
    @SerializedName("occupation_id")
    private Integer occupationId;
    @SerializedName("program_ids")
    private List<Integer> programIds;
    @SerializedName("hope_object_ids")
    private List<Integer> hopeObjectIds;
    @SerializedName("permanent_city_ids")
    private List<Integer> permanentCityIds;
    private String desc;
    private Integer weight;
    private Integer height;
    @JsonAdapter(BooleanTypeAdapter.class)
    @SerializedName("is_weixin_show")
    private boolean isWeixinShow;
    private String weixin;
    private String insgram;
    private Integer sex; //1男 0女
    @SerializedName("is_vip")
    private Integer isVip;
    private Integer certification;
    @SerializedName("end_time")
    private String endTime;
    @JsonAdapter(BooleanTypeAdapter.class)
    @SerializedName("is_invite_code")
    private boolean isInviteCode;
    @SerializedName("invite_url")
    private String inviteUrl;
    @SerializedName("city_id")
    private Integer cityId;
    private Integer age;
    private String constellation;


    @SerializedName("p_id")
    private Integer pId;

    @SerializedName("dialog_im_vip_img")
    private String dialogImVipImg;
    //录音
    private String sound;
    @SerializedName("sound_time")
    private Integer soundTime;

    //当前用户im id
    @SerializedName("imId")
    private String ImUserId;

    private String email;
    //设置密码 1是0否
    @SerializedName("is_password")
    private Integer isPassword;

    //是否开放公会主播入口 0否 1是
    @SerializedName("anchor")
    private int anchor;
    //是否开启视频
    private boolean allowVideo;
    //是否开启语音
    private boolean allowAudio;

    public int getAnchor() {
        return anchor;
    }

    public void setAnchor(int anchor) {
        this.anchor = anchor;
    }

    public boolean isAllowVideo() {
        return allowVideo;
    }

    public void setAllowVideo(boolean allowVideo) {
        this.allowVideo = allowVideo;
    }

    public boolean isAllowAudio() {
        return allowAudio;
    }

    public void setAllowAudio(boolean allowAudio) {
        this.allowAudio = allowAudio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getIsPassword() {
        return isPassword;
    }

    public void setIsPassword(Integer isPassword) {
        this.isPassword = isPassword;
    }

    public String getImUserId() {
        return ImUserId;
    }

    public void setImUserId(String imUserId) {
        ImUserId = imUserId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
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

    public String getDialogImVipImg() {
        return dialogImVipImg;
    }

    public void setDialogImVipImg(String dialogImVipImg) {
        this.dialogImVipImg = dialogImVipImg;
    }

    public Integer getpId() {
        return pId;
    }

    public void setpId(Integer pId) {
        this.pId = pId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Bindable
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
        notifyPropertyChanged(BR.avatar);
    }

    @Bindable
    public String getBirthday() {
        return birthday;
    }

    @Bindable
    public Integer getOccupationId() {
        return occupationId;
    }

    public void setOccupationId(Integer occupationId) {
        this.occupationId = occupationId;
        notifyPropertyChanged(BR.occupationId);
    }

    @Bindable
    public List<Integer> getProgramIds() {
        return programIds;
    }

    public void setProgramIds(List<Integer> programIds) {
        this.programIds = programIds;
        notifyPropertyChanged(BR.programIds);
    }

    @Bindable
    public List<Integer> getHopeObjectIds() {
        return hopeObjectIds;
    }

    public void setHopeObjectIds(List<Integer> hopeObjectIds) {
        this.hopeObjectIds = hopeObjectIds;
        notifyPropertyChanged(BR.hopeObjectIds);
    }

    @Bindable
    public List<Integer> getPermanentCityIds() {
        return permanentCityIds;
    }

    public void setPermanentCityIds(List<Integer> permanentCityIds) {
        this.permanentCityIds = permanentCityIds;
        notifyPropertyChanged(BR.permanentCityIds);
    }

    @Bindable
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
        notifyPropertyChanged(BR.desc);
    }

    @Bindable
    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
        notifyPropertyChanged(BR.weight);
    }

    @Bindable
    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
        notifyPropertyChanged(BR.height);
    }

    public boolean isWeixinShow() {
        return isWeixinShow;
    }

    public void setWeixinShow(boolean weixinShow) {
        isWeixinShow = weixinShow;
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

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
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

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isInviteCode() {
        return isInviteCode;
    }

    public void setInviteCode(boolean inviteCode) {
        isInviteCode = inviteCode;
    }

    public String getInviteUrl() {
        return inviteUrl;
    }

    public void setInviteUrl(String inviteUrl) {
        this.inviteUrl = inviteUrl;
    }


    public Integer getIsNewUser() {
        return isNewUser;
    }

    public void setIsNewUser(Integer isNewUser) {
        this.isNewUser = isNewUser;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserSig() {
        return userSig;
    }

    public void setUserSig(String userSig) {
        this.userSig = userSig;
    }

    public int getIsContract() {
        return isContract;
    }

    public void setIsContract(int isContract) {
        this.isContract = isContract;
    }

    public Integer getIsBindGame() {
        return isBindGame;
    }

    public void setIsBindGame(Integer isBindGame) {
        this.isBindGame = isBindGame;
    }

    public boolean isPerfect() {
        if (StringUtil.isEmpty(avatar)) {
            return false;
        }
        if (StringUtils.isEmpty(nickname)) {
            return false;
        }
        if (ObjectUtils.isEmpty(birthday)) {
            return false;
        }
        if (ObjectUtils.isEmpty(occupationId) || occupationId.intValue() == 0) {
            return false;
        }
        return true;
    }
}