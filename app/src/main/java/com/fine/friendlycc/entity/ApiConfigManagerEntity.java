package com.fine.friendlycc.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Author: 彭石林
 * Time: 2022/7/2 12:12
 * Description: API配置类
 */
public class ApiConfigManagerEntity implements Parcelable {
    //playchat web网址
    @SerializedName("playChatH5")
    private String playChatWebUrl;
    //playcc web网址
    @SerializedName("playFunH5")
    private String playFunWebUrl;
    //福袋网址
    @SerializedName("goodBagUrl")
    private String goodBagWebUrl;
    //playcc api
    @SerializedName("playFunApi")
    private String playFunApiUrl;
    //playchat api
    @SerializedName("playChatApi")
    private String playChatApiUrl;
    //IM appID
    private Integer imAppId;
    //离线推送ID
    private Integer offlinePushId;
    //聊天客服ID
    private String customerId;

    protected ApiConfigManagerEntity(Parcel in) {
        playChatWebUrl = in.readString();
        playFunWebUrl = in.readString();
        goodBagWebUrl = in.readString();
        playFunApiUrl = in.readString();
        playChatApiUrl = in.readString();
        if (in.readByte() == 0) {
            imAppId = null;
        } else {
            imAppId = in.readInt();
        }
        if (in.readByte() == 0) {
            offlinePushId = null;
        } else {
            offlinePushId = in.readInt();
        }
        customerId = in.readString();
    }

    public static final Creator<ApiConfigManagerEntity> CREATOR = new Creator<ApiConfigManagerEntity>() {
        @Override
        public ApiConfigManagerEntity createFromParcel(Parcel in) {
            return new ApiConfigManagerEntity(in);
        }

        @Override
        public ApiConfigManagerEntity[] newArray(int size) {
            return new ApiConfigManagerEntity[size];
        }
    };

    public String getPlayChatWebUrl() {
        return playChatWebUrl;
    }

    public void setPlayChatWebUrl(String playChatWebUrl) {
        this.playChatWebUrl = playChatWebUrl;
    }

    public String getPlayFunWebUrl() {
        return playFunWebUrl;
    }

    public void setPlayFunWebUrl(String playFunWebUrl) {
        this.playFunWebUrl = playFunWebUrl;
    }

    public String getGoodBagWebUrl() {
        return goodBagWebUrl;
    }

    public void setGoodBagWebUrl(String goodBagWebUrl) {
        this.goodBagWebUrl = goodBagWebUrl;
    }

    public String getPlayFunApiUrl() {
        return playFunApiUrl;
    }

    public void setPlayFunApiUrl(String playFunApiUrl) {
        this.playFunApiUrl = playFunApiUrl;
    }

    public String getPlayChatApiUrl() {
        return playChatApiUrl;
    }

    public void setPlayChatApiUrl(String playChatApiUrl) {
        this.playChatApiUrl = playChatApiUrl;
    }

    public Integer getImAppId() {
        return imAppId;
    }

    public void setImAppId(Integer imAppId) {
        this.imAppId = imAppId;
    }

    public Integer getOfflinePushId() {
        return offlinePushId;
    }

    public void setOfflinePushId(Integer offlinePushId) {
        this.offlinePushId = offlinePushId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(playChatWebUrl);
        dest.writeString(playFunWebUrl);
        dest.writeString(goodBagWebUrl);
        dest.writeString(playFunApiUrl);
        dest.writeString(playChatApiUrl);
        if (imAppId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(imAppId);
        }
        if (offlinePushId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(offlinePushId);
        }
        dest.writeString(customerId);
    }

    @Override
    public String toString() {
        return "ApiConfigManagerEntity{" +
                "playChatWebUrl='" + playChatWebUrl + '\'' +
                ", playFunWebUrl='" + playFunWebUrl + '\'' +
                ", goodBagWebUrl='" + goodBagWebUrl + '\'' +
                ", playFunApiUrl='" + playFunApiUrl + '\'' +
                ", playChatApiUrl='" + playChatApiUrl + '\'' +
                ", imAppId=" + imAppId +
                ", offlinePushId=" + offlinePushId +
                ", customerId='" + customerId + '\'' +
                '}';
    }
}
